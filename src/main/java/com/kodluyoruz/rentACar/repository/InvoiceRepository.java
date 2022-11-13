package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    boolean existsByInvoiceId(int invoiceId);

    Invoice getInvoiceByInvoiceNo(String invoiceNo);

    boolean existsByInvoiceNo(String invoiceNo);

    boolean existsByPayment_PaymentId(int paymentId);

    Invoice getInvoiceByPayment_PaymentId(int paymentId);

    boolean existsByCustomer_CustomerId(int customerId);

    List<Invoice> getAllByCustomer_CustomerId(int customerId);

    boolean existsByRentalCar_RentalCarId(int rentalCarId);

    List<Invoice> getAllByRentalCar_RentalCarId(int rentalCarId);

    List<Invoice> getByCreationDateBetween(Date startDate, Date endDate);

}
