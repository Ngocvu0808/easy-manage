package com.example.business.entity;

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
@Table(name = "fund_history")
public class FundHistory {
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;
  @Column(name = "uuid")
  private String uuid;
  @Column(name = "amount")
  private long amount;
  @Column(name = "customer")
  private String customer;
  @Column(name = "type")
  private String type;
  @Column(name = "batch")
  private String batch;
  @Column(name = "user_id")
  private int userId;
}
