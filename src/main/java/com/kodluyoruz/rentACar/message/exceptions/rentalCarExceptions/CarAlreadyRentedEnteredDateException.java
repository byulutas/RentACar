package com.kodluyoruz.rentACar.message.exceptions.rentalCarExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CarAlreadyRentedEnteredDateException extends BusinessException {

    public CarAlreadyRentedEnteredDateException(String message) {
        super(message);
    }
}
