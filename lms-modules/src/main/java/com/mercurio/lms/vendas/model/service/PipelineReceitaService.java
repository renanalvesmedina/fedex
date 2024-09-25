package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.PipelineReceita;
import com.mercurio.lms.vendas.model.dao.PipelineReceitaDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.pipelineReceitaService"
 */
public class PipelineReceitaService extends CrudService<PipelineReceita, Long> {
	
	/**
	 * Recupera uma Lista de PipelineReceita a partir do idPipelineCliente
	 * @param idPipelineCliente
	 * @return
	 */
	public List findPipelineReceitaByPipelineCliente(Long idPipelineCliente){
		return getPipelineReceitaDAO().findPipelineReceitaByPipelineCliente(idPipelineCliente);
	}
	
	

	/**
	 * Recupera uma instância de <code>PipelineReceita</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public PipelineReceita findById(java.lang.Long id) {
		return (PipelineReceita)super.findById(id);
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
	 * Apaga várias entidades através do IdVisita
	 */
	public void removeByIdPipelineCliente(Long idPipelineCliente){
		getPipelineReceitaDAO().removeByIdPipelineCliente(idPipelineCliente);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(PipelineReceita bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPipelineReceitaDAO(PipelineReceitaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private PipelineReceitaDAO getPipelineReceitaDAO() {
		return (PipelineReceitaDAO) getDao();
	}

}
