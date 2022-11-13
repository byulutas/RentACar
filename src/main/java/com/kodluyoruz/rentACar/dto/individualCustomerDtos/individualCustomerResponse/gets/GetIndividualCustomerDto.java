package com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerResponse.gets;

import com.kodluyoruz.rentACar.dto.customerDtos.customerResponse.gets.GetCustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetIndividualCustomerDto  extends GetCustomerDto {

    private String firstName;
    private String lastName;
    private String nationalIdentity;

}