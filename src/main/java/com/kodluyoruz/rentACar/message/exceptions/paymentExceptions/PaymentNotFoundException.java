package com.kodluyoruz.rentACar.message.exceptions.paymentExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class PaymentNotFoundException extends BusinessException {

    public PaymentNotFoundException(String message) {
        super(message);
    }
}
