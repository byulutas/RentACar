package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.Additional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdditionalRepository extends JpaRepository<Additional, Integer> {

    boolean existsByAdditionalName(String additionalName);
    boolean existsByAdditionalId(int additionalId);

}
