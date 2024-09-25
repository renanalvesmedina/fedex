package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.DoctoServicoSeguros;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DoctoServicoSegurosDAO extends BaseCrudDao<DoctoServicoSeguros, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DoctoServicoSeguros.class;
    }

	/**
	 * Busca DoctoServicoSeguro pelo idDoctoServico
	 * 
	 *   Jira LMS-3996
	 *   
	 * @param idDoctoServico
	 * @return {@link DoctoServicoSeguros}
	 */
    @SuppressWarnings("unchecked")
	public DoctoServicoSeguros findByIdDoctoServico(Long idDoctoServico) {
    	DoctoServicoSeguros doctoServicoSeguros = null;
   
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "dss")
    		.add(Restrictions.eq("dss.conhecimento.id", idDoctoServico));

    	List<DoctoServicoSeguros> result = findByDetachedCriteria(dc);

    	if(result.size() >= 1) {
    		doctoServicoSeguros = result.get(0);
    	}

    	return doctoServicoSeguros;
    }

}