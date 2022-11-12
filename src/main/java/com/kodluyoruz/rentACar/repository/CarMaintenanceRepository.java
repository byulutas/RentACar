package com.kodluyoruz.rentACar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodluyoruz.rentACar.entity.CarMaintenance;

import java.util.List;

@Repository
public interface CarMaintenanceRepository extends JpaRepository<CarMaintenance, Integer>{

    boolean existsByMaintenanceId(int carMaintenanceId);
    boolean existsByCar_CarId(int carId);

    List<CarMaintenance> findAllByCar_CarId(int carId);

}
