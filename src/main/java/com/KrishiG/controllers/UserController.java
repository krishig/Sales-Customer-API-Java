package com.KrishiG.controllers;

import com.KrishiG.dtos.request.UserRequestDto;
import com.KrishiG.responsesApiMessages.ApiResponseMessage;
import com.KrishiG.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
@Tag(name="UserController",description = "APIs for UserController!!")
public class UserController {

    @Autowired
    private UserService userService;

    //create
    @PostMapping
    @Operation(summary = "Create new user ", description = "API for create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Success !! | Ok"),
            @ApiResponse(responseCode = "401", description = "Not Authorized !!"),
            @ApiResponse(responseCode = "201", description = "Created !!")

    })
    public ResponseEntity<UserRequestDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto)
    {
         UserRequestDto user = userService.createUser(userRequestDto);
         return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{userId}")
    @Operation(summary = "Update user ", description = "API for update user")
    public ResponseEntity<UserRequestDto> updateUser(@Valid @RequestBody UserRequestDto userRequestDto,
                                                     @PathVariable("userId") Long userId)
    {
        UserRequestDto updatedUser = userService.updateUser(userRequestDto,userId);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user ", description = "API for delete user")
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
    @Operation(summary = "Get all users ", description = "API for get all users")
    public ResponseEntity<List<UserRequestDto>> getAllUsers()
    {
        List<UserRequestDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
