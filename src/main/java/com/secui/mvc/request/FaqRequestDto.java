package com.secui.mvc.request;

import com.secui.mvc.utility.ErrorUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FaqRequestDto {
    @NotNull(message = ErrorUtil.FAQ_QUESTION_REQUIRED)
    @Size(min = 5,message = ErrorUtil.MIN_LENGTH)
    private String faqQuestion;

    @NotNull(message = ErrorUtil.FAQ_ANSWER_ERROR)
    @Size(min = 5,message = ErrorUtil.MAX_LENGTH)
    private String faqAnswer;

    @NotBlank(message = ErrorUtil.STATUS_VALIDATION)
    @Size( max = 7, message = ErrorUtil.STATUS_LENGTH_VALIDATION)
    private String status;
}
