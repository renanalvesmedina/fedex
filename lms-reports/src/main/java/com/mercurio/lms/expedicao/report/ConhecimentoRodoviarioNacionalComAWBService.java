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
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.conhecimentoRodoviarioNacionalComAWBService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirConhecimentosRodoviariosNacionaisAWB.jasper"
 */
public class ConhecimentoRodoviarioNacionalComAWBService extends ReportServiceSupport {

	private ConhecimentoCanceladoService  conhecimentoCanceladoService; 
	
	public JRReportDataObject execute(Map parameters) throws Exception {

		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		Long idFilial = MapUtilsPlus.getLong(parameters,"filial.idFilial",null);
		String dsFilial = getDsFilial(idFilial)==null ? "" : getDsFilial(idFilial);
		sql.addFilterSummary("filialOrigem", dsFilial);

		Long idFilialDestino = MapUtilsPlus.getLong(parameters,"filialDestino.idFilial",null);
		String dsFilialDestino = getDsFilial(idFilialDestino)==null ? "" : getDsFilial(idFilialDestino);
		sql.addFilterSummary("filialDestino", dsFilialDestino);
		
		Object dataInicial = MapUtilsPlus.getObject(parameters,"dtInicial", null);
		String dataInicialFormatada = (dataInicial == null || dataInicial.equals(""))? "" : formatDate(dataInicial);
		Object dataFinal = MapUtilsPlus.getObject(parameters,"dtFinal", null);
		String dataFinalFormatada = (dataFinal == null || dataFinal.equals(""))? "" : formatDate(dataFinal);
		
		sql.addFilterSummary("periodoInicial", dataInicialFormatada);
		sql.addFilterSummary("periodoFinal", dataFinalFormatada);		
		
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		Moeda moeda = SessionUtils.getMoedaSessao();
		parametersReport.put("DS_SIMBOLO", moeda.getDsSimbolo());
		
		jr.setParameters(parametersReport);

		return jr;
	}

	/**
	 * 
	 * @param idFilial
	 * @return
	 */
	private String getDsFilial(Long idFilial)
	{
		if(idFilial == null) return null;
		
		List ret = getJdbcTemplate().queryForList(" select SG_FILIAL || ' - ' ||  NM_FANTASIA as header from filial, pessoa  where pessoa.id_pessoa = filial.id_filial and id_filial = " + idFilial);
		if (ret==null || ret.size()==0) return null;
		return (String)((Map)ret.get(0)).get("header");
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	private String formatDate(Object date) throws Exception
	{
		java.text.SimpleDateFormat dtShort = new java.text.SimpleDateFormat("yyyy-mm-dd");	   
		java.text.SimpleDateFormat dtBR    = new SimpleDateFormat("dd/mm/yyyy");		   
		return dtBR.format(dtShort.parse((String)date));		   
	}
	
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("DISTINCT f.id_filial, f.SG_FILIAL || ' - ' || p.NM_FANTASIA as header, " +
						  "(select fd.sg_filial from filial fd, pessoa pd where fd.id_filial = pd.id_pessoa " +
						  " and fd.id_filial = ds.id_filial_destino) AS filialdestino," +
						  "	f.SG_FILIAL || ' ' || lpad(cnh.NR_CONHECIMENTO,6,0) || '-' || cnh.DV_CONHECIMENTO as num_ctrc, " +
						  " ds.VL_TOTAL_DOC_SERVICO as vlr_ctrc, " +
						  " to_char(ds.DH_EMISSAO,'dd/MM/yyyy') as dt_emissao, " +
						  " a.NR_AWB , a.VL_FRETE as vlr_awb, ds.ps_real as peso_tot_ctrc, a.ps_cubado as peso_cub_awb," + 
						  " pcd.NM_PESSOA as destinatario, pcr.NM_PESSOA as remetente " );
		
		sql.addFrom(" conhecimento cnh, DOCTO_SERVICO ds, SERVICO s, CTO_AWB ca, AWB a, filial f, pessoa p, pessoa pcd, pessoa pcr ");
		
		sql.addCustomCriteria(" cnh.ID_CONHECIMENTO = ds.ID_DOCTO_SERVICO " );
		sql.addCustomCriteria(" s.ID_SERVICO = ds.ID_SERVICO ");
		sql.addCustomCriteria(" s.TP_MODAL = 'R' ");
		sql.addCustomCriteria(" s.TP_ABRANGENCIA = 'N' ");
		sql.addCustomCriteria(" cnh.ID_CONHECIMENTO = ca.ID_CONHECIMENTO ");
		sql.addCustomCriteria(" ca.ID_AWB = a.ID_AWB ");
		sql.addCustomCriteria(" cnh.TP_SITUACAO_CONHECIMENTO = 'E' ");
		sql.addCustomCriteria(" a.TP_STATUS_AWB='E' ");
		sql.addCustomCriteria(" f.id_filial = p.id_pessoa ");
		sql.addCustomCriteria(" cnh.id_filial_origem = f.id_filial ");
		sql.addCustomCriteria(" ds.id_cliente_destinatario = pcd.id_pessoa ");
		sql.addCustomCriteria(" ds.id_cliente_remetente = pcr.id_pessoa ");
		
		if (MapUtilsPlus.getLong(parameters,"filial.idFilial",null)!=null)
			sql.addCriteria("cnh.ID_FILIAL_ORIGEM","=",MapUtilsPlus.getLong(parameters,"filial.idFilial",null));
		else
			sql.addCustomCriteria(" cnh.ID_FILIAL_ORIGEM IN (" +  getConhecimentoCanceladoService().getSqlTodasFiliaisUserLogado() + ") ");

		if(MapUtilsPlus.getLong(parameters,"filialDestino.idFilial",null)!=null)
		{
			sql.addCriteria(" ds.id_filial_destino","=",MapUtilsPlus.getLong(parameters,"filialDestino.idFilial",null));
		}
		
		if ( (MapUtilsPlus.getObject(parameters,"dtInicial",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtInicial",null)).equals("")) )			
			sql.addCustomCriteria(" to_char(ds.DH_EMISSAO,'yyyy-mm-dd') >= '" + MapUtilsPlus.getObject(parameters,"dtInicial",null).toString() + "'");
	
		if ( (MapUtilsPlus.getObject(parameters,"dtFinal",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtFinal",null)).equals("")) )
			sql.addCustomCriteria(" to_char(ds.DH_EMISSAO,'yyyy-mm-dd') <= '" + MapUtilsPlus.getObject(parameters,"dtFinal",null).toString() + "'");
		
	
		sql.addOrderBy(" header, num_ctrc, dt_emissao ");		

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
