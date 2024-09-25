package com.mercurio.lms.sim.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.UncategorizedSQLException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 * 
 */
public class EmitirDocumentosServicoEmitidosMunicipioService extends
		ReportServiceSupport {

	private ConversaoMoedaService conversaoMoedaService;
	private InscricaoEstadualService inscricaoEstadualService;
	private EnderecoPessoaService enderecoPessoaService;
	
	public EmitirDocumentosServicoEmitidosMunicipioService() {
		super();
	}

	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap) parameters;
		SqlTemplate sql = montaSql(map);
		
		montaFilterSummary(map, sql);
		
		Map parametersReport = new HashMap();		
		parametersReport.put("ID_MOEDA", map.getLong("moedaPais.moeda.idMoeda"));
		parametersReport.put("DS_SIMBOLO", map.getString("moedaPais.moeda.dsSimbolo"));
		parametersReport.put("ID_PAIS", map.getLong("moedaPais.pais.idPais"));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());		
		parametersReport.put("identificacao_cliente", map.getString("cliente.pessoa.nrIdentificacao") + " - " + map.getString("cliente.pessoa.nmPessoa"));
		Long idCliente = map.getLong("cliente.idCliente");
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idCliente);
		parametersReport.put("identificacao_cli_municipio", enderecoPessoa.getMunicipio().getNmMunicipio());
		parametersReport.put("identificacao_cli_uf", enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
				
		InscricaoEstadual ie = inscricaoEstadualService.findByPessoaIndicadorPadrao(map.getLong("cliente.idCliente"), true);
		if (ie != null)
			parametersReport.put("nr_ie", ie.getNrInscricaoEstadual());
		
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,
					parameters.get("tpFormatoRelatorio"));
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		JRReportDataObject jr = null;
		try{
		 jr = executeQuery(sql.getSql(), sql.getCriteria());
		 // definindo os parametros do relatorio
		 jr.setParameters(parametersReport);
		}catch(UncategorizedSQLException e){
			throw new InfrastructureException(e.getCause());
		}
        

		
		return jr;
	
	}
	
	private void montaFilterSummary(TypedFlatMap map, SqlTemplate sql) {
		
		sql.addFilterSummary("cliente", map.getString("cliente.pessoa.nrIdentificacao") + " - " + map.getString("cliente.pessoa.nmPessoa"));
		sql.addFilterSummary("tipoRelatorio", map.getString("dsTipoRelatorio"));
		
		String tpOpcao = map.getString("tpOpcao");
		if (StringUtils.isNotBlank(tpOpcao)) {
			if (tpOpcao.equals("M") && map.getLong("municipio.idMunicipio") != null) {
				sql.addFilterSummary("municipio", map.getString("municipio.nmMunicipio"));
			}	
			else if (map.getLong("unidadeFederativa.idUnidadeFederativa") != null) {
				sql.addFilterSummary("uf", map.getString("unidadeFederativa.sgUnidadeFederativa")
						.concat(" - ").concat(map.getString("unidadeFederativa.nmUnidadeFederativa")));
			}
		}
		
		
		sql.addFilterSummary("converterParaMoeda", map.getString("moedaPais.moeda.siglaSimbolo"));
		sql.addFilterSummary("tipoDocumentoServico", map.getString("dsTipoDocumentoServico"));		
		sql.addFilterSummary("periodoEmissaoInicial", map.getYearMonthDay("dtEmissaoInicial"));
		sql.addFilterSummary("periodoEmissaoFinal", map.getYearMonthDay("dtEmissaoFinal"));
		
	}

	private SqlTemplate montaSql(TypedFlatMap map){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("m.nm_municipio");
		sql.addProjection("m.id_municipio");
		sql.addProjection("uf.id_unidade_federativa");
		sql.addProjection("uf.sg_unidade_federativa");
		sql.addProjection("uf.nm_unidade_federativa");
		sql.addProjection("ds.id_docto_servico");
		sql.addProjection("ds.qt_volumes");
		sql.addProjection("ds.ps_real");
		sql.addProjection("ds.ps_aforado");
		sql.addProjection("ds.ps_referencia_calculo");
		sql.addProjection("ds.vl_mercadoria");
		sql.addProjection("ds.vl_total_parcelas");
		sql.addProjection("ds.vl_imposto");
		sql.addProjection("ds.id_moeda");
		sql.addProjection("ds.id_pais");
		sql.addProjection("ds.dh_emissao");	
		
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("municipio", "m");
		sql.addFrom("unidade_federativa", "uf");	
		
		sql.addFrom("conhecimento", "c");
		sql.addJoin("ds.id_docto_servico", "c.id_conhecimento");
		// Se é origem:
		if (map.getString("tpRelatorio").equals("O")){
			sql.addJoin("c.id_municipio_coleta", "m.id_municipio");
		// Senão é destino:
		} else {		
			sql.addJoin("c.id_municipio_entrega", "m.id_municipio");
		}
			
		sql.addJoin("m.id_unidade_federativa", "uf.id_unidade_federativa");	
		sql.addFrom("localizacao_mercadoria", "lm");
		sql.addJoin("ds.id_localizacao_mercadoria", "lm.id_localizacao_mercadoria"); 
		
		//Bug CQPRO00026842
		//Testa se o tipo de relatório é Destino ou Origem 
		if ( map.getString("tpRelatorio")!= null && map.getString("tpRelatorio").equals("D")) 
				sql.addCriteria("ds.id_cliente_destinatario", "=", map.getLong("cliente.idCliente"));		
		else
			sql.addCriteria("ds.id_cliente_remetente", "=", map.getLong("cliente.idCliente"));

		sql.addCriteria("ds.tp_documento_servico", "=", map.getString("tpDocumentoServico"));
		sql.addCriteria("trunc(cast(ds.dh_emissao as DATE))", ">=", map.getYearMonthDay("dtEmissaoInicial"));
		sql.addCriteria("trunc(cast(ds.dh_emissao as DATE))", "<=", map.getYearMonthDay("dtEmissaoFinal"));
		
		//Não retornar documentos cancelados 
		sql.addCriteria("lm.cd_localizacao_mercadoria", "<>", 25);
		
		String tpOpcao = map.getString("tpOpcao");
		if (StringUtils.isNotBlank(tpOpcao)) {
			if (tpOpcao.equals("M")) {
				sql.addCriteria("m.id_municipio", "=", map.getLong("municipio.idMunicipio"));
			}	
			else {
				sql.addCriteria("uf.id_unidade_federativa", "=", map.getLong("unidadeFederativa.idUnidadeFederativa"));
			}
		}
		
		sql.addOrderBy("uf.sg_unidade_federativa"); 
		sql.addOrderBy("uf.id_unidade_federativa");
		sql.addOrderBy("m.nm_municipio");
		sql.addOrderBy("m.id_municipio");
		
		return sql;
	}
	
	public BigDecimal converteMoeda(Object[] parameters) {
		String strDhEmissao = (String)parameters[5];
		BigDecimal vlMoeda = (BigDecimal)parameters[4];
		
		if (StringUtils.isBlank(strDhEmissao) || vlMoeda == null)
			return BigDecimal.ZERO;
		
		YearMonthDay dhEmissao = JTFormatUtils.buildDateTimeFromTimestampTzString(strDhEmissao).toYearMonthDay();
		BigDecimal retorno = null;
		try{
		retorno = conversaoMoedaService.findConversaoMoeda((Long)parameters[0],(Long)parameters[1],
				(Long)parameters[2],(Long)parameters[3],dhEmissao,vlMoeda); 
		}catch(Exception e){
			throw new InfrastructureException(e.getCause());
		}
		return retorno;
	}


	/**
	 * @param conversaoMoedaService The conversaoMoedaService to set.
	 */
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}


	/**
	 * @param inscricaoEstadualService The inscricaoEstadualService to set.
	 */
	public void setInscricaoEstadualService(
			InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}


	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
}
