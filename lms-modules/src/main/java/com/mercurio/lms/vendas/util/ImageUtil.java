package com.mercurio.lms.vendas.util;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.mercurio.adsm.core.report.JRAdsmRunner;
import com.mercurio.adsm.core.util.ADSMInitArgs;

public class ImageUtil {

	private static final Log LOG = LogFactory.getLog(ImageUtil.class);
	
	private ImageUtil(){
		
	}
	
	public static Image getImage(String caminho) {
		String reportHostUrl = ADSMInitArgs.ADSM_REPORT_HOST.getValue();
		Image image = null;

		if (reportHostUrl != null && caminho != null) {
			try {
				image = Image.getInstance(reportHostUrl + caminho);
			} catch (BadElementException e) {
				LOG.error(e);
			} catch (MalformedURLException e) {
				LOG.error(e);
			} catch (IOException e) {
				LOG.error(e);
			}
		}

		return image;
	}

}
