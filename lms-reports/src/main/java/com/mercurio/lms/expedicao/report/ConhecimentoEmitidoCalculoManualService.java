package com.mercurio.lms.expedicao.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Rodrigo F. Dias
 *
 * @spring.bean id="lms.expedicao.conhecimentoEmitidoCalculoManualService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/conhecimentosEmitidosCalculoManual.jasper"
 */
public class ConhecimentoEmitidoCalculoManualService extends ReportServiceSupport {
	private ConfiguracoesFacade configuracoesFacade;

	public JRReportDataObject execute(Map parameters) throws Exception {
		Long idFilial = MapUtilsPlus.getLongOnMap(parameters, "filial", "idFilial", null);
		String dataInicial = MapUtils.getString(parameters,"dataInicial");
		String dataFinal = MapUtils.getString(parameters,"dataFinal");

		//Se não for informado idFilialOrigem pegar as filiais que o usuário logado tem acesso.
		List listFiliaisOrigem = new ArrayList();
		if(idFilial == null) {
			listFiliaisOrigem = SessionUtils.getFiliaisUsuarioLogado();
		} else {
			Filial filial = new Filial();
			filial.setIdFilial(idFilial);
			listFiliaisOrigem.add(filial);
		}
		String sql = montaSql(parameters, listFiliaisOrigem);

		JRReportDataObject jr = executeQuery(sql, new String[]{dataInicial, dataFinal});

		Map reportParameters = montaParametersReport(parameters);
		jr.setParameters(reportParameters);
		return jr;
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	private String montaSql(Map parameters, List listFiliaisOrigem) {
		Long idCliente = MapUtilsPlus.getLongOnMap(parameters, "cliente", "idCliente", null);
		String valModal = MapUtilsPlus.getStringOnMap(parameters, "modal", "valor", null);
		String valAbrangencia = MapUtilsPlus.getStringOnMap(parameters, "abrangencia", "valor", null);		
		Long idServico = MapUtilsPlus.getLongOnMap(parameters, "servico", "idServico", null);
		String dataInicial = MapUtils.getString(parameters, "dataInicial");
		String dataFinal = MapUtils.getString(parameters, "dataFinal");

		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT fil.SG_FILIAL as SG_FILIAL, ")
		.append(" con.NR_CONHECIMENTO as NR_CONHECIMENTO, ")
		.append(" con.DV_CONHECIMENTO as DV_CONHECIMENTO, ")
		.append(" con.TP_DOCUMENTO_SERVICO as TP_DOCUMENTO_SERVICO, ")
		.append(" trunc(cast(ds.DH_EMISSAO as date)) as DATAEMISSAOINICIAL, ")
		.append(" perem.NM_PESSOA as REMETENTE, ")
		.append(" pedes.NM_PESSOA as DESTINATARIO, ");
		String selectDominio = "(select vdom.DS_VALOR_DOMINIO_I from dominio dom, valor_dominio vdom  " +
				"where dom.id_dominio = vdom.id_dominio " +
				"and dom.NM_DOMINIO = 'DM_TIPO_FRETE' " +
				"and vl_valor_dominio = con.TP_FRETE) ";
		sql.append(PropertyVarcharI18nProjection.createProjection(selectDominio,"TIPOFRETE")).append(",")
		.append(" ds.PS_REAL as PESOMERCADORIA, ")
		.append(" ds.VL_MERCADORIA as VALORMERCADORIA, ")
		.append(" ds.QT_VOLUMES as QTDVOLUMES, ")
		.append(" ds.VL_TOTAL_DOC_SERVICO as VALORFRETE, ")
		.append(" fil.SG_FILIAL as FILIALLIBERADORA, ")
		.append(" moe.DS_SIMBOLO, ")

		.append(" (SELECT nm_usuario ")
		.append(" FROM liberacao_doc_serv lds, ")
		.append(" usuario u ")
		.append(" WHERE lds.tp_bloqueio_liberado = 'CM' ")
		.append(" AND u.id_usuario = lds.id_usuario ")
		.append(" AND lds.id_docto_servico = con.id_conhecimento) as RESPLIBERACAO ")

		.append(" FROM DOCTO_SERVICO ds, ")
		.append(" CONHECIMENTO con, ")
		.append(" MOEDA moe, ")
		.append(" PESSOA perem, ")
		.append(" PESSOA pedes, ")
		.append(" SERVICO ser, ")
		.append(" FILIAL fil ")

		.append(" WHERE ")
		.append(" ds.ID_DOCTO_SERVICO = con.ID_CONHECIMENTO ")
		.append(" and ds.ID_MOEDA = moe.ID_MOEDA ")
		.append(" and ds.ID_CLIENTE_REMETENTE = perem.ID_PESSOA ")
		.append(" and ds.ID_CLIENTE_DESTINATARIO = pedes.ID_PESSOA ")
		.append(" and ds.ID_SERVICO = ser.ID_SERVICO ")
		.append(" and con.ID_FILIAL_ORIGEM = fil.ID_FILIAL ")
		.append(" and ds.TP_CALCULO_PRECO = 'M' ")
		.append(" and con.TP_SITUACAO_CONHECIMENTO = 'E' ");

		if(listFiliaisOrigem != null && listFiliaisOrigem.size() > 0) {
			String inList = "(";
			for(Iterator i = listFiliaisOrigem.iterator(); i.hasNext();) {
				Filial filial = (Filial)i.next();
				inList += filial.getIdFilial();
				if(i.hasNext()) inList += ",";
			}
			inList += ")";

			sql.append(" AND con.ID_FILIAL_ORIGEM in ").append(inList);
		}
		if(idCliente != null) {
			sql.append(" AND ds.ID_CLIENTE_REMETENTE = ").append(idCliente);
		}
		if(valModal != null) {
			sql.append(" AND ser.TP_MODAL = '").append(valModal).append("' ");
		}
		if(valAbrangencia != null) {
			sql.append(" AND ser.TP_ABRANGENCIA = '").append(valAbrangencia).append("' ");
		}		
		if(idServico != null) {
			sql.append(" AND ser.ID_SERVICO = ").append(idServico);
		}
		if(dataInicial != null && dataFinal != null) {
			sql.append(" AND ds.DH_EMISSAO >= to_date(?,'yyyy-mm-dd')  AND ds.DH_EMISSAO <= to_date(?,'yyyy-mm-dd') ");
		}
		sql.append(" ORDER BY con.TP_DOCUMENTO_SERVICO, fil.SG_FILIAL, con.NR_CONHECIMENTO, ds.DH_EMISSAO, perem.NM_PESSOA ");

		return sql.toString();
	}

	/**
	 * 
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	private Map montaParametersReport(Map parameters) throws Exception {
		StringBuffer pesquisa = new StringBuffer("");
		JdbcTemplate jdbcTemplate = getJdbcTemplate();

		Long idRegional = MapUtilsPlus.getLongOnMap(parameters, "regional", "idRegional", null);
		if(idRegional != null) {
			String dsRegional = MapUtilsPlus.getStringOnMap(parameters,"regional","siglaDescricao", null);
			adicionaParametroPesquisa(pesquisa, dsRegional, configuracoesFacade.getMensagem("regional"));
		}

		Long idFilial = MapUtilsPlus.getLongOnMap(parameters, "filial", "idFilial", null);
		if(idFilial != null) {
			Map mapFilial = MapUtils.getMap(parameters, "filial");
			String dsFilial = MapUtilsPlus.getStringOnMap(mapFilial, "pessoa","nmFantasia", "");
			
			Map mapResFilial = jdbcTemplate.queryForMap("select SG_FILIAL from filial where id_filial = ?", new Long[]{idFilial});
			String sgFilial = (mapResFilial.get("SG_FILIAL") == null)? "" : (String)mapResFilial.get("SG_FILIAL") + " - ";
			adicionaParametroPesquisa(pesquisa, (sgFilial + dsFilial), configuracoesFacade.getMensagem("filial"));
		}

		String valModal = MapUtilsPlus.getStringOnMap(parameters, "modal", "valor", null);
		if(valModal != null) {
			String dsModal = MapUtilsPlus.getStringOnMap(parameters, "modal", "descricao", null);
			adicionaParametroPesquisa(pesquisa, dsModal, configuracoesFacade.getMensagem("modal"));
		}

		String valAbrangencia = MapUtilsPlus.getStringOnMap(parameters, "abrangencia", "valor", null);
		if(valAbrangencia != null) {
			String dsAbrangencia = MapUtilsPlus.getStringOnMap(parameters, "abrangencia", "descricao", null);
			adicionaParametroPesquisa(pesquisa, dsAbrangencia, configuracoesFacade.getMensagem("abrangencia"));
		}

		Long idServico = MapUtilsPlus.getLongOnMap(parameters, "servico", "idServico", null);
		if(idServico != null) {
			String dsServico = MapUtilsPlus.getStringOnMap(parameters, "servico", "dsServico", null);
			adicionaParametroPesquisa(pesquisa, dsServico, configuracoesFacade.getMensagem("servico"));
		}

		String dataInicial = MapUtils.getString(parameters, "dataInicial");
		if(dataInicial != null) {
			String dataInicialFormatada = !StringUtils.isBlank(dataInicial) ? JTDateTimeUtils.convertFrameworkDateToFormat(dataInicial,"dd/MM/yyyy") : null;
			adicionaParametroPesquisa(pesquisa, dataInicialFormatada, configuracoesFacade.getMensagem("periodoEmissaoInicial"));
		}

		String dataFinal = MapUtils.getString(parameters, "dataFinal");
		if(dataFinal != null) {
			String dataFinalFormatada = !StringUtils.isBlank(dataFinal) ? JTDateTimeUtils.convertFrameworkDateToFormat(dataFinal,"dd/MM/yyyy") : null;
			adicionaParametroPesquisa(pesquisa, dataFinalFormatada, configuracoesFacade.getMensagem("periodoEmissaoFinal"));
		}

		Long idCliente = MapUtilsPlus.getLongOnMap(parameters, "cliente", "idCliente", null);
		if(idCliente != null) {
			Map mapResCliente = jdbcTemplate.queryForMap("select TP_IDENTIFICACAO, NR_IDENTIFICACAO, NM_PESSOA from pessoa where id_pessoa = ?", new Long[]{idCliente});

			String dsCliente = (mapResCliente.get("NM_PESSOA") == null)? "" :(String)mapResCliente.get("NM_PESSOA");
			String tpIdentificacao = (mapResCliente.get("TP_IDENTIFICACAO") == null)? "" :(String)mapResCliente.get("TP_IDENTIFICACAO");
			String nrIdentificacao = (mapResCliente.get("NR_IDENTIFICACAO") == null)? "" :(String)mapResCliente.get("NR_IDENTIFICACAO");
			nrIdentificacao = FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao) + " - ";

			adicionaParametroPesquisa(pesquisa, (nrIdentificacao + dsCliente), configuracoesFacade.getMensagem("cliente"));
		}

		Map par = new HashMap();
		String paramPesquisa = (pesquisa.toString().equals("")==true)? configuracoesFacade.getMensagem("reportSemParametros") : pesquisa.toString();
		par.put("parametrosPesquisa", paramPesquisa);	
		par.put("SERVICE",this);

		Usuario loggedUser = SessionUtils.getUsuarioLogado();
		par.put("usuarioEmissor", loggedUser.getNmUsuario());

		return par;
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
		} else {
			sb.append(label).append(": ").append(filtro);
		}
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
