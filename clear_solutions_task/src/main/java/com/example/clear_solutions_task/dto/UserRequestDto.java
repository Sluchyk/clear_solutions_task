package com.example.clear_solutions_task.dto;

import static com.example.clear_solutions_task.utils.ValidationConstants.EMAIL_REGEX;
import static com.example.clear_solutions_task.utils.ValidationConstants.INVALID_EMAIL_MESSAGE;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequestDto {
    @Email(regexp = EMAIL_REGEX,message = INVALID_EMAIL_MESSAGE)
    @NotBlank(message = "Please enter email")
    private String userEmail;
    @NotBlank(message = "Please input first name")
    @Size(min = 2,message = "Firs name should contain`s 2 or more symbols")
    private String firstName;
    @NotBlank(message = "Please input last name")
    @Size(min = 2,message = "Last name should contain`s 2 or more symbols")
    private String lastName;
    @NotBlank(message = "Please enter your birthday date")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message = "Wrong birthday input.Birthday date (year-month-day) example: 2001-01-01")
    private String birthDayDate;
    private AddressDto address;
    private String phoneNumber;
}
