package com.secui.mvc.service;

import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.request.LeadMailRequestDto;

public interface MailApiInterface {
    void sendLeadMail(LeadMailRequestDto leadMailRequestDto, LeadEntity leadEntity);
}
