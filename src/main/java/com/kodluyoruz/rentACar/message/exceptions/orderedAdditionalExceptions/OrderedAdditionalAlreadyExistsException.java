package com.kodluyoruz.rentACar.message.exceptions.orderedAdditionalExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class OrderedAdditionalAlreadyExistsException extends BusinessException {

    public OrderedAdditionalAlreadyExistsException(String message) {
        super(message);
    }
}
