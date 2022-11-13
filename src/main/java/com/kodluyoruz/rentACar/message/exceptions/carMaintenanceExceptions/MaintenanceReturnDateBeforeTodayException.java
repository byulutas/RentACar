package com.kodluyoruz.rentACar.message.exceptions.carMaintenanceExceptions;

import com.kodluyoruz.rentACar.message.exceptions.businessExceptions.BusinessException;

public class MaintenanceReturnDateBeforeTodayException extends BusinessException {

    public MaintenanceReturnDateBeforeTodayException(String message) {
        super(message);
    }
}
