package com.kodluyoruz.rentACar.controller;


import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.InvoiceNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.invoiceExceptions.PaymentNotFoundInInvoiceException;
import com.kodluyoruz.rentACar.messaaages.exceptions.paymentExceptions.PaymentNotFoundException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.messaaages.exceptions.corporateCustomerExceptions.CorporateCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.customerExceptions.CustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.individualCustomerExceptions.IndividualCustomerNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.RentalCarNotFoundException;
import com.kodluyoruz.rentACar.dto.invoiceDtos.invoiceResponse.gets.*;
import com.kodluyoruz.rentACar.service.InvoiceService;
import com.kodluyoruz.rentACar.dto.invoiceDtos.invoiceResponse.lists.InvoiceListDto;
import com.kodluyoruz.rentACar.dto.invoiceDtos.invoiceRequests.InvoiceGetDateBetweenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {

        this.invoiceService = invoiceService;
    }


    @GetMapping("/getIndividualCustomerInvoiceByInvoiceId")
    public DataResult<GetIndividualCustomerInvoiceByInvoiceIdDto> getIndividualCustomerInvoiceByInvoiceId(@RequestParam int invoiceId) throws InvoiceNotFoundException, IndividualCustomerNotFoundException {

        return this.invoiceService.getIndividualCustomerInvoiceByInvoiceId(invoiceId);
    }

    @GetMapping("/getCorporateCustomerInvoiceByInvoiceId")
    public DataResult<GetCorporateCustomerInvoiceByInvoiceIdDto> getCorporateCustomerInvoiceByInvoiceId(@RequestParam int invoiceId) throws CorporateCustomerNotFoundException, InvoiceNotFoundException {

        return this.invoiceService.getCorporateCustomerInvoiceByInvoiceId(invoiceId);
    }

    @GetMapping("/getIndividualCustomerInvoiceByInvoiceNo")
    public DataResult<GetIndividualCustomerInvoiceByInvoiceNoDto> getIndividualCustomerInvoiceByInvoiceNo(@RequestParam String invoiceNo) throws InvoiceNotFoundException, IndividualCustomerNotFoundException {

        return this.invoiceService.getIndividualCustomerInvoiceByInvoiceNo(invoiceNo);
    }

    @GetMapping("/getCorporateCustomerInvoiceByInvoiceNo")
    public DataResult<GetCorporateCustomerInvoiceByInvoiceNoDto> getCorporateCustomerInvoiceByInvoiceNo(@RequestParam String invoiceNo) throws CorporateCustomerNotFoundException, InvoiceNotFoundException {

        return this.invoiceService.getCorporateCustomerInvoiceByInvoiceNo(invoiceNo);
    }

    @GetMapping("/getInvoiceByPayment_PaymentId")
    public DataResult<GetInvoiceDto> getInvoiceByPayment_PaymentId(@RequestParam int paymentId) throws PaymentNotFoundInInvoiceException, PaymentNotFoundException {

        return this.invoiceService.getInvoiceByPayment_PaymentId(paymentId);
    }

    @GetMapping("/getAllByRentalCar_RentalCarId")
    public DataResult<List<InvoiceListDto>> getAllByRentalCar_RentalCarId(@RequestParam int rentalCarId) throws RentalCarNotFoundException {

        return this.invoiceService.getAllByRentalCar_RentalCarId(rentalCarId);
    }

    @GetMapping("/getAllByCustomer_CustomerId")
    public DataResult<List<InvoiceListDto>> getAllByCustomer_CustomerId(@RequestParam int customerId) throws CustomerNotFoundException {

        return this.invoiceService.getAllByCustomer_CustomerId(customerId);
    }

    @GetMapping("/getDateBetween")
    public DataResult<List<InvoiceListDto>> findByInvoiceDateBetween(@RequestBody @Valid InvoiceGetDateBetweenRequest invoiceGetDateBetweenRequest){

        return this.invoiceService.findByInvoiceDateBetween(invoiceGetDateBetweenRequest);
    }

    @GetMapping("/getAll")
    public DataResult<List<InvoiceListDto>> getAll(){

        return this.invoiceService.getAll();
    }

}