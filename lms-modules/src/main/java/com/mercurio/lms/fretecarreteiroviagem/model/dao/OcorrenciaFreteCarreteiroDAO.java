package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteiroviagem.model.OcorrenciaFreteCarreteiro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaFreteCarreteiroDAO extends BaseCrudDao<OcorrenciaFreteCarreteiro, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return OcorrenciaFreteCarreteiro.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("reciboFreteCarreteiro",FetchMode.JOIN);
    	lazyFindById.put("reciboFreteCarreteiro.filial",FetchMode.JOIN);
    	lazyFindById.put("reciboFreteCarreteiro.filial.pessoa",FetchMode.JOIN);
    	lazyFindById.put("reciboFreteCarreteiro.controleCarga",FetchMode.JOIN);
    	lazyFindById.put("reciboFreteCarreteiro.controleCarga.filialByIdFilialOrigem",FetchMode.JOIN);
    	lazyFindById.put("reciboFreteCarreteiro.moedaPais",FetchMode.JOIN);
    	lazyFindById.put("reciboFreteCarreteiro.moedaPais.moeda",FetchMode.JOIN);
    }

    public ResultSetPage findPaginatedCustom(TypedFlatMap criteria, FindDefinition fDef) {
    	SqlTemplate hql = this.getSqlTemplateCustom(criteria);
    	hql.addProjection("new Map(OFC.idOcorrenciaFreteCarreteiro","idOcorrenciaFreteCarreteiro");
    	hql.addProjection("F.sgFilial","reciboFreteCarreteiro_sgFilial");
    	hql.addProjection("RFC.nrReciboFreteCarreteiro","reciboFreteCarreteiro_nrReciboFreteCarreteiro");
    	hql.addProjection("OFC.dtOcorrenciaFreteCarreteiro","dtOcorrenciaFreteCarreteiro");
    	hql.addProjection("OFC.tpOcorrencia","tpOcorrencia");
    	hql.addProjection("RFC.tpSituacaoRecibo","tpSituacaoRecibo");
    	hql.addProjection("RFC.tpReciboFreteCarreteiro","tpRecibo");
    	hql.addProjection("FO.sgFilial","controleCarga_sgFilialOrigem");
    	hql.addProjection("CC.nrControleCarga","controleCarga_nrControleCarga");
    	hql.addProjection("MP.moeda","moeda");
    	hql.addProjection("RFC.vlBruto","reciboFreteCarreteiro_vlBruto");
    	hql.addProjection("RFC.vlLiquido","reciboFreteCarreteiro_vlLiquido");
    	hql.addProjection("OFC.vlDesconto","vlDesconto");
    	hql.addProjection("OFC.blDescontoCancelado","blDescontoCancelado");
    	hql.addProjection("RFC.reciboComplementado.id","idReciboComplementado)");

    	hql.addOrderBy("RFC.nrReciboFreteCarreteiro");
    	return getAdsmHibernateTemplate().findPaginated(hql.getSql(),fDef.getCurrentPage(),fDef.getPageSize(),hql.getCriteria());
    }
    
    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	SqlTemplate hql = this.getSqlTemplateCustom(criteria);
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
    }
    
    public SqlTemplate getSqlTemplateCustom(TypedFlatMap criteria) {
    	StringBuilder hqlFrom = new StringBuilder()
    		.append(OcorrenciaFreteCarreteiro.class.getName()).append(" as OFC ")
    		.append(" inner join OFC.reciboFreteCarreteiro as RFC ")
    		.append(" inner join RFC.filial as F ")
    		.append("  left join RFC.controleCarga as CC ")
    		.append("  left join CC.filialByIdFilialOrigem as FO ")
    		.append(" inner join RFC.moedaPais as MP ");
 
    	SqlTemplate hql = new SqlTemplate();
    	hql.addFrom(hqlFrom.toString());

    	hql.addCriteria("F.id","=",criteria.getLong("reciboFreteCarreteiro.filial.idFilial"));
    	hql.addCriteria("RFC.id","=",criteria.getLong("reciboFreteCarreteiro.idReciboFreteCarreteiro"));
    	hql.addCriteria("RFC.tpReciboFreteCarreteiro","=",criteria.getString("tpReciboFreteCarreteiro"));
    	hql.addCriteria("CC.id","=",criteria.getLong("reciboFreteCarreteiro.controleCarga.idControleCarga"));
    	hql.addCriteria("OFC.dtOcorrenciaFreteCarreteiro",">=",criteria.getYearMonthDay("dtOcorrenciaFreteCarreteiroInicial"));
    	hql.addCriteria("OFC.dtOcorrenciaFreteCarreteiro","<=",criteria.getYearMonthDay("dtOcorrenciaFreteCarreteiroFinal"));
    	hql.addCriteria("OFC.tpOcorrencia","=",criteria.getString("tpOcorrencia"));

    	return hql;
    }
}