package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.OrdemServico;
import com.mercurio.lms.expedicao.model.OrdemServicoAnexo;
import com.mercurio.lms.expedicao.model.OrdemServicoDocumento;
import com.mercurio.lms.expedicao.model.OrdemServicoItem;
import com.mercurio.lms.expedicao.model.ServicoAdicionalCalculo;
import com.mercurio.lms.expedicao.model.dao.OrdemServicoDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class OrdemServicoService extends CrudService<OrdemServico, Long> {
	private CalculoServicoAdicionalService calculoServicoAdicionalService;
	private ConfiguracoesFacade configuracoesFacade;
	private OrdemServicoItemService ordemServicoItemService;
	private OrdemServicoAnexoService ordemServicoAnexoService;
	private OrdemServicoDocumentoService ordemServicoDocumentoService;
		
	@Override
	public void removeById(Long id) {
		OrdemServico os = (OrdemServico) findById(id);	
		
		/* Regra 2.4 - E.T. 04.03.01.01 Manter Ordem de Serviço */
		if(!ConstantesExpedicao.TP_SITUACAO_OS_DIGITADA.equals(os.getTpSituacao().getValue())) {
			throw new BusinessException("LMS-04462");
		}
		
		/* Busca os registros filhos para remoção em cascata */
		List<Long> idsItens = ordemServicoItemService.findIdsByOrdemServico(id);
		List<Long> idsAnexos = ordemServicoAnexoService.findIdsByOrdemServico(id);
		List<Long> idsDoctos = ordemServicoDocumentoService.findIdsByOrdemServico(id);
		
		/* Remove os registros filhos */
		ordemServicoAnexoService.removeByIds(idsAnexos);
		ordemServicoItemService.removeByIds(idsItens);
		ordemServicoDocumentoService.removeByIds(idsDoctos);
		
		/* Remove o registro pai */
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		for(Long id : ids) {
			this.removeById(id);
		}
	}
	
	public Serializable store(OrdemServico ordemServico, 
			List<OrdemServicoAnexo> anexosToRemove,
			List<OrdemServicoAnexo> anexos,
			List<OrdemServicoItem> itensToRemove,
			List<OrdemServicoItem> itens) {
		
		if (itens == null || itens.isEmpty()) {
			throw new BusinessException("LMS-04471");
		}
				
		executeCalculoServicosAdicionais(itens, ordemServico);
		
		if(ordemServico.getIdOrdemServico() == null) {
			ordemServico.setNrOrdemServico(configuracoesFacade.incrementaParametroSequencial(
					SessionUtils.getFilialSessao().getIdFilial(), "NR_ORDEM_SERVICO", true));
		}
		
		Serializable id = super.store(ordemServico);
		
		List<Long> idsNaoRemover = new ArrayList<Long>();
		if (ordemServico.getOrdemServicoDocumentos() != null && !ordemServico.getOrdemServicoDocumentos().isEmpty()) {
			ordemServicoDocumentoService.storeAll(ordemServico.getOrdemServicoDocumentos());
			
			for (OrdemServicoDocumento ordemServicoDocumento : ordemServico.getOrdemServicoDocumentos()) {
				idsNaoRemover.add(ordemServicoDocumento.getIdOrdemServicoDocumento());
			}
		}
		ordemServicoDocumentoService.removeByIdOrdemServicoByNotInIds(ordemServico.getIdOrdemServico(), idsNaoRemover);
		
		/* Remove anexos filhos */
		List<Long> idsAnexosToRemove = new ArrayList<Long>();
		for (OrdemServicoAnexo anexo : anexosToRemove) {
			idsAnexosToRemove.add(anexo.getIdOrdemServicoAnexo());
		}
		ordemServicoAnexoService.removeByIds(idsAnexosToRemove);
		
		/* Remove itens filhos */		
		List<Long> idsItensToRemove = new ArrayList<Long>();
		for (OrdemServicoItem item : itensToRemove) {
			idsItensToRemove.add(item.getIdOrdemServicoItem());
		}
		ordemServicoItemService.removeByIds(idsItensToRemove);
		
		/* Persiste anexos filhos */
		for (OrdemServicoAnexo anexo : anexos) {			
			anexo.setOrdemServico(ordemServico);
			if(anexo.getIdOrdemServicoAnexo() != null && anexo.getIdOrdemServicoAnexo() < 1) {
				anexo.setIdOrdemServicoAnexo(null);
			}
		}
		ordemServicoAnexoService.storeAll(anexos);
				
		/* Persiste anexos filhos */
		for (OrdemServicoItem item : itens) {
			item.setOrdemServico(ordemServico);
			if(item.getIdOrdemServicoItem() != null && item.getIdOrdemServicoItem() < 1) {
				item.setIdOrdemServicoItem(null);
			}
		}
		ordemServicoItemService.storeAll(itens);
		
		return id;
	};
	
	private void executeCalculoServicosAdicionais(List<OrdemServicoItem> itens, OrdemServico ordemServico) {		
		Long idServico = null;
		Long idDivisaoCliente = null;
		
		if(ordemServico.getDivisaoCliente() != null) {
			idDivisaoCliente = ordemServico.getDivisaoCliente().getIdDivisaoCliente();
		}
								
		if(ordemServico.getTpDocumento() != null &&
				ConstantesExpedicao.TP_DOCUMENTO_DOCTO_SERVICO.equals(ordemServico.getTpDocumento().getValue()) &&
				ordemServico.getOrdemServicoDocumentos() != null &&
				ordemServico.getOrdemServicoDocumentos().size() > 0) {
			
			OrdemServicoDocumento osDocto = ordemServico.getOrdemServicoDocumentos().get(0);								
			idServico = osDocto.getDoctoServico().getServico().getIdServico();
		}	
										
				
		List<ServicoAdicionalCalculo> servicosAdicionais = generateServicosAdicionaisCalculo(itens);				
		
		if (ordemServico.getOrdemServicoDocumentos() != null && !ordemServico.getOrdemServicoDocumentos().isEmpty()) {
			calculoServicoAdicionalService.executeCalculo(
					idServico, idDivisaoCliente, servicosAdicionais, ordemServico.getOrdemServicoDocumentos().get(0).getIdOrdemServicoDocumento());			
		} else {
			calculoServicoAdicionalService.executeCalculo(
					idServico, idDivisaoCliente, servicosAdicionais, null);
		}
				
		for(OrdemServicoItem item : itens) {			
			ServicoAdicionalCalculo calculo = 
					calculoServicoAdicionalService.getByCdParcela(item.getParcelaPreco().getCdParcelaPreco(), servicosAdicionais);
			
			item.setVlTabela(calculo.getVlCalculado());										
		}
	}
	
	private List<ServicoAdicionalCalculo> generateServicosAdicionaisCalculo(List<OrdemServicoItem> itens) {
		List<ServicoAdicionalCalculo> servicosAdicionais = new ArrayList<ServicoAdicionalCalculo>();
		for(OrdemServicoItem item : itens) {
			ServicoAdicionalCalculo calculo = new ServicoAdicionalCalculo();
			calculo.setCdParcela(item.getParcelaPreco().getCdParcelaPreco());						
			calculo.setQtKmRodados(item.getNrKmRodado());				
			calculo.setQtDias(calculoServicoAdicionalService.executeCalculoPeriodosEstadia(
					item.getDhPeriodoInicial(), item.getDhPeriodoFinal()));
			calculo.setQtPaletes(item.getQtPalete());
			calculo.setQtSegurancasAdicionais(item.getQtHomem());
			calculo.setVlCusto(item.getVlCusto());
			calculo.setVlNegociado(item.getVlNegociado());		
			
			servicosAdicionais.add(calculo);			
		}
		return servicosAdicionais;
	}
	
	public OrdemServico executeAprovar(Long idOrdemServico) {
		OrdemServico ordemServico = findById(idOrdemServico);
		ordemServico.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_SITUACAO_APROVADA));
		store(ordemServico);
		return ordemServico;
	}
	
	public OrdemServico executeReprovar(Long idOrdemServico, String motivo) {
		OrdemServico ordemServico = findById(idOrdemServico);
		ordemServico.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_SITUACAO_REPROVADA));
		ordemServico.setDsMotivoRejeicao(motivo);
		store(ordemServico);
		return ordemServico;
	}

	@Override
	public OrdemServico findById(Long id) {
		return getOrdemServicoDAO().findById(id);
	}
	
	public ResultSetPage<Map<String, Object>> findPaginated(PaginatedQuery paginatedQuery) {
		ResultSetPage<Map<String, Object>> retorno = getOrdemServicoDAO().findPaginated(paginatedQuery);
		YearMonthDay dtSolicitacao = null;
		
		//LMS - 7113
		for (Map<String, Object> map : retorno.getList()) {
			map.put("nrIdentificacaoTomador", FormatUtils.formatIdentificacao(map.get("tpIdentificacaoTomador").toString(), map.get("nrIdentificacaoTomador").toString()));
			map.put("nrOrdemServico", FormatUtils.completaDados(map.get("nrOrdemServico"), "0", 9, 0, true));
			map.put("tpSituacaoOrdemServico",((DomainValue)map.get("tpSituacaoOrdemServico")).getDescriptionAsString());
			dtSolicitacao = (YearMonthDay) map.get("dtSolicitacao");
			map.put("dtSolicitacao",dtSolicitacao.toString("dd/MM/yyyy"));
			if(map.get("preFatura")!=null){
				map.put("preFatura", map.get("sgFilialCobranca") + " " + FormatUtils.completaDados(map.get("preFatura"), "0", 9, 0, true)) ;
				map.put("tpSituacaoPreFatura", ((DomainValue)map.get("tpSituacaoPreFatura")).getDescriptionAsString());
			}
		}
		
		return retorno;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {		
		return getOrdemServicoDAO().getRowCount(criteria);
	}
	
	public ResultSetPage<Map<String, Object>> findPaginatedAprovacao(PaginatedQuery paginatedQuery) {
		/* Caso não tenha sido informado tomador */
		if(paginatedQuery.getCriteria().get("idCliente") == null) {
			/* Se foi informado uma filial de cobrança na tela, filtra por ela
			 * caso contrário filtrar pelas filiais que o usuário tem acesso */
			List<Long> idsFiliaisUsuario = null;			
			if(paginatedQuery.getCriteria().get("idFilialCobranca") != null) {
				idsFiliaisUsuario = new ArrayList<Long>();
				idsFiliaisUsuario.add((Long)paginatedQuery.getCriteria().get("idFilialCobranca"));
			} else {
				idsFiliaisUsuario = SessionUtils.getIdsFiliaisUsuarioLogado();
			}
			
			paginatedQuery.addCriteria("idsFiliaisUsuario", idsFiliaisUsuario);
		}
		
		ResultSetPage<Map<String, Object>> retorno = getOrdemServicoDAO().findPaginatedAprovacao(paginatedQuery);
		for (Map<String, Object> map : retorno.getList()) {
			map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)map.get("tpIdentificacao"), map.get("nrIdentificacao").toString()));
		}
		return retorno;
	}
	
	public List<OrdemServico> findByDoctoServico(Long idDoctoServico, List<String> tpSituacoes){
		return getOrdemServicoDAO().findByDoctoServico(idDoctoServico, tpSituacoes);
	}
	
	public Integer getRowCountAprovacao(TypedFlatMap criteria) {		
		return getOrdemServicoDAO().getRowCountAprovacao(criteria);
	}	
	
	public ResultSetPage<Map<String, Object>> findPaginatedAprovacaoOrdens(PaginatedQuery paginatedQuery) {
		/* Caso não tenha sido informado tomador */
		if(paginatedQuery.getCriteria().get("idCliente") == null) {
			/* Se foi informado uma filial de cobrança na tela, filtra por ela
			 * caso contrário filtrar pelas filiais que o usuário tem acesso */
			List<Long> idsFiliaisUsuario = null;			
			if(paginatedQuery.getCriteria().get("idFilialCobranca") != null) {
				idsFiliaisUsuario = new ArrayList<Long>();
				idsFiliaisUsuario.add((Long)paginatedQuery.getCriteria().get("idFilialCobranca"));
			} else {
				idsFiliaisUsuario = SessionUtils.getIdsFiliaisUsuarioLogado();
			}
			
			paginatedQuery.addCriteria("idsFiliaisUsuario", idsFiliaisUsuario);
		}
		
		return getOrdemServicoDAO().findPaginatedAprovacaoOrdens(paginatedQuery);
	}
	
	public Integer getRowCountAprovacaoOrdens(TypedFlatMap criteria) {		
		return getOrdemServicoDAO().getRowCountAprovacaoOrdens(criteria);
	}
	
	public void executeUpdateOrdensServicoEmPreFatura(List<Long> idsItens) {
		if(idsItens != null && idsItens.size() > 0) {
			getOrdemServicoDAO().executeUpdateOrdensServicoEmPreFatura(idsItens);
		}
	}
			
	/**
	 * LMS-7113
	 * 
	 */
	public TypedFlatMap findByIdWithPreFatura(Long id) {
		
		OrdemServico ordemServico = getOrdemServicoDAO().findById(id);
		TypedFlatMap preFaturaServico =  getOrdemServicoDAO().findPreFaturaByIdOrdemServico(id);
		
		TypedFlatMap result = new TypedFlatMap();
		
		result.put("ordemServico", ordemServico);
		result.put("preFaturaServico", preFaturaServico);
		
		return result;
	}
	
	public void setOrdemServicoDAO(OrdemServicoDAO reemissaoEtiquetaVolumeDAO) {
		setDao( reemissaoEtiquetaVolumeDAO );
	}

	private OrdemServicoDAO getOrdemServicoDAO() {
		return (OrdemServicoDAO) getDao();
	}

	public void setCalculoServicoAdicionalService(CalculoServicoAdicionalService calculoServicoAdicionalService) {
		this.calculoServicoAdicionalService = calculoServicoAdicionalService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setOrdemServicoItemService(OrdemServicoItemService ordemServicoItemService) {
		this.ordemServicoItemService = ordemServicoItemService;
	}

	public void setOrdemServicoAnexoService(OrdemServicoAnexoService ordemServicoAnexoService) {
		this.ordemServicoAnexoService = ordemServicoAnexoService;
	}

	public void setOrdemServicoDocumentoService(OrdemServicoDocumentoService ordemServicoDocumentoService) {
		this.ordemServicoDocumentoService = ordemServicoDocumentoService;
	}
}
