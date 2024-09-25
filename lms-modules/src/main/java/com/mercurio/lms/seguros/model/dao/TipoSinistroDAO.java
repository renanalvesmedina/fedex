package com.mercurio.lms.seguros.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.TipoSinistro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoSinistroDAO extends BaseCrudDao<TipoSinistro, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
    	return TipoSinistro.class;
    }

	public List findTipoProcessoSinistroByTipoSeguro(TypedFlatMap tfm) {
		StringBuilder hql = new StringBuilder();
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idTipoSeguro", tfm.getLong("idTipoSeguro"));
		
		hql.append("SELECT ts");
		hql.append(" FROM " + TipoSinistro.class.getName() + " as ts");
		hql.append(" LEFT JOIN ts.seguroTipoSinistros as sts");
		hql.append(" WHERE sts.tipoSeguro.idTipoSeguro = :idTipoSeguro");
		if(tfm.getString("tpSituacao") != null) {
			hql.append(" AND ts.tpSituacao = :tpSituacao");
			parameters.put("tpSituacao", tfm.getDomainValue("tpSituacao"));
		}
		hql.append(" ORDER BY ts.dsTipo ASC");
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parameters);
	}
}