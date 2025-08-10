package com.email_service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserProfile implements Serializable {

    private Integer id;
    private String name;
    private String email;

    private Phone phone;


    private LocalDateTime  createdAt  ;

    private LocalDateTime  updatedAt ;

    private String createdBy;
    private String updatedBy;


}
