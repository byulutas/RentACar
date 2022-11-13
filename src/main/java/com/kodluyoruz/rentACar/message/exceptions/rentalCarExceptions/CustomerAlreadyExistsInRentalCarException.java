package com.kodluyoruz.rentACar.message.exceptions.rentalCarExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CustomerAlreadyExistsInRentalCarException extends BusinessException {

    public CustomerAlreadyExistsInRentalCarException(String message) {
        super(message);
    }
}
