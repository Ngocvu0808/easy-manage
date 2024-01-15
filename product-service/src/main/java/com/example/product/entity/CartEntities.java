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
@Table(name = "cart")
public class CartEntities {
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;

  @Column(name = "customer_id")
  private int customerId;
  @Column(name = "deleted")
  private int deleted;
  @Column(name = "create_date")
  private long createDate;
  @Column(name = "update_date")
  private long updateDate;

}
