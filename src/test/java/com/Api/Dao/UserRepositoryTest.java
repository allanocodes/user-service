package com.Api.Dao;


import com.Api.Entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepositories userRepositories;
    User user;
    @BeforeEach
    void setUp(){
    user = new User("allan","098023004620","allan@gmail.com");
    userRepositories.save(user);
    }

    @Test
    void testGetByEmail(){
         Optional<User> user1 = userRepositories.getUserByEmail("allan@gmail.com");
         assertThat(user1.get().getName()).isEqualTo("allan");
    }

    @Test
    void emailNotFound(){
        Optional<User> user1 = userRepositories.getUserByEmail("allan78@gmail.com");
        assertThat(user1).isEmpty();
    }




   @AfterEach
    void tearDown(){
        userRepositories.delete(user);
        user = null;


    }





}
