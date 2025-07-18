package com.Api.Dao;


import com.Api.Entity.Phone;
import com.Api.Entity.Role;
import com.Api.Entity.User;
import com.Api.Entity.UserProfile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepositories userRepositories;

    @Autowired
    RoleJpa roleJpa;



    @BeforeEach
    void setUp(){
        Phone phone = Phone.builder()
                .countryCode("254")
                .number("123456789")
                .build();

        UserProfile profile = UserProfile.builder()
                .name("allan")
                .email("ndururiallan92gmail.com")
                .phone(phone).build();

        Role role = Role.builder()
                .role_name("USER").build();

        roleJpa.save(role);

        Set<Role> set = new HashSet<>();
        set.add(role);
       User user = User.builder()
                .username("allank")
                .password("1234@now")
                .userRoles(set)
               .profile(profile)
                .build();

        userRepositories.save(user);
    }

    @Test
    public void testGetByUsername(){

        assertThat(userRepositories.getUserByUsername("allank").get().getPassword()).isEqualTo("1234@now");
    }
    @Test
    public void testGetByUnknownUsername(){
        assertThat(userRepositories.getUserByUsername("unknown")).isEmpty();
    }




}
