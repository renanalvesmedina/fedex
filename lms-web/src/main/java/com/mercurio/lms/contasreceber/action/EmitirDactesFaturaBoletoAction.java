package com.mercurio.lms.contasreceber.action;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JasperPrint;

import org.springframework.core.io.ClassPathResource;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.security.model.Usuario;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;
import com.mercurio.lms.expedicao.reports.NFeJasperReportFiller;

public abstract class EmitirDactesFaturaBoletoAction extends ReportActionSupport {

	protected File emiteCTEs(Integer nrVias, List<Map<String, Object>> listCtes) {
		File result = null;
		
		if (listCtes.size() > 0) {
			
			final Usuario currentUser = SessionContext.getUser();
			Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? 
											currentUser.getLocale() : new Locale("pt","BR");
	
			try{
				HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
				String host = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
				
				JRRemoteReportsRunner runner = new JRRemoteReportsRunner(currentUserLocale,host);
				
				result =  runner.executeReport(listCtes, nrVias); 
			}catch (Exception e) {
				throw new ADSMException(e);
			}
		}
		
		return result;
	}

	protected File emiteNTE(List<Map<String, Object>> listCtes) {
		File result = null;
		
		if (listCtes.size() > 0) {
			final Usuario currentUser = SessionContext.getUser();
			Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt","BR");
					
			try{
				HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
				String host = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
				
				JRRemoteReportsRunner runner = new JRRemoteReportsRunner(currentUserLocale,host);
	
				ClassPathResource jasperResourceNte = new ClassPathResource("com/mercurio/lms/expedicao/reports/impressaoRPST.jasper");
				JasperPrint jasperPrintNte = null;
				for (Map<String, Object> map : listCtes) {
					Map<String, Object> nfeInfs = (Map<String, Object>) map.get("nfeInfs");
					JasperPrint jasperPrint = NFeJasperReportFiller.executeFillXmlJasperReportRpst(1,
							(String)nfeInfs.get("nfeXML"),
							(List)nfeInfs.get("listNrNotas"),
							(Map<String, String>)nfeInfs.get("infRpst"),
							(List)nfeInfs.get("dsObservacaoDoctoServico"),
							currentUserLocale, jasperResourceNte.getInputStream(), host);
					if(jasperPrintNte == null){
						jasperPrintNte = jasperPrint;
					} else {
						jasperPrintNte.getPages().addAll(jasperPrint.getPages());
					}
				}
				result = runner.createPdf(jasperPrintNte, "nte");
				
			}catch (Exception e) {
				throw new ADSMException(e);
			}
		}
		
		return result;
	}
	
	protected File emiteNSE(List<Map<String, Object>> listCtes) {
		File result = null;
		
		if (listCtes.size() > 0) {
			final Usuario currentUser = SessionContext.getUser();
			Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt","BR");
			
			try{
				HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
				String host = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
				
				JRRemoteReportsRunner runner = new JRRemoteReportsRunner(currentUserLocale,host);
				
				ClassPathResource jasperResourceNte = new ClassPathResource("com/mercurio/lms/expedicao/reports/impressaoRPSS.jasper");
				JasperPrint jasperPrintNte = null;
				for (Map<String, Object> map : listCtes) {
					Map<String, Object> nfeInfs = (Map<String, Object>) map.get("nfeInfs");
					JasperPrint jasperPrint = NFeJasperReportFiller.executeFillXmlJasperReportRpss(1,
							(String)nfeInfs.get("nfeXML"),
							(Map<String, String>)nfeInfs.get("infRpss"),
							(List)nfeInfs.get("dsObservacaoDoctoServico"),
							currentUserLocale, jasperResourceNte.getInputStream(), host);
					if(jasperPrintNte == null){
						jasperPrintNte = jasperPrint;
					} else {
						jasperPrintNte.getPages().addAll(jasperPrint.getPages());
					}
				}
				result = runner.createPdf(jasperPrintNte, "nse");
				
			}catch (Exception e) {
				throw new ADSMException(e);
			}
		}
		
		return result;
	}
	
}
