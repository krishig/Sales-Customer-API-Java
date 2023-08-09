package com.KrishiG.services;

import com.KrishiG.dtos.request.PaymentMethodRequestDto;
import com.KrishiG.entities.PaymentMethod;

public interface PaymentService {
    PaymentMethod savePayment(PaymentMethodRequestDto paymentReqDto);
}
