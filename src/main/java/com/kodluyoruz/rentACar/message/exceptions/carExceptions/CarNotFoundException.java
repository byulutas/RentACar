package com.kodluyoruz.rentACar.message.exceptions.carExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CarNotFoundException extends BusinessException {

    public CarNotFoundException(String message) {
        super(message);
    }
}
