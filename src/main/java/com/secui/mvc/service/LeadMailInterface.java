package com.secui.mvc.service;

import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.request.LeadMailRequestDto;
import com.secui.mvc.response.LeadMailResponseDto;

import java.util.List;

public interface LeadMailInterface {
    List<LeadMailResponseDto> finaAllByLead(LeadEntity leadEntity);

    boolean save(LeadMailRequestDto leadMailRequestDto, LeadEntity leadEntity);
}
