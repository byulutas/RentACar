package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalRequests.CreateOrderedAdditionalRequest;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalRequests.DeleteOrderedAdditionalRequest;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalRequests.UpdateOrderedAdditionalRequest;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalResponse.gets.GetOrderedAdditionalDto;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalResponse.lists.OrderedAdditionalListByAdditionalIdDto;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalResponse.lists.OrderedAdditionalListByRentalCarIdDto;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalResponse.lists.OrderedAdditionalListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.additionalExceptions.AdditionalNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.orderedAdditionalExceptions.*;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.RentalCarNotFoundException;
import com.kodluyoruz.rentACar.repository.OrderedAdditionalRepository;
import com.kodluyoruz.rentACar.entity.OrderedAdditional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderedAdditionalService {

    private final OrderedAdditionalRepository orderedAdditionalRepository;
    private final ModelMapperService modelMapperService;
    private final AdditionalService additionalService;
    private final RentalCarService rentalCarService;

    @Autowired
    public OrderedAdditionalService(OrderedAdditionalRepository orderedAdditionalRepository, ModelMapperService modelMapperService, AdditionalService additionalService, RentalCarService rentalCarService) {

        this.orderedAdditionalRepository = orderedAdditionalRepository;
        this.modelMapperService = modelMapperService;
        this.additionalService = additionalService;
        this.rentalCarService = rentalCarService;

    }

    public Result add(CreateOrderedAdditionalRequest createOrderedAdditionalRequest) throws RentalCarNotFoundException {

        this.rentalCarService.checkIsExistsByRentalCarId(createOrderedAdditionalRequest.getRentalCarId());
        OrderedAdditional orderedAdditional = this.modelMapperService.forRequest().map(createOrderedAdditionalRequest, OrderedAdditional.class);
        orderedAdditional.setOrderedAdditionalId(0);
        this.orderedAdditionalRepository.save(orderedAdditional);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    public Result update(UpdateOrderedAdditionalRequest updateOrderedAdditionalRequest) throws OrderedAdditionalNotFoundException {

        checkIsExistsByOrderedAdditionalId(updateOrderedAdditionalRequest.getOrderedAdditionalId());
        OrderedAdditional orderedAdditional = this.modelMapperService.forRequest().map(updateOrderedAdditionalRequest, OrderedAdditional.class);
        orderedAdditional.setOrderedAdditionalId(updateOrderedAdditionalRequest.getOrderedAdditionalId());
        this.orderedAdditionalRepository.save(orderedAdditional);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_UPDATED_SUCCESSFULLY + updateOrderedAdditionalRequest.getOrderedAdditionalId());
    }

    public Result delete(DeleteOrderedAdditionalRequest deleteOrderedAdditionalRequest) throws OrderedAdditionalNotFoundException {

        checkIsExistsByOrderedAdditionalId(deleteOrderedAdditionalRequest.getOrderedAdditionalId());
        OrderedAdditional orderedAdditional = this.orderedAdditionalRepository.getById(deleteOrderedAdditionalRequest.getOrderedAdditionalId());
        this.orderedAdditionalRepository.deleteById(orderedAdditional.getOrderedAdditionalId());

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY + deleteOrderedAdditionalRequest.getOrderedAdditionalId());
    }

    public DataResult<List<OrderedAdditionalListDto>> getAll() {

        List<OrderedAdditional> orderedAdditionalList = this.orderedAdditionalRepository.findAll();
        List<OrderedAdditionalListDto> result = orderedAdditionalList.stream().map(orderedAdditional -> this.modelMapperService.forDto().map(orderedAdditional, OrderedAdditionalListDto.class)).collect(Collectors.toList());
        for(int i = 0; i < result.size(); i++){
            result.get(i).setRentalCarId(orderedAdditionalList.get(i).getRentalCar().getRentalCarId());
        }

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    public DataResult<GetOrderedAdditionalDto> getByOrderedAdditionalId(int orderedAdditionalId) throws OrderedAdditionalNotFoundException {

        checkIsExistsByOrderedAdditionalId(orderedAdditionalId);
        OrderedAdditional orderedAdditional = this.orderedAdditionalRepository.getById(orderedAdditionalId);
        GetOrderedAdditionalDto result = this.modelMapperService.forDto().map(orderedAdditional, GetOrderedAdditionalDto.class);
        if(result != null) {
            result.setRentalCarId(orderedAdditional.getRentalCar().getRentalCarId());
        }

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + orderedAdditionalId);
    }

    public DataResult<List<OrderedAdditionalListByRentalCarIdDto>> getByOrderedAdditional_RentalCarId(int rentalCarId) throws RentalCarNotFoundException {

        this.rentalCarService.checkIsExistsByRentalCarId(rentalCarId);
        List<OrderedAdditional> orderedAdditionalList = this.orderedAdditionalRepository.getAllByRentalCar_RentalCarId(rentalCarId);
        List<OrderedAdditionalListByRentalCarIdDto> result = orderedAdditionalList.stream().map(orderedAdditional -> this.modelMapperService.forDto().map(orderedAdditional, OrderedAdditionalListByRentalCarIdDto.class)).collect(Collectors.toList());
        if(result.size() != 0){
            for(int i = 0; i < result.size(); i++) {
                result.get(i).setRentalCarId(orderedAdditionalList.get(i).getRentalCar().getRentalCarId());
            }
        }

        return new SuccessDataResult<>(result, BusinessMessages.OrderedAdditionalMessages.ORDERED_ADDITIONAL_LISTED_BY_RENTAL_CAR_ID + rentalCarId);
    }

    public DataResult<List<OrderedAdditionalListByAdditionalIdDto>> getByOrderedAdditional_AdditionalId(int additionalId) throws AdditionalNotFoundException {

        this.additionalService.checkIfExistsByAdditionalId(additionalId);
        List<OrderedAdditional> orderedAdditionalList = this.orderedAdditionalRepository.getAllByAdditional_AdditionalId(additionalId);
        List<OrderedAdditionalListByAdditionalIdDto> result = orderedAdditionalList.stream().map(orderedAdditional -> this.modelMapperService.forDto().map(orderedAdditional, OrderedAdditionalListByAdditionalIdDto.class)).collect(Collectors.toList());
        if(result.size() != 0){
            for(int i = 0; i < result.size(); i++){
                result.get(i).setRentalCarId(orderedAdditionalList.get(i).getRentalCar().getRentalCarId());
            }
        }

        return new SuccessDataResult<>(result, BusinessMessages.OrderedAdditionalMessages.ORDERED_ADDITIONAL_LISTED_BY_ADDITIONAL_ID + additionalId);
    }

    public void saveOrderedAdditionalList(List<CreateOrderedAdditionalRequest> createOrderedAdditionalRequestList, int rentalCarId) throws RentalCarNotFoundException {

        for(CreateOrderedAdditionalRequest createOrderedAdditionalRequest : createOrderedAdditionalRequestList){
            createOrderedAdditionalRequest.setRentalCarId(rentalCarId);
            add(createOrderedAdditionalRequest);
        }
    }

    public double getPriceCalculatorForOrderedAdditionalListByRentalCarId(int rentalCarId, int totalDays) throws AdditionalNotFoundException {

        List<OrderedAdditional> orderedAdditionalList = this.orderedAdditionalRepository.getAllByRentalCar_RentalCarId(rentalCarId);
        double totalPrice = 0;
        if(orderedAdditionalList != null){
            for (OrderedAdditional orderedAdditional : orderedAdditionalList){
                totalPrice += getPriceCalculatorForOrderedAdditional(orderedAdditional.getAdditional().getAdditionalId(), orderedAdditional.getOrderedAdditionalQuantity(), totalDays);
            }
        }

        return totalPrice;
    }

    public double getPriceCalculatorForOrderedAdditionalList(List<CreateOrderedAdditionalRequest> createOrderedAdditionalRequestList, int totalDays) throws AdditionalNotFoundException {

        double totalPrice = 0;
        if(createOrderedAdditionalRequestList != null){
            for(CreateOrderedAdditionalRequest orderedAdditionalList : createOrderedAdditionalRequestList) {
                totalPrice += getPriceCalculatorForOrderedAdditional(orderedAdditionalList.getAdditionalId(),orderedAdditionalList.getOrderedAdditionalQuantity(), totalDays);
            }
        }

        return totalPrice;
    }

    public double getPriceCalculatorForOrderedAdditional(int additionalId, double orderedAdditionalQuantity, int totalDays) throws AdditionalNotFoundException {

        double dailyPrice = this.additionalService.getByAdditionalId(additionalId).getData().getAdditionalDailyPrice();

        return dailyPrice * orderedAdditionalQuantity * totalDays;
    }

    public OrderedAdditional getById(int orderedAdditionalId){
        return this.orderedAdditionalRepository.getById(orderedAdditionalId);
    }

    public void checkAllValidationForAddOrderedAdditional(int additionalId, int orderedAdditionalQuantity) throws AdditionalQuantityNotValidException, AdditionalNotFoundException {

        this.additionalService.checkIfExistsByAdditionalId(additionalId);
        int maxUnitsPerRental = this.additionalService.getByAdditionalId(additionalId).getData().getMaxUnitsPerRental();
        if(orderedAdditionalQuantity > maxUnitsPerRental || orderedAdditionalQuantity < 1){
            throw new AdditionalQuantityNotValidException(BusinessMessages.OrderedAdditionalMessages.ADDITIONAL_QUANTITY_NOT_VALID + maxUnitsPerRental);
        }
    }

    public void checkAllValidationForAddOrderedAdditionalList(List<CreateOrderedAdditionalRequest> orderedAdditionalRequestList) throws AdditionalQuantityNotValidException, AdditionalNotFoundException {

        for (CreateOrderedAdditionalRequest orderedAdditionalRequest : orderedAdditionalRequestList){
            checkAllValidationForAddOrderedAdditional(orderedAdditionalRequest.getAdditionalId(), orderedAdditionalRequest.getOrderedAdditionalQuantity());
        }
    }

    public void checkIsOnlyOneOrderedAdditionalByAdditionalIdAndRentalCarIdForUpdate(int additionalId, int rentalCarId) throws OrderedAdditionalAlreadyExistsException {

        if(this.orderedAdditionalRepository.getAllByAdditional_AdditionalIdAndRentalCar_RentalCarId(additionalId, rentalCarId).size() > 1){
            throw new OrderedAdditionalAlreadyExistsException(BusinessMessages.OrderedAdditionalMessages.ORDERED_ADDITIONAL_ALREADY_EXISTS);
        }
    }

    public void checkIsExistsByOrderedAdditionalId(int orderedAdditionalId) throws OrderedAdditionalNotFoundException {

        if(!this.orderedAdditionalRepository.existsByOrderedAdditionalId(orderedAdditionalId)){
            throw new OrderedAdditionalNotFoundException(BusinessMessages.OrderedAdditionalMessages.ORDERED_ADDITIONAL_ID_NOT_FOUND + orderedAdditionalId);
        }
    }

    public void checkIsNotExistsByOrderedAdditional_RentalCarId(int rentalCarId) throws RentalCarAlreadyExistsInOrderedAdditionalException {

        if(this.orderedAdditionalRepository.existsByRentalCar_RentalCarId(rentalCarId)){
            throw new RentalCarAlreadyExistsInOrderedAdditionalException(BusinessMessages.OrderedAdditionalMessages.RENTAL_CAR_ID_ALREADY_EXISTS_IN_THE_ORDERED_ADDITIONAL_TABLE + rentalCarId);
        }
    }

    public void checkIsNotExistsByOrderedAdditional_AdditionalId(int additionalId) throws AdditionalAlreadyExistsInOrderedAdditionalException {

        if(this.orderedAdditionalRepository.existsByAdditional_AdditionalId(additionalId)){
            throw new AdditionalAlreadyExistsInOrderedAdditionalException(BusinessMessages.OrderedAdditionalMessages.ADDITIONAL_ID_ALREADY_EXISTS_IN_THE_ORDERED_ADDITIONAL_TABLE+ additionalId);
        }
    }

}