package com.kodluyoruz.rentACar.message.exceptions.paymentExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class RentalCarAlreadyExistsInPaymentException extends BusinessException {

    public RentalCarAlreadyExistsInPaymentException(String message) {
        super(message);
    }
}
