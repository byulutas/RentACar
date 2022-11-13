package com.kodluyoruz.rentACar.message.exceptions.carExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class ModelYearAfterThisYearException extends BusinessException {

    public ModelYearAfterThisYearException(String message) {
        super(message);
    }
}
