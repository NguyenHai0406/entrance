package com.test.entrance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenDTO {
    private String token;
    private String refreshToken;
}
