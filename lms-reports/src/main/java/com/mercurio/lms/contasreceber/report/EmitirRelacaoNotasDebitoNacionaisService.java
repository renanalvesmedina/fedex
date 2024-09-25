package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Hector junior
 *
 * @spring.bean id="lms.tributos.emitirRelacaoNotasDebitoNacionaisService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirRelacaoNotasDebitoNacionais.jasper"
 */
public class EmitirRelacaoNotasDebitoNacionaisService extends ReportServiceSupport {

	/** 
	 * Método invocado pela EmitirRelacaoNotasDebitoNacionaisAction, é o método default do Struts
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
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Adiciona o tipo de relatório no Map */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
		jr.setParameters(parametersReport);
		return jr;
	}
	
	/**
	 * Configura variáveis do relatório, para receberem valores não abreviados do domínio 
	 * Ex: situação = I  -  vai ser configurado, e exibido no relatório como Inativo
	 */
	public void configReportDomains(ReportDomainConfig config) {		
		config.configDomainField("ESTADO_COBRANCA","DM_STATUS_COBRANCA_DOCTO_SERVICO");
		super.configReportDomains(config);
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("filddsfcob.id_filial as ID_FILIAL_COB, " +
						  "(filddsfcob.sg_filial || ' - ' || pfilddsfcob.nm_fantasia) as FILIAL_COBRANCA, " +
						  "ndn.id_nota_debito_nacional ID_NDN, " +
						  "ndn.nr_nota_debito_nac as NOTA, " +
						  "filds.sg_filial as FILIAL_NOTA, " +
						  "ds.dh_emissao as EMISSAO, " +
					      "pclindn.tp_identificacao as TP_IDENTIFICACAO, " +
						  "pclindn.nr_identificacao as NR_IDENTIFICACAO, " +
						  "pclindn.nm_pessoa as CLIENTE, " +
						  "ddsf.tp_situacao_cobranca as ESTADO_COBRANCA, " +
						  "decode(red.id_redeco, null, fat.nr_fatura, red.nr_redeco) as DOCUMENTO_COBRANCA, " +
						  "decode(red.id_redeco, null, filfat.sg_filial, filred.sg_filial) as SG_FILIAL_DOCUMENTO_COBRANCA, " +
						  "fat.dt_vencimento as VENCIMENTO, " +
						  "decode(fat.dt_liquidacao, null, to_char(trunc(sysdate) - fat.dt_vencimento), '0') as DIAS_PENDENTES,  " +
						  "fat.dt_liquidacao as BAIXA, " +
						  "filfatindn.sg_filial as FILIAL_FATURA, " +
						  "fatindn.nr_fatura as NR_FATURA, " +
						  "fatindn.vl_total as VALOR, " +
						  "fatindn.vl_juro_calculado as JUROS_CALCULADO, " +
						  "indn.vl_juro_receber as JUROS_COBRADO ");
		
		sql.addFrom("docto_servico ds " +
				    "inner join nota_debito_nacional ndn on  ndn.id_nota_debito_nacional = ds.id_docto_servico " +
				    "inner join filial filds on filds.id_filial = ds.id_filial_origem " +
				    "inner join devedor_doc_serv_fat ddsf on ddsf.id_docto_servico = ds.id_docto_servico " +
				    "inner join filial filddsfcob on filddsfcob.id_filial = ddsf.id_filial " +
				    "inner join pessoa pfilddsfcob on pfilddsfcob.id_pessoa = filddsfcob.id_filial " +
				    "inner join cliente clindn on clindn.id_cliente = ndn.id_cliente " +
				    "inner join pessoa pclindn on pclindn.id_pessoa = clindn.id_cliente " +
				    "left join item_fatura if on if.id_devedor_doc_serv_fat = ddsf.id_devedor_doc_serv_fat " +
				    "left join fatura fat on fat.id_fatura = if.id_fatura and fat.tp_situacao_fatura <> 'CA' " +
				    "left join item_redeco ir on ir.id_fatura = fat.id_fatura " +
				    "left join redeco red on red.id_redeco = ir.id_redeco and red.tp_situacao_redeco <> 'CA' " +
				    "left join filial filfat on filfat.id_filial = fat.id_filial " +
				    "left join filial filred on filred.id_filial = red.id_filial " +
				    "inner join item_nota_debito_nacional indn on indn.id_nota_debito_nacional = ndn.id_nota_debito_nacional " +
				    "inner join fatura fatindn on fatindn.id_fatura = indn.id_fatura " +
				    "inner join filial filfatindn on filfatindn.id_filial = fatindn.id_filial ");
		
		//Variáveis recebidas do manter notas debito nacional
		Long idNotaDebitoNacional = tfm.getLong("idDoctoServico");
		Long nrNotaDebitoNac = tfm.getLong("nrNotaDebitoNac");
		
		if( idNotaDebitoNacional != null ){
			sql.addCriteria("ndn.id_nota_debito_nacional","=",idNotaDebitoNacional);
		}
		
		if( nrNotaDebitoNac != null ){
			sql.addCriteria("ndn.NR_NOTA_DEBITO_NAC","=",nrNotaDebitoNac);
		}		
		
		/** Resgata  a filialFaturamento.idFilial do request */
		Long idFiliaFat = tfm.getLong("filialFaturamento.idFilial");
		String nmFantasiaFat = tfm.getString("filialFaturamento.pessoa.nmPessoa");
		String sgFilialFat = tfm.getString("filialFaturamento.siglaFilial");
		if(idFiliaFat != null) {
			sql.addCriteria("ds.id_filial_origem", "=", idFiliaFat);
			sql.addFilterSummary("filialFaturamento", sgFilialFat + " - " + nmFantasiaFat);
		}
		
		/** Resgata a filialCobranca.idFilial do request */
		Long idFiliaCob = tfm.getLong("filialCobranca.idFilial");
		String nmFantasiaCob = tfm.getString("filialCobranca.pessoa.nmPessoa");
		String sgFilialCob = tfm.getString("filialCobranca.siglaFilial");
		if(idFiliaCob != null) {
			sql.addCriteria("ddsf.id_filial", "=", idFiliaCob);
			sql.addFilterSummary("filialCobranca", sgFilialCob + " - " + nmFantasiaCob);
		}
		
		/** Resgata  o tpSituacaoCobranca do request */
		YearMonthDay ymdInicial = tfm.getYearMonthDay("emissaoIni");
		YearMonthDay ymdFinal = tfm.getYearMonthDay("emissaoFim");
		
		if(ymdInicial != null && ymdFinal != null) {
			sql.addCustomCriteria(" (trunc(ds.dh_emissao)  between ? and ? ) ");
			
			sql.addCriteriaValue(ymdInicial);
			sql.addCriteriaValue(ymdFinal);
			
			sql.addFilterSummary("periodoEmissaoInicial", ymdInicial);
			sql.addFilterSummary("periodoEmissaoFinal", ymdFinal);
		}
		
		/** Resgata  o tpSituacaoCobranca do request */
		String tpSituacaoCobranca = tfm.getString("situacaoCobranca");
		if(tpSituacaoCobranca != null && StringUtils.isNotBlank(tpSituacaoCobranca)){
			sql.addCriteria("ndn.tp_situacao_nota_debito_nac", "=", tpSituacaoCobranca);
			sql.addFilterSummary("situacao", this.getDomainValueService().findDomainValueDescription("DM_STATUS_RECIBO_FRETE", tpSituacaoCobranca));
		}
		
		/** Filtra pelo campo tpSituacaoCobranca */
		String tpEstadoCobranca = tfm.getDomainValue("tpSituacaoCobranca").getValue();
		if( StringUtils.isNotBlank(tpEstadoCobranca) ){
			sql.addCriteria("ddsf.tp_situacao_cobranca", "=", tpEstadoCobranca);
			sql.addFilterSummary("estadoCobranca", this.getDomainValueService().findDomainValueDescription("DM_STATUS_COBRANCA_DOCTO_SERVICO", tpEstadoCobranca));
		}
		
		sql.addOrderBy("filddsfcob.sg_filial, pfilddsfcob.nm_fantasia, filds.sg_filial, ndn.nr_nota_debito_nac, filfatindn.sg_filial, fatindn.nr_fatura");

		return sql;
	}

}
