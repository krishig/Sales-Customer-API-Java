package com.KrishiG.services;

import com.KrishiG.dtos.request.UserRequestDto;

import java.util.List;

public interface UserService {

    //create user
    UserRequestDto createUser(UserRequestDto userRequestDto);

    //update
    UserRequestDto updateUser(UserRequestDto userRequestDto, Long userId);

    //delete
    void deleteUser(Long userId);

    //get all users
    List<UserRequestDto> getAllUsers();

    public boolean getUserById(Long userId);
}
