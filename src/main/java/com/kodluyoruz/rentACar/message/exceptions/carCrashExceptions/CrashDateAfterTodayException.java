package com.kodluyoruz.rentACar.message.exceptions.carCrashExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CrashDateAfterTodayException extends BusinessException {

    public CrashDateAfterTodayException(String message) {
        super(message);
    }
}
