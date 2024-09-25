package com.mercurio.lms.facade.radar.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;

import com.mercurio.adsm.core.ADSMException;
import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.GerarConhecimentoEletronicoXMLService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;
import com.mercurio.lms.expedicao.reports.NFeJasperReportFiller;
import com.mercurio.lms.facade.radar.DoctoServicoFacade;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FileUtils;

import net.sf.jasperreports.engine.JasperPrint;


/**
 * @author Celso Martins
 * @spring.bean id="lms.radar.doctoServicoFacade"
 */
@ServiceSecurity
public class DoctoServicoFacadeImpl implements DoctoServicoFacade {
	
	private static final int NR_VIAS = 1;
	
	private DoctoServicoService  doctoServicoService;
	private UsuarioService usuarioService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	private GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService;
	
	/**
	 * Metodo responsavel por buscar informações de imagem
	 * 
	 */
	@Override
	@MethodSecurity(processGroup = "radar.doctoServico", processName = "findDetailImage", authenticationRequired=false)
	public TypedFlatMap findDetailImage(TypedFlatMap criteria) {
		Object[] retorno = doctoServicoService.findDetailImage(criteria);
		return parseResultDToMap(retorno);
	}
	
	/**
	 * Realiza parse para retornar ym map
	 */
	private TypedFlatMap parseResultDToMap(Object[] result) {
		TypedFlatMap map = new TypedFlatMap();
		if(result != null && result.length > 0){
			map.put("FILIAL", result[0]);
			map.put("CTRC", result[1]);
			map.put("DATA", result[2]);
		}
		return map;
	}

	/**
	 * Metodo responsavel por imprimir cte/rps atraves de um documento de serviço 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@MethodSecurity(processGroup = "radar.doctoServico", processName = "reemitirCteRps", authenticationRequired=false)
	public TypedFlatMap reemitirCteRps(TypedFlatMap criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		byte[] byteFile = null;
		File file = null;
		
		Filial filial = new Filial();
		filial.setIdFilial(doctoServicoService.findById(criteria.getLong("idDoctoServico")).getFilialByIdFilialOrigem().getIdFilial());
		SessionContext.set("FILIAL_KEY", filial);
		
		Map<String, Object> map = monitoramentoDocEletronicoService.executeReemitir(criteria);
		try {
			if(map != null && map.get("cte") != null){
				setXmlMap(map, (Map<String, Object>) map.get("cte"));
				file = buildFileCte(map);
				if(file != null){
					byteFile = FileUtils.readFile(file);
				}
			}else if(map != null && map.get("nfeXML") != null){
				file = buildFileNTE(map);
				if(file != null){
					byteFile = FileUtils.readFile(file);
				}
			}
			
			tfm.put("file", byteFile);
		} catch (Exception e) {
			throw new ADSMException(e);
		}
		return tfm;
	}
	
	/**
	 * seta no corpo do map o node xml para enviar para o jasper gerar os bytes do pdf
	 */
	public void setXmlMap(Map<String, Object> map, Map<String, Object> mapXML){
		if(map != null){
			map.put("xml", mapXML.get("xml"));
		}
	}
	
	/**
	 * seta complemento no corpo do xml 
	 */
	private void setXmlCteComComplementos(List<Map<String,Object>> listFiles){
		gerarConhecimentoEletronicoXMLService.addListXmlCteComComplementos(listFiles);
	}
	
	/**
	 * Gera arquivo RPS com base no xml de entrada
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private File buildFileNTE(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		
		File result = null;
		Usuario usuario = usuarioService.findUsuarioByLogin((String) configuracoesFacade.getValorParametro("USUARIO_RADAR_WEB"));
		SessionContext.setUser(usuario);
		final Usuario currentUser = SessionContext.getUser();
		Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt","BR");
				
		try{
			HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
			String host = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
			
			JRRemoteReportsRunner runner = new JRRemoteReportsRunner(currentUserLocale,host);

			ClassPathResource jasperResourceNte = new ClassPathResource("com/mercurio/lms/expedicao/reports/impressaoRPST.jasper");
			JasperPrint jasperPrintNte = null;
			
			JasperPrint jasperPrint = NFeJasperReportFiller.executeFillXmlJasperReportRpst(1,
					(String)map.get("nfeXML"),
					(List)map.get("listNrNotas"),
					(Map<String, String>)map.get("infRpst"),
					(List)map.get("dsObservacaoDoctoServico"),
					currentUserLocale, jasperResourceNte.getInputStream(), host);
			if(jasperPrintNte == null){
				jasperPrintNte = jasperPrint;
			} else {
				jasperPrintNte.getPages().addAll(jasperPrint.getPages());
			}
			result = runner.createPdf(jasperPrintNte, "nte");
			
		}catch (Exception e) {
			throw new ADSMException(e);
		}
		return result;
	}
	
	/**
	 * Gera arquivo CTE com base no xml de entrada
	 */
	private File buildFileCte(Map<String, Object> map) {
		List<Map<String, Object>> listFiles = new ArrayList<Map<String,Object>>();
		if (map == null || map.isEmpty()) {
			return null;
		}
		
		listFiles.add(map);
		setXmlCteComComplementos(listFiles);
		
		File result = null;
		
		Usuario usuario = usuarioService.findUsuarioByLogin((String) configuracoesFacade.getValorParametro("USUARIO_RADAR_WEB"));
		SessionContext.setUser(usuario);
		final Usuario currentUser = SessionContext.getUser();
		Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? 
										currentUser.getLocale() : new Locale("pt","BR");

		try{
			HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
			String host = "http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath();
			
			JRRemoteReportsRunner runner = new JRRemoteReportsRunner(currentUserLocale,host);
			
			result =  runner.executeReport(listFiles, NR_VIAS); 
		}catch (Exception e) {
			throw new ADSMException(e);
		}
		return result;
	
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public MonitoramentoDocEletronicoService getMonitoramentoDocEletronicoService() {
		return monitoramentoDocEletronicoService;
	}

	public void setMonitoramentoDocEletronicoService(
			MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public GerarConhecimentoEletronicoXMLService getGerarConhecimentoEletronicoXMLService() {
		return gerarConhecimentoEletronicoXMLService;
	}

	public void setGerarConhecimentoEletronicoXMLService(
			GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService) {
		this.gerarConhecimentoEletronicoXMLService = gerarConhecimentoEletronicoXMLService;
	}


}