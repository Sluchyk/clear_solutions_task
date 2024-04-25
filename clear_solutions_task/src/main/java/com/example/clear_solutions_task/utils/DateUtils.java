package com.example.clear_solutions_task.utils;

import com.example.clear_solutions_task.exception.InvalidUserDateInputException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateUtils {
    public static Long convertDateStringToLong(String date) {
        try {
        return LocalDate.parse(date).toEpochDay();
        } catch (DateTimeParseException e){
            log.error(e.getMessage());
          throw new InvalidUserDateInputException("Please enter correct date");
        }
    }
    public static String convertLongDateToStringDate(Long date) {
        LocalDate localDate = LocalDate.ofEpochDay(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }
    public static void checkAge(Long birthDate, long minAge) {
        if (birthDate >= LocalDate.now().toEpochDay()){
            throw new InvalidUserDateInputException("Please enter correct date");
        }
        if (birthDate > LocalDate.now().minusYears(minAge).toEpochDay()) {
            throw new InvalidUserDateInputException("User must be older than " + minAge);
        }
    }
}
