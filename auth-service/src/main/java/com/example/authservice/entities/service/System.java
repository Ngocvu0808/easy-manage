package com.example.authservice.entities.service;

import com.example.authservice.entities.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nguyen
 * @created_date 31/07/2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "system")
public class System extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = 7925351816268542735L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String code;

  private String name;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
