package com.mercurio.lms.entrega.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.DocumentoMir;
import com.mercurio.lms.entrega.model.dao.DocumentoMirDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.documentoMirService"
 */
public class DocumentoMirService extends CrudService<DocumentoMir, Long> {


	/**
	 * Recupera uma inst�ncia de <code>DocumentoMir</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public DocumentoMir findById(java.lang.Long id) {
        return (DocumentoMir)super.findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(DocumentoMir bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setDocumentoMirDAO(DocumentoMirDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private DocumentoMirDAO getDocumentoMirDAO() {
        return (DocumentoMirDAO) getDao();
    }

    
    /**
     * M�todo que retora um DocumentoMir a partir do ID do ReciboReembolso
     * 
     * Usado pela integra��o.
     * 
     * @param idReciboReembols
     * @return DocumentoMir
     */
    public DocumentoMir findDocumentoMirByIdReciboReembolso(Long idReciboReembolso) {
    	return this.getDocumentoMirDAO().findDocumentoMirByIdReciboReembolso(idReciboReembolso, null);
    } 
    
    /**
     * M�todo que retora um DocumentoMir a partir do ID do ReciboReembolso
     * @param idReciboReembols
     * @return DocumentoMir
     */
    public DocumentoMir findDocumentoMirByIdReciboReembolso(Long idReciboReembolso, String tpMir) {
    	return this.getDocumentoMirDAO().findDocumentoMirByIdReciboReembolso(idReciboReembolso, tpMir);
    } 
    
    /**
     * M�todo que retora um boolean a partir do ID do ReciboReembolso
     * @param idReciboReembols
     * @return boolean
     */
    public boolean findDocMirByIdReciboReembolso(Long idReciboReembolso) {
		return getDocumentoMirDAO().findDocMirByIdReciboReembolso(idReciboReembolso);
    }
    
}