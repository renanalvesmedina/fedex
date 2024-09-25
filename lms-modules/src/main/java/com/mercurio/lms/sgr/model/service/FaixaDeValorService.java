package com.mercurio.lms.sgr.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.sgr.model.FaixaDeValor;
import com.mercurio.lms.sgr.model.dao.FaixaDeValorDAO;
import com.mercurio.lms.util.CompareUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.faixaDeValorService"
 */
public class FaixaDeValorService extends CrudService<FaixaDeValor, Long> {

	private ExigenciaFaixaValorService exigenciaFaixaValorService;

	public ExigenciaFaixaValorService getExigenciaFaixaValorService() {
		return exigenciaFaixaValorService;
	}
	public void setExigenciaFaixaValorService(ExigenciaFaixaValorService exigenciaFaixaValorService) {
		this.exigenciaFaixaValorService = exigenciaFaixaValorService;
	}

	
	
	/**
	 * Recupera uma instância de <code>FaixaDeValor</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public FaixaDeValor findById(java.lang.Long id) {
        return (FaixaDeValor)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	getExigenciaFaixaValorService().removeByIdFaixaDeValor(id);    	
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
    	for(Iterator it=ids.iterator();it.hasNext();){
    		Long idFaixaDeValor = (Long)it.next();
        	getExigenciaFaixaValorService().removeByIdFaixaDeValor(idFaixaDeValor);    	
    	}    	
        super.removeByIds(ids);
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(FaixaDeValor bean) {
       	if (!validaFaixaValores(bean)) {
			throw new BusinessException("LMS-11003");
       	}
    	
        return super.store(bean);
    }
    
	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsFaixaValores(List ids) {
		
    	for(Iterator it=ids.iterator();it.hasNext();){
    		Long id = (Long)it.next();
        	getExigenciaFaixaValorService().removeByIdFaixaDeValor(id);    	
    	}    	
        super.removeByIds(ids);    	
    }

	/**
	 * Apaga uma entidade através do IdEnquadramentoRegra
	 *
	 * @param id indica o enquadramentoRegra
	 */
    public void removeByIdEnquadramentoRegra(java.lang.Long id) {
        Map criteria = new HashMap();
 		criteria.put("enquadramentoRegra.idEnquadramentoRegra", id);		
    	List faixadeValorEnquadramentos = find(criteria);
    	if (faixadeValorEnquadramentos != null && faixadeValorEnquadramentos.size() >0) {
    		List ids = new ArrayList();
    		for (Iterator iterator = faixadeValorEnquadramentos.iterator(); iterator.hasNext();) {
				FaixaDeValor faixaDeValor = (FaixaDeValor) iterator.next();
				ids.add(faixaDeValor.getIdFaixaDeValor());
    	}
    		removeByIdsFaixaValores(ids);    		
    }
    }
    
	/**
     * Valida a faixa de valor 
     * 
     * @param Lista de faixa de valores
     */
     private boolean validaFaixaValores(FaixaDeValor bean) {
        Map criteria = new HashMap();

        BigDecimal valorMinimo = bean.getVlLimiteMinimo();
        BigDecimal valorMaximo = bean.getVlLimiteMaximo();
  		criteria.put("enquadramentoRegra.idEnquadramentoRegra", bean.getEnquadramentoRegra().getIdEnquadramentoRegra());  		
    	List faixadeValorEnquadramentos = find(criteria);
       	for(Iterator it=faixadeValorEnquadramentos.iterator();it.hasNext();){       		
       		FaixaDeValor faixaDeValor = (FaixaDeValor)it.next();
       		if (!faixaDeValor.getIdFaixaDeValor().equals(bean.getIdFaixaDeValor())) {
       			if (valorMaximo == null) {
           			if (CompareUtils.ge(faixaDeValor.getVlLimiteMinimo(), valorMinimo)) {
           				return false;
           			}	
       			}           			
           		if (faixaDeValor.getVlLimiteMaximo() != null) { 
           			if (CompareUtils.le(faixaDeValor.getVlLimiteMinimo(), valorMinimo) && CompareUtils.ge(faixaDeValor.getVlLimiteMaximo(), valorMinimo)) {
           				return false;
           			}
           		}	
       			if (valorMaximo != null && faixaDeValor.getVlLimiteMaximo() != null) { 
           	       	if (CompareUtils.ge(faixaDeValor.getVlLimiteMinimo(), valorMinimo) && CompareUtils.le(faixaDeValor.getVlLimiteMinimo(), valorMaximo)) {
           				return false;           	       		
           	       	}
           	       	if (CompareUtils.ge(faixaDeValor.getVlLimiteMaximo(), valorMaximo) && CompareUtils.le(faixaDeValor.getVlLimiteMinimo(), valorMaximo)) {
           				return false;
           			}
           		}
       			// testa valores infinitos
       			if (faixaDeValor.getVlLimiteMaximo() == null) {
           			if (CompareUtils.le(faixaDeValor.getVlLimiteMinimo(), valorMinimo)) {
           				return false;
           			}
           			if (valorMaximo != null) {
           				if (CompareUtils.le(faixaDeValor.getVlLimiteMinimo(), valorMinimo) || CompareUtils.le(faixaDeValor.getVlLimiteMinimo(), valorMaximo)) {
               				return false;           						
           				}
           			}
       			}
       		} else {
       			bean.setNaturezasImpedidas(faixaDeValor.getNaturezasImpedidas());
       			bean.setExigenciaFaixaValors(faixaDeValor.getExigenciaFaixaValors());
       			getFaixaDeValorDAO().evictFaixaDeValor(faixaDeValor);
       		}
       	}
       	return true;
     }
    
	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFaixaDeValorDAO(FaixaDeValorDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FaixaDeValorDAO getFaixaDeValorDAO() {
        return (FaixaDeValorDAO) getDao();
    }

	/**
	 * Identifica a faixa de valor de um enquadramento de regra.
	 * 
	 * @param idEnquadramentoRegra (required)
	 * @param valor (required)
	 * @param blExclusivaAeroporto
	 * @return
	 */
	public FaixaDeValor validateByEnquadramentoRegraAndValor(
			Long idEnquadramentoRegra, String dsEnquadramentoRegra, BigDecimal valor, Boolean blExclusivaAeroporto) {
		return getFaixaDeValorDAO().findByEnquadramentoRegraAndValor(idEnquadramentoRegra, valor, blExclusivaAeroporto);
    }
    
    
    
    /**
	 * Identifica a faixa de valor de um enquadramento de regra.
	 * 
	 * @param idEnquadramentoRegra (required)
	 * @param valor (required)
	 * @param blExclusivaAeroporto
	 * @return
	 */
	public FaixaDeValor findByEnquadramentoRegraAndValor(Long idEnquadramentoRegra, BigDecimal valor, Boolean blExclusivaAeroporto) {
		return getFaixaDeValorDAO().findByEnquadramentoRegraAndValor(idEnquadramentoRegra, valor, blExclusivaAeroporto);
    }
    
    

    
    /**
     * 
     * @param idEnquadramentoRegra
     * @param valor
     * @return
     */
    public FaixaDeValor findFaixaDeValorByIdEnquadramentoRegraByValor(Long idEnquadramentoRegra, BigDecimal valor) {
    	return getFaixaDeValorDAO().findFaixaDeValorByIdEnquadramentoRegraByValor(idEnquadramentoRegra, valor);
    }
}    