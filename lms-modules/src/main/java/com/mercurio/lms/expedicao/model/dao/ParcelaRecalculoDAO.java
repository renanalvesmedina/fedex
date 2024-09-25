package com.mercurio.lms.expedicao.model.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


/**
 * Classe DAO responsável pelo processo de recalculo de parcelas 
 * 
 * @spring.bean
 */
public class ParcelaRecalculoDAO extends JdbcDaoSupport {

	
	/**
	 * Insere parcelas de recálculo na tabela temporária
	 * 
	 * Tabela utilizada para recálculo PARCELA_RECALCULO
	 * 
	 * @param pds
	 */
	public void storeParcelaRecalculo(Long idDoctoRecalculo, Long idRecalculoFrete, Long idParcelaPreco, BigDecimal vlParcela){
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate();		
    	
		String select = "SELECT 1 FROM PARCELA_RECALCULO WHERE ID_DOCTO_RECALCULO = ? AND ID_PARCELA_PRECO = ? ";
		List list = jdbcTemplate.queryForList(select, new Object[]{idDoctoRecalculo,idParcelaPreco});
		if(list != null && list.isEmpty()){
			String sql = "INSERT INTO PARCELA_RECALCULO (ID_PARCELA_RECALCULO, ID_DOCTO_RECALCULO, ID_RECALCULO_FRETE,ID_PARCELA_PRECO,VL_PARCELA) VALUES (PARCELA_RECALCULO_SQ.nextval, ?,?,?,?) ";
	    	jdbcTemplate.update(sql, new Object[]{idDoctoRecalculo, idRecalculoFrete, idParcelaPreco, vlParcela});				
		}
		
	}
	
	/**
	 * Verifica se o CTRC ja foi calculado
	 * @param idDoctoServico
	 * @return
	 */
	public List findConhecimentoRecalculado(Long idDoctoServico) {
		
		String sql = "SELECT ID_DOCTO_SERVICO FROM PARCELA_RECALCULO PR INNER JOIN DOCTO_RECALCULO DR ON DR.ID_DOCTO_RECALCULO = PR.ID_DOCTO_RECALCULO WHERE DR.ID_DOCTO_SERVICO = ? ";
		
		JdbcTemplate jdbcTemplate = getJdbcTemplate();		
    	
		return jdbcTemplate.queryForList(sql, new Object[]{idDoctoServico});		
	}
	
}
