package com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarRequests;

import com.kodluyoruz.rentACar.dto.creditCardDtos.creditCardRequests.CreateCreditCardRequest;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalRequests.CreateOrderedAdditionalRequest;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarRequests.CreateRentalCarRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakePaymentForCorporateRentAdd {

    @NotNull
    @Valid
    CreateCreditCardRequest createCreditCardRequest;

    @NotNull
    @Valid
    CreateRentalCarRequest createRentalCarRequest;

    @Valid
    List<CreateOrderedAdditionalRequest> createOrderedAdditionalRequestList;

}
