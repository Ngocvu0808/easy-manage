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
@Table(name = "finance_report")
public class Finance {
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private int id;

  @Column(name = "balance")
  private long balance;

  @Column(name = "update_time")
  private long updateTime;

  @Column(name = "trade_type")
  private String tradeType;

  @Column(name = "amount")
  private long amount;
}
