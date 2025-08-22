package com.email_service.dto;


import com.email_service.helper.phoneDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisplayDto implements Serializable {
    private String uuid;
    private String name;
    private String email;

    @JsonDeserialize(using = phoneDeserializer.class)
    Phone phone;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;


}
