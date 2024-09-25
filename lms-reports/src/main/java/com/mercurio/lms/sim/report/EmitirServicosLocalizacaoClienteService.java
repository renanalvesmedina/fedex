package com.mercurio.lms.sim.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 * 
 *  
 */
public class EmitirServicosLocalizacaoClienteService extends ReportServiceSupport {
	
	private ConversaoMoedaService conversaoMoedaService;
	private DomainService domainService;
	 
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap) parameters;	
		SqlTemplate sql = montaSqlTemplate(map);
		
		montaFilterSummary(map, sql);
	    
		final List dados = new LinkedList();
		
		boolean isRemetente = false;
		if (StringUtils.isNotBlank(map.getString("remetente.idCliente"))){
			isRemetente = true;
		}
		
		final String tpCabecalho = isRemetente ? "R" : "D";

		final Long idMoeda = map.getLong("moedaPais.moeda.idMoeda");
		final Long idPais = map.getLong("moedaPais.pais.idPais");
		
		getJdbcTemplate().query(sql.getSql(true), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()), new ResultSetExtractor() {
			
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {				
				
				Domain dominio = domainService.findByName("DM_TIPO_DOCUMENTO_SERVICO");
			 	
				while (rs.next()) {
					Map linha = new HashMap();
					
					linha.put("idDoctoServico", Long.valueOf(rs.getLong("ID_DOCTO_SERVICO")));
					linha.put("tpDocumentoServico", dominio.findDomainValueByValue(rs.getString("TP_DOCUMENTO_SERVICO")).getDescription().getValue());					
					linha.put("nrDoctoServico", FormatUtils.formatSgFilialWithLong(rs.getString("SG_FILIAL"), Long.valueOf(rs.getLong("NR_DOCTO_SERVICO"))));
					
					if (rs.getLong("NR_NOTA_FISCAL") > 0)
						linha.put("nrNotaFiscal", Long.valueOf(rs.getLong("NR_NOTA_FISCAL")));					
					
					linha.put("qtVolumes", Integer.valueOf(rs.getInt("QT_VOLUMES")));	
					
					if (rs.getDate("DT_PREV_ENTREGA") != null)					
						linha.put("dtPrevEntrega", JTFormatUtils.format(rs.getDate("DT_PREV_ENTREGA")));
					
					//Determina se mostra remetente ou destinatario, dependendo do que foi informado na tela 
					if (tpCabecalho.equals("R")) {
						linha.put("nrIdentificacao", FormatUtils.formatIdentificacao(rs.getString("TP_IDENTIFICACAO_DESTINATARIO"), rs.getString("NR_IDENTIFICACAO_DESTINATARIO")));
						linha.put("tpIdentificacao", rs.getString("TP_IDENTIFICACAO_DESTINATARIO"));							
						linha.put("nmPessoa", rs.getString("NM_PESSOA_DESTINATARIO"));
					} else {																	
						linha.put("nrIdentificacao", FormatUtils.formatIdentificacao(rs.getString("TP_IDENTIFICACAO_REMETENTE"), rs.getString("NR_IDENTIFICACAO_REMETENTE")));
						linha.put("tpIdentificacao", rs.getString("TP_IDENTIFICACAO_REMETENTE"));	
						linha.put("nmPessoa", rs.getString("NM_PESSOA_REMETENTE"));
					}					
										
					//Encontra rota
					linha.put("dsFluxoFilial", determinaVia(rs.getString("DS_FLUXO_FILIAL")));
										 					
					//Calcula qual maior peso					
					linha.put("peso", determinaPeso(rs.getBigDecimal("PS_REAL"), rs.getBigDecimal("PS_AFORADO")));
					
					//Calcula o valor da mercadoria		
					linha.put("vlMercadoria", calculaValorMercadoria(rs.getBigDecimal("VL_MERCADORIA"), 
																	 rs.getLong("ID_MOEDA"), 
																	 rs.getLong("ID_PAIS"),
																	 idMoeda,
																	 idPais,
																	 new YearMonthDay(rs.getDate("DH_EMISSAO"))));	
					
					linha.put("dsSimbolo", rs.getString("DS_SIMBOLO"));
					
					
					linha.put("sgFilialDestino", rs.getString("sgFilialDestino"));
					
					dados.add(linha);
				}
				return null;
			}}
		);
				
		JRMapCollectionDataSource ds = new JRMapCollectionDataSource(dados);
				
		Map parametersReport = new HashMap();	
		
		if ("C".equals(map.getString("tpRelatorio")))
		    parametersReport.put("data", criaDataCarta());
		
		Long idContato = map.getLong("contato.idContato");
		if (idContato == null) {
			idContato = Long.valueOf(0);
		}
		parametersReport.put("idContato", idContato);
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("tpCabecalho", tpCabecalho);
		parametersReport.put("idContato", idContato);
		parametersReport.put("PAR_SIMBOLO_MOEDA",map.getString("moedaPais.moeda.dsSimbolo"));		
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
		return createReportDataObject(ds, parametersReport);
	}
	
	private void montaFilterSummary(TypedFlatMap map, SqlTemplate sql) {
		if (map.getLong("remetente.idCliente") != null)
			sql.addFilterSummary("remetente", map.getString("remetente.pessoa.nrIdentificacao") + " - " + map.getString("remetente.pessoa.nmPessoa"));

		if (map.getLong("destinatario.idCliente") != null)
			sql.addFilterSummary("destinatario", map.getString("destinatario.pessoa.nrIdentificacao") + " - " + map.getString("destinatario.pessoa.nmPessoa"));

		if (map.getLong("localizacaoMercadoria.idLocalizacaoMercadoria") != null)
			sql.addFilterSummary("localizacaoMercadoria", map.getString("localizacaoMercadoria.cdLocalizacaoMercadoria") + " - " + map.getString("localizacaoMercadoria.dsLocalizacaoMercadoria"));

		sql.addFilterSummary("periodoEmissaoInicial", map.getYearMonthDay("dhEmissaoInicial"));
		sql.addFilterSummary("periodoEmissaoFinal", map.getYearMonthDay("dhEmissaoFinal"));
		
		sql.addFilterSummary("converterParaMoeda",map.getString("moedaPais.moeda.siglaSimbolo"));		
	}

	private String criaDataCarta() {
		Filial filial = SessionUtils.getFilialSessao();
		Municipio municipio = filial.getPessoa().getEnderecoPessoa().getMunicipio();
		
		DateTimeFormatter formato = DateTimeFormat.longDate().withLocale(LocaleContextHolder.getLocale());
		String dataTexto = formato.print(JTDateTimeUtils.getDataHoraAtual());
		
		StringBuffer data = new StringBuffer();
		data.append(municipio.getNmMunicipio())
			.append(", ")
			.append(dataTexto)
			.append(".");	
		
		return data.toString();
	}
	
	private String determinaVia(String dsFluxo){		
		String retorno = null;
					
		if (dsFluxo != null){	
			int firstIndex = dsFluxo.indexOf("-");
			int lastIndex = dsFluxo.lastIndexOf("-");
			
			if (firstIndex > -1 && firstIndex != lastIndex){
				retorno = dsFluxo.substring(firstIndex+2, lastIndex-1);
			}
		}
		
		return retorno;
	}
	
	
	private BigDecimal determinaPeso(BigDecimal psReal, BigDecimal psAforado){
		BigDecimal psRealAux = BigDecimal.ZERO;					
		BigDecimal psAforadoAux = BigDecimal.ZERO;
		BigDecimal peso = null;
		
		if (psReal != null) 
			psRealAux = psReal;
		
		if (psAforado != null)
			psAforadoAux = psAforado;
		
		if (psRealAux.compareTo(psAforadoAux) > 0){
			peso = psRealAux;
		} else{
			peso = psAforadoAux;
		}
		
		return peso;
	}
	
	
	private String calculaValorMercadoria(BigDecimal vlMercadoria, Long idMoedaOrigem, Long idPaisOrigem, 
			Long idMoedaDestino, Long idPaisDestino, YearMonthDay dhEmissao){
		if (vlMercadoria != null){
			vlMercadoria = conversaoMoedaService.findConversaoMoeda(idPaisOrigem, 
																		idMoedaOrigem, 
																		idPaisDestino, 
																		idMoedaDestino, 
																		dhEmissao, 
																		vlMercadoria); 
		}
		
		return FormatUtils.formatDecimal("#,##0.00", vlMercadoria, true);
	}
	
	private SqlTemplate montaSqlTemplate(TypedFlatMap parametros){
		SqlTemplate sqlTemplate = createSqlTemplate();
		
		sqlTemplate.addProjection("DS.ID_DOCTO_SERVICO");
		sqlTemplate.addProjection("DS.TP_DOCUMENTO_SERVICO");
		sqlTemplate.addProjection("FIL_DS.SG_FILIAL");
		sqlTemplate.addProjection("DS.NR_DOCTO_SERVICO");
		sqlTemplate.addProjection("NFC.NR_NOTA_FISCAL");
		sqlTemplate.addProjection("FF.DS_FLUXO_FILIAL");
		sqlTemplate.addProjection("PES_REMET.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_REMETENTE");
		sqlTemplate.addProjection("PES_REMET.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_REMETENTE");
		sqlTemplate.addProjection("PES_REMET.NM_PESSOA", "NM_PESSOA_REMETENTE");
		sqlTemplate.addProjection("PES_DEST.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_DESTINATARIO");
		sqlTemplate.addProjection("PES_DEST.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_DESTINATARIO");
		sqlTemplate.addProjection("PES_DEST.NM_PESSOA", "NM_PESSOA_DESTINATARIO");
		sqlTemplate.addProjection("DS.DT_PREV_ENTREGA");
		sqlTemplate.addProjection("DS.QT_VOLUMES");
		sqlTemplate.addProjection("DS.PS_REAL");
		sqlTemplate.addProjection("DS.PS_AFORADO");
		sqlTemplate.addProjection("DS.VL_MERCADORIA");
	    sqlTemplate.addProjection("DS.ID_MOEDA");
	    sqlTemplate.addProjection("DS.ID_PAIS");
	    sqlTemplate.addProjection("DS.DH_EMISSAO");
	    sqlTemplate.addProjection("M.DS_SIMBOLO");
	    //Sigla da filial de destino
	    sqlTemplate.addProjection("FD.SG_FILIAL", "sgFilialDestino");
	    
	    sqlTemplate.addFrom("DOCTO_SERVICO", "DS");
		sqlTemplate.addFrom("FILIAL", "FIL_DS");
		//Filial de destino
		sqlTemplate.addFrom("FILIAL", "FD");
		sqlTemplate.addFrom("NOTA_FISCAL_CONHECIMENTO", "NFC");
		sqlTemplate.addFrom("FLUXO_FILIAL", "FF");          
		sqlTemplate.addFrom("PESSOA", "PES_REMET");
		sqlTemplate.addFrom("PESSOA", "PES_DEST");
		sqlTemplate.addFrom("MOEDA", "M");
		sqlTemplate.addFrom("LOCALIZACAO_MERCADORIA", "LM");
		
		sqlTemplate.addJoin("FIL_DS.ID_FILIAL", "DS.ID_FILIAL_ORIGEM");
		//Filial de destino
		sqlTemplate.addJoin("FD.ID_FILIAL", "DS.ID_FILIAL_DESTINO");
		sqlTemplate.addJoin("DS.ID_DOCTO_SERVICO", "NFC.ID_CONHECIMENTO (+)");
		sqlTemplate.addJoin("DS.ID_FLUXO_FILIAL", "FF.ID_FLUXO_FILIAL");
		sqlTemplate.addJoin("DS.ID_CLIENTE_REMETENTE", "PES_REMET.ID_PESSOA");
		sqlTemplate.addJoin("DS.ID_CLIENTE_DESTINATARIO", "PES_DEST.ID_PESSOA");
		sqlTemplate.addJoin("DS.ID_MOEDA", "M.ID_MOEDA");
		sqlTemplate.addJoin("LM.ID_LOCALIZACAO_MERCADORIA", "DS.ID_LOCALIZACAO_MERCADORIA");

		sqlTemplate.addCriteria("DS.ID_CLIENTE_REMETENTE", "=", parametros.getLong("remetente.idCliente"));
		sqlTemplate.addCriteria("DS.ID_CLIENTE_DESTINATARIO", "=", parametros.getLong("destinatario.idCliente"));
		sqlTemplate.addCriteria("TRUNC(CAST(DS.DH_EMISSAO AS DATE))", ">=", parametros.getYearMonthDay("dhEmissaoInicial"));
		sqlTemplate.addCriteria("TRUNC(CAST(DS.DH_EMISSAO AS DATE))", "<=", parametros.getYearMonthDay("dhEmissaoFinal"));
		sqlTemplate.addCriteria("DS.ID_LOCALIZACAO_MERCADORIA", "=", parametros.getLong("localizacaoMercadoria.idLocalizacaoMercadoria"));
		sqlTemplate.addCriteria("LM.cd_LOCALIZACAO_MERCADORIA", "<>", ConstantesSim.CD_MERCADORIA_CANCELADA);
		
		sqlTemplate.addOrderBy("DS.TP_DOCUMENTO_SERVICO");
		sqlTemplate.addOrderBy("FIL_DS.SG_FILIAL");
		sqlTemplate.addOrderBy("DS.NR_DOCTO_SERVICO");
		
		return sqlTemplate;
	}


	/**
	 * @param conversaoMoedaService The conversaoMoedaService to set.
	 */
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	/**
	 * @param domainService The domainService to set.
	 */
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	
	
	
}
