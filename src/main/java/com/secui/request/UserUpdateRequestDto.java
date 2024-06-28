package com.secui.request;

import com.secui.entity.RoleEntity;
import com.secui.utility.ErrorUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
    @NotBlank(message = ErrorUtil.FULL_NAME_REQUIRED)
    @Size(min = 3,max = 50, message = ErrorUtil.FULL_NAME_LENGTH_ERROR)
    private String fullName;

    @NotBlank(message = ErrorUtil.EMAIL_REQUIRED)
    @Size(max = 100 , message = ErrorUtil.MAX_LENGTH)
    private String email;

    @NotBlank(message = ErrorUtil.PHONE_NO_REQUIRED)
    @Size(max = 15 ,message = ErrorUtil.MAX_LENGTH)
    private String mobileNo;
    private MultipartFile profileImage;
    private String profileImageUrl;

    private String role;
    private Set<RoleEntity> roles;
    private List<String> roleModelList;
    private String lastModifiedBy;

}
