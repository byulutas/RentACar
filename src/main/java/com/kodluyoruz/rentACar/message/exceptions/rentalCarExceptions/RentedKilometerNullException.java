package com.kodluyoruz.rentACar.message.exceptions.rentalCarExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class RentedKilometerNullException extends BusinessException {

    public RentedKilometerNullException(String message) {
        super(message);
    }
}
