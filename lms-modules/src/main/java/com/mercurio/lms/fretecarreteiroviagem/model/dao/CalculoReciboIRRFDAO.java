package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.lms.fretecarreteiroviagem.dto.CalculoReciboIRRFDTO;

public class CalculoReciboIRRFDAO extends JdbcDaoSupport {

	private static final String P_MGC_CALC_IMP_RENDA = "{call P_MGC_CALC_IMP_RENDA(?,?,?,?,?,?,?,?,?,?,?,?)}";
	private static final String P_MGC_EST_IMP_RENDA = "{call P_MGC_EST_IMP_RENDA(?,?,?,?,?,?)}";

	/**
	 * Invoca a procedure P_MGC_CALC_IMP_RENDA.
	 * 
	 * @param in
	 * 
	 * @return CalculoReciboIRRFDTO.OUT
	 */
	@SuppressWarnings("rawtypes")
	public CalculoReciboIRRFDTO.OUT executeCalculoReciboIRRFDAO(final CalculoReciboIRRFDTO.IN in) {	
		CalculoReciboIRRFDTO.OUT out = null;
		
		SqlParameter cpf = new SqlParameter("P_CPF", Types.NUMERIC);
		SqlParameter nrDependentes = new SqlParameter("P_NR_DEPENDENTES", Types.NUMERIC);
		SqlParameter data = new SqlParameter("P_DATA", Types.DATE);
		SqlParameter perBaseCarreteiro = new SqlParameter("P_PER_BASE_CARRETEIRO", Types.NUMERIC);
		SqlParameter vlMinimoIRRF = new SqlParameter("P_VALOR_MINIMO_IRRF", Types.NUMERIC);
		SqlParameter vlrFC = new SqlParameter("P_VLR_FC", Types.NUMERIC);
		SqlParameter vlrComplPagto = new SqlParameter("P_VLR_COMPL_PAGTO", Types.NUMERIC);
		SqlParameter irrfCalc = new SqlOutParameter("P_IRRF_CALC", Types.NUMERIC);
		SqlParameter aliqIRRF = new SqlOutParameter("P_ALIQ_IRRF", Types.NUMERIC);
		SqlParameter atualizaIRRF = new SqlParameter("P_ATUALIZA_IRRF", Types.NUMERIC);
		SqlParameter inssFC = new SqlParameter("P_INSS_FC", Types.NUMERIC);
		SqlParameter acumuladoIrrf = new SqlOutParameter("P_ACUM_IRRF", Types.NUMERIC);
		
		List<SqlParameter> inValues = new ArrayList<SqlParameter>();
		inValues.add(cpf);
		inValues.add(nrDependentes);
		inValues.add(data);
		inValues.add(perBaseCarreteiro);
		inValues.add(vlMinimoIRRF);
		inValues.add(vlrFC);
		inValues.add(vlrComplPagto);
		inValues.add(irrfCalc);
		inValues.add(aliqIRRF);
		inValues.add(atualizaIRRF);
		inValues.add(inssFC);
		inValues.add(acumuladoIrrf);

		Map result = getJdbcTemplate().call(new CallableStatementCreator() {	
			@Override
			public CallableStatement createCallableStatement(Connection connection) throws SQLException {	
				CallableStatement callableStatement = connection.prepareCall(P_MGC_CALC_IMP_RENDA);
				callableStatement.setObject(1, Long.valueOf(in.getNrCpf()));
				callableStatement.setObject(2, in.getNrDependentes() != null ? in.getNrDependentes().longValue() : 0L);						
				callableStatement.setObject(3, new java.sql.Date(in.getDhEmissaoRFC().getMillis()));
				callableStatement.setObject(4, in.getVlrPerBaseCarreteiro());
				callableStatement.setObject(5, in.getVlrMinimoIRRF());
				callableStatement.setObject(6, in.getVlrBruto());
				callableStatement.setObject(7, in.getVlrComplPago());
				callableStatement.setObject(10, in.getAtualizaIRRF());
				callableStatement.setObject(11, in.getVlInssRFC());
				
				callableStatement.registerOutParameter(8, Types.NUMERIC);
				callableStatement.registerOutParameter(9, Types.NUMERIC);
				callableStatement.registerOutParameter(12, Types.NUMERIC);
				
				return callableStatement;	
			}
		}, inValues);
		
		if(result != null){			
			out = new CalculoReciboIRRFDTO().new OUT();
			out.setVlAliqIRRF((BigDecimal) result.get("P_ALIQ_IRRF"));
			out.setVlIRRF((BigDecimal) result.get("P_IRRF_CALC"));			
			out.setVlAcumuloIRRF((BigDecimal) result.get("P_ACUM_IRRF"));
			
		}
		
		return out;
	}
	
	/**
	 * Invoca a procedure P_MGC_EST_IMP_RENDA.
	 * 
	 * @param in
	 */
	public void executeEstornoReciboIRRFDAO(final CalculoReciboIRRFDTO.IN in) {		
		SqlParameter cpf = new SqlParameter("P_CPF", Types.NUMERIC);
		SqlParameter data = new SqlParameter("P_DATA", Types.DATE);
		SqlParameter aliqIRRF = new SqlParameter("P_ALIQ_IRRF", Types.NUMERIC);
		SqlParameter irrfCalc = new SqlParameter("P_IRRF_CALC", Types.NUMERIC);
		SqlParameter inssFC = new SqlParameter("P_INSS_FC", Types.NUMERIC);
		SqlParameter vlrFC = new SqlParameter("P_VLR_FC", Types.NUMERIC);
		
		List<SqlParameter> inValues = new ArrayList<SqlParameter>();
		inValues.add(cpf);
		inValues.add(data);
		inValues.add(aliqIRRF);
		inValues.add(irrfCalc);
		inValues.add(inssFC);
		inValues.add(vlrFC);

		getJdbcTemplate().call(new CallableStatementCreator() {	
			@Override
			public CallableStatement createCallableStatement(Connection connection) throws SQLException {	
				CallableStatement callableStatement = connection.prepareCall(P_MGC_EST_IMP_RENDA);
				callableStatement.setObject(1, Long.valueOf(in.getNrCpf()));
				callableStatement.setObject(2, new java.sql.Date(in.getDhEmissaoRFC().getMillis()));				
				callableStatement.setObject(3, in.getPcAliquotaIRRF());				
				callableStatement.setObject(4, in.getVlIRRF());
				callableStatement.setObject(5, in.getVlInssRFC());
				callableStatement.setObject(6, in.getVlrBruto());
				
				return callableStatement;	
			}
		}, inValues);
	}
}