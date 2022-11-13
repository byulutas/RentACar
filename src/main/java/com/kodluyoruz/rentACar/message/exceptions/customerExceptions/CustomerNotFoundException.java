package com.kodluyoruz.rentACar.message.exceptions.customerExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CustomerNotFoundException extends BusinessException {

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
