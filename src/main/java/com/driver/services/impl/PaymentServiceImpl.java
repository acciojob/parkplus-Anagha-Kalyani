package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId)
                .orElseThrow(() -> new Exception("Cannot make reservation"));

        int billAmount = calculateBillAmount(reservation);

        if (amountSent < billAmount) {
            throw new Exception("Cannot make reservation");
        }

        if (!isValidPaymentMode(mode)) {
            throw new Exception("Cannot make reservation");
        }

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setPaymentCompleted(true);
        payment.setPaymentMode(PaymentMode.valueOf(mode.toUpperCase()));

        return paymentRepository2.save(payment);
    }

    private int calculateBillAmount(Reservation reservation) {
        // Logic to calculate the bill amount
        return 0;  // Replace with actual implementation
    }

    private boolean isValidPaymentMode(String mode) {
        return mode.equalsIgnoreCase("cash") || mode.equalsIgnoreCase("card") || mode.equalsIgnoreCase("upi");
    }
}
