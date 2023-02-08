package com.example.product.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "business_product")
public class BusinessProduct {

  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;

  @Column(name = "product_id")
  private int productId;

  @Column(name = "batch")
  private String batch;

  @Column(name = "in_date")
  private long inDate;

  @Column(name = "available")
  private int available;

  @Column(name = "source")
  private String source;
}
