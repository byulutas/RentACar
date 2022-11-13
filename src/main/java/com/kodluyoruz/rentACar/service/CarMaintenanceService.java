package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceRequests.CreateCarMaintenanceRequest;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceRequests.DeleteCarMaintenanceRequest;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceRequests.UpdateCarMaintenanceRequest;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceResponse.gets.GetCarMaintenanceDto;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceResponse.lists.CarMaintenanceListByCarIdDto;
import com.kodluyoruz.rentACar.dto.carMaintenanceDtos.carMaintenanceResponse.lists.CarMaintenanceListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.CarNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.CarAlreadyInMaintenanceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.CarExistsInCarMaintenanceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.CarMaintenanceNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.MaintenanceReturnDateBeforeTodayException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.CarAlreadyRentedEnteredDateException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.StartDateBeforeTodayException;
import com.kodluyoruz.rentACar.repository.CarMaintenanceRepository;
import com.kodluyoruz.rentACar.entity.CarMaintenance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarMaintenanceService {

	private final CarMaintenanceRepository carMaintenanceRepository;
	private final CarService carService;
	private final RentalCarService rentalCarService;
	private final ModelMapperService modelMapperService;

	@Autowired
	public CarMaintenanceService(CarMaintenanceRepository carMaintenanceRepository, CarService carService, RentalCarService rentalCarService, ModelMapperService modelMapperService){
		this.carMaintenanceRepository = carMaintenanceRepository;
		this.carService = carService;
		this.rentalCarService = rentalCarService;
		this.modelMapperService = modelMapperService;
	}

	public Result add(CreateCarMaintenanceRequest createCarMaintenanceRequest) throws MaintenanceReturnDateBeforeTodayException, CarAlreadyInMaintenanceException, CarNotFoundException, CarAlreadyRentedEnteredDateException, StartDateBeforeTodayException {

		checkAllValidationForCarMaintenanceForAdd(createCarMaintenanceRequest.getReturnDate(),createCarMaintenanceRequest.getCarId());
		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(createCarMaintenanceRequest, CarMaintenance.class);
		carMaintenance.setMaintenanceId(0);
		this.carMaintenanceRepository.save(carMaintenance);

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
	}

	public Result update(UpdateCarMaintenanceRequest updateCarMaintenanceRequest) throws CarMaintenanceNotFoundException, MaintenanceReturnDateBeforeTodayException, CarAlreadyInMaintenanceException, CarNotFoundException, CarAlreadyRentedEnteredDateException, StartDateBeforeTodayException {

		checkIsExistsByCarMaintenanceId(updateCarMaintenanceRequest.getMaintenanceId());
		checkAllValidationForCarMaintenanceForUpdate(updateCarMaintenanceRequest.getReturnDate(), updateCarMaintenanceRequest.getCarId(), updateCarMaintenanceRequest.getMaintenanceId());
		CarMaintenance carMaintenance = this.modelMapperService.forRequest().map(updateCarMaintenanceRequest, CarMaintenance.class);
		this.carMaintenanceRepository.save(carMaintenance);

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_UPDATED_SUCCESSFULLY + updateCarMaintenanceRequest.getMaintenanceId());
	}

	public Result delete(DeleteCarMaintenanceRequest carMaintenanceRequest) throws CarMaintenanceNotFoundException {

		checkIsExistsByCarMaintenanceId(carMaintenanceRequest.getMaintenanceId());
		this.carMaintenanceRepository.deleteById(carMaintenanceRequest.getMaintenanceId());

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY + carMaintenanceRequest.getMaintenanceId());
	}

	public DataResult<List<CarMaintenanceListDto>> getAll() {

		List<CarMaintenance> carMaintenances = this.carMaintenanceRepository.findAll();
		List<CarMaintenanceListDto> result = carMaintenances.stream().map(carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
	}


	public DataResult<GetCarMaintenanceDto> getById(int carMaintenanceId) throws CarMaintenanceNotFoundException {

		checkIsExistsByCarMaintenanceId(carMaintenanceId);
		CarMaintenance carMaintenance = this.carMaintenanceRepository.getById(carMaintenanceId);
		GetCarMaintenanceDto result = this.modelMapperService.forDto().map(carMaintenance, GetCarMaintenanceDto.class);

		return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + carMaintenanceId);
	}


	public DataResult<List<CarMaintenanceListByCarIdDto>> getAllByCarMaintenance_CarId(int carId) throws CarNotFoundException {

		this.carService.checkIsExistsByCarId(carId);
		List<CarMaintenance> carMaintenances = this.carMaintenanceRepository.findAllByCar_CarId(carId);
		List<CarMaintenanceListByCarIdDto> result = carMaintenances.stream().map(carMaintenance -> this.modelMapperService.forDto().map(carMaintenance, CarMaintenanceListByCarIdDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(result, BusinessMessages.CarMaintenanceMessages.CAR_MAINTENANCE_LISTED_BY_CAR_ID + carId);
	}

	private void checkAllValidationForCarMaintenanceForAdd(LocalDate returnDate, int carId) throws CarAlreadyInMaintenanceException, MaintenanceReturnDateBeforeTodayException, CarNotFoundException, CarAlreadyRentedEnteredDateException, StartDateBeforeTodayException {

		checkIfNotReturnDateBeforeToday(returnDate);
		this.carService.checkIsExistsByCarId(carId);
		checkIfNotCarAlreadyInMaintenanceOnTheEnteredDate(carId, LocalDate.now());
		this.rentalCarService.checkIfNotCarAlreadyRentedEnteredDate(carId,LocalDate.now());
		this.rentalCarService.checkIfNotCarAlreadyRentedEnteredDate(carId,returnDate);
		this.rentalCarService.checkIfNotCarAlreadyRentedBetweenStartAndFinishDates(carId, LocalDate.now(), returnDate);

	}

	private void checkAllValidationForCarMaintenanceForUpdate(LocalDate returnDate, int carId, int maintenanceId) throws CarAlreadyInMaintenanceException, MaintenanceReturnDateBeforeTodayException, CarNotFoundException, CarAlreadyRentedEnteredDateException, StartDateBeforeTodayException {

		checkIfNotReturnDateBeforeToday(returnDate);
		this.carService.checkIsExistsByCarId(carId);
		checkIfNotCarAlreadyInMaintenanceOnTheEnteredDateForUpdate(carId, LocalDate.now(), maintenanceId);
		this.rentalCarService.checkIfNotCarAlreadyRentedEnteredDate(carId,LocalDate.now());
		this.rentalCarService.checkIfNotCarAlreadyRentedEnteredDate(carId,returnDate);
		this.rentalCarService.checkIfNotCarAlreadyRentedBetweenStartAndFinishDates(carId, LocalDate.now(), returnDate);

	}

	private void checkIfNotReturnDateBeforeToday(LocalDate returnDate) throws MaintenanceReturnDateBeforeTodayException {

		if(returnDate != null){
			if(returnDate.isBefore(LocalDate.now())){
				throw new MaintenanceReturnDateBeforeTodayException(BusinessMessages.CarMaintenanceMessages.RETURN_DATE_CANNOT_BEFORE_TODAY + returnDate);
			}
		}
	}

	public void checkIfNotCarAlreadyInMaintenanceOnTheEnteredDate(int carId, LocalDate enteredDate) throws CarAlreadyInMaintenanceException {

		List<CarMaintenance> carMaintenances = this.carMaintenanceRepository.findAllByCar_CarId(carId);
		if(carMaintenances != null){
			for (CarMaintenance carMaintenance : carMaintenances){
				if (carMaintenance.getReturnDate()==null){
					if(carMaintenance.getReturnDate().now().plusDays(14).isAfter(enteredDate)){
						throw new CarAlreadyInMaintenanceException(BusinessMessages.CarMaintenanceMessages.CAR_ALREADY_IN_MAINTENANCE);
					}
				}else if(carMaintenance.getReturnDate().isAfter(enteredDate)){
					throw new CarAlreadyInMaintenanceException(BusinessMessages.CarMaintenanceMessages.CAR_ALREADY_IN_MAINTENANCE);
				}
			}
		}
	}

	public void checkIfNotCarAlreadyInMaintenanceOnTheEnteredDateForUpdate(int carId, LocalDate enteredDate, int maintenanceId) throws CarAlreadyInMaintenanceException {

		List<CarMaintenance> carMaintenances = this.carMaintenanceRepository.findAllByCar_CarId(carId);
		if(carMaintenances != null){
			for (CarMaintenance carMaintenance : carMaintenances){
				if(carMaintenance.getMaintenanceId() == maintenanceId){
					continue;
				}
				if (carMaintenance.getReturnDate()==null){
					if(carMaintenance.getReturnDate().now().plusDays(14).isAfter(enteredDate)){
						throw new CarAlreadyInMaintenanceException(BusinessMessages.CarMaintenanceMessages.CAR_ALREADY_IN_MAINTENANCE);
					}
				}else if(carMaintenance.getReturnDate().isAfter(enteredDate)){
					throw new CarAlreadyInMaintenanceException(BusinessMessages.CarMaintenanceMessages.CAR_ALREADY_IN_MAINTENANCE);
				}
			}
		}
	}

	private void checkIsExistsByCarMaintenanceId(int carMaintenanceId) throws CarMaintenanceNotFoundException {

		if(!this.carMaintenanceRepository.existsByMaintenanceId(carMaintenanceId)){
			throw new CarMaintenanceNotFoundException(BusinessMessages.CarMaintenanceMessages.CAR_MAINTENANCE_ID_NOT_FOUND + carMaintenanceId);
		}
	}

	public void checkIsExistsByCar_CarId(int carId) throws CarExistsInCarMaintenanceException {

		if(this.carMaintenanceRepository.existsByCar_CarId(carId)){
			throw new CarExistsInCarMaintenanceException(BusinessMessages.CarMaintenanceMessages.CAR_ID_ALREADY_EXISTS_IN_THE_CAR_MAINTENANCE_TABLE + carId);
		}
	}

}