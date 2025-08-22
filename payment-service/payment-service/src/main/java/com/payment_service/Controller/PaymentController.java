package com.payment_service.Controller;

import com.payment_service.Dto.StkCallbackResponse;
import com.payment_service.Dto.StkPushResponse;
import com.payment_service.Dto.StkRequest;
import com.payment_service.Entity.Payment;
import com.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @PostMapping("/pay")
    public ResponseEntity<Payment> pushToStk(@RequestBody StkRequest stkRequest){

      Payment payment=  paymentService.stkPushRequest(stkRequest.getPhone(),
                stkRequest.getAmount(),stkRequest.getAccountRef(),stkRequest.getAccountRef());

        return ResponseEntity.ok(payment);
    }

    @PostMapping("/complete")
    public ResponseEntity<String> stkCallBack(@RequestBody StkCallbackResponse stkCallbackResponse){
        paymentService.callBack(stkCallbackResponse);
        return ResponseEntity.ok("success");
    }
}
