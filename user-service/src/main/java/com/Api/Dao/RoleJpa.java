package com.Api.Dao;

import com.Api.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpa extends JpaRepository<Role,Long> {
}
