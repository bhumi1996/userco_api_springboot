package com.secui.restapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TestimonialResponse {

    private String name;

    private String designation;

    private String comments;

    private String fileName;
}
