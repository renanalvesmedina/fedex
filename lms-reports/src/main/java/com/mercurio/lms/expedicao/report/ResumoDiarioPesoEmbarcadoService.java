package com.mercurio.lms.expedicao.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Rodrigo F. Dias
 *
 * @spring.bean id="lms.expedicao.resumoDiarioPesoEmbarcadoService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/resumoDiarioPesoEmbarcadoManifesto.jasper"
 */
public class ResumoDiarioPesoEmbarcadoService extends ReportServiceSupport {

	private ConfiguracoesFacade configuracoesFacade;
	
	public JRReportDataObject execute(Map parameters) throws Exception {

		Map mapFilialOrigem	 = MapUtils.getMap(parameters,"filialByIdFilialOrigem");
		Long idFilialOrigem  = MapUtils.getLong(mapFilialOrigem,"idFilial");
		Map mapFilialDestino = MapUtils.getMap(parameters,"filialByIdFilialDestino");
		Long idFilialDestino = MapUtils.getLong(mapFilialDestino,"idFilial");
		
		String dataInicial 	 = MapUtils.getString(parameters,"dataInicial");
		String dataFinal 	 = MapUtils.getString(parameters,"dataFinal");
		String dataInicialFormatada = !StringUtils.isBlank(dataInicial) ? JTDateTimeUtils.convertFrameworkDateToFormat(dataInicial,"dd/MM/yyyy") : null;
		String dataFinalFormatada = !StringUtils.isBlank(dataFinal) ? JTDateTimeUtils.convertFrameworkDateToFormat(dataFinal,"dd/MM/yyyy") : null;
		
		Usuario loggedUser = SessionUtils.getUsuarioLogado();
		
		//Se não for informado idFilialOrigem pegar as filiais que o usuário logado tem acesso.
		List listFiliaisOrigem = new ArrayList();
		if(idFilialOrigem == null)
		{
			listFiliaisOrigem = SessionUtils.getFiliaisUsuarioLogado();
		}else{
			Filial filial = new Filial();
			filial.setIdFilial(idFilialOrigem);
			listFiliaisOrigem.add(filial);
		}
		
		String sql = montaSql(listFiliaisOrigem, idFilialOrigem, idFilialDestino, dataInicial, dataFinal);
		YearMonthDay dataInicialYMD = JTDateTimeUtils.convertDataStringToYearMonthDay(dataInicialFormatada.replace("/",""));
		YearMonthDay dataFinalYMD = JTDateTimeUtils.convertDataStringToYearMonthDay(dataFinalFormatada.replace("/",""));
		JRReportDataObject jr = executeQuery(sql, new YearMonthDay[]{dataInicialYMD, dataFinalYMD});

		Map reportParameters = montaParametersReport(parameters, loggedUser, idFilialOrigem, idFilialDestino, dataInicialFormatada, dataFinalFormatada);
		
		jr.setParameters(reportParameters);
		return jr;
	}
	
	/**
	 * Metodo que monta os parameters do relatorio.
	 * 
	 * @param parameters
	 * @param loggedUser
	 * @param dataFormatada
	 * @return
	 */
	private Map montaParametersReport(Map parameters, Usuario loggedUser, Long idFilialOrigem, Long idFilialDestino, String dataInicialFormatada, String dataFinalFormatada)
	{		 
		
		String filialOrigem = "", filialDestino = "";
		
		if(idFilialOrigem != null)
		{
			String consFanFilial = "select f.sg_filial, p.nm_fantasia from pessoa p, filial f " +
					"where f.id_filial = p.id_pessoa and f.id_filial = " + idFilialOrigem;
			List listFilial = getJdbcTemplate().queryForList(consFanFilial);
			if(listFilial.size() > 0)
			{
				Map map = (Map)listFilial.get(0);
				String siglaFilialOrigem = map.get("SG_FILIAL").toString();
				String nomeFilialOrigem = " - " + map.get("NM_FANTASIA");
				filialOrigem = siglaFilialOrigem + nomeFilialOrigem;
			}
		}

		if(idFilialDestino != null)
		{
			String consFanFilial = "select f.sg_filial, p.nm_fantasia from pessoa p, filial f " +
					"where f.id_filial = p.id_pessoa and f.id_filial = " + idFilialDestino;
			List listFilial = getJdbcTemplate().queryForList(consFanFilial);
			if(listFilial.size() > 0)
			{
				Map map = (Map)listFilial.get(0);
				String siglaFilialDestino = map.get("SG_FILIAL").toString();
				String nomeFilialDestino = " - " + map.get("NM_FANTASIA");
				filialDestino = siglaFilialDestino + nomeFilialDestino;
			}
		}
		
		StringBuffer pesquisa = new StringBuffer("");
		
		if(idFilialOrigem != null){
			adicionaParametroPesquisa(pesquisa, filialOrigem, configuracoesFacade.getMensagem("filialOrigem2"));
		}
		if(idFilialDestino != null){
			adicionaParametroPesquisa(pesquisa, filialDestino, configuracoesFacade.getMensagem("filialDestino2"));
		}
		if(StringUtils.isNotBlank(dataInicialFormatada)){
			adicionaParametroPesquisa(pesquisa, dataInicialFormatada, configuracoesFacade.getMensagem("dataEmissaoManifestoInicial"));
		}
		if(StringUtils.isNotBlank(dataFinalFormatada)){
			adicionaParametroPesquisa(pesquisa, dataFinalFormatada, configuracoesFacade.getMensagem("dataEmissaoManifestoFinal"));
		}
		Map par = new HashMap();
		String paramPesquisa = (pesquisa.toString().equals("")==true)? configuracoesFacade.getMensagem("reportSemParametros") : pesquisa.toString();
		par.put("parametrosPesquisa", paramPesquisa);	
		par.put("SERVICE",this);
		par.put("usuarioEmissor", loggedUser.getNmUsuario());
		
		return par;
	}
	
	/**
	 * 
	 * @param idRegional
	 * @param idFilial
	 * @param data
	 * @return
	 */
	private static String montaSql(List listFiliaisOrigem, Long idFilialOrigem , Long idFilialDestino, String dataInicial, String dataFinal){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ")
		.append(" FO.SG_FILIAL||' - '||PO.NM_FANTASIA as FILIALORIGEM, ")
		.append(" FD.SG_FILIAL||' - '||PD.NM_FANTASIA as FILIALDESTINO, ")
		.append(" C.ID_FILIAL_ORIGEM, D.ID_FILIAL_DESTINO, ")
		.append(" SUM(D.PS_REAL) as PESO, ")
		.append(" COUNT(*) as CONHECIMENTO ")
		
		.append(" FROM CONHECIMENTO C, ")
		.append(" DOCTO_SERVICO D, ")
		.append(" FILIAL FO, ")
		.append(" PESSOA PO, ")
		.append(" FILIAL FD, ")
		.append(" PESSOA PD, ")
		.append(" EVENTO_DOCUMENTO_SERVICO EDS, ")
		.append(" EVENTO E")
		
		.append(" WHERE D.ID_DOCTO_SERVICO 		= C.ID_CONHECIMENTO ")
		.append(" AND D.ID_FILIAL_DESTINO 		= FD.ID_FILIAL")
		.append(" AND FD.ID_FILIAL              = PD.ID_PESSOA")
		.append(" AND C.ID_FILIAL_ORIGEM 		= FO.ID_FILIAL")
		.append(" AND FO.ID_FILIAL              = PO.ID_PESSOA")
		.append(" AND C.ID_CONHECIMENTO			= EDS.ID_DOCTO_SERVICO")
		.append(" AND C.ID_FILIAL_ORIGEM		= EDS.ID_FILIAL")
		.append(" AND EDS.BL_EVENTO_CANCELADO	<> 'N'")
		.append(" AND EDS.ID_EVENTO				= E.ID_EVENTO")
		.append(" AND E.CD_EVENTO				= ").append(ConstantesSim.EVENTO_MANIFESTO_EMITIDO);

		if(listFiliaisOrigem != null && listFiliaisOrigem.size()>0)
		{
			String inList = "(";
			for(Iterator i = listFiliaisOrigem.iterator(); i.hasNext();)
			{
				Filial filial = (Filial)i.next();
				inList += filial.getIdFilial();
				if(i.hasNext()) inList += ",";
			}
			inList += ")";
			
			sql.append(" AND FO.ID_FILIAL in ").append(inList);
		}
		
		if(idFilialDestino != null)
		{
			sql.append(" AND FD.ID_FILIAL = ").append(idFilialDestino);
		}
		
		if(dataInicial != null && dataFinal != null && !dataInicial.equals("") && !dataFinal.equals(""))
		{
			sql.append(" AND (trunc(cast(eds.dh_evento as date)) >= ?")
			.append( " AND trunc(cast(eds.dh_evento as date)) <= ? )");
		}
		
		sql.append(" GROUP BY C.ID_FILIAL_ORIGEM, D.ID_FILIAL_DESTINO, ")
		.append(" FO.SG_FILIAL, PO.NM_FANTASIA, FD.SG_FILIAL, PD.NM_FANTASIA ")
		
		.append(" ORDER BY FO.SG_FILIAL, FD.SG_FILIAL");
		
		
		return sql.toString();
	}
	
	/**
	 * 
	 * @param sb
	 * @param filtro
	 * @param label
	 */
	private void adicionaParametroPesquisa(StringBuffer sb, String filtro, String label){
		if(sb.length() > 0){
			sb.append(" | ").append(label).append(": ").append(filtro);
		}else{
			sb.append(label).append(": ").append(filtro);
		}
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
