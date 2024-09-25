package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.MotivoCancelamentoCc;
import com.mercurio.lms.carregamento.model.dao.MotivoCancelamentoCcDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.motivoCancelamentoCcService"
 */
public class MotivoCancelamentoCcService extends CrudService<MotivoCancelamentoCc, Long> {


	/**
	 * Recupera uma inst�ncia de <code>MotivoCancelamentoCc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public MotivoCancelamentoCc findById(java.lang.Long id) {
        return (MotivoCancelamentoCc)super.findById(id);
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
    public java.io.Serializable store(MotivoCancelamentoCc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMotivoCancelamentoCcDAO(MotivoCancelamentoCcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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