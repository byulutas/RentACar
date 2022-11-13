package com.kodluyoruz.rentACar.message.exceptions.userExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class UserEmailNotValidException extends BusinessException {

    public UserEmailNotValidException(String message) {
        super(message);
    }
}
