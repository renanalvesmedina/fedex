package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * Classe responsável pela geração do Relatório de Monitoramento  de Documentos Eletrônico.
 * Especificação técnica 04.05.01.09
 * 
 * @spring.bean id="lms.expedicao.emitirRelatorioMonitoramentoDocEletronicoService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirRelatorioMonitoramentoDocEletronico.jasper"
 */
public class EmitirRelatorioMonitoramentoDocEletronicoService extends ReportServiceSupport{
	
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		// TODO Auto-generated method stub
		SqlTemplate sql = this.mountSql(parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,JRReportDataObject.EXPORT_XLS);
        
        jr.setParameters(parametersReport);
        
		return jr;
	}

    /**
     * Monta o sql do relatório
     * @param parameters
     * @return
     * @throws Exception
     */     
    private SqlTemplate mountSql(Map parameters) throws Exception { 
    	SqlTemplate sqlTemplate = new SqlTemplate();

        // Adiciona os campos de retorno do select
    	sqlTemplate.addProjection("f.sg_filial", "Origem");
    	sqlTemplate.addProjection("ds.nr_docto_servico", "Numero");
    	sqlTemplate.addProjection("SUBSTR(v3.ds_valor_dominio_i,7,LENGTH(v3.ds_valor_dominio_i)-7)", "Tipo");
    	sqlTemplate.addProjection("to_char(ds.dh_emissao, 'dd/MM/yyyy')", "Emissao");
    	sqlTemplate.addProjection("m.nr_chave", "Chave_de_Acesso");
    	sqlTemplate.addProjection("SUBSTR(v1.ds_valor_dominio_i,7,LENGTH(v1.ds_valor_dominio_i)-7)", "Sit_SEFAZ_Prefeitura");
    	sqlTemplate.addProjection("SUBSTR(v2.ds_valor_dominio_i,7,LENGTH(v2.ds_valor_dominio_i)-7)", "Sit_LMS");
    	sqlTemplate.addProjection("p.nr_identificacao", "CPF_CNPJ");
    	sqlTemplate.addProjection("p.nm_pessoa", "Cliente_Tomador");
    	sqlTemplate.addProjection("ds.vl_total_doc_servico", "Frete");
    	sqlTemplate.addProjection("p.tp_pessoa", "Tp_Pessoa");
    	
    	// Adiciona as tabelas necessárias
    	sqlTemplate.addFrom("MONITORAMENTO_DOC_ELETRONICO", "m");
    	sqlTemplate.addFrom("DOCTO_SERVICO", "ds");
    	sqlTemplate.addFrom("CONHECIMENTO", "c");
    	sqlTemplate.addFrom("FILIAL", "f");
    	sqlTemplate.addFrom("DEVEDOR_DOC_SERV", "dds");
    	sqlTemplate.addFrom("PESSOA", "p");
    	sqlTemplate.addFrom("DOMINIO", "d1");
    	sqlTemplate.addFrom("VALOR_DOMINIO", "v1");
    	sqlTemplate.addFrom("DOMINIO", "d2");
    	sqlTemplate.addFrom("VALOR_DOMINIO", "v2");
    	sqlTemplate.addFrom("DOMINIO", "d3");
    	sqlTemplate.addFrom("VALOR_DOMINIO", "v3");

    	// Adiciona os joins
    	sqlTemplate.addJoin("m.id_docto_servico", "ds.id_docto_servico");
    	sqlTemplate.addJoin("c.id_conhecimento", "ds.id_docto_servico");
    	sqlTemplate.addJoin("ds.id_filial_origem", "f.id_filial");
    	sqlTemplate.addJoin("dds.id_docto_servico", "ds.id_docto_servico");
    	sqlTemplate.addJoin("dds.id_cliente", "p.id_pessoa");
    	sqlTemplate.addJoin("v1.id_dominio",  "d1.id_dominio");
    	sqlTemplate.addJoin("m.tp_situacao_documento", "v1.vl_valor_dominio");
    	sqlTemplate.addJoin("v2.id_dominio", "d2.id_dominio");
    	sqlTemplate.addJoin("c.tp_situacao_conhecimento", "v2.vl_valor_dominio");
    	sqlTemplate.addJoin("v3.id_dominio", "d3.id_dominio");
    	sqlTemplate.addJoin("ds.tp_documento_servico", "v3.vl_valor_dominio");

    	//Adicionar Ordenação
    	sqlTemplate.addOrderBy("ds.id_filial_origem");
    	sqlTemplate.addOrderBy("ds.nr_docto_servico");
    	
    	
    	Long idDoctoServico = MapUtils.getLong(parameters, "idDoctoServico");    	
		if (idDoctoServico != null) {
			sqlTemplate.addCriteria("ds.id_Docto_Servico", "=", idDoctoServico);
			
			Long idFilialOrigem = MapUtils.getLong(parameters, "idFilialOrigem");
			sqlTemplate.addCriteria("ds.id_Filial_Origem", "=", idFilialOrigem);
		} else {

	    	//Adicionar Critério
	    	Long idFilial = MapUtils.getLong(parameters, "idFilial");
	    	if(idFilial != null) {
	    		sqlTemplate.addCriteria("f.id_filial", "=", idFilial);
	    	}

	    	YearMonthDay data = (YearMonthDay) parameters.get("data");
	    	if(data != null) {
	    		sqlTemplate.addCriteria("ds.dh_emissao", ">=", data.toDateTimeAtMidnight());
	    		YearMonthDay dataPlus = data.plusDays(1);
	    		sqlTemplate.addCriteria("ds.dh_emissao", "<", dataPlus.toDateTimeAtMidnight());
	    	}

	    	String situacaoSefazPref = (String) parameters.get("situacaoSefazPref");
	    	if(situacaoSefazPref != null) {
	    		sqlTemplate.addCriteria("v1.vl_valor_dominio", "=", situacaoSefazPref);
	    	}
	
	    	String statusConhecimento = (String) parameters.get("situacaoOperacional");
	    	if(statusConhecimento != null) {
	    		sqlTemplate.addCriteria("v2.vl_valor_dominio", "=", statusConhecimento);
	    	}
	    	
			String tpDocumentoServico = (String) parameters.get("tpDocumentoServico");
			sqlTemplate.addCriteria("ds.tp_documento_servico", "=", tpDocumentoServico);
	    	
	    	Long idResponsavelFrete = MapUtils.getLong(parameters, "idCliente");
	    	if(idResponsavelFrete != null) {
	    		sqlTemplate.addCriteria("dds.id_cliente", "=", idResponsavelFrete);
	    	}
    	}
		
		sqlTemplate.addCriteria("d1.nm_dominio", "=", "DM_SITUACAO_DOC_ELETRONICO");
		sqlTemplate.addCriteria("d2.nm_dominio", "=", "DM_STATUS_CONHECIMENTO");
		sqlTemplate.addCriteria("d3.nm_dominio", "=", "DM_TIPO_DOCUMENTO_SERVICO");
    	
    	return sqlTemplate;
    }

}
