package com.kodluyoruz.rentACar.controller;

import java.util.List;

import javax.validation.Valid;

import com.kodluyoruz.rentACar.messaaages.exceptions.colorExceptions.ColorNotFoundException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.brandExceptions.BrandNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carCrashExceptions.CarExistsInCarCrashException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.CarNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.ModelYearAfterThisYearException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.CarExistsInCarMaintenanceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.CarAlreadyExistsInRentalCarException;
import com.kodluyoruz.rentACar.dto.carDtos.carResponse.lists.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodluyoruz.rentACar.service.CarService;
import com.kodluyoruz.rentACar.dto.carDtos.carResponse.gets.GetCarDto;
import com.kodluyoruz.rentACar.dto.carDtos.carRequests.CreateCarRequest;
import com.kodluyoruz.rentACar.dto.carDtos.carRequests.DeleteCarRequest;
import com.kodluyoruz.rentACar.dto.carDtos.carRequests.UpdateCarRequest;

@RestController
@RequestMapping("/api/cars")
public class CarController {
	
	private final CarService carService;
	
	@Autowired
	public CarController(CarService carService) {

		this.carService = carService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateCarRequest createCarRequest) throws ColorNotFoundException, ModelYearAfterThisYearException, BrandNotFoundException {

		return this.carService.add(createCarRequest);
	}

	@PutMapping("/update")
	public Result update(@RequestBody @Valid UpdateCarRequest updateCarRequest) throws ColorNotFoundException, CarNotFoundException, ModelYearAfterThisYearException, BrandNotFoundException {

		return this.carService.update(updateCarRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody @Valid DeleteCarRequest deleteCarRequest) throws CarNotFoundException, CarAlreadyExistsInRentalCarException, CarExistsInCarMaintenanceException, CarExistsInCarCrashException {

		return this.carService.delete(deleteCarRequest);
	}

	@GetMapping("/getById")
	public DataResult<GetCarDto> getById(@RequestParam int carId) throws CarNotFoundException {

		return this.carService.getById(carId);
	}

	@GetMapping("/getAllByCar_BrandId")
	public DataResult<List<CarListByBrandIdDto>> getAllByCar_BrandId(@RequestParam int brandId) throws BrandNotFoundException {

		return this.carService.getAllByCar_BrandId(brandId);
	}

	@GetMapping("/getAllByCar_ColorId")
	public DataResult<List<CarListByColorIdDto>> getAllByCar_ColorId(@RequestParam int colorId) throws ColorNotFoundException {

		return this.carService.getAllByCar_ColorId(colorId);
	}

	@GetMapping("findByDailyPriceLessThenEqual")
	public DataResult<List<CarListByDailyPriceDto>> findByDailyPriceLessThenEqual(@RequestParam double dailyPrice){

		return this.carService.findByDailyPriceLessThenEqual(dailyPrice);
	}

	@GetMapping("/getAll")
	public DataResult<List<CarListDto>> getAll(){

		return this.carService.getAll();
	}

	@GetMapping("getAllPagedCar")
	public DataResult<List<CarPagedDto>> getAllPagedCar(@RequestParam int pageNo, int pageSize) throws CarNotFoundException {

		return this.carService.getAllPagedCar(pageNo, pageSize);
	}
	
	@GetMapping("getAllSortedCar")
	public DataResult<List<CarSortedDto>> getAllSortedCar(@RequestParam int sort){

		return this.carService.getAllSortedCar(sort);
	}

}