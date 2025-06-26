package com.Api.controller;

import com.Api.Entity.User;
import com.Api.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@ImportAutoConfiguration(JacksonAutoConfiguration.class)
class UserControllerTest {

    @MockBean
    UserService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    User user;

    List<User> userList;
    @BeforeEach
    void setUp(){
        user = new User(1,"allan","093202004","allan@gmail.com");
        User user1 = new User("peter","981299736","peter@gmail.com");

        userList = Arrays.asList(user,user1 );


    }
    @AfterEach
    void  tearDown(){

    }


    @Test
    void findAll() throws Exception {
        when(service.findAll()).thenReturn(userList);

        this.mockMvc.perform(get("/user"))
                .andDo(print())
                .andExpect(status().isOk());


    }

    @Test
    void findById() throws Exception {
        when(service.findById(1)).thenReturn(user);

        this.mockMvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(jsonPath("$.name").value("allan"))
                .andExpect(status().isFound());
    }

    @Test
    void idNotFound() throws Exception {
        when(service.findById(any())).thenReturn(null);

        this.mockMvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }




    @Test
    void updateUser() throws Exception {

        when(service.updateById(any(User.class))).thenReturn(user);
        String userJson = mapper.writeValueAsString(user);
         this.mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                        .accept(MediaType.APPLICATION_JSON) )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("allan"));

}

   @Test
   void userNotFoundDuringUpdate() throws Exception {
        when(service.updateById(any(User.class))).thenReturn(null);

        this.mockMvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(status().isNotFound());

   }



    @Test
    void deleteById() throws Exception {
        doNothing().when(service).deleteById(any());

        this.mockMvc.perform(delete("/user/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void insertUser() throws Exception {
        when(service.insert(any(User.class))).thenReturn(user);

         mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);

        this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)
        ).andDo(print())
                .andExpect(jsonPath("$.name").value("allan"))
                .andExpect(status().isCreated());


    }
}