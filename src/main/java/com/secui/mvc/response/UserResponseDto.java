package com.secui.mvc.response;

import com.secui.mvc.entity.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDto {
    private String uKey;
    private String fullName;
    private String email;
    private String mobileNo;
    private String password;
    private String confirmPassword;
    private MultipartFile profileImage;
    private String profileImageUrl;
    private String role;
    private Set<RoleEntity> roles;
    private List<String> roleModelList;
    private String createdBy;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private Date createdDate;
}
