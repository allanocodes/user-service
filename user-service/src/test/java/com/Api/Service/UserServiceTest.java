package com.Api.Service;

import com.Api.Dao.ProfileRepo;
import com.Api.Dao.UserRepositories;
import com.Api.Dto.DisplayDto;
import com.Api.Dto.MessageUser;
import com.Api.Dto.UserDto;
import com.Api.Dto.UserLogin;
import com.Api.Entity.Phone;
import com.Api.Entity.Role;
import com.Api.Entity.User;
import com.Api.Entity.UserProfile;
import com.Api.Exceptions.ResourceNotFoundException;
import com.Api.Exceptions.ServiceException;
import com.Api.Helpers.ResponseUuidDto;
import com.Api.Helpers.ResponseWrapper;
import com.Api.service.JwtService;
import com.Api.service.RabbitMqProducer;
import com.Api.service.UserService;
import com.Api.service.UuidInterface;
import com.mysql.cj.util.DnsSrv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class UserServiceTest {

    @Mock
    UserRepositories userRepositories;

    @Mock
    AuthenticationManager manager;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtService jwtService;

    @Mock
    MessageUser messageUser;

    @Mock
    ProfileRepo profileRepo;


    @Mock
    RabbitMqProducer producer;

    @InjectMocks
    UserService service;

    @Mock
    UuidInterface uuidInterface;

    AutoCloseable closeable;

    UserProfile profile;

    String uuid = "123e4567-e89b-12d3-a456-426614174000";
    User user;
    UserDto userDto;

    List<DisplayDto> displayDtoList = new ArrayList<>();
    List<User>  userList = new ArrayList<>();

    UserLogin login;

    @BeforeEach
    void setUp(){
        closeable =  MockitoAnnotations.openMocks(this);
        Phone phone = Phone.builder()
                .countryCode("254")
                .number("123456789")
                .build();

         profile = UserProfile.builder()
                .name("allan")
                .email("ndururiallan92gmail.com")
                .phone(phone).build();

        Role role = Role.builder()
                .role_name("USER").build();

        Set<Role> set = new HashSet<>();
        set.add(role);
         user = User.builder()
                 .id("123e4567-e89b-12d3-a456-426614174000")
                .username("allank")
                .password("1234@now")
                .userRoles(set)
                .profile(profile)
                .build();

         userDto = UserDto.builder()
                 .email("ndururiallan92gmail.com")
                 .password("1234@now")
                 .name("allan")
                 .phone(phone)
                 .username("allank")
                 .build();

         userList.add(user);


         DisplayDto  dto = DisplayDto.builder()
                 .email("ndururiallan92gmail.com")
                 .name("allan")
                 .phone(phone)
                 .build();

         login= UserLogin.builder().username("allank").password("1234").build();

    }


    @Test
    public void testInsertUser(){
        ResponseUuidDto responseUuidDto =ResponseUuidDto.builder().id("123e4567-e89b-12d3-a456-426614174000").build();
        ResponseWrapper<ResponseUuidDto>  wrapper = new ResponseWrapper<>("success","uuid sent",responseUuidDto);
        ResponseEntity<ResponseWrapper<ResponseUuidDto>> responseEntity= ResponseEntity.ok(wrapper);
       when(userRepositories.save(any(User.class))).thenReturn(user);
       when(passwordEncoder.encode(any())).thenReturn("1234@now");
       when(userRepositories.getUserByUsername(any())).thenReturn(Optional.empty());
       when(uuidInterface.generateUuid(any(String.class))).thenReturn(responseEntity);
       when(profileRepo.save(any(UserProfile.class))).thenReturn(profile);

       doNothing().when(producer).sendJsonMessage(messageUser);

       assertThat(service.insertUser(userDto).getEmail())
               .isEqualTo(user.getProfile().getEmail());
    }


    @Test
    public void testInsertExistingUser(){

            when(userRepositories.save(any(User.class))).thenReturn(user);
            when(passwordEncoder.encode(any())).thenReturn("1234@now");
            when(userRepositories.getUserByUsername(any())).thenReturn(Optional.ofNullable(user));
            doNothing().when(producer).sendJsonMessage(messageUser);

            assertThat(service.insertUser(userDto))
                    .isEqualTo(null);

    }

   @Test
    public void testGetAllUsers() {
        when(userRepositories.findAll()).thenReturn(userList);
        assertThat(service.getAll().get(0).getName()).isEqualTo("allan");

    }

    @Test
    public void testGetUsersWithNoUsers(){
        List<User> list = new ArrayList<>();
        when(userRepositories.findAll()).thenReturn(list);
        assertThat(service.getAll().size()).isEqualTo(0);
    }

   @Test
    public void testUserById(){
        when(userRepositories.findById(uuid)).thenReturn(Optional.ofNullable(user));
         assertThat(service.getById(uuid).getName()).isEqualTo("allan");
    }

    @Test
    public void testuserWhenIdDoesNotExist(){
        when(userRepositories.findById(any(String.class))).thenReturn(Optional.empty());
        assertThat(service.getById(uuid)).isEqualTo(null);
    }

    @Test
    public void testUpdateById(){
        when(userRepositories.findById(any(String.class))).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.encode("1234@now")).thenReturn("1234@now");
        when(userRepositories.save(any(User.class))).thenReturn(user);
        assertThat(service.updateById(uuid, userDto).getName()).isEqualTo("allan");

    }
   @Test
    public void testUpdateUnknownUserById(){
        when(userRepositories.findById(any(String.class))).thenReturn(Optional.empty());
        assertThat(service.updateById(uuid, userDto)).isEqualTo(null);
    }

    @Test
    public void testfindByUsername(){
        when(userRepositories.getUserByUsername(any(String.class)))
                .thenReturn(Optional.ofNullable(user));
        assertThat(service.getByUsername("allan").getEmail())
                .isEqualTo("ndururiallan92gmail.com");
    }


    @Test
    public void testFindByUnknownUsername(){
        when(userRepositories.getUserByUsername(any(String.class)))
                .thenReturn(Optional.empty());
        assertThat(service.getByUsername("unknown")).isEqualTo(null);
    }

  @Test
    public void testDeleteById(){
        doNothing().when(userRepositories).deleteById(any(String.class));
        assertThat(service.deleteById(uuid)).isEqualTo("Delete Success");
    }

    @Test
    public void testDeleteByIdWithUnkwownId(){
       doThrow(new ResourceNotFoundException("Id does not exist"))
               .when(userRepositories)
               .deleteById(any(String.class));

       assertThrows(ResourceNotFoundException.class,()->{
           service.deleteById(uuid);
       });
    }

    @Test
    public void testLoginByUsername() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Authentication authMock = mock(Authentication.class);
        String token ="jwt.auth.token";
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authMock);
        when(authMock.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(any(String.class))).thenReturn(token);
        doNothing().when(producer).sendJsonMessage(any(MessageUser.class));

        String actualtoken = service.loginuser(login);

        assertEquals(token,actualtoken);

        ArgumentCaptor<MessageUser>  messageCapture = ArgumentCaptor.forClass(MessageUser.class);
        verify(producer).sendJsonMessage(messageCapture.capture());

        MessageUser messageSent = messageCapture.getValue();
        assertEquals("allank",messageSent.getUsername());



    }

    @Test
    public void loginWithWrongCredentials() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Authentication authMock = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authMock);
        when(authMock.isAuthenticated()).thenReturn(false);

        assertNull(service.loginuser(login));

    }





    void tearDown() throws Exception {
        closeable.close();
    }

}