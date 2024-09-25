package com.mercurio.lms.seguros.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.seguros.model.DoctoProcessoSinistro;
import com.mercurio.lms.seguros.model.dao.DoctoProcessoSinistroDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.doctoProcessoSinistroService"
 */
public class DoctoProcessoSinistroService extends CrudService<DoctoProcessoSinistro, Long> {


	/**
	 * Recupera uma instância de <code>DoctoProcessoSinistro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public DoctoProcessoSinistro findById(java.lang.Long id) {
        return (DoctoProcessoSinistro)super.findById(id);
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
    public java.io.Serializable store(DoctoProcessoSinistro bean) {
        
    	return super.store(bean);
    }
    

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDoctoProcessoSinistroDAO(DoctoProcessoSinistroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DoctoProcessoSinistroDAO getDoctoProcessoSinistroDAO() {
        return (DoctoProcessoSinistroDAO) getDao();
    }
    
    /**
     * Obtém o maior número de protocolo de acordo com o tipo de entrega de recebimento.
     * Caso este não seja informado, obtém o maior número de protocolo.
     * @author luisfco
     * @param tpEntregaRecebimento
     * @return
     */
    public Long findMaxNrProtocolo(String tpEntregaRecebimento) {
    	return getDoctoProcessoSinistroDAO().findMaxNrProtocolo(tpEntregaRecebimento);
    }

    
   }