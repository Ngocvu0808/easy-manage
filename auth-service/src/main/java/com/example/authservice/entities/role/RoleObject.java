package com.example.authservice.entities.role;

import com.example.authservice.entities.BaseEntity;
import com.example.authservice.entities.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(
        name = "base_role_object"
)
public class RoleObject extends BaseEntity<Integer> {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;
    @Column(
            name = "service_name"
    )
    private String serviceName;
    @Column(
            name = "object_id"
    )
    private Integer objectId;
    @Column(
            name = "role_id",
            insertable = false,
            updatable = false
    )
    private Integer roleId;
    @Column(
            name = "user_id",
            insertable = false,
            updatable = false
    )
    private Integer userId;
    @ManyToOne(
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
    )
    private User user;
    @ManyToOne(
            cascade = {CascadeType.MERGE}
    )
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "id"
    )
    private Role role;

    public RoleObject() {
    }

    public Integer getId() {
        return this.id;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public Integer getObjectId() {
        return this.objectId;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public User getUser() {
        return this.user;
    }

    public Role getRole() {
        return this.role;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }

    public void setObjectId(final Integer objectId) {
        this.objectId = objectId;
    }

    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }

    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RoleObject)) {
            return false;
        } else {
            RoleObject other = (RoleObject)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label95: {
                    Object this$id = this.getId();
                    Object other$id = other.getId();
                    if (this$id == null) {
                        if (other$id == null) {
                            break label95;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label95;
                    }

                    return false;
                }

                Object this$serviceName = this.getServiceName();
                Object other$serviceName = other.getServiceName();
                if (this$serviceName == null) {
                    if (other$serviceName != null) {
                        return false;
                    }
                } else if (!this$serviceName.equals(other$serviceName)) {
                    return false;
                }

                Object this$objectId = this.getObjectId();
                Object other$objectId = other.getObjectId();
                if (this$objectId == null) {
                    if (other$objectId != null) {
                        return false;
                    }
                } else if (!this$objectId.equals(other$objectId)) {
                    return false;
                }

                label74: {
                    Object this$roleId = this.getRoleId();
                    Object other$roleId = other.getRoleId();
                    if (this$roleId == null) {
                        if (other$roleId == null) {
                            break label74;
                        }
                    } else if (this$roleId.equals(other$roleId)) {
                        break label74;
                    }

                    return false;
                }

                label67: {
                    Object this$userId = this.getUserId();
                    Object other$userId = other.getUserId();
                    if (this$userId == null) {
                        if (other$userId == null) {
                            break label67;
                        }
                    } else if (this$userId.equals(other$userId)) {
                        break label67;
                    }

                    return false;
                }

                Object this$user = this.getUser();
                Object other$user = other.getUser();
                if (this$user == null) {
                    if (other$user != null) {
                        return false;
                    }
                } else if (!this$user.equals(other$user)) {
                    return false;
                }

                Object this$role = this.getRole();
                Object other$role = other.getRole();
                if (this$role == null) {
                    if (other$role != null) {
                        return false;
                    }
                } else if (!this$role.equals(other$role)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RoleObject;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $serviceName = this.getServiceName();
        result = result * 59 + ($serviceName == null ? 43 : $serviceName.hashCode());
        Object $objectId = this.getObjectId();
        result = result * 59 + ($objectId == null ? 43 : $objectId.hashCode());
        Object $roleId = this.getRoleId();
        result = result * 59 + ($roleId == null ? 43 : $roleId.hashCode());
        Object $userId = this.getUserId();
        result = result * 59 + ($userId == null ? 43 : $userId.hashCode());
        Object $user = this.getUser();
        result = result * 59 + ($user == null ? 43 : $user.hashCode());
        Object $role = this.getRole();
        result = result * 59 + ($role == null ? 43 : $role.hashCode());
        return result;
    }

    public String toString() {
        return "RoleObject(id=" + this.getId() + ", serviceName=" + this.getServiceName() + ", objectId=" + this.getObjectId() + ", roleId=" + this.getRoleId() + ", userId=" + this.getUserId() + ", user=" + this.getUser() + ", role=" + this.getRole() + ")";
    }
}
