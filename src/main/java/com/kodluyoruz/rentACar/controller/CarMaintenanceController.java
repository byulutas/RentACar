package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.CarNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.CarAlreadyInMaintenanceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.CarMaintenanceNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.MaintenanceReturnDateBeforeTodayException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.CarAlreadyRentedEnteredDateException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.StartDateBeforeTodayException;
import com.kodluyoruz.rentACar.service.CarMaintenanceService;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceResponse.lists.CarMaintenanceListByCarIdDto;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceResponse.lists.CarMaintenanceListDto;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceResponse.gets.GetCarMaintenanceDto;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceRequests.DeleteCarMaintenanceRequest;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carMaintenances")
public class CarMaintenanceController {

    private final CarMaintenanceService carMaintenanceService;

    @Autowired
    public CarMaintenanceController(CarMaintenanceService carMaintenanceService){

        this.carMaintenanceService = carMaintenanceService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCarMaintenanceRequest createCarMaintenanceRequest) throws CarNotFoundException, MaintenanceReturnDateBeforeTodayException, CarAlreadyInMaintenanceException, StartDateBeforeTodayException, CarAlreadyRentedEnteredDateException {

        return this.carMaintenanceService.add(createCarMaintenanceRequest);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Valid UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws CarNotFoundException, MaintenanceReturnDateBeforeTodayException, CarAlreadyInMaintenanceException, CarMaintenanceNotFoundException, StartDateBeforeTodayException, CarAlreadyRentedEnteredDateException {

        return this.carMaintenanceService.update(updateCarMaintenanceRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteCarMaintenanceRequest deleteCarMaintenanceRequest) throws CarMaintenanceNotFoundException {

        return this.carMaintenanceService.delete(deleteCarMaintenanceRequest);
    }

    @GetMapping("/getByCarMaintenanceId")
    public DataResult<GetCarMaintenanceDto> getByCarMaintenanceId(@RequestParam int carMaintenanceId) throws CarMaintenanceNotFoundException {

        return this.carMaintenanceService.getById(carMaintenanceId);
    }

    @GetMapping("/getAllByCarMaintenance_CarId")
    public DataResult<List<CarMaintenanceListByCarIdDto>> getAllByCarMaintenance_CarId(@RequestParam int carId) throws CarNotFoundException {

        return this.carMaintenanceService.getAllByCarMaintenance_CarId(carId);
    }

    @GetMapping("/getAll")
    public DataResult<List<CarMaintenanceListDto>> getAll() {

        return this.carMaintenanceService.getAll();
    }

}