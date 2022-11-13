package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.CustomerNotFoundInInvoiceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserEmailNotValidException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.creditCardExceptions.CreditCardAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.individualCustomerExceptions.IndividualCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.individualCustomerExceptions.NationalIdentityAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.CustomerAlreadyExistsInRentalCarException;
import com.kodluyoruz.rentACar.service.IndividualCustomerService;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerResponse.gets.GetIndividualCustomerDto;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerResponse.lists.IndividualCustomerListDto;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerRequests.DeleteIndividualCustomerRequest;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerRequests.UpdateIndividualCustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/individualCustomers")
public class IndividualCustomerController {

    private final IndividualCustomerService individualCustomerService;

    @Autowired
    public IndividualCustomerController(IndividualCustomerService individualCustomerService) {

        this.individualCustomerService = individualCustomerService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateIndividualCustomerRequest createIndividualCustomerRequest) throws NationalIdentityAlreadyExistsException, UserAlreadyExistsException {

        return this.individualCustomerService.add(createIndividualCustomerRequest);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Valid UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws NationalIdentityAlreadyExistsException, IndividualCustomerNotFoundException, UserEmailNotValidException {

        return this.individualCustomerService.update(updateIndividualCustomerRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) throws CreditCardAlreadyExistsException, IndividualCustomerNotFoundException, CustomerAlreadyExistsInRentalCarException, CustomerNotFoundInInvoiceException {

        return this.individualCustomerService.delete(deleteIndividualCustomerRequest);
    }

    @GetMapping("/getById")
    public DataResult<GetIndividualCustomerDto> getById(@RequestParam int individualCustomerId) throws IndividualCustomerNotFoundException {

        return this.individualCustomerService.getById(individualCustomerId);
    }

    @GetMapping("/getAll")
    public DataResult<List<IndividualCustomerListDto>> getAll(){

        return this.individualCustomerService.getAll();
    }

}