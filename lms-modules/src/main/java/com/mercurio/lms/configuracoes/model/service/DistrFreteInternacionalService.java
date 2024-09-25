package com.mercurio.lms.configuracoes.model.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.lms.configuracoes.model.DistrFreteInternacional;
import com.mercurio.lms.configuracoes.model.TramoFreteInternacional;
import com.mercurio.lms.configuracoes.model.dao.DistrFreteInternacionalDAO;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.municipios.model.Filial;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.configuracoes.distrFreteInternacionalService"
 */
public class DistrFreteInternacionalService extends CrudService<DistrFreteInternacional, Long> {
	
	/**
	 * Filial origem e Filial destino devem ser diferentes.
	 * @author Robson Edemar Gehl
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-00044") Filial destino e Filial origem são iguais
	 * @param distr
	 */
	public void validarFiliais(DistrFreteInternacional distr){
		Filial filialOrigem = distr.getFilialOrigem();
		Filial filialDestino = distr.getFilialDestino();
		
		if (filialOrigem != null && filialOrigem.equals(filialDestino)){
			throw new BusinessException("LMS-00044");
		}
		
	}
	
	/**
	 * O somatóriod o '% do frete origem' com o '% do frete de destino' deve ser igual a 100%
	 * @author Robson Edemar Gehl
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27017") % de frete origem e destino não somam 100%
	 * @param distr
	 */
	public void validarFreteOrigemDestino(DistrFreteInternacional distr){

		float pcFreteOrigem = distr.getPcFreteOrigem().setScale(2).floatValue();
		float pcFreteDestino = distr.getPcFreteDestino().setScale(2).floatValue();

		if (  !((float)(pcFreteDestino + pcFreteOrigem) == 100.00f )  ){
			throw new BusinessException("LMS-27017");
		}

	}
	
	/**
	 * '% de frete externo' deve estar contido no intervalo de 1-100.
	 * @author Robson Edemar Gehl
	 * @exception com.mercurio.adsm.framework.BusinessException("LMS-27018") % de frete externo não está no intervalo 1-100
	 * @param distr
	 */
	public void validarFreteExterno(DistrFreteInternacional distr){
		float pcFreteExterno = distr.getPcFreteExterno().setScale(2).floatValue();
		if (  pcFreteExterno < 1.00f || pcFreteExterno > 100.00f  ){
			throw new BusinessException("LMS-27018");
		}
	}
	
	/**
	 * Valida total de percentual dos tramos.<BR>
	 * A soma do percentual de todos os Tramos deve fechar em 100%, levando em cosideração o cliente.<BR>
	 * Para cada cliente deve fechar um total de 100%, assim como para os Tramos que não tiverem clientes.<BR>
	 *@author Robson Edemar Gehl
	 *@exception com.mercurio.adsm.framework.BusinessException("LMS-27019") percentual total dos tramos não fechou 100%
	 *@param tramos
	 */
	public void validatePercentualTramos(Iterator tramos){
    	if (tramos != null){
    		/*
    		 * Este map é utilizado para agrupar os Tramos por clientes.
    		 * A 'chave' do Map é o ID do cliente e o 'valor' é o somatório do pcFrete.
    		 * Para os tramos sem clientes, o somatório é armazenado como chave (String) '0'.
    		 */
    		Map totalClientes = new HashMap();
    		
    		//Iteração para agrupar os clientes e realizar o somatório
    		while(tramos.hasNext()){
    		
    			TramoFreteInternacional tramo = (TramoFreteInternacional) tramos.next();
    			
    			//Verifica se já está contido no map
    			if (tramo.getCliente() != null && !totalClientes.containsKey(tramo.getCliente().getIdCliente())){
    				totalClientes.put(tramo.getCliente().getIdCliente(), new Double(0d));
    				
    			//para os Tramos sem clientes, armazena o somatório com chave '0'
    			}else if ( tramo.getCliente() == null && !totalClientes.containsKey("0")){
    				totalClientes.put("0", new Double(0d));
    			}
    			
    			double total = 0.00d;
    			
    			if (tramo.getCliente() != null){
    				//busca total atual para o tramo, por cliente
    				total = ((Double) totalClientes.get(tramo.getCliente().getIdCliente())).doubleValue();	
    			}else{
    				//busca total atual para os tramos sem cliente
    				total = ((Double) totalClientes.get("0")).doubleValue();
    			}
    			
    			//somatório
    			total += tramo.getPcFrete().doubleValue();
    			
    			//Atualiza o somatório
    			if (tramo.getCliente() != null){
    				totalClientes.put(tramo.getCliente().getIdCliente(), new Double(total) );	
    			}else{
    				totalClientes.put("0", new Double(total) );
    			}
    		}
    		
    		/*
    		 * Neste momento é realizada a validação para o total percentual dos Tramos, agrupados por Cliente, ou sem cliente.
    		 * No momento que achar um agrupamento onde o total do frete é diferente que 100d, é criada a Exception.
    		 */
    		Iterator keys = totalClientes.keySet().iterator();
    		while (keys.hasNext()){
    			double total = ((Double) totalClientes.get(keys.next())).doubleValue();
    			if (total != 100d){
    				throw new BusinessException("LMS-27019");		
    			}
    		}
    	}
	}
	
	/**
	 * Recupera uma instância de <code>DistrFreteInternacional</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public DistrFreteInternacional findById(java.lang.Long id) {
        
        DistrFreteInternacional dfi = (DistrFreteInternacional)super.findById(id);
//        dfi.setTramoFreteInternacionais(this.getDistrFreteInternacionalDAO().findTramosFreteInternacionais(dfi.getIdDistrFreteInternacional()));
        
        return dfi;
    }
    
    public Integer getRowCountdTramosFreteInternacionais(Long id){
    	return getDistrFreteInternacionalDAO().getRowCountTramos(id);
    }
    
    public List findTramosFreteInternacionais(Long id){
    	return getDistrFreteInternacionalDAO().findTramosFreteInternacionais(id);
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

    protected DistrFreteInternacional beforeStore(DistrFreteInternacional bean, ItemList itemList, ItemListConfig config) {
        
    	DistrFreteInternacional distr = (DistrFreteInternacional) bean;
        
    	if (itemList != null){
            
    		validatePercentualTramos(itemList.iterator(distr.getIdDistrFreteInternacional(), config));	
    	
        }
        
    	validarFiliais(distr);
    	validarFreteOrigemDestino(distr);
    	validarFreteExterno(distr);
    	
    	return super.beforeStore(bean);
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(DistrFreteInternacional bean, ItemList items, ItemListConfig config) {
    	
		if (items == null || !items.hasItems())
			throw new BusinessException("LMS-27048");

        boolean rollbackMasterId = bean.getIdDistrFreteInternacional() == null;
        
    	try{            
            Long masterId = null;            
            this.beforeStore(bean, items, config);      
            bean = this.getDistrFreteInternacionalDAO().store(bean, items);
            masterId = bean.getIdDistrFreteInternacional();
            return masterId;    
        }catch (RuntimeException re) {
            this.rollbackMasterState(bean, rollbackMasterId, re);
            items.rollbackItemsState();
            throw re;
        }       
        
        
    }

    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDistrFreteInternacionalDAO(DistrFreteInternacionalDAO dao) {
        setDao( dao );
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    public DistrFreteInternacionalDAO getDistrFreteInternacionalDAO() {
        return (DistrFreteInternacionalDAO) getDao();
    }

    public DistrFreteInternacional findByCtoInternacional(CtoInternacional ctoInternacional){
    	return getDistrFreteInternacionalDAO().findByCtoInternacional(ctoInternacional);
    }

}