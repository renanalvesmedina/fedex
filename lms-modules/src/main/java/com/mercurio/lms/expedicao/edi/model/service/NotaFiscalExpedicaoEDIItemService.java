package com.mercurio.lms.expedicao.edi.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.edi.model.dao.NotaFiscalEdiItemDAO;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.edi.notaFiscalExpedicaoEDIItemService"
 */
public class NotaFiscalExpedicaoEDIItemService extends CrudService<NotaFiscalEdiItem, Long> {

	/**
	 * Recupera uma instância de <code>NotaFiscalEdiItem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public NotaFiscalEdiItem findById(java.lang.Long id) {
		return (NotaFiscalEdiItem)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	public void removeByIdNotaFiscalEdi(List<Long> list) {
		getNotaFiscalEdiItemDAO().removeByIdNotaFiscalEdi(list);
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
	public java.io.Serializable store(NotaFiscalEdiItem bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setNotaFiscalEdiItemDAO(NotaFiscalEdiItemDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private NotaFiscalEdiItemDAO getNotaFiscalEdiItemDAO() {
		return (NotaFiscalEdiItemDAO) getDao();
	}

	public List findAllEntities() {
		return getNotaFiscalEdiItemDAO().findAllEntities();
	}
	
	public List findByIdNotaFiscalEdi(Long idNotaFiscalEdi) {
		return getNotaFiscalEdiItemDAO().findByIdNotaFiscalEdi(idNotaFiscalEdi);
	}
	
}