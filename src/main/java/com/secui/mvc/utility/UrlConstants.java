package com.secui.mvc.utility;

public class UrlConstants {
    public static final String SETTINGS_BASE_URL ="admin/settings/";
    public static final String ADMIN_BASE_URL ="admin/";
    public static final String ROLE = "role";
    public static final String LIST_URL ="/list" ;
    public static final String ADD_URL ="/add" ;

    public static final String EDIT_URL = "/edit/{uKey}";
    public static final String REDIRECT_LIST_ROLE_URL = "redirect:/admin/settings/role/list";
    public static final String DELETE_URL ="/delete/{uKey}" ;
    public static final String USER = "user";
    public static final String REDIRECT_USER_LIST_URL =  "redirect:/admin/settings/user/list";
    public static final String PRIVILEGE ="privilege" ;
    public static final String ROLE_PRIVILEGES_LIST = "listprivileges/{uKey}";
    public static final String REDIRECT_ROLE_LIST_PRIVILEGES = "redirect:/admin/settings/privilege/listprivileges/";
    public static final String ADMIN_DASHBOARD_URL ="admin/dashboard" ;

    public static final String LOGIN_URL = "/login";
    public static final String USER_DASHBOARD_URL = "redirect:/admin/dashboard";
    public static final String DASHBOARD ="dashboard";

    public static final String COUNTRY = "country";
    public static final String REDIRECT_COUNTRY_LIST_URL = "redirect:/admin/settings/country/list";
    public static final String EMAIL_TEMPLATE = "email/template";
    public static final String REDIRECT_EMAIL_TEMPLATE_LIST_URL = "redirect:/admin/settings/email/template/list";
    public static final String LEAD = "lead";
    public static final String REDIRECT_LEAD_LIST_URL = "redirect:/admin/lead/list";

    public static final String EMAIL_CONFIGURATION = "email/config";
    public static final String REDIRECT_EMAIL_CONFIGURATION_LIST_URL = "redirect:/admin/settings/email/config/list";
    public static final String LEAD_MAIL ="{leaduKey}/mail" ;
    public static final String REDIRECT_LEAD_MAIL_URL = "redirect:/admin/lead/mail/list";
    public static final String LEAD_BASE_URL ="/admin/lead/" ;
    public static final String NOTES_URL = "{leaduKey}/note";
    public static final String NOTE_LIST_URL = "{leaduKey}/note/list";
    public static final String REDIRECT_NOTE_LIST_URL = "redirect:/admin/lead/";


    public static final String GROUP = "group";
    public static final String CAMPAIGN = "campaign";
    public static final String REDIRECT_GROUP_LIST_URL = "redirect:/admin/group/list";
    public static final String REDIRECT_CAMPAIGN_LIST_URL ="redirect:/admin/campaign/list" ;
    public static final String PORTAL = "portal";
    public static final String SERVICE = "service";
    public static final String REDIRECT_PORTAL_LIST_URL = "redirect:/admin/settings/portal/list";
    public static final String REDIRECT_SERVICE_LIST_URL ="redirect:/admin/settings/service/list" ;
    public static final String TESTIMONIAL = "testimonial";
    public static final String REDIRECT_TESTIMONIAL_LIST_URL ="redirect:/admin/settings/testimonial/list" ;
    public static final String CAMPAIGN_TYPE = "campaign/type";
    public static final String REDIRECT_CAMPAIGN_TYPE_LIST_URL ="redirect:/admin/settings/campaign/type/list" ;
    public static final String ADD_GROUP_LEADS_URL = "{uKey}/lead/add";
    public static final String LEAD_ADD = "/lead/add";
    public static final String REDIRECT_GROUP_BASE = "redirect:/admin/group/";
    public static final String LIST_GROUP_LEADS_URL = "{uKey}/lead/list";
    public static final String REMOVE_GROUP_LEAD_URL = "{uKey}/lead/remove/{leaduKey}";
    public static final String ASSIGN_GROUP_TO_CAMPGAIN_URL = "{uKey}/assign/group";
    public static final String REMOVE_GROUP_FROM_CAMPGAIN_URL = "{uKey}/remove/group";
    public static final String REDIRECT_LEAD ="redirect:/admin/lead/" ;
    public static final String LEAD_MAIL_URL ="/mail/list" ;
    public static final String ARTICLE = "article";
    public static final String REDIRECT_ARTICLE_LIST_URL ="redirect:/admin/article/list" ;
    public static final String FAQ ="faq" ;
    public static final String REDIRECT_FAQ_LIST_URL ="redirect:/admin/settings/faq/list" ;
}
