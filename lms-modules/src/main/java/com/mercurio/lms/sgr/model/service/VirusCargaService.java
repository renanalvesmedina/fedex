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
	 * contrário.
	 *
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(VirusCarga bean) {
		return super.store(bean);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
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
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setVirusCargaDAO(VirusCargaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private VirusCargaDAO getVirusCargaDAO() {
		return (VirusCargaDAO) getDao();
	}

}
