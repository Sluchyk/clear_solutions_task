package com.example.clear_solutions_task;

import com.example.clear_solutions_task.exception.InvalidUserDateInputException;
import com.example.clear_solutions_task.utils.DateUtils;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DateUtilsTest {

    @Test
    void testConvertDateStringToLong() {

        String dateString = "2002-04-25";
        long expected = 11802;
        long result = DateUtils.convertDateStringToLong(dateString);

        assertEquals(expected, result);
    }

    @Test
    void testConvertLongDateToStringDate() {

        long date = 11802;
        String expected = "2002-04-25";
        String result = DateUtils.convertLongDateToStringDate(date);

        assertEquals(expected, result);
    }

    @Test
    void testCheckAge_ValidDate() {

        long birthDate = 11802;
        long minAge = 18;

        assertDoesNotThrow(() -> DateUtils.checkAge(birthDate, minAge));
    }

    @Test
    void testCheckAge_InvalidDate() {

        long birthDate = LocalDate.now().toEpochDay();
        long minAge = 18;

        assertThrows(InvalidUserDateInputException.class, () -> DateUtils.checkAge(birthDate, minAge));
    }

    @Test
    void testCheckAge_UserTooYoung() {
        long birthDate = LocalDate.now().minusYears(10).toEpochDay();
        long minAge = 18;

        assertThrows(InvalidUserDateInputException.class, () -> DateUtils.checkAge(birthDate, minAge));
    }
}