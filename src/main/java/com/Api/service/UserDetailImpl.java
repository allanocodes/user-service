package com.Api.service;

import com.Api.Dao.UserRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.Api.Entity.User;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailImpl implements UserDetailsService {

    @Autowired
    UserRepositories userRepositories;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> usersOptional = userRepositories.getUserByUsername(username);
        User appuser = usersOptional.orElseThrow(()-> new UsernameNotFoundException("username not found"));

        System.out.println(appuser);

        Set<String> roles = appuser.getUserRoles().stream().map(r->r.getRole_name())
                .collect(Collectors.toSet());

       return org.springframework.security.core.userdetails.User.builder()
                .password(appuser.getPassword())
                .username(appuser.getUsername())
                .roles(roles.toArray( new String[0])).build();



    }
}
