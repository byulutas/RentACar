package com.kodluyoruz.rentACar.message.exceptions.orderedAdditionalExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class OrderedAdditionalNotFoundException extends BusinessException {

    public OrderedAdditionalNotFoundException(String message) {
        super(message);
    }
}
