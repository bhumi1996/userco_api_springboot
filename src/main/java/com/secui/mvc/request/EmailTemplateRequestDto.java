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
public class EmailTemplateRequestDto {

    @NotNull(message = ErrorUtil.TEMPLATE_NAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String templateName;

    @NotNull(message = ErrorUtil.TEMPLATE_HEADING_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String templateHeading;

    @NotNull(message = ErrorUtil.TEMPLATE_BODY_REQUIRED)
    private String templateBody;

    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5, max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;
}
