package com.kodluyoruz.rentACar.message.exceptions.userExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
