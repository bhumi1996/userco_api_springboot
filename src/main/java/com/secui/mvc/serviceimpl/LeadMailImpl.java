package com.secui.mvc.serviceimpl;

import com.secui.mvc.entity.LeadEntity;
import com.secui.mvc.entity.MailEntity;
import com.secui.mvc.repository.LeadMailRepository;
import com.secui.mvc.request.LeadMailRequestDto;
import com.secui.mvc.response.EmailTemplateResponseDto;
import com.secui.mvc.response.LeadMailResponseDto;
import com.secui.mvc.service.CurrentUserAuthenticationServiceInterface;
import com.secui.mvc.service.EmailTemplateInterface;
import com.secui.mvc.service.LeadMailInterface;
import com.secui.mvc.utility.ConstantUtil;
import com.secui.mvc.utility.UtilHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeadMailImpl implements LeadMailInterface {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ModelMapper mapper;
    private final CurrentUserAuthenticationServiceInterface currentUserAuthenticationServiceInterface;
    private final EmailTemplateInterface emailTemplateInterface;
    private final LeadMailRepository leadMailRepository;

    @Override
    public List<LeadMailResponseDto> finaAllByLead(LeadEntity leadEntity) {
        List<MailEntity>mailEntities=leadMailRepository.findAllByLeadEntityOrderByLastModifiedDateDesc(leadEntity);
        List<LeadMailResponseDto> leadMailResponseDTOs=new ArrayList<>();
        for (MailEntity mailEntity:mailEntities){
            LeadMailResponseDto leadMailResponseDto= mapper.map(mailEntity,LeadMailResponseDto.class);
            leadMailResponseDTOs.add(leadMailResponseDto);
        }
        return leadMailResponseDTOs;
    }

    @Override
    public boolean save(LeadMailRequestDto leadMailRequestDto, LeadEntity leadEntity) {
        try {
            MailEntity mailEntity = new MailEntity();
            mailEntity.setLeadEntity(leadEntity);
            mailEntity.setUKey(UtilHelper.uKey());
            mailEntity.setSubject(leadMailRequestDto.getSubject());
            mailEntity.setEmail(leadMailRequestDto.getEmail());
            String user = currentUserAuthenticationServiceInterface.getCurrentUserName();
            mailEntity.setCreatedBy(user);
            mailEntity.setLastModifiedBy(user);
            EmailTemplateResponseDto emailTemplateResponseDto = emailTemplateInterface.findByTemplateNameAndStatus(ConstantUtil.LEAD_MAIL,ConstantUtil.ACTIVE);
            mailEntity.setTemplateName(emailTemplateResponseDto.getTemplateName());
            mailEntity.setMessage(emailTemplateResponseDto.getTemplateBody().replace("{{description}}", leadMailRequestDto.getMessage() != null && !leadMailRequestDto.getMessage().isEmpty() ? leadMailRequestDto.getMessage() : StringUtils.EMPTY)
                    .replace("{{subject}}", mailEntity.getSubject())
                    .replace("{{name}}", leadMailRequestDto.getName())
                    .replace("{{phone}}", "+98765432211")
                    .replace("{{whatsapp}}", "+9876543210"));
            leadMailRepository.save(mailEntity);
            return true;
        }catch (Exception ex){
            log.error("Exception in LeadMailImpl :: Method save() :: {}",ex.getMessage());
            return false;
        }
    }
}
