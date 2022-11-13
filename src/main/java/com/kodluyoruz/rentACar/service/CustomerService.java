package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.dto.customerDtos.customerResponse.gets.GetCustomerDto;
import com.kodluyoruz.rentACar.dto.customerDtos.customerResponse.lists.CustomerListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.customerExceptions.CustomerNotFoundException;
import com.kodluyoruz.rentACar.repository.CustomerRepository;
import com.kodluyoruz.rentACar.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapperService modelMapperService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ModelMapperService modelMapperService) {
        this.customerRepository = customerRepository;
        this.modelMapperService = modelMapperService;
    }

    public DataResult<List<CustomerListDto>> getAll() {

        List<Customer> customers = this.customerRepository.findAll();

        List<CustomerListDto> result = customers.stream().map(customer -> this.modelMapperService.forDto().map(customer, CustomerListDto.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    public DataResult<GetCustomerDto> getById(int customerId) throws CustomerNotFoundException {

        checkIfCustomerIdExists(customerId);
        Customer customer = this.customerRepository.getById(customerId);
        GetCustomerDto result = this.modelMapperService.forDto().map(customer, GetCustomerDto.class);

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + customerId);
    }

    public void checkIfCustomerIdExists(int customerId) throws CustomerNotFoundException {

        if(!this.customerRepository.existsByCustomerId(customerId)){
            throw new CustomerNotFoundException(BusinessMessages.CustomerMessages.CUSTOMER_ID_NOT_FOUND + customerId);
        }
    }

    public Customer getCustomerById(int customerId){

        return this.customerRepository.getById(customerId);
    }

}