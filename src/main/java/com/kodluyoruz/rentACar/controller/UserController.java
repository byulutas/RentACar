package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserNotFoundException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.service.UserService;
import com.kodluyoruz.rentACar.dto.userDtos.userResponse.gets.GetUserDto;
import com.kodluyoruz.rentACar.dto.userDtos.userResponse.lists.UserListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/getAll")
    public DataResult<List<UserListDto>> getAll(){
        return this.userService.getAll();
    }

    @GetMapping("/getById")
    public DataResult<GetUserDto> getById(@RequestParam int userId) throws UserNotFoundException {
        return this.userService.getById(userId);
    }

}