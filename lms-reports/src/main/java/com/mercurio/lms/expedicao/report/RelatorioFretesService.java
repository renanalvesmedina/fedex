package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.util.ListUtilsPlus;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Diego Pacheco - GT5 - LMS
 *
 * @spring.bean id="lms.expedicao.RelatorioFretesService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/relatorioFretesDigitador.jasper"
 */
public class RelatorioFretesService extends ReportServiceSupport {
	private static final int NUMBER_HUNDRED = 100;
	private static final double NUMBER_HUNDRED_DOUBLE = 100.0;
	private ConhecimentoCanceladoService conhecimentoCanceladoService;	

	public JRReportDataObject execute(Map parameters) throws Exception { 
		SqlTemplate sqlTotRegistros = createSqlTemplate();
		SqlTemplate sql = montaSQL(parameters,sqlTotRegistros);
		Integer totRegistros = getJdbcTemplate().queryForInt(sqlTotRegistros.getSql(),sqlTotRegistros.getCriteria());

		List newSource = new ArrayList();
		List percents = new ArrayList();
		Moeda moeda = SessionUtils.getMoedaSessao();

		sql = montaSQL(parameters,sqlTotRegistros);
		List result = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
		if (result!=null && result.size()!=0) {
			ListIterator lit = result.listIterator();
			while(lit.hasNext()) {
				Map mapAux = (Map)lit.next();
				Map newDataMap = new HashMap();				

				newDataMap.put("VALOR_ICMS",mapAux.get("VALOR_ICMS"));
				newDataMap.put("VALOR_FRETE",mapAux.get("VALOR_FRETE"));
				newDataMap.put("PESO_MERCADORIA",mapAux.get("PESO_MERCADORIA"));
				newDataMap.put("VALOR_MERCADORIA",mapAux.get("VALOR_MERCADORIA"));
				newDataMap.put("QTDE_CONHECIMENTOS",mapAux.get("QTDE_CONHECIMENTOS"));
				newDataMap.put("DS_SIMBOLO", moeda.getDsSimbolo());
				newDataMap.put("TIPOAGRUPAMENTO", mapAux.get("TIPOAGRUPAMENTO"));

				String tipoAgrupamento = MapUtilsPlus.getString(parameters,"tpAgrupamento",null);
				if("CL".equals(tipoAgrupamento)) {
					String tipoIdent = (String)mapAux.get("TP_IDENTIFICACAO");
					String nroIdent  = (String)mapAux.get("NR_IDENTIFICACAO");
					String identificacaoFormatada = FormatUtils.formatIdentificacao(tipoIdent, nroIdent);
					if(identificacaoFormatada != null) {
						newDataMap.put("TIPOAGRUPAMENTO", identificacaoFormatada + " - " + mapAux.get("TIPOAGRUPAMENTO"));
					} else {
						newDataMap.put("TIPOAGRUPAMENTO", mapAux.get("TIPOAGRUPAMENTO"));
					}
				}

				BigDecimal dAux = new BigDecimal( (java.lang.Math.floor( 
												  ( ( ((java.math.BigDecimal)mapAux.get("QTDE_CONHECIMENTOS")).intValue() * NUMBER_HUNDRED_DOUBLE )
														  / totRegistros.intValue() ) * NUMBER_HUNDRED ) / NUMBER_HUNDRED ));				
				percents.add(dAux);
				newDataMap.put("PERC_CONH",dAux);				
				newSource.add(newDataMap);				
			}

			BigDecimal sum = ListUtilsPlus.sumBigDecimal(percents);
			if ( (sum!=null) && (sum.doubleValue()!=0) && (sum.doubleValue()<NUMBER_HUNDRED_DOUBLE)) {
				double diff = NUMBER_HUNDRED_DOUBLE - sum.doubleValue();
				BigDecimal valor = ((BigDecimal)percents.get(percents.size()-1));
				((Map)newSource.get(newSource.size()-1)).put("PERC_CONH",new BigDecimal(valor.doubleValue()+diff));	
			}
		}

		sql.addFilterSummary("filialOrigem",getDsFilial(MapUtilsPlus.getLong(parameters,"filialByIdFilialOrigem.idFilial",null)));
		sql.addFilterSummary("filialDestino",getDsFilial(MapUtilsPlus.getLong(parameters,"filialByIdFilialDestino.idFilial",null)));
		sql.addFilterSummary("digitador",getNmUsuario(MapUtilsPlus.getLong(parameters,"usuario.idUsuario",null)));		
		sql.addFilterSummary("cliente",getNmCliente(MapUtilsPlus.getLong(parameters,"cliente.idCliente",null)));		
		sql.addFilterSummary("periodoEmissaoInicial",formatDate(MapUtilsPlus.getObject(parameters,"dtInicial",null)));
		sql.addFilterSummary("periodoEmissaoFinal",formatDate(MapUtilsPlus.getObject(parameters,"dtFinal",null)));
		sql.addFilterSummary("tipoFrete",getDsDominio("DM_TIPO_FRETE", MapUtilsPlus.getString(parameters,"tpFrete")));
		sql.addFilterSummary("tipoConhecimento",getDsDominio("DM_TIPO_CONHECIMENTO", MapUtilsPlus.getString(parameters,"tpConhecimento")));
		sql.addFilterSummary("tpIndicadorEDI",getDsDominio("DM_TP_INDICADOR_EDI", MapUtilsPlus.getString(parameters,"tpIndicador")));
		sql.addFilterSummary("modal",getDsDominio("DM_MODAL", MapUtilsPlus.getString(parameters,"modal")));
		sql.addFilterSummary("abrangencia",getDsDominio("DM_ABRANGENCIA", MapUtilsPlus.getString(parameters,"tpAbrangencia")));
		String dsTipoAgrupamento = getDsDominio("DM_TP_AGRUPAMENTO", MapUtilsPlus.getString(parameters,"tpAgrupamento"));
		sql.addFilterSummary("tipoAgrupamento",dsTipoAgrupamento);

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("ROTULO_COLUNA", dsTipoAgrupamento);
		parametersReport.put("TOT_CONHECIMENTOS", totRegistros);
		parametersReport.put("DS_SIMBOLO", moeda.getDsSimbolo());

		JRReportDataObject jr = createReportDataObject(new JRMapCollectionDataSource(newSource),parametersReport);
		jr.setParameters(parametersReport);

		return jr;
	}

	private String getDsDominio(String dominio, String value) {	
		List ret = getJdbcTemplate().queryForList("SELECT "+PropertyVarcharI18nProjection.createProjection("vd.DS_VALOR_DOMINIO_I", "DS_VALOR_DOMINIO")+" FROM valor_dominio vd, dominio d WHERE d.id_dominio = vd.id_dominio and d.NM_DOMINIO = '" + dominio + "'  and vd.VL_VALOR_DOMINIO = '" + value + "'");
		if (ret==null || ret.isEmpty() ) {
			return null;
		}
		return ((String)((Map)ret.get(0)).get("DS_VALOR_DOMINIO"));
	}

	private String formatDate(Object date) throws Exception	{
		if (date == null || "".equals(date)) {
			return null;
		}
		java.text.SimpleDateFormat dtShort = new java.text.SimpleDateFormat("yyyy-mm-dd");	   
		java.text.SimpleDateFormat dtBR    = new SimpleDateFormat("dd/mm/yyyy");		   
		return dtBR.format(dtShort.parse((String)date));		   
	}

	private String getNmUsuario(Long idUsu)	{	
		if (idUsu==null) {
			return null;
		}
		List ret = getJdbcTemplate().queryForList("SELECT nm_usuario FROM usuario WHERE id_usuario = " + idUsu);
		if (ret==null || ret.isEmpty() ) { 
			return null;
		}
		return ((String)((Map)ret.get(0)).get("nm_usuario"));
	}	

	private String getNmCliente(Long idCli)	{
		List ret = getJdbcTemplate().queryForList(" SELECT nm_pessoa FROM pessoa WHERE id_pessoa = " + idCli);
		if (ret==null || ret.isEmpty() ) {
			return null;
		}
		return ((String)((Map)ret.get(0)).get("nm_pessoa"));
	}

	private String getDsFilial(Long idFilial) {
		List ret = getJdbcTemplate().queryForList(" SELECT SG_FILIAL || ' - ' ||  NM_FANTASIA as header FROM filial, pessoa  WHERE pessoa.id_pessoa = filial.id_filial and id_filial = " + idFilial);
		if (ret==null || ret.isEmpty() ) {
			return "";
		}
		return (String)((Map)ret.get(0)).get("header");
	}

	/**
	 * 
	 * @param parameters
	 * @param sqlTotRegistros
	 * @param othersFields
	 * @return
	 */
	private SqlTemplate montaSQL(Map parameters, SqlTemplate sqlTotRegistros)	{
		SqlTemplate sql = createSqlTemplate();
	    
		sql.addProjection(" sum(doc.vl_imposto) as valor_icms, " + 
						  " sum(doc.vl_total_doc_servico) as valor_frete, " +
						  " sum(doc.ps_real) as peso_mercadoria, " +
						  " sum(doc.VL_MERCADORIA) as valor_mercadoria, " +
						  " count(*) as qtde_conhecimentos ");		
		sqlTotRegistros.addProjection(" count(*) ");
		
		sql.addFrom(" DOCTO_SERVICO DOC, CONHECIMENTO CON, DEVEDOR_DOC_SERV DDS ");
		sqlTotRegistros.addFrom(" DOCTO_SERVICO DOC, CONHECIMENTO CON, DEVEDOR_DOC_SERV DDS ");
	
		sql.addCustomCriteria(" DOC.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO ");
		sql.addCustomCriteria(" CON.TP_SITUACAO_CONHECIMENTO = 'E' "); 
		sql.addCustomCriteria(" DDS.ID_DOCTO_SERVICO = DOC.ID_DOCTO_SERVICO ");
		
		sqlTotRegistros.addCustomCriteria(" DOC.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO ");
		sqlTotRegistros.addCustomCriteria(" CON.TP_SITUACAO_CONHECIMENTO = 'E' "); 
		sqlTotRegistros.addCustomCriteria(" DDS.ID_DOCTO_SERVICO = DOC.ID_DOCTO_SERVICO ");
	      
		String tpAgrupamento = MapUtilsPlus.getString(parameters,"tpAgrupamento",null); 
		
		addWhereTpAgrupamento(tpAgrupamento, sql, sqlTotRegistros);	
		addWhereParameters(parameters, sql, sqlTotRegistros);	
		
		if (MapUtilsPlus.getString(parameters,"tpIndicador",null)!=null){
			sql.addCriteria("con.BL_INDICADOR_EDI","=",MapUtilsPlus.getString(parameters,"tpIndicador",null));
			sqlTotRegistros.addCriteria("con.BL_INDICADOR_EDI","=",MapUtilsPlus.getString(parameters,"tpIndicador",null));
		}
		
		if (MapUtilsPlus.getLong(parameters,"cliente.idCliente",null)!=null){
			sql.addCriteria("DDS.ID_CLIENTE","=",MapUtilsPlus.getLong(parameters,"cliente.idCliente",null));
			sqlTotRegistros.addCriteria("DDS.ID_CLIENTE","=",MapUtilsPlus.getLong(parameters,"cliente.idCliente",null));
		}
		
		if (MapUtilsPlus.getLong(parameters,"usuario.idUsuario",null)!=null){
			sql.addCriteria("doc.ID_USUARIO_INCLUSAO","=",MapUtilsPlus.getLong(parameters,"usuario.idUsuario",null));
			sqlTotRegistros.addCriteria("doc.ID_USUARIO_INCLUSAO","=",MapUtilsPlus.getLong(parameters,"usuario.idUsuario",null));
		}
		sql.addOrderBy("TIPOAGRUPAMENTO");
		return sql;
	}
	
	private void addWhereParameters(Map parameters,SqlTemplate sql,SqlTemplate sqlTotRegistros){
		
		if (MapUtilsPlus.getLong(parameters,"filialByIdFilialOrigem.idFilial",null)!=null) {
			sql.addCriteria(" DOC.ID_FILIAL_ORIGEM","=",MapUtilsPlus.getLong(parameters,"filialByIdFilialOrigem.idFilial",null));
			sqlTotRegistros.addCriteria(" DOC.ID_FILIAL_ORIGEM","=",MapUtilsPlus.getLong(parameters,"filialByIdFilialOrigem.idFilial",null));
		}else{
			sql.addCustomCriteria(" DOC.ID_FILIAL_ORIGEM IN (" +  conhecimentoCanceladoService.getSqlTodasFiliaisUserLogado() + ") ");
			sqlTotRegistros.addCustomCriteria(" DOC.ID_FILIAL_ORIGEM IN (" +  conhecimentoCanceladoService.getSqlTodasFiliaisUserLogado() + ") ");
		}

		if (MapUtilsPlus.getLong(parameters,"filialByIdFilialDestino.idFilial",null)!=null)	{
			sql.addCriteria(" DOC.ID_FILIAL_DESTINO","=",MapUtilsPlus.getLong(parameters,"filialByIdFilialDestino.idFilial",null));
			sqlTotRegistros.addCriteria(" DOC.ID_FILIAL_DESTINO","=",MapUtilsPlus.getLong(parameters,"filialByIdFilialDestino.idFilial",null));
		}
		
		if ( (MapUtilsPlus.getObject(parameters,"dtInicial",null)!=null) && (!( "".equals((String)MapUtilsPlus.getObject(parameters,"dtInicial",null)))) ){
			sql.addCustomCriteria(" doc.DH_EMISSAO >= to_timestamp_tz('" + MapUtilsPlus.getObject(parameters,"dtInicial",null) + " 00:00:00,000000000 -03:00','RR/MM/DD HH24:MI:SS,FF TZR')");
			sqlTotRegistros.addCustomCriteria(" doc.DH_EMISSAO >= to_timestamp_tz('" + MapUtilsPlus.getObject(parameters,"dtInicial",null) + " 00:00:00,000000000 -03:00','RR/MM/DD HH24:MI:SS,FF TZR')");
		} 
	
		if ( (MapUtilsPlus.getObject(parameters,"dtFinal",null)!=null) && (!( "".equals((String)MapUtilsPlus.getObject(parameters,"dtFinal",null)))) ){
			sql.addCustomCriteria(" doc.DH_EMISSAO <= to_timestamp_tz('" + MapUtilsPlus.getObject(parameters,"dtFinal",null) + " 23:59:00,000000000 -03:00','RR/MM/DD HH24:MI:SS,FF TZR')");
			sqlTotRegistros.addCustomCriteria(" doc.DH_EMISSAO <= to_timestamp_tz('" + MapUtilsPlus.getObject(parameters,"dtFinal",null) + " 23:59:00,000000000 -03:00','RR/MM/DD HH24:MI:SS,FF TZR')");
		}
		
		if (MapUtilsPlus.getString(parameters,"tpFrete",null)!=null){
			sql.addCriteria("con.TP_FRETE","=",MapUtilsPlus.getString(parameters,"tpFrete",null));
			sqlTotRegistros.addCriteria("con.TP_FRETE","=",MapUtilsPlus.getString(parameters,"tpFrete",null));
		}
		
		if (MapUtilsPlus.getString(parameters,"tpConhecimento",null)!=null)	{
			sql.addCriteria("con.TP_CONHECIMENTO", "=", MapUtilsPlus.getString(parameters,"tpConhecimento",null));
			// regra adicionada devido a falha cadastrada pelo Eri para considerar documentos de NFT.
			sql.addCriteria("DOC.TP_DOCUMENTO_SERVICO", "=", ConstantesExpedicao.CONHECIMENTO_NACIONAL);
			sqlTotRegistros.addCriteria("con.TP_CONHECIMENTO", "=", MapUtilsPlus.getString(parameters,"tpConhecimento",null));
		}
		
		boolean modal = false;
		if (MapUtilsPlus.getString(parameters,"modal",null)!=null){
			sql.addFrom(" servico ss ");
			sql.addCustomCriteria(" doc.ID_SERVICO = ss.ID_SERVICO ");			
			sql.addCriteria("ss.TP_MODAL","=",MapUtilsPlus.getString(parameters,"modal",null));
			
			sqlTotRegistros.addFrom(" servico ss ");
			sqlTotRegistros.addCustomCriteria(" doc.ID_SERVICO = ss.ID_SERVICO ");			
			sqlTotRegistros.addCriteria("ss.TP_MODAL","=",MapUtilsPlus.getString(parameters,"modal",null));
			modal = true;
		}
		
		if (MapUtilsPlus.getString(parameters,"tpAbrangencia",null)!=null)	{
			if (!modal)	{
				sql.addFrom(" servico ss ");
				sql.addCustomCriteria(" doc.ID_SERVICO = ss.ID_SERVICO ");
				
				sqlTotRegistros.addFrom(" servico ss ");
				sqlTotRegistros.addCustomCriteria(" doc.ID_SERVICO = ss.ID_SERVICO ");
			}
			sql.addCriteria("ss.TP_ABRANGENCIA","=",MapUtilsPlus.getString(parameters,"tpAbrangencia",null));
			sqlTotRegistros.addCriteria("ss.TP_ABRANGENCIA","=",MapUtilsPlus.getString(parameters,"tpAbrangencia",null));
		}	 
		

		
	}
	
	private void addWhereTpAgrupamento(String tpAgrupamento, SqlTemplate sql, SqlTemplate sqlTotRegistros ){
	  if(tpAgrupamento!=null){	
		if("CL".equals(tpAgrupamento)) {
			//para posteriormente formatar a identificacao do cliente
			sql.addProjection(" p.tp_identificacao, p.nr_identificacao ");
			sql.addProjection(" p.NM_PESSOA as TIPOAGRUPAMENTO ");
			sql.addFrom(" pessoa p ");
			sql.addCustomCriteria("DDS.ID_CLIENTE = p.id_pessoa ");
			
			sqlTotRegistros.addFrom(" pessoa p ");
			sqlTotRegistros.addCustomCriteria("DDS.ID_CLIENTE = p.id_pessoa ");
			sql.addGroupBy(" p.tp_identificacao, p.nr_identificacao, p.NM_PESSOA ");
		}
		if ("FO".equals(tpAgrupamento))	{
			sql.addProjection(" f.SG_FILIAL || ' - ' || p.NM_FANTASIA as TIPOAGRUPAMENTO ");
			sql.addFrom(" filial f, pessoa p ");
			sql.addCustomCriteria(" doc.ID_FILIAL_ORIGEM = f.ID_FILIAL ");
			sql.addCustomCriteria(" f.ID_FILIAL = p.id_pessoa ");
			
			sqlTotRegistros.addFrom(" filial f, pessoa p ");
			sqlTotRegistros.addCustomCriteria(" doc.ID_FILIAL_ORIGEM = f.ID_FILIAL ");
			sqlTotRegistros.addCustomCriteria(" f.ID_FILIAL = p.id_pessoa ");
			sql.addGroupBy(" f.SG_FILIAL || ' - ' || p.NM_FANTASIA ");
		}
		if ("FD".equals(tpAgrupamento))	{
			sql.addProjection(" f.SG_FILIAL || ' - ' || p.NM_FANTASIA as TIPOAGRUPAMENTO ");
			sql.addFrom(" filial f, pessoa p ");
			sql.addCustomCriteria(" doc.ID_FILIAL_DESTINO = f.ID_FILIAL ");
			sql.addCustomCriteria(" f.ID_FILIAL = p.id_pessoa ");
			
			sqlTotRegistros.addFrom(" filial f, pessoa p ");
			sqlTotRegistros.addCustomCriteria(" doc.ID_FILIAL_DESTINO = f.ID_FILIAL ");
			sqlTotRegistros.addCustomCriteria(" f.ID_FILIAL = p.id_pessoa ");
			sql.addGroupBy(" f.SG_FILIAL || ' - ' || p.NM_FANTASIA ");
		}
		if ("DG".equals(tpAgrupamento))	{
			sql.addProjection(" u.NM_USUARIO as TIPOAGRUPAMENTO ");
			sql.addFrom(" usuario u ");
			sql.addCustomCriteria(" doc.ID_USUARIO_INCLUSAO = u.ID_USUARIO ");				
			
			sqlTotRegistros.addFrom(" usuario u ");
			sqlTotRegistros.addCustomCriteria(" doc.ID_USUARIO_INCLUSAO = u.ID_USUARIO ");
			sql.addGroupBy(" u.NM_USUARIO ");
		}
		if ("CK".equals(tpAgrupamento))	{
			sql.addProjection(" i.DS_CHECK_IN as TIPOAGRUPAMENTO ");
			sql.addFrom(" impressora i ");
			sql.addCustomCriteria(" doc.ID_IMPRESSORA = i.ID_IMPRESSORA ");				
			
			sqlTotRegistros.addFrom(" impressora i ");
			sqlTotRegistros.addCustomCriteria(" doc.ID_IMPRESSORA = i.ID_IMPRESSORA ");
			sql.addGroupBy(" i.DS_CHECK_IN ");
		}
	  }	
	}
	public void setConhecimentoCanceladoService(ConhecimentoCanceladoService conhecimentoCanceladoService) {
		this.conhecimentoCanceladoService = conhecimentoCanceladoService;
	}
}