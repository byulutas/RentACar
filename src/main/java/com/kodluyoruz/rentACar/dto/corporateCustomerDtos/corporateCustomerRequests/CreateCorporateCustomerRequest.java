package com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerRequests;

import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.dto.customerDtos.customerRequests.CreateCustomerRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCorporateCustomerRequest extends CreateCustomerRequest {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 300)
    private String companyName;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[0-9]{10}", message = BusinessMessages.CorporateCustomerMessages.TAX_NUMBER_NOT_VALID)
    private String taxNumber;

}
