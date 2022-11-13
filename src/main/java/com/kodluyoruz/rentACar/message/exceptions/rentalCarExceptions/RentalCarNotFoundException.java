package com.kodluyoruz.rentACar.message.exceptions.rentalCarExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class RentalCarNotFoundException extends BusinessException {

    public RentalCarNotFoundException(String message) {
        super(message);
    }
}
