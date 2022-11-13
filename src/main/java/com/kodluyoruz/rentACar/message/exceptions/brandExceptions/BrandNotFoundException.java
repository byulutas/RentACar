package com.kodluyoruz.rentACar.message.exceptions.brandExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class BrandNotFoundException extends BusinessException {

    public BrandNotFoundException(String message) {
        super(message);
    }
}
