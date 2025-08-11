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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/security")
@Tag(name = "User Security", description = "Endpoints for user management and authentication")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/find")
    @Tag(name = "Get ",description = "Gets All Users In the Database")
    public ResponseEntity<ResponceApi<List<DisplayDto>> > findAll() {
        List<DisplayDto> userDtos = userService.getAll();

        if(userDtos.size() == 0){
            return ResponseBuilder.sucess("No records found",userDtos);
        }

        return ResponseBuilder.sucess("users found",userDtos);

    }

    @PostMapping("/create")
    @Tag(name = "Post ",description = "Register users")
    public ResponseEntity<ResponceApi<DisplayDto>> insertUser( @Parameter(name = "userDto", description = "userdto containing user details",schema =
    @Schema(implementation = UserDto.class))   @RequestBody @Valid UserDto userDto){
        DisplayDto displayDto = userService.insertUser(userDto);

        if(displayDto != null){
            return  ResponseBuilder.sucess("user inserted",displayDto);
        }

        return ResponseBuilder.error("username exist",HttpStatus.NOT_ACCEPTABLE);


    }

    @GetMapping("/find/{id}")
    @Tag(name = "Get",description = "Gets All Users By Id")
    public ResponseEntity<ResponceApi<DisplayDto>> findById(@Parameter( name = "id", description = "Uuid String unique to every user",required = true)
                                                                @PathVariable("id") String id){
        DisplayDto dto = userService.getById(id);
        if(dto != null){
            return ResponseBuilder.sucess("User Found",dto);
        }

        return ResponseBuilder.error("User Not Found",HttpStatus.NOT_FOUND);
    }
    @GetMapping("/findBy/{username}")
    @Tag(name = "Get",description = "Get By Username")
    public ResponseEntity<ResponceApi<DisplayDto>> findByUsername(@Parameter (name = "username", description = "username",required = true) @PathVariable("username")  String username){
        DisplayDto dto = userService.getByUsername(username);
        if(dto != null){
            return ResponseBuilder.sucess("User Found",dto);
        }

        return ResponseBuilder.error("User Not Found",HttpStatus.NOT_FOUND);
    }
    @Tag(name = "update",description = "Update all users by id")
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponceApi<DisplayDto>> updateById(@Parameter(name = "id" ,description = "user uuid",required = true)  @PathVariable("id")  String id,@Parameter(name = "userdto"
   ,description = "json object containing user credentials", schema = @Schema(implementation = UserDto.class))
    @RequestBody @Valid UserDto dto){
        DisplayDto displayDto = userService.updateById(id,dto);
        if(displayDto != null){
            return ResponseBuilder.sucess("Record Updated",displayDto);

        }
        return ResponseBuilder.error("Id Does Not Exist",HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/delete/{id}")
    @Tag(name = "Delete",description = "Delete By Uuid String")
    public ResponseEntity<ResponceApi<String>> deleteById(  @Parameter(name = "id", description = "Uuid of the user",required = true) @PathVariable("id") String id){
        String message = userService.deleteById(id);
        return ResponseBuilder.sucess(message,null);
    }

    @PostMapping("/userLogin")
    @Tag(name = "Login",description = "login to system")
    public ResponseEntity<ApiLoginResponse> loginUser( @Parameter (name = "userLogin", description = "It a json object for login purposes",required = true,
    schema = @Schema(implementation = UserLogin.class)) @RequestBody  UserLogin userLogin  ) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String token = userService.loginuser(userLogin);

        if( token != null){
            return ResponseBuilder.loginSucess("Login Sucess",token);
        }

        return ResponseBuilder.loginError("Wrong credentials",HttpStatus.NOT_FOUND);

    }


}




