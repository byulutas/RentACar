package com.kodluyoruz.rentACar.dto.userDtos.userRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest {

    @NotNull
    @Min(1)
    private int userId;

}
