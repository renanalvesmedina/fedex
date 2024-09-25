package com.mercurio.lms.expedicao.edi.model.service;
		
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;
import com.mercurio.lms.edi.model.dao.NotaFiscalEdiComplementoDAO;

/**
 * Classe de servi�o para CRUD: 
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.edi.notaFiscalExpedicaoEDIComplementoService"
 */
public class NotaFiscalExpedicaoEDIComplementoService extends CrudService<NotaFiscalEdiComplemento, Long> {
			 
	/**
	 * Recupera uma inst�ncia de <code>NotaFiscalEdiComplemento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public NotaFiscalEdiComplemento findById(java.lang.Long id) {
		return (NotaFiscalEdiComplemento)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	public void removeByIdNotaFiscalEdi(List<Long> list) {
		getNotaFiscalEdiComplementoDAO().removeByIdNotaFiscalEdi(list);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(NotaFiscalEdiComplemento bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setNotaFiscalEdiComplementoDAO(NotaFiscalEdiComplementoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private NotaFiscalEdiComplementoDAO getNotaFiscalEdiComplementoDAO() {
		return (NotaFiscalEdiComplementoDAO) getDao();
	}

	public List findAllEntities() {
		return getNotaFiscalEdiComplementoDAO().findAllEntities();
	}
	
    public List findByIdNotaFiscalEdi(Long idNotaFiscalEdi) {
		return getNotaFiscalEdiComplementoDAO().findByIdNotaFiscalEdi(idNotaFiscalEdi);
    }
    
    public List<NotaFiscalEdiComplemento> findByIdNotaFiscalEdiIndcIdInformacaoDoctoClien(final Long idNotaFiscalEdi, final Long indcIdInformacaoDoctoClien) {
    	return getNotaFiscalEdiComplementoDAO().findByIdNotaFiscalEdiIndcIdInformacaoDoctoClien(idNotaFiscalEdi, indcIdInformacaoDoctoClien);
    }
}