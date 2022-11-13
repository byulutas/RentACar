package com.kodluyoruz.rentACar.message.exceptions.carCrashExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CarExistsInCarCrashException extends BusinessException {

    public CarExistsInCarCrashException(String message) {
        super(message);
    }
}
