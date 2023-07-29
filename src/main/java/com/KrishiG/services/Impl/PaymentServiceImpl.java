package com.KrishiG.services.Impl;

import com.KrishiG.dtos.request.PaymentMethodRequestDto;
import com.KrishiG.enitites.PaymentMethod;
import com.KrishiG.repositories.PaymentRepository;
import com.KrishiG.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentMethod savePayment(PaymentMethodRequestDto paymentMethodReqDtoRequestDto) {
        PaymentMethod payment = convertDtoToEntity(paymentMethodReqDtoRequestDto);
        PaymentMethod paymentDb = paymentRepository.save(payment);
        return paymentDb;
    }
    private PaymentMethod convertDtoToEntity(PaymentMethodRequestDto paymentMethodRequestDto) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentType(paymentMethodRequestDto.getPaymentType());
        return paymentMethod;
    }
}
