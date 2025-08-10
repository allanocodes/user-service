package com.Api.controller;

import com.Api.Dto.DisplayDto;
import com.Api.Dto.UserDto;
import com.Api.Dto.UserLogin;
import com.Api.Entity.Phone;
import com.Api.Entity.User;
import com.Api.Exceptions.ResourceNotFoundException;
import com.Api.config.RabbitmqConfig;
import com.Api.config.SecurityConfig;
import com.Api.service.JwtService;
import com.Api.service.UserDetailImpl;
import com.Api.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.asm.Advice;
import org.assertj.core.util.Streams;
import org.hibernate.grammars.hql.HqlParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.print.DocFlavor;
import javax.security.auth.login.LoginContext;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@Import({SecurityConfig.class,GlobalControllerHandler.class, AuthControllerHandler.class})
class UserControllerTest {

    @MockBean
    UserService service;

    @MockBean
    private JwtService jwtService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserDetailImpl userDetailImpl;

    @Autowired
    ObjectMapper mapper;


    AutoCloseable closeable;

    UserDto userDto;
    DisplayDto displayDto;
    List<DisplayDto>  displayDtoList = new ArrayList<>();
    UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
   UserDto userDto1;
    UserLogin userLogin;

    @BeforeEach
    void setUp(){
     closeable = MockitoAnnotations.openMocks(this);

     Phone  phone = Phone.builder()
             .countryCode("254")
             .number("123456789")
             .build();


     userDto = UserDto.builder()
             .username("allank")
             .password("1234@now")
             .email("ndururiallan92@gmail.com")
             .name("allan")
             .phone(phone)
             .build();


        displayDto = DisplayDto.builder()
                .email("ndururiallan92@gmail.com")
                .name("allan")
                .phone(phone)
                .createdAt(LocalDateTime.now())
                .build();

        displayDtoList.add(displayDto);


        userLogin = UserLogin.builder()
                .password("1234@now")
                .username("allank")
                .build();


        Phone phone2 = Phone.builder()
                .countryCode("254")
                .number("123456789")
                .build();

         userDto1 = UserDto.builder()
                .name("allan")
                .password("1234@now")
                .email("ndururiallan92@gmail.com")
                .username("allank")
                .phone(phone2)
                .build();
    }
    @Test
     public void testInsertUserMethod() throws Exception {
        when(service.insertUser(any(UserDto.class))).thenReturn(displayDto);
        String jsonString = mapper.writeValueAsString(userDto);

        mockMvc.perform(post("/security/create").contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("allan"))
                .andExpect(jsonPath("$.data.email").value("ndururiallan92@gmail.com"));

     }

    @Test
    public void testInsertUserMethodWithExistingUsername() throws Exception {
        when(service.insertUser(any(UserDto.class))).thenReturn(null);
        String jsonString = mapper.writeValueAsString(userDto);

        mockMvc.perform(post("/security/create").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").value("username exist"));

    }
     @Test
     @WithMockUser(username = "allan")
     public void testGetAllUsers() throws Exception {
        when(service.getAll()).thenReturn(displayDtoList);

        mockMvc.perform(get("/security/find"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("allan"));

     }
     @Test
     @WithMockUser(username = "allan")
     public void testGetAllUsersWithNoUsers() throws Exception {
        List<DisplayDto> emptyList = new ArrayList<>();
        when(service.getAll()).thenReturn(emptyList);
         mockMvc.perform(get("/security/find"))
                 .andDo(print())
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.message").value("No records found"));

     }

     @Test
     @WithMockUser(username = "allan")
     public void testfindById() throws Exception {
       when(service.getById(any(String.class))).thenReturn(displayDto);
       mockMvc.perform(get("/security/find/" + uuid))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data.name").value("allan"));
     }
    @Test
    @WithMockUser(username = "allan")
    public void testfindByIdWithUnknownId() throws Exception {
        when(service.getById(any(String.class))).thenReturn(null);
        mockMvc.perform(get("/security/find/" + uuid))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User Not Found"));
    }

    @Test
    @WithMockUser(username = "allan")
    public void testfindByUsername() throws Exception {
        when(service.getByUsername(any(String.class))).thenReturn(displayDto);
        mockMvc.perform(get("/security/findBy/allank"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("allan"));
    }

    @Test
    @WithMockUser(username = "allan")
    public void testfindByUsernameWithUnknownUsername() throws Exception {
        when(service.getByUsername(any(String.class))).thenReturn(null);
        mockMvc.perform(get("/security/findBy/allank"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User Not Found"));
    }
    @Test
    @WithMockUser(username = "allan")
    public void testUpdateById() throws Exception {
        when(service.updateById(any(String.class),any(UserDto.class))).thenReturn(displayDto);
        String jsonString = mapper.writeValueAsString(userDto);

         mockMvc.perform(put("/security/update/"+uuid)
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(jsonString)
                 )
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.data.name").value("allan"));
    }

    @Test
    @WithMockUser(username = "allan")
    public void testUpdateByIdWithUnknownId() throws Exception {
        when(service.updateById(any(String.class),any(UserDto.class))).thenReturn(null);
        String jsonString = mapper.writeValueAsString(userDto);

        mockMvc.perform(put("/security/update/"+uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Id Does Not Exist"));
    }
    @Test
    @WithMockUser(username = "allan")
    public void testDeleteByUsername() throws Exception {
       when(service.deleteById(any(String.class))) .thenReturn("Delete Success");

       mockMvc.perform(delete(("/security/delete/"+ uuid))).andDo(print())
               .andExpect(jsonPath("$.message").value("Delete Success"));
    }

    @Test
    public void testLoginuser() throws Exception {
        String token = "login.jwt.token";
        when(service.loginuser(any(UserLogin.class))).thenReturn(token);
        String jsonString = mapper.writeValueAsString(userLogin);
        mockMvc.perform(post("/security/userLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("login.jwt.token"));

    }
    @Test
    public void testLoginuserWithUnknownUsername() throws Exception {
        String token = "login.jwt.token";
        when(service.loginuser(any(UserLogin.class))).thenReturn(null);
        String jsonString = mapper.writeValueAsString(userLogin);
        mockMvc.perform(post("/security/userLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Wrong credentials"));

    }

    @Test
    @WithMockUser(username = "allan")
    public void testdeleteByIdWhereIdIsNotFound() throws Exception {
     when(service.deleteById(any())).thenThrow( new ResourceNotFoundException("id not found"));

        mockMvc.perform(delete(("/security/delete/"+ uuid))).andDo(print())
                .andExpect(jsonPath("$.message").value("Record Does Not Exist"));
    }

    @Test
    public void testMissingAllField() throws Exception{

        UserDto userDto1 = new UserDto();
        String jsonString = mapper.writeValueAsString(userDto1);

        mockMvc.perform(post("/security/create")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andDo(print())
                .andExpect(jsonPath("$.errors.username")
                        .value("username is required"))
                .andExpect(jsonPath("$.errors.password")
                        .value("Password is required"))
                .andExpect(jsonPath("$.errors.name")
                .value("name is required"))
                .andExpect(jsonPath("$.errors.email")
                        .value("Email is Required"));


    }




    @Test
    public void testPhoneMissingField() throws Exception{

        UserDto userDto1 = UserDto.builder()
                .name("allan")
                .password("allank@now2")
                .email("ndururiallan92@gmail.com")
                .username("allank")
                .build();


        String jsonString = mapper.writeValueAsString(userDto1);

        mockMvc.perform(post("/security/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print())
                .andExpect(jsonPath("$.errors.phone").value("Phone is required"));

    }


    @Test
    public void testInvalidUsername() throws Exception {
        UserDto userDto1 = UserDto.builder()
                .name("allan")
                .password("allank@now2")
                .email("ndururiallan92@gmail.com")
                .username("all")
                .build();
        String jsonString = mapper.writeValueAsString(userDto1);

        mockMvc.perform(post("/security/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print())
                .andExpect(jsonPath("$.errors.username").value("Username should have between 5 to 10 character"));

    }

    @Test
    public void testInvalidUsernameWithMoreCharacters() throws Exception {
        UserDto userDto1 = UserDto.builder()
                .name("allan")
                .password("allank@now2")
                .email("ndururiallan92@gmail.com")
                .username("allkksjjsjskskk")
                .build();
        String jsonString = mapper.writeValueAsString(userDto1);

        mockMvc.perform(post("/security/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print())
                .andExpect(jsonPath("$.errors.username").value("Username should have between 5 to 10 character"));

    }

    @ParameterizedTest
    @MethodSource("invalidPasswords")
    public void testInvalidPassword(String password,Integer expectedCode) throws Exception {
       userDto1.setPassword(password);

        if(expectedCode == 200){
            when(service.insertUser(any(UserDto.class))).thenReturn(displayDto);
        }


        String jsonString = mapper.writeValueAsString(userDto1);

        mockMvc.perform(post("/security/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString)).andDo(print())
                .andExpect(status().is(expectedCode));

    }

     static Stream<Arguments> invalidPasswords(){
        return Stream.of(Arguments.of("NoNumber",400),
                Arguments.of("NoSpecial123",400),
                Arguments.of("short",400),
                   Arguments.of("correct123@",200));
     }


     @ParameterizedTest
     @MethodSource("invalidNames")
     public void testInvalidName(String username,Integer expectedCode) throws Exception {
        userDto1.setUsername(username);

         if(expectedCode == 200){
             when(service.insertUser(any(UserDto.class))).thenReturn(displayDto);
         }
         String jsonString = mapper.writeValueAsString(userDto1);
         mockMvc.perform(post("/security/create")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(jsonString)
         ).andDo(print())
                 .andExpect(status().is(expectedCode));



     }



     public static Stream<Arguments> invalidNames(){
        return Stream.of(
            Arguments.of("al",400)    ,
                Arguments.of("all12345678901234567890",400),
                Arguments.of("allank",200)
        );
     }

     @ParameterizedTest
     @MethodSource("invalidEmails")
     public void testEmailInput(String email,Integer expectedCode) throws Exception {

        userDto1.setEmail(email);

        if(expectedCode == 200){
            when(service.insertUser(any(UserDto.class))).thenReturn(displayDto);
        }
        String jsonString = mapper.writeValueAsString(userDto1);

        mockMvc.perform(post("/security/create").contentType(MediaType.APPLICATION_JSON)
                .content(jsonString)).andDo(print())
                .andExpect(status().is(expectedCode));


     }

     public static Stream<Arguments>  invalidEmails(){
        return Stream.of(
                Arguments.of("email",400),
                Arguments.of("email@com",400),
                Arguments.of("email@gmail",400),
                Arguments.of("email.com",400),
                Arguments.of("email@gmail.com",200)
        );
     }

     @ParameterizedTest
     @MethodSource("phoneFormats")
     public void testPhoneInputFormats(String phoneFormat,Integer expectedCode) throws Exception {
         String json =  String.format("""
        {
            "name": "Allan",
            "username": "allan1234",
            "password": "Valid1234!",
            "email": "allan@example.com",
            "phone": "%s" 
        }
    """,phoneFormat);

         if(expectedCode == 200){
             when(service.insertUser(any(UserDto.class))).thenReturn(displayDto);
         }


         mockMvc.perform(post("/security/create").contentType(MediaType.APPLICATION_JSON)
                         .content(json)).andDo(print())
                 .andExpect(status().is(expectedCode));


     }

     public static Stream<Arguments>  phoneFormats(){
         return Stream.of(
                 Arguments.of("+254-123456789",200),
                 Arguments.of("254-123456789",400),
                 Arguments.of("+254123456789",400),
                 Arguments.of("234123456789",400),
                 Arguments.of("+254-12345678",400),
                 Arguments.of("+254-1234567890",400)
         );

     }




        @Test
        void whenWrongPassword_thenReturnUnauthorized() throws Exception {
            String json = """
        {
            "username": "allan",
            "password": "wrongPassword"
        }
        """;


            when(service.loginuser(any())).thenThrow(new BadCredentialsException("Invalid password"));

            mockMvc.perform(post("/security/userLogin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value("Wrong Password"));
        }

        @Test
        void whenUnknownUsername_thenReturnUnauthorized() throws Exception {
            String json = """
        {
            "username": "nonexistent",
            "password": "anything"
        }
        """;

            when(service.loginuser(any())).thenThrow(new UsernameNotFoundException("Not found"));

            mockMvc.perform(post("/security/userLogin")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value("error"))
                    .andExpect(jsonPath("$.message").value("Username Not Found"));
        }







    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }




}