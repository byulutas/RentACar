package com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashResponse.gets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCarCrashDto {

    private int carCrashId;
    private LocalDate crashDate;
    private double crashValuation;
    private int carId;

}
