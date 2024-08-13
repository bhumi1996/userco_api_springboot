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
public class TestimonialRequestDto {


    @NotNull(message = ErrorUtil.PORTAL_NAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String portalName;

    @NotNull(message = ErrorUtil.TESTIMONIAL_NAME_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String name;

    private String location;

    @NotNull(message = ErrorUtil.DESIGNATION_REQUIRED)
    @Size(max = 100, message = ErrorUtil.MAX_LENGTH)
    private String designation;

    @NotNull(message = ErrorUtil.COMMENTS_REQUIRED)
    @Size(max = 500, message = ErrorUtil.MAX_LENGTH)
    private String comments;

    private String fileName;

    private MultipartFile profileImage;

    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5, max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;
}
