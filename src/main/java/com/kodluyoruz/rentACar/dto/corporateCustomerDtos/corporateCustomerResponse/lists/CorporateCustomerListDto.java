package com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerResponse.lists;

import com.kodluyoruz.rentACar.dto.customerDtos.customerResponse.lists.CustomerListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CorporateCustomerListDto extends CustomerListDto {

    private String companyName;
    private String taxNumber;

}
