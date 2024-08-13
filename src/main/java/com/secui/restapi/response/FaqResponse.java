package com.secui.restapi.response;

import com.secui.mvc.utility.ColumnUtils;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FaqResponse {
    private String faqQuestion;
    private String faqAnswer;
}
