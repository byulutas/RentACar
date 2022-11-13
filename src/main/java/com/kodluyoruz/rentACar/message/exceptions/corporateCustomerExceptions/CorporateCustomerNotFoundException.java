package com.kodluyoruz.rentACar.message.exceptions.corporateCustomerExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CorporateCustomerNotFoundException extends BusinessException {

    public CorporateCustomerNotFoundException(String message) {
        super(message);
    }
}
