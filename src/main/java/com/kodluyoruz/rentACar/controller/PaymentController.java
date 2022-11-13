package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.messaaages.exceptions.paymentExceptions.PaymentNotFoundException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarRequests.*;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.*;
import com.kodluyoruz.rentACar.service.CreditCardService;
import com.kodluyoruz.rentACar.messaaages.exceptions.PosServiceExceptions.MakePaymentFailedException;
import com.kodluyoruz.rentACar.messaaages.exceptions.additionalExceptions.AdditionalNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.CarNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carMaintenanceExceptions.CarAlreadyInMaintenanceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.cityExceptions.CityNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.corporateCustomerExceptions.CorporateCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.customerExceptions.CustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.individualCustomerExceptions.IndividualCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.orderedAdditionalExceptions.AdditionalQuantityNotValidException;
import com.kodluyoruz.rentACar.messaaages.exceptions.orderedAdditionalExceptions.OrderedAdditionalAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.orderedAdditionalExceptions.OrderedAdditionalNotFoundException;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.OrderedAdditionalAddModel;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.OrderedAdditionalUpdateModel;
import com.kodluyoruz.rentACar.service.PaymentService;
import com.kodluyoruz.rentACar.dto.paymentDtos.paymentResponse.gets.GetPaymentDto;
import com.kodluyoruz.rentACar.dto.paymentDtos.paymentResponse.lists.PaymentListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @PostMapping("/makePaymentForIndividualRentAdd")
    public Result makePaymentForIndividualRentAdd(@RequestBody @Valid MakePaymentForIndividualRentAdd makePaymentForIndividualRentAdd, @RequestParam CreditCardService.CardSaveInformation cardSaveInformation) throws AdditionalQuantityNotValidException, AdditionalNotFoundException, CustomerNotFoundException, MakePaymentFailedException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, IndividualCustomerNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException, RentalCarNotFoundException {

        return this.paymentService.makePaymentForIndividualRentAdd(makePaymentForIndividualRentAdd, cardSaveInformation);
    }

    @PostMapping("/makePaymentForCorporateRentAdd")
    public Result makePaymentForCorporateRentAdd(@RequestBody @Valid MakePaymentForCorporateRentAdd makePaymentForCorporateRentAdd, @RequestParam CreditCardService.CardSaveInformation cardSaveInformation) throws AdditionalQuantityNotValidException, AdditionalNotFoundException, CustomerNotFoundException, MakePaymentFailedException, CorporateCustomerNotFoundException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException, RentalCarNotFoundException {

        return this.paymentService.makePaymentForCorporateRentAdd(makePaymentForCorporateRentAdd, cardSaveInformation);
    }

    @PutMapping("/makePaymentForIndividualRentUpdate")
    public Result makePaymentForIndividualRentUpdate(@RequestBody @Valid MakePaymentForIndividualRentUpdate makePaymentForIndividualRentUpdate, @RequestParam CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, MakePaymentFailedException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, IndividualCustomerNotFoundException, RentalCarNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException, AdditionalNotFoundException {

        return this.paymentService.makePaymentForIndividualRentUpdate(makePaymentForIndividualRentUpdate, cardSaveInformation);
    }

    @PutMapping("/makePaymentForCorporateRentUpdate")
    public Result makePaymentForCorporateRentUpdate(@RequestBody @Valid MakePaymentForCorporateRentUpdate makePaymentForCorporateRentUpdate, @RequestParam CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, MakePaymentFailedException, CorporateCustomerNotFoundException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, RentalCarNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException, AdditionalNotFoundException {

        return this.paymentService.makePaymentForCorporateRentUpdate(makePaymentForCorporateRentUpdate, cardSaveInformation);
    }

    @PutMapping("/makePaymentForRentDeliveryDateUpdate")
    public Result makePaymentForRentDeliveryDateUpdate(@RequestBody @Valid MakePaymentForRentDeliveryDateUpdate makePaymentForRentDeliveryDateUpdate, @RequestParam CreditCardService.CardSaveInformation cardSaveInformation) throws AdditionalNotFoundException, CustomerNotFoundException, MakePaymentFailedException, StartDateBeforeFinishDateException, DeliveredKilometerAlreadyExistsException, CarAlreadyRentedEnteredDateException, RentalCarNotFoundException, RentedKilometerNullException {

        return this.paymentService.makePaymentForRentDeliveryDateUpdate(makePaymentForRentDeliveryDateUpdate, cardSaveInformation);
    }

    @PostMapping("/makePaymentForOrderedAdditionalAdd")
    public Result makePaymentForOrderedAdditionalAdd(@RequestBody @Valid OrderedAdditionalAddModel orderedAdditionalAddModel, @RequestParam CreditCardService.CardSaveInformation cardSaveInformation) throws AdditionalQuantityNotValidException, AdditionalNotFoundException, CustomerNotFoundException, MakePaymentFailedException, RentalCarNotFoundException {

        return this.paymentService.makePaymentForOrderedAdditionalAdd(orderedAdditionalAddModel, cardSaveInformation);
    }

    @PutMapping("/makePaymentForOrderedAdditionalUpdate")
    public Result makePaymentForOrderedAdditionalUpdate(@RequestBody @Valid OrderedAdditionalUpdateModel orderedAdditionalUpdateModel, @RequestParam CreditCardService.CardSaveInformation cardSaveInformation) throws OrderedAdditionalNotFoundException, AdditionalQuantityNotValidException, AdditionalNotFoundException, OrderedAdditionalAlreadyExistsException, CustomerNotFoundException, MakePaymentFailedException, RentalCarNotFoundException {

        return this.paymentService.makePaymentForOrderedAdditionalUpdate(orderedAdditionalUpdateModel, cardSaveInformation);
    }

    @GetMapping("/getById")
    public DataResult<GetPaymentDto> getById(@RequestParam int paymentId) throws PaymentNotFoundException {

        return this.paymentService.getById(paymentId);
    }

    @GetMapping("/getAllByRentalCar_RentalCarId")
    public DataResult<List<PaymentListDto>> getAllPaymentByRentalCar_RentalCarId(@RequestParam int rentalCarId) throws RentalCarNotFoundException {

        return this.paymentService.getAllPaymentByRentalCar_RentalCarId(rentalCarId);
    }

    @GetMapping("/getAll")
    public DataResult<List<PaymentListDto>> getAll(){

        return this.paymentService.getAll();
    }

}