package com.uuid.Uuid_Service.controller;

import com.uuid.Uuid_Service.dto.ResponseUuidDto;
import com.uuid.Uuid_Service.helper.ResponseBuilder;
import com.uuid.Uuid_Service.helper.ResponseWrapper;
import com.uuid.Uuid_Service.service.IdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uuid")
public class UiidGenerationController {

    @Autowired
    IdGeneratorService idGeneratorService;

    @GetMapping("/create")
   public ResponseEntity<ResponseWrapper<ResponseUuidDto>> generateUuid(@RequestParam String type){
     String uuid= idGeneratorService.generateId(type);
     ResponseUuidDto responseDto = ResponseUuidDto.builder()
             .id(uuid)
             .build();
        return ResponseBuilder.success("uuid generation successful",responseDto,HttpStatus.OK);
   }

}
