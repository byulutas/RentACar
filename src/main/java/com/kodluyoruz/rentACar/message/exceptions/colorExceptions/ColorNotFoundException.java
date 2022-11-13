package com.kodluyoruz.rentACar.message.exceptions.colorExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class ColorNotFoundException extends BusinessException {

    public ColorNotFoundException(String message) {
        super(message);
    }
}
