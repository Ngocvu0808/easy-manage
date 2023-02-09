package com.example.product.job;

import com.example.product.entity.BusinessProduct;
import com.example.product.entity.Product;
import com.example.product.entity.ProductReport;
import com.example.product.entity.ProductStatus;
import com.example.product.repo.ProductBusinessRepository;
import com.example.product.repo.ProductReportRepository;
import com.example.product.repo.ProductRepository;
import com.example.product.utils.DateUtil;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class GetAllProductValue {
  private static final Logger logger = LoggerFactory.getLogger(GetAllProductValue.class);
  private final ProductRepository productRepository;
  private final ProductBusinessRepository productBusinessRepository;
  private final ProductReportRepository productReportRepository;

  public GetAllProductValue(ProductRepository productRepository,
      ProductBusinessRepository productBusinessRepository,
      ProductReportRepository productReportRepository) {
    this.productRepository = productRepository;
    this.productBusinessRepository = productBusinessRepository;
    this.productReportRepository = productReportRepository;
  }

  @Scheduled(cron = "${job.cron.get-all-product-value}")
  public void getAllProductValue() throws ParseException {
    logger.info("getAllProductValue()");
    ProductReport productReport = new ProductReport();
    productReport.setTime(DateUtil.getOnlyDateFromTimeStamp(new Date().getTime()));
    long productValue = 0L;
    List<Product> productAvailable = productRepository.findAllByStatus(ProductStatus.ACTIVE.name());
    if (!productAvailable.isEmpty()) {
      for (Product product : productAvailable) {
        List<BusinessProduct> businessProducts = productBusinessRepository.findAllByProductId(
            product.getId());
        if (!businessProducts.isEmpty()) {
          for (BusinessProduct each : businessProducts) {
            productValue += each.getAvailable() * product.getSellPrice();
          }
        }
      }
    }
    productReport.setValue(productValue);
    ProductReport result = productReportRepository.save(productReport);
    logger.info("Saved product report: {}", result);
  }
}
