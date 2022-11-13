package com.kodluyoruz.rentACar.dto.paymentDtos.paymentResponse.lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentListDto {

    private int paymentId;
    private double totalPrice;
    private int rentalCarId;
    private int invoiceId;

}
