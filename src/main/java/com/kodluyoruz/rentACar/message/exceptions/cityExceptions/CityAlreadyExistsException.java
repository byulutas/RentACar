package com.kodluyoruz.rentACar.message.exceptions.cityExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CityAlreadyExistsException extends BusinessException {

    public CityAlreadyExistsException(String message) {
        super(message);
    }
}
