package com.toiec.toiec.dto.response.auths;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse
{
    private String access_token;
    private String refresh_token;
}
