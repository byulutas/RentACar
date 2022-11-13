package com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarRequests;

import com.kodluyoruz.rentACar.dto.creditCardDtos.creditCardRequests.CreateCreditCardRequest;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarRequests.UpdateDeliveryDateRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakePaymentForRentDeliveryDateUpdate {

    @NotNull
    @Valid
    CreateCreditCardRequest createCreditCardRequest;

    @NotNull
    @Valid
    UpdateDeliveryDateRequest updateDeliveryDateRequest;
    
}
