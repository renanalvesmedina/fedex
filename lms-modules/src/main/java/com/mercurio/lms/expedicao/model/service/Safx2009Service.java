package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.Safx2009DAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.safx2009Service"
 */

public class Safx2009Service extends CrudService<com.mercurio.lms.expedicao.model.Safx2009, Long> {
	
	
	/**
	 * Retorna a descri��o a partir do c�digo de 
	 * obserca��o.
	 * 
	 * @param codObservacao
	 * @return List
	 * @author DouglasSM
	 * @return 
	 * @since 01/02/2010
	 */
	public  java.util.List findDescricaoByCodObservacao(String codObservacao){
		return getSafx2009DAO().findDescricaoByCodObservacao(codObservacao);
	}
	
	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private Safx2009DAO getSafx2009DAO() {
		return (Safx2009DAO) getDao();
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setSafx2009DAO(Safx2009DAO dao) {
		setDao( dao );
	}
	
}