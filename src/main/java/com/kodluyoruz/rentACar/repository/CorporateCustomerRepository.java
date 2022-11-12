package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.CorporateCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorporateCustomerRepository extends JpaRepository<CorporateCustomer, Integer> {

    boolean existsByCorporateCustomerId(int corporateCustomerId);
    boolean existsByTaxNumber(String taxNumber);
    boolean existsByTaxNumberAndCorporateCustomerIdIsNot(String taxNumber, int corporateCustomerId);
}
