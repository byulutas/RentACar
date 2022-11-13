package com.kodluyoruz.rentACar.message.exceptions.additionalExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class AdditionalNotFoundException extends BusinessException {

    public AdditionalNotFoundException(String message) {
        super(message);
    }
}
