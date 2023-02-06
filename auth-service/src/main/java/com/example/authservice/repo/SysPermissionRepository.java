package com.example.authservice.repo;

import com.example.authservice.entities.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SysPermissionRepository extends JpaRepository<SysPermission, Integer> {
    List<SysPermission> findAllByIdIn(List<Integer> ids);

    SysPermission findByCode(String code);
}
