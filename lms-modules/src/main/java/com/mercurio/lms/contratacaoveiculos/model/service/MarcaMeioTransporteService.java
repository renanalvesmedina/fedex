package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contratacaoveiculos.model.MarcaMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.MarcaMeioTransporteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.marcaMeioTransporteService"
 */
public class MarcaMeioTransporteService extends CrudService<MarcaMeioTransporte, Long> {

	/**
	 * Recupera uma instância de <code>MarcaMeioTransporte</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MarcaMeioTransporte findById(java.lang.Long id) {
		return (MarcaMeioTransporte)super.findById(id);
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
	public java.io.Serializable store(MarcaMeioTransporte bean) {
		return super.store(bean);
	}

	/**
	 * Busca marca de meio de transporte a partir da descrição
	 * 
	 * Método utilizado pela Integração
	 * @author Felipe Ferreira
	 * 
	 * @param dsMarcaMeioTransporte
	 * @return
	 */
	public MarcaMeioTransporte findMarcaMeioTransporte(String dsMarcaMeioTransporte) {
		return getMarcaMeioTransporteDAO().findMarcaMeioTransporte(dsMarcaMeioTransporte);
	}

	//verifica se existe alguma marca com o mesmo tipo de meio de transporte. 
	protected MarcaMeioTransporte beforeStore(MarcaMeioTransporte bean) {
		MarcaMeioTransporte marcaMeioTransporte = (MarcaMeioTransporte)bean;
		if(getMarcaMeioTransporteDAO().findMarcaMeioTransporte(marcaMeioTransporte)){
			throw new BusinessException("LMS-00003");
		}
		return super.beforeStore(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMarcaMeioTransporteDAO(MarcaMeioTransporteDAO dao) {
		setDao( dao );
	}
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private final MarcaMeioTransporteDAO getMarcaMeioTransporteDAO() {
		return (MarcaMeioTransporteDAO) getDao();
	}

}