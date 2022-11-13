package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.CarCrash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarCrashRepository extends JpaRepository<CarCrash, Integer> {

    boolean existsByCar_CarId(int carId);

    List<CarCrash> getAllByCar_CarId(int carId);

    boolean existsByCarCrashId(int carCrashId);

}
