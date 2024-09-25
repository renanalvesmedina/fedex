package com.mercurio.lms.expedicao.report;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.conhecimentoSemAWBService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirRelatorioConhecimentosSemAWB.jasper"
 */
public class ConhecimentoSemAWBService extends ReportServiceSupport {
	private ConhecimentoCanceladoService conhecimentoCanceladoService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

		sql.addFilterSummary("filialOrigem",getDsFilial(MapUtilsPlus.getLong(parameters,"filial.idFilial",null)));
		sql.addFilterSummary("filialDestino",getDsFilial(MapUtilsPlus.getLong(parameters,"filialDestino.idFilial",null)));

		String dtInicial = (String) MapUtilsPlus.getObject(parameters,"dtInicial", null);
		if(dtInicial != null && !dtInicial.equals("")) {
			sql.addFilterSummary("periodoInicial",formatDate(dtInicial));
		}
		String dtFinal = (String) MapUtilsPlus.getObject(parameters,"dtFinal", null);
		if(dtFinal != null && !dtFinal.equals("")) {
			sql.addFilterSummary("periodoFinal",formatDate(dtFinal));
		}

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private String formatDate(Object date) throws Exception {
		java.text.SimpleDateFormat dtShort = new java.text.SimpleDateFormat("yyyy-mm-dd");
		java.text.SimpleDateFormat dtBR = new SimpleDateFormat("dd/mm/yyyy");
		return dtBR.format(dtShort.parse((String)date));
	}

	/**
	 * Retorna sigla - nome fantasia do filial do identificador especifico
	 */
	private String getDsFilial(Long idFilial) {
		if(idFilial == null) return "";
		List ret = getJdbcTemplate().queryForList(" select SG_FILIAL || ' - ' ||  NM_FANTASIA as header from filial, pessoa  where pessoa.id_pessoa = filial.id_filial and id_filial = " + idFilial);
		if (ret==null || ret.size()==0) return "";
		return (String)((Map)ret.get(0)).get("header");
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection(" f.SG_FILIAL || ' - ' ||  p.NM_FANTASIA as header, " +
						  " f.SG_FILIAL || ' ' || lpad(cnh.NR_CONHECIMENTO,6,0) || '-' || cnh.dv_conhecimento as ctrc, " +
						  " to_char(d.DH_EMISSAO,'dd/MM/yyyy') as DH_EMISSAO ,f.id_filial, " + 
						  " fdest.SG_FILIAL || ' - ' ||  pdest.NM_FANTASIA as fdestino");

		sql.addFrom(" CONHECIMENTO cnh, DOCTO_SERVICO D, SERVICO S, FILIAL F, PESSOA P, FILIAL FDEST, PESSOA PDEST ");

		sql.addCriteria(" cnh.TP_DOCUMENTO_SERVICO", "=", ConstantesExpedicao.CONHECIMENTO_NACIONAL);
		sql.addCriteria(" cnh.TP_SITUACAO_CONHECIMENTO", "=", ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO);
		sql.addCustomCriteria(" cnh.ID_CONHECIMENTO = D.ID_DOCTO_SERVICO ");
		sql.addCustomCriteria(" D.ID_SERVICO = S.ID_SERVICO ");
		sql.addCustomCriteria(" D.ID_FILIAL_DESTINO = FDEST.ID_FILIAL ");
		sql.addCustomCriteria(" FDEST.ID_FILIAL = PDEST.ID_PESSOA ");
		sql.addCustomCriteria(" S.TP_MODAL = 'A' ");
		sql.addCustomCriteria(" cnh.ID_FILIAL_ORIGEM = F.ID_FILIAL ");
		sql.addCustomCriteria(" F.ID_FILIAL = P.ID_PESSOA ");
		sql.addCustomCriteria(" not exists (select AWB.ID_CONHECIMENTO from CTO_AWB AWB where AWB.ID_CONHECIMENTO = CNH.ID_CONHECIMENTO) ");

		sql.addOrderBy("ctrc");
		sql.addOrderBy(" f.SG_FILIAL ASC ");		 

		if (MapUtilsPlus.getLong(parameters,"filial.idFilial",null)!=null)
			sql.addCriteria("cnh.ID_FILIAL_ORIGEM","=",MapUtilsPlus.getLong(parameters,"filial.idFilial",null));
		else
			sql.addCustomCriteria(" cnh.ID_FILIAL_ORIGEM in ( " + conhecimentoCanceladoService.getSqlTodasFiliaisUserLogado() + " ) " );
		
		if (MapUtilsPlus.getLong(parameters,"filialDestino.idFilial",null)!=null)
			sql.addCriteria("D.ID_FILIAL_DESTINO","=",MapUtilsPlus.getLong(parameters,"filialDestino.idFilial",null));
		
		if ( (MapUtilsPlus.getObject(parameters,"dtInicial",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtInicial",null)).equals("")) )
			sql.addCustomCriteria(" to_char(d.DH_EMISSAO ,'yyyy-mm-dd') >= '"  + MapUtilsPlus.getObject(parameters,"dtInicial",null).toString() + "'");
		
		if ( (MapUtilsPlus.getObject(parameters,"dtFinal",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtFinal",null)).equals("")) )
			sql.addCustomCriteria(" to_char(d.DH_EMISSAO ,'yyyy-mm-dd') <= '"  + MapUtilsPlus.getObject(parameters,"dtFinal",null).toString() + "'");
		
		return sql;
	}

	public ConhecimentoCanceladoService getConhecimentoCanceladoService() {
		return conhecimentoCanceladoService;
	}

	public void setConhecimentoCanceladoService(
			ConhecimentoCanceladoService conhecimentoCanceladoService) {
		this.conhecimentoCanceladoService = conhecimentoCanceladoService;
	}
	
	
}
