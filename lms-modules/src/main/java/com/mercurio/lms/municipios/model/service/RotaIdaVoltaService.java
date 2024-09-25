package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.dao.RotaIdaVoltaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.rotaIdaVoltaService"
 */
public class RotaIdaVoltaService extends CrudService<RotaIdaVolta, Long> {

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		return getRotaIdaVoltaDAO().findPaginatedCustom(criteria,FindDefinition.createFindDefinition(criteria));
	}
	
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getRotaIdaVoltaDAO().getRowCountCustom(criteria);
	}

	 public List findLookupRotaIdaVolta(TypedFlatMap criteria){
		 return getRotaIdaVoltaDAO().findLookupRotaIdaVolta(criteria);
	 }
	 
	 /**
	  * Busca as rotas ida e volta que estao dentro do periodo de vigencia.
	  * 
	  * @param criteria
	  * @return
	  */
	 public List findRotaIdaVoltaInVigencia(Integer nrRota, YearMonthDay vigencia){
		 return getRotaIdaVoltaDAO().findRotaIdaVoltaInVigencia(nrRota, vigencia);
	 }
	 
	/**
	 * Recupera uma instância de <code>RotaIdaVolta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public RotaIdaVolta findById(java.lang.Long id) {
        return (RotaIdaVolta)super.findById(id);
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
    public java.io.Serializable store(RotaIdaVolta bean) {
        return super.store(bean);
    }
    
    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public RotaIdaVolta storeReturnPojo(RotaIdaVolta bean) {
        super.store(bean);
        return bean;
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRotaIdaVoltaDAO(RotaIdaVoltaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RotaIdaVoltaDAO getRotaIdaVoltaDAO() {
        return (RotaIdaVoltaDAO) getDao();
    }
    
    public Map findByIdToConsultarRotas(Long idRotaViagem, boolean tipoRota) {
    	return getRotaIdaVoltaDAO().findByIdToConsultarRotas(idRotaViagem,tipoRota);
    }
    
    public List findFiliaisRotaByIdRotaIdaVolta(Long idRotaIdaVolta) {
    	return getRotaIdaVoltaDAO().findFiliaisRotaByIdRotaIdaVolta(idRotaIdaVolta);
    }
    
    /**
     * Retorna uma lista com as rotas vigentes para a filial de origem informada 
     * @param idFilialOrigem
     * @return
     */
    public List<RotaIdaVolta> findRotasVigentesByIdFilialOrigem(Long idFilialOrigem) {
    	return getRotaIdaVoltaDAO().findRotasVigentesByIdFilialOrigem(idFilialOrigem);
    }
    
    public List<TrechoRotaIdaVolta> findRotasViagemByIdFilialOrigem(Long idFilialOrigem) {    	
    	return getRotaIdaVoltaDAO().findRotasViagemByIdFilialOrigem(idFilialOrigem);
    }
}