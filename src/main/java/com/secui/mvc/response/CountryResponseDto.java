package com.secui.mvc.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CountryResponseDto {

    private String name;
    private String capital;
    private String isoTwo;
    private String isoThree;
    private String isdCode;
    private String status;
    private String uKey;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;


    public CountryResponseDto(String name, String isoTwo, String isdCode) {
        this.name = name;
        this.isoTwo = isoTwo;
        this.isdCode = isdCode;
    }
}
