package com.kodluyoruz.rentACar.message.exceptions.carCrashExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CarCrashNotFoundException extends BusinessException {

    public CarCrashNotFoundException(String message) {
        super(message);
    }
}
