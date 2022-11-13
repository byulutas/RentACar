package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.messaaages.exceptions.customerExceptions.CustomerNotFoundException;
import com.kodluyoruz.rentACar.service.CustomerService;
import com.kodluyoruz.rentACar.dto.customerDtos.customerResponse.lists.CustomerListDto;
import com.kodluyoruz.rentACar.dto.customerDtos.customerResponse.gets.GetCustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {

        this.customerService = customerService;
    }

    @GetMapping("getById")
    public DataResult<GetCustomerDto> getById(@RequestParam int customerId) throws CustomerNotFoundException {

        return this.customerService.getById(customerId);
    }

    @GetMapping("/getAll")
    public DataResult<List<CustomerListDto>> getAll(){

        return this.customerService.getAll();
    }

}