package com.mercurio.lms.entrega.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.DocumentoMir;
import com.mercurio.lms.entrega.model.dao.DocumentoMirDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.documentoMirService"
 */
public class DocumentoMirService extends CrudService<DocumentoMir, Long> {


	/**
	 * Recupera uma instância de <code>DocumentoMir</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public DocumentoMir findById(java.lang.Long id) {
        return (DocumentoMir)super.findById(id);
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
    public java.io.Serializable store(DocumentoMir bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDocumentoMirDAO(DocumentoMirDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DocumentoMirDAO getDocumentoMirDAO() {
        return (DocumentoMirDAO) getDao();
    }

    
    /**
     * Método que retora um DocumentoMir a partir do ID do ReciboReembolso
     * 
     * Usado pela integração.
     * 
     * @param idReciboReembols
     * @return DocumentoMir
     */
    public DocumentoMir findDocumentoMirByIdReciboReembolso(Long idReciboReembolso) {
    	return this.getDocumentoMirDAO().findDocumentoMirByIdReciboReembolso(idReciboReembolso, null);
    } 
    
    /**
     * Método que retora um DocumentoMir a partir do ID do ReciboReembolso
     * @param idReciboReembols
     * @return DocumentoMir
     */
    public DocumentoMir findDocumentoMirByIdReciboReembolso(Long idReciboReembolso, String tpMir) {
    	return this.getDocumentoMirDAO().findDocumentoMirByIdReciboReembolso(idReciboReembolso, tpMir);
    } 
    
    /**
     * Método que retora um boolean a partir do ID do ReciboReembolso
     * @param idReciboReembols
     * @return boolean
     */
    public boolean findDocMirByIdReciboReembolso(Long idReciboReembolso) {
		return getDocumentoMirDAO().findDocMirByIdReciboReembolso(idReciboReembolso);
    }
    
}