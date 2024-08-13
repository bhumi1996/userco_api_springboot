package com.secui.mvc.request;

import com.secui.mvc.response.LeadResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupLeadsListDto {

    List<LeadResponseDto> leadDtoList=new ArrayList<>();
}
