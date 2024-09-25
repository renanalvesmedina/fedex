package com.mercurio.lms.expedicao.report;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.conhecimentoCanceladoService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirConhecimentosCancelados.jasper"
 */
public class ConhecimentoCanceladoService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {

		SqlTemplate sql = getSqlTemplate(parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		Long idFilial = MapUtilsPlus.getLong(parameters,"filial.idFilial",null);
		if(idFilial != null)
		{
			sql.addFilterSummary("filial",getDsFilial(idFilial));
		}
		Object dataInicial = MapUtilsPlus.getObject(parameters,"dtInicial", null);
		String dataInicialFormatada = (dataInicial == null || dataInicial.equals(""))? "" : formatDate(dataInicial);
		Object dataFinal = MapUtilsPlus.getObject(parameters,"dtFinal", null);
		String dataFinalFormatada = (dataFinal == null || dataFinal.equals(""))? "" : formatDate(dataFinal);
		sql.addFilterSummary("periodoInicial", dataInicialFormatada);
		sql.addFilterSummary("periodoFinal", dataFinalFormatada);	

		String abrangencia = MapUtilsPlus.getString(parameters,"tpAbrangencia", null);
		if(abrangencia != null)
		{
			String abrang = getAbrangencia(abrangencia);
			sql.addFilterSummary("abrangencia", abrang);
		}
		
		Long idMotivoCanc = MapUtilsPlus.getLong(parameters,"motivoCancelamento.idMotivoCancelamento", null);
		if(idMotivoCanc != null)
		{
			String motivoCanc = getMotivoCancelamento(idMotivoCanc);
			sql.addFilterSummary("motivoCancelamento", motivoCanc);
		}
		
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		parametersReport.put("TOT_CONHECIMENTOS",getTotalConhecimentos(parameters));
		jr.setParameters(parametersReport);

		return jr;
	}

	/**
	 * 
	 * @return
	 */
	public String getSqlTodasFiliaisUserLogado(){
		
		List filiais = SessionUtils.getFiliaisUsuarioLogado();
		StringBuffer sbFiliais = new StringBuffer();
		
		if (filiais!=null && filiais.size()!=0)
		{
			ListIterator lit = filiais.listIterator();
			int i=0;
			while(lit.hasNext())
			{
				if (i==0)
					sbFiliais.append(((Filial)lit.next()).getIdFilial());
				else
					sbFiliais.append(", " + ((Filial)lit.next()).getIdFilial());	
				i++;
			}
		}
		return sbFiliais.toString();		
	}
	
	/**
	 * 
	 * @param abrangencia
	 * @return
	 */
	private String getAbrangencia(String abrangencia)
	{
		if (StringUtils.isBlank(abrangencia)) {
			return null;
		}
		
		// caso ocorram erros neste ponto é porque não havia um Valor Dominio cadastrado
		// para a abrangência retornada, isto não é uma situação comum, já que o sistema deve
		// ter todas as informações previamente cadastradas. Este método usa o cache de dominios
		return getDomainValueService().findDomainValueByValue("DM_ABRANGENCIA", abrangencia).getDescription().getValue();
	}
	
	/**
	 * 
	 * @param parameters
	 * @return
	 */
	private Integer getTotalConhecimentos(Map parameters)
	{
		String proj    = " SELECT COUNT(*) AS TOT FROM ";
		String from    = " CONHECIMENTO con, DOCTO_SERVICO ds ";
		String where   = " WHERE TP_SITUACAO_CONHECIMENTO IN ('E','C') and con.id_conhecimento = ds.id_docto_servico ";
		String groupBy = "";
		
		if (MapUtilsPlus.getLong(parameters,"filial.idFilial",null)!=null)
			where += " and con.ID_FILIAL_ORIGEM=" + MapUtilsPlus.getLong(parameters,"filial.idFilial",null);
		else
			where += " and con.ID_FILIAL_ORIGEM IN (" +  getSqlTodasFiliaisUserLogado() + ")";

		if ( (MapUtilsPlus.getObject(parameters,"dtInicial",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtInicial",null)).equals("")) )			
			where += " and to_char(ds.DH_ALTERACAO,'yyyy-mm-dd') >= '" + MapUtilsPlus.getObject(parameters,"dtInicial",null).toString() + "'";
		
		if ( (MapUtilsPlus.getObject(parameters,"dtFinal",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtFinal",null)).equals("")) )
			where += " and to_char(ds.DH_ALTERACAO,'yyyy-mm-dd') <= '" + MapUtilsPlus.getObject(parameters,"dtFinal",null).toString() + "'";
		
		if (MapUtilsPlus.getString(parameters,"tpAbrangencia",null)!=null)
		{
			from  += " ,servico ser ";
			where += " and ds.ID_SERVICO = ser.ID_SERVICO";
			where += " and ser.TP_ABRANGENCIA='" + MapUtilsPlus.getString(parameters,"tpAbrangencia",null) + "'";
		}
		
		if (MapUtilsPlus.getLong(parameters,"motivoCancelamento.idMotivoCancelamento",null)!=null)		
			where += " and con.ID_MOTIVO_CANCELAMENTO=" + MapUtilsPlus.getLong(parameters,"motivoCancelamento.idMotivoCancelamento",null);
		
		return getJdbcTemplate().queryForInt(proj + from + where + groupBy);
	}
	
	/**
	 * 
	 * @param idFilial
	 * @return
	 */
	private String getDsFilial(Long idFilial)
	{
		if(idFilial == null) return null;
		
		List ret = getJdbcTemplate().queryForList(" select SG_FILIAL || ' - ' ||  NM_FANTASIA as header from filial, pessoa  where pessoa.id_pessoa = filial.id_filial and id_filial = ?", new Object[]{idFilial});
		if (ret==null || ret.size()==0) return null;
		return (String)((Map)ret.get(0)).get("header");
	}
	
	/**
	 * @param idMotivoCanc
	 * @return
	 */
	private String getMotivoCancelamento(Long idMotivoCanc){
		if(idMotivoCanc == null) return null;		
		List ret = getJdbcTemplate().queryForList(" select "+PropertyVarcharI18nProjection.createProjection("DS_MOTIVO_CANCELAMENTO_I", "header")+" from MOTIVO_CANCELAMENTO where id_motivo_cancelamento = ?", new Object[]{idMotivoCanc});
		if (ret==null || ret.size()==0) return null;
		return (String)((Map)ret.get(0)).get("header");
	}
	
	/**
	 * @param date
	 * @return
	 * @throws Exception
	 */
	private String formatDate(Object date) throws Exception	{
		java.text.SimpleDateFormat dtShort = new java.text.SimpleDateFormat("yyyy-mm-dd");	   
		java.text.SimpleDateFormat dtBR = new SimpleDateFormat("dd/mm/yyyy");		   
		return dtBR.format(dtShort.parse((String)date));		   
	}

	/**
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	private SqlTemplate getSqlTemplate(Map parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append(" f.sg_filial, ");
		query.append(PropertyVarcharI18nProjection.createProjection("mc.ds_motivo_cancelamento_i", "ds_motivo_cancelamento")).append(", ");
		query.append("co.nr_conhecimento, ");
		query.append("f.id_filial, ");
		query.append(" f.sg_filial || ' - ' || p.nm_fantasia as header, ");
		query.append("mc.id_motivo_cancelamento, ");
		query.append(" f.sg_filial || ' ' || lpad(co.nr_conhecimento,6,0) || '-' || co.dv_conhecimento as conhecimento, ");
		query.append(" to_char(ds.dh_alteracao,'dd/MM/yyyy hh:mm') as data_cancelamento, ");
		query.append("vfunc.nm_funcionario as resp_cancel");

		SqlTemplate sql = this.createSqlTemplate();
		sql.addProjection(query.toString());
		sql.addFrom(" conhecimento co , filial f, motivo_cancelamento mc, pessoa p, docto_servico ds, v_funcionario vfunc, usuario u ");

		sql.addCustomCriteria(" co.TP_SITUACAO_CONHECIMENTO = 'C' ");	
		sql.addCustomCriteria(" co.id_filial_origem = f.id_filial ");
		sql.addCustomCriteria(" co.id_motivo_cancelamento = mc.id_motivo_cancelamento ");
		sql.addCustomCriteria(" p.id_pessoa = f.id_filial "); 
		sql.addCustomCriteria(" co.id_conhecimento = ds.id_docto_servico ");  
		sql.addCustomCriteria(" u.id_usuario =  ds.id_usuario_alteracao "); 
		sql.addCustomCriteria(" u.nr_matricula = vfunc.nr_matricula ");

		String dataInicial = MapUtils.getString(parameters,"dtInicial");
		String dataFinal = MapUtils.getString(parameters,"dtFinal");
		String dataInicialFormatada = !StringUtils.isBlank(dataInicial) ? JTDateTimeUtils.convertFrameworkDateToFormat(dataInicial,"dd/MM/yyyy") : null;
		String dataFinalFormatada   = !StringUtils.isBlank(dataFinal)   ? JTDateTimeUtils.convertFrameworkDateToFormat(dataFinal,"dd/MM/yyyy") : null;
		YearMonthDay dataInicialYMD = !StringUtils.isBlank(dataInicial) ? JTDateTimeUtils.convertDataStringToYearMonthDay(dataInicialFormatada.replace("/","")) : null;
		YearMonthDay dataFinalYMD   = !StringUtils.isBlank(dataFinal)   ? JTDateTimeUtils.convertDataStringToYearMonthDay(dataFinalFormatada.replace("/","")) : null;		

		if (MapUtilsPlus.getLong(parameters,"filial.idFilial",null)!=null)
				sql.addCriteria("co.ID_FILIAL_ORIGEM","=",MapUtilsPlus.getLong(parameters,"filial.idFilial",null));
		else
				sql.addCustomCriteria(" co.ID_FILIAL_ORIGEM IN (" +  getSqlTodasFiliaisUserLogado() + ") ");

		if ( (MapUtilsPlus.getObject(parameters,"dtInicial",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtInicial",null)).equals("")) )			
			sql.addCustomCriteria(" to_char(ds.DH_ALTERACAO,'yyyy-mm-dd') >= '" + dataInicialYMD + "'");
		
		if ( (MapUtilsPlus.getObject(parameters,"dtFinal",null)!=null) && (!((String)MapUtilsPlus.getObject(parameters,"dtFinal",null)).equals("")) )
			sql.addCustomCriteria(" to_char(ds.DH_ALTERACAO,'yyyy-mm-dd') <= '" + dataFinalYMD + "'");

		if (MapUtilsPlus.getString(parameters,"tpAbrangencia",null)!=null)
		{
			sql.addFrom(" servico ser ");
			sql.addCustomCriteria(" ds.ID_SERVICO = ser.ID_SERVICO");
			sql.addCriteria("ser.TP_ABRANGENCIA","=",MapUtilsPlus.getString(parameters,"tpAbrangencia",null));
		}

		if (MapUtilsPlus.getLong(parameters,"motivoCancelamento.idMotivoCancelamento",null)!=null)		
			sql.addCriteria("co.ID_MOTIVO_CANCELAMENTO","=",MapUtilsPlus.getLong(parameters,"motivoCancelamento.idMotivoCancelamento",null)); 

		sql.addOrderBy(" f.SG_FILIAL, ds_motivo_cancelamento, co.NR_CONHECIMENTO ");

		return sql;
	}
}
