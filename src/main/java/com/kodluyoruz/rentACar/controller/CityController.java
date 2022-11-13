package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.cityExceptions.CityAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.cityExceptions.CityNotFoundException;
import com.kodluyoruz.rentACar.service.CityService;
import com.kodluyoruz.rentACar.dto.cityDtos.cityResponse.lists.CityListDto;
import com.kodluyoruz.rentACar.dto.cityDtos.cityResponse.gets.GetCityDto;
import com.kodluyoruz.rentACar.dto.cityDtos.cityRequests.CreateCityRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {

        this.cityService = cityService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateCityRequest createCityRequest) throws CityAlreadyExistsException {

        return this.cityService.add(createCityRequest);
    }

    @GetMapping("getByCityId")
    public DataResult<GetCityDto> getByCityId(@RequestParam int cityId) throws CityNotFoundException {

        return this.cityService.getByCityId(cityId);
    }

    @GetMapping("/getAll")
    public DataResult<List<CityListDto>> getAll(){

        return this.cityService.getAll();
    }


}
