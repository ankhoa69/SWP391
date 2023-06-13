package com.be.dto.response;

import lombok.Data;

@Data
public class RefreshTokenResponse {
    public String jwtToken;
    public String refreshToken;
}
