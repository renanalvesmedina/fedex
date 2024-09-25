package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.BlackList;
import com.mercurio.lms.configuracoes.model.dao.BlackListDAO;

public class BlackListService extends CrudService<BlackList, Long> {

	
	/**
	 * Recupera uma inst�ncia de <code>BlackList</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	@Override
	public BlackList findById(java.lang.Long id) {
		return (BlackList) super.findById(id);
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setBlackListDAO(BlackListDAO dao) {
		setDao( dao );
	}
	
	private BlackListDAO getBlackListDAO() {
		return (BlackListDAO) getDao();
	}
	
	/**
	 * Retorna os emails que est�o na blackList
	 * @param listEmail
	 * @return
	 */
	public List<Object[]> findBlackListByEmails(String[] listEmail) {
		return getBlackListDAO().findBlackListByEmails(listEmail);
	}
	

	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(BlackList bean) {
		return super.store(bean);
	}
}
