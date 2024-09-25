package com.mercurio.lms.contasreceber.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.Type;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DescontoAnexo;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DescontoAnexoDAO extends BaseCrudDao<DescontoAnexo, Long>
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
    	
        return DescontoAnexo.class;
    }
    
    public Map findDescontoAnexoInfo(Long idDescontoAnexo){
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
		obj[0] = idDescontoAnexo;
	
		List lista = getAdsmHibernateTemplate().find(sql.toString(), obj);
		return (Map)lista.get(0);
    }

	public List<DescontoAnexo> findDescontoAnexosByIdDesconto(Long idDesconto) {
		SqlTemplate hql = getSqlTemplateFilterDesconto(idDesconto);
   		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	private SqlTemplate getSqlTemplateFilterDesconto(Long idDesconto) {
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "descontoAnexo");
   		hql.addInnerJoin("fetch descontoAnexo.usuario","usuario");
   		
   		hql.addCriteria("descontoAnexo.desconto.id", "=", idDesconto);
		return hql;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {

		TypedFlatMap map = new TypedFlatMap();
		map.putAll(criteria);
		
		SqlTemplate sql = getSqlTemplateFilterDesconto(map.getLong("idDesconto"));

		
		return getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}

	public void removeDescontoAnexoByIdFatura(Long idFatura) {
		StringBuilder sb = new StringBuilder()

    	.append(" DELETE FROM " + DescontoAnexo.class.getName() + " AS da \n")
		.append(" WHERE		da.desconto.id IN ( \n") 
		.append("		SELECT 		des.id \n")
		.append("		FROM 		" + ItemFatura.class.getName() + " AS ifat, \n")
		.append("					" + Fatura.class.getName() + " AS fat, \n")
		.append("		 			" + DevedorDocServFat.class.getName() + " AS dev, \n")
		.append("		 			" + Desconto.class.getName() + " AS des \n")
		.append("		WHERE      	fat.id = ? \n")
		.append("		AND 	   	ifat.fatura.id = fat.id \n")
		.append("		AND 	   	ifat.devedorDocServFat.id = dev.id \n")
		.append("		AND 	   	des.devedorDocServFat.id = dev.id \n")
		.append("		AND 	   	des.idPendencia is null \n")
		.append(" ) \n");
    	
    	Object[] values = new Object[]{idFatura};
    	Type[] types = new Type[]{Hibernate.LONG};
    	
    	executeUpdate(sb.toString(), values, types);
	}
	
	public void executeUpdate(final String hql, final Object[] values, final Type[] types){
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setParameters(values, types);
				if (StringUtils.isNotBlank(hql)){
					query.executeUpdate();
				}
				
				return null;
			}
		});
	}	
}