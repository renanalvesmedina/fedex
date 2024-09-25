package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.expedicao.model.TipoRegistroComplemento;
import com.mercurio.lms.expedicao.model.dao.TipoRegistroComplementoDAO;

/**
 * Classe de serviço para CRUD: 
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.tipoRegistroComplementoService"
 */
public class TipoRegistroComplementoService extends CrudService<TipoRegistroComplemento, Long> {

	/**
	 * Recupera uma instância de <code>TipoRegistroComplemento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public TipoRegistroComplemento findById(java.lang.Long id) {
		return (TipoRegistroComplemento)super.findById(id);
	}

	protected void beforeRemoveById(Long id) {
		verificaDadosComplemento(id);
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		verificaDadosComplemento(ids);
		super.beforeRemoveByIds(ids);
	}

	private void verificaDadosComplemento(Object id){
		if(!getTipoRegistroComplementoDAO().verificaDadosComplemento(id))
			throw new BusinessException("LMS-40001");
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
	 * @param list 
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(TipoRegistroComplemento bean, ItemList items) {
		Long masterId = null;
		if (items==null || !items.hasItems()) {
			throw new BusinessException("LMS-40002");
		}
		boolean rollbackMasterId = bean.getIdTipoRegistroComplemento() == null;
		try {
			this.beforeStore(bean);
			bean = getTipoRegistroComplementoDAO().store(bean,items);
			masterId = bean.getIdTipoRegistroComplemento();
		} catch (RuntimeException e) {
			this.rollbackMasterState(bean,rollbackMasterId,e);
			items.rollbackItemsState();
			throw e;
		}
		return masterId;
	}

	public List findInformacaoDocServico(Long masterId) {
		return getTipoRegistroComplementoDAO().findInformacaoDocServicoByTipoRegistroComplementoId(masterId);
	}

	public Integer getRowCountInformacaoDocServico(Long masterId) {
		return getTipoRegistroComplementoDAO().getRowCountInfoDocServicoByTipoRegistroId(masterId);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setTipoRegistroComplementoDAO(TipoRegistroComplementoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private TipoRegistroComplementoDAO getTipoRegistroComplementoDAO() {
		return (TipoRegistroComplementoDAO) getDao();
	}
}