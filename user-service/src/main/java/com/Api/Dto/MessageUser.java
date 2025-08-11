package com.Api.Dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageUser {
    private String username;
    private String email;
    private String subject;
    private String text;
}
