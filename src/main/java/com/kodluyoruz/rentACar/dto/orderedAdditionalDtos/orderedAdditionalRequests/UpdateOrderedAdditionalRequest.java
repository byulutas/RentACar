package com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderedAdditionalRequest {

    @NotNull
    @Min(1)
    private int orderedAdditionalId;

    @NotNull
    @Min(1)
    private short orderedAdditionalQuantity;

    @NotNull
    @Min(1)
    private int additionalId;

    @NotNull
    @Min(1)
    private int rentalCarId;

}
