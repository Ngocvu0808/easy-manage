package com.example.authservice.dto.group;

import com.example.authservice.dto.UserGroupCustomDto;
import com.example.authservice.dto.role.RoleCustomDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupDto {
    private Integer id;
    private @Size(
            max = 150,
            message = "Code longer than 150 characters"
    ) @Pattern(
            regexp = "^[A-Za-z0-9_.+-@%]+$",
            message = "Code has spec character"
    ) @NotBlank(
            message = "Code not null"
    ) String code;
    private @Size(
            max = 255,
            message = "Name longer than 255 characters"
    ) @NotBlank(
            message = "Name not null"
    ) String name;
    private Integer numberMember;
    private Date createdTime;
    private List<RoleCustomDto> roles;
    private List<UserGroupCustomDto> users;
    private Set<Integer> roleIds = new HashSet();

    public GroupDto() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Integer> getRoleIds() {
        return this.roleIds;
    }

    public void setRoleIds(Set<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public Integer getNumberMember() {
        return this.numberMember;
    }

    public void setNumberMember(Integer numberMember) {
        this.numberMember = numberMember;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public List<RoleCustomDto> getRoles() {
        return this.roles;
    }

    public void setRoles(List<RoleCustomDto> roles) {
        this.roles = roles;
    }

    public List<UserGroupCustomDto> getUsers() {
        return this.users;
    }

    public void setUsers(List<UserGroupCustomDto> users) {
        this.users = users;
    }
}
