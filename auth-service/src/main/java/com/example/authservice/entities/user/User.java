package com.example.authservice.entities.user;


import com.example.authservice.entities.BaseEntity;
import com.example.authservice.entities.GroupUser;
import com.example.authservice.entities.RoleUser;
import com.example.authservice.entities.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */
@Entity
@Table(
    name = "base_user"
)
@NamedEntityGraphs({@NamedEntityGraph(
    name = "graph.User.roles",
    attributeNodes = {@NamedAttributeNode(
        value = "roles",
        subgraph = "sub-role"
    )},
    subgraphs = {@NamedSubgraph(
        name = "sub-role",
        attributeNodes = {@NamedAttributeNode("role")}
    )}
), @NamedEntityGraph(
    name = "graph.User.groups",
    attributeNodes = {@NamedAttributeNode(
        value = "groups",
        subgraph = "sub-group"
    )},
    subgraphs = {@NamedSubgraph(
        name = "sub-group",
        attributeNodes = {@NamedAttributeNode("group")}
    )}
)})
public class User extends BaseEntity<Integer> implements Serializable {

  private static final long serialVersionUID = -4322942271396270532L;
  @Id
  @GeneratedValue(
      strategy = GenerationType.IDENTITY
  )
  private Integer id;
  @Column(
      name = "uuid",
      unique = true
  )
  private String uuid;
  @Column(
      name = "username",
      unique = true
  )
  private String username;
  private String name;
  @Column(
      name = "email",
      unique = true
  )
  private String email;
  private String password;
  @Column(
      name = "last_name"
  )
  private String lastName;
  @Column(
      name = "first_name"
  )
  private String firstName;
  @Column(
      name = "status"
  )
  @Enumerated(EnumType.STRING)
  private UserStatus status;
  @OneToMany(
      mappedBy = "user",
      cascade = {CascadeType.ALL}
  )
  private List<GroupUser> groups;
  @OneToMany(
      mappedBy = "user",
      cascade = {CascadeType.ALL}
  )
  private List<RoleUser> roles;
  @Column(
      name = "is_internal"
  )
  private Boolean isUserInternal;
  @Column(
      name = "secret_key"
  )
  private String secretKey;

  public User() {
    this.status = UserStatus.ACTIVE;
  }

  public Integer getId() {
    return this.id;
  }

  public String getUuid() {
    return this.uuid;
  }

  public String getUsername() {
    return this.username;
  }

  public String getName() {
    return this.name;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPassword() {
    return this.password;
  }

  public String getLastName() {
    return this.lastName;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public UserStatus getStatus() {
    return this.status;
  }

  public List<GroupUser> getGroups() {
    return this.groups;
  }

  public List<RoleUser> getRoles() {
    return this.roles;
  }

  public Boolean getIsUserInternal() {
    return this.isUserInternal;
  }

  public String getSecretKey() {
    return this.secretKey;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public void setUuid(final String uuid) {
    this.uuid = uuid;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public void setStatus(final UserStatus status) {
    this.status = status;
  }

  public void setGroups(final List<GroupUser> groups) {
    this.groups = groups;
  }

  public void setRoles(final List<RoleUser> roles) {
    this.roles = roles;
  }

  public void setIsUserInternal(final Boolean isUserInternal) {
    this.isUserInternal = isUserInternal;
  }

  public void setSecretKey(final String secretKey) {
    this.secretKey = secretKey;
  }

  public boolean equals(final Object o) {
    if (o == this) {
      return true;
    } else if (!(o instanceof User)) {
      return false;
    } else {
      User other = (User) o;
      if (!other.canEqual(this)) {
        return false;
      } else {
        label167:
        {
          Object this$id = this.getId();
          Object other$id = other.getId();
          if (this$id == null) {
            if (other$id == null) {
              break label167;
            }
          } else if (this$id.equals(other$id)) {
            break label167;
          }

          return false;
        }

        Object this$uuid = this.getUuid();
        Object other$uuid = other.getUuid();
        if (this$uuid == null) {
          if (other$uuid != null) {
            return false;
          }
        } else if (!this$uuid.equals(other$uuid)) {
          return false;
        }

        label153:
        {
          Object this$username = this.getUsername();
          Object other$username = other.getUsername();
          if (this$username == null) {
            if (other$username == null) {
              break label153;
            }
          } else if (this$username.equals(other$username)) {
            break label153;
          }

          return false;
        }

        Object this$name = this.getName();
        Object other$name = other.getName();
        if (this$name == null) {
          if (other$name != null) {
            return false;
          }
        } else if (!this$name.equals(other$name)) {
          return false;
        }

        label139:
        {
          Object this$email = this.getEmail();
          Object other$email = other.getEmail();
          if (this$email == null) {
            if (other$email == null) {
              break label139;
            }
          } else if (this$email.equals(other$email)) {
            break label139;
          }

          return false;
        }

        Object this$password = this.getPassword();
        Object other$password = other.getPassword();
        if (this$password == null) {
          if (other$password != null) {
            return false;
          }
        } else if (!this$password.equals(other$password)) {
          return false;
        }

        label125:
        {
          Object this$lastName = this.getLastName();
          Object other$lastName = other.getLastName();
          if (this$lastName == null) {
            if (other$lastName == null) {
              break label125;
            }
          } else if (this$lastName.equals(other$lastName)) {
            break label125;
          }

          return false;
        }

        label118:
        {
          Object this$firstName = this.getFirstName();
          Object other$firstName = other.getFirstName();
          if (this$firstName == null) {
            if (other$firstName == null) {
              break label118;
            }
          } else if (this$firstName.equals(other$firstName)) {
            break label118;
          }

          return false;
        }

        Object this$status = this.getStatus();
        Object other$status = other.getStatus();
        if (this$status == null) {
          if (other$status != null) {
            return false;
          }
        } else if (!this$status.equals(other$status)) {
          return false;
        }

        label104:
        {
          Object this$groups = this.getGroups();
          Object other$groups = other.getGroups();
          if (this$groups == null) {
            if (other$groups == null) {
              break label104;
            }
          } else if (this$groups.equals(other$groups)) {
            break label104;
          }

          return false;
        }

        label97:
        {
          Object this$roles = this.getRoles();
          Object other$roles = other.getRoles();
          if (this$roles == null) {
            if (other$roles == null) {
              break label97;
            }
          } else if (this$roles.equals(other$roles)) {
            break label97;
          }

          return false;
        }

        Object this$isUserInternal = this.getIsUserInternal();
        Object other$isUserInternal = other.getIsUserInternal();
        if (this$isUserInternal == null) {
          if (other$isUserInternal != null) {
            return false;
          }
        } else if (!this$isUserInternal.equals(other$isUserInternal)) {
          return false;
        }

        Object this$secretKey = this.getSecretKey();
        Object other$secretKey = other.getSecretKey();
        if (this$secretKey == null) {
          if (other$secretKey != null) {
            return false;
          }
        } else if (!this$secretKey.equals(other$secretKey)) {
          return false;
        }

        return true;
      }
    }
  }

  protected boolean canEqual(final Object other) {
    return other instanceof User;
  }

  public int hashCode() {
    boolean PRIME = true;
    int result = 1;
    Object $id = this.getId();
    result = result * 59 + ($id == null ? 43 : $id.hashCode());
    Object $uuid = this.getUuid();
    result = result * 59 + ($uuid == null ? 43 : $uuid.hashCode());
    Object $username = this.getUsername();
    result = result * 59 + ($username == null ? 43 : $username.hashCode());
    Object $name = this.getName();
    result = result * 59 + ($name == null ? 43 : $name.hashCode());
    Object $email = this.getEmail();
    result = result * 59 + ($email == null ? 43 : $email.hashCode());
    Object $password = this.getPassword();
    result = result * 59 + ($password == null ? 43 : $password.hashCode());
    Object $lastName = this.getLastName();
    result = result * 59 + ($lastName == null ? 43 : $lastName.hashCode());
    Object $firstName = this.getFirstName();
    result = result * 59 + ($firstName == null ? 43 : $firstName.hashCode());
    Object $status = this.getStatus();
    result = result * 59 + ($status == null ? 43 : $status.hashCode());
    Object $groups = this.getGroups();
    result = result * 59 + ($groups == null ? 43 : $groups.hashCode());
    Object $roles = this.getRoles();
    result = result * 59 + ($roles == null ? 43 : $roles.hashCode());
    Object $isUserInternal = this.getIsUserInternal();
    result = result * 59 + ($isUserInternal == null ? 43 : $isUserInternal.hashCode());
    Object $secretKey = this.getSecretKey();
    result = result * 59 + ($secretKey == null ? 43 : $secretKey.hashCode());
    return result;
  }

  public String toString() {
    return "User(id=" + this.getId() + ", uuid=" + this.getUuid() + ", username=" + this
        .getUsername() + ", name=" + this.getName() + ", email=" + this.getEmail() + ", password="
        + this.getPassword() + ", lastName=" + this.getLastName() + ", firstName=" + this
        .getFirstName() + ", status=" + this.getStatus() + ", groups=" + this.getGroups()
        + ", roles=" + this.getRoles() + ", isUserInternal=" + this.getIsUserInternal()
        + ", secretKey=" + this.getSecretKey() + ")";
  }
}