package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.PaymentReqDto;
import com.KrishiG.enitites.Payment;
import com.KrishiG.repositories.PaymentRepository;
import com.KrishiG.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment savePayment(PaymentReqDto paymentReqDto) {
        Payment payment = convertDtoToEntity(paymentReqDto);
        Payment paymentDb = paymentRepository.save(payment);
        return paymentDb;
    }
    private Payment convertDtoToEntity(PaymentReqDto paymentReqDto) {
        Payment payment = new Payment();
        payment.setPaymentType(paymentReqDto.getPaymentType());
        return payment;
    }
}
