package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.PipelineReceita;
import com.mercurio.lms.vendas.model.dao.PipelineReceitaDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
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
	 * Recupera uma inst�ncia de <code>PipelineReceita</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public PipelineReceita findById(java.lang.Long id) {
		return (PipelineReceita)super.findById(id);
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
	 * Apaga v�rias entidades atrav�s do IdVisita
	 */
	public void removeByIdPipelineCliente(Long idPipelineCliente){
		getPipelineReceitaDAO().removeByIdPipelineCliente(idPipelineCliente);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(PipelineReceita bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setPipelineReceitaDAO(PipelineReceitaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private PipelineReceitaDAO getPipelineReceitaDAO() {
		return (PipelineReceitaDAO) getDao();
	}

}
