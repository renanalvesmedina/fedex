package com.mercurio.lms.fretecarreteirocoletaentrega.action;


import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.mercurio.adsm.core.web.HttpServletRequestHolder;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.security.model.Usuario;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contasreceber.report.EmitirBoletoService;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.report.EmitirNotasCreditoColetaEntregaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.reports.ExcelRemoteReportsRunner;
import com.mercurio.lms.fretecarreteirocoletaentrega.reports.NotaCreditoCalculoDoisReportRunner;
import com.mercurio.lms.fretecarreteirocoletaentrega.reports.NotaCreditoCalculoUmReportRunner;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.emitirNotasCreditoAction"
 */

public class EmitirNotasCreditoAction extends ReportActionSupport{

	private Logger log = LogManager.getLogger(this.getClass());
	private FilialService filialService;
	private ProprietarioService proprietarioService;
	private MeioTransporteService meioTransporteService;
	private EmitirNotasCreditoColetaEntregaService emitirNotasCreditoColetaEntregaService;
	private NotaCreditoService notaCreditoService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private ConfiguracoesFacade configuracoesFacade;
	private ReportExecutionManager reportExecutionManager;
	
	public void setEmitirNotasCreditoColetaEntregaService(EmitirNotasCreditoColetaEntregaService emitirNotasCreditoColetaEntregaService) {
		this.emitirNotasCreditoColetaEntregaService = emitirNotasCreditoColetaEntregaService;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public File execute(TypedFlatMap parameters) throws Exception {
		return emitirNotasCreditoColetaEntregaService.executeReport(parameters);
	}
	
	public File executeEmitirNotaCreditoExcel(TypedFlatMap typedFlatMap){
		String tpCalculoNotaCredito = notaCreditoService.findTpCalculoNotaCredito(typedFlatMap.getLong("idNotaCredito"));
		Long idNotaCredito = typedFlatMap.getLong("idNotaCredito");
		Long idControleCarga;
	    File result = null;
		try{
			HttpServletRequest request = HttpServletRequestHolder.getHttpServletRequest();
			String host = "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + request.getContextPath();
			//instancia um runner do excelreport
			if ("C1".equals(tpCalculoNotaCredito)){
				NotaCreditoCalculoUmReportRunner runner = new NotaCreditoCalculoUmReportRunner(host);		
				//passa os dados via Map/List para o runner do report em quest�o.
				runner.setDadosNotaCredito(emitirNotasCreditoColetaEntregaService.findMapDadosExecute(typedFlatMap));
				idControleCarga = Long.valueOf(runner.getDadosNotaCredito().get("ID_CONTROLE_CARGA").toString());
				runner.setListTabelaFrete(emitirNotasCreditoColetaEntregaService.findMapTabelaDeFrete(idNotaCredito, tpCalculoNotaCredito));
				runner.setMapTabelaManifestoColeta(emitirNotasCreditoColetaEntregaService.findMapManifestoColeta(idControleCarga, idNotaCredito, tpCalculoNotaCredito));
				runner.setMapTabelaManifestoEntrega(emitirNotasCreditoColetaEntregaService.findMapManifestoEntrega(idControleCarga, idNotaCredito, tpCalculoNotaCredito));
				runner.setDadosTotalParcelas(emitirNotasCreditoColetaEntregaService.findMapDadosParcelas(idNotaCredito, tpCalculoNotaCredito));
				result =  runner.executeReport(); 
			}
			else{
				NotaCreditoCalculoDoisReportRunner runner = new NotaCreditoCalculoDoisReportRunner(host);		
				//passa os dados via Map/List para o runner do report em quest�o.
				runner.setDadosNotaCredito(emitirNotasCreditoColetaEntregaService.findMapDadosExecute(typedFlatMap));
				idControleCarga = Long.valueOf(runner.getDadosNotaCredito().get("ID_CONTROLE_CARGA").toString());
				runner.setListTabelaFrete(emitirNotasCreditoColetaEntregaService.findMapTabelaDeFrete(idNotaCredito, tpCalculoNotaCredito));
				runner.setMapTabelaManifestoColeta(emitirNotasCreditoColetaEntregaService.findMapManifestoColeta(idControleCarga, idNotaCredito, tpCalculoNotaCredito));
				runner.setMapTabelaManifestoEntrega(emitirNotasCreditoColetaEntregaService.findMapManifestoEntrega(idControleCarga, idNotaCredito, tpCalculoNotaCredito));
				runner.setDadosTotalParcelas(emitirNotasCreditoColetaEntregaService.findMapDadosParcelas(idNotaCredito, tpCalculoNotaCredito));
				result =  runner.executeReport(); 
			}
		}catch (Exception e) {
			log.error(e);
		}
		return result;
	}	
	
	public TypedFlatMap executeReport(TypedFlatMap parameters) throws Exception {
		TypedFlatMap result = new TypedFlatMap();
		
		String reportLocator = "";
		boolean emissao =  !"".equals(parameters.get("dhEmissao")) ;
				
		List generateWorkFlow = new ArrayList();
		if (!"EX".equals(parameters.get("tpViewReport")) && !"VP".equals(parameters.get("tpViewReport"))){
			generateWorkFlow = emitirNotasCreditoColetaEntregaService.executeWorkFlow(parameters);
		}		
		
		if (generateWorkFlow.isEmpty()){
			
			result.put("workflow",Boolean.FALSE);
		} else {
			result.put("workflow",Boolean.TRUE);
			StringBuffer sb = new StringBuffer();
			String token = null;
			for(Object texto : generateWorkFlow) {
				if("pendencia".equals(texto)){
					if(emissao){
						result.put("emissao",emissao);
					}				
					
						result.put("texto",configuracoesFacade.getMensagem("LMS-25063",new Object[]{}));
					
					break;
				}
				if (token == null){
					sb.append(texto);
					token = ", ";
				}else{
					sb.append(token).append(texto);
			}
			result.put("texto",configuracoesFacade.getMensagem("LMS-25043",new Object[]{sb.toString()}));
		}
		}
		
		if (!"EX".equals(parameters.get("tpViewReport"))){
			reportLocator = this.reportExecutionManager.generateReportLocator(emitirNotasCreditoColetaEntregaService, parameters);
		}
		else{		
			reportLocator = reportExecutionManager.generateReportLocator(executeEmitirNotaCreditoExcel(parameters));			
		}		
		
		result.put("path",reportLocator);		
		
		return result;
	}
	public EmitirNotasCreditoColetaEntregaService getEmitirNotasCreditoColetaEntregaService() {
		return emitirNotasCreditoColetaEntregaService;
	}
	
    public TypedFlatMap findFilialSession() {
    	Filial  filial  = SessionUtils.getFilialSessao();
    	TypedFlatMap result = new TypedFlatMap();
    				 result.put("filial.idFilial",filial.getIdFilial());
    				 result.put("filial.sgFilial",filial.getSgFilial());
    				 result.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
    	return result;
    }
	//LOKUPS
    public List findLookupFilial(Map criteria) {
    	return filialService.findLookupFilial(criteria);
    }
    public List findLookupProprietario(Map criteria) {
    	return proprietarioService.findLookup(criteria);
    }
    public List findLookupMeioTransporte(Map criteria) {
    	Map proprietario = (Map)criteria.get("proprietario");
    	Map meioTranspProprietarios = new HashMap();
    	meioTranspProprietarios.put("proprietario",proprietario);
    	criteria.put("meioTranspProprietarios",meioTranspProprietarios);
    	criteria.remove("proprietario");
    	return meioTransporteService.findLookup(criteria);
    }
    public TypedFlatMap findProprietarioByMeioTransporte(Long idMeioTransporte) {
    	Proprietario proprietario = meioTranspProprietarioService.findProprietarioByIdMeioTransporte(idMeioTransporte,JTDateTimeUtils.getDataAtual());
    	if (proprietario != null) {
    		Pessoa pessoa = proprietario.getPessoa();
    		TypedFlatMap result = new TypedFlatMap();
    		result.put("controleCargas.proprietario.idProprietario",proprietario.getIdProprietario());
    		result.put("controleCargas.proprietario.pessoa.nmPessoa",pessoa.getNmPessoa());
    		String nrIdentificacao = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao());
    		result.put("controleCargas.proprietario.pessoa.nrIdentificacaoFormatado",nrIdentificacao);
    		result.put("controleCargas.proprietario.pessoa.nrIdentificacao",nrIdentificacao);
    		return result;
    	}
    	return null;
    }
    public List findLookupNotaCredito(TypedFlatMap criteria) {
    	return notaCreditoService.findLookupCuston(criteria);
    }

    public TypedFlatMap findDateTimeNow() {
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("dateTimeNow",JTDateTimeUtils.getDataHoraAtual());
    	return result;
    }


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}

	public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
		this.notaCreditoService = notaCreditoService;
	}

	public void setMeioTranspProprietarioService(
			MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
