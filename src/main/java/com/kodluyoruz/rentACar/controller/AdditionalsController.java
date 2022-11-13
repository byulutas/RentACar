package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalResponse.gets.GetAdditionalDto;
import com.kodluyoruz.rentACar.messaaages.exceptions.additionalExceptions.AdditionalAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.additionalExceptions.AdditionalNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.orderedAdditionalExceptions.AdditionalAlreadyExistsInOrderedAdditionalException;
import com.kodluyoruz.rentACar.service.AdditionalService;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalResponse.lists.AdditionalListDto;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.CreateAdditionalRequest;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.DeleteAdditionalRequest;
import com.kodluyoruz.rentACar.dto.additionalDtos.additionalRequests.UpdateAdditionalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/additionals")
public class AdditionalsController {

    private final AdditionalService additionalService;

    @Autowired
    public AdditionalsController(AdditionalService additionalService) {

        this.additionalService = additionalService;
    }

    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateAdditionalRequest createAdditionalRequest) throws AdditionalAlreadyExistsException {

        return this.additionalService.add(createAdditionalRequest);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Valid UpdateAdditionalRequest updateAdditionalRequest) throws AdditionalNotFoundException, AdditionalAlreadyExistsException {

        return this.additionalService.update(updateAdditionalRequest);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteAdditionalRequest deleteAdditionalRequest) throws AdditionalNotFoundException, AdditionalAlreadyExistsInOrderedAdditionalException {

        return this.additionalService.delete(deleteAdditionalRequest);
    }

    @GetMapping("/getById")
    public DataResult<GetAdditionalDto> getById(@RequestParam int additionalId) throws AdditionalNotFoundException {

        return this.additionalService.getByAdditionalId(additionalId);
    }

    @GetMapping("/getAll")
    public DataResult<List<AdditionalListDto>> getAll(){

        return this.additionalService.getAll();
    }

}