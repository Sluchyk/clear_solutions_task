package com.example.clear_solutions_task.service;

import com.example.clear_solutions_task.dto.UserRequestDto;
import com.example.clear_solutions_task.dto.UserResponseDto;
import com.example.clear_solutions_task.dto.UserUpdateResponse;
import com.example.clear_solutions_task.dto.UsersInDateRangeRequest;
import com.example.clear_solutions_task.model.UserEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserService {
    List<UserResponseDto> getUsersInDateRange(UsersInDateRangeRequest request);
    UserResponseDto createNewUser(UserRequestDto request);
    UserUpdateResponse updateUser(UserRequestDto userUpdateRequest, Long id);

    void deleteUser(Long id);

    UserUpdateResponse updateUserOneOrFewParameters(UserRequestDto userRequestDto, Long id);
}
