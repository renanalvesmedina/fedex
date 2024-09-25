package com.mercurio.lms.entrega.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.constantes.ConstantesLetras;
import com.mercurio.lms.constantes.ConstantesNumeros;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.dao.ManifestoEntregaDocumentoDAO;
import com.mercurio.lms.entrega.model.dao.ManifestoEntregaVolumeDAO;
import com.mercurio.lms.entrega.model.dao.RegistrarBaixaEntregaDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;
/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.registrarBaixaEntregasService"
 */
public class RegistrarBaixaEntregasService {
	private static final String ID_CONTROLE_CARGA = "idControleCarga";
	private DomainValueService domainValueService;
	private ManifestoService manifestoService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private RegistrarBaixaEntregaDAO registrarBaixaEntregaDAO;
	private DadosComplementoService dadosComplementoService;
	private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private ManifestoEntregaVolumeDAO manifestoEntregaVolumeDAO;
	private ManifestoEntregaDocumentoDAO manifestoEntregaDocumentoDAO;
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	private EventoVolumeService eventoVolumeService;

	public ControleCarga findControleCargaByMeioTransporte(Long idMeioTransporte, Long idFilial,String tpStatusControleCarga) {
		return getDAO().findControleCargaByMeioTransporte(idMeioTransporte, idFilial,tpStatusControleCarga);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getDAO().getRowCount(criteria);
	}

	public EventoMeioTransporte findLastEvetoMeioTransporteByIdMeioTranspote(Long idMeioTransporte,Long idFilial) {
		return getDAO().findLastEvetoMeioTransporteByIdMeioTranspote(idMeioTransporte,idFilial);
	}

	public void executeInsersaoEvento(String sgFilial, String nrManifesto) {
		incluirEventosRastreabilidadeInternacionalService.insereEventos(
			ConstantesEntrega.MANIFESTO_VIAGEM,
			new StringBuilder(sgFilial).append(" ").append(nrManifesto).toString(),
			ConstantesSim.EVENTO_ENTREGA_REALIZADA_AEROPORTO,
			SessionUtils.getFilialSessao().getIdFilial(),
			JTDateTimeUtils.getDataHoraAtual(),
			null,
			null
		);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		TypedFlatMap tpDoctoServico = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues("DM_TIPO_DOCUMENTO_SERVICO").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpDoctoServico.put(domain.getValue(),domain);
		}
		TypedFlatMap tpIdentificacao = new TypedFlatMap();
		for (Iterator i = domainValueService.findDomainValues("DM_TIPO_IDENTIFICACAO").iterator(); i.hasNext();) {
			DomainValue domain = (DomainValue)i.next();
			tpIdentificacao.put(domain.getValue(),domain);
		}
		List newList = new ArrayList();
		for(Iterator i = rsp.getList().iterator(); i.hasNext();) {
			Object[] projections = (Object[])i.next();
			TypedFlatMap result = new TypedFlatMap();
			if (projections[ConstantesNumeros.ZERO] != null) {
				if ("MAV".equals(projections[ConstantesNumeros.ZERO])){
					result.put("docto_tp",new DomainValue((String)projections[ConstantesNumeros.ZERO],new VarcharI18n((String)projections[ConstantesNumeros.ZERO]),Boolean.TRUE));
					result.put("idManifestoEntregaDocumento",((Long)projections[ConstantesNumeros.OITO]).toString());
				} else {
					result.put("docto_tp",tpDoctoServico.getDomainValue((String)projections[ConstantesNumeros.ZERO]));
					result.put("idManifestoEntregaDocumento",new StringBuffer(((Long)projections[ConstantesNumeros.OITO]).toString()).append(((Long)projections[ConstantesNumeros.NOVE]).toString()).toString());
				}	
			}

			result.put("docto_fi",projections[ConstantesNumeros.UM]);
			result.put("docto_id",projections[ConstantesNumeros.OITO]);
			result.put("docto_fi_id",projections[ConstantesNumeros.ONZE]);
			result.put("docto_nr",projections[ConstantesNumeros.DOIS]);
			if (projections[ConstantesNumeros.TRES] != null) {
				result.put("dest_tpIdent",tpIdentificacao.getDomainValue((String)projections[ConstantesNumeros.TRES]));
				result.put("dest_nrIdent",FormatUtils.formatIdentificacao((String)projections[ConstantesNumeros.TRES],(String)projections[ConstantesNumeros.QUATRO]));
				result.put("dest_nome",projections[ConstantesNumeros.CINCO]);
			}
			if (projections[ConstantesNumeros.OITO] != null && projections[ConstantesNumeros.NOVE] != null)
				result.put("dpe",manifestoService.findDpe((Long)projections[ConstantesNumeros.OITO],(Long)projections[ConstantesNumeros.NOVE]));

			result.put("dsEndereco",projections[ConstantesNumeros.DEZ]);
			result.put("manif_sgF",projections[ConstantesNumeros.SEIS]);
			result.put("manif_idF",projections[ConstantesNumeros.DOZE]);
			result.put("manif_nr",projections[ConstantesNumeros.SETE]);
			result.put("manif_id",projections[ConstantesNumeros.NOVE]);

			result.put("ordem",projections[ConstantesNumeros.TREZE]);
			result.put("cc_nr",projections[ConstantesNumeros.QUATORZE]);
			result.put("cc_sg",projections[ConstantesNumeros.QUINZE]);

			result.put("manifestoViagemNacional.idFilial",projections[ConstantesNumeros.DOZE]);
			result.put("manifestoViagemNacional.sgFilial",projections[ConstantesNumeros.DEZESEIS]);
			result.put("manifestoViagemNacional.idManifesto",projections[ConstantesNumeros.NOVE]);
			result.put("manifestoViagemNacional.nrManifesto",projections[ConstantesNumeros.DEZESETE]);

			result.put("tpManifesto",projections[ConstantesNumeros.DEZOITO]);
			
			result.put("id_awb", projections[ConstantesNumeros.DEZENOVE]);
			result.put("cia_aerea", projections[ConstantesNumeros.VINTE]);

			newList.add(result);
		}
		rsp.setList(newList);
		return rsp;
	}

	public Boolean executeValidateListManifestoEntregaVolume(Long[] idsManifestoEntregaVolume, Long idOcorrenciaEntrega) {
		Boolean validaListManifesto = true;
		
		//Lista os manifestosEntregaVolumes Selecionados na Grid
		ArrayList<ManifestoEntregaVolume> listManifestoEntregaVolumeGrid = new ArrayList<ManifestoEntregaVolume>();
		for (long idManifestoEntregaVolume : idsManifestoEntregaVolume) {
			ManifestoEntregaVolume manifestoEntregaVolume = this.manifestoEntregaVolumeService.findById(idManifestoEntregaVolume);
			listManifestoEntregaVolumeGrid.add(manifestoEntregaVolume);
		}
		
		//Lista todos os manifestosEntregaVolumes para o manifesto de entrega
		ManifestoEntregaVolume manifestoEntregaVolume = listManifestoEntregaVolumeGrid.get(0);
		List<ManifestoEntregaVolume> listManifestoEntregaVolume = this.manifestoEntregaVolumeService.findManifestoEntregaVolumeByManifestoDoctoServico(manifestoEntregaVolume.getManifestoEntrega().getIdManifestoEntrega(),manifestoEntregaVolume.getDoctoServico().getIdDoctoServico());
		
		List<ManifestoEntregaVolume> listManifestoEntregaVolumePendentes = this.manifestoEntregaVolumeService.findManifestoEntregaVolumePendentesByManifestoDoctoServico(manifestoEntregaVolume.getManifestoEntrega().getIdManifestoEntrega(),manifestoEntregaVolume.getDoctoServico().getIdDoctoServico());
				
		if(listManifestoEntregaVolumeGrid.size() == listManifestoEntregaVolumePendentes.size()){
			if(listManifestoEntregaVolumeGrid.size() != listManifestoEntregaVolume.size()){
				for(ManifestoEntregaVolume manifestoVolume : listManifestoEntregaVolume){
					if(manifestoVolume.getOcorrenciaEntrega() != null){
						if(!(idOcorrenciaEntrega.equals(manifestoVolume.getOcorrenciaEntrega().getIdOcorrenciaEntrega()))){
							validaListManifesto = false;
							return validaListManifesto;
						}
					}
				}
			}else{
				return validaListManifesto;
			}
		}else{
			validaListManifesto = false;
			return validaListManifesto;
		}
		
		return validaListManifesto;
	}
	
	public void storeOcorrenciaOnListManifestoEntregaVolume(
			Long[] idsManifestoEntregaVolume, 
			Short cdOcorrenciaEntrega,
			String tpFormaBaixa,
			DateTime dhBaixa,
			Boolean isValidExistenciaPceDestinatario,
			Boolean isValidExistenciaPceRemetente){
		
		for (long idManifestoEntregaVolume : idsManifestoEntregaVolume) {
			this.storeOcorrenciaOnManifestoEntregaVolume(
					idManifestoEntregaVolume, 
					cdOcorrenciaEntrega, 
					tpFormaBaixa,
					dhBaixa, 
					isValidExistenciaPceDestinatario, 
					isValidExistenciaPceRemetente);
		}
	}
	
	public void storeOcorrenciaOnManifestoEntregaVolume(
			Long idManifestoEntregaVolume, 
			Short cdOcorrenciaEntrega,
			String tpFormaBaixa,
			DateTime dhOcorrencia,
			Boolean isValidExistenciaPceDestinatario,
			Boolean isValidExistenciaPceRemetente){
		
		storeOcorrenciaOnManifestoEntregaVolume(
				idManifestoEntregaVolume, cdOcorrenciaEntrega,
				tpFormaBaixa, dhOcorrencia,
				isValidExistenciaPceDestinatario, isValidExistenciaPceRemetente, null, SessionUtils.getUsuarioLogado());
	}

	//LMS-3569 - chamada ajustada para incluir a filial
	public void storeOcorrenciaOnManifestoEntregaVolume(
			Long idManifestoEntregaVolume,
			Short cdOcorrenciaEntrega,
			String tpFormaBaixa,
			DateTime dhOcorrencia,
			Boolean isValidExistenciaPceDestinatario,
			Boolean isValidExistenciaPceRemetente,
			Filial filial, Usuario usuario){
		
		if (dhOcorrencia == null)
			dhOcorrencia = JTDateTimeUtils.getDataHoraAtual();
		
		DateTime dhInclusao = JTDateTimeUtils.getDataHoraAtual();

		if (isValidExistenciaPceDestinatario == null)
			isValidExistenciaPceDestinatario = Boolean.TRUE;
		if (isValidExistenciaPceRemetente == null)
			isValidExistenciaPceRemetente = Boolean.TRUE;
		
		/*
		 * 1 - Verificar se existe alguma ocorrência de entrega com o código
		 * recebido (tabela OCORRENCIA_ENTREGA onde CD_OCORRENCIA_ENTREGA =
		 * cdOcorrenciaEntrega). Se não existir mostrar mensagem LMS-09043
		 * passando o código recebido e abortar a operação;
		 */
		OcorrenciaEntrega ocorrenciaEntrega = ocorrenciaEntregaService.findOcorrenciaEntrega(cdOcorrenciaEntrega);
		if (ocorrenciaEntrega == null)
			throw new BusinessException("LMS-09043");
		
		/*
		 * SE OCORRENCIA DE ENTREGA FOR DO TIPO PARCIAL GERA EXCEÇÃO.
		 * NÃO FOI VALIDADO NO LOOKUP POIS O FRAMEWORK NÃO SUPORTA.
		 */
		if (ocorrenciaEntrega.getTpOcorrencia().getValue().equals(ConstantesLetras.P))
			throw new BusinessException("LMS-09???");
		
		ManifestoEntregaVolume manifestoEntregaVolume = manifestoEntregaVolumeService.findById(idManifestoEntregaVolume);
		
		DoctoServico docto = (DoctoServico) manifestoEntregaVolumeDAO.getAdsmHibernateTemplate().load(
				DoctoServico.class, manifestoEntregaVolume.getDoctoServico().getIdDoctoServico());

		long idManifesto = manifestoEntregaVolume.getManifestoEntrega().getIdManifestoEntrega();
		
		/*
		 * 3 - Se a data/hora da baixa (dhBaixa) for menor que a data/hora de
		 * emissão do manifesto de entrega (MANIFESTO.DH_EMISSAO) mostrar
		 * mensagem LMS-09047 e abortar a operação;
		 */
		Manifesto manifesto = (Manifesto) getManifestoEntregaDocumentoDAO().getAdsmHibernateTemplate().load(Manifesto.class, idManifesto);
		
		if (ConstantesLetras.C.equals(tpFormaBaixa)
				&& (manifesto.getDhEmissaoManifesto().compareTo(dhOcorrencia) > ConstantesNumeros.ZERO || JTDateTimeUtils.getDataHoraAtual().compareTo(dhOcorrencia) > 0)) {
			dhOcorrencia = JTDateTimeUtils.getDataHoraAtual();
		}else{
			if(manifesto.getDhEmissaoManifesto().compareTo(dhOcorrencia) > ConstantesNumeros.ZERO){
			throw new BusinessException("LMS-09047");
			}else if(JTDateTimeUtils.getDataHoraAtual().compareTo(dhOcorrencia) < ConstantesNumeros.ZERO){
				throw new BusinessException("LMS-09126", new Object[] { JTDateTimeUtils.formatDateTimeToString(dhOcorrencia),
						JTDateTimeUtils.formatDateTimeToString(JTDateTimeUtils.getDataHoraAtual())});
			}
		}

		verificarOcorrenciasDocumento(docto, tpFormaBaixa);
		
		/*
		 * 5 - Se a forma de baixa for normal (tpFormaBaixa = ‘N’) ou a partir
		 * do fechamento (tpFormaBaixa = ‘F’) Se a data/hora da baixa (dhBaixa)
		 * for menor que a data/hora de saída do controle de cargas na portaria
		 * (MANIFESTO -> CONTROLE_CARGA -> EVENTO_CONTROLE_CARGA.DH_EVENTO onde
		 * TP_EVENTO_CONTROLE_CARGA = ‘SP’) mostrar mensagem LMS-09046 e abortar
		 * a operação; Se a data/hora da baixa (dhBaixa) for maior que a
		 * data/hora de entrada do controle de cargas na portaria (MANIFESTO ->
		 * CONTROLE_CARGA -> EVENTO_CONTROLE_CARGA.DH_EVENTO onde
		 * TP_EVENTO_CONTROLE_CARGA = ‘CP’) mostrar mensagem LMS-09046 e abortar
		 * a operação;
		 */
		if (tpFormaBaixa.equals(ConstantesLetras.N) || tpFormaBaixa.equals(ConstantesLetras.F)) {
			EventoControleCarga ecc = getManifestoEntregaDocumentoDAO().findEventoControleCarga(idManifesto, "CP");
			if (ecc != null && ecc.getDhEvento().compareTo(dhOcorrencia) < 0)
				throw new BusinessException("LMS-09046");
	
			ecc = getManifestoEntregaDocumentoDAO().findEventoControleCarga(idManifesto, "SP");
			if (ecc != null && ecc.getDhEvento().compareTo(dhOcorrencia) > 0)
				throw new BusinessException("LMS-09046");
		}

		manifestoEntregaVolume.setOcorrenciaEntrega(ocorrenciaEntrega);

		if (manifestoEntregaVolume.getOcorrenciaEntrega().getEvento() != null) {
			getEventoVolumeService().generateEventoVolume(manifestoEntregaVolume.getVolumeNotaFiscal(), manifestoEntregaVolume
					.getOcorrenciaEntrega().getEvento().getCdEvento(), "LM", null, manifestoEntregaVolume.getOcorrenciaEntrega(), filial, dhOcorrencia, usuario);
		}

		if (manifesto.getTpManifesto().getValue().equals(ConstantesEntrega.TP_MANIFESTO_ENTREGA)) {
				manifestoEntregaVolume.setDhOcorrencia(dhOcorrencia);
				manifestoEntregaVolume.setDhInclusao(dhInclusao);
				manifestoEntregaVolume.setTpFormaBaixa(new DomainValue(tpFormaBaixa));
				manifestoEntregaVolume.setUsuario(usuario);
		}
			
		manifestoEntregaVolumeService.store(manifestoEntregaVolume);
	}

	/**
	 * LMS-3342 / LMS-4567: 
	 * 1. Se alguma ocorrência do conhecimento estiver bloqueada e ainda não foi liberada 
	 * (DOCTO_SERVICO --> OCORRENCIA_DOCTO_SERVICO.dh_bloqueio IS NOT NULL e OCORRENCIA_DOCTO_SERVICO.dh_liberacao IS NULL) .
	 * 
	 * @param tpFormaBaixa
	 * @param docto
	 * @return
	 */
	private void verificarOcorrenciasDocumento(DoctoServico docto, String tpFormaBaixa) {
		if (!ConstantesLetras.A.equals(tpFormaBaixa)) {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(OcorrenciaDoctoServico.class);
		detachedCriteria.add(Restrictions.eq("doctoServico.idDoctoServico", docto.getIdDoctoServico()));
			detachedCriteria.add(Restrictions.isNotNull("dhBloqueio"));
			detachedCriteria.add(Restrictions.isNull("dhLiberacao"));
		List<OcorrenciaDoctoServico> listaOcorrencias = ocorrenciaDoctoServicoService.findByDetachedCriteria(detachedCriteria);

			/*
			 * Se a data de liberação do documento for igual a nulo
			 * (OCORRENCIA_DOCTO_SERVICO.dh_liberacao IS NULL), mostrar a
			 * mensagem LMS-09137.
			 */
			if (!listaOcorrencias.isEmpty()) {
				throw new BusinessException("LMS-09137");
			}
		}
	}
	
	public void executeConfirmation(
			Long idManifesto,
			Long idDoctoServico,
			Short cdOcorrenciaEntrega,
			String nmRecebedor,
			String obManifesto,
			Boolean isValidExistenciaPceRemetente,
			Boolean isValidExistenciaPceDestinatario,
			DateTime dhOcorrencia, 
			DomainValue complementoBaixaSPP,
			Boolean isDocumentoFisico,
			String tpEntregaParcial,
                        String rg
	) {
		String tpFormaBaixa = "N";
		/*CQPRO00023468*/
		DoctoServico doc = new DoctoServico();
		doc.setIdDoctoServico(idDoctoServico);
				
		dadosComplementoService.executeOcorrenciaSPPCliente(doc, complementoBaixaSPP);		
				
		manifestoEntregaDocumentoService.executeBaixaDocumento(
				idManifesto, idDoctoServico, cdOcorrenciaEntrega, tpFormaBaixa, tpEntregaParcial, dhOcorrencia, nmRecebedor,
				obManifesto, isValidExistenciaPceRemetente, isValidExistenciaPceDestinatario,isDocumentoFisico, rg, null, null, SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
	}

	public List findControleCargaByManifestoEntrega(Long idManifestoEntrega) {
		return getDAO().findControleCargaByManifestoEntrega(idManifestoEntrega);
	}

	public List findControleCargaByDoctoServico(Long idDoctoServico) {
		return getDAO().findControleCargaByDoctoServico(idDoctoServico);
	}

	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String, Object>> findPaginatedEventosColetas(TypedFlatMap criteria) {
		criteria.put(ID_CONTROLE_CARGA, criteria.getLong(ID_CONTROLE_CARGA));
		return getDAO().findPaginatedEventosColetas(new PaginatedQuery(criteria));
	}

	public Integer getRowCountEventosColetas(TypedFlatMap criteria) {
		criteria.put(ID_CONTROLE_CARGA, criteria.getLong(ID_CONTROLE_CARGA));
		return getDAO().getRowCountEventosColetas(criteria);
	}
	
	public void setDAO(RegistrarBaixaEntregaDAO dao) {
		this.registrarBaixaEntregaDAO = dao;
	}
	private RegistrarBaixaEntregaDAO getDAO() {
		return this.registrarBaixaEntregaDAO;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}

	public DadosComplementoService getDadosComplementoService() {
		return dadosComplementoService;
}

	public void setDadosComplementoService(
			DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}

	public void setManifestoEntregaVolumeService(ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
		this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
}

	public ManifestoEntregaVolumeService getManifestoEntregaVolumeService() {
		return manifestoEntregaVolumeService;
	}

	public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public OcorrenciaEntregaService getOcorrenciaEntregaService() {
		return ocorrenciaEntregaService;
	}

	public void setManifestoEntregaVolumeDAO(ManifestoEntregaVolumeDAO manifestoEntregaVolumeDAO) {
		this.manifestoEntregaVolumeDAO = manifestoEntregaVolumeDAO;
	}

	public ManifestoEntregaVolumeDAO getManifestoEntregaVolumeDAO() {
		return manifestoEntregaVolumeDAO;
	}

	public void setManifestoEntregaDocumentoDAO(ManifestoEntregaDocumentoDAO manifestoEntregaDocumentoDAO) {
		this.manifestoEntregaDocumentoDAO = manifestoEntregaDocumentoDAO;
	}

	public ManifestoEntregaDocumentoDAO getManifestoEntregaDocumentoDAO() {
		return manifestoEntregaDocumentoDAO;
	}

	public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}

	public PreManifestoDocumentoService getPreManifestoDocumentoService() {
		return preManifestoDocumentoService;
	}

	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	
	public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}

	public VersaoDescritivoPceService getVersaoDescritivoPceService() {
		return versaoDescritivoPceService;
	}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public EventoVolumeService getEventoVolumeService() {
		return eventoVolumeService;
	}
}
