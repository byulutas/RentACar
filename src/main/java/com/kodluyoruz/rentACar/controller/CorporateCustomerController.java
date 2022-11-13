package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.CustomerNotFoundInInvoiceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserEmailNotValidException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.corporateCustomerExceptions.CorporateCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.corporateCustomerExceptions.TaxNumberAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.creditCardExceptions.CreditCardAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.CustomerAlreadyExistsInRentalCarException;
import com.kodluyoruz.rentACar.service.CorporateCustomerService;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerResponse.gets.GetCorporateCustomerDto;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerResponse.lists.CorporateCustomerListDto;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerRequests.DeleteCorporateCustomerRequest;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/corporateCustomers")
public class CorporateCustomerController {

    private final CorporateCustomerService corporateCustomerService;

    @Autowired
    public CorporateCustomerController(CorporateCustomerService corporateCustomerService) {

        this.corporateCustomerService = corporateCustomerService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCorporateCustomerRequest createCorporateCustomerRequest) throws TaxNumberAlreadyExistsException, UserAlreadyExistsException {

        return this.corporateCustomerService.add(createCorporateCustomerRequest);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Valid UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws CorporateCustomerNotFoundException, TaxNumberAlreadyExistsException, UserEmailNotValidException {

        return this.corporateCustomerService.update(updateCorporateCustomerRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) throws CorporateCustomerNotFoundException, CreditCardAlreadyExistsException, CustomerAlreadyExistsInRentalCarException, CustomerNotFoundInInvoiceException {

        return this.corporateCustomerService.delete(deleteCorporateCustomerRequest);
    }

    @GetMapping("/getById")
    public DataResult<GetCorporateCustomerDto> getById(@RequestParam int corporateCustomerId) throws CorporateCustomerNotFoundException {

        return this.corporateCustomerService.getById(corporateCustomerId);
    }

    @GetMapping("/getAll")
    public DataResult<List<CorporateCustomerListDto>> getAll(){

        return this.corporateCustomerService.getAll();
    }

}