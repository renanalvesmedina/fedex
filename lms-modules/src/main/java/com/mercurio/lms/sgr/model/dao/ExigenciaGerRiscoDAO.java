package com.mercurio.lms.sgr.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.PerifericoExigenciaGerRisco;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ExigenciaGerRiscoDAO extends BaseCrudDao<ExigenciaGerRisco, Long> {

    /**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ExigenciaGerRisco.class;
    }

	protected void initFindByIdLazyProperties(Map map) {
		map.put("tipoExigenciaGerRisco", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("tipoExigenciaGerRisco", FetchMode.JOIN);
	}
    
    /**
     * Busca o valor máximo do nrNivel da coleção de exigenciaGerRisco
     * baseado no id do objeto tipoExigenciaGerRisco
     * @return O próximo nivel(Long)
     */
    public Long findNextNrNivel(Long tipoExigenciaGerRisco) {
    	StringBuffer sql = new StringBuffer()
			.append("select max(egr.nrNivel) as maxNivel from ")
			.append(ExigenciaGerRisco.class.getName()).append(" as egr ")
    		.append("where egr.tipoExigenciaGerRisco.id = ? and egr.tpSituacao = 'A' ");

    	List result = getAdsmHibernateTemplate().find(sql.toString(), new Object[] {tipoExigenciaGerRisco});
        Long nextNivel = Long.valueOf(0);
        long nivel = 1;
        Iterator iter = result.iterator();

        if (iter.hasNext()) {
            nextNivel = (Long) iter.next();
        }

        if (nextNivel!=null) {
            nivel = nextNivel.longValue() + nivel;
        }
    	return Long.valueOf(nivel);
    }

	public void deletePerifericosRastreador(ExigenciaGerRisco exigenciaGerRisco) {
		getAdsmHibernateTemplate().deleteAll(exigenciaGerRisco.getPerifericosRastreador());
		getAdsmHibernateTemplate().flush();
	}

	public void storePerifericosRastreador(ExigenciaGerRisco exigenciaGerRisco) {
		List<PerifericoExigenciaGerRisco> perifericosRastreador = exigenciaGerRisco.getPerifericosRastreador();
		for (PerifericoExigenciaGerRisco perifericoExigenciaGerRisco : perifericosRastreador) {
			perifericoExigenciaGerRisco.setExigenciaGerRisco(exigenciaGerRisco);
		}
		getAdsmHibernateTemplate().saveOrUpdateAll(perifericosRastreador);
	}

	@SuppressWarnings("unchecked")
	public List<ExigenciaGerRisco> findByTipoExigenciaGerRisco(List<Long> idsTipoExigenciaGerRisco) {
		StringBuilder hql = new StringBuilder()
				.append("FROM ExigenciaGerRisco egr ")
				.append("WHERE egr.tipoExigenciaGerRisco.idTipoExigenciaGerRisco IN (:idsTipoExigenciaGerRisco) ")
				.append("AND egr.tpSituacao = 'A' ")
				.append("ORDER BY egr.dsResumida, egr.idExigenciaGerRisco ");
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idsTipoExigenciaGerRisco", idsTipoExigenciaGerRisco);
	}

}
