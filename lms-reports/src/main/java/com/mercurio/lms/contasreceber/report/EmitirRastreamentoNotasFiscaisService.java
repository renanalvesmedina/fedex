package com.mercurio.lms.contasreceber.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class EmitirRastreamentoNotasFiscaisService extends ReportServiceSupport {

	private FilialService filialService;
	
	private void addColumn(BufferedWriter writer, String value) throws IOException{
		addColumn(writer, value, false);
	}
	
	private void addColumn(BufferedWriter writer, String value, boolean last) throws IOException{
		if(value != null){
			writer.write(value);
		}
		if(!last){
			writer.write(";");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.report.ReportServiceSupport#execute(java.util.Map)
	 */
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		//Monta o SQL para geração do relatório 
		SqlTemplate sql = getSqlTemplate(parameters);
		//Inicializa o report.
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		//Passa os parâmetros para o relatório
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, "csv");
		jr.setParameters(parametersReport);
		
		
		return jr;
	}
	
	
	/**
	 * Monta a query de pesquisa para o relatório
	 * @param tfm Critérios de pesquisa
	 * @return SqlTemplate
	 */
	private SqlTemplate getSqlTemplate(final Map parameters) {
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("NFC.NR_NOTA_FISCAL AS NR_NOTAFISCAL_CLIENTE, " + 
		   "F.SG_FILIAL AS FILIAL ," +
		   "F.SG_FILIAL ||'/'|| C.NR_CONHECIMENTO AS DOCUMENTO ," + 
	       " TO_CHAR(NFC.DT_EMISSAO,'dd/mm/yyyy') AS DATA_EMISSAO ," + 
	       "DECODE(C.TP_FRETE,'C','CIF','FOB') AS TIPO_FRETE, " + 
	       "NFC.PS_MERCADORIA AS PESO_MERCADORIA, "+ 
	       "NFC.QT_VOLUMES AS QDE_VOLUMES, "+ 
	       "P.NR_IDENTIFICACAO AS CNPJ_REMETENTE, "+
	       "P.TP_IDENTIFICACAO AS TP_IDENTIFICACAO_REMETENTE, "+
	       "P.NM_PESSOA AS REMETENTE,"+
	       "PD.NR_IDENTIFICACAO AS CNPJ_DESTINATARIO, "+
	       "PD.TP_IDENTIFICACAO AS TP_IDENTIFICACAO_DESTINATARIO, "+
	       "PD.NM_PESSOA AS DESTINATARIO, "+
	       "DS.VL_TOTAL_DOC_SERVICO AS VALOR_FRETE, "+
	       "F.SG_FILIAL AS FILIAL_LIBERADORA, "+
	       "(select MAX(us.nm_usuario) KEEP (DENSE_RANK LAST ORDER BY lds.id_liberacao_doc_serv) "+
           "FROM LIBERACAO_DOC_SERV LDS "+
           "inner join usuario us on us.id_usuario = lds.id_usuario "+
           "where lds.id_docto_servico = ds.id_docto_servico) as RESPONSAVEL, "+
           "TO_CHAR(ds.dh_inclusao,'dd/mm/yyyy') AS DATA_INCLUSAO ");
		   	
		
		sql.addFrom("CONHECIMENTO C,"+
	       "FILIAL F, " +
	       "PESSOA P, "+
	       "PESSOA PD, "+
	       "NOTA_FISCAL_CONHECIMENTO NFC, "+
	       "DOCTO_SERVICO DS ");
	    		
		sql.addJoin("C.ID_CONHECIMENTO", "NFC.ID_CONHECIMENTO");
		sql.addJoin("C.ID_CONHECIMENTO", "DS.ID_DOCTO_SERVICO");
		sql.addJoin("C.ID_FILIAL_ORIGEM", "F.ID_FILIAL");
		sql.addJoin("DS.ID_CLIENTE_REMETENTE", "P.ID_PESSOA");
		sql.addJoin("DS.ID_CLIENTE_DESTINATARIO","PD.ID_PESSOA");
		 
		sql.addCriteria("C.TP_SITUACAO_CONHECIMENTO", "=", "P");

		sql.addOrderBy("FILIAL");
		sql.addOrderBy("REMETENTE");
	    sql.addOrderBy("NR_NOTAFISCAL_CLIENTE");
		
		//Monta filtros
	    final String DATA = "(DS.DH_INCLUSAO >= to_date('%s:00','dd/mm/yyyy hh24:mi:ss') AND DS.DH_INCLUSAO <= TO_DATE('%s:59','dd/mm/yyyy hh24:mi:ss'))";
				
		final YearMonthDay data = (YearMonthDay) parameters.get("data");
		final TimeOfDay hrInicio = (TimeOfDay) parameters.get("hrInicio");
		final TimeOfDay hrFim = (TimeOfDay) parameters.get("hrFim");

		final String dataIni = data.toString("dd/MM/yyyy").concat(" ").concat(hrInicio.toString("HH:mm"));
		final String dataFim = data.toString("dd/MM/yyyy").concat(" ").concat(hrFim.toString("HH:mm"));
		
		final Long idFilial = MapUtils.getLong(parameters, "idFilial");
		
		sql.addCustomCriteria(String.format(DATA, dataIni, dataFim));
		sql.addFilterSummary("data", data);
		sql.addFilterSummary("inicio", hrInicio.toString("HH:mm"));
		sql.addFilterSummary("final", hrFim.toString("HH:mm"));
		
		//Se foi informado o filtro de filial, adiciona o mesmo
		if (idFilial != null && idFilial != 0){
			sql.addCriteria("c.id_filial_origem", "=", idFilial);
			sql.addFilterSummary("filial", filialService.findSgFilialByIdFilial(idFilial));
		}
		
		
		return sql;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
}
	
}
