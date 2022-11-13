package com.kodluyoruz.rentACar.message.exceptions.rentalCarExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CarAlreadyExistsInRentalCarException extends BusinessException {

    public CarAlreadyExistsInRentalCarException(String message) {
        super(message);
    }
}
