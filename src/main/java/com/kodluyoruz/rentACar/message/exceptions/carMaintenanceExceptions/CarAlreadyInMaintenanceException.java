package com.kodluyoruz.rentACar.message.exceptions.carMaintenanceExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CarAlreadyInMaintenanceException extends BusinessException {

    public CarAlreadyInMaintenanceException(String message) {
        super(message);
    }
}
