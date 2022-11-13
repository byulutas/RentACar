package com.kodluyoruz.rentACar.message.exceptions.individualCustomerExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class IndividualCustomerNotFoundException extends BusinessException {

    public IndividualCustomerNotFoundException(String message) {
        super(message);
    }
}
