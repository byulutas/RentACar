package com.kodluyoruz.rentACar.service;

import java.util.List;
import java.util.stream.Collectors;

import com.kodluyoruz.rentACar.messaaages.exceptions.colorExceptions.ColorAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.colorExceptions.ColorNotFoundException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessResult;
import com.kodluyoruz.rentACar.dto.colorDtos.colorRequests.CreateColorRequest;
import com.kodluyoruz.rentACar.dto.colorDtos.colorRequests.DeleteColorRequest;
import com.kodluyoruz.rentACar.dto.colorDtos.colorRequests.UpdateColorRequest;
import com.kodluyoruz.rentACar.dto.colorDtos.colorResponse.gets.GetColorDto;
import com.kodluyoruz.rentACar.dto.colorDtos.colorResponse.lists.ColorListDto;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import com.kodluyoruz.rentACar.messaaages.exceptions.carExceptions.ColorExistsInCarException;
import com.kodluyoruz.rentACar.repository.ColorRepository;
import com.kodluyoruz.rentACar.entity.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ColorService {

	private final ColorRepository colorRepository;
	private final CarService carService;
	private final ModelMapperService modelMapperService;

	@Autowired
	public ColorService(ColorRepository colorRepository, ModelMapperService modelMapperService, CarService carService) {
		this.colorRepository = colorRepository;
		this.carService = carService;
		this.modelMapperService = modelMapperService;
	}

	public Result add(CreateColorRequest createColorRequest) throws ColorAlreadyExistsException {

		checkIsNotExistsByColorName(createColorRequest.getColorName());
		Color color = this.modelMapperService.forRequest().map(createColorRequest, Color.class);
		this.colorRepository.save(color);

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_ADDED_SUCCESSFULLY);
	}

	public Result update(UpdateColorRequest updateColorRequest) throws ColorAlreadyExistsException, ColorNotFoundException {

		checkIsExistsByColorId(updateColorRequest.getColorId());
		checkIsNotExistsByColorName(updateColorRequest.getColorName());
		Color color = this.modelMapperService.forRequest().map(updateColorRequest, Color.class);
		this.colorRepository.save(color);

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_UPDATED_SUCCESSFULLY + updateColorRequest.getColorId());
	}

	public Result delete(DeleteColorRequest deleteColorRequest) throws ColorExistsInCarException, ColorNotFoundException {

		checkIsExistsByColorId(deleteColorRequest.getColorId());
		this.carService.checkIsNotExistsByCar_ColorId(deleteColorRequest.getColorId());
		this.colorRepository.deleteById(deleteColorRequest.getColorId());

		return new SuccessResult(BusinessMessages.GlobalMessages.DATA_DELETED_SUCCESSFULLY + deleteColorRequest.getColorId());
	}

	public DataResult<List<ColorListDto>> getAll() {

		List<Color> colors = this.colorRepository.findAll();
		List<ColorListDto> result = colors.stream().map(color -> this.modelMapperService.forDto().map(color, ColorListDto.class)).collect(Collectors.toList());

		return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
	}

	public DataResult<GetColorDto> getById(int id) throws ColorNotFoundException {

		checkIsExistsByColorId(id);
		Color color = this.colorRepository.getById(id);
		GetColorDto getColorDto = this.modelMapperService.forDto().map(color, GetColorDto.class);

		return new SuccessDataResult<>(getColorDto, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + id);
	}

	public void checkIsExistsByColorId(int brandId) throws ColorNotFoundException {

		if(!this.colorRepository.existsByColorId(brandId)) {
			throw new ColorNotFoundException(BusinessMessages.ColorMessages.COLOR_ID_NOT_FOUND + brandId);
		}
	}

	private void checkIsNotExistsByColorName(String brandName) throws ColorAlreadyExistsException {
		if(this.colorRepository.existsByColorName(brandName)) {
			throw new ColorAlreadyExistsException(BusinessMessages.ColorMessages.COLOR_NAME_ALREADY_EXISTS + brandName);
		}
	}

}