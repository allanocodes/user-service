package com.email_service.helper;


import com.email_service.dto.Phone;
import com.email_service.exception.WrongPhoneFormat;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class phoneDeserializer extends JsonDeserializer<Phone> {

    @Override
    public Phone deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
       String text = p.getText().trim();

      if(!text.matches("^\\+[0-9]{3}-[0-9]{9}$")){
          throw new WrongPhoneFormat("wrong phone format");
      }

        Phone phone = null;
      //not necessary because the pattern handles it
       if(text.startsWith("+")){
        text = text.substring(1);
       }
        String[] parts = text.split("-");


        if (parts.length != 2) {
            throw new WrongPhoneFormat("Phone number format is invalid");
        }

       if(parts.length == 2){
           phone = Phone.builder()
                   .countryCode(parts[0])
                   .number(parts[1])
                   .build();
       }


        return phone;
    }
}
