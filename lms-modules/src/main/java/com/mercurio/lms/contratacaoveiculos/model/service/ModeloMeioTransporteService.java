package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.ModeloMeioTransporteDAO;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.modeloMeioTransporteService"
 */
public class ModeloMeioTransporteService extends CrudService<ModeloMeioTransporte, Long> {
	MarcaMeioTransporteService marcaMeioTransporteService ;
	TipoMeioTransporteService tipoMeioTransporteService;

	//popula combo de marcas
	public List findMarcaMeioTranspByMeio(Map criteria) {
		List listaIntegral = marcaMeioTransporteService.find(criteria);
		List listaCriterio = new ArrayList();
		listaCriterio.add("idMarcaMeioTransporte");
		listaCriterio.add("dsMarcaMeioTransporte");
		listaCriterio.add("tpSituacao");
		List listaNova = (List)ReflectionUtils.copyAndFilterNestedBean(listaIntegral,listaCriterio);
		return listaNova;
	}

	//popula combo de tipos de meio de transporte
	public List findTipoMeioTranspByMeio(Map criteria) {
		List listEmpty = new ArrayList();
		if(criteria.get("tpMeioTransporte") == null || criteria.get("tpMeioTransporte").equals("")){
			return listEmpty;
		}
		List listaIntegralTMT = tipoMeioTransporteService.find(criteria);
		List listaCriterioTMT = new ArrayList();
		listaCriterioTMT.add("idTipoMeioTransporte");
		listaCriterioTMT.add("dsTipoMeioTransporte");
		listaCriterioTMT.add("tpMeioTransporte");
		listaCriterioTMT.add("tpSituacao");
		List listaNovaTMT = (List)ReflectionUtils.copyAndFilterNestedBean(listaIntegralTMT,listaCriterioTMT);
		return listaNovaTMT;
	}

	/**
	 * Recupera uma instância de <code>ModeloMeioTransporte</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public ModeloMeioTransporte findById(java.lang.Long id) {
		return (ModeloMeioTransporte)super.findById(id);
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
	// FIXME corrigir para retornar o ID
	public ModeloMeioTransporte store(ModeloMeioTransporte bean) {
		super.store(bean);
		return bean;
	}

	/**
	 * Busca modelo de meio de transporte a partir da descrição.
	 * 
	 * Método utilizado pela Integração
	 * @author Felipe Ferreira
	 * 
	 * @param dsModeloMeioTransporte
	 * @return
	 */
	public ModeloMeioTransporte findModeloMeioTransporte(String dsModeloMeioTransporte) {
		return getModeloMeioTransporteDAO().findModeloMeioTransporte(dsModeloMeioTransporte);
	}

	/**
	 * Busca modelos de meio de transporte a partir de
	 * tipo de meio de transporte e marca de meio de transporte.
	 * 
	 * Método utilizado pela Integração
	 * @author Felipe Ferreira
	 * 
	 * @param idTipoMeioTransporte identificador do tipo
	 * @param idMarcaMeioTransporte identificador da marca
	 * @return Lista de modelos de meio de transporte
	 */
	public List<ModeloMeioTransporte> findModelosMeioTransporte(Long idTipoMeioTransporte, Long idMarcaMeioTransporte) {
		return getModeloMeioTransporteDAO().findModelosMeioTransporte(idTipoMeioTransporte,idMarcaMeioTransporte);
	}

	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}

	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setModeloMeioTransporteDAO(ModeloMeioTransporteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private final ModeloMeioTransporteDAO getModeloMeioTransporteDAO() {
		return (ModeloMeioTransporteDAO) getDao();
	}
	public void setTipoMeioTransporteService(TipoMeioTransporteService tipoMeioTransporteService) {
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}
	public void setMarcaMeioTransporteService(MarcaMeioTransporteService marcaMeioTransporteService) {
		this.marcaMeioTransporteService = marcaMeioTransporteService;
	}
	
}