package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    boolean existsByCreditCardId(int creditCardId);

    boolean existsByCustomer_CustomerId(int customerId);

    List<CreditCard> getAllByCustomer_CustomerId(int customerId);

    boolean existsByCardNumber(String cardNumber);

}
