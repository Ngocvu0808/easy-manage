package com.example.product.service.impl;

import com.example.product.config.ErrorCodeEnum;
import com.example.product.dto.request.AddProductRequest;
import com.example.product.dto.request.UpdateProductRequest;
import com.example.product.dto.response.GetProductResponse;
import com.example.product.entity.BusinessProduct;
import com.example.product.entity.Product;
import com.example.product.entity.ProductReport;
import com.example.product.entity.ProductStatus;
import com.example.product.filter.ProductFilter;
import com.example.product.repo.ProductBusinessRepository;
import com.example.product.repo.ProductReportRepository;
import com.example.product.repo.ProductRepository;
import com.example.product.service.iface.ProductService;
import com.example.product.utils.DateUtil;
import com.example.product.utils.SortingUtils;
import com.example.product.utils.Utils;
import com.example.product.utils.exception.ResourceNotFoundException;
import com.example.product.utils.response.DataPagingResponse;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import org.springframework.util.StringUtils;

@Service
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductBusinessRepository productBusinessRepository;

  private final ProductReportRepository productReportRepository;
  public ProductServiceImpl(ProductRepository productRepository,
      ProductBusinessRepository productBusinessRepository, ProductReportRepository productReportRepository) {
    this.productRepository = productRepository;
    this.productBusinessRepository = productBusinessRepository;
    this.productReportRepository = productReportRepository;
  }

  @Override
  public int add(AddProductRequest addProductRequest) throws ResourceNotFoundException {
    if (StringUtils.isEmpty(addProductRequest.getType()) || !PRODUCT_TYPE.contains(addProductRequest.getType())) {
      throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_TYPE_NOT_VALID);
    }
    Product product = new Product();
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
    String timeAdd = simpleDateFormat.format(date);
    BeanUtils.copyProperties(addProductRequest, product);
    product.setCode(addProductRequest.getType().concat(timeAdd));
    product.setCreateDate(date.getTime());
    product.setStatus(ProductStatus.DEACTIVE.name());
    product.setSellPrice(addProductRequest.getSellPrice());
    Product result = productRepository.save(product);
    return result.getId();
  }

  List<String> PRODUCT_TYPE = Arrays.asList("BOOK", "DISK", "TOYS");

  @Override
  public DataPagingResponse<GetProductResponse> getAll(Integer page, Integer limit,
      String search, String status, String sort) {
    Map<String, String> sortMap = SortingUtils.detectSortType(sort);
    Specification<Product> filter = new ProductFilter().filter(null, search, status, sortMap);
    Page<Product> productPages = productRepository.findAll(filter, PageRequest.of(page - 1, limit));

    List<Product> products = productPages.getContent();
    List<GetProductResponse> clientDetails = new ArrayList<>();
    for (Product product : products) {
      GetProductResponse each = new GetProductResponse();
      BeanUtils.copyProperties(product, each);
      each.setAvailable(getAvailable(product));
      clientDetails.add(each);
    }
    DataPagingResponse<GetProductResponse> dataPagingResponses = new DataPagingResponse<>();
    dataPagingResponses.setList(clientDetails);
    dataPagingResponses.setTotalPage(productPages.getTotalPages());
    dataPagingResponses.setNum(productPages.getTotalElements());
    dataPagingResponses.setCurrentPage(page);
    return dataPagingResponses;
  }

  @Override
  public List<GetProductResponse> search(String search) {
    Map<String, String> sortMap = SortingUtils.detectSortType(null);
    Specification<Product> filter = new ProductFilter().filter(null, search, ProductStatus.ACTIVE.name(), sortMap);
    Page<Product> productPages = productRepository.findAll(filter, PageRequest.of(0, 10));

    List<Product> products = productPages.getContent();
    List<GetProductResponse> clientDetails = new ArrayList<>();
    for (Product product : products) {
      GetProductResponse each = new GetProductResponse();
      BeanUtils.copyProperties(product, each);
      each.setAvailable(getAvailable(product));
      clientDetails.add(each);
    }
    return clientDetails;
  }

  private long getAvailable(Product product) {
    List<BusinessProduct> businessProducts = productBusinessRepository.findAllByProductId(
        product.getId());
    long result = 0l;
    if (businessProducts.isEmpty())
      return result;
    for (BusinessProduct businessProduct : businessProducts) {
      result += businessProduct.getAvailable();
    }
    return result;
  }

  @Override
  public boolean delete(int id) throws ResourceNotFoundException {
    Optional<Product> products = productRepository.findById(id);
    if (products.isEmpty()) {
      throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_FOUND);
    }
    Product product = products.get();
    if (product.getStatus().equals(ProductStatus.DEACTIVE.name())) {
      product.setStatus(ProductStatus.ACTIVE.name());
    }
    else
      product.setStatus(ProductStatus.DEACTIVE.name());
    productRepository.save(product);
    return true;
  }

  @Override
  public GetProductResponse findOne(int id) throws ResourceNotFoundException {
    Optional<Product> products = productRepository.findById(id);
    if (products.isEmpty()) {
      throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_FOUND);
    }
    Product product = products.get();
    GetProductResponse result = new GetProductResponse();
    BeanUtils.copyProperties(product, result);
    return result;
  }

  @Override
  public boolean update(int id, UpdateProductRequest request) throws ResourceNotFoundException {
    if (id == 0) {
      throw new ResourceNotFoundException(ErrorCodeEnum.ID_BLANK);
    }
    Optional<Product> products = productRepository.findById(id);
    if (products.isEmpty()) {
      throw new ResourceNotFoundException(ErrorCodeEnum.PRODUCT_NOT_FOUND);
    }
    Product product = products.get();
    Utils.myCopyProperties(request, product);
    productRepository.save(product);
    return true;
  }

  @Override
  public long getAllValueProduct(long time) throws ParseException {
    long todayTime = time;
    if (time == 0) {
      todayTime = DateUtil.getOnlyDateFromTimeStamp(new Date().getTime());
    }
    ProductReport productReport = productReportRepository.findTopByTimeIsLessThanOrderByIdDesc(todayTime);
    if (productReport != null) {
      return productReport.getValue();
    }
    return 0;
  }

  @Override
  public List<Long> getListValueProduct(String times) throws ParseException {
    List<Long> result = new ArrayList<>();
    String[] timeSplit = times.split(",");
    SimpleDateFormat df  = new SimpleDateFormat("dd-MM-yyyy");
    for (String time: timeSplit) {
      if (!StringUtils.isEmpty(time)) {
        long date = df.parse(time).getTime();
        ProductReport productReport = productReportRepository.findTopByTimeIsLessThanOrderByIdDesc(date);
        if (productReport == null || productReport.getValue() == 0) {
          result.add(0L);
        } else {
          result.add(productReport.getValue());
        }
      }
    }
    return result;
  }

  @Override
  public List<Product> findByIdIn(String ids) {
    List<Integer> idList = new ArrayList<>();
    String [] idSplit = ids.trim().split(",");
    for (String each : idSplit) {
      if (!each.equals("undefined")) {
        idList.add(Integer.parseInt(each.trim()));
      }
    }
    return productRepository.findAllByIdIn(idList);
  }
}
