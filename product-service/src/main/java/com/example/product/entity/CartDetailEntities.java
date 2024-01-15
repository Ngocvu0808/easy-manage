package com.example.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_detail")
public class CartDetailEntities {
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;

  @Column(name = "product_id")
  private int productId;
  @Column(name = "deleted")
  private int deleted;
  @Column(name = "product_amount")
  private int amount;
  @Column(name = "cart_id")
  private int cartId;
}
