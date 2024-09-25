package com.mercurio.lms.vendas.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.MapUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;


/**
 * @author Diego Pacheco, Rodrigo Feio Dias
 *  
 *  ET: 30.03.02.30 - Emitir Tabela de frete aéreo com tarifa específica
 *
 * @spring.bean id="lms.vendas.tabelaFreteAereoTarifaEspecificaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirTabelaFreteAereoTarifaEspecifica.jasper"
 */
public class TabelaFreteAereoTarifaEspecificaService extends ReportServiceSupport {

	private static final String NOME_TABELA = "TMP_AEREO_TARIFA_EXPEC";	
	private EmitirTabelaTaxaCombustivelPortraitService emitirTabelaTaxaCombustivel;
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private EmitirTabelasClientesService emitirTabelasClientesService;
	private TabelasClienteService tabelasClienteService;

	private class ParametrosVO
	{
		private Long idCliente;
		private Long idDivisao; 
		private Long idTabelaPreco;
		private Boolean isTabelaNova;
		private Long idSimulacao;
		private Long idTabelaDivisaoCliente;
		private Long idContato;
		private Long idProduto;
		private List parametrosCli;
		private String dsSimbolo;
		private String dsTipoServico;
		
		public ParametrosVO(Long idCliente, Long idDivisao, Long idTabelaPreco, Boolean isTabelaNova, Long idSimulacao, Long idTabelaDivisaoCliente, Long idContato, List parametrosCli, String dsSimbolo, String dsTipoServico, Long idProduto) {
			this.idCliente = idCliente;
			this.idDivisao = idDivisao;
			this.idTabelaPreco = idTabelaPreco;
			this.isTabelaNova = isTabelaNova;
			this.idTabelaDivisaoCliente = idTabelaDivisaoCliente;
			this.idContato = idContato;
			this.parametrosCli = parametrosCli;
			this.dsSimbolo = dsSimbolo;
			this.dsTipoServico = dsTipoServico;
			this.idProduto = idProduto;
		}

		public void setAttributesToParameters(Map parameters)
		{
			parameters.put("idCliente", this.getIdCliente());
			parameters.put("idDivisao", this.getIdDivisao());
			parameters.put("idTabelaPreco", this.getIdTabelaPreco());
			parameters.put("idTabelaDivisaoCliente", this.getIdTabelaDivisaoCliente());
			parameters.put("idContato", this.getIdContato());
			parameters.put("parametrosCli", this.getParametrosCli());
			parameters.put("dsSimbolo", this.getDsSimbolo());
			parameters.put("dsTipoServico", this.getDsTipoServico());
			parameters.put("idProduto", this.getIdProduto());
		}
		
		public Long getIdCliente() {
			return idCliente;
		}

		public void setIdCliente(Long idCliente) {
			this.idCliente = idCliente;
		}

		public Long getIdDivisao() {
			return idDivisao;
		}

		public void setIdDivisao(Long idDivisao) {
			this.idDivisao = idDivisao;
		}

		public Long getIdTabelaDivisaoCliente() {
			return idTabelaDivisaoCliente;
		}

		public void setIdTabelaDivisaoCliente(Long idTabelaDivisaoCliente) {
			this.idTabelaDivisaoCliente = idTabelaDivisaoCliente;
		}

		public Long getIdTabelaPreco() {
			return idTabelaPreco;
		}

		public void setIdTabelaPreco(Long idTabelaPreco) {
			this.idTabelaPreco = idTabelaPreco;
		}

		public List getParametrosCli() {
			return parametrosCli;
		}

		public void setParametrosCli(List parametrosCli) {
			this.parametrosCli = parametrosCli;
		}

		public String getDsSimbolo() {
			return dsSimbolo;
		}

		public void setDsSimbolo(String dsSimbolo) {
			this.dsSimbolo = dsSimbolo;
		}

		public String getDsTipoServico() {
			
			return dsTipoServico;
		}

		public void setDsTipoServico(String dsTipoServico) {
			this.dsTipoServico = dsTipoServico;
		}

		public Long getIdContato() {
			return idContato;
		}

		public void setIdContato(Long idContato) {
			this.idContato = idContato;
		}

		public Long getIdProduto() {
			return idProduto;
		}

		public void setIdProduto(Long idProduto) {
			this.idProduto = idProduto;
		}

		public Long getIdSimulacao() {
			return idSimulacao;
		}

		public void setIdSimulacao(Long idSimulacao) {
			this.idSimulacao = idSimulacao;
		}

		public Boolean getIsTabelaNova() {
			return isTabelaNova;
		}

		public void setIsTabelaNova(Boolean isTabelaNova) {
			this.isTabelaNova = isTabelaNova;
		}		
	}
	

	/**
	 * Para utiliza&ccedil;&aatilde;o de RecursosMensagens
	 */
	private ConfiguracoesFacade configuracoesFacade = null;
	

	private ParametrosVO createParametroVO(Map parameters,JdbcTemplate jdbcTemplate)
	{	
		return new ParametrosVO(
									MapUtilsPlus.getLong(parameters, "idCliente",null),
									MapUtilsPlus.getLong(parameters, "idDivisao",null),
									MapUtilsPlus.getLong(parameters, "idTabelaPreco",null),
									MapUtils.getBoolean(parameters, "isTabelaNova", false),
									MapUtilsPlus.getLong(parameters, "idSimulacao",null),
									MapUtilsPlus.getLong(parameters, "idTabelaDivisao",null),
									MapUtilsPlus.getLong(parameters, "idContato",null),
									MapUtilsPlus.getList(parameters, "listaParametros", null),
									getTabelasClienteService().getMoeda(MapUtilsPlus.getLong(parameters, "idTabelaPreco",null), jdbcTemplate),
									getTabelasClienteService().getTipoServico(MapUtilsPlus.getLong(parameters, "idTabelaDivisao",null), jdbcTemplate), 
									MapUtilsPlus.getLong(parameters, "idProduto",null)
									
							   );		

	}	
	
	/**
	 * 
	 * @param reportParameters
	 * @param pvo
	 * @param mapAgrupamento
	 * @param jdbcTemplate
	 */
	private void montaParametersReport(Map reportParameters,ParametrosVO pvo,Map mapAgrupamento,JdbcTemplate jdbcTemplate)
	{		
		pvo.setAttributesToParameters(reportParameters);
		reportParameters.put("HEADER",(JRMapCollectionDataSource)getTabelasClienteService().montaHeader(reportParameters, jdbcTemplate,TabelasClienteService.RETORNO_DATASOURCE));		
		reportParameters.put("SERVICO", pvo.getDsTipoServico());		
		reportParameters.put("dsSimbolo", pvo.getDsSimbolo());		
		reportParameters.put("TE", reportParameters.get("TE"));
		
		emitirTabelasClientesService.montaParametrosAereo(pvo.getIdTabelaPreco(), pvo.getIdCliente(), pvo.getIdDivisao(), pvo.isTabelaNova, pvo.getIdDivisao(), reportParameters, mapAgrupamento, jdbcTemplate, configuracoesFacade);		
	}

	public JRReportDataObject execute(Map parameters) throws Exception {

		/* limpa a tabela temporaria e pega uma instancia de JdbcTemplate */
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		jdbcTemplate.execute("DELETE FROM " + NOME_TABELA);		
		
		Long idSimulacao = MapUtilsPlus.getLong(parameters, "idSimulacao");
		
		/* cria o VO com os parametros necessarios aos metodos. */
		ParametrosVO pvo = createParametroVO(parameters,jdbcTemplate);
		if(pvo.idTabelaPreco == null){
			JRReportDataObject jr = executeQuery("SELECT * FROM "+ NOME_TABELA +" ",new HashMap());
			parameters.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf(1)});
			jr.setParameters(parameters);
			return jr;
		}
		
		Map parametersReport = new HashMap();

		/* Monta o agrupamento do relatório */
		Map mapAgrupamento = new HashMap();
		mapAgrupamento = getTabelasClienteService().montaAgrupamento(pvo.getIdCliente(),pvo.getIdDivisao(),idSimulacao,pvo.getParametrosCli(),mapAgrupamento,jdbcTemplate);		

		populateTable(mapAgrupamento,pvo,parametersReport,jdbcTemplate);

		int totRegistros = jdbcTemplate.queryForInt("SELECT COUNT(*) FROM(SELECT DISTINCT * FROM " + NOME_TABELA + ")");
		JRReportDataObject jr = executeQuery("SELECT DISTINCT * FROM " + NOME_TABELA	+ " Order by GRUPO", parameters);
		if(totRegistros == 0)
		{
			parameters.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf(1)});
			jr.setParameters(parameters);
			return jr;
		}

		/* Monta os parameters necessarios ao relatorio e seus sub-reports. */
		parametersReport.put("TOTAL", Integer.valueOf(totRegistros));
		parametersReport.put("idCliente", pvo.getIdCliente());
		parametersReport.put("idContato", pvo.getIdContato());
		montaParametersReport(parametersReport,pvo,mapAgrupamento,jdbcTemplate);
		parametersReport.put("SERVICE_TX_COMB", emitirTabelaTaxaCombustivel);
		getTabelasClienteService().montaLogoMercurio(parametersReport, jdbcTemplate);
		
		jr.setParameters(parametersReport);

		return jr;
	}

	
	/**
	 * Popula a tabela tempor&aacute;ria com dados
	 * 
	 * @param parametros
	 * @param divisao
	 * @param idCliente
	 * @param agrup
	 */
	private void populateTable(Map agrup,ParametrosVO pvo, Map parameters, JdbcTemplate jdbcTemplate) {
		if (pvo.getParametrosCli() != null && pvo.getParametrosCli().size() > 0) {
			String sql = montaSql(pvo.getParametrosCli());
			int tamanho = agrup.size();
			List listaUFOrigem = new ArrayList();
			List listaUFDestino = new ArrayList();
			
		    List registros =  jdbcTemplate.queryForList(sql, new Long[]{pvo.getIdProduto(),pvo.getIdCliente(),pvo.getIdDivisao()});
		    Map dados = null;
			String origem = null, destino = null, grupo = null, ufOrigem = null, ufDestino = null, mOrigem = null, mDestino = null;
			String tarifa = null;
			Long idParametro = null;
			Long idAeroportoOrigem = null, idMunicipioOrigem = null, idFilialOrigem = null, idTipoLocMunicipioOrigem = null;
			Long idAeroportoDestino = null, idMunicipioDestino = null, idFilialDestino = null, idTipoLocMunicipioDestino = null;
			BigDecimal valorFaixaProgressiva = null, tf_minima = null;
			
			String dsGrupoRegiao = null;
			
			String msgTodoEstado = configuracoesFacade.getMensagem("todoEstado");
			String msgDemaisLocalidades = configuracoesFacade.getMensagem("demaisLocalidades");
			
		    for (Iterator iter = registros.iterator(); iter.hasNext();)
		    {
				dados = (Map) iter.next();
				StringBuffer sqlInsert = new StringBuffer();
				idParametro = Long.valueOf(dados.get("ID_PARAMETRO_CLIENTE").toString());
				
				grupo = getTabelasClienteService().getGrupo(idParametro, agrup, tamanho);
				
				if (dados.get("UF_ORIGEM")== null || dados.get("UF_DESTINO") == null) continue;
				
				String strProduto = (String) dados.get("PRODUTO_ESPECIFICO");
				parameters.put("PRODUTO_ESPECIFICO", strProduto);

				tarifa					  =  MapUtilsPlus.getStringCaseInsensitive(dados,"TARIFA",null);
				tf_minima     			  =  MapUtilsPlus.getBigDecimalCaseInsensitive(dados,"TAXA_MINIMA",null);
				valorFaixaProgressiva     = MapUtilsPlus.getBigDecimalCaseInsensitive(dados,"VL_TBL_ESPECIFICA",null);
				
				ufOrigem  = dados.get("UF_ORIGEM").toString();
				ufDestino = dados.get("UF_DESTINO").toString();
				//ORIGEM
				if(ufOrigem != null){//valida origem dos parametros do cliente
					
					idAeroportoOrigem 		  =  MapUtilsPlus.getLongCaseInsensitive(dados,"ID_AEROPORTO_ORIGEM",null);
					idMunicipioOrigem 		  =  MapUtilsPlus.getLongCaseInsensitive(dados,"ID_MUNICIPIO_ORIGEM",null);
					idFilialOrigem			  =  MapUtilsPlus.getLongCaseInsensitive(dados,"ID_FILIAL_ORIGEM",null);
					idTipoLocMunicipioOrigem  =  MapUtilsPlus.getLongCaseInsensitive(dados,"ID_TIPO_LOC_MUNICIPIO_ORIGEM",null);
				}
				mOrigem = getOrigemDestino(idAeroportoOrigem, idMunicipioOrigem, idFilialOrigem, idTipoLocMunicipioOrigem, jdbcTemplate);

				//DESTINO
				if(ufDestino != null){//valida destino pelo parametro do cliente
					
					idAeroportoDestino		  =  MapUtilsPlus.getLongCaseInsensitive(dados,"ID_AEROPORTO_DESTINO",null);
					idMunicipioDestino 		  =  MapUtilsPlus.getLongCaseInsensitive(dados,"ID_MUNICIPIO_DESTINO",null);
					idFilialDestino			  =  MapUtilsPlus.getLongCaseInsensitive(dados,"ID_FILIAL_DESTINO",null);
					idTipoLocMunicipioDestino =  MapUtilsPlus.getLongCaseInsensitive(dados,"ID_TIPO_LOC_MUNICIPIO_DESTINO",null);
					idTipoLocMunicipioDestino =  MapUtilsPlus.getLongCaseInsensitive(dados,"ID_TIPO_LOC_MUNICIPIO_DESTINO",null);
					dsGrupoRegiao 		      =  MapUtilsPlus.getString(dados,"ds_grupo_regiao",null);
				}
					
				if(dsGrupoRegiao != null){
					mDestino = dsGrupoRegiao;
				}else{
				mDestino = getOrigemDestino(idAeroportoDestino, idMunicipioDestino, idFilialDestino, idTipoLocMunicipioDestino, jdbcTemplate);
				}
				
				//Concatena o uf da origem e do destino com "-" para ser usado no metodo verificaComplemento
				String ufOrigemDestino = ufOrigem+"-"+ufDestino;
				mOrigem 	= getTabelasClienteService().verificaComplemento(listaUFOrigem, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mOrigem);
				mDestino 	= getTabelasClienteService().verificaComplemento(listaUFDestino, msgTodoEstado, msgDemaisLocalidades, ufOrigemDestino, mDestino);
				
				
				origem  = mOrigem  != null ? ufOrigem + " - " + mOrigem : ufOrigem;
				destino = mDestino != null ? ufDestino + " - " + mDestino : ufDestino;
			
				//salva registro na tabela temporaria
				sqlInsert.append("INSERT INTO " + NOME_TABELA + " (ID_PARAMETRO_CLIENTE, TARIFA, TAXAMINIMA,")
				.append(" ORIGEM, DESTINO, VL_FAIXA_PROGRESIVA, GRUPO) VALUES(?, ?, ?, ?, ?, ?, ?)");
				
				jdbcTemplate.update(sqlInsert.toString(), new Object[]{idParametro, tarifa, tf_minima, origem, destino, valorFaixaProgressiva, grupo});
			}
		    getTabelasClienteService().verificaUfs(listaUFOrigem,"ORIGEM",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);
		    getTabelasClienteService().verificaUfs(listaUFDestino,"DESTINO",NOME_TABELA,msgTodoEstado,msgDemaisLocalidades,jdbcTemplate);
		
		}
	}
	
	/**
	 * 
	 * @param idAeroporto
	 * @param idMunicipio
	 * @param idFilial
	 * @param idTipoMunicipio
	 * @param jdbcTemplate
	 * @return
	 */
	private String getOrigemDestino(Long idAeroporto, Long idMunicipio, Long idFilial, Long idTipoLocMunicipio, JdbcTemplate jdbcTemplate){
		
		String origemDestino = null;
		
		//pre-requisitos
		//obtencao da origem e destino
		StringBuffer sqlAeroporto = new StringBuffer("");
		sqlAeroporto.append("select P.NM_FANTASIA from PESSOA P, AEROPORTO A, FILIAL F ");   
		sqlAeroporto.append("where A.ID_AEROPORTO = ?");
		sqlAeroporto.append(" and A.ID_FILIAL_RESPONSAVEL = F.ID_FILIAL");
		sqlAeroporto.append(" and F.ID_FILIAL = P.ID_PESSOA");
		
		StringBuffer sqlMunicipio = new StringBuffer("");
		sqlMunicipio.append("select M.NM_MUNICIPIO from MUNICIPIO M where M.ID_MUNICIPIO = ?");
		
		StringBuffer sqlFilial = new StringBuffer("");
		sqlFilial.append("select F.SG_FILIAL from FILIAL F where F.ID_FILIAL = ?");
		
		StringBuffer sqlTipoLocMunicipio = new StringBuffer("");
		sqlTipoLocMunicipio.append("select T.DS_TIPO_LOCALIZACAO_MUNICIPIO from TIPO_LOCALIZACAO_MUNICIPIO T");
		sqlTipoLocMunicipio.append(" where T.ID_TIPO_LOCALIZACAO_MUNICIPIO = ?");
		
		//processamento conforme regra 2.14 do relatorio
		if(idAeroporto != null)
		{
			List listaOrigem = jdbcTemplate.queryForList(sqlAeroporto.toString(), new Long[]{idAeroporto});
			Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
			origemDestino = MapUtils.getString(mapOrigem,"NM_FANTASIA");
		}
		else if (idMunicipio != null)
		{
			List listaOrigem = jdbcTemplate.queryForList(sqlMunicipio.toString(), new Long[]{idMunicipio});
			Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
			origemDestino = MapUtils.getString(mapOrigem,"NM_MUNICIPIO");
		}
		else if (idFilial != null)
		{
			List listaOrigem = jdbcTemplate.queryForList(sqlFilial.toString(), new Long[]{idFilial});
			Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
			origemDestino = MapUtils.getString(mapOrigem,"SG_FILIAL");
		}
		else if (idTipoLocMunicipio != null)
		{
			List listaOrigem = jdbcTemplate.queryForList(sqlTipoLocMunicipio.toString(), new Long[]{idTipoLocMunicipio});
			Map  mapOrigem   = listaOrigem.isEmpty() ? new HashMap() : (Map)listaOrigem.get(0);
			origemDestino = MapUtils.getString(mapOrigem,"DS_TIPO_LOCALIZACAO_MUNICIPIO");
		}
		
		return origemDestino;
	}
	
	/**
	 * Monta sql para consulta principal
	 * 
	 * @param parametros
	 * @return
	 */
	private String montaSql(List parametros) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT DISTINCT \n")
		   .append("lpad(pe.NR_TARIFA_ESPECIFICA,3,0) ||' - '|| \n")
		   .append(PropertyVarcharI18nProjection.createProjection("pe.DS_PRODUTO_ESPECIFICO_I")).append(" as PRODUTO_ESPECIFICO,  \n")
		   .append("pc.id_parametro_cliente,  \n")
		   .append("tp.id_tabela_preco,  \n")
		   
		   .append("uf_origem.sg_unidade_federativa AS uf_origem,  \n" )
		   .append("uf_destino.sg_unidade_federativa AS uf_destino,  \n")
		   //.append("uf_origem_rota.sg_unidade_federativa AS uf_origem_rota, \n") 
		   //.append("uf_destino_rota.sg_unidade_federativa AS uf_destino_rota, \n") 		   
		   
		   .append("pc.VL_TARIFA_MINIMA AS TAXA_MINIMA,  \n")		   
		   .append("pc.vl_min_frete_peso AS peso_minimo,  \n")
		   .append("pc.vl_frete_peso AS excedentekg,  \n")
		   .append(PropertyVarcharI18nProjection.createProjection("ts.ds_tipo_servico_i")).append(" , \n")

		   .append("pc.id_aeroporto_origem, \n")
		   .append("pc.id_municipio_origem, \n")
		   .append("pc.id_filial_origem, \n")
		   .append("pc.id_tipo_loc_municipio_origem, \n")
		   
		   .append("pc.id_aeroporto_destino,  \n")
		   .append("pc.id_municipio_destino,  \n")
		   .append("pc.id_filial_destino, \n")
		   .append("pc.id_tipo_loc_municipio_destino, \n")
		   
		   .append("pc.tp_indic_vlr_tbl_especifica,  \n")
		   .append("pes.nm_fantasia,  \n")
		   .append("decode(pc.tp_indic_vlr_tbl_especifica,'V',pc.vl_tbl_especifica, vfp.vl_fixo) AS vl_fixo,  \n")
		   .append("pc.vl_tbl_especifica,  \n")
		   .append("gr_destino.ds_grupo_regiao  \n")

		   .append("FROM cliente c,  \n")
		   .append("divisao_cliente dc,  \n")
		   .append("tabela_divisao_cliente tdc,  \n")
		   .append("tabela_preco tp,  \n")
		   .append("tabela_preco_parcela tpp,  \n")
		   .append("parcela_preco pp,  \n")
		   .append("faixa_progressiva fp,  \n")
		   .append("produto_especifico pe,  \n")
		   .append("valor_faixa_progressiva vfp,  \n")
		   //.append("rota_preco rp,  \n")
		   .append("tipo_tabela_preco ttp,  \n")
		   .append("empresa emp,  \n")
		   .append("pessoa pes,  \n")
		   .append("parametro_cliente pc,  \n")
		   .append("preco_frete pf,  \n")
		   .append("servico s,  \n")
		   .append("tipo_servico ts,  \n")
		   .append("municipio m_origem,  \n")
		   .append("municipio m_destino,  \n")
		   .append("unidade_federativa uf_origem,  \n")
		   .append("unidade_federativa uf_destino,  \n")
		   .append("grupo_regiao gr_destino,  \n")
		   
		   
		   //.append("unidade_federativa uf_origem_rota, \n")  
		   //.append("unidade_federativa uf_destino_rota, \n")
		   
		   .append("filial f_origem,  \n")
		   .append("filial f_destino,  \n")
		   .append("aeroporto aero_origem,  \n")
		   .append("aeroporto aero_destino  \n")

		   .append("WHERE c.id_cliente = dc.id_cliente  \n")
		   .append("AND dc.id_divisao_cliente = tdc.id_divisao_cliente  \n")
		   .append("AND tdc.id_tabela_divisao_cliente = pc.id_tabela_divisao_cliente  \n")
		   .append("AND tdc.id_tabela_preco = tp.id_tabela_preco  \n")
		   .append("AND tdc.id_servico = s.id_servico  \n")
		   .append("AND s.id_tipo_servico = ts.id_tipo_servico  \n")
		   
		   .append("AND pc.id_grupo_regiao_destino = gr_destino.id_grupo_regiao (+)  \n")
		   
		   
		   //.append("AND rp.id_uf_origem = uf_origem_rota.id_unidade_federativa \n")
		   //.append("AND rp.id_uf_destino = uf_destino_rota.id_unidade_federativa \n")  
		   
		   .append("AND pc.id_uf_origem = uf_origem.id_unidade_federativa(+)  \n")
		   .append("AND pc.id_uf_destino = uf_destino.id_unidade_federativa(+)  \n")
		   .append("AND pc.id_municipio_origem = m_origem.id_municipio(+)  \n")
		   .append("AND pc.id_municipio_destino = m_destino.id_municipio(+)  \n")
		   .append("AND pc.id_filial_origem = f_origem.id_filial(+)  \n")
		   .append("AND pc.id_filial_destino = f_destino.id_filial(+)  \n")
		   .append("AND pc.id_aeroporto_origem = aero_origem.id_aeroporto(+)  \n")
		   .append("AND pc.id_aeroporto_destino = aero_destino.id_aeroporto(+)  \n")
		   .append("AND tpp.id_tabela_preco = tp.id_tabela_preco  \n")
		   .append("AND tpp.id_parcela_preco = pp.id_parcela_preco  \n")
		   .append("AND tpp.id_tabela_preco_parcela = fp.id_tabela_preco_parcela  \n")
		   .append("AND tpp.id_tabela_preco_parcela = pf.id_tabela_preco_parcela (+) \n") 
		   .append("AND fp.id_produto_especifico = pe.id_produto_especifico  \n")
		   .append("AND pe.id_produto_especifico = ? \n")
		   .append("AND fp.id_faixa_progressiva = vfp.id_faixa_progressiva  \n")
		   //.append("AND vfp.id_rota_preco = rp.id_rota_preco  \n")
		   .append("AND tp.id_tipo_tabela_preco = ttp.id_tipo_tabela_preco  \n")
		   .append("AND ttp.id_empresa_cadastrada = emp.id_empresa  \n")
		   .append("AND pes.id_pessoa = emp.id_empresa  \n")
				 
		   .append("AND c.id_cliente = ?  \n")
		   .append("AND dc.id_divisao_cliente = ?  \n")
		   .append("AND pc.TP_TARIFA_MINIMA = 'V'  \n")
		   .append("AND pc.tp_indic_vlr_tbl_especifica = 'V' \n");

		StringBuffer idsParamCliente = new StringBuffer("");
		boolean primeiro = true;
		for (Iterator iter = parametros.iterator(); iter.hasNext();) {
			Long pc = (Long) iter.next();
			if (!primeiro)
				idsParamCliente.append(",");
			else
				primeiro = false;

			idsParamCliente.append(pc);
		}
		if (!idsParamCliente.toString().equalsIgnoreCase(""))
			sql.append(" AND PC.ID_PARAMETRO_CLIENTE in (").append(idsParamCliente).append(") \n");

		sql.append("order by UF_ORIGEM.SG_UNIDADE_FEDERATIVA, UF_DESTINO.SG_UNIDADE_FEDERATIVA ");

		return sql.toString();
	}

		
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
		}

	public EmitirTabelaTaxaCombustivelPortraitService getEmitirTabelaTaxaCombustivel() {
		return emitirTabelaTaxaCombustivel;
		}
		
	public void setEmitirTabelaTaxaCombustivel(EmitirTabelaTaxaCombustivelPortraitService emitirTabelaTaxaCombustivel) {
		this.emitirTabelaTaxaCombustivel = emitirTabelaTaxaCombustivel;
		}
		
	public List<Map<String, String>> findDados(TypedFlatMap parameters) {
		String produtoEspecifico = parameters.getString("produtoEspecifico.idProdutoEspecifico");
		if(produtoEspecifico==null || (produtoEspecifico!=null&&produtoEspecifico.trim().equals(""))) return null;

		//Busca os dados iniciais do relatório
		List<Map> data = getEmitirTabelasClienteDAO().findRelatorioAereoTarifaEspecifica(parameters);
		TypedFlatMap parametrosReport = new TypedFlatMap();
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		List listaParametros = new ArrayList();
		boolean primeiro = true;
		Long idTabelaDivisao = null;
        
		if(data == null || data.isEmpty()){
			return null;
		}

		for (Map map : data) {
			parameters.put("idCliente", MapUtils.getLong(map, "idCliente"));
			parameters.put("idDivisao", MapUtils.getLong(map, "idDivisao"));
			parameters.put("idTabelaPreco", MapUtils.getLong(map, "idTabelaPreco"));
			parameters.put("idContato", MapUtils.getLong(map, "idContato"));
			if(primeiro) {
				idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
				parametrosReport = getCommonParameter(map);
				parametrosReport.put("idTabelaDivisao", idTabelaDivisao);
				parametrosReport.put("idProduto", MapUtils.getLong(map,"idProduto"));
				parametrosReport.put("TE",produtoEspecifico);
				listaParametros.add(MapUtilsPlus.getLong(map,"listaParametros",null));
				parametrosReport.put("listaParametros", listaParametros );
				primeiro = false;
			} else {
				if(!idTabelaDivisao.equals(MapUtils.getLong(map,"idTabelaDivisao"))) {
					parametrosReport.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
			
					idTabelaDivisao = MapUtils.getLong(map, "idTabelaDivisao");
					parametrosReport = getCommonParameter(map);
					parametrosReport.put("idTabelaDivisao", idTabelaDivisao);
					parametrosReport.put("idProduto", MapUtils.getLong(map, "idProduto"));
					parametrosReport.put("TE",produtoEspecifico);
					listaParametros = new ArrayList();
					listaParametros.add(MapUtilsPlus.getLong(map, "listaParametros", null));
					parametrosReport.put("listaParametros", listaParametros );
				} else {
					idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
					listaParametros.add(MapUtilsPlus.getLong(map, "listaParametros", null));
					parametrosReport.put("listaParametros", listaParametros);
				}
			}
			parameters.put(idTabelaDivisao, parametrosReport);
		}
		if(!listaParametros.isEmpty()) {
			parametrosReport.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
		}
		return result;
	}
		
	private TypedFlatMap getCommonParameter(Map param) {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idCliente", MapUtils.getLong(param, "idCliente"));
		parameters.put("idDivisao", MapUtils.getLong(param, "idDivisao"));
		parameters.put("idTabelaPreco", MapUtils.getLong(param, "idTabelaPreco"));
		parameters.put("idContato", MapUtils.getLong(param, "idContato"));
		return parameters;
	}

	public EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return emitirTabelasClienteDAO;
	}

	public void setEmitirTabelasClienteDAO(
			EmitirTabelasClienteDAO emitirTabelasClienteDAO) {
		this.emitirTabelasClienteDAO = emitirTabelasClienteDAO;
	}

	public EmitirTabelasClientesService getEmitirTabelasClientesService() {
		return emitirTabelasClientesService;
	}

	public void setEmitirTabelasClientesService(
			EmitirTabelasClientesService emitirTabelasClientesService) {
		this.emitirTabelasClientesService = emitirTabelasClientesService;
	}

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}

}
