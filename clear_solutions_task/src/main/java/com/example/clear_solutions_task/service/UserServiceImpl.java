package com.example.clear_solutions_task.service;

import com.example.clear_solutions_task.dto.AddressDto;
import com.example.clear_solutions_task.dto.UserRequestDto;
import com.example.clear_solutions_task.dto.UserResponseDto;
import com.example.clear_solutions_task.dto.UserUpdateResponse;
import com.example.clear_solutions_task.dto.UsersInDateRangeRequest;
import com.example.clear_solutions_task.exception.InvalidUserDateInputException;
import com.example.clear_solutions_task.exception.UniqueUserException;
import com.example.clear_solutions_task.exception.UserNotFoundException;
import com.example.clear_solutions_task.model.Address;
import com.example.clear_solutions_task.model.UserEntity;
import com.example.clear_solutions_task.model.UserRepository;
import com.example.clear_solutions_task.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;
    @Value("${user.min}")
    private long minAge;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersInDateRange(UsersInDateRangeRequest request) {
        Long dateFrom = DateUtils.convertDateStringToLong(request.getFrom());
        Long dateTo = DateUtils.convertDateStringToLong(request.getTo());
        if (dateFrom > dateTo) {
            throw new InvalidUserDateInputException(String
                    .format("FromDate: (%s)" + "can`t be bigger then ToDate: (%s)", request.getFrom(), request.getTo()));
        }
        try (var userStream = repository.findUserEntitiesByBirthDayDateBetween(dateFrom, dateTo)) {
            return userStream.map(this::castUserEntityToDto).collect(Collectors.toList());
        }
    }

    @Override
    @Transactional
    public UserResponseDto createNewUser(UserRequestDto request) {
        long userAge = DateUtils.convertDateStringToLong(request.getBirthDayDate());
        DateUtils.checkAge(userAge, minAge);
        UserEntity userEntity = castUserDtoToEntity(request);
        try {
            return castUserEntityToDto(repository.save(userEntity));
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            throw new UniqueUserException("User with such email has been registered");
        }
    }

    @Override
    @Transactional
    public UserUpdateResponse updateUser(UserRequestDto userUpdateRequest, Long id) {
        long userAge = DateUtils.convertDateStringToLong(userUpdateRequest.getBirthDayDate());
        DateUtils.checkAge(userAge, minAge);
        UserEntity user = findUserById(id);
        UserResponseDto oldUser = castUserEntityToDto(user);
        user.setUserEmail(userUpdateRequest.getUserEmail());
        user.setAddress(modelMapper.map(userUpdateRequest.getAddress(), Address.class));
        user.setBirthDayDate(userAge);
        user.setFirstName(userUpdateRequest.getFirstName());
        user.setLastName(userUpdateRequest.getLastName());
        user.setPhoneNumber(userUpdateRequest.getPhoneNumber());
        return new UserUpdateResponse(castUserEntityToDto(user), oldUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        UserEntity user = findUserById(id);
        repository.delete(user);
    }

    @Override
    @Transactional
    public UserUpdateResponse updateUserOneOrFewParameters(UserRequestDto userRequestDto, Long id) {
        UserEntity user = findUserById(id);
        UserResponseDto oldUser = castUserEntityToDto(user);
        if (isNullOrEmpty(userRequestDto.getUserEmail())) {
            user.setUserEmail(userRequestDto.getUserEmail());
        }

        if (isNullOrEmpty(userRequestDto.getBirthDayDate())) {
            long date = DateUtils.convertDateStringToLong(userRequestDto.getBirthDayDate());
            DateUtils.checkAge(date, minAge);
            user.setBirthDayDate(date);
        }

        if (isNullOrEmpty(userRequestDto.getFirstName())) {
            user.setFirstName(userRequestDto.getFirstName());
        }

        if (isNullOrEmpty(userRequestDto.getLastName())) {
            user.setLastName(userRequestDto.getLastName());
        }

        if (isNullOrEmpty(userRequestDto.getPhoneNumber())) {
            user.setPhoneNumber(userRequestDto.getPhoneNumber());
        }
        var address = userRequestDto.getAddress();
        var addressToUpdate = user.getAddress();
        if (address != null) {
            if (isNullOrEmpty(address.getApartmentNumber())) {
                addressToUpdate.setApartmentNumber(address.getApartmentNumber());
            }
            if (isNullOrEmpty(address.getCity())) {
                addressToUpdate.setCity(address.getCity());
            }
            if (isNullOrEmpty(address.getStreet())) {
                addressToUpdate.setStreet(address.getStreet());
            }
            user.setAddress(addressToUpdate);
        }

        return new UserUpdateResponse(castUserEntityToDto(user), oldUser);
    }

    private UserEntity findUserById(Long id) {
        try {
            return repository.getReferenceById(id);
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            throw new UserNotFoundException("Can`t find user with id: " + id);
        }
    }
    private UserResponseDto castUserEntityToDto(UserEntity user){
        return UserResponseDto.builder()
                .id(user.getId())
                .userEmail(user.getUserEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .birthDayDate(DateUtils.convertLongDateToStringDate(user.getBirthDayDate()))
                .address(modelMapper.map(user.getAddress(), AddressDto.class))
                .build();

    }
    private UserEntity castUserDtoToEntity(UserRequestDto request){
        return UserEntity.builder()
                .userEmail(request.getUserEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .birthDayDate(DateUtils.convertDateStringToLong(request.getBirthDayDate()))
                .address(modelMapper.map(request.getAddress(),Address.class))
                .build();

    }
    private boolean isNullOrEmpty(String userParameter){
        return userParameter != null && !userParameter.isEmpty() && !userParameter.isBlank();
    }
}
