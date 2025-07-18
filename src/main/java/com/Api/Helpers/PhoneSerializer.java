package com.Api.Helpers;

import com.Api.Entity.Phone;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.redis.connection.stream.StreamInfo;

import java.io.IOException;

public class PhoneSerializer extends JsonSerializer<Phone> {
    @Override
    public void serialize(Phone value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String result = "+" + value.getCountryCode() + "-" + value.getNumber();
        gen.writeString(result);
    }
}
