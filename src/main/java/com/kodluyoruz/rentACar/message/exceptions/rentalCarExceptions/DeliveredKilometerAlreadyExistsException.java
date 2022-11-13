package com.kodluyoruz.rentACar.message.exceptions.rentalCarExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class DeliveredKilometerAlreadyExistsException extends BusinessException {

    public DeliveredKilometerAlreadyExistsException(String message) {
        super(message);
    }
}
