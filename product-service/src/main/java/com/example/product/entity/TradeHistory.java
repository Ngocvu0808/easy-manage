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
  @Column(name = "amount")
  private int amount;
  @Column(name = "type")
  private String type;
}
