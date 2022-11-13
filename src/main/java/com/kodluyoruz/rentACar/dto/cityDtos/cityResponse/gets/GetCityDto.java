package com.kodluyoruz.rentACar.dto.cityDtos.cityResponse.gets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCityDto {

    private int cityId;
    private String cityName;

}
