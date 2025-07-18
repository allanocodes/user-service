package com.Api.controller;

import com.Api.Dto.DisplayDto;
import com.Api.Dto.MessageUser;
import com.Api.Dto.UserDto;
import com.Api.Dto.UserLogin;
import com.Api.Entity.User;
import com.Api.Helpers.ApiLoginResponse;
import com.Api.Helpers.ResponceApi;
import com.Api.Helpers.ResponseBuilder;
import com.Api.service.RabbitMqProducer;
import com.Api.service.UserService;
import jakarta.validation.Valid;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user")
    public ResponseEntity<ResponceApi<List<DisplayDto>> > findAll() {
        List<DisplayDto> userDtos = userService.getAll();

        if(userDtos.size() == 0){
            return ResponseBuilder.sucess("No records found",userDtos);
        }

        return ResponseBuilder.sucess("users found",userDtos);

    }

    @PostMapping("/userSignUp")
    public ResponseEntity<ResponceApi<DisplayDto>> insertUser(@RequestBody @Valid UserDto userDto){
        DisplayDto displayDto = userService.insertUser(userDto);

        if(displayDto != null){
            return  ResponseBuilder.sucess("user inserted",displayDto);
        }

        return ResponseBuilder.error("username exist",HttpStatus.NOT_ACCEPTABLE);


    }

    @GetMapping("/user/api/{id}")
    public ResponseEntity<ResponceApi<DisplayDto>> findById(@PathVariable UUID id){
        DisplayDto dto = userService.getById(id);
        if(dto != null){
            return ResponseBuilder.sucess("User Found",dto);
        }

        return ResponseBuilder.error("User Not Found",HttpStatus.NOT_FOUND);
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<ResponceApi<DisplayDto>> findByUsername(@PathVariable  String username){
        DisplayDto dto = userService.getByUsername(username);
        if(dto != null){
            return ResponseBuilder.sucess("User Found",dto);
        }

        return ResponseBuilder.error("User Not Found",HttpStatus.NOT_FOUND);
    }
    @PutMapping("/user/{id}")
    public ResponseEntity<ResponceApi<DisplayDto>> updateById(@PathVariable  UUID id,@RequestBody @Valid UserDto dto){
        DisplayDto displayDto = userService.updateById(id,dto);
        if(displayDto != null){
            return ResponseBuilder.sucess("Record Updated",displayDto);

        }
        return ResponseBuilder.error("Id Does Not Exist",HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/user/{id}")
    public ResponseEntity<ResponceApi<String>> deleteById(@PathVariable UUID id){
        String message = userService.deleteById(id);
        return ResponseBuilder.sucess(message,null);
    }

    @PostMapping("/userLogin")
    public ResponseEntity<ApiLoginResponse> loginUser(@RequestBody  UserLogin userLogin){
        String token = userService.loginuser(userLogin);

        if( token != null){
            return ResponseBuilder.loginSucess("Login Sucess",token);
        }

        return ResponseBuilder.loginError("Wrong credentials",HttpStatus.NOT_FOUND);

    }


}




