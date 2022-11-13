package com.kodluyoruz.rentACar.controller;

import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.Result;
import com.kodluyoruz.rentACar.messaaages.exceptions.additionalExceptions.AdditionalNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.orderedAdditionalExceptions.OrderedAdditionalNotFoundException;
import com.kodluyoruz.rentACar.messaaages.exceptions.rentalCarExceptions.RentalCarNotFoundException;
import com.kodluyoruz.rentACar.service.OrderedAdditionalService;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalResponse.gets.GetOrderedAdditionalDto;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalResponse.lists.OrderedAdditionalListByAdditionalIdDto;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalResponse.lists.OrderedAdditionalListByRentalCarIdDto;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalResponse.lists.OrderedAdditionalListDto;
import com.kodluyoruz.rentACar.dto.orderedAdditionalDtos.orderedAdditionalRequests.DeleteOrderedAdditionalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orderedAdditionals")
public class OrderedAdditionalController {

    private final OrderedAdditionalService orderedAdditionalService;

    @Autowired
    public OrderedAdditionalController(OrderedAdditionalService orderedAdditionalService) {

        this.orderedAdditionalService = orderedAdditionalService;
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody @Valid DeleteOrderedAdditionalRequest deleteOrderedAdditionalRequest) throws OrderedAdditionalNotFoundException {

        return this.orderedAdditionalService.delete(deleteOrderedAdditionalRequest);
    }

    @GetMapping("getByOrderedAdditionalId")
    public DataResult<GetOrderedAdditionalDto> getByOrderedAdditionalId(@RequestParam int orderedAdditionalId) throws OrderedAdditionalNotFoundException {

        return this.orderedAdditionalService.getByOrderedAdditionalId(orderedAdditionalId);
    }

    @GetMapping("/getByOrderedAdditional_RentalCarId")
    public DataResult<List<OrderedAdditionalListByRentalCarIdDto>> getByOrderedAdditional_RentalCarId(@RequestParam int rentalCarId) throws RentalCarNotFoundException {

        return this.orderedAdditionalService.getByOrderedAdditional_RentalCarId(rentalCarId);
    }

    @GetMapping("/getByOrderedAdditional_AdditionalId")
    public DataResult<List<OrderedAdditionalListByAdditionalIdDto>> getByOrderedAdditional_AdditionalId(@RequestParam int additionalId) throws AdditionalNotFoundException {

        return this.orderedAdditionalService.getByOrderedAdditional_AdditionalId(additionalId);
    }

    @GetMapping("/getAll")
    public DataResult<List<OrderedAdditionalListDto>> getAll(){

        return this.orderedAdditionalService.getAll();
    }

}