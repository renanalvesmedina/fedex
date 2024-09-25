package com.mercurio.lms.entrega.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Felipe Ferreira
 * 
 * @spring.bean id="lms.entrega.emitirEficienciaEntregaClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirEficienciaEntregaCliente.jasper"
 */
public class EmitirEficienciaEntregaClienteService extends ReportServiceSupport {
	
	private ConversaoMoedaService conversaoMoedaService;
	private PessoaService pessoaService;
	private EnderecoPessoaService enderecoPessoaService;
	private ConfiguracoesFacade configuracoesFacade;
	private TelefoneEnderecoService telefoneEnderecoService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters; 
		
        SqlTemplate sql = getSqlTemplate(tfm);	    
	    JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        Map parametersReport = new HashMap(); 
 
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("municipioEmissor",
        		SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio());
        parametersReport.put("infoCliente",this.getInfoCliente(tfm.getLong("cliente.idCliente"),tfm.getString("contato.nmContato")));
        
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        Moeda moeda = SessionUtils.getMoedaSessao();
		parametersReport.put("ID_MOEDA",moeda.getIdMoeda());
        parametersReport.put("ID_PAIS",SessionUtils.getPaisSessao().getIdPais());
        parametersReport.put("DS_SIMBOLO",moeda.getDsSimbolo());
        
        parametersReport.put("dtEventoInicial", JTFormatUtils.format(tfm.getYearMonthDay("dtEventoInicial")));
        parametersReport.put("dtEventoFinal", JTFormatUtils.format(tfm.getYearMonthDay("dtEventoFinal")));
        
        // parâmetros usados na assinatura.
        parametersReport.putAll(getInfoAssinatura());
        
        jr.setParameters(parametersReport);
        
        return jr;
	}
	
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) {	
		SqlTemplate mainSql = createSqlTemplate();
		
		mainSql.addProjection("DS.ID_DOCTO_SERVICO");
		mainSql.addProjection("DS.TP_DOCUMENTO_SERVICO");
		mainSql.addProjection("DS.NR_DOCTO_SERVICO");
		mainSql.addProjection("FO.SG_FILIAL","SG_FILIAL_ORIGEM");
		mainSql.addProjection("(" + getSqlNotaFiscal() + ")","NR_NOTA_FISCAL");
		mainSql.addProjection("FD.SG_FILIAL","SG_FILIAL_DESTINO");
		mainSql.addProjection("PD.NM_FANTASIA","NM_FILIAL_DESTINO");
		mainSql.addProjection("DDSF.VL_DEVIDO");
		mainSql.addProjection("DS.DH_EMISSAO");
		mainSql.addProjection("EDS.DH_EVENTO");
		mainSql.addProjection("DS.DT_PREV_ENTREGA");
		mainSql.addProjection("DS.NR_DIAS_PREV_ENTREGA","PRV");
		mainSql.addProjection("DS.NR_DIAS_REAL_ENTREGA","RLZ");
		mainSql.addProjection("DS.NR_DIAS_BLOQUEIO","BLQ");
		mainSql.addProjection("Nvl(DS.NR_DIAS_PREV_ENTREGA,0) " +
				"- Nvl(DS.NR_DIAS_REAL_ENTREGA,0) ","ATR");
		mainSql.addProjection("M_DS.NM_MUNICIPIO","NM_MUNICIPIO_DESTINO");
		mainSql.addProjection("DS.PS_REFERENCIA_CALCULO","PESO_MAIOR");
		mainSql.addProjection("DS.VL_MERCADORIA");
		mainSql.addProjection("DS.QT_VOLUMES");
		
		mainSql.addProjection("CASE " +
				"WHEN DDSF.TP_SITUACAO_COBRANCA <> ? THEN 1 " +
				"ELSE 0 " +
				"END","PENDENTE");
		mainSql.addCriteriaValue("L");
		
		mainSql.addProjection("CASE " +
				"WHEN DDSF.TP_SITUACAO_COBRANCA = ? THEN 1 " +
				"ELSE 0 " +
				"END","COBRADO");
		mainSql.addCriteriaValue("L");
		
		mainSql.addProjection("DDSF.DT_LIQUIDACAO");
		mainSql.addProjection("DS.ID_MOEDA");
		mainSql.addProjection("DS.ID_PAIS");
		 
		mainSql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO","MED");
		mainSql.addFrom("MANIFESTO_ENTREGA","ME");
		mainSql.addFrom("DOCTO_SERVICO","DS");
		mainSql.addFrom("EVENTO_DOCUMENTO_SERVICO","EDS");
		mainSql.addFrom("EVENTO","EV");
		mainSql.addFrom("MANIFESTO","M");
		mainSql.addFrom("DEVEDOR_DOC_SERV_FAT","DDSF");
		mainSql.addFrom("FILIAL","FO");
		mainSql.addFrom("FILIAL","FD");
		mainSql.addFrom("PESSOA","PD");
		mainSql.addFrom("CONHECIMENTO","C");
		mainSql.addFrom("ENDERECO_PESSOA","END_DS");
		mainSql.addFrom("MUNICIPIO","M_DS");
		
		mainSql.addJoin("ME.ID_MANIFESTO_ENTREGA","MED.ID_MANIFESTO_ENTREGA");
		mainSql.addJoin("DS.ID_DOCTO_SERVICO","MED.ID_DOCTO_SERVICO");
		mainSql.addJoin("M.ID_MANIFESTO","ME.ID_MANIFESTO_ENTREGA");
		mainSql.addJoin("EDS.ID_DOCTO_SERVICO","DS.ID_DOCTO_SERVICO");
		mainSql.addJoin("EV.ID_EVENTO","EDS.ID_EVENTO");
		mainSql.addCriteria("EV.CD_EVENTO","=",Short.valueOf((short)21));
		mainSql.addCriteria("EDS.BL_EVENTO_CANCELADO","=","N");
		mainSql.addJoin("DDSF.ID_DOCTO_SERVICO","DS.ID_DOCTO_SERVICO");
		mainSql.addCriteria("M.TP_STATUS_MANIFESTO","<>","CA");
		mainSql.addJoin("FO.ID_FILIAL","DS.ID_FILIAL_ORIGEM");
		mainSql.addJoin("FD.ID_FILIAL (+)","DS.ID_FILIAL_DESTINO");
		mainSql.addJoin("PD.ID_PESSOA (+)","FD.ID_FILIAL");
		mainSql.addJoin("C.ID_CONHECIMENTO (+)","DS.ID_DOCTO_SERVICO");
		mainSql.addJoin("END_DS.ID_ENDERECO_PESSOA (+)","DS.ID_ENDERECO_ENTREGA");
		mainSql.addJoin("M_DS.ID_MUNICIPIO (+)","END_DS.ID_MUNICIPIO");
		
		
		String tpCliente = parameters.getString("tpCliente");
		// Caso remetente:
		if (tpCliente.equals("R")) {
			mainSql.addCriteria("DS.ID_CLIENTE_REMETENTE","=",parameters.getLong("cliente.idCliente"));
		// Caso destinatário:
		} else if (tpCliente.equals("D")) {
			mainSql.addCriteria("DS.ID_CLIENTE_DESTINATARIO","=",parameters.getLong("cliente.idCliente"));
		// Caso responsável pelo frete:
		} else {
			mainSql.addCriteria("DDSF.ID_CLIENTE","=",parameters.getLong("cliente.idCliente"));
		}
		
		Long idUnidadeFederativa = parameters.getLong("unidadeFederativa.idUnidadeFederativa");
		if (idUnidadeFederativa != null) {
			mainSql.addCustomCriteria("EXISTS (" + this.getSqlUnidadeFederativaFilialDestino(idUnidadeFederativa,mainSql) + ")");
		}
		
		mainSql.addCriteria("FD.ID_FILIAL","=",parameters.getLong("filial.idFilial"));
		
		mainSql.addCriteria("Trunc(EDS.DH_EVENTO)",">=",parameters.getYearMonthDay("dtEventoInicial"));
		mainSql.addCriteria("Trunc(EDS.DH_EVENTO)","<=",parameters.getYearMonthDay("dtEventoFinal"));
		
		mainSql.addOrderBy("DS.TP_DOCUMENTO_SERVICO");
		mainSql.addOrderBy("FD.ID_FILIAL");
		mainSql.addOrderBy("DS.NR_DOCTO_SERVICO");
		
		return mainSql;
	}
		
	private String getSqlNotaFiscal() {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("Min(NFC.NR_NOTA_FISCAL)");
		sql.addFrom("NOTA_FISCAL_CONHECIMENTO","NFC");
	    sql.addJoin("NFC.ID_CONHECIMENTO","DS.ID_DOCTO_SERVICO");
	    
	    return sql.getSql();
	}
	
	private String getSqlUnidadeFederativaFilialDestino(Long idUnidadeFederativa, SqlTemplate mainSql) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("*");
		sql.addFrom("ENDERECO_PESSOA","EP_FD");
		sql.addFrom("MUNICIPIO","M_FD");
		sql.addJoin("EP_FD.ID_PESSOA","FD.ID_FILIAL");
		sql.addJoin("M_FD.ID_MUNICIPIO","EP_FD.ID_MUNICIPIO");
		sql.addCustomCriteria("M_FD.ID_UNIDADE_FEDERATIVA = ? ");
		mainSql.addCriteriaValue(idUnidadeFederativa);
		
		return sql.getSql();
	}
	
	/**
	 * Método para criar mensagem ao Destinatário da relatório.
	 * @param idCliente
	 * @param nmContato
	 * @return
	 */
	private String getInfoCliente(Long idCliente, String nmContato) {
		Pessoa pessoa = pessoaService.findById(idCliente);
		StringBuffer info = new StringBuffer();
		
		info.append(pessoa.getNmPessoa()).append(" - ");
		DomainValue tpIdentificacao = pessoa.getTpIdentificacao();
		if (tpIdentificacao != null) {
			info.append(FormatUtils.formatIdentificacao(tpIdentificacao.getValue(),pessoa.getNrIdentificacao()));
		} else {
			info.append(pessoa.getNrIdentificacao());
		}
		
		EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa();
		if (enderecoPessoa == null) {
			enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(pessoa.getIdPessoa());
		}
		
		if (enderecoPessoa != null) {
			info.append("\n");
			
			info.append(enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro());
			info.append(" ").append(enderecoPessoa.getDsEndereco());
			info.append(", ").append(enderecoPessoa.getNrEndereco());

			String dsComplemento = enderecoPessoa.getDsComplemento();
			if (StringUtils.isNotBlank(dsComplemento)) {
				info.append(" - ").append(dsComplemento);
			}

			String dsBairro = enderecoPessoa.getDsBairro();
			if (StringUtils.isNotBlank(dsBairro)) {
				info.append(", ").append(dsBairro);
			}

			info.append("\n");
			
			Municipio municipio = enderecoPessoa.getMunicipio();
			UnidadeFederativa uf = municipio.getUnidadeFederativa();
			Pais pais = uf.getPais();
			
			String nrCep = enderecoPessoa.getNrCep();
			if (StringUtils.isNotBlank(nrCep)) {
				info.append(FormatUtils.formatCep(pais.getSgPais(),nrCep)).append(" - ");
			}			
			info.append(municipio.getNmMunicipio()).append(" - ").append(uf.getSgUnidadeFederativa());
		}
		
		if (StringUtils.isNotBlank(nmContato)) {
			info.append("\n");
			info.append(configuracoesFacade.getMensagem("aosCuidados")).append(".: ").append(nmContato);
		}
		
		return info.toString();
	}
	
	/**
	 * Método responsável pela conversão de valores de acordo com as moedas informadas.
	 * @param idPaisOrigem
	 * @param idMoedaOrigem
	 * @param idPaisDestino
	 * @param idMoedaDestino
	 * @param valor
	 * @return
	 */
	public BigDecimal converteValorMoeda(Long idPaisOrigem, Long idMoedaOrigem,
			Long idPaisDestino, Long idMoedaDestino, BigDecimal valor) {
		if (valor == null)
			return BigDecimal.valueOf(0);
		return conversaoMoedaService.findConversaoMoeda(idPaisOrigem,idMoedaOrigem,
				idPaisDestino,idMoedaDestino,JTDateTimeUtils.getDataAtual(),valor);
	}
	
	/**
	 * Método responsável por calcular a média de dias de pendentes
	 * @param dhEmissao
	 * @param documentosNaoLiquidados
	 * @return
	 */
	public Double getMediaDiasPendentes(String dhEmissao) {
		if (dhEmissao == null)
			return Double.valueOf(0);
		
		return Double.valueOf(calculaDiferencaEntreDatasEmDias(new Date(),dhEmissao));
	}
	
	/**
	 * Método responsável por calcular a média de dias de cobrados
	 * @param dtLiquidacao
	 * @param dhEmissao
	 * @param documentosLiquidados
	 * @return
	 */
	public Double getMediaDiasCobrados(Date dtLiquidacao, String dhEmissao) {
		if (dtLiquidacao == null || StringUtils.isBlank(dhEmissao))
			return Double.valueOf(0);

		return Double.valueOf(calculaDiferencaEntreDatasEmDias(dtLiquidacao,dhEmissao));
	}
	
	private double calculaDiferencaEntreDatasEmDias(Date dtDate1, String dtString2) {
		DateTime a = JTFormatUtils.buildYmd(dtDate1).toDateTimeAtMidnight(); 
		DateTime b = JTFormatUtils.buildDateTimeFromTimestampTzString(dtString2).withTime(0,0,0,0);
		
		long dias = 0;
		Duration d = new Duration(b.getMillis(),a.getMillis());
		if (a != null && b != null) {
			long fator = 1000 * 60 * 60 * 24;
			// divisao para gerar dias
			dias = d.getMillis() / fator;
		}

		return dias;
	}

	/**
	 * Parâmetros utilizados na assinatura.
	 * @return
	 */
	private Map getInfoAssinatura() {
		Empresa e = SessionUtils.getEmpresaSessao();
		Filial f = SessionUtils.getFilialSessao();
		
		Map params = new HashMap();
		params.put("EMPRESA_SESSAO",e.getPessoa().getNmPessoa());
		params.put("FILIAL_SESSAO",f.getSgFilial());
		params.put("FANTASIA_SESSAO",f.getPessoa().getNmFantasia());
		
		EnderecoPessoa ep = enderecoPessoaService.findEnderecoPessoaPadrao(e.getIdEmpresa());		
		if (ep != null) {
			TelefoneEndereco fone = telefoneEnderecoService
					.findTelefoneEnderecoPadraoPorTpUso(ep.getIdEnderecoPessoa(),"FO");
			if (fone != null) {
				params.put("FONE_SESSAO",FormatUtils.formatTelefone(
						fone.getNrTelefone(),
						fone.getNrDdd(),
						fone.getNrDdi()));
			}
			
			TelefoneEndereco fax = telefoneEnderecoService
					.findTelefoneEnderecoPadraoPorTpUso(ep.getIdEnderecoPessoa(),"FA");
			if (fax != null) {
				params.put("FAX_SESSAO",FormatUtils.formatTelefone(
						fax.getNrTelefone(),
						fax.getNrDdd(),
						fax.getNrDdi()));
			}
		}
		return params;
	}
	
	public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
    }
	
	
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}	
}