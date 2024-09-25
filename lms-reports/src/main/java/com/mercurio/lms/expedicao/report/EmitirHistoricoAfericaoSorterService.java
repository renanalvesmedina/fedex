package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.model.HistoricoAfericao;


/**
 * @spring.bean id="lms.pendencia.emitirHistoricoAfericaoSorterService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirHistoricoAfericaoSorter.jasper"
 */

public class EmitirHistoricoAfericaoSorterService extends ReportServiceSupport{

	/**
	 * método responsável por gerar o relatório. 
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {  	
		TypedFlatMap criteria = (TypedFlatMap) parameters;

		SqlTemplate sql = getSql();

		JRReportDataObject jrReportDataObject = executeQuery(sql.getSql(), sql.getCriteria());

		Map parametersReport = new HashMap();
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);

		jrReportDataObject.setParameters(parametersReport);
		return jrReportDataObject;
	}

	/**
	 * Método que monta o select para a consulta.
	 * 
	 * @param map
	 * @return SqlTemplate
	 */
	private SqlTemplate getSql() {
		StringBuilder query = new StringBuilder();
		query.append(" ha.ID_HISTORICO_AFERICAO as idHistoricoAfericao, ");
		query.append(" ea.ID_ETIQUETA_AFERICAO as idEtiquetaAfericao, ");
		query.append(" ha.DH_AFERICAO as dhAfericao, ");
		query.append(" fa.SG_FILIAL as sgFilialAfericao, ");
		query.append(" fo.SG_FILIAL as sgFilialOrigem, ");
		query.append(" fd.SG_FILIAL as sgFilialDestino, ");
		query.append(" rce.DS_ROTA as rota, ");
		query.append(" (ha.NR_COMPRIMENTO||' X '||ha.NR_LARGURA||' X '||ha.NR_ALTURA) as dimensoesAferidas, ");
		query.append(" (ea.NR_COMPRIMENTO||' X '||ea.NR_LARGURA||' X '||ea.NR_ALTURA) as dimensoesPadrao, ");
		query.append(" ha.PS_AFERIDO as pesoAferido, ");
		query.append(" ea.PS_INFORMADO as pesoPadrao ");
		
		SqlTemplate sql = this.createSqlTemplate();
		sql.addProjection(query.toString());
		sql.addFrom(" HISTORICO_AFERICAO ha , FILIAL fa, FILIAL fo, FILIAL fd, ETIQUETA_AFERICAO ea, ROTA_COLETA_ENTREGA rce ");
		
		sql.addCustomCriteria(" ha.ID_ETIQUETA_AFERICAO = ea.ID_ETIQUETA_AFERICAO ");
		sql.addCustomCriteria(" ha.ID_FILIAL = fa.ID_FILIAL ");
		sql.addCustomCriteria(" ea.ID_FILIAL_ORIGEM = fo.ID_FILIAL ");
		sql.addCustomCriteria(" ea.ID_FILIAL_DESTINO = fd.ID_FILIAL ");
		sql.addCustomCriteria(" ea.ID_ROTA_COLETA_ENTREGA = rce.ID_ROTA_COLETA_ENTREGA ");
		
		DateTime dataAtual = new DateTime();
		sql.addCriteria("ha.DH_AFERICAO",">=", dataAtual.minusDays(7));
		sql.addCriteria("ha.DH_AFERICAO","<=", dataAtual);
		
		sql.addOrderBy(" ha.DH_AFERICAO asc, fa.SG_FILIAL asc, fo.SG_FILIAL asc, fd.SG_FILIAL asc, rce.DS_ROTA asc ");		
		
		return sql; 
	}
}
