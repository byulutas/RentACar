package com.kodluyoruz.rentACar.dto.customerDtos.customerRequests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodluyoruz.rentACar.dto.userDtos.userRequests.CreateUserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequest extends CreateUserRequest {

    @JsonIgnore
    @CreationTimestamp
    private LocalDate registrationDate;

}
