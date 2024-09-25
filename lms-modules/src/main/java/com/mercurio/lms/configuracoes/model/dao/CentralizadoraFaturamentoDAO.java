package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.CentralizadoraFaturamento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CentralizadoraFaturamentoDAO extends BaseCrudDao<CentralizadoraFaturamento, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CentralizadoraFaturamento.class;
    }

    protected void initFindPaginatedLazyProperties(Map fetchModes) {
        fetchModes.put("filialByIdFilialCentralizadora", FetchMode.JOIN);
        fetchModes.put("filialByIdFilialCentralizadora.pessoa", FetchMode.JOIN);
        fetchModes.put("filialByIdFilialCentralizada", FetchMode.JOIN);        
        fetchModes.put("filialByIdFilialCentralizada.pessoa", FetchMode.JOIN);        
    }
    
    protected void initFindByIdLazyProperties(Map fetchModes) {
        fetchModes.put("filialByIdFilialCentralizadora", FetchMode.JOIN);
        fetchModes.put("filialByIdFilialCentralizadora.pessoa", FetchMode.JOIN);
        fetchModes.put("filialByIdFilialCentralizada", FetchMode.JOIN);        
        fetchModes.put("filialByIdFilialCentralizada.pessoa", FetchMode.JOIN);        
    }    

    /**
     * Retorna as centralizadoras de faturamento
     * onde já existe a mesma filial de 
     * faturamento como centralizada.
     * 
     * Regra de negócio 3.1 do documento 27.1.1.40
     * */ 
    
    public List findInvalidFilial(CentralizadoraFaturamento bean, int tipo) {
		DetachedCriteria cf = createDetachedCriteria();
		
		cf.add(Expression.and(					
				Expression.eq("tpModal", bean.getTpModal()),
				Expression.eq("tpAbrangencia", bean.getTpAbrangencia())));
		
		if (tipo == 1) {
			/**
			 * Centralizadora já existe como centralizada
			 * */			
			cf.add(Expression.eq("filialByIdFilialCentralizada.idFilial", bean.getFilialByIdFilialCentralizadora().getIdFilial()));		
		}else if (tipo == 2){
			/**
			 * Centralizada já existe como centralizadora
			 * */				
			cf.add(Expression.eq("filialByIdFilialCentralizadora.idFilial", bean.getFilialByIdFilialCentralizada().getIdFilial()));			
		}else{
			/**
			 * Centralizada já existe
			 * */				
			cf.add(Expression.eq("filialByIdFilialCentralizada.idFilial", bean.getFilialByIdFilialCentralizada().getIdFilial()));		
		}	


		if (bean.getIdCentralizadoraFaturamento() != null) {
			cf.add(Expression.not(Expression.eq("idCentralizadoraFaturamento", bean.getIdCentralizadoraFaturamento())));
		}		
    	List list = findByDetachedCriteria(cf);
    	return list;    	
    }    
}