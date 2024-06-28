package com.secui.controller;

import com.secui.entity.LeadEntity;
import com.secui.entity.NoteEntity;
import com.secui.request.NoteRequestDto;
import com.secui.response.NoteResponseDto;
import com.secui.service.InitBinderInterface;
import com.secui.service.LeadInterface;
import com.secui.service.LeadNoteInterface;
import com.secui.utility.ConstantUtil;
import com.secui.utility.PageConstants;
import com.secui.utility.UrlConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(UrlConstants.LEAD_BASE_URL + UrlConstants.NOTES_URL)
@RequiredArgsConstructor
@Slf4j
public class LeadNoteController implements InitBinderInterface {

    private final LeadInterface leadInterface;

    private final LeadNoteInterface noteInterface;

    @GetMapping(UrlConstants.LIST_URL)
    public String list(Model model, @PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey, RedirectAttributes redirectAttributes) {
        LeadEntity leadEntity = leadInterface.findByKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty() || leadEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        model.addAttribute(ConstantUtil.LEAD_UKEY, leaduKey);
        model.addAttribute(ConstantUtil.NAME, leadEntity.getLeadName());
        model.addAttribute(ConstantUtil.LIST, noteInterface.findAllByLead(leadEntity));
        model.addAttribute(ConstantUtil.LEAD_INFO, leadEntity);
        return PageConstants.NOTE_LIST_PAGE;
    }


    @GetMapping(UrlConstants.ADD_URL)
    public String save(Model model, @PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey, RedirectAttributes redirectAttributes) {
        LeadEntity leadEntity = leadInterface.findByKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty() || leadEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, " Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        model.addAttribute(ConstantUtil.NOTE_DTO, new NoteRequestDto());
        model.addAttribute(ConstantUtil.LEAD_UKEY, leaduKey);
        model.addAttribute(ConstantUtil.NAME, leadEntity.getLeadName());
        return PageConstants.NOTE_ADD_PAGE;
    }

    @PostMapping(UrlConstants.ADD_URL)
    public String save(@PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey, @ModelAttribute(ConstantUtil.NOTE_DTO) @Valid NoteRequestDto noteRequestDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        LeadEntity leadEntity = leadInterface.findByKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty() || leadEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        // Controller side validation
        noteRequestDto.setLeadEntity(leadEntity);
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.LEAD_UKEY, leaduKey);
            model.addAttribute(ConstantUtil.NAME, leadEntity.getLeadName());
            return PageConstants.NOTE_ADD_PAGE;
        }
        if (noteInterface.save(noteRequestDto)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Note is saved successfully");
            return UrlConstants.REDIRECT_NOTE_LIST_URL + UrlConstants.NOTE_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Note is not saved");
        return UrlConstants.REDIRECT_NOTE_LIST_URL + leaduKey + UrlConstants.NOTE_LIST_URL;
    }

    @GetMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey,
                       @PathVariable(ConstantUtil.UKEY) String uKey,
                       RedirectAttributes redirectAttributes, Model model) {
        LeadEntity leadEntity = leadInterface.findByKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty() || leadEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        NoteResponseDto noteResponseDto = noteInterface.findByuKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || noteResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry,  Invalid Note");
            return UrlConstants.REDIRECT_NOTE_LIST_URL + UrlConstants.NOTE_LIST_URL;
        }

        model.addAttribute(ConstantUtil.LEAD_UKEY, leaduKey);
        model.addAttribute(ConstantUtil.NAME, leadEntity.getLeadName());
        model.addAttribute(ConstantUtil.UKEY, uKey);
        model.addAttribute(ConstantUtil.NOTE_DTO, noteResponseDto);
        return PageConstants.NOTE_EDIT_PAGE;

    }

    @PostMapping(UrlConstants.EDIT_URL)
    public String edit(@PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey,
                       @PathVariable(ConstantUtil.UKEY) String uKey,
                       @ModelAttribute(ConstantUtil.NOTE_DTO) @Valid NoteRequestDto noteRequestDto,
                       BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        // Controller side validation
        LeadEntity leadEntity = leadInterface.findByKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty() || leadEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        NoteEntity noteEntity = noteInterface.findByKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || noteEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry,  Invalid Note");
            return UrlConstants.REDIRECT_NOTE_LIST_URL + UrlConstants.NOTE_LIST_URL;
        }
        if (result.hasErrors()) {
            model.addAttribute(ConstantUtil.ERRORS, result);
            model.addAttribute(ConstantUtil.LEAD_UKEY, leaduKey);
            model.addAttribute(ConstantUtil.NAME, leadEntity.getLeadName());
            model.addAttribute(ConstantUtil.UKEY, uKey);
            return PageConstants.NOTE_EDIT_PAGE;
        }
        if (noteInterface.update(noteRequestDto, noteEntity)) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Note updated successfully");
            return UrlConstants.REDIRECT_NOTE_LIST_URL + UrlConstants.NOTE_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERRORS, "Note not updated successfully");
        return UrlConstants.REDIRECT_NOTE_LIST_URL + UrlConstants.NOTE_LIST_URL;

    }

    @GetMapping(path = UrlConstants.DELETE_URL)
    public String delete(@PathVariable(ConstantUtil.LEAD_UKEY) String leaduKey,
                         @PathVariable(ConstantUtil.UKEY) String uKey,
                         RedirectAttributes redirectAttributes) {
        // Controller side validation
        LeadEntity leadEntity = leadInterface.findByKey(leaduKey);
        if (leaduKey == null || leaduKey.isEmpty() || leadEntity == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Lead doesn't exist");
            return UrlConstants.REDIRECT_LEAD_LIST_URL;
        }
        NoteResponseDto noteResponseDto = noteInterface.findByuKey(uKey);
        // Controller side validation
        if (uKey == null || uKey.isEmpty() || noteResponseDto == null) {
            redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry,  invalid note");
            return UrlConstants.REDIRECT_NOTE_LIST_URL + UrlConstants.NOTE_LIST_URL;
        }
        /*
         * Repository side operation
         */
        if (noteInterface.deleteByuKey(uKey, noteResponseDto.getLeadEntity())) {
            redirectAttributes.addFlashAttribute(ConstantUtil.SUCCESS, "Note deleted successfully");
            return UrlConstants.REDIRECT_NOTE_LIST_URL + UrlConstants.NOTE_LIST_URL;
        }
        redirectAttributes.addFlashAttribute(ConstantUtil.ERROR, "Sorry, Note is not deleted");
        return UrlConstants.REDIRECT_NOTE_LIST_URL + UrlConstants.NOTE_LIST_URL;
    }


}
