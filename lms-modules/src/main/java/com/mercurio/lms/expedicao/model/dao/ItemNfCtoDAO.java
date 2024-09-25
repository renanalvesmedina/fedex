package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ItemNfCto;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemNfCtoDAO extends BaseCrudDao<ItemNfCto, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<ItemNfCto> getPersistentClass() {
		return ItemNfCto.class;
	}
   
	public List<Long> findIdsByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
		String sql = "select pojo.idItemNfCto " +
		"from "+ ItemNfCto.class.getName() + " as pojo " +
		"join pojo.notaFiscalConhecimento as nf " +
		"where nf.idNotaFiscalConhecimento = :idNotaFiscalConhecimento ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idNotaFiscalConhecimento", idNotaFiscalConhecimento);
	}

}