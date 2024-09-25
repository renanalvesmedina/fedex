package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.AwbEmbalagem;
import com.mercurio.lms.expedicao.model.dao.AwbEmbalagemDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.awbEmbalagemService"
 */
public class AwbEmbalagemService extends CrudService<AwbEmbalagem, Long> {

	/**
	 * Recupera uma inst�ncia de <code>AwbEmbalagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public AwbEmbalagem findById(java.lang.Long id) {
		return (AwbEmbalagem)super.findById(id);
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
	public java.io.Serializable store(AwbEmbalagem bean) {
		return super.store(bean);
	}

	/**
	 * Remove todas embalagens relacionadas ao AWB.
	 * 
	 * @param idAwb id do AWB que as embalagens ser�o removidas.
	 */
	public void removeByIdAwb(Long idAwb) {
		getAwbEmbalagemDAO().removeByIdAwb(idAwb);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setAwbEmbalagemDAO(AwbEmbalagemDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private AwbEmbalagemDAO getAwbEmbalagemDAO() {
		return (AwbEmbalagemDAO) getDao();
	}
}