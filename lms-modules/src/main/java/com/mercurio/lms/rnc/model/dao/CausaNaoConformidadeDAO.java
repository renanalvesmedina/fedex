package com.mercurio.lms.rnc.model.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.lms.rnc.model.CausaNaoConformidade;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CausaNaoConformidadeDAO extends BaseCrudDao<CausaNaoConformidade, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return CausaNaoConformidade.class;
    }
    
    /**
     * Busca List com os 'Causas de N�o Conformidade' relacionados com as 'Ocorrencia N�o Conformidade'
     * 
     * @param criteria List de ids de 'Ocorrencia N�o Conformidade'
     * @return List com os Motivos Disposi��o
     */
	public List findCausasNaoConformidadeByOcorrenciaNC(List idsOcorrenciaNC) {
		StringBuffer sql = new StringBuffer();
		sql.append("select new map( ");
		sql.append("cnc.id as idCausaNaoConformidade, ");
		sql.append(""+PropertyVarcharI18nProjection.createProjection("cnc.dsCausaNaoConformidade")+" as dsCausaNaoConformidade )");
		sql.append("from CausaNaoConformidade cnc, ");
		sql.append("OcorrenciaNaoConformidade onc ");
		sql.append("where onc.causaNaoConformidade = cnc ");
		sql.append("and onc.id in (:idsOcorrenciaNC)");
		sql.append("order by "+PropertyVarcharI18nProjection.createProjection("cnc.dsCausaNaoConformidade")+"");
    	
		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sql.toString());
 	    q.setParameterList("idsOcorrenciaNC", idsOcorrenciaNC);
 	    return new AliasToNestedBeanResultTransformer(CausaNaoConformidade.class).transformListResult(q.list());
	}

	/**
	 * Solicita��o CQPRO00005285 da integra��o.
	 * M�todo que retorna uma instancia da classe CausaNaoConformidadeconforme de acordo com a descri��o passada por par�metro.
	 * @param dsCausaNaoConformidade
	 * @return
	 */
	public CausaNaoConformidade findByDsCausaNaoConformidade(String dsCausaNaoConformidade){
		DetachedCriteria dc = DetachedCriteria.forClass(CausaNaoConformidade.class, "cnc");
		dc.add(Restrictions.eq("cnc.dsCausaNaoConformidade", dsCausaNaoConformidade).ignoreCase());
		List result = findByDetachedCriteria(dc);
		if (result.isEmpty()){
			return null;
		}
		return (CausaNaoConformidade)result.get(0);
	}
	
}