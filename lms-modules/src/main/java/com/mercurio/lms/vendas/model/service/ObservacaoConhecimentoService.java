package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.ObservacaoConhecimento;
import com.mercurio.lms.vendas.model.dao.ObservacaoConhecimentoDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.vendas.observacaoConhecimentoService"
 */
public class ObservacaoConhecimentoService extends CrudService<ObservacaoConhecimento, Long> {


	public ResultSetPage findPaginated(TypedFlatMap map) {
		FindDefinition fd = FindDefinition.createFindDefinition(map);
		return getObservacaoConhecimentoDAO().findPaginated(map, fd);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getObservacaoConhecimentoDAO().getRowCount(criteria);
	}

	public ObservacaoConhecimento findById(Long id) {
		return (ObservacaoConhecimento)super.findById(id);
	}
	
	public java.io.Serializable store(ObservacaoConhecimento bean) {
		return super.store(bean);
	}
	
	
	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(Long id) {
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
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setObservacaoConhecimentoDAO(ObservacaoConhecimentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private ObservacaoConhecimentoDAO getObservacaoConhecimentoDAO() {
		return (ObservacaoConhecimentoDAO) getDao();
	}


}