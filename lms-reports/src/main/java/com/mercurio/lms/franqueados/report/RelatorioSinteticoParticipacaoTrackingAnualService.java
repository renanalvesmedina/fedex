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
import com.mercurio.lms.franqueados.dto.TrackingAnualDTO;
import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.FixoFranqueadoService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class RelatorioSinteticoParticipacaoTrackingAnualService extends RelatorioSinteticoParticipacaoService {

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
		List<Map<String, Object>> listaServicosAdicionais = null;
		List<TrackingAnualDTO> mediaAnualDTOs = new ArrayList<TrackingAnualDTO>();
		
		Calendar calendar = Calendar.getInstance();
		
		for (int i = 0; i <= dtCompetencia.getMonthOfYear(); i++) {
			
			calendar.set(Calendar.YEAR, dtCompetencia.getYear());
			calendar.set(Calendar.MONTH, i-1);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			
			parameters.put("competencia", new YearMonthDay(calendar));
			
			listaServicosAdicionais = doctoServicoFranqueadoService.findRelatorioSinteticoServicoAdicional(parameters);
			lista = doctoServicoFranqueadoService.findRelatorioSinteticoDefault(parameters, false);
			
			if(!lista.isEmpty()){
				TrackingAnualDTO trackingAnualDTO = converteListaToTrackingAnualDTO(lista);
				trackingAnualDTO = converteListaToTrackingAnualDTO(listaServicosAdicionais, trackingAnualDTO);
				trackingAnualDTO.setMes(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,LocaleContextHolder.getLocale()));
				
				mediaAnualDTOs.add(trackingAnualDTO);
				
				List<TrackingAnualDTO> anualDTOs = new ArrayList<TrackingAnualDTO>();
				anualDTOs.add(trackingAnualDTO);
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

	private List<TrackingAnualDTO> gerarDtoRelatorioMedia(List<TrackingAnualDTO> mediaAnualDTOs) {
		TrackingAnualDTO media = calcularMedia(mediaAnualDTOs);
		media.setMes("Média");
		List<TrackingAnualDTO> mediaList = new ArrayList<TrackingAnualDTO>();
		mediaList.add(media);
		return mediaList;
	}

	private List<TrackingAnualDTO> gerarDtoRelatorioVazioMes(Calendar calendar) {
		List<TrackingAnualDTO> anualDTOs = new ArrayList<TrackingAnualDTO>();
		
		TrackingAnualDTO vazia = new TrackingAnualDTO();
		Field [] attributes =  TrackingAnualDTO.class.getDeclaredFields();
		
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

	private TrackingAnualDTO calcularMedia(List<TrackingAnualDTO> anualDTOs) {
		TrackingAnualDTO media = new TrackingAnualDTO();
		
		if(!anualDTOs.isEmpty()){
			Field [] attributes =  TrackingAnualDTO.class.getDeclaredFields();
			
			for (TrackingAnualDTO trackingAnualDTO : anualDTOs) {
		        for (Field field : attributes) {
		
		            try {
		            	if(field.getType().equals(BigDecimal.class)){
		            		
		            		BigDecimal valorDto = (BigDecimal)PropertyUtils.getSimpleProperty(trackingAnualDTO, field.getName());
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
	
	private TrackingAnualDTO converteListaToTrackingAnualDTO(List<Map<String, Object>> lista) {
 		TrackingAnualDTO resumoDTO = new TrackingAnualDTO();

		for (Map<String, Object> listaOperacao : lista) {
			if(ConstantesFranqueado.CIF_EXPEDIDO.equalsIgnoreCase((String)listaOperacao.get("TP_FRETE_REL"))){
				resumoDTO.setCifExpedido(resumoDTO.getCifExpedido().add((BigDecimal) listaOperacao.get("VL_DOCTO_SERVICO")));
			}else if(ConstantesFranqueado.FOB_EXPEDIDO.equalsIgnoreCase((String)listaOperacao.get("TP_FRETE_REL"))){
				resumoDTO.setFobExpedido(resumoDTO.getFobExpedido().add((BigDecimal) listaOperacao.get("VL_DOCTO_SERVICO")));
			}else if(ConstantesFranqueado.CIF_RECEBIDO.equalsIgnoreCase((String)listaOperacao.get("TP_FRETE_REL"))){
				resumoDTO.setCifRecebido(resumoDTO.getCifRecebido().add((BigDecimal) listaOperacao.get("VL_DOCTO_SERVICO")));
			}else if(ConstantesFranqueado.FOB_RECEBIDO.equalsIgnoreCase((String)listaOperacao.get("TP_FRETE_REL"))){
				resumoDTO.setFobRecebido(resumoDTO.getFobRecebido().add((BigDecimal) listaOperacao.get("VL_DOCTO_SERVICO")));
			}else if(ConstantesFranqueado.FRETE_LOCAL.equalsIgnoreCase((String)listaOperacao.get("TP_FRETE_REL"))){
				resumoDTO.setLocal(resumoDTO.getLocal().add((BigDecimal) listaOperacao.get("VL_DOCTO_SERVICO")));
			}else if(ConstantesFranqueado.AEREO.equalsIgnoreCase((String)listaOperacao.get("TP_FRETE_REL"))){
				resumoDTO.setAereo(resumoDTO.getAereo().add((BigDecimal) listaOperacao.get("VL_DOCTO_SERVICO")));
			}

			if(ConstantesFranqueado.FRETE_LOCAL.equalsIgnoreCase((String)listaOperacao.get("TP_FRETE"))){
				resumoDTO.setFreteLocal(resumoDTO.getFreteLocal().add((BigDecimal) listaOperacao.get("VL_PARTICIPACAO")));
			}
			
			resumoDTO.setICMS(resumoDTO.getICMS().add((BigDecimal) listaOperacao.get("VL_ICMS")));
			resumoDTO.setPIS(resumoDTO.getPIS().add((BigDecimal) listaOperacao.get("VL_PIS")));
			resumoDTO.setCOFINS(resumoDTO.getCOFINS().add((BigDecimal) listaOperacao.get("VL_COFINS")));
			
			resumoDTO.setDescCobranca(resumoDTO.getDescCobranca().add((BigDecimal) listaOperacao.get("VL_DESCONTO")));
			resumoDTO.setFreteCarreteiro(resumoDTO.getFreteCarreteiro().add((BigDecimal) listaOperacao.get("VL_CUSTO_CARRETEIRO")));
			resumoDTO.setGeneralidade(resumoDTO.getGeneralidade().add((BigDecimal) listaOperacao.get("VL_GENERALIDADE")));
			resumoDTO.setGris(resumoDTO.getGris().add((BigDecimal) listaOperacao.get("VL_GRIS")));
			resumoDTO.setCreditoBaseCalc(resumoDTO.getCreditoBaseCalc().add((BigDecimal) listaOperacao.get("VL_AJUSTE_BASE_NEGATIVA")));
			
			resumoDTO.setBaseCalculo(resumoDTO.getBaseCalculo().add((BigDecimal) listaOperacao.get("VL_BASE_CALCULO")));
			
			resumoDTO.setParticipacaoBasica(resumoDTO.getParticipacaoBasica().add((BigDecimal) listaOperacao.get("VL_KM_TRANSFERENCIA")).add((BigDecimal) listaOperacao.get("VL_KM_COLETA_ENTREGA")));
			
			resumoDTO.setFixoCTRC(resumoDTO.getFixoCTRC().add((BigDecimal) listaOperacao.get("VL_FIXO_COLETA_ENTREGA")));
			
			resumoDTO.setRepasseICMS(resumoDTO.getRepasseICMS().add((BigDecimal) listaOperacao.get("VL_REPASSE_ICMS")));
			resumoDTO.setRepassePIS(resumoDTO.getRepassePIS().add((BigDecimal) listaOperacao.get("VL_REPASSE_PIS")));
			resumoDTO.setRepasseCOFINS(resumoDTO.getRepasseCOFINS().add((BigDecimal) listaOperacao.get("VL_REPASSE_COFINS")));
			
			resumoDTO.setSomaLimitador(resumoDTO.getSomaLimitador().add((BigDecimal) listaOperacao.get("VL_DESCONTO_LIMITADOR")));
			
			resumoDTO.setRepGeneralidades(resumoDTO.getRepGeneralidades().add((BigDecimal) listaOperacao.get("VL_REPASSE_GENERALIDADE")));
			
			resumoDTO.setParticipacaoFinal(resumoDTO.getParticipacaoFinal().add((BigDecimal) listaOperacao.get("VL_PARTICIPACAO")));
			
			if( ! ConstantesFranqueado.SERVICO_ADICIONAL.equalsIgnoreCase((String)listaOperacao.get("TP_FRETE_REL"))){
				resumoDTO.setCteMovimentados(resumoDTO.getCteMovimentados().add(BigDecimal.ONE));
			}
		}
		
		return resumoDTO;
	}
	
	private TrackingAnualDTO converteListaToTrackingAnualDTO(List<Map<String, Object>> listaServicosAdicionais, TrackingAnualDTO resumoDTO) {
		
		for (Map<String, Object> listaOperacao : listaServicosAdicionais) {
			resumoDTO.setServicosAdicionais(resumoDTO.getServicosAdicionais().add((BigDecimal) listaOperacao.get("VL_TOTAL_SERVICO")));
		}
		
		return resumoDTO;
	}

	public BigDecimal calcularPercentual(BigDecimal value, BigDecimal divid) {
		BigDecimal result = BigDecimal.ZERO;
		
		if(value != null && divid != null && divid.compareTo(BigDecimal.ZERO) > 0){
			result = value.divide(divid, 4, FranqueadoUtils.ROUND_TYPE);
		}
		
		return result;
	}

	public void setDoctoServicoFranqueadoService(
			DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
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
