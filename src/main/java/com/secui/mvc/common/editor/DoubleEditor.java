package com.secui.mvc.common.editor;

import com.secui.mvc.utility.UtilHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;


public class DoubleEditor extends PropertyEditorSupport {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		double rate=0d;
		try {
			  rate = Double.parseDouble(text);
		} catch (NumberFormatException e) {
			logger.error("Exception is raised from CountryBeanEditor");
			rate=0d;
		}
		finally 
		{	
			this.setValue(UtilHelper.twoDecimalRoundOffDouble(rate));
		}		
	}
}
