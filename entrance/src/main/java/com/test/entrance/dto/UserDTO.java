package com.test.entrance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

import static com.test.entrance.constant.ErrorMessage.*;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserDTO {
    private long id;

    private String firstName;

    private String lastName;

    @Email(message = EMAIL_INVALID)
    private String email;

    private String displayName;

    @Size(min = 8, max = 20)
    @NotBlank
    private String password;
}
