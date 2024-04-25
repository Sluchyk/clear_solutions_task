package com.example.clear_solutions_task.dto;

import java.util.Objects;
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
public class UserResponseDto {
    private Long id;
    private String userEmail;
    private String firstName;
    private String lastName;
    private String birthDayDate;
    private AddressDto address;
    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponseDto that)) return false;
        return getId().equals(that.getId()) && getUserEmail().equals(that.getUserEmail()) && getFirstName().equals(that.getFirstName()) && getLastName().equals(that.getLastName()) && getBirthDayDate().equals(that.getBirthDayDate()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getPhoneNumber(), that.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserEmail(), getFirstName(), getLastName(), getBirthDayDate(), getAddress(), getPhoneNumber());
    }
}
