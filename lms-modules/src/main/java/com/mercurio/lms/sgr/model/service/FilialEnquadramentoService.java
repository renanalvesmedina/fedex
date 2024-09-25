package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.FilialEnquadramento;
import com.mercurio.lms.sgr.model.dao.FilialEnquadramentoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.filialEnquadramentoService"
 */
public class FilialEnquadramentoService extends CrudService<FilialEnquadramento, Long> {


	/**
	 * Recupera uma instância de <code>FilialEnquadramento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */	
    public FilialEnquadramento findById(java.lang.Long id) {
        return (FilialEnquadramento)super.findById(id);
    }

	/**
	 * Procura todos os municipios de origem atraves de um enquadramentoRegra
	 *
	 * @param id indica a entidade enquadramentoRegra
	 */
	public List findFiliaisOrigemById(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);
    	List filiaisEnquadramentos = find(criteria);
    	List filiaisOrigem = new ArrayList();
    	for(Iterator it=filiaisEnquadramentos.iterator();it.hasNext();){
    		FilialEnquadramento filialEnquadramento = (FilialEnquadramento)it.next();
    		if (filialEnquadramento.getTpInfluenciaMunicipio().getValue().equals("O")) {
    			filiaisOrigem.add(filialEnquadramento.getFilial());
    		}	
    	}
    	
    	return filiaisOrigem;
	}
	
	/**
	 * Procura todos os municipios de destino atraves de um enquadramentoRegra
	 *
	 * @param id indica a entidade enquadramentoRegra
	 */
	public List findFiliaisDestinoById(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);
    	List filiaisEnquadramentos = find(criteria);
    	List filiaisDestino = new ArrayList();
    	for(Iterator it=filiaisEnquadramentos.iterator();it.hasNext();){
    		FilialEnquadramento filialEnquadramento = (FilialEnquadramento)it.next();
    		if (filialEnquadramento.getTpInfluenciaMunicipio().getValue().equals("D")) {
    			filiaisDestino.add(filialEnquadramento.getFilial());
    		}	
    	}
    	
    	return filiaisDestino;
	}

	/**
	 * Apaga uma entidade através do IdEnquadramentoRegra
	 *
	 * @param id indica o enquadramentoRegra
	 */
    public void removeByIdEnquadramentoRegra(java.lang.Long id) {
        Map criteria = new HashMap();
 		criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);		
    	List filiaisEnquadramentos = find(criteria);
    	if (filiaisEnquadramentos != null && filiaisEnquadramentos.size() >0) {
    		List ids = new ArrayList();
    		for (Iterator iterator = filiaisEnquadramentos.iterator(); iterator.hasNext();) {
    			FilialEnquadramento filialEnquadramento = (FilialEnquadramento)iterator.next();
				ids.add(filialEnquadramento.getIdFilialEnquadramento());
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
    public void storeFiliais(List filialEnquadramentosOrigem, List filialEnquadramentosDestino, EnquadramentoRegra bean) {
    	removeByIdEnquadramentoRegra(bean.getIdEnquadramentoRegra());    	
    	DomainValue domainOrigem = new DomainValue();
    	domainOrigem.setValue("O");
        storeFiliaisOrigemDestino(filialEnquadramentosOrigem, bean, domainOrigem);
    	DomainValue domainDestino = new DomainValue();
    	domainDestino.setValue("D");
        storeFiliaisOrigemDestino(filialEnquadramentosDestino, bean, domainDestino);    	
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public void storeFiliaisOrigemDestino(List filialEnquadramentos, EnquadramentoRegra bean, DomainValue tipoOrigemDestino) {
		if (filialEnquadramentos != null && filialEnquadramentos.size() > 0) {
 	    	for(Iterator it=filialEnquadramentos.iterator();it.hasNext();){
 	    		Filial filial = (Filial)it.next();
 	    		FilialEnquadramento filialEnquadramento = new FilialEnquadramento();
 	    		filialEnquadramento.setFilial(filial);
 	    		filialEnquadramento.setEnquadramentoRegra(bean);
 	    		filialEnquadramento.setTpInfluenciaMunicipio(tipoOrigemDestino);
 	     		store(filialEnquadramento);
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
    public java.io.Serializable store(FilialEnquadramento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFilialEnquadramentoDAO(FilialEnquadramentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FilialEnquadramentoDAO getFilialEnquadramentoDAO() {
        return (FilialEnquadramentoDAO) getDao();
    }

	public List find(Map criteria) {
    	List orderBy = new ArrayList(1);
    	orderBy.add("filial_.sgFilial");
        return this.getFilialEnquadramentoDAO().findListByCriteria(criteria,orderBy);   
	}

    
   }