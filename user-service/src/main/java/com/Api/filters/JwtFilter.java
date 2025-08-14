package com.Api.filters;

import com.Api.Entity.User;
import com.Api.Exceptions.InvalidTokenException;
import com.Api.Helpers.ResponseErrorApi;
import com.Api.service.JwtService;
import com.Api.service.UserDetailImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailImpl userDetailImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        String username =null;
        String token = "";

        try {

        if(header != null && header.startsWith("Bearer ")){
            token = header.substring(7);
            username = jwtService.extractUsername(token);

        }
        if(username != null){
            UserDetails user = userDetailImpl.loadUserByUsername(username);
            if(jwtService.validateToken(token)){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }

        filterChain.doFilter(request,response);


        } catch (InvalidTokenException ex) {
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           response.setContentType("application/json");

           String message = "";
           Throwable cause = ex.getCause();


            if (cause instanceof ExpiredJwtException) message = "Token has expired";
            else if (cause instanceof SignatureException) message = "Token signature is invalid";
            else if (cause instanceof MalformedJwtException) message = "Token is malformed";
            else if (cause instanceof UnsupportedJwtException) message = "Token type is unsupported";
            else if (cause instanceof IllegalArgumentException) message = "Token is null or empty";


            response.getWriter().write(

                    new ObjectMapper().writeValueAsString( new ResponseErrorApi<>("error",message,null))
            );

        }


    }
}
