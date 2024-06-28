package com.secui.controller;

import com.secui.request.LoginRequestDto;
import com.secui.service.InitBinderInterface;
import com.secui.service.UserInterface;
import com.secui.utility.ConstantUtil;
import com.secui.utility.ErrorUtil;
import com.secui.utility.PageConstants;
import com.secui.utility.UrlConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController implements InitBinderInterface {

    private final UserInterface userInterface;


    @GetMapping(UrlConstants.LOGIN_URL)
    public String login(@RequestParam(value = ConstantUtil.ERROR, required = false) String error,
                        @RequestParam(value = ConstantUtil.LOGOUT, required = false) String logout,
                        @RequestParam(value = ConstantUtil.EXPIRED, required = false) String expired,
                        Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String errorMessage = null;
        if (error != null) {
            errorMessage = "Username and Password is incorrect";
        }
        if (logout != null) {
            errorMessage = "You have been successfully logout!";
        }
        if (expired != null) {
            errorMessage = "Session expired.Login Again";
        }

        if (!(auth instanceof AnonymousAuthenticationToken)) {
            /* The user is logged in :) */
            return UrlConstants.USER_DASHBOARD_URL;
        }
        model.addAttribute(ConstantUtil.LOGIN_DTO, new LoginRequestDto());
        model.addAttribute(ErrorUtil.ERROR_MSG, errorMessage);
        return PageConstants.LOGIN_PAGE;
    }


    @GetMapping("/admin/success")
    public String success() {
        return "success";
    }
}
