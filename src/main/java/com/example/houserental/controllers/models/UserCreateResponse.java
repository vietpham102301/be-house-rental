package com.example.houserental.controllers.models;

import com.example.houserental.internal.models.user.Role;
import com.example.houserental.internal.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateResponse {
    private Integer id;

    private String username;
    private String password;
    private Role role;

    public static UserCreateResponse toResponse(User user){
        return new UserCreateResponse(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }
}
