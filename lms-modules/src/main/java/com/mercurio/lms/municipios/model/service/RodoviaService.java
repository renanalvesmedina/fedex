package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Rodovia;
import com.mercurio.lms.municipios.model.dao.RodoviaDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.rodoviaService"
 */
public class RodoviaService extends CrudService<Rodovia, Long> {

	/**
	 * Recupera uma instância de <code>Rodovia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public Rodovia findById(java.lang.Long id) {
        return (Rodovia) super.findById(id);
    }
    
    public Integer getRowCountCustom(TypedFlatMap criteria) {
		Integer rowCountCustom = this.getRodoviaDAO().getRowCount(criteria);
		return rowCountCustom;
	}
	
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
        ResultSetPage rsp = this.getRodoviaDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));

        List<Map<String, Object>> result = bean2Map(rsp.getList());
        rsp.setList(result);

        return rsp;
	}

	private List<Map<String, Object>> bean2Map(List<Map<String, Object>> result) {
        List<Map<String, Object>> newResult = new ArrayList<Map<String, Object>>();
        for(Map<String, Object> linha : result) { 
        	TypedFlatMap map = new TypedFlatMap();
        	map.put("idRodovia", linha.get("idRodovia"));
        	map.put("sgRodovia", linha.get("sgRodovia"));
        	map.put("dsRodovia", linha.get("dsRodovia"));
        	map.put("pais.nmPais", linha.get("pais_nmPais"));
        	map.put("unidadeFederativa.sgUnidadeFederativa" , linha.get("unidadeFederativa_sgUnidadeFederativa"));
        	map.put("unidadeFederativa.idUnidadeFederativa",  linha.get("unidadeFederativa_idUnidadeFederativa"));
        	map.put("tpSituacao", linha.get("tpSituacao"));
        	newResult.add(map); 
        }
		return newResult;
	} 
  
	/**
	 * Implementa a funcionalidade da lookup de rodovia,<BR>
	 * porem se o criteria <b>flag</b> e <b>unidadeFederativa</b> for passado ele filtra</br>
	 * todas as rodovias com a respectiva UF ou com UF <code>null<code>
	 * @author Samuel Herrmann 
	 * @param criteria
	 * @return
	 */
	public List findLookupWithUFNull(Map<String, Object> criteria) {
		return getRodoviaDAO().findLookupWithUFNull(criteria);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
    public void removeById(Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	@Override
    protected Rodovia beforeStore(Rodovia bean) {
    	if(bean.getPais() == null)
    		throw new BusinessException("LMS-29052");
    	return super.beforeStore(bean);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Rodovia bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRodoviaDAO(RodoviaDAO dao) {
        setDao( dao );
    }
    
    public List findRodoviaBySgAndPais(String sgRodovia,Long idPais) {
    	return getRodoviaDAO().findRodoviaBySgAndPais(sgRodovia,idPais);
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RodoviaDAO getRodoviaDAO() {
        return (RodoviaDAO) getDao();
    }

	public boolean possuiUnidadeFederativaDiferente(Long idRodovia, Long idUnidadeFederativa) {
		return getRodoviaDAO().possuiUnidadeFederativaDiferente(idRodovia, idUnidadeFederativa);
	}

	public boolean findRodoviaByUf(Long idRodovia,Long idUf) {
		return getRodoviaDAO().findRodoviaByUf(idRodovia,idUf).size() > 0; 
	}
}