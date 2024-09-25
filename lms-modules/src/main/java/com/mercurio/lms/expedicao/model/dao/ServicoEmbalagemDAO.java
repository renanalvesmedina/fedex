package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ServicoEmbalagem;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServicoEmbalagemDAO extends BaseCrudDao<ServicoEmbalagem, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class<ServicoEmbalagem> getPersistentClass() {
		return ServicoEmbalagem.class;
	}
   
	public List<Long> findIdsByIdDoctoServico(Long idDoctoServico) {
		String sql = "select pojo.idServicoEmbalagem " +
		"from "+ ServicoEmbalagem.class.getName() + " as  pojo " +
		"join pojo.doctoServico as ds " +
		"where ds.idDoctoServico = :idDoctoServico ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idDoctoServico", idDoctoServico);
	}

}