package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.municipios.emitirMunicipiosAtendidosFilialService"
 * @spring.property name="reportName" value="com/mercurio/lms/municipios/report/emitirMunicipiosAtendidoFilial.jasper"
 */
 
public class EmitirMunicipiosAtendidosFilialService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
        SqlTemplate sql = getSqlTemplate(tfm);
	    
	     
	    configureFilterSummary(sql,tfm);
	    
	    JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        Map parametersReport = new HashMap(); 
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("dsSimboloMoeda", SessionUtils.getMoedaSessao().getDsSimbolo());
        parametersReport.put("ID_SERVICO", tfm.getLong("servico.idServico"));
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);
        
        return jr;
	}
	
	private void configureFilterSummary(SqlTemplate sql, TypedFlatMap tfm) {
		String nmEmpresa = tfm.getString("empresa.pessoa.nmPessoa");
		String sgFilial = tfm.getString("filial.sgFilial");
		String nmFilial = tfm.getString("filial.pessoa.nmFantasia");
		String dsServico = tfm.getString("servico.dsServico");
		
		if (StringUtils.isNotBlank(nmEmpresa))
        	sql.addFilterSummary("empresa", nmEmpresa);
		if (StringUtils.isNotBlank(sgFilial)) 
        	sql.addFilterSummary("filial", sgFilial.concat(" - ").concat(nmFilial));
		if (StringUtils.isNotBlank(dsServico)) 
        	sql.addFilterSummary("servico", dsServico);
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap map) {
 		 
		SqlTemplate sql = createSqlTemplate();
		  
		Long idFilial = map.getLong("filial.idFilial");
		
		SqlTemplate sqlFilial = new SqlTemplate();
		sqlFilial.addProjection("F.ID_FILIAL","ID_FILIAL");
		sqlFilial.addProjection("(F.SG_FILIAL || ' - ' || P.NM_FANTASIA)","NM_FILIAL");
		sqlFilial.addProjection("(SELECT MAX(HF.TP_FILIAL) FROM HISTORICO_FILIAL HF " +
				"WHERE HF.DT_REAL_OPERACAO_INICIAL <= ? " +
				"AND (HF.DT_REAL_OPERACAO_FINAL >= ? OR HF.DT_REAL_OPERACAO_FINAL IS NULL) " +
				"AND F.ID_FILIAL = HF.ID_FILIAL " +
				")","TP_FILIAL");  
		
		sqlFilial.addProjection("P.TP_IDENTIFICACAO","TP_IDENTIFICACAO");
		sqlFilial.addProjection("P.TP_IDENTIFICACAO","TIPO_ID");
		sqlFilial.addProjection("P.NR_IDENTIFICACAO","NR_IDENTIFICACAO");
		sqlFilial.addProjection("F.NR_INSCRICAO_MUNICIPAL","NR_INSCRICAO_MUNICIPAL");
		
		sqlFilial.addProjection("(SELECT MAX(R.SG_REGIONAL || ' - ' || R.DS_REGIONAL) " +
				"FROM REGIONAL_FILIAL RF, " +
				"REGIONAL R " +
				"WHERE RF.ID_REGIONAL = R.ID_REGIONAL " +
				"AND RF.ID_FILIAL = F.ID_FILIAL " +
				"AND RF.DT_VIGENCIA_INICIAL <= ? " +
				"AND RF.DT_VIGENCIA_FINAL >= ? " +
				")","NM_REGIONAL");
		
		sqlFilial.addProjection("F.NR_CENTRO_CUSTO","NR_CENTRO_CUSTO");
		sqlFilial.addProjection("(FR.SG_FILIAL || NVL2(FR.SG_FILIAL,' - ','') || PR.NM_FANTASIA)","NM_FILIAL_RESPONSAVEL");
		sqlFilial.addProjection("(FAWB.SG_FILIAL || NVL2(FAWB.SG_FILIAL,' - ','') || PAWB.NM_FANTASIA)","NM_FILIAL_RESPONSAVEL_AWB");
		sqlFilial.addProjection("F.NR_PRAZO_COBRANCA","NR_PRAZO_COBRANCA");
		sqlFilial.addProjection("M.DS_SIMBOLO","DS_SIMBOLO");
		sqlFilial.addProjection("M.SG_MOEDA","SG_MOEDA");
		sqlFilial.addProjection("F.VL_CUSTO_REEMBARQUE","VL_CUSTO_REEMBARQUE");
		sqlFilial.addProjection("F.DS_HOMEPAGE","DS_HOMEPAGE");
		sqlFilial.addProjection("P.DS_EMAIL","DS_EMAIL");
		 
		sqlFilial.addProjection("(SELECT INS.NR_INSCRICAO_ESTADUAL " +
				"FROM INSCRICAO_ESTADUAL INS " +
				"WHERE INS.ID_PESSOA = F.ID_FILIAL " +
				"AND INS.TP_SITUACAO = 'A' " +
				"AND INS.BL_INDICADOR_PADRAO = 'S' " +
				")","NR_INSCRICAO_ESTADUAL");

		sqlFilial.addFrom("FILIAL","F");
		sqlFilial.addFrom("PESSOA","P");
		sqlFilial.addFrom("FILIAL","FR");
		sqlFilial.addFrom("PESSOA","PR"); 
		sqlFilial.addFrom("FILIAL","FAWB");
		sqlFilial.addFrom("PESSOA","PAWB");
		sqlFilial.addFrom("MOEDA","M");
		
		sqlFilial.addJoin("F.ID_FILIAL","P.ID_PESSOA");
		sqlFilial.addJoin("F.ID_FILIAL_RESPONSAVEL","FR.ID_FILIAL (+)");
		sqlFilial.addJoin("FR.ID_FILIAL","PR.ID_PESSOA (+)");
		sqlFilial.addJoin("F.ID_FILIAL_RESPONSAVEL_AWB","FAWB.ID_FILIAL (+)");
		sqlFilial.addJoin("FAWB.ID_FILIAL","PAWB.ID_PESSOA (+)");
		sqlFilial.addJoin("F.ID_MOEDA","M.ID_MOEDA (+)");
		
		sqlFilial.addCustomCriteria("F.ID_FILIAL = ?");	
		
		SqlTemplate sqlMunicipio = new SqlTemplate();
		sqlMunicipio.addProjection("EP.ID_PESSOA","ID_PESSOA");
		sqlMunicipio.addProjection(PropertyVarcharI18nProjection.createProjection("TLOG.DS_TIPO_LOGRADOURO_I"),"DS_TIPO_LOGRADOURO");
		sqlMunicipio.addProjection("(EP.DS_ENDERECO || ', ' || EP.NR_ENDERECO || NVL2(EP.DS_COMPLEMENTO,', ' || " +
				"EP.DS_COMPLEMENTO,'') || ' - ' || EP.DS_BAIRRO)","DS_ENDERECO");
		sqlMunicipio.addProjection("M.NM_MUNICIPIO","NM_MUNICIPIO");
		sqlMunicipio.addProjection("UF.NM_UNIDADE_FEDERATIVA","NM_UNIDADE_FEDERATIVA");
		sqlMunicipio.addProjection("UF.SG_UNIDADE_FEDERATIVA","SG_UNIDADE_FEDERATIVA");
		sqlMunicipio.addProjection(PropertyVarcharI18nProjection.createProjection("P.NM_PAIS_I"),"NM_PAIS");
		sqlMunicipio.addProjection("M.NR_CEP","NR_CEP");
		sqlMunicipio.addProjection("(SELECT MAX(TE.NR_TELEFONE) FROM TELEFONE_ENDERECO TE " +
				"WHERE " +
				"    TE.ID_PESSOA = EP.ID_PESSOA " +
				"AND TE.TP_TELEFONE = 'C' " +
				"AND (TP_USO = 'FO' OR TP_USO = 'FF') " +
				")","NR_FONE");
		sqlMunicipio.addProjection("(SELECT MAX(TE.NR_TELEFONE) FROM TELEFONE_ENDERECO TE " +
				"WHERE " +
				"    TE.ID_PESSOA = EP.ID_PESSOA " +
				"AND TE.TP_TELEFONE = 'C' " +
				"AND TP_USO = 'FA' " +
				")","NR_FAX");
		
		sqlMunicipio.addFrom("ENDERECO_PESSOA","EP");
		sqlMunicipio.addFrom("TIPO_ENDERECO_PESSOA","TEP");
		sqlMunicipio.addFrom("TIPO_LOGRADOURO","TLOG");
		sqlMunicipio.addFrom("MUNICIPIO","M");
		sqlMunicipio.addFrom("UNIDADE_FEDERATIVA","UF");
		sqlMunicipio.addFrom("PAIS","P");
		
		sqlMunicipio.addJoin("EP.ID_MUNICIPIO","M.ID_MUNICIPIO");
		sqlMunicipio.addJoin("UF.ID_UNIDADE_FEDERATIVA","M.ID_UNIDADE_FEDERATIVA");
		sqlMunicipio.addJoin("P.ID_PAIS","UF.ID_PAIS");
		sqlMunicipio.addJoin("EP.ID_ENDERECO_PESSOA","TEP.ID_ENDERECO_PESSOA");
		sqlMunicipio.addJoin("EP.ID_TIPO_LOGRADOURO","TLOG.ID_TIPO_LOGRADOURO");
		
		sqlMunicipio.addCustomCriteria("EP.ID_ENDERECO_PESSOA = " +
				"(SELECT MAX(EP2.ID_ENDERECO_PESSOA) FROM ENDERECO_PESSOA EP2 " +
				"WHERE EP2.DT_VIGENCIA_INICIAL <= ? " +
				"AND EP2.DT_VIGENCIA_FINAL >= ? " +
				"AND EP2.ID_PESSOA = ? " +
				")");
		
	 	sqlMunicipio.addCustomCriteria("TEP.TP_ENDERECO = 'COM'");

		sql.addProjection("*");
		sql.addFrom("("+sqlFilial.getSql()+")","V_FILIAL");
		sql.addFrom("("+sqlMunicipio.getSql()+")","V_MUNICIPIO");
		sql.addJoin("V_FILIAL.ID_FILIAL","V_MUNICIPIO.ID_PESSOA (+)");

		sql.addOrderBy("V_FILIAL.NM_FILIAL");
		
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(idFilial);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(idFilial);

	
		return sql;
	}
	
	public JRDataSource executeMunicipioFilial(Long idFilial) throws Exception {
		SqlTemplate sql = createSqlTemplate();

		SqlTemplate countPostosPassagem = new SqlTemplate();
		countPostosPassagem.addProjection("COUNT(PPM.ID_POSTO_PASSAGEM_MUNICIPIO)");
		countPostosPassagem.addFrom("POSTO_PASSAGEM_MUNICIPIO","PPM");
		countPostosPassagem.addJoin("PPM.ID_MUNICIPIO_FILIAL", "MF.ID_MUNICIPIO_FILIAL");
		countPostosPassagem.addCustomCriteria("PPM.DT_VIGENCIA_INICIAL <= ? AND PPM.DT_VIGENCIA_FINAL >= ?");
		
		// os 3 campos seguintes são necessários para a query na tabela Feriados
		sql.addProjection("M.ID_MUNICIPIO","ID_MUNICIPIO");
		sql.addProjection("UF.ID_UNIDADE_FEDERATIVA","ID_UNIDADE_FEDERATIVA");
		sql.addProjection("P.ID_PAIS","ID_PAIS");
		
		sql.addProjection("MF.ID_MUNICIPIO_FILIAL","ID_MUNICIPIO_FILIAL");
		sql.addProjection("M.NM_MUNICIPIO","NM_MUNICIPIO");
		sql.addProjection("UF.NM_UNIDADE_FEDERATIVA","NM_UNIDADE_FEDERATIVA");
		sql.addProjection("UF.SG_UNIDADE_FEDERATIVA","SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("P.NM_PAIS_I"),"NM_PAIS");
		sql.addProjection("M.NR_CEP","NR_CEP");
		sql.addProjection("M.BL_DISTRITO","BL_DISTRITO");
		sql.addProjection("M.NR_POPULACAO","NR_POPULACAO");
		sql.addProjection("MF.NR_DISTANCIA_ASFALTO","NR_DISTANCIA_ASFALTO");
		sql.addProjection("MF.NR_DISTANCIA_CHAO","NR_DISTANCIA_CHAO");
		sql.addProjection("("+countPostosPassagem.getSql()+")","NR_POSTOS_PASSAGEM");
		sql.addFrom("MUNICIPIO_FILIAL","MF");
		sql.addFrom("MUNICIPIO","M");
		sql.addFrom("FILIAL","F");
		sql.addFrom("UNIDADE_FEDERATIVA","UF");
		sql.addFrom("V_PAIS_I","P");
		
		sql.addJoin("M.ID_MUNICIPIO","MF.ID_MUNICIPIO");
		sql.addJoin("F.ID_FILIAL","MF.ID_FILIAL");
		sql.addJoin("UF.ID_UNIDADE_FEDERATIVA","M.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("P.ID_PAIS","UF.ID_PAIS");
		
		sql.addOrderBy("M.NM_MUNICIPIO");

		sql.addCustomCriteria("F.ID_FILIAL = ?");
		
		sql.addCustomCriteria("MF.DT_VIGENCIA_INICIAL <= ? AND MF.DT_VIGENCIA_FINAL >= ?");
	
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(idFilial);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());

		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}
 
	public JRDataSource executeOperacaoServico(Object[] values) throws Exception {
		SqlTemplate sql = createSqlTemplate();
		
		Long idMunicipioFilial = values[0] == null ? null : (Long)values[0];
		Long idServico = values[1] == null ? null : (Long)values[1];
		
		SqlTemplate sqlCliente = new SqlTemplate();
		
		sqlCliente.addProjection("MAX(P.NM_PESSOA)");
		sqlCliente.addFrom("ATENDIMENTO_CLIENTE","AC");
		sqlCliente.addFrom("CLIENTE","C");
		sqlCliente.addFrom("PESSOA","P");
		sqlCliente.addJoin("AC.ID_OPERACAO_SERVICO_LOCALIZA","OSL.ID_OPERACAO_SERVICO_LOCALIZA");
		sqlCliente.addJoin("AC.ID_CLIENTE","C.ID_CLIENTE");
		sqlCliente.addJoin("C.ID_CLIENTE","P.ID_PESSOA");
		sqlCliente.addCustomCriteria("AC.DT_VIGENCIA_INICIAL <= ? AND AC.DT_VIGENCIA_FINAL >= ?");
		
		sql.addProjection("OSL.ID_OPERACAO_SERVICO_LOCALIZA","ID_OPERACAO_SERVICO_LOCALIZA");
		sql.addProjection("OSL.TP_OPERACAO","TP_OPERACAO");
		sql.addProjection("S.ID_SERVICO","ID_SERVICO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("S.DS_SERVICO_I"),"DS_SERVICO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("TLM.DS_TIPO_LOCAL_MUNICIPIO_I"),"DS_TIPO_LOCALIZACAO_MUNICIPIO");
		sql.addProjection("(OSL.NR_TEMPO_COLETA / 60)","NR_TEMPO_COLETA");
		sql.addProjection("(OSL.NR_TEMPO_ENTREGA / 60)","NR_TEMPO_ENTREGA");
		sql.addProjection("(DECODE(OSL.BL_DOMINGO,'S',1,'N',0) + " +
						  " DECODE(OSL.BL_SEGUNDA,'S',1,'N',0) + " +
						  " DECODE(OSL.BL_TERCA,'S',1,'N',0) + " +
						  " DECODE(OSL.BL_QUARTA,'S',1,'N',0) + " +
						  " DECODE(OSL.BL_QUINTA,'S',1,'N',0) + " +
						  " DECODE(OSL.BL_SEXTA,'S',1,'N',0) + " +
						  " DECODE(OSL.BL_SABADO,'S',1,'N',0))","NR_FREQUENCIA");
		sql.addProjection("OSL.BL_DOMINGO","BL_DOMINGO");
		sql.addProjection("OSL.BL_SEGUNDA","BL_SEGUNDA");
		sql.addProjection("OSL.BL_TERCA","BL_TERCA");
		sql.addProjection("OSL.BL_QUARTA","BL_QUARTA");
		sql.addProjection("OSL.BL_QUINTA","BL_QUINTA");
		sql.addProjection("OSL.BL_SEXTA","BL_SEXTA");
		sql.addProjection("OSL.BL_SABADO","BL_SABADO");
		sql.addProjection("("+sqlCliente.getSql()+")","NM_CLIENTE_ESPECIFICO");
		
		sql.addFrom("OPERACAO_SERVICO_LOCALIZA","OSL");
		sql.addFrom("SERVICO","S");
		sql.addFrom("TIPO_LOCALIZACAO_MUNICIPIO","TLM");
		
	 	sql.addJoin("OSL.ID_SERVICO","S.ID_SERVICO (+)");
		sql.addJoin("OSL.ID_TIPO_LOCALIZACAO_MUNICIPIO","TLM.ID_TIPO_LOCALIZACAO_MUNICIPIO");
		
		sql.addCustomCriteria("OSL.DT_VIGENCIA_INICIAL <= ? AND OSL.DT_VIGENCIA_FINAL >= ?");
		sql.addCustomCriteria("OSL.ID_MUNICIPIO_FILIAL = ?");
		if (idServico != null) sql.addCustomCriteria("S.ID_SERVICO = ?");

		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(idMunicipioFilial);
		if (idServico != null) sql.addCriteriaValue(idServico);
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}
	 
	public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("TP_OPERACAO", "DM_TIPO_OPERACAO_COLETA_ENTREGA");
        config.configDomainField("BL_DISTRITO", "DM_SIM_NAO"); 
        config.configDomainField("TP_FILIAL", "DM_TIPO_FILIAL");
        config.configDomainField("TP_FILIAL", "DM_TIPO_FILIAL");
    }
	
	public JRDataSource executeFeriados(Object[] ids) throws Exception {
		SqlTemplate sql = createSqlTemplate();
		
		Long idMunicipio = ids[0] == null ? null : (Long)ids[0];
		Long idUnidadeFederativa = ids[1] == null ? null : (Long)ids[1];
		Long idPais = ids[2] == null ? null : (Long)ids[2];
		
		sql.addProjection("F.DT_FERIADO","DT_FERIADO");
		
		sql.addFrom("FERIADO","F");
				
		// Como há uma lógica or, é necessário um StringBuffer antes de adicionar o criteria.
		StringBuffer orClause = new StringBuffer();
		 
		//pega os feriados mundiais
		orClause.append(" (F.ID_PAIS IS NULL AND F.ID_UNIDADE_FEDERATIVA IS NULL AND F.ID_MUNICIPIO IS NULL)");
		
		if (idPais != null) {
			orClause.append(" OR (F.ID_PAIS = ? AND F.ID_UNIDADE_FEDERATIVA IS NULL AND F.ID_MUNICIPIO IS NULL)");
			sql.addCriteriaValue(idPais);
		}
		if (idUnidadeFederativa != null) {
			if (orClause.length() > 0)
				orClause.append(" OR ");
			orClause.append("(F.ID_UNIDADE_FEDERATIVA = ? AND F.ID_MUNICIPIO IS NULL)");
			sql.addCriteriaValue(idUnidadeFederativa);
		}
		if (idMunicipio != null) {
			if (orClause.length() > 0)
				orClause.append(" OR ");
			orClause.append("F.ID_MUNICIPIO = ?");
			sql.addCriteriaValue(idMunicipio);
		} 
			 
		if (orClause.length() > 0)
			sql.addCustomCriteria("("+orClause.toString()+")");
		
		sql.addCustomCriteria("F.DT_VIGENCIA_INICIAL <= ? AND F.DT_VIGENCIA_FINAL >= ?");
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addOrderBy("to_char(DT_FERIADO,'mm/dd')");
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}
	
	public JRDataSource executeClientes(Long idOperacaoServico) throws Exception {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("PC.NM_PESSOA","NM_CLIENTE_ESPECIFICO");
		
		sql.addFrom("ATENDIMENTO_CLIENTE","AC");
		sql.addFrom("CLIENTE","C");
		sql.addFrom("PESSOA","PC");
		
		sql.addJoin("AC.ID_CLIENTE","C.ID_CLIENTE");
		sql.addJoin("C.ID_CLIENTE","PC.ID_PESSOA");
				
		
		sql.addCustomCriteria("AC.ID_OPERACAO_SERVICO_LOCALIZA = ?");

		sql.addCustomCriteria("AC.DT_VIGENCIA_INICIAL <= ? AND AC.DT_VIGENCIA_FINAL >= ?");
		
		sql.addCriteriaValue(idOperacaoServico);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());

		sql.addOrderBy("PC.NM_PESSOA"); 
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}
}