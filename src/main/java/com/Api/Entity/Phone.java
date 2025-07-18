package com.Api.Entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Phone implements Serializable {
   @NotBlank(message = "country code must not be blank")
   @Pattern(regexp = "[0-9]{3}", message = "Must be exactly 3 and specifically digits")
   private String countryCode;
   @NotBlank
    @Pattern(regexp = "[0-9]{9}", message = "Must be exactly 9 and specifically digits")
    private  String number;

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }


}
