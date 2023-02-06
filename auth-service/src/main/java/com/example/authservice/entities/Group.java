package com.example.authservice.entities;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author nguyen
 * @create_date 02/09/2022
 */

@Entity
@Table(
        name = "base_group"
)
@NamedEntityGraphs({@NamedEntityGraph(
        name = "GroupRoles",
        attributeNodes = {@NamedAttributeNode(
                value = "roles",
                subgraph = "sub-role"
        )},
        subgraphs = {@NamedSubgraph(
                name = "sub-role",
                attributeNodes = {@NamedAttributeNode("role")}
        )}
), @NamedEntityGraph(
        name = "GroupUsers",
        attributeNodes = {@NamedAttributeNode(
                value = "users",
                subgraph = "sub-user"
        )},
        subgraphs = {@NamedSubgraph(
                name = "sub-user",
                attributeNodes = {@NamedAttributeNode("user")}
        )}
)})
public class Group extends BaseEntity<Integer> implements Serializable {
   private static final long serialVersionUID = -8448334656386508649L;
   @Id
   @GeneratedValue(
           strategy = GenerationType.IDENTITY
   )
   private Integer id;
   private String name;
   @Column(
           name = "code"
   )
   private String code;
   @LazyCollection(LazyCollectionOption.TRUE)
   @OneToMany(
           mappedBy = "group",
           cascade = {CascadeType.ALL}
   )
   private List<RoleGroup> roles;
   @LazyCollection(LazyCollectionOption.TRUE)
   @OneToMany(
           mappedBy = "group",
           cascade = {CascadeType.ALL}
   )
   private List<GroupUser> users;

   public Group() {
   }

   public Integer getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String getCode() {
      return this.code;
   }

   public List<RoleGroup> getRoles() {
      return this.roles;
   }

   public List<GroupUser> getUsers() {
      return this.users;
   }

   public void setId(final Integer id) {
      this.id = id;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public void setCode(final String code) {
      this.code = code;
   }

   public void setRoles(final List<RoleGroup> roles) {
      this.roles = roles;
   }

   public void setUsers(final List<GroupUser> users) {
      this.users = users;
   }

   public boolean equals(final Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Group)) {
         return false;
      } else {
         Group other = (Group)o;
         if (!other.canEqual(this)) {
            return false;
         } else {
            label71: {
               Object this$id = this.getId();
               Object other$id = other.getId();
               if (this$id == null) {
                  if (other$id == null) {
                     break label71;
                  }
               } else if (this$id.equals(other$id)) {
                  break label71;
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

            label57: {
               Object this$code = this.getCode();
               Object other$code = other.getCode();
               if (this$code == null) {
                  if (other$code == null) {
                     break label57;
                  }
               } else if (this$code.equals(other$code)) {
                  break label57;
               }

               return false;
            }

            Object this$roles = this.getRoles();
            Object other$roles = other.getRoles();
            if (this$roles == null) {
               if (other$roles != null) {
                  return false;
               }
            } else if (!this$roles.equals(other$roles)) {
               return false;
            }

            Object this$users = this.getUsers();
            Object other$users = other.getUsers();
            if (this$users == null) {
               if (other$users == null) {
                  return true;
               }
            } else if (this$users.equals(other$users)) {
               return true;
            }

            return false;
         }
      }
   }

   protected boolean canEqual(final Object other) {
      return other instanceof Group;
   }

   public int hashCode() {
      boolean PRIME = true;
      int result = 1;
      Object $id = this.getId();
      result = result * 59 + ($id == null ? 43 : $id.hashCode());
      Object $name = this.getName();
      result = result * 59 + ($name == null ? 43 : $name.hashCode());
      Object $code = this.getCode();
      result = result * 59 + ($code == null ? 43 : $code.hashCode());
      Object $roles = this.getRoles();
      result = result * 59 + ($roles == null ? 43 : $roles.hashCode());
      Object $users = this.getUsers();
      result = result * 59 + ($users == null ? 43 : $users.hashCode());
      return result;
   }

   public String toString() {
      return "Group(id=" + this.getId() + ", name=" + this.getName() + ", code=" + this.getCode() + ", roles=" + this.getRoles() + ", users=" + this.getUsers() + ")";
   }
}
