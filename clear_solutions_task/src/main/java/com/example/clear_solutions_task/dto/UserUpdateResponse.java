package com.example.clear_solutions_task.dto;

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
public class UserUpdateResponse {
    private UserResponseDto updatedUserData;
    private UserResponseDto oldUserData;
}
