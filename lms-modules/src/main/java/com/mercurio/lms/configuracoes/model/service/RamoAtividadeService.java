package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.RamoAtividade;
import com.mercurio.lms.configuracoes.model.dao.RamoAtividadeDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.ramoAtividadeService"
 */
public class RamoAtividadeService extends CrudService<RamoAtividade, Long> {


	/**
	 * Recupera uma inst�ncia de <code>RamoAtividade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public RamoAtividade findById(java.lang.Long id) {
        return (RamoAtividade)super.findById(id);
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
    public java.io.Serializable store(RamoAtividade bean) {
        return super.store(bean);
    }
    
    /**
     * Busca a lista de ramos de atividade ordenados pelo dsRamoAtividade.
     * 
     * @param criterions
     * @return
     */
    public List findCombo(Map criterions) {
    	List order = new ArrayList();
    	order.add("dsRamoAtividade:asc");
    	return getDao().findListByCriteria(criterions, order);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setRamoAtividadeDAO(RamoAtividadeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private RamoAtividadeDAO getRamoAtividadeDAO() {
        return (RamoAtividadeDAO) getDao();
    }
   }