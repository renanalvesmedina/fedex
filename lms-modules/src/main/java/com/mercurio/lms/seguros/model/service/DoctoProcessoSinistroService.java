package com.mercurio.lms.seguros.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.seguros.model.DoctoProcessoSinistro;
import com.mercurio.lms.seguros.model.dao.DoctoProcessoSinistroDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguros.doctoProcessoSinistroService"
 */
public class DoctoProcessoSinistroService extends CrudService<DoctoProcessoSinistro, Long> {


	/**
	 * Recupera uma inst�ncia de <code>DoctoProcessoSinistro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public DoctoProcessoSinistro findById(java.lang.Long id) {
        return (DoctoProcessoSinistro)super.findById(id);
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
    public java.io.Serializable store(DoctoProcessoSinistro bean) {
        
    	return super.store(bean);
    }
    

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setDoctoProcessoSinistroDAO(DoctoProcessoSinistroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private DoctoProcessoSinistroDAO getDoctoProcessoSinistroDAO() {
        return (DoctoProcessoSinistroDAO) getDao();
    }
    
    /**
     * Obt�m o maior n�mero de protocolo de acordo com o tipo de entrega de recebimento.
     * Caso este n�o seja informado, obt�m o maior n�mero de protocolo.
     * @author luisfco
     * @param tpEntregaRecebimento
     * @return
     */
    public Long findMaxNrProtocolo(String tpEntregaRecebimento) {
    	return getDoctoProcessoSinistroDAO().findMaxNrProtocolo(tpEntregaRecebimento);
    }

    
   }