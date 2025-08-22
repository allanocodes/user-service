package com.payment_service.service;

import com.payment_service.Dao.PaymentRepo;
import com.payment_service.Dto.StkCallbackResponse;
import com.payment_service.Dto.StkPushResponse;
import com.payment_service.Entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionMessage;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentService {


    RestTemplate restTemplate = new RestTemplate();

    @Value("${mpesa.consumerKey}")
    String consumerKey;

    @Value("${mpesa.consumerSecret}")
    String consumerSecret;

    @Value("${mpesa.authUrl}")
    String authUrl;

    @Value("${mpesa.shortCode}")
    String shortCode;

    @Value("${mpesa.passCode}")
    String passKey;

    @Value("${mpesa.stkpushurl}")
    String stkUrl;

    @Autowired
    PaymentRepo repo;

    @Value("${mpesa.callbackurl}")
    String callBackUrl;

    public String getToken(){
        String auth = consumerKey + ":" + consumerSecret;
        String encodedString = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedString);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response =
        restTemplate.exchange(authUrl, HttpMethod.GET,httpEntity, Map.class);

        return (String) response.getBody().get("access_token");
    }

    public Payment stkPushRequest(String phone, Double amount, String desc, String accountRef){

        String timestamp= new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String password = Base64.getEncoder().encodeToString((shortCode+passKey+timestamp).getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String,Object> payLoad = new HashMap<>();
        payLoad.put("Timestamp",timestamp);
        payLoad.put("PartyA",phone);
        payLoad.put("PartyB",shortCode);
        payLoad.put("Amount",amount);
        payLoad.put("Password",password);
        payLoad.put("BusinessShortCode",shortCode);
        payLoad.put("PhoneNumber",phone);
        payLoad.put("TransactionType","CustomerPayBillOnline");
        payLoad.put("AccountReference",accountRef != null? accountRef:"Ref");
        payLoad.put("TransactionDesc",desc != null ? desc : "transaction");
        payLoad.put("CallBackURL",callBackUrl);

        HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<>(payLoad,headers);
        ResponseEntity response = restTemplate.postForEntity(stkUrl,httpEntity,StkPushResponse.class);

        StkPushResponse stkPushResponse = (StkPushResponse) response.getBody();

        Payment payment = Payment.builder()
                .amount(amount)
                .phoneNumber(phone)
                .merchantRequestId(stkPushResponse.getMerchantRequestID())
                .checkoutRequestId(stkPushResponse.getCheckoutRequestID())
                .localDateTime(LocalDateTime.now())
                .status("Pending")
                .build();

        repo.save(payment);


        return payment;




    }

    public void callBack(StkCallbackResponse callbackResponse){
        String checkoutRequestID = callbackResponse.getBody().getStkCallback().getCheckoutRequestID();


        Payment payment = repo.findByCheckoutID(checkoutRequestID).orElseGet(null);

        if(payment != null){
         payment.setResultCode(callbackResponse.getBody().getStkCallback().getResultCode());
         payment.setResultDescription(callbackResponse.getBody().getStkCallback().getResultDesc());

         int resultCode=callbackResponse.getBody().getStkCallback().getResultCode();
         if(resultCode == 0){
             payment.setStatus("success");

         }
         else{
             payment.setStatus("failed");
         }
         for(StkCallbackResponse.Item item: callbackResponse.getBody().getStkCallback().getCallbackMetadata().getItem()){
             switch (item.getName()){
                 case "MpesaReceiptNumber"-> payment.setMpesaReceiptNumber((String) item.getValue());
                 case "Amount" -> {
                     if (item.getValue() instanceof Number number) {
                         payment.setAmount(number.doubleValue());
                     } else {
                         payment.setAmount(Double.valueOf(item.getValue().toString()));
                     }
                 }}
         }

         repo.save(payment);

        }


    }



}
