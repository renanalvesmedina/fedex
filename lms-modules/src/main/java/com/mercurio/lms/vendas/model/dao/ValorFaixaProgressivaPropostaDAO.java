package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.DestinoProposta;
import com.mercurio.lms.vendas.model.ValorFaixaProgressivaProposta;

public class ValorFaixaProgressivaPropostaDAO extends BaseCrudDao<ValorFaixaProgressivaProposta, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ValorFaixaProgressivaProposta.class;
	}

    @SuppressWarnings("unchecked")
    public List<ValorFaixaProgressivaProposta> findByDestinoPropostaFretePeso(DestinoProposta destinoProposta) {
        StringBuffer query = new StringBuffer();
        query.append("select valor from ")
            .append(ValorFaixaProgressivaProposta.class.getSimpleName()).append(" valor ")
            .append(" join fetch valor.faixaProgressivaProposta faixa")
            .append(" where faixa.produtoEspecifico is null")
            .append(" and valor.destinoProposta.id = :idDestinoProposta");
        
        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), "idDestinoProposta", destinoProposta.getIdDestinoProposta());
    }
    
    
    @SuppressWarnings("unchecked")
    public List<ValorFaixaProgressivaProposta> findByDestinoPropostaProdutoEspecifico(DestinoProposta destinoProposta) {
        StringBuffer query = new StringBuffer();
        query.append("select valor from ")
            .append(ValorFaixaProgressivaProposta.class.getSimpleName()).append(" valor ")
            .append(" join fetch valor.faixaProgressivaProposta faixa")
            .append(" join fetch faixa.produtoEspecifico produto")
            .append(" where valor.destinoProposta.id = :idDestinoProposta");
        
        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), "idDestinoProposta", destinoProposta.getIdDestinoProposta());
    }

    @SuppressWarnings("unchecked")
    public List<ValorFaixaProgressivaProposta> findByDestinoProposta(DestinoProposta destinoProposta) {
        StringBuffer query = new StringBuffer();
        query.append("select valor from ")
            .append(ValorFaixaProgressivaProposta.class.getSimpleName()).append(" valor ")
            .append(" where valor.destinoProposta.id = :idDestinoProposta");
        
        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), "idDestinoProposta", destinoProposta.getIdDestinoProposta());
    }

}