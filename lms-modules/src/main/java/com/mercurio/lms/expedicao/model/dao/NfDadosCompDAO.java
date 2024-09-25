package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.NfDadosComp;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NfDadosCompDAO extends BaseCrudDao<NfDadosComp, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NfDadosComp.class;
    }

   

	public List findIdsByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento)
	{
		String sql = "select pojo.idNfDadosComp " +
		"from "+ NfDadosComp.class.getName() + " as pojo " +
		"join pojo.notaFiscalConhecimento as nf " +
		"where nf.idNotaFiscalConhecimento = :idNotaFiscalConhecimento ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idNotaFiscalConhecimento", idNotaFiscalConhecimento);
	}



	public List findByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
		DetachedCriteria dc = DetachedCriteria.forClass(NfDadosComp.class, "nfdc")
			.add(Restrictions.eq("nfdc.notaFiscalConhecimento.id", idNotaFiscalConhecimento));
		return findByDetachedCriteria(dc);
	}

	public List findByIdDadosComplemento(Long idDadosComplemento) {
		DetachedCriteria dc = DetachedCriteria.forClass(NfDadosComp.class, "nfdc")
			.add(Restrictions.eq("nfdc.dadosComplemento.id", idDadosComplemento));
		return findByDetachedCriteria(dc);
	}
	
}