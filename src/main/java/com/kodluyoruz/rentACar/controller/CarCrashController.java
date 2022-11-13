package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.carCrashExceptions.CarCrashNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carCrashExceptions.CrashDateAfterTodayException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.CarNotFoundException;
import com.kodluyoruz.rentACar.service.CarCrashService;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashResponse.gets.GetCarCrashDto;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashResponse.lists.CarCrashListByCarIdDto;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashResponse.lists.CarCrashListDto;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashRequests.CreateCarCrashRequest;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashRequests.DeleteCarCrashRequest;
import com.kodluyoruz.rentACar.dto.carCrashDtos.carCrashRequests.UpdateCarCrashRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carCrashes")
public class CarCrashController {

    private final CarCrashService carCrashService;

    @Autowired
    public CarCrashController(CarCrashService carCrashService) {

        this.carCrashService = carCrashService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCarCrashRequest createCarCrashRequest) throws CrashDateAfterTodayException, CarNotFoundException {

        return this.carCrashService.add(createCarCrashRequest);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Valid UpdateCarCrashRequest updateCarCrashRequest) throws CrashDateAfterTodayException, CarCrashNotFoundException, CarNotFoundException {

        return this.carCrashService.update(updateCarCrashRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteCarCrashRequest deleteCarCrashRequest) throws CarCrashNotFoundException {

        return this.carCrashService.delete(deleteCarCrashRequest);
    }

    @GetMapping("/getById")
    public DataResult<GetCarCrashDto> getById(@RequestParam int carCrashId) throws CarCrashNotFoundException {

        return this.carCrashService.getById(carCrashId);
    }

    @GetMapping("/getCarCrashByCar_CarId")
    public DataResult<List<CarCrashListByCarIdDto>> getCarCrashByCar_CarId(@RequestParam int carId) throws CarNotFoundException {

        return this.carCrashService.getCarCrashByCar_CarId(carId);
    }

    @GetMapping("/getAll")
    public DataResult<List<CarCrashListDto>> getAll(){

        return this.carCrashService.getAll();
    }

}