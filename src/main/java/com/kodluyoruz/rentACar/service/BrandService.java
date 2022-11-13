package com.kodluyoruz.rentACar.service;

import java.util.List;
import java.util.stream.Collectors;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.brandDtos.brandRequests.CreateBrandRequest;
import com.kodluyoruz.rentACar.dto.brandDtos.brandRequests.DeleteBrandRequest;
import com.kodluyoruz.rentACar.dto.brandDtos.brandRequests.UpdateBrandRequest;
import com.kodluyoruz.rentACar.dto.brandDtos.brandResponse.gets.GetBrandDto;
import com.kodluyoruz.rentACar.dto.brandDtos.brandResponse.lists.BrandListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.brandExceptions.BrandAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.brandExceptions.BrandNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.BrandExistsInCarException;
import com.kodluyoruz.rentACar.repository.BrandRepository;
import com.kodluyoruz.rentACar.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

	private final BrandRepository brandRepository;
	private final CarService carService;
	private final ModelMapperService modelMapperService;

	@Autowired
	public BrandService(BrandRepository brandRepository, ModelMapperService modelMapperService, CarService carService) {
		this.brandRepository = brandRepository;
		this.carService = carService;
		this.modelMapperService = modelMapperService;
	}

	public Result add(CreateBrandRequest createBrandRequest) throws BrandAlreadyExistsException {

		checkIsNotExistByBrandName(createBrandRequest.getBrandName());
		Brand brand = this.modelMapperService.forRequest().map(createBrandRequest, Brand.class);
		this.brandRepository.save(brand);

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
	}

	public Result update(UpdateBrandRequest updateBrandRequest) throws BrandAlreadyExistsException, BrandNotFoundException {

		checkIsExistsByBrandId(updateBrandRequest.getBrandId());
		checkIsNotExistByBrandName(updateBrandRequest.getBrandName());
		Brand brand = this.modelMapperService.forRequest().map(updateBrandRequest, Brand.class);
		this.brandRepository.save(brand);

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_UPDATED_SUCCESSFULLY + updateBrandRequest.getBrandId());
	}

	public Result delete(DeleteBrandRequest deleteBrandRequest) throws BrandNotFoundException, BrandExistsInCarException {

		checkIsExistsByBrandId(deleteBrandRequest.getBrandId());
		this.carService.checkIsNotExistsByCar_BrandId(deleteBrandRequest.getBrandId());
		this.brandRepository.deleteById(deleteBrandRequest.getBrandId());

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY + deleteBrandRequest.getBrandId());
	}

	public DataResult<List<BrandListDto>> getAll() {

		List<Brand> brands = this.brandRepository.findAll();
		List<BrandListDto> result = brands.stream().map(brand -> this.modelMapperService.forDto().map(brand, BrandListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
	}

	public DataResult<GetBrandDto> getById(int id) throws BrandNotFoundException {

		checkIsExistsByBrandId(id);
		Brand brand = this.brandRepository.getById(id);
		GetBrandDto getBrandDto = this.modelMapperService.forDto().map(brand, GetBrandDto.class);

		return new SuccessDataResult<>(getBrandDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + id);
	}

	public void checkIsExistsByBrandId(int id) throws BrandNotFoundException {

		if(!this.brandRepository.existsByBrandId(id)) {
			throw new BrandNotFoundException(BusinessMessages.BrandMessages.BRAND_ID_NOT_FOUND + id);
		}
	}

	public void checkIsNotExistByBrandName(String name) throws BrandAlreadyExistsException {

		if(this.brandRepository.existsByBrandName(name)) {
			throw new BrandAlreadyExistsException(BusinessMessages.BrandMessages.BRAND_NAME_ALREADY_EXISTS + name);
		}
	}

}