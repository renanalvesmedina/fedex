package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contratacaoveiculos.model.AtributoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.AtributoMeioTransporteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.atributoMeioTransporteService"
 */
public class AtributoMeioTransporteService extends CrudService<AtributoMeioTransporte, Long> {

	//	faz a ordena��o da combo zona pela descri��o da zona.
	public List findAtributoMeioTransporte(Map criteria) {
		List listaOrder = new ArrayList();
		listaOrder.add("dsAtributoMeioTransporte:asc");
		List lista = getAtributoMeioTransporteDAO().findListByCriteria(criteria,listaOrder);
		return lista;
	}

	/**
	 * Recupera uma inst�ncia de <code>AtributoMeioTransporte</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public AtributoMeioTransporte findById(java.lang.Long id) {
		return (AtributoMeioTransporte)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(AtributoMeioTransporte bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setAtributoMeioTransporteDAO(AtributoMeioTransporteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private AtributoMeioTransporteDAO getAtributoMeioTransporteDAO() {
		return (AtributoMeioTransporteDAO) getDao();
	}

}