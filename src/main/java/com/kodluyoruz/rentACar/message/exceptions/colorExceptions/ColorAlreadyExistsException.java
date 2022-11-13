package com.kodluyoruz.rentACar.message.exceptions.colorExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class ColorAlreadyExistsException extends BusinessException {

    public ColorAlreadyExistsException(String message) {
        super(message);
    }
}
