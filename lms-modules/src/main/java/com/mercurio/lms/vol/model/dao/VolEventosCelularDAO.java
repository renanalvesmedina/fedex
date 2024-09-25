package com.mercurio.lms.vol.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vol.model.VolEventosCelular;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolEventosCelularDAO extends BaseCrudDao<VolEventosCelular, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolEventosCelular.class;
    }

    private SqlTemplate mountSqlTratativas(Long idMeioTransporte) {
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer sb = new StringBuffer();
		sb.append(VolEventosCelular.class.getName()).append(" as ve ");
		sb.append(" inner join ve.meioTransporte as mt " );
	
		sql.addFrom(sb.toString()); 	   
    	
		sql.addCriteria("mt.idMeioTransporte","=",idMeioTransporte);
		return sql;
	}
	
	public Integer findTotalTratativasByMeioTransporte(Long idMeioTransporte) {
        SqlTemplate sql = mountSqlTratativas(idMeioTransporte);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
	}

    public void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("conhecimento",FetchMode.JOIN);
    	lazyFindById.put("pedidoColeta",FetchMode.JOIN);
    }
    
	public List findEventoCelular(Long idDoctoServico, Long idControleCarga) {

		Map<String, Object> parametersValues = new HashMap<String, Object>();

		StringBuilder query = new StringBuilder();
		query.append("	select evc");
		query.append("	from VolEventosCelular evc");
		query.append("	where evc.conhecimento.id = :idDoctoServico");
		query.append("	and evc.controleCarga.id = :idControleCarga)");

		parametersValues.put("idDoctoServico", idDoctoServico);
		parametersValues.put("idControleCarga", idControleCarga);

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
	}

}