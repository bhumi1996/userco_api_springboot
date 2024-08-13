package com.secui.mvc.common.editor;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalEditor extends PropertyEditorSupport {

    public void setAsText(String text) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
        try {
            Number number = formatter.parse(text);
            BigDecimal bigDecimal = BigDecimal.valueOf(number.doubleValue());
            setValue(bigDecimal.toPlainString());
        } catch (ParseException e) {
            // handle exception here
        }
    }

}