package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.dao.X2009ObservacaoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.x2009ObservacaoService"
 */

public class X2009ObservacaoService extends CrudService<com.mercurio.lms.expedicao.model.X2009Observacao, Long> {
	
	
	
	public List findDescricaoByCodObservacao(YearMonthDay dtEmissaoConhecimento,String codObservacao){
		return getX2009ObservacaoDAO().findDescricaoByCodObservacao(dtEmissaoConhecimento, codObservacao);
	}
	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private X2009ObservacaoDAO getX2009ObservacaoDAO() {
		return (X2009ObservacaoDAO) getDao();
	}
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setX2009ObservacaoDAO(X2009ObservacaoDAO dao) {
		setDao( dao );
	}
	
}