package com.mercurio.lms.indenizacoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacaoNf;
import com.mercurio.lms.indenizacoes.model.dao.ReciboIndenizacaoNfDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.indenizacoes.reciboIndenizacaoNfService"
 */
public class ReciboIndenizacaoNfService extends CrudService<ReciboIndenizacaoNf, Long> {


	/**
	 * Recupera uma instância de <code>ReciboIndenizacaoNf</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public ReciboIndenizacaoNf findById(java.lang.Long id) {
        return (ReciboIndenizacaoNf)super.findById(id);
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
    public java.io.Serializable store(ReciboIndenizacaoNf bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setReciboIndenizacaoNfDAO(ReciboIndenizacaoNfDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ReciboIndenizacaoNfDAO getReciboIndenizacaoNfDAO() {
        return (ReciboIndenizacaoNfDAO) getDao();
    }
    
    public void removeByIdDoctoServicoIndenizacao(Long idDoctoServicoIndenizacao) {
    	getReciboIndenizacaoNfDAO().removeByIdDoctoServicoIndenizacao(idDoctoServicoIndenizacao);
    }

    public List findByIdDoctoServicoIndenizacao(Long idDoctoServicoIndenizacao) {
    	return getReciboIndenizacaoNfDAO().findByIdDoctoServicoIndenizacao(idDoctoServicoIndenizacao);
    }
    
   }