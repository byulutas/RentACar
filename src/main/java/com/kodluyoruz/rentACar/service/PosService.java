package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.messaaages.exceptions.PosServiceExceptions.MakePaymentFailedException;

public interface PosService {

    boolean payment(String cardNumber, String cardOwner, String cardCvv, String cardExpirationDate, double totalPrice) throws MakePaymentFailedException;

}
