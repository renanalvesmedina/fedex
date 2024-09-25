package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.PaisEnquadramento;
import com.mercurio.lms.sgr.model.dao.PaisEnquadramentoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.paisEnquadramentoService"
 */
public class PaisEnquadramentoService extends CrudService<PaisEnquadramento, Long> {


	/**
	 * Recupera uma instância de <code>PaisEnquadramento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public PaisEnquadramento findById(java.lang.Long id) {
        return (PaisEnquadramento)super.findById(id);
    }

	/**
	 * Procura todos os municipios de origem atraves de um enquadramentoRegra
	 *
	 * @param id indica a entidade enquadramentoRegra
	 */
	public List findPaisesOrigemById(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);
    	List paisesEnquadramentos = find(criteria);
    	List paisesOrigem = new ArrayList();
    	for(Iterator it=paisesEnquadramentos.iterator();it.hasNext();){
    		PaisEnquadramento paisEnquadramento = (PaisEnquadramento)it.next();
    		if (paisEnquadramento.getTpInfluenciaMunicipio().getValue().equals("O")) {
    			paisesOrigem.add(paisEnquadramento.getPais());
    		}	
    	}
    	
    	return paisesOrigem;
	}
	
	/**
	 * Procura todos os municipios de destino atraves de um enquadramentoRegra
	 *
	 * @param id indica a entidade enquadramentoRegra
	 */
	public List findPaisesDestinoById(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);
    	List paisesEnquadramentos = find(criteria);
    	List paisesDestino = new ArrayList();
    	for(Iterator it=paisesEnquadramentos.iterator();it.hasNext();){
    		PaisEnquadramento paisEnquadramento = (PaisEnquadramento)it.next();
    		if (paisEnquadramento.getTpInfluenciaMunicipio().getValue().equals("D")) {
    			paisesDestino.add(paisEnquadramento.getPais());
    		}	
    	}
    	
    	return paisesDestino;
	}

	/**
	 * Apaga uma entidade através do IdEnquadramentoRegra
	 *
	 * @param id indica o enquadramentoRegra
	 */
    public void removeByIdEnquadramentoRegra(java.lang.Long id) {
        Map criteria = new HashMap();
 		criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);		
    	List paisesEnquadramentos = find(criteria);
    	if (paisesEnquadramentos != null && paisesEnquadramentos.size() >0) {
    		List ids = new ArrayList();
    		for (Iterator iterator = paisesEnquadramentos.iterator(); iterator.hasNext();) {
				PaisEnquadramento paisEnquadramento = (PaisEnquadramento) iterator.next();
				ids.add(paisEnquadramento.getIdPaisEnquadramento());
    	}
     		removeByIds(ids);    		
    }
    }
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public void storePaises(List paisEnquadramentosOrigem, List paisEnquadramentosDestino, EnquadramentoRegra bean) {
    	removeByIdEnquadramentoRegra(bean.getIdEnquadramentoRegra());    	
    	DomainValue domainOrigem = new DomainValue();
    	domainOrigem.setValue("O");
        storePaisesOrigemDestino(paisEnquadramentosOrigem, bean, domainOrigem);
    	DomainValue domainDestino = new DomainValue();
    	domainDestino.setValue("D");
        storePaisesOrigemDestino(paisEnquadramentosDestino, bean, domainDestino);    	
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public void storePaisesOrigemDestino(List paisEnquadramentos, EnquadramentoRegra bean, DomainValue tipoOrigemDestino) {
		if (paisEnquadramentos != null && paisEnquadramentos.size() > 0) {
 	    	for(Iterator it=paisEnquadramentos.iterator();it.hasNext();){
 	    		Pais pais = (Pais)it.next();
 	    		PaisEnquadramento paisEnquadramento = new PaisEnquadramento();
 	    		paisEnquadramento.setPais(pais);
 	    		paisEnquadramento.setEnquadramentoRegra(bean);
 	    		paisEnquadramento.setTpInfluenciaMunicipio(tipoOrigemDestino);
 	     		store(paisEnquadramento);
 	    	}
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
    public java.io.Serializable store(PaisEnquadramento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPaisEnquadramentoDAO(PaisEnquadramentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PaisEnquadramentoDAO getPaisEnquadramentoDAO() {
        return (PaisEnquadramentoDAO) getDao();
    }
    
	public List find(Map criteria) {
    	List orderBy = new ArrayList(1);
    	orderBy.add("pais_.nmPais");
        return this.getPaisEnquadramentoDAO().findListByCriteria(criteria,orderBy);   
	}
    
   }