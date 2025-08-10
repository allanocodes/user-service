package com.Api.Dao;


import com.Api.Dto.UserDto;
import com.Api.Entity.User;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepositories extends JpaRepository<User, String> {

    @Query("from User where username =:username")
    public Optional<User> getUserByUsername(@Param("username") String username);

}
