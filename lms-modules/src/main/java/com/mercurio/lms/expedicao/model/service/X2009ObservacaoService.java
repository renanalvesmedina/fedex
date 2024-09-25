package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.X2009ObservacaoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.x2009ObservacaoService"
 */

public class X2009ObservacaoService extends CrudService<com.mercurio.lms.expedicao.model.X2009Observacao, Long> {
	
	
	
	public List findDescricaoByCodObservacao(YearMonthDay dtEmissaoConhecimento,String codObservacao){
		return getX2009ObservacaoDAO().findDescricaoByCodObservacao(dtEmissaoConhecimento, codObservacao);
	}
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private X2009ObservacaoDAO getX2009ObservacaoDAO() {
		return (X2009ObservacaoDAO) getDao();
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setX2009ObservacaoDAO(X2009ObservacaoDAO dao) {
		setDao( dao );
	}
	
}