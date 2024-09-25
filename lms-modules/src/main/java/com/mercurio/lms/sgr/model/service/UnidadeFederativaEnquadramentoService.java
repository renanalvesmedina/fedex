package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.UnidadeFederativaEnquadramento;
import com.mercurio.lms.sgr.model.dao.UnidadeFederativaEnquadramentoDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sgr.unidadeFederativaEnquadramentoService"
 */
public class UnidadeFederativaEnquadramentoService extends CrudService<UnidadeFederativaEnquadramento, Long> {


	/**
	 * Recupera uma inst�ncia de <code>UnidadeFederativaEnquadramento</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public UnidadeFederativaEnquadramento findById(java.lang.Long id) {
        return (UnidadeFederativaEnquadramento)super.findById(id);
    }

	/**
	 * Procura todos os municipios de origem atraves de um enquadramentoRegra
	 *
	 * @param id indica a entidade enquadramentoRegra
	 */
	public List findUnidadesFederativaOrigemById(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);
    	List unidadesFederativaEnquadramentos = find(criteria);
    	List unidadesFederativaOrigem = new ArrayList();
    	for(Iterator it=unidadesFederativaEnquadramentos.iterator();it.hasNext();){
    		UnidadeFederativaEnquadramento unidadeFederativaEnquadramento = (UnidadeFederativaEnquadramento)it.next();
    		if (unidadeFederativaEnquadramento.getTpInfluenciaMunicipio().getValue().equals("O")) {
    			unidadesFederativaOrigem.add(unidadeFederativaEnquadramento.getUnidadeFederativa());
    		}	
    	}
    	
    	return unidadesFederativaOrigem;
	}
	
	/**
	 * Procura todos os municipios de destino atraves de um enquadramentoRegra
	 *
	 * @param id indica a entidade enquadramentoRegra
	 */
	public List findUnidadesFederativaDestinoById(java.lang.Long id) {
    	Map criteria = new HashMap();
        criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);
    	List unidadesFederativaEnquadramentos = find(criteria);
    	List unidadesFederativaDestino = new ArrayList();
    	for(Iterator it=unidadesFederativaEnquadramentos.iterator();it.hasNext();){
    		UnidadeFederativaEnquadramento unidadeFederativaEnquadramento = (UnidadeFederativaEnquadramento)it.next();
    		if (unidadeFederativaEnquadramento.getTpInfluenciaMunicipio().getValue().equals("D")) {
    			unidadesFederativaDestino.add(unidadeFederativaEnquadramento.getUnidadeFederativa());
    		}	
    	}
    	
    	return unidadesFederativaDestino;
	}

	/**
	 * Apaga uma entidade atrav�s do IdEnquadramentoRegra
	 *
	 * @param id indica o enquadramentoRegra
	 */
    public void removeByIdEnquadramentoRegra(java.lang.Long id) {
        Map criteria = new HashMap();
 		criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);		
    	List unidadesFederativaEnquadramentos = find(criteria);
    	if (unidadesFederativaEnquadramentos != null && unidadesFederativaEnquadramentos.size() >0) {
    		List ids = new ArrayList();
    		for (Iterator iterator = unidadesFederativaEnquadramentos.iterator(); iterator.hasNext();) {
				UnidadeFederativaEnquadramento unidadeFederativaEnquadramento = (UnidadeFederativaEnquadramento) iterator.next();
				ids.add(unidadeFederativaEnquadramento.getIdUnidadeFederativaEnquadramento());
    	}
     		removeByIds(ids);    		
    }
    }
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public void storeUnidadeFederativa(List unidadesFederativaEnquadramentosOrigem, List unidadesFederativaEnquadramentosDestino, EnquadramentoRegra bean) {
    	removeByIdEnquadramentoRegra(bean.getIdEnquadramentoRegra());    	
    	DomainValue domainOrigem = new DomainValue();
    	domainOrigem.setValue("O");
    	storeUnidadesFederativaOrigemDestino(unidadesFederativaEnquadramentosOrigem, bean, domainOrigem);
    	DomainValue domainDestino = new DomainValue();
    	domainDestino.setValue("D");
    	storeUnidadesFederativaOrigemDestino(unidadesFederativaEnquadramentosDestino, bean, domainDestino);    	
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public void storeUnidadesFederativaOrigemDestino(List unidadesFederativaEnquadramentos, EnquadramentoRegra bean, DomainValue tipoOrigemDestino) {
		if (unidadesFederativaEnquadramentos != null && unidadesFederativaEnquadramentos.size() > 0) {
 	    	for(Iterator it=unidadesFederativaEnquadramentos.iterator();it.hasNext();){
 	    		UnidadeFederativa unidadeFederativa = (UnidadeFederativa)it.next();
 	    		UnidadeFederativaEnquadramento unidadeFederativaEnquadramento = new UnidadeFederativaEnquadramento();
 	    		unidadeFederativaEnquadramento.setUnidadeFederativa(unidadeFederativa);
 	    		unidadeFederativaEnquadramento.setEnquadramentoRegra(bean);
 	    		unidadeFederativaEnquadramento.setTpInfluenciaMunicipio(tipoOrigemDestino);
 	     		store(unidadeFederativaEnquadramento);
 	    	}
 		}
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
    public java.io.Serializable store(UnidadeFederativaEnquadramento bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setUnidadeFederativaEnquadramentoDAO(UnidadeFederativaEnquadramentoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private UnidadeFederativaEnquadramentoDAO getUnidadeFederativaEnquadramentoDAO() {
        return (UnidadeFederativaEnquadramentoDAO) getDao();
    }
    
	public List find(Map criteria) {
    	List orderBy = new ArrayList(1);
    	orderBy.add("unidadeFederativa_.sgUnidadeFederativa");
        return this.getUnidadeFederativaEnquadramentoDAO().findListByCriteria(criteria,orderBy);   
	}
   
    
   }