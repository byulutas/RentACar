package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.messaaages.exceptions.businessExceptions.BusinessException;
import com.kodluyoruz.rentACar.messaaages.exceptions.paymentExceptions.PaymentNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.paymentExceptions.RentalCarAlreadyExistsInPaymentException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalRequests.CreateOrderedAdditionalRequest;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalRequests.UpdateOrderedAdditionalRequest;
import com.kodluyoruz.rentACar.dto.paymentDtos.paymentRequests.CreatePaymentRequest;
import com.kodluyoruz.rentACar.dto.paymentDtos.paymentResponse.gets.GetPaymentDto;
import com.kodluyoruz.rentACar.dto.paymentDtos.paymentResponse.lists.PaymentListDto;
import com.kodluyoruz.rentACar.dto.rentalCarDtos.rentalCarRequests.*;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.OrderedAdditionalAddModel;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.OrderedAdditionalUpdateModel;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
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
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.*;
import com.kodluyoruz.rentACar.repository.PaymentRepository;
import com.kodluyoruz.rentACar.entity.OrderedAdditional;
import com.kodluyoruz.rentACar.entity.Payment;
import com.kodluyoruz.rentACar.entity.RentalCar;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ModelMapperService modelMapperService;
    private final CarService carService;
    private final RentalCarService rentalCarService;
    private final OrderedAdditionalService orderedAdditionalService;
    private final InvoiceService invoiceService;
    private final PosService posService;
    private final CreditCardService creditCardService;

    public PaymentService(PaymentRepository paymentRepository, ModelMapperService modelMapperService, @Lazy CarService carService, @Lazy RentalCarService rentalCarService, @Lazy OrderedAdditionalService orderedAdditionalService,
                          @Lazy InvoiceService invoiceService, @Lazy PosService posService, CreditCardService creditCardService) {
        this.paymentRepository = paymentRepository;
        this.carService = carService;
        this.modelMapperService = modelMapperService;
        this.rentalCarService = rentalCarService;
        this.orderedAdditionalService = orderedAdditionalService;
        this.invoiceService = invoiceService;
        this.posService = posService;
        this.creditCardService = creditCardService;
    }



    public DataResult<List<PaymentListDto>> getAll() {

        List<Payment> payments = this.paymentRepository.findAll();

        List<PaymentListDto> result = payments.stream().map(payment -> this.modelMapperService.forDto()
                .map(payment, PaymentListDto.class)).collect(Collectors.toList());
        manuelIdSetter(payments, result);

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }


    @Transactional(rollbackFor = BusinessException.class)
    public Result makePaymentForIndividualRentAdd(MakePaymentForIndividualRentAdd makePayment, CreditCardService.CardSaveInformation cardSaveInformation) throws AdditionalQuantityNotValidException, AdditionalNotFoundException, MakePaymentFailedException, CustomerNotFoundException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, IndividualCustomerNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException, RentalCarNotFoundException {

        checkAllValidationsForIndividualAdd(makePayment.getCreateRentalCarRequest(), makePayment.getCreateOrderedAdditionalRequestList());

        double totalPrice = calculateTotalPrice(makePayment.getCreateRentalCarRequest(), makePayment.getCreateOrderedAdditionalRequestList());
        this.posService.payment(makePayment.getCreateCreditCardRequest().getCardNumber(), makePayment.getCreateCreditCardRequest().getCardOwner(), makePayment.getCreateCreditCardRequest().getCardCvv(), makePayment.getCreateCreditCardRequest().getCardExpirationDate(), totalPrice);
        runPaymentSuccessorForIndividualRentAdd(makePayment, totalPrice, cardSaveInformation);
        return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_RENT_CAR_SUCCESSFULLY);
    }


    @Transactional(rollbackFor = BusinessException.class)
    public Result makePaymentForCorporateRentAdd(MakePaymentForCorporateRentAdd makePayment, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, MakePaymentFailedException, AdditionalNotFoundException, AdditionalQuantityNotValidException, CorporateCustomerNotFoundException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException, RentalCarNotFoundException {

        checkAllValidationsForCorporateAdd(makePayment.getCreateRentalCarRequest(), makePayment.getCreateOrderedAdditionalRequestList());
        double totalPrice = calculateTotalPrice(makePayment.getCreateRentalCarRequest(), makePayment.getCreateOrderedAdditionalRequestList());
        this.posService.payment(makePayment.getCreateCreditCardRequest().getCardNumber(), makePayment.getCreateCreditCardRequest().getCardOwner(), makePayment.getCreateCreditCardRequest().getCardCvv(), makePayment.getCreateCreditCardRequest().getCardExpirationDate(), totalPrice);
        runPaymentSuccessorForCorporateRentAdd(makePayment, totalPrice, cardSaveInformation);
        return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_RENT_CAR_SUCCESSFULLY);
    }

    @Transactional(rollbackFor = BusinessException.class)
    public Result makePaymentForIndividualRentUpdate(MakePaymentForIndividualRentUpdate makePaymentForIndividualRentUpdate, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, MakePaymentFailedException, RentalCarNotFoundException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, IndividualCustomerNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException, AdditionalNotFoundException {


        checkAllValidationsForIndividualUpdate(makePaymentForIndividualRentUpdate.getUpdateRentalCarRequest());
        double totalPrice = calculatePriceDifferenceWithPreviousRentalCar(makePaymentForIndividualRentUpdate.getUpdateRentalCarRequest());
        if(totalPrice > 0){
            this.posService.payment(makePaymentForIndividualRentUpdate.getCreateCreditCardRequest().getCardNumber(), makePaymentForIndividualRentUpdate.getCreateCreditCardRequest().getCardOwner(), makePaymentForIndividualRentUpdate.getCreateCreditCardRequest().getCardCvv(), makePaymentForIndividualRentUpdate.getCreateCreditCardRequest().getCardExpirationDate(), totalPrice);
            runPaymentSuccessorForIndividualRentUpdate(makePaymentForIndividualRentUpdate, totalPrice, cardSaveInformation);
            return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_RENT_CAR_UPDATE_SUCCESSFULLY);
        }

        this.rentalCarService.updateForIndividualCustomer(makePaymentForIndividualRentUpdate.getUpdateRentalCarRequest());

        return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_RENT_CAR_UPDATE_SUCCESSFULLY);
    }


    @Transactional(rollbackFor = BusinessException.class)
    public Result makePaymentForCorporateRentUpdate(MakePaymentForCorporateRentUpdate makePaymentForCorporateRentUpdate, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, MakePaymentFailedException, RentalCarNotFoundException, CorporateCustomerNotFoundException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException, AdditionalNotFoundException {

        checkAllValidationsForCorporateUpdate(makePaymentForCorporateRentUpdate.getUpdateRentalCarRequest());
        double totalPrice = calculatePriceDifferenceWithPreviousRentalCar(makePaymentForCorporateRentUpdate.getUpdateRentalCarRequest());
        if(totalPrice > 0){
            this.posService.payment(makePaymentForCorporateRentUpdate.getCreateCreditCardRequest().getCardNumber(), makePaymentForCorporateRentUpdate.getCreateCreditCardRequest().getCardOwner(),
                    makePaymentForCorporateRentUpdate.getCreateCreditCardRequest().getCardCvv(), makePaymentForCorporateRentUpdate.getCreateCreditCardRequest().getCardExpirationDate(), totalPrice);
            runPaymentSuccessorForCorporateRentUpdate(makePaymentForCorporateRentUpdate, totalPrice, cardSaveInformation);

            return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_RENT_CAR_UPDATE_SUCCESSFULLY);
        }

        this.rentalCarService.updateForCorporateCustomer(makePaymentForCorporateRentUpdate.getUpdateRentalCarRequest());

        return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_RENT_CAR_UPDATE_SUCCESSFULLY);
    }


    @Transactional(rollbackFor = BusinessException.class)
    public Result makePaymentForRentDeliveryDateUpdate(MakePaymentForRentDeliveryDateUpdate makePaymentModel, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, MakePaymentFailedException, AdditionalNotFoundException, RentalCarNotFoundException, StartDateBeforeFinishDateException, DeliveredKilometerAlreadyExistsException, CarAlreadyRentedEnteredDateException, RentedKilometerNullException {

        checkAllValidationsForRentDeliveryDateUpdate(makePaymentModel.getUpdateDeliveryDateRequest());

        double totalPrice = calculateLateDeliveryPrice(makePaymentModel.getUpdateDeliveryDateRequest());

        this.posService.payment(makePaymentModel.getCreateCreditCardRequest().getCardNumber(), makePaymentModel.getCreateCreditCardRequest().getCardOwner(),
                makePaymentModel.getCreateCreditCardRequest().getCardCvv(), makePaymentModel.getCreateCreditCardRequest().getCardExpirationDate(), totalPrice);


        runPaymentSuccessorForRentDeliveryDateUpdate(makePaymentModel, totalPrice, cardSaveInformation);

        return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_RENT_DELIVERY_DATE_UPDATE_SUCCESSFULLY);
    }


    @Transactional(rollbackFor = BusinessException.class)
    public Result makePaymentForOrderedAdditionalAdd(OrderedAdditionalAddModel orderedAdditionalAddModel, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, MakePaymentFailedException, AdditionalNotFoundException, AdditionalQuantityNotValidException, RentalCarNotFoundException {

        checkAllValidationsForOrderedAdditionalAdd(orderedAdditionalAddModel.getRentalCarId(), orderedAdditionalAddModel.getCreateOrderedAdditionalRequestList());

        double totalPrice = this.orderedAdditionalService.getPriceCalculatorForOrderedAdditionalList(orderedAdditionalAddModel.getCreateOrderedAdditionalRequestList(),
                this.rentalCarService.getTotalDaysForRental(orderedAdditionalAddModel.getRentalCarId()));

        this.posService.payment(orderedAdditionalAddModel.getCreateCreditCardRequest().getCardNumber(), orderedAdditionalAddModel.getCreateCreditCardRequest().getCardOwner(),
                orderedAdditionalAddModel.getCreateCreditCardRequest().getCardCvv(), orderedAdditionalAddModel.getCreateCreditCardRequest().getCardExpirationDate(), totalPrice);

        runPaymentSuccessorForOrderedAdditionalAdd(orderedAdditionalAddModel, totalPrice, cardSaveInformation);

        return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_ADDITIONAL_SERVICE_ADDING_SUCCESSFULLY);
    }


    @Transactional(rollbackFor = BusinessException.class)
    public Result makePaymentForOrderedAdditionalUpdate(OrderedAdditionalUpdateModel orderedAdditionalUpdateModel, CreditCardService.CardSaveInformation cardSaveInformation) throws OrderedAdditionalNotFoundException, CustomerNotFoundException, MakePaymentFailedException, AdditionalNotFoundException, AdditionalQuantityNotValidException, OrderedAdditionalAlreadyExistsException, RentalCarNotFoundException {

        checkAllValidationsForOrderedAdditionalUpdate(orderedAdditionalUpdateModel.getUpdateOrderedAdditionalRequest());

        double totalPrice = calculatePriceDifferenceWithPreviousOrderedAdditional(orderedAdditionalUpdateModel.getUpdateOrderedAdditionalRequest());

        if(totalPrice > 0){
            this.posService.payment(orderedAdditionalUpdateModel.getCreateCreditCardRequest().getCardNumber(), orderedAdditionalUpdateModel.getCreateCreditCardRequest().getCardOwner(),
                    orderedAdditionalUpdateModel.getCreateCreditCardRequest().getCardCvv(), orderedAdditionalUpdateModel.getCreateCreditCardRequest().getCardExpirationDate(), totalPrice);

            runPaymentSuccessorForOrderedAdditionalUpdate(orderedAdditionalUpdateModel, totalPrice, cardSaveInformation);

            return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_ADDITIONAL_SERVICE_UPDATE_SUCCESSFULLY);
        }

        this.orderedAdditionalService.update(orderedAdditionalUpdateModel.getUpdateOrderedAdditionalRequest());

        return new SuccessResult(BusinessMessages.PaymentMessages.PAYMENT_AND_ADDITIONAL_SERVICE_UPDATE_SUCCESSFULLY);
    }

    @Transactional(rollbackFor = BusinessException.class)
    void runPaymentSuccessorForIndividualRentAdd(MakePaymentForIndividualRentAdd makePayment, double totalPrice, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, AdditionalNotFoundException, RentalCarNotFoundException {
        int rentalCarId = this.rentalCarService.addForIndividualCustomer(makePayment.getCreateRentalCarRequest());
        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setRentalCarId(rentalCarId);
        createPaymentRequest.setTotalPrice(totalPrice);
        makePayment.getCreateCreditCardRequest().setCustomerId(makePayment.getCreateRentalCarRequest().getCustomerId());

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
        payment.setPaymentId(0);

        int paymentId = this.paymentRepository.save(payment).getPaymentId();
        this.creditCardService.checkSaveInformationAndSaveCreditCard(makePayment.getCreateCreditCardRequest(), cardSaveInformation);
        this.orderedAdditionalService.saveOrderedAdditionalList(makePayment.getCreateOrderedAdditionalRequestList(), rentalCarId);
        this.invoiceService.createAndAddInvoice(rentalCarId, paymentId);
    }

    @Transactional(rollbackFor = BusinessException.class)
    void runPaymentSuccessorForCorporateRentAdd(MakePaymentForCorporateRentAdd makePayment, double totalPrice, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, AdditionalNotFoundException, RentalCarNotFoundException {

        int rentalCarId = this.rentalCarService.addForCorporateCustomer(makePayment.getCreateRentalCarRequest());

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setTotalPrice(totalPrice);
        createPaymentRequest.setRentalCarId(rentalCarId);
        makePayment.getCreateCreditCardRequest().setCustomerId(makePayment.getCreateRentalCarRequest().getCustomerId());

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
        payment.setPaymentId(0);

        int paymentId = this.paymentRepository.save(payment).getPaymentId();
        this.creditCardService.checkSaveInformationAndSaveCreditCard(makePayment.getCreateCreditCardRequest(), cardSaveInformation);
        this.orderedAdditionalService.saveOrderedAdditionalList(makePayment.getCreateOrderedAdditionalRequestList(), rentalCarId);
        this.invoiceService.createAndAddInvoice(rentalCarId, paymentId);
    }

    @Transactional(rollbackFor = BusinessException.class)
    void runPaymentSuccessorForIndividualRentUpdate(MakePaymentForIndividualRentUpdate makePayment, double totalPrice, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, RentalCarNotFoundException, AdditionalNotFoundException {

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setRentalCarId(makePayment.getUpdateRentalCarRequest().getRentalCarId());
        createPaymentRequest.setTotalPrice(totalPrice);
        makePayment.getCreateCreditCardRequest().setCustomerId(makePayment.getUpdateRentalCarRequest().getCustomerId());

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
        payment.setPaymentId(0);
        int paymentId = this.paymentRepository.save(payment).getPaymentId();
        this.rentalCarService.updateForIndividualCustomer(makePayment.getUpdateRentalCarRequest());
        this.creditCardService.checkSaveInformationAndSaveCreditCard(makePayment.getCreateCreditCardRequest(), cardSaveInformation);
        this.invoiceService.createAndAddInvoice(makePayment.getUpdateRentalCarRequest().getRentalCarId(), paymentId);
    }

    @Transactional(rollbackFor = BusinessException.class)
    void runPaymentSuccessorForCorporateRentUpdate(MakePaymentForCorporateRentUpdate makePayment, double totalPrice, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, RentalCarNotFoundException, AdditionalNotFoundException {

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setRentalCarId(makePayment.getUpdateRentalCarRequest().getRentalCarId());
        createPaymentRequest.setTotalPrice(totalPrice);
        makePayment.getCreateCreditCardRequest().setCustomerId(makePayment.getUpdateRentalCarRequest().getCustomerId());

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
        payment.setPaymentId(0);


        int paymentId = this.paymentRepository.save(payment).getPaymentId();
        this.rentalCarService.updateForCorporateCustomer(makePayment.getUpdateRentalCarRequest());
        this.creditCardService.checkSaveInformationAndSaveCreditCard(makePayment.getCreateCreditCardRequest(), cardSaveInformation);
        this.invoiceService.createAndAddInvoice(makePayment.getUpdateRentalCarRequest().getRentalCarId(), paymentId);
    }

    @Transactional(rollbackFor = BusinessException.class)
    void runPaymentSuccessorForRentDeliveryDateUpdate(MakePaymentForRentDeliveryDateUpdate makePaymentModel, double totalPrice, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, RentalCarNotFoundException, AdditionalNotFoundException {

        RentalCar rentalCar = this.rentalCarService.getById(makePaymentModel.getUpdateDeliveryDateRequest().getRentalCarId());

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setRentalCarId(makePaymentModel.getUpdateDeliveryDateRequest().getRentalCarId());
        createPaymentRequest.setTotalPrice(totalPrice);
        makePaymentModel.getCreateCreditCardRequest().setCustomerId(rentalCar.getCustomer().getCustomerId());
        rentalCar.setFinishDate(makePaymentModel.getUpdateDeliveryDateRequest().getFinishDate());

        UpdateRentalCarRequest request = this.modelMapperService.forDto().map(rentalCar, UpdateRentalCarRequest.class);
        request.setCustomerId(rentalCar.getCustomer().getCustomerId());
        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
        payment.setPaymentId(0);

        int paymentId = this.paymentRepository.save(payment).getPaymentId();
        this.rentalCarService.updateForIndividualCustomer(request);
        this.creditCardService.checkSaveInformationAndSaveCreditCard(makePaymentModel.getCreateCreditCardRequest(), cardSaveInformation);
        this.invoiceService.createAndAddInvoice(rentalCar.getRentalCarId(), paymentId);
    }

    @Transactional(rollbackFor = BusinessException.class)
    void runPaymentSuccessorForOrderedAdditionalAdd(OrderedAdditionalAddModel orderedAdditionalAddModel, double totalPrice, CreditCardService.CardSaveInformation cardSaveInformation) throws CustomerNotFoundException, RentalCarNotFoundException, AdditionalNotFoundException {

        RentalCar rentalCar = this.rentalCarService.getById(orderedAdditionalAddModel.getRentalCarId());

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setRentalCarId(orderedAdditionalAddModel.getRentalCarId());
        createPaymentRequest.setTotalPrice(totalPrice);
        orderedAdditionalAddModel.getCreateCreditCardRequest().setCustomerId(rentalCar.getCustomer().getCustomerId());

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
        payment.setPaymentId(0);

        int paymentId = this.paymentRepository.save(payment).getPaymentId();
        this.creditCardService.checkSaveInformationAndSaveCreditCard(orderedAdditionalAddModel.getCreateCreditCardRequest(), cardSaveInformation);
        this.orderedAdditionalService.saveOrderedAdditionalList(orderedAdditionalAddModel.getCreateOrderedAdditionalRequestList(), orderedAdditionalAddModel.getRentalCarId());
        this.invoiceService.createAndAddInvoice(orderedAdditionalAddModel.getRentalCarId(), paymentId);
    }

    @Transactional(rollbackFor = BusinessException.class)
    void runPaymentSuccessorForOrderedAdditionalUpdate(OrderedAdditionalUpdateModel orderedAdditionalUpdateModel, double totalPrice, CreditCardService.CardSaveInformation cardSaveInformation) throws OrderedAdditionalNotFoundException, CustomerNotFoundException, RentalCarNotFoundException, AdditionalNotFoundException {

        RentalCar rentalCar = this.rentalCarService.getById(orderedAdditionalUpdateModel.getUpdateOrderedAdditionalRequest().getRentalCarId());

        CreatePaymentRequest createPaymentRequest = new CreatePaymentRequest();
        createPaymentRequest.setRentalCarId(orderedAdditionalUpdateModel.getUpdateOrderedAdditionalRequest().getRentalCarId());
        createPaymentRequest.setTotalPrice(totalPrice);
        orderedAdditionalUpdateModel.getCreateCreditCardRequest().setCustomerId(rentalCar.getCustomer().getCustomerId());

        Payment payment = this.modelMapperService.forRequest().map(createPaymentRequest, Payment.class);
        payment.setPaymentId(0);

        int paymentId = this.paymentRepository.save(payment).getPaymentId();
        this.creditCardService.checkSaveInformationAndSaveCreditCard(orderedAdditionalUpdateModel.getCreateCreditCardRequest(), cardSaveInformation);
        this.orderedAdditionalService.update(orderedAdditionalUpdateModel.getUpdateOrderedAdditionalRequest());
        this.invoiceService.createAndAddInvoice(orderedAdditionalUpdateModel.getUpdateOrderedAdditionalRequest().getRentalCarId(), paymentId);
    }


    public DataResult<GetPaymentDto> getById(int paymentId) throws PaymentNotFoundException {

        checkIfExistsByPaymentId(paymentId);

        Payment payment = this.paymentRepository.getById(paymentId);

        GetPaymentDto result = this.modelMapperService.forDto().map(payment, GetPaymentDto.class);
        result.setRentalCarId(payment.getRentalCar().getRentalCarId());
        result.setInvoiceId(payment.getPaymentId());

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + paymentId );
    }


    public DataResult<List<PaymentListDto>> getAllPaymentByRentalCar_RentalCarId(int rentalCarId) throws RentalCarNotFoundException {

        this.rentalCarService.checkIsExistsByRentalCarId(rentalCarId);

        List<Payment> payments = this.paymentRepository.getAllByRentalCar_RentalCarId(rentalCarId);

        List<PaymentListDto> result = payments.stream().map(payment -> this.modelMapperService.forDto()
                .map(payment, PaymentListDto.class)).collect(Collectors.toList());
        manuelIdSetter(payments, result);

        return new SuccessDataResult<>(result, BusinessMessages.PaymentMessages.PAYMENT_LISTED_BY_RENTAL_CAR_ID + rentalCarId);
    }

    private void checkAllValidationsForIndividualAdd(CreateRentalCarRequest createRentalCarRequest, List<CreateOrderedAdditionalRequest> createOrderedAdditionalRequestList) throws AdditionalQuantityNotValidException, AdditionalNotFoundException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, IndividualCustomerNotFoundException, CustomerNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException {
        this.rentalCarService.checkAllValidationsForIndividualRentAdd(createRentalCarRequest);
        this.orderedAdditionalService.checkAllValidationForAddOrderedAdditionalList(createOrderedAdditionalRequestList);
    }

    private void checkAllValidationsForCorporateAdd(CreateRentalCarRequest createRentalCarRequest, List<CreateOrderedAdditionalRequest> createOrderedAdditionalRequestList) throws AdditionalQuantityNotValidException, AdditionalNotFoundException, CorporateCustomerNotFoundException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, CustomerNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException {
        this.rentalCarService.checkAllValidationsForCorporateRentAdd(createRentalCarRequest);
        this.orderedAdditionalService.checkAllValidationForAddOrderedAdditionalList(createOrderedAdditionalRequestList);
    }

    private void checkAllValidationsForIndividualUpdate(UpdateRentalCarRequest updateRentalCarRequest) throws StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, IndividualCustomerNotFoundException, CustomerNotFoundException, RentalCarNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException {
        this.rentalCarService.checkAllValidationsForIndividualRentUpdate(updateRentalCarRequest);
    }

    private void checkAllValidationsForCorporateUpdate(UpdateRentalCarRequest updateRentalCarRequest) throws CorporateCustomerNotFoundException, StartDateBeforeTodayException, StartDateBeforeFinishDateException, CarAlreadyRentedEnteredDateException, CarNotFoundException, CustomerNotFoundException, RentalCarNotFoundException, CarAlreadyInMaintenanceException, CityNotFoundException {
        this.rentalCarService.checkAllValidationsForCorporateRentUpdate(updateRentalCarRequest);
    }

    private void checkAllValidationsForOrderedAdditionalAdd(int rentalCarId, List<CreateOrderedAdditionalRequest> createOrderedAdditionalRequestList) throws AdditionalQuantityNotValidException, AdditionalNotFoundException, RentalCarNotFoundException {
        this.rentalCarService.checkIsExistsByRentalCarId(rentalCarId);
        this.orderedAdditionalService.checkAllValidationForAddOrderedAdditionalList(createOrderedAdditionalRequestList);
    }

    private void checkAllValidationsForOrderedAdditionalUpdate(UpdateOrderedAdditionalRequest updateOrderedAdditionalRequest) throws OrderedAdditionalAlreadyExistsException, AdditionalQuantityNotValidException, AdditionalNotFoundException, OrderedAdditionalNotFoundException, RentalCarNotFoundException {
        this.orderedAdditionalService.checkIsExistsByOrderedAdditionalId(updateOrderedAdditionalRequest.getOrderedAdditionalId());
        this.rentalCarService.checkIsExistsByRentalCarId(updateOrderedAdditionalRequest.getRentalCarId());
        this.orderedAdditionalService.checkAllValidationForAddOrderedAdditional(updateOrderedAdditionalRequest.getAdditionalId(), updateOrderedAdditionalRequest.getOrderedAdditionalQuantity());
        this.orderedAdditionalService.checkIsOnlyOneOrderedAdditionalByAdditionalIdAndRentalCarIdForUpdate(updateOrderedAdditionalRequest.getAdditionalId(), updateOrderedAdditionalRequest.getRentalCarId());
    }

    private void checkAllValidationsForRentDeliveryDateUpdate(UpdateDeliveryDateRequest updateDeliveryDateRequest) throws DeliveredKilometerAlreadyExistsException, RentedKilometerNullException, CarAlreadyRentedEnteredDateException, StartDateBeforeFinishDateException, RentalCarNotFoundException {

        this.rentalCarService.checkIsExistsByRentalCarId(updateDeliveryDateRequest.getRentalCarId());
        RentalCar rentalCar = this.rentalCarService.getById(updateDeliveryDateRequest.getRentalCarId());

        this.rentalCarService.checkIfFirstDateBeforeSecondDate(rentalCar.getFinishDate(),updateDeliveryDateRequest.getFinishDate());
        this.rentalCarService.checkIfCarAlreadyRentedForDeliveryDateUpdate(rentalCar.getCar().getCarId(), updateDeliveryDateRequest.getFinishDate(), rentalCar.getRentalCarId());
        this.rentalCarService.checkIfRentedKilometerIsNotNull(rentalCar.getRentedKilometer());
        this.rentalCarService.checkIfDeliveredKilometerIsNull(rentalCar.getDeliveredKilometer());
    }


    private double calculateTotalPrice(CreateRentalCarRequest rentalCarRequest, List<CreateOrderedAdditionalRequest> orderedAdditionalRequestList) throws AdditionalNotFoundException {

        int totalDays = this.rentalCarService.getTotalDaysForRental(rentalCarRequest.getStartDate(), rentalCarRequest.getFinishDate());
        double priceOfRental = this.rentalCarService.calculateAndReturnRentPrice(rentalCarRequest.getStartDate(), rentalCarRequest.getFinishDate(),
                this.carService.getDailyPriceByCarId(rentalCarRequest.getCarId()), rentalCarRequest.getRentedCityCityId(), rentalCarRequest.getDeliveredCityId());
        double priceOfAdditionals = this.orderedAdditionalService.getPriceCalculatorForOrderedAdditionalList(orderedAdditionalRequestList,totalDays);

        return priceOfRental + priceOfAdditionals;
    }

    private double calculateLateDeliveryPrice(UpdateDeliveryDateRequest updateDeliveryDateRequest) throws AdditionalNotFoundException, RentalCarNotFoundException {

        RentalCar rentalCar = this.rentalCarService.getById(updateDeliveryDateRequest.getRentalCarId());
        int totalDays = this.rentalCarService.getTotalDaysForRental(rentalCar.getFinishDate(),updateDeliveryDateRequest.getFinishDate());

        double priceOfRental = this.rentalCarService.calculateRentalCarTotalDayPrice(rentalCar.getFinishDate(), updateDeliveryDateRequest.getFinishDate(),
                this.carService.getDailyPriceByCarId(rentalCar.getCar().getCarId()));
        double priceOfAdditionals = this.orderedAdditionalService.getPriceCalculatorForOrderedAdditionalListByRentalCarId(rentalCar.getRentalCarId(), totalDays);

        return priceOfRental + priceOfAdditionals;
    }

    private double calculatePriceDifferenceWithPreviousRentalCar(UpdateRentalCarRequest updateRentalCarRequest) throws RentalCarNotFoundException {

        RentalCar beforeRentalCar = this.rentalCarService.getById(updateRentalCarRequest.getRentalCarId());

        double previousPrice = this.rentalCarService.calculateAndReturnRentPrice(beforeRentalCar.getStartDate(), beforeRentalCar.getFinishDate(), beforeRentalCar.getCar().getDailyPrice(),
                beforeRentalCar.getRentedCity().getCityId(), beforeRentalCar.getDeliveredCity().getCityId());
        double nextPrice = this.rentalCarService.calculateAndReturnRentPrice(updateRentalCarRequest.getStartDate(), updateRentalCarRequest.getFinishDate(),
                this.carService.getDailyPriceByCarId(updateRentalCarRequest.getCarId()), updateRentalCarRequest.getRentedCityId(), updateRentalCarRequest.getDeliveredCityId());

        if(nextPrice>previousPrice){
            return nextPrice-previousPrice;
        }
        return 0;
    }

    private double calculatePriceDifferenceWithPreviousOrderedAdditional(UpdateOrderedAdditionalRequest updateOrderedAdditionalRequest) throws AdditionalNotFoundException {
        OrderedAdditional orderedAdditional = this.orderedAdditionalService.getById(updateOrderedAdditionalRequest.getOrderedAdditionalId());

        double previousPrice = this.orderedAdditionalService.getPriceCalculatorForOrderedAdditional(orderedAdditional.getAdditional().getAdditionalId(),
                orderedAdditional.getOrderedAdditionalQuantity(), this.rentalCarService.getTotalDaysForRental(orderedAdditional.getRentalCar().getRentalCarId()));

        double nextPrice = this.orderedAdditionalService.getPriceCalculatorForOrderedAdditional(updateOrderedAdditionalRequest.getAdditionalId(),
                updateOrderedAdditionalRequest.getOrderedAdditionalQuantity(), this.rentalCarService.getTotalDaysForRental(updateOrderedAdditionalRequest.getRentalCarId()));

        if(nextPrice>previousPrice){
            return nextPrice - previousPrice;
        }
        return 0;
    }


    public void checkIfExistsByPaymentId(int paymentId) throws PaymentNotFoundException {
        if(!this.paymentRepository.existsByPaymentId(paymentId)){
            throw new PaymentNotFoundException(BusinessMessages.PaymentMessages.PAYMENT_ID_NOT_FOUND + paymentId);
        }
    }

    public void checkIfNotExistsRentalCar_RentalCarId(int rentalCarId) throws RentalCarAlreadyExistsInPaymentException {
        if(this.paymentRepository.existsByRentalCar_RentalCarId(rentalCarId)){
            throw new RentalCarAlreadyExistsInPaymentException(BusinessMessages.PaymentMessages.RENTAL_CAR_ID_ALREADY_EXISTS_IN_THE_PAYMENT_TABLE  + rentalCarId);
        }
    }

    private void manuelIdSetter(List<Payment> paymentList, List<PaymentListDto> paymentListDtoList){
        for(int i=0;i<paymentList.size();i++){
            paymentListDtoList.get(i).setRentalCarId(paymentList.get(i).getRentalCar().getRentalCarId());
        }
    }

}
