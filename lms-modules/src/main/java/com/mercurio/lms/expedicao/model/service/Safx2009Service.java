package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.Safx2009DAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.safx2009Service"
 */

public class Safx2009Service extends CrudService<com.mercurio.lms.expedicao.model.Safx2009, Long> {
	
	
	/**
	 * Retorna a descrição a partir do código de 
	 * obsercação.
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
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private Safx2009DAO getSafx2009DAO() {
		return (Safx2009DAO) getDao();
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setSafx2009DAO(Safx2009DAO dao) {
		setDao( dao );
	}
	
}