package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.AgrupamentoCliente;
import com.mercurio.lms.vendas.model.dao.AgrupamentoClienteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.agrupamentoClienteService"
 */
public class AgrupamentoClienteService extends CrudService<AgrupamentoCliente, Long> {

	/**
	 * Recupera uma instância de <code>AgrupamentoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public AgrupamentoCliente findById(java.lang.Long id) {
		return (AgrupamentoCliente)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(AgrupamentoCliente bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setAgrupamentoClienteDAO(AgrupamentoClienteDAO dao) {
		setDao( dao );
	}

	
	/**
	 * Retorna o numero de agrupamentos dentro da tabela AGRUPAMENTO_CLIENTE a partir da divisão do cliente.
	 * @param idDivisao
	 * @return Numero de Agrupamentos
	 */	
	public Integer findByDivisaoClienteId(Long idDivisao) {
		return getAgrupamentoClienteDAO().findByDivisaoClienteId(idDivisao); 
	}
	
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private AgrupamentoClienteDAO getAgrupamentoClienteDAO() {
		return (AgrupamentoClienteDAO) getDao();
	}
}