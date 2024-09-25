package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.TipoDocumentoEntrega;
import com.mercurio.lms.entrega.model.dao.RegistrarBaixaEntregaOnTimeDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.registrarBaixaEntregasOnTimeService"
 */
public class RegistrarBaixaEntregasOnTimeService {
	private ManifestoService manifestoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private ConfiguracoesFacade configuracoesFacade;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private RegistrarBaixaEntregaOnTimeDAO registrarBaixaEntregaOnTimeDAO; 
	private DadosComplementoService dadosComplementoService;
	
	public ControleCarga findControleCargaByMeioTransporte(Long idMeioTransporte, Long idFilial,String tpStatusControleCarga) {
		return getRegistrarBaixaEntregaOnTimeDAO().findControleCargaByMeioTransporte(idMeioTransporte, idFilial,tpStatusControleCarga);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getRegistrarBaixaEntregaOnTimeDAO().getRowCount(criteria);
	}

	public EventoMeioTransporte findLastEvetoMeioTransporteByIdMeioTranspote(Long idMeioTransporte,Long idFilial, Long idControleCarga) {
		return getRegistrarBaixaEntregaOnTimeDAO().findLastEvetoMeioTransporteByIdMeioTranspote(idMeioTransporte,idFilial, idControleCarga);
	}

	public void executeInsersaoEvento(String sgFilial, String nrManifesto) {
		incluirEventosRastreabilidadeInternacionalService.insereEventos(ConstantesEntrega.MANIFESTO_VIAGEM,new StringBuilder(sgFilial).append(" ").append(nrManifesto).toString(),
				ConstantesSim.EVENTO_ENTREGA_REALIZADA_AEROPORTO,SessionUtils.getFilialSessao().getIdFilial(),JTDateTimeUtils.getDataHoraAtual(),null,null);
	}
	
	public TypedFlatMap executeConfirmation(Long idDoctoServico, Short cdOcorrenciaEntrega, String nmRecebedor, String obManifesto, Boolean isValidExistenciaPceRemetente, Boolean isValidExistenciaPceDestinatario, DateTime dhOcorrencia, DomainValue tpEntregaParcial, String rg) {
		String tpFormaBaixa = "O";
		return executeConfirmation(idDoctoServico, cdOcorrenciaEntrega, nmRecebedor, obManifesto, isValidExistenciaPceRemetente, isValidExistenciaPceDestinatario, dhOcorrencia, tpEntregaParcial, tpFormaBaixa, rg);
	}
	
	public TypedFlatMap executeConfirmation(Long idDoctoServico, Short cdOcorrenciaEntrega, String nmRecebedor, String obManifesto, Boolean isValidExistenciaPceRemetente, Boolean isValidExistenciaPceDestinatario, DateTime dhOcorrencia, DomainValue tpEntregaParcial, String tpFormaBaixa, String rg) {

		List<ManifestoEntregaDocumento> manifestos = manifestoEntregaDocumentoService.findByIdDoctoServico(idDoctoServico);
		ManifestoEntregaDocumento med = manifestos.get(0);
		ManifestoEntrega manifestoEntrega = med.getManifestoEntrega();

		manifestoEntregaDocumentoService.executeBaixaDocumento(manifestoEntrega.getIdManifestoEntrega(),idDoctoServico,cdOcorrenciaEntrega,tpFormaBaixa,tpEntregaParcial.getValue(),dhOcorrencia,nmRecebedor,obManifesto,isValidExistenciaPceRemetente,isValidExistenciaPceDestinatario, rg, null, null);
		
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("cdOcorrenciaEntrega", cdOcorrenciaEntrega);
		OcorrenciaEntrega ocorrenciaEntrega = (OcorrenciaEntrega) ocorrenciaEntregaService.find(criteria).get(0);

		// Apenas exibe as mensagens de alerta caso seja uma ocorrência de entrega,
		// visto que não é necessário o recolhimento de documentos em caso de
		// ocorrências que não são de entrega
		TypedFlatMap result = new TypedFlatMap();
		if ("E".equals(ocorrenciaEntrega.getTpOcorrencia().getValue())) {
			List<TipoDocumentoEntrega> tiposDocumentoEntrega = getRegistrarBaixaEntregaOnTimeDAO().findRegistroDocumentoEntregaBy(idDoctoServico);
			if (!tiposDocumentoEntrega.isEmpty()) {
				StringBuilder warning = new StringBuilder();
				for (TipoDocumentoEntrega tipoDocumentoEntrega : tiposDocumentoEntrega) {
					warning.append(tipoDocumentoEntrega.getDsTipoDocumentoEntrega().getValue());
					warning.append(", ");
				}
				warning.delete(warning.length() - 2, warning.length());
				warning.append(".");
				result.put("warning", configuracoesFacade.getMensagem("LMS-09100", new Object[]{warning.toString()}));
				return result;
			}
		}
		
		if(SessionContext.containsKey("LMS-09143")){
			result.put("LMS09143", true);
			SessionContext.remove("LMS-09143");
			return result;
		}
		
		return null;
	}
	
	public TypedFlatMap executeConfirmation(Long idDoctoServico, Short cdOcorrenciaEntrega, String nmRecebedor, String obManifesto, Boolean isValidExistenciaPceRemetente, Boolean isValidExistenciaPceDestinatario, DomainValue tpEntregaParcial, String rg) {
		DateTime dhOcorrencia = JTDateTimeUtils.getDataHoraAtual();
		return executeConfirmation(idDoctoServico,cdOcorrenciaEntrega,nmRecebedor,obManifesto,isValidExistenciaPceRemetente,isValidExistenciaPceDestinatario,dhOcorrencia, tpEntregaParcial, rg);
	}

	public TypedFlatMap executeConfirmation(Long idDoctoServico, Short cdOcorrenciaEntrega, String nmRecebedor, String obManifesto, Boolean isValidExistenciaPceRemetente, Boolean isValidExistenciaPceDestinatario, DomainValue tpEntregaParcial, String tpFormaBaixa, String rg) {
		DateTime dhOcorrencia = JTDateTimeUtils.getDataHoraAtual();
		return executeConfirmation(idDoctoServico,cdOcorrenciaEntrega,nmRecebedor,obManifesto,isValidExistenciaPceRemetente,isValidExistenciaPceDestinatario,dhOcorrencia, tpEntregaParcial, tpFormaBaixa, rg);
	}

	public TypedFlatMap executeConfirmation(Long idDoctoServico, Short cdOcorrenciaEntrega, String nmRecebedor, String obManifesto, Boolean isValidExistenciaPceRemetente, Boolean isValidExistenciaPceDestinatario, DomainValue complementoBaixaSPP, DomainValue tpEntregaParcial, String rg) {
		/*CQPRO00023468*/
		DoctoServico doc = new DoctoServico();
		doc.setIdDoctoServico(idDoctoServico);
				
		dadosComplementoService.executeOcorrenciaSPPCliente(doc, complementoBaixaSPP);		
		
		return executeConfirmation(idDoctoServico,cdOcorrenciaEntrega,nmRecebedor,obManifesto,isValidExistenciaPceRemetente,isValidExistenciaPceDestinatario,JTDateTimeUtils.getDataHoraAtual(), tpEntregaParcial, rg);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getRegistrarBaixaEntregaOnTimeDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> projections = rsp.getList();
		for (Map<String, Object> projection : projections) {
			TypedFlatMap result = new TypedFlatMap();
			DomainValue tpDocumentoServico = (DomainValue)projection.get("tpDocumentoServico");
			Long idDoctoServico = (Long)projection.get("idDoctoServico");
			if(tpDocumentoServico != null) {
				result.put("docto_tp", tpDocumentoServico);
				if ("MAV".equals(tpDocumentoServico.getValue())) {
					result.put("idManifestoEntregaDocumento", projection.get("idManifestoEntregaDocumento"));
				} else {
					result.put("idManifestoEntregaDocumento", idDoctoServico);
				}
			}

			result.put("docto_fi", projection.get("sgFilialOrigem"));
			result.put("docto_fi_id", projection.get("idFilialOrigem"));
			result.put("docto_id", idDoctoServico);
			result.put("docto_nr", projection.get("nrDoctoServico"));

			DomainValue tpIdentificacao = (DomainValue) projection.get("tpIdentificacao");
			if(tpIdentificacao != null) {
				result.put("dest_tpIdent", tpIdentificacao);
				result.put("dest_nrIdent", FormatUtils.formatIdentificacao(tpIdentificacao.getValue(),(String)projection.get("nrIdentificacao")));
				result.put("dest_nome", (String)projection.get("nmPessoa"));
			}

			Long idManifestoEntrega =  (Long)projection.get("idManifestoEntrega");
			if (idDoctoServico != null && idManifestoEntrega != null)
				result.put("dpe", manifestoService.findDpe(idDoctoServico, idManifestoEntrega));

			result.put("dsEndereco", projection.get("dsEndereco"));
			result.put("manif_sgF", projection.get("sgFilialManifestoEntrega"));
			result.put("manif_idF", projection.get("idFilialManifestoEntrega"));
			result.put("manif_nr", projection.get("nrManifestoEntrega"));
			result.put("manif_id", idManifestoEntrega);

			result.put("cc_nr", projection.get("nrControleCarga"));
			result.put("cc_sg", projection.get("sgFilialControleCarga"));
			result.put("id_awb", projection.get("idAwb"));
			result.put("cia_aerea", projection.get("nmCiaAerea"));

			results.add(result);
		}
		rsp.setList(results);

		return rsp;
	}

	public Integer getRowCountConfirmation(TypedFlatMap criteria) {
		return getRegistrarBaixaEntregaOnTimeDAO().getRowCountConfirmation(criteria);
	}

	public ResultSetPage findPaginatedConfirmation(TypedFlatMap criteria, FindDefinition findDef) {
		ResultSetPage rsp = getRegistrarBaixaEntregaOnTimeDAO().findPaginatedConfirmation(criteria, findDef);
		List<Map<String, Object>> results = rsp.getList();
		for (Map<String, Object> result : results) {
			result.put("nrDpe", manifestoService.findDpe(MapUtils.getLong(result, "idDoctoServico"), MapUtils.getLong(result, "idManifesto")));
		}
		return rsp;
	}

	public void setDAO(RegistrarBaixaEntregaOnTimeDAO dao) {
		this.registrarBaixaEntregaOnTimeDAO = dao;
	}
	private RegistrarBaixaEntregaOnTimeDAO getRegistrarBaixaEntregaOnTimeDAO() {
		return registrarBaixaEntregaOnTimeDAO;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public void setOcorrenciaEntregaService(
			OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public DadosComplementoService getDadosComplementoService() {
		return dadosComplementoService;
}

	public void setDadosComplementoService(
			DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}
}
