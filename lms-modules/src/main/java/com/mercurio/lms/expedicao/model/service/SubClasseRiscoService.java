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
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.classeRiscoService"
 */
public class SubClasseRiscoService extends CrudService{
	
	/**
	 * Recupera uma instância de <code>ClasseRisco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public SubClasseRisco findById(java.lang.Long id) {
		return (SubClasseRisco)super.findById(id);
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
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setSubClasseRiscoDAO(SubClasseRiscoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private SubClasseRiscoDAO getSubClasseRiscoDAO() {
		return (SubClasseRiscoDAO) getDao();
	}
}
