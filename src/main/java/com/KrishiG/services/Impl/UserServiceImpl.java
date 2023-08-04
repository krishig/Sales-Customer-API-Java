package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.UserRequestDto;
import com.KrishiG.enitites.User;
import com.KrishiG.exception.ResourceNotFoundException;
import com.KrishiG.repositories.UserRepository;
import com.KrishiG.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserRequestDto createUser(UserRequestDto userRequestDto) {

        User user = mapper.map(userRequestDto, User.class);
        User savedUser = userRepository.save(user);
        return mapper.map(savedUser, UserRequestDto.class);
    }

    @Override
    public UserRequestDto updateUser(UserRequestDto userRequestDto, Long userId) {

        //fetch the user from DB using userId
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with the Given userID"));

        //setting the values
        user.setUserName(userRequestDto.getUserName());
        user.setFullName(userRequestDto.getFullName());
        user.setEmailId(userRequestDto.getEmailId());
        user.setHouseNumber(userRequestDto.getHouseNumber());
        user.setGender(userRequestDto.getGender());
        user.setPassword(userRequestDto.getPassword());
        user.setMobileNumber(userRequestDto.getMobileNumber());
        user.setPinCode(userRequestDto.getPinCode());
        user.setDistrict(userRequestDto.getDistrict());
        user.setCity(userRequestDto.getCity());
        user.setState(userRequestDto.getState());
        user.setRole(userRequestDto.getRole());
        user.setCreatedBy(userRequestDto.getCreatedBy());
        user.setCreatedAt(userRequestDto.getCreatedAt());
        user.setModifiedBy(userRequestDto.getModifiedBy());
        user.setLandMark(userRequestDto.getLandMark());
        user.setGender(userRequestDto.getGender());

        //save the user
        userRepository.save(user);

        //convert the user from User -> UserDto
       return mapper.map(user, UserRequestDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        User user  = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserRequestDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserRequestDto> dtoList = users.stream().map(user -> mapper.map(user, UserRequestDto.class)).collect(Collectors.toList());
        return dtoList;
    }

}
