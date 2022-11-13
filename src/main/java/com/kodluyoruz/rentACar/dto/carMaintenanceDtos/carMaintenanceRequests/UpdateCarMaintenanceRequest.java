package com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarMaintenanceRequest {

    @NotNull
    @Min(1)
    private int maintenanceId;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 300)
    private String description;

    private LocalDate returnDate;

    @NotNull
    @Min(1)
    private int carId;

}
