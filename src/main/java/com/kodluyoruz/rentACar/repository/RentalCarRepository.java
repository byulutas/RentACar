package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.RentalCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalCarRepository extends JpaRepository<RentalCar, Integer> {

    boolean existsByRentalCarId(int rentalCarId);

    List<RentalCar> getAllByRentedCity_CityId(int rentedCityId);

    boolean existsByCar_CarId(int carId);

    List<RentalCar> getAllByCar_CarId(int carId);

    boolean existsByCustomer_CustomerId(int customerId);

    List<RentalCar> getAllByCustomer_CustomerId(int customerId);

    boolean existsByRentalCarIdAndCar_CarId(int rentalCarId, int carId);

    List<RentalCar> getAllByDeliveredCity_CityId(int deliveredCityId);

}
