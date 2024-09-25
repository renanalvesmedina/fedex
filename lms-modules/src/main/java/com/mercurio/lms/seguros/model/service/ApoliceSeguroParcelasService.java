package com.mercurio.lms.seguros.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.ApoliceSeguroParcela;
import com.mercurio.lms.seguros.model.dao.ApoliceSeguroParcelasDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.apoliceSeguroParcelasService"
 */
public class ApoliceSeguroParcelasService extends CrudService<ApoliceSeguroParcela, Long> {
	
	public void setApoliceSeguroParcelasDAO(ApoliceSeguroParcelasDAO apoliceSeguroParcelasDAO) {
		setDao(apoliceSeguroParcelasDAO);
	}

	public ApoliceSeguroParcelasDAO getApoliceSeguroParcelasDAO() {
		return (ApoliceSeguroParcelasDAO) getDao();
	}
	
	public List findParcelasByIdApoliceSeguro(Long idApoliceSeguro){
		return getApoliceSeguroParcelasDAO().findParcelasByIdApoliceSeguro(idApoliceSeguro);
	}

	public Integer getRowCountParcelasByIdApoliceSeguro(Long idApoliceSeguro){
		return getApoliceSeguroParcelasDAO().getRowCountParcelasByIdApoliceSeguro(idApoliceSeguro);
	}
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ApoliceSeguroParcela bean) {
        return super.store(bean);
    }
    
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
    
    /**
     * LMS-6146 -- Calcula os valores dos premios para a aba de detalhamento com base nos valores da aba parcelas
     * @param listParcelas
     * @return
     */
    public TypedFlatMap findCalculaValorPremio(Long idApoliceSeguro, ItemList listParcelas, boolean isGridClick){
    	BigDecimal valorPremio = new BigDecimal(0);
    	BigDecimal vlPremioVencer = new BigDecimal(0);
    	TypedFlatMap map = new TypedFlatMap(); 
    	List listaParcelas = new ArrayList();
    	
    	// Se vier pelo click na grid da aba listagem, então busca os valores atraves find para buscar as parcelas, caso contrario busca do itemList
    	if(isGridClick){
    		
    		if(idApoliceSeguro != null){
    			listaParcelas = findParcelasByIdApoliceSeguro(idApoliceSeguro);
    			
    			if(listaParcelas != null && !listaParcelas.isEmpty()){
            		for (Object parcelas : listaParcelas) {
            			ApoliceSeguroParcela apoliceParcela = (ApoliceSeguroParcela) parcelas;
            			valorPremio = valorPremio.add(apoliceParcela.getVlParcela());
            			
            			if(JTDateTimeUtils.comparaData(apoliceParcela.getDtVencimento(), JTDateTimeUtils.getDataAtual()) > 0){
            				vlPremioVencer = vlPremioVencer.add(apoliceParcela.getVlParcela());
            			}
            			
            		}
            	}
    			
    		}
    		
    	}else{
    		if(!listParcelas.getItems().isEmpty()){
        		for (Object parcelas : listParcelas.getItems()) {
        			ApoliceSeguroParcela apoliceParcela = (ApoliceSeguroParcela) parcelas;
        			valorPremio = valorPremio.add(apoliceParcela.getVlParcela());
        			
        			if(JTDateTimeUtils.comparaData(apoliceParcela.getDtVencimento(), JTDateTimeUtils.getDataAtual()) > 0){
        				vlPremioVencer = vlPremioVencer.add(apoliceParcela.getVlParcela());
        			}
        			
        		}
        	}
    	}

		map.put("valorPremio", FormatUtils.formatBigDecimalWithPattern(valorPremio, "#,###,###,##0.00"));
		map.put("vlPremioVencer", FormatUtils.formatBigDecimalWithPattern(vlPremioVencer, "#,###,###,##0.00"));
		
		return map;
    }
    
	
}
