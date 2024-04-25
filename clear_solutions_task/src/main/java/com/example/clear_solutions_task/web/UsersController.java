package com.example.clear_solutions_task.web;

import com.example.clear_solutions_task.dto.ResponseDto;
import com.example.clear_solutions_task.dto.UserRequestDto;
import com.example.clear_solutions_task.dto.UserResponseDto;
import com.example.clear_solutions_task.dto.UserUpdateResponse;
import com.example.clear_solutions_task.dto.UsersInDateRangeRequest;
import com.example.clear_solutions_task.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<ResponseDto<UserResponseDto>> registerUser(@Valid @RequestBody UserRequestDto userRequestDto,
                                                                     HttpServletRequest request) {
        return new  ResponseEntity<>(
                new ResponseDto<>(userService.createNewUser(userRequestDto), request.getRequestURI()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<UserUpdateResponse>> updateUser(@Valid @RequestBody UserRequestDto userRequestDto,
                                                                      @PathVariable Long id,
                                                                      HttpServletRequest request) {
        UserUpdateResponse userUpdateResponse = userService.updateUser(userRequestDto, id);
        return ResponseEntity.ok(
                new ResponseDto<>(userUpdateResponse, request.getRequestURI()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto<UserUpdateResponse>> updateUserOneOrFewParameters(@RequestBody UserRequestDto userRequestDto,
                                                                      @PathVariable Long id,
                                                                      HttpServletRequest request) {
        UserUpdateResponse userUpdateResponse = userService.updateUserOneOrFewParameters(userRequestDto, id);
        return ResponseEntity.ok(
                new ResponseDto<>(userUpdateResponse, request.getRequestURI()));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getUsersBetweenDates(@Valid @RequestBody UsersInDateRangeRequest usersInDateRangeRequest,
                                                                       HttpServletRequest request) {

        return ResponseEntity.ok(
                new ResponseDto<>(userService.getUsersInDateRange(usersInDateRangeRequest),request.getRequestURI()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<String>> deleteUser(@PathVariable Long id,
                                           HttpServletRequest request){
        userService.deleteUser(id);
        return new ResponseEntity<>(
                new ResponseDto<>("User has been deleted",request.getRequestURI()),HttpStatus.OK);
    }
}
