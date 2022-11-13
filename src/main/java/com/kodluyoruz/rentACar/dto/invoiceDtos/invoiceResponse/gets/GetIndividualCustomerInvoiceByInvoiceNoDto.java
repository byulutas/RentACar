package com.kodluyoruz.rentACar.dto.invoiceDtos.invoiceResponse.gets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetIndividualCustomerInvoiceByInvoiceNoDto extends GetInvoiceDto {

    private String firstName;
    private String lastName;
    private String nationalIdentity;

}
