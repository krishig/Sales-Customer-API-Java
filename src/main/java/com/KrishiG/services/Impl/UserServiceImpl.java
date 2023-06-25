package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.UserDto;
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
    public UserDto createUser(UserDto userDto) {

        User user = mapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return mapper.map(savedUser,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {

        //fetch the user from DB using userId
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with the Given userID"));

        //setting the values
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmailId(userDto.getEmailId());
        user.setAddress(userDto.getAddress());
        user.setGender(userDto.getGender());
        user.setPassword(userDto.getPassword());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setPinCode(userDto.getPinCode());
        user.setTehsil(userDto.getTehsil());
        user.setCity(userDto.getCity());
        user.setState(userDto.getState());
        user.setRole(userDto.getRole());
        user.setCreatedBy(userDto.getCreatedBy());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setModifiedBy(userDto.getModifiedBy());
        user.setLandMark(userDto.getLandMark());
        user.setGender(userDto.getGender());

        //save the user
        userRepository.save(user);

        //convert the user from User -> UserDto
       return mapper.map(user,UserDto.class);
    }

    @Override
    public void deleteUser(Long userId) {
        User user  = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User Not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtoList = users.stream().map(user -> mapper.map(user,UserDto.class)).collect(Collectors.toList());
        return dtoList;
    }
}
