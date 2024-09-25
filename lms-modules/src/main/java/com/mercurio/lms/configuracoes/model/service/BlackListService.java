package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.BlackList;
import com.mercurio.lms.configuracoes.model.dao.BlackListDAO;

public class BlackListService extends CrudService<BlackList, Long> {

	
	/**
	 * Recupera uma instância de <code>BlackList</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public BlackList findById(java.lang.Long id) {
		return (BlackList) super.findById(id);
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setBlackListDAO(BlackListDAO dao) {
		setDao( dao );
	}
	
	private BlackListDAO getBlackListDAO() {
		return (BlackListDAO) getDao();
	}
	
	/**
	 * Retorna os emails que estão na blackList
	 * @param listEmail
	 * @return
	 */
	public List<Object[]> findBlackListByEmails(String[] listEmail) {
		return getBlackListDAO().findBlackListByEmails(listEmail);
	}
	

	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(BlackList bean) {
		return super.store(bean);
	}
}
