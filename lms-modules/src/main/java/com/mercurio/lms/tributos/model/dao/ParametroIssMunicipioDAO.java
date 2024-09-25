package com.mercurio.lms.tributos.model.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ParametroIssMunicipio;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParametroIssMunicipioDAO extends BaseCrudDao<ParametroIssMunicipio, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ParametroIssMunicipio.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("municipio",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(lazyFindById);
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("municipio",FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(lazyFindPaginated);
    }

    /**
     * Retorna a data de emiss�o da nota fiscal eletr�nica para um munic�pio em espec�fico.
     * @param idMunicipioIncidencia id do munc�pio.
     * @return data de emiss�o da nota fiscal eletr�nica.
     */
    public Date findDataEmissaoNFE(Long idMunicipioIncidencia) {
        DetachedCriteria dc = DetachedCriteria.forClass(ParametroIssMunicipio.class);

        ProjectionList projectionList = Projections.projectionList().add(Projections.property("dtEmissaoNotaFiscalEletronica"));
        
        dc.setProjection(projectionList);
        dc.add(Restrictions.eq("municipio.idMunicipio", idMunicipioIncidencia));
        
        return (Date) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

	public ParametroIssMunicipio findByMunicipio(Long idMunicipio) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select p ");
		sql.append(" from ParametroIssMunicipio p ");
		sql.append(" where p.municipio.idMunicipio = ? ");
		
		List list = getAdsmHibernateTemplate().find(sql.toString(), idMunicipio);
		
		return list == null || list.isEmpty() ? null : (ParametroIssMunicipio)list.get(0);
	}
	
}