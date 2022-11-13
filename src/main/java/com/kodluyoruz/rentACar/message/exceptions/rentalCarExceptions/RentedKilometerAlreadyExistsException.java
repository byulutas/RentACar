package com.kodluyoruz.rentACar.message.exceptions.rentalCarExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class RentedKilometerAlreadyExistsException extends BusinessException {

    public RentedKilometerAlreadyExistsException(String message) {
        super(message);
    }
}
