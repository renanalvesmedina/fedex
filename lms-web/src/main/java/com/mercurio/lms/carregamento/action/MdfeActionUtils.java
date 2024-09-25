package com.mercurio.lms.carregamento.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.security.model.Usuario;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.carregamento.reports.JRRemoteReportsRunnerMDFe;
import com.mercurio.lms.carregamento.reports.MDFeUtils;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MdfeActionUtils {

	private static final Logger LOGGER = LogManager.getLogger(MdfeActionUtils.class);

	public static File imprimirMDFe(List<ManifestoEletronico> manifestos, String obsContingMdfe1, String obsContingMdfe2) {
		
		List<byte[]> dsXmls = new ArrayList<byte[]>();
		List<Long> protocolos = new ArrayList<Long>();
		List<String> observacoes = new ArrayList<String>();
		
		Map<String,List<Map<String,String>>> listChaveCte = new HashMap<String, List<Map<String,String>>>();
		for(ManifestoEletronico manifestoEletronico: manifestos){
			byte[] dsDados = manifestoEletronico.getDsDados();
			dsXmls.add(dsDados);
			protocolos.add(manifestoEletronico.getNrProtocolo());
			observacoes.add(manifestoEletronico.getDsObservacao());
			
            listChaveCte = MDFeUtils.putCtes(listChaveCte, dsDados);
			
		}

		final Usuario currentUser = SessionContext.getUser();
		Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt", "BR");

		HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
		String host = "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath();

		JRRemoteReportsRunnerMDFe runnerMDFe = new JRRemoteReportsRunnerMDFe(currentUserLocale, host);

		File reportFileMVNParaMDFe = null;

		try {
			reportFileMVNParaMDFe = runnerMDFe.executeReportMDFe(dsXmls, protocolos, observacoes, obsContingMdfe1, obsContingMdfe2, listChaveCte);
		} catch (Exception e) {
			LOGGER.error(e);
		}

		return reportFileMVNParaMDFe;
	}
	
}
