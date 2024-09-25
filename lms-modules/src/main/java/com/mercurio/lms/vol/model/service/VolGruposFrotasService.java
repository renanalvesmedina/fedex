package com.mercurio.lms.vol.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.VolGruposFrotas;
import com.mercurio.lms.vol.model.dao.VolGruposFrotasDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volGruposFrotasService"
 */
public class VolGruposFrotasService extends CrudService<VolGruposFrotas, Long> {
    private FilialService filialService; 

	/**
	 * Recupera uma instância de <code>VolGruposFrotas</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public VolGruposFrotas findById(java.lang.Long id) {
        return (VolGruposFrotas)super.findById(id);
    }
    
    public List findDsNomeById(Long id){
    	return getVolGruposFrotasDAO().findDsNomeById(id);
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
    public java.io.Serializable store(VolGruposFrotas bean) {
        return super.store(bean);
    }
    
    public List findGruposFrotaByUsuario(TypedFlatMap criteria){
    	return getVolGruposFrotasDAO().findGruposFrotaByUsuario(criteria);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVolGruposFrotasDAO(VolGruposFrotasDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VolGruposFrotasDAO getVolGruposFrotasDAO() {
        return (VolGruposFrotasDAO) getDao();
    }
    
    public ResultSetPage findPaginatedGruposFrota(TypedFlatMap criteria) {
	    return getVolGruposFrotasDAO().findPaginatedGruposFrota(criteria,FindDefinition.createFindDefinition(criteria)); 
	}
	               
	public Integer getRowCountGruposFrota(TypedFlatMap criteria) {
		return getVolGruposFrotasDAO().getRowCountGruposFrota(criteria);
	}
	
	public ResultSetPage findPaginatedGruposFrotaMeiosTransporte(TypedFlatMap criteria) {
	    return getVolGruposFrotasDAO().findPaginatedGruposFrotaMeiosTransporte(criteria,FindDefinition.createFindDefinition(criteria)); 
	}
	               
	public Integer getRowCountGruposFrotaMeiosTransporte(TypedFlatMap criteria) {
		return getVolGruposFrotasDAO().getRowCountGruposFrotaMeiosTransporte(criteria);
	}
	
	public ResultSetPage findPaginatedGruposFrotaUsuario(TypedFlatMap criteria) {
		return getVolGruposFrotasDAO().findPaginatedGruposFrotaUsuario(criteria,FindDefinition.createFindDefinition(criteria));
	}
	               
	public Integer getRowCountGruposFrotaUsuario(TypedFlatMap criteria) {
		return getVolGruposFrotasDAO().getRowCountGruposFrotaUsuario(criteria);
	}
	
	public List findMeioTransporteByIdGrupoFrota(Long idGrupoFrota) {
		return getVolGruposFrotasDAO().findMeioTransporteByIdGrupoFrota(idGrupoFrota);
	}
	
	public Integer getRowCountMeioTransporteByIdGrupoFrota(Long idGrupoFrota) {
		return getVolGruposFrotasDAO().getRowCountMeioTransporteByIdGrupoFrota(idGrupoFrota);
	}
	
	public List findUsuarioByIdGrupoFrota(Long idGrupoFrota) {
		return getVolGruposFrotasDAO().findUsuarioByIdGrupoFrota(idGrupoFrota);
	}
	
	public Integer getRowCountUsuarioByIdGrupoFrota(Long idGrupoFrota) {
		return getVolGruposFrotasDAO().getRowCountUsuarioByIdGrupoFrota(idGrupoFrota);
	}
	public void validateFilialVigente(Long idFilial) {
		getFilialService().verificaVigenciasEmHistoricoFilial(idFilial, JTDateTimeUtils.getDataAtual(), null);
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
   }