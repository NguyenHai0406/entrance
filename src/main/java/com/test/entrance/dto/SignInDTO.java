package com.test.entrance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignInDTO {
    private UserDTO user;
    private String token;
    private String refreshToken;
}
