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
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Rodrigo Dias
 *
 * @spring.bean id="lms.expedicao.resumoDiarioPesoEmbarcadoPorAWBService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/resumoDiarioPesoEmbarcadoAWB.jasper"
 */
public class ResumoDiarioPesoEmbarcadoPorAWBService extends ReportServiceSupport {
	
	private ConfiguracoesFacade configuracoesFacade;

	public JRReportDataObject execute(Map parameters) throws Exception {

		Map mapFilialOrigem	 	= MapUtils.getMap(parameters,"filialByIdFilialOrigem");
		Long idFilialOrigem  	= MapUtils.getLong(mapFilialOrigem,"idFilial");
		Map mapFilialDestino 	= MapUtils.getMap(parameters,"filialByIdFilialDestino");
		Long idFilialDestino 	= MapUtils.getLong(mapFilialDestino,"idFilial");
		Map mapFilialCia 		= MapUtils.getMap(parameters,"ciaFilialMercurio");
		Long idFilialCia 		= MapUtils.getLong(mapFilialCia,"idCiaFilialMercurio");

		String dataInicial 	 = MapUtils.getString(parameters,"dataInicial");
		String dataFinal 	 = MapUtils.getString(parameters,"dataFinal");
		String dataInicialFormatada = !StringUtils.isBlank(dataInicial) ? JTDateTimeUtils.convertFrameworkDateToFormat(dataInicial,"dd/MM/yyyy") : null;
		String dataFinalFormatada   = !StringUtils.isBlank(dataFinal)   ? JTDateTimeUtils.convertFrameworkDateToFormat(dataFinal,"dd/MM/yyyy") : null;
		YearMonthDay dataInicialYMD = !StringUtils.isBlank(dataInicial) ? JTDateTimeUtils.convertDataStringToYearMonthDay(dataInicialFormatada.replace("/","")) : null;
		YearMonthDay dataFinalYMD   = !StringUtils.isBlank(dataFinal)   ? JTDateTimeUtils.convertDataStringToYearMonthDay(dataFinalFormatada.replace("/","")) : null;
		
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
		
		String sql = montaSql(listFiliaisOrigem, idFilialCia, dataInicial, dataFinal, idFilialDestino);
		JRReportDataObject jr = executeQuery(sql, new YearMonthDay[]{dataInicialYMD, dataFinalYMD});

		Map reportParameters = montaParametersReport(parameters, loggedUser, idFilialOrigem, idFilialDestino, idFilialCia, dataInicialFormatada, dataFinalFormatada);
		
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
	private Map montaParametersReport(Map parameters, Usuario loggedUser, Long idFilialOrigem, Long idFilialDestino, Long idFilialCia, String dataInicialFormatada, String dataFinalFormatada)
	{		 
		
		String filialOrigem = "", filialDestino = "", filialCia = "";
		
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
		
		if(idFilialCia != null)
		{
			String consFanFilial = "select cia.id_empresa,pcia.NM_PESSOA " +
				" from PESSOA pcia, CIA_FILIAL_MERCURIO cia " +
				" where cia.id_empresa = pcia.id_pessoa and cia.id_empresa = " + idFilialCia;
			List listFilial = getJdbcTemplate().queryForList(consFanFilial);
			if(listFilial.size() > 0)
			{
				Map map = (Map)listFilial.get(0);
				filialCia = map.get("NM_PESSOA").toString();
			}
		}
		
		StringBuffer pesquisa = new StringBuffer("");
		
		if(idFilialOrigem != null){
			adicionaParametroPesquisa(pesquisa, filialOrigem, configuracoesFacade.getMensagem("filialOrigem2"));
		}
		if(idFilialDestino != null){
			adicionaParametroPesquisa(pesquisa, filialDestino, configuracoesFacade.getMensagem("filialDestino"));
		}
		if(idFilialCia != null){
			adicionaParametroPesquisa(pesquisa, filialCia, configuracoesFacade.getMensagem("ciaAerea"));
		}
		if(StringUtils.isNotBlank(dataInicialFormatada)){
			adicionaParametroPesquisa(pesquisa, dataInicialFormatada, configuracoesFacade.getMensagem("periodoEmissaoAWBinicial"));
		}
		if(StringUtils.isNotBlank(dataFinalFormatada)){
			adicionaParametroPesquisa(pesquisa, dataFinalFormatada, configuracoesFacade.getMensagem("periodoEmissaoAWBfinal"));
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
	private static String montaSql(List listFiliaisOrigem, Long idEmpresaCia, String dataInicial, String dataFinal, Long idFilialDestino){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT F.ID_FILIAL as IDFILIAL, ")
        .append(" F.SG_FILIAL||' - '||P.NM_FANTASIA as FILIALORIGEM, ")
        .append(" FDestino.SG_FILIAL||' - '||PDestino.NM_FANTASIA as FILIALDESTINO, ")
        .append(" PCIA.ID_PESSOA as IDCIAFILIAL, ")
		.append(" PCIA.NM_PESSOA as CIAAEREA, ")
		.append(" MOEDA.DS_SIMBOLO, ")
        .append(" COALESCE(SUM(AWB.PS_TOTAL),0) as PESO, ")
        .append(" COALESCE(SUM (AWB.PS_CUBADO),0) as PESOCUBADO, ")
        .append(" COALESCE(SUM(AWB.VL_FRETE),0) as VALORAWB, ")
        .append(" COUNT(*) as QTDEAWB ") 
        
        .append(" FROM AWB, FILIAL F, FILIAL FDestino, PESSOA P, PESSOA PDestino, CIA_FILIAL_MERCURIO CIA, PESSOA PCIA, MOEDA ")
        
        .append(" WHERE AWB.TP_STATUS_AWB = 'E'")
        .append(" AND AWB.ID_FILIAL_ORIGEM = F.ID_FILIAL")
        .append(" AND F.ID_FILIAL = P.ID_PESSOA ")
        .append(" AND AWB.ID_FILIAL_Destino = FDestino.ID_FILIAL")
        .append(" AND FDestino.ID_FILIAL = PDestino.ID_PESSOA ")
        .append(" AND AWB.ID_CIA_FILIAL_MERCURIO = CIA.ID_CIA_FILIAL_MERCURIO ")
        .append(" AND CIA.ID_EMPRESA = PCIA.ID_PESSOA ")
        .append(" AND AWB.ID_MOEDA = MOEDA.ID_MOEDA ");
        
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
			
			sql.append(" AND AWB.ID_FILIAL_ORIGEM  in ").append(inList);
		}
		
		if(idFilialDestino != null){
			sql.append(" AND AWB.ID_FILIAL_DESTINO  = "+ idFilialDestino );
		}
		
		if(idEmpresaCia != null)
		{
			sql.append(" AND CIA.ID_EMPRESA = ").append(idEmpresaCia);
		}
		
		if(dataInicial != null && dataFinal != null && !dataInicial.equals("") && !dataFinal.equals(""))
		{
			sql.append(" AND (trunc(cast(AWB.DH_EMISSAO as date)) >= ?")
			.append( " AND trunc(cast(AWB.DH_EMISSAO as date)) <= ? )");
		}
		
        sql.append(" GROUP BY F.ID_FILIAL, F.SG_FILIAL, P.NM_FANTASIA, FDestino.ID_FILIAL, FDestino.SG_FILIAL, PDestino.NM_FANTASIA, PCIA.ID_PESSOA, PCIA.NM_PESSOA, MOEDA.DS_SIMBOLO ")
        .append(" ORDER BY F.ID_FILIAL, F.SG_FILIAL, P.NM_FANTASIA, PCIA.NM_PESSOA ");
		
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
