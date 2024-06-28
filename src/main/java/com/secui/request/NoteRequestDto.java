package com.secui.request;

import com.secui.entity.LeadEntity;
import com.secui.utility.ColumnUtils;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoteRequestDto {

    private String heading;

    private String message;
    private LeadEntity leadEntity;
}
