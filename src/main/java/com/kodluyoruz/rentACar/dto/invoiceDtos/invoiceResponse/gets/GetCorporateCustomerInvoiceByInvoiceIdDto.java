package com.kodluyoruz.rentACar.dto.invoiceDtos.invoiceResponse.gets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCorporateCustomerInvoiceByInvoiceIdDto extends GetInvoiceDto {

    private String companyName;
    private String taxNumber;

}