package com.kodluyoruz.rentACar.message.exceptions.carExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class BrandExistsInCarException extends BusinessException {

    public BrandExistsInCarException(String message) {
        super(message);
    }
}
