package com.mercurio.lms.municipios.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.HorarioTransito;
import com.mercurio.lms.municipios.model.dao.HorarioTransitoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.horarioTransitoService"
 */
public class HorarioTransitoService extends CrudService<HorarioTransito, Long> {

	/**
	 * Recupera uma instância de <code>HorarioTransito</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public HorarioTransito findById(java.lang.Long id) {
		return (HorarioTransito)super.findById(id);
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
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	protected HorarioTransito beforeStore(HorarioTransito bean) {
		HorarioTransito horario = (HorarioTransito) bean;
		if (getHorarioTransitoDAO().verificaConvergenciaHorario(horario.getIdHorarioTransito(), horario.getRotaIntervaloCep().getIdRotaIntervaloCep(), horario.getHrTransitoInicial(), horario.getHrTransitoFinal()))
			throw new BusinessException("LMS-29083");
		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(HorarioTransito bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setHorarioTransitoDAO(HorarioTransitoDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private HorarioTransitoDAO getHorarioTransitoDAO() {
		return (HorarioTransitoDAO) getDao();
	}

}