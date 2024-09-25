package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DescritivoPce;
import com.mercurio.lms.vendas.model.dao.DescritivoPceDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.descritivoPceService"
 */
public class DescritivoPceService extends CrudService<DescritivoPce, Long> {


	protected DescritivoPce beforeStore(DescritivoPce bean) {
		DescritivoPce descritivoPce = (DescritivoPce)bean;
		Empresa empresa = SessionUtils.getEmpresaSessao();
		descritivoPce.setEmpresa(empresa);
		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma inst�ncia de <code>DescritivoPce</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public DescritivoPce findById(java.lang.Long id) {
		return (DescritivoPce)super.findById(id);
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

	public List findCombo(Map criteria) {
		List order = new ArrayList();
		order.add("idDescritivoPce");
		return getDescritivoPceDAO().findListByCriteria(criteria,order);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(DescritivoPce bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setDescritivoPceDAO(DescritivoPceDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private DescritivoPceDAO getDescritivoPceDAO() {
		return (DescritivoPceDAO) getDao();
	}

}
