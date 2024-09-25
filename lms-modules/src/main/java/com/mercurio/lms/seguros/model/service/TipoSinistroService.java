package com.mercurio.lms.seguros.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.TipoSinistro;
import com.mercurio.lms.seguros.model.dao.TipoSinistroDAO;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguros.tipoSinistroService"
 */
public class TipoSinistroService extends CrudService<TipoSinistro, Long> {


	/**
	 * Recupera uma inst�ncia de <code>TipoSinistro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public TipoSinistro findById(java.lang.Long id) {
        return (TipoSinistro)super.findById(id);
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
    public java.io.Serializable store(TipoSinistro bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTipoSinistroDAO(TipoSinistroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TipoSinistroDAO getTipoSinistroDAO() {
        return (TipoSinistroDAO) getDao();
    }
    
	public List findOrderByDsTipo(Map criteria) {
    	List orderBy = new ArrayList(1);
    	orderBy.add("dsTipo");
        return this.getTipoSinistroDAO().findListByCriteria(criteria,orderBy);   
	}

	public List findTipoProcessoSinistroByTipoSeguro(TypedFlatMap tfm) {
		return this.getTipoSinistroDAO().findTipoProcessoSinistroByTipoSeguro(tfm);
	}
	
   }