package com.secui.mvc.controller;

import com.secui.mvc.utility.UrlConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(UrlConstants.ADMIN_DASHBOARD_URL)
@Slf4j
public class DashboardController {
    public static final String DASHBOARD_URL = "";
    public static final String DASHBOARD_PAGE = "admin/settings/dashboard";


    @GetMapping(DASHBOARD_URL)
    public String getSettings()
    {
        return  DASHBOARD_PAGE;
    }
}
