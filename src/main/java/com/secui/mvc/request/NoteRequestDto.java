package com.secui.mvc.request;

import com.secui.mvc.entity.LeadEntity;
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
