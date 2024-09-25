package com.mercurio.lms.ppd.model.dao;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.LongUtils;

public class PpdCorporativoDAO extends JdbcDaoSupport  {	
	private SqlTemplate getQueryConhecimento() {
		SqlTemplate sql = new SqlTemplate();					
		
		sql.addProjection("CTOS.UNID_SIGLA_ORIGEM");
		sql.addProjection("CTOS.NUMERO");
		sql.addProjection("CTOS.UNID_SIGLA_DESTINO");
		sql.addProjection("CTOS.VLR_FRETE");
		sql.addProjection("CTOS.NATUREZA_MERCADORIA");
		sql.addProjection("CTOS.CLIB_PESS_ID_REMETENTE");
		sql.addProjection("REME.NOME", "REME_NOME");
		sql.addProjection("CTOS.CLIB_PESS_ID_DESTINATARIO");
		sql.addProjection("DEST.NOME", "DEST_NOME");
		sql.addProjection("CTOS.CLIB_PESS_ID_DEVEDOR");
		sql.addProjection("DEVE.NOME", "DEVE_NOME");
		sql.addProjection("CTOS.CLIB_PESS_ID_CONSIGNATARIO");
		sql.addProjection("CONS.NOME", "CONS_NOME");
		sql.addProjection("TRUNC(CTOS.DTHR_EMISSAO)", "DTHR_EMISSAO");
		sql.addProjection("(SELECT SUM(VLR_TOTAL) FROM NOTAS_FISCAIS_CTOS NOTA " +
				"WHERE NOTA.CTOS_UNID_SIGLA = CTOS.UNID_SIGLA_ORIGEM " +
				"AND NOTA.CTOS_NUMERO = CTOS.NUMERO)","VLR_MERCADORIA");

		sql.addFrom("CONHECIMENTOS CTOS " + 
					"	LEFT JOIN " +
					"	PESSOAS REME " +
					"		ON CTOS.CLIB_PESS_ID_REMETENTE = REME.ID " +
					"	LEFT JOIN " +
					"	PESSOAS DEST " +
					"		ON CTOS.CLIB_PESS_ID_DESTINATARIO = DEST.ID " +
					"	LEFT JOIN " +
					"	PESSOAS DEVE " +
					"		ON CTOS.CLIB_PESS_ID_DEVEDOR = DEVE.ID " +
					"	LEFT JOIN " +
					"	PESSOAS CONS " +
					"		ON CTOS.CLIB_PESS_ID_CONSIGNATARIO = CONS.ID ");
		return sql;
	}
	
	private Map<String,Object> transformMapConhecimento(Map<String,Object> ctrcMapped) {
		Map<String,Object> retorno = new HashMap<String, Object>(); 
		
		BigDecimal numeroCtrc = ((BigDecimal)ctrcMapped.get("NUMERO"));
		BigDecimal numeroCtrcCompleto = ((BigDecimal)ctrcMapped.get("NUMERO"));
		
		if (numeroCtrc.compareTo(new BigDecimal(999999)) > 0) {
			String ctrcParcial = numeroCtrc.toString().substring(numeroCtrc.toString().length()-6);
			numeroCtrc = new BigDecimal(ctrcParcial);
		}
		
		retorno.put("sgFilialOrigem", ctrcMapped.get("UNID_SIGLA_ORIGEM"));
		retorno.put("nrCtrc", numeroCtrc);
		retorno.put("nrCtrcCompleto", numeroCtrcCompleto);
		retorno.put("sgFilialDestino", ctrcMapped.get("UNID_SIGLA_DESTINO"));
		retorno.put("vlFrete", ctrcMapped.get("VLR_FRETE"));
		retorno.put("vlMercadoria", ctrcMapped.get("VLR_MERCADORIA"));
		retorno.put("dsNaturezaProdutoCtrc", ctrcMapped.get("NATUREZA_MERCADORIA"));
		retorno.put("nrCnpjRemetente", ctrcMapped.get("CLIB_PESS_ID_REMETENTE"));
		retorno.put("nmRemetente", ctrcMapped.get("REME_NOME"));
		retorno.put("nrCnpjDestinatario", ctrcMapped.get("CLIB_PESS_ID_DESTINATARIO"));
		retorno.put("nmDestinatario", ctrcMapped.get("DEST_NOME"));
		retorno.put("nrCnpjDevedor", ctrcMapped.get("CLIB_PESS_ID_DEVEDOR"));
		retorno.put("nmDevedor", ctrcMapped.get("DEVE_NOME"));
		retorno.put("nrCnpjConsignatario", ctrcMapped.get("CLIB_PESS_ID_CONSIGNATARIO"));
		retorno.put("nmConsignatario", ctrcMapped.get("CONS_NOME"));
		
		// Converte data para formato joda
		Date data = (Date) ctrcMapped.get("DTHR_EMISSAO");
		YearMonthDay dataJoda = new YearMonthDay(data);			
		retorno.put("dtEmissaoCtrc", dataJoda);
		
		return retorno;
	}
	
	public Map<String,Object> findConhecimento(String unidSiglaOrigem, Long numero, YearMonthDay dtEmissao) {
		SqlTemplate sql = this.getQueryConhecimento();
		
		sql.addCriteria("CTOS.UNID_SIGLA_ORIGEM", "=", unidSiglaOrigem);
		sql.addCriteria("SUBSTR(LPAD(TO_CHAR(CTOS.NUMERO),10,'0'),5,6)", "=", numero);
		sql.addCustomCriteria("TRUNC(CTOS.DTHR_EMISSAO) = TO_DATE('" + dtEmissao + "','YYYY-MM-DD')");
		sql.addOrderBy("DTHR_EMISSAO","DESC");			
				
		List<Map<String,Object>> listaCtrcs = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
		
		if(listaCtrcs.size() == 0) {		
			return null;
		} else {			
			return this.transformMapConhecimento(listaCtrcs.get(0));
		}
	}
	
	private SqlTemplate getQueryRnc() {
		SqlTemplate sql = new SqlTemplate();					
		
		sql.addProjection("UNID_SIGLA");
		sql.addProjection("NUMERO");
		sql.addProjection("DECODE(STATUS,1,'Aberto',2,'Fechado','Desconhecido')","STATUS");
		sql.addProjection("TRUNC(DTHR_EMISSAO)", "DTHR_EMISSAO");
		sql.addProjection("SETOR");
		sql.addProjection("EMISSOR");
		sql.addProjection("TIPO_NAO_CONF");
		sql.addProjection("TIPO_DOCUMENTO");
		sql.addProjection("UNID_SIGLA_ORIG_DOC","UNID_SIGLA_CTRC");
		sql.addProjection("NRO_DOCUMENTO","NUMERO_CTRC");
		sql.addProjection("TRUNC(DATA_EMISSAO_DOCUMENTO)","DATA_EMISSAO_CTRC");
		
		sql.addFrom("RELATORIOS_NAO_CONFORMIDADES");
		return sql;
	}
	
	private Map<String,Object> transformMapRnc(Map<String,Object> rncMapped) {
		Map<String,Object> retorno = new HashMap<String, Object>(); 
		
		retorno.put("sgFilialRnc", rncMapped.get("UNID_SIGLA"));
		retorno.put("nrRnc", ((BigDecimal)rncMapped.get("NUMERO")).longValue());
		retorno.put("dsStatusRnc", rncMapped.get("STATUS"));
		retorno.put("nmSetorRnc", rncMapped.get("SETOR"));
		retorno.put("nmEmissorRnc", rncMapped.get("EMISSOR"));
		retorno.put("tpRnc", rncMapped.get("TIPO_NAO_CONF"));
		retorno.put("tpDocumentoRnc", rncMapped.get("TIPO_DOCUMENTO"));
		retorno.put("ctrcUnidSigla", rncMapped.get("UNID_SIGLA_CTRC"));
		retorno.put("ctrcNumero", ((BigDecimal)rncMapped.get("NUMERO_CTRC")).longValue());
		
		Date dataCtrc = (Date) rncMapped.get("DATA_EMISSAO_CTRC");
		if(dataCtrc != null) {
			YearMonthDay dataCtrcJoda = new YearMonthDay(dataCtrc);
			retorno.put("ctrcDataEmissao", dataCtrcJoda);
		}
		
		// Converte data para formato joda
		Date data = (Date) rncMapped.get("DTHR_EMISSAO");
		if(data != null) {
			YearMonthDay dataJoda = new YearMonthDay(data);			
			retorno.put("dtEmissaoRnc", dataJoda);
		}
		
		return retorno;
	}
	
	public Map<String,Object> findLastRnc(String unidSigla, Long numero) {
		SqlTemplate sql = this.getQueryRnc();
		sql.addCriteria("UNID_SIGLA", "=", unidSigla);
		sql.addCriteria("NUMERO", "=", numero);
		sql.addCriteria("TIPO_DOCUMENTO", "=", 1);
		sql.addOrderBy("DTHR_EMISSAO","DESC");					
		
		List<Map<String,Object>> listaRncs = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
		
		if(listaRncs.size() == 0) {		
			return null;
		} else {			
			return this.transformMapRnc(listaRncs.get(0));
		}
	}
	
	public Map<String,Object> findRnc(String unidSigla, Long numero, YearMonthDay dtEmissao) {
		SqlTemplate sql = this.getQueryRnc();
		sql.addCriteria("UNID_SIGLA", "=", unidSigla);
		sql.addCriteria("NUMERO", "=", numero);
		sql.addCriteria("TIPO_DOCUMENTO", "=", 1);		
		sql.addCustomCriteria("TRUNC(DTHR_EMISSAO) = TO_DATE('" + dtEmissao + "','YYYY-MM-DD')");
		sql.addOrderBy("DTHR_EMISSAO","DESC");	
		
		List<Map<String,Object>> listaRncs = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
		
		if(listaRncs.size() == 0) {		
			return null;
		} else {			
			return this.transformMapRnc(listaRncs.get(0));
		}
	}
	
	public Map<String,Object> findRncByConhecimento(String unidSiglaOrigem, Long numero, YearMonthDay dtEmissao) {
		SqlTemplate sql = this.getQueryRnc();
		sql.addCriteria("UNID_SIGLA_ORIG_DOC", "=", unidSiglaOrigem);
		sql.addCustomCriteria("substr(lpad(NRO_DOCUMENTO, 10, '0'), 5, 6) = substr(lpad(" + numero + ", 10, '0'), 5, 6)");
		sql.addCustomCriteria("DATA_EMISSAO_DOCUMENTO = TO_DATE('" + dtEmissao + "','YYYY-MM-DD')");
		sql.addCriteria("TIPO_DOCUMENTO", "=", 1);
		sql.addOrderBy("DTHR_EMISSAO","DESC");	
		
		List<Map<String,Object>> listaRncs = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
		
		if(listaRncs.size() == 0) {		
			return null;
		} else {			
			return this.transformMapRnc(listaRncs.get(0));
		}
	}
	
	public String findSerieDocumento(String unidOrigemLeg, DateTime dataEmissao, String tipoRetorno, Long nrConhecimento) throws DataAccessException {

		String retorno = null;

		CallableStatementCreatorFactory csf = new CallableStatementCreatorFactory(
				"{? = call F_OPE_SERIE_DOCFIS(?,?,?,?)}");

		csf.addParameter(new SqlOutParameter("result", Types.VARCHAR));
		csf.addParameter(new SqlParameter("unidOrigem", Types.VARCHAR));
		csf.addParameter(new SqlParameter("dataEmissao", Types.DATE));
		csf.addParameter(new SqlParameter("tipoRetorno", Types.VARCHAR));
		csf.addParameter(new SqlParameter("nrConhecimento", Types.NUMERIC));

		// Monta um list com os tipos de dados, para o tratamento de retorno
		List l = new ArrayList();
		l.add(new SqlOutParameter("result", Types.VARCHAR));

		// Monta o map com os parametros a serem pesquisados.
		Map in = new HashMap();
		in.put("unidOrigem", unidOrigemLeg);
		in.put("dataEmissao", new Date(dataEmissao.getMillis()));
		in.put("tipoRetorno", tipoRetorno);
		in.put("nrConhecimento", nrConhecimento);

		Map retornoConsulta = getJdbcTemplate().call(
				csf.newCallableStatementCreator(in), l);

		if (retornoConsulta != null) {
			retorno = (String) retornoConsulta.get("result");
}	
		return retorno;
	}
	
	public Long findIdDoctoServico(String sgFilialOrigem, Long nrDoctoServico) throws DataAccessException {
		Long idDoctoServico = null;
		
		CallableStatementCreatorFactory csf = new CallableStatementCreatorFactory("{? = call F_ID_DOCTO_SERVICO_LMS(?,?)}");
		
		csf.addParameter(new SqlOutParameter("result", Types.NUMERIC));
		csf.addParameter(new SqlParameter("sgFilialOrigem", Types.VARCHAR));
		csf.addParameter(new SqlParameter("nrDoctoServico", Types.NUMERIC));
		
		// Monta um list com os tipos de dados, para o tratamento de retorno
		List list = new ArrayList();
		list.add(new SqlOutParameter("result", Types.NUMERIC));

		// Monta o map com os parametros a serem pesquisados.
		Map in = new HashMap();
		in.put("sgFilialOrigem", sgFilialOrigem);
		in.put("nrDoctoServico", nrDoctoServico);
		
		Map retornoConsulta = getJdbcTemplate().call(csf.newCallableStatementCreator(in), list);

		if (retornoConsulta != null) {
			idDoctoServico = LongUtils.getLong((BigDecimal) retornoConsulta.get("result"));
}	
		
		return idDoctoServico;
	}
}	
