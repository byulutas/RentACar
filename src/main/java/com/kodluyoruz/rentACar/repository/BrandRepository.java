package com.kodluyoruz.rentACar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodluyoruz.rentACar.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

	boolean existsByBrandId(int brandId);
	boolean existsByBrandName(String brandName);

}
