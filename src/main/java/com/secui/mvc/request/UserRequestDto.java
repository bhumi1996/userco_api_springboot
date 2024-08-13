package com.secui.mvc.request;

import com.secui.mvc.entity.RoleEntity;
import com.secui.mvc.utility.ErrorUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDto {
    @NotBlank(message = ErrorUtil.FULL_NAME_REQUIRED)
    @Size(min = 3,max = 50, message = ErrorUtil.FULL_NAME_LENGTH_ERROR)
    private String fullName;

    //private String userName;

    @NotBlank(message = ErrorUtil.EMAIL_REQUIRED)
    @Size(max = 100 , message = ErrorUtil.MAX_LENGTH)
    private String email;

    @NotBlank(message = ErrorUtil.PHONE_NO_REQUIRED)
    @Size(max = 15 ,message = ErrorUtil.MAX_LENGTH)
    private String mobileNo;

    @NotBlank(message = ErrorUtil.PASSWORD_REQUIRED)
    @Size(max = 100 , message = ErrorUtil.MAX_LENGTH)
    private String password;

    @NotBlank(message = ErrorUtil.CONFIRM_PASSWORD_REQUIRED)
    @Size(max = 100 , message = ErrorUtil.MAX_LENGTH)
    private String confirmPassword;


    @NotNull(message = ErrorUtil.STATUS_VALIDATION)
    @Size(min = 5, max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;


    private MultipartFile profileImage;

    private String profileImageUrl;

    private String role;
    private Set<RoleEntity> roles;
    private List<String> roleModelList;
    private String createdBy;
    private String lastModifiedBy;

}

