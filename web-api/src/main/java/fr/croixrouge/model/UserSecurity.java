package fr.croixrouge.model;

import fr.croixrouge.domain.model.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserSecurity extends User implements UserDetails {

    public Map<Resources, Set<Operations>> allAuthorizations;

    public UserSecurity(ID userId, String username, String password, LocalUnit localUnit, List<Role> roles, boolean emailValidated, String tokenToValidateEmail, String firebaseToken) {
        super(userId, username, password, localUnit, roles, emailValidated, tokenToValidateEmail, firebaseToken);

        allAuthorizations = new HashMap<>();
        for (var role : roles) {
            for (var entry : role.getAuthorizations().entrySet()) {
                var resource = entry.getKey();
                var operations = entry.getValue();

                if (!allAuthorizations.containsKey(resource)) {
                    allAuthorizations.put(resource, new HashSet<>());
                }

                for (var operation : operations) {

                    allAuthorizations.get(resource).add(operation);
                }
            }
        }

    }

    public UserSecurity(User user) {
        this(user.getId(), user.getUsername(), user.getPassword(), user.getLocalUnit(), user.getRoles(), user.isEmailValidated(), user.getTokenToValidateEmail(), user.getFirebaseToken());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            for (Map.Entry<Resources, Set<Operations>> entry : role.getAuthorizations().entrySet()) {
                Resources resource = entry.getKey();
                for (Operations operation : entry.getValue()) {
                    authorities.add(new SimpleGrantedAuthority(resource.name() + "_" + operation.name()));
                }
            }
        }
        return authorities;
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

    @Override
    public String toString() {
        return "User{" +
                "userId='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", authorities=" + getAuthorities() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSecurity userSecurity = (UserSecurity) o;
        return Objects.equals(id, userSecurity.id) && Objects.equals(username, userSecurity.username) && Objects.equals(password, userSecurity.password) && Objects.equals(roles, userSecurity.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, roles);
    }
}
