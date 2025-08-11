package com.Api.service;

import com.Api.Helpers.ResponseUuidDto;
import com.Api.Helpers.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("UUID-SERVICE")
public interface UuidInterface {
    @GetMapping("/uuid/create")
    public ResponseEntity<ResponseWrapper<ResponseUuidDto>> generateUuid(@RequestParam("type") String type);

}
