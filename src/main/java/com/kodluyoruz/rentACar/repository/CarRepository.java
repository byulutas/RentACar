package com.kodluyoruz.rentACar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodluyoruz.rentACar.entity.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer>{
	
	boolean existsByCarId(int carId);

	boolean existsByBrand_BrandId(int brandId);

	List<Car> getAllByBrand_BrandId(int brandId);

	boolean existsByColor_ColorId(int colorId);

	List<Car> getAllByColor_ColorId(int colorId);

	List<Car> findByDailyPriceLessThanEqual(double dailyPrice);

}
