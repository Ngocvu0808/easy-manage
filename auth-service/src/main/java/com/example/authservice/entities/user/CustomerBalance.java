package com.example.authservice.entities.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "customer_balance")
public class CustomerBalance {
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private Integer id;
  @Column(name = "customer_id")
  private int customerId;
  @Column(name = "balance")
  private long balance;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getCustomerId() {
    return customerId;
  }

  public void setCustomerId(int customerId) {
    this.customerId = customerId;
  }

  public long getBalance() {
    return balance;
  }

  public void setBalance(long balance) {
    this.balance = balance;
  }
}
