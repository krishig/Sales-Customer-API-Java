package com.KrishiG.services;

import com.KrishiG.dtos.request.PaymentReqDto;
import com.KrishiG.enitites.Payment;

public interface PaymentService {
    Payment savePayment(PaymentReqDto paymentReqDto);
}
