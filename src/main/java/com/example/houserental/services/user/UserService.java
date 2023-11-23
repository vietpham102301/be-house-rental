package com.example.houserental.services.user;

import com.example.houserental.controllers.models.UserInforResponse;
import com.example.houserental.controllers.models.UserLoginRequest;
import com.example.houserental.controllers.models.UserUpdateRequest;
import com.example.houserental.internal.models.user.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User login(UserLoginRequest userLoginRequest);
    Boolean removeUserById(int id);
    Page<UserInforResponse> listUserWithFilter(int pageNumber, int pageSize, String houseName, String status);
    boolean updateUserByUserId(int id, UserUpdateRequest userUpdateRequest);
    Page<UserInforResponse> searchUserByKeywords(String keywords, int page, int size);
    User getUserById(int id);

}
