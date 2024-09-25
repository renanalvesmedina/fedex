package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.ItemNfCto;
import com.mercurio.lms.expedicao.model.dao.ItemNfCtoDAO;
import com.mercurio.lms.sim.model.dao.LocalizacaoMercadoriaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.itemNfCtoService"
 */
public class ItemNfCtoService extends CrudService<ItemNfCto, Long> {
	
	private LocalizacaoMercadoriaDAO localizacaoMercadoriaDAO;

	public void setLocalizacaoMercadoriaDAO(LocalizacaoMercadoriaDAO localizacaoMercadoriaDAO) {
		this.localizacaoMercadoriaDAO = localizacaoMercadoriaDAO;
	}

	/**
	 * Recupera uma instância de <code>ItemNfCto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ItemNfCto findById(java.lang.Long id) {
		return (ItemNfCto)super.findById(id);
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
	public java.io.Serializable store(ItemNfCto bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setItemNfCtoDAO(ItemNfCtoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ItemNfCtoDAO getItemNfCtoDAO() {
		return (ItemNfCtoDAO) getDao();
	}

	public List findIdsByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
		return getItemNfCtoDAO().findIdsByIdNotaFiscalConhecimento(idNotaFiscalConhecimento);
	}
	
	public List findPaginatedItemNFC(Long idNFC){
		return localizacaoMercadoriaDAO.findPaginatedItemNFC(idNFC);
	}

}