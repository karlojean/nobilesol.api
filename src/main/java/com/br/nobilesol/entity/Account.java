package com.br.nobilesol.entity;

import com.br.nobilesol.entity.enums.AccountRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "accounts",
        uniqueConstraints = @UniqueConstraint(name = "uq_accounts_email", columnNames = "email"))
public class Account extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 32)
    private AccountRole role;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Employee employee;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Investor investor;

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new java.util.ArrayList<>();
        
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        
        if (role == AccountRole.EMPLOYEE && employee != null && employee.isAdmin()) {
                authorities.add(new SimpleGrantedAuthority("PERMISSION_EMPLOYEE_MANAGEMENT"));
        }
        
        return authorities;
    }

    @Override public String getPassword() { return passwordHash; }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return active; }
}
