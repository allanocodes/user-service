package com.Api.Dao;


import com.Api.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositories extends JpaRepository<User,Integer> {
    @Query("from User where email = :email")
    public Optional<User> getUserByEmail(String email);

}
