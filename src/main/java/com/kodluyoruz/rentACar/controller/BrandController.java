package com.kodluyoruz.rentACar.controller;

import java.util.List;

import javax.validation.Valid;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.brandExceptions.BrandAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.brandExceptions.BrandNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.BrandExistsInCarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodluyoruz.rentACar.service.BrandService;
import com.kodluyoruz.rentACar.dto.brandDtos.brandResponse.lists.BrandListDto;
import com.kodluyoruz.rentACar.dto.brandDtos.brandResponse.gets.GetBrandDto;
import com.kodluyoruz.rentACar.dto.brandDtos.brandRequests.CreateBrandRequest;
import com.kodluyoruz.rentACar.dto.brandDtos.brandRequests.DeleteBrandRequest;
import com.kodluyoruz.rentACar.dto.brandDtos.brandRequests.UpdateBrandRequest;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
	
	private final BrandService brandService;
	
	@Autowired
	public BrandController(BrandService brandService) {

		this.brandService = brandService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateBrandRequest createBrandRequest) throws BrandAlreadyExistsException {

		return this.brandService.add(createBrandRequest);
	}

	@PutMapping("/update")
	public Result update(@RequestBody @Valid UpdateBrandRequest updateBrandRequest) throws BrandAlreadyExistsException, BrandNotFoundException {

		return this.brandService.update(updateBrandRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody @Valid DeleteBrandRequest deleteBrandRequest) throws BrandNotFoundException, BrandExistsInCarException {

		return this.brandService.delete(deleteBrandRequest);
	}

	@GetMapping("/getById")
	public DataResult<GetBrandDto> getById(@RequestParam int brandId) throws BrandNotFoundException {

		return this.brandService.getById(brandId);
	}

	@GetMapping("/getAll")
	public DataResult<List<BrandListDto>> getAll(){

		return this.brandService.getAll();
	}

}