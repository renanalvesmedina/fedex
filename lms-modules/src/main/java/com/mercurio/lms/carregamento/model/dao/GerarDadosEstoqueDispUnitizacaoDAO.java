package com.mercurio.lms.carregamento.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.lms.configuracoes.model.ParametroGeral;

/**
 * Classe DAO responsável pelo processo de Geração de Dados de Estoque dos 
 * Dispositivos de Unitização de acordo com o item de número 05.03.01.12
 * @spring.bean
 */
public class GerarDadosEstoqueDispUnitizacaoDAO extends JdbcDaoSupport {

    /**
     * Executa insert->select na tabela de histórico, copiando os 
     * dados de ESTOQUE_DISPOSITIVO_QTDE, com a data do mês indicado
     * @param month
     */
    public void executeCopyEstoqueDispositivoQtdeForMonth(YearMonthDay month) {    	
    	StringBuffer sb = new StringBuffer()
    	.append(" INSERT /*+append*/ INTO ESTOQUE_DISP_QTDE_HIST(")
	    	.append(" ID_ESTOQUE_DISP_QTDE_HIST,")
	    	.append(" ID_TIPO_DISPOSITIVO_UNITIZACAO,")
	    	.append(" ID_EMPRESA,")
	    	.append(" DT_REFERENCIA,")
	    	.append(" QT_ESTOQUE,")
	    	.append(" ID_FILIAL,")
	    	.append(" ID_CONTROLE_CARGA)")
    	.append(" SELECT ")
	    	.append(" ESTOQUE_DISP_QTDE_HIST_SQ.NEXTVAL,")
	    	.append(" EDQ.ID_TIPO_DISPOSITIVO_UNITIZACAO,")
	    	.append(" EDQ.ID_EMPRESA,")
	    	.append(" ?,")
	    	.append(" EDQ.QT_ESTOQUE,")
	    	.append(" EDQ.ID_FILIAL,")
	    	.append(" EDQ.ID_CONTROLE_CARGA")
    	.append(" FROM ESTOQUE_DISPOSITIVO_QTDE EDQ");

    	JdbcTemplate jdbcTemplate = getJdbcTemplate();
    	Object[] parametros = JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, new Object[]{month});    	
    	jdbcTemplate.update(sb.toString(), parametros);
    }

    /**
     * Remove os registros contidos no intervalo de datas
     * @param dtInicial
     * @param dtFinal
     */
    public void removeEstoqueDispQtdeHistBetweenDtInicialFinal(YearMonthDay dtInicial, YearMonthDay dtFinal) {
    	String s = "DELETE FROM ESTOQUE_DISP_QTDE_HIST WHERE DT_REFERENCIA BETWEEN ? AND ?";
    	JdbcTemplate jdbcTemplate = getJdbcTemplate();
    	Object[] parametros = JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, new Object[]{dtInicial, dtFinal});    	
    	jdbcTemplate.update(s, parametros);
    }  
    
    /**
     * Executa insert->select na tabela de histórico, copiando os 
     * dados de ESTOQUE_DISP_IDENTIFICADO, com a data do mês indicado
     * @param month
     */
    public void executeCopyEstoqueDispIdentificadoForMonth(YearMonthDay month) {    	
    	StringBuffer sb = new StringBuffer()
    	.append(" INSERT /*+ append */ INTO ESTOQUE_DISP_IDENT_HIST(")
	    	.append(" ID_ESTOQUE_DISP_IDENT_HIST,")
	    	.append(" ID_CONTROLE_CARGA,")
	    	.append(" ID_DISPOSITIVO_UNITIZACAO,")
	    	.append(" ID_FILIAL,")
	    	.append(" DT_REFERENCIA)")
    	.append(" SELECT ")
	    	.append(" ESTOQUE_DISP_IDENT_HIST_SQ.NEXTVAL,")
	    	.append(" EDI.ID_CONTROLE_CARGA,")
	    	.append(" EDI.ID_DISPOSITIVO_UNITIZACAO,")
	    	.append(" EDI.ID_FILIAL,")
	    	.append(" ?")
    	.append(" FROM ESTOQUE_DISP_IDENTIFICADO EDI");

    	JdbcTemplate jdbcTemplate = getJdbcTemplate();
    	Object[] parametros = JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, new Object[]{month});    	
    	jdbcTemplate.update(sb.toString(), parametros);
    }

    /**
     * Remove os registros contidos no intervalo de datas
     * @param dtInicial
     * @param dtFinal
     */
    public void removeEstoqueDispIdentHistBetweenDtInicialFinal(YearMonthDay dtInicial, YearMonthDay dtFinal) {
    	String s = "DELETE FROM ESTOQUE_DISP_IDENT_HIST WHERE DT_REFERENCIA BETWEEN ? AND ?";
    	JdbcTemplate jdbcTemplate = getJdbcTemplate();
    	Object[] parametros = JodaTimeUtils.jdbcPureParamConverter(jdbcTemplate, new Object[]{dtInicial, dtFinal});    	
    	jdbcTemplate.update(s, parametros);
    }
    
    /**
     * Obtém o parâmetro geral de acordo com o nome do parâmetro
     * @param nomeParametro
     * @return
     */
    public ParametroGeral findParametroGeralByNomeParametro(String nomeParametro) {
    	StringBuffer sb = new StringBuffer()
    	.append("SELECT ")
    	.append(" ID_PARAMETRO_GERAL,")
    	.append(" NR_TAMANHO,") 
    	.append(" NM_PARAMETRO_GERAL,")
    	.append(" TP_FORMATO,")
    	.append(" DS_CONTEUDO")
    	.append(" FROM PARAMETRO_GERAL PG")
    	.append(" WHERE PG.NM_PARAMETRO_GERAL LIKE ?");

	   	Object object = getJdbcTemplate().query(sb.toString(), new Object[]{nomeParametro}, new ResultSetExtractor() {
	   		public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
	   			ParametroGeral parametroGeral = null;
	   			if (rs.next()) {
			   		parametroGeral = new ParametroGeral();
			   		parametroGeral.setIdParametroGeral(Long.valueOf(rs.getLong("ID_PARAMETRO_GERAL")));
			   		parametroGeral.setNrTamanho(Short.valueOf((short)rs.getInt("NR_TAMANHO")));
			   		parametroGeral.setNmParametroGeral(rs.getString("NM_PARAMETRO_GERAL"));
			   		parametroGeral.setTpFormato(new DomainValue(rs.getString("TP_FORMATO")));
			   		parametroGeral.setDsConteudo(rs.getString("DS_CONTEUDO"));
	   			}
		   		return parametroGeral;
	   		}
	   	});   	
   		return (ParametroGeral)object;
    }
    
    /**
     * Atualiza o valor do parâmetro geral
     * @param newValue
     * @param idParametroGeral
     */
    public void updateConteudoParametroGeralByIdParametroGeral(String newValue, Long idParametroGeral) {
    	String s = "UPDATE PARAMETRO_GERAL SET DS_CONTEUDO = ? WHERE ID_PARAMETRO_GERAL = ?";
    	getJdbcTemplate().update(s, new Object[]{newValue, idParametroGeral});
    }
    
}