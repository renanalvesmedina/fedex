package com.mercurio.lms.franqueados.model.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.adsm.framework.util.TypedFlatMap;

public class GeneralidadeFranqueadoJdbcDAO extends JdbcDaoSupport {

	public List<TypedFlatMap> findParcelaParticipacao(YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal, String tpFrete, Long idDoctoServico){
		StringBuilder query = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		
		query.append("SELECT PDS.VL_PARCELA, GF.PC_PARTICIPACAO");
		query.append("  FROM GENERALIDADE_FRQ GF");
		query.append(" INNER JOIN PARCELA_PRECO PP ON (PP.ID_PARCELA_PRECO = GF.ID_PARCELA_PRECO) ");
		query.append(" INNER JOIN PARCELA_DOCTO_SERVICO PDS ON (PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO) ");		
		query.append(" WHERE GF.DT_VIGENCIA_INICIAL <= to_date(?,'yyyy-mm-dd') ");  
		query.append("   AND GF.DT_VIGENCIA_FINAL >= to_date(?, 'yyyy-mm-dd') ");
		query.append("   AND GF.TP_FRETE = ? ");
		query.append("   AND PDS.ID_DOCTO_SERVICO = ? ");
				
		params.add(dataVigenciaInicial.toString());
		params.add(dataVigenciaFinal.toString());
		params.add(tpFrete);
		params.add(idDoctoServico);
		
		
		@SuppressWarnings("unchecked")
		List<TypedFlatMap> result = (List<TypedFlatMap>)getJdbcTemplate().query(query.toString(), params.toArray(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet parcelaParticipacao) throws SQLException, DataAccessException {
				
				ResultSetMetaData metaData = parcelaParticipacao.getMetaData();
				int colCount = metaData.getColumnCount();
				 
				List<TypedFlatMap> result = new ArrayList<TypedFlatMap>(); 

				while(parcelaParticipacao.next()){
					TypedFlatMap flatMap = new TypedFlatMap();
					for (int i = 1; i <= colCount; i++) {
						flatMap.put(metaData.getColumnLabel(i), parcelaParticipacao.getObject(i));
					}
					result.add(flatMap);
				}
					
				return result;
			}
		});
		
		return result;
	}

	public List<TypedFlatMap> findParcelaParticipacao(YearMonthDay dataVigenciaInicial, YearMonthDay dataVigenciaFinal){
		StringBuilder query = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		
		query.append("SELECT PDS.VL_PARCELA, GF.PC_PARTICIPACAO");
		query.append("  FROM GENERALIDADE_FRQ GF");
		query.append(" INNER JOIN PARCELA_PRECO PP ON (PP.ID_PARCELA_PRECO = GF.ID_PARCELA_PRECO) ");
		query.append(" INNER JOIN PARCELA_DOCTO_SERVICO PDS ON (PDS.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO) ");		
		query.append(" WHERE GF.DT_VIGENCIA_INICIAL <= to_date(?,'yyyy-mm-dd') ");  
		query.append("   AND GF.DT_VIGENCIA_FINAL >= to_date(?, 'yyyy-mm-dd') ");
				
		params.add(dataVigenciaInicial.toString());
		params.add(dataVigenciaFinal.toString());
		
		@SuppressWarnings("unchecked")
		List<TypedFlatMap> result = (List<TypedFlatMap>)getJdbcTemplate().query(query.toString(), params.toArray(), new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet parcelaParticipacao) throws SQLException, DataAccessException {
				
				ResultSetMetaData metaData = parcelaParticipacao.getMetaData();
				int colCount = metaData.getColumnCount();
				 
				List<TypedFlatMap> result = new ArrayList<TypedFlatMap>(); 

				while(parcelaParticipacao.next()){
					TypedFlatMap flatMap = new TypedFlatMap();
					for (int i = 1; i <= colCount; i++) {
						flatMap.put(metaData.getColumnLabel(i), parcelaParticipacao.getObject(i));
					}
					result.add(flatMap);
				}
					
				return result;
			}
		});
		
		return result;
	}

}
