package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.Embalagem;
import com.mercurio.lms.expedicao.model.dao.EmbalagemDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.embalagemService"
 */
public class EmbalagemService extends CrudService<Embalagem, Long> {

	/**
	 * Recupera uma inst�ncia de <code>Embalagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Embalagem findById(java.lang.Long id) {
		return (Embalagem)super.findById(id);
	}

	public List find(Map criteria) {
		List<String> orderBy = new ArrayList<String>(1);
		orderBy.add("dsEmbalagem");
		return this.getEmbalagemDAO().findListByCriteria(criteria, orderBy);
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
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(Embalagem bean) {
		return super.store(bean);
	}

	@Override
	public Embalagem beforeStore(Embalagem bean) {
		if (bean.getNrAltura().intValue() <=0 || bean.getNrComprimento().intValue() <=0 || bean.getNrLargura().intValue() <= 0)
			throw new BusinessException("LMS-30007");
		return super.beforeStore(bean);	
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setEmbalagemDAO(EmbalagemDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private EmbalagemDAO getEmbalagemDAO() {
		return (EmbalagemDAO) getDao();
	}

}