package com.kodluyoruz.rentACar.message.exceptions.orderedAdditionalExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class AdditionalAlreadyExistsInOrderedAdditionalException extends BusinessException {

    public AdditionalAlreadyExistsInOrderedAdditionalException(String message) {
        super(message);
    }
}
