package com.kodluyoruz.rentACar.service;


import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.CreateAdditionalRequest;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.DeleteAdditionalRequest;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.UpdateAdditionalRequest;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalResponse.gets.GetAdditionalDto;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalResponse.lists.AdditionalListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.additionalExceptions.AdditionalAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.additionalExceptions.AdditionalNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.orderedAdditionalExceptions.AdditionalAlreadyExistsInOrderedAdditionalException;
import com.kodluyoruz.rentACar.repository.AdditionalRepository;
import com.kodluyoruz.rentACar.entity.Additional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdditionalService {

    private final AdditionalRepository additionalRepository;
    private final OrderedAdditionalService orderedAdditionalService;
    private final ModelMapperService modelMapperService;

    @Autowired
    public AdditionalService(AdditionalRepository additionalRepository, ModelMapperService modelMapperService, @Lazy OrderedAdditionalService orderedAdditionalService) {
        this.additionalRepository = additionalRepository;
        this.orderedAdditionalService = orderedAdditionalService;
        this.modelMapperService = modelMapperService;
    }

    public Result add(CreateAdditionalRequest createAdditionalRequest) throws AdditionalAlreadyExistsException {

        checkIsNotExistsByAdditionalName(createAdditionalRequest.getAdditionalName());
        Additional additional = this.modelMapperService.forRequest().map(createAdditionalRequest, Additional.class);
        this.additionalRepository.save(additional);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    public Result update(UpdateAdditionalRequest updateAdditionalRequest) throws AdditionalAlreadyExistsException, AdditionalNotFoundException {

        checkIfExistsByAdditionalId(updateAdditionalRequest.getAdditionalId());
        checkIsNotExistsByAdditionalName(updateAdditionalRequest.getAdditionalName());
        Additional additional = this.modelMapperService.forRequest().map(updateAdditionalRequest, Additional.class);
        this.additionalRepository.save(additional);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_UPDATED_SUCCESSFULLY + updateAdditionalRequest.getAdditionalId());
    }

    public Result delete(DeleteAdditionalRequest deleteAdditionalRequest) throws AdditionalNotFoundException, AdditionalAlreadyExistsInOrderedAdditionalException {

        checkIfExistsByAdditionalId(deleteAdditionalRequest.getAdditionalId());
        this.orderedAdditionalService.checkIsNotExistsByOrderedAdditional_AdditionalId(deleteAdditionalRequest.getAdditionalId());
        this.additionalRepository.deleteById(deleteAdditionalRequest.getAdditionalId());

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY + deleteAdditionalRequest.getAdditionalId());
    }

    public DataResult<List<AdditionalListDto>> getAll() {

        List<Additional> additionalList = this.additionalRepository.findAll();
        List<AdditionalListDto> result = additionalList.stream().map(additional -> this.modelMapperService.forDto().map(additional, AdditionalListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    public DataResult<GetAdditionalDto> getByAdditionalId(int additionalId) throws AdditionalNotFoundException {

        checkIfExistsByAdditionalId(additionalId);
        Additional addition = this.additionalRepository.getById(additionalId);
        GetAdditionalDto result = this.modelMapperService.forDto().map(addition, GetAdditionalDto.class);

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + additionalId);
    }

    public void checkIfExistsByAdditionalId(int additionalId) throws AdditionalNotFoundException {

        if(!this.additionalRepository.existsByAdditionalId(additionalId)){
            throw new AdditionalNotFoundException(BusinessMessages.AdditionalMessages.ADDITIONAL_ID_NOT_FOUND + additionalId);
        }
    }

    private void checkIsNotExistsByAdditionalName(String additionalName) throws AdditionalAlreadyExistsException {

        if(this.additionalRepository.existsByAdditionalName(additionalName)){
            throw new AdditionalAlreadyExistsException(BusinessMessages.AdditionalMessages.ADDITIONAL_NAME_ALREADY_EXISTS + additionalName);
        }
    }

}