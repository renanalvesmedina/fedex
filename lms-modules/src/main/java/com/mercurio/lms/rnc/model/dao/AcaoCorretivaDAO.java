package com.mercurio.lms.rnc.model.dao;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.lms.rnc.model.AcaoCorretiva;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AcaoCorretivaDAO extends BaseCrudDao<AcaoCorretiva, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return AcaoCorretiva.class;
    }

    /**
     * Busca List com as A��es Corretivas relacionados com as 'Ocorrencia N�o Conformidade'
     * 
     * @param criteria ids de 'Ocorrencia N�o Conformidade'
     * @return List com os A��es Corretivas
     */
	public List findAcoesCorretivasByOcorrenciaNC(List idsOcorrenciaNC) {

		String ids = new String();
		for (Iterator iter = idsOcorrenciaNC.iterator(); iter.hasNext();) {
			ids = ids + ((Long)iter.next()).toString();
			if (iter.hasNext()) ids = ids + ", ";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select new map( ");
		sql.append("ac.id as idAcaoCorretiva, ");
		sql.append(""+PropertyVarcharI18nProjection.createProjection("ac.dsAcaoCorretiva")+" as dsAcaoCorretiva )");
		sql.append("from AcaoCorretiva ac, ");
		sql.append("CausaAcaoCorretiva cac, ");
		sql.append("CausaNaoConformidade cnc, ");
		sql.append("OcorrenciaNaoConformidade onc ");
		sql.append("where cac.acaoCorretiva = ac ");
		sql.append("and cac.causaNaoConformidade = cnc ");
		sql.append("and onc.causaNaoConformidade = cnc ");
		sql.append("and onc.id in (" + ids + ") ");
		sql.append("order by "+PropertyVarcharI18nProjection.createProjection("ac.dsAcaoCorretiva")+"");
		List list = getAdsmHibernateTemplate().find(sql.toString());
		list = new AliasToNestedBeanResultTransformer(AcaoCorretiva.class).transformListResult(list);
		return list;
	}
	
    /**
     * Busca List com as A��es Corretivas relacionados com uma 'Ocorrencia N�o Conformidade'
     * 
     * @param criteria id da 'Ocorrencia N�o Conformidade'
     * @return List com os A��es Corretivas
     */
	public List findAcoesCorretivasByIdOcorrenciaNC(Long idOcorrenciaNC) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select new map( ");
		sql.append("ac.id as idAcaoCorretiva, ");
		sql.append(""+PropertyVarcharI18nProjection.createProjection("ac.dsAcaoCorretiva")+" as dsAcaoCorretiva )");
		sql.append("from AcaoCorretiva ac, ");
		sql.append("CausaAcaoCorretiva cac, ");
		sql.append("CausaNaoConformidade cnc, ");
		sql.append("OcorrenciaNaoConformidade onc ");
		sql.append("where cac.acaoCorretiva = ac ");
		sql.append("and cac.causaNaoConformidade = cnc ");
		sql.append("and onc.causaNaoConformidade = cnc ");
		sql.append("and onc.id = " + idOcorrenciaNC + " ");
		sql.append("order by "+PropertyVarcharI18nProjection.createProjection("ac.dsAcaoCorretiva")+"");
		List list = getAdsmHibernateTemplate().find(sql.toString());
		list = new AliasToNestedBeanResultTransformer(AcaoCorretiva.class).transformListResult(list);
		return list;
	}
}