package com.example.authservice.repo;

import com.example.authservice.entities.UserActivity;
import com.example.authservice.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author nguyen
 * @create_date 09/11/2021
 */
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {
  Page<UserActivity> findAll(Specification<UserActivity> specification, Pageable pageable);
  List<UserActivity> findByToken(String token);
}
