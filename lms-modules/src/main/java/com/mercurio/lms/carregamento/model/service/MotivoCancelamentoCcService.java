package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.MotivoCancelamentoCc;
import com.mercurio.lms.carregamento.model.dao.MotivoCancelamentoCcDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.motivoCancelamentoCcService"
 */
public class MotivoCancelamentoCcService extends CrudService<MotivoCancelamentoCc, Long> {


	/**
	 * Recupera uma instância de <code>MotivoCancelamentoCc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public MotivoCancelamentoCc findById(java.lang.Long id) {
        return (MotivoCancelamentoCc)super.findById(id);
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
    public java.io.Serializable store(MotivoCancelamentoCc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMotivoCancelamentoCcDAO(MotivoCancelamentoCcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MotivoCancelamentoCcDAO getMotivoCancelamentoCcDAO() {
        return (MotivoCancelamentoCcDAO) getDao();
    }
    
    
    public List findOrderByDsMotivoCancelamentoCc(Map criteria){
        List campoOrdenacao = new ArrayList(1);
        campoOrdenacao.add("dsMotivoCancelamentoCc");
        return getDao().findListByCriteria(criteria, campoOrdenacao);
    }
}