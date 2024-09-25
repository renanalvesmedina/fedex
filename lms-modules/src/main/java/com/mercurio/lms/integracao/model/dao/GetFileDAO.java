package com.mercurio.lms.integracao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.integracao.model.GetFile;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class GetFileDAO extends BaseCrudDao {

	@Override
	protected Class getPersistentClass() {
		return GetFile.class;
	}

	public ResultSetPage findPaginated(String filial, FindDefinition findDef){

		StringBuilder from = new StringBuilder().append(GetFile.class.getName()).append(" as getfile ");
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());
		if(filial != null){
			if(!filial.equals("")){
				sql.addCriteria("getfile.filial", "=", filial);
			}
		}
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}	
	
}