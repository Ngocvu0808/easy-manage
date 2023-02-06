package com.example.authservice.repo;

import com.example.authservice.entities.service.ServiceTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ServiceTagRepository extends JpaRepository<ServiceTag, Integer>,
    JpaSpecificationExecutor<ServiceTag> {

}
