package com.mercurio.lms.portaria.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.portaria.model.ControleEntSaidaTerceiro;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ControleEntSaidaTerceiroDAO extends BaseCrudDao<ControleEntSaidaTerceiro, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ControleEntSaidaTerceiro.class;
    }

    public List findDadosSaida(Long idControleEntSaidaTerceiro){
    	DetachedCriteria dc = createDetachedCriteria();    	
    	
    	ProjectionList pl = Projections.projectionList()
    						.add(Projections.property("tmt.dsTipoMeioTransporte"), "dsTipoMeioTransporte") 
    						.add(Projections.property("mtt.nrIdentificao"), "nrIdentificadorTransportado")
    						.add(Projections.property("nrIdentificacaoSemiReboque"), "nrIdentificadorReboque")
    						.add(Projections.property("mot.nrCpf"), "nrCpf")
    						.add(Projections.property("mot.nmMotorista"), "nmPessoa")    						
    						.add(Projections.property("mtt.idMeioTransporteTerceiro"), "idMeioTransporteTerceiro");    						
    	
    	dc.setProjection(pl);
    	dc.createAlias("meioTransporteTerceiro", "mtt");
    	dc.createAlias("mtt.modeloMeioTransporte", "mt");
    	dc.createAlias("mt.tipoMeioTransporte", "tmt");
    	dc.createAlias("motoristaTerceiro", "mot");
    	
    	
    	dc.add(Restrictions.eq("idControleEntSaidaTerceiro", idControleEntSaidaTerceiro));
    	    	
    	dc.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
    	
    	return findByDetachedCriteria(dc);
    }

    public List findMotoristaUltimaEntradaMeioTransporte(Long idMeioTransporte){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("new Map(mt.nmMotorista", "nmMotorista");
    	sql.addProjection("mt.nrRg", "nrRg");
    	sql.addProjection("mt.nrCpf", "nrCpf");
    	sql.addProjection("mt.nrCnh", "nrCnh");
    	sql.addProjection("mt.idMotoristaTerceiro", "idMotoristaTerceiro");
    	sql.addProjection("ct.nrIdentificacaoSemiReboque", "nrIdentificacaoSemiReboque)");
    	
    	sql.addFrom("ControleEntSaidaTerceiro ct inner join ct.meioTransporteTerceiro mtt inner join ct.motoristaTerceiro mt");
    	sql.addCriteria("mtt.idMeioTransporteTerceiro", "=", idMeioTransporte);
    	sql.addCustomCriteria("ct.dhEntrada.value = (select max(ct_.dhEntrada.value)" +
    											" from ControleEntSaidaTerceiro ct_" +
    											" where ct_.meioTransporteTerceiro.idMeioTransporteTerceiro = mtt.idMeioTransporteTerceiro)");
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
    }

    public boolean validateSaidaMeioTransporte(Long idMeioTransporte){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(getPersistentClass().getName(), "cet");
    	sql.addCustomCriteria("cet.dhSaida.value is null");
    	sql.addCriteria("cet.meioTransporteTerceiro.id", "=", idMeioTransporte);
    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria()).intValue() > 0;
    }
}