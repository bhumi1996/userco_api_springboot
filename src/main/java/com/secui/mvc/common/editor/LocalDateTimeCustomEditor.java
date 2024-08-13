package com.secui.mvc.common.editor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeCustomEditor extends PropertyEditorSupport {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public String getAsText() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String ret = null;
        Object value = this.getValue();
        if(value instanceof LocalDateTime)
        {
            ret = ((LocalDateTime) value).format(dateTimeFormatter);
        }
        return ret;
    }

    @Override
    public void setAsText(String text) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime localDateTime = null;
        try {
            localDateTime = LocalDateTime.parse(text,dateTimeFormatter);
        } finally
        {
            this.setValue(localDateTime);
        }
    }
}
