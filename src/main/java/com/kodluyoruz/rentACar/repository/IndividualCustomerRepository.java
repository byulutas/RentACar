package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.IndividualCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndividualCustomerRepository extends JpaRepository<IndividualCustomer, Integer> {

    boolean existsByIndividualCustomerId(int individualCustomerId);
    boolean existsByNationalIdentity(String nationalIdentity);
    boolean existsByNationalIdentityAndIndividualCustomerIdIsNot(String nationalIdentity, int individualCustomerId);

}
