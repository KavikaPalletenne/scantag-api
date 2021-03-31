package xyz.scantag.dev.api.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.scantag.dev.api.entity.User;
import xyz.scantag.dev.api.entity.UserRole;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
public class UserDetailsImpl implements UserDetails {

    private User user;

    private UserDetails userDetails;


    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(User user, List<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        final Set<GrantedAuthority> grantedAuths = new HashSet<GrantedAuthority>();

        List<UserRole> roles = null;

        if (user != null) {
            roles = user.getRoles();
        }

        if (roles != null) {
            for (UserRole role : roles) {
                grantedAuths.add(new SimpleGrantedAuthority(role.getRoleName()));
            }
        }

        return grantedAuths;
    }


    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.getAccountActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.getAccountNonLocked();
    }
}
