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
@Table(name = "sell_address")
@Getter
@Setter
public class SellAddressInfo {
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;

  @Column(name = "batch")
  private String batch;
  @Column(name = "cus_id")
  private int cusId;
  @Column(name = "cus_name")
  private String cusName;
  @Column(name = "phone")
  private String phone;
  @Column(name = "address")
  private String address;
  @Column(name = "city")
  private String city;
  @Column(name = "status")
  private String status;
  @Column(name = "username")
  private String username;
}
