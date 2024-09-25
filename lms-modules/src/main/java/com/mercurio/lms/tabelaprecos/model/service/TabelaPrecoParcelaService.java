package com.mercurio.lms.tabelaprecos.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.dao.TabelaPrecoParcelaDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.tabelaPrecoParcelaService"
 */
public class TabelaPrecoParcelaService extends CrudService<TabelaPrecoParcela, Long> {
	private TabelaPrecoService tabelaPrecoService;
	private ParcelaPrecoService parcelaPrecoService;
	
	/**
	 * Recupera uma instância de <code>TabelaPreco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public TabelaPrecoParcela findById(java.lang.Long id) {
		return (TabelaPrecoParcela)super.findById(id);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getTabelaPrecoParcelaDAO().findPaginatedByIdTabelaPreco(criteria);
	}

  /**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		List<Long> ids = new ArrayList<Long>(1);
		ids.add(id);
		validateTabelasPrecoEfetivadas(ids);
		validateHistoricoWorkflow(id);
		getTabelaPrecoParcelaDAO().removeById(id, true);
	}

	private void validateHistoricoWorkflow(Long id) {
		TabelaPrecoParcela tabelaPrecoParcela = this.findById(id);
		tabelaPrecoService.validatePendenciaWorkflow(tabelaPrecoParcela.getTabelaPreco());
	}
	
	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		validateTabelasPrecoEfetivadas(ids);
		
		/*
		 * Verifica se a TabelaPreco relacionada com TabelaPrecoParcela possui Workflow
		 * pendente de aprovação. Como a TabelaPreco é igual para todos os
		 * TabelaPrecoParcela, realiza a validação somente para o primeiro id de
		 * TabelaPrecoParcela.
		 */
		validateHistoricoWorkflow((Long)ids.get(0));
		
		getTabelaPrecoParcelaDAO().removeByIds(ids, true);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(TabelaPrecoParcela bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setTabelaPrecoParcelaDAO(TabelaPrecoParcelaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private TabelaPrecoParcelaDAO getTabelaPrecoParcelaDAO() {
		return (TabelaPrecoParcelaDAO) getDao();
	}

	/**
	 * Valida se as tabelas preco estao efetivadas atraves dos ids parcelas. 
	 * 
	 * @param ids
	 */
	public void validateTabelasPrecoEfetivadas(List<Long> ids) {
		Integer count = getTabelaPrecoParcelaDAO().getCountTabelaPrecoEfetivadas(ids); 
		if(CompareUtils.gt(count, IntegerUtils.ZERO))
			throw new BusinessException("LMS-30042");
	}

	public List<TypedFlatMap> findMappedList(Map<String, Object> map, boolean onlyActiveValues){
		List<TabelaPrecoParcela> tabelasPrecoParcela = getTabelaPrecoParcelaDAO().findListByCriteria(map);
		List<TypedFlatMap> result = new ArrayList<TypedFlatMap>();
		for(TabelaPrecoParcela tpp : tabelasPrecoParcela) {
			if( ( onlyActiveValues && "A".equals(tpp.getParcelaPreco().getTpSituacao().getValue()) )
					|| !onlyActiveValues ) {
				TypedFlatMap pai = new TypedFlatMap();
				TypedFlatMap filho = new TypedFlatMap();
				filho.put("idParcelaPreco",tpp.getParcelaPreco().getIdParcelaPreco().toString());
				filho.put("nmParcelaPreco",tpp.getParcelaPreco().getNmParcelaPreco().toString());
				pai.put("parcelaPreco",filho);
				pai.put("idParcelaPreco",tpp.getParcelaPreco().getIdParcelaPreco().toString());
				pai.put("nmParcelaPreco",tpp.getParcelaPreco().getNmParcelaPreco().toString());
				filho = new TypedFlatMap();
				filho.put("description",tpp.getParcelaPreco().getTpSituacao().getDescription().toString());
				filho.put("value",tpp.getParcelaPreco().getTpSituacao().getValue().toString());
				filho.put("status",tpp.getParcelaPreco().getTpSituacao().getStatus().toString());
				pai.put("tpSituacao",filho);
				result.add(pai);
			}
		}
		return result;
	}

	public ParcelaPreco findParcelaByIdServicoAdicional(Long idTabelaPreco, Long idServicoAdicional) {
		return getTabelaPrecoParcelaDAO().findParcelaByIdServicoAdicional(idTabelaPreco, idServicoAdicional);
	}

	public List findByTpParcelaPrecoTpPrecificacaoIdTabelaPreco(String tpParcelaPreco, String tpPrecificacao, Long idTabelaPreco) {
		return getTabelaPrecoParcelaDAO().findByTpParcelaPrecoTpPrecificacaoIdTabelaPreco(tpParcelaPreco, tpPrecificacao, idTabelaPreco);
	}

	public List<TabelaPrecoParcela> findParcelasPrecoByIdTabelaPreco(Long idTabelaPreco, String tpPrecificacao){
		return getTabelaPrecoParcelaDAO().findParcelasPrecoByIdTabelaPreco(idTabelaPreco, tpPrecificacao);
	}

	public Long findByIdTabelaPrecoIdParcelaPreco(Long idTabelaPreco, Long idParcelaPreco) {
		return getTabelaPrecoParcelaDAO().findByIdTabelaPrecoIdParcelaPreco(idTabelaPreco, idParcelaPreco);
	}

	public void removeByIdTabelaPreco(Long idTabelaPreco) {
		getTabelaPrecoParcelaDAO().removeByTabelaPreco(idTabelaPreco);
	}

	/**
	 * Busca todas as TabelaPrecoParcela relacionada a tabela de preco recebida
	 * e que suas parcelas de preco possuam codigo igual ao codigo recebido.
	 * 
	 * @param idTabelaPreco id da tabela a ser pesquisada
	 * @param cdParcelaPreco codigo da parcela de filtro
	 * @return lista das parcelas de preco da tabela
	 */
	public List<TabelaPrecoParcela> findByIdTabelaPrecoCdParcelaPreco(Long idTabelaPreco, String cdParcelaPreco) {
		return getTabelaPrecoParcelaDAO().findByIdTabelaPrecoCdParcelaPreco(idTabelaPreco, cdParcelaPreco);
	}

	/**
	 * Busca todas as TabelaPrecoParcela relacionadas a tabela de preco e que
	 * nao estejam relacionadas com nenhuma das parcelas de preco fornecidas.
	 * 
	 * @param idTabelaPreco id da tabela a ser pesquisada
	 * @param idsParcelaPrecos ids de exceções de parcelas de preco
	 * @return lista das parcelas encontradas
	 */
	public List<TabelaPrecoParcela> findByIdTabelaPrecoNotParcelaPreco(Long idTabelaPreco, List<Long> idsParcelaPrecos) {
		return getTabelaPrecoParcelaDAO().findByIdTabelaPrecoNotParcelaPreco(idTabelaPreco, idsParcelaPrecos);
	}

	/**
	 * Busca todas as parcelas de preco que estao relacionadas com a tabela
	 * antiga e não estao relacionadas com a tabela nova.
	 * 
	 * @param idTabelaPrecoNova tabela nova
	 * @param idTabelaPrecoAntiga tabela antiga
	 * @return lista de parcelas de preco
	 */
	public List<Long> findIdsByTabelaPrecoAntigaNotInTabelaPrecoNova(Long idTabelaPrecoNova, Long idTabelaPrecoAntiga) {
		return getTabelaPrecoParcelaDAO().findIdsByTabelaPrecoAntigaNotInTabelaPrecoNova(idTabelaPrecoNova, idTabelaPrecoAntiga);
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public boolean findTaxaPermanenciaCargaOrTaxaFielDepositario(Map criteria) {
		if(criteria.containsKey("idParcelaPreco") && LongUtils.hasValue(MapUtils.getLong(criteria,"idParcelaPreco"))){
			Long idParcelaPreco = MapUtils.getLong(criteria,"idParcelaPreco");
			ParcelaPreco parcela = parcelaPrecoService.findById(idParcelaPreco);

			if(parcela != null && ("IDArmazenagem".equals(parcela.getCdParcelaPreco()) || "IdTaxaFielDep".equals(parcela.getCdParcelaPreco()))){
				return true;
			}
		}
		return false;
	}
	
	public ParcelaPrecoService getParcelaPrecoService() {
		return parcelaPrecoService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	
}