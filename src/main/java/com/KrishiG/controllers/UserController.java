package com.KrishiG.controllers;

import com.KrishiG.dtos.request.UserRequestDto;
import com.KrishiG.responsesApiMessages.ApiResponseMessage;
import com.KrishiG.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //create
    @PostMapping
    public ResponseEntity<UserRequestDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto)
    {
         UserRequestDto user = userService.createUser(userRequestDto);
         return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{userId}")
    public ResponseEntity<UserRequestDto> updateUser(@Valid @RequestBody UserRequestDto userRequestDto,
                                                     @PathVariable("userId") Long userId)
    {
        UserRequestDto updatedUser = userService.updateUser(userRequestDto,userId);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") Long userId)
    {
        userService.deleteUser(userId);
        ApiResponseMessage message = ApiResponseMessage.builder()
                        .message("User Deleted Successfully")
                        .success(true).status(HttpStatus.OK)
                        .build();
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //getAll
    @GetMapping
    public ResponseEntity<List<UserRequestDto>> getAllUsers()
    {
        List<UserRequestDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
