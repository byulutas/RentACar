package com.kodluyoruz.rentACar.dto.userDtos.userResponse.lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDto {

    private int userId;
    private String email;

}
