package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.BoletoAnexo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class BoletoAnexoDAO extends BaseCrudDao<BoletoAnexo, Long>
{
	
	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("usuario", FetchMode.JOIN);
		super.initFindListLazyProperties(lazyFindList);
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("usuario", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return BoletoAnexo.class;
    }

	public List<BoletoAnexo> findBoletoAnexosByIdBoleto(Long idBoleto) {
		SqlTemplate hql = getSqlTemplateFilterDesconto(idBoleto);
   		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	private SqlTemplate getSqlTemplateFilterDesconto(Long idBoleto) {
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "boletoAnexo");
   		hql.addInnerJoin("fetch boletoAnexo.usuario","usuario");
   		
   		//TODO
   		
   		hql.addCriteria("boletoAnexo.boleto.id", "=", idBoleto);
		return hql;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {

		TypedFlatMap map = new TypedFlatMap();
		map.putAll(criteria);
		
		SqlTemplate sql = getSqlTemplateFilterDesconto(map.getLong("idBoleto"));

		
		return getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}

	public Map findBoletoAnexoInfo(Long idBoletoAnexo) {
	   	StringBuilder sql = new StringBuilder()
		.append("select new map(da.dcArquivo as dcArquivo, ")
		.append("da.blEnvAnexoQuestFat as blEnvAnexoQuestFat, ")
		.append("da.dsAnexo as dsAnexo ")
		.append(") ")
		.append("from ")
		.append(getPersistentClass().getName()).append(" as da ")
		.append("where ")
		.append("da.id = ? ");
	
		Object obj[] = new Object[1];
		obj[0] = idBoletoAnexo;
	
		List lista = getAdsmHibernateTemplate().find(sql.toString(), obj);
		return (Map)lista.get(0);
	}

	
}