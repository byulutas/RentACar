package com.kodluyoruz.rentACar.message.exceptions.orderedAdditionalExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class AdditionalQuantityNotValidException extends BusinessException {

    public AdditionalQuantityNotValidException(String message) {
        super(message);
    }
}
