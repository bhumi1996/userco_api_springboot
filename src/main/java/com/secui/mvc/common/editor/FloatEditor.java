package com.secui.mvc.common.editor;

import com.secui.mvc.utility.UtilHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;


public class FloatEditor extends PropertyEditorSupport {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void setAsText(String text) {
		float rate=0.00f;
		try {
			  rate = Float.parseFloat(text);
		} catch (NumberFormatException e) {
			logger.error("Exception is raised from CountryBeanEditor");
			rate=0.00f;
		}
		finally 
		{	
			this.setValue(UtilHelper.twoDecimalRoundOfFloat(rate));
		}	
	}
}
