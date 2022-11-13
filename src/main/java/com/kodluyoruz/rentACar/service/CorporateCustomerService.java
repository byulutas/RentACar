package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.CustomerNotFoundInInvoiceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserEmailNotValidException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerRequests.CreateCorporateCustomerRequest;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerRequests.DeleteCorporateCustomerRequest;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerRequests.UpdateCorporateCustomerRequest;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerResponse.gets.GetCorporateCustomerDto;
import com.kodluyoruz.rentACar.dto.corporateCustomerDtos.corporateCustomerResponse.lists.CorporateCustomerListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.corporateCustomerExceptions.CorporateCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.corporateCustomerExceptions.TaxNumberAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.creditCardExceptions.CreditCardAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.CustomerAlreadyExistsInRentalCarException;
import com.kodluyoruz.rentACar.repository.CorporateCustomerRepository;
import com.kodluyoruz.rentACar.entity.CorporateCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CorporateCustomerService {

    private final CorporateCustomerRepository corporateCustomerRepository;
    private final RentalCarService rentalCarService;
    private final UserService userService;
    private final InvoiceService invoiceService;
    private final CreditCardService creditCardService;
    private final ModelMapperService modelMapperService;

    @Autowired
    public CorporateCustomerService(CorporateCustomerRepository corporateCustomerRepository, ModelMapperService modelMapperService, RentalCarService rentalCarService, UserService userService, InvoiceService invoiceService, @Lazy CreditCardService creditCardService) {

        this.corporateCustomerRepository = corporateCustomerRepository;
        this.rentalCarService = rentalCarService;
        this.userService = userService;
        this.modelMapperService = modelMapperService;
        this.invoiceService = invoiceService;
        this.creditCardService = creditCardService;

    }

    public Result add(CreateCorporateCustomerRequest createCorporateCustomerRequest) throws TaxNumberAlreadyExistsException, UserAlreadyExistsException {

        this.userService.checkIfUserEmailNotExists(createCorporateCustomerRequest.getEmail());
        checkIfTaxNumberNotExists(createCorporateCustomerRequest.getTaxNumber());
        CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(createCorporateCustomerRequest, CorporateCustomer.class);
        this.corporateCustomerRepository.save(corporateCustomer);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    public Result update(UpdateCorporateCustomerRequest updateCorporateCustomerRequest) throws TaxNumberAlreadyExistsException, CorporateCustomerNotFoundException, UserEmailNotValidException {

        checkIfCorporateCustomerIdExists(updateCorporateCustomerRequest.getUserId());
        this.userService.checkIfUserEmailNotExistsForUpdate(updateCorporateCustomerRequest.getUserId(), updateCorporateCustomerRequest.getEmail());
        checkIfTaxNumberNotExistsForUpdate(updateCorporateCustomerRequest.getUserId(), updateCorporateCustomerRequest.getTaxNumber());
        CorporateCustomer corporateCustomer = this.modelMapperService.forRequest().map(updateCorporateCustomerRequest, CorporateCustomer.class);
        this.corporateCustomerRepository.save(corporateCustomer);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_UPDATED_SUCCESSFULLY + updateCorporateCustomerRequest.getUserId());
    }

    public Result delete(DeleteCorporateCustomerRequest deleteCorporateCustomerRequest) throws CorporateCustomerNotFoundException, CreditCardAlreadyExistsException, CustomerNotFoundInInvoiceException, CustomerAlreadyExistsInRentalCarException {

        checkIfCorporateCustomerIdExists(deleteCorporateCustomerRequest.getUserId());
        this.rentalCarService.checkIfRentalCar_CustomerIdNotExists(deleteCorporateCustomerRequest.getUserId());
        this.creditCardService.checkIfNotExistsByCustomer_CustomerId(deleteCorporateCustomerRequest.getUserId());
        this.invoiceService.checkIfNotExistsByCustomer_CustomerId(deleteCorporateCustomerRequest.getUserId());
        this.corporateCustomerRepository.deleteById(deleteCorporateCustomerRequest.getUserId());

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY + deleteCorporateCustomerRequest.getUserId());
    }

    public DataResult<List<CorporateCustomerListDto>> getAll() {

        List<CorporateCustomer> corporateCustomers = this.corporateCustomerRepository.findAll();
        List<CorporateCustomerListDto> result = corporateCustomers.stream().map(corporateCustomer -> this.modelMapperService.forDto().map(corporateCustomer, CorporateCustomerListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    public DataResult<GetCorporateCustomerDto> getById(int corporateCustomerId) throws CorporateCustomerNotFoundException {

        checkIfCorporateCustomerIdExists(corporateCustomerId);
        CorporateCustomer corporateCustomer = this.corporateCustomerRepository.getById(corporateCustomerId);
        GetCorporateCustomerDto result = this.modelMapperService.forDto().map(corporateCustomer, GetCorporateCustomerDto.class);

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + corporateCustomerId);
    }

    public CorporateCustomer getCorporateCustomerById(int corporateCustomerId){

        return this.corporateCustomerRepository.getById(corporateCustomerId);
    }

    public void checkIfCorporateCustomerIdExists(int corporateCustomerId) throws CorporateCustomerNotFoundException {

        if(!this.corporateCustomerRepository.existsByCorporateCustomerId(corporateCustomerId)){
            throw new CorporateCustomerNotFoundException(BusinessMessages.CorporateCustomerMessages.CORPORATE_CUSTOMER_ID_NOT_FOUND + corporateCustomerId);
        }
    }

    void checkIfTaxNumberNotExists(String taxNumber) throws TaxNumberAlreadyExistsException {

        if(this.corporateCustomerRepository.existsByTaxNumber(taxNumber)){
            throw new TaxNumberAlreadyExistsException(BusinessMessages.CorporateCustomerMessages.TAX_NAME_ALREADY_EXISTS + taxNumber);
        }
    }

    void checkIfTaxNumberNotExistsForUpdate(int corporateCustomerId, String taxNumber) throws TaxNumberAlreadyExistsException {

        if(this.corporateCustomerRepository.existsByTaxNumberAndCorporateCustomerIdIsNot(taxNumber, corporateCustomerId)){
            throw new TaxNumberAlreadyExistsException(BusinessMessages.CorporateCustomerMessages.TAX_NAME_ALREADY_EXISTS + taxNumber);
        }
    }

}