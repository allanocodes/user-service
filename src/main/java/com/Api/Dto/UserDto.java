package com.Api.Dto;

import com.Api.Entity.Phone;
import com.Api.Helpers.PhoneSerializer;
import com.Api.Helpers.phoneDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @Size(max = 10,min = 5,message = "Username should have between 5 to 10 character")
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must be at least 8 characters and include letters, numbers, and a special character"
    )
    private String password;
    @Size(max = 20,min = 2,message = "Username should have between 2 to 20 character")
    @NotBlank(message = "name is required")
    private String name;
    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email Format")
    @Pattern(regexp = ".+@.+\\.com", message = "Only .com emails are allowed")
    private String email;
    @JsonSerialize(using = PhoneSerializer.class)
   @JsonDeserialize(using = phoneDeserializer.class)
    @Valid
    @NotNull(message = "Phone is required")
    private Phone phone;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Phone getPhone() {
        return this.phone;
    }

    public void setPhone(final Phone phone) {
        this.phone = phone;
    }
}

