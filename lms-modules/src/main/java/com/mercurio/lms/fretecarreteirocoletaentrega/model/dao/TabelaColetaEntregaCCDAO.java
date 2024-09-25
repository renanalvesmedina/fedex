 package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntregaCC;
import com.mercurio.lms.util.JTDateTimeUtils;
 
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TabelaColetaEntregaCCDAO extends BaseCrudDao<TabelaColetaEntregaCC, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
	   return TabelaColetaEntregaCC.class;
	}

	public List<TabelaColetaEntregaCC> findByIdContoleCarga(Long idControleCarga) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("tcc");
		sql.addFrom("TabelaColetaEntregaCC tcc inner join tcc.tabelaColetaEntrega tce inner join tcc.controleCarga cc");		
		sql.addCriteria("cc.idControleCarga", "=", idControleCarga);
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	
	public List findByIdControleCarga(Long idControleCarga)  {
		StringBuffer hql = new StringBuffer();
		hql.append(" select tace ");
		hql.append(" from TabelaColetaEntregaCC tace ");
		hql.append(" inner join tace.controleCarga coca ");		
		hql.append(" where coca.idControleCarga = ? ");
		hql.append(" and tace.tabelaColetaEntrega is not null ");
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idControleCarga});
	}

	 /**
     * Remove as instâncias do pojo de acordo com os parâmetros recebidos.
     * 
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	StringBuffer sql = new StringBuffer()
	    	.append("delete from ")
	    	.append(TabelaColetaEntregaCC.class.getName()).append(" as at ")
	    	.append(" where at.controleCarga.idControleCarga = ? ");

    	List param = new ArrayList();
    	param.add(idControleCarga);

    	super.executeHql(sql.toString(), param);
    }

}