package com.kodluyoruz.rentACar.dto.carDtos.carResponse.lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarListByColorIdDto {
	
	private int carId;
	private double dailyPrice;
	private int modelYear;
	private int kilometer;
	private int colorId;
	private String colorName;
	private int brandId;
	private String brandName;

}
