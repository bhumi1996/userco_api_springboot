package com.secui.service;

import com.secui.entity.LeadEntity;
import com.secui.request.LeadMailRequestDto;

public interface MailApiInterface {
    void sendLeadMail(LeadMailRequestDto leadMailRequestDto, LeadEntity leadEntity);
}
