package com.kodluyoruz.rentACar.message.exceptions.rentalCarExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class StartDateBeforeFinishDateException extends BusinessException {

    public StartDateBeforeFinishDateException(String message) {
        super(message);
    }
}
