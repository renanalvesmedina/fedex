package com.mercurio.lms.carregamento.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.Transponder;
import com.mercurio.lms.carregamento.model.Transponder.SITUACAO_TRANSPONDER;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TransponderDAO extends BaseCrudDao<Transponder, Long>{

	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("filial", FetchMode.JOIN);
		lazyFindList.put("filial.pessoa", FetchMode.JOIN);
		lazyFindList.put("controleCarga", FetchMode.JOIN);
		lazyFindList.put("controleCarga.filialByIdFilialOrigem", FetchMode.JOIN);
		super.initFindListLazyProperties(lazyFindList);
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
		lazyFindById.put("controleCarga", FetchMode.JOIN);
		lazyFindById.put("controleCarga.filialByIdFilialOrigem", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	
	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filial", FetchMode.JOIN);
		lazyFindLookup.put("filial.pessoa", FetchMode.JOIN);
		lazyFindLookup.put("controleCarga", FetchMode.JOIN);
		lazyFindLookup.put("controleCarga.filialByIdFilialOrigem", FetchMode.JOIN);
		super.initFindLookupLazyProperties(lazyFindLookup);
	}
	
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("filial", FetchMode.JOIN);
		lazyFindPaginated.put("filial.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("controleCarga", FetchMode.JOIN);
		lazyFindPaginated.put("controleCarga.filialByIdFilialOrigem", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}
	
	@Override
	protected Class getPersistentClass() {
		return Transponder.class;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		return super.findPaginated(criteria, findDef);
	}

	public Transponder findByNrTransponder(Long nrTransponder, boolean apenasAtivos, Long ... excludeIds) {
		SqlTemplate hql = new SqlTemplate();
   		hql.addFrom(getPersistentClass().getName() , "transponder");
   		hql.addCriteria("transponder.nrTransponder", "=", nrTransponder);
   		for (Long excludeId : excludeIds) {
   			hql.addCriteria("transponder.idTransponder", "!=", excludeId);
		}
   		if(apenasAtivos){
   			hql.addCriteria("tpSituacaoTransponder", "!=", SITUACAO_TRANSPONDER.INUTILIZADO.getValorDominio());
   		}

   		return (Transponder)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	public List<Transponder> findTranspondersControleCarga(Long idControleCarga) {
		SqlTemplate hql = new SqlTemplate();
   		hql.addFrom(getPersistentClass().getName() , "transponder");
   		hql.addCriteria("transponder.controleCarga.id", "=", idControleCarga);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()) ;
	}

}
