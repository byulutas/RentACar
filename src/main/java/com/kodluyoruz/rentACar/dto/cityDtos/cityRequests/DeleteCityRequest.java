package com.kodluyoruz.rentACar.dto.cityDtos.cityRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCityRequest {

    @NotNull
    @Min(1)
    private int cityId;

}
