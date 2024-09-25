package com.mercurio.lms.municipios.model.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.McdMunicipioFilial;
import com.mercurio.lms.municipios.model.dao.McdMunicipioFilialDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.mcdMunicipioFilialService"
 */
public class McdMunicipioFilialService extends CrudService<McdMunicipioFilial, Long> {


	/**
	 * Recupera uma inst�ncia de <code>McdMunicipioFilial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public McdMunicipioFilial findById(java.lang.Long id) {
        return (McdMunicipioFilial)super.findById(id);
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
    public java.io.Serializable store(McdMunicipioFilial bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMcdMunicipioFilialDAO(McdMunicipioFilialDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private McdMunicipioFilialDAO getMcdMunicipioFilialDAO() {
        return (McdMunicipioFilialDAO) getDao();
    }
    		
    public ResultSetPage findPaginatedToConsultarMcd(TypedFlatMap criteria) {
    	ResultSetPage rsp = getMcdMunicipioFilialDAO().findPaginatedToConsultarMcd(criteria,FindDefinition.createFindDefinition(criteria));
    	
    	Iterator i = rsp.getList().iterator();
    	while (i.hasNext()) {
    		Map m = (Map)i.next();
    		m.put("municipioFilialByIdMunicipioFilialOrigem_nmMunicipio",
    				(String)m.get("nmMunicipioOrigem") + " - " + (String)m.get("sgUnidadeFederativaOrigem") +
    				" - " + ((VarcharI18n)m.get("nmPaisOrigem")).getValue());
    		m.put("municipioFilialByIdMunicipioFilialDestino_nmMunicipio",
    				(String)m.get("nmMunicipioDestino") + " - " + (String)m.get("sgUnidadeFederativaDestino") + 
    				" - " + ((VarcharI18n)m.get("nmPaisDestino")).getValue());
    	}
    	
    	return rsp;
    }
    
    public Integer getRowCountToConsultarMcd(TypedFlatMap criteria) {
    	return getMcdMunicipioFilialDAO().getRowCountToConsultarMcd(criteria);
    }
    
    public void deleteByIdMcd(Serializable idMcd){
    	getMcdMunicipioFilialDAO().deleteByIdMcd(idMcd);
    }
}