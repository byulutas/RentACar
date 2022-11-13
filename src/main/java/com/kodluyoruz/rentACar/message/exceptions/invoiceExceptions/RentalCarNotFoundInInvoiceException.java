package com.kodluyoruz.rentACar.message.exceptions.invoiceExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class RentalCarNotFoundInInvoiceException extends BusinessException {

    public RentalCarNotFoundInInvoiceException(String message) {
        super(message);
    }
}
