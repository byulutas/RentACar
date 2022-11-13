package com.kodluyoruz.rentACar.controller;

import java.util.List;

import javax.validation.Valid;

import com.kodluyoruz.rentACar.messaaages.exceptions.colorExceptions.ColorAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.colorExceptions.ColorNotFoundException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.ColorExistsInCarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodluyoruz.rentACar.service.ColorService;
import com.kodluyoruz.rentACar.dto.colorDtos.colorResponse.lists.ColorListDto;
import com.kodluyoruz.rentACar.dto.colorDtos.colorResponse.gets.GetColorDto;
import com.kodluyoruz.rentACar.dto.colorDtos.colorRequests.CreateColorRequest;
import com.kodluyoruz.rentACar.dto.colorDtos.colorRequests.DeleteColorRequest;
import com.kodluyoruz.rentACar.dto.colorDtos.colorRequests.UpdateColorRequest;

@RestController
@RequestMapping("/api/colors")
public class ColorController {
	
	private final ColorService colorService;
	
	@Autowired
	public ColorController(ColorService colorService) {

		this.colorService = colorService;
	}

	@PostMapping("/add")
	public Result add(@RequestBody @Valid CreateColorRequest createColorRequest) throws ColorAlreadyExistsException {

		return this.colorService.add(createColorRequest);
	}

	@PutMapping("/update")
	public Result update(@RequestBody @Valid UpdateColorRequest updateColorRequest) throws ColorNotFoundException, ColorAlreadyExistsException {

		return this.colorService.update(updateColorRequest);
	}

	@DeleteMapping("/delete")
	public Result delete(@RequestBody @Valid DeleteColorRequest deleteColorRequest) throws ColorNotFoundException, ColorExistsInCarException {

		return this.colorService.delete(deleteColorRequest);
	}

	@GetMapping("/getById")
	public DataResult<GetColorDto> getById(@RequestParam int colorId) throws ColorNotFoundException {

		return this.colorService.getById(colorId);
	}

	@GetMapping("/getAll")
	public DataResult<List<ColorListDto>> getAll(){

		return this.colorService.getAll();
	}

}