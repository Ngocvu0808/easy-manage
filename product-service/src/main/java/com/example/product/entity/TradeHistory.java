package com.example.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "trade_history")
@Getter
@Setter
public class TradeHistory {
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;

  @Column(name = "batch")
  private String batch;
  @Column(name = "create_date")
  private long createDate;
  @Column(name = "product_id")
  private int productId;
  @Column(name = "product_name")
  private String productName;
  @Column(name = "product_code")
  private String productCode;
  @Column(name = "amount")
  private int amount;
  @Column(name = "bill_value")
  private long billValue;
  @Column(name = "each_value")
  private long eachValue;
  @Column(name = "username")
  private String username;
  @Column(name = "cus_name")
  private String cusName;
  @Column(name = "type")
  private String type;
}
