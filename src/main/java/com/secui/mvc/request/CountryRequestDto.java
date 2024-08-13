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
public class CountryRequestDto {
    @NotNull(message = ErrorUtil.COUNTRY_NAME_REQUIRED)
    @Size(min = 2, max = 100, message = ErrorUtil.NAME_LENGTH_VALIDATION)
    private String name;
    @NotNull(message = ErrorUtil.CAPITAL_NAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String capital;
    @NotNull(message = ErrorUtil.ISO_CODE_REQUIRED)
    @Size(min=2,max = 2, message = ErrorUtil.MIN_MAX_LENGTH)
    private String isoTwo;
    @NotNull(message = ErrorUtil.ISO_CODE_REQUIRED)
    @Size(min=3,max = 3, message = ErrorUtil.MIN_MAX_LENGTH)
    private String isoThree;
    @NotNull(message = ErrorUtil.ISD_CODE_REQUIRED)
    @Size(max = 10, message = ErrorUtil.MAX_LENGTH)
    private String isdCode;
    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5, max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;

}
