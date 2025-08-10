package com.email_service.service;

import com.email_service.dto.DisplayDto;
import com.email_service.helper.ResponceApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "security-service")
public interface SecurityInterface {
    @GetMapping("/security/findBy/{username}")
    public ResponseEntity<ResponceApi<DisplayDto>> findByUsername(@PathVariable String username);

}