package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.expedicao.model.dao.ParcelaRecalculoDAO;


/**
 * Service responsável pelo processo de Geração de Dados de Estoque dos 
 * Dispositivos de Unitização de acordo com o item de número 05.03.01.12
 * @spring.bean id="lms.expedicao.parcelaRecalculoService"
 */
public class ParcelaRecalculoService {

	private ParcelaRecalculoDAO parcelaRecalculoDAO;
	
	/**
	 * Salva uma lista de parcelas recalculadas
	 * @param idDoctoRecalculo 
	 * @param idRecalculoFrete 
	 * 
	 * @param list
	 */
	public void storeParcelasRecalculo(final Long idDoctoRecalculo, Long idRecalculoFrete, final List<Map> parcelas){
		if(CollectionUtils.isNotEmpty(parcelas)){
			for (Map parcela : parcelas) {
				storeParcelasRecalculo(idDoctoRecalculo, idRecalculoFrete, (Long)parcela.get("idParcelaPreco"), (BigDecimal)parcela.get("vlParcela"));
			}
		}
	}
	
	public void storeParcelasRecalculo(final Long idDoctoRecalculo, Long idRecalculoFrete, final Long idParcelaPreco, final BigDecimal vlParcela){
		parcelaRecalculoDAO.storeParcelaRecalculo(idDoctoRecalculo, idRecalculoFrete, idParcelaPreco, vlParcela);
	}

	/**
	 * Verifica se o CTRC ja foi calculado 
	 * 
	 * @param idConhecimento
	 * @return
	 */
	public List findConhecimentoRecalculado(Long idDoctoServico) {
		return parcelaRecalculoDAO.findConhecimentoRecalculado(idDoctoServico);		
	}	
	
	public ParcelaRecalculoDAO getParcelaRecalculoDAO() {
		return parcelaRecalculoDAO;
	}

	public void setParcelaRecalculoDAO(ParcelaRecalculoDAO parcelaRecalculoDAO) {
		this.parcelaRecalculoDAO = parcelaRecalculoDAO;
	}

}
