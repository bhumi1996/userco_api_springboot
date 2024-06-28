package com.secui.request;

import com.secui.entity.LeadEntity;
import com.secui.utility.ColumnUtils;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LeadMailRequestDto {

    String email;
    String name;
    String templateName;
    String subject;
    String message;
    MultipartFile attachment;
    private LeadEntity leadEntity;
}
