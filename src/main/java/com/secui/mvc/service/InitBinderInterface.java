package com.secui.mvc.service;

import com.secui.mvc.common.editor.BigDecimalEditor;
import com.secui.mvc.common.editor.DateCustomEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.math.BigDecimal;
import java.util.Date;

public interface InitBinderInterface {
    @InitBinder
    default void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(BigDecimal.class, new BigDecimalEditor());
        binder.registerCustomEditor(Date.class, new DateCustomEditor());
    }
}
