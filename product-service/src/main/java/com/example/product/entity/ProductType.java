package com.example.product.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "base_product_type")
@Getter
@Setter
public class ProductType {

  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;

  @Column(name = "name")
  private String name;
  @Column(name = "create_date")
  private long createDate;
  @Column(name = "status")
  private String status;
}
