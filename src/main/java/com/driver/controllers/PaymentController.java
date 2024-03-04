package com.driver.controllers;

import com.driver.model.Payment;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestParam Integer reservationId, @RequestParam Integer amountSent, @RequestParam String mode) {
        try {
            Payment payment = paymentService.pay(reservationId, amountSent, mode);
            return ResponseEntity.ok(payment); // Payment successful
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.equals("Insufficient Amount")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient Amount");
            } else if (errorMessage.equals("Payment mode not detected")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment mode not detected");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        }
    }
}
