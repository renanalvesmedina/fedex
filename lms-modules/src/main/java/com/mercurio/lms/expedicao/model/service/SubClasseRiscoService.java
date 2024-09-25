package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.SubClasseRisco;
import com.mercurio.lms.expedicao.model.dao.SubClasseRiscoDAO;

/**
 * Classe de servi�o para CRUD: 
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.classeRiscoService"
 */
public class SubClasseRiscoService extends CrudService{
	
	/**
	 * Recupera uma inst�ncia de <code>ClasseRisco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public SubClasseRisco findById(java.lang.Long id) {
		return (SubClasseRisco)super.findById(id);
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
	public java.io.Serializable store(SubClasseRisco bean) {
		return super.store(bean);
	}

	public List findSubClasseRisco(Map criteria) {
        List retorno = new ArrayList();
        
        List order = new ArrayList(1);
        order.add("nrSubClasseRisco:asc");
        
        List listSubClasseRisco = getSubClasseRiscoDAO().findListByCriteria(criteria, order);
        for (Iterator iter = listSubClasseRisco.iterator(); iter.hasNext();) {
            TypedFlatMap map = new TypedFlatMap();
            SubClasseRisco subClasseRisco = (SubClasseRisco) iter.next();
            map.put("idSubClasseRisco",
                    subClasseRisco.getIdSubClasseRisco());
            map.put("nrSubClasseRisco",
                    subClasseRisco.getNrSubClasseRisco());
            retorno.add(map);
        }
        return retorno;
    }
	
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setSubClasseRiscoDAO(SubClasseRiscoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private SubClasseRiscoDAO getSubClasseRiscoDAO() {
		return (SubClasseRiscoDAO) getDao();
	}
}
