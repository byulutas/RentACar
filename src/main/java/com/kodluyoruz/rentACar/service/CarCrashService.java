package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashRequests.CreateCarCrashRequest;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashRequests.DeleteCarCrashRequest;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashRequests.UpdateCarCrashRequest;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashResponse.gets.GetCarCrashDto;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashResponse.lists.CarCrashListByCarIdDto;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashResponse.lists.CarCrashListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.carCrashExceptions.CarCrashNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carCrashExceptions.CarExistsInCarCrashException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carCrashExceptions.CrashDateAfterTodayException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.CarNotFoundException;
import com.kodluyoruz.rentACar.repository.CarCrashRepository;
import com.kodluyoruz.rentACar.entity.CarCrash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarCrashService {

    private final CarCrashRepository carCrashRepository;
    private final CarService carService;
    private final ModelMapperService modelMapperService;

    @Autowired
    public CarCrashService(CarCrashRepository carCrashRepository, CarService carService, ModelMapperService modelMapperService) {
        this.carCrashRepository = carCrashRepository;
        this.carService = carService;
        this.modelMapperService = modelMapperService;
    }

    public Result add(CreateCarCrashRequest createCarCrashRequest) throws CrashDateAfterTodayException, CarNotFoundException {

        checkIfCrashDateBeforeToday(createCarCrashRequest.getCrashDate());
        this.carService.checkIsExistsByCarId(createCarCrashRequest.getCarId());
        CarCrash carCrash = this.modelMapperService.forRequest().map(createCarCrashRequest, CarCrash.class);
        this.carCrashRepository.save(carCrash);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    public Result update(UpdateCarCrashRequest updateCarCrashRequest) throws CrashDateAfterTodayException, CarCrashNotFoundException, CarNotFoundException {

        checkIfExistsByCarCrashId(updateCarCrashRequest.getCarCrashId());
        checkIfCrashDateBeforeToday(updateCarCrashRequest.getCrashDate());
        this.carService.checkIsExistsByCarId(updateCarCrashRequest.getCarId());
        CarCrash carCrash = this.modelMapperService.forRequest().map(updateCarCrashRequest, CarCrash.class);
        this.carCrashRepository.save(carCrash);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_UPDATED_SUCCESSFULLY + updateCarCrashRequest.getCarCrashId());
    }

    public Result delete(DeleteCarCrashRequest deleteCarCrashRequest) throws CarCrashNotFoundException {

        checkIfExistsByCarCrashId(deleteCarCrashRequest.getCarCrashId());
        this.carCrashRepository.deleteById(deleteCarCrashRequest.getCarCrashId());

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY + deleteCarCrashRequest.getCarCrashId());
    }

    public DataResult<List<CarCrashListDto>> getAll() {

        List<CarCrash> carCrashes = this.carCrashRepository.findAll();
        List<CarCrashListDto> result = carCrashes.stream().map(carCrash -> this.modelMapperService.forDto().map(carCrash, CarCrashListDto.class)).collect(Collectors.toList());
        for(int i=0;i<carCrashes.size();i++){
            result.get(i).setCarId(carCrashes.get(i).getCar().getCarId());
        }

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    public DataResult<GetCarCrashDto> getById(int carCrashId) throws CarCrashNotFoundException {

        checkIfExistsByCarCrashId(carCrashId);
        CarCrash carCrash = this.carCrashRepository.getById(carCrashId);
        GetCarCrashDto result = this.modelMapperService.forDto().map(carCrash, GetCarCrashDto.class);
        result.setCarId(carCrash.getCar().getCarId());

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + carCrashId);
    }

    public DataResult<List<CarCrashListByCarIdDto>> getCarCrashByCar_CarId(int carId) throws CarNotFoundException {

        this.carService.checkIsExistsByCarId(carId);
        List<CarCrash> carCrashes = this.carCrashRepository.getAllByCar_CarId(carId);
        List<CarCrashListByCarIdDto> result = carCrashes.stream().map(carCrash -> this.modelMapperService.forDto().map(carCrash, CarCrashListByCarIdDto.class)).collect(Collectors.toList());
        for(int i=0;i<carCrashes.size();i++){
            result.get(i).setCarId(carCrashes.get(i).getCar().getCarId());
        }

        return new SuccessDataResult<>(result, BusinessMessages.CarCrashMessages.CAR_CRASH_LISTED_BY_CAR_ID + carId);
    }

    private void checkIfExistsByCarCrashId(int carCrashId) throws CarCrashNotFoundException {

        if(!this.carCrashRepository.existsByCarCrashId(carCrashId)){
            throw new CarCrashNotFoundException(BusinessMessages.CarCrashMessages.CAR_CRASH_ID_NOT_FOUND + carCrashId);
        }
    }

    private void checkIfCrashDateBeforeToday(LocalDate crashDate) throws CrashDateAfterTodayException {

        if(crashDate.isAfter(LocalDate.now())){
            throw new CrashDateAfterTodayException(BusinessMessages.CarCrashMessages.CRASH_DATE_CANNOT_AFTER_TODAY + crashDate);
        }
    }

    public void checkIfNotExistsCarCrashByCar_CarId(int carId) throws CarExistsInCarCrashException {

        if(this.carCrashRepository.existsByCar_CarId(carId)){
            throw new CarExistsInCarCrashException(BusinessMessages.CarCrashMessages.CAR_ID_ALREADY_EXISTS_IN_THE_CAR_CRASH_TABLE + carId);
        }
    }

}