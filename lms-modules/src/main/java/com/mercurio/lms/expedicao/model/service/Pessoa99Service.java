package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.Pessoa99;
import com.mercurio.lms.expedicao.model.dao.Pessoa99DAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.expedicao.pessoa99Service"
 */
public class Pessoa99Service extends CrudService<Pessoa99, Long> {

	public Pessoa99 findByPessoa(Long idPessoa) {
		return getPessoa99DAO().findByPessoa(idPessoa);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Pessoa99 pessoa99) {
		return super.store(pessoa99);
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setPessoa99DAO(Pessoa99DAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private Pessoa99DAO getPessoa99DAO() {
		return (Pessoa99DAO) getDao();
	}
}
