package com.mercurio.lms.integracao.model.dao;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.commons.lang.StringUtils.upperCase;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsmmanager.integracao.model.PontoLayoutBinder;

/**
* DAO pattern. 
*
* Esta classe fornece acesso a camada de dados da aplicação
* através do suporte ao Hibernate em conjunto com o Spring.
* Não inserir documentação após ou remover a tag do XDoclet a seguir.
* @spring.bean 
*/
public class PontoLayoutBinderDAO extends BaseCrudDao<PontoLayoutBinder, Long> {
	protected Class getPersistentClass() {
		return PontoLayoutBinder.class;
	}	

	public PontoLayoutBinder findByIdPojo(Long id) {
		return (PontoLayoutBinder) super.findById(id);
	}

	public TypedFlatMap findByIdMap(Long id) {	
		
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idPontoLayoutBinder", id);
		SqlTemplate hql = getHql(criteria);
		hql.addProjection(getHqlProjection());

		List result = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);

		if (!result.isEmpty()) return (TypedFlatMap) result.get(0);

		return null;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {	
		SqlTemplate hql = getHql(criteria);
		hql.addProjection(getHqlProjection());
		hql.addOrderBy("pontoBinder.nome");    	

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
    	List result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(rsp.getList());
    	rsp.setList(result);
		return rsp;
	}

	private String getHqlProjection() {
		StringBuilder hqlProjection = new StringBuilder()
		.append("new Map(\n")
		.append("		pontoLayoutBinder.id as id\n")
		.append("		,layoutBinder.nome as layoutBinder_nome\n")
		.append("		,layoutBinder.id as layoutBinder_id\n")
		.append("		,layoutBinder.descricao as layoutBindert_descricao\n")
		.append("		,layoutBinder.idIdentificadorPrimario as layoutBinder_idIdentificadorPrimario\n")
		.append("		,layoutBinder.idIdentificadorSecundario as layoutBinder_idIdentificadorSecundario\n")
		.append("		,layoutBinder.idIdentificadorTerciario as layoutBinder_idIdentificadorTerciario\n")
		.append("		,grupoLayoutBinder.nome as grupoLayoutBinder_nome\n")
		.append("		,grupoLayoutBinder.id as grupoLayoutBinder_id\n")
		.append("		,layoutPrincipal.nome as grupoLayoutBinder_layoutPrincipal_nome\n")
		.append("		,layoutPrincipal.id as grupoLayout_layoutPrincipal_id\n")
		.append("		,layoutPrincipal.descricao as grupoLayout_layoutPrincipal_descricao\n")
		.append("		,layoutPrincipal.idIdentificadorPrimario as grupoLayoutBinder_layoutPrincipal_idIdentificadorPrimario\n")
		.append("		,layoutPrincipal.idIdentificadorSecundario as grupoLayoutBinder_layoutPrincipal_idIdentificadorSecundario\n")
		.append("		,layoutPrincipal.idIdentificadorTerciario as grupoLayoutBinder_layoutPrincipal_idIdentificadorTerciario\n")
		.append("		,pontoBinder.id as pontoBinder_id\n")
		.append("		,pontoBinder.nome as pontoBinder_nome\n")
		.append("		,pontoBinder.tpOrigem as pontoBinder_tpOrigem\n")
		.append("       ,case when layoutBinder.nome is null or layoutBinder.nome = '' then grupoLayoutBinder.nome else layoutBinder.nome end  as layoutOrGroup\n")
		.append(")\n")
		;
		return hqlProjection.toString();
	}

	public Integer getRowCount(TypedFlatMap criteria) {	
		SqlTemplate sql = getHql(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	private SqlTemplate getHql(TypedFlatMap criteria) {
	   	SqlTemplate hql = new SqlTemplate();
	   	StringBuilder hqlFrom = new StringBuilder()
		.append(getPersistentClass().getName()).append(" as pontoLayoutBinder\n")
		.append("left join pontoLayoutBinder.layoutBinder as layoutBinder\n")
		.append("join pontoLayoutBinder.pontoBinder as pontoBinder\n")
		.append("left join pontoLayoutBinder.grupoLayoutBinder as grupoLayoutBinder\n")
		.append("left join grupoLayoutBinder.layoutPrincipal as layoutPrincipal\n")
		;
	   	hql.addFrom(hqlFrom.toString());

	   	Long idLayout = criteria.getLong("layoutBinder.id");
	   	String id1 = upperCase(criteria.getString("layoutBinder.idIdentificadorPrimario"));
   		String id2 = upperCase(criteria.getString("layoutBinder.idIdentificadorSecundario"));
   		String id3 = upperCase(criteria.getString("layoutBinder.idIdentificadorTerciario"));

	   	if(idLayout != null) {
	   		hql.addCriteria("layoutBinder.id", "=" , idLayout);
	   	} else if(isNotBlank(id1) || isNotBlank(id2) || isNotBlank(id3)) {

	   		if(isNotBlank(id1))
	   			hql.addCriteria("upper(layoutBinder.idIdentificadorPrimario)", "like" , id1);
	   		else
	   			hql.addCustomCriteria("layoutBinder.idIdentificadorPrimario is null");
	   		
	   		if(isNotBlank(id2))
	   			hql.addCriteria("upper(layoutBinder.idIdentificadorSecundario)", "like" , id2);
	   		else
	   			hql.addCustomCriteria("layoutBinder.idIdentificadorSecundario is null");

	   		if(isNotBlank(id3))
	   			hql.addCriteria("upper(layoutBinder.idIdentificadorTerciario)", "like" , id3);
	   		else
	   			hql.addCustomCriteria("layoutBinder.idIdentificadorTerciario is null");
	   		
	   	}

	   	hql.addCriteria("pontoLayoutBinder.id", "=", criteria.getLong("idPontoLayoutBinder"));
		hql.addCriteria("pontoBinder.id", "=", criteria.getLong("pontoIntegracao.id"));
		hql.addCriteria("grupoLayoutBinder.id", "=", criteria.getLong("grupoLayout.id"));

	   	return hql;
	}


	/**
	 * 
	 * @param idPontoBinder
	 * @param idLayoutBinder
	 * @return
	 */
	public PontoLayoutBinder findPontoLayoutBinder(Long idPontoBinder, Long idLayoutBinder) {
		StringBuilder hql = new StringBuilder()
		.append("select	plb ")
		.append("from ")
		.append(getPersistentClass().getName()).append(" as plb ")
		.append("inner join fetch plb.pontoBinder as pb ")
		.append("where plb.pontoBinder.id = ? ")
		.append("and plb.layoutBinder.id = ? ");
		return (PontoLayoutBinder) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idPontoBinder, idLayoutBinder});
	}
	
	/**
     * Método responsável por popular a combo de Ponto de Integracao
     * 
     * 
     */
    public List findComboPontoIntegracao(){
    	StringBuilder hql = new StringBuilder()
		.append("select	pb ")
		.append("from ")
		.append("PontoBinder as pb ")
    	.append("order by pb.nome ");
       	return getAdsmHibernateTemplate().find(hql.toString());
    }
    
    public List findAllGruposLayout(){
    	StringBuilder hql = new StringBuilder()
		.append("select	glb ")
		.append("from ")
		.append("GrupoLayoutBinder as glb ")
    	.append("order by glb.nome ");
       	return getAdsmHibernateTemplate().find(hql.toString());
       	
    }
    
    public List findAllLayout(){
    	StringBuilder hql = new StringBuilder()
		.append("select	lb ")
		.append("from ")
		.append("LayoutBinder as lb ")
    	.append("order by lb.nome ");
    	return getAdsmHibernateTemplate().find(hql.toString());
    }
}