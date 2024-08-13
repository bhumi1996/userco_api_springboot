package com.secui.mvc.request;


import com.secui.mvc.utility.ErrorUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleRequestDto {
    @NotNull(message = ErrorUtil.ROLE_NAME_REQUIRED)
    @Size(min = 2, max = 50, message = ErrorUtil.NAME_LENGTH_VALIDATION)
    private String name;

    private String description;

    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5, max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;
}
