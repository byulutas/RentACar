package com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCarMaintenanceRequest {

    @NotNull
    @Min(1)
    private int maintenanceId;

}
