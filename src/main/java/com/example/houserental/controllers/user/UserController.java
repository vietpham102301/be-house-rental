package com.example.houserental.controllers.user;

import com.example.houserental.config.JwtService;
import com.example.houserental.controllers.helper.ListCommonRes;
import com.example.houserental.controllers.models.*;
import com.example.houserental.internal.models.user.User;
import com.example.houserental.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class UserController {
    private final UserService userService;

    private final JwtService jwtService;

    @PostMapping("/user")
    public ResponseEntity<Object> createUser(@Validated @RequestBody UserCreateRequest userCreateRequest, BindingResult bindingResult) {
        HttpStatus status;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (bindingResult.hasErrors()) {
            status = HttpStatus.BAD_REQUEST;
            Map<String, List<String>> errors = new HashMap<>();
            errors.put("message", new ArrayList<>());
            bindingResult.getFieldErrors().forEach(error -> {
                if(errors.containsKey("message")){
                    errors.get("message").add(error.getDefaultMessage());
                }
            });
            return new ResponseEntity<>(errors, headers, status);
        }
        userCreateRequest.preProcessing();
        User user = userService.createUser(User.convertToUser(userCreateRequest));
        if(user != null){
            status = HttpStatus.CREATED;
            var jwtToken = jwtService.generatorToken(user);
            UserCreateResponse userCreateResponse = UserCreateResponse.toResponse(user);
            headers.set("Authorization", jwtToken);
            return new ResponseEntity<>(userCreateResponse, headers, status);
        }else{
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(null, headers, status);
        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> login (@RequestBody UserLoginRequest userLoginRequest){
        HttpStatus status = HttpStatus.OK;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User user = userService.login(userLoginRequest);
        var jwtToken = jwtService.generatorToken(user);
        headers.set("Authorization", jwtToken);
        user.setPassword(null);
        return new ResponseEntity<>(user, headers, status);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> removeUser(@PathVariable("id") int id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(userService.removeUserById(id)){
            Map<String, String> response = new HashMap<>();
            response.put("message", "User has been Inactivated");
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/user/list")
    public ResponseEntity<Object> listUserWithFilter(@RequestParam(value = "page", defaultValue = "0") int pageNumber,
                                                @RequestParam(value = "size", defaultValue = "10") int pageSize,
                                                @RequestParam(value = "houseName", required = false) String houseName,
                                                @RequestParam(value = "status", required = false) String status){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Page<UserInforResponse> users = userService.listUserWithFilter(pageNumber, pageSize, houseName, status);
        if(users != null){
            Map<String, Object> response = ListCommonRes.toResponse(users);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") int id, @RequestBody UserUpdateRequest userUpdateRequest){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if(userService.updateUserByUserId(id, userUpdateRequest)){
            Map<String, String> response = new HashMap<>();
            response.put("message", "User has been updated");
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/user/search")
    public ResponseEntity<Object> searchUserByKeywords(@RequestParam(value = "keywords", required = false) String keywords,
                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Page<UserInforResponse> users = userService.searchUserByKeywords(keywords, page, size);
        if(users != null){
            Map<String, Object> response = ListCommonRes.toResponse(users);
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") int id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        User user = userService.getUserById(id);
        if(user != null){
            return new ResponseEntity<>(user, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
