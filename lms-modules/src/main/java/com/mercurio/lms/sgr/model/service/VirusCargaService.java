package com.mercurio.lms.sgr.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.model.VirusCarga;
import com.mercurio.lms.sgr.model.dao.VirusCargaDAO;

public class VirusCargaService extends CrudService<VirusCarga, Long> {

	public VirusCarga findById(Long id) {
		return (VirusCarga) super.findById(id);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 *
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(VirusCarga bean) {
		return super.store(bean);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id
	 *            indica a entidade que dever� ser removida.
	 */
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids
	 *            lista com as entidades que dever�o ser removida.
	 */
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		if (criteria.containsKey("idControleCarga")) {
			return getVirusCargaDAO().getRowCountByControleCarga(
					criteria.getLong("idControleCarga"), criteria.getBoolean("tpCancelado"));
		}
		return getVirusCargaDAO().getRowCountByFilter(criteria);
	}

	public List<VirusCarga> find(TypedFlatMap criteria) {
		if (criteria.containsKey("idControleCarga")) {
			return getVirusCargaDAO().findByControleCarga(
					criteria.getLong("idControleCarga"), criteria.getBoolean("tpCancelado"));
		}
		return getVirusCargaDAO().findByFilter(criteria);
	}

	public Integer getRowCountByControleCarga(Long idControleCarga) {
		return getVirusCargaDAO().getRowCountByControleCarga(idControleCarga, null);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia
	 *            do DAO.
	 */
	public void setVirusCargaDAO(VirusCargaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private VirusCargaDAO getVirusCargaDAO() {
		return (VirusCargaDAO) getDao();
	}

}
