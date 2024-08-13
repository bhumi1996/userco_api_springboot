package com.secui.mvc.utility;

public class QueryUtils {
    public static final String FAILED_ATTEMPT_QUERY = "update usr_mstr_tbl u set u.failed_attempt = :failAttempts where u.email = :email";
    public static final String COUNTRY_QUERY = "select new com.secui.mvc.response.CountryResponseDto(c.name, c.isoTwo,c.isdCode) from CountryEntity c where c.status=:status";
    public static final String GROUPS_BASEDON_PORTAL_AND_UKEY_QUERY = "select * from group_mstr_tbl where u_key in (:uKeys)";
    public static final String PORTAL_SHORT_NAME_QUERY = "select short_name from portal_mstr_tbl where status=:active";
    public static final String FIND_FIRST_10_TESTIMONIALS = "select name,designation,comments,location,file_name from testimonial_mstr_tbl where portal_name=:portal and status=:status order by last_modified_date desc limit 10";
}
