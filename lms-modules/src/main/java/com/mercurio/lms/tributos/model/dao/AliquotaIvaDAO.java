package com.mercurio.lms.tributos.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.AliquotaIva;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AliquotaIvaDAO extends BaseCrudDao<AliquotaIva, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AliquotaIva.class;
    }

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pais", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pais", FetchMode.JOIN);
	}

    /**
     * Finder de pcAliquota vigente, de acordo com parametros informados.<BR>
     *@author Robson Edemar Gehl
     * @param idPais
     * @param date
     * @return pcAliquota vigente
     */
    public BigDecimal findAliquotaVigente(Long idPais, YearMonthDay date){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("pais.id", idPais));
    	dc.add(
	    	Restrictions.and(
		    	Restrictions.le("dtVigenciaInicial", date),
		    	Restrictions.ge("dtVigenciaFinal", date)
		    ));
    	dc.setProjection(Projections.property("pcAliquota"));
    	List list = findByDetachedCriteria(dc);
    	if (!list.isEmpty()){
    		return (BigDecimal) list.get(0);
    	}
    	return null;
    }
	
	public boolean verificaExisteVigencia(AliquotaIva aliquotaIva) {
		StringBuffer hql = new StringBuffer();
		ArrayList values = new ArrayList();

		hql.append("select count(*) ");
		hql.append("from AliquotaIva ai ");
		hql.append("where ai.pais.id = ? ");
		values.add(aliquotaIva.getPais().getIdPais());

		if (aliquotaIva.getIdAliquotaIva() != null) {
			hql.append("and ai.id != ? ");
			values.add(aliquotaIva.getIdAliquotaIva());
		}

		if (aliquotaIva.getDtVigenciaFinal() != null) {
			values.add(aliquotaIva.getDtVigenciaInicial());
			values.add(aliquotaIva.getDtVigenciaFinal());

			hql.append("and not(ai.dtVigenciaFinal < ? or ai.dtVigenciaInicial > ?) ");
		} else {
			values.add(aliquotaIva.getDtVigenciaInicial());
			hql.append("and ai.dtVigenciaFinal >= ?  ");
		}

			
		List result = this.getAdsmHibernateTemplate().find(hql.toString(), values.toArray());
		return ((Long) result.get(0)).intValue() > 0;
	}
}