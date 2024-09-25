package com.mercurio.lms.tabelaprecos.model.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class AtualizaTabelaCustoTNTDAO extends JdbcDaoSupport {

	public String atualizaTabelaCustosTNT(String tipoServico) {

		CallableStatementCreatorFactory csf = new CallableStatementCreatorFactory(
				"{? = call F_ATUALIZA_TABELA_CUSTO_TNT(?)}");

		csf.addParameter(new SqlOutParameter("retorno", Types.VARCHAR));
		csf.addParameter(new SqlParameter("tipo_servico", Types.VARCHAR));

		List<SqlOutParameter> resultado = new ArrayList<SqlOutParameter>();
		resultado.add(new SqlOutParameter("retorno", Types.VARCHAR));

		Map<String, Object> input = new HashMap<String, Object>();
		input.put("tipo_servico", tipoServico);

		@SuppressWarnings("unchecked")
		Map<String, Object> retornoConsulta = getJdbcTemplate().call(csf.newCallableStatementCreator(input), resultado);

		if (retornoConsulta == null) {
			return "";
		}
		return (String) retornoConsulta.get("retorno");
	}

}