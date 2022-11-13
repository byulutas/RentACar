package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.RentalCarNotFoundInInvoiceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.paymentExceptions.RentalCarAlreadyExistsInPaymentException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.CarNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.ReturnKilometerLessThanRentKilometerException;
import com.kodluyoruz.rentACar.messaaages.exceptions.cityExceptions.CityNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.corporateCustomerExceptions.CorporateCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.customerExceptions.CustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.individualCustomerExceptions.IndividualCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.orderedAdditionalExceptions.RentalCarAlreadyExistsInOrderedAdditionalException;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarResponse.lists.*;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.*;
import com.kodluyoruz.rentACar.service.RentalCarService;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarResponse.gets.GetRentalCarDto;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarResponse.gets.GetRentalCarStatusDto;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarRequests.DeleteRentalCarRequest;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarRequests.DeliverTheCarRequest;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarRequests.ReceiveTheCarRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/rentalCars")
public class RentalCarController {

    private final RentalCarService rentalCarService;

    @Autowired
    public RentalCarController(RentalCarService rentalCarService) {
        this.rentalCarService = rentalCarService;
    }


    @GetMapping("/getAll")
    public DataResult<List<RentalCarListDto>> getAll(){
        return this.rentalCarService.getAll();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteRentalCarRequest deleteRentalCarRequest) throws RentalCarAlreadyExistsInOrderedAdditionalException, RentalCarNotFoundInInvoiceException, RentalCarAlreadyExistsInPaymentException, RentalCarNotFoundException {
        return this.rentalCarService.delete(deleteRentalCarRequest);
    }

    @PutMapping("/deliverTheCar")
    public DataResult<GetRentalCarStatusDto> deliverTheCar(@RequestBody @Valid DeliverTheCarRequest deliverTheCarRequest) throws StartDateBeforeTodayException, RentedKilometerAlreadyExistsException, RentalCarNotFoundException {
        return this.rentalCarService.deliverTheCar(deliverTheCarRequest);
    }

    @PutMapping("/receiveTheCar")
    public DataResult<GetRentalCarStatusDto> receiveTheCar(@RequestBody @Valid ReceiveTheCarRequest receiveTheCarRequest) throws DeliveredKilometerAlreadyExistsException, ReturnKilometerLessThanRentKilometerException, CarNotFoundException, RentalCarNotFoundException, RentedKilometerNullException {
        return this.rentalCarService.receiveTheCar(receiveTheCarRequest);
    }

    @GetMapping("/getById")
    public DataResult<GetRentalCarDto> getById(@RequestParam int rentalCarId) throws RentalCarNotFoundException {
        return this.rentalCarService.getByRentalCarId(rentalCarId);
    }

    @GetMapping("/getByRentalCar_CarId")
    public DataResult<List<RentalCarListByCarIdDto>> getAllByRentalCar_CarId(@RequestParam int carId) throws CarNotFoundException {
        return this.rentalCarService.getAllByRentalCar_CarId(carId);
    }

    @GetMapping("/getByRentedCity_CityId")
    public DataResult<List<RentalCarListByRentedCityIdDto>> getAllByRentedCity_CityId(@RequestParam int rentedCityId) throws CityNotFoundException {
        return this.rentalCarService.getAllByRentedCity_CityId(rentedCityId);
    }

    @GetMapping("/getByDeliveredCity_CityId")
    public DataResult<List<RentalCarListByDeliveredCityIdDto>> getAllByDeliveredCity_CityId(@RequestParam int deliveredCityId) throws CityNotFoundException {
        return this.rentalCarService.getAllByDeliveredCity_CityId(deliveredCityId);
    }

    @GetMapping("/getByCustomer_CustomerId")
    public DataResult<List<RentalCarListByCustomerIdDto>> getAllByCustomer_CustomerId(@RequestParam int customerId) throws CustomerNotFoundException {
        return this.rentalCarService.getAllByCustomer_CustomerId(customerId);
    }

    @GetMapping("/getByIndividualCustomer_IndividualCustomerId")
    public DataResult<List<RentalCarListByIndividualCustomerIdDto>> getAllByIndividualCustomer_IndividualCustomerId(@RequestParam int individualCustomerId) throws IndividualCustomerNotFoundException {
        return this.rentalCarService.getAllByIndividualCustomer_IndividualCustomerId(individualCustomerId);
    }

    @GetMapping("/getByCorporateCustomer_CorporateCustomerId")
    public DataResult<List<RentalCarListByCorporateCustomerIdDto>> getAllByCorporateCustomer_CorporateCustomerId(@RequestParam int corporateCustomerId) throws CorporateCustomerNotFoundException {
        return this.rentalCarService.getAllByCorporateCustomer_CorporateCustomerId(corporateCustomerId);
    }

}