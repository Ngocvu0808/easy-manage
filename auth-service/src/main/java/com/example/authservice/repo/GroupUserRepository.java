package com.example.authservice.repo;

import com.example.authservice.entities.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupUserRepository extends JpaRepository<GroupUser, Integer> {
    int countAllByGroupIdAndIsDeleted(Integer groupId, Boolean delete);

    List<GroupUser> findByUserIdAndIsDeletedFalse(Integer userId);

    List<GroupUser> findByUserIdAndGroupId(Integer userId, Integer groupId);

    List<GroupUser> findByUserIdAndGroupIdAndIsDeletedFalse(Integer userId, Integer groupId);

    List<GroupUser> findByGroupIdAndUserIdAndIsDeletedFalse(Integer groupId, Integer userId);

    List<GroupUser> findByGroupIdAndIsDeletedFalse(Integer groupId);

    List<GroupUser> findAllByIsDeletedFalse();

    List<GroupUser> findAllByGroupIdInAndIsDeletedFalse(List<Integer> groupIdList);
}
