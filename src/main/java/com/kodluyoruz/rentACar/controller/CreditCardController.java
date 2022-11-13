package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.messaaages.exceptions.creditCardExceptions.CreditCardNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.customerExceptions.CustomerNotFoundException;
import com.kodluyoruz.rentACar.service.CreditCardService;
import com.kodluyoruz.rentACar.dto.creditCardDtos.creditCardResponse.gets.GetCreditCardDto;
import com.kodluyoruz.rentACar.dto.creditCardDtos.creditCardResponse.lists.CreditCardListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/creditCards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardController(CreditCardService creditCardService) {

        this.creditCardService = creditCardService;
    }

    @GetMapping("/getById")
    public DataResult<GetCreditCardDto> getById(@RequestParam int creditCardId) throws CreditCardNotFoundException {

        return this.creditCardService.getById(creditCardId);
    }

    @GetMapping("/getAllCreditCardByCustomer_CustomerId")
    public DataResult<List<CreditCardListDto>> getAllCreditCardByCustomer_CustomerId(@RequestParam int customerId) throws CustomerNotFoundException {

        return this.creditCardService.getAllCreditCardByCustomer_CustomerId(customerId);
    }

    @GetMapping("/getAll")
    public DataResult<List<CreditCardListDto>> getAll(){

        return this.creditCardService.getAll();
    }

}