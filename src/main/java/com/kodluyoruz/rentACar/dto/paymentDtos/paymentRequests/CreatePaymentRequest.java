package com.kodluyoruz.rentACar.dto.paymentDtos.paymentRequests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CreatePaymentRequest {

    @JsonIgnore
    private double totalPrice;

    @JsonIgnore
    private int rentalCarId;

}
