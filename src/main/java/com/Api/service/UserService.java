package com.Api.service;

import com.Api.Dao.UserRepositories;
import com.Api.Entity.User;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepositories userRepositories;

    public List<User> findAll(){
        return userRepositories.findAll();
    }

    @Cacheable(value ="user",key = "#id",condition = "#id != null")
    public User findById(Integer id){
        return  userRepositories.findById(id).orElseGet(()-> new User());
    }


    public User insert(User user){

        return userRepositories.save(user);
    }
    @CacheEvict(value = "user", key="#id")
    public void deleteById(Integer id){
         userRepositories.deleteById(id);
    }
    @CachePut(value = "user", key = "#user.id")
    public User updateById(User user){
        if(userRepositories.findById(user.getId()).isPresent()){

            return  userRepositories.save(user);
        }
       else {
            return  null;
        }

    }
    public User findByEmail(String email){
        return userRepositories.getUserByEmail(email).orElseGet(()->new User());
    }


}
