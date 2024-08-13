package com.secui.mvc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FaqResponseDto {

    private String faqQuestion;
    private String faqAnswer;
    private String status;
    private String uKey;

    public FaqResponseDto(String  faqQuestion, String faqAnswer) {
        this.faqQuestion = faqQuestion;
        this.faqAnswer = faqAnswer;
    }
}
