package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.MunicipioEnquadramento;
import com.mercurio.lms.sgr.model.dao.MunicipioEnquadramentoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.municipioEnquadramentoService"
 */
public class MunicipioEnquadramentoService extends CrudService<MunicipioEnquadramento, Long> {


	/**
	 * Recupera uma instância de <code>MunicipioEnquadramento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public MunicipioEnquadramento findById(java.lang.Long id) {
        return (MunicipioEnquadramento)super.findById(id);
    }

	/**
	 * Procura todos os municipios de origem atraves de um enquadramentoRegra
	 *
	 * @param id indica a entidade enquadramentoRegra
	 */
	public List findMuncipiosOrigemById(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);
    	List municipiosEnquadramentos = find(criteria);
    	List municipiosOrigem = new ArrayList();
    	for(Iterator it=municipiosEnquadramentos.iterator();it.hasNext();){
    		MunicipioEnquadramento municipioEnquadramento = (MunicipioEnquadramento)it.next();
    		if (municipioEnquadramento.getTpInfluenciaMunicipio().getValue().equals("O")) {
    			municipiosOrigem.add(municipioEnquadramento.getMunicipio());
    		}	
    	}
    	
    	return municipiosOrigem;
	}
	
	/**
	 * Procura todos os municipios de destino atraves de um enquadramentoRegra
	 *
	 * @param id indica a entidade enquadramentoRegra
	 */
	public List findMuncipiosDestinoById(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);
    	List municipiosEnquadramentos = find(criteria);
    	List municipiosDestino = new ArrayList();
    	for(Iterator it=municipiosEnquadramentos.iterator();it.hasNext();){
    		MunicipioEnquadramento municipioEnquadramento = (MunicipioEnquadramento)it.next();
    		if (municipioEnquadramento.getTpInfluenciaMunicipio().getValue().equals("D")) {
    			municipiosDestino.add(municipioEnquadramento.getMunicipio());
    		}	
    	}
    	
    	return municipiosDestino;
	}

	/**
	 * Apaga uma entidade através do IdEnquadramentoRegra
	 *
	 * @param id indica o enquadramentoRegra
	 */
    public void removeByIdEnquadramentoRegra(java.lang.Long id) {
        Map criteria = new HashMap();
 		criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);		
    	List municipiosEnquadramentos = find(criteria);
    	if (municipiosEnquadramentos != null && municipiosEnquadramentos.size() >0) {
    		List ids = new ArrayList();
    		for (Iterator iterator = municipiosEnquadramentos.iterator(); iterator.hasNext();) {
				MunicipioEnquadramento municipioEnquadramento = (MunicipioEnquadramento) iterator.next();
				ids.add(municipioEnquadramento.getIdMunicipioEnquadramento());
    	}
     		removeByIds(ids);    		
    }
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
    public java.io.Serializable store(MunicipioEnquadramento bean) {
        return super.store(bean);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public void storeMunicipios(List municipioEnquadramentosOrigem, List municipioEnquadramentosDestino, EnquadramentoRegra bean) {
    	removeByIdEnquadramentoRegra(bean.getIdEnquadramentoRegra());    	
    	DomainValue domainOrigem = new DomainValue();
    	domainOrigem.setValue("O");
        storeMunicipiosOrigemDestino(municipioEnquadramentosOrigem, bean, domainOrigem);
    	DomainValue domainDestino = new DomainValue();
    	domainDestino.setValue("D");
        storeMunicipiosOrigemDestino(municipioEnquadramentosDestino, bean, domainDestino);    	
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public void storeMunicipiosOrigemDestino(List municipioEnquadramentos, EnquadramentoRegra bean, DomainValue tipoOrigemDestino) {
		if (municipioEnquadramentos != null && municipioEnquadramentos.size() > 0) {
 	    	for(Iterator it=municipioEnquadramentos.iterator();it.hasNext();){
 	    		Municipio municipio = (Municipio)it.next();
 	    		MunicipioEnquadramento municipioEnquadramento = new MunicipioEnquadramento();
 	    		municipioEnquadramento.setMunicipio(municipio);
 	    		municipioEnquadramento.setEnquadramentoRegra(bean);
 	    		municipioEnquadramento.setTpInfluenciaMunicipio(tipoOrigemDestino);
 	     		store(municipioEnquadramento);
 	    	}
 		}
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMunicipioEnquadramentoDAO(MunicipioEnquadramentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MunicipioEnquadramentoDAO getMunicipioEnquadramentoDAO() {
        return (MunicipioEnquadramentoDAO) getDao();
    }
    
	public List find(Map criteria) {
    	List orderBy = new ArrayList(1);
    	orderBy.add("municipio_.nmMunicipio");
        return this.getMunicipioEnquadramentoDAO().findListByCriteria(criteria,orderBy);   
	}
    
   }