package com.example.product.config;

public enum ErrorCodeEnum {
  PRODUCT_NOT_FOUND("001", "Không tìm thấy sản phẩm"),
  ID_BLANK("002", "Id truyền vào null"),
  PRODUCT_NOT_SUPPORT("003", "Sản phẩm đã ngừng kinh doanh"),
  BALANCE_NOT_ENOUGH("004", "Số dư không đủ"),
  PRODUCT_NOT_ENOUGH("005", "Không còn đủ hàng"),
  BUSINESS_ERROR("006", "Thực hiện yêu cầu đến business-service không thành công"),
  NOT_FOUND("007", "Không tìm thấy bản ghi"),

  ;

  private final String value;
  private final String description;

  private ErrorCodeEnum(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public String getValue() {
    return this.value;
  }

  public String getDescription() {
    return this.description;
  }
}
