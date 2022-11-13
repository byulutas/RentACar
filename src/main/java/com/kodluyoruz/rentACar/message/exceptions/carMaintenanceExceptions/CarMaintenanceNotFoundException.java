package com.kodluyoruz.rentACar.message.exceptions.carMaintenanceExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class CarMaintenanceNotFoundException extends BusinessException {

    public CarMaintenanceNotFoundException(String message) {
        super(message);
    }
}
