package com.mercurio.lms.tributos.model.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tributos.model.ObservacaoICMS;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ObservacaoICMSDAO extends BaseCrudDao<ObservacaoICMS, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ObservacaoICMS.class;
    } 
    
    
    public List findVigenteByTipoTributacao(Long idUfOrigem, Long idTipoTributacao, String tpObservacaoIcms, YearMonthDay dtVigencia){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("oi");
    	
    	hql.addInnerJoin(ObservacaoICMS.class.getName(), "oi");
    	hql.addInnerJoin("oi.descricaoTributacaoIcms", "dti");
    	hql.addCriteria("dti.unidadeFederativa.id", "=", idUfOrigem);
    	hql.addCriteria("dti.tipoTributacaoIcms.id", "=", idTipoTributacao);
    	hql.addCriteria("oi.tpObservacaoICMS", "=", tpObservacaoIcms);
    	JTVigenciaUtils.getHqlValidaVigencia(hql, "oi.dtVigenciaInicial", "oi.dtVigenciaFinal", dtVigencia, null);
    	
    	hql.addOrderBy("oi.nrOrdemImpressao");
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());    	
    }

    public List findListByIdDescricaoTributacao(Long id){
    	
		DetachedCriteria dc = DetachedCriteria.forClass(ObservacaoICMS.class)
		.setFetchMode("descricaoTributacaoIcms", FetchMode.JOIN)
		.add(Restrictions.eq("descricaoTributacaoIcms.id", id));	
		
		return super.findByDetachedCriteria(dc);		
    }
    

}