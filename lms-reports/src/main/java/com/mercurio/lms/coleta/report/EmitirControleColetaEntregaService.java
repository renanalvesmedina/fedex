package com.mercurio.lms.coleta.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sgr.model.service.ProcedimentoGerRiscoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de Controle de Cargas Coleta-Entrega
 * Especificação técnica 02.01.02.08
 * 
 * @spring.bean id="lms.coleta.emitirControleColetaEntregaService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirControleColetaEntrega.jasper"
 */

public class EmitirControleColetaEntregaService extends ReportServiceSupport{

	private ProcedimentoGerRiscoService procedimentoGerRiscoService;
	private MessageSource messageSource;
	private PedidoColetaService pedidoColetaService;
	private FilialService filialService;
	private ControleCargaService controleCargaService;

	public void setProcedimentoGerRiscoService(ProcedimentoGerRiscoService procedimentoGerRiscoService) {
		this.procedimentoGerRiscoService = procedimentoGerRiscoService;
	}
	
	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

    /**
     * método responsável por gerar o relatório. 
     */
    @Override
	public JRReportDataObject execute(Map parameters) throws Exception {
    	StringBuilder sql = new StringBuilder()
	    	.append("select ")
	    	.append("filialOrigem.SG_FILIAL AS SG_FILIAL, ")
	    	.append("controleCarga.NR_CONTROLE_CARGA AS NR_CONTROLE_CARGA, ")
	    	.append("controleCarga.TP_CONTROLE_CARGA AS TP_CONTROLE_CARGA, ")
	    	.append("controleCarga.DH_GERACAO AS DH_GERACAO, ")
	    	.append("rotaColetaEntrega.NR_ROTA AS NR_ROTA, ")
	    	.append("rotaColetaEntrega.DS_ROTA AS DS_ROTA, ")
	    	.append("tipoTabelaColetaEntrega.DS_TIPO_TABELA_COLETA_ENTREGA AS DS_TIPO_TABELA_COLETA_ENTREGA,")
	    	.append("meioTransporteTransportado.NR_FROTA AS NR_FROTA_TRANSPORTADO, ")
	    	.append("meioTransporteTransportado.NR_IDENTIFICADOR AS NR_IDENTIFICADOR_TRANSPORTADO, ")
	    	.append("meioTransporteSemiRebocado.NR_FROTA AS NR_FROTA_SEMI_REBOCADO, ")
	    	.append("meioTransporteSemiRebocado.NR_IDENTIFICADOR AS NR_IDENTIFICADOR_SEMI_REBOCADO, ")
	    	.append("pessoaProprietario.TP_IDENTIFICACAO AS TP_IDENTIFICACAO_PROPRIETARIO, ")
	    	.append("pessoaProprietario.NR_IDENTIFICACAO AS NR_IDENTIFICACAO_PROPRIETARIO, ")
	    	.append("pessoaProprietario.NM_PESSOA AS NM_PESSOA_PROPRIETARIO, ")
	    	.append("pessoaMotorista.TP_IDENTIFICACAO AS TP_IDENTIFICACAO_MOTORISTA, ")
	    	.append("pessoaMotorista.NR_IDENTIFICACAO AS NR_IDENTIFICACAO_MOTORISTA, ")
	    	.append("pessoaMotorista.NM_PESSOA AS NM_PESSOA_MOTORISTA, ")
	    	.append("controleCarga.PS_TOTAL_FROTA AS PS_TOTAL_FROTA, ")
	    	.append("controleCarga.PS_TOTAL_AFORADO AS PS_TOTAL_AFORADO, ")
	    	.append("(100 - controleCarga.PC_OCUPACAO_CALCULADO) AS PC_OCUPACAO_CALCULADO, ")
	    	.append("(100 - controleCarga.PC_OCUPACAO_AFORADO_CALCULADO) AS PC_OCUPACAO_AFORADO_CALCULADO, ")
	    	.append("(100 - controleCarga.PC_OCUPACAO_INFORMADO) AS PC_OCUPACAO_INFORMADO, ")
	    	.append("controleQuilometragem.NR_QUILOMETRAGEM AS NR_QUILOMETRAGEM, ")
	    	.append("controleCarga.BL_EXIGE_CIOT AS BL_EXIGE_CIOT, ")
	    	.append("ciot.NR_CIOT AS NR_CIOT, ")
	    	.append("ciot.NR_COD_VERIFICADOR AS NR_COD_VERIFICADOR ")
	    	.append("from ")
	    	.append("controle_carga controleCarga ") 
	    	.append("INNER JOIN filial filialOrigem on (controleCarga.ID_FILIAL_ORIGEM = filialOrigem.ID_FILIAL) ")
	    	.append("LEFT JOIN rota_coleta_entrega rotaColetaEntrega on (controleCarga.ID_ROTA_COLETA_ENTREGA = rotaColetaEntrega.ID_ROTA_COLETA_ENTREGA) ")
	    	.append("LEFT JOIN tipo_tabela_coleta_entrega tipoTabelaColetaEntrega on (controleCarga.ID_TIPO_TABELA_COLETA_ENTREGA = tipoTabelaColetaEntrega.ID_TIPO_TABELA_COLETA_ENTREGA) ")
	    	.append("LEFT JOIN meio_transporte meioTransporteTransportado on (controleCarga.ID_TRANSPORTADO = meioTransporteTransportado.ID_MEIO_TRANSPORTE) ")
	    	.append("LEFT JOIN meio_transporte meioTransporteSemiRebocado on (controleCarga.ID_SEMI_REBOCADO = meioTransporteSemiRebocado.ID_MEIO_TRANSPORTE) ")
	    	.append("LEFT JOIN proprietario on (controleCarga.ID_PROPRIETARIO = proprietario.ID_PROPRIETARIO) ")
	    	.append("LEFT JOIN pessoa pessoaProprietario on (proprietario.ID_PROPRIETARIO = pessoaProprietario.ID_PESSOA) ")
	    	.append("LEFT JOIN motorista on (controleCarga.ID_MOTORISTA = motorista.ID_MOTORISTA) ")
	    	.append("LEFT JOIN pessoa pessoaMotorista on (motorista.ID_MOTORISTA = pessoaMotorista.ID_PESSOA) ")
	    	.append("LEFT JOIN controle_quilometragem controleQuilometragem on (controleCarga.ID_CONTROLE_CARGA = controleQuilometragem.ID_CONTROLE_CARGA AND controleQuilometragem.BL_SAIDA = 'S') ")
	    	.append("LEFT JOIN ciot_controle_carga ciotControleCarga on (controleCarga.ID_CONTROLE_CARGA = ciotControleCarga.ID_CONTROLE_CARGA) ")
	    	.append("LEFT JOIN ciot on (ciotControleCarga.ID_CIOT = ciot.ID_CIOT) ")
	    	.append("where ")
	    	.append("controleCarga.ID_CONTROLE_CARGA = ? ");
    	
    	TypedFlatMap typedFlatMap = (TypedFlatMap)parameters;

    	Object[] criterias = new Object[1];
		criterias[0] = typedFlatMap.getLong("controleCarga.idControleCarga");
		
		Boolean blEmissao = typedFlatMap.getBoolean("blEmissao") != null ? typedFlatMap.getBoolean("blEmissao") : true;
		String labelReemissao = "";
		if (!blEmissao.booleanValue()) {
			labelReemissao = (messageSource.getMessage("reemissao", null, LocaleContextHolder.getLocale())).toUpperCase();
		}
		
		logger.info(sql.toString());
		JRReportDataObject jr = executeQuery(sql.toString(),criterias);

        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        
        Moeda moedaUsuario = SessionUtils.getMoedaSessao();
        
        Long idControleCarga = typedFlatMap.getLong("controleCarga.idControleCarga");
        
        parametersReport.put("blPrintTermoResponsabilidade", controleCargaService.validateImprimeTermoRespColeta(idControleCarga));
        parametersReport.put("blPrintContratoPrestacao", controleCargaService.validateImprimeContratoPrestServColeta(idControleCarga));
        parametersReport.put("blPrintModeloDeclaracaoRet", controleCargaService.validateImprimeModeloDeclaracaoPrevSocialColeta(idControleCarga));
        
        parametersReport.put("labelReemissao", labelReemissao);
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("filialUsuarioEmissor", typedFlatMap.containsKey("filialUsuarioEmissor") 
        												? filialService.findSgFilialByIdFilial(typedFlatMap.getLong("filialUsuarioEmissor")) 
        													: SessionUtils.getFilialSessao().getSgFilial());
        parametersReport.put("dsSimboloMoedaUsuario", moedaUsuario.getSgMoeda() + " " + moedaUsuario.getDsSimbolo());
        parametersReport.put("idControleCarga", typedFlatMap.getLong("controleCarga.idControleCarga"));
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));

        jr.setParameters(parametersReport);
        return jr;
    }

    @Override
	public void configReportDomains(ReportDomainConfig config) {
    	config.configDomainField("TP_CONTROLE_CARGA", "DM_TIPO_CONTROLE_CARGAS"); 
    }

    /**
     * Método que gera o subrelatorio de Controle de Cargas - Lacres. 
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirControleColetaEntregaLacres (Object[] parameters) throws Exception {
        SqlTemplate sql = new SqlTemplate();
        // select
        sql.addProjection("lacreControleCarga.NR_LACRES","NR_LACRES");
        //from
        sql.addFrom("LACRE_CONTROLE_CARGA", "lacreControleCarga");
        // where
        sql.addCriteria("lacreControleCarga.ID_CONTROLE_CARGA","=", parameters[0]);
        sql.addCriteria("lacreControleCarga.TP_STATUS_LACRE","=", "FE");
        sql.addCustomCriteria("lacreControleCarga.NR_LACRES IS NOT NULL");
        
        logger.info(sql.toString());
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }

    /**
     * Método que gera o subrelatorio de Controle de Cargas - Ocorrencias. 
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirControleColetaEntregaOcorrencias (Object[] parameters) throws Exception {
        return new JREmptyDataSource();
    }
    
    /**
     * Método que gera o subrelatorio de Controle de Cargas - PagamentoPostos. 
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirControleColetaEntregaPagamentoPostos (Object[] parameters) throws Exception {
    	StringBuilder sql = new StringBuilder()
	    	.append("select ")
	    	.append("pagtoPedagioCC.ID_PAGTO_PEDAGIO_CC AS ID_PAGTO_PEDAGIO_CC, ")
	    	.append(PropertyVarcharI18nProjection.createProjection("tipoPagamPostoPassagem.DS_TIPO_PAGAM_POSTO_PASSAGEM_I", "DS_TIPO_PAGAM_POSTO_PASSAGEM")).append(", ")
	    	.append("moeda.SG_MOEDA AS SG_MOEDA, ")
	    	.append("moeda.DS_SIMBOLO AS DS_SIMBOLO, ")
	    	.append("pagtoPedagioCC.VL_PEDAGIO, ")
	    	.append("pessoaOperadora.NM_PESSOA AS NM_PESSOA_OPERADORA, ")
	    	.append("cartaoPedagio.NR_CARTAO AS NR_CARTAO ")
	    	.append("from ")
	    	.append("PAGTO_PEDAGIO_CC pagtoPedagioCC ")
	    	.append("INNER JOIN MOEDA moeda on (pagtoPedagioCC.ID_MOEDA = moeda.ID_MOEDA) ")
	    	.append("LEFT JOIN CARTAO_PEDAGIO cartaoPedagio on (pagtoPedagioCC.ID_CARTAO_PEDAGIO = cartaoPedagio.ID_CARTAO_PEDAGIO) ")
	    	.append("LEFT JOIN OPERADORA_CARTAO_PEDAGIO operadoraCartaoPedagio on (pagtoPedagioCC.ID_OPERADORA_CARTAO_PEDAGIO = operadoraCartaoPedagio.ID_OPERADORA_CARTAO_PEDAGIO) ")
	    	.append("LEFT JOIN PESSOA pessoaOperadora on (operadoraCartaoPedagio.ID_OPERADORA_CARTAO_PEDAGIO = pessoaOperadora.ID_PESSOA) ")
	    	.append("INNER JOIN TIPO_PAGAM_POSTO_PASSAGEM tipoPagamPostoPassagem on (pagtoPedagioCC.ID_TIPO_PAGAM_POSTO_PASSAGEM = tipoPagamPostoPassagem.ID_TIPO_PAGAM_POSTO_PASSAGEM) ")
	    	.append("where ")
	    	.append("pagtoPedagioCC.ID_CONTROLE_CARGA = ? ");
    	
		Object[] criterias = new Object[1];
		criterias[0] = parameters[0];

		logger.info(sql.toString());
        return executeQuery(sql.toString(),criterias).getDataSource();
    }
    
    /**
     * Método que gera o subrelatorio de Controle de Cargas - Manifestos. 
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirControleColetaEntregaManifestos (Object[] parameters) throws Exception {
    	StringBuilder sqlColeta = new StringBuilder()
	    	.append("select ")
			.append("filialOrigem.SG_FILIAL AS SG_FILIAL, ")
			.append("manifestoColeta.ID_MANIFESTO_COLETA as ID_MANIFESTO_COLETA, ")
			.append("manifestoColeta.NR_MANIFESTO AS NR_MANIFESTO, ") 
			.append("(select sum (pc.PS_TOTAL_VERIFICADO) FROM PEDIDO_COLETA pc ") 
			.append("WHERE pc.ID_MANIFESTO_COLETA = manifestoColeta.ID_MANIFESTO_COLETA ")
			.append(") AS PESO, ")
			.append("(select sum (pc.PS_TOTAL_AFORADO_VERIFICADO) FROM PEDIDO_COLETA pc ")
			.append("WHERE pc.ID_MANIFESTO_COLETA = manifestoColeta.ID_MANIFESTO_COLETA ")
			.append(") AS PS_AFORADO, ")
			.append("(select count(*) FROM PEDIDO_COLETA pc ") 
			.append("WHERE pc.ID_MANIFESTO_COLETA = manifestoColeta.ID_MANIFESTO_COLETA ")
			.append(") AS QTD ")
			.append("from ")
			.append("MANIFESTO_COLETA manifestoColeta ")
			.append("INNER JOIN FILIAL filialOrigem on (manifestoColeta.ID_FILIAL_ORIGEM = filialOrigem.ID_FILIAL) ")
			.append("where ")
			.append("manifestoColeta.ID_CONTROLE_CARGA = ? ")
	    	.append("order by ")
			.append("SG_FILIAL, NR_MANIFESTO ");

    	StringBuilder sqlEntrega = new StringBuilder()
	    	.append("select ")
	    	.append("filialOrigem.SG_FILIAL AS SG_FILIAL, ")
	    	.append("manifestoEntrega.NR_MANIFESTO_ENTREGA AS NR_MANIFESTO, ") 
	    	.append("manifesto.TP_MANIFESTO AS TP_OPERACAO, ")
	    	.append("manifesto.TP_MANIFESTO_ENTREGA AS TP_MANIFESTO, ") 
	    	.append("manifesto.PS_TOTAL_MANIFESTO AS PESO, ")
	    	.append("manifesto.PS_TOTAL_AFORADO_MANIFESTO AS PS_AFORADO, ") 
	    	.append("manifesto.VL_TOTAL_MANIFESTO AS VL_MERCADORIA, ")
	    	.append("(select count(*) FROM ")
	    	.append("MANIFESTO_ENTREGA_DOCUMENTO med ")
	    	.append("INNER JOIN MANIFESTO_ENTREGA me on (med.ID_MANIFESTO_ENTREGA = me.ID_MANIFESTO_ENTREGA) ")
	    	.append("WHERE me.ID_MANIFESTO_ENTREGA = manifesto.ID_MANIFESTO ")
	    	.append(") AS QTD ")
	    	.append("from ") 
	    	.append("MANIFESTO manifesto ")
	    	.append("INNER JOIN FILIAL filialOrigem on (manifesto.ID_FILIAL_ORIGEM = filialOrigem.ID_FILIAL) ")
	    	.append("INNER JOIN MANIFESTO_ENTREGA manifestoEntrega on (manifesto.ID_MANIFESTO = manifestoEntrega.ID_MANIFESTO_ENTREGA) ")
	    	.append("where ")
	    	.append("manifesto.ID_CONTROLE_CARGA = ? ")
	    	.append("order by ")
			.append("SG_FILIAL, NR_MANIFESTO ");
    	
		Object[] criterias = new Object[1];
		criterias[0] = parameters[0];

		List linhas = populateLinhasManifesto(sqlColeta, criterias, true);
		linhas.addAll(populateLinhasManifesto(sqlEntrega, criterias, false));
 
        return new JRMapCollectionDataSource(linhas);
    }
    
	private List populateLinhasManifesto(StringBuilder sql, Object[] criterias, boolean tipoIsColeta) {
		final boolean isColeta = tipoIsColeta;
		
		logger.info(sql.toString());
		// Esta implementação se baseia no formato de ResultSetExtractor do JdbcTemplate do Spring.
		List linhas = (List) getJdbcTemplate().query(sql.toString(), criterias, new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				List linhasRs = new LinkedList();
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				while (rs.next()) {
					// otimizando tamanho do HashMap.
					Map dados = new HashMap(8);
					
					String tpOperacao = isColeta ? null : getDomainValueService().
			    			findDomainValueByValue("DM_TIPO_MANIFESTO", rs.getString("TP_OPERACAO")).getDescription().toString();
					
					String tpManifesto = isColeta ? null : getDomainValueService().
							findDomainValueByValue("DM_TIPO_MANIFESTO_ENTREGA", rs.getString("TP_MANIFESTO")).getDescription().toString();
					
					BigDecimal vlMercadoria = null;
					if (isColeta) {
						typedFlatMap.clear();
						typedFlatMap.put("idManifestoColeta", Long.valueOf(rs.getLong("ID_MANIFESTO_COLETA")) );
						typedFlatMap = pedidoColetaService.findSumValoresByMoedaByManifestoColeta(typedFlatMap.getLong("idManifestoColeta"));
						vlMercadoria = typedFlatMap.getBigDecimal("vlTotalVerificado");
					}
					else
						vlMercadoria = rs.getBigDecimal("VL_MERCADORIA");

					dados.put("SG_FILIAL", rs.getString("SG_FILIAL"));
					dados.put("NR_MANIFESTO", Integer.valueOf(rs.getInt("NR_MANIFESTO")) );
			    	dados.put("TP_OPERACAO", tpOperacao);
			    	dados.put("TP_MANIFESTO", tpManifesto);
			    	dados.put("PESO", rs.getBigDecimal("PESO"));
			    	dados.put("PS_AFORADO", rs.getBigDecimal("PS_AFORADO")); 
			    	dados.put("VL_MERCADORIA", vlMercadoria);
					dados.put("QTD", Integer.valueOf(rs.getInt("QTD")) );
					linhasRs.add(dados);
				}
				return linhasRs;
			}
		});
		return linhas;
	}
    
    /**
     * Método que gera o subrelatorio de Controle de Cargas - Equipe. 
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirControleColetaEntregaEquipe (Object[] parameters) throws Exception {
    	StringBuilder sql = new StringBuilder()
    		.append("select ")
	    	.append("equipe.ID_EQUIPE, ")
	    	.append("equipe.DS_EQUIPE, ")
	    	.append("integranteEqOperac.TP_INTEGRANTE, ")
	    	
	    	.append("case when integranteEqOperac.TP_INTEGRANTE = 'F' ") 
	    	.append("then usuario.NM_USUARIO ")
	    	.append("else pessoaTerceiro.NM_PESSOA end AS NOME, ")
	    	
	    	.append("case when integranteEqOperac.TP_INTEGRANTE = 'F' ") 
	    	.append("then usuario.NR_MATRICULA ")
	    	.append("else pessoaTerceiro.NR_IDENTIFICACAO end AS NR_IDENTIFICACAO, ")
	    	
	    	.append("case when integranteEqOperac.TP_INTEGRANTE = 'F' ")
	    	.append("then '' ")
	    	.append("else pessoaTerceiro.TP_IDENTIFICACAO end AS TP_IDENTIFICACAO, ")
	
	    	.append("case when integranteEqOperac.TP_INTEGRANTE = 'F' ")
	    	.append("then vPfuncao.NOME ")
	    	.append("else cargoOperacional.DS_CARGO end AS CARGO, ")
	
	    	.append("case when integranteEqOperac.TP_INTEGRANTE = 'F' ") 
	    	.append("then '' ")
	    	.append("else pessoaEmpresa.NM_PESSOA end AS NM_EMPRESA ")
	
	    	.append("from ")
	    	.append("EQUIPE equipe ")
	    	.append("INNER JOIN EQUIPE_OPERACAO equipeOperacao on (equipe.ID_EQUIPE = equipeOperacao.ID_EQUIPE) ")
	    	.append("INNER JOIN INTEGRANTE_EQ_OPERAC integranteEqOperac on (equipeOperacao.ID_EQUIPE_OPERACAO = integranteEqOperac.ID_EQUIPE_OPERACAO) ")
	
	    	.append("LEFT JOIN USUARIO usuario on (integranteEqOperac.ID_USUARIO = usuario.ID_USUARIO) ")
	    	.append("LEFT JOIN V_PFUNC vPfunc on (usuario.NR_MATRICULA = vPfunc.CHAPA) ")
	    	.append("LEFT JOIN V_PFUNCAO vPfuncao on (vpFunc.CODFUNCAO = vPfuncao.CODIGO) ")
	
	    	.append("LEFT JOIN PESSOA pessoaTerceiro on (integranteEqOperac.ID_PESSOA = pessoaTerceiro.ID_PESSOA) ")
	    	.append("LEFT JOIN CARGO_OPERACIONAL cargoOperacional on (integranteEqOperac.ID_CARGO_OPERACIONAL = cargoOperacional.ID_CARGO_OPERACIONAL) ")
	    	.append("LEFT JOIN EMPRESA empresa on (integranteEqOperac.ID_EMPRESA = empresa.ID_EMPRESA) ")
	    	.append("LEFT JOIN PESSOA pessoaEmpresa on (empresa.ID_EMPRESA = pessoaEmpresa.ID_PESSOA) ")
	
	    	.append("where ")
	    	.append("equipeOperacao.ID_CONTROLE_CARGA = ? ")
	    	.append("AND equipeOperacao.DH_INICIO_OPERACAO =  ")
	    	.append("(select max(eo.DH_INICIO_OPERACAO) from EQUIPE_OPERACAO eo where eo.ID_CONTROLE_CARGA = ?) ")
	
	    	.append("ORDER BY integranteEqOperac.TP_INTEGRANTE asc, NOME asc ");
    	
		Object[] criterias = new Object[2];
		criterias[0] = parameters[0];
		criterias[1] = parameters[0];

		logger.info(sql.toString());
        return executeQuery(sql.toString(),criterias).getDataSource();
    }

    /**
     * Método que gera o subrelatorio de Controle de Cargas - Gerenciamento de riscos. 
     * @param parameters
     * @return
     * @throws Exception
     */
    public JRDataSource executeEmitirControleColetaEntregaGerRiscos (Object[] parameters) throws Exception {
    	List lista = procedimentoGerRiscoService.generateExigenciasGerRiscoParaColetaEntrega(
    			(Long)parameters[0], 
    			SessionUtils.getMoedaSessao().getIdMoeda(), 
    			SessionUtils.getPaisSessao().getIdPais(), 
    			"A");
 
        return new JRMapCollectionDataSource(lista);
    }
    
    public JRDataSource executeEmitirTermoResponsabilidadeTransporteColetaEntrega(Object[] parameters) throws Exception {    	
    	SqlTemplate sql = createSqlTemplate();
    	
    	sql.addProjection("PESSOA.tp_pessoa", "tp_pessoa");
    	sql.addProjection("CONTROLE_CARGA.ID_CONTROLE_CARGA", "id_controle_carga");
        sql.addFrom("PESSOA", "PESSOA");
        sql.addFrom("CONTROLE_CARGA", "CONTROLE_CARGA");
        sql.addFrom("PROPRIETARIO", "PROPRIETARIO");
        sql.addJoin("PROPRIETARIO.id_proprietario", "PESSOA.id_pessoa");
        sql.addJoin("CONTROLE_CARGA.id_proprietario", "PESSOA.id_pessoa");
        sql.addCriteria("CONTROLE_CARGA.ID_CONTROLE_CARGA", "=", parameters[0]);
        sql.addCustomCriteria("PROPRIETARIO.tp_proprietario = 'E'");
        
        JRDataSource jrdt = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource(); 
        if (jrdt == null) {
        	jrdt = new JRMapCollectionDataSource(null);
        }
        
        return jrdt;
    }     

    public JRDataSource executeEmitirModeloDeclaracaoRetencaoTransporteColetaEntrega(Object[] parameters) throws Exception {    	
    	SqlTemplate sql = createSqlTemplate();
    	
    	Calendar calendar = Calendar.getInstance();
    	int ano = calendar.get(Calendar.YEAR);
    	int dia = calendar.get(Calendar.DAY_OF_MONTH);
    	
    	Locale local = new Locale("pt","BR");
    	DateFormat dateFormat = new SimpleDateFormat("MMMM",local); 
    	String mes = dateFormat.format(calendar.getTime()).toUpperCase();
    	
    	sql.addProjection("PROPRIETARIO.nr_pis", "nr_pis");//Se retorno nulo, vazio ou 0 (zero) deixar campo em branco.
    	sql.addProjection("PROPRIETARIO.nr_dependentes", "nr_dependentes");
    	sql.addProjection("PESSOA.nm_pessoa", "nm_pessoa");//Formatar o nome em letras maiúsculas
    	sql.addProjection("PESSOA.tp_identificacao", "tp_identificacao");//Formatar a descrição em letras maiúsculas
    	sql.addProjection("PESSOA.nr_identificacao", "nr_identificacao");//Formatar de acordo com o tipo [2], conforme segue.  //“CPF” NNN.NNN.NNN-NN //“CNPJ” NN.NNN.NNN/NNNN-NN
    	sql.addProjection("MUNICIPIO.nm_municipio", "nm_municipio");
    	sql.addProjection("MUNICIPIO_F.nm_municipio", "nm_municipio_f");
    	sql.addProjection("" + ano, "ano");
    	sql.addProjection("" + dia, "dia");
    	sql.addProjection("'" + mes+"'", "mes");
    	
    	sql.addProjection("(OPERACAO_SERVICO_LOCALIZA.nr_tempo_entrega / 1440) || ' dia' ", "nr_tempo_entrega");
    	//Se retorno nulo, vazio ou 0 (zero) apresentar 1 dia. Sempre concatenar o numeral de retorno ou 
        //fixo 1 com a palavra “dia”. Exemplo: “1 dia”.
    	
    	sql.addFrom("CONTROLE_CARGA", "CONTROLE_CARGA");
        sql.addFrom("PROPRIETARIO", "PROPRIETARIO");
        sql.addFrom("PESSOA", "PESSOA");
        sql.addFrom("PESSOA", "PESSOA_FILIAL");
        sql.addFrom("MUNICIPIO", "MUNICIPIO");
        sql.addFrom("MUNICIPIO", "MUNICIPIO_F");
        sql.addFrom("ROTA_INTERVALO_CEP", "ROTA_INTERVALO_CEP");
        sql.addFrom("ENDERECO_PESSOA", "ENDERECO_PESSOA");
        sql.addFrom("MUNICIPIO_FILIAL", "MUNICIPIO_FILIAL");
        sql.addFrom("OPERACAO_SERVICO_LOCALIZA", "OPERACAO_SERVICO_LOCALIZA");
        
        sql.addJoin("PROPRIETARIO.id_proprietario", "CONTROLE_CARGA.id_proprietario");
        sql.addJoin("PESSOA.id_pessoa", "CONTROLE_CARGA.id_proprietario");
        sql.addJoin("CONTROLE_CARGA.id_rota_coleta_entrega", "ROTA_INTERVALO_CEP.id_rota_coleta_entrega");
        sql.addJoin("ROTA_INTERVALO_CEP.id_municipio", "MUNICIPIO.id_municipio");
        sql.addJoin("PESSOA_FILIAL.id_pessoa", "MUNICIPIO_FILIAL.id_filial");
        sql.addJoin("PESSOA_FILIAL.id_endereco_pessoa", "ENDERECO_PESSOA.id_endereco_pessoa");
        sql.addJoin("ENDERECO_PESSOA.id_municipio", "MUNICIPIO_FILIAL.id_municipio");
        sql.addJoin("MUNICIPIO_FILIAL.id_municipio", "MUNICIPIO_F.id_municipio");
        sql.addJoin("MUNICIPIO_FILIAL.id_municipio_filial", "OPERACAO_SERVICO_LOCALIZA.id_municipio_filial");		
        sql.addJoin("PESSOA_FILIAL.id_pessoa", "MUNICIPIO_FILIAL.id_filial");
        
        sql.addCriteria("CONTROLE_CARGA.ID_CONTROLE_CARGA", "=", parameters[0]);
        
        sql.addCriteria("PESSOA_FILIAL.id_pessoa", "=", SessionUtils.getFilialSessao().getIdFilial());
        sql.addCustomCriteria("MUNICIPIO_FILIAL.bl_padrao_mcd = 'S' ");
        sql.addCustomCriteria("OPERACAO_SERVICO_LOCALIZA.tp_operacao IN ('A','E')");
        
        sql.addCustomCriteria("ROTA_INTERVALO_CEP.dt_vigencia_final >= sysdate");
        sql.addCustomCriteria("MUNICIPIO_FILIAL.dt_vigencia_final >= sysdate");
        sql.addCustomCriteria("OPERACAO_SERVICO_LOCALIZA.dt_vigencia_final >= sysdate");
                
        sql.addCustomCriteria("ROWNUM = 1");  
     	 
        return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
    }

    public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}      

}
