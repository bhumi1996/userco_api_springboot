package com.secui.mvc.request;

import com.secui.mvc.utility.ErrorUtil;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceRequestDto {


    @NotNull(message = ErrorUtil.SERVICE_NAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String serviceName;

    @NotNull(message = ErrorUtil.DESCRIPTION_REQUIRED)
    @Size(max = 500, message = ErrorUtil.MAX_LENGTH)
    private String description;

    private String imageUrl;
    private MultipartFile image;

    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5, max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;
}
