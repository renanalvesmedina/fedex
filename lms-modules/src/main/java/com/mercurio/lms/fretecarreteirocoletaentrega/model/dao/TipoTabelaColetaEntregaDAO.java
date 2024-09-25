package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega;
import com.mercurio.lms.vendas.model.Cotacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoTabelaColetaEntregaDAO extends BaseCrudDao<TipoTabelaColetaEntrega, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoTabelaColetaEntrega.class;
    }

   
    public Integer getRowCount(Map criteria) {
    	// TODO Auto-generated method stub
    	return super.getRowCount(criteria);
    }
    public boolean validateExistBeforeStore(Long idTipoTabelaColetaEntrega) {
    	DetachedCriteria dc = createDetachedCriteria().setProjection(Projections.rowCount())
    			.add(Restrictions.eq("blNormal",Boolean.TRUE));
    	if (idTipoTabelaColetaEntrega != null)
    		dc.add(Restrictions.ne("idTipoTabelaColetaEntrega",idTipoTabelaColetaEntrega));
    	return ((Integer)dc.getExecutableCriteria(getSession()).uniqueResult()).compareTo(Integer.valueOf(0)) > 0;
    }

    public TipoTabelaColetaEntrega findTipoTabelaColetaEntregaByBlNormal(boolean blNormal){
		DetachedCriteria dc = DetachedCriteria.forClass(TipoTabelaColetaEntrega.class)
		.add(Restrictions.eq("blNormal", blNormal));
		return (TipoTabelaColetaEntrega) this.getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}