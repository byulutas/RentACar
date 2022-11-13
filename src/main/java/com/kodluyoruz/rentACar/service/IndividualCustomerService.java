package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.CustomerNotFoundInInvoiceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserEmailNotValidException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerRequests.CreateIndividualCustomerRequest;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerRequests.DeleteIndividualCustomerRequest;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerRequests.UpdateIndividualCustomerRequest;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerResponse.gets.GetIndividualCustomerDto;
import com.kodluyoruz.rentACar.dto.individualCustomerDtos.individualCustomerResponse.lists.IndividualCustomerListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.creditCardExceptions.CreditCardAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.individualCustomerExceptions.IndividualCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.individualCustomerExceptions.NationalIdentityAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.CustomerAlreadyExistsInRentalCarException;
import com.kodluyoruz.rentACar.repository.IndividualCustomerRepository;
import com.kodluyoruz.rentACar.entity.IndividualCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndividualCustomerService {

    private final IndividualCustomerRepository individualCustomerRepository;
    private final UserService userService;
    private final RentalCarService rentalCarService;
    private final InvoiceService invoiceService;
    private final CreditCardService creditCardService;
    private final ModelMapperService modelMapperService;

    @Autowired
    public IndividualCustomerService(IndividualCustomerRepository individualCustomerRepository, ModelMapperService modelMapperService, UserService userService, RentalCarService rentalCarService, InvoiceService invoiceService, @Lazy CreditCardService creditCardService) {

        this.individualCustomerRepository = individualCustomerRepository;
        this.userService = userService;
        this.rentalCarService = rentalCarService;
        this.modelMapperService = modelMapperService;
        this.invoiceService = invoiceService;
        this.creditCardService = creditCardService;

    }

    public Result add(CreateIndividualCustomerRequest createIndividualCustomerRequest) throws NationalIdentityAlreadyExistsException, UserAlreadyExistsException {

        this.userService.checkIfUserEmailNotExists(createIndividualCustomerRequest.getEmail());
        checkIfNationalIdentityNotExists(createIndividualCustomerRequest.getNationalIdentity());
        IndividualCustomer individualCustomer = this.modelMapperService.forRequest().map(createIndividualCustomerRequest, IndividualCustomer.class);
        this.individualCustomerRepository.save(individualCustomer);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }


    public Result update(UpdateIndividualCustomerRequest updateIndividualCustomerRequest) throws NationalIdentityAlreadyExistsException, IndividualCustomerNotFoundException, UserEmailNotValidException {

        checkIfIndividualCustomerIdExists(updateIndividualCustomerRequest.getUserId());
        this.userService.checkIfUserEmailNotExistsForUpdate(updateIndividualCustomerRequest.getUserId(), updateIndividualCustomerRequest.getEmail());
        checkIfNationalIdentityNotExistsForUpdate(updateIndividualCustomerRequest.getUserId(), updateIndividualCustomerRequest.getNationalIdentity());
        IndividualCustomer individualCustomer = this.modelMapperService.forRequest().map(updateIndividualCustomerRequest, IndividualCustomer.class);
        this.individualCustomerRepository.save(individualCustomer);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_UPDATED_SUCCESSFULLY + updateIndividualCustomerRequest.getUserId());
    }


    public Result delete(DeleteIndividualCustomerRequest deleteIndividualCustomerRequest) throws CreditCardAlreadyExistsException, IndividualCustomerNotFoundException, CustomerNotFoundInInvoiceException, CustomerAlreadyExistsInRentalCarException {

        checkIfIndividualCustomerIdExists(deleteIndividualCustomerRequest.getUserId());
        this.rentalCarService.checkIfRentalCar_CustomerIdNotExists(deleteIndividualCustomerRequest.getUserId());
        this.creditCardService.checkIfNotExistsByCustomer_CustomerId(deleteIndividualCustomerRequest.getUserId());
        this.invoiceService.checkIfNotExistsByCustomer_CustomerId(deleteIndividualCustomerRequest.getUserId());
        this.individualCustomerRepository.deleteById(deleteIndividualCustomerRequest.getUserId());

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY + deleteIndividualCustomerRequest.getUserId());
    }

    public DataResult<List<IndividualCustomerListDto>> getAll() {

        List<IndividualCustomer> individualCustomers = this.individualCustomerRepository.findAll();
        List<IndividualCustomerListDto> result = individualCustomers.stream().map(individualCustomer -> this.modelMapperService.forDto().map(individualCustomer,IndividualCustomerListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    public DataResult<GetIndividualCustomerDto> getById(int individualCustomerId) throws IndividualCustomerNotFoundException {

        checkIfIndividualCustomerIdExists(individualCustomerId);
        IndividualCustomer individualCustomer = this.individualCustomerRepository.getById(individualCustomerId);
        GetIndividualCustomerDto result = this.modelMapperService.forDto().map(individualCustomer, GetIndividualCustomerDto.class);

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + individualCustomerId);
    }

    public IndividualCustomer getIndividualCustomerById(int individualCustomerId){

        return this.individualCustomerRepository.getById(individualCustomerId);
    }


    public void checkIfIndividualCustomerIdExists(int individualCustomerId) throws IndividualCustomerNotFoundException {

        if(!this.individualCustomerRepository.existsByIndividualCustomerId(individualCustomerId)){
            throw new IndividualCustomerNotFoundException(BusinessMessages.IndividualCustomerMessages.INDIVIDUAL_CUSTOMER_ID_NOT_FOUND + individualCustomerId);
        }
    }

    void checkIfNationalIdentityNotExists(String nationalIdentity) throws NationalIdentityAlreadyExistsException {

        if(this.individualCustomerRepository.existsByNationalIdentity(nationalIdentity)){
            throw new NationalIdentityAlreadyExistsException(BusinessMessages.IndividualCustomerMessages.NATIONAL_IDENTITY_ALREADY_EXISTS + nationalIdentity);
        }
    }

    void checkIfNationalIdentityNotExistsForUpdate(int individualCustomerId, String nationalIdentity) throws NationalIdentityAlreadyExistsException {

        if(this.individualCustomerRepository.existsByNationalIdentityAndIndividualCustomerIdIsNot(nationalIdentity, individualCustomerId)){
            throw new NationalIdentityAlreadyExistsException(BusinessMessages.IndividualCustomerMessages.NATIONAL_IDENTITY_ALREADY_EXISTS + nationalIdentity);
        }
    }

}