package com.mercurio.lms.prestcontasciaaerea.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.prestcontasciaaerea.model.service.FaturaCiaAereaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;


public class EmitirRelatorioAwbService extends ReportServiceSupport {
	/**
	 * Constante que identifica que a exportação deve ocorrer no modo XLS (Excel).
	 */
	private static final String SUB_REPORT_1 = "SUB_REPORT_1";
	private static final String SUB_REPORT_2 = "SUB_REPORT_2";
	private static final String SUB_REPORT_3 = "SUB_REPORT_3";
	private static final String RELATORIO_AWB_LISTAR_FATURAS = "relatorioAwbListarFaturas";
	private static final String RELATORIO_AWB_LISTAR_CTES = "relatorioAwbListarCtes";
	private static final String RELATORIO_AWB_LISTAR_AWBS = "relatorioAwbListarAwbs";
	private static final String RELATORIO_AWB_LISTAR_AWBS_COM_RENTABILIDADE = "relatorioAwbListarAwbsComRentabilidade";
	private static final String RELATORIO_AWB_LISTAR_AWBS_COM_DIFERENCA = "relatorioAwbListarAwbsComDiferenca";
	private static final String RELATORIO_SUB_RENTABILIDADE_AND_AWBS_COM_DIFERENCA = "relatorioAwbListarAwbsComRentabilidadeAndDiferenca";

	private static final String AWBS_COM_DIFERENCA_PARAM_VIEW = "awbsComDiferenca";
	private static final String RENTABILIDADE_PARAM_VIEW = "rentabilidade";
	private static final String DATA_SOURCE_3 = "DATA_SOURCE_3";
	private static final String DATA_SOURCE_2 = "DATA_SOURCE_2";
	private static final String DATA_SOURCE_1 = "DATA_SOURCE_1";
	private static final String LISTAR_AWBS = "listarAwbs";
	private static final String LISTAR_CTES = "listarCtes";
	private static final String LISTAR_FATURAS = "listarFaturas";

	
	private FaturaCiaAereaService faturaCiaAereaService;
	private EmpresaService empresaService;
	private DomainValueService domainValueService;
	private AwbService awbService;
	private AeroportoService aeroportoService;
	private PessoaService pessoaService;
	private FilialService filialService;
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap typedFlatMap = (TypedFlatMap)parameters;
        SqlTemplate sql = mountSql(typedFlatMap);
        final List lista = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());

        JRReportDataObject jr = new JRReportDataObject() {
        	Map parameters = new HashMap();
        
        	public JRDataSource getDataSource() {
        	return new JRBeanCollectionDataSource(lista);
        	}
        
        	public Map getParameters() {
        	return parameters;
        	}
        
        	public void setParameters(Map arg0) {
        	parameters = arg0;
        	}
        	};      
        
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
                
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        
		// listarFaturas
		if (typedFlatMap.getBoolean(LISTAR_FATURAS)) {
			parametersReport.put(SUB_REPORT_1, RELATORIO_AWB_LISTAR_FATURAS);
		}

		// listarCtes
		if (!typedFlatMap.getBoolean(LISTAR_FATURAS)
				&& typedFlatMap.getBoolean(LISTAR_CTES)) {
			parametersReport.put(SUB_REPORT_1, RELATORIO_AWB_LISTAR_CTES);
		} else if (typedFlatMap.getBoolean(LISTAR_FATURAS)
				&& typedFlatMap.getBoolean(LISTAR_CTES)) {
			parametersReport.put(SUB_REPORT_2, RELATORIO_AWB_LISTAR_CTES);
		} else {
			parametersReport.put(SUB_REPORT_2, null);
		}

		// listarAwbs
	    Boolean isRentabilidade = (Boolean)parameters.get(RENTABILIDADE_PARAM_VIEW);
	    Boolean isAwbsComDiferenca = (Boolean)parameters.get(AWBS_COM_DIFERENCA_PARAM_VIEW);
		if (!typedFlatMap.getBoolean(LISTAR_FATURAS)
				&& !typedFlatMap.getBoolean(LISTAR_CTES)
				&& typedFlatMap.getBoolean(LISTAR_AWBS)) {
			parametersReport.put(SUB_REPORT_1, getSubReportRentabilidadeAndAwbsComAcrescimo(isRentabilidade, isAwbsComDiferenca));

		} else if (typedFlatMap.getBoolean(LISTAR_FATURAS)
				&& !typedFlatMap.getBoolean(LISTAR_CTES)
				&& typedFlatMap.getBoolean(LISTAR_AWBS)) {
			parametersReport.put(SUB_REPORT_2, getSubReportRentabilidadeAndAwbsComAcrescimo(isRentabilidade, isAwbsComDiferenca));
		}else if (!typedFlatMap.getBoolean(LISTAR_FATURAS)
				&& typedFlatMap.getBoolean(LISTAR_CTES)
				&& typedFlatMap.getBoolean(LISTAR_AWBS)) {
			parametersReport.put(SUB_REPORT_2, getSubReportRentabilidadeAndAwbsComAcrescimo(isRentabilidade, isAwbsComDiferenca));
		} else if (typedFlatMap.getBoolean(LISTAR_CTES)
				&& typedFlatMap.getBoolean(LISTAR_AWBS)
				&& typedFlatMap.getBoolean(LISTAR_FATURAS)) {
			parametersReport.put(SUB_REPORT_3, getSubReportRentabilidadeAndAwbsComAcrescimo(isRentabilidade, isAwbsComDiferenca));
		} else {
			parametersReport.put(SUB_REPORT_3, null);
		}
	    	
		parametersReport.put(DATA_SOURCE_1, new JRBeanCollectionDataSource(lista));
	    parametersReport.put(DATA_SOURCE_2, new JRBeanCollectionDataSource(lista));
	    parametersReport.put(DATA_SOURCE_3, new JRBeanCollectionDataSource(lista));
	    
	    this.preencherParametrosPesquisa(typedFlatMap, parametersReport);
	    jr.setParameters(parametersReport);
        
        return jr;		
	}
	
	private String getSubReportRentabilidadeAndAwbsComAcrescimo(Boolean isRentabilidade, Boolean isAwbsComDiferenca) {
		
		if (BooleanUtils.isTrue(isRentabilidade) && BooleanUtils.isFalse(isAwbsComDiferenca)) {
			return RELATORIO_AWB_LISTAR_AWBS_COM_RENTABILIDADE;
		} else if (BooleanUtils.isFalse(isRentabilidade) && BooleanUtils.isTrue(isAwbsComDiferenca)) {
			return RELATORIO_AWB_LISTAR_AWBS_COM_DIFERENCA;
		} else if (BooleanUtils.isTrue(isRentabilidade) && BooleanUtils.isTrue(isAwbsComDiferenca)) {
			return RELATORIO_SUB_RENTABILIDADE_AND_AWBS_COM_DIFERENCA;
		}
		
		return RELATORIO_AWB_LISTAR_AWBS;
	}
	
    private SqlTemplate mountSql(TypedFlatMap parameters) throws Exception {    	
    	SqlTemplate sql = createSqlTemplate();
    	sql.setDistinct();

    	Boolean isListarFaturas = parameters.getBoolean(LISTAR_FATURAS);
    	Boolean isListarAwbs = parameters.getBoolean(LISTAR_AWBS);
    	Boolean isListarCtes = parameters.getBoolean(LISTAR_CTES);

    	//Campos Faturas
    	if(isListarFaturas) {
    		sql.addProjection("PCIAFAT.NM_PESSOA" , "CIA_AEREA_FATURA");
    		sql.addProjection("FCA.NR_FATURA_CIA_AEREA" , "NR_FATURA_CIA_AEREA");
    		sql.addProjection("FCA.DT_EMISSAO" , "DT_EMISSAO_FATURA");
    		sql.addProjection("FCA.DT_INCLUSAO" , "DT_INCLUSAO_FATURA");
    		sql.addProjection("FCA.DT_VENCIMENTO" , "DT_VENCIMENTO_FATURA");
    		sql.addProjection("FCA.DT_ENVIO_JDE" , "DT_ENVIO_JDE");
    		sql.addProjection("FCA.DT_PAGAMENTO", "DT_PAGAMENTO_FATURA");
    		sql.addProjection("FCA.DT_PERIODO_INICIAL", "DT_FATURAMENTO_INICIAL");
    		sql.addProjection("FCA.DT_PERIODO_FINAL", "DT_FATURAMENTO_FINAL");
    		sql.addProjection(this.subQueryProjectionSituacaoFaturaAereo(), "SITUACAO_FATURA");
    		sql.addProjection(this.subQueryProjectionTotalPesoReal(), "TOTAL_PESO_REAL");
    		sql.addProjection(this.subQueryProjectionTotalPesoCubado(), "TOTAL_PESO_CUBADO");
    		sql.addProjection(this.subQueryProjectionTotalPesoCobrado(), "TOTAL_PESO_COBRADO");
    		sql.addProjection(this.subQueryProjectionTotalFrete(), "TOTAL_FRETE");
    		sql.addProjection(this.subQueryProjectionTotalDesconto(), "TOTAL_DESCONTOS");
    		sql.addProjection(this.subQueryProjectionTotalAcrescimo(), "TOTAL_ACRESCIMOS");
    		sql.addProjection(this.subQueryProjectionTotalIcms(), "TOTAL_ICMS");
    		sql.addProjection(this.subQueryProjectionValorReceita(), "VL_RECEITA");
    	}
    	
    	//Campos Awbs
    	if(isListarAwbs) {
    		sql.addProjection("ECIAAW.SG_EMPRESA", "SG_EMPRESA");
    		sql.addProjection("AW.DS_SERIE", "DS_SERIE");
    		sql.addProjection("AW.NR_AWB", "NR_AWB");
    		sql.addProjection("AW.DV_AWB", "DV_AWB");
    		sql.addProjection("TRUNC(AW.DH_EMISSAO)" , "DH_EMISSAO_AWB");
    		sql.addProjection("TRUNC(AW.DH_DIGITACAO)","DH_DIGITACAO_AWB");
    		sql.addProjection(this.subQueryProjectionSituacaoAwb(), "SITUACAO_AWB");
    		sql.addProjection(this.subQueryProjectionTipoAwb(), "TIPO_AWB");
    		sql.addProjection(this.subQueryProjectionSituacaoPagamentoAwb(), "SITUACAO_PAGAMENTO_AWB");
    		sql.addProjection("AW.VL_FRETE", "VL_FRETE_AWB");
    		sql.addProjection("AW.PC_ALIQUOTA_ICMS", "ALIQUOTA_ICMS");
    		sql.addProjection("AW.VL_ICMS", "VL_ICMS_AWB");
    		sql.addProjection("CAST(NVL(AW.VL_FRETE, 0) - NVL(AW.VL_ICMS, 0) AS NUMBER(18, 2))", "VL_LIQUIDO_AWB");
    		sql.addProjection(this.subQueryProjectionValorReceitaAwb(), "VL_RECEITA_AWB");
    		sql.addProjection("AW.PS_TOTAL", "PS_REAL");
    		sql.addProjection("AW.PS_CUBADO", "PS_CUBADO");
    		sql.addProjection("IFCA.PS_COBRADO", "PS_COBRADO");
    		sql.addProjection("AW.QT_VOLUMES", "QT_VOLUMES");
    		sql.addProjection("FORIGAW.SG_FILIAL", "SG_FILIAL_ORIG");
    		sql.addProjection("FDESTAW.SG_FILIAL", "SG_FILIAL_DEST");
    		sql.addProjection("AEORIG.SG_AEROPORTO", "SG_AEROPORTO_ORIG");
    		sql.addProjection("AEDEST.SG_AEROPORTO", "SG_AEROPORTO_DEST");
    		sql.addProjection("AW.DS_VOO_PREVISTO",  "DS_VOO_PREVISTO");
    		sql.addProjection("PEXP.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_EXP");
    		sql.addProjection("PEXP.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_EXP");
    		sql.addProjection("PEXP.NM_PESSOA", "NM_PESSOA_EXP");
    		sql.addProjection("PDESTAW.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_DEST");
    		sql.addProjection("PDESTAW.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_DEST");
    		sql.addProjection("PDESTAW.NM_PESSOA", "NM_PESSOA_DEST");
    		sql.addProjection(this.subQueryProjectionPercetualRentabilidade(),  "PERCENTUAL_RENTABILIDADE");
    		sql.addProjection("AW.DS_JUSTIFICATIVA_PREJUIZO",  "DS_JUSTIFICATIVA_PREJUIZO");
    		sql.addProjection("US.NM_USUARIO",  "NM_USUARIO_JUSTIFICATIVA");
    		sql.addProjection("USI.NM_USUARIO", "NM_USUARIO_INCLUSAO");
    		sql.addProjection("NVL(AW.VL_FRETE_CALCULADO, 0)", "VL_FRETE_CALCULADO");
    		sql.addProjection("CAST(NVL(AW.VL_FRETE, 0) - NVL(AW.VL_FRETE_CALCULADO, 0)  AS NUMBER(18, 2))", "VL_FRETE_CALCULADO_DIFERENCA");
    	} 
    	
    	//Campos Ctes
    	if(isListarCtes) {
    		sql.addProjection("FORIG.SG_FILIAL", "SG_FILIAL_ORIG_CTE");
    		sql.addProjection("DS.NR_DOCTO_SERVICO", "NR_DOCTO_SERVICO");
    		sql.addProjection(this.subQueryProjectionTipoDocumento(), "TP_DOCUMENTO");
    		sql.addProjection("FDEST.SG_FILIAL","SG_FILIAL_DEST_CTE");
    		sql.addProjection("TRUNC(CAST(DS.DH_EMISSAO AS DATE))", "DH_EMISSAO_CTE");
    		sql.addProjection(this.subQueryProjectionTipoFrete(), "TP_FRETE_CTE");
    		sql.addProjection(this.subQueryProjectionTipoConhecimento(), "TP_CONHECIMENTO_CTE");
    		sql.addProjection(this.subQueryProjectionTipoCalculo(), "TP_CALCULO");
    		sql.addProjection("DS.DT_PREV_ENTREGA", "DT_PREVISTA_ENTREGA");
    		sql.addProjection(this.subQueryProjectionDataEntrega(), "DT_ENTREGA");
    		sql.addProjection("DS.NR_DIAS_REAL_ENTREGA", "NR_DIAS_REAL_ENTREGA");
    		sql.addProjection("DS.VL_MERCADORIA", "VL_MERCADORIA");
    		sql.addProjection("DS.VL_IMPOSTO", "VL_IMPOSTO");
    		sql.addProjection("DS.VL_ICMS_ST", "VL_ICMS_ST");
    		sql.addProjection("DS.VL_FRETE_LIQUIDO", "VL_FRETE_LIQUIDO");
    		sql.addProjection("DEV.VL_DEVIDO", "VL_DEVIDO");
    		sql.addProjection(this.subQueryProjectionSituacaoCobrancaFrete(),"TP_SITUACAO_COBRANCA_FRETE");
    		sql.addProjection("DEV.DT_LIQUIDACAO","DT_LIQUIDACAO");
    		sql.addProjection("DS.PS_AFORADO", "PS_AFORADO");
    		sql.addProjection("DS.PS_AFERIDO", "PS_AFERIDO");
    		sql.addProjection("DS.PS_REAL","PS_REAL");
    		sql.addProjection("DS.PS_CUBADO_DECLARADO", "PS_CUBADO_DECLARADO");
    		sql.addProjection("DS.PS_CUBADO_AFERIDO", "PS_CUBADO_AFERIDO");
    		sql.addProjection("DS.PS_CUBADO_REAL", "PS_CUBADO_REAL");
    		sql.addProjection("PREM.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_PREM");
    		sql.addProjection("PREM.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_PREM");
    		sql.addProjection("PREM.NM_PESSOA", "NM_PESSOA_PREM");
    		sql.addProjection("PDES.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_PDES");
    		sql.addProjection("PDES.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_PDES");
    		sql.addProjection("PDES.NM_PESSOA", "NM_PESSOA_PDES");
    		sql.addProjection("PDEV.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_PDEV");
    		sql.addProjection("PDEV.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_PDEV");
    		sql.addProjection("PDEV.NM_PESSOA", "NM_PESSOA_PDEV");
    		sql.addProjection(this.subQueryProjectionModal(), "MODAL");
    		sql.addProjection(this.subQueryProjectionAbrangencia(), "ABRANGENCIA");
    		sql.addProjection("VI18N(S.DS_SERVICO_I)", "SERVICO");
    	}
    	
		sql.addFrom("AWB", "AW");
		sql.addFrom("CIA_FILIAL_MERCURIO   CFM");
		sql.addFrom("PESSOA                PCIAAW");
		sql.addFrom("EMPRESA               ECIAAW");
		sql.addFrom("PESSOA                PEXP");
		sql.addFrom("PESSOA                PDESTAW");
		sql.addFrom("FILIAL                FORIGAW");
		sql.addFrom("FILIAL                FDESTAW");
		sql.addFrom("AEROPORTO             AEORIG");
		sql.addFrom("AEROPORTO             AEDEST");
		sql.addFrom("ITEM_FATURA_CIA_AEREA IFCA");
		sql.addFrom("FATURA_CIA_AEREA      FCA");
		sql.addFrom("PESSOA                PCIAFAT");
		sql.addFrom("CTO_AWB               CAW");
		sql.addFrom("DOCTO_SERVICO         DS");
		sql.addFrom("CONHECIMENTO          CO");
		sql.addFrom("FILIAL                FORIG");
		sql.addFrom("FILIAL                FDEST");
		sql.addFrom("DEVEDOR_DOC_SERV_FAT  DEV");
		sql.addFrom("DEVEDOR_DOC_SERV      DEVS");
		sql.addFrom("PESSOA                PREM");
		sql.addFrom("PESSOA                PDES");
		sql.addFrom("PESSOA                PDEV");
		sql.addFrom("SERVICO               S");
		sql.addFrom("NATUREZA_PRODUTO      NP");
		sql.addFrom("USUARIO      		   US");
		sql.addFrom("USUARIO      		   USI");
		
		sql.addJoin("AW.ID_CIA_FILIAL_MERCURIO", "CFM.ID_CIA_FILIAL_MERCURIO(+)");
		sql.addJoin("CFM.ID_EMPRESA", "ECIAAW.ID_EMPRESA(+)");
		sql.addJoin("CFM.ID_EMPRESA", "PCIAAW.ID_PESSOA(+)");
		sql.addJoin("AW.ID_AWB", "IFCA.ID_AWB(+)");
		sql.addJoin("IFCA.ID_FATURA_CIA_AEREA", "FCA.ID_FATURA_CIA_AEREA(+)");
		sql.addJoin("FCA.ID_CIA_AEREA", "PCIAFAT.ID_PESSOA(+)");
		sql.addJoin("AW.ID_AWB", "CAW.ID_AWB(+)");
		sql.addJoin("AW.ID_CLIENTE_EXPEDIDOR", "PEXP.ID_PESSOA(+)");
		sql.addJoin("AW.ID_CLIENTE_DESTINATARIO", "PDESTAW.ID_PESSOA(+)");
		sql.addJoin("AW.ID_FILIAL_ORIGEM", "FORIGAW.ID_FILIAL(+)");
		sql.addJoin("AW.ID_FILIAL_DESTINO", "FDESTAW.ID_FILIAL(+)");
		sql.addJoin("AW.ID_AEROPORTO_ORIGEM", "AEORIG.ID_AEROPORTO(+)");
		sql.addJoin("AW.ID_AEROPORTO_DESTINO", "AEDEST.ID_AEROPORTO(+)");
		sql.addJoin("CAW.ID_CONHECIMENTO", "DS.ID_DOCTO_SERVICO(+)");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "CO.ID_CONHECIMENTO(+)");
		sql.addJoin("DS.ID_FILIAL_ORIGEM", "FORIG.ID_FILIAL(+)");
		sql.addJoin("DS.ID_FILIAL_DESTINO", "FDEST.ID_FILIAL(+)");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "DEV.ID_DOCTO_SERVICO(+)");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "DEVS.ID_DOCTO_SERVICO(+)");
		sql.addJoin("DS.ID_CLIENTE_REMETENTE", "PREM.ID_PESSOA(+)");
		sql.addJoin("DS.ID_CLIENTE_DESTINATARIO", "PDES.ID_PESSOA(+)");
		sql.addJoin("DEVS.ID_CLIENTE", "PDEV.ID_PESSOA(+)");
		sql.addJoin("DS.ID_SERVICO", "S.ID_SERVICO(+)");
		sql.addJoin("CO.ID_NATUREZA_PRODUTO", "NP.ID_NATUREZA_PRODUTO(+)");
		sql.addJoin("AW.ID_USUARIO_JUSTIFICATIVA", "US.ID_USUARIO(+)");
		sql.addJoin("AW.ID_USUARIO_INCLUSAO", "USI.ID_USUARIO(+)");
    	
//		AW.ID_AWB = “AWB” (ID_ID_AWB)
		Long idAwb = parameters.getLong("idAwb");
		if(idAwb != null) {
			sql.addCriteria("AW.ID_AWB", "=", idAwb);
		}
		
//		AEORIG.ID_AEROPORTO = “Aeroporto de origem” (ID_AEROPORTO)
		Long idAeroportoOrigem = parameters.getLong("idAeroportoOrigem");
		if(idAeroportoOrigem != null) {
			sql.addCriteria("AEORIG.ID_AEROPORTO", "=", idAeroportoOrigem);
		}

//		AEDEST.ID_AEROPORTO = “Aeroporto de destino” (ID_AEROPORTO)
		Long idAeroportoDestino = parameters.getLong("idAeroportoDestino");
		if(idAeroportoDestino != null) {
			sql.addCriteria("AEDEST.ID_AEROPORTO", "=", idAeroportoDestino);
		}

//		FORIGAW.ID_FILIAL = “Filial de origem” (ID_FILIAL)
		Long idFilialOrigem = parameters.getLong("idFilialOrigem");
		if(idFilialOrigem != null) {
			sql.addCriteria("FORIGAW.ID_FILIAL", "=", idFilialOrigem);
		}
		
//		FDESTAW.ID_FILIAL = “Filial de destino” (ID_FILIAL)
		Long idFilialDestino = parameters.getLong("idFilialDestino");
		if(idFilialDestino != null) {
			sql.addCriteria("FDESTAW.ID_FILIAL", "=", idFilialDestino);
		}

//		PEXP.ID_PESSOA = “Expedidor” (ID_PESSOA)
		Long idPessoaExpedidor = parameters.getLong("idPessoaExpedidor");
		if (idPessoaExpedidor != null) {
			sql.addCriteria("PEXP.ID_PESSOA", "=", idPessoaExpedidor);
		}
		
//		PDESTAW.ID_PESSOA = “Destinatário” (ID_PESSOA)
		Long idPessoaDestinatario = parameters.getLong("idPessoaDestinatario");
		if (idPessoaDestinatario != null) {
			sql.addCriteria("PDESTAW.ID_PESSOA", "=", idPessoaDestinatario);
		}
		
//		PCIAFAT.ID_PESSOA = “Cia aérea fatura” (ID_PESSOA)
		Long idCiaAereaFatura = parameters.getLong("idCiaAereaFatura");
		if(idCiaAereaFatura != null) {
			sql.addCriteria("PCIAFAT.ID_PESSOA", "=", idCiaAereaFatura);
		}
		
//		CASE WHEN FCA.DT_PAGAMENTO IS NOT NULL THEN 'LI' WHEN FCA.DT_ENVIO_JDE IS NOT NULL THEN 'EJ' WHEN FCA.DT_EMISSAO IS NOT NULL THEN 'DI' ELSE NULL END = “Situação”
		String situacaoFaturaAereo = parameters.getString("situacaoFaturaAereo");
		if(situacaoFaturaAereo != null) {
			sql.addCriteria("CASE WHEN FCA.DT_PAGAMENTO IS NOT NULL THEN 'LI' WHEN FCA.DT_ENVIO_JDE IS NOT NULL THEN 'EJ' " +
					"WHEN FCA.DT_EMISSAO IS NOT NULL THEN 'DI' ELSE NULL END", "=", situacaoFaturaAereo);
		}

		String comDescontos = parameters.getString("comDescontos");
		if(comDescontos != null) {
			sql.addCustomCriteria(createCaseWhenOfComDesconto(domainValueService.findDomainValueByValue("DM_SIM_NAO", comDescontos), "VL_DESCONTO"));
		}
		
		String comAcrescimos = parameters.getString("comAcrescimos");
		if(comAcrescimos != null) {
			sql.addCustomCriteria(createCaseWhenOfComDesconto(domainValueService.findDomainValueByValue("DM_SIM_NAO", comAcrescimos), "VL_ACRESCIMO"));
		}
		
//		PCIAAW.ID_PESSOA = “Cia aérea AWB” (ID_PESSOA)
		Long idCiaAereaAwb = parameters.getLong("idCiaAereaAwb");
		if(idCiaAereaAwb != null) {
			sql.addCriteria("PCIAAW.ID_PESSOA", "=", idCiaAereaAwb);
		}
		
//		AW.TP_STATUS_AWB = “Situação AWB”
		String situacaoAwb = parameters.getString("situacaoAwb");
		if(situacaoAwb != null) {
			sql.addCriteria("AW.TP_STATUS_AWB", "=", situacaoAwb);
		}

//		CASE WHEN FCA.DT_PAGAMENTO IS NOT NULL THEN 'L' WHEN FCA.ID_FATURA_CIA_AEREA IS NOT NULL THEN 'F' ELSE 'P' END = “Situação pagamento”
		String situacaoPagamento  = parameters.getString("situacaoPagamento");
		if(situacaoPagamento != null) {
			sql.addCustomCriteria("CASE WHEN FCA.DT_PAGAMENTO IS NOT NULL THEN 'L' WHEN FCA.ID_FATURA_CIA_AEREA IS NOT NULL THEN 'F' ELSE 'P' END = '" + situacaoPagamento + "'");
		}
		
//		AW.TP_AWB = “Tipo de AWB”
		String tipoAwb = parameters.getString("tipoAwb");
		if(tipoAwb != null) {
			sql.addCriteria("AW.TP_AWB", "=", tipoAwb);
		}
		
//		FCA.DT_EMISSAO >= “Período de emissão (fatura)” (inicial)
		YearMonthDay dtEmissaoInicialFatura = parameters.getYearMonthDay("dtEmissaoInicial");
		if(dtEmissaoInicialFatura != null) {
			sql.addCustomCriteria("FCA.DT_EMISSAO >= TO_DATE('" + dtEmissaoInicialFatura + "','YYYY-MM-DD')");
		}
		
//		FCA.DT_EMISSAO <= “Período de emissão (fatura)” (final)
		YearMonthDay dtEmissaoFinalFatura = parameters.getYearMonthDay("dtEmissaoFinal");
		if(dtEmissaoFinalFatura != null) {
			sql.addCustomCriteria("FCA.DT_EMISSAO <= TO_DATE('" + dtEmissaoFinalFatura + "','YYYY-MM-DD')");
		}
		
//		FCA.DT_VENCIMENTO >= “Período de vencimento” (inicial)
		YearMonthDay dtVencimentoInicial = parameters.getYearMonthDay("dtVencimentoInicial");
		if(dtVencimentoInicial != null) {
			sql.addCustomCriteria("TRUNC(FCA.DT_VENCIMENTO) >= TO_DATE('" + dtVencimentoInicial + "','YYYY-MM-DD')");
		}
		
//		FCA.DT_VENCIMENTO <= “Período de vencimento” (final)
		YearMonthDay dtVencimentoFinal = parameters.getYearMonthDay("dtVencimentoFinal");
		if(dtVencimentoFinal != null) {
			sql.addCustomCriteria("TRUNC(FCA.DT_VENCIMENTO) <= TO_DATE('" + dtVencimentoFinal + "','YYYY-MM-DD')");
		}

		YearMonthDay dtFaturamentoInicial = parameters.getYearMonthDay("dtFaturamentoInicial");
		YearMonthDay dtFaturamentoFinal = parameters.getYearMonthDay("dtFaturamentoFinal");
		
//		(FCA.DT_PERIODO_INICIAL, FCA.DT_PERIODO_FINAL) OVERLAPS (“Período de faturamento” (inicial), “Período de faturamento” (final))
		if(dtFaturamentoInicial != null && dtFaturamentoFinal != null) {
			StringBuilder overlaps = new StringBuilder();
			overlaps.append("(NVL(FCA.DT_PERIODO_INICIAL, TO_DATE('01/01/1900', 'dd/mm/yyyy')), NVL(FCA.DT_PERIODO_FINAL, TO_DATE('01/01/4000', 'dd/mm/yyyy')))");
			overlaps.append(" OVERLAPS (TO_DATE('" + dtFaturamentoInicial + "','YYYY-MM-DD'), TO_DATE('" + dtFaturamentoFinal + "','YYYY-MM-DD'))");
			sql.addCustomCriteria(overlaps.toString());
		} else if(dtFaturamentoInicial != null) {//FCA.DT_PERIODO_INICIAL >= “Período de faturamento” (inicial)
			sql.addCustomCriteria("TRUNC(FCA.DT_PERIODO_INICIAL) >= TO_DATE('" + dtFaturamentoInicial + "','YYYY-MM-DD')");
		} else if(dtFaturamentoFinal != null) {//FCA.DT_PERIODO_FINAL <= “Período de faturamento” (final)
			sql.addCustomCriteria("TRUNC(FCA.DT_PERIODO_FINAL) <= TO_DATE('" + dtFaturamentoFinal + "','YYYY-MM-DD')");
		}
		
//		FCA.DT_ENVIO_JDE >= “Período de envio ao JDE” (inicial)
		YearMonthDay dtEnvioJDEInicial = parameters.getYearMonthDay("dtEnvioJDEInicial");
		if(dtEnvioJDEInicial != null) {
			sql.addCustomCriteria("TRUNC(FCA.DT_ENVIO_JDE) >= TO_DATE('" + dtEnvioJDEInicial + "','YYYY-MM-DD')");
		}
		
//		FCA.DT_ENVIO_JDE <= “Período de envio ao JDE” (final)
		YearMonthDay dtEnvioJDEFinal = parameters.getYearMonthDay("dtEnvioJDEFinal");
		if(dtEnvioJDEFinal != null) {
			sql.addCustomCriteria("TRUNC(FCA.DT_ENVIO_JDE) <= TO_DATE('" + dtEnvioJDEFinal + "','YYYY-MM-DD')");
		}
		
//		FCA.DT_PAGAMENTO >= “Período de pagamento” (inicial)
		YearMonthDay dtPagamentoInicial = parameters.getYearMonthDay("dtPagamentoInicial");
		if(dtPagamentoInicial != null) {
			sql.addCustomCriteria("TRUNC(FCA.DT_PAGAMENTO) >= TO_DATE('" + dtPagamentoInicial + "','YYYY-MM-DD')");
		}
		
//		FCA.DT_PAGAMENTO <= “Período de pagamento” (final)
		YearMonthDay dtPagamentoFinal = parameters.getYearMonthDay("dtPagamentoFinal");
		if(dtPagamentoFinal != null) {
			sql.addCustomCriteria("TRUNC(FCA.DT_PAGAMENTO) <= TO_DATE('" + dtPagamentoFinal + "','YYYY-MM-DD')");
		}		
		
//		FCA.NR_FATURA_CIA_AEREA >= “Número da fatura” (inicial)
		Long nrFaturaInicial = parameters.getLong("nrFaturaInicial");
		if(nrFaturaInicial != null) {
			sql.addCriteria("FCA.NR_FATURA_CIA_AEREA", ">=", nrFaturaInicial);
		}
//		FCA.NR_FATURA_CIA_AEREA <= “Número da fatura” (final)
		Long nrFaturaFinal = parameters.getLong("nrFaturaFinal");
		if(nrFaturaFinal != null) {
			sql.addCriteria("FCA.NR_FATURA_CIA_AEREA", "<=", nrFaturaFinal);
		}		

//		TRUNC(AW.DH_EMISSAO) >= “Período de emissão (AWB)” (inicial)
		YearMonthDay dtEmissaoAWBInicial = parameters.getYearMonthDay("dtEmissaoAWBInicial");
		if(dtEmissaoAWBInicial != null) {
			sql.addCustomCriteria("TRUNC(AW.DH_EMISSAO) >= TO_DATE('" + dtEmissaoAWBInicial + "','YYYY-MM-DD')");
		}
//		TRUNC(AW.DH_EMISSAO) <= “Período de emissão (AWB)” (final)
		YearMonthDay dtEmissaoAWBFinal = parameters.getYearMonthDay("dtEmissaoAWBFinal");
		if(dtEmissaoAWBFinal != null) {
			sql.addCustomCriteria("TRUNC(AW.DH_EMISSAO) <= TO_DATE('" + dtEmissaoAWBFinal + "','YYYY-MM-DD')");
		}		
		
//		TRUNC(AW.DH_EMISSAO) >= “Data inicial de corte”
		YearMonthDay dtInicialCorte = parameters.getYearMonthDay("dtInicialCorte");
		if(dtInicialCorte != null) {
			sql.addCustomCriteria("TRUNC(AW.DH_EMISSAO) >= TO_DATE('" + dtInicialCorte + "','YYYY-MM-DD')");
		}
		
//		“Cia aérea fatura”,
//		“Número”
		if(isListarFaturas) {
			sql.addOrderBy("PCIAFAT.NM_PESSOA");
			sql.addOrderBy("FCA.NR_FATURA_CIA_AEREA");
		}
		
//		“Cia aérea AWB”,
//		“Série”,
//		“Número”
		if(isListarAwbs) {
			sql.addOrderBy("AW.DS_SERIE");
			sql.addOrderBy("AW.NR_AWB || DECODE(AW.DV_AWB, NULL, '', '-' || AW.DV_AWB)");
		}
		
//		“Filial de origem CTRC”,
//		“Tipo documento”,
//		“Número”,
//		“Data de emissão”
		if(isListarCtes) {
			sql.addOrderBy("FORIG.SG_FILIAL");
			sql.addOrderBy("DS.NR_DOCTO_SERVICO");
			sql.addOrderBy("TP_DOCUMENTO");
			sql.addOrderBy("TRUNC(CAST(DS.DH_EMISSAO AS DATE))");
		}
    	
        return sql;
    }

	private String subQueryProjectionPercetualRentabilidade() {
		StringBuilder subQuery = new StringBuilder();
    	
		subQuery.append(" (CASE ");
		subQuery.append(" WHEN AW.VL_FRETE <> 0 THEN ");
    	subQuery.append(" 			(SELECT SUM (DOSE.VL_FRETE_LIQUIDO) ");
    	subQuery.append(" 			FROM DOCTO_SERVICO DOSE, CTO_AWB CA ");
    	subQuery.append(" 			WHERE DOSE.ID_DOCTO_SERVICO = CA.ID_CONHECIMENTO ");
    	subQuery.append(" 			AND CA.ID_AWB = AW.ID_AWB) ");
    	subQuery.append(" 			/ ");
    	subQuery.append(" 			AW.VL_FRETE * 100 ");
    	subQuery.append(" ELSE 0  ");
    	subQuery.append(" END) ");
    	
    	return subQuery.toString();
	}

	/**
	 * Layout do relátorio exibe os parâmetros utilizados na pesquisa em várias
	 * linhas, devido a isto não foi possível utlizar o método do framework
	 * sql.getFilterSummary(). 
	 * 
	 * @param tfm
	 * @param parametersReport
	 */
	private void preencherParametrosPesquisa(TypedFlatMap tfm, Map<String, Object> parametersReport) {
		
//		Cia aérea fatura
		Long idCiaAereaFatura = tfm.getLong("idCiaAereaFatura");
		if(idCiaAereaFatura != null) {
			parametersReport.put("PP_CIA_AEREA_FATURA", empresaService.findByIdDetalhamento(idCiaAereaFatura).getPessoa().getNmPessoa());
		}		

//		Número da fatura
		Long nrFaturaInicial = tfm.getLong("nrFaturaInicial");
		if(nrFaturaInicial != null) {
			parametersReport.put("PP_NR_FATURA_INICIAL", nrFaturaInicial);
		}
//		Número da fatura
		Long nrFaturaFinal = tfm.getLong("nrFaturaFinal");
		if(nrFaturaFinal != null) {
			parametersReport.put("PP_NR_FATURA_FINAL", nrFaturaFinal);
		}			

//		Situação (Fatura Aereo)
		String situacaoFaturaAereo  = tfm.getString("situacaoFaturaAereo");
		if(situacaoFaturaAereo != null) {
			DomainValue situacaoFaturaAereoDm = domainValueService.findDomainValueByValue("DM_SITUACAO_FATURA_AEREO", situacaoFaturaAereo);
			parametersReport.put("PP_SITUACAO_FATURA", situacaoFaturaAereoDm.getDescriptionAsString());
		}
		
//		Com Descontos
		String comDescontos = tfm.getString("comDescontos");
		if(comDescontos != null) {
			DomainValue comDescontoDm = domainValueService.findDomainValueByValue("DM_SIM_NAO", comDescontos);
			parametersReport.put("PP_COM_DESCONTO", comDescontoDm.getDescriptionAsString());
		}		
		
//		Com Acréscimos
		String comAcrescimos = tfm.getString("comAcrescimos");
		if(comAcrescimos != null) {
			DomainValue comAcrescimoDm = domainValueService.findDomainValueByValue("DM_SIM_NAO", comAcrescimos);
			parametersReport.put("PP_COM_ACRESCIMO", comAcrescimoDm.getDescriptionAsString());
		}		
		
//		Período de emissão (fatura) (inicial)
		YearMonthDay dtEmissaoInicialFatura = tfm.getYearMonthDay("dtEmissaoInicial");
		if(dtEmissaoInicialFatura != null) {
			parametersReport.put("PP_DATA_EMISSAO_FAT_INICIAL", JTFormatUtils.format(dtEmissaoInicialFatura));
		}
		
//		Período de emissão (fatura) (final)
		YearMonthDay dtEmissaoFinalFatura = tfm.getYearMonthDay("dtEmissaoFinal");
		if(dtEmissaoFinalFatura != null) {
			parametersReport.put("PP_DATA_EMISSAO_FAT_FINAL", JTFormatUtils.format(dtEmissaoFinalFatura));
		}		
		
//		Período de vencimento (inicial)
		YearMonthDay dtVencimentoInicial = tfm.getYearMonthDay("dtVencimentoInicial");
		if(dtVencimentoInicial != null) {
			parametersReport.put("PP_DATA_VECTO_FAT_INICIAL", JTFormatUtils.format(dtVencimentoInicial));
		}
		
//		Período de vencimento (final)
		YearMonthDay dtVencimentoFinal = tfm.getYearMonthDay("dtVencimentoFinal");
		if(dtVencimentoFinal != null) {
			parametersReport.put("PP_DATA_VECTO_FAT_FINAL", JTFormatUtils.format(dtVencimentoFinal));
		}

		YearMonthDay dtFaturamentoInicial = tfm.getYearMonthDay("dtFaturamentoInicial");
//		Período de faturamento (inicial)
		if(dtFaturamentoInicial != null) {
			parametersReport.put("PP_DATA_FATURAMENTO_INICIAL", JTFormatUtils.format(dtFaturamentoInicial));
		}
		
//		Período de faturamento (final)
		YearMonthDay dtFaturamentoFinal = tfm.getYearMonthDay("dtFaturamentoFinal");
		if(dtFaturamentoFinal != null) {
			parametersReport.put("PP_DATA_FATURAMENTO_FINAL", JTFormatUtils.format(dtFaturamentoFinal));
		}
		
//		Período de envio ao JDE (inicial)
		YearMonthDay dtEnvioJDEInicial = tfm.getYearMonthDay("dtEnvioJDEInicial");
		if(dtEnvioJDEInicial != null) {
			parametersReport.put("PP_DATA_ENVIO_JDE_INICIAL", JTFormatUtils.format(dtEnvioJDEInicial));
		}
		
//		Período de envio ao JDE (final)
		YearMonthDay dtEnvioJDEFinal = tfm.getYearMonthDay("dtEnvioJDEFinal");
		if(dtEnvioJDEFinal != null) {
			parametersReport.put("PP_DATA_ENVIO_JDE_FINAL", JTFormatUtils.format(dtEnvioJDEFinal));
		}
		
//		Período de pagamento (inicial)
		YearMonthDay dtPagamentoInicial = tfm.getYearMonthDay("dtPagamentoInicial");
		if(dtPagamentoInicial != null) {
			parametersReport.put("PP_DATA_PGTO_INICIAL", JTFormatUtils.format(dtPagamentoInicial));
		}
//		Período de pagamento (final)
		YearMonthDay dtPagamentoFinal = tfm.getYearMonthDay("dtPagamentoFinal");
		if(dtPagamentoFinal != null) {
			parametersReport.put("PP_DATA_PGTO_FINAL", JTFormatUtils.format(dtPagamentoFinal));
		}		

//		Cia aérea AWB
		Long idCiaAereaAwb = tfm.getLong("idCiaAereaAwb");
		if(idCiaAereaAwb != null) {
			parametersReport.put("PP_CIA_AEREA_AWB", empresaService.findByIdDetalhamento(idCiaAereaAwb).getPessoa().getNmPessoa());
		}
		
//		AWB (Série/Número)
		Long idAwb = tfm.getLong("idAwb");
		if(idAwb != null) {
			Awb awb = awbService.findById(idAwb);
			parametersReport.put("PP_NUMERO_AWB", AwbUtils.getSgEmpresaAndNrAwbFormated(awb));
		}
		
//		AEORIG.ID_AEROPORTO = “Aeroporto de origem” (ID_AEROPORTO)
		Long idAeroportoOrigem = tfm.getLong("idAeroportoOrigem");
		if(idAeroportoOrigem != null) {
			Aeroporto aeroportoOrig = aeroportoService.findById(idAeroportoOrigem);
			parametersReport.put("PP_SG_FILIAL_AEROPORTO_ORIG", aeroportoOrig.getSgAeroporto());
			parametersReport.put("PP_NM_FILIAL_AEROPORTO_ORIG", aeroportoOrig.getPessoa().getNmPessoa());
		}

//		AEDEST.ID_AEROPORTO = “Aeroporto de destino” (ID_AEROPORTO)
		Long idAeroportoDestino = tfm.getLong("idAeroportoDestino");
		if(idAeroportoDestino != null) {
			Aeroporto aeroportoDest = aeroportoService.findById(idAeroportoDestino);
			parametersReport.put("PP_SG_FILIAL_AEROPORTO_DEST", aeroportoDest.getSgAeroporto());
			parametersReport.put("PP_NM_FILIAL_AEROPORTO_DEST", aeroportoDest.getPessoa().getNmPessoa());			
		}

//		Situação AWB
		String situacaoAwb = tfm.getString("situacaoAwb");
		if(situacaoAwb != null) {
			DomainValue situacaoAwbDm = domainValueService.findDomainValueByValue("DM_STATUS_AWB", situacaoAwb);
			parametersReport.put("PP_SITUACAO_AWB", situacaoAwbDm.getDescriptionAsString());
		}		

//		Expedidor
		Long idPessoaExpedidor = tfm.getLong("idPessoaExpedidor");
		if (idPessoaExpedidor != null) {
			Pessoa expedidor = pessoaService.findById(idPessoaExpedidor);
			parametersReport.put("PP_NR_IDENTIFICACAO_EXP", FormatUtils.formatIdentificacao(expedidor.getTpIdentificacao().getValue(), expedidor.getNrIdentificacao()));
			parametersReport.put("PP_NM_IDENTIFICACAO_EXP", expedidor.getNmPessoa());
		}
		
//		Destinatário
		Long idPessoaDestinatario = tfm.getLong("idPessoaDestinatario");
		if (idPessoaDestinatario != null) {
			Pessoa destinatario = pessoaService.findById(idPessoaDestinatario);
			parametersReport.put("PP_NR_IDENTIFICACAO_DEST", FormatUtils.formatIdentificacao(destinatario.getTpIdentificacao().getValue(), destinatario.getNrIdentificacao()));
			parametersReport.put("PP_NM_IDENTIFICACAO_DEST", destinatario.getNmPessoa());			
		}	
		
//		TRUNC(AW.DH_EMISSAO) >= “Período de emissão (AWB)” (inicial)
		YearMonthDay dtEmissaoAWBInicial = tfm.getYearMonthDay("dtEmissaoAWBInicial");
		if(dtEmissaoAWBInicial != null) {
			parametersReport.put("PP_DATA_EMISSAO_AWB_INICIAL", JTFormatUtils.format(dtEmissaoAWBInicial));
		}
		
//		TRUNC(AW.DH_EMISSAO) <= “Período de emissão (AWB)” (final)
		YearMonthDay dtEmissaoAWBFinal = tfm.getYearMonthDay("dtEmissaoAWBFinal");
		if(dtEmissaoAWBFinal != null) {
			parametersReport.put("PP_DATA_EMISSAO_AWB_FINAL", JTFormatUtils.format(dtEmissaoAWBFinal));
		}		
		
//		TRUNC(AW.DH_EMISSAO) >= “Data inicial de corte”
		YearMonthDay dtInicialCorte = tfm.getYearMonthDay("dtInicialCorte");
		if(dtInicialCorte != null) {
			parametersReport.put("PP_DATA_INICIAL_CORTE", JTFormatUtils.format(dtInicialCorte));
		}

//		Filial de origem
		Long idFilialOrigem = tfm.getLong("idFilialOrigem");
		if(idFilialOrigem != null) {
			Filial filialOrigem = filialService.findById(idFilialOrigem);
			parametersReport.put("PP_SG_FILIAL_ORIG", filialOrigem.getSgFilial());
			parametersReport.put("PP_NM_FILIAL_ORIG", filialOrigem.getPessoa().getNmFantasia());
		}
		
//		Filial de destino
		Long idFilialDestino = tfm.getLong("idFilialDestino");
		if(idFilialDestino != null) {
			Filial filialDestino = filialService.findById(idFilialDestino);
			parametersReport.put("PP_SG_FILIAL_DEST", filialDestino.getSgFilial());
			parametersReport.put("PP_NM_FILIAL_DEST", filialDestino.getPessoa().getNmFantasia());
		}

//		Situação pagamento
		String situacaoPagamento  = tfm.getString("situacaoPagamento");
		if(situacaoPagamento != null) {
			DomainValue situacaoPagtoDm = domainValueService.findDomainValueByValue("DM_SITUACAO_PAGAMENTO_AWB", situacaoPagamento);
			parametersReport.put("PP_SITUACAO_PAGTO", situacaoPagtoDm.getDescriptionAsString());
		}
		
		
//		AW.TP_AWB = “Tipo de AWB”
		String tipoAwb = tfm.getString("tipoAwb");
		if(tipoAwb != null) {
			DomainValue tipoAwbDm = domainValueService.findDomainValueByValue("DM_TIPO_AWB", tipoAwb);
			parametersReport.put("PP_TIPO_AWB", tipoAwbDm.getDescriptionAsString());
		}
		
	}
    
    private String subQueryProjectionTipoConhecimento() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("    FROM VALOR_DOMINIO");
    	subQuery.append("   	WHERE ID_DOMINIO IN");
    	subQuery.append("         (SELECT ID_DOMINIO");
    	subQuery.append("            FROM DOMINIO");
    	subQuery.append("           	WHERE NM_DOMINIO = 'DM_TIPO_CONHECIMENTO')");
    	subQuery.append("     	AND VL_VALOR_DOMINIO = CO.TP_CONHECIMENTO)");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionTipoFrete() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("    FROM VALOR_DOMINIO");
    	subQuery.append("		WHERE ID_DOMINIO IN");
    	subQuery.append("			(SELECT ID_DOMINIO");
    	subQuery.append("            	FROM DOMINIO");
    	subQuery.append("           	WHERE NM_DOMINIO = 'DM_TIPO_FRETE')");
    	subQuery.append("		AND VL_VALOR_DOMINIO = CO.TP_FRETE)");
    	
    	return subQuery.toString();
    }    

    private String subQueryProjectionAbrangencia() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("   FROM VALOR_DOMINIO");
    	subQuery.append("  	WHERE ID_DOMINIO IN");
    	subQuery.append("		(SELECT ID_DOMINIO");
    	subQuery.append("			FROM DOMINIO");
    	subQuery.append("          	WHERE NM_DOMINIO = 'DM_ABRANGENCIA')");
    	subQuery.append("    AND VL_VALOR_DOMINIO = S.TP_ABRANGENCIA)");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionModal() {
    	StringBuilder subQuery = new StringBuilder();

    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("    FROM VALOR_DOMINIO");
    	subQuery.append("   WHERE ID_DOMINIO IN");
    	subQuery.append("         (SELECT ID_DOMINIO");
    	subQuery.append("            FROM DOMINIO");
    	subQuery.append("           WHERE NM_DOMINIO = 'DM_MODAL')");
    	subQuery.append("    AND VL_VALOR_DOMINIO = S.TP_MODAL)");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionSituacaoCobrancaFrete() {
    	StringBuilder subQuery = new StringBuilder();

    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("    FROM VALOR_DOMINIO");
    	subQuery.append("   WHERE ID_DOMINIO IN");
    	subQuery.append("         (SELECT ID_DOMINIO");
    	subQuery.append("            FROM DOMINIO");
    	subQuery.append("           WHERE NM_DOMINIO =");
    	subQuery.append("                 'DM_STATUS_COBRANCA_DOCTO_SERVICO')");
    	subQuery.append("     AND VL_VALOR_DOMINIO = DEV.TP_SITUACAO_COBRANCA)");
    	
    	return subQuery.toString();
    }
    	
    private String subQueryProjectionDataEntrega() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT TRUNC(CAST(MAX(MED.DH_OCORRENCIA) AS DATE)) DH_OCORRENCIA");
    	subQuery.append("    		   FROM MANIFESTO_ENTREGA_DOCUMENTO MED");
    	subQuery.append("    		  WHERE MED.ID_OCORRENCIA_ENTREGA = 5");
    	subQuery.append("    		    AND MED.TP_SITUACAO_DOCUMENTO <> 'CANC'");
    	subQuery.append("    		    AND MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO)");
    	
    	return subQuery.toString();
    }
    	
    private String subQueryProjectionTipoCalculo() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("    FROM VALOR_DOMINIO");
    	subQuery.append("	WHERE ID_DOMINIO IN");
    	subQuery.append("		(SELECT ID_DOMINIO");
    	subQuery.append("          	FROM DOMINIO");
    	subQuery.append("  		WHERE NM_DOMINIO = 'DM_TIPO_CALCULO_FRETE')");
    	subQuery.append("	AND VL_VALOR_DOMINIO = DS.TP_CALCULO_PRECO)");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionTipoDocumento() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("    FROM VALOR_DOMINIO");
    	subQuery.append("   	WHERE ID_DOMINIO IN");
    	subQuery.append("         (SELECT ID_DOMINIO");
    	subQuery.append("            FROM DOMINIO");
    	subQuery.append("           	WHERE NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO')");
    	subQuery.append("   	 AND VL_VALOR_DOMINIO = DS.TP_DOCUMENTO_SERVICO)");
    	
    	return subQuery.toString();
    }    
    
    private String subQueryProjectionValorReceitaAwb() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("CAST(NVL((SELECT SUM(DS2.VL_FRETE_LIQUIDO)");
    	subQuery.append("			FROM CTO_AWB       CAW2,");
    	subQuery.append("				 DOCTO_SERVICO DS2");
    	subQuery.append("			WHERE CAW2.ID_AWB = AW.ID_AWB");
    	subQuery.append("			AND CAW2.ID_CONHECIMENTO = DS2.ID_DOCTO_SERVICO),");
    	subQuery.append("				0) AS NUMBER(18, 2))");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionSituacaoPagamentoAwb() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("	FROM VALOR_DOMINIO");
    	subQuery.append("	WHERE ID_DOMINIO IN");
    	subQuery.append("		(SELECT ID_DOMINIO");
    	subQuery.append("			FROM DOMINIO");
    	subQuery.append("			WHERE NM_DOMINIO = 'DM_SITUACAO_PAGAMENTO_AWB')");
    	subQuery.append("	AND VL_VALOR_DOMINIO = CASE WHEN");
    	subQuery.append("	FCA.DT_PAGAMENTO IS NOT NULL THEN 'L' WHEN FCA.ID_FATURA_CIA_AEREA IS NOT NULL THEN 'F' ELSE 'P' END)");
    	
    	return subQuery.toString();
    }
    private String subQueryProjectionTipoAwb() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("	FROM VALOR_DOMINIO");
    	subQuery.append("	WHERE ID_DOMINIO IN");
    	subQuery.append("		(SELECT ID_DOMINIO");
    	subQuery.append("			FROM DOMINIO");
    	subQuery.append("			WHERE NM_DOMINIO = 'DM_TIPO_AWB')");
    	subQuery.append("	AND VL_VALOR_DOMINIO = AW.TP_AWB)");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionSituacaoAwb() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append("	FROM VALOR_DOMINIO");
    	subQuery.append("	WHERE ID_DOMINIO IN");
    	subQuery.append("		(SELECT ID_DOMINIO");
    	subQuery.append("			FROM DOMINIO");
    	subQuery.append("			WHERE NM_DOMINIO = 'DM_STATUS_AWB')");
    	subQuery.append("		AND VL_VALOR_DOMINIO = AW.TP_STATUS_AWB)");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionValorReceita() {
    	StringBuilder subQuery = new StringBuilder();
    	subQuery.append("CAST(NVL((SELECT SUM(DS2.VL_FRETE_LIQUIDO)");
    	subQuery.append("	FROM ITEM_FATURA_CIA_AEREA IFCA2,");
    	subQuery.append("		AWB AW2,");
    	subQuery.append("		CTO_AWB CAW2,");
    	subQuery.append("		DOCTO_SERVICO DS2");
    	subQuery.append("	WHERE IFCA2.ID_FATURA_CIA_AEREA =");
    	subQuery.append("		FCA.ID_FATURA_CIA_AEREA");
    	subQuery.append("		AND IFCA2.ID_AWB = AW2.ID_AWB");
    	subQuery.append("		AND AW2.ID_AWB = CAW2.ID_AWB");
    	subQuery.append("		AND CAW2.ID_CONHECIMENTO = DS2.ID_DOCTO_SERVICO),");
    	subQuery.append("		0) AS NUMBER(18, 2))");
    	
    	
    	return subQuery.toString();
    }
    private String subQueryProjectionTotalIcms() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("CAST(NVL((SELECT SUM(AW2.VL_ICMS)");
    	subQuery.append("	FROM ITEM_FATURA_CIA_AEREA IFCA2,");
    	subQuery.append("	AWB AW2");
    	subQuery.append("	WHERE IFCA2.ID_FATURA_CIA_AEREA =");
    	subQuery.append("		FCA.ID_FATURA_CIA_AEREA");
    	subQuery.append("		AND IFCA2.ID_AWB = AW2.ID_AWB),");
    	subQuery.append("		0) AS NUMBER(18, 2))");
    	
    	return subQuery.toString();
    }
    private String subQueryProjectionTotalAcrescimo() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("CAST(NVL(FCA.VL_ACRESCIMO, 0) +");
    	subQuery.append("	NVL((SELECT SUM(IFCA2.VL_ACRESCIMO)");
    	subQuery.append("		FROM ITEM_FATURA_CIA_AEREA IFCA2");
    	subQuery.append("		WHERE IFCA2.ID_FATURA_CIA_AEREA =");
    	subQuery.append("			FCA.ID_FATURA_CIA_AEREA),");
    	subQuery.append("			0) AS NUMBER(18, 2))");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionTotalDesconto() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("CAST(NVL(FCA.VL_DESCONTO, 0) +");
    	subQuery.append("	NVL((SELECT SUM(IFCA2.VL_DESCONTO)");
    	subQuery.append("		FROM ITEM_FATURA_CIA_AEREA IFCA2");
    	subQuery.append("		WHERE IFCA2.ID_FATURA_CIA_AEREA =");
    	subQuery.append("			FCA.ID_FATURA_CIA_AEREA),");
    	subQuery.append("			0) AS NUMBER(18, 2))");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionTotalFrete() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("CAST(NVL((SELECT SUM(AW2.VL_FRETE)");
    	subQuery.append(" FROM ITEM_FATURA_CIA_AEREA IFCA2,");
    	subQuery.append("	AWB AW2");
    	subQuery.append(" WHERE IFCA2.ID_FATURA_CIA_AEREA =");
    	subQuery.append(" FCA.ID_FATURA_CIA_AEREA");
    	subQuery.append(" AND IFCA2.ID_AWB = AW2.ID_AWB),");
    	subQuery.append(" 0) AS NUMBER(18, 2))");
    	
    	return subQuery.toString();
    }
    
    private String subQueryProjectionTotalPesoCobrado() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("CAST(NVL((SELECT SUM(IFCA2.PS_COBRADO)");
    	subQuery.append(" FROM ITEM_FATURA_CIA_AEREA IFCA2");
    	subQuery.append("	WHERE IFCA2.ID_FATURA_CIA_AEREA =");
    	subQuery.append("	FCA.ID_FATURA_CIA_AEREA),");
    	subQuery.append("	0) AS NUMBER(18, 3))");
    	
    	return subQuery.toString(); 
    }

    private String subQueryProjectionTotalPesoCubado() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("CAST(NVL((SELECT SUM(AW2.PS_CUBADO)");
    	subQuery.append(" FROM ITEM_FATURA_CIA_AEREA IFCA2,");
    	subQuery.append(" AWB AW2");
    	subQuery.append(" WHERE IFCA2.ID_FATURA_CIA_AEREA =");
    	subQuery.append(" 		FCA.ID_FATURA_CIA_AEREA");
    	subQuery.append("		AND IFCA2.ID_AWB = AW2.ID_AWB),");
    	subQuery.append(" 0) AS NUMBER(18, 3))");
    	
    	return subQuery.toString();
    }
    
    
    private String subQueryProjectionTotalPesoReal() {
      StringBuilder subQuery = new StringBuilder();
      
      subQuery.append("CAST(NVL((SELECT SUM(AW2.PS_TOTAL)");
      subQuery.append(" FROM ITEM_FATURA_CIA_AEREA IFCA2,");
      subQuery.append(" AWB AW2");
      subQuery.append(" WHERE IFCA2.ID_FATURA_CIA_AEREA =");
      subQuery.append(" FCA.ID_FATURA_CIA_AEREA");
      subQuery.append(" AND IFCA2.ID_AWB = AW2.ID_AWB),");
      subQuery.append(" 0) AS NUMBER(18, 3))");

      return subQuery.toString();
    }
    
    private String subQueryProjectionSituacaoFaturaAereo() {
    	StringBuilder subQuery = new StringBuilder();
    	
    	subQuery.append("(SELECT VI18N(DS_VALOR_DOMINIO_I)");
    	subQuery.append(" FROM VALOR_DOMINIO");
    	subQuery.append(" WHERE ID_DOMINIO IN");
    	subQuery.append(" (SELECT ID_DOMINIO");
    	subQuery.append("	FROM DOMINIO");
    	subQuery.append("	WHERE NM_DOMINIO = 'DM_SITUACAO_FATURA_AEREO')");
    	subQuery.append(" AND VL_VALOR_DOMINIO = CASE WHEN");
    	subQuery.append(" FCA.DT_PAGAMENTO IS NOT NULL THEN 'LI' WHEN FCA.DT_ENVIO_JDE IS NOT NULL THEN 'EJ' WHEN FCA.DT_EMISSAO IS NOT NULL THEN 'DI' ELSE NULL END)");
    	
    	return subQuery.toString();
    }
    
    
    private String createCaseWhenOfComDesconto(DomainValue filter, String tableColumnName) {
		StringBuilder caseComDesconto = new StringBuilder();
		caseComDesconto.append("CASE WHEN '" + filter.getValue() + "' = 'S' THEN");
		caseComDesconto.append(" CASE WHEN(FCA." + tableColumnName + " > 0 OR NVL((SELECT SUM(IFCA2." + tableColumnName + ") FROM ITEM_FATURA_CIA_AEREA IFCA2 WHERE IFCA2.ID_FATURA_CIA_AEREA = FCA.ID_FATURA_CIA_AEREA), 0) > 0) THEN");
		caseComDesconto.append(" 1");
		caseComDesconto.append(" ELSE");
		caseComDesconto.append(" 0");
		caseComDesconto.append(" END");
		caseComDesconto.append(" WHEN '" + filter.getValue() + "' = 'N' THEN");
		caseComDesconto.append(" CASE WHEN(FCA." + tableColumnName + " = 0 AND NVL((SELECT SUM(IFCA2." + tableColumnName + ") FROM ITEM_FATURA_CIA_AEREA IFCA2 WHERE IFCA2.ID_FATURA_CIA_AEREA = FCA.ID_FATURA_CIA_AEREA), 0) = 0) THEN");
		caseComDesconto.append(" 1");
		caseComDesconto.append(" ELSE");
		caseComDesconto.append(" 0");
		caseComDesconto.append(" END");
		caseComDesconto.append(" END = 1");
		
		return caseComDesconto.toString();
    }

	/**
	 * @return the faturaCiaAereaService
	 */
	public FaturaCiaAereaService getFaturaCiaAereaService() {
		return faturaCiaAereaService;
	}

	/**
	 * @param faturaCiaAereaService the faturaCiaAereaService to set
	 */
	public void setFaturaCiaAereaService(FaturaCiaAereaService faturaCiaAereaService) {
		this.faturaCiaAereaService = faturaCiaAereaService;
	}

	/**
	 * @return the empresaService
	 */
	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	/**
	 * @param empresaService the empresaService to set
	 */
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	/**
	 * @return the domainValueService
	 */
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	/**
	 * @param domainValueService the domainValueService to set
	 */
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	/**
	 * @return the awbService
	 */
	public AwbService getAwbService() {
		return awbService;
	}

	/**
	 * @param awbService the awbService to set
	 */
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	/**
	 * @return the aeroportoService
	 */
	public AeroportoService getAeroportoService() {
		return aeroportoService;
	}

	/**
	 * @param aeroportoService the aeroportoService to set
	 */
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}

	/**
	 * @return the pessoaService
	 */
	public PessoaService getPessoaService() {
		return pessoaService;
	}

	/**
	 * @param pessoaService the pessoaService to set
	 */
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	/**
	 * @return the filialService
	 */
	public FilialService getFilialService() {
		return filialService;
	}

	/**
	 * @param filialService the filialService to set
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
    
}