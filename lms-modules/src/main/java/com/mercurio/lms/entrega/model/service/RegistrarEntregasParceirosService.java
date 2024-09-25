package com.mercurio.lms.entrega.model.service;

import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.dao.RegistrarEntregasParceirosDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.registrarEntregasParceirosService"
 */
public class RegistrarEntregasParceirosService {
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private DoctoServicoService doctoServicoService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private RegistrarEntregasParceirosDAO registrarEntregasParceirosDAO;
	private AgendamentoEntregaService agendamentoEntregaService;
	
	public ControleCarga findControleCargaByMeioTransporte(Long idMeioTransporte, Long idFilial,String tpStatusControleCarga) {
		return getRegistrarEntregasParceirosDAO().findControleCargaByMeioTransporte(idMeioTransporte, idFilial,tpStatusControleCarga);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getRegistrarEntregasParceirosDAO().getRowCount(criteria);
	}

	public EventoMeioTransporte findLastEvetoMeioTransporteByIdMeioTranspote(Long idMeioTransporte,Long idFilial) {
		return getRegistrarEntregasParceirosDAO().findLastEvetoMeioTransporteByIdMeioTranspote(idMeioTransporte,idFilial);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getRegistrarEntregasParceirosDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		return rsp;
	}

	public void executeConfirmation(
			Long idDoctoServico,
			Long idOcorrenciaEntrega,
			String nmRecebedor,
			String dsDoctoServico,
			DateTime dhEvento,
			String tpDocumento
	) {
		DateTime dhAtual = new DateTime(dhEvento.getZone());

		if (dhAtual.compareTo(dhEvento) < 0)
			throw new BusinessException("LMS-00074");

		Short cdEvento = null;
		if (idOcorrenciaEntrega != null) {
			OcorrenciaEntrega ocorrenciaEntrega = ocorrenciaEntregaService.findById(idOcorrenciaEntrega);
			if (ocorrenciaEntrega.getEvento() == null)
				throw new BusinessException("LMS-09103");

			cdEvento = ocorrenciaEntrega.getEvento().getCdEvento();
		}
 		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
 				cdEvento,
 				idDoctoServico,
 				SessionUtils.getFilialSessao().getIdFilial(),
 				dsDoctoServico,
 				dhEvento,
 				null,
 				null,
 				tpDocumento,
 				idOcorrenciaEntrega,
 				null,
 				null,
				SessionUtils.getUsuarioLogado()
 		);
	}

	// LMS-4599 -- busca os dados para a tela de manter agendamentos de empresas parceiras
		public TypedFlatMap findDadosAgendamentoParceira(TypedFlatMap doctoServico){
			TypedFlatMap result = new TypedFlatMap();
			DoctoServico documentoServico = new DoctoServico();
			ManifestoEntregaDocumento manifestoEntregaDocumento = new ManifestoEntregaDocumento();
			
			if(doctoServico != null){
				documentoServico = doctoServicoService.findById(doctoServico.getLong("idDoctoServico"));
				manifestoEntregaDocumento = manifestoEntregaDocumentoService.findById(doctoServico.getLong("idManifestoEntregaDocumento"));
				
				result.put("tipoEmpresaSessao", SessionUtils.getEmpresaSessao().getTpEmpresa().getValue());
				
				result.put("idFilialAgendamento", manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getControleCarga().getFilialByIdFilialOrigem().getIdFilial());
				result.put("sgFilialAgendamento", manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
				result.put("nmFilialAgendamento", manifestoEntregaDocumento.getManifestoEntrega().getManifesto().getControleCarga().getFilialByIdFilialOrigem().getPessoa().getNmFantasia()); // arrumar
				
				result.put("idDestinatario", documentoServico.getClienteByIdClienteDestinatario().getIdCliente());
				result.put("nrDestinatario", documentoServico.getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacaoFormatado());
				result.put("destinatario", documentoServico.getClienteByIdClienteDestinatario().getPessoa().getNmFantasia());
				
				result.put("tipoDocto", documentoServico.getTpDocumentoServico().getValue());
				result.put("sgDoctoServico", documentoServico.getFilialByIdFilialOrigem().getSgFilial());//
				result.put("nrDoctoServico", documentoServico.getNrDoctoServico());
				
				result.put("idFilialDestino", documentoServico.getFilialByIdFilialDestino().getIdFilial());
				result.put("sgFilialDestino", documentoServico.getFilialByIdFilialDestino().getSgFilial());
				result.put("nmFilialDestino", documentoServico.getFilialByIdFilialDestino().getPessoa().getNmFantasia());
				
				result.put("bl_pode_agendar", SessionUtils.getEmpresaSessao().getBlPodeAgendar());
				
				result.put("idDoctoServico", documentoServico.getIdDoctoServico());
				result.put("idManifestoEntregaDocumento", manifestoEntregaDocumento.getIdManifestoEntregaDocumento());
				
				// Verifica se o documento possui Agendamento aberto
				AgendamentoEntrega agendamentoEntrega = agendamentoEntregaService.findAgendamentoAberto(documentoServico.getIdDoctoServico());
				
				if(agendamentoEntrega != null){								
						result.put("idAgendamentoEntrega", agendamentoEntrega.getIdAgendamentoEntrega());	
				}
				
				
			}
			
			return result;
		}

	public List findManifestoByMeioTransporte(Long idControleCarga,String tpStatusControleCarga,String tpStatusManifesto) {
		return getRegistrarEntregasParceirosDAO().findManifestoByMeioTransporte(idControleCarga,tpStatusControleCarga,tpStatusManifesto);
	}

	public void setRegistrarEntregasParceirosDAO(RegistrarEntregasParceirosDAO dao) {
		this.registrarEntregasParceirosDAO = dao;
	}
	private RegistrarEntregasParceirosDAO getRegistrarEntregasParceirosDAO() {
		return this.registrarEntregasParceirosDAO;
	}

	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public ManifestoEntregaDocumentoService getManifestoEntregaDocumentoService() {
		return manifestoEntregaDocumentoService;
	}

	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public AgendamentoEntregaService getAgendamentoEntregaService() {
		return agendamentoEntregaService;
	}

	public void setAgendamentoEntregaService(AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}
	
	
}
