package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.RegraLiberacaoReguladora;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegraLiberacaoReguladoraDAO extends BaseCrudDao<RegraLiberacaoReguladora, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RegraLiberacaoReguladora.class;
    }

    public boolean verificaReguladoraVinculoVigente(RegraLiberacaoReguladora regraLiberacao){
    	DetachedCriteria dc = createDetachedCriteria();
    	if (regraLiberacao.getIdRegraLiberacaoReguladora() != null){
    		dc.add(Restrictions.ne("idRegraLiberacaoReguladora",regraLiberacao.getIdRegraLiberacaoReguladora()));
    	}
    	dc.add(Restrictions.eq("reguladoraSeguro.id",regraLiberacao.getReguladoraSeguro().getIdReguladora()));
    	dc.add(Restrictions.eq("tpVinculo",regraLiberacao.getTpVinculo()));
    	
    	dc=JTVigenciaUtils.getDetachedVigencia(dc,regraLiberacao.getDtVigenciaInicial(), regraLiberacao.getDtVigenciaFinal());  	
    	return findByDetachedCriteria(dc).size()>0;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("reguladoraSeguro", FetchMode.JOIN);
    	lazyFindById.put("reguladoraSeguro.pessoa", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	super.initFindPaginatedLazyProperties(lazyFindPaginated);
    	lazyFindPaginated.put("reguladoraSeguro", FetchMode.JOIN);
    	lazyFindPaginated.put("reguladoraSeguro.pessoa", FetchMode.JOIN);
    	}
    
    public List findRegraLiberacaoByTpVinculoAndVigencia(String tpVinculo, Long idReguladoraSeguro, YearMonthDay dtVigencia) {
    	DetachedCriteria dc = createDetachedCriteria()
    					   .createAlias("reguladoraSeguro","rs")
    					   .add(Restrictions.eq("tpVinculo",tpVinculo))
    					   .add(Restrictions.eq("rs.idReguladora",idReguladoraSeguro))
    					   .add(Restrictions.le("dtVigenciaInicial",dtVigencia))
    					   .add(Restrictions.or(Restrictions.ge("dtVigenciaFinal",dtVigencia),
    							 Restrictions.isNull("dtVigenciaFinal")));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

}