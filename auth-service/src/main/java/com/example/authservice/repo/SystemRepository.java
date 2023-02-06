package com.example.authservice.repo;

import com.example.authservice.entities.service.System;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SystemRepository extends JpaRepository<System, Integer>,
    JpaSpecificationExecutor<System> {

}
