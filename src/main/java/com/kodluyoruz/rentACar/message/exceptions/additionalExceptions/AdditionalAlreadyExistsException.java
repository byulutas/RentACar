package com.kodluyoruz.rentACar.message.exceptions.additionalExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class AdditionalAlreadyExistsException extends BusinessException {

    public AdditionalAlreadyExistsException(String message) {
        super(message);
    }
}
