package com.kodluyoruz.rentACar.dto.colorDtos.colorResponse.gets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetColorDto {
	
	private int colorId;
	private String colorName;

}
