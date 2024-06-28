package com.secui.request;

import com.secui.utility.ErrorUtil;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDto {
    @NotBlank(message = ErrorUtil.USER_NAME_EMPTY_ERROR)
    private String userName;

    @NotBlank(message = ErrorUtil.PASSWORD_EMPTY_ERROR)
    private String password;

    private String code;
}
