package com.Api.filters;

import com.Api.service.JwtService;
import com.Api.service.UserDetailImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @InjectMocks
    JwtFilter jwtFilter;

    @Mock
    JwtService jwtService;

    @Mock
    UserDetailImpl userDetailImpl;

    @Mock
    FilterChain filterChain;

    @Mock
    UserDetails userDetails;


    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
   void shouldAuthenticatedWhenTokenIsValid() throws ServletException, IOException {
       String token= "spring.jwt.token";
       String username = "allan";

       MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
       mockHttpServletRequest.addHeader("Authorization","Bearer " + token);
       MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();

       when(jwtService.extractUsername(any(String.class))).thenReturn(username);
       when(jwtService.validateToken(any(String.class))).thenReturn(true);
       when( userDetailImpl.loadUserByUsername(any(String.class))).thenReturn(userDetails);
       when( userDetails.getAuthorities()).thenReturn(new ArrayList<>());


       jwtFilter.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, filterChain);

       assertNotNull(SecurityContextHolder.getContext().getAuthentication());
       verify(filterChain, times(1)).doFilter(mockHttpServletRequest, mockHttpServletResponse);


   }


}