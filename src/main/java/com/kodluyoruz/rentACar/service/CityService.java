package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.cityDtos.cityRequests.CreateCityRequest;
import com.kodluyoruz.rentACar.dto.cityDtos.cityResponse.gets.GetCityDto;
import com.kodluyoruz.rentACar.dto.cityDtos.cityResponse.lists.CityListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.cityExceptions.CityAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.cityExceptions.CityNotFoundException;
import com.kodluyoruz.rentACar.repository.CityRepository;
import com.kodluyoruz.rentACar.entity.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final ModelMapperService modelMapperService;

    @Autowired
    public CityService(CityRepository cityRepository, ModelMapperService modelMapperService) {
        this.cityRepository = cityRepository;
        this.modelMapperService = modelMapperService;
    }

    public DataResult<List<CityListDto>> getAll() {

        List<City> cities = this.cityRepository.findAll();
        List<CityListDto> result = cities.stream().map(city -> this.modelMapperService.forDto().map(city, CityListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }


    public Result add(CreateCityRequest createCityRequest) throws CityAlreadyExistsException {

        checkIsNotExistByCityName(createCityRequest.getCityName());
        City city = this.modelMapperService.forRequest().map(createCityRequest, City.class);
        city.setCityId(0);

        this.cityRepository.save(city);

        return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
    }


    public DataResult<GetCityDto> getByCityId(int cityId) throws CityNotFoundException {

        checkIfExistsByCityId(cityId);
        City city = this.cityRepository.getById(cityId);
        GetCityDto result = this.modelMapperService.forDto().map(city, GetCityDto.class);

        return new SuccessDataResult<>(result,BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + cityId);
    }

    public void checkIfExistsByCityId(int cityId) throws CityNotFoundException {

        if(!this.cityRepository.existsByCityId(cityId)){
            throw new CityNotFoundException(BusinessMessages.CityMessages.CITY_ID_NOT_FOUND + cityId);
        }
    }

    private void checkIsNotExistByCityName(String cityName) throws CityAlreadyExistsException {

        if(this.cityRepository.existsByCityName(cityName)) {
            throw new CityAlreadyExistsException(BusinessMessages.CityMessages.CITY_NAME_ALREADY_EXISTS + cityName);
        }
    }

}