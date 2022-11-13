package com.kodluyoruz.rentACar.message.exceptions.carExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class ReturnKilometerLessThanRentKilometerException extends BusinessException {

    public ReturnKilometerLessThanRentKilometerException(String message) {
        super(message);
    }
}
