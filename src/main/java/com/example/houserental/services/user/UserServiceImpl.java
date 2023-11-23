package com.example.houserental.services.user;

import com.example.houserental.Common.Message;
import com.example.houserental.controllers.models.*;
import com.example.houserental.exception.user.UserException;
import com.example.houserental.internal.models.house.House;
import com.example.houserental.internal.models.image.Image;
import com.example.houserental.internal.models.user.User;
import com.example.houserental.internal.repositories.house.HouseRepository;
import com.example.houserental.internal.repositories.user.UserRepository;
import com.example.houserental.services.image.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final HouseRepository houseRepository;
    private final ImageService imageService;

    public User createUser(User user) {

        List<User> existedUsers = userRepository.findByUsernameOrEmailOrPhoneOrIdNumber(user.getUsername(), user.getEmail(), user.getPhone(), user.getIdNumber());
        for(User u: existedUsers){
                if (user.getUsername().equals(u.getUsername())){
                    throw new UserException(Message.Exist_USERNAME);
                }else if(user.getEmail().equals(u.getEmail())){
                    throw new UserException(Message.Exist_EMAIL);
                }else if(user.getPhone().equals(u.getPhone())){
                    throw new UserException(Message.Exist_PHONE);
                }else if(user.getIdNumber().equals(u.getIdNumber())){
                    throw new UserException(Message.Exist_ID_NUM);
                }

        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public User login(UserLoginRequest userLoginRequest){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginRequest.getUsername(),
                            userLoginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
           throw new UserException(Message.WRONG_USERNAME_OR_PASSWORD);
        }

        User user = userRepository.findByUsername(userLoginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(user.getStatus().equals("Inactive") ){
            throw new UserException(Message.USER_INACTIVE);
        }
        return user;
    }

    @Override
    @Transactional
    public Boolean removeUserById(int id) {
        User user = userRepository.findUserById(id);
        if(user != null){
            return userRepository.updateUserStatusById(id) > 0;
        }
       throw new UserException(Message.USER_NOTFOUND);
    }

    @Override
    public Page<UserInforResponse> listUserWithFilter(int pageNumber, int pageSize, String houseName, String status) {
        List<UserInforResponse> result = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<User> users = userRepository.findAll();
        Comparator<User> createdAtComparator = Comparator.comparing(User::getCreatedAt);
        Collections.sort(users, createdAtComparator.reversed());
        if(status != null && status != ""){
            users = userRepository.findAllByStatus(status, PageRequest.of(pageNumber, pageSize));
        }

        addHouses(result, users);

        if(houseName != null && houseName != ""){
            List<UserInforResponse> resultFilter = new ArrayList<>();
            for(UserInforResponse userInforResponse: result){
                if(userInforResponse.getHouseNames().contains(houseName)){
                    resultFilter.add(userInforResponse);
                }
            }
            return getUserInforResponses(pageable, resultFilter);
        }

        return getUserInforResponses(pageable, result);
    }

    private Page<UserInforResponse> getUserInforResponses(Pageable pageable, List<UserInforResponse> resultFilter) {
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), resultFilter.size());
        final Page<UserInforResponse> response = new PageImpl<>(resultFilter.subList(start, end), pageable, resultFilter.size());
        return response;
    }
    //todo: add constraint unique for username, email, phone, idNumber
    @Override
    @Transactional
    public boolean updateUserByUserId(int id, UserUpdateRequest userUpdateRequest) {

        User existUser = userRepository.findUserById(id);
        if(existUser == null){
            throw new UserException(Message.USER_NOTFOUND);
        }
        String updatedPassword;
        if(userUpdateRequest.getPassword() != "" && userUpdateRequest.getPassword() != null){
            updatedPassword = passwordEncoder.encode(userUpdateRequest.getPassword());
            if(updatedPassword.equals(existUser.getPassword())){
                throw new UserException(Message.PASSWORD_NOT_CHANGE);
            }
        }else{
            updatedPassword = existUser.getPassword();
        }

        int updatedRow = userRepository.updateUserById(id, userUpdateRequest.getBirthDate(), userUpdateRequest.getGender(),
                userUpdateRequest.getEmail(), userUpdateRequest.getPhone(),
                userUpdateRequest.getIdNumber(), userUpdateRequest.getStartedDate(),
                userUpdateRequest.getStatus(), userUpdateRequest.getDescription(),
                updatedPassword, userUpdateRequest.getName(), userUpdateRequest.getRole(), userUpdateRequest.getUsername());
        return updatedRow > 0;
    }

    @Override
    public Page<UserInforResponse> searchUserByKeywords(String keywords, int page, int size) {
        List<UserInforResponse> result = new ArrayList<>();
        List<User> users;
        if(keywords == null){
            users = userRepository.findAll();
            addHouses(result, users);
            return getUserInforResponses(PageRequest.of(page, size), result);
        }
        users = userRepository.searchUsers(keywords);

        Pageable pageable = PageRequest.of(page, size);

        addHouses(result, users);
        return getUserInforResponses(pageable, result);
    }

    @Override
    public User getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        throw new UserException(Message.USER_NOTFOUND);
    }

    private void addHouses(List<UserInforResponse> result, List<User> users) {

        for(User user: users){
            List<String> houseNames = new ArrayList<>();
            List<House> houses = houseRepository.findHousesByManager(user.getId());
            for(House house: houses){
                houseNames.add(house.getName());
            }
            List<Map<String, Object>> imageResponse = new ArrayList<>();
            List<Image> images = imageService.getImagesByEntityId(user.getId(), "user");
            for(Image image: images){
                Map<String, Object> imageMap = new HashMap<>();
                imageMap.put("id", image.getId());
                imageMap.put("url", image.getUrl());
                imageResponse.add(imageMap);
            }
            result.add(new UserInforResponse(user, houseNames, imageResponse));
        }
    }
}
