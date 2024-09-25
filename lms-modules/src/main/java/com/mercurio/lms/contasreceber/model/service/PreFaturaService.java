package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.PreFatura;
import com.mercurio.lms.contasreceber.model.dao.PreFaturaDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.preFaturaService"
 */
public class PreFaturaService extends CrudService<PreFatura, Long> {


	/**
	 * Recupera uma instância de <code>RepositorioItemRedeco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public PreFatura findById(java.lang.Long id) {
        return (PreFatura)super.findById(id);
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
    public java.io.Serializable store(PreFatura bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPreFaturaDAO(PreFaturaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PreFaturaDAO getPreFaturaDAO() {
        return (PreFaturaDAO) getDao();
    }
    
    
    /**
	 * Recuperda os dados da PreFatura, Fatura e Filial da Fatura para o idFatura especificado
	 * @author Diego Umpierre
	 *
	 *
	 * @param id da fatura 
	 * @return Instância com os dados a partir do id da fatura
	 */
    public Object findByIdFatura(java.lang.Long id) {
        return  getPreFaturaDAO().findByIdFatura(id);
    }
    
    
   }