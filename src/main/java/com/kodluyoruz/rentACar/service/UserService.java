package com.kodluyoruz.rentACar.service;

import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserAlreadyExistsException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserEmailNotValidException;
import com.kodluyoruz.rentACar.messaaages.exceptions.userExceptions.UserNotFoundException;
import com.kodluyoruz.rentACar.core.utilities.result.DataResult;
import com.kodluyoruz.rentACar.core.utilities.result.SuccessDataResult;
import com.kodluyoruz.rentACar.dto.userDtos.userResponse.gets.GetUserDto;
import com.kodluyoruz.rentACar.dto.userDtos.userResponse.lists.UserListDto;
import com.kodluyoruz.rentACar.repository.UserRepository;
import com.kodluyoruz.rentACar.entity.User;
import com.kodluyoruz.rentACar.messaaages.BusinessMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapperService modelMapperService;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapperService modelMapperService) {
        this.userRepository = userRepository;
        this.modelMapperService = modelMapperService;
    }

    public DataResult<List<UserListDto>> getAll() {

        List<User> users = this.userRepository.findAll();
        List<UserListDto> result = users.stream().map(user -> this.modelMapperService.forDto().map(user, UserListDto.class)).collect(Collectors.toList());

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_LISTED_SUCCESSFULLY);
    }

    public DataResult<GetUserDto> getById(int userId) throws UserNotFoundException {

        checkIfUserIdExists(userId);
        User user = this.userRepository.getById(userId);
        GetUserDto result = this.modelMapperService.forDto().map(user, GetUserDto.class);

        return new SuccessDataResult<>(result, BusinessMessages.GlobalMessages.DATA_BROUGHT_SUCCESSFULLY + userId);
    }

    public boolean checkIfUserIdExists(int userId) throws UserNotFoundException {

        if(!this.userRepository.existsByUserId(userId)){
            throw new UserNotFoundException(BusinessMessages.UserMessages.USER_ID_NOT_FOUND + userId);
        }

        return true;
    }

    public boolean checkIfUserEmailNotExists(String email) throws UserAlreadyExistsException {

        if(this.userRepository.existsByEmail(email)){
            throw new UserAlreadyExistsException(BusinessMessages.UserMessages.USER_EMAIL_ALREAY_EXISTS + email);
        }

        return true;
    }

    public boolean checkIfUserEmailNotExistsForUpdate(int userId, String email) throws UserEmailNotValidException {

        if(this.userRepository.existsByEmailAndUserIdIsNot(email, userId)){
            throw new UserEmailNotValidException(BusinessMessages.UserMessages.USER_EMAIL_ALREAY_EXISTS + email);
        }

        return true;
    }

}