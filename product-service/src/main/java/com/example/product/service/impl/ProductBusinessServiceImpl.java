package com.example.product.service.impl;

import com.example.product.config.ErrorCodeEnum;
import com.example.product.dto.request.business.BuyingEachProductRequest;
import com.example.product.dto.request.business.BuyingProductRequest;
import com.example.product.dto.request.business.FundRequestData;
import com.example.product.dto.request.business.SellingProductRequest;
import com.example.product.entity.BusinessProduct;
import com.example.product.entity.FundType;
import com.example.product.entity.Product;
import com.example.product.entity.ProductStatus;
import com.example.product.entity.TradeHistory;
import com.example.product.repo.ProductBusinessRepository;
import com.example.product.repo.ProductRepository;
import com.example.product.repo.TradeHistoryRepository;
import com.example.product.service.iface.ProductBusinessService;
import com.example.product.service.iface.kafka.ProducerService;
import com.example.product.service.iface.ms.BusinessService;
import com.example.product.utils.JsonUtils;
import com.example.product.utils.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductBusinessServiceImpl implements ProductBusinessService {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProductBusinessServiceImpl.class);
  private final ProductRepository productRepository;
  private final ProductBusinessRepository productBusinessRepository;
  private final BusinessService businessService;
  private final ProducerService producerService;
  private final TradeHistoryRepository tradeHistoryRepository;

  public ProductBusinessServiceImpl(ProductRepository productRepository,
      ProductBusinessRepository productBusinessRepository, BusinessService businessService,
      ProducerService producerService, TradeHistoryRepository tradeHistoryRepository) {
    this.productRepository = productRepository;
    this.productBusinessRepository = productBusinessRepository;
    this.businessService = businessService;
    this.producerService = producerService;
    this.tradeHistoryRepository = tradeHistoryRepository;
  }

  @Value("${kafka.topic.fund}")
  private String fundTopic;

  @Override
  @Transactional
  public boolean buy(BuyingProductRequest request) throws ResourceNotFoundException {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
    String timeAdd = simpleDateFormat.format(date);
    checkProductAvailable(request.getProducts(), FundType.BUYING.name());
    long billValue = getBillValue(request.getProducts());
    buyProduct(request, timeAdd, date);
    fund(billValue, request.getSource(), FundType.BUYING.name(), timeAdd);
    return true;
  }

  private void buyProduct(BuyingProductRequest request, String timeAdd, Date date) {
    Set<Integer> productIds = request.getProducts().keySet();
    Map<Integer, Integer> products = request.getProducts();
    List<BusinessProduct> saveRequest = new ArrayList<BusinessProduct>();
    List<TradeHistory> tradeSaveRequest = new ArrayList<TradeHistory>();
    for (int productId : productIds) {
      BusinessProduct businessProduct = new BusinessProduct();
      businessProduct.setProductId(productId);
      businessProduct.setAvailable(products.get(productId));
      businessProduct.setSource(request.getSource());
      businessProduct.setInDate(date.getTime());
      businessProduct.setBatch(timeAdd);
      saveRequest.add(businessProduct);
      TradeHistory tradeHistory = new TradeHistory();
      tradeHistory.setProductId(productId);
      tradeHistory.setType(FundType.BUYING.name());
      tradeHistory.setAmount(products.get(productId));
      tradeHistory.setBatch(timeAdd);
      tradeSaveRequest.add(tradeHistory);
    }
    productBusinessRepository.saveAll(saveRequest);
    tradeHistoryRepository.saveAll(tradeSaveRequest);
  }

  //check product available
  private void checkProductAvailable(Map<Integer, Integer> products, String type)
      throws ResourceNotFoundException {
    Set<Integer> productIds = products.keySet();
    for (int productId : productIds) {
      Optional<Product> productOptional = productRepository.findById(productId);
      if (productOptional.isEmpty()) {
        throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_FOUND);
      }
      Product product = productOptional.get();
      if (product.getStatus().equals(ProductStatus.DEACTIVATE.name())) {
        throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_SUPPORT);
      }
      if (FundType.SELLING.name().equals(type)) {
        checkSellableProduct(products);
      }
    }
  }

  private void checkSellableProduct(Map<Integer, Integer> products)
      throws ResourceNotFoundException {
    Set<Integer> productIds = products.keySet();
    for (int productId : productIds) {
      List<BusinessProduct> availableProducts = productBusinessRepository.findAllByProductId(
          productId);
      if (availableProducts == null || availableProducts.isEmpty()) {
        throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_ENOUGH);
      }
      int avai = 0;
      for (BusinessProduct availableProduct : availableProducts) {
        avai += availableProduct.getAvailable();
      }
      if (avai <= products.get(productId)) {
        throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_ENOUGH);
      }
    }
  }

  private long getBillValue(Map<Integer, Integer> products) throws ResourceNotFoundException {
    long totalBill = 0L;
    for (Integer productId : products.keySet()) {
      Optional<Product> productOptional = productRepository.findById(productId);
      Product product = productOptional.get();
      totalBill += product.getBuyPrice() * products.get(productId);
    }
    long balance = this.getBalance();
    if (totalBill >= balance) {
      throw new ResourceNotFoundException(ErrorCodeEnum.BALANCE_NOT_ENOUGH);
    }
    return totalBill;
  }

  private long getBalance() throws ResourceNotFoundException {
    return businessService.checkBalance();
  }

  @Override
  @Transactional
  public boolean sell(SellingProductRequest request) throws ResourceNotFoundException {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
    String timeAdd = simpleDateFormat.format(date);
    checkProductAvailable(request.getProducts(), FundType.SELLING.name());
    sellProduct(request.getProducts(), timeAdd);
    long billValue = getBillValue(request.getProducts());
    fund(billValue, request.getCustomerPhone(), FundType.SELLING.name(), timeAdd);
    return false;
  }

  @Override
  public boolean revert(String batch) throws ResourceNotFoundException {
    List<TradeHistory> tradeHistories = tradeHistoryRepository.findAllByBatch(batch);
    if (tradeHistories == null || tradeHistories.size() == 0) {
      throw new ResourceNotFoundException(ErrorCodeEnum.BUSINESS_ERROR);
    }
    for (TradeHistory tradeHistory : tradeHistories) {
      if (tradeHistory.getType().equals(FundType.BUYING.name())) {
        logger.info("revert(), delete all by batch {}, the number of record: {}", batch,
            tradeHistories.size());
        return productBusinessRepository.deleteAllByBatch(batch);
      }
      List<BusinessProduct> businessProducts = productBusinessRepository
          .findAllByProductIdOrderByInDateAsc(tradeHistory.getProductId());
      if (businessProducts != null && businessProducts.size() > 0) {
        for (BusinessProduct businessProduct : businessProducts) {
          if (businessProduct.getAvailable() >0) {
            businessProduct.setAvailable(businessProduct.getAvailable() + tradeHistory.getAmount());
            break;
          }
        }
        productBusinessRepository.saveAll(businessProducts);
      }
    }
    return true;
  }

  private void sellProduct(Map<Integer, Integer> products, String batch) {
    Set<Integer> productIds = products.keySet();
    List<BusinessProduct> saveRequest = new ArrayList<>();
    List<TradeHistory> tradeSaveRequest = new ArrayList<>();
    for (Integer productId : productIds) {
      List<BusinessProduct> availableProducts = productBusinessRepository
          .findAllByProductIdOrderByInDateAsc(productId);
      int amount = products.get(productId);
      for (BusinessProduct availableProduct : availableProducts) {
        while (amount > 0) {
          int subtract = Math.max((availableProduct.getAvailable() - amount), 0);
          amount = Math.max((amount - availableProduct.getAvailable()), 0);
          availableProduct.setAvailable(subtract);
          saveRequest.add(availableProduct);
        }
      }
      TradeHistory tradeHistory = new TradeHistory();
      tradeHistory.setProductId(productId);
      tradeHistory.setAmount(products.get(productId));
      tradeHistory.setType(FundType.SELLING.name());
      tradeHistory.setBatch(batch);
      tradeSaveRequest.add(tradeHistory);
    }
    productBusinessRepository.saveAll(saveRequest);
    tradeHistoryRepository.saveAll(tradeSaveRequest);
  }

  private void fund(long amount, String customer, String type, String batch) {
    FundRequestData fundData = new FundRequestData();
    fundData.setCustomer(customer);
    fundData.setType(type);
    fundData.setBatch(batch);
    fundData.setAmount(amount);
    fundData.setUuid(UUID.randomUUID().toString());
    producerService.sendMessage(JsonUtils.toJson(fundData), fundTopic);
  }


}
