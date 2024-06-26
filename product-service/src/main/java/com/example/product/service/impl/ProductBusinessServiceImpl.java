package com.example.product.service.impl;

import com.example.product.config.ErrorCodeEnum;
import com.example.product.dto.request.business.BuyingProductRequest;
import com.example.product.dto.request.business.FundRequestData;
import com.example.product.dto.request.business.RequestData;
import com.example.product.dto.request.business.SellingOnlineRequest;
import com.example.product.dto.request.business.SellingOnlineRequest.Address;
import com.example.product.dto.request.business.SellingProductRequest;
import com.example.product.entity.BusinessProduct;
import com.example.product.entity.FundType;
import com.example.product.entity.Product;
import com.example.product.entity.ProductStatus;
import com.example.product.entity.SellAddressInfo;
import com.example.product.entity.TradeHistory;
import com.example.product.repo.ProductBusinessRepository;
import com.example.product.repo.ProductRepository;
import com.example.product.repo.SellAddressRepository;
import com.example.product.repo.TradeHistoryRepository;
import com.example.product.service.iface.ProductBusinessService;
import com.example.product.service.iface.kafka.ProducerService;
import com.example.product.service.iface.ms.BusinessService;
import com.example.product.utils.JsonUtils;
import com.example.product.utils.auth.AuthGuardService;
import com.example.product.utils.exception.ResourceNotFoundException;
import com.example.product.utils.exception.UnAuthorizedException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProductBusinessServiceImpl implements ProductBusinessService {
  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory
      .getLogger(ProductBusinessServiceImpl.class);

  private final String BUY = "BUY";
  private final String SELL = "SELL";

  private final ProductRepository productRepository;
  private final ProductBusinessRepository productBusinessRepository;
  private final BusinessService businessService;
  private final ProducerService producerService;
  private final SellAddressRepository sellAddressRepository;
  private final TradeHistoryRepository tradeHistoryRepository;
  private final AuthGuardService authGuardService;

  public ProductBusinessServiceImpl(ProductRepository productRepository,
      ProductBusinessRepository productBusinessRepository, BusinessService businessService,
      ProducerService producerService, SellAddressRepository sellAddressRepository,
      TradeHistoryRepository tradeHistoryRepository, AuthGuardService authGuardService) {
    this.productRepository = productRepository;
    this.productBusinessRepository = productBusinessRepository;
    this.businessService = businessService;
    this.producerService = producerService;
    this.sellAddressRepository = sellAddressRepository;
    this.tradeHistoryRepository = tradeHistoryRepository;
    this.authGuardService = authGuardService;
  }

  @Value("${kafka.topic.fund}")
  private String fundTopic;

  @Override
  @Transactional
  public boolean buy(BuyingProductRequest request) throws ResourceNotFoundException {
    if (request.getUserId() == null || request.getUserId() ==0) {
      throw new ResourceNotFoundException(ErrorCodeEnum.ID_BLANK);
    }
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
    String timeAdd = simpleDateFormat.format(date);
    checkProductAvailable(request.getProducts(), FundType.BUYING.name());
    long billValue = getBillValue(request.getProducts(), BUY);
    buyProduct(request, timeAdd, date, billValue);
    fund(billValue, request.getSource(), FundType.BUYING.name(), timeAdd, request.getUserId(), null);
    return true;
  }

  private void buyProduct(BuyingProductRequest request, String timeAdd, Date date, long billValue) {
    Set<Integer> productIds = request.getProducts().stream().map(RequestData::getId).collect(Collectors.toSet());
    List<RequestData> products = request.getProducts();
    List<BusinessProduct> saveRequest = new ArrayList<BusinessProduct>();
    List<TradeHistory> tradeSaveRequest = new ArrayList<TradeHistory>();
    for (int productId : productIds) {
      RequestData data = products.stream().filter(r -> r.getId() == productId).findFirst().get();
      BusinessProduct businessProduct = new BusinessProduct();
      businessProduct.setProductId(productId);
      businessProduct.setAvailable(findAmount(products, productId).getAmount());
      businessProduct.setSource(request.getSource());
      businessProduct.setInDate(date.getTime());
      businessProduct.setBatch(timeAdd);
      saveRequest.add(businessProduct);
      TradeHistory tradeHistory = new TradeHistory();
      Optional<Product> productOptional = productRepository.findById(productId);
      tradeHistory.setProductName(productOptional.get().getName());
      tradeHistory.setProductCode(productOptional.get().getCode());
      tradeHistory.setProductId(productId);
      tradeHistory.setType(FundType.BUYING.name());
      tradeHistory.setAmount(findAmount(products, productId).getAmount());
      tradeHistory.setBatch(timeAdd);
      tradeHistory.setCreateDate(new Date().getTime());
      tradeHistory.setEachValue(data.getValue());
      tradeHistory.setBillValue(billValue);
      tradeSaveRequest.add(tradeHistory);
    }
    productBusinessRepository.saveAll(saveRequest);
    tradeHistoryRepository.saveAll(tradeSaveRequest);
  }

  //check product available
  private void checkProductAvailable(List<RequestData> products, String type)
      throws ResourceNotFoundException {
    Set<Integer> productIds = products.stream().map(RequestData::getId).collect(Collectors.toSet());
    for (int productId : productIds) {
      Optional<Product> productOptional = productRepository.findById(productId);
      if (productOptional.isEmpty()) {
        throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_FOUND);
      }
      Product product = productOptional.get();
      if (product.getStatus().equals(ProductStatus.DEACTIVE.name())) {
        throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_SUPPORT);
      }
      if (FundType.SELLING.name().equals(type)) {
        checkSellableProduct(products);
      }
    }
  }


  private void checkSellableProduct(List<RequestData> products)
      throws ResourceNotFoundException {
    Set<Integer> productIds = products.stream().map(RequestData::getId).collect(Collectors.toSet());
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
      if (avai < findAmount(products, productId).getAmount() ){
        throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_ENOUGH);
      }
    }
  }

  private RequestData findAmount(List<RequestData> products, int id) {
    return products.stream().filter(r -> r.getId() == id).findFirst().get();
  }


  private long getBillValue(List<RequestData> products, String type) throws ResourceNotFoundException {
    long totalBill = 0L;
    Set<Integer> ids = products.stream().map(RequestData::getId).collect(Collectors.toSet());
    for (Integer productId : ids) {
      Optional<Product> productOptional = productRepository.findById(productId);
      Product product = productOptional.get();
      totalBill += findAmount(products, productId).getValue();
    }
//    long balance = 9999999999999L;
    long balance = this.getBalance();
    if (totalBill >= balance && type.equals(BUY)) {
      throw new ResourceNotFoundException(ErrorCodeEnum.BALANCE_NOT_ENOUGH);
    }
    return totalBill;
  }

  private long getBalance() throws ResourceNotFoundException {
    return businessService.checkBalance();
  }

  @Override
  @Transactional
  public boolean sell(HttpServletRequest servletRequest, SellingProductRequest request)
      throws ResourceNotFoundException, UnAuthorizedException {
    if (request.getUserId() == null || request.getUserId() ==0) {
      throw new ResourceNotFoundException(ErrorCodeEnum.ID_BLANK);
    }
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
    String timeAdd = simpleDateFormat.format(date);
    checkProductAvailable(request.getProducts(), FundType.SELLING.name());

    long billValue = getBillValue(request.getProducts(), SELL);
    String username = authGuardService.getUsernameFromToken(servletRequest);
    sellProduct(request.getProducts(), timeAdd, billValue, username, request.getCustomerPhone());
    fund(billValue, request.getCustomerPhone(), FundType.SELLING.name(), timeAdd,
        request.getUserId(), request.getType());
    return true;
  }

  @Override
  @Transactional
  public boolean sellOnline(SellingOnlineRequest request) throws ResourceNotFoundException {
    if (request.getUserId() == null || request.getUserId() ==0) {
      throw new ResourceNotFoundException(ErrorCodeEnum.ID_BLANK);
    }
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
    String timeAdd = simpleDateFormat.format(date);
    checkProductAvailable(request.getProducts(), FundType.SELLING.name());

    long billValue = getBillValue(request.getProducts(), SELL);
    sellProduct(request.getProducts(), timeAdd, billValue, "", request.getCustomerPhone());
    saveAddressInfo(request, timeAdd);
    fund(billValue, request.getCustomerPhone(), FundType.SELLING.name(), timeAdd,
        request.getUserId(), request.getType());
    return true;
  }

  private void saveAddressInfo(SellingOnlineRequest request, String batch) {
    Address address = request.getAddress();
    if (address != null) {
      SellAddressInfo info = new SellAddressInfo();
      info.setBatch(batch);
      info.setAddress(address.getAddress());
      info.setCity(address.getCity());
      info.setCusName(address.getName());
      info.setPhone(address.getPhone());
      info.setCusId(request.getUserId());
      info.setStatus("0");
      sellAddressRepository.save(info);
    }

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
          .findAllByProductIdAndAvailableIsNotOrderByInDateAsc(tradeHistory.getProductId(), 0);
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

  private void sellProduct(List<RequestData> products, String batch, long billValue, String username, String cusName) {
    Set<Integer> productIds = products.stream().map(RequestData::getId).collect(Collectors.toSet());
    List<BusinessProduct> saveRequest = new ArrayList<>();
    List<TradeHistory> tradeSaveRequest = new ArrayList<>();
    for (Integer productId : productIds) {
      RequestData data = products.stream().filter(r -> r.getId() == productId).findFirst().get();
      List<BusinessProduct> availableProducts = productBusinessRepository
          .findAllByProductIdAndAvailableIsNotOrderByInDateAsc(productId, 0);
      int amount = findAmount(products, productId).getAmount();
      for (BusinessProduct availableProduct : availableProducts) {
        while (amount > 0 && availableProduct.getAvailable() != 0) {
          int subtract = Math.max((availableProduct.getAvailable() - amount), 0);
          amount = Math.max((amount - availableProduct.getAvailable()), 0);
          availableProduct.setAvailable(subtract);
          saveRequest.add(availableProduct);
        }
      }
      TradeHistory tradeHistory = new TradeHistory();
      Optional<Product> productOptional = productRepository.findById(productId);
      tradeHistory.setProductName(productOptional.get().getName());
      tradeHistory.setProductCode(productOptional.get().getCode());
       tradeHistory.setProductId(productId);
      tradeHistory.setAmount(findAmount(products, productId).getAmount());
      tradeHistory.setType(FundType.SELLING.name());
      tradeHistory.setBatch(batch);
      tradeHistory.setCreateDate(new Date().getTime());
      tradeHistory.setEachValue(data.getValue());
      tradeHistory.setBillValue(billValue);
      tradeHistory.setUsername(username);
      tradeHistory.setCusName(cusName);
      tradeSaveRequest.add(tradeHistory);
    }
    productBusinessRepository.saveAll(saveRequest);
    tradeHistoryRepository.saveAll(tradeSaveRequest);
  }

  private void fund(long amount, String customer, String type, String batch, int userId, String paymentType) {
    FundRequestData fundData = new FundRequestData();
    fundData.setCustomer(customer);
    fundData.setType(type);
    fundData.setBatch(batch);
    fundData.setAmount(amount);
    fundData.setUuid(UUID.randomUUID().toString());
    fundData.setUserId(userId);
    fundData.setPaymentType(paymentType);
    boolean send = producerService.sendMessage(JsonUtils.toJson(fundData), fundTopic);
    logger.info("Sending message: {");
  }


}
