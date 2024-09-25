package com.mercurio.lms.tributos.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Hector junior
 *
 * @spring.bean id="lms.tributos.emitirAliquotasISSService"
 * @spring.property name="reportName" value="com/mercurio/lms/tributos/report/emitirAliquotasISS.jasper"
 */
public class EmitirAliquotasISSService extends ReportServiceSupport {
	
	/** 
	 * Método invocado pela EmitirAliquotasISSAction, é o método default do Struts
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        
        /** Seta o parâmetro de tipo de arquivo a ser gerado */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
		
		/** Adiciona os dados necessários para o relatório no Map */
		
		if(StringUtils.isNotBlank(tfm.getString("tpPeriodicidade"))){
			parametersReport.put("periodicidade", tfm.getString("tpPeriodicidade"));
		}
		
		if(StringUtils.isNotBlank(tfm.getString("dsDiaEntrega1"))){
			parametersReport.put("ds_dia_entrega_1", tfm.getString("dsDiaEntrega1"));
		}
		
		if(StringUtils.isNotBlank(tfm.getString("dsDiaEntrega2"))){
			parametersReport.put("ds_dia_entrega_2", tfm.getString("dsDiaEntrega2"));
		}
		
		if(StringUtils.isNotBlank(tfm.getString("dsDiaEntrega3"))){
			parametersReport.put("ds_dia_entrega_3", tfm.getString("dsDiaEntrega3"));
		}
		
		if(StringUtils.isNotBlank(tfm.getString("dsDiaEntrega1"))){
			parametersReport.put("dia_entrega", tfm.getString("dsDiaEntrega1"));
		}
		
		if(StringUtils.isNotBlank(tfm.getString("dtVigencia"))){
			parametersReport.put("vigencia", tfm.getYearMonthDay("dtVigencia"));
		}
		
		if(StringUtils.isNotBlank(tfm.getString("dtDiaRecolhimento"))){
			parametersReport.put("dt_diaRecolhimento", tfm.getString("dtDiaRecolhimento"));
		}
		
		if(StringUtils.isNotBlank(tfm.getString("tpFormaPagamento"))){
			parametersReport.put("tp_formaPagamento", tfm.getString("tpFormaPagamento"));
		}
		
		jr.setParameters(parametersReport);
		return jr;
	}
	
	/**
	 * Configura variáveis do relatório, para receberem valores não abreviados do domínio 
	 * Ex: situação = I  -  vai ser configurado, e exibido no relatório como Inativo
	 */
	public void configReportDomains(ReportDomainConfig config) {	
		config.configDomainField("PERIODICIDADE","DM_PERIODICIDADE_OBRIGACOES_MUNICIPIO");
		config.configDomainField("FORMA_PAGAMENTO","DM_FORMA_PGTO_ISS");
		config.configDomainField("FORMA_PAG","DM_FORMA_PGTO_ISS");
		config.configDomainField("DISP_LEGAL_TIPO","DM_TIPO_DISPOSITIVO_LEGAL"); 
		config.configDomainField("LOCAL_DEVIDO","DM_LOCAL_DEVIDO_ISS");
		super.configReportDomains(config);
	}

	/** Relatório principal - aliquotasIss */
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("DISTINCT M.ID_MUNICIPIO", "ID_MUNICIPIO");
		sql.addProjection("M.NM_MUNICIPIO", "MUNICIPIO");
		
		sql.addFrom("MUNICIPIO M INNER JOIN ISS_MUNICIPIO_SERVICO IMS " +
				"ON M.ID_MUNICIPIO = IMS.ID_MUNICIPIO " +
			"INNER JOIN ALIQUOTA_ISS_MUNICIPIO_SERV AIMS " +
				"ON IMS.ID_ISS_MUNICIPIO_SERVICO = AIMS.ID_ISS_MUNICIPIO_SERVICO " +
			"LEFT JOIN PARAMETRO_ISS_MUNICIPIO PIM " +
				"ON M.ID_MUNICIPIO = PIM.ID_MUNICIPIO ");
			
		
		/** Resgata o parametro  do request */
		String idMuncipio = tfm.getString("municipio.idMunicipio");
		
		/** Verifica se o parametro não é nulo, caso não seja, adiciona o parametro como critério na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(StringUtils.isNotBlank(idMuncipio)) {
			sql.addCriteria("M.ID_MUNICIPIO", "=", idMuncipio);
			sql.addFilterSummary("municipio", tfm.getString("nomeMunicipio"));
		}
		
		/** Vigência ServicoTributo OU Vigência ServicoAdicional */
		YearMonthDay vigencia = tfm.getYearMonthDay("dtVigencia");
        
        /** Verifica se a variável vigencia é nula para adicioná-la ao critéria e ao filterSummary */
		if(vigencia != null) {
            sql.addFilterSummary("vigencia", JTFormatUtils.format(vigencia));            
            /** Vigência AliquotaIssMunicipioServ */
            sql.addCustomCriteria("(? BETWEEN AIMS.DT_VIGENCIA_INICIAL AND AIMS.DT_VIGENCIA_FINAL)");
            sql.addCriteriaValue(vigencia);            
        }
		
		/** Variável que armazena true se a periodicidade for semanal */
		boolean periodicidadeSemanal = false;
		
		String periodicidade = tfm.getString("tpPeriodicidade");
        String dsPeriodicidade = tfm.getString("dsPeriodicidade");
        
        
        /** Verifica se a variável periodicidade é nula para adicioná-la ao filterSummary */
		if(StringUtils.isNotBlank(periodicidade)) {
			/** Verifica se a periodicidade é semanal */
            if( dsPeriodicidade.equalsIgnoreCase("semanal") ){
				periodicidadeSemanal = true;
			}
			sql.addFilterSummary("periodicidade", dsPeriodicidade);
			
			/** Resgata os parâmetros de dia de entrega*/
			String dsDiaEntrega1 = tfm.getString("dsDiaEntrega1");
			String dsDiaEntrega2 = tfm.getString("dsDiaEntrega2");
			String dsDiaEntrega3 = tfm.getString("dsDiaEntrega3");
			
			/** Verifica se a periodicidade é semanal*/
			if(!periodicidadeSemanal){
				/** Verifica quais campos do "dia's de entrega" serão escritos nos "parâmetros de pesquisa" */
				if(StringUtils.isNotBlank(dsDiaEntrega1) 
						&& StringUtils.isNotBlank(dsDiaEntrega2) 
							&& StringUtils.isNotBlank(dsDiaEntrega3)) {
					sql.addFilterSummary("diaEntrega", dsDiaEntrega1 + " - " + dsDiaEntrega2 + " - " + dsDiaEntrega3);
				}else if(StringUtils.isNotBlank(dsDiaEntrega2)){
					sql.addFilterSummary("diaEntrega", dsDiaEntrega1 + " - " + dsDiaEntrega2);
				}else if(StringUtils.isNotBlank(dsDiaEntrega1)){
					sql.addFilterSummary("diaEntrega", dsDiaEntrega1);
				}
			}else if(StringUtils.isNotBlank(dsDiaEntrega1)){
			        sql.addFilterSummary("diaEntrega", tfm.getString("dsDiaSemana"));
			}
		}
		
		/** Verifica se a variável diaRecolhimento é nula para adicioná-la ao filterSummary */
		String diaRecolhimento = tfm.getString("dtDiaRecolhimento");
		if(StringUtils.isNotBlank(diaRecolhimento)) {
			sql.addFilterSummary("diaRecolhimento", diaRecolhimento);
			sql.addCustomCriteria("((PIM.DT_DIA_RECOLHIMENTO = ? AND IMS.DD_RECOLHIMENTO IS NULL) OR IMS.DD_RECOLHIMENTO = ? )");
			sql.addCriteriaValue(diaRecolhimento);
			sql.addCriteriaValue(diaRecolhimento);
		}
		
		/** Verifica se a variável formaPagamento é nula para adicioná-la ao filterSummary */
		String formaPagamento = tfm.getString("tpFormaPagamento");
		if(StringUtils.isNotBlank(formaPagamento)) {
		    sql.addFilterSummary("formaPagamento", tfm.getString("dsFormaPagamento"));
		    sql.addCustomCriteria("((PIM.TP_FORMA_PAGAMENTO = ? AND IMS.TP_FORMA_PAGAMENTO IS NULL) OR IMS.TP_FORMA_PAGAMENTO = ? )");
			sql.addCriteriaValue(formaPagamento);
			sql.addCriteriaValue(formaPagamento);
		}
		
		sql.addOrderBy("M.NM_MUNICIPIO");
		
		return sql;
	}
	
	/** Subrelatório - aliquotasIssServico */ 
	public JRDataSource executeSubRelatorioAliquotasISSServicos(Object[] parameters) throws Exception {
        SqlTemplate sql = createSqlTemplate();
        
        /** Resgata o id do municipio que é passado por parâmetro do relatório pai */
        Long idMunicipio = null;
        idMunicipio = (Long) parameters[0];
        
        /** Resgata a formaPagamento que é passado por parâmetro do relatório pai */
        String formaPagamento = null;
        formaPagamento = (String) parameters[1];
        
        /** Resgata o dia de recolhimento que é passado por parâmetro do relatório pai */
        String diaRecolhimento = null;
        diaRecolhimento = (String) parameters[2];
        
        /** Resgata a vigencia que é passado por parâmetro do relatório pai */
        YearMonthDay vigencia = (YearMonthDay) parameters[3];
        
        sql.addProjection("NVL(ST.DS_SERVICO_TRIBUTO, " + PropertyVarcharI18nProjection.createProjection("SA.DS_SERVICO_ADICIONAL_I") + ")", "DESCRICAO");
		sql.addProjection("SOT.TP_LOCAL_DEVIDO", "LOCAL_DEVIDO");
		sql.addProjection("SOT.BL_RETENCAO_TOMADOR_SERVICO", "RETENCAO_TOMADOR_SERVICO");
		sql.addProjection("AIMS.DT_VIGENCIA_INICIAL", "VIGENCIA_INICIAL");
		sql.addProjection("AIMS.DT_VIGENCIA_FINAL", "VIGENCIA_FINAL");
		sql.addProjection("AIMS.PC_ALIQUOTA", "ALIQUOTA");
		sql.addProjection("AIMS.PC_EMBUTE", "EMBUTE");
		sql.addProjection("NVL(IMS.DD_RECOLHIMENTO, PIM.DT_DIA_RECOLHIMENTO)", "DIA_REC");
		sql.addProjection("NVL(IMS.TP_FORMA_PAGAMENTO, PIM.TP_FORMA_PAGAMENTO)", "FORMA_PAG");
		
		sql.addFrom("MUNICIPIO M INNER JOIN ISS_MUNICIPIO_SERVICO IMS " +
				"ON M.ID_MUNICIPIO = IMS.ID_MUNICIPIO " +
			"LEFT JOIN SERVICO_TRIBUTO ST " +
				"ON IMS.ID_SERVICO_TRIBUTO = ST.ID_SERVICO_TRIBUTO " +
			"LEFT JOIN SERVICO_ADICIONAL SA " +
				"ON IMS.ID_SERVICO_ADICIONAL = SA.ID_SERVICO_ADICIONAL " +
			"INNER JOIN SERVICO_OFICIAL_TRIBUTO SOT " +
				"ON (ST.ID_SERVICO_OFICIAL_TRIBUTO = SOT.ID_SERVICO_OFICIAL_TRIBUTO OR SA.ID_SERVICO_OFICIAL_TRIBUTO = SOT.ID_SERVICO_OFICIAL_TRIBUTO) " +
			"INNER JOIN ALIQUOTA_ISS_MUNICIPIO_SERV AIMS " +
				"ON IMS.ID_ISS_MUNICIPIO_SERVICO = AIMS.ID_ISS_MUNICIPIO_SERVICO " +
			"LEFT JOIN PARAMETRO_ISS_MUNICIPIO PIM " +
				"ON M.ID_MUNICIPIO = PIM.ID_MUNICIPIO ");
		
		if(idMunicipio   != null && idMunicipio.longValue() > 0) {
			sql.addCriteria("M.ID_MUNICIPIO", "=", idMunicipio, Long.class);
		}
		
		/** Filtra pela forma de pagamento */
		if(formaPagamento != null && StringUtils.isNotBlank(formaPagamento)){
			sql.addCustomCriteria("((PIM.TP_FORMA_PAGAMENTO = ? AND IMS.TP_FORMA_PAGAMENTO IS NULL) OR IMS.TP_FORMA_PAGAMENTO = ? )");
			sql.addCriteriaValue(formaPagamento);
			sql.addCriteriaValue(formaPagamento);
		}
		
		/** Filtra pelo dia de recolhimento */
		if(diaRecolhimento != null && StringUtils.isNotBlank(diaRecolhimento)){
			sql.addCustomCriteria("((PIM.DT_DIA_RECOLHIMENTO = ? AND IMS.DD_RECOLHIMENTO IS NULL) OR IMS.DD_RECOLHIMENTO = ? )");
			sql.addCriteriaValue(diaRecolhimento);
			sql.addCriteriaValue(diaRecolhimento);
		}
		
		/** Vigência AliquotaIssMunicipioServ */
		if(vigencia != null){
			sql.addCustomCriteria("(? BETWEEN AIMS.DT_VIGENCIA_INICIAL AND AIMS.DT_VIGENCIA_FINAL)"); 
	        sql.addCriteriaValue(vigencia);
		}	
		
        sql.addOrderBy(PropertyVarcharI18nProjection.createProjection("SA.DS_SERVICO_ADICIONAL_I"));
        sql.addOrderBy("ST.DS_SERVICO_TRIBUTO");
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
	
	/** Subrelatório - aliquotasIssObrigacoesAcessorias */
	public JRDataSource executeSubRelatorioAliquotasISSObrigAcess(Object[] parameters) throws Exception {
        SqlTemplate sql = createSqlTemplate();
        
        /** Resgata o id do municipio que é passado por parâmetro do relatório pai */
        Long idMunicipio = null;
        idMunicipio = (Long) parameters[0];
        
        /** Resgata a periodicidade que é passado por parâmetro do relatório pai */
        String periodicidade = null;
        periodicidade = (String) parameters[1];
        
        /** Resgata a dsEntrega1 que é passado por parâmetro do relatório pai */
        String dsDiaEntrega1 = null;
        dsDiaEntrega1 = (String) parameters[3];
        
        /** Resgata a dsEntrega2 que é passado por parâmetro do relatório pai */
        String dsDiaEntrega2 = null;
        dsDiaEntrega2 = (String) parameters[4];
        
        /** Resgata a dsEntrega3 que é passado por parâmetro do relatório pai */
        String dsDiaEntrega3 = null;
        dsDiaEntrega3 = (String) parameters[5];
        
        sql.addProjection("OAIM.DS_OBRIGACAO_ACESSORIA_ISS_MUN", "DESCRICAO");
		sql.addProjection("OAIM.TP_PERIODICIDADE", "PERIODICIDADE");
		sql.addProjection("OAIM.DS_DIA_ENTREGA_2", "DIA_ENTREGA_2");
		sql.addProjection("OAIM.DS_DIA_ENTREGA_3", "DIA_ENTREGA_3");
		sql.addProjection("OAIM.OB_OBRIGACAO_ACESSORIA_ISS_MUN", "OBSERVACAO");
		
		sql.addFrom("MUNICIPIO M " +
				    "	INNER JOIN OBRIGACAO_ACESSORIA_ISS_MUN OAIM ON M.ID_MUNICIPIO = OAIM.ID_MUNICIPIO ");
		
		/** Verifica se o idMunicipio não é nulo para adicioná-lo ao critéria */
		if (idMunicipio != null && idMunicipio.longValue() > 0){
			sql.addCriteria("M.ID_MUNICIPIO", "=", idMunicipio);
		}
		
		/** Verifica se a periodicidade é semanal, para buscar o dominio DM_DIAS_SEMANA */
		if(StringUtils.isNotBlank(periodicidade) && periodicidade.equals("S")){
			/** Verifica se a variável periodicidade não é nula para adiciona-la ao critéria */
			sql.addCriteria("OAIM.TP_PERIODICIDADE", "=", periodicidade);
			
			sql.addProjection(PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I", "DIA_ENTREGA_1"));
			
			sql.addProjection(PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I") + " || " +
						         "DECODE(OAIM.DS_DIA_ENTREGA_2, '', '', ' - ') || " +
						         "OAIM.DS_DIA_ENTREGA_2 ||  " +
						         "DECODE(OAIM.DS_DIA_ENTREGA_3, '', '', ' - ') || " +
						         "OAIM.DS_DIA_ENTREGA_3", "DIA_ENTREGA");
			
			sql.addCustomCriteria("(D.NM_DOMINIO = ? AND VD.VL_VALOR_DOMINIO = ?)");
			sql.addCriteriaValue("DM_DIAS_SEMANA");
			sql.addCriteriaValue(dsDiaEntrega1);
			
			sql.addFrom("DOMINIO D INNER JOIN  VALOR_DOMINIO VD " +
				"ON D.ID_DOMINIO = VD.ID_DOMINIO");
			
		/** Verifica se a periodicidade não é nula para */
		}else if(StringUtils.isNotBlank(periodicidade)){
			
			sql.addProjection("OAIM.DS_DIA_ENTREGA_1 || " +
			         "DECODE(OAIM.DS_DIA_ENTREGA_2, '', '', ' - ') || " +
			         "OAIM.DS_DIA_ENTREGA_2 ||  " +
			         "DECODE(OAIM.DS_DIA_ENTREGA_3, '', '', ' - ') || " +
			         "OAIM.DS_DIA_ENTREGA_3", "DIA_ENTREGA");

			/** Verifica se a variável periodicidade não é nula para adiciona-la ao critéria */
			if(periodicidade != null && StringUtils.isNotBlank(periodicidade)) {
				sql.addCriteria("OAIM.TP_PERIODICIDADE", "=", periodicidade);
			}
			
			/** Verifica se a variavel dsDiaEntrega1 é nula para adiciona-la ao criteria */
			if(dsDiaEntrega1 != null && StringUtils.isNotBlank(dsDiaEntrega1)){ 
				sql.addCriteria("OAIM.DS_DIA_ENTREGA_1", "=", dsDiaEntrega1);
			}
			
			/** Verifica se a variável dsDiaEntrega2 é nula para adicioná-la ao critéria */
			if(dsDiaEntrega2 != null && StringUtils.isNotBlank(dsDiaEntrega2)){
				sql.addCriteria("OAIM.DS_DIA_ENTREGA_2", "=", dsDiaEntrega2);
			}
			
			/** Verifica se a variável dsDiaEntrega3 é nula para adicioná-la ao critéria */
			if(dsDiaEntrega3 != null && StringUtils.isNotBlank(dsDiaEntrega3)){
				sql.addCriteria("OAIM.DS_DIA_ENTREGA_3", "=", dsDiaEntrega3);
			}
				
		}else{
			sql.addProjection("OAIM.DS_DIA_ENTREGA_1 || " +
			         "DECODE(OAIM.DS_DIA_ENTREGA_2, '', '', ' - ') || " +
			         "OAIM.DS_DIA_ENTREGA_2 ||  " +
			         "DECODE(OAIM.DS_DIA_ENTREGA_3, '', '', ' - ') || " +
			         "OAIM.DS_DIA_ENTREGA_3", "DIA_ENTREGA");

		}
		
        sql.addOrderBy("OAIM.DS_OBRIGACAO_ACESSORIA_ISS_MUN");
        
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
	
	/** Subrelatório - aliquotasIssParametros */
	public JRDataSource executeSubRelatorioParametros(Object[] parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("DISTINCT PIM.DT_DIA_RECOLHIMENTO", "DIA_RECOLHIMENTO");
		sql.addProjection("PIM.TP_FORMA_PAGAMENTO", "FORMA_PAGAMENTO"); 
		sql.addProjection("PIM.DS_SITE_INTERNET", "SITE");
		sql.addProjection("PIM.TP_DISPOSITIVO_LEGAL", "DISP_LEGAL_TIPO");
		sql.addProjection("PIM.NR_DISPOSITIVO_LEGAL", "DISP_LEGAL_NUM");
		sql.addProjection("TO_CHAR(PIM.DT_ANO_DISPOSITIVO_LEGAL, 'yyyy')", "DISP_LEGAL_ANO");
		sql.addProjection("PIM.BL_PROC_ELETRONICO_LIVRO", "PROC_ELETRONICO");
		sql.addProjection("PIM.BL_EMISSAO_COM_CTRC", "EMIS_CTR");
		sql.addProjection("M.NM_MUNICIPIO");
		
		sql.addFrom("MUNICIPIO M INNER JOIN PARAMETRO_ISS_MUNICIPIO PIM " +
						"ON M.ID_MUNICIPIO = PIM.ID_MUNICIPIO " +
					"INNER JOIN ISS_MUNICIPIO_SERVICO IMS " +
						"ON M.ID_MUNICIPIO = IMS.ID_MUNICIPIO " +
					"LEFT JOIN SERVICO_TRIBUTO ST " +
						"ON IMS.ID_SERVICO_TRIBUTO = ST.ID_SERVICO_TRIBUTO " +
					"LEFT JOIN SERVICO_ADICIONAL SA " +
						"ON IMS.ID_SERVICO_ADICIONAL = SA.ID_SERVICO_ADICIONAL " +
					"INNER JOIN SERVICO_OFICIAL_TRIBUTO SOT " +
						"ON ST.ID_SERVICO_OFICIAL_TRIBUTO = SOT.ID_SERVICO_OFICIAL_TRIBUTO " +
					"INNER JOIN ALIQUOTA_ISS_MUNICIPIO_SERV AIMS " +
						"ON IMS.ID_ISS_MUNICIPIO_SERVICO = AIMS.ID_ISS_MUNICIPIO_SERVICO ");
		
		/** Resgata o id do municipio que é passado por parâmetro do relatório pai */
        Long idMunicipio = null;
        idMunicipio = (Long) parameters[0];
        
        /** Resgata a vigencia que é passado por parâmetro do relatório pai */
        YearMonthDay vigencia = (YearMonthDay) parameters[2];
        
        /** Resgata o dia de recolhimento que é passado por parâmetro do relatório pai */
        String diaRecolhimento = null;
        diaRecolhimento = (String) parameters[6];
        
        /** Resgata a formaPagamento que é passado por parâmetro do relatório pai */
        String formaPagamento = null;
        formaPagamento = (String) parameters[7];
        
		/** Verifica se o parametro não é nulo, caso não seja, adiciona o parametro como critério na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(idMunicipio != null && idMunicipio.longValue() > 0) {
			sql.addCriteria("M.ID_MUNICIPIO", "=", idMunicipio);
		}
		
		/** Verifica se a variável vigencia não é nula para adiciona-la ao critéria s*/
		if(vigencia != null) {
			/** Vigência ServicoTributo OU Vigência ServicoAdicional */
            sql.addCustomCriteria("(" +
                                  "   ( " +
                                  "      (ST.ID_SERVICO_TRIBUTO IS NOT NULL) AND " +
                                  "      (" +
                                  "         ( ? BETWEEN ST.DT_VIGENCIA_INICIAL AND ST.DT_VIGENCIA_FINAL) " +
                                  "      )" +
                                  "   ) OR " +
                                  "   ( " +
                                  "      (SA.ID_SERVICO_ADICIONAL IS NOT NULL) AND " +
                                  "      (" +
                                  "         ( ? BETWEEN SA.DT_VIGENCIA_INICIAL AND SA.DT_VIGENCIA_FINAL) " +
                                  "      )" +
                                  "   )" +
                                  ") ");
			sql.addCriteriaValue(vigencia);
			sql.addCriteriaValue(vigencia);
            
            /** Vigência AliquotaIssMunicipioServ */
            sql.addCustomCriteria("(? BETWEEN AIMS.DT_VIGENCIA_INICIAL AND AIMS.DT_VIGENCIA_FINAL)");
			sql.addCriteriaValue(vigencia);
		}
		
		/** Verifica se a variável diarecolhimento é nula para adicioná-la ao critéria */
		if(diaRecolhimento != null && StringUtils.isNotBlank(diaRecolhimento)) {
			sql.addCriteria("PIM.DT_DIA_RECOLHIMENTO", "=", diaRecolhimento);
		}
		
		/** Verifica se a variável formaPagamento é nula para adicioná-la ao critéria */
		if(formaPagamento != null && StringUtils.isNotBlank(formaPagamento)) {
			sql.addCriteria("PIM.TP_FORMA_PAGAMENTO", "=", formaPagamento);
        }	
		
		sql.addOrderBy("M.NM_MUNICIPIO");
		
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }
	
}
