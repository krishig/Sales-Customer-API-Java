package com.KrishiG.services;

import com.KrishiG.dtos.UserDto;

import java.util.List;

public interface UserService {

    //create user
    UserDto createUser(UserDto userDto);

    //update
    UserDto updateUser(UserDto userDto, Long userId);

    //delete
    void deleteUser(Long userId);

    //get all users
    List<UserDto> getAllUsers();
}
