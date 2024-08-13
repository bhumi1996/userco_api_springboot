package com.secui.mvc.utility;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

import java.text.DecimalFormat;
import java.util.*;

public class UtilHelper {
    public static final String DECIMAL_FORMAT_TWO = "#0.00";
    public static final String ERRORS ="errors" ;
    public static final String ERROR ="error" ;
    public static final String SUCCESS = "success";

    public static Double twoDecimalRoundOffDouble(Double num) {
        String str = fixedDecimalFormatString(DECIMAL_FORMAT_TWO, num);
        return Double.valueOf(str);
    }

    public static String fixedDecimalFormatString(String pattern, Double num) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        if (num == null) {
            return decimalFormat.format(0);
        }
        return decimalFormat.format(num);
    }

    public static Float twoDecimalRoundOfFloat(Float num) {
        String str = fixedFloatFormatString(DECIMAL_FORMAT_TWO, num);
        return Float.valueOf(str);
    }

    public static String fixedFloatFormatString(String pattern, Float num) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        // DecimalFormat uses default rounding mode HALF_EVEN
        if (num == null) {
            return decimalFormat.format(0);
        }
        return decimalFormat.format(num);
    }

    public static List<String> status() {
        List<String> list = new ArrayList<>();
        list.add(ConstantUtil.ACTIVE);
        list.add(ConstantUtil.BLOCK);
        return list;
    }
    public static List<String> booleanList()
    {
        List<String>list = new ArrayList<>();
        list.add(ConstantUtil.TRUE);
        list.add(ConstantUtil.FALSE);
        return list;
    }
    public static List<String> encodingList()
    {
        List<String>list = new ArrayList<>();
        list.add(ConstantUtil.UTF);
        return list;
    }
    public static List<String> portList()
    {
        List<String>list = new ArrayList<>();
        list.add("587");
        return list;
    }
    public static List<String> protocolList(){
        List<String>list = new ArrayList<>();
        list.add(ConstantUtil.SMTP);
        return list;
    }


    public static List<String> leadStage() {
        List<String> list = new ArrayList<>();
        list.add(ConstantUtil.RAW);
        list.add(ConstantUtil.INPROCESS);
        list.add(ConstantUtil.INTERESTED);
        list.add(ConstantUtil.WON);
        list.add(ConstantUtil.BAD);
        return list;
    }

    public static List<String> leadChannel() {
        List<String> list = new ArrayList<>();
        list.add(ConstantUtil.INSTAGRAM);
        list.add(ConstantUtil.FACEBOOK);
        list.add(ConstantUtil.GOOGLE_ADS);
        return list;
    }

    public static List<String> gender() {
        List<String> list = new ArrayList<>();
        list.add(ConstantUtil.MALE);
        list.add(ConstantUtil.FEMALE);
        list.add(ConstantUtil.THIRD_GENDER);
        return list;
    }


    public static List<String> templates() {
        List<String> list = new ArrayList<>();
        list.add(ConstantUtil.LEAD_EMAIL);
        return list;
    }

    public static String uKey() {
        StringBuilder reference = new StringBuilder();
        double d, f;
        for (int i = 1; i <= 16; i++) {
            d = Math.random() * 10;
            reference.append((int) d);
            if (i % 4 == 0 && i != 16) {
                f = Math.random() * 27;
                reference.append((int) f);
            }
        }
        return reference.substring(0, 16);
    }

    public static Map<String, String> getSearchMap(String search, String sortBy, String sortDir, String status, String portal) {
        Map<String, String> searchMap = new HashMap<>();
        //Search
        if (search == null || search.equalsIgnoreCase("null") || search.isEmpty()) {
            searchMap.put(ConstantUtil.SEARCH, StringUtils.EMPTY);
        } else {
            searchMap.put(ConstantUtil.SEARCH, search);
        }
        //Sort
        if (sortBy == null || sortBy.equalsIgnoreCase("null") || sortBy.isEmpty()) {
            searchMap.put(ConstantUtil.SORT_BY, StringUtils.EMPTY);
        } else {
            searchMap.put(ConstantUtil.SORT_BY, sortBy);
        }
        //SortDir
        if (sortDir == null || sortDir.equalsIgnoreCase("null") || sortDir.isEmpty()) {
            searchMap.put(ConstantUtil.SORT_DIR, StringUtils.EMPTY);
        } else {
            searchMap.put(ConstantUtil.SORT_DIR, sortDir);
        }
        //Status
        if (status == null || status.equalsIgnoreCase("null") || status.isEmpty()) {
            searchMap.put(ConstantUtil.STATUS, StringUtils.EMPTY);
        } else {
            searchMap.put(ConstantUtil.STATUS, status);
        }
        //Portal
        if (portal == null || portal.equalsIgnoreCase("null") || portal.isEmpty()) {
            searchMap.put(ConstantUtil.PORTAL, StringUtils.EMPTY);
        } else {
            searchMap.put(ConstantUtil.PORTAL, portal);
        }
        return searchMap;
    }

    public static void setTablePagination(Page<?> pages, int page, int size, String sortBy, String sortDir, String status, String search, String paginationPrefix, Model model) {
        if (pages != null) {
            model.addAttribute(ConstantUtil.LIST, pages.getContent());
            model.addAttribute(ConstantUtil.TOTAL_PAGES, pages.getTotalPages());
            model.addAttribute(ConstantUtil.TOTAL_ELEMENTS, pages.getTotalElements());
        }
        model.addAttribute(ConstantUtil.CURRENT_PAGE, page);
        model.addAttribute(ConstantUtil.SIZE, size);
        model.addAttribute(ConstantUtil.SEARCH, search);
        model.addAttribute(ConstantUtil.SORT_BY, sortBy);
        model.addAttribute(ConstantUtil.SORT_DIR, sortDir);
        model.addAttribute(ConstantUtil.SIZE_LIST, UtilHelper.getSizeList());
        if (status != null) {
            model.addAttribute(ConstantUtil.STATUS, status);
            model.addAttribute(ConstantUtil.STATUS_LIST, UtilHelper.status());
        }
        if (paginationPrefix != null) {
            model.addAttribute(ConstantUtil.PAGINATION_POSTFIX, "&size=" + size + "&status=" + status + "&search=" + search + "&sortBy=" + sortBy + "&sortDir=" + sortDir);
            model.addAttribute(ConstantUtil.PAGINATION_PREFIX, paginationPrefix);
        }
    }

    public static List<Integer> getSizeList() {
        List<Integer> list = new ArrayList<>();
        list.add(50);
        list.add(100);
        list.add(200);
        list.add(500);
        list.add(1000);
        return list;
    }
    public static boolean isBlankString(String string) {
        return string == null || string.trim().isEmpty() || string.isBlank();
    }

    public static String mask(String maskString) {
        if (maskString != null && !maskString.isEmpty()) {
            if (maskString.contains("@")) {
                int at = maskString.indexOf("@");
                // If S is an email
                if (at > 0) {
                    String maskStringLowerCase = maskString.toLowerCase();
                    return maskStringLowerCase.charAt(0) + "******" + maskStringLowerCase.substring(at - 1);
                }
            } else {
                return maskString.substring(0, 2) + "****" + maskString.substring(maskString.length() - 2);
            }
            return maskString;
        }
        return maskString;
    }

    public static Pageable getPageSort(int page, int size, String sortBy, String sortDir) {
        Pageable pageable;
        if (sortDir != null && !sortDir.isEmpty() && !sortDir.equals("null")) {
            if (sortBy != null && !sortBy.isEmpty() && !sortBy.equals("null")) {
                Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
                pageable = PageRequest.of(page - 1, size, sort);
            } else {
                if (sortDir.equalsIgnoreCase(ConstantUtil.DEFAULT_SORT_DIRECTION)) {
                    pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC,ConstantUtil.LST_MODY_DT);
                } else {
                    pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, ConstantUtil.LST_MODY_DT);
                }
            }
        } else {
            pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, ConstantUtil.LST_MODY_DT));
        }
        return pageable;
    }


    public static Pageable pageable(int page, int size, String sortBy, String sortDir) {
        Pageable pageable;
        if (sortBy != null && !sortBy.isEmpty() && !sortBy.equals("null")) {
            Sort sort;
            sort = Sort.by(sortBy).descending();
            pageable = PageRequest.of(page - 1, size, sort);
        } else {
            if (sortDir != null && !sortDir.isEmpty() && !sortDir.equals("null")) {
                if (sortDir.equalsIgnoreCase(ConstantUtil.DEFAULT_SORT_DIRECTION)) {
                    pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, ColumnUtils.LST_MODY_DT);
                } else {
                    pageable = PageRequest.of(page - 1, size, Sort.Direction.ASC, ColumnUtils.LST_MODY_DT);
                }
            } else {
                pageable = PageRequest.of(page - 1, size);
            }
        }
        return pageable;
    }


}
