package com.kodluyoruz.rentACar.message.exceptions.cityExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CityNotFoundException extends BusinessException {

    public CityNotFoundException(String message) {
        super(message);
    }
}
