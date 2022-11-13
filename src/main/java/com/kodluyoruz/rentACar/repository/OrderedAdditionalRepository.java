package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.OrderedAdditional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedAdditionalRepository extends JpaRepository<OrderedAdditional, Integer> {

    boolean existsByOrderedAdditionalId(int orderedAdditionalId);

    boolean existsByRentalCar_RentalCarId(int rentalCarId);

    List<OrderedAdditional> getAllByRentalCar_RentalCarId(int rentalCarId);

    boolean existsByAdditional_AdditionalId(int additionalId);

    List<OrderedAdditional> getAllByAdditional_AdditionalId(int additionalId);

    List<OrderedAdditional> getAllByAdditional_AdditionalIdAndRentalCar_RentalCarId(int additionalId, int rentalCarId);

}
