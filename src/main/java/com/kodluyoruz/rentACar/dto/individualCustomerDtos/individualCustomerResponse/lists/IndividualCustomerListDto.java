package com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerResponse.lists;

import com.kodluyoruz.rentACar.dto.customerDtos.customerResponse.lists.CustomerListDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndividualCustomerListDto extends CustomerListDto {

    private String firstName;
    private String lastName;
    private String nationalIdentity;

}