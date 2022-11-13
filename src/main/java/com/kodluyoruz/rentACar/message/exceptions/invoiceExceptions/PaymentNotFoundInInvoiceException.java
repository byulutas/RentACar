package com.kodluyoruz.rentACar.message.exceptions.invoiceExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class PaymentNotFoundInInvoiceException extends BusinessException {

    public PaymentNotFoundInInvoiceException(String message) {
        super(message);
    }
}
