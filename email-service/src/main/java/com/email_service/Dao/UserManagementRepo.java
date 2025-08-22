package com.email_service.Dao;

import com.email_service.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserManagementRepo extends JpaRepository<User,String> {

    @Query("from User where username =:username")
    public Optional<User> getUserByUsername(@Param("username") String username);
}
