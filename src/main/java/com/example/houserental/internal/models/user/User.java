package com.example.houserental.internal.models.user;

import com.example.houserental.controllers.models.UserCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String name;

    @Column(name = "birthdate")
    private Date birthdate;

    private String gender;
    private String phone;
    private String email;

    @Column(name = "id_number")
    private String idNumber;

    @Column(name = "started_date")
    private Date startedDate;

    private String status;
    private String description;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    public static User convertToUser(UserCreateRequest userCreateRequest){
        User user = new User(null, userCreateRequest.getUsername(), userCreateRequest.getPassword(),
                userCreateRequest.getName(), userCreateRequest.getBirthdate(), userCreateRequest.getGender(), userCreateRequest.getPhone(),
                userCreateRequest.getEmail(), userCreateRequest.getIdNumber(),  userCreateRequest.getStartedDate(),
                "Active", userCreateRequest.getDescription(), userCreateRequest.getRole(), new Date());
        return user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
}