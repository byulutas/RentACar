package com.kodluyoruz.rentACar.message.exceptions.rentalCarExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class StartDateBeforeTodayException extends BusinessException {

    public StartDateBeforeTodayException(String message) {
        super(message);
    }
}
