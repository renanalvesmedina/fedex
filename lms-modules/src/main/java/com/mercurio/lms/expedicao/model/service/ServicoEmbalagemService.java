package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.ServicoEmbalagem;
import com.mercurio.lms.expedicao.model.dao.ServicoEmbalagemDAO;
import com.mercurio.lms.sim.model.dao.LMComplementoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.servicoEmbalagemService"
 */
public class ServicoEmbalagemService extends CrudService<ServicoEmbalagem, Long> {

	private LMComplementoDAO lmComplementoDao;
	public void setLmComplementoDao(LMComplementoDAO lmComplementoDao) {
		this.lmComplementoDao = lmComplementoDao;
	}

	/**
	 * Recupera uma inst�ncia de <code>ServicoEmbalagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ServicoEmbalagem findById(java.lang.Long id) {
		return (ServicoEmbalagem)super.findById(id);
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
	public java.io.Serializable store(ServicoEmbalagem bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setServicoEmbalagemDAO(ServicoEmbalagemDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ServicoEmbalagemDAO getServicoEmbalagemDAO() {
		return (ServicoEmbalagemDAO) getDao();
	}

	public List findIdsByIdDoctoServico(Long idDoctoServico) {
		return getServicoEmbalagemDAO().findIdsByIdDoctoServico(idDoctoServico);
	}

	public List findPaginatedComplEmbalagens(Long idDoctoServico){
		return lmComplementoDao.findPaginatedComplEmbalagens(idDoctoServico);
	}

	public boolean findEmbalagensAba(Long idDoctoServico){
		return lmComplementoDao.findEmbalagensAba(idDoctoServico);
	}

}