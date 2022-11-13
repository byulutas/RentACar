package com.kodluyoruz.rentACar.message.exceptions.invoiceExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class InvoiceNotFoundException extends BusinessException {

    public InvoiceNotFoundException(String message) {
        super(message);
    }
}
