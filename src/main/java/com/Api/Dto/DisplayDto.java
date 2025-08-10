package com.Api.Dto;

import com.Api.Entity.Phone;
import com.Api.Helpers.PhoneSerializer;
import com.ctc.wstx.shaded.msv_core.util.StringPair;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.Valid;
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
    @JsonSerialize(using = PhoneSerializer.class)
    @Valid
    private Phone phone;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;

}
