package com.kodluyoruz.rentACar.message.exceptions.creditCardExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CreditCardAlreadyExistsException extends BusinessException {

    public CreditCardAlreadyExistsException(String message) {
        super(message);
    }
}
