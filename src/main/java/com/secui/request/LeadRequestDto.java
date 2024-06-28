package com.secui.request;

import com.secui.utility.ColumnUtils;
import com.secui.utility.ErrorUtil;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeadRequestDto {

    @NotNull(message = ErrorUtil.LEAD_NAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String leadName;
    @NotNull(message = ErrorUtil.EMAIL_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String email;
    @NotNull(message = ErrorUtil.DIAL_CODE_REQUIRED)
    @Size(max=10, message = ErrorUtil.MAX_LENGTH)
    private String dialCode;
    @NotNull(message = ErrorUtil.MOBILE_NUMBER_REQUIRED)
    @Size(min=5, max = 15, message = ErrorUtil.MIN_MAX_LENGTH)
    private String mobileNumber;
    @NotNull(message = ErrorUtil.COUNTRY_NAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String country;
    @NotNull(message = ErrorUtil.STAGE_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String stage;
    @NotNull(message = ErrorUtil.CHANNEL_REQUIRED)
    @Size( max = 100, message = ErrorUtil.MAX_LENGTH)
    private String channel;
    @NotNull(message = ErrorUtil.GENDER_REQUIRED)
    @Size(max = 50, message = ErrorUtil.MAX_LENGTH)
    private String gender;
}
