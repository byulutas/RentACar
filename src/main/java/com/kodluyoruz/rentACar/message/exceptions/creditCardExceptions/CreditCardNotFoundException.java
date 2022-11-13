package com.kodluyoruz.rentACar.message.exceptions.creditCardExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CreditCardNotFoundException extends BusinessException {

    public CreditCardNotFoundException(String message) {
        super(message);
    }
}
