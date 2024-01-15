package com.example.product.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "base_product")
@Getter
@Setter
public class Product {

  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;

  @Column(name = "code")
  private String code;

  @Column(name = "name")
  private String name;

  @Column(name = "type")
  private String type;

  @Column(name = "sell_price")
  private long sellPrice;

  @Column(name = "buy_price")
  private long buyPrice;

  @Column(name = "create_date")
  private long createDate;

  @Column(name = "status")
  private String status;
  @Column(name = "link")
  private String link;
}