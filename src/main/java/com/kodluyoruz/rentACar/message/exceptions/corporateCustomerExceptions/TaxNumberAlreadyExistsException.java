package com.kodluyoruz.rentACar.message.exceptions.corporateCustomerExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class TaxNumberAlreadyExistsException extends BusinessException {

    public TaxNumberAlreadyExistsException(String message) {
        super(message);
    }
}
