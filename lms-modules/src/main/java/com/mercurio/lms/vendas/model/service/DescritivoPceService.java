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
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
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
	 * Recupera uma instância de <code>DescritivoPce</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public DescritivoPce findById(java.lang.Long id) {
		return (DescritivoPce)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(DescritivoPce bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setDescritivoPceDAO(DescritivoPceDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private DescritivoPceDAO getDescritivoPceDAO() {
		return (DescritivoPceDAO) getDao();
	}

}
