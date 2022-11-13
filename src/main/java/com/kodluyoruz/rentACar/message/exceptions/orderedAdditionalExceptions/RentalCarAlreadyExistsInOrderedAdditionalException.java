package com.kodluyoruz.rentACar.message.exceptions.orderedAdditionalExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class RentalCarAlreadyExistsInOrderedAdditionalException extends BusinessException {

    public RentalCarAlreadyExistsInOrderedAdditionalException(String message) {
        super(message);
    }
}
