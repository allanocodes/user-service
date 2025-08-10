package com.sms_service.service;

import com.sms_service.Dto.Destinations;
import com.sms_service.Dto.SmsMessages;
import com.sms_service.Dto.SmsRequest;
import com.sms_service.Dto.SmsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SmsService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${infobip.api.url}")
    private String url;
    @Value("${infobip.api.key}")
    private String key;

    public void sendSms(SmsRequest request){

        Destinations destinations = new Destinations(request.getTo());
        SmsMessages messages = new SmsMessages(request.getFrom(), List.of(destinations),request.getText());
        SmsResponse requestbody = new SmsResponse(List.of(messages));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","App "+key);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SmsResponse> httpEntity = new HttpEntity<>(requestbody,headers);

        ResponseEntity<String> responseEntity =restTemplate.postForEntity(url,httpEntity,String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("SMS sent successfully: " + responseEntity.getBody());
        } else {
            System.err.println("Failed to send SMS: " + responseEntity.getStatusCode());
        }
    }

}
