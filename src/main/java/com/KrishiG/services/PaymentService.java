package com.KrishiG.services;

import com.KrishiG.dtos.request.PaymentMethodRequestDto;
import com.KrishiG.enitites.PaymentMethod;

public interface PaymentService {
    PaymentMethod savePayment(PaymentMethodRequestDto paymentReqDto);
}
