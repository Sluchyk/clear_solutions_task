package com.example.clear_solutions_task;

import com.example.clear_solutions_task.dto.UserRequestDto;
import com.example.clear_solutions_task.dto.UserResponseDto;
import com.example.clear_solutions_task.dto.UserUpdateResponse;
import com.example.clear_solutions_task.dto.UsersInDateRangeRequest;
import com.example.clear_solutions_task.exception.InvalidUserDateInputException;
import com.example.clear_solutions_task.exception.UniqueUserException;
import com.example.clear_solutions_task.exception.UserNotFoundException;
import com.example.clear_solutions_task.model.UserEntity;
import com.example.clear_solutions_task.model.UserRepository;
import com.example.clear_solutions_task.service.UserServiceImpl;
import com.example.clear_solutions_task.utils.DateUtils;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Stream;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@SpringBootTest
class ClearSolutionsTaskApplicationTests {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetUsersInDateRange_ValidRange() {
        var birthday = DateUtils.convertDateStringToLong("1999-01-01");
        UserEntity user1 = new UserEntity(1L, "sl@gmail.com", "test1", "test1", 11802L, null, null);
        UserEntity user2 = new UserEntity(2L, "john@gmail.com", "John", "Doe", 11803L, null, null);
        UserEntity user3 = new UserEntity(3L, "jane@gmail.com", "Jane", "Doe", 11804L, null, null);
        UserEntity user4 = new UserEntity(3L, "jane@gmail.com", "Jane", "Doe", birthday, null, null);
        UsersInDateRangeRequest request = new UsersInDateRangeRequest("2000-01-01", "2002-12-31");
        when(repository.findUserEntitiesByBirthDayDateBetween(anyLong(), anyLong())).thenReturn(Stream.of(user1, user2, user3));

        List<UserResponseDto> result = userService.getUsersInDateRange(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
    }


    @Test
    void testCreateNewUser_ValidRequest() {
        UserRequestDto request = new UserRequestDto("sl@gmail.com", "test", "test", "2002-04-25", null, null);
        UserResponseDto expectedResponse = new UserResponseDto(1L, "sl@gmail.com", "test", "test", "2002-04-25", null, null);
        UserEntity user = new UserEntity(1L, "sl@gmail.com", "test", "test", 11802L, null, null);

        when(repository.save(any())).thenReturn(user);

        UserResponseDto result = userService.createNewUser(request);
        assertNotNull(result);
        assertEquals(expectedResponse.getBirthDayDate(), result.getBirthDayDate());
        assertEquals(expectedResponse.getUserEmail(), result.getUserEmail());
        assertEquals(expectedResponse, result);

    }

    @Test
    void testGetUsersInDateRange_InvalidRange() {
        UsersInDateRangeRequest request = new UsersInDateRangeRequest("2003-12-31", "2002-01-01");
        assertThrows(InvalidUserDateInputException.class, () -> userService.getUsersInDateRange(request));
    }

    @Test
    void testCreateNewUser_DuplicateEmail() {

        UserRequestDto request = new UserRequestDto("sl@gmail.com", "test", "test", "2002-04-25", null, null);
        when(repository.save(any())).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UniqueUserException.class, () -> userService.createNewUser(request));
    }

    @Test
    void testUpdateUserOneOrFewParameters_ValidRequest() {
        UserRequestDto request = new UserRequestDto("", "John", "John", "2002-04-25", null, null);
        UserResponseDto expectedResponse = new UserResponseDto(1L, "sl@gmail.com", "test", "test", "2002-04-25", null, null);
        UserEntity user = new UserEntity(1L, "sl@gmail.com", "test", "test", 11807L, null, null);

        when(repository.getReferenceById(1L)).thenReturn(user);
        UserUpdateResponse result = userService.updateUserOneOrFewParameters(request, 1L);

        assertNotNull(result);
        assertEquals(expectedResponse.getUserEmail(),result.getUpdatedUserData().getUserEmail());
        assertEquals(expectedResponse.getBirthDayDate(),result.getUpdatedUserData().getBirthDayDate());
        assertNotEquals(expectedResponse.getBirthDayDate(),result.getOldUserData().getBirthDayDate());

    }
    @Test
    void testUpdateUser_ValidRequest() {
        UserRequestDto request = new UserRequestDto("sl@gmail.com", "John", "John", "2002-04-25", null, null);
        UserResponseDto expectedResponse = new UserResponseDto(1L, "sl@gmail.com", "test", "test", "2002-04-25", null, null);
        UserEntity user = new UserEntity(1L, "john@gmail.com", "test", "test", 11807L, null, null);

        when(repository.getReferenceById(1L)).thenReturn(user);
        UserUpdateResponse result = userService.updateUser(request, 1L);

        assertNotNull(result);
        assertEquals(expectedResponse.getUserEmail(),result.getUpdatedUserData().getUserEmail());
        assertEquals(expectedResponse.getBirthDayDate(),result.getUpdatedUserData().getBirthDayDate());
        assertNotEquals(expectedResponse.getBirthDayDate(),result.getOldUserData().getBirthDayDate());

    }
    @Test
    void testUpdateUser_InValidRequest() {
        UserRequestDto request = new UserRequestDto("sl@gmail.com", "John", "John", "20022-04-25", null, null);
        UserResponseDto expectedResponse = new UserResponseDto(1L, "sl@gmail.com", "test", "test", "2002-04-25", null, null);
        UserEntity user = new UserEntity(1L, "john@gmail.com", "test", "test", 11807L, null, null);

        when(repository.getReferenceById(1L)).thenReturn(user);
        assertThrows(InvalidUserDateInputException.class, () -> userService.updateUser(request, 1L));

    }
    @Test
    void testDeleteUser_ValidId() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        when(repository.getReferenceById(userId)).thenReturn(user);

        userService.deleteUser(userId);
        verify(repository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_InvalidId() {
        Long userId = 1L;
        when(repository.getReferenceById(userId)).thenThrow(EntityNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userId));
    }
}
