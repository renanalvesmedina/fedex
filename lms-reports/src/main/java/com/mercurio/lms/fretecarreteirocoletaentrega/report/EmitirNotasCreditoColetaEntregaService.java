package com.mercurio.lms.fretecarreteirocoletaentrega.report;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.DadoNotaCreditoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.DetalheNotaCreditoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.ManifestoColetaControleCargaNotaCreditoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.ManifestoColetaNotaCreditoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.ManifestoEntregaControleCargaNotaCreditoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.ManifestoEntregaNotaCreditoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.ParcelaTabelaCeNotaCreditoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.TabelaFreteNotaCreditoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.dto.TabelaNotaCreditoParcelasCeDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega.DM_TP_CALCULO_TABELA_COLETA_ENTREGA;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.EventoNotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoColetaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoDoctoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoPadraoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoParcelaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ParcelaTabelaCeService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TabelaColetaEntregaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.util.notacredito.report.NotaCreditoPadraoPDF;
import com.mercurio.lms.fretecarreteirocoletaentrega.utils.ConstantesEventosNotaCredito;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.PendenciaService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public class EmitirNotasCreditoColetaEntregaService extends
		ReportServiceSupport {

	private ConfiguracoesFacade configuracoesFacade;
	private ControleCargaService controleCargaService;
    private NotaCreditoService notaCreditoService;
    private NotaCreditoColetaService notaCreditoColetaService;
    private NotaCreditoDoctoService notaCreditoDoctoService;
    private FilialService filialService;
    private EnderecoPessoaService enderecoPessoaService;
    private ConversaoMoedaService conversaoMoedaService;
    private WorkflowPendenciaService workflowPendenciaService;
    private PendenciaService pendenciaService;
    private ManifestoEntregaService manifestoEntregaService;
    private DoctoServicoService doctoServicoService;
    private ParcelaTabelaCeService parcelaTabelaCeService;
    private ManifestoColetaService manifestoColetaService;
    private TabelaColetaEntregaService tabelaColetaEntregaService;
    private NotaCreditoParcelaService notaCreditoParcelaService;
    private PedidoColetaService pedidoColetaService;
    private EventoNotaCreditoService eventoNotaCreditoService;
    private OcorrenciaEntregaService ocorrenciaEntregaService;
    private NotaCreditoPadraoService notaCreditoPadraoService;
    private AwbService awbService;
	
    private static String PDF_REPORT_NAME = "com/mercurio/lms/fretecarreteirocoletaentrega/report/emitirNotaCredito.jasper";
    private static String EXCEL_REPORT_NAME = "com/mercurio/lms/fretecarreteirocoletaentrega/report/emitirNotaCreditoExcel.jasper";
    private static String TP_MANIFESTO_ENTREGA_PARCEIRA = "EP";

	public JRReportDataObject execute(Map parametersTemp) throws Exception {
    	final String tpViewReport = parametersTemp.get("tpViewReport")!=null?(String) parametersTemp.get("tpViewReport"):"EM";    	
    	Map reportParams = new HashMap();        
    	reportParams.put(JRReportDataObject.EXPORT_MODE_PARAM, getTipoReport(tpViewReport));
        return createReportDataObject(new JRMapCollectionDataSource(findDadosExecute(parametersTemp)),reportParams);
    }
    
    public TypedFlatMap findMapDadosExecute(Map parametersTemp) throws Exception {
    	List<TypedFlatMap> dados = findDadosExecute(parametersTemp);
    	TypedFlatMap result = null;
    	if( dados != null && !dados.isEmpty() ){
	    	result = dados.get(0);    	
	    	
    	DetalheNotaCreditoDto detalhe = (DetalheNotaCreditoDto)result.get("DETALHES_NOTA_CREDITO");
    	result.put("dsObsFormatada", detalhe.getObsFormatada());
    	result.put("vlAcrescimo", detalhe.getVlAcrescimo());
    	result.put("vlDesconto", detalhe.getVlDesconto());
    	result.put("vlDescUsoEquipamento", detalhe.getVlDescUsoEquipamento());
	    	
    	}
		return result;
    }
    
	private List findDadosExecute(Map parametersTemp) throws Exception {
		final TypedFlatMap parameters = (TypedFlatMap) parametersTemp;
		final String tpViewReport = parametersTemp.get("tpViewReport")!=null?(String) parametersTemp.get("tpViewReport"):"EM";
		
		if (!"EX".equals(tpViewReport) && !"VP".equals(tpViewReport)){
			if (parameters.getBoolean("skipValidations") == null || Boolean.FALSE.equals(parameters.getBoolean("skipValidations"))){
		validateParameters(parameters);
		validateManifestoEntrega(parameters);
			}
		}
		
		SqlTemplate sql = montaSql(parameters); 
		setReportName(PDF_REPORT_NAME);
    	if ("EX".equalsIgnoreCase(tpViewReport)){
    		setReportName(EXCEL_REPORT_NAME);
    	}
        List result = (List)getJdbcTemplate().query(sql.getSql(),JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()),new ResultSetExtractor() {

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List rows = new ArrayList();
				while(rs.next()) {
					
					if(rs.getString("TP_NOTA_CREDITO")!= null){
						//se contem tp_nota é calculo padrão não deve ser emitida por aqui;
						continue;
					}

					TypedFlatMap values = new TypedFlatMap();
				     
    				Long idNotaCredito = rs.getLong("ID_NOTA_CREDITO");
    				values.put("ID_NOTA_CREDITO", idNotaCredito);

    				values.put("COLETASPROGRAMADAS", rs.getLong("COLETASPROGRAMADAS"));
    				values.put("ENTREGASPROGRAMADAS", rs.getLong("ENTREGASPROGRAMADAS"));
    				values.put("COLETASEXECUTADAS", rs.getLong("COLETASEXECUTADAS"));
    				values.put("ENTREGASEXECUTADAS", rs.getLong("ENTREGASEXECUTADAS"));
    				values.put("COLETASCONTABILIZADAS", rs.getLong("COLETASCONTABILIZADAS"));
    				values.put("ENTREGASCONTABILIZADAS", rs.getLong("ENTREGASCONTABILIZADAS"));
    				
    				String tpCalculoNotaCredito = notaCreditoService.findTpCalculoNotaCredito(idNotaCredito);
					values.put("TP_CALCULO_PARCELA", tpCalculoNotaCredito);

					values.put("VL_TOTAL_NOTA_CREDITO", notaCreditoService.findValorTotalNotaCredito(idNotaCredito));

					DomainValue tpRegistroTabelaColetaNotaCredito = tabelaColetaEntregaService.findTpRegistroTabelaColetaEntregaByIdNotaCredito(idNotaCredito);

					values.put("TP_REGISTRO_TABELA", tpRegistroTabelaColetaNotaCredito == null
							? "" : tpRegistroTabelaColetaNotaCredito.getDescriptionAsString());

    				values.put("NR_FRANQUIA_KM", rs.getLong("NR_FRANQUIA_KM"));
    				values.put("NR_KM", rs.getLong("NR_KM"));

    				DadoNotaCreditoDto contagemEntregaColeta = notaCreditoService.findDadosColetaEntregaByIdNotaCredito(idNotaCredito);

    				values.put("QT_COLETAS_EXECUTADAS", contagemEntregaColeta.getQtColetasExecutadas());
    				values.put("QT_ENTREGAS_REALIZADAS", contagemEntregaColeta.getQtEntregasRealizadas());

    				values.put("ID_CONTROLE_CARGA", rs.getLong("ID_CONTROLE_CARGA"));

					 values.put("NR_NOTA_CREDITO",new StringBuffer(new DecimalFormat("000000000").format(rs.getDouble("NR_NOTA_CREDITO"))).insert(0," ").insert(0,rs.getString("SG_FILIAL")).toString());
					 values.put("NR_CAPACIDADE_PESO_FINAL",formatNumber(rs.getDouble("NR_CAPACIDADE_PESO_FINAL")));
					 values.put("PROGRAMADO",formatNumber(rs.getDouble("PROGRAMADO")));
					 Long idFilial = parameters.getLong("filial.idFilial");					 
					 DateTime dhEmissao = null;
					 
					 if (StringUtils.isNotBlank(rs.getString("DH_EMISSAO"))) {
						 dhEmissao = JTFormatUtils.buildDateTimeFromTimestampTzString(rs.getString("DH_EMISSAO"));
						 values.put("TP_EMISSAO_RELATORIO", getTpEmissaoRelatorio(tpViewReport, true));
					 }else{
						 dhEmissao = JTDateTimeUtils.getDataHoraAtual();
						 values.put("TP_EMISSAO_RELATORIO", getTpEmissaoRelatorio(tpViewReport, false));

    					if(!tpViewReport.equals("VP") && !"EX".equals(tpViewReport)) {
						 Long idPendencia = (rs.getLong("ID_PENDENCIA") == 0) ? null : Long.valueOf(rs.getLong("ID_PENDENCIA"));
								 
						 if (idPendencia != null) {
							 Pendencia pendencia = pendenciaService.findById(idPendencia);
							 if (pendencia.getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento().equals(ConstantesWorkflow.NR2504_CTO_MAIOR_VLR)) {
								 if (pendencia.getTpSituacaoPendencia().getValue().equals("E"))
									 continue;
							 }else{								 
								 BigDecimal desconto = parameters.getBigDecimal("vlDesconto");
								 if( desconto != null && pendencia.getTpSituacaoPendencia().getValue().equals("E") ){
									 continue;
							 }
							}
						 }else{
							 BigDecimal total = notaCreditoService.findValorTotalNotaCredito(idNotaCredito);
							 BigDecimal vlMax = (BigDecimal)configuracoesFacade.getValorParametro(idFilial,"VALOR_MAXIMO_NOTA_CR");
							 if (vlMax != null && vlMax.compareTo(total) < 0)
								 continue;
						 }

						 NotaCredito bean = notaCreditoService.findById(idNotaCredito);
						 bean.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
						 notaCreditoService.store(bean);
    							
    							eventoNotaCreditoService.storeEventoNotaCredito(bean, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_EMITIDO);
					 }
					 }

					 dhEmissao = dhEmissao.toDateTime(DateTimeZone.forID(rs.getString("DS_TIMEZONE")));
					 
    				List<ControleCarga> controlesCargas = Collections.emptyList();
    				if (idNotaCredito != null) {
    					controlesCargas = controleCargaService.findControlesByIdNotaCredito(idNotaCredito);
    				}

					 BigDecimal executados = new BigDecimal(0);

					 for(Iterator i = controlesCargas.iterator(); i.hasNext();) {
						 ControleCarga bean = (ControleCarga)i.next();
						 executados = executados.add(BigDecimal.valueOf(
						         notaCreditoColetaService.findQuantidadeColetasEfetuadasControleCarga(
						                 bean.getIdControleCarga())));
						 executados = executados.add(
						         BigDecimal.valueOf(
						                 notaCreditoDoctoService.findQuantidadeEntregasEfetuadasControleCarga(
						                         bean.getIdControleCarga())));
					 }
						 
					 values.put("EXECUTADOS",executados.toString());
					 values.put("DH_EMISSAO",JTFormatUtils.format(dhEmissao, "dd/MM/YYYY H:mm"));
					 values.put("NR_FROTA",rs.getString("NR_FROTA"));
					 values.put("NR_IDENTIFICADOR",rs.getString("NR_IDENTIFICADOR"));
					 values.put("DS_TIPO_MEIO_TRANSPORTE",rs.getString("DS_TIPO_MEIO_TRANSPORTE"));
					 values.put("DS_MARCA_MEIO_TRANSPORTE",rs.getString("DS_MARCA_MEIO_TRANSPORTE"));
					 values.put("DS_MODELO_MEIO_TRANSPORTE",rs.getString("DS_MODELO_MEIO_TRANSPORTE"));
					 values.put("NM_MOTORISTA",rs.getString("NM_MOTORISTA"));
					 values.put("EQUIPE",rs.getString("EQUIPE"));
					 values.put("CALCULO",rs.getString("CALCULO"));
    				values.put("DETALHES_NOTA_CREDITO", findDetalhesNotaCredito(idNotaCredito));
					 
    				Long nrRota = rs.getLong("NR_ROTA");
    				if (nrRota != null) {
    					values.put("DS_ROTA", nrRota + " - " + rs.getString("DS_ROTA"));
					}

    				if(rs.getString("DH_GERACAO") != null) {
    					values.put("DH_GERACAO", JTFormatUtils.format(JTFormatUtils.buildDateTimeFromTimestampTzString(rs.getString("DH_GERACAO")), "dd/MM/YYYY H:mm"));
    				}

					 createControleCarga(Long.valueOf(rs.getLong("ID_NOTA_CREDITO")),values);
				     values.put("REPEAT","0");
					 rows.add(values);
					 if(!"EX".equals(tpViewReport)){
					 TypedFlatMap values2 = new TypedFlatMap();
					 values2.putAll(values);
					 values2.put("REPEAT","1");
					 rows.add(values2);
				}
				}
				return rows;
	}
        

			});
        return result;        
	}
	
    private DetalheNotaCreditoDto findDetalhesNotaCredito(Long idNotaCredito) {
    	NotaCredito notaCreditoLoaded = notaCreditoService.findById(idNotaCredito);
    	DetalheNotaCreditoDto detalheNotaCreditoDto = new DetalheNotaCreditoDto();
		detalheNotaCreditoDto.setVlAcrescimo(BigDecimalUtils.defaultBigDecimal(notaCreditoLoaded.getVlAcrescimo()));
    	detalheNotaCreditoDto.setVlDesconto(BigDecimalUtils.defaultBigDecimal(notaCreditoLoaded.getVlDesconto()));
    	detalheNotaCreditoDto.setVlDescUsoEquipamento(BigDecimalUtils.defaultBigDecimal(notaCreditoLoaded.getVlDescUsoEquipamento()));
    	detalheNotaCreditoDto.setObNotaCredito(notaCreditoLoaded.getObNotaCredito());
    	if (notaCreditoLoaded.getControleCarga() != null) {
    		List<NotaCredito> notasCreditosByControleCarga = notaCreditoService
    			.findNotasCreditosByIdControleCargaAndIgnoreIdNotaCredito(notaCreditoLoaded.getControleCarga().getIdControleCarga()
    					, idNotaCredito);
    		if (CollectionUtils.isNotEmpty(notasCreditosByControleCarga)) {
    			StringBuffer dsNotasCreditos = new StringBuffer("(");
    			for (Iterator<NotaCredito> it = notasCreditosByControleCarga.iterator(); it.hasNext();) {
    				NotaCredito notaCredito = it.next();
    				dsNotasCreditos.append(notaCredito.getFilial().getSgFilial()).append(" ").append(notaCredito.getNrNotaCredito());
    				if (it.hasNext()) {
    					dsNotasCreditos.append(", ");
    				}
    			}
    			dsNotasCreditos.append(")");
    			detalheNotaCreditoDto.setDsObservacaoOutrasNotas(dsNotasCreditos.toString());
			}
		}
		return detalheNotaCreditoDto;
	}
	
    protected String getTpCalculoParcelasNotaCredito(TabelaColetaEntrega tabelaColetaEntrega) {
		String result = "C1";
		if (tabelaColetaEntrega != null && tabelaColetaEntrega.getTpCalculo() != null) {
			result = tabelaColetaEntrega.getTpCalculo().getValue();
		}
		return result;
	}

	private String getTipoReport(String tpViewReport) {
		if("EX".equals(tpViewReport)){
			return "xls";
		}
		return "pdf";
	}

//  EM - Emissao
//  RE - Reemissao
//	EX - exportar excel
//	VP - Vis. prÃ©via
	protected String getTpEmissaoRelatorio(String tpViewReport, boolean reemissao) {
		String result = "EM";
		if((StringUtils.isBlank(tpViewReport) || tpViewReport.equals("EM")) && reemissao) {
			result = "RE";
		} else if(StringUtils.isNotBlank(tpViewReport)) {
			result = tpViewReport;
		}
		return result;
	}

	private String formatNumber(double arg) {
		return FormatUtils.formatDecimal("###,##0", arg, true);
	}
	
	private void validateManifestoEntrega(TypedFlatMap parameters) {
		//NotaCredito -> ControleCarga -> Manifesto -> ManifestoEntrega (DH_FECHAMENTO != NULL)
		Long idNotaCredito = parameters.getLong("idNotaCredito"); 
		
		if (idNotaCredito == null) {
			idNotaCredito = parameters.getLong("notaCredito.idNotaCredito"); 
		}
		
		if (idNotaCredito != null) {
			validateManifestoEmAberto(idNotaCredito);
			
		} else { //MÃºltiplas NC's
			SqlTemplate sql = createSqlTemplate();
			
			sql.addProjection("NC.ID_NOTA_CREDITO");
			
			sql.addFrom(new StringBuffer("NOTA_CREDITO NC ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO ")
						.append("LEFT  JOIN PROPRIETARIO P ON P.ID_PROPRIETARIO = CC.ID_PROPRIETARIO ")
						.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ")
						.append("INNER JOIN MODELO_MEIO_TRANSPORTE MOMT ON MOMT.ID_MODELO_MEIO_TRANSPORTE = MT.ID_MODELO_MEIO_TRANSPORTE ")
						.append("INNER JOIN MARCA_MEIO_TRANSPORTE MAMT ON MAMT.ID_MARCA_MEIO_TRANSPORTE = MOMT.ID_MARCA_MEIO_TRANSPORTE ")
						.append("INNER JOIN TIPO_MEIO_TRANSPORTE TMT ON TMT.ID_TIPO_MEIO_TRANSPORTE = MOMT.ID_TIPO_MEIO_TRANSPORTE ")
						.append("INNER JOIN PESSOA PM ON PM.ID_PESSOA = CC.ID_MOTORISTA ")
					.append("INNER JOIN FILIAL FI ON FI.ID_FILIAL = NC.ID_FILIAL ").toString());
			
			
			sql.addCustomCriteria("(NC.TP_SITUACAO_APROVACAO = ? OR NC.TP_SITUACAO_APROVACAO IS NULL)");
			sql.addCriteriaValue("A");
			
			sql.addCustomCriteria("P.TP_PROPRIETARIO IN (?,?)");
			sql.addCriteriaValue("A");
			sql.addCriteriaValue("E");
			
			sql.addCustomCriteria("NC.DH_EMISSAO IS NULL");
			
			sql.addCriteria("NC.ID_NOTA_CREDITO","=",parameters.getLong("idNotaCredito"));
			sql.addCriteria("NC.ID_NOTA_CREDITO","=",parameters.getLong("notaCredito.idNotaCredito"));
			
			sql.addCriteria("CC.ID_TRANSPORTADO","=",parameters.getLong("controleCargas.meioTransporte.idMeioTransporte"));
			sql.addCriteria("CC.ID_PROPRIETARIO","=",parameters.getLong("controleCargas.proprietario.idProprietario"));
			sql.addCriteria("NC.ID_NOTA_CREDITO","=",parameters.getLong("notaCredito.idNotaCredito"));
			sql.addCriteria("FI.ID_FILIAL","=",parameters.getLong("filial.idFilial"));
			sql.addCriteria("TRUNC(CAST(NC.DH_EMISSAO AS DATE))",">=",parameters.getYearMonthDay("dhEmissaoInicial"));
			sql.addCriteria("TRUNC(CAST(NC.DH_EMISSAO AS DATE))","<=",parameters.getYearMonthDay("dhEmissaoFinal"));
			
			List<Long> notas = (List<Long>) getJdbcTemplate().query(sql.getSql(),JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()),new ResultSetExtractor() {
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<Long> rows = new ArrayList<Long>();
					
					while(rs.next()) {
						Long idNotaCredito = Long.valueOf(rs.getLong("ID_NOTA_CREDITO"));
						 rows.add(idNotaCredito);
					}
					
					return rows;
				}});

			for (Long id : notas) {
				validateManifestoEmAberto(id);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void validateManifestoEmAberto(Long idNotaCredito) {
		List<ControleCarga> controleCargas = controleCargaService.findControleCargaByNotaCredito(idNotaCredito);
		
				for (ControleCarga c : controleCargas) {
					List<ManifestoEntrega> manifestoEntregas = manifestoEntregaService.findManifestoEntregaAtivoByControleCarga(c.getIdControleCarga());

					for (ManifestoEntrega m : manifestoEntregas) {
				if (m.getDhFechamento() == null && isManifestoEnregaNaoParceira(m)) {
							throw new BusinessException("LMS-25060");
						}
					}
				}
			}

	private boolean isManifestoEnregaNaoParceira(ManifestoEntrega m) {
		return m.getManifesto().getTpManifestoEntrega() == null 
				|| !TP_MANIFESTO_ENTREGA_PARCEIRA.equals(m.getManifesto().getTpManifestoEntrega().getValue());
		}
	
	private void validateParameters(TypedFlatMap parameters) {
		if (parameters.getLong("idNotaCredito") == null && parameters.getLong("controleCargas.meioTransporte.idMeioTransporte") == null &&
				parameters.getLong("controleCargas.proprietario.idProprietario") == null &&
				parameters.getLong("notaCredito.idNotaCredito") == null) {
			
			if (parameters.getYearMonthDay("dhEmissaoInicial") != null &&
					parameters.getYearMonthDay("dhEmissaoFinal") != null) {
				
				YearMonthDay dhEmissaoInicial = parameters.getYearMonthDay("dhEmissaoInicial");
				YearMonthDay dhEmissaoFinal = parameters.getYearMonthDay("dhEmissaoFinal");
				dhEmissaoInicial = dhEmissaoInicial.plusDays(7);
				
				if (dhEmissaoInicial.compareTo(dhEmissaoFinal) < 0) {
					throw new BusinessException("LMS-25023");
				}
			}else if (parameters.getYearMonthDay("dhEmissaoInicial") != null ||
					parameters.getYearMonthDay("dhEmissaoFinal") != null) {
				throw new BusinessException("LMS-25023");
		}
	}
	}
	
	
	
	public String executeWorkflow(List idsNotaCredito,List tpStituacoes) {
        Long idNotaCredito = (Long)idsNotaCredito.get(0);
        String tpStituacao = (String)tpStituacoes.get(0);
        NotaCredito bean = notaCreditoService.findById(idNotaCredito);
        String tpComplementoEvento = "";
        if (tpStituacao.equalsIgnoreCase("A")) {
        	bean.setTpSituacaoAprovacao(new DomainValue("A"));
        	notaCreditoService.store(bean);
        	tpComplementoEvento = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_APROVADO;
        }else if (tpStituacao.equalsIgnoreCase("R")) {
			bean.setTpSituacaoAprovacao(null);
			bean.setPendencia(null);
			notaCreditoService.store(bean);
			tpComplementoEvento = ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_REPROVADO;
		}
        
        eventoNotaCreditoService.storeEventoNotaCredito(bean, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_VALOR_EXCEDENTE, tpComplementoEvento);
        
        return null;
    }

	private SqlTemplate montaSql(TypedFlatMap parameters){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("NC.ID_NOTA_CREDITO");
		sql.addProjection("NC.NR_NOTA_CREDITO");
		sql.addProjection("NC.DH_EMISSAO");
		sql.addProjection("FI.DS_TIMEZONE");
		sql.addProjection("FI.SG_FILIAL");
		sql.addProjection("NC.ID_PENDENCIA");
		sql.addProjection("MT.NR_FROTA"); 
		sql.addProjection("MT.NR_IDENTIFICADOR");
		sql.addProjection("TMT.DS_TIPO_MEIO_TRANSPORTE", "DS_TIPO_MEIO_TRANSPORTE"); 
		sql.addProjection("TMT.NR_CAPACIDADE_PESO_FINAL"); 
		sql.addProjection("MAMT.DS_MARCA_MEIO_TRANSPORTE");
		sql.addProjection("MOMT.DS_MODELO_MEIO_TRANSPORTE");
		sql.addProjection("PM.NM_PESSOA","NM_MOTORISTA");
		sql.addProjection("CC.ID_CONTROLE_CARGA");
		sql.addProjection("CC.DH_GERACAO", "DH_GERACAO");
		sql.addProjection("NC.VL_DESC_USO_EQUIPAMENTO");
		sql.addProjection("ROCE.NR_ROTA", "NR_ROTA");
		sql.addProjection("ROCE.DS_ROTA", "DS_ROTA");
		sql.addProjection("FIO.NR_FRANQUIA_KM");
		sql.addProjection("ROCE.NR_KM");
		sql.addProjection("NC.TP_NOTA_CREDITO");

		//-----------EQUIPE-------------
		sql.addProjection(new StringBuffer("(SELECT E.DS_EQUIPE FROM EQUIPE_OPERACAO EO ")
			.append("INNER JOIN EQUIPE E ON E.ID_EQUIPE = EO.ID_EQUIPE ")
			.append("WHERE EO.ID_CARREGAMENTO_DESCARGA IS NULL AND EO.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA)").toString(),"EQUIPE");
		/**
		 * TODO: FOI removido da sql e Ã© trazido pela funÃ§Ã£o gerarNotaCredito.finqQuilometragem....
		//------KM RODADOS---------
		sql.addProjection(new StringBuffer("(SELECT SUM(QT_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP ")
		   		.append("INNER JOIN PARCELA_TABELA_CE PTCE ON PTCE.ID_PARCELA_TABELA_CE = NCP.ID_PARCELA_TABELA_CE ")
		   		.append("WHERE PTCE.TP_PARCELA = ? AND ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO)").toString(),"KM_RODADOS");
		sql.addCriteriaValue("QU");
		*/
		//Calculo
		sql.addProjection(new StringBuffer("(SELECT MAX(TTCE.DS_TIPO_TABELA_COLETA_ENTREGA) FROM NOTA_CREDITO_PARCELA NCP ")
				.append("INNER JOIN PARCELA_TABELA_CE PTCE ON PTCE.ID_PARCELA_TABELA_CE = NCP.ID_PARCELA_TABELA_CE ")
				.append("INNER JOIN TABELA_COLETA_ENTREGA TCE ON TCE.ID_TABELA_COLETA_ENTREGA = PTCE.ID_TABELA_COLETA_ENTREGA ")
				.append("INNER JOIN TIPO_TABELA_COLETA_ENTREGA TTCE ON TTCE.ID_TIPO_TABELA_COLETA_ENTREGA = TCE.ID_TIPO_TABELA_COLETA_ENTREGA ")
		   		.append("WHERE PTCE.TP_PARCELA = ? AND ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO)").toString(),"CALCULO");
		sql.addCriteriaValue("DH");
 
		//------PROGRAMADO------------
		sql.addProjection(new StringBuffer("((SELECT COUNT(*) FROM CONTROLE_CARGA CC ")
					.append("INNER JOIN MANIFESTO MA ON MA.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = MA.ID_MANIFESTO ")
		   			.append("INNER JOIN MANIFESTO_ENTREGA_DOCUMENTO MED ON MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA ")
		   			.append("WHERE MA.TP_STATUS_MANIFESTO <> ? AND CC.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO) ")
		   			.append(" + ")
		   			.append("(SELECT COUNT(*) FROM CONTROLE_CARGA CC ")
		   			.append("INNER JOIN MANIFESTO_COLETA MC ON MC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ")
		   			.append("INNER JOIN PEDIDO_COLETA PC ON PC.ID_MANIFESTO_COLETA = MC.ID_MANIFESTO_COLETA ")
		   			.append("WHERE CC.ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO))").toString(),"PROGRAMADO");
		sql.addCriteriaValue("C");
		   
		// ---- QTDE de dias ----
		sql.addProjection(new StringBuffer("(SELECT SUM(NCP.QT_NOTA_CREDITO_PARCELA) FROM NOTA_CREDITO_PARCELA NCP ")
			.append("INNER JOIN PARCELA_TABELA_CE PTCE ON PTCE.ID_PARCELA_TABELA_CE = NCP.ID_PARCELA_TABELA_CE ")
			.append("WHERE PTCE.TP_PARCELA = ? AND ID_NOTA_CREDITO = NC.ID_NOTA_CREDITO)").toString(),"QT_DIAS");
		sql.addCriteriaValue("DH");
		
		
		// ---- QTDE DE COLETAS PROGRAMADAS ----
		sql.addProjection(new StringBuffer("(SELECT SUM(MC.QT_TOTAL_COLETAS_EMISSAO) FROM MANIFESTO_COLETA MC ")
			.append("WHERE MC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA) ").toString(),"COLETASPROGRAMADAS");
		
		// ---- QTDE DE ENTREGAS PROGRAMADAS ----
		sql.addProjection(new StringBuffer("(SELECT COUNT(*) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
			.append("INNER JOIN MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA ")
			.append("INNER JOIN MANIFESTO MA ON MA.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
			.append("WHERE MA.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA) ").toString(),"ENTREGASPROGRAMADAS");
	
		// ---- QTDE DE COLETAS EXECUTADAS ----
		sql.addProjection(new StringBuffer("(SELECT COUNT(*) FROM NOTA_CREDITO_COLETA NCC ")
			.append("WHERE NC.ID_NOTA_CREDITO = NCC.ID_NOTA_CREDITO) ").toString(),"COLETASEXECUTADAS");
		
		// ---- QTDE DE ENTREGAS EXECUTADAS ----
		sql.addProjection(new StringBuffer("(SELECT COUNT(*) FROM NOTA_CREDITO_DOCTO NCD ")
			.append("WHERE NC.ID_NOTA_CREDITO = NCD.ID_NOTA_CREDITO) ").toString(),"ENTREGASEXECUTADAS");
		
		// ---- QTDE DE COLETAS CONTABILIZADAS ----
		sql.addProjection(new StringBuffer("(SELECT SUM(NCP.QT_COLETA) FROM NOTA_CREDITO_PARCELA NCP ")
			.append("INNER JOIN PARCELA_TABELA_CE PTCE ON PTCE.ID_PARCELA_TABELA_CE = NCP.ID_PARCELA_TABELA_CE ")
			.append("WHERE NC.ID_NOTA_CREDITO = NCP.ID_NOTA_CREDITO AND PTCE.TP_PARCELA = 'EV') ").toString(),"COLETASCONTABILIZADAS");
		
		// ---- QTDE DE ENTREGAS CONTABILIZADAS ----
		sql.addProjection(new StringBuffer("(SELECT SUM(NCP.QT_ENTREGA) FROM NOTA_CREDITO_PARCELA NCP ")
			.append("INNER JOIN PARCELA_TABELA_CE PTCE ON PTCE.ID_PARCELA_TABELA_CE = NCP.ID_PARCELA_TABELA_CE ")
			.append("WHERE NC.ID_NOTA_CREDITO = NCP.ID_NOTA_CREDITO AND PTCE.TP_PARCELA = 'EV') ").toString(),"ENTREGASCONTABILIZADAS");
		
		sql.addFrom(new StringBuffer("NOTA_CREDITO NC ")
				.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = NC.ID_CONTROLE_CARGA ")
					.append("LEFT  JOIN PROPRIETARIO P ON P.ID_PROPRIETARIO = CC.ID_PROPRIETARIO ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ")
					.append("INNER JOIN MODELO_MEIO_TRANSPORTE MOMT ON MOMT.ID_MODELO_MEIO_TRANSPORTE = MT.ID_MODELO_MEIO_TRANSPORTE ")
					.append("INNER JOIN MARCA_MEIO_TRANSPORTE MAMT ON MAMT.ID_MARCA_MEIO_TRANSPORTE = MOMT.ID_MARCA_MEIO_TRANSPORTE ")
					.append("INNER JOIN TIPO_MEIO_TRANSPORTE TMT ON TMT.ID_TIPO_MEIO_TRANSPORTE = MOMT.ID_TIPO_MEIO_TRANSPORTE ")
					.append("INNER JOIN PESSOA PM ON PM.ID_PESSOA = CC.ID_MOTORISTA ")
				.append("INNER JOIN FILIAL FI ON FI.ID_FILIAL = NC.ID_FILIAL ")
				.append("INNER JOIN FILIAL FIO ON FIO.ID_FILIAL = CC.ID_FILIAL_ORIGEM ")
				.append("LEFT JOIN ROTA_COLETA_ENTREGA ROCE ON ROCE.ID_ROTA_COLETA_ENTREGA = CC.ID_ROTA_COLETA_ENTREGA ").toString());
		
		
		sql.addCustomCriteria("(NC.TP_SITUACAO_APROVACAO = ? OR NC.TP_SITUACAO_APROVACAO IS NULL)");
		sql.addCriteriaValue("A");
		
		sql.addCustomCriteria("P.TP_PROPRIETARIO IN (?,?)");
		sql.addCriteriaValue("A");
		sql.addCriteriaValue("E");
		
		if (parameters.getLong("idNotaCredito") == null &&  
				parameters.getLong("notaCredito.idNotaCredito") == null &&
				parameters.getYearMonthDay("dhEmissaoInicial") == null &&
				parameters.getYearMonthDay("dhEmissaoFinal") == null)
			sql.addCustomCriteria("NC.DH_EMISSAO IS NULL");
		
		
		sql.addCriteria("NC.ID_NOTA_CREDITO","=",parameters.getLong("idNotaCredito"));
		sql.addCriteria("NC.ID_NOTA_CREDITO","=",parameters.getLong("notaCredito.idNotaCredito"));
		
		sql.addCriteria("CC.ID_TRANSPORTADO","=",parameters.getLong("controleCargas.meioTransporte.idMeioTransporte"));

		// 2709
		sql.addCriteria("CC.ID_TRANSPORTADO","=",parameters.getLong("idMeioTransporteEmissao"));

		sql.addCriteria("CC.ID_PROPRIETARIO","=",parameters.getLong("controleCargas.proprietario.idProprietario"));
		sql.addCriteria("NC.ID_NOTA_CREDITO","=",parameters.getLong("notaCredito.idNotaCredito"));
		sql.addCriteria("FI.ID_FILIAL","=",parameters.getLong("filial.idFilial"));
		sql.addCriteria("TRUNC(CAST(NC.DH_EMISSAO AS DATE))",">=",parameters.getYearMonthDay("dhEmissaoInicial"));
		sql.addCriteria("TRUNC(CAST(NC.DH_EMISSAO AS DATE))","<=",parameters.getYearMonthDay("dhEmissaoFinal"));
		
		return sql;
	}

	
	public List executeWorkFlow(TypedFlatMap parameters){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("NC.NR_NOTA_CREDITO");
		sql.addProjection("NC.ID_NOTA_CREDITO");
		sql.addProjection("NC.ID_PENDENCIA");
		sql.addProjection("NC.DH_EMISSAO");
		sql.addProjection("FI.ID_FILIAL");
		sql.addProjection("FI.SG_FILIAL");
		   
		sql.addFrom(new StringBuffer("NOTA_CREDITO NC ")
				.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = NC.ID_CONTROLE_CARGA ")
					.append("LEFT  JOIN PROPRIETARIO P ON P.ID_PROPRIETARIO = CC.ID_PROPRIETARIO ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ")
					.append("INNER JOIN MODELO_MEIO_TRANSPORTE MOMT ON MOMT.ID_MODELO_MEIO_TRANSPORTE = MT.ID_MODELO_MEIO_TRANSPORTE ")
					.append("INNER JOIN MARCA_MEIO_TRANSPORTE MAMT ON MAMT.ID_MARCA_MEIO_TRANSPORTE = MOMT.ID_MARCA_MEIO_TRANSPORTE ")
					.append("INNER JOIN TIPO_MEIO_TRANSPORTE TMT ON TMT.ID_TIPO_MEIO_TRANSPORTE = MOMT.ID_TIPO_MEIO_TRANSPORTE ")
					.append("INNER JOIN PESSOA PM ON PM.ID_PESSOA = CC.ID_MOTORISTA ")
					.append("INNER JOIN FILIAL FI ON FI.ID_FILIAL = NC.ID_FILIAL ").toString());
		
		
		sql.addCustomCriteria("P.TP_PROPRIETARIO IN (?,?)");
		sql.addCriteriaValue("A");
		sql.addCriteriaValue("E");

		
		sql.addCriteria("NC.ID_NOTA_CREDITO","=",parameters.getLong("idNotaCredito"));
		sql.addCriteria("NC.ID_NOTA_CREDITO","=",parameters.getLong("notaCredito.idNotaCredito"));
		
		sql.addCriteria("CC.ID_TRANSPORTADO","=",parameters.getLong("controleCargas.meioTransporte.idMeioTransporte"));
		sql.addCriteria("CC.ID_PROPRIETARIO","=",parameters.getLong("controleCargas.proprietario.idProprietario"));
		sql.addCriteria("NC.ID_NOTA_CREDITO","=",parameters.getLong("notaCredito.idNotaCredito"));
		sql.addCriteria("FI.ID_FILIAL","=",parameters.getLong("filial.idFilial"));
		sql.addCriteria("TRUNC(CAST(NC.DH_EMISSAO AS DATE))",">=",parameters.getYearMonthDay("dhEmissaoInicial"));
		sql.addCriteria("TRUNC(CAST(NC.DH_EMISSAO AS DATE))","<=",parameters.getYearMonthDay("dhEmissaoFinal"));
		
		return (List)getJdbcTemplate().query(sql.getSql(),JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()),new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List result = new ArrayList();
				while(rs.next()) {
					Long idNotaCredito = Long.valueOf(rs.getLong("ID_NOTA_CREDITO"));
					Long idFilial = Long.valueOf(rs.getLong("ID_FILIAL"));
					Long idPendencia = (rs.getLong("ID_PENDENCIA") == 0) ? null : Long.valueOf(rs.getLong("ID_PENDENCIA"));
					
					if (idPendencia != null) {
						Pendencia pendencia = pendenciaService.findById(idPendencia);
						if (pendencia.getTpSituacaoPendencia().getValue().equals("E")){
							result.add("pendencia");
							break;
						}else{
							continue;
						}
					}else{
						if (StringUtils.isBlank(rs.getString("DH_EMISSAO"))) {						
						BigDecimal total = notaCreditoService.findValorTotalNotaCredito(idNotaCredito);
						BigDecimal vlMax = (BigDecimal)configuracoesFacade.getValorParametro(idFilial,"VALOR_MAXIMO_NOTA_CR");
						NotaCredito bean = notaCreditoService.findById(idNotaCredito);
						if (vlMax != null && vlMax.compareTo(total) < 0) {
							// LMS4050 -- Concatena descrição para atender o evento 2504
							StringBuilder descricao = new StringBuilder();
							descricao.append(configuracoesFacade.getMensagem("aprovarNotaCreditoValorSuperiorMax"));
							descricao.append(" ");
							descricao.append(rs.getString("SG_FILIAL"));
							descricao.append(" ");
							descricao.append(new DecimalFormat("000000000").format(rs.getLong("NR_NOTA_CREDITO")));
							
							Pendencia pendencia = workflowPendenciaService.generatePendencia(idFilial,Short.valueOf("2504"),idNotaCredito, descricao.toString() ,JTDateTimeUtils.getDataHoraAtual());
							bean.setPendencia(pendencia);
							bean.setTpSituacaoAprovacao(new DomainValue("S"));
							bean = notaCreditoService.storeNotaCredito(bean);
							
							eventoNotaCreditoService.storeEventoNotaCredito(bean, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_VALOR_EXCEDENTE, ConstantesEventosNotaCredito.EVENTO_NOTA_CREDITO_FLUXO_SOLICITADO);
							
							result.add(new StringBuffer(rs.getString("SG_FILIAL")).append(" ").append(new DecimalFormat("000000000").format(rs.getLong("NR_NOTA_CREDITO"))).toString());
						}
					}
				}
				}
				return result;
			}
		});
	}

	
	private void createControleCarga(Long idNotaCredito,TypedFlatMap map) {
		List controlesCargas = controleCargaService.findControlesByIdNotaCredito(idNotaCredito);
		StringBuffer result = new StringBuffer();
		BigDecimal totatQuilometragem = new BigDecimal(0);

		// FIXME: Um controle de carga possui vÃ¡rias notas de crÃ©dito, mas uma nota de crÃ©dito possui apenas um controle de carga
		for(Iterator i = controlesCargas.iterator(); i.hasNext();) {
			ControleCarga bean = (ControleCarga)i.next();
			Filial filial = bean.getFilialByIdFilialOrigem();
			totatQuilometragem = totatQuilometragem.add(new BigDecimal(controleCargaService.findQuilometrosPercorridosControleCarga(bean)));
			result.append(((result.length() > 0) ? ", " : ""))
					.append(filial.getSgFilial()).append(" ").append(new DecimalFormat("00000000").format(bean.getNrControleCarga().doubleValue()));
		}
		map.put("CONTROLE_CARGA",result.toString());
		map.put("KM_RODADOS", FormatUtils.formatDecimal("#,##0", totatQuilometragem, true));
	}

	public JRDataSource executeParcelas(Long idNotaCredito) {
		SqlTemplate sql = createSqlParcela(idNotaCredito);
				
		List result = (List)getJdbcTemplate().query(sql.getSql(),sql.getCriteria(),new ResultSetExtractor() {
			
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Map> rows = new ArrayList<Map>();
				boolean vlDescUsoEquipamento = true;
				Map values = null;
				
				//esse IF resolve 'Exhausted Resultset' exception?
				//CQPRO00028740
				if (rs.next()) {
				Map mapValorAcrescimoDesconto = notaCreditoService.findValorAcrescimoDesconto(rs.getLong("ID_NOTA_CREDITO"));
				if(mapValorAcrescimoDesconto!= null){
					BigDecimal vlDesconto = null;
					BigDecimal vlAcrescimo = null;
					String dsSimboloMoeda = "";
					if(mapValorAcrescimoDesconto.get("dsSimbolo")!= null)
						dsSimboloMoeda = mapValorAcrescimoDesconto.get("dsSimbolo").toString();
					
					if(mapValorAcrescimoDesconto.get("vlDesconto")!= null){
						vlDesconto =(BigDecimal)mapValorAcrescimoDesconto.get("vlDesconto");
						values = new FastHashMap();
						values.put("TEXTO",createMessager("valorDesconto",null));
						values.put("VALUE",vlDesconto);
						values.put("MOEDA",dsSimboloMoeda);
						values.put("NEGATIVO",true);
						rows.add(values);
					}
					
					if(mapValorAcrescimoDesconto.get("vlAcrescimo")!= null){
						vlAcrescimo = (BigDecimal)mapValorAcrescimoDesconto.get("vlAcrescimo");
						values = new FastHashMap();
						values.put("TEXTO",createMessager("valorAcrescimo",null));
						values.put("VALUE",vlAcrescimo);
						values.put("MOEDA",dsSimboloMoeda);
						rows.add(values);
					}
					
				}	
				String tpCalculo = rs.getString("TP_CALCULO");
				


				List<Map> rowsParcelas = new ArrayList<Map>();
				do {
					BigDecimal vlNotaCreditoParcela = rs.getBigDecimal("VL_NOTA_CREDITO_PARCELA");
					BigDecimal qtNotaCreditoParcela = rs.getBigDecimal("QT_NOTA_CREDITO_PARCELA");
					BigDecimal pcSobreValor = rs.getBigDecimal("PC_SOBRE_VALOR");
					BigDecimal vlDefinido = rs.getBigDecimal("VL_DEFINIDO");
					String dsSimboloMoeda = rs.getString("DS_SIMBOLO");
					String tpParcela = rs.getString("TP_PARCELA");
					
					if (vlDescUsoEquipamento) {
						if (rs.getBigDecimal("VL_DESC_USO_EQUIPAMENTO") != null) {
							values = new FastHashMap();
							values.put("TEXTO",createMessager("descontoReferenteUsoCarretaMercurio",null));
							values.put("VALUE",rs.getBigDecimal("VL_DESC_USO_EQUIPAMENTO"));
							values.put("MOEDA",dsSimboloMoeda);
							
							values.put("NEGATIVO",true);
							rows.add(values);
							
						}
						vlDescUsoEquipamento = false;
					}
										
					values = new FastHashMap();
					values.put("MOEDA",dsSimboloMoeda);
	 				BigDecimal vlFrete = notaCreditoService.findValorTotalNotaCredito(Long.valueOf(rs.getLong("ID_NOTA_CREDITO")));
					
	 				values.put("TP_PARCELA", tpParcela);
	 				values.put("VALUE",vlNotaCreditoParcela.multiply(qtNotaCreditoParcela));
	 				if (tpParcela.equals("DH")) {
						values.put("TEXTO",createMessager("freteDiario",null));
					}else if (tpParcela.equals("EV")) {
						StringBuffer texto = null;
						if(DM_TP_CALCULO_TABELA_COLETA_ENTREGA.CALCULO_1.equals(tpCalculo)){
							texto = new StringBuffer(createMessager("freteEventoVlEvento",new Object[] {dsSimboloMoeda,formatToMoney(vlNotaCreditoParcela),qtNotaCreditoParcela}));
						}else{
							texto = new StringBuffer(createMessager("freteEventoVlCtrc",new Object[] {dsSimboloMoeda,formatToMoney(vlNotaCreditoParcela),qtNotaCreditoParcela}));
						}
						texto.append(" (");
						if (rs.getInt("QT_COLETA") != 0)
							texto.append(rs.getInt("QT_COLETA")).append(" ").append(configuracoesFacade.getMensagem("coletas2"));
						if (rs.getInt("QT_ENTREGA") != 0 && rs.getInt("QT_COLETA") != 0)
							texto.append(" - ");
						if (rs.getInt("QT_ENTREGA") != 0)
							texto.append(rs.getInt("QT_ENTREGA")).append(" ").append(configuracoesFacade.getMensagem("entregas2"));
						texto.append(")");
						values.put("TEXTO",texto.toString());
					}else if (tpParcela.equals("QU")) {
						values.put("TEXTO",createMessager("fretePorKmExcedentePorKmKm",new Object[] {dsSimboloMoeda,formatToMoney(vlNotaCreditoParcela),qtNotaCreditoParcela}));
					}else if (tpParcela.equals("FP")) {
						if(DM_TP_CALCULO_TABELA_COLETA_ENTREGA.CALCULO_2.equals(tpCalculo)){
							String chave = "";
							if(BigDecimal.ONE.equals(qtNotaCreditoParcela)){
								chave = "freteFaixaPesoDocumentoVl";
						}else{
								chave = "freteFaixaPesoExcedenteVl";							
							}
							values.put("TEXTO",createMessager(chave,new Object[] {dsSimboloMoeda +" "+formatToMoney(vlNotaCreditoParcela),qtNotaCreditoParcela}));
						}else{
						values.put("TEXTO",createMessager("freteFracaoPeso",new Object[] {dsSimboloMoeda,formatToMoney(vlNotaCreditoParcela),qtNotaCreditoParcela}));
						}
					}else if (tpParcela.equals("PF")) {
						values.put("TEXTO",createMessager("freteSobreValorFreteFreteMinimo",new Object[] {pcSobreValor,dsSimboloMoeda,formatToMoney(vlNotaCreditoParcela),dsSimboloMoeda,formatToMoney(vlNotaCreditoParcela)}));
					}else if (tpParcela.equals("PV")) {
						values.put("TEXTO",createMessager("freteSobreValorMercadoria",new Object[] {pcSobreValor,formatToMoney(vlFrete),formatToMoney(vlDefinido),formatToMoney(vlNotaCreditoParcela)}));
					}else if (tpParcela.equals("VO")) {
						values.put("TEXTO",createMessager("freteVolume",new Object[] {dsSimboloMoeda,formatToMoney(vlNotaCreditoParcela),qtNotaCreditoParcela}));
					}	 				
	 				rowsParcelas.add(values);
				}while (rs.next()); 
				
				if( DM_TP_CALCULO_TABELA_COLETA_ENTREGA.CALCULO_2.equals(tpCalculo) ){
					BigDecimal valorMaiorParcelaDH = new BigDecimal(0);
					Map maiorParcelaDH = null;
					BigDecimal valorParcelas = new BigDecimal(0);
					List<Map> rowsNaoDH = new ArrayList<Map>();
					for (Map parcela : rowsParcelas) {
						BigDecimal valorParcela = (BigDecimal) parcela.get("VALUE");
						if("DH".equals(parcela.get("TP_PARCELA"))){
							if( valorParcela.compareTo(valorMaiorParcelaDH) >= 0 ){
								valorMaiorParcelaDH= valorParcela;
								maiorParcelaDH = parcela;
							}
						} else {
							valorParcelas = valorParcelas.add(valorParcela);
							rowsNaoDH.add(parcela);
						}
					}
					rowsParcelas = new ArrayList<Map>();
					if(valorMaiorParcelaDH.compareTo(valorParcelas)>=0){
						rowsParcelas.add(maiorParcelaDH);
					}else{
						rowsParcelas.addAll(rowsNaoDH);
					}
				} 
				
				rows.addAll(rowsParcelas);
				}
				
				return rows;
			}});
		return new JRMapCollectionDataSource(result);
	}
	
	private String createMessager(String chave,Object[] arg) throws SQLException {
		StringBuffer sb = new StringBuffer();
		if (arg == null)
			sb.append(configuracoesFacade.getMensagem(chave));
		else
			sb.append(configuracoesFacade.getMensagem(chave,arg));
		
		return sb.toString();
	}
	
	private SqlTemplate createSqlParcela(Long idNotaCredito) {
		SqlTemplate sql = createSqlParcelaStructure(idNotaCredito);
		sql.addProjection("PTC.TP_PARCELA");
		sql.addProjection("NCP.VL_NOTA_CREDITO_PARCELA");
		sql.addProjection("NCP.QT_NOTA_CREDITO_PARCELA");
		sql.addProjection("NCP.QT_COLETA");
		sql.addProjection("NCP.QT_ENTREGA");
		sql.addProjection("PTC.PC_SOBRE_VALOR");
		sql.addProjection("PTC.VL_DEFINIDO");
		sql.addProjection("M.DS_SIMBOLO");
		sql.addProjection("NC.ID_NOTA_CREDITO");
		sql.addProjection("NC.VL_DESC_USO_EQUIPAMENTO");
		sql.addProjection("TABELA_COLETA_ENTREGA.TP_CALCULO");
		
		return sql;
	}
	private SqlTemplate createSqlParcelaStructure(Long idNotaCredito) {
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(new StringBuilder("NOTA_CREDITO_PARCELA NCP ")
				.append("INNER JOIN NOTA_CREDITO NC ON NC.ID_NOTA_CREDITO = NCP.ID_NOTA_CREDITO ")
				.append("INNER JOIN PARCELA_TABELA_CE PTC ON PTC.ID_PARCELA_TABELA_CE = NCP.ID_PARCELA_TABELA_CE ")
				.append("INNER JOIN MOEDA_PAIS MP ON MP.ID_MOEDA_PAIS = NC.ID_MOEDA_PAIS ")
				.append("INNER JOIN MOEDA M ON M.ID_MOEDA = MP.ID_MOEDA ")
				.append("INNER JOIN TABELA_COLETA_ENTREGA ON TABELA_COLETA_ENTREGA.ID_TABELA_COLETA_ENTREGA = PTC.ID_TABELA_COLETA_ENTREGA ").toString());   
		
		sql.addCriteria("NCP.ID_NOTA_CREDITO","=",idNotaCredito);
		return sql;
	}
	private BigDecimal findTotalCustoParcela(Long idNotaCredito, final Long idFilial) {
		SqlTemplate sql = createSqlParcelaStructure(idNotaCredito);
		sql.addProjection("MP.ID_PAIS");
		sql.addProjection("M.ID_MOEDA");
		sql.addProjection("PTC.TP_PARCELA");
		sql.addProjection("NCP.VL_NOTA_CREDITO_PARCELA");
		sql.addProjection("NCP.QT_NOTA_CREDITO_PARCELA");
		
		return (BigDecimal)getJdbcTemplate().query(sql.getSql(),sql.getCriteria(),new ResultSetExtractor(){

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				BigDecimal vlTotal = new BigDecimal(0);
				while (rs.next()) {

					Long idPais = Long.valueOf(rs.getLong("ID_PAIS"));
					Long idMoeda = Long.valueOf(rs.getLong("ID_MOEDA"));
					Filial filial = filialService.findById(idFilial);
					Long idMoedaDest = filial.getMoeda().getIdMoeda();
					Long idPaisDest = enderecoPessoaService.findEnderecoPessoaPadrao(idFilial).getMunicipio().getUnidadeFederativa().getPais().getIdPais();
					
					BigDecimal vlNotaCreditoParcela = conversaoMoedaService.findConversaoMoeda(idPais,idMoeda,idPaisDest,idMoedaDest,JTDateTimeUtils.getDataAtual(),rs.getBigDecimal("VL_NOTA_CREDITO_PARCELA"));
					BigDecimal qtNotaCreditoParcela = rs.getBigDecimal("QT_NOTA_CREDITO_PARCELA");
					String tpParcela = rs.getString("TP_PARCELA");
					
					if (tpParcela.equals("EV") || tpParcela.equals("QU") || tpParcela.equals("FP"))
						vlTotal = vlTotal.add(vlNotaCreditoParcela.multiply(qtNotaCreditoParcela));
					else
						vlTotal = vlTotal.add(vlNotaCreditoParcela);
				}
				return vlTotal;
			}});
	}

	public JRBeanCollectionDataSource findManifestoEntrega(Long idControleCarga, Long idNotaCredito, String tpCalculoNotaCredito) {
		return new JRBeanCollectionDataSource(findDadosManifestoEntrega(idControleCarga, idNotaCredito, tpCalculoNotaCredito));
	}
	
	public Map<String, List<Map<String, Object>>> findMapManifestoEntrega(Long idControleCarga, Long idNotaCredito, String tpCalculoNotaCredito) {
		List<ManifestoEntregaNotaCreditoDto> dados = findDadosManifestoEntrega(idControleCarga, idNotaCredito, tpCalculoNotaCredito);
		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> manifestoEntrega;
		for (ManifestoEntregaNotaCreditoDto tabela : dados){
			Map<String, Object> doctoServico = new HashMap<String, Object>();					
			doctoServico.put("enderecoReal", tabela.getEnderecoReal());
			doctoServico.put("nrDoctoServico", tabela.getNrDoctoServico());
			doctoServico.put("sgFilial", tabela.getSgFilialOrigem());
			doctoServico.put("qtEvento", tabela.getEvento());
			doctoServico.put("vlPeso", tabela.getVlPeso());
			doctoServico.put("qtVolume", tabela.getQtVolumes());
			doctoServico.put("vlFrete", tabela.getVlFrete());
			doctoServico.put("vlMercadoria", tabela.getVlMercadorias());
			doctoServico.put("isManifestoForaNota", tabela.getManifestoEntrega().getIsManifestoForaNota());
			doctoServico.put("tpManifestoEntrega", tabela.getManifestoEntrega().getTipoManifestoEntrega().getValue());
			manifestoEntrega = result.get(tabela.getManifestoEntrega().getFormattedManifesto());
			if (manifestoEntrega == null){
				manifestoEntrega = new ArrayList<Map<String,Object>>();
			}			
			manifestoEntrega.add(doctoServico);
			result.put(tabela.getManifestoEntrega().getFormattedManifesto(), manifestoEntrega);	
		}
		return result;
	}

	private List<ManifestoEntregaNotaCreditoDto> findDadosManifestoEntrega(Long idControleCarga, Long idNotaCredito, String tpCalculoNotaCredito) {
		List<ManifestoEntregaNotaCreditoDto> result = new ArrayList<ManifestoEntregaNotaCreditoDto>();
		Set<String> enderecos = new HashSet<String>();
		List<ManifestoEntrega> manifestos = manifestoEntregaService.findManifestoByIdControleCarga(idControleCarga);
		for (ManifestoEntrega manifestoEntrega : manifestos) {
			ManifestoEntregaControleCargaNotaCreditoDto dto = new ManifestoEntregaControleCargaNotaCreditoDto(manifestoEntrega);
			List<Map<String, Object>> doctos = doctoServicoService.findConhecimentoByIdManifestoEntregaIdNotaCredito(manifestoEntrega.getIdManifestoEntrega(),
					idNotaCredito);
			List<ParcelaTabelaCe> listParcelasCe = parcelaTabelaCeService.findParcelaTabelaCeByIdNotaCredito(idNotaCredito);
			
			if(doctos == null || doctos.isEmpty()){
				dto.setIsManifestoForaNota(true);
				ManifestoEntregaNotaCreditoDto dtoManifestos = new ManifestoEntregaNotaCreditoDto(dto);
				result.add(dtoManifestos);
			}else{
				for (Map<String, Object> map : doctos) {
					Long idDoctoServico = LongUtils.getLong(map.get("idDoctoServico"));
					OcorrenciaEntrega oe = ocorrenciaEntregaService.
							findOcorrenciaEntregaDoctoServico(idDoctoServico, manifestoEntrega.getIdManifestoEntrega());

					if (oe != null && ConstantesEntrega.TP_OCORRENCIA_ENTREGUE_AEROPORTO.equals(oe.getTpOcorrencia().getValue())) {
						Awb awb = awbService.findAwbByDoctoServicoAndManifesto(idDoctoServico, manifestoEntrega.getIdManifestoEntrega());
						if (awb != null) {
							Aeroporto aeroporto = awb.getAeroportoByIdAeroportoOrigem();
							map.put("dsEnderecoEntregaReal", this.enderecoPessoaService.getEnderecoCompleto(aeroporto.getPessoa().getEnderecoPessoa().getIdEnderecoPessoa()));
						}
					}
					
					ManifestoEntregaNotaCreditoDto dtoManifestos = new ManifestoEntregaNotaCreditoDto(map, tpCalculoNotaCredito, listParcelasCe, enderecos);
					dtoManifestos.setManifestoEntrega(dto);
					result.add(dtoManifestos);
				}
			}
			
		}
		return result;
	}
	
	public JRBeanCollectionDataSource findManifestoColeta(Long idControleCarga, Long idNotaCredito, String tpCalculoNotaCredito){
		return new JRBeanCollectionDataSource(findDadosManifestoColeta(idControleCarga, idNotaCredito, tpCalculoNotaCredito));
	}
	
	public Map<String, List<Map<String, Object>>> findMapManifestoColeta(Long idControleCarga, Long idNotaCredito, String tpCalculoNotaCredito){
		List<ManifestoColetaNotaCreditoDto> dados = findDadosManifestoColeta(idControleCarga, idNotaCredito, tpCalculoNotaCredito);				
		Map<String, List<Map<String, Object>>> result = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> manifestoColeta;
		for (ManifestoColetaNotaCreditoDto tabela : dados){			
			Map<String, Object> pedidoColeta = new HashMap<String, Object>();
			pedidoColeta.put("dsEndereco", tabela.getEnderecoReal());
			pedidoColeta.put("qtEvento", tabela.getEvento());
			pedidoColeta.put("nrPedidoColeta", tabela.getNrPedidoColeta());
			pedidoColeta.put("psTotalAforado", tabela.getPsTotalAforadoVerificado());
			pedidoColeta.put("psTotalVerificado", tabela.getPsTotalVerificado());
			pedidoColeta.put("qtVolume", tabela.getQtVolumes());
			pedidoColeta.put("sgFilial", tabela.getSgFilial());
			pedidoColeta.put("vlPeso", tabela.getVlPeso());
			pedidoColeta.put("isManifestoForaNota", tabela.getManifestoColeta().getIsManifestoForaNota());
			manifestoColeta = result.get(tabela.getManifestoColeta().getFormattedManifesto());
			if (manifestoColeta == null){
				manifestoColeta = new ArrayList<Map<String,Object>>();
			}			
			manifestoColeta.add(pedidoColeta);
			result.put(tabela.getManifestoColeta().getFormattedManifesto(), manifestoColeta);		
		}
		return result;
	}

	@SuppressWarnings({ "unchecked" })
	private List<ManifestoColetaNotaCreditoDto> findDadosManifestoColeta(Long idControleCarga, Long idNotaCredito, String tpCalculoNotaCredito){
		List<String> dsEnderecosPessoas = manifestoEntregaService.findEnderecosManifestosEntregas(idControleCarga);
		
		List<ManifestoColetaNotaCreditoDto> result = new ArrayList<ManifestoColetaNotaCreditoDto>();
		List<ManifestoColeta> manifestosColetas = manifestoColetaService.findManifestoColetaByIdControleCarga(idControleCarga);
		for (ManifestoColeta manifestoColeta : manifestosColetas) {
			ManifestoColetaControleCargaNotaCreditoDto dto = new ManifestoColetaControleCargaNotaCreditoDto(manifestoColeta);
			
			List<PedidoColeta> pedidosColeta = pedidoColetaService.findPedidoColetaByIdManifestoColetaIdNotaCredito(manifestoColeta.getIdManifestoColeta(),
					idNotaCredito);

			if(pedidosColeta == null || pedidosColeta.isEmpty()){
				dto.setIsManifestoForaNota(true);
				ManifestoColetaNotaCreditoDto manifestoColetaNotaCreditoDto = new ManifestoColetaNotaCreditoDto(dto);
				result.add(manifestoColetaNotaCreditoDto);
			}else{
				for (PedidoColeta pedidoColeta : pedidosColeta) {
					
					Map<String, Object> map = new HashMap<String, Object>();
					
					map.put("sgFilial", pedidoColeta.getFilialByIdFilialSolicitante().getSgFilial()); 
					map.put("nrPedidoColeta", pedidoColeta.getNrColeta());
					
					EnderecoPessoa ep = pedidoColeta.getEnderecoPessoa();
					
					if ( ep != null) {
						map.put("enderecoReal", ConhecimentoUtils.getEnderecoEntregaReal(ep));
					}
			
					map.put("qtVolumes", pedidoColeta.getQtTotalVolumesVerificado());
					map.put("psTotalVerificado", pedidoColeta.getPsTotalVerificado());
					map.put("psTotalAforadoVerificado", pedidoColeta.getPsTotalAforadoVerificado());
					
					ManifestoColetaNotaCreditoDto manifestoColetaNotaCreditoDto = new ManifestoColetaNotaCreditoDto(map, dto, tpCalculoNotaCredito, dsEnderecosPessoas);
					result.add(manifestoColetaNotaCreditoDto);
				}
			}
		}

		return result;
	}
	
	public JRBeanCollectionDataSource findTabelaDeFrete(Long idNotaCredito, String tpCalculoNotaCredito) {
		return new JRBeanCollectionDataSource(findDadosTabelaDeFrete(idNotaCredito, tpCalculoNotaCredito));
	}
	
	public List<Map<String, Object>> findMapTabelaDeFrete(Long idNotaCredito, String tpCalculoNotaCredito) {
		List<TabelaFreteNotaCreditoDto> dados = findDadosTabelaDeFrete(idNotaCredito, tpCalculoNotaCredito);				
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (TabelaFreteNotaCreditoDto tabela : dados){
			Map<String, Object> tabelaFrete = new HashMap<String, Object>();
			tabelaFrete.put("dsCliente", tabela.getDsCliente());
			tabelaFrete.put("dsFracaoPeso", tabela.getDsFracaoPeso());
			tabelaFrete.put("dtVigenciaFinal", tabela.getDtVigenciaFinal());
			tabelaFrete.put("dtVigenciaInicial", tabela.getDtVigenciaInicial());
			tabelaFrete.put("pePernoite", tabela.getPePernoite());
			tabelaFrete.put("peSobreFrete", tabela.getPeSobreFrete());
			tabelaFrete.put("peSobreValorMercadoria", tabela.getPeSobreValorMercadoria());
			tabelaFrete.put("qtFracaoPeso", tabela.getQtFracaoPeso());
			tabelaFrete.put("vlKm", tabela.getQtKm());
			tabelaFrete.put("tpCalculo", tabela.getTpCalculo());
			tabelaFrete.put("vlDefinidoPeSobreValorMercadoria", tabela.getVlDefinidoPeSobreValorMercadoria());
			tabelaFrete.put("vlDefinidoSobreFrete", tabela.getVlDefinidoSobreFrete());
			tabelaFrete.put("vlDiaria", tabela.getVlDiaria());
			tabelaFrete.put("vlEvento", tabela.getVlEvento());
			tabelaFrete.put("vlFracaoPeso", tabela.getVlFracaoPeso());
			tabelaFrete.put("vlVolume", tabela.getQtVolume());
			tabelaFrete.put("vlPeSobreMerc", tabela.getPeSobreValorMercadoria());
			tabelaFrete.put("vlPeSobreFrete", tabela.getPeSobreFrete());
			tabelaFrete.put("vlMinimoPeSobreMerc", tabela.getVlDefinidoPeSobreValorMercadoria());
			tabelaFrete.put("vlMinimoPeSobreFrete", tabela.getVlDefinidoSobreFrete());
			List<Map<String, Object>> parcelas = new ArrayList<Map<String, Object>>();
			for (TabelaNotaCreditoParcelasCeDto parcela : tabela.getListParcelasCe()){
				Map<String, Object> tabelaParcelaCe = new HashMap<String, Object>();
				tabelaParcelaCe.put("dsFaixaFretePeso", parcela.getDsFaixa());
				tabelaParcelaCe.put("tpFatorFretePeso", parcela.getTpFatorFretePeso());
				tabelaParcelaCe.put("vlFretePeso", parcela.getVlFretePeso());
				parcelas.add(tabelaParcelaCe);
			}
			tabelaFrete.put("listParcelaCe", parcelas);
			result.add(tabelaFrete);
		}
		return result;
	}

	private List<TabelaFreteNotaCreditoDto> findDadosTabelaDeFrete(Long idNotaCredito, String tpCalculoNotaCredito) {
		boolean foundParcelasFretePeso = false;
		
		List<TabelaFreteNotaCreditoDto> result = new ArrayList<TabelaFreteNotaCreditoDto>();
		NotaCredito notaCreditoLoaded = notaCreditoService.findById(idNotaCredito);
		List<TabelaColetaEntrega> tabelasColetasEntregas = getTabelasColetasEntregaByNotaCreditoAndTpCalculo(notaCreditoLoaded, tpCalculoNotaCredito);
		for (TabelaColetaEntrega tabelaColetaEntrega : tabelasColetasEntregas) {
			TabelaFreteNotaCreditoDto dto = new TabelaFreteNotaCreditoDto();
			foundParcelasFretePeso = false;
			for (ParcelaTabelaCe parcelaTabelaCe : tabelaColetaEntrega.getParcelaTabelaCes()) {
				dto.setDtVigenciaInicial(tabelaColetaEntrega.getDtVigenciaInicial());
				dto.setDtVigenciaFinal(tabelaColetaEntrega.getDtVigenciaFinal());
				if (tabelaColetaEntrega.getCliente() != null) {
					dto.setDsCliente(tabelaColetaEntrega.getCliente().getPessoa().getNmPessoa());
				}
				if (parcelaTabelaCe.getVlDefinido() != null && parcelaTabelaCe.getVlDefinido().doubleValue() >= 0.0d) {
					if ("DH".equals(parcelaTabelaCe.getTpParcela().getValue())) {
						dto.setVlDiaria(BigDecimalUtils.defaultBigDecimal(parcelaTabelaCe.getVlDefinido()).add(dto.getVlDiaria()));
						dto.setPePernoite(calcPercentual(parcelaTabelaCe.getPcSobreValor()).add(dto.getPePernoite()));
					} else if("EV".equals(parcelaTabelaCe.getTpParcela().getValue())) {
						dto.setVlEvento(BigDecimalUtils.defaultBigDecimal(parcelaTabelaCe.getVlDefinido()).add(dto.getVlEvento()));
					} else if("QU".equals(parcelaTabelaCe.getTpParcela().getValue())) {
						dto.setQtKm(BigDecimalUtils.defaultBigDecimal(parcelaTabelaCe.getVlDefinido()).add(dto.getQtKm()));
					} else if("PV".equals(parcelaTabelaCe.getTpParcela().getValue())) {
						dto.setVlDefinidoPeSobreValorMercadoria(BigDecimalUtils.defaultBigDecimal(parcelaTabelaCe.getVlDefinido()));
						dto.setPeSobreValorMercadoria(calcPercentual(parcelaTabelaCe.getPcSobreValor()));
					} else if("PF".equals(parcelaTabelaCe.getTpParcela().getValue())) {
						dto.setVlDefinidoSobreFrete(BigDecimalUtils.defaultBigDecimal(parcelaTabelaCe.getVlDefinido()));
						dto.setPeSobreFrete(calcPercentual(parcelaTabelaCe.getPcSobreValor()));
					}
					
					dto.setTpCalculo(tabelaColetaEntrega.getTpCalculo().getDescriptionAsString());
					if("C1".equals(tpCalculoNotaCredito)) {
						if("FP".equals(parcelaTabelaCe.getTpParcela().getValue())) {
							dto.setVlFracaoPeso(BigDecimalUtils.defaultBigDecimal(parcelaTabelaCe.getVlDefinido().add(dto.getVlFracaoPeso())));
							dto.setQtFracaoPeso(calculaQtFracaoPeso(notaCreditoLoaded.getControleCarga()));
						}
					} else { // C2
						if("VO".equals(parcelaTabelaCe.getTpParcela().getValue())) {
							dto.setQtVolume(parcelaTabelaCe.getVlDefinido());
						} else if("FP".equals(parcelaTabelaCe.getTpParcela().getValue())) {									
							if (CollectionUtils.isNotEmpty(tabelaColetaEntrega.getFaixasPesoParcelaTabelaCE()) && !foundParcelasFretePeso) {
								foundParcelasFretePeso = true;
								for (FaixaPesoParcelaTabelaCE faixaPesoParcelaTabelaCE : tabelaColetaEntrega.getFaixasPesoParcelaTabelaCE()) {
									TabelaNotaCreditoParcelasCeDto tabelaNotaCreditoParcelasCeDto = new TabelaNotaCreditoParcelasCeDto();
									tabelaNotaCreditoParcelasCeDto.setTpFatorFretePeso(faixaPesoParcelaTabelaCE.getTpFator());
									tabelaNotaCreditoParcelasCeDto.setDsFaixa(faixaPesoParcelaTabelaCE.getPsInicial() + " - " + faixaPesoParcelaTabelaCE.getPsFinal());
									tabelaNotaCreditoParcelasCeDto.setVlFretePeso(BigDecimalUtils.defaultBigDecimal(faixaPesoParcelaTabelaCE.getVlValor()));
									dto.addParcelaCe(tabelaNotaCreditoParcelasCeDto);
								}
							}
						}
					}

				}
			}
			if (dto.getListParcelasCe() == null){
				TabelaNotaCreditoParcelasCeDto tabelaNotaCreditoParcelasCeDto = new TabelaNotaCreditoParcelasCeDto();
				tabelaNotaCreditoParcelasCeDto.setDsFaixa("-");
				tabelaNotaCreditoParcelasCeDto.setTpFatorFretePeso("-");
				tabelaNotaCreditoParcelasCeDto.setVlFretePeso(new BigDecimal(0));
				dto.addParcelaCe(tabelaNotaCreditoParcelasCeDto);
			}
			result.add(dto);
		}
		return result;
	}
	
	public JRBeanCollectionDataSource getArrayListAsDataSource(List list){
		return new JRBeanCollectionDataSource(list);
	}

	private BigDecimal calcPercentual(BigDecimal value) {
		return BigDecimalUtils.defaultBigDecimal(value, BigDecimal.ZERO).divide(BigDecimal.valueOf(100));
	}

	private List<TabelaColetaEntrega> getTabelasColetasEntregaByNotaCreditoAndTpCalculo(NotaCredito notaCreditoLoaded, String tpCalculoNotaCredito) {
		List<TabelaColetaEntrega> result = new ArrayList<TabelaColetaEntrega>();
		if("C1".equals(tpCalculoNotaCredito)){
			result.add(notaCreditoLoaded.getControleCarga().getTabelaColetaEntrega());
		} else {	
			for (NotaCreditoParcela parcela : notaCreditoLoaded.getNotaCreditoParcelas()) {	
				if (!result.contains(parcela.getParcelaTabelaCe().getTabelaColetaEntrega()))
					result.add(parcela.getParcelaTabelaCe().getTabelaColetaEntrega());						
			}
		}
		return result;
	}
	
	public JRBeanCollectionDataSource findParcelas(Long idNotaCredito, String tpCalculoParcela) {
		return new JRBeanCollectionDataSource(findDadosParcelas(idNotaCredito, tpCalculoParcela));
	}
	
	public Map<String, Object> findMapDadosParcelas(Long idNotaCredito, String tpCalculoParcela) {
		List<ParcelaTabelaCeNotaCreditoDto> parcelas = findDadosParcelas(idNotaCredito, tpCalculoParcela);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("vlTotalDiaria", parcelas.get(0).getVlDiaria());
		result.put("vlTotalKm", parcelas.get(0).getVlKm());
		result.put("vlTotalFracaoPeso", parcelas.get(0).getVlFracaoPeso());
		result.put("vlTotalEvento", parcelas.get(0).getVlEvento());		
		result.put("vlTotalVolume", parcelas.get(0).getVlVolume());	
		result.put("vlTotalPeSobreFrete", parcelas.get(0).getPeSobreFrete());
		result.put("vlTotalPeSobreValorMercadoria", parcelas.get(0).getPeSobreValorMercadoria());
		List<Map<String, Object>> parcelasCe = new ArrayList<Map<String, Object>>();
		for (TabelaNotaCreditoParcelasCeDto parcelaCe : parcelas.get(0).getLstTabelaNotaCreditoParcelasCeDto()){
			Map<String, Object> tabelaParcelaCe = new HashMap<String, Object>();
			tabelaParcelaCe.put("dsFaixaFretePeso", parcelaCe.getDsFaixa());
			tabelaParcelaCe.put("tpFatorFretePeso", parcelaCe.getTpFatorFretePeso());
			tabelaParcelaCe.put("vlFretePeso", parcelaCe.getVlFretePeso());
			parcelasCe.add(tabelaParcelaCe);
		}
		result.put("listParcelaCe", parcelasCe);
		return result;
	}

	private List<ParcelaTabelaCeNotaCreditoDto> findDadosParcelas(Long idNotaCredito, String tpCalculoParcela) {
		List<ParcelaTabelaCeNotaCreditoDto> result = new ArrayList<ParcelaTabelaCeNotaCreditoDto>();
		List<NotaCreditoParcela> listNotasCreditosParcelas = notaCreditoParcelaService.findByIdNotaCredito(idNotaCredito);
		ParcelaTabelaCeNotaCreditoDto dto = new ParcelaTabelaCeNotaCreditoDto();
		for (NotaCreditoParcela notaCreditoParcela : listNotasCreditosParcelas) {
			dto.setTpParcela(notaCreditoParcela.getParcelaTabelaCe().getTpParcela().getValue());

			if("PF".equals(dto.getTpParcela())) {
				dto.setPeSobreFrete(BigDecimalUtils.defaultBigDecimal(notaCreditoParcela.getVlNotaCreditoParcela()).add(dto.getPeSobreFrete()));
			}
			if("PV".equals(dto.getTpParcela())) {
				dto.setPeSobreValorMercadoria(BigDecimalUtils.defaultBigDecimal(notaCreditoParcela.getVlNotaCreditoParcela()).add(dto.getPeSobreValorMercadoria()));
			}
			BigDecimal vlCalc = BigDecimalUtils.defaultBigDecimal(notaCreditoParcela.getQtNotaCreditoParcela())
								.multiply(BigDecimalUtils.defaultBigDecimal(notaCreditoParcela.getVlNotaCreditoParcela()));

			if("DH".equals(dto.getTpParcela())) {
				dto.setVlDiaria(dto.getVlDiaria().add(vlCalc));				
			}
			if("QU".equals(dto.getTpParcela())) {
				dto.setVlKm(dto.getVlKm().add(vlCalc));
			}
			if("VO".equals(dto.getTpParcela())) {
				dto.setVlVolume(dto.getVlVolume().add(vlCalc));
			}
			if("EV".equals(dto.getTpParcela())) {
				dto.setVlEvento(dto.getVlEvento().add(vlCalc));
				dto.setVlCtrc(dto.getVlCtrc().add(vlCalc));
			}
			if("FP".equals(dto.getTpParcela())) {
				if("C1".equals(tpCalculoParcela)) {
						dto.setVlFracaoPeso(dto.getVlFracaoPeso().add(vlCalc));
				}else{
					List<FaixaPesoParcelaTabelaCE> faixasPesoParcela = notaCreditoParcela.getParcelaTabelaCe().getTabelaColetaEntrega().getFaixasPesoParcelaTabelaCE();
					if (CollectionUtils.isNotEmpty(faixasPesoParcela)) {
						FaixaPesoParcelaTabelaCE faixaPesoParcelaTabelaCE = notaCreditoParcela.getFaixaPesoParcelaTabelaCE();
						if (faixaPesoParcelaTabelaCE!=null){
							TabelaNotaCreditoParcelasCeDto tabelaNotaCreditoParcelasCeDto = new TabelaNotaCreditoParcelasCeDto();
							tabelaNotaCreditoParcelasCeDto.setTpFatorFretePeso(faixaPesoParcelaTabelaCE.getTpFator());
							tabelaNotaCreditoParcelasCeDto.setDsFaixa(faixaPesoParcelaTabelaCE.getPsInicial() + " - " + faixaPesoParcelaTabelaCE.getPsFinal());
							
							tabelaNotaCreditoParcelasCeDto.setVlFretePeso(BigDecimalUtils.defaultBigDecimal(vlCalc));
							dto.addParcelaCe(tabelaNotaCreditoParcelasCeDto);
						}
					}
				}
			}
		}
		if (dto.getLstTabelaNotaCreditoParcelasCeDto() == null){
			TabelaNotaCreditoParcelasCeDto tabelaNotaCreditoParcelasCeDto = new TabelaNotaCreditoParcelasCeDto();
			tabelaNotaCreditoParcelasCeDto.setVlFretePeso(new BigDecimal(0));
			dto.addParcelaCe(tabelaNotaCreditoParcelasCeDto);
		}
		result.add(dto);
		return result;
	}

	private BigDecimal calculaQtFracaoPeso(ControleCarga controleCarga) {
		Double qtPeso = 0.0;
		if(controleCarga.getMeioTransporteByIdTransportado() != null){
			TipoMeioTransporte tipoMeioTransporte = controleCarga.getMeioTransporteByIdTransportado().getModeloMeioTransporte().getTipoMeioTransporte();			
			qtPeso = (double) (tipoMeioTransporte.getNrCapacidadePesoInicial() + tipoMeioTransporte.getNrCapacidadePesoFinal());
			qtPeso = qtPeso / 2;
			qtPeso *= (double) controleCarga.getFilialByIdFilialOrigem().getNrFranquiaPeso() / 100.0;
		}
		BigDecimal result = new BigDecimal(qtPeso);
		return result;
	}

	public File executeNotaCreditoPadraoReport(Long idNotaCredito, Boolean preview) {
		
		NotaCredito notaCredito = notaCreditoService.findById(idNotaCredito);
		
		if("C".equals(notaCredito.getTpNotaCredito().getValue()) || "E".equals(notaCredito.getTpNotaCredito().getValue())){
			validaManifestoAberto(idNotaCredito);		
		}
		
		
		Map<String, Object> summary = notaCreditoPadraoService.findDataPDFReport(idNotaCredito, preview);
		
		if(summary != null && !summary.isEmpty()){			
			return new NotaCreditoPadraoPDF(summary, preview).getReport();
		}
		
		return null;
	}
	
	private void validaManifestoAberto(Long idNotaCredito) {
		validateManifestoEmAberto(idNotaCredito);
				}

	private String formatToMoney(BigDecimal vl) {
		return FormatUtils.formatDecimal("###,###,##0.00", vl, true);
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
		this.notaCreditoService = notaCreditoService;
	}

    public void setNotaCreditoColetaService(NotaCreditoColetaService notaCreditoColetaService) {
        this.notaCreditoColetaService = notaCreditoColetaService;
	}

    public void setNotaCreditoDoctoService(NotaCreditoDoctoService notaCreditoDoctoService) {
        this.notaCreditoDoctoService = notaCreditoDoctoService;
    }

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}


	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}


	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}


	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
}

	public void setParcelaTabelaCeService(ParcelaTabelaCeService parcelaTabelaCeService) {
		this.parcelaTabelaCeService = parcelaTabelaCeService;
	}

	public void setManifestoColetaService(ManifestoColetaService manifestoColetaService) {
		this.manifestoColetaService = manifestoColetaService;
	}

	public void setTabelaColetaEntregaService(TabelaColetaEntregaService tabelaColetaEntregaService) {
		this.tabelaColetaEntregaService = tabelaColetaEntregaService;
	}

	public void setNotaCreditoParcelaService(NotaCreditoParcelaService notaCreditoParcelaService) {
		this.notaCreditoParcelaService = notaCreditoParcelaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public EventoNotaCreditoService getEventoNotaCreditoService() {
		return eventoNotaCreditoService;
}

	public void setEventoNotaCreditoService(
			EventoNotaCreditoService eventoNotaCreditoService) {
		this.eventoNotaCreditoService = eventoNotaCreditoService;
	}

	public OcorrenciaEntregaService getOcorrenciaEntregaService() {
		return ocorrenciaEntregaService;
	}

	public void setOcorrenciaEntregaService(
			OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public AwbService getAwbService() {
		return awbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public NotaCreditoPadraoService getNotaCreditoPadraoService() {
		return notaCreditoPadraoService;
	}

	public void setNotaCreditoPadraoService(
			NotaCreditoPadraoService notaCreditoPadraoService) {
		this.notaCreditoPadraoService = notaCreditoPadraoService;
	}	
	
}
