package com.secui.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/settings")
public class SettingController {
    public static final String SETTING_URL = "";
    public static final String SETTING_PAGE = "admin/settings/setting";


    @GetMapping(SETTING_URL)
    public String getSettings()
    {
        return  SETTING_PAGE;
    }
}
