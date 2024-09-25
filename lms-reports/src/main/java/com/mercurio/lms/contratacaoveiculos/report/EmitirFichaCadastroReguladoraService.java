package com.mercurio.lms.contratacaoveiculos.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.contratacaoveiculos.emitirFichaCadastroReguladoraService"
 * @spring.property name="reportName" value="com/mercurio/lms/contratacaoveiculos/report/emitirFichaCadastroReguladora.jasper"
 */
public class EmitirFichaCadastroReguladoraService extends ReportServiceSupport {
	
    private ConfiguracoesFacade configuracoesFacade;
    
    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
   
	public void configReportDomains(ReportDomainConfig config) {
    	config.configDomainField("TP_COR_PELE","DM_COR_PELE");
    	config.configDomainField("TP_COR_CABELO","DM_COR_CABELO");
    	config.configDomainField("TP_COR_OLHOS","DM_COR_OLHOS");
    	config.configDomainField("BL_POSSUI_BARBA","DM_SIM_NAO");
    	config.configDomainField("TP_SEXO","DM_TIPO_SEXO");
    	
    	super.configReportDomains(config);
    }
	
    

	public JRReportDataObject execute(Map criteria) throws Exception {
		
		TypedFlatMap map = (TypedFlatMap)criteria;
		
		Long idSemiReboque = map.getLong("meioTransporteRodoviario3.idMeioTransporte");
		Long idProprietarioSemi = map.getLong("proprietarioSemi.idProprietario");
		
		SqlTemplate sql = createSqlTemplate(map);
				
		Map mapa = getJdbcTemplate().queryForMap(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()));
		
		String tpCorPele = (mapa.get("TP_COR_PELE") != null)?getDomainValueService().findDomainValueDescription("DM_COR_PELE",(String)mapa.get("TP_COR_PELE")):"";
		String tpCorCabelo = (mapa.get("TP_COR_CABELO") != null)?getDomainValueService().findDomainValueDescription("DM_COR_CABELO",(String)mapa.get("TP_COR_CABELO")):"";
		String tpCorOlhos = (mapa.get("TP_COR_OLHOS") != null)?getDomainValueService().findDomainValueDescription("DM_COR_OLHOS",(String)mapa.get("TP_COR_OLHOS")):"";
		String blPossuiBarba = (mapa.get("BL_POSSUI_BARBA") != null)?getDomainValueService().findDomainValueDescription("DM_SIM_NAO",(String)mapa.get("BL_POSSUI_BARBA")):"";
		String tpSexo = (mapa.get("TP_SEXO") != null)?getDomainValueService().findDomainValueDescription("DM_TIPO_SEXO",(String)mapa.get("TP_SEXO")):"";
		
		mapa.put("TP_COR_PELE",tpCorPele);
		mapa.put("TP_COR_CABELO",tpCorCabelo);
		mapa.put("TP_COR_OLHOS",tpCorOlhos);
		mapa.put("BL_POSSUI_BARBA",blPossuiBarba);
		mapa.put("TP_SEXO",tpSexo);
		
		
		if (idSemiReboque == null) {
			mapa.put("NR_IDENTIFICADOR_SEMI", null);
			mapa.put("NR_ANO_FABRICAO_SEMI", null);
			mapa.put("NR_CAPACIDADE_KG_SEMI", null);
			mapa.put("NR_CAPACIDADE_M3_SEMI", null);
			mapa.put("DS_MARCA_MT_SEMI", null);
			mapa.put("DS_MODELO_MT_SEMI", null);
			mapa.put("MUNUF_MT_SEMI", null);
			mapa.put("QT_EIXOS_SEMI", null);
			mapa.put("NR_CHASSI_SEMI", null);
			mapa.put("NR_CERTIFICADO_SEMI", null);
			mapa.put("DT_EMISSAO_MTR_SEMI", null);
			mapa.put("CD_RENAVAM_SEMI", null);
			mapa.put("NR_BILHETE_SEGURO_SEMI", null);
			mapa.put("DT_VENC_SEGURO_SEMI", null);
			mapa.put("TIPO_RODADO_MT_SEMI", null);
			mapa.put("TIPO_CARROCERIA_MT_SEMI", null);
			mapa.put("COR_CABINE_MT_SEMI", null);
			mapa.put("COR_CARROCERIA_MT_SEMI", null);
		}

		if(idProprietarioSemi== null){
			mapa.put("NR_PIS_PROP_SEMI", null);
			mapa.put("NR_IE_PROP_SEMI", null);
			mapa.put("NM_PESSOA_PROP_SEMI", null);
			mapa.put("TP_IDENTIFICACAO_PROP_SEMI", null);
			mapa.put("NR_IDENTIFICACAO_PROP_SEMI", null);
			mapa.put("DS_ENDERECO_PROP_SEMI", null);
			mapa.put("NR_ENDERECO_PROP_SEMI", null);
			mapa.put("DS_COMPLEMENTO_PROP_SEMI", null);
			mapa.put("DS_BAIRRO_PROP_SEMI", null);
			mapa.put("NR_CEP_PROP_SEMI", null);
			mapa.put("SG_UNIDADE_FEDERATIVA_PROP_SEMI", null);
			mapa.put("NM_MUNICIPIO_PROP_SEMI", null);
			mapa.put("SG_PAIS_PROP_SEMI", null);
			mapa.put("NR_DDI_PROP_SEMI", null);
			mapa.put("NR_DDD_PROP_SEMI", null);
			mapa.put("NR_TELEFONE_PROP_SEMI", null);
			mapa.put("NR_CONTA_BANCARIA_SEMI", null);
			mapa.put("NR_BANCO_SEMI", null);
			mapa.put("NM_BANCO_SEMI", null);
			mapa.put("NR_AGENCIA_SEMI", null);
		}
		
		String sim = configuracoesFacade.getMensagem("sim");
		String nao = configuracoesFacade.getMensagem("nao");
		
		mapa.put("FILIAL_ORIGEM",map.getString("filialO.sgFilial") + " - " + map.getString("filialO.pessoa.nmFantasia"));
		mapa.put("FILIAL_DESTINO",map.getString("filialD.sgFilial") + " - " + map.getString("filialD.pessoa.nmFantasia"));
		mapa.put("VALOR",SessionUtils.getMoedaSessao().getSiglaSimbolo() + " " + FormatUtils.formatDecimal("#,###,##0.00",map.getBigDecimal("vlValor")));
		mapa.put("TIPO_CARGA",map.getString("segmentoMercado.dsSegmentoMercado"));

		mapa.put("ROUBO", ((map.getBoolean("blRoubo").booleanValue()) ? sim : nao));
		mapa.put("ROUBO_X",map.getString("qtRoubo"));
		mapa.put("ACIDENTE", ((map.getBoolean("blAcidente").booleanValue()) ? sim : nao));
		mapa.put("ACIDENTE_X",map.getString("qtAcidente"));
		mapa.put("EMPRESA", ((map.getBoolean("blEmpresa").booleanValue()) ? sim : nao));
		mapa.put("EMPRESA_X",map.getString("qtEmpresa"));
		
		ArrayList lista = new ArrayList();
		lista.add(mapa);
		
		Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
      
		
		JRReportDataObject jr = createReportDataObject(new JRMapCollectionDataSource(lista), parametersReport);
		
		jr.setParameters(parametersReport);
		
		return jr;
	}
	
	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) {
		
		SqlTemplate sql = createSqlTemplate();
		
		//filtros
		Long idMotorista = criteria.getLong("motorista.idMotorista");
		Long idProprietario = criteria.getLong("proprietario.idProprietario");
		Long idMeioTransporte = criteria.getLong("meioTransporteRodoviario.idMeioTransporte");
		Long idSemiReboque = criteria.getLong("meioTransporteRodoviario3.idMeioTransporte");
		Long idProprietarioSemi = criteria.getLong("proprietarioSemi.idProprietario");
		
		BigDecimal idCorCabine =(BigDecimal)configuracoesFacade.getValorParametro("ID_ATRIB_MEIO_TRANSPORTE_CABINE");
		BigDecimal idCorCarroceria =(BigDecimal)configuracoesFacade.getValorParametro("ID_ATRIB_MEIO_TRANSPORTE_CARROCERIA");
		BigDecimal idTpCarroceria = (BigDecimal) configuracoesFacade.getValorParametro("ID_ATRIB_MEIO_TRANSPORTE_TP_CARROCERIA");
		
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		
		
		//******************************************* PROJECAO DO MOTORISTA ***************************************
		//PROJECAO DO MOTORISTA	
		sql.addProjection("MOT.ID_MOTORISTA");
		sql.addProjection("MOT.DT_NASCIMENTO");
		
		sql.addProjection("MOT.TP_SEXO");
		sql.addProjection("MOT.NR_CARTEIRA_HABILITACAO");
		sql.addProjection("MOT.NR_PRONTUARIO");
		sql.addProjection("MOT.DS_CLASSE");
		sql.addProjection("MOT.DT_VENCIMENTO_HABILITACAO");
		sql.addProjection("MOT.DT_EMISSAO");
		sql.addProjection("MOT.NM_PAI");
		sql.addProjection("MOT.NM_MAE");
		sql.addProjection("MOT.TP_COR_PELE");
		sql.addProjection("MOT.TP_COR_CABELO");
		sql.addProjection("MOT.TP_COR_OLHOS");
		sql.addProjection("MOT.BL_POSSUI_BARBA");
		sql.addProjection("MOT.NR_CARTEIRA_PROFISSIONAL");
		sql.addProjection("MOT.NR_SERIE_CARTEIRA_PROFISSIONAL");
		sql.addProjection("MOT.DT_EMISSAO_CARTEIRA_PROFISSION");
		sql.addProjection("MOT.NR_CARTEIRA_PROFISSIONAL");
		
		//PROJECAO DE PESSOA - MOTORISTA
		sql.addProjection("PES.NM_PESSOA");
		sql.addProjection("PES.NR_RG");
		sql.addProjection("PES.DS_ORGAO_EMISSOR_RG");
		sql.addProjection("PES.DT_EMISSAO_RG");
		sql.addProjection("PES.TP_IDENTIFICACAO","TP_IDENTIFICACAO_PES");
		sql.addProjection("PES.NR_IDENTIFICACAO","NR_IDENTIFICACAO_PES");
		
		//PROJECAO DO ENDEREÇO DO MOTORISTA
		sql.addProjection("V_ENDERECO.DS_ENDERECO");
		sql.addProjection("V_ENDERECO.NR_ENDERECO");
		sql.addProjection("V_ENDERECO.DS_COMPLEMENTO");
		sql.addProjection("V_ENDERECO.DS_BAIRRO");
		sql.addProjection("V_ENDERECO.NR_CEP");
		sql.addProjection("V_ENDERECO.SG_PAIS_MOT");
		sql.addProjection("V_ENDERECO.NM_MUNICIPIO_MOT");
		sql.addProjection("V_ENDERECO.UF_MOT");
		sql.addProjection("V_ENDERECO.DS_TIPO_LOGRADOURO");
		
		//PROJECAO DO LOCAL DE EMISSAO DA IDENTIDADE DO MOTORISTA
		sql.addProjection("MUN_IDENT.NM_MUNICIPIO");
		
		//PROJECAO DO TELEFONE DO MOTORISTA
		sql.addProjection("V_TELEFONE.NR_DDI");
		sql.addProjection("V_TELEFONE.NR_DDD");
		sql.addProjection("V_TELEFONE.NR_TELEFONE");
		
		//PROJECAO DA NATURALIDADE DO MOTORISTA
		sql.addProjection("NVL2(MUN_NAT.NM_MUNICIPIO, MUN_NAT.NM_MUNICIPIO || '/' || UF_NAT.SG_UNIDADE_FEDERATIVA, NULL)","NATURALIDADE");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PA_NAT.NM_PAIS_I"), "NM_PAIS");
		
		
		sql.addProjection("UF_CNH.SG_UNIDADE_FEDERATIVA","SG_UNIDADE_FEDERATIVA_CNH");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("PAIS_CNH.NM_PAIS_I"),"NM_PAIS_CNH");
		
		
		//********************************************* PROJECAO DO PROPRIETARIO **********************************
		
		//PROJECAO DOS DADOS DO PROPRIETARIO
		sql.addProjection("PROP.NR_PIS","NR_PIS_PROP");
		
		//PROJECAO DE PESSOA - PROPRIETARIO
		sql.addProjection("PES_PROP.NM_PESSOA","NM_PESSOA_PROP");
		sql.addProjection("PES_PROP.TP_IDENTIFICACAO","TP_IDENTIFICACAO_PROP");
		sql.addProjection("PES_PROP.NR_IDENTIFICACAO","NR_IDENTIFICACAO_PROP");
		
		//PROJECAO DE ENDERECO DE PROPRIETARIO
		sql.addProjection("V_ENDERECO_PROP.DS_ENDERECO","DS_ENDERECO_PROP");
		sql.addProjection("V_ENDERECO_PROP.NR_ENDERECO","NR_ENDERECO_PROP");
		sql.addProjection("V_ENDERECO_PROP.DS_COMPLEMENTO","DS_COMPLEMENTO_PROP");
		sql.addProjection("V_ENDERECO_PROP.DS_BAIRRO","DS_BAIRRO_PROP");
		sql.addProjection("V_ENDERECO_PROP.NR_CEP","NR_CEP_PROP");
		sql.addProjection("V_ENDERECO_PROP.DS_TIPO_LOGRADOURO","DS_TIPO_LOGRADOURO_PROP");
		sql.addProjection("V_ENDERECO_PROP.SG_UNIDADE_FEDERATIVA_PROP");
		sql.addProjection("V_ENDERECO_PROP.NM_MUNICIPIO_PROP");
		sql.addProjection("V_ENDERECO_PROP.SG_PAIS_PROP");
		
		//PROJECAO DE TELEFONE DE PROPRIETARIO
		sql.addProjection("V_TELEFONE_PROP.NR_DDI","NR_DDI_PROP"); 
		sql.addProjection("V_TELEFONE_PROP.NR_DDD","NR_DDD_PROP");
		sql.addProjection("V_TELEFONE_PROP.NR_TELEFONE","NR_TELEFONE_PROP");
		
		//PROJECAO DOS DADOS BANCARIOS DE PROPRIETARIO
		sql.addProjection("V_DADOS_BANCARIOS_PROP.NR_CONTA_BANCARIA");
		sql.addProjection("V_DADOS_BANCARIOS_PROP.NR_BANCO");
		sql.addProjection("V_DADOS_BANCARIOS_PROP.NM_BANCO");
		sql.addProjection("V_DADOS_BANCARIOS_PROP.NR_AGENCIA");
		
		sql.addProjection(new StringBuffer()
				.append("(SELECT MAX(IE.NR_INSCRICAO_ESTADUAL) FROM INSCRICAO_ESTADUAL IE")
				.append(" WHERE IE.ID_PESSOA = PROP.ID_PROPRIETARIO")
				.append("   AND IE.TP_SITUACAO = ?) AS NR_IE_PROP")
				.toString());
		sql.addCriteriaValue("A");
		
//********************************************* PROJECAO DO PROPRIETARIO SEMI REBOQUE **********************************
		if(idProprietarioSemi!= null){
			//PROJECAO DOS DADOS DO PROPRIETARIO SEMI REBOQUE
			sql.addProjection("PROP_SEMI.NR_PIS","NR_PIS_PROP_SEMI");
			
			//PROJECAO DE PESSOA - PROPRIETARIO SEMI REBOQUE
			sql.addProjection("PES_PROP_SEMI.NM_PESSOA","NM_PESSOA_PROP_SEMI");
			sql.addProjection("PES_PROP_SEMI.TP_IDENTIFICACAO","TP_IDENTIFICACAO_PROP_SEMI");
			sql.addProjection("PES_PROP_SEMI.NR_IDENTIFICACAO","NR_IDENTIFICACAO_PROP_SEMI");
			
			//PROJECAO DE ENDERECO DE PROPRIETARIO SEMI REBOQUE
			sql.addProjection("V_ENDERECO_PROP_SEMI.DS_ENDERECO","DS_ENDERECO_PROP_SEMI");
			sql.addProjection("V_ENDERECO_PROP_SEMI.NR_ENDERECO","NR_ENDERECO_PROP_SEMI");
			sql.addProjection("V_ENDERECO_PROP_SEMI.DS_COMPLEMENTO","DS_COMPLEMENTO_PROP_SEMI");
			sql.addProjection("V_ENDERECO_PROP_SEMI.DS_BAIRRO","DS_BAIRRO_PROP_SEMI");
			sql.addProjection("V_ENDERECO_PROP_SEMI.NR_CEP","NR_CEP_PROP_SEMI");
			sql.addProjection("V_ENDERECO_PROP_SEMI.DS_TIPO_LOGRADOURO","DS_TIPO_LOGRADOURO_PROP_SEMI");
			sql.addProjection("V_ENDERECO_PROP_SEMI.SG_UNIDADE_FEDERATIVA_PROP","SG_UNIDADE_FEDERATIVA_SEMI");
			sql.addProjection("V_ENDERECO_PROP_SEMI.NM_MUNICIPIO_PROP","NM_MUNICIPIO_PROP_SEMI");
			sql.addProjection("V_ENDERECO_PROP_SEMI.SG_PAIS_PROP","SG_PAIS_PROP_SEMI");
			
			//PROJECAO DE TELEFONE DE PROPRIETARIO SEMI REBOQUE
			sql.addProjection("V_TELEFONE_PROP_SEMI.NR_DDI","NR_DDI_PROP_SEMI"); 
			sql.addProjection("V_TELEFONE_PROP_SEMI.NR_DDD","NR_DDD_PROP_SEMI");
			sql.addProjection("V_TELEFONE_PROP_SEMI.NR_TELEFONE","NR_TELEFONE_PROP_SEMI");
			
			//PROJECAO DOS DADOS BANCARIOS DE PROPRIETARIO SEMI REBOQUE
			sql.addProjection("V_DADOS_BANCARIOS_PROP_SEMI.NR_CONTA_BANCARIA","NR_CONTA_BANCARIA_SEMI");
			sql.addProjection("V_DADOS_BANCARIOS_PROP_SEMI.NR_BANCO","NR_BANCO_SEMI");
			sql.addProjection("V_DADOS_BANCARIOS_PROP_SEMI.NM_BANCO","NM_BANCO_SEMI");
			sql.addProjection("V_DADOS_BANCARIOS_PROP_SEMI.NR_AGENCIA","NR_AGENCIA_SEMI");
			
			sql.addProjection(new StringBuffer()
					.append("(SELECT MAX(IE.NR_INSCRICAO_ESTADUAL) FROM INSCRICAO_ESTADUAL IE")
					.append(" WHERE IE.ID_PESSOA = PROP_SEMI.ID_PROPRIETARIO")
					.append("   AND IE.TP_SITUACAO = ?) AS NR_IE_PROP_SEMI")
					.toString());
			sql.addCriteriaValue("A");		
		}
		//***************************************** PROJECAO DO MEIO DE TRANSPORTE *********************************
		//PROJECAO DE MEIO DE TRANSPORTE
		sql.addProjection("MT.NR_IDENTIFICADOR");
		sql.addProjection("MT.NR_ANO_FABRICAO");
		sql.addProjection("MT.NR_CAPACIDADE_KG");
		sql.addProjection("MT.NR_CAPACIDADE_M3");
		
		//PROJECAO DA MARCA DO MEIO DE TRANSPORTE
		sql.addProjection("MARCAMT.DS_MARCA_MEIO_TRANSPORTE");
		
        //PROJECAO DO MODELO DO MEIO DE TRANSPORTE
		sql.addProjection("MODELOMT.DS_MODELO_MEIO_TRANSPORTE");
		
		//PROJECAO DO MUNICIPIO DO MEIO DE TRANSPORTE
		sql.addProjection("NVL2(MUNMT.NM_MUNICIPIO, MUNMT.NM_MUNICIPIO || '/' || UFMT.SG_UNIDADE_FEDERATIVA, NULL)","MUNUF_MEIO_TRANSP");
		 
		//PROJECAO DO MEIO DE TRANSPORTE RODOVIARIO
		sql.addProjection("EIXOS.QT_EIXOS");
		sql.addProjection("MTR.NR_CHASSI");
		sql.addProjection("MTR.NR_CERTIFICADO");
		sql.addProjection("MTR.DT_EMISSAO","DT_EMISSAO_MTR");
		sql.addProjection("MTR.CD_RENAVAM");
		sql.addProjection("MTR.NR_BILHETE_SEGURO");
		sql.addProjection("MTR.DT_VENCIMENTO_SEGURO");
		
			
		//NOVA PROJECAO DO TIPO DE VEICULO
		sql.addProjection("TIPO_MT.DS_TIPO_MEIO_TRANSPORTE", "TIPO_RODADO_MT");
		
		
		//PROJECAO DO TIPO DE CARROCERIA DO MEIO DE TRANSPORTE
		sql.addProjection("(SELECT MTCA.DS_CONTEUDO " +
				"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA, " +
				"MODELO_MEIO_TRANSP_ATRIBUTO MMTA, " + 
			    "CONTEUDO_ATRIBUTO_MODELO CAM " +
			    "WHERE 	 MTCA.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE " +
			    "AND MTCA.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
			    "AND MMTA.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
			    "AND CAM.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"TIPO_CARROCERIA_DSCONTEUDO_MT");
       //PROJECAO DO TIPO DE CARROCERIA DO MEIO DE TRANSPORTE VARCHARI18N
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("(SELECT CAM.DS_CONTEUDO_ATRIBUTO_MODELO_I " +
				"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA, " +
				"MODELO_MEIO_TRANSP_ATRIBUTO MMTA, " + 
			    "CONTEUDO_ATRIBUTO_MODELO CAM " +
			    "WHERE 	 MTCA.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE " +
			    "AND MTCA.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
			    "AND MMTA.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
			    "AND CAM.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"TIPO_CARROCERIA_MT"));
		
		
        //PROJECAO DA COR DA CABINE DO MEIO DE TRANSPORTE
		sql.addProjection("(SELECT  MTCA.DS_CONTEUDO " +
				"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA, " +
				"MODELO_MEIO_TRANSP_ATRIBUTO MMTA, " + 
			    "CONTEUDO_ATRIBUTO_MODELO CAM " +
			    "WHERE 	 MTCA.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE " +
			    "AND MTCA.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
			    "AND MMTA.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
			    "AND CAM.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"COR_CABINE_DSCONTEUDO_MT");
		
        //PROJECAO DA COR DA CABINE DO MEIO DE TRANSPORTE VARCHARI18N
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("(SELECT  CAM.DS_CONTEUDO_ATRIBUTO_MODELO_I " +
				"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA, " +
				"MODELO_MEIO_TRANSP_ATRIBUTO MMTA, " + 
			    "CONTEUDO_ATRIBUTO_MODELO CAM " +
			    "WHERE 	 MTCA.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE " +
			    "AND MTCA.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
			    "AND MMTA.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
			    "AND CAM.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"COR_CABINE_MT"));
		
		//PROJECAO DA COR DA CARROCERIA DO MEIO DE TRANSPORTE
		sql.addProjection("(SELECT  MTCA.DS_CONTEUDO " +
				"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA, " +
				"MODELO_MEIO_TRANSP_ATRIBUTO MMTA, " + 
			    "CONTEUDO_ATRIBUTO_MODELO CAM " +
			    "WHERE 	 MTCA.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE " +
			    "AND MTCA.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
			    "AND MMTA.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
			    "AND CAM.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"COR_CARROCERIA_DSCONTEUDO_MT");
		
       //PROJECAO DA COR DA CARROCERIA DO MEIO DE TRANSPORTE VARCHARI18N
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("(SELECT CAM.DS_CONTEUDO_ATRIBUTO_MODELO_I " +
				"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA, " +
				"MODELO_MEIO_TRANSP_ATRIBUTO MMTA, " + 
			    "CONTEUDO_ATRIBUTO_MODELO CAM " +
			    "WHERE 	 MTCA.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE " +
			    "AND MTCA.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
			    "AND MMTA.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
			    "AND CAM.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"COR_CARROCERIA_MT"));
		
		//*****************************************PROJECAO DO SEMI-REBOQUE *****************************************
		if(idSemiReboque != null){
			//PROJECAO DO SEMI-REBOQUE
			sql.addProjection("MT_SEMI.NR_IDENTIFICADOR","NR_IDENTIFICADOR_SEMI");
			sql.addProjection("MT_SEMI.NR_ANO_FABRICAO","NR_ANO_FABRICAO_SEMI");
			sql.addProjection("MT_SEMI.NR_CAPACIDADE_KG","NR_CAPACIDADE_KG_SEMI");
			sql.addProjection("MT_SEMI.NR_CAPACIDADE_M3","NR_CAPACIDADE_M3_SEMI");
			
	        //PROJECAO DA MARCA DO SEMI-REBOQUE
			sql.addProjection("MARCAMT_SEMI.DS_MARCA_MEIO_TRANSPORTE","DS_MARCA_MT_SEMI");
			
	        //PROJECAO DO MODELO DO SEMI-REBOQUE
			sql.addProjection("MODELOMT_SEMI.DS_MODELO_MEIO_TRANSPORTE","DS_MODELO_MT_SEMI");
			
			 //PROJECAO DO MUNICIPIO DO SEMI-REBOQUE
			sql.addProjection("NVL2(MUNMT_SEMI.NM_MUNICIPIO, MUNMT_SEMI.NM_MUNICIPIO || '/' || UFMT_SEMI.SG_UNIDADE_FEDERATIVA, NULL)","MUNUF_MT_SEMI");
			
		     //PROJECAO DO SEMI-REBOQUE RODOVIARIO
			sql.addProjection("EIXOS_SEMI.QT_EIXOS","QT_EIXOS_SEMI");
			sql.addProjection("MTR_SEMI.NR_CHASSI","NR_CHASSI_SEMI");
			sql.addProjection("MTR_SEMI.NR_CERTIFICADO","NR_CERTIFICADO_SEMI");
			sql.addProjection("MTR_SEMI.DT_EMISSAO","DT_EMISSAO_MTR_SEMI");
			sql.addProjection("MTR_SEMI.CD_RENAVAM","CD_RENAVAM_SEMI");
			sql.addProjection("MTR_SEMI.NR_BILHETE_SEGURO","NR_BILHETE_SEGURO_SEMI");
			sql.addProjection("MTR_SEMI.DT_VENCIMENTO_SEGURO","DT_VENC_SEGURO_SEMI");
		
	       
				
			//NOVA PROJECAO DO TIPO DE VEICULO
			sql.addProjection("TIPO_MT_SEMI.DS_TIPO_MEIO_TRANSPORTE", "TIPO_RODADO_MT_SEMI");
			
	       //PROJECAO DO TIPO DE CARROCERIA DO SEMI-REBOQUE
			sql.addProjection("(SELECT  MTCA_SEMI.DS_CONTEUDO " +
					"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA_SEMI, " +
					"MODELO_MEIO_TRANSP_ATRIBUTO MMTA_SEMI, " + 
				    "CONTEUDO_ATRIBUTO_MODELO CAM_SEMI " +
				    "WHERE 	 MTCA_SEMI.ID_MEIO_TRANSPORTE = MT_SEMI.ID_MEIO_TRANSPORTE " +
				    "AND MTCA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
				    "AND MMTA_SEMI.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
				    "AND CAM_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"TIPO_CARROCERIA_DSCONT_SEMI");
			//PROJECAO DO TIPO DE CARROCERIA DO SEMI-REBOQUE VARCHARI18N
			sql.addProjection(PropertyVarcharI18nProjection.createProjection("(SELECT CAM_SEMI.DS_CONTEUDO_ATRIBUTO_MODELO_I " +
					"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA_SEMI, " +
					"MODELO_MEIO_TRANSP_ATRIBUTO MMTA_SEMI, " + 
				    "CONTEUDO_ATRIBUTO_MODELO CAM_SEMI " +
				    "WHERE 	 MTCA_SEMI.ID_MEIO_TRANSPORTE = MT_SEMI.ID_MEIO_TRANSPORTE " +
				    "AND MTCA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
				    "AND MMTA_SEMI.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
				    "AND CAM_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"TIPO_CARROCERIA_MT_SEMI"));

	        //PROJECAO DA COR DA CABINE DO SEMI-REBOQUE
			sql.addProjection("(SELECT  MTCA_SEMI.DS_CONTEUDO " +
					"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA_SEMI, " +
					"MODELO_MEIO_TRANSP_ATRIBUTO MMTA_SEMI, " + 
				    "CONTEUDO_ATRIBUTO_MODELO CAM_SEMI " +
				    "WHERE 	 MTCA_SEMI.ID_MEIO_TRANSPORTE = MT_SEMI.ID_MEIO_TRANSPORTE " +
				    "AND MTCA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
				    "AND MMTA_SEMI.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
				    "AND CAM_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"COR_CABINE_DSCONT_SEMI");
			//PROJECAO DA COR DA CABINE DO SEMI-REBOQUE VARCHARI18N
			sql.addProjection(PropertyVarcharI18nProjection.createProjection("(SELECT  CAM_SEMI.DS_CONTEUDO_ATRIBUTO_MODELO_I " +
					"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA_SEMI, " +
					"MODELO_MEIO_TRANSP_ATRIBUTO MMTA_SEMI, " + 
				    "CONTEUDO_ATRIBUTO_MODELO CAM_SEMI " +
				    "WHERE 	 MTCA_SEMI.ID_MEIO_TRANSPORTE = MT_SEMI.ID_MEIO_TRANSPORTE " +
				    "AND MTCA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
				    "AND MMTA_SEMI.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
				    "AND CAM_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"COR_CABINE_MT_SEMI"));
			
			//PROJECAO DA COR DA CARROCERIA DO SEMI-REBOQUE
			sql.addProjection("(SELECT  MTCA_SEMI.DS_CONTEUDO " +
					"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA_SEMI, " +
					"MODELO_MEIO_TRANSP_ATRIBUTO MMTA_SEMI, " + 
				    "CONTEUDO_ATRIBUTO_MODELO CAM_SEMI " +
				    "WHERE 	 MTCA_SEMI.ID_MEIO_TRANSPORTE = MT_SEMI.ID_MEIO_TRANSPORTE " +
				    "AND MTCA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
				    "AND MMTA_SEMI.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
				    "AND CAM_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"COR_CARROCERIA_DSCONT_SEMI");
			//PROJECAO DA COR DA CARROCERIA DO SEMI-REBOQUE VARCHARI18N
			sql.addProjection(PropertyVarcharI18nProjection.createProjection("(SELECT  CAM_SEMI.DS_CONTEUDO_ATRIBUTO_MODELO_I " +
					"FROM  MEIO_TRANSP_CONTEUDO_ATRIB MTCA_SEMI, " +
					"MODELO_MEIO_TRANSP_ATRIBUTO MMTA_SEMI, " + 
				    "CONTEUDO_ATRIBUTO_MODELO CAM_SEMI " +
				    "WHERE 	 MTCA_SEMI.ID_MEIO_TRANSPORTE = MT_SEMI.ID_MEIO_TRANSPORTE " +
				    "AND MTCA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO = MMTA_SEMI.ID_MODELO_MEIO_TRANSP_ATRIBUTO " +
				    "AND MMTA_SEMI.ID_ATRIBUTO_MEIO_TRANSPORTE = ? " +
				    "AND CAM_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO (+)= MTCA_SEMI.ID_CONTEUDO_ATRIBUTO_MODELO) " ,"COR_CARROCERIA_MT_SEMI"));
		}
		
		
		//*************************************** FROM ********************************************************
		//FROM DO MOTORISTA E SEUS CONTATOS
		sql.addFrom("MOTORISTA","MOT");
		sql.addFrom("PESSOA","PES");
		sql.addFrom("MUNICIPIO","MUN_IDENT");
		sql.addFrom("MUNICIPIO","MUN_NAT");
		sql.addFrom("UNIDADE_FEDERATIVA","UF_NAT");
		sql.addFrom("UNIDADE_FEDERATIVA","UF_CNH");
		sql.addFrom("PAIS","PA_NAT");
		sql.addFrom("PAIS","PAIS_CNH");
		sql.addFrom("PESSOA","PES_PROP");
		
		sql.addFrom("PROPRIETARIO","PROP");
		
	
		
		//FROM DO MEIO TRANSPORTE
		sql.addFrom("MEIO_TRANSPORTE","MT");
		sql.addFrom("MARCA_MEIO_TRANSPORTE","MARCAMT");
		sql.addFrom("MODELO_MEIO_TRANSPORTE","MODELOMT");
		sql.addFrom("MEIO_TRANSPORTE_RODOVIARIO","MTR");
		sql.addFrom("EIXOS_TIPO_MEIO_TRANSPORTE","EIXOS");
		sql.addFrom("MUNICIPIO","MUNMT");
		sql.addFrom("UNIDADE_FEDERATIVA","UFMT");
		
		//Novo relacionamento
		sql.addFrom("TIPO_MEIO_TRANSPORTE","TIPO_MT");
		
		if(idSemiReboque != null){
			//FROM DO SEMI-REBOQUE
			sql.addFrom("MEIO_TRANSPORTE","MT_SEMI");
			sql.addFrom("MARCA_MEIO_TRANSPORTE","MARCAMT_SEMI");
			sql.addFrom("MODELO_MEIO_TRANSPORTE","MODELOMT_SEMI");
			sql.addFrom("MEIO_TRANSPORTE_RODOVIARIO","MTR_SEMI");
			sql.addFrom("EIXOS_TIPO_MEIO_TRANSPORTE","EIXOS_SEMI");
			sql.addFrom("MUNICIPIO","MUNMT_SEMI");
			sql.addFrom("UNIDADE_FEDERATIVA","UFMT_SEMI");
			
			//Novo relacionamento
			sql.addFrom("TIPO_MEIO_TRANSPORTE","TIPO_MT_SEMI");
		}
		//********************************************* JOINS ******************************************************
		
		//JOIN DO MOTORISTA	
		sql.addCustomCriteria("MOT.ID_MOTORISTA =  ? ");
		sql.addJoin("PES.ID_PESSOA(+)","MOT.ID_MOTORISTA");
		sql.addJoin("V_ENDERECO.ID_PESSOA(+)","MOT.ID_MOTORISTA");
		sql.addJoin("V_TELEFONE.ID_PESSOA(+)","MOT.ID_MOTORISTA");
		sql.addJoin("MUN_NAT.ID_MUNICIPIO(+)","MOT.ID_MUNICIPIO_NATURALIDADE");
		sql.addJoin("UF_NAT.ID_UNIDADE_FEDERATIVA(+)","MUN_NAT.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("PA_NAT.ID_PAIS(+)","UF_NAT.ID_PAIS");
		sql.addJoin("MUN_IDENT.ID_MUNICIPIO(+)","MOT.ID_LOCAL_EMISSAO_IDENTIDADE");
		sql.addJoin("UF_CNH.ID_UNIDADE_FEDERATIVA(+)","MOT.ID_UNIDADE_FEDERATIVA_CNH");
		sql.addJoin("PAIS_CNH.ID_PAIS(+)","UF_CNH.ID_PAIS");
		
				
		//JOIN DO PROPRIETARIO
		sql.addCustomCriteria("PROP.ID_PROPRIETARIO =  ? "); 
		sql.addJoin("PES_PROP.ID_PESSOA(+)", "PROP.ID_PROPRIETARIO");
		sql.addJoin("V_ENDERECO_PROP.ID_PESSOA(+)","PROP.ID_PROPRIETARIO");
		sql.addJoin("V_TELEFONE_PROP.ID_PESSOA(+)","PROP.ID_PROPRIETARIO");
		sql.addJoin("V_DADOS_BANCARIOS_PROP.ID_PESSOA(+)","PROP.ID_PROPRIETARIO");
		
        //JOIN DO PROPRIETARIO SEMI - REBOQUE
		if(idProprietarioSemi!= null){
			sql.addFrom("PROPRIETARIO","PROP_SEMI");
			sql.addFrom("PESSOA","PES_PROP_SEMI");
			sql.addCustomCriteria("PROP_SEMI.ID_PROPRIETARIO =  ? "); 
			sql.addJoin("PES_PROP_SEMI.ID_PESSOA(+)", "PROP_SEMI.ID_PROPRIETARIO");
			sql.addJoin("V_ENDERECO_PROP_SEMI.ID_PESSOA(+)","PROP_SEMI.ID_PROPRIETARIO");
			sql.addJoin("V_TELEFONE_PROP_SEMI.ID_PESSOA(+)","PROP_SEMI.ID_PROPRIETARIO");
			sql.addJoin("V_DADOS_BANCARIOS_PROP_SEMI.ID_PESSOA(+)","PROP_SEMI.ID_PROPRIETARIO");
		}
		
		//JOIN DO MEIO-TRANSPORTE
		sql.addCustomCriteria("MT.ID_MEIO_TRANSPORTE =  ? ");
		sql.addJoin("MODELOMT.ID_MODELO_MEIO_TRANSPORTE","MT.ID_MODELO_MEIO_TRANSPORTE");
		sql.addJoin("MARCAMT.ID_MARCA_MEIO_TRANSPORTE","MODELOMT.ID_MARCA_MEIO_TRANSPORTE");
		sql.addJoin("MTR.ID_MEIO_TRANSPORTE","MT.ID_MEIO_TRANSPORTE");
		sql.addJoin("MTR.ID_MUNICIPIO","MUNMT.ID_MUNICIPIO(+)");
		sql.addJoin("MUNMT.ID_UNIDADE_FEDERATIVA","UFMT.ID_UNIDADE_FEDERATIVA(+)");
		sql.addJoin("MTR.ID_EIXOS_TIPO_MEIO_TRANSPORTE "," EIXOS.ID_EIXOS_TIPO_MEIO_TRANSPORTE(+)");
		  
		//Novo join
		sql.addJoin("MODELOMT.ID_TIPO_MEIO_TRANSPORTE","TIPO_MT.ID_TIPO_MEIO_TRANSPORTE");
		
		if(idSemiReboque != null) {
	        //JOIN DO SEMI-REBOQUE
			sql.addCustomCriteria("MT_SEMI.ID_MEIO_TRANSPORTE =  ? ");
			sql.addJoin("MODELOMT_SEMI.ID_MODELO_MEIO_TRANSPORTE","MT_SEMI.ID_MODELO_MEIO_TRANSPORTE");
			sql.addJoin("MARCAMT_SEMI.ID_MARCA_MEIO_TRANSPORTE","MODELOMT_SEMI.ID_MARCA_MEIO_TRANSPORTE");
			sql.addJoin("MTR_SEMI.ID_MEIO_TRANSPORTE","MT_SEMI.ID_MEIO_TRANSPORTE");
			sql.addJoin("MTR_SEMI.ID_MUNICIPIO","MUNMT_SEMI.ID_MUNICIPIO(+)");
			sql.addJoin("MUNMT_SEMI.ID_UNIDADE_FEDERATIVA","UFMT_SEMI.ID_UNIDADE_FEDERATIVA(+)");
			sql.addJoin("MTR_SEMI.ID_EIXOS_TIPO_MEIO_TRANSPORTE "," EIXOS_SEMI.ID_EIXOS_TIPO_MEIO_TRANSPORTE(+)");
			//Novo join
			sql.addJoin("MODELOMT_SEMI.ID_TIPO_MEIO_TRANSPORTE","TIPO_MT_SEMI.ID_TIPO_MEIO_TRANSPORTE");
		}
		 
		//******************************************************** SUB-SELECTS *************************************
				 
		//SUB-SELECT DO ENDEREÇO DO MOTORISTA
		SqlTemplate sqlEndereco = new SqlTemplate(); 
		sqlEndereco.addProjection("END_PES.ID_PESSOA");
		sqlEndereco.addProjection("END_PES.DS_ENDERECO");
		sqlEndereco.addProjection("END_PES.NR_ENDERECO");
		sqlEndereco.addProjection("END_PES.DS_COMPLEMENTO");
		sqlEndereco.addProjection("END_PES.DS_BAIRRO");
		sqlEndereco.addProjection("END_PES.NR_CEP");
		sqlEndereco.addProjection(PropertyVarcharI18nProjection.createProjection("TIPO_LOGR.DS_TIPO_LOGRADOURO_I"),"DS_TIPO_LOGRADOURO");
		sqlEndereco.addProjection("MUN_ENDPES.NM_MUNICIPIO|| '/' ||UF_ENDPES.SG_UNIDADE_FEDERATIVA","NM_MUNICIPIO_MOT");
		sqlEndereco.addProjection("UF_ENDPES.SG_UNIDADE_FEDERATIVA","UF_MOT");
		sqlEndereco.addProjection("PAIS_ENDPES.SG_PAIS","SG_PAIS_MOT");
		
				 
		sqlEndereco.addFrom("ENDERECO_PESSOA END_PES");
		sqlEndereco.addFrom("TIPO_LOGRADOURO TIPO_LOGR");
		sqlEndereco.addFrom("MUNICIPIO MUN_ENDPES");
		sqlEndereco.addFrom("UNIDADE_FEDERATIVA UF_ENDPES");
		sqlEndereco.addFrom("PAIS PAIS_ENDPES");
		
			
		sqlEndereco.addJoin("MUN_ENDPES.ID_MUNICIPIO","END_PES.ID_MUNICIPIO");
		sqlEndereco.addJoin("UF_ENDPES.ID_UNIDADE_FEDERATIVA","MUN_ENDPES.ID_UNIDADE_FEDERATIVA");
		sqlEndereco.addJoin("UF_ENDPES.ID_PAIS","PAIS_ENDPES.ID_PAIS");
		sqlEndereco.addJoin("TIPO_LOGR.ID_TIPO_LOGRADOURO","END_PES.ID_TIPO_LOGRADOURO");
		
		sqlEndereco.addCustomCriteria("END_PES.ID_ENDERECO_PESSOA = "+
				"(SELECT MAX(END_PES.ID_ENDERECO_PESSOA) " +
				 "FROM ENDERECO_PESSOA END_PES, MOTORISTA MOT, TIPO_ENDERECO_PESSOA TIPO, PESSOA PES " +
				 "WHERE " +
				 "END_PES.ID_PESSOA(+)=MOT.ID_MOTORISTA  AND MOT.ID_MOTORISTA=? " +
				 "AND END_PES.DT_VIGENCIA_INICIAL <= ? AND " +
				 "END_PES.DT_VIGENCIA_FINAL >= ? " +
				 "AND MOT.ID_MOTORISTA = PES.ID_PESSOA " +
				 "AND TIPO.ID_ENDERECO_PESSOA = END_PES.ID_ENDERECO_PESSOA " +
				 "AND TIPO.TP_ENDERECO = (CASE WHEN PES.TP_PESSOA='J' THEN 'COM' ELSE 'RES' END))) V_ENDERECO ");
				 
		//SUB-SELECT TELEFONE DO MOTORISTA
		SqlTemplate sqlTelefone = new SqlTemplate();
		sqlTelefone.addProjection("TEL_END.NR_DDI");
		sqlTelefone.addProjection("TEL_END.NR_DDD");
		sqlTelefone.addProjection("TEL_END.NR_TELEFONE");
		sqlTelefone.addProjection("TEL_END.ID_PESSOA");
		
		sqlTelefone.addFrom("TELEFONE_ENDERECO","TEL_END");
		
		sqlTelefone.addCustomCriteria("TEL_END.ID_TELEFONE_ENDERECO = " +
				"(SELECT ID_TELEFONE_ENDERECO FROM (SELECT TEL_END.ID_TELEFONE_ENDERECO " +
				 "FROM TELEFONE_ENDERECO TEL_END, " +
				 "TELEFONE_CONTATO TEL_CON, " +
				 "MOTORISTA MOT " +
				 "WHERE (TEL_END.TP_USO = 'FO' OR TEL_END.TP_USO = 'FA') " +
		         "  AND TEL_END.ID_PESSOA = MOT.ID_MOTORISTA " +
		         "  AND TEL_CON.ID_TELEFONE_ENDERECO = TEL_END.ID_TELEFONE_ENDERECO " +
		         "  AND MOT.ID_MOTORISTA=? " +
		         "ORDER BY TEL_CON.ID_TELEFONE_CONTATO " +
		         ") WHERE ROWNUM = 1 )) V_TELEFONE");
		
//#################################### VIEWS DO PROPRIETARIO #####################################################		
		
		//SUB-SELECT ENDEREÇO DO PROPRIETARIO
		SqlTemplate sqlEnderecoProp = new SqlTemplate();
		
		sqlEnderecoProp.addProjection("ENDPROP.ID_PESSOA");
		sqlEnderecoProp.addProjection("ENDPROP.DS_ENDERECO");
		sqlEnderecoProp.addProjection("ENDPROP.NR_ENDERECO");
		sqlEnderecoProp.addProjection("ENDPROP.DS_COMPLEMENTO");
		sqlEnderecoProp.addProjection("ENDPROP.DS_BAIRRO");
		sqlEnderecoProp.addProjection("ENDPROP.NR_CEP");
		sqlEnderecoProp.addProjection(PropertyVarcharI18nProjection.createProjection("TIPO_LOGR.DS_TIPO_LOGRADOURO_I"),"DS_TIPO_LOGRADOURO");
		sqlEnderecoProp.addProjection("MUN_ENDPROP.NM_MUNICIPIO|| '/' ||UF_ENDPROP.SG_UNIDADE_FEDERATIVA","NM_MUNICIPIO_PROP");
		sqlEnderecoProp.addProjection("UF_ENDPROP.SG_UNIDADE_FEDERATIVA","SG_UNIDADE_FEDERATIVA_PROP");
		sqlEnderecoProp.addProjection("PAIS_ENDPROP.SG_PAIS","SG_PAIS_PROP");
		
		
		sqlEnderecoProp.addFrom("ENDERECO_PESSOA","ENDPROP");
		sqlEnderecoProp.addFrom("TIPO_LOGRADOURO","TIPO_LOGR");
		sqlEnderecoProp.addFrom("MUNICIPIO","MUN_ENDPROP");
		sqlEnderecoProp.addFrom("UNIDADE_FEDERATIVA","UF_ENDPROP");
		sqlEnderecoProp.addFrom("PAIS","PAIS_ENDPROP");
		
		sqlEnderecoProp.addJoin("MUN_ENDPROP.ID_MUNICIPIO(+)","ENDPROP.ID_MUNICIPIO");
		sqlEnderecoProp.addJoin("TIPO_LOGR.ID_TIPO_LOGRADOURO","ENDPROP.ID_TIPO_LOGRADOURO");
		sqlEnderecoProp.addJoin("UF_ENDPROP.ID_UNIDADE_FEDERATIVA","MUN_ENDPROP.ID_UNIDADE_FEDERATIVA");
		sqlEnderecoProp.addJoin("UF_ENDPROP.ID_PAIS","PAIS_ENDPROP.ID_PAIS");    
		
		sqlEnderecoProp.addCustomCriteria("ENDPROP.ID_ENDERECO_PESSOA = "+
				"(SELECT MAX(ENDPROP.ID_ENDERECO_PESSOA) " +
				"FROM ENDERECO_PESSOA ENDPROP, PROPRIETARIO PROP, TIPO_ENDERECO_PESSOA TIPO, PESSOA PES " +
				"WHERE ENDPROP.ID_PESSOA(+)=PROP.ID_PROPRIETARIO  " +
				"AND PROP.ID_PROPRIETARIO=? " +
				"AND ENDPROP.DT_VIGENCIA_INICIAL <= ? " +
				"AND ENDPROP.DT_VIGENCIA_FINAL >= ? " +
				"AND PROP.ID_PROPRIETARIO = PES.ID_PESSOA " +
				"AND TIPO.ID_ENDERECO_PESSOA = ENDPROP.ID_ENDERECO_PESSOA " +
				"AND TIPO.TP_ENDERECO = (CASE WHEN PES.TP_PESSOA='J' THEN 'COM' ELSE 'RES' END))) V_ENDERECO_PROP");
		
		//SUB-SELECT TELEFONE DO PROPRIETARIO
		SqlTemplate sqlTelefoneProp = new SqlTemplate();
		sqlTelefoneProp.addProjection("TELPROP.NR_DDI");
		sqlTelefoneProp.addProjection("TELPROP.NR_DDD");
		sqlTelefoneProp.addProjection("TELPROP.NR_TELEFONE");
		sqlTelefoneProp.addProjection("TELPROP.ID_PESSOA");
		
		sqlTelefoneProp.addFrom("TELEFONE_ENDERECO","TELPROP");
		sqlTelefoneProp.addCustomCriteria("TELPROP.ID_TELEFONE_ENDERECO = " +
				"(SELECT MAX(TELPROP.ID_TELEFONE_ENDERECO) FROM TELEFONE_ENDERECO TELPROP, PROPRIETARIO PROP " +
				"WHERE (TELPROP.TP_USO = 'FO' OR TELPROP.TP_USO = 'FA') " +
				"AND TELPROP.ID_PESSOA = PROP.ID_PROPRIETARIO " +
				"AND PROP.ID_PROPRIETARIO= ? )) V_TELEFONE_PROP");
		
		//SUB-SELECT DOS DADOS BANCARIOS DO PROPRIETARIO
		SqlTemplate sqlDadosBancarios = new SqlTemplate();
		sqlDadosBancarios.addProjection("CB.NR_CONTA_BANCARIA || '-' || CB.DV_CONTA_BANCARIA", "NR_CONTA_BANCARIA");
		sqlDadosBancarios.addProjection("AB.NR_AGENCIA_BANCARIA || '-' || AB.NR_DIGITO","NR_AGENCIA");
		sqlDadosBancarios.addProjection("BA.NR_BANCO");
		sqlDadosBancarios.addProjection("BA.NM_BANCO");
		sqlDadosBancarios.addProjection("CB.ID_PESSOA");
		
		sqlDadosBancarios.addFrom("CONTA_BANCARIA","CB");
		sqlDadosBancarios.addFrom("AGENCIA_BANCARIA","AB");
		sqlDadosBancarios.addFrom("BANCO","BA");
		sqlDadosBancarios.addFrom("PESSOA","PES");
		sqlDadosBancarios.addFrom("PROPRIETARIO","PROP");
						
	    sqlDadosBancarios.addCustomCriteria("PROP.ID_PROPRIETARIO = ?");
	    
	    sqlDadosBancarios.addJoin("CB.ID_AGENCIA_BANCARIA", "AB.ID_AGENCIA_BANCARIA");
	    sqlDadosBancarios.addJoin("AB.ID_BANCO", "BA.ID_BANCO");
	    sqlDadosBancarios.addJoin("PROP.ID_PROPRIETARIO", "PES.ID_PESSOA");
	    sqlDadosBancarios.addJoin("CB.ID_PESSOA(+)", "PROP.ID_PROPRIETARIO");
	    
	    sqlDadosBancarios.addCustomCriteria("CB.DT_VIGENCIA_INICIAL<= ? " +
	    		"AND CB.DT_VIGENCIA_FINAL>= ? ) V_DADOS_BANCARIOS_PROP");
	    
//	  #################################### VIEWS DO PROPRIETARIO SEMI REBOQUE ###################################		
		
	    //SUB-SELECT ENDEREÇO DO PROPRIETARIO SEMI REBOQUE
		SqlTemplate sqlEnderecoPropSemi = new SqlTemplate();
		
		sqlEnderecoPropSemi.addProjection("ENDPROP.ID_PESSOA");
		sqlEnderecoPropSemi.addProjection("ENDPROP.DS_ENDERECO");
		sqlEnderecoPropSemi.addProjection("ENDPROP.NR_ENDERECO");
		sqlEnderecoPropSemi.addProjection("ENDPROP.DS_COMPLEMENTO");
		sqlEnderecoPropSemi.addProjection("ENDPROP.DS_BAIRRO");
		sqlEnderecoPropSemi.addProjection("ENDPROP.NR_CEP");
		sqlEnderecoPropSemi.addProjection(PropertyVarcharI18nProjection.createProjection("TIPO_LOGR.DS_TIPO_LOGRADOURO_I"),"DS_TIPO_LOGRADOURO");
		sqlEnderecoPropSemi.addProjection("MUN_ENDPROP.NM_MUNICIPIO|| '/' ||UF_ENDPROP.SG_UNIDADE_FEDERATIVA","NM_MUNICIPIO_PROP");
		sqlEnderecoPropSemi.addProjection("UF_ENDPROP.SG_UNIDADE_FEDERATIVA","SG_UNIDADE_FEDERATIVA_PROP");
		sqlEnderecoPropSemi.addProjection("PAIS_ENDPROP.SG_PAIS","SG_PAIS_PROP");
		
		
		sqlEnderecoPropSemi.addFrom("ENDERECO_PESSOA","ENDPROP");
		sqlEnderecoPropSemi.addFrom("TIPO_LOGRADOURO","TIPO_LOGR");
		sqlEnderecoPropSemi.addFrom("MUNICIPIO","MUN_ENDPROP");
		sqlEnderecoPropSemi.addFrom("UNIDADE_FEDERATIVA","UF_ENDPROP");
		sqlEnderecoPropSemi.addFrom("PAIS","PAIS_ENDPROP");
		
		sqlEnderecoPropSemi.addJoin("MUN_ENDPROP.ID_MUNICIPIO(+)","ENDPROP.ID_MUNICIPIO");
		sqlEnderecoPropSemi.addJoin("TIPO_LOGR.ID_TIPO_LOGRADOURO","ENDPROP.ID_TIPO_LOGRADOURO");
		sqlEnderecoPropSemi.addJoin("UF_ENDPROP.ID_UNIDADE_FEDERATIVA","MUN_ENDPROP.ID_UNIDADE_FEDERATIVA");
		sqlEnderecoPropSemi.addJoin("UF_ENDPROP.ID_PAIS","PAIS_ENDPROP.ID_PAIS");    
		
		sqlEnderecoPropSemi.addCustomCriteria("ENDPROP.ID_ENDERECO_PESSOA = "+
				"(SELECT MAX(ENDPROP.ID_ENDERECO_PESSOA) " +
				"FROM ENDERECO_PESSOA ENDPROP, PROPRIETARIO PROP, TIPO_ENDERECO_PESSOA TIPO, PESSOA PES " +
				"WHERE ENDPROP.ID_PESSOA(+)=PROP.ID_PROPRIETARIO  " +
				"AND PROP.ID_PROPRIETARIO=? " +
				"AND ENDPROP.DT_VIGENCIA_INICIAL <= ? " +
				"AND ENDPROP.DT_VIGENCIA_FINAL >= ? " +
				"AND PROP.ID_PROPRIETARIO = PES.ID_PESSOA " +
				"AND TIPO.ID_ENDERECO_PESSOA = ENDPROP.ID_ENDERECO_PESSOA " +
				"AND TIPO.TP_ENDERECO = (CASE WHEN PES.TP_PESSOA='J' THEN 'COM' ELSE 'RES' END))) V_ENDERECO_PROP_SEMI");
		
		//SUB-SELECT TELEFONE DO PROPRIETARIO SEMI REBOQUE
		SqlTemplate sqlTelefonePropSemi = new SqlTemplate();
		sqlTelefonePropSemi.addProjection("TELPROP.NR_DDI");
		sqlTelefonePropSemi.addProjection("TELPROP.NR_DDD");
		sqlTelefonePropSemi.addProjection("TELPROP.NR_TELEFONE");
		sqlTelefonePropSemi.addProjection("TELPROP.ID_PESSOA");
		
		sqlTelefonePropSemi.addFrom("TELEFONE_ENDERECO","TELPROP");
		sqlTelefonePropSemi.addCustomCriteria("TELPROP.ID_TELEFONE_ENDERECO = " +
				"(SELECT MAX(TELPROP.ID_TELEFONE_ENDERECO) FROM TELEFONE_ENDERECO TELPROP, PROPRIETARIO PROP " +
				"WHERE (TELPROP.TP_USO = 'FO' OR TELPROP.TP_USO = 'FA') " +
				"AND TELPROP.ID_PESSOA = PROP.ID_PROPRIETARIO " +
				"AND PROP.ID_PROPRIETARIO= ? )) V_TELEFONE_PROP_SEMI");
				
		//SUB-SELECT DOS DADOS BANCARIOS DO PROPRIETARIO SEMI REBOQUE
		SqlTemplate sqlDadosBancariosSemi = new SqlTemplate();
		sqlDadosBancariosSemi.addProjection("CB.NR_CONTA_BANCARIA || '-' || CB.DV_CONTA_BANCARIA", "NR_CONTA_BANCARIA");
		sqlDadosBancariosSemi.addProjection("AB.NR_AGENCIA_BANCARIA || '-' || AB.NR_DIGITO","NR_AGENCIA");
		sqlDadosBancariosSemi.addProjection("BA.NR_BANCO");
		sqlDadosBancariosSemi.addProjection("BA.NM_BANCO");
		sqlDadosBancariosSemi.addProjection("CB.ID_PESSOA");
		
		sqlDadosBancariosSemi.addFrom("CONTA_BANCARIA","CB");
		sqlDadosBancariosSemi.addFrom("AGENCIA_BANCARIA","AB");
		sqlDadosBancariosSemi.addFrom("BANCO","BA");
		sqlDadosBancariosSemi.addFrom("PESSOA","PES");
		sqlDadosBancariosSemi.addFrom("PROPRIETARIO","PROP");
						
		sqlDadosBancariosSemi.addCustomCriteria("PROP.ID_PROPRIETARIO = ?");
	    
		sqlDadosBancariosSemi.addJoin("CB.ID_AGENCIA_BANCARIA", "AB.ID_AGENCIA_BANCARIA");
		sqlDadosBancariosSemi.addJoin("AB.ID_BANCO", "BA.ID_BANCO");
		sqlDadosBancariosSemi.addJoin("PROP.ID_PROPRIETARIO", "PES.ID_PESSOA");
		sqlDadosBancariosSemi.addJoin("CB.ID_PESSOA(+)", "PROP.ID_PROPRIETARIO");
	    
		sqlDadosBancariosSemi.addCustomCriteria("CB.DT_VIGENCIA_INICIAL<= ? " +
	    		"AND CB.DT_VIGENCIA_FINAL>= ? ) V_DADOS_BANCARIOS_PROP_SEMI");
		
		
	    sql.addFrom("("+sqlEndereco.getSql());
		sql.addFrom("("+sqlTelefone.getSql());
		sql.addFrom("("+sqlEnderecoProp.getSql());
		sql.addFrom("("+sqlTelefoneProp.getSql());
		sql.addFrom("("+sqlDadosBancarios.getSql());
		if(idProprietarioSemi != null){
			sql.addFrom("("+sqlEnderecoPropSemi.getSql());
			sql.addFrom("("+sqlTelefonePropSemi.getSql());
			sql.addFrom("("+sqlDadosBancariosSemi.getSql());
		}	
		 
		
		//************************************************ FILTROS ********************************************
		
		//filtros para trazer os atributos do meio de transporte
		sql.addCriteriaValue(idTpCarroceria);
		sql.addCriteriaValue(idTpCarroceria);
		sql.addCriteriaValue(idTpCarroceria);
		sql.addCriteriaValue(idCorCabine);
		sql.addCriteriaValue(idCorCabine);
		sql.addCriteriaValue(idCorCabine);
		sql.addCriteriaValue(idCorCarroceria);
		sql.addCriteriaValue(idCorCarroceria);
		sql.addCriteriaValue(idCorCarroceria);
		
		
		if(idSemiReboque != null){
	        //filtros para trazer os atributos do semi-reboque
			sql.addCriteriaValue(idTpCarroceria);
			sql.addCriteriaValue(idTpCarroceria);
			sql.addCriteriaValue(idCorCabine);
			sql.addCriteriaValue(idCorCabine);
			sql.addCriteriaValue(idCorCarroceria);
			sql.addCriteriaValue(idCorCarroceria);
		}
		
		
		//filtros para o endereço do motorista
		sql.addCriteriaValue(idMotorista);
		sql.addCriteriaValue(dataAtual);
		sql.addCriteriaValue(dataAtual);
		
		//telefone motorista
		sql.addCriteriaValue(idMotorista);
		
		
        //filtros para o endereço do proprietario
		sql.addCriteriaValue(idProprietario);
		sql.addCriteriaValue(dataAtual);
		sql.addCriteriaValue(dataAtual);
		
        //telefone proprietario
		sql.addCriteriaValue(idProprietario);
		
		//filtros da conta bancaria
		sql.addCriteriaValue(idProprietario);
		sql.addCriteriaValue(dataAtual);
		sql.addCriteriaValue(dataAtual);
		
		if(idProprietarioSemi!= null){
			  //filtros para o endereço do proprietario semi reboque
			sql.addCriteriaValue(idProprietarioSemi);
			sql.addCriteriaValue(dataAtual);
			sql.addCriteriaValue(dataAtual);
			
	        //telefone proprietario semi reboque
			sql.addCriteriaValue(idProprietarioSemi);
			
			//filtros da conta bancaria semi reboque
			sql.addCriteriaValue(idProprietarioSemi);
			sql.addCriteriaValue(dataAtual);
			sql.addCriteriaValue(dataAtual);
		
		}		
       //filtros para trazer o motorista, proprietario e meio de transporte
		sql.addCriteriaValue(idMotorista);
		sql.addCriteriaValue(idProprietario);
		if(idSemiReboque != null)
			sql.addCriteriaValue(idProprietarioSemi);
		sql.addCriteriaValue(idMeioTransporte);
		if(idSemiReboque != null)
			sql.addCriteriaValue(idSemiReboque);
			
			
		sql.addFilterSummary("meioTransporte",criteria.getString("meioTransporteNrFrota")+ 
				" - " + criteria.getString("identificacaoMeioTransporte"));
		sql.addFilterSummary("proprietario",criteria.getString("proprietarioNrIdentificacao")+ 
				" - " + criteria.getString("proprietario.pessoa.nmPessoa"));
						
		if(idSemiReboque != null) {
			sql.addFilterSummary("semiReboque",criteria.getString("semiReboqueNrFrota")+ 
					" - " + criteria.getString("identificacaoSemiReboque"));
		}
		
		if(idProprietarioSemi!= null){
			sql.addFilterSummary("proprietario",criteria.getString("proprietarioSemiNrIdentificacao") +
					" - " + criteria.getString("proprietarioSemi.pessoa.nmPessoa"));
		}
		
		sql.addFilterSummary("motorista",criteria.getString("motorista.pessoa.nrIdentificacao")+ 
				" - " + criteria.getString("motorista.pessoa.nmPessoa"));
		
		return sql;
	}
	
	public JRDataSource executeContatosRPMotorista(BigDecimal idMotorista) throws Exception {
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("TELEND.NR_DDI","NR_DDI_RP");
		sql.addProjection("TELEND.NR_DDD","NR_DDD_RP");
		sql.addProjection("TELEND.NR_TELEFONE","NR_TELEFONE_RP");
		sql.addProjection("CONT.NM_CONTATO","NM_CONTATO_RP");
		
		sql.addFrom("CONTATO","CONT");
		sql.addFrom("MOTORISTA","MOT");
		sql.addFrom("TELEFONE_CONTATO","TELCONT");
		sql.addFrom("TELEFONE_ENDERECO","TELEND");
		
		sql.addJoin("MOT.ID_MOTORISTA","CONT.ID_PESSOA");
		sql.addJoin("TELCONT.ID_CONTATO","CONT.ID_CONTATO");
		sql.addJoin("TELCONT.ID_TELEFONE_ENDERECO","TELEND.ID_TELEFONE_ENDERECO");
		 
		sql.addCustomCriteria("MOT.ID_MOTORISTA=? AND CONT.TP_CONTATO ='RP' AND (TELEND.TP_USO = 'FO' OR TELEND.TP_USO = 'FA') ");
		 
		Long idMotoristaRP = Long.valueOf(idMotorista.longValue());
		sql.addCriteriaValue(idMotoristaRP);
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();

	}
	
	public JRDataSource executeContatosRSMotorista(BigDecimal idMotorista) throws Exception {
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("TELEND.NR_DDI","NR_DDI_RS" );
		sql.addProjection("TELEND.NR_DDD","NR_DDD_RS");
		sql.addProjection("TELEND.NR_TELEFONE", "NR_TELEFONE_RS");
		sql.addProjection("CONT.NM_CONTATO","NM_CONTATO_RS");
		
		sql.addFrom("CONTATO","CONT");
		sql.addFrom("MOTORISTA","MOT");
		sql.addFrom("TELEFONE_CONTATO","TELCONT");
		sql.addFrom("TELEFONE_ENDERECO","TELEND");
		
		sql.addJoin("MOT.ID_MOTORISTA","CONT.ID_PESSOA");
		sql.addJoin("TELCONT.ID_CONTATO","CONT.ID_CONTATO");
		sql.addJoin("TELCONT.ID_TELEFONE_ENDERECO","TELEND.ID_TELEFONE_ENDERECO");
		 
		sql.addCustomCriteria("MOT.ID_MOTORISTA=? AND CONT.TP_CONTATO ='RS' AND (TELEND.TP_USO = 'FO' OR TELEND.TP_USO = 'FA') ");
	
		Long idMotoristaRS = Long.valueOf(idMotorista.longValue());
		sql.addCriteriaValue(idMotoristaRS);
		
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();

	}
	
	public String formataTelefone(String nrDDI, String nrDDD, String fone){
		StringBuffer foneConcatenado = new StringBuffer();
		
		if(nrDDI!= null)
			foneConcatenado.append("+"+nrDDI);
		if(nrDDD != null)
			foneConcatenado.append("("+nrDDD+") ");
		if(fone != null)
			foneConcatenado.append(fone);
		return foneConcatenado.toString();
		
	}
	
	public String formataCep(String siglaPais, String cep){
		return FormatUtils.formatCep(siglaPais,cep);
	}
	
	public String formataIdentificacao(String tpIdentificacao, String conteudo){
		return FormatUtils.formatIdentificacao(tpIdentificacao,conteudo);
		
	}
	
}

