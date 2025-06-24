package com.Api.Service;

import com.Api.Dao.UserRepositories;
import com.Api.Entity.User;
import com.Api.service.UserService;
import com.mysql.cj.util.DnsSrv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UserServiceTest {
    @Mock
    UserRepositories userRepositories;
   @InjectMocks
    UserService service;

   List<User> userList = new ArrayList<>();
   User user;
   AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        user = new User(1,"allan","093202004","allan@gmail.com");
        User user1 = new User("peter","981299736","peter@gmail.com");

         userList.add(user);
         userList.add(user1);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void findAll() {
        when( userRepositories.findAll()).thenReturn(userList);
        assertThat(service.findAll().get(0).getName()).isEqualTo("allan");
    }

    @Test
    void findById() {
     when(userRepositories.findById(any())).thenReturn(Optional.ofNullable(user));
     assertThat(service.findById(1).getName()).isEqualTo("allan");
    }

    @Test
    void insert() {
    when(userRepositories.save(any())).thenReturn(user);
    assertThat(service.insert(user).getName()).isEqualTo("allan");
    }

    @Test
    void deleteById() {
        doNothing().when(userRepositories).delete(any());
        service.deleteById(user.getId());
        verify(userRepositories).deleteById(any());
    }

    @Test
    void updateById() {
        when(userRepositories.findById(1)).thenReturn(Optional.ofNullable(user));
        when(userRepositories.save(user)).thenReturn(user);
        assertThat(service.updateById(user).getName()).isEqualTo("allan");
    }

    @Test
    void findByEmail() {
        when(userRepositories.getUserByEmail(any())).thenReturn(Optional.ofNullable(user));
        assertThat(service.findByEmail("allan@gmail.com").getName()).isEqualTo("allan");
    }
}