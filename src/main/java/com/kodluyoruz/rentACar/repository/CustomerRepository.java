package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByCustomerId(int customerId);

}
