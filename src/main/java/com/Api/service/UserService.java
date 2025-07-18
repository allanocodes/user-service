package com.Api.service;

import com.Api.Dao.UserRepositories;
import com.Api.Dto.DisplayDto;
import com.Api.Dto.MessageUser;
import com.Api.Dto.UserDto;
import com.Api.Dto.UserLogin;
import com.Api.Entity.Role;
import com.Api.Entity.User;

import com.Api.Entity.UserProfile;
import com.Api.Exceptions.ResourceNotFoundException;
import com.Api.Exceptions.ServiceException;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepositories userRepositories;
   @Autowired
    PasswordEncoder passwordEncoder;

   @Autowired
   AuthenticationManager authenticationManager;

   @Autowired
   JwtService jwtService;

    @Autowired
    RabbitMqProducer producer;

    public DisplayDto insertUser(UserDto userDto){

    Optional<User> userOptional = userRepositories.getUserByUsername(userDto.getUsername());
        User user2 = userOptional.orElseGet(()->{
            return null;
        });

        if(user2 != null){
            return null;
        }

        MessageUser messageUser = MessageUser.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .subject("User " + userDto.getUsername() + " created")
                .text("User " + userDto.getUsername() + " created at " + new Date().getTime())
                .build();


        Role role = Role.builder().id(1l)
                .build();
        UserProfile profile = UserProfile.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .build();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .userRoles(userRoles)
                .profile(profile)
                .build();
     User user1 =   userRepositories.save(user);

     DisplayDto dto = DisplayDto.builder()
             .uuid(user1.getId())
             .name(user1.getProfile().getName())
             .email(user1.getProfile().getEmail())
             .phone(user1.getProfile().getPhone())
             .lastModified(user.getProfile().getUpdatedAt())
             .createdAt(user.getProfile().getCreatedAt())
             .build();

     producer.sendJsonMessage(messageUser);

     return dto;

    }

    public List<DisplayDto> getAll(){
        List<User> userList = userRepositories.findAll();
    List<DisplayDto>  dtoList =userList.stream().map(user -> {
            DisplayDto displayDto = DisplayDto.builder()
                    .uuid(user.getId())
                    .name(user.getProfile().getName())
                    .email(user.getProfile().getEmail())
                    .phone(user.getProfile().getPhone())
                    .lastModified(user.getProfile().getUpdatedAt())
                    .createdAt(user.getProfile().getCreatedAt())
                    .build();

            return displayDto;
        }).collect(Collectors.toList());

        return dtoList;
    }


    @Cacheable(value = "user",key = "#id", unless = "#result == null")
    public DisplayDto getById(UUID id){
     Optional<User>  userOptional =  userRepositories.findById(id);
     User user = userOptional.orElseGet(()->null);

     if(user != null){
       DisplayDto dto = DisplayDto.builder()
               .name(user.getProfile().getName())
               .uuid(user.getId())
               .email(user.getProfile().getEmail())
               .phone(user.getProfile().getPhone())
               .lastModified(user.getProfile().getUpdatedAt())
               .createdAt(user.getProfile().getCreatedAt())
               .build();

       return dto;

     }

     return null;

    }

    public DisplayDto getByUsername(String username){
        Optional<User>  userOptional =  userRepositories.getUserByUsername(username);
        User user = userOptional.orElseGet(()->{
            return null;
        });

        if(user != null){
            DisplayDto dto = DisplayDto.builder()
                    .name(user.getProfile().getName())
                    .uuid(user.getId())
                    .email(user.getProfile().getEmail())
                    .phone(user.getProfile().getPhone())
                    .lastModified(user.getProfile().getUpdatedAt())
                    .createdAt(user.getProfile().getCreatedAt())
                    .build();

            return dto;

        }

        return null;

    }

   @CachePut(value = "user" ,key = "#id",unless = "result == null")
    public DisplayDto updateById(UUID id,UserDto userDto){
      Optional<User> userOptional = userRepositories.findById(id);

      User user2 = userOptional.orElseGet(()->{
          return null;
      });

      if(user2 == null){
          return null;
      }
        Role role = Role.builder().id(1l)
                .build();
        // Update the profile
        UserProfile profile = user2.getProfile();
        if (profile == null) {
            profile = new UserProfile();
        }
        profile.setName(userDto.getName());
        profile.setEmail(userDto.getEmail());
        profile.setPhone(userDto.getPhone());

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);

        user2.setUsername(userDto.getUsername());
        user2.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user2.setUserRoles(userRoles);

        // Set updated profile
        user2.setProfile(profile);

        User user1 =   userRepositories.save(user2);

        DisplayDto dto = DisplayDto.builder()
                .uuid(user1.getId())
                .name(user1.getProfile().getName())
                .email(user1.getProfile().getEmail())
                .phone(user1.getProfile().getPhone())
                .lastModified(user1.getProfile().getUpdatedAt())
                .createdAt(user1.getProfile().getCreatedAt())
                .build();

        return dto;



    }
    @CacheEvict(value = "user",key = "#id")
    public String deleteById(UUID id){

        try{
            userRepositories.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw  new ResourceNotFoundException("Username not found");
        }

        return "Delete Success";

    }

   public String loginuser(UserLogin userLogin){
       Authentication authentication = authenticationManager.
               authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(),
                       userLogin.getPassword()));

       if(authentication.isAuthenticated()){
           MessageUser messageUser = MessageUser.builder()
                   .username(userLogin.getUsername())
                   .subject("Login Alert!")
                   .text("You credential were used for login! If its not contact support")
                   .build();


           producer.sendJsonMessage(messageUser);
           return jwtService.generateToken(userLogin.getUsername());
       }

       return  null;
   }



}
