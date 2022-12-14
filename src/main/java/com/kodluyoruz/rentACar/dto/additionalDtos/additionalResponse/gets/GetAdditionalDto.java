package com.kodluyoruz.rentACar.dto.additionalDtos.additionalResponse.gets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAdditionalDto {

    private int additionalId;
    private String additionalName;
    private double additionalDailyPrice;
    private short maxUnitsPerRental;
}
