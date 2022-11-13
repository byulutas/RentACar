package com.kodluyoruz.rentACar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kodluyoruz.rentACar.entity.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer>{

	boolean existsByColorId(int colorId);

	boolean existsByColorName(String colorName);

}
