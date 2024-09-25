package com.mercurio.lms.franqueados.report;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.dto.ResumoOperacaoTipoFreteAnualDTO;
import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.FixoFranqueadoService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class RelatorioSinteticoOperacaoTipoFreteAnualService extends RelatorioSinteticoParticipacaoService {

	private Logger log = LogManager.getLogger(this.getClass());
	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	private FixoFranqueadoService fixoFranqueadoService;
	private PessoaService pessoaService;
	
	/**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		YearMonthDay dtCompetencia = null;
		if (parameters.containsKey("competencia") && parameters.get("competencia") != null) {
			dtCompetencia = (YearMonthDay) parameters.get("competencia");
		}


		List listaDataSources = new ArrayList<List>();
		List<Map<String, Object>> lista = null;
		List<ResumoOperacaoTipoFreteAnualDTO> mediaAnualDTOs = new ArrayList<ResumoOperacaoTipoFreteAnualDTO>();
		
		Calendar calendar = Calendar.getInstance();
		
		for (int i = 1; i <= dtCompetencia.getMonthOfYear(); i++) {
			
			calendar.set(Calendar.YEAR, dtCompetencia.getYear());
			calendar.set(Calendar.MONTH, i-1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			
			parameters.put("competencia", new YearMonthDay(calendar));
			
			lista = doctoServicoFranqueadoService.findRelatorioSinteticoDefault(parameters, true);
			
			if(!lista.isEmpty()){
				ResumoOperacaoTipoFreteAnualDTO resumoOperacaoTipoFreteAnualDTO = converteListaToResumoOperacaoTipoFreteAnualDTO(lista);
				resumoOperacaoTipoFreteAnualDTO.setMes(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,LocaleContextHolder.getLocale()));
				mediaAnualDTOs.add(resumoOperacaoTipoFreteAnualDTO);
				
				List<ResumoOperacaoTipoFreteAnualDTO> anualDTOs = new ArrayList<ResumoOperacaoTipoFreteAnualDTO>();
				anualDTOs.add(resumoOperacaoTipoFreteAnualDTO);
				parameters.put("dataSourceMes" + i, new JRBeanCollectionDataSource(anualDTOs));
				listaDataSources = lista;
			}else{
				parameters.put("dataSourceMes" + i, new JRBeanCollectionDataSource(gerarDtoRelatorioVazioMes(calendar)));
			}
			
		}
		
		for (int i = dtCompetencia.getMonthOfYear()+1; i <= 12; i++) {
	        calendar.set(Calendar.MONTH, i-1);
			parameters.put("dataSourceMes" + i, new JRBeanCollectionDataSource(gerarDtoRelatorioVazioMes(calendar)));
		}
		
		parameters.put("dataSourceMesMedia", new JRBeanCollectionDataSource(gerarDtoRelatorioMedia(mediaAnualDTOs)));

		parameters.put("competencia", dtCompetencia);
		
		parameters.put("parametrosPesquisa", getFilterSummary(parameters));
		parameters.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parameters.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		if (parameters.containsKey("idFilial") && parameters.get("idFilial") != null)
			parameters.put("idFranquia", parameters.get("idFilial"));
		
		parameters.put("Cte", fixoFranqueadoService.getValorCte((Long)parameters.get("idFilial"), (YearMonthDay)parameters.get("competencia")));
		parameters.put("NomeEmpresa", pessoaService.findNomePessoaByCompetenciaIdFranquia((YearMonthDay)parameters.get("competencia"), (Long)parameters.get("idFilial")));
		

		final List listaRetorno = listaDataSources;
		JRReportDataObject jr = new JRReportDataObject() {
			Map parameters = new HashMap();

			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(listaRetorno);
			}

			public Map getParameters() {
				return parameters;
			}

			public void setParameters(Map arg0) {
				parameters = arg0;
			}
		};
		
		jr.setParameters(parameters);

		return jr;
		
	}

	private List<ResumoOperacaoTipoFreteAnualDTO> gerarDtoRelatorioMedia(List<ResumoOperacaoTipoFreteAnualDTO> mediaAnualDTOs) {
		ResumoOperacaoTipoFreteAnualDTO media = calcularMedia(mediaAnualDTOs);
		media.setMes("Média");
		List<ResumoOperacaoTipoFreteAnualDTO> mediaList = new ArrayList<ResumoOperacaoTipoFreteAnualDTO>();
		mediaList.add(media);
		return mediaList;
	}

	private List<ResumoOperacaoTipoFreteAnualDTO> gerarDtoRelatorioVazioMes(Calendar calendar) {
		List<ResumoOperacaoTipoFreteAnualDTO> anualDTOs = new ArrayList<ResumoOperacaoTipoFreteAnualDTO>();
		
		ResumoOperacaoTipoFreteAnualDTO vazia = new ResumoOperacaoTipoFreteAnualDTO();
		Field [] attributes =  ResumoOperacaoTipoFreteAnualDTO.class.getDeclaredFields();
		
	    for (Field field : attributes) {
	        try {
	        	if(!field.getType().equals(long.class)){
	        		PropertyUtils.setSimpleProperty(vazia, field.getName(), null);
	        	}
	        } catch (Exception e) {
	            log.error(e);
	        }

	    }
		
		vazia.setMes(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,LocaleContextHolder.getLocale()));
		anualDTOs.add(vazia);
		return anualDTOs;
	}

	private ResumoOperacaoTipoFreteAnualDTO calcularMedia(List<ResumoOperacaoTipoFreteAnualDTO> anualDTOs) {
		
		ResumoOperacaoTipoFreteAnualDTO media = new ResumoOperacaoTipoFreteAnualDTO();
		
		if(!anualDTOs.isEmpty()){
			Field [] attributes =  ResumoOperacaoTipoFreteAnualDTO.class.getDeclaredFields();
			
			for (ResumoOperacaoTipoFreteAnualDTO resumoOperacaoTipoFreteAnualDTO : anualDTOs) {
		        for (Field field : attributes) {
		
		            try {
		            	if(field.getType().equals(BigDecimal.class)){
		            		
		            		BigDecimal valorDto = (BigDecimal)PropertyUtils.getSimpleProperty(resumoOperacaoTipoFreteAnualDTO, field.getName());
		            		valorDto = valorDto != null ? valorDto : BigDecimal.ZERO;
		            		
		            		BigDecimal valorMedia = (BigDecimal)PropertyUtils.getSimpleProperty(media, field.getName());
		            		valorMedia = valorMedia != null ? valorMedia : BigDecimal.ZERO;
		            		
		            		PropertyUtils.setSimpleProperty(media, field.getName(), valorMedia.add(valorDto));
		            	}
		            } catch (Exception e) {
		                log.error(e);
		            }
		
		        }
			}
			
			BigDecimal numMeses = new BigDecimal(anualDTOs.size());
			for (Field field : attributes) {
				
				try {
					if(field.getType().equals(BigDecimal.class)){
						
						BigDecimal valorMedia = (BigDecimal)PropertyUtils.getSimpleProperty(media, field.getName());
						valorMedia = valorMedia != null ? valorMedia : BigDecimal.ZERO;
						
						PropertyUtils.setSimpleProperty(media, field.getName(), valorMedia.divide(numMeses, 4, FranqueadoUtils.ROUND_TYPE));
					}
				} catch (Exception e) {
					log.error(e);
				}
				
			}
		}
		return media;
	}
	
	private ResumoOperacaoTipoFreteAnualDTO converteListaToResumoOperacaoTipoFreteAnualDTO(List<Map<String, Object>> lista) {
 		ResumoOperacaoTipoFreteAnualDTO resumoDTO = new ResumoOperacaoTipoFreteAnualDTO();
		BigDecimal totalCtesCE = BigDecimal.ZERO;
		BigDecimal totalCtesFE = BigDecimal.ZERO;
		BigDecimal totalCtesCR = BigDecimal.ZERO;
		BigDecimal totalCtesFR = BigDecimal.ZERO;
		BigDecimal totalCtesFL = BigDecimal.ZERO;
		BigDecimal totalCtesAE = BigDecimal.ZERO;
		
		for (Map<String, Object> listaOperacao : lista) {
			
			resumoDTO.setBaseCalcTotal(montarBaseCalcTotal(listaOperacao, resumoDTO));
			
			resumoDTO.setLimitadorTotal(montarLimitadorTotal(listaOperacao, resumoDTO));
			
			resumoDTO.setPesoTotal(montarPesoTotal(listaOperacao, resumoDTO));
			
			if(ConstantesFranqueado.CIF_EXPEDIDO.equalsIgnoreCase((String)listaOperacao.get("tpFreteRel"))){
				totalCtesCE = montarTotalCtes(listaOperacao);
				resumoDTO.setCtesColeta(resumoDTO.getCtesColeta().add(totalCtesCE));
				resumoDTO.setCtesTotal(resumoDTO.getCtesTotal().add(totalCtesCE));
				
				resumoDTO.setPesoColeta(montarPeso(resumoDTO.getPesoColeta(), listaOperacao));
				resumoDTO.setFreteColeta(montarTotalFreteColeta(listaOperacao, resumoDTO));
				resumoDTO.setFreteTotal(resumoDTO.getFreteTotal().add(buscarTotalFrete(listaOperacao)));
				
				resumoDTO.setParticColeta(montarTotalParticColeta(listaOperacao, resumoDTO));
				resumoDTO.setParticTotal(resumoDTO.getParticTotal().add(buscarTotalParticTipo(listaOperacao)));
				
			}else if(ConstantesFranqueado.FOB_EXPEDIDO.equalsIgnoreCase((String)listaOperacao.get("tpFreteRel"))){
				totalCtesFE = montarTotalCtes(listaOperacao);
				resumoDTO.setCtesColeta(resumoDTO.getCtesColeta().add(totalCtesFE));
				resumoDTO.setCtesTotal(resumoDTO.getCtesTotal().add(totalCtesFE));
				
				resumoDTO.setPesoColeta(montarPeso(resumoDTO.getPesoColeta(), listaOperacao));
				resumoDTO.setFreteColeta(montarTotalFreteColeta(listaOperacao, resumoDTO));
				resumoDTO.setFreteTotal(resumoDTO.getFreteTotal().add(buscarTotalFrete(listaOperacao)));
				
				resumoDTO.setParticColeta(montarTotalParticColeta(listaOperacao, resumoDTO));
				resumoDTO.setParticTotal(resumoDTO.getParticTotal().add(buscarTotalParticTipo(listaOperacao)));
				
				
			}else if(ConstantesFranqueado.CIF_RECEBIDO.equalsIgnoreCase((String)listaOperacao.get("tpFreteRel"))){
				totalCtesCR =  montarTotalCtes(listaOperacao);
				resumoDTO.setCtesEntrega(resumoDTO.getCtesEntrega().add(totalCtesCR));
				resumoDTO.setCtesTotal(resumoDTO.getCtesTotal().add(totalCtesCR));
				
				resumoDTO.setPesoEntrega(montarPeso(resumoDTO.getPesoEntrega(), listaOperacao));
				resumoDTO.setFreteEntrega(montarTotalFreteEntrega(listaOperacao, resumoDTO));
				resumoDTO.setFreteTotal(resumoDTO.getFreteTotal().add(buscarTotalFrete(listaOperacao)));
				
				resumoDTO.setParticEntrega(montarTotalParticEntrega(listaOperacao, resumoDTO));
				resumoDTO.setParticTotal(resumoDTO.getParticTotal().add(buscarTotalParticTipo(listaOperacao)));
				
			}else if(ConstantesFranqueado.FOB_RECEBIDO.equalsIgnoreCase((String)listaOperacao.get("tpFreteRel"))){
				totalCtesFR =  montarTotalCtes(listaOperacao);
				resumoDTO.setCtesEntrega(resumoDTO.getCtesEntrega().add(totalCtesFR));
				resumoDTO.setCtesTotal(resumoDTO.getCtesTotal().add(totalCtesFR));
				
				resumoDTO.setPesoEntrega(montarPeso(resumoDTO.getPesoEntrega(), listaOperacao));
				resumoDTO.setFreteEntrega(montarTotalFreteEntrega(listaOperacao, resumoDTO));
				resumoDTO.setFreteTotal(resumoDTO.getFreteTotal().add(buscarTotalFrete(listaOperacao)));
				
				resumoDTO.setParticEntrega(montarTotalParticEntrega(listaOperacao, resumoDTO));
				resumoDTO.setParticTotal(resumoDTO.getParticTotal().add(buscarTotalParticTipo(listaOperacao)));
				
			}else if(ConstantesFranqueado.FRETE_LOCAL.equalsIgnoreCase((String)listaOperacao.get("tpFreteRel"))){
				totalCtesFL =  montarTotalCtes(listaOperacao);
				resumoDTO.setCtesTotal(resumoDTO.getCtesTotal().add(totalCtesFL));
				resumoDTO.setFreteTotal(resumoDTO.getFreteTotal().add(buscarTotalFrete(listaOperacao)));
				resumoDTO.setParticTotal(resumoDTO.getParticTotal().add(buscarTotalParticTipo(listaOperacao)));
				
			}else if(ConstantesFranqueado.AEREO.equalsIgnoreCase((String)listaOperacao.get("tpFreteRel"))){
				totalCtesAE =  montarTotalCtes(listaOperacao);
				resumoDTO.setCtesTotal(resumoDTO.getCtesTotal().add(totalCtesAE));
				resumoDTO.setFreteTotal(resumoDTO.getFreteTotal().add(buscarTotalFrete(listaOperacao)));
				resumoDTO.setParticTotal(resumoDTO.getParticTotal().add(buscarTotalParticTipo(listaOperacao)));
				
			}
			
		}
		
		resumoDTO.setRpkTotal(calcularPercentual(resumoDTO.getFreteTotal(), resumoDTO.getPesoTotal()));
		resumoDTO.setRpcTotal(calcularPercentual(resumoDTO.getFreteTotal(), resumoDTO.getCtesTotal()));
		resumoDTO.setPpcTotal(calcularPercentual(resumoDTO.getParticTotal(), resumoDTO.getCtesTotal()));
		resumoDTO.setPercFrete(calcularPercentual(resumoDTO.getParticTotal(), resumoDTO.getFreteTotal()));
		resumoDTO.setPercBaseCalc(calcularPercentual(resumoDTO.getParticTotal(), resumoDTO.getBaseCalcTotal()));
		
		resumoDTO.setRpkColeta(calcularPercentual(resumoDTO.getFreteColeta(), resumoDTO.getPesoColeta()));
		resumoDTO.setRpcColeta(calcularPercentual(resumoDTO.getFreteColeta(), resumoDTO.getCtesColeta()));
		resumoDTO.setPpcColeta(calcularPercentual(resumoDTO.getParticColeta(), resumoDTO.getCtesColeta()));
		resumoDTO.setPercFreteColeta(calcularPercentual(resumoDTO.getParticColeta(), resumoDTO.getFreteColeta()));
		resumoDTO.setPercBaseCalcColeta(calcularPercentual(resumoDTO.getParticColeta(), resumoDTO.getBaseCalcTotal()));
		
		resumoDTO.setRpkEntrega(calcularPercentual(resumoDTO.getFreteEntrega(), resumoDTO.getPesoEntrega()));
		resumoDTO.setRpcEntrega(calcularPercentual(resumoDTO.getFreteEntrega(), resumoDTO.getCtesEntrega()));
		resumoDTO.setPpcEntrega(calcularPercentual(resumoDTO.getParticEntrega(), resumoDTO.getCtesEntrega()));
		resumoDTO.setPercFreteEntrega(calcularPercentual(resumoDTO.getParticEntrega(), resumoDTO.getFreteEntrega()));
		resumoDTO.setPercBaseCalcEntrega(calcularPercentual(resumoDTO.getParticEntrega(), resumoDTO.getBaseCalcTotal()));
		
		resumoDTO.setCifExpedido(calcularPercentual(totalCtesCE, resumoDTO.getCtesTotal()));
		resumoDTO.setFobExpedido(calcularPercentual(totalCtesFE, resumoDTO.getCtesTotal()));
		resumoDTO.setCifRecebido(calcularPercentual(totalCtesCR, resumoDTO.getCtesTotal()));
		resumoDTO.setFobRecebido(calcularPercentual(totalCtesFR, resumoDTO.getCtesTotal()));
		resumoDTO.setLocal(calcularPercentual(totalCtesFL, resumoDTO.getCtesTotal()));
		resumoDTO.setAereo(calcularPercentual(totalCtesAE, resumoDTO.getCtesTotal()));
		
		return resumoDTO;
	}

	private BigDecimal calcularPercentual(BigDecimal total, BigDecimal divid) {
		if(total != null && divid != null && divid.compareTo(BigDecimal.ZERO) > 0){
			return total.divide(divid, 4, FranqueadoUtils.ROUND_TYPE);
		}
		return BigDecimal.ZERO;
	}


	private BigDecimal montarPesoTotal(Map<String, Object> listaOperacao, ResumoOperacaoTipoFreteAnualDTO resumoOperacaoTipoFreteAnualDTO) {
		BigDecimal pesototal = (BigDecimal)listaOperacao.get("peso") != null ? (BigDecimal)listaOperacao.get("peso") : BigDecimal.ZERO;
		return resumoOperacaoTipoFreteAnualDTO.getPesoTotal().add(pesototal);
	}
	
	private BigDecimal montarPeso(BigDecimal peso, Map<String, Object> listaOperacao) {
		BigDecimal pesototal = (BigDecimal)listaOperacao.get("peso") != null ? (BigDecimal)listaOperacao.get("peso") : BigDecimal.ZERO;
		return peso.add(pesototal);
	}

	private BigDecimal montarLimitadorTotal(Map<String, Object> listaOperacao, ResumoOperacaoTipoFreteAnualDTO resumoOperacaoTipoFreteAnualDTO) {
		BigDecimal limiteTotal = (BigDecimal)listaOperacao.get("limt") != null ? (BigDecimal)listaOperacao.get("limt") : BigDecimal.ZERO;
		return resumoOperacaoTipoFreteAnualDTO.getLimitadorTotal().add(limiteTotal);
	}

	private BigDecimal montarBaseCalcTotal(Map<String, Object> listaOperacao, ResumoOperacaoTipoFreteAnualDTO resumoOperacaoTipoFreteAnualDTO) {
		BigDecimal totalBase = (BigDecimal)listaOperacao.get("base") != null ? (BigDecimal)listaOperacao.get("base") : BigDecimal.ZERO;
		return resumoOperacaoTipoFreteAnualDTO.getBaseCalcTotal().add(totalBase);
	}

	private BigDecimal montarTotalCtes(Map<String, Object> listaOperacao) {
		return (BigDecimal)listaOperacao.get("ctesTotal") != null ? (BigDecimal)listaOperacao.get("ctesTotal") : BigDecimal.ZERO;
	}

	private BigDecimal montarTotalFreteColeta(Map<String, Object> listaOperacao, ResumoOperacaoTipoFreteAnualDTO resumoOperacaoTipoFreteAnualDTO) {
		return resumoOperacaoTipoFreteAnualDTO.getFreteColeta().add(buscarTotalFrete(listaOperacao));
	}
	
	private BigDecimal montarTotalFreteEntrega(Map<String, Object> listaOperacao, ResumoOperacaoTipoFreteAnualDTO resumoOperacaoTipoFreteAnualDTO) {
		return resumoOperacaoTipoFreteAnualDTO.getFreteEntrega().add(buscarTotalFrete(listaOperacao));
	}
	
	private BigDecimal buscarTotalFrete(Map<String, Object> listaOperacao) {
		return (BigDecimal)listaOperacao.get("frete") != null ? (BigDecimal)listaOperacao.get("frete") : BigDecimal.ZERO;
	}

	private BigDecimal montarTotalParticColeta(Map<String, Object> listaOperacao, ResumoOperacaoTipoFreteAnualDTO resumoOperacaoTipoFreteAnualDTO) {
		return resumoOperacaoTipoFreteAnualDTO.getParticColeta().add(buscarTotalParticTipo(listaOperacao));
	}
	
	private BigDecimal montarTotalParticEntrega(Map<String, Object> listaOperacao, ResumoOperacaoTipoFreteAnualDTO resumoOperacaoTipoFreteAnualDTO) {
		return resumoOperacaoTipoFreteAnualDTO.getParticEntrega().add(buscarTotalParticTipo(listaOperacao));
	}

	private BigDecimal buscarTotalParticTipo(Map<String, Object> listaOperacao) {
		return (BigDecimal)listaOperacao.get("partic") != null ? (BigDecimal)listaOperacao.get("partic") : BigDecimal.ZERO;
	}

	public void setDoctoServicoFranqueadoService(DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
	
	public void setFixoFranqueadoService(
			FixoFranqueadoService fixoFranqueadoService) {
		this.fixoFranqueadoService = fixoFranqueadoService;
	}
	
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

}
