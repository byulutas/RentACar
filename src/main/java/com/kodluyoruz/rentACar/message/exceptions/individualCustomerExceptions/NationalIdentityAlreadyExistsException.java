package com.kodluyoruz.rentACar.message.exceptions.individualCustomerExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class NationalIdentityAlreadyExistsException extends BusinessException {

    public NationalIdentityAlreadyExistsException(String message) {
        super(message);
    }
}
