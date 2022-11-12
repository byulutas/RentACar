package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> getAllByRentalCar_RentalCarId(int rentalCarId);

    boolean existsByPaymentId(int paymentId);
    boolean existsByRentalCar_RentalCarId(int rentalCarId);

}
