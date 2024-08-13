package com.secui.mvc.common.editor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCustomEditor extends PropertyEditorSupport {

	Logger LOG = LoggerFactory.getLogger(this.getClass());
	
	@Override
    public void setAsText(String text) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(text);
        }catch(ParseException e){
        	LOG.error("ParseException is raised from DateCustomEditor Catch block");        	
        }finally
        {
            this.setValue(date);
        }
    }
	
	@Override
    public String getAsText() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ret = null;
        Object value = this.getValue();
        if(value instanceof Date)
        {
                ret = sdf.format((Date)value);
        }
        return ret;
    }
}
