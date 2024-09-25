package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.Pessoa99;
import com.mercurio.lms.expedicao.model.dao.Pessoa99DAO;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.expedicao.pessoa99Service"
 */
public class Pessoa99Service extends CrudService<Pessoa99, Long> {

	public Pessoa99 findByPessoa(Long idPessoa) {
		return getPessoa99DAO().findByPessoa(idPessoa);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Pessoa99 pessoa99) {
		return super.store(pessoa99);
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPessoa99DAO(Pessoa99DAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private Pessoa99DAO getPessoa99DAO() {
		return (Pessoa99DAO) getDao();
	}
}
