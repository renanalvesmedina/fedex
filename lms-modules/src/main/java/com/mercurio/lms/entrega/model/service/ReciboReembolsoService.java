package com.mercurio.lms.entrega.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.ChequeReembolso;
import com.mercurio.lms.entrega.model.DocumentoMir;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.dao.ReciboReembolsoDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.sim.model.dao.LMComplementoDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.reciboReembolsoService"
 */
public class ReciboReembolsoService extends CrudService<ReciboReembolso, Long> {
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ChequeReembolsoService chequeReembolsoService;
	private LMComplementoDAO lmComplementoDao;
	private DoctoServicoService doctoServicoService;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	private DocumentoMirService documentoMirService;

	public Map<String, Object> findForUpdate(Long id) {
		Map<String, Object> result = getReciboReembolsoDAO().findForUpdate(id);

		List<Map<String, Object>> notasFiscais = notaFiscalConhecimentoService.findNFByIdDoctoServico((Long)result.get("idDoctoServicoReembolsado"));
		Map<String, Object> mapNF = new HashMap<String, Object>();
		result.put("notasFiscais", notasFiscais);

		if(documentoMirService.findDocMirByIdReciboReembolso(MapUtils.getLong(result, "idDoctoServico"))) {
			result.put("documentoMir", "true");
		} else {
			result.put("documentoMir", "false");
		}

		if(result.get("tpDocumentoServico") != null) {
			Map<String, String> tpDocumentoServico = MapUtils.getMap(result, "tpDocumentoServico");
			String tipo = MapUtils.getString(tpDocumentoServico, "value");
			result.put("nrDoctoServico", FormatUtils.formataNrDocumento(MapUtils.getString(result, "nrDoctoServico"), tipo));
			String descricaoTp = MapUtils.getString(tpDocumentoServico, "description");
			result.put("tpDocumentoServico", descricaoTp);
		}

		if(result.get("tpIdentificacaoRemetente") != null) {
			String tpIdentificacaoR = MapUtils.getString(MapUtils.getMap(result, "tpIdentificacaoRemetente"), "value");
			result.put("nrIdentificacaoRemetente", FormatUtils.formatIdentificacao(tpIdentificacaoR, result.get("nrIdentificacaoRemetente").toString()));
		}

		if(StringUtils.isNotBlank(result.get("tpIdentificacaoDestinatario").toString())) {
			String tpIdentificacaoD = ((Map<String, String>)result.get("tpIdentificacaoDestinatario")).get("value");
			result.put("nrIdentificacaoDestinatario", FormatUtils.formatIdentificacao(tpIdentificacaoD,result.get("nrIdentificacaoDestinatario").toString()));
		}

		if(result.get("dhEmissaoReciboReembolso")!= null && StringUtils.isNotBlank(result.get("dhEmissaoReciboReembolso").toString())) {
			DateTime dataEmissaoRe = (DateTime)result.get("dhEmissaoReciboReembolso");
			result.put("dhEmissaoReciboReembolso", JTFormatUtils.format(dataEmissaoRe, JTFormatUtils.CUSTOM, JTFormatUtils.YEARMONTHDAY));
		}

		if(result.get("dhEmissaoDoc")!= null && StringUtils.isNotBlank(result.get("dhEmissaoDoc").toString())) {
			DateTime dataEmissaoDoc = (DateTime)result.get("dhEmissaoDoc");
			result.put("dhEmissaoDoc", JTFormatUtils.format(dataEmissaoDoc, JTFormatUtils.CUSTOM, JTFormatUtils.YEARMONTHDAY));
		}
		return result;
	}

	@Override
	public ReciboReembolso findById(Long id) {
		return (ReciboReembolso)super.findById(id);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {	
		ResultSetPage result = getReciboReembolsoDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List<Map<String, Object>> lista = AliasToNestedMapResultTransformer.getInstance().transformListResult(result.getList());
		result.setList(lista);

		return result;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getReciboReembolsoDAO().getRowCount(criteria);
	}

	public Integer getRowCountCheques(TypedFlatMap criteria) {
		return getReciboReembolsoDAO().getRowCountCheques(criteria);
	}

	public List<Map<String, Object>> findCheques(TypedFlatMap map) {
		Long idDoctoServico = map.getLong("idDoctoServico");
		return getReciboReembolsoDAO().findCheques(idDoctoServico);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(int i = 0; i<ids.size();i++){
			Long idDoctoServico =(Long)ids.get(i);

			ReciboReembolso reciboReembolso =(ReciboReembolso)getReciboReembolsoDAO().getHibernateTemplate().get(ReciboReembolso.class,idDoctoServico);

			if(reciboReembolso.getChequeReembolsos() != null && !reciboReembolso.getChequeReembolsos().isEmpty()) {
				List<Long> lista = new ArrayList<Long>();
				List<ChequeReembolso> chequesReembolso = reciboReembolso.getChequeReembolsos();
				for (ChequeReembolso chequeReembolso : chequesReembolso) {
					lista.add(chequeReembolso.getIdChequeReembolso());
				}
				chequeReembolsoService.removeByIds(lista);
			}
			getReciboReembolsoDAO().removeRecibos(reciboReembolso);
		}
	}

	/**
	 * Busca os documentos de servico (Recibos Reembolso - RRE)
	 * a partir do id do Manifesto.
	 */
	public List<ReciboReembolso> findRecibosReembolsoByIdManifestoEntrega(Long idManifesto){
		return getReciboReembolsoDAO().findRecibosReembolsoByIdManifestoEntrega(idManifesto);
	}

	public boolean verifyTotalCheques(TypedFlatMap infCheques){
		List<TypedFlatMap> lista = infCheques.getList("listaCheques.chequeReembolso");
		BigDecimal vlReembolso = infCheques.getBigDecimal("vlReembolso");
		BigDecimal total = new BigDecimal(0);
		for (TypedFlatMap chequeReembolso : lista) {
			if(chequeReembolso.getBigDecimal("valorCheque") != null) {
				total = total.add(chequeReembolso.getBigDecimal("valorCheque"));
			}
		}
		int scala = 2;
		if(total.setScale(scala).compareTo(vlReembolso.setScale(scala))>=0)
			return true;

		return false;
	}

	public java.io.Serializable store(ReciboReembolso bean) {
		return super.store(bean);
	}

	public java.io.Serializable storeCheques(Long idDoctoServico, List<ChequeReembolso> chequesReembolso) {
		ReciboReembolso reciboReembolso = (ReciboReembolso)getReciboReembolsoDAO().getHibernateTemplate()
			.get(ReciboReembolso.class, idDoctoServico);
		List<ChequeReembolso> chequesReembolsoOld = reciboReembolso.getChequeReembolsos();

		String tpSituacaoRecibo = reciboReembolso.getTpSituacaoRecibo().getValue();

		// Regra 3.14. O documento de serviço deve aguardar a digitação de cheques.
		if (!tpSituacaoRecibo.equals("CD") && !tpSituacaoRecibo.equals("CR")) {
			throw new BusinessException("LMS-09113");
		}

		BigDecimal vlTotalReembolso = BigDecimalUtils.ZERO;
		for (ChequeReembolso chequeReembolso : chequesReembolso) {
			if (chequeReembolso.getNrBanco() == null
					|| chequeReembolso.getNrAgencia() == null
					|| chequeReembolso.getNrCheque() == null
					|| chequeReembolso.getVlCheque() == null
					|| chequeReembolso.getDtCheque() == null) {
				throw new BusinessException("LMS-09007");
			}
			vlTotalReembolso = vlTotalReembolso.add(chequeReembolso.getVlCheque());
		}
		// Regra 3.11
		if(CompareUtils.ne(vlTotalReembolso, reciboReembolso.getVlReembolso())) {
			throw new BusinessException("LMS-09005");
		}

		boolean canInsert = false;
		boolean canUpdate = false;
		boolean canDelete = false;
		// Se nenhum cheque foi informado na lista:
		if(chequesReembolso.isEmpty()) {
			// Se a situação do recibo era 'Cheques Digitados',
			// devemos atualizar a situação para 'Aguardando digitação dos cheques':
			if ("CD".equals(reciboReembolso.getTpSituacaoRecibo().getValue())) {
				reciboReembolso.setTpSituacaoRecibo(new DomainValue("CR"));
				reciboReembolso.setDhDigitacaoCheque(null);
			// Senão, devemos obrigar a digitação dos cheques.
			} else {
				throw new BusinessException("LMS-09007");
			}
		}

		List<DocumentoMir> documentosMir = reciboReembolso.getDocumentoMirs();
		//Se o recibo não estiver vinculado a uma MIR
		if(documentosMir == null || documentosMir.isEmpty()) {
			//Permite alterar se a situação do recibo for "Aguardando Digitação dos Cheques" (CR)
			if("CR".equals(reciboReembolso.getTpSituacaoRecibo().getValue())) {
				canUpdate = true;
			}
			//Permite incluir ou excluir
			canInsert = true;
			canDelete = true;
		} else {
			//Permite inserir e excluir se estiver vinculado apenas a um MIR do tipo "Entrega para o Administrativo" (EA) 
			if(documentosMir.size() == 1) {
				DocumentoMir documentoMir = documentosMir.get(0);
				if("EA".equals(documentoMir.getMir().getTpMir().getValue())) {
					canInsert = true;
					canDelete = true;
				}
			}
		}

		boolean isChanged = false;
		boolean isDelete = false;
		//Verifica se houve exclusao
		for (ChequeReembolso chequeReembolsoOld : chequesReembolsoOld) {
			isDelete = true;
			Long idChequeReembolsoOld = chequeReembolsoOld.getIdChequeReembolso();
			for(ChequeReembolso chequeReembolso : chequesReembolso) {
				Long idChequeReembolsoNew = chequeReembolso.getIdChequeReembolso();
				if(idChequeReembolsoNew == null) {
					continue;
				}
				if(CompareUtils.eq(idChequeReembolsoOld, idChequeReembolsoNew)) {
					isDelete = false;
					break;
				}
			}
			if(isDelete) {
				if(!canDelete) {
					throw new BusinessException("LMS-09116");
				}
				isChanged = true;
			}
		}

		//Verifica se houve inclusao ou alteracao
		for (ChequeReembolso chequeReembolso : chequesReembolso) {
			Long idChequeReembolso = chequeReembolso.getIdChequeReembolso();
			if(idChequeReembolso == null) {
				if(!canInsert) {
					throw new BusinessException("LMS-09116");
				}
				isChanged = true;
			} else {
				for(ChequeReembolso chequeReembolsoOld : chequesReembolsoOld) {
					if(CompareUtils.eq(idChequeReembolso, chequeReembolsoOld.getIdChequeReembolso())) {
						boolean isEqual = new EqualsBuilder()
							.append(chequeReembolso.getNrBanco(), chequeReembolsoOld.getNrBanco())
							.append(chequeReembolso.getNrAgencia(), chequeReembolsoOld.getNrAgencia())
							.append(chequeReembolso.getNrCheque(), chequeReembolsoOld.getNrCheque())
							.append(chequeReembolso.getVlCheque().doubleValue(), chequeReembolsoOld.getVlCheque().doubleValue())
							.append(chequeReembolso.getDtCheque(), chequeReembolsoOld.getDtCheque())
							.isEquals();
						if(!isEqual) {
							if(!canUpdate) {
								throw new BusinessException("LMS-09116");
							}
							isChanged = true;
						}
					}
				}
			}
			chequeReembolso.setReciboReembolso(reciboReembolso);
		}

		// Se nenhum cheque foi informado na lista:
		if(chequesReembolso.isEmpty()) {
			// Se a situação do recibo era 'Cheques Digitados',
			// atualizar a situação para 'Aguardando digitação dos cheques':
			if ("CD".equals(tpSituacaoRecibo)) {
				reciboReembolso.setTpSituacaoRecibo(new DomainValue("CR"));
				reciboReembolso.setDhDigitacaoCheque(null);
			} else {
				// Senão, obrigar a digitação dos cheques.
				throw new BusinessException("LMS-09007");
			}
		} else if(isChanged) {
			// Se existem cheques e estes foram modificados, atualizar a situação e a data de digitação
			reciboReembolso.setTpSituacaoRecibo(new DomainValue("CD"));
			reciboReembolso.setDhDigitacaoCheque(JTDateTimeUtils.getDataHoraAtual());
		}

		for (ChequeReembolso chequeReembolsoOld : chequesReembolsoOld) {
			getReciboReembolsoDAO().getHibernateTemplate().evict(chequeReembolsoOld);
		}
		chequesReembolsoOld.clear();
		chequesReembolsoOld.addAll(chequesReembolso);

		return this.store(reciboReembolso);
	}

	public List<ReciboReembolso> findRecibosNaoEmitidosByManifesto(Long idManifestoEntrega, Long idManifestoViagem, Long idConhecimento){
		return getReciboReembolsoDAO().findRecibosNaoEmitidosByManifesto(idManifestoEntrega, idManifestoViagem, idConhecimento);
	}

	public List<Map<String, Object>> findLookupCustom(TypedFlatMap criteria) {
		return getReciboReembolsoDAO().findLookupCustom(criteria);
	}

	/**
	 * Cancela 'Recibos de Reembolso Ativos' do 'Conhecimento'
	 * @author Andre Valadas
	 * @param idDoctoServico
	 */
	public void executeCancelaReciboByDoctoServico(Long idDoctoServico){
		//*** Verifica se há recibo de reembolso associado ao conhecimento 
		List<ReciboReembolso> lista = getReciboReembolsoDAO().findRecibosAtivos(idDoctoServico);
		DomainValue tpSituacaoRecibo = new DomainValue("CA");
		for (ReciboReembolso reciboReembolso : lista) {
			reciboReembolso.setTpSituacaoRecibo(tpSituacaoRecibo);
			reciboReembolso.setDhCancelamento(JTDateTimeUtils.getDataHoraAtual());
			store(reciboReembolso);
		}
	}

	/**
	 * @author Andresa Vargas
	 * 
	 * @param nrReciboReembolso
	 * @param idFilial
	 * @return
	 * @see getReciboReembolsoDAO().findByNrReciboReembolsoByFilialOrigem(Long nrReciboReembolso, Long idFilial)
	 */
	public List<ReciboReembolso> findByNrReciboReembolsoByFilialOrigem(Long nrReciboReembolso, Long idFilial){
		return getReciboReembolsoDAO().findByNrReciboReembolsoByFilialOrigem(nrReciboReembolso, idFilial);
	}

	/**
	 * @author Andresa Vargas
	 * 
	 * @param idReciboReembolso
	 * @return
	 * @see getReciboReembolsoDAO().findDocumentosServico(Long idReciboReembolso)
	 */
	public List<ReciboReembolso> findDocumentosServico(Long idReciboReembolso){
		return getReciboReembolsoDAO().findDocumentosServico(idReciboReembolso);
	}

	public Map<String, Object> findReembolsoByIdReembolsado(Long idDoctoServico){
		List<Map<String, Object>> lista = lmComplementoDao.findReembolsoByIdReembolsado(idDoctoServico);
		Map<String, Object> map = lista.get(0);
		if(map.get("vlReembolso") != null) {
			 String vlReembolso = map.get("dsSimbolo") + " " + FormatUtils.formatDecimal("#,##0.00", (BigDecimal) map.get("vlReembolso"));
			 map.put("vlReembolso", vlReembolso);
		 }	
		if(map.get("tpIdentificacaoRem")!= null){
			DomainValue dv = (DomainValue)map.get("tpIdentificacaoRem");
			String nrIdentificacaoRemFormatado = FormatUtils.formatIdentificacao(dv,map.get("nrIdentificacaoRem").toString());
			map.put("nrIdentificacaoRemFormatado", nrIdentificacaoRemFormatado);
		}
		if(map.get("tpIdentificacaoDest")!= null){
			DomainValue dv = (DomainValue)map.get("tpIdentificacaoDest");
			String nrIdentificacaoDestFormatado = FormatUtils.formatIdentificacao(dv,map.get("nrIdentificacaoDest").toString());
			map.put("nrIdentificacaoDestFormatado", nrIdentificacaoDestFormatado);
		}
		if(map.get("dhEmissao")!= null)
			map.put("dhEmissao",JTFormatUtils.format((DateTime)map.get("dhEmissao")));
		return map;
	}

	public List<Map<String, Object>> findRastreamentoME(Long idDoctoServico){
		return lmComplementoDao.findRastreamentoMercadoriaME(idDoctoServico);
	}
	public List<Map<String, Object>> findRastreamentoReembDest(Long idDoctoServico){
		return lmComplementoDao.findRastreamentoReembDest(idDoctoServico);
	}
	public List<Map<String, Object>> findRastreamentoMirEntregaAdm(Long idDoctoServico){
		return lmComplementoDao.findRastreamentoMirEntregaAdm(idDoctoServico);
	}
	public List<Map<String, Object>> findRastreamentoMirEnt(Long idDoctoServico){
		return lmComplementoDao.findRastreamentoMirEnt(idDoctoServico);
	}
	public List<Map<String, Object>> findRastreamentoMirDestOri(Long idDoctoServico){
		return lmComplementoDao.findRastreamentoMirDestOri(idDoctoServico);
	}
	public boolean findReembolsoAba(Long idDoctoServico){
		return lmComplementoDao.findReembolsoAba(idDoctoServico);
	}
	public List<Long> findReembolsadoByIdReembolso(Long idReembolso){
		return getReciboReembolsoDAO().findReembolsadoByIdReembolso(idReembolso);
	}

	/**
	 * Faz a validacao do PCE para a tela de registrarDisposicao.
	 * 
	 * @param idNaoConformidade
	 * @return
	 */
	public TypedFlatMap validatePCE(Long idDoctoServico) {
		
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		TypedFlatMap result = new TypedFlatMap();
		
		if (doctoServico!=null) {
			
			if (doctoServico.getClienteByIdClienteRemetente()!=null) {
				result.put("remetente",
					versaoDescritivoPceService.validatePCE(
						doctoServico.getClienteByIdClienteRemetente().getIdCliente(),
						Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_MERCADORIA_DISPOSICAO),
						Long.valueOf(EventoPce.ID_EVENTO_PCE_MERC_APREENDIDA_FISCALIZACAO),
						Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_GERAR_CARTA_EMIT_CARTA_MERC_APREEND_FISCALIZ)
					)
				);
			}

			if (doctoServico.getClienteByIdClienteDestinatario()!=null) {
				result.put("destinatario", 
					versaoDescritivoPceService.validatePCE(
						doctoServico.getClienteByIdClienteDestinatario().getIdCliente(),
						Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_MERCADORIA_DISPOSICAO),
						Long.valueOf(EventoPce.ID_EVENTO_PCE_MERC_APREENDIDA_FISCALIZACAO),
						Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_GERAR_CARTA_EMIT_CARTA_MERC_APREEND_FISCALIZ)
					)
				);
			}	
		}
		
		return result;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setReciboReembolsoDAO(ReciboReembolsoDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ReciboReembolsoDAO getReciboReembolsoDAO() {
		return (ReciboReembolsoDAO) getDao();
	}
	public void setLmComplementoDao(LMComplementoDAO lmComplementoDao) {
		this.lmComplementoDao = lmComplementoDao;
	}
	public void setChequeReembolsoService(ChequeReembolsoService chequeReembolsoService) {
		this.chequeReembolsoService = chequeReembolsoService;
	}
	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}	
	public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}
	public void setDocumentoMirService(DocumentoMirService documentoMirService) {
		this.documentoMirService = documentoMirService;
	}
}