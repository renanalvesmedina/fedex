package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Alexandre Poletto, Rodrigo Feio Dias
 *
 * @spring.bean id="lms.vendas.clienteParametrizacaoForaLimiteService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/clientesParametrizacoesForaLimites.jasper"
 */
public class ClienteParametrizacaoForaLimiteService extends ReportServiceSupport {
	private static String filtroVigencia = " AND to_char(pc.dt_vigencia_inicial ,'yyyy-MM-dd') <= ? \n AND to_char(pc.dt_vigencia_final ,'yyyy-MM-dd') >= ? \n";
	private static final int ORIGEM = 0;
	private static final int DESTINO = 1;
	private static final String SEM_COMPLEMENTO = "SEM_COMPLEMENTO";
	private ConfiguracoesFacade configuracoesFacade;
	

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap criteria = new TypedFlatMap(parameters);
		Long idRegional = criteria.getLong("regional.idRegional");
		Long idFilial = criteria.getLong("filial.idFilial");
		String dataReferencia = criteria.getString("dataReferencia");

		/** Pelo menos uma Regional ou Filial devem ser informadas */
		if (idRegional == null && idFilial == null) {
			throw new BusinessException("LMS-30050");
		}

		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		String dataFormatada = !StringUtils.isBlank(dataReferencia) ? JTDateTimeUtils.convertFrameworkDateToFormat(dataReferencia,"dd/MM/yyyy") : null;
		Usuario loggedUser = SessionUtils.getUsuarioLogado();

		String sql = getSql(idRegional,idFilial,dataFormatada);
		List resultQuery = null;
		if(StringUtils.isNotBlank(dataReferencia)){
			YearMonthDay dataQuery = JTDateTimeUtils.convertDataStringToYearMonthDay(dataFormatada.replace("/",""));
			String strDataQuery  = dataQuery.toString();
			resultQuery = jdbcTemplate.queryForList(sql, new String[]{
				strDataQuery,strDataQuery,strDataQuery,strDataQuery,strDataQuery,
				strDataQuery,strDataQuery,strDataQuery,strDataQuery,strDataQuery,
				strDataQuery,strDataQuery,strDataQuery,strDataQuery,strDataQuery,
				strDataQuery,strDataQuery,strDataQuery,strDataQuery,strDataQuery,
				strDataQuery,strDataQuery,strDataQuery,strDataQuery,strDataQuery,
				strDataQuery,strDataQuery,strDataQuery,strDataQuery,strDataQuery,
				strDataQuery,strDataQuery});
		} else {
			resultQuery = jdbcTemplate.queryForList(sql);
		}
		processaDadosRelatorio(resultQuery);

		JRReportDataObject jr = createReportDataObject(new JRMapCollectionDataSource(resultQuery), criteria);

		Map parametersReport = montaParametersReport(criteria, loggedUser, dataFormatada);
		parametersReport.put("SERVICE", this);
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, criteria.get("tpFormatoRelatorio"));
		jr.setParameters(parametersReport);
		return jr;
	}
	
	/**
	 * Método chamado dentro do relatorio para formatar o campo IDENTIFICAÇÂO
	 * @param tpIdent
	 * @param ident
	 * @return
	 */
	public String formataIdent(String tpIdent, String ident){
		return FormatUtils.formatIdentificacao(tpIdent,ident);
	}
	
	/*
	 * METODOS PRIVADOS
	 */
	
	/**
	 * Metodo para controle do conteudo de origem e destino do relatorio
	 * @param result
	 */
	private List processaDadosRelatorio(List result) {
		if (result.isEmpty()) {
			return result;
		}

		//PROMOTOR
		StringBuilder sqlPromotor = new StringBuilder()
		.append("SELECT U.NM_USUARIO AS USUARIO FROM PROMOTOR_CLIENTE PRO, USUARIO u" )
		.append(" WHERE PRO.ID_USUARIO = U.ID_USUARIO ")
		.append(" AND PRO.TP_MODAL = ? AND PRO.TP_ABRANGENCIA = ? AND PRO.ID_CLIENTE = ? ORDER BY U.NM_USUARIO");
		
		String todoEstado = " - " + configuracoesFacade.getMensagem("todoEstado");
		String demaisLocalidades = " - " + configuracoesFacade.getMensagem("demaisLocalidades");
		
		Map<Long, Map<String, Integer>[]> ufsCliente = new HashMap<Long, Map<String,Integer>[]>();
		
		/*
		 * constrói mapRota, mapTemOrigem e mapTemDestino.
		 * Já alimenta a lista com o complemento da origem e destino se tiver.
		 */
		for(Iterator it = result.iterator(); it.hasNext();) {
			Map registro = (Map) it.next();
			Long idCliente = MapUtils.getLong(registro,"ID_CLIENTE");
			String tpModal = MapUtils.getString(registro,"TP_MODAL");
			String tpAbrangencia = MapUtils.getString(registro,"TP_ABRANGENCIA");
			
			processaRegistro(registro, ORIGEM, ufsCliente);
			processaRegistro(registro, DESTINO, ufsCliente);
			
			//PROMOTORES
			List promotores = getJdbcTemplate().queryForList(sqlPromotor.toString(), new Object[]{tpModal, tpAbrangencia, idCliente});
			if(promotores != null && !promotores.isEmpty()) {
				registro.put("PROMOTOR", createNomesPromotores(promotores));
			}
		}

		/*
		 * Ajusta lista com os valores 'Todo Estado' ou 'Demais Localidades' caso seja necessário.
		 */
		for(Iterator it = result.iterator(); it.hasNext();) {
			Map registro = (Map)it.next();
			String rgOrigem  = MapUtils.getString(registro,"ORIGEM");
			String rgDestino = MapUtils.getString(registro,"DESTINO");
			Long idCliente = MapUtils.getLong(registro,"ID_CLIENTE");
			String complementoOrigem = MapUtils.getString(registro, "COMPLEMENTO_ORIGEM");
			String complementoDestino = MapUtils.getString(registro, "COMPLEMENTO_DESTINO");
			
			
			if (StringUtils.isBlank(complementoOrigem)) {
				if (countUfs(idCliente, ORIGEM, rgOrigem, ufsCliente) > 0) {
					registro.put("COMPLEMENTO_ORIGEM", demaisLocalidades);
				} else {
					registro.put("COMPLEMENTO_ORIGEM", todoEstado);
				}
			} else if (SEM_COMPLEMENTO.equals(complementoOrigem)) {
				registro.put("COMPLEMENTO_ORIGEM", "");
			}
		
			if (StringUtils.isBlank(complementoDestino)) {
				if (countUfs(idCliente, DESTINO, rgDestino, ufsCliente) > 0) {
					registro.put("COMPLEMENTO_DESTINO", demaisLocalidades);
				} else {
					registro.put("COMPLEMENTO_DESTINO", todoEstado);
				}
			} else if (SEM_COMPLEMENTO.equals(complementoDestino)) {
				registro.put("COMPLEMENTO_DESTINO", "");
			}
		}
		
		return result;
	}
	
	/**
	 * Metodo para descobrir o complemento do registro
	 * @param registro
	 * @return
	 */
	private String findComplementoRegistro(Map registro, String origemDestino) {
		String sqlMunicipio = "SELECT M.NM_MUNICIPIO FROM MUNICIPIO M WHERE M.ID_MUNICIPIO = ?";
		String sqlAeroporto = "SELECT A.SG_AEROPORTO FROM AEROPORTO A WHERE ID_AEROPORTO = ?";
		
		StringBuilder sqlFilial = new StringBuilder()
		.append("SELECT F.SG_FILIAL, ")
		.append("       P.NM_FANTASIA ")
		.append("  FROM FILIAL F, ")
		.append("       PESSOA P ")
		.append(" WHERE F.ID_FILIAL = P.ID_PESSOA ")
		.append("   AND F.ID_FILIAL = ? ");
		
		StringBuilder sqlTipoLocMunicipio = new StringBuilder()
		.append(" SELECT ").append(PropertyVarcharI18nProjection.createProjection("T.DS_TIPO_LOCAL_MUNICIPIO_I", "DS_TIPO_LOCALIZACAO_MUNICIPIO"))
		.append(" FROM TIPO_LOCALIZACAO_MUNICIPIO T")
		.append(" WHERE T.ID_TIPO_LOCALIZACAO_MUNICIPIO = ?");

		Long idMunicipio = MapUtils.getLong(registro,"ID_MUNICIPIO_"+origemDestino);
		Long idFilial = MapUtils.getLong(registro,"ID_FILIAL_"+origemDestino);
		Long idTipoLocMunicipio = MapUtils.getLong(registro,"ID_TIPO_LOC_MUNICIPIO_"+origemDestino);
		Long idAeroporto = MapUtils.getLong(registro,"ID_AEROPORTO_"+origemDestino);
		
		if (idFilial == null) {
			if (idAeroporto != null) {
				List listaOrigemDestino = getJdbcTemplate().queryForList(sqlAeroporto, new Long[]{idAeroporto});
				Map mapOrigemDestino = listaOrigemDestino.isEmpty() ? new HashMap() : (Map) listaOrigemDestino.get(0);
				return MapUtils.getString(mapOrigemDestino, "SG_AEROPORTO");
			}
			
			if (idTipoLocMunicipio != null) {
				List listaOrigemDestino = getJdbcTemplate().queryForList(sqlTipoLocMunicipio.toString(), new Long[]{idTipoLocMunicipio});
				Map mapOrigemDestino = listaOrigemDestino.isEmpty() ? new HashMap() : (Map) listaOrigemDestino.get(0);
				return MapUtils.getString(mapOrigemDestino, "DS_TIPO_LOCALIZACAO_MUNICIPIO");
			}
		} else {
			List listaOrigemDestino = getJdbcTemplate().queryForList(sqlFilial.toString(), new Long[]{idFilial});
			Map mapOrigemDestino = listaOrigemDestino.isEmpty() ? new HashMap() : (Map) listaOrigemDestino.get(0);
			StringBuilder result = new StringBuilder()
			.append(MapUtils.getString(mapOrigemDestino, "SG_FILIAL"))
			.append(" - ")
			.append(MapUtils.getString(mapOrigemDestino, "NM_FANTASIA"));
			
			if (idMunicipio != null) {
				listaOrigemDestino = getJdbcTemplate().queryForList(sqlMunicipio, new Long[]{idMunicipio});
				mapOrigemDestino = listaOrigemDestino.isEmpty() ? new HashMap() : (Map) listaOrigemDestino.get(0);
				result.append(" - ");
				result.append(MapUtils.getString(mapOrigemDestino, "NM_MUNICIPIO"));
			}
			return result.toString();
		}		
		return null;
	}
	
	/* Metodo que monta os parameters do relatorio. */
	private Map montaParametersReport(TypedFlatMap parameters, Usuario loggedUser, String dataFormatada) {		 
		Long idRegional = MapUtils.getLong(parameters, "regional.idRegional");
		String siglaFilial = MapUtils.getString(parameters, "filial.sgFilial");
		StringBuilder pesquisa = new StringBuilder();
		if(idRegional != null) {
			List listRegional = getJdbcTemplate().queryForList(" SELECT sg_regional, ds_regional" +
															   " FROM regional" +
															   " WHERE id_regional = '" + idRegional + "' ");
			if(!listRegional.isEmpty()) {
				Map map = (Map) listRegional.get(0);
				String descRegional = map.get("SG_REGIONAL") + " - " + map.get("DS_REGIONAL");
				adicionaParametroPesquisa(pesquisa, descRegional, configuracoesFacade.getMensagem("regional"));
			}
		}

		if(StringUtils.isNotBlank(siglaFilial)) {
			List listFilial = getJdbcTemplate().queryForList(" SELECT p.nm_fantasia" +
														     " FROM pessoa p, filial f" +
														     " WHERE f.id_filial = p.id_pessoa and sg_filial = '" + siglaFilial + "' ");
			if(!listFilial.isEmpty()) {
				Map map = (Map) listFilial.get(0);
				String descFilial = siglaFilial + " - " + map.get("NM_FANTASIA");
				adicionaParametroPesquisa(pesquisa, descFilial, configuracoesFacade.getMensagem("filial"));
			}
		}

		if(StringUtils.isNotBlank(dataFormatada)) {
			adicionaParametroPesquisa(pesquisa, dataFormatada,configuracoesFacade.getMensagem("dataReferencia"));
		}

		Map par = new HashMap();
		String paramPesquisa = ("".equals(pesquisa.toString())) ? configuracoesFacade.getMensagem("reportSemParametros") : pesquisa.toString();
		par.put("parametrosPesquisa", paramPesquisa);	
		par.put("SERVICE",this);
		par.put("usuarioEmissor", loggedUser.getNmUsuario());
		return par;
	}

	private void adicionaParametroPesquisa(StringBuilder sb, String filtro, String label){
		if(sb.length() > 0){
			sb.append(" | ").append(label).append(": ").append(filtro);
		}else{
			sb.append(label).append(": ").append(filtro);
		}
	}

	private String getSql(Long idRegional, Long idFilial, String data) throws Exception {
		StringBuilder sql = new StringBuilder()
		.append(" SELECT DISTINCT ")  
		.append(" D.DS_DIVISAO_CLIENTE AS DIVISAO, \n")
		.append(" T.PARCELA AS PARCELA, \n")
		.append(" ttp.tp_tipo_tabela_preco || lpad(ttp.nr_versao,2,0) || ' - ' || stp.tp_subtipo_tabela_preco as TABELA, \n")
		.append(PropertyVarcharI18nProjection.createProjection("ser.ds_servico_i") +" as  SERVICO, \n")
		.append(" (reg.SG_REGIONAL || ' - ' || reg.DS_REGIONAL) as REGIONAL, \n")
		.append(" (f.SG_FILIAL || ' - ' || pFilial.NM_FANTASIA) as FILIAL, \n")
		.append(" uf_origem.SG_UNIDADE_FEDERATIVA as ORIGEM, \n")
		.append(" uf_destino.SG_UNIDADE_FEDERATIVA as DESTINO, \n")
		.append(PropertyVarcharI18nProjection.createProjection("PAIS_ORIGEM.NM_PAIS_I") + " AS NM_PAIS_ORIGEM, \n")
		.append(PropertyVarcharI18nProjection.createProjection("PAIS_DESTINO.NM_PAIS_I") + " AS NM_PAIS_DESTINO, \n")
		.append(PropertyVarcharI18nProjection.createProjection("ZONA_ORIGEM.DS_ZONA_I") + " AS DS_ZONA_ORIGEM, \n")
		.append(PropertyVarcharI18nProjection.createProjection("ZONA_DESTINO.DS_ZONA_I") + " AS DS_ZONA_DESTINO, \n")
		.append(" p.tp_identificacao as TIPO_IDENT, \n")
		.append(" p.nr_identificacao as IDENT, \n")
		.append(" p.nm_PESSOA as CLIENTE, \n")
		.append(" c.id_cliente as ID_CLIENTE, \n")
		.append(" ser.TP_MODAL, ser.TP_ABRANGENCIA, \n")
		.append(" T.valor as DESCONTO, \n")
	 	.append(" PC.ID_MUNICIPIO_ORIGEM, \n")
	 	.append(" PC.ID_FILIAL_ORIGEM, \n")
	 	.append(" PC.ID_AEROPORTO_ORIGEM, \n")
	 	.append(" PC.ID_TIPO_LOC_MUNICIPIO_ORIGEM, \n")
	 	.append(" PC.ID_MUNICIPIO_DESTINO, \n")
	 	.append(" PC.ID_FILIAL_DESTINO, \n")
	 	.append(" PC.ID_AEROPORTO_DESTINO, \n")
	 	.append(" PC.ID_TIPO_LOC_MUNICIPIO_DESTINO \n")		

		.append(" FROM ")
		.append(" cliente c, \n")
		.append(" pessoa p, \n")
		.append(" pessoa pFilial, \n")
		.append(" divisao_cliente d, \n")
		.append(" tabela_divisao_cliente tdc, \n")
		.append(" tabela_preco tp, \n")
		.append(" tipo_tabela_preco ttp, \n")
		.append(" subtipo_tabela_preco stp, \n")
		.append(" servico ser, \n")
		.append(" Regional reg, \n")
		.append(" Filial f, \n")
		.append(" parametro_cliente pc, \n")
		.append(" unidade_federativa uf_origem, \n")
		.append(" unidade_federativa uf_destino, \n")
		.append(" PAIS PAIS_ORIGEM, \n")
		.append(" PAIS PAIS_DESTINO, \n")
		.append(" ZONA ZONA_ORIGEM, \n")
		.append(" ZONA ZONA_DESTINO, \n")
		.append(" ( ")
		.append(" select ")
		.append(PropertyVarcharI18nProjection.createProjection("p.ds_parcela_preco_i") +" as parcela, \n")
		.append(" g.vl_generalidade as valor, d.id_divisao_cliente as divisao, pc.id_parametro_cliente as pc   \n")
		.append(" from limite_desconto l, \n")
		.append("      generalidade_cliente g, \n")
		.append("      divisao_cliente d, \n")
		.append("      tabela_divisao_cliente tdc, \n")
		.append("      parametro_cliente pc, \n")
		.append("      parcela_preco p, \n")
		.append("      LIMITE_DESCONTO LD, \n")
	    .append("      TABELA_PRECO TP, \n")
	    .append("      TIPO_TABELA_PRECO TTP, \n")
	    .append("      SUBTIPO_TABELA_PRECO STP \n")
		.append(" where d.id_divisao_cliente = tdc.id_divisao_cliente \n")
		.append("   and tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente \n")
		.append("   and pc.id_parametro_cliente = g.id_parametro_cliente \n")
	    .append("   AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO \n")
	    .append("   AND TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO \n")
	    .append("   AND LD.TP_TIPO_TABELA_PRECO = TTP.TP_TIPO_TABELA_PRECO \n")
	    .append("   AND STP.ID_SUBTIPO_TABELA_PRECO = TP.ID_SUBTIPO_TABELA_PRECO \n")
	    .append("   AND LD.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO \n")
		.append("   and L.TP_SITUACAO = 'A' \n")
		.append("   and L.ID_USUARIO is null \n")
		.append("   and L.ID_DIVISAO_GRUPO_CLASSIFIC is null \n")
		.append("   and L.ID_FILIAL is null \n")
		.append("   and g.id_parcela_preco = l.id_parcela_preco \n")
		.append("   and g.id_parcela_preco = p.id_parcela_preco \n")
		.append("   and g.tp_indicador = 'D' \n")
		.append("   and l.pc_limite_desconto < g.vl_generalidade \n");
		if(StringUtils.isNotBlank(data)) {
			sql.append(filtroVigencia);
		}

		sql.append(" union \n")
		.append(" select ")
		.append(PropertyVarcharI18nProjection.createProjection("p.ds_parcela_preco_i") +" as parcela, \n")
		.append(" tx.vl_taxa as valor, d.id_divisao_cliente as divisao, pc.id_parametro_cliente as pc  \n")
		.append("  from limite_desconto l, \n")
		.append("       taxa_cliente tx, \n")
		.append("       divisao_cliente d, \n")
		.append("       tabela_divisao_cliente tdc, \n")
		.append("       parametro_cliente pc, \n")
		.append("       parcela_preco p, \n")
		.append("       LIMITE_DESCONTO LD, \n")
	    .append("       TABELA_PRECO TP, \n")
	    .append("       TIPO_TABELA_PRECO TTP, \n")
	    .append("       SUBTIPO_TABELA_PRECO STP \n")
		.append(" where d.id_divisao_cliente = tdc.id_divisao_cliente \n")
		.append("   and tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente \n")		
		.append("   and pc.id_parametro_cliente = tx.id_parametro_cliente \n")
		.append("   AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO \n")
	    .append("   AND TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO \n")
	    .append("   AND LD.TP_TIPO_TABELA_PRECO = TTP.TP_TIPO_TABELA_PRECO \n")
	    .append("   AND STP.ID_SUBTIPO_TABELA_PRECO = TP.ID_SUBTIPO_TABELA_PRECO \n")
	    .append("   AND LD.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO \n")
		.append("   and L.TP_SITUACAO = 'A' \n")
		.append("   and L.ID_USUARIO is null \n")
		.append("   and L.ID_DIVISAO_GRUPO_CLASSIFIC is null \n")
		.append("   and L.ID_FILIAL is null \n")
		.append("   and tx.id_parcela_preco = l.id_parcela_preco \n")
		.append("   and tx.id_parcela_preco = p.id_parcela_preco \n")
		.append("   and tx.tp_taxa_indicador = 'D' \n")
		.append("   and l.pc_limite_desconto < tx.vl_taxa \n");
		if(StringUtils.isNotBlank(data)) {
			sql.append(filtroVigencia);
		}

		sql.append(" union \n")
		.append(" select ")
		.append(PropertyVarcharI18nProjection.createProjection("p.ds_parcela_preco_i") +" as parcela, \n")
		.append(" sac.vl_valor as valor, d.id_divisao_cliente as divisao, null as pc \n")
		.append("  from limite_desconto l, \n")
		.append("       servico_adicional_cliente sac, \n")
		.append("       divisao_cliente d, \n")
		.append("       tabela_divisao_cliente tdc, \n")
		.append("       parcela_preco p, \n")
		.append("       LIMITE_DESCONTO LD, \n")
	    .append("       TABELA_PRECO TP, \n")
	    .append("       TIPO_TABELA_PRECO TTP, \n")
	    .append("       SUBTIPO_TABELA_PRECO STP \n")
		.append(" where d.id_divisao_cliente = tdc.id_divisao_cliente \n")
		.append("   and tdc.id_tabela_divisao_cliente = sac.id_tabela_divisao_cliente \n")
		.append("   AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO \n")
	    .append("   AND TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO \n")
	    .append("   AND LD.TP_TIPO_TABELA_PRECO = TTP.TP_TIPO_TABELA_PRECO \n")
	    .append("   AND STP.ID_SUBTIPO_TABELA_PRECO = TP.ID_SUBTIPO_TABELA_PRECO \n")
	    .append("   AND LD.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO \n")
		.append("   and L.TP_SITUACAO = 'A' \n")
		.append("   and L.ID_USUARIO is null \n")
		.append("   and L.ID_DIVISAO_GRUPO_CLASSIFIC is null \n")
		.append("   and L.ID_FILIAL is null \n")
		.append("   and sac.id_parcela_preco = l.id_parcela_preco \n")
		.append("   and sac.id_parcela_preco = p.id_parcela_preco \n")
		.append("   and sac.tp_indicador = 'D' \n")
		.append("   and l.pc_limite_desconto < sac.vl_valor \n");
		if(StringUtils.isNotBlank(data)){
			sql.append(" and exists( ");
			sql.append("            select * from parametro_cliente pc, tabela_divisao_cliente tdc1 \n");
			sql.append("            where tdc1.id_tabela_divisao_cliente = sac.id_tabela_divisao_cliente \n"); 
			sql.append("            and tdc1.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente \n");
			sql.append(filtroVigencia);
			sql.append("            ) ");
		}

		sql.append(createSqlCampoDesconto("VL_ADVALOREM", "TP_INDICADOR_ADVALOREM", data, ConstantesExpedicao.CD_ADVALOREM));
		sql.append(createSqlCampoDesconto("VL_ADVALOREM_2", "TP_INDICADOR_ADVALOREM_2", data, ConstantesExpedicao.CD_ADVALOREM));
		sql.append(createSqlCampoDesconto("VL_PEDAGIO", "TP_INDICADOR_PEDAGIO", data, ConstantesExpedicao.CD_PEDAGIO));
		sql.append(createSqlCampoDesconto("VL_PERCENTUAL_GRIS", "TP_INDICADOR_PERCENTUAL_GRIS", data, ConstantesExpedicao.CD_GRIS));
		sql.append(createSqlCampoDesconto("VL_TARIFA_MINIMA", "TP_TARIFA_MINIMA", data, ConstantesExpedicao.CD_TARIFA_MINIMA));
		sql.append(createSqlCampoDesconto("VL_FRETE_PESO", "TP_INDICADOR_FRETE_PESO", data, ConstantesExpedicao.CD_FRETE_PESO));
		sql.append(createSqlCampoDesconto("VL_TBL_ESPECIFICA", "TP_INDIC_VLR_TBL_ESPECIFICA", data, ConstantesExpedicao.CD_FRETE_PESO));
		sql.append(createSqlCampoDesconto("VL_MINIMO_GRIS", "TP_INDICADOR_MINIMO_GRIS", data, ConstantesExpedicao.CD_GRIS));
		sql.append(createSqlCampoDesconto("VL_PERCENTUAL_TDE", "TP_INDICADOR_PERCENTUAL_TDE", data, ConstantesExpedicao.CD_TDE));
		sql.append(createSqlCampoDesconto("VL_MINIMO_TDE", "TP_INDICADOR_MINIMO_TDE", data, ConstantesExpedicao.CD_TDE));
		sql.append(createSqlCampoDesconto("PC_DESCONTO_FRETE_TOTAL", null, data, ConstantesExpedicao.CD_TOTAL_FRETE));
		sql.append(createSqlCampoDesconto("vl_perc_minimo_progr", "tp_indicador_perc_minimo_progr", data, ConstantesExpedicao.CD_FRETE_PESO));
		
		sql.append(" ) T \n")
		.append(" WHERE \n")
		.append("  c.id_cliente = d.id_cliente \n")
		.append("  AND PC.ID_UF_ORIGEM = UF_ORIGEM.ID_UNIDADE_FEDERATIVA (+) \n")
		.append("  AND PC.ID_UF_DESTINO = UF_DESTINO.ID_UNIDADE_FEDERATIVA (+) \n")
		.append("  AND PC.ID_PAIS_ORIGEM = PAIS_ORIGEM.ID_PAIS (+) \n")
		.append("  AND PC.ID_PAIS_DESTINO = PAIS_DESTINO.ID_PAIS (+) \n")
		.append("  AND PC.ID_ZONA_ORIGEM = ZONA_ORIGEM.ID_ZONA \n")
		.append("  AND PC.ID_ZONA_DESTINO = ZONA_DESTINO.ID_ZONA \n")
		.append("  AND c.id_cliente = p.id_pessoa \n")
		.append("  AND d.id_divisao_cliente = tdc.id_divisao_cliente \n")
		.append("  AND tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente \n")
		.append("  AND tdc.id_tabela_preco = tp.id_tabela_preco \n")
		.append("  AND tdc.id_servico = ser.id_servico \n")
		.append("  AND tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco \n")
		.append("  AND tp.id_subtipo_tabela_preco = stp.id_subtipo_tabela_preco \n")
		.append("  AND d.id_divisao_cliente = T.divisao \n")
		.append("  AND pc.id_parametro_cliente = T.pc (+) \n")
		.append("  AND reg.id_regional = c.id_regional_comercial \n")
		.append("  AND f.id_filial = c.id_filial_atende_comercial \n")
		.append("  AND f.id_filial = pFilial.id_pessoa \n");
		if(StringUtils.isNotBlank(data)) {
			sql.append(filtroVigencia);
		}
		
		if (idFilial != null) {
			sql.append(" and c.id_filial_atende_comercial ="+ idFilial);
		} else {
			sql.append(" and c.id_filial_atende_comercial in ").append(SQLUtils.joinExpressionsWithComma(getIdsFiliais()));
		}
		
		if (idRegional != null) {
			sql.append(" and c.id_regional_comercial = "+idRegional);
		}
		
		sql.append("  order by regional, filial, cliente, IDENT, divisao, tabela");

		return sql.toString();
	}

	private List<Long> getIdsFiliais() {
		List<Long> result = new ArrayList<Long>();
		List<Filial> filiais = SessionUtils.getFiliaisUsuarioLogado();
		for (Filial filial : filiais) {
			result.add(filial.getIdFilial());
		}
		return result;
	}
	
	private String createSqlCampoDesconto(String valor, String indicador, String data, String cdParcelaPreco) {
		StringBuilder sql = new StringBuilder()
		.append("UNION \n")
		.append("SELECT ").append(PropertyVarcharI18nProjection.createProjection("PP.DS_PARCELA_PRECO_I")).append(" AS PARCELA, \n")
		.append("       PC.").append(valor).append(" AS VALOR, \n")
		.append("       D.ID_DIVISAO_CLIENTE AS DIVISAO, \n")
		.append("       PC.ID_PARAMETRO_CLIENTE AS PC \n")
		.append("  FROM PARAMETRO_CLIENTE PC, \n")
	    .append("       PARCELA_PRECO PP, \n")
	    .append("       TABELA_DIVISAO_CLIENTE TDC, \n")
	    .append("       DIVISAO_CLIENTE D, \n")
	    .append("       LIMITE_DESCONTO LD, \n")
	    .append("       TABELA_PRECO TP, \n")
	    .append("       TIPO_TABELA_PRECO TTP, \n")
	    .append("       SUBTIPO_TABELA_PRECO STP \n")
	    .append(" WHERE D.ID_DIVISAO_CLIENTE = TDC.ID_DIVISAO_CLIENTE \n")
	    .append("   AND PC.ID_TABELA_DIVISAO_CLIENTE = TDC.ID_TABELA_DIVISAO_CLIENTE \n")
	    .append("   AND TDC.ID_TABELA_PRECO = TP.ID_TABELA_PRECO \n")
	    .append("   AND TP.ID_TIPO_TABELA_PRECO = TTP.ID_TIPO_TABELA_PRECO \n")
	    .append("   AND LD.TP_TIPO_TABELA_PRECO = TTP.TP_TIPO_TABELA_PRECO \n")
	    .append("   AND STP.ID_SUBTIPO_TABELA_PRECO = TP.ID_SUBTIPO_TABELA_PRECO \n")
	    .append("   AND LD.ID_SUBTIPO_TABELA_PRECO = STP.ID_SUBTIPO_TABELA_PRECO \n")
	    .append("   AND PP.CD_PARCELA_PRECO = '").append(cdParcelaPreco).append("' \n")
	    .append("   AND LD.TP_SITUACAO = 'A' \n")
	    .append("   AND LD.ID_USUARIO IS NULL \n")
	    .append("   AND LD.ID_DIVISAO_GRUPO_CLASSIFIC IS NULL \n")
	    .append("   AND LD.ID_FILIAL IS NULL \n")
	    .append("   AND (LD.TP_INDICADOR_DESCONTO = 'P' OR \n") 
	    .append("        LD.TP_INDICADOR_DESCONTO IS NULL) \n")
	    .append("   AND LD.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO \n")
	    .append("   AND PC.").append(valor).append(" > LD.PC_LIMITE_DESCONTO \n");
		
		if (StringUtils.isNotBlank(indicador)) {
			sql.append("   AND PC.").append(indicador).append(" = 'D' \n");
		}
		
		if (StringUtils.isNotBlank(data)) {
			sql.append(filtroVigencia);
		}
		
		return sql.toString();
	}
	
	private String createNomesPromotores(List promotores) {
		StringBuilder nomes = new StringBuilder();
		for (Iterator itProm = promotores.iterator(); itProm.hasNext();) {
			Map promotor = (Map) itProm.next();
			nomes.append(MapUtils.getString(promotor,"USUARIO"));
			if (itProm.hasNext()) {
				nomes.append("\n");
			}
		}
		return nomes.toString();
	}
	
	private void adicionaUf(Long idCliente, int index, String uf, Map<Long, Map<String, Integer>[]> ufsCliente) {
		Map<String, Integer>[] mapas = ufsCliente.get(idCliente);
		if (mapas == null) {
			mapas = new Map[] {new HashMap<String, Integer>(), new HashMap<String, Integer>()};
			ufsCliente.put(idCliente, mapas);
		}
		Map<String, Integer> mapa = mapas[index];
		Integer count = mapa.get(uf);
		if (count != null) {
			mapa.put(uf, IntegerUtils.incrementValue(count));
		} else {
			mapa.put(uf, IntegerUtils.ONE);
		}
	}
	
	private int countUfs(Long idCliente, int index, String uf, Map<Long, Map<String, Integer>[]> ufsCliente) {
		Map<String, Integer>[] ufs = ufsCliente.get(idCliente);
		if (ufs == null) {
			return 0;
		}
		Map<String, Integer> mapa = ufs[index];
		Integer count = mapa.get(uf);
		if (count != null) {
			return count.intValue();
		}
		return 0;
	}
	
	private void processaRegistro(Map registro, int tipo, Map ufsCliente) {
		String sufixo = "ORIGEM";
		if (DESTINO == tipo) {
			sufixo = "DESTINO";
		}
		String uf = MapUtils.getString(registro, sufixo);
		
		if (StringUtils.isNotBlank(uf)) {
			Long idCliente = MapUtils.getLong(registro, "ID_CLIENTE");
			
			StringBuilder rota = new StringBuilder()
			.append(MapUtils.getString(registro, "NM_PAIS_"+sufixo))
			.append(" - ")
			.append(uf);
			
			registro.put(sufixo, rota.toString());
			
			Long idMunicipio = MapUtils.getLong(registro,"ID_MUNICIPIO_"+sufixo);
			Long idFilial = MapUtils.getLong(registro,"ID_FILIAL_"+sufixo);
			Long idTipoLocMunicipio = MapUtils.getLong(registro,"ID_TIPO_LOC_MUNICIPIO_"+sufixo);
			Long idAeroporto = MapUtils.getLong(registro,"ID_AEROPORTO_"+sufixo);
			
			if (idMunicipio != null || idFilial != null || idTipoLocMunicipio != null || idAeroporto != null) {
				adicionaUf(idCliente, tipo, rota.toString(), ufsCliente);
			}
			
			String complemento = findComplementoRegistro(registro, sufixo);
			if (complemento != null) {
				complemento = " - " + complemento;
			}
			registro.put("COMPLEMENTO_"+sufixo, complemento);
		} else {
			String nmPais = MapUtils.getString(registro, "NM_PAIS_"+sufixo);
			if (StringUtils.isNotBlank(nmPais)) {
				registro.put(sufixo, nmPais);
			} else {
				registro.put(sufixo, MapUtils.getString(registro, "DS_ZONA_"+sufixo));
			}
			registro.put("COMPLEMENTO_"+sufixo, "SEM_COMPLEMENTO");
		}
	}
	
	/*
	 * GETTERS E SETTERS
	 */

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}