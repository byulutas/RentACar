package com.kodluyoruz.rentACar.dto.userDtos.userResponse.gets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserDto {

    private int userId;
    private String email;

}