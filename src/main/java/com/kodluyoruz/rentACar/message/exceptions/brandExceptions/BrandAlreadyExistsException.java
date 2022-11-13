package com.kodluyoruz.rentACar.message.exceptions.brandExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class BrandAlreadyExistsException extends BusinessException {

    public BrandAlreadyExistsException(String message) {
        super(message);
    }
}
