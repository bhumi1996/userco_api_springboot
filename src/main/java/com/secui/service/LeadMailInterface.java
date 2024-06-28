package com.secui.service;

import com.secui.entity.LeadEntity;
import com.secui.request.LeadMailRequestDto;
import com.secui.response.LeadMailResponseDto;

import java.util.List;

public interface LeadMailInterface {
    List<LeadMailResponseDto> finaAllByLead(LeadEntity leadEntity);

    boolean save(LeadMailRequestDto leadMailRequestDto, LeadEntity leadEntity);
}
