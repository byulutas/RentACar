package com.kodluyoruz.rentACar.service;


import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.creditCardDtos.creditCardRequests.CreateCreditCardRequest;
import com.kodluyoruz.rentACar.dto.creditCardDtos.creditCardResponse.gets.GetCreditCardDto;
import com.kodluyoruz.rentACar.dto.creditCardDtos.creditCardResponse.lists.CreditCardListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.creditCardExceptions.CreditCardAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.creditCardExceptions.CreditCardNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.customerExceptions.CustomerNotFoundException;
import com.kodluyoruz.rentACar.repository.CreditCardRepository;
import com.kodluyoruz.rentACar.entity.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CustomerService customerService;
    private final ModelMapperService modelMapperService;

    @Autowired
    public CreditCardService(CreditCardRepository creditCardRepository, ModelMapperService modelMapperService, CustomerService customerService) {
        this.creditCardRepository = creditCardRepository;
        this.customerService = customerService;
        this.modelMapperService = modelMapperService;
    }

    public enum CardSaveInformation {
        SAVE, DONT_SAVE;
    }

    public Result add(CreateCreditCardRequest createCreditCardRequest) throws CustomerNotFoundException {

        this.customerService.checkIfCustomerIdExists(createCreditCardRequest.getCustomerId());
        CreditCard creditCard = this.modelMapperService.forRequest().map(createCreditCardRequest, CreditCard.class);
        creditCard.setCustomer(this.customerService.getCustomerById(createCreditCardRequest.getCustomerId()));
        if(checkIfNotExistsByCardNumber(creditCard.getCardNumber())){
            this.creditCardRepository.save(creditCard);

        }

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    public DataResult<List<CreditCardListDto>> getAll() {

        List<CreditCard> creditCardList = this.creditCardRepository.findAll();
        List<CreditCardListDto> result = creditCardList.stream().map(creditCard -> this.modelMapperService.forDto().map(creditCard, CreditCardListDto.class)).collect(Collectors.toList());
        setCustomerIdForAllCreditCard(creditCardList, result);

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    public DataResult<GetCreditCardDto> getById(int creditCardId) throws CreditCardNotFoundException {

        checkIfExistsById(creditCardId);
        CreditCard creditCard = this.creditCardRepository.getById(creditCardId);
        GetCreditCardDto result = this.modelMapperService.forDto().map(creditCard, GetCreditCardDto.class);
        result.setCustomerId(creditCard.getCustomer().getCustomerId());

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + creditCardId);
    }

    public DataResult<List<CreditCardListDto>> getAllCreditCardByCustomer_CustomerId(int customerId) throws CustomerNotFoundException {

        this.customerService.checkIfCustomerIdExists(customerId);
        List<CreditCard> creditCardList = this.creditCardRepository.getAllByCustomer_CustomerId(customerId);
        List<CreditCardListDto> result = creditCardList.stream().map(creditCard -> this.modelMapperService.forDto().map(creditCard, CreditCardListDto.class)).collect(Collectors.toList());
        setCustomerIdForAllCreditCard(creditCardList, result);

        return new SuccessDataResult<>(result, BusinessMessages.CreditCardMessages.CREDIT_CARD_LISTED_BY_CUSTOMER_ID + customerId);
    }

    private void setCustomerIdForAllCreditCard(List<CreditCard> creditCardList, List<CreditCardListDto> result) {

        for(int i=0; i< creditCardList.size();i++){
            result.get(i).setCustomerId(creditCardList.get(i).getCustomer().getCustomerId());
        }
    }

    public void checkSaveInformationAndSaveCreditCard(CreateCreditCardRequest createCreditCardRequest, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException {

        if(cardSaveInformation.equals(CreditCardService.CardSaveInformation.SAVE)){
            add(createCreditCardRequest);
        }
    }


    public void checkIfNotExistsByCustomer_CustomerId(int customerId) throws CreditCardAlreadyExistsException {

        if(this.creditCardRepository.existsByCustomer_CustomerId(customerId)){
            throw new CreditCardAlreadyExistsException(BusinessMessages.CreditCardMessages.CREDIT_CARD_ALREADY_EXISTS + customerId);
        }
    }

    private boolean checkIfNotExistsByCardNumber(String cardNumber) {

        return !this.creditCardRepository.existsByCardNumber(cardNumber);
    }

    private void checkIfExistsById(int creditCardId) throws CreditCardNotFoundException {

        if(!this.creditCardRepository.existsByCreditCardId(creditCardId)){
            throw new CreditCardNotFoundException(BusinessMessages.CreditCardMessages.CREDIT_CARD_ID_NOT_FOUND + creditCardId);
        }
    }

}