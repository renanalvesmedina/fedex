package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 *
 * @spring.bean id="lms.contasreceber.emitirRelacaoContaFreteService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirRelacaoContaFrete.jasper"
 */
public class EmitirRelacaoContaFreteService extends ReportServiceSupport {
    
	private DomainService domainService;
        
	public JRReportDataObject execute(Map criteria) throws Exception {
		
		TypedFlatMap map = (TypedFlatMap)criteria;
		
		
		SqlTemplate sql = mountSqlCTRC(map);		
		String strSql = sql.getSql();
		Object[] criterios = new Object[sql.getCriteria().length * 4];
		System.arraycopy(sql.getCriteria(), 0, criterios, 0, sql.getCriteria().length);
		
		sql = mountSqlCRT(map);		
		strSql += "\nUNION\n"+sql.getSql();
		System.arraycopy(sql.getCriteria(), 0, criterios, sql.getCriteria().length, sql.getCriteria().length);
		
		sql = mountSqlNFS(map);		
		strSql += "\nUNION\n"+sql.getSql();
		System.arraycopy(sql.getCriteria(), 0, criterios, sql.getCriteria().length * 2, sql.getCriteria().length);		
		
		sql = mountSqlNDN(map);
		sql.addOrderBy("nmPessoaRemDest, sgDocumento, sgFilialOrigem, nrDocumento");
		strSql += "\nUNION\n"+sql.getSql();
		System.arraycopy(sql.getCriteria(), 0, criterios, sql.getCriteria().length * 3, sql.getCriteria().length);				
		
		JRReportDataObject jr = executeQuery(strSql,criterios);
		
		Map parametersReport = new HashMap();
		
		sql.addFilterSummary("moeda",map.getString("moeda.siglaSimbolo"));
		
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,criteria.get("tpFormatoRelatorio"));
		
		
        // Monta a moeda destino
        String dsSimboloMoeda = map.getString("moeda.dsSimbolo");
        YearMonthDay dataCotacao = JTDateTimeUtils.getDataAtual();
        
        parametersReport.put("dataCotacao",dataCotacao);
        parametersReport.put("moedaSelecionada",dsSimboloMoeda);
		
		jr.setParameters(parametersReport);
		
		return jr;
	}
	
	private SqlTemplate mountSqlCTRC(TypedFlatMap parametros){
		SqlTemplate sql = this.mountSqlBasic(parametros);
		sql.addProjection("decode(doc.tp_documento_servico,'CTR', 'CTRC', 'CTE', 'CT-e', doc.tp_documento_servico)",
				"sgDocumento");
		sql.addProjection("docf.nr_conhecimento","nrDocumento");
		
		
		sql.addProjection("(select min(nfc.nr_nota_fiscal) from nota_fiscal_conhecimento nfc where nfc.id_conhecimento = docf.id_conhecimento)","nrNotaFiscal");
		
		sql.addFrom("conhecimento","docf");
		
		sql.addJoin("doc.id_docto_servico","docf.id_conhecimento");
		return sql;
	}

	private SqlTemplate mountSqlCRT(TypedFlatMap parametros){
		SqlTemplate sql = this.mountSqlBasic(parametros);
		
		sql.addProjection("'CRT'","sgDocumento");
		sql.addProjection("docf.nr_crt","nrDocumento");

		
		sql.addProjection("null","nrNotaFiscal");
		
		sql.addFrom("cto_internacional","docf");
		
		sql.addJoin("doc.id_docto_servico","docf.id_cto_internacional");
		return sql;
	}
	
	private SqlTemplate mountSqlNFS(TypedFlatMap parametros){
		SqlTemplate sql = this.mountSqlBasic(parametros);
		
		sql.addProjection("doc.tp_documento_servico","sgDocumento");
		sql.addProjection("docf.nr_nota_fiscal_servico","nrDocumento");
		
		sql.addProjection("null","nrNotaFiscal");
		
		sql.addFrom("nota_fiscal_servico","docf");
		
		sql.addJoin("doc.id_docto_servico","docf.id_nota_fiscal_servico");
		return sql;
	}
	
	private SqlTemplate mountSqlNDN(TypedFlatMap parametros){
		SqlTemplate sql = this.mountSqlBasic(parametros);
		
		sql.addProjection("'NDN'","sgDocumento");
		sql.addProjection("docf.nr_nota_debito_nac","nrDocumento");
		
		sql.addProjection("null","nrNotaFiscal");
		
		sql.addFrom("nota_debito_nacional","docf");
		
		sql.addJoin("doc.id_docto_servico","docf.id_nota_debito_nacional");
		return sql;
	}
	
	private SqlTemplate mountSqlBasic(TypedFlatMap parametros){
		//comprovanteintregaservice
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("fat.id_fatura","idFatura");
		
		sql.addProjection("filfat.sg_filial","sgFilialFat");
		sql.addProjection("fat.nr_fatura","nrFatura");
		
		sql.addProjection("fat.dt_emissao","dtEmissao");
		sql.addProjection("fat.dt_vencimento","dtVencimento");

		// Edenilson - Passado para genérico. Era específico do conhecimento
		sql.addProjection("doc.qt_volumes","volume");
		sql.addProjection("doc.ps_real","peso");		
		
		sql.addProjection("doc.vl_icms_st","vlicmsst");
		
		// Edenilson - se o remetente for o cliente da fatura, mostra o destinatário,
		// se o destinatário for o cliente da fatura, mostra o remetente
		sql.addProjection("decode(pescli.id_pessoa, pesrem.id_pessoa, pesdes.tp_identificacao, pesrem.tp_identificacao)","tpIdentificacaoRemDest");
		sql.addProjection("decode(pescli.id_pessoa, pesrem.id_pessoa, pesdes.nr_Identificacao, pesrem.nr_Identificacao)","nrIdentificacaoRemDest");
		sql.addProjection("decode(pescli.id_pessoa, pesrem.id_pessoa, pesdes.nm_pessoa, pesrem.nm_pessoa)","nmPessoaRemDest");
		sql.addProjection("decode(pescli.id_pessoa, pesrem.id_pessoa, ufdes.sg_unidade_federativa, ufori.sg_unidade_federativa)","sgUfRemDest");

		sql.addProjection("filori.sg_filial","sgFilialOrigem");
		sql.addProjection("trunc(doc.dh_emissao)","dhEmissao");
		
		sql.addProjection("F_CONV_MOEDA(ufcob.id_pais,fat.id_moeda,?,?,?,doc.vl_mercadoria)","vlmercadoria");
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(parametros.getLong("moeda.idMoeda"));
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addProjection("F_CONV_MOEDA(ufcob.id_pais,fat.id_moeda,?,?,?,ddsf.vl_devido)","vldevido");
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(parametros.getLong("moeda.idMoeda"));
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addProjection("F_CONV_MOEDA(ufcob.id_pais,fat.id_moeda,?,?,?,doc.vl_base_calc_imposto)","vlbasecalculoimposto");
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(parametros.getLong("moeda.idMoeda"));
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addProjection("F_CONV_MOEDA(ufcob.id_pais,fat.id_moeda,?,?,?,doc.vl_imposto)","vlimposto");
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(parametros.getLong("moeda.idMoeda"));
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addProjection("doc.id_moeda","idmoeda");
		sql.addProjection("doc.id_pais","idpaisorigem");		

		sql.addProjection("pescli.nr_identificacao","nrIdentificacaoCliente");
		sql.addProjection("pescli.tp_identificacao","tpIdentificacaoCliente");
		sql.addProjection("pescli.nm_pessoa","nmPessoaCliente");
		sql.addProjection("doc.vl_total_doc_servico","vlTotalDocServico");
		
		sql.addFrom("fatura","fat");
		sql.addFrom("pessoa","pescli");
		sql.addFrom("item_fatura","ifat");
		sql.addFrom("docto_servico","doc");
		sql.addFrom("servico","serv");
		sql.addFrom("devedor_doc_serv_fat","ddsf");
		sql.addFrom("pessoa","pesrem");
		sql.addFrom("pessoa","pesdes");
		sql.addFrom("filial","filfat");
		sql.addFrom("filial","filcob");

		// Edenilson - Busca UF da filial origem
		sql.addFrom("pessoa","pescob");
		sql.addFrom("endereco_pessoa","endcob");
		sql.addFrom("municipio","muncob");
		sql.addFrom("unidade_federativa","ufcob");
		
		// Edenilson - Busca UF da filial origem
		sql.addFrom("filial","filori");
		sql.addFrom("pessoa","pesfilor");
		sql.addFrom("endereco_pessoa","endori");
		sql.addFrom("municipio","munori");
		sql.addFrom("unidade_federativa","ufori");
		
		// Edenilson - Busca UF da filial destino
		sql.addFrom("filial","fildes");
		sql.addFrom("pessoa","pesfildes");
		sql.addFrom("endereco_pessoa","enddes");
		sql.addFrom("municipio","mundes");
		sql.addFrom("unidade_federativa","ufdes");
		
		sql.addFrom("centralizadora_faturamento","cenfat");
		
		sql.addJoin("fat.id_fatura","ifat.id_fatura");
		sql.addJoin("pescli.id_pessoa","fat.id_cliente");
		sql.addJoin("filfat.id_filial","fat.id_filial");
		sql.addJoin("ddsf.id_devedor_doc_serv_fat","ifat.id_devedor_doc_serv_fat");
		sql.addJoin("doc.id_docto_servico","ddsf.id_docto_servico");
		sql.addJoin("pesrem.id_pessoa","doc.id_cliente_remetente");
		sql.addJoin("pesdes.id_pessoa","doc.id_cliente_destinatario");
		sql.addJoin("filcob.id_filial","ddsf.id_filial");
		sql.addJoin("filori.id_filial","doc.id_filial_origem");
		sql.addJoin("fildes.id_filial","doc.id_filial_destino");
		
		sql.addJoin("ddsf.id_filial","pescob.id_pessoa");
		sql.addJoin("pescob.id_endereco_pessoa","endcob.id_endereco_pessoa");
		sql.addJoin("endcob.id_municipio","muncob.id_municipio");
		sql.addJoin("muncob.id_unidade_federativa","ufcob.id_unidade_federativa");
		
		sql.addJoin("filori.id_filial","pesfilor.id_pessoa");
		sql.addJoin("pesfilor.id_endereco_pessoa","endori.id_endereco_pessoa");
		sql.addJoin("endori.id_municipio","munori.id_municipio");
		sql.addJoin("munori.id_unidade_federativa","ufori.id_unidade_federativa");
		
		sql.addJoin("fildes.id_filial","pesfildes.id_pessoa");
		sql.addJoin("pesfildes.id_endereco_pessoa","enddes.id_endereco_pessoa");
		sql.addJoin("enddes.id_municipio","mundes.id_municipio");
		sql.addJoin("mundes.id_unidade_federativa","ufdes.id_unidade_federativa");
		
		sql.addJoin("doc.id_servico","serv.id_servico");
		sql.addJoin("ddsf.id_filial","cenfat.id_filial_centralizadora(+)");
		sql.addJoin("serv.tp_modal","nvl(cenfat.tp_modal,serv.tp_modal)");
		sql.addJoin("serv.tp_abrangencia","nvl(cenfat.tp_abrangencia,serv.tp_abrangencia)");
		
		sql.addCriteria("fat.id_fatura","=",parametros.getLong("fatura.idFatura"));
		
		if (parametros.getLong("fatura.idFatura") != null){
			sql.addFilterSummary("fatura",parametros.getString("siglaFilial") + " "  +FormatUtils.completaDados(parametros.getString("fatura.nrFatura"), "0", 10, 0, true));
		}
		
		return sql;
	}


	/**
	 * @return Returns the domainService.
	 */
	public DomainService getDomainService() {
		return domainService;
	}


	/**
	 * @param domainService The domainService to set.
	 */
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}
}