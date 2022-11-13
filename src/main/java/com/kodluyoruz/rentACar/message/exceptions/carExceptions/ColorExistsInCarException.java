package com.kodluyoruz.rentACar.message.exceptions.carExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class ColorExistsInCarException extends BusinessException {

    public ColorExistsInCarException(String message) {
        super(message);
    }
}
