package com.example.clear_solutions_task.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsersInDateRangeRequest {
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message = "Wrong birthday input. Birthday date (year-month-day) example: 2001-01-01")
    private String from;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}",message = "Wrong birthday input. Birthday date (year-month-day) example: 2001-01-01")
    private String to;
}
