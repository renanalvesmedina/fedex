package com.mercurio.lms.indenizacoes.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacaoNf;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboIndenizacaoNfDAO extends BaseCrudDao<ReciboIndenizacaoNf, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ReciboIndenizacaoNf.class;
    }

    public void removeByIdDoctoServicoIndenizacao(Long idDoctoServicoIndenizacao) {
    	String s = "delete from " + ReciboIndenizacaoNf.class.getName() + " as rinf where rinf.doctoServicoIndenizacao.id = ?";
    	getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s).setParameter(0, idDoctoServicoIndenizacao).executeUpdate();
    }
    
    public List findByIdDoctoServicoIndenizacao(Long idDoctoServicoIndenizacao) {
    	return getAdsmHibernateTemplate().find("from "+ReciboIndenizacaoNf.class.getName()+" rinf join fetch rinf.notaFiscalConhecimento where rinf.doctoServicoIndenizacao.id = ?", idDoctoServicoIndenizacao);
    }
}