package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.MotivoCancelDescarga;
import com.mercurio.lms.carregamento.model.dao.MotivoCancelDescargaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.motivoCancelDescargaService"
 */
public class MotivoCancelDescargaService extends CrudService<MotivoCancelDescarga, Long> {


	/**
	 * Recupera uma inst�ncia de <code>MotivoCancelDescarga</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public MotivoCancelDescarga findById(java.lang.Long id) {
        return (MotivoCancelDescarga)super.findById(id);
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
    public java.io.Serializable store(MotivoCancelDescarga bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMotivoCancelDescargaDAO(MotivoCancelDescargaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private MotivoCancelDescargaDAO getMotivoCancelDescargaDAO() {
        return (MotivoCancelDescargaDAO) getDao();
    }
    
    /**
     * Retorna uma cole��o de MotivoCancelDescarga ordenados pelo campo dsMotivo.
     * Utilizado geralmente em combos.
     * @author Rodrigo Antunes
     * @param map
     * @return Lista ordenada por dsMotivo
     */
    public List findMotivoCancelDescargaOrderByDsMotivo(Map map) {
        //Este if � para garantir que o map n�o venha null do jsp
        if( map == null ) {
            map = new HashMap();
        }
        // monta uma list com os itens para ordena��o
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsMotivo:asc");

        return getDao().findListByCriteria(map, campoOrdenacao);        
    }

}