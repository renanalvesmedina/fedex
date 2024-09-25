package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.municipios.model.IntervaloCepUF;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.dao.IntervaloCepUFDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.LongUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.intervaloCepUFService"
 */
public class IntervaloCepUFService extends CrudService<IntervaloCepUF, Long> {
	private PaisService paisService;

	public IntervaloCepUF beforeStore(IntervaloCepUF intervaloCepUF) {
		Pais pais = paisService.findById(intervaloCepUF.getUnidadeFederativa().getPais().getIdPais());
		String nrCepInicial = intervaloCepUF.getNrCepInicial();
		String nrCepFinal = intervaloCepUF.getNrCepFinal();

		if(!pais.getBlCepAlfanumerico()) {
			if(!StringUtils.isNumeric(nrCepInicial) || !StringUtils.isNumeric(nrCepFinal)) {
				throw new BusinessException("LMS-29121");
			}
			if(CompareUtils.gt(LongUtils.getLong(nrCepInicial), LongUtils.getLong(nrCepFinal))) {
				throw new BusinessException("LMS-29013");
			}
			if(!pais.getBlCepDuplicado()) {
				if(getIntervaloCepUFDAO().findIntervaloCepByUF(intervaloCepUF)) {
					throw new BusinessException("LMS-29004");
				}
			}
		}

		return super.beforeStore(intervaloCepUF);
	}

	/**
	 * Recupera uma instância de <code>Turno</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public IntervaloCepUF findById(java.lang.Long id) {
		return (IntervaloCepUF)super.findById(id);
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

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(IntervaloCepUF bean) {
		this.beforeStore(bean);
		return super.store(bean);
	}

	public ResultSetPage findPaginatedCustom(Map criteria) {
		return getIntervaloCepUFDAO().findPaginatedCustom(criteria);
	}

	public Integer getRowCountCustom(Map criteria) {
		return getIntervaloCepUFDAO().getRowCountCustom(criteria);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setIntervaloCepUFDAO(IntervaloCepUFDAO dao) {
		setDao( dao );
	}
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private final IntervaloCepUFDAO getIntervaloCepUFDAO() {
		return (IntervaloCepUFDAO) getDao();
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
}
