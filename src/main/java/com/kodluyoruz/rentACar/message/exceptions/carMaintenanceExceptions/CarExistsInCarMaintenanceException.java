package com.kodluyoruz.rentACar.message.exceptions.carMaintenanceExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CarExistsInCarMaintenanceException extends BusinessException {

    public CarExistsInCarMaintenanceException(String message) {
        super(message);
    }
}
