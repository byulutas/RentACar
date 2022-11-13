package com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerResponse.gets;

import com.kodluyoruz.rentACar.dto.customerDtos.customerResponse.gets.GetCustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCorporateCustomerDto extends GetCustomerDto {

    private String companyName;
    private String taxNumber;

}
