package com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests;

import com.kodluyoruz.rentACar.dto.creditCardDtos.creditCardRequests.CreateCreditCardRequest;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalRequests.UpdateOrderedAdditionalRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderedAdditionalUpdateModel {

    @NotNull
    @Valid
    CreateCreditCardRequest createCreditCardRequest;

    @NotNull
    @Valid
    UpdateOrderedAdditionalRequest updateOrderedAdditionalRequest;

}
