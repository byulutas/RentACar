package com.kodluyoruz.rentACar.message.exceptions.PosServiceExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class MakePaymentFailedException extends BusinessException {

    public MakePaymentFailedException(String message) {
        super(message);
    }
}
