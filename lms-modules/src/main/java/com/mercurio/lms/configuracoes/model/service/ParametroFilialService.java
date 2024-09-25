package com.mercurio.lms.configuracoes.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.ParametroFilial;
import com.mercurio.lms.configuracoes.model.dao.ParametroFilialDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.parametroFilialService"
 */
public class ParametroFilialService extends CrudService<ParametroFilial, Long> {


	/**
	 * Recupera uma inst�ncia de <code>ParametroFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ParametroFilial findById(java.lang.Long id) {
        return (ParametroFilial)super.findById(id);
    }

    /**
     * 
     * @param parametro
     * @return
     * @deprecated @see ConteudoParametroFilialService#findByNomeParametro(Long, String, boolean)
     * 
     */
    public ParametroFilial findByParametro(String parametro) {
		Map map = new HashMap(1);
		map.put("nmParametroFilial",parametro);
		List l = find(map);
		if(!l.isEmpty())
		{
			return (ParametroFilial)l.get(0);
		}
        return null;
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
    public java.io.Serializable store(ParametroFilial bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setParametroFilialDAO(ParametroFilialDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ParametroFilialDAO getParametroFilialDAO() {
        return (ParametroFilialDAO) getDao();
    }
   }