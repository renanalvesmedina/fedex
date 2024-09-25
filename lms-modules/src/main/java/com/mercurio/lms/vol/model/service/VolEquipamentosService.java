package com.mercurio.lms.vol.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.vol.model.VolEquipamentos;
import com.mercurio.lms.vol.model.dao.VolEquipamentosDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vol.volEquipamentosService"
 */
public class VolEquipamentosService extends CrudService<VolEquipamentos, Long> {
	private FilialService filialService;


	/**
	 * Recupera uma inst�ncia de <code>VolEquipamentos</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public VolEquipamentos findById(java.lang.Long id) {
        return (VolEquipamentos)super.findById(id);
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
    public java.io.Serializable store(VolEquipamentos bean) {
    	
    	if (bean.getMeioTransporte() != null) {
    		List equipamentos = findEquipamentoByIdMeioTransporte(bean.getMeioTransporte().getIdMeioTransporte(), bean.getIdEquipamento());
    		
    		if (equipamentos.size() > 0) {
    			throw new BusinessException("LMS-41050");
    		}   		
    	}
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setVolEquipamentosDAO(VolEquipamentosDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private VolEquipamentosDAO getVolEquipamentosDAO() {
        return (VolEquipamentosDAO) getDao();
    }
    
    public List findEquipamentoByIdMeioTransporte(Long idMeioTransporte, Long idEquipamento) {
		List result =  getVolEquipamentosDAO().findEquipamentoByIdMeioTransporte(idMeioTransporte, idEquipamento);
		return result;
	}
    
    
    
    /**
     * Valida se o IMEI enviado pelo aparelho (na instala��o do midlet) est� cadastrado na base
     * @param map com o imei e a filial
     * @return idEquipamento
     */
	public Map executeValidaImei(TypedFlatMap map ){
		Map retorno = new HashMap();
		retorno.put("idEquipamento", "NUL");
		
		String cdFilial = map.getString("filial");
		String imei = map.getString("imei");		
		Filial oFilial = filialService.findFilialByCodigoFilial(Integer.valueOf(cdFilial));
		
		Long idEquipamento = getVolEquipamentosDAO().findIdEquipamentoByImeiEFilial(imei, oFilial.getIdFilial());
		
		if( idEquipamento != null){
			retorno.put("idEquipamento", idEquipamento );
		}
		
		return retorno;
		
	}
    
	public VolEquipamentos findEquipamentoByImei( String imei){
		return getVolEquipamentosDAO().findEquipamentoByImei(imei);
	}
    
    public List findMeioTransporte(Long idMeioTransporte) {
		List result =  getVolEquipamentosDAO().findMeioTransporte(idMeioTransporte);
		return result;
	}
    
    public ResultSetPage findPaginatedEquipamentos(TypedFlatMap criteria){
    	FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
    	return getVolEquipamentosDAO().findPaginatedOperadoras(criteria, findDef);
    }
    
    public Integer getRowCountEquipamentos(TypedFlatMap criteria){
    	return getVolEquipamentosDAO().getRowCountEquipamentos(criteria);
    }

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
   }
