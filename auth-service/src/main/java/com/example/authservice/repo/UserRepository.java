package com.example.authservice.repo;

import com.example.authservice.entities.UserStatus;
import com.example.authservice.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByUuid(String id);

  Optional<User> findByUsernameAndIsDeletedFalse(String userName);

  Optional<User> findByUsernameOrEmail(String userName, String email);

  Optional<User> findByUsername(String userName);

  Optional<User> findByEmail(String email);

  @EntityGraph(
      value = "graph.User.roles",
      type = EntityGraph.EntityGraphType.LOAD
  )
  Page<User> findAll(Specification<User> specification, Pageable pageable);

  @EntityGraph(
      value = "graph.User.roles",
      type = EntityGraph.EntityGraphType.LOAD
  )
  Optional<User> findById(Integer id);

  @Transactional
  @Modifying(
      clearAutomatically = true
  )
  @Query("UPDATE User u SET u.status=:status, u.updaterUserId=:updaterId WHERE u.id<>:updaterId")
  void updateStatusAll(@Param("status") UserStatus status, @Param("updaterId") Integer updaterId);

  @Transactional
  @Modifying(
      clearAutomatically = true
  )
  @Query("UPDATE User u SET u.isDeleted=TRUE, u.deleterUserId=:deleterId WHERE u.id <>:deleterId")
  void deleteAllUser(@Param("deleterId") Integer deleterId);

  @Query("SELECT u.username FROM User u WHERE u.isDeleted=FALSE")
  List<?> findAllUserNameByIsDeletedFalse();

  List<User> findAllByStatus(UserStatus status);

  @Transactional
  @Modifying(
      clearAutomatically = true
  )
  @Query("UPDATE User u SET u.status=:status, u.updaterUserId=:updaterId WHERE u.id <> :updaterId AND u.id IN (:idList)")
  void updateStatusListUser(@Param("status") UserStatus status,
      @Param("updaterId") Integer updaterId, @Param("idList") List<Integer> idList);

  @Transactional
  @Modifying(
      clearAutomatically = true
  )
  @Query("UPDATE User u SET u.status=:status, u.updaterUserId=:updaterId WHERE u.id <> :updaterId AND u.id NOT IN (:idList)")
  void updateStatusListUserBlacklist(@Param("status") UserStatus status,
      @Param("updaterId") Integer updaterId, @Param("idList") List<Integer> idList);

  @Transactional
  @Modifying(
      clearAutomatically = true
  )
  @Query("UPDATE User u SET u.isDeleted=TRUE, u.deleterUserId=:deleterId WHERE u.id <>:deleterId AND u.id IN (:idList)")
  void deleteListUser(@Param("deleterId") Integer deleterId, @Param("idList") List<Integer> idList);

  @Transactional
  @Modifying(
      clearAutomatically = true
  )
  @Query("UPDATE User u SET u.isDeleted=TRUE, u.deleterUserId=:deleterId WHERE u.id <>:deleterId AND u.id NOT IN (:idList)")
  void deleteListUserBlacklist(@Param("deleterId") Integer deleterId,
      @Param("idList") List<Integer> idList);
}
