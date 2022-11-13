package com.kodluyoruz.rentACar.message.exceptions.invoiceExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CustomerNotFoundInInvoiceException extends BusinessException {

    public CustomerNotFoundInInvoiceException(String message) {
        super(message);
    }
}
