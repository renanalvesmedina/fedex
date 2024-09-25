package com.mercurio.lms.tributos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.CodigoFiscalOperacao;
import com.mercurio.lms.tributos.model.dao.CodigoFiscalOperacaoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.codigoFiscalOperacaoService"
 */
public class CodigoFiscalOperacaoService extends CrudService<CodigoFiscalOperacao, Long> {


	/**
	 * Recupera uma instância de <code>CodigoFiscalOperacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public CodigoFiscalOperacao findById(java.lang.Long id) {
        return (CodigoFiscalOperacao)super.findById(id);
    }
    
    /**
     * Busca o CdCfop do Ramo de Atividade do Cliente informado.<BR>
     *@author Robson Edemar Gehl
     * @param idCliente
     * @return
     */
    public Long findCdCfopRamoAtividadeByCliente(Long idCliente){
    	return getCodigoFiscalOperacaoDAO().findCdCfopRamoAtividadeByCliente(idCliente);
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
    public java.io.Serializable store(CodigoFiscalOperacao bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setCodigoFiscalOperacaoDAO(CodigoFiscalOperacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private CodigoFiscalOperacaoDAO getCodigoFiscalOperacaoDAO() {
        return (CodigoFiscalOperacaoDAO) getDao();
    }
   }