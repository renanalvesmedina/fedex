package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteiroviagem.model.ValorCombustivel;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ValorCombustivelDAO extends BaseCrudDao<ValorCombustivel, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ValorCombustivel.class;
    }

    public ResultSetPage findPaginated(TypedFlatMap typedFlatMap,FindDefinition findDefinition) {
    	ProjectionList pl = Projections.projectionList()
    					 .add(Projections.alias(Projections.property("P.nmPais"),"moedaPais_pais_nmPais"))
    					 .add(Projections.alias(Projections.property("MP.idMoedaPais"),"moedaPais_idMoedaPais"))
    					 .add(Projections.alias(Projections.property("MO.dsSimbolo"),"moedaPais_moeda_dsSimbolo"))
    					 .add(Projections.alias(Projections.property("MO.sgMoeda"),"moedaPais_moeda_sgMoeda"))
    					 .add(Projections.alias(Projections.property("TC.dsTipoCombustivel"),"tipoCombustivel_dsTipoCombustivel"))
    					 .add(Projections.alias(Projections.property("TC.idTipoCombustivel"),"tipoCombustivel_idTipoCombustivel"))
    					 .add(Projections.alias(Projections.property("idValorCombustivel"),"idValorCombustivel"))
    					 .add(Projections.alias(Projections.property("dtVigenciaInicial"),"dtVigenciaInicial"))
    					 .add(Projections.alias(Projections.property("dtVigenciaFinal"),"dtVigenciaFinal"))
    					 .add(Projections.alias(Projections.property("vlValorCombustivel"),"vlValorCombustivel"));
    	
    	DetachedCriteria dc = createDetachedCriteriaDefault(typedFlatMap)
    					   .setProjection(pl)
    					   .setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
    	return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc,findDefinition.getCurrentPage(),findDefinition.getPageSize());
    }
    public Integer getRowCount(TypedFlatMap typedFlatMap) {
    	DetachedCriteria dc = createDetachedCriteriaDefault(typedFlatMap)
		   .setProjection(Projections.rowCount());
    	return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
    }
    
    public List findById(TypedFlatMap typedFlatMap) {
    	ProjectionList pl = Projections.projectionList()
						 .add(Projections.alias(Projections.property("P.nmPais"),"moedaPais_pais_nmPais"))
						 .add(Projections.alias(Projections.property("P.idPais"),"moedaPais_pais_idPais"))
						 .add(Projections.alias(Projections.property("MP.idMoedaPais"),"moedaPais_idMoedaPais"))
						 .add(Projections.alias(Projections.property("TC.idTipoCombustivel"),"tipoCombustivel_idTipoCombustivel"))
						 .add(Projections.alias(Projections.property("idValorCombustivel"),"idValorCombustivel"))
						 .add(Projections.alias(Projections.property("dtVigenciaInicial"),"dtVigenciaInicial"))
						 .add(Projections.alias(Projections.property("dtVigenciaFinal"),"dtVigenciaFinal"))
						 .add(Projections.alias(Projections.property("vlValorCombustivel"),"vlValorCombustivel"));

		DetachedCriteria dc = createDetachedCriteriaDefault(typedFlatMap)
				   .setProjection(pl)
				   .setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    public List findValorCombustivelByDtVigenciaTpCombustivelMoeda(Long idMoedaPais,Long tpCombustivel,YearMonthDay dtVigenciaInicial) {
    	DetachedCriteria dc = createDetachedCriteria()
    				       .setProjection(Projections.property("vlValorCombustivel"))
    				       .createAlias("moedaPais","MP")
    				       .createAlias("MP.pais","P")
    				       .createAlias("tipoCombustivel","TC")
    				       .add(Restrictions.eq("TC.idTipoCombustivel",tpCombustivel))
    				       .add(Restrictions.eq("MP.idMoedaPais",idMoedaPais))
    				       .add(Restrictions.lt("dtVigenciaInicial",dtVigenciaInicial))
    				       .addOrder(Order.desc("dtVigenciaInicial"));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    public List findBeforeStoreVigencia(ValorCombustivel bean) {
    	DetachedCriteria dc = createDetachedCriteria()
    					   .setProjection(Projections.property("idValorCombustivel"))
    					   .createAlias("tipoCombustivel","TC")
    					   .createAlias("moedaPais","MP")
    					   .createAlias("MP.pais","P")
    					   .add(Restrictions.eq("TC.idTipoCombustivel",bean.getTipoCombustivel().getIdTipoCombustivel()))
    					   .add(Restrictions.eq("P.idPais",bean.getMoedaPais().getPais().getIdPais()));
    	if (bean.getIdValorCombustivel() != null)
    		dc.add(Restrictions.ne("idValorCombustivel",bean.getIdValorCombustivel()));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(JTVigenciaUtils.getDetachedVigencia(dc,bean.getDtVigenciaInicial(),bean.getDtVigenciaFinal()));    					
    }
    private DetachedCriteria createDetachedCriteriaDefault(TypedFlatMap typedFlatMap) {
    	DetachedCriteria dc = DetachedCriteria.forClass(ValorCombustivel.class,"VC")
		   .createAlias("VC.moedaPais","MP")
		   .createAlias("MP.pais","P")
		   .createAlias("MP.moeda","MO")
		   .createAlias("VC.tipoCombustivel","TC")
		   
		   .addOrder(OrderVarcharI18n.asc("P.nmPais",LocaleContextHolder.getLocale()))
		   .addOrder(OrderVarcharI18n.asc("TC.dsTipoCombustivel",LocaleContextHolder.getLocale()))
		   .addOrder(Order.asc("VC.dtVigenciaInicial"));
		   
    	
    	if (typedFlatMap.getLong("moedaPais.pais.idPais") != null)
			dc.add(Restrictions.eq("P.idPais",typedFlatMap.getLong("moedaPais.pais.idPais")));
    	if (typedFlatMap.getLong("tipoCombustivel.idTipoCombustivel") != null)
			dc.add(Restrictions.eq("TC.idTipoCombustivel",typedFlatMap.getLong("tipoCombustivel.idTipoCombustivel")));
    	if (typedFlatMap.getLong("idValorCombustivel") != null)
			dc.add(Restrictions.eq("VC.idValorCombustivel",typedFlatMap.getLong("idValorCombustivel")));
    	if (typedFlatMap.getYearMonthDay("dtVigenciaInicial") != null)	
    		dc.add(Restrictions.ge("VC.dtVigenciaInicial",typedFlatMap.getYearMonthDay("dtVigenciaInicial")));
    	if (typedFlatMap.getYearMonthDay("dtVigenciaFinal") != null)	
    		dc.add(Restrictions.le("VC.dtVigenciaFinal",typedFlatMap.getYearMonthDay("dtVigenciaFinal")));
    	
    	return dc;
    }


}