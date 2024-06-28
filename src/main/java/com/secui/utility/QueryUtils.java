package com.secui.utility;

public class QueryUtils {
    public static final String FAILED_ATTEMPT_QUERY = "update usr_mstr_tbl u set u.failed_attempt = :failAttempts where u.email = :email";
    public static final String COUNTRY_QUERY = "select new com.secui.response.CountryResponseDto(c.name, c.isoTwo,c.isdCode) from CountryEntity c where c.status=:status";
    public static final String GROUPS_BASEDON_PORTAL_AND_UKEY_QUERY = "select * from group_mstr_tbl where u_key in (:uKeys)";
}
