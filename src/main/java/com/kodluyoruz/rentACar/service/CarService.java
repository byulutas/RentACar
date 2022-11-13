package com.kodluyoruz.rentACar.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.*;
import com.kodluyoruz.rentACar.messaaages.exceptions.colorExceptions.ColorNotFoundException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.carDtos.carRequests.CreateCarRequest;
import com.kodluyoruz.rentACar.dto.carDtos.carRequests.DeleteCarRequest;
import com.kodluyoruz.rentACar.dto.carDtos.carRequests.UpdateCarRequest;
import com.kodluyoruz.rentACar.dto.carDtos.carResponse.lists.*;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.brandExceptions.BrandNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carCrashExceptions.CarExistsInCarCrashException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.CarExistsInCarMaintenanceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.CarAlreadyExistsInRentalCarException;
import com.kodluyoruz.rentACar.repository.CarRepository;
import com.kodluyoruz.rentACar.entity.Car;
import com.kodluyoruz.rentACar.dto.carDtos.carResponse.gets.GetCarDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CarService {

	private final CarRepository carRepository;
	private final ModelMapperService modelMapperService;

	private final BrandService brandService;
	private final ColorService colorService;
	private final RentalCarService rentalCarService;
	private final CarMaintenanceService carMaintenanceService;
	private final CarCrashService carCrashService;

	@Autowired
	public CarService(CarRepository carRepository, ModelMapperService modelMapperService, @Lazy BrandService brandService, @Lazy ColorService colorService, RentalCarService rentalCarService, @Lazy CarMaintenanceService carMaintenanceService, @Lazy CarCrashService carCrashService) {
		this.carRepository = carRepository;
		this.brandService = brandService;
		this.colorService = colorService;
		this.rentalCarService = rentalCarService;
		this.carMaintenanceService = carMaintenanceService;
		this.carCrashService = carCrashService;
		this.modelMapperService = modelMapperService;

	}

	public Result add(CreateCarRequest createCarRequest) throws ModelYearAfterThisYearException, BrandNotFoundException, ColorNotFoundException {

		this.brandService.checkIsExistsByBrandId(createCarRequest.getBrandId());
		this.colorService.checkIsExistsByColorId(createCarRequest.getColorId());
		checkIsModelYearBeforeThisYear(createCarRequest.getModelYear());
		Car car = this.modelMapperService.forRequest().map(createCarRequest, Car.class);
		this.carRepository.save(car);

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
	}

	public Result update(UpdateCarRequest updateCarRequest) throws ModelYearAfterThisYearException, BrandNotFoundException, CarNotFoundException, ColorNotFoundException {

		checkIsExistsByCarId(updateCarRequest.getCarId());
		this.brandService.checkIsExistsByBrandId(updateCarRequest.getBrandId());
		this.colorService.checkIsExistsByColorId(updateCarRequest.getColorId());
		checkIsModelYearBeforeThisYear(updateCarRequest.getModelYear());
		Car car = this.modelMapperService.forRequest().map(updateCarRequest, Car.class);
		this.carRepository.save(car);

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_UPDATED_SUCCESSFULLY + updateCarRequest.getCarId());
	}


	public Result delete(DeleteCarRequest deleteCarRequest) throws CarExistsInCarCrashException, CarExistsInCarMaintenanceException, CarNotFoundException, CarAlreadyExistsInRentalCarException {

		checkIsExistsByCarId(deleteCarRequest.getCarId());
		this.rentalCarService.checkIsNotExistsByRentalCar_CarId(deleteCarRequest.getCarId());
		this.carMaintenanceService.checkIsExistsByCar_CarId(deleteCarRequest.getCarId());
		this.carCrashService.checkIfNotExistsCarCrashByCar_CarId(deleteCarRequest.getCarId());
		this.carRepository.deleteById(deleteCarRequest.getCarId());

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY + deleteCarRequest.getCarId());
	}

	public DataResult<List<CarListDto>> getAll() {

		List<Car> cars = this.carRepository.findAll();
		List<CarListDto> carsDto = cars.stream().map(car -> this.modelMapperService.forDto().map(car, CarListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(carsDto, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
	}

	public void updateKilometer(int carId, int kilometer) throws ReturnKilometerLessThanRentKilometerException, CarNotFoundException {

		checkIsExistsByCarId(carId);
		Car car = this.carRepository.getById(carId);
		checkIfReturnKilometerValid(car.getKilometer(), kilometer);
		car.setKilometer(kilometer);
		this.carRepository.save(car);

	}


	public DataResult<GetCarDto> getById(int id) throws CarNotFoundException {

		checkIsExistsByCarId(id);
		Car car = this.carRepository.getById(id);
		GetCarDto carDto = this.modelMapperService.forDto().map(car, GetCarDto.class);

		return new SuccessDataResult<>(carDto, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
	}


	public DataResult<List<CarListByBrandIdDto>> getAllByCar_BrandId(int brandId) throws BrandNotFoundException {

		this.brandService.checkIsExistsByBrandId(brandId);
		List<Car> cars = this.carRepository.getAllByBrand_BrandId(brandId);
		List<CarListByBrandIdDto> result = cars.stream().map(car -> this.modelMapperService.forDto().map(car, CarListByBrandIdDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(result, BusinessMessages.CarMessages.CAR_LISTED_BY_BRAND_ID + brandId);
	}


	public DataResult<List<CarListByColorIdDto>> getAllByCar_ColorId(int colorId) throws ColorNotFoundException {

		this.colorService.checkIsExistsByColorId(colorId);
		List<Car> cars = this.carRepository.getAllByColor_ColorId(colorId);
		List<CarListByColorIdDto> result = cars.stream().map(car -> this.modelMapperService.forDto().map(car, CarListByColorIdDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(result, BusinessMessages.CarMessages.CAR_LISTED_BY_COLOR_ID + colorId);
	}


	public DataResult<List<CarListByDailyPriceDto>> findByDailyPriceLessThenEqual(double dailyPrice) {

		List<Car> cars = this.carRepository.findByDailyPriceLessThanEqual(dailyPrice);
		List<CarListByDailyPriceDto> response = cars.stream().map(car -> this.modelMapperService.forDto().map(car, CarListByDailyPriceDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(response, BusinessMessages.CarMessages.CAR_LISTED_BY_LESS_THEN_EQUAL + dailyPrice);
	}


	public DataResult<List<CarPagedDto>> getAllPagedCar(int pageNo, int pageSize) throws CarNotFoundException {

		checkIfPageNoAndPageSizeValid(pageNo, pageSize);
		Pageable pageable = PageRequest.of(pageNo-1, pageSize);
		List<Car> cars = this.carRepository.findAll(pageable).getContent();
		List<CarPagedDto> result = cars.stream().map(car -> this.modelMapperService.forDto().map(car, CarPagedDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(result,BusinessMessages.CarMessages.ALL_CARS_PAGED);
	}

	public DataResult<List<CarSortedDto>> getAllSortedCar(int sort) {

		Sort sortList=selectSortedType(sort);
		List<Car> cars = this.carRepository.findAll(sortList);
		List<CarSortedDto> result = cars.stream().map(car -> this.modelMapperService.forDto().map(car, CarSortedDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(result, BusinessMessages.CarMessages.ALL_CARS_SORTED);
	}

	private void checkIsModelYearBeforeThisYear(int modelYear) throws ModelYearAfterThisYearException {

		if(modelYear > LocalDate.now().getYear()){
			throw new ModelYearAfterThisYearException(BusinessMessages.CarMessages.MODEL_YEAR_CANNOT_AFTER_TODAY + modelYear);
		}
	}

	private void checkIfReturnKilometerValid(int beforeKilometer, int afterKilometer) throws ReturnKilometerLessThanRentKilometerException {

		if(beforeKilometer> afterKilometer){
			throw new ReturnKilometerLessThanRentKilometerException(BusinessMessages.CarMessages.DELIVERED_KILOMETER_CANNOT_LESS_THAN_RENTED_KILOMETER);
		}
	}

	private void checkIfPageNoAndPageSizeValid(int pageNo, int pageSize) throws CarNotFoundException {

		if(pageNo <= 0 || pageSize <= 0) {
			throw new CarNotFoundException(BusinessMessages.CarMessages.PAGE_NO_OR_PAGE_SIZE_NOT_VALID);
		}
	}


	public void checkIsExistsByCarId(int carId) throws CarNotFoundException {

		if(!this.carRepository.existsByCarId(carId)) {
			throw new CarNotFoundException(BusinessMessages.CarMessages.CAR_ID_NOT_FOUND + carId);
		}
	}

	public void checkIsNotExistsByCar_BrandId(int brandId) throws BrandExistsInCarException {

		if(this.carRepository.existsByBrand_BrandId(brandId)) {
			throw new BrandExistsInCarException(BusinessMessages.CarMessages.BRAND_ID_ALREADY_EXISTS_IN_THE_CAR_TABLE + brandId);
		}
	}

	public void checkIsNotExistsByCar_ColorId(int colorId) throws ColorExistsInCarException {

		if(this.carRepository.existsByColor_ColorId(colorId)) {
			throw new ColorExistsInCarException(BusinessMessages.CarMessages.COLOR_ID_ALREADY_EXISTS_IN_THE_CAR_TABLE + colorId);
		}
	}

	public double getDailyPriceByCarId(int carId) {

		Car car = this.carRepository.getById(carId);

		return car.getDailyPrice();
	}

	Sort selectSortedType(int sort) {

		if(sort==1) {
			return Sort.by(Sort.Direction.ASC, "dailyPrice");
		}else if(sort==0) {
			return Sort.by(Sort.Direction.DESC, "dailyPrice");
		}else {
			return Sort.by(Sort.Direction.DESC, "dailyPrice");
		}
	}

}