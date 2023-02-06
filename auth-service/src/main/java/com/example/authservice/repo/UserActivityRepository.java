package com.example.authservice.repo;

import com.example.authservice.entities.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author nguyen
 * @create_date 09/11/2021
 */
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {
  List<UserActivity> findByToken(String token);
}
