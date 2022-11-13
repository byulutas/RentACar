package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.messaaages.exceptions.businessExceptions.BusinessException;
import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.CustomerNotFoundInInvoiceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.InvoiceNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.PaymentNotFoundInInvoiceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.RentalCarNotFoundInInvoiceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.paymentExceptions.PaymentNotFoundException;
import com.kodluyoruz.rentACar.core.utilities.generate.GenerateRandomCode;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.invoiceDtos.invoiceRequests.CreateInvoiceRequest;
import com.kodluyoruz.rentACar.dto.invoiceDtos.invoiceRequests.InvoiceGetDateBetweenRequest;
import com.kodluyoruz.rentACar.dto.invoiceDtos.invoiceResponse.gets.*;
import com.kodluyoruz.rentACar.dto.invoiceDtos.invoiceResponse.lists.InvoiceListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.additionalExceptions.AdditionalNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.corporateCustomerExceptions.CorporateCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.customerExceptions.CustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.individualCustomerExceptions.IndividualCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.RentalCarNotFoundException;
import com.kodluyoruz.rentACar.repository.InvoiceRepository;
import com.kodluyoruz.rentACar.entity.CorporateCustomer;
import com.kodluyoruz.rentACar.entity.IndividualCustomer;
import com.kodluyoruz.rentACar.entity.Invoice;
import com.kodluyoruz.rentACar.entity.RentalCar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CarService carService;
    private final CustomerService customerService;
    private final IndividualCustomerService individualCustomerService;
    private final CorporateCustomerService corporateCustomerService;
    private final RentalCarService rentalCarService;
    private final OrderedAdditionalService orderedAdditionalService;
    private final PaymentService paymentService;
    private final ModelMapperService modelMapperService;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, CarService carService, CustomerService customerService, @Lazy IndividualCustomerService individualCustomerService, @Lazy CorporateCustomerService corporateCustomerService, RentalCarService rentalCarService, ModelMapperService modelMapperService, @Lazy OrderedAdditionalService orderedAdditionalService, PaymentService paymentService) {

        this.invoiceRepository = invoiceRepository;
        this.carService = carService;
        this.customerService = customerService;
        this.individualCustomerService = individualCustomerService;
        this.corporateCustomerService = corporateCustomerService;
        this.rentalCarService = rentalCarService;
        this.orderedAdditionalService = orderedAdditionalService;
        this.modelMapperService = modelMapperService;
        this.paymentService = paymentService;

    }

    public DataResult<List<InvoiceListDto>> getAll() {

        List<Invoice> invoices = this.invoiceRepository.findAll();
        List<InvoiceListDto> result = invoices.stream().map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class)).collect(Collectors.toList());
        manuelFieldSetter(invoices, result);

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    @Transactional(rollbackFor = BusinessException.class)
    public Result add(CreateInvoiceRequest createInvoiceRequest) throws RentalCarNotFoundException {

        this.rentalCarService.checkIsExistsByRentalCarId(createInvoiceRequest.getRentalCarId());
        createInvoiceRequest.setInvoiceNo(generateCode());
        Invoice invoice = this.modelMapperService.forRequest().map(createInvoiceRequest, Invoice.class);
        invoice.setInvoiceId(0);
        invoice.setCustomer(this.customerService.getCustomerById(createInvoiceRequest.getCustomerId()));
        this.invoiceRepository.save(invoice);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }

    public DataResult<GetIndividualCustomerInvoiceByInvoiceIdDto> getIndividualCustomerInvoiceByInvoiceId(int invoiceId) throws IndividualCustomerNotFoundException, InvoiceNotFoundException {

        checkIfInvoiceIdExists(invoiceId);
        Invoice invoice = this.invoiceRepository.getById(invoiceId);
        this.individualCustomerService.checkIfIndividualCustomerIdExists(invoice.getRentalCar().getCustomer().getCustomerId());
        GetIndividualCustomerInvoiceByInvoiceIdDto result = this.modelMapperService.forDto().map(invoice, GetIndividualCustomerInvoiceByInvoiceIdDto.class);
        IndividualCustomer individualCustomer = this.individualCustomerService.getIndividualCustomerById(invoice.getCustomer().getCustomerId());
        result.setFirstName(individualCustomer.getFirstName());
        result.setLastName(individualCustomer.getLastName());
        result.setNationalIdentity(individualCustomer.getNationalIdentity());
        manuelFieldSetter(invoice, result);

        return new SuccessDataResult<>(result, BusinessMessages.InvoiceMessages.INDIVIDUAL_CUSTOMER_INVOICE_LISTED_BY_INVOICE_ID + invoiceId);
    }

    public DataResult<GetCorporateCustomerInvoiceByInvoiceIdDto> getCorporateCustomerInvoiceByInvoiceId(int invoiceId) throws CorporateCustomerNotFoundException, InvoiceNotFoundException {

        checkIfInvoiceIdExists(invoiceId);
        Invoice invoice = this.invoiceRepository.getById(invoiceId);
        this.corporateCustomerService.checkIfCorporateCustomerIdExists(invoice.getRentalCar().getCustomer().getCustomerId());
        GetCorporateCustomerInvoiceByInvoiceIdDto result = this.modelMapperService.forDto().map(invoice, GetCorporateCustomerInvoiceByInvoiceIdDto.class);
        CorporateCustomer corporateCustomer = this.corporateCustomerService.getCorporateCustomerById(invoice.getCustomer().getCustomerId());
        result.setCompanyName(corporateCustomer.getCompanyName());
        result.setTaxNumber(corporateCustomer.getTaxNumber());
        manuelFieldSetter(invoice, result);

        return new SuccessDataResult<>(result, BusinessMessages.InvoiceMessages.CORPORATE_CUSTOMER_INVOICE_LISTED_BY_INVOICE_ID + invoiceId);
    }

    public DataResult<GetIndividualCustomerInvoiceByInvoiceNoDto> getIndividualCustomerInvoiceByInvoiceNo(String invoiceNo) throws IndividualCustomerNotFoundException, InvoiceNotFoundException {

        checkIfInvoiceNoExists(invoiceNo);
        Invoice invoice = this.invoiceRepository.getInvoiceByInvoiceNo(invoiceNo);
        this.individualCustomerService.checkIfIndividualCustomerIdExists(invoice.getRentalCar().getCustomer().getCustomerId());
        GetIndividualCustomerInvoiceByInvoiceNoDto result = this.modelMapperService.forDto().map(invoice, GetIndividualCustomerInvoiceByInvoiceNoDto.class);
        IndividualCustomer individualCustomer = this.individualCustomerService.getIndividualCustomerById(invoice.getCustomer().getCustomerId());
        result.setFirstName(individualCustomer.getFirstName());
        result.setLastName(individualCustomer.getLastName());
        result.setNationalIdentity(individualCustomer.getNationalIdentity());
        manuelFieldSetter(invoice, result);

        return new SuccessDataResult<>(result, BusinessMessages.InvoiceMessages.INDIVIDUAL_CUSTOMER_INVOICE_LISTED_BY_INVOICE_NO + invoiceNo);
    }

    public DataResult<GetCorporateCustomerInvoiceByInvoiceNoDto> getCorporateCustomerInvoiceByInvoiceNo(String invoiceNo) throws CorporateCustomerNotFoundException, InvoiceNotFoundException {

        checkIfInvoiceNoExists(invoiceNo);
        Invoice invoice = this.invoiceRepository.getInvoiceByInvoiceNo(invoiceNo);
        this.corporateCustomerService.checkIfCorporateCustomerIdExists(invoice.getRentalCar().getCustomer().getCustomerId());
        GetCorporateCustomerInvoiceByInvoiceNoDto result = this.modelMapperService.forDto().map(invoice, GetCorporateCustomerInvoiceByInvoiceNoDto.class);
        CorporateCustomer corporateCustomer = this.corporateCustomerService.getCorporateCustomerById(invoice.getCustomer().getCustomerId());
        result.setCompanyName(corporateCustomer.getCompanyName());
        result.setTaxNumber(corporateCustomer.getTaxNumber());
        manuelFieldSetter(invoice, result);

        return new SuccessDataResult<>(result, BusinessMessages.InvoiceMessages.CORPORATE_CUSTOMER_INVOICE_LISTED_BY_INVOICE_NO + invoiceNo);
    }

    public DataResult<GetInvoiceDto> getInvoiceByPayment_PaymentId(int paymentId) throws PaymentNotFoundInInvoiceException, PaymentNotFoundException {

        this.paymentService.checkIfExistsByPaymentId(paymentId);
        checkIfExistsByPaymentId(paymentId);
        Invoice invoice = this.invoiceRepository.getInvoiceByPayment_PaymentId(paymentId);
        GetInvoiceDto result = this.modelMapperService.forDto().map(invoice, GetInvoiceDto.class);
        manuelFieldSetter(invoice, result);

        return new SuccessDataResult<>(result, BusinessMessages.InvoiceMessages.INVOICE_LISTED_BY_PAYMENT_ID + paymentId);
    }

    public DataResult<List<InvoiceListDto>> getAllByRentalCar_RentalCarId(int rentalCarId) throws RentalCarNotFoundException {

        this.rentalCarService.checkIsExistsByRentalCarId(rentalCarId);
        List<Invoice> invoiceList = this.invoiceRepository.getAllByRentalCar_RentalCarId(rentalCarId);
        List<InvoiceListDto> result = invoiceList.stream().map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class)).collect(Collectors.toList());
        manuelFieldSetter(invoiceList, result);

        return new SuccessDataResult<>(result, BusinessMessages.InvoiceMessages.INVOICE_LISTED_BY_RENTAL_ID + rentalCarId);
    }

    public DataResult<List<InvoiceListDto>> getAllByCustomer_CustomerId(int customerId) throws CustomerNotFoundException {

        this.customerService.checkIfCustomerIdExists(customerId);
        List<Invoice> invoiceList = this.invoiceRepository.getAllByCustomer_CustomerId(customerId);
        List<InvoiceListDto> result = invoiceList.stream().map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class)).collect(Collectors.toList());
        manuelFieldSetter(invoiceList, result);

        return new SuccessDataResult<>(result, BusinessMessages.InvoiceMessages.INVOICE_LISTED_BY_CUSTOMER_ID + customerId);
    }

    public DataResult<List<InvoiceListDto>> findByInvoiceDateBetween(InvoiceGetDateBetweenRequest invoiceGetDateBetweenRequest) {

        List<Invoice> invoices = this.invoiceRepository.getByCreationDateBetween(invoiceGetDateBetweenRequest.getStartDate(), invoiceGetDateBetweenRequest.getEndDate());
        List<InvoiceListDto> result = invoices.stream().map(invoice -> this.modelMapperService.forDto().map(invoice, InvoiceListDto.class)).collect(Collectors.toList());
        manuelFieldSetter(invoices, result);

        return new SuccessDataResult<>(result, BusinessMessages.InvoiceMessages.INVOICE_LISTED_BY_BETWEEN_DATE);
    }

    private void manuelFieldSetter(Invoice invoice, GetInvoiceDto result) {

        result.setRentalCarId(invoice.getRentalCar().getRentalCarId());
        result.setCustomerId(invoice.getRentalCar().getCustomer().getCustomerId());
    }

    private void manuelFieldSetter(List<Invoice> invoices, List<InvoiceListDto> invoiceListDtoList){

        for(int i=0; i<invoices.size();i++){
            invoiceListDtoList.get(i).setRentalCarId(invoices.get(i).getRentalCar().getRentalCarId());
            invoiceListDtoList.get(i).setCustomerId(invoices.get(i).getRentalCar().getCustomer().getCustomerId());
        }
    }

    public void createAndAddInvoice(int rentalCarId, int paymentId) throws AdditionalNotFoundException, RentalCarNotFoundException {

        RentalCar rentalCar = this.rentalCarService.getById(rentalCarId);
        int totalDays = this.rentalCarService.getTotalDaysForRental(rentalCar.getStartDate(), rentalCar.getFinishDate());
        double priceOfDays = this.rentalCarService.calculateRentalCarTotalDayPrice(rentalCar.getStartDate(), rentalCar.getFinishDate(), this.carService.getDailyPriceByCarId(rentalCar.getCar().getCarId()));
        double priceOfDiffCity = this.rentalCarService.calculateCarDeliveredToTheSamCity(rentalCar.getRentedCity().getCityId(), rentalCar.getDeliveredCity().getCityId());
        double priceOfAdditionals = this.orderedAdditionalService.getPriceCalculatorForOrderedAdditionalListByRentalCarId(rentalCar.getRentalCarId(), totalDays);
        double totalPrice = priceOfDays + priceOfDiffCity + priceOfAdditionals;
        CreateInvoiceRequest createInvoiceRequest = new CreateInvoiceRequest();
        createInvoiceRequest.setStartDate(rentalCar.getStartDate());
        createInvoiceRequest.setFinishDate(rentalCar.getFinishDate());
        createInvoiceRequest.setTotalRentalDay((short) totalDays);
        createInvoiceRequest.setPriceOfDays(priceOfDays);
        createInvoiceRequest.setPriceOfDiffCity(priceOfDiffCity);
        createInvoiceRequest.setPriceOfAdditionals(priceOfAdditionals);
        createInvoiceRequest.setRentalCarTotalPrice(totalPrice);
        createInvoiceRequest.setRentalCarId(rentalCarId);
        createInvoiceRequest.setCustomerId(rentalCar.getCustomer().getCustomerId());
        createInvoiceRequest.setPaymentId(paymentId);

        add(createInvoiceRequest);
    }

    private String generateCode() {

        while (true){
            String code = GenerateRandomCode.generate();
            if(!this.invoiceRepository.existsByInvoiceNo(code)){
                return code;
            }
        }
    }

    private void checkIfInvoiceIdExists(int invoiceId) throws InvoiceNotFoundException {

        if(!this.invoiceRepository.existsByInvoiceId(invoiceId)){
            throw new InvoiceNotFoundException(BusinessMessages.InvoiceMessages.INVOICE_ID_NOT_FOUND + invoiceId);
        }
    }

    private void checkIfInvoiceNoExists(String invoiceNo) throws InvoiceNotFoundException {

        if(!this.invoiceRepository.existsByInvoiceNo(invoiceNo)){
            throw new InvoiceNotFoundException(BusinessMessages.InvoiceMessages.INVOICE_NO_NOT_FOUND + invoiceNo);
        }
    }

    private void checkIfExistsByPaymentId(int paymentId) throws PaymentNotFoundInInvoiceException {

        if(!this.invoiceRepository.existsByPayment_PaymentId(paymentId)){
            throw new PaymentNotFoundInInvoiceException(BusinessMessages.InvoiceMessages.PAYMENT_ID_NOT_FOUND_IN_THE_INVOICE_TABLE + paymentId);
        }
    }

    public void checkIfNotExistsByCustomer_CustomerId(int customerId) throws CustomerNotFoundInInvoiceException {

        if(this.invoiceRepository.existsByCustomer_CustomerId(customerId)){
            throw new CustomerNotFoundInInvoiceException(BusinessMessages.InvoiceMessages.CUSTOMER_ID_ALREADY_EXISTS_IN_THE_INVOICE_TABLE + customerId);
        }
    }

    public void checkIfNotExistsByRentalCar_RentalCarId(int rentalCarId) throws RentalCarNotFoundInInvoiceException {

        if(this.invoiceRepository.existsByRentalCar_RentalCarId(rentalCarId)){
            throw new RentalCarNotFoundInInvoiceException(BusinessMessages.InvoiceMessages.RENTAL_CAR_ID_ALREADY_EXISTS_IN_THE_INVOICE_TABLE + rentalCarId);
        }
    }

}