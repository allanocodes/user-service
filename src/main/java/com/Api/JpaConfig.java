package com.Api;

import com.Api.Dao.RoleJpa;
import com.Api.Entity.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Autowired
    RoleJpa roleJpa;

    @PostConstruct
    public void configure(){
        Role role = Role.builder().role_name("USER").build();
        roleJpa.save(role);
    }

}
