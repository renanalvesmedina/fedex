package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.HistoricoEfetivacao;
import com.mercurio.lms.vendas.model.dao.HistoricoEfetivacaoDAO;

public class HistoricoEfetivacaoService extends CrudService<HistoricoEfetivacao, Long> {
	
	private HistoricoEfetivacaoDAO getHistoricoEfetivacaoDAO() {
		return (HistoricoEfetivacaoDAO) getDao();
	}
	
	public void setHistoricoEfetivacaoDAO(HistoricoEfetivacaoDAO historicoEfetivacaoDAO) {
        setDao(historicoEfetivacaoDAO);
    }
	
	@SuppressWarnings("rawtypes")
	public List findByIdSimulacao(Long idSimulacao){
		return getHistoricoEfetivacaoDAO().findByIdSimulacao(idSimulacao);
	}
	
	@SuppressWarnings("rawtypes")
	public HistoricoEfetivacao findLastHistoricoByIdSimulacao(Long idSimulacao){
		List historicos = getHistoricoEfetivacaoDAO().findByIdSimulacao(idSimulacao);
		
		HistoricoEfetivacao historicoEfetivacao = null;
		for (Object object : historicos) {
			HistoricoEfetivacao tmp = (HistoricoEfetivacao) object;
			if(historicoEfetivacao == null){
				historicoEfetivacao = tmp;
			} else if(tmp.getDhSolicitacao().isAfter(historicoEfetivacao.getDhSolicitacao())){
				historicoEfetivacao = tmp;
			}
		}
		
		if(historicoEfetivacao != null){
			Hibernate.initialize(historicoEfetivacao.getUsuarioSolicitacao());
			Hibernate.initialize(historicoEfetivacao.getUsuarioSolicitacao().getUsuarioADSM());
		}		
		
		return historicoEfetivacao;
	}

	/**
	 * Busca lista de atributos de <tt>HistoricoEfetivacao</tt> relacionadas a
	 * uma <tt>Simulacao</tt> com id correspondente atributo
	 * <tt>simulacao.idSimulacao</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>simulacao.idSimulacao</tt>
	 * @return lista de <tt>SimulacaoAnexo</tt>
	 */
	public ResultSetPage<TypedFlatMap> findHistoricoEfetivacaoList(TypedFlatMap criteria) {
		return getHistoricoEfetivacaoDAO().findHistoricoEfetivacaoList(criteria);
	}

	/**
	 * Busca quantidade de <tt>HistoricoEfetivacao</tt> relacionadas a uma
	 * <tt>Simulacao</tt> com id correspondente atributo
	 * <tt>simulacao.idSimulacao</tt> mapeado no critério.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>idSimulacao</tt>
	 * @return quantidade de <tt>HistoricoEfetivacao</tt>
	 */
	public Integer findHistoricoEfetivacaoRowCount(TypedFlatMap criteria) {
		Long idSimulacao = criteria.getLong("simulacao.idSimulacao");
		return getHistoricoEfetivacaoDAO().findHistoricoEfetivacaoRowCount(idSimulacao);
	}

	/**
	 * Exclui um conjunto de <tt>HistoricoEfetivacao</tt> de acordo com a lista de
	 * id's mapeada no atributo <tt>ids</tt>.
	 * 
	 * @param criteria
	 *            mapa incluindo <tt>ids</tt>
	 */
	public void removeHistoricoEfetivacao(TypedFlatMap criteria) {
		List<Long> ids = new ArrayList<Long>();
		for (Object object : criteria.getList("ids")) {
			if (object instanceof String) {
				ids.add(Long.valueOf((String) object));
			}
		}
		getHistoricoEfetivacaoDAO().removeByIds(ids);
	}
	
	
	@Override
	public Serializable store(HistoricoEfetivacao bean) {
		return super.store(bean);
	}

}
