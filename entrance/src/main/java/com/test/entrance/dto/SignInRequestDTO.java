package com.test.entrance.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

import static com.test.entrance.constant.ErrorMessage.*;

@Data
@Builder
public class SignInRequestDTO {
    @Email(message = EMAIL_INVALID)
    private String email;
    @Size(min = 8, max = 20)
    @NotBlank
    private String password;
}
