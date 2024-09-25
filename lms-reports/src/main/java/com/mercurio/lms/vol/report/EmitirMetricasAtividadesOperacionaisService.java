package com.mercurio.lms.vol.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTimeConstants;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vol.model.service.VolGruposFrotasService;



/**
 * @spring.bean id="lms.vol.emitirMetricasAtividadesOperacionaisService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/emitirMetricasAtividadesOperacionais.jasper"
 */
public class EmitirMetricasAtividadesOperacionaisService extends ReportServiceSupport {
	private FilialService filialService;
	private VolGruposFrotasService volGruposFrotasService;
	
	public VolGruposFrotasService getVolGruposFrotasService() {
		return volGruposFrotasService;
	}

	public void setVolGruposFrotasService(
			VolGruposFrotasService volGruposFrotasService) {
		this.volGruposFrotasService = volGruposFrotasService;
	}

	public JRReportDataObject execute(Map parameters) throws Exception {		

		TypedFlatMap criteria = (TypedFlatMap)parameters;
			
		YearMonthDay dataInicio = calculaDataInicio(criteria.getYearMonthDay("dtMesAno"));
		YearMonthDay dataFim = calculaDataFim(criteria.getYearMonthDay("dtMesAno"));
		
		String mes = DateTimeFormat.forPattern("MMM").withLocale(LocaleContextHolder.getLocale()).print(criteria.getYearMonthDay("dtMesAno")).trim();
		
		SqlTemplate sqlT = createSqlTemplate();
		
		//busca o id da filial, foi necessário devido ao comportamento da popup manter grupos frota. pois essa não retorna o id da filial
    	if( criteria.getLong("filial.idFilial") == null ){
	    	List filial =  this.getFilialService().findLookupBySgFilial(criteria.getString("filial.sgFilial"), criteria.getString("tpAcesso"));
	        Iterator it = filial.iterator();
	        Map linha = (Map) it.next();
	        criteria.put("filial", linha.get("idFilial"));
    	} else {
    		criteria.put("filial", criteria.getLong("filial.idFilial"));
    	}
		 
					
		criteria.put("dataInicio", dataInicio); 
		criteria.put("dataFim", dataFim);
		criteria.put("sgFilial", criteria.getString("filial.sgFilial"));
		criteria.put("nmFilial", criteria.getString("filial.pessoa.nmFantasia"));
		criteria.put("grupo", criteria.getLong("grupo.idGrupoFrota"));
		criteria.put("mes", mes);
			
		StringBuffer sql = mountSql(criteria, sqlT);
		JRReportDataObject jr = executeQuery(sql.toString(), criteria);
		
		Map parametersReport = new HashMap();

		parametersReport.put("PARAMETROS_PESQUISA",sqlT.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("MES", mes);
		
		jr.setParameters(parametersReport);
	
		return jr;
	
	}
	
	/**
	 * verifica se o primeiro dia do mês é segunda-feira, caso não seja altera a data de inicio para a primeira
	 * segunda-feira mês
	 * @param data
	 * @return primeira seguda-feira do mês
	 */
	private YearMonthDay calculaDataInicio(YearMonthDay data){
		YearMonthDay dataInicial = JTDateTimeUtils.getFirstDayOfYearMonthDay(data);
		int diaSemana = JTDateTimeUtils.getNroDiaSemana(data);
		
		if(diaSemana != DateTimeConstants.MONDAY){
			switch (diaSemana) {
			case DateTimeConstants.TUESDAY:	
				dataInicial = JTDateTimeUtils.setDay(data, 7);
				break;
			case DateTimeConstants.WEDNESDAY:
				dataInicial = JTDateTimeUtils.setDay(data, 6);		
				break;
			case DateTimeConstants.THURSDAY:
				dataInicial = JTDateTimeUtils.setDay(data, 5);		
				break;
			case DateTimeConstants.FRIDAY:
				dataInicial = JTDateTimeUtils.setDay(data, 4);		
				break;
			case DateTimeConstants.SATURDAY:
				dataInicial = JTDateTimeUtils.setDay(data, 3);		
				break;
			case DateTimeConstants.SUNDAY:
				dataInicial = JTDateTimeUtils.setDay(data, 2);
				break;	
			default:
				break;
			}
		}
		return dataInicial;	
	}
	
	
	private YearMonthDay calculaDataFim(YearMonthDay data){
			YearMonthDay dataFinal = JTDateTimeUtils.getLastDayOfYearMonthDay(data); //pega o último dia do mês
			int diaSemana = JTDateTimeUtils.getNroDiaSemana(dataFinal);
			if(diaSemana != DateTimeConstants.SUNDAY){
				int m = data.getMonthOfYear(); //pega o mês data (parametro)
				dataFinal = JTDateTimeUtils.setMonth(data, m+1); //seta o mês da data para o mês posterior ao da data passada como parâmetro
				switch (diaSemana) {
				case DateTimeConstants.MONDAY:
					dataFinal = JTDateTimeUtils.setDay(dataFinal, 6);
					break;
				case DateTimeConstants.TUESDAY:	
					dataFinal = JTDateTimeUtils.setDay(dataFinal, 5);
					break;
				case DateTimeConstants.WEDNESDAY:
					dataFinal = JTDateTimeUtils.setDay(dataFinal, 4);		
					break;
				case DateTimeConstants.THURSDAY:
					dataFinal = JTDateTimeUtils.setDay(dataFinal, 3);		
					break;
				case DateTimeConstants.FRIDAY:
					dataFinal = JTDateTimeUtils.setDay(dataFinal, 2);		
					break;
				case DateTimeConstants.SATURDAY:
					dataFinal = JTDateTimeUtils.setDay(dataFinal, 1);		
					break;	
				default:
					break;
				}
			}
			return dataFinal;	
		}

	
	private StringBuffer mountSql(TypedFlatMap criteria, SqlTemplate sql) throws Exception{
			
		StringBuffer sb = new StringBuffer();
		
		sb.append("select dias.data data, dias.semana semana , ")
		  .append(":mes mes_d, ")
		  .append("nvl(aux.ctrc_col,0) ctrc_col, nvl(aux.ctrc_ent,0) ctrc_ent, nvl(aux.total_ctrc,0) total_ctrc , ")
	      .append("nvl(aux.eventos_col,0) eventos_col, nvl(aux.eventos_ent,0) eventos_ent, nvl(aux.total_eventos,0) total_eventos , ")  
	      .append("nvl(aux.carros,0) carros, nvl(aux.media_ctrc,0) media_ctrc, nvl(aux.media_eventos,0) media_eventos ")
	      .append("from ( ")
	      .append("select ") 
	      .append("    (:dataInicio + (level - 1)) data, ceil(rownum / 7) semana ")
	      .append("from dual connect by level <= 40 ) dias, ")
	      .append("( ")
	      .append("  select ")  
	      .append("    to_date(nvl(entr.dt,cole.dt)) data, ")
	      .append("	   sum(nvl(cole.ctrc_col,0)) ctrc_col, ")
	      .append("    sum(nvl(entr.ctrc_ent,0)) ctrc_ent, ")
	      .append("    sum(nvl(cole.ctrc_col,0)) + sum(nvl(entr.ctrc_ent,0)) total_ctrc, ")
	      .append("    sum(nvl(cole.eventos_col,0)) eventos_col, ")
	      .append("    sum(nvl(entr.eventos_ent,0)) eventos_ent, ")
	      .append("    sum(nvl(cole.eventos_col,0)) + sum(nvl(entr.eventos_ent,0)) total_eventos, ")
	      .append("    count(distinct frot.id_meio_transporte)    carros, ")
	      .append("   (sum(nvl(cole.ctrc_col,0)) + sum(nvl(entr.ctrc_ent,0))) / count(distinct frot.id_meio_transporte)  media_ctrc, ")
	      .append("   (sum(nvl(cole.eventos_col,0)) + sum(nvl(entr.eventos_ent,0))) /  count(distinct frot.id_meio_transporte) media_eventos ")
	      .append("from meio_transporte frot ")
	      .append("left join ( ") 
	      .append("     select trunc(medo.dh_ocorrencia) dt, ")
	      .append("            coca.id_transportado id_meio_transporte, ")
	      .append("            count(distinct medo.ID_DOCTO_SERVICO)    ctrc_ent, ")
	      .append("            count(distinct ctrc.DS_ENDERECO_ENTREGA) eventos_ent ")
	      .append("     from  controle_carga coca ")  
	      .append("     inner join manifesto  mani on mani.id_controle_carga = coca.id_controle_carga ") 
	      .append("     inner join manifesto_entrega maen on maen.id_manifesto_entrega = mani.id_manifesto ") 
	      .append("     inner join manifesto_entrega_documento medo on medo.ID_MANIFESTO_ENTREGA = maen.ID_MANIFESTO_ENTREGA ") 
	      .append("     inner join filial fili on maen.id_filial = fili.id_filial ")
	      .append("     inner join conhecimento ctrc on medo.id_docto_servico = ctrc.id_conhecimento ")
	      .append("     where medo.dh_ocorrencia between :dataInicio and :dataFim ")
	      .append("           and fili.id_Filial = :filial ")
	      .append("     group by trunc(medo.dh_ocorrencia), coca.id_transportado ")
	      .append(" ) entr on entr.id_meio_transporte  = frot.id_meio_transporte ")
	      .append("left join ( ")
	      .append("     select trunc(evco.dh_evento) dt, ")
	      .append("            evco.id_meio_transporte id_meio_transporte, ")
	      .append("            count(distinct evco.ID_EVENTO_COLETA) ctrc_col, ")
	      .append("            count(distinct peco.ID_ENDERECO_PESSOA) eventos_col ")
	      .append("     from evento_coleta evco ")
	      .append("     inner join pedido_coleta peco on peco.id_pedido_coleta = evco.id_pedido_coleta ")
	      .append("     inner join filial  fili on fili.id_filial = peco.id_filial_responsavel ") 
	      .append("     where evco.tp_evento_coleta = 'EX' and fili.id_filial = :filial ")
	      .append(" 	      and evco.dh_evento between :dataInicio and :dataFim ")
	      .append("     group by trunc(evco.dh_evento), evco.id_meio_transporte ")			  
	      .append(" ) cole on cole.id_meio_transporte  = frot.id_meio_transporte ")
	      .append("left join GRUPO_FROTA_VEICULO grve on grve.ID_MEIO_TRANSPORTE = frot.ID_MEIO_TRANSPORTE ") 
	      .append("left join grupo_frota grfr on grfr.ID_GRUPO_FROTA = grve.ID_GRUPO_FROTA ")
	      .append("where nvl(cole.ctrc_col,0) + nvl(entr.ctrc_ent,0) >0 ");
	      
        sql.addFilterSummary("filial", criteria.getString("sgFilial") + " - " + criteria.getString("nmFilial"));
		
		if (criteria.getLong("grupo") != null) {
			  sb.append("and grve.ID_GRUPO_FROTA = :grupo ");
			  List listGrupo = this.getVolGruposFrotasService().findDsNomeById(criteria.getLong("grupo"));
			  Iterator itGrupo = listGrupo.iterator();
			  Map linhaGrupo = (Map)itGrupo.next();
			  String dsGrupoFrota = (String)linhaGrupo.get("dsNome");
			  sql.addFilterSummary("grupoFrota", dsGrupoFrota);
			} else {
				sql.addFilterSummary("grupoFrota", "");
			}
		
		String mes = DateTimeFormat.forPattern("MM").withLocale(LocaleContextHolder.getLocale()).print(criteria.getYearMonthDay("dtMesAno"));
		String ano = DateTimeFormat.forPattern("yyyy").withLocale(LocaleContextHolder.getLocale()).print(criteria.getYearMonthDay("dtMesAno"));
		
		sql.addFilterSummary("periodo", mes + "/" + ano);
		
		sb.append("group by nvl(entr.dt,cole.dt) ) aux ")
		  .append("where dias.data <= :dataFim and dias.data = aux.data(+) ")
		  .append("order by dias.data");
		
		return sb;
	   }

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}
    
