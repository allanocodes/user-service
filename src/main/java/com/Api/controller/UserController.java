package com.Api.controller;

import com.Api.Entity.User;
import com.Api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/user")
    public ResponseEntity<List<User>> findAll() {
        List<User> userList = userService.findAll();
        if(userList.size() != 0){
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
        return new ResponseEntity<>(userList, HttpStatus.NO_CONTENT);

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> findById(@PathVariable Integer id) {
        User user = userService.findById(id);
        if(user != null)
        {
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        }
        else{
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user,@PathVariable Integer id) {
        user.setId(id);
      User user1 = userService.updateById(user);
      if(user1 != null){
          return  new ResponseEntity<>(user1,HttpStatus.CREATED) ;
      }
        return  new ResponseEntity<>(user1,HttpStatus.NOT_FOUND) ;

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteById(@PathVariable Integer id) {
        userService.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

@PostMapping("/user")
public ResponseEntity<User> insertUser(@RequestBody  User user){

    return new ResponseEntity<>( userService.insert(user),HttpStatus.CREATED);
}




}




