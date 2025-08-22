package com.email_service.Dao;


import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepo extends JpaRepository<com.email_service.dto.UserProfile,String> {
}
