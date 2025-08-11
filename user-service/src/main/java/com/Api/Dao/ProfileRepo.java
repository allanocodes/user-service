package com.Api.Dao;

import com.Api.Entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepo extends JpaRepository<UserProfile,String> {
}
