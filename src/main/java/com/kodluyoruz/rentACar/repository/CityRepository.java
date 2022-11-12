package com.kodluyoruz.rentACar.repository;

import com.kodluyoruz.rentACar.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    boolean existsByCityId(int cityId);
    boolean existsByCityName(String name);

}
