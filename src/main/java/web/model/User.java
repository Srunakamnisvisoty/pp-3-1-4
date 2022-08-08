package web.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "roleList")
public class User implements Serializable, UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;


    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Role> roleList = new ArrayList<>();

    @Transient
    private List<SecurityRoles> securityRolesList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList;
    }

    @Override
    public String getUsername() {
        return getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRoleListString() {
        StringBuilder role = new StringBuilder();
        for (Role r : roleList) {
            role.append(r.getRole());
            role.append(" ");
        }
        return role.toString();
    }

    public void setSecurityRolesList(List<SecurityRoles> securityRoles) {
        securityRolesList = securityRoles;
    }

    public List<SecurityRoles> getSecurityRolesList() {
        if (securityRolesList.size() == 0) {
            for (Role r : roleList) {
                securityRolesList.add(r.getRoles());
            }
        }
        return securityRolesList;
    }

    public void addRole(Role role) {
        role.setUser(this);
        roleList.add(role);
    }

    public void removeRole(Role role) {
        roleList.remove(role);
        role.setUser(null);
    }

    public boolean isAdmin() {
        return roleList.stream().anyMatch(role -> "ADMIN".equalsIgnoreCase(role.getRole()));
    }

}
