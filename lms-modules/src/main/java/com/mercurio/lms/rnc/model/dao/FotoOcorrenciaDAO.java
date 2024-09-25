package com.mercurio.lms.rnc.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.rnc.model.FotoOcorrencia;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FotoOcorrenciaDAO extends BaseCrudDao<FotoOcorrencia, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return FotoOcorrencia.class;
    }

    protected void initFindByIdLazyProperties(Map map) {
    	map.put("foto", FetchMode.JOIN);
    }
    
    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("foto", FetchMode.JOIN);
    }

	public List findByIdDoctoTpBO(Long idDoctoServico) {
		StringBuffer sql = new StringBuffer()
    	.append("from " + FotoOcorrencia.class.getName() + " fo ")
    	.append("join fetch fo.ocorrenciaNaoConformidade onc ")
    	.append("join fetch onc.naoConformidade nc ")
    	.append("join fetch nc.doctoServico ds ")
    	.append("where ds.idDoctoServico = ? ")
		.append("and fo.tpAnexo = 'B' ");

    	List param = new ArrayList();
		param.add(idDoctoServico);
    	return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
}