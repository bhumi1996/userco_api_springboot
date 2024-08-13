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
public class CampaignRequestDto {

    @NotNull(message = ErrorUtil.CAMPAIGN_NAME_REQUIRED)
    @Size(min = 2,max = 100, message = ErrorUtil.MAX_LENGTH)
    String name;

    String type;

    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5,max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    String status;

}
