package com.mercurio.lms.vendas.model.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.ADSMInitArgs;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.MultiReportServiceSupport;
import com.mercurio.adsm.framework.report.ReportUploader;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.model.service.TabelaServicoAdicionalService;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.EmitirCapaAereoPDF;
import com.mercurio.lms.vendas.util.EmitirFreteAereoConvencionalPDF;
import com.mercurio.lms.vendas.util.EmitirFreteAereoEspecificaKiloExcedentePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteAereaClientePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteAereaPDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteMinimoProgressivoRotaClientePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteMinimoProgressivoTarifaRotaClientePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteMinimoProgressivoTarifaRotaDiferenciadaClientePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteMinimoRotaClientePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteMinimoRotaDifenciadaClientePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteMinimoTarifaClientePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFretePercentualClientePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaMinimoProgressivaClientePDF;
import com.mercurio.lms.vendas.util.TabelasClienteUtil;
import com.mercurio.lms.vendas.util.TemplatePdf;

/**
 * Classe de serviço para MultiReport de Tabelas de cliente
 *
 * @spring.bean id="lms.vendas.emitirTabelasClienteService"
 */
public class EmitirTabelasClientesService extends MultiReportServiceSupport {
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;
	private Map<String , String> dados = new HashMap<String, String>();
	private JdbcTemplate jdbcTemplate;
	private TabelaServicoAdicionalService tabelaServicoAdicionalService;
    private TabelasClienteService tabelasClienteService; 
	private ConfiguracoesFacade configuracoesFacade;

	private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	private static final String VALOR_POR_KG = "valorPorKg";
	private static final String VALOR_EXCEDENTE = "valorExcedente";
	private static final String CONVENCIONAL = "convencional";

	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}


	private TypedFlatMap getCommonParameter(Map param) {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idCliente", MapUtils.getLong(param, "idCliente"));
		parameters.put("idDivisao", MapUtils.getLong(param, "idDivisao"));
		parameters.put("idTabelaPreco", MapUtils.getLong(param, "idTabelaPreco"));
		parameters.put("idContato", MapUtils.getLong(param, "idContato"));
		return parameters;
	}

	public List findClientesParametrizados(TypedFlatMap parameters){
		return getEmitirTabelasClienteDAO().findClientesParametrizados(parameters);
	}

	private void groupParameters(String reportBeanId,MultiReportCommand mrc,List<Map> list,	TypedFlatMap parameters) {
		if(list.isEmpty()) {
			return;
		}

			TypedFlatMap parametros = new TypedFlatMap();
		List listaParametros = new ArrayList();
		boolean primeiro = true;
		Long idTabelaDivisao = null;
		for (Map map : list) {
			if(primeiro) {
				idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
				parametros = getCommonParameter(map);
				parametros.put("idTabelaDivisao", idTabelaDivisao);
				listaParametros.add(MapUtils.getLong(map,"listaParametros"));
				parametros.put("listaParametros", listaParametros);					
				primeiro = false;
			} else {
				if(!idTabelaDivisao.equals(MapUtils.getLong(map,"idTabelaDivisao"))) {
					parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "isTabelaNova"));
					mrc.addCommand(reportBeanId, parametros);//adiciona anterior

					idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
				parametros = getCommonParameter(map);
					parametros.put("idTabelaDivisao", idTabelaDivisao);
					listaParametros = new ArrayList();
					listaParametros.add(MapUtils.getLong(map,"listaParametros"));
					parametros.put("listaParametros", listaParametros);
				} else {
					idTabelaDivisao = MapUtils.getLong(map,"idTabelaDivisao");
					listaParametros.add(MapUtils.getLong(map,"listaParametros"));
					parametros.put("listaParametros", listaParametros);
				}
			}
		}
		parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "isTabelaNova"));
		mrc.addCommand(reportBeanId, parametros);
	}

	/**
	 * Verifica se as faixas_progressivas associadas a tabela de preco sao por volume
	 * @param idTabelaPreco
	 * @return
	 */
	public boolean hasFaixaPorVolume(Long idTabelaPreco){
		return getEmitirTabelasClienteDAO().hasFaixaPorVolume(idTabelaPreco);
			}

	/**
	 * Verifica se as faixas_progressivas associadas a tabela de preco tem rota 
	 * @param idTabelaPreco
	 * @return
	 */
	public boolean hasFaixaRota(Long idTabelaPreco){
		return getEmitirTabelasClienteDAO().hasFaixaRota(idTabelaPreco);
		}

	public List<Map> findTabelaFob(TypedFlatMap parameters, String tpTabelaPreco) {
		return getEmitirTabelasClienteDAO().findTabelaFobPacotinho(parameters, tpTabelaPreco);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param Instância do DAO.
	 */
	public void setEmitirTabelasClienteDAO(EmitirTabelasClienteDAO emitirTabelasClienteDAO) {
		this.emitirTabelasClienteDAO = emitirTabelasClienteDAO;
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
	 */
	private final EmitirTabelasClienteDAO getEmitirTabelasClienteDAO() {
		return emitirTabelasClienteDAO;
	}

	@Override
	protected MultiReportCommand prepareMultiReport(TypedFlatMap parameters) throws Exception {
		String reportFormat = parameters.getString(JRReportDataObject.EXPORT_MODE_PARAM);
		MultiReportCommand mrc = new MultiReportCommand("tabelaClientes", reportFormat);
		return mrc;
			}

	
	public void executeMontarParametrosAereoNovo(Long idTabelaPreco, Boolean isTabelaNova,Long idCliente, Long idDivisao, Long idParametro, Long idSimulacao,Map parametersReport, Map agrup) {
		this.montaParametrosRodoviario(idTabelaPreco, isTabelaNova, idCliente, idDivisao, idParametro, idSimulacao, parametersReport, agrup, jdbcTemplate, null, configuracoesFacade);
	}
	
	public void montaParametrosRodoviario(Long idTabelaPreco, Boolean isTabelaNova,Long idCliente, Long idDivisao, Long idParametro, Long idSimulacao,Map parametersReport, Map agrup, JdbcTemplate jdbcTemplate,String nomeTabela,ConfiguracoesFacade configuracoesFacade) {
		List listaFormalidades = null;
		if(idSimulacao != null){
			listaFormalidades = getTabelasClienteService().getFormalidadesPropostaRodoviarioNacional(idTabelaPreco, idCliente, idDivisao, idSimulacao, idParametro, jdbcTemplate);
		}else{
			//FIXME icaro.damiani Verificar idParametro
			listaFormalidades = getTabelasClienteService().getFormalidadesClienteRodoviarioNacional(idTabelaPreco, idCliente, idDivisao, idParametro, isTabelaNova, jdbcTemplate);
		}
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_FORMALIDADES, listaFormalidades);
		Long idServico = MapUtils.getLong(parametersReport,"idServico");

		String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco,getJdbcTemplate());
		List listaGeneralidades = new ArrayList();
		List listaServicoContratados = new ArrayList();
		List listaServicoAdicionais = new ArrayList();
		List listaDificuldadeEntrega = new ArrayList();
		List listGen = new ArrayList();
		Map controle = new HashMap();
		List despachos = new ArrayList();
		boolean localizado = false;
		List padraoGeneralidade = null;
		List padraoGeneralidadeEntrega = null;
		List padraoSevicosAdicionais = null;
		List padraoSevicosAdicionaisNaocontratados = null;
		for (int i = 0; i < agrup.size();i++) {
			localizado = false;
			String chave = "grupo"+i;

   			List chaves = (List) agrup.get(chave);
   			for (Object keyParametro : chaves) {
   				if(localizado){
   					break;
		}
   				Long idParametroCliente = (Long) keyParametro;
   				chaves = getTabelasClienteService().getSubRelGeneralidadesRota(idParametroCliente,idTabelaPreco,isTabelaNova,dsSimbolo,configuracoesFacade,jdbcTemplate,idDivisao);
   				
   				padraoGeneralidade = null;
   				padraoGeneralidadeEntrega = null;
   				padraoSevicosAdicionais = null;
   				padraoSevicosAdicionaisNaocontratados = null;
   				
   				padraoGeneralidade = chaves;
   				
   				List listaDE = getTabelasClienteService().getDificuldadeEntrega(idParametroCliente, idTabelaPreco, isTabelaNova, jdbcTemplate);
   				getTabelasClienteService().aplicaGeneralidade(listaDE, isTabelaNova, jdbcTemplate, idParametroCliente, configuracoesFacade, dsSimbolo);
   				
   				padraoGeneralidadeEntrega = listaDE;
   				
   	   	   		//List listaServicosContr = getSubServicoAdicionalContratado(idTabelaPreco,idDivisao,idSimulacao,dsSimbolo,configuracoesFacade,jdbcTemplate);
   				List listaServicosContr = null;
   	   	   		
   	   	   		//padraoSevicosAdicionais = listaServicoContratados;
   	   	   		
   				List listaServicosAd = getTabelasClienteService().getSubServicosAdicionais(idDivisao,idServico,idSimulacao,dsSimbolo,configuracoesFacade,jdbcTemplate, idParametroCliente);
   	   	   		
   	   	   		padraoSevicosAdicionaisNaocontratados = listaServicoAdicionais;
   				
				if (!localizado) {
					listaGeneralidades.add(chaves);
					if (listaDE != null && !listaDE.isEmpty()) {
						listaDificuldadeEntrega.add(listaDE);
					}
					if (!listaServicosAd.isEmpty()) {
						listaServicoAdicionais.add(listaServicosAd);
					}
					localizado = true;
				}
   			}
   			if(!localizado && i != 0){
   				if(!listaServicoAdicionais.isEmpty()){
   					listaServicoAdicionais.add(padraoSevicosAdicionaisNaocontratados.get(0));   					
   				}

   				if(!listaDificuldadeEntrega.isEmpty()){
   					listaDificuldadeEntrega.add(padraoGeneralidadeEntrega);   					
   				}

   				if(!listaGeneralidades.isEmpty()){
   					listaGeneralidades.add(padraoGeneralidade);   					
			}
		}
	}

		parametersReport.put(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA,listaDificuldadeEntrega);
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_GENERALIDADES,listaGeneralidades);
		parametersReport.put("SUBREPORTSERVICOADNAOCONTR",listaServicoAdicionais);

		List legenda = new ArrayList();
		List legendaTmp = new ArrayList();
		for (Iterator iter = listGen.iterator(); iter.hasNext();) {
			List lista = (List) iter.next();
			legendaTmp = ListUtils.union(legendaTmp,lista);
		}
		legenda = getTabelasClienteService().montaPageFooter(legendaTmp);
		parametersReport.put("PAGEFOOTER",legenda);

		Map listaHeader = (Map) getTabelasClienteService().montaHeader(parametersReport, jdbcTemplate, TabelasClienteService.RETORNO_LIST);
		parametersReport.put("dados", listaHeader);
	}

	public void montaParametrosAereo(Long idTabelaPreco,Long idCliente, Long idDivisaoCliente, Boolean isTabelaNova,Long idSimulacao, Map parametersReport, Map reportGroups, JdbcTemplate jdbcTemplate, ConfiguracoesFacade configuracoesFacade) {
		String keyGroup = null;
		List valuesGroup = null;
		Long idParametroCliente = null;

		//COLETA
		List coletaRota = new ArrayList();
		for (int i = 0; i < reportGroups.size(); i++) {
			keyGroup = "grupo" + i;
			valuesGroup = (List) reportGroups.get(keyGroup);
			idParametroCliente = (Long) valuesGroup.get(0);
			coletaRota.add(getTabelasClienteService().getSubRelColetaRota(idTabelaPreco, idParametroCliente, jdbcTemplate));
		}
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_COLETA,coletaRota);

		//ENTREGA
		List entregaRota = new ArrayList();
		for (int i = 0; i < reportGroups.size(); i++) {
			keyGroup = "grupo" + i;
			valuesGroup = (List) reportGroups.get(keyGroup);			
			idParametroCliente = (Long) valuesGroup.get(0);
			entregaRota.add(getTabelasClienteService().getSubRelEntregaRota(idTabelaPreco, idParametroCliente, jdbcTemplate));
		}
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_ENTREGA, entregaRota);

		//TAXA TERRESTRE
		idParametroCliente = null;
		List listTaxTerrestre = new ArrayList();
		List taxaTerrestre = null;
		for (int i = 0; i < reportGroups.size(); i++)
		{
			keyGroup = "grupo" + i;
			valuesGroup = (List) reportGroups.get(keyGroup);			
			idParametroCliente = (Long) valuesGroup.get(0);
			taxaTerrestre = getTabelasClienteService().getSubRelTaxaTerrestre(idTabelaPreco, idParametroCliente, jdbcTemplate);
			listTaxTerrestre.add(taxaTerrestre);
		}
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_TAXA_TERRESTRE,listTaxTerrestre);

		//TAXA COMBUSTIVEL
		List listTaxCombustivel = new ArrayList();
		for (int i = 0; i < reportGroups.size(); i++)
		{
			keyGroup = "grupo" + i;
			valuesGroup = (List) reportGroups.get(keyGroup);			
			idParametroCliente = (Long) valuesGroup.get(0);
			
			Map mapTaxCombustivel = getTabelasClienteService().getSubRelTaxaCombustivel(idTabelaPreco, idParametroCliente, jdbcTemplate);
			List taxaCombustivel = (List)mapTaxCombustivel.get("RESULT");
			Set subCrossTab         = (Set)mapTaxCombustivel.get("SUBCROSSTAB");
			parametersReport.put(TabelasClienteService.KEY_PARAMETER_TAXA_COMBUSTIVEL+ "CABECALHO", subCrossTab);
			
			if(taxaCombustivel !=null && !taxaCombustivel.isEmpty())
			{
				List listColumn = new ArrayList();
				listColumn.addAll(subCrossTab);
				String nomeSubRel = getTabelasClienteService().PATH_TABELAPRECOS + "report/subReportTaxaCombustivel_Portrait_ct_" + subCrossTab.size() + ".jasper";
				parametersReport.put("SUBREPORTTAXACOMBUSTIVEL_PATH", nomeSubRel);

				for(int j=0; j<subCrossTab.size();j++)
				{
					mapTaxCombustivel.put("COLUMN"+(j+1), listColumn.get(j));
				}
			}
			
			listTaxCombustivel.add(taxaCombustivel);
		}
	
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_TAXA_COMBUSTIVEL, listTaxCombustivel);

		//FORMALIDADES
		List formalidadesAereo = null;
		if(idSimulacao != null){
			formalidadesAereo = getTabelasClienteService().getFormalidadesPropostaAereoNacional(idTabelaPreco,idCliente, idDivisaoCliente, idParametroCliente, idSimulacao,
							configuracoesFacade, getJdbcTemplate());
					} else {
			formalidadesAereo = getTabelasClienteService().getFormalidadesClienteAereoNacional(idTabelaPreco,idCliente, idDivisaoCliente, idParametroCliente,configuracoesFacade,
							getJdbcTemplate());
					}
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_FORMALIDADES,  formalidadesAereo);

		//GENERALIDADES
		List listaGeneralidades = new ArrayList(reportGroups.size());
		List listaGeneralidadesMap = new ArrayList();
		
		List listGen = new ArrayList(reportGroups.size());
		String dsSimbolo = getTabelasClienteService().getMoeda(idTabelaPreco,getJdbcTemplate());
		for (int i = 0; i < reportGroups.size(); i++) {
			Map mapGeneralidades = new HashMap();
			keyGroup = "grupo"+i;
   			valuesGroup = (List) reportGroups.get(keyGroup);
   			idParametroCliente = (Long) valuesGroup.get(0);
   			valuesGroup = getTabelasClienteService().getSubRelGeneralidadesRota(idParametroCliente, idTabelaPreco, isTabelaNova,dsSimbolo, configuracoesFacade, jdbcTemplate,idDivisaoCliente);
   			listaGeneralidades.add(valuesGroup);
   			listGen.add(valuesGroup);
   			mapGeneralidades.put(keyGroup, valuesGroup);
   			listaGeneralidadesMap.add(mapGeneralidades);
				}
		parametersReport.put(TabelasClienteService.KEY_PARAMETER_GENERALIDADES, valuesGroup);

		//LEGENDA
		List legendas = new ArrayList();
		for (Iterator iter = listGen.iterator(); iter.hasNext();) {
			List lista = (List) iter.next();
			legendas = ListUtils.union(legendas,lista);
			}
		parametersReport.put("PAGEFOOTER",getTabelasClienteService().montaPageFooter(legendas));

		List<Map> generalidadesDificuldadeEntrega = getTabelasClienteService().getDificuldadeEntrega(idParametroCliente, idTabelaPreco, isTabelaNova, jdbcTemplate);
		
		/*//AD CONTRATADO
		List listaServicosContr = getSubServicoAdicionalContratado(idTabelaPreco, idDivisaoCliente, idSimulacao, dsSimbolo, configuracoesFacade, jdbcTemplate);
    	parametersReport.put(TabelasClienteUtil.KEY_PARAMETER_SERVICOSCONTRATADOS,listaServicosContr);*/

		//AD NÃO CONTRATADO
		List listaServicosAdicionais = tabelasClienteService.getSubServicosAdicionais(idDivisaoCliente,null,idSimulacao,dsSimbolo,configuracoesFacade,jdbcTemplate,idParametroCliente);
		parametersReport.put(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS,listaServicosAdicionais);
			}

	public void addSubReportTabelaFreteAereoTaxaMinimaPesoExcedente(TypedFlatMap parameters, List<TemplatePdf> templatesPdf,List<Map<String, String>> precos) {
		TemplatePdf tabelaFreteAereoTaxaMinimaPesoExcedente = null;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> formalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		dados = new HashMap<String, String>();
		
		int grupos = (Integer) parameters.get("grupos") == null ? 1 :(Integer) parameters.get("grupos");

		for (int i = 0; i < grupos; i++) {

			for (Map map : precos) {
				if (String.valueOf("grupo" + i).equals(String.valueOf(map.get("GRUPO")))) {
					precosList.add(map);
		}
	}
		}
		getDados(parameters);

		for (Map map : precos) {

			if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
				List<Map<String, String>> generalidadeGrupo = null;
				generalidadeGrupo = (List) getGeneralidades(map);
				Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
				g.put("grupo0" , normalizaGeneralidades(generalidadeGrupo));
				generalidadesList.add(g);
				break;
			}

			getDados(map);
			}

		for (Map map : precos) {

			if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
				Map<String, List<Map<String, String>>> f = new HashMap<String, List<Map<String, String>>>();
				List<Map<String, String>> formalidadesGrupo = normalizaFormalidades((List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
				f.put("grupo0", formalidadesGrupo);
				formalidadesList.add(f);
				break;
		}

	}

		for (Map map : precos) {

			if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
				Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
				dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
				if (!dificuldadeEntrega.isEmpty()) {
					List<Map<String, String>> dificuldadeGrupo = null;
					dificuldadeGrupo = (List) dificuldadeEntrega;
					dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
					d.put("grupo0", dificuldadeEntrega);
					dificuldadeEntregaList.add(d);
			}
				break;
				}

			}

		for (Map map : precos) {

			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
				Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
				if (!servicosAdicionais.isEmpty()) {
					
					List<Map<String, String>> servicosAdicionaisGrupo = null;
					servicosAdicionaisGrupo = (List) servicosAdicionais;
					
					servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
					sa.put("grupo0", servicosAdicionais);
					servicosAdicionaisList.add(sa);
		}
				break;
	}

		}

		for (Map map : precos) {
			if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
				Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map
						.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
				if (!servicosAdicionais.isEmpty()) {
					List<Map<String, String>> servicosAdicionaisGrupo = null;
					servicosAdicionaisGrupo = (List) servicosAdicionais;
					
					servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
					sa.put("grupo0", servicosAdicionais);
					servicosAdicionaisList.add(sa);
		}
				break;
	}
		}

		for (Map map : precos) {
			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
				Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
				if (!servicosAdicionais.isEmpty()) {
					List<Map<String, String>> servicosAdicionaisGrupo = null;
					servicosAdicionaisGrupo = (List) servicosAdicionais;
					servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
					sa.put("grupo0", servicosAdicionais);
					servicosAdicionaisList.add(sa);
				}
				break;
			}
			
				}

		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);
		List<Map<String, String>> taxaTerrestre = getTaxaTerrestre(parameters);
		List<Map<String, String>> coleta = getTaxaColeta(parameters);
		List<Map<String, String>> entrega = getTaxaEntrega(parameters);
		List<Map<String, String>> legendaGeneralidades = getLegendaGeneralidade(parameters);											
		
		addSimulacaoParameters(parameters);
		
		tabelaFreteAereoTaxaMinimaPesoExcedente = 				
		new EmitirTabelaFreteAereaClientePDF(cabecalho, dados, precosList, generalidadesList, formalidadesList, servicosAdicionais, dificuldadeEntrega, legendas, aereo, taxaCombustivel, taxaCombustivelCabecalho, coleta, entrega, legendaGeneralidades, taxaTerrestre);

		templatesPdf.add(tabelaFreteAereoTaxaMinimaPesoExcedente);
			}

	public void addSubReportTabelaFreteMinimoTarifa(TypedFlatMap parameters,
			List<TemplatePdf> templatesPdf, List<Map<String, String>> precos) {
		TemplatePdf tabelaFreteMinimoTarifa;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> formalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		dados = new HashMap<String, String>();
		
		int grupos = ((Integer) parameters.get("grupos") == null ? 1
				: (Integer) parameters.get("grupos"));

		getDados(parameters);
		
		filtraDados(precos, precosList, grupos);

		for (Map map : precos) {

			if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
				List<Map<String, String>> generalidadeGrupo = null;
				generalidadeGrupo = (List) getGeneralidades(map);
				Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
				g.put("grupo0", normalizaGeneralidades(generalidadeGrupo));
				generalidadesList.add(g);
				break;
		}
			
			getDados(map);
	}

		for (Map map : precos) {

			if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
				Map<String, List<Map<String, String>>> f = new HashMap<String, List<Map<String, String>>>();
				List<Map<String, String>> formalidadesGrupo = normalizaFormalidades((List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
				f.put("grupo0", formalidadesGrupo);
				formalidadesList.add(f);
				break;
			}

		}

		for (Map map : precos) {
			if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
				Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
				dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
				if (!dificuldadeEntrega.isEmpty()) {
					List<Map<String, String>> dificuldadeGrupo = null;
					dificuldadeGrupo = (List) dificuldadeEntrega;
					dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
					d.put("grupo0", dificuldadeEntrega);
					dificuldadeEntregaList.add(d);
					}
				break;
				}

			}

		for (Map map : precos) {
			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
				Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
				if (!servicosAdicionais.isEmpty()) {
					
					List<Map<String, String>> servicosAdicionaisGrupo = null;
					servicosAdicionaisGrupo = (List) servicosAdicionais;
					servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
					sa.put("grupo0", servicosAdicionais);
					servicosAdicionaisList.add(sa);
			}
				break;
		}

	}

		for (Map map : precos) {
			if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
				Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map
						.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
				if (!servicosAdicionais.isEmpty()) {
					List<Map<String, String>> servicosAdicionaisGrupo = null;
					servicosAdicionaisGrupo = (List) servicosAdicionais;

					servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
					sa.put("grupo0", servicosAdicionais);
					servicosAdicionaisList.add(sa);
				}
				break;
			}

			}

		for (Map map : precos) {
			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
				Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
				if (!servicosAdicionais.isEmpty()) {
					List<Map<String, String>> servicosAdicionaisGrupo = null;
					servicosAdicionaisGrupo = (List) servicosAdicionais;
					servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
					sa.put("grupo0", servicosAdicionais);
					servicosAdicionaisList.add(sa);
		}
				break;
	}

		}
		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);

		if(parameters.containsKey("simulacao") && parameters.get("simulacao") != null){
			Simulacao simulacao = (Simulacao) parameters.get("simulacao");
			dados.put("NR_SIMULACAO", simulacao.getNrSimulacao().toString());
	        dados.put("DT_SIMULACAO", simulacao.getDtSimulacao().toString());
	        dados.put("TP_SIMULACAO", simulacao.getTpSimulacao().getDescriptionAsString());
		}
		
		tabelaFreteMinimoTarifa = new EmitirTabelaFreteMinimoTarifaClientePDF(grupos, dados, precosList, generalidadesList,formalidadesList, servicosAdicionaisList,dificuldadeEntregaList, legendas);
		templatesPdf.add(tabelaFreteMinimoTarifa);
	}
			
	public void addSubReportTabelaFreteMinimoPesoExcedente(
			TypedFlatMap parameters, List<TemplatePdf> templatesPdf,
			List<Map<String, String>> precos) {
		TemplatePdf tabelaFreteMinimoPesoExcedenteService;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> formalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		
		dados = new HashMap<String, String>();
			
		int grupos = ((Integer) parameters.get("grupos") == null ? 1
				: (Integer) parameters.get("grupos"));
		getDados(parameters);
			
		filtraDados(precos, precosList, grupos);
			
		for (int i = 0; i < grupos; i++) {
			
		for (Map map : precos) {

				getDados(map);
				
				if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
					List<Map<String, String>> generalidadeGrupo = null;
					generalidadeGrupo = (List) getGeneralidades(map).get(i);
					Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
					g.put("grupo" + i, normalizaGeneralidades(generalidadeGrupo));
					generalidadesList.add(g);
					break;
					}
			}
			
			for (Map map : precos) {

			if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
					Map<String, List<Map<String, String>>> f = new HashMap<String, List<Map<String, String>>>();
					List<Map<String, String>> formalidadesGrupo = normalizaFormalidades((List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
					f.put("grupo" + i, formalidadesGrupo);
					formalidadesList.add(f);
					break;
				}
			}


			for (Map map : precos) {

			if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
					Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
					dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
				if (!dificuldadeEntrega.isEmpty()) {
						List<Map<String, String>> dificuldadeGrupo = null;
						dificuldadeGrupo = (List) dificuldadeEntrega.get(i);
						
						dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
						d.put("grupo" + i, dificuldadeEntrega);
						dificuldadeEntregaList.add(d);
			}
					break;
		}

			}

			for (Map map : precos) {
			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
					if (!servicosAdicionais.isEmpty()) {

						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
	}
					break;
			}
			}

			for (Map map : precos) {

			if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map
							.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
				if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
			}
					break;
		}
			}

			for (Map map : precos) {
			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map
							.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
				if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
	}
					break;
			}
		}
		}

		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);

		dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeEntrega);

		addSimulacaoParameters(parameters);
		
		// LMS-4526 - Busca mapeamento por grupo para cabeçalho
		// "CIF expedido FOB recebido" e produz relatório PDF
		Map<String, Boolean> mapCifFob = (Map<String, Boolean>) parameters.get("mapCifFob");
		tabelaFreteMinimoPesoExcedenteService = new EmitirTabelaFreteMinimoRotaClientePDF(
				grupos,
				dados,
				precosList,
				generalidadesList,
				formalidadesList,
				servicosAdicionaisList,
				dificuldadeEntregaList,
				legendas,
				true,
				mapCifFob);

		templatesPdf.add(tabelaFreteMinimoPesoExcedenteService);
			}

	public void addSubReportTabelaFreteMinimoProgressivoPesoExcedenteReport(
			TypedFlatMap parameters, List<TemplatePdf> templatesPdf,
			List<Map<String, String>> precos) {
		TemplatePdf tabelaFretePercentualServicePdf;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> formalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		int grupos = ((Integer) parameters.get("grupos") == null ? 1: (Integer) parameters.get("grupos"));
		dados = new HashMap<String, String>();
		
		filtraDados(precos, precosList, grupos);

		for (int i = 0; i < grupos; i++) {
			filtraSubReports(precos, generalidadesList, formalidadesList,
					dificuldadeEntregaList, servicosAdicionaisList, i, dados);
		}

		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);

		addSimulacaoParameters(parameters);
		
		tabelaFretePercentualServicePdf = new EmitirTabelaFreteMinimoProgressivoTarifaRotaDiferenciadaClientePDF(
				grupos, dados, precosList, generalidadesList,
				formalidadesList, servicosAdicionaisList,
				dificuldadeEntregaList, legendas);
		templatesPdf.add(tabelaFretePercentualServicePdf);

	}
	
	public void addCapaAereo(TypedFlatMap parameters, List<TemplatePdf> templatesPdf) {
		TemplatePdf capaAereoPDF;
		dados = new HashMap<String, String>();
		
		dados =	getTabelasClienteService().montaDadosCapaAereo((Long)parameters.get("idSimulacao"), jdbcTemplate);
		
		capaAereoPDF = new EmitirCapaAereoPDF(dados);
		templatesPdf.add(0, capaAereoPDF);
	}

	public void addSubreportConvencional(TypedFlatMap parameters, List<TemplatePdf> templatesPdf, List<Map<String, String>> precos) {
		TemplatePdf tabelaFretePercentualServicePdf;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> produtoEspecificoList = new ArrayList<Map<String, String>>();
		List<Map<String, Object>> faixaPesoList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> faixaPesoCabecalhoList = new ArrayList<Map<String, Object>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		dados = new HashMap<String, String>();

		int grupos = ((Integer) parameters.get("grupos") == null ? 1 : (Integer) parameters.get("grupos"));
		getDados(parameters);
		filtraDados(precos, precosList, grupos);
		filtraDadosSubgrupo(precos, produtoEspecificoList, grupos);
		filtraDadosFaixaPeso(precos, faixaPesoList, grupos);
		filtraDadosFaixaPesoCabecalho(precos, faixaPesoCabecalhoList, grupos);
		
		for (int i = 0; i < grupos; i++) {
			for (Map map : precos) {

				getDados(map);

				if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
					List<Map<String, String>> generalidadeGrupo = null;
					generalidadeGrupo = (List) getGeneralidades(map).get(i);
					Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
					g.put("grupo" + i, normalizaGeneralidades(generalidadeGrupo));
					generalidadesList.add(g);
					break;
				}
			}

			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
					Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
					dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
					if (!dificuldadeEntrega.isEmpty()) {
						List<Map<String, String>> dificuldadeGrupo = null;
						dificuldadeGrupo = (List) dificuldadeEntrega.get(i);

						dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
						d.put("grupo" + i, dificuldadeEntrega);
						dificuldadeEntregaList.add(d);
					}
					break;
				}
			}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
					if (!servicosAdicionais.isEmpty()) {

						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);
						;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}

			}

			for (Map map : precos) {
				if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}
			}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);
						;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}
			}
		}
		
		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> cabecalhoProdutoEspecifico = (List<Map<String, String>>) parameters.get("cabecalhoProdutoEspecifico");
		addSimulacaoParameters(parameters);
		
		tabelaFretePercentualServicePdf = new EmitirFreteAereoConvencionalPDF(grupos, dados, precosList, produtoEspecificoList, faixaPesoList, generalidadesList, servicosAdicionaisList, dificuldadeEntregaList, legendas, aereo, cabecalhoProdutoEspecifico, faixaPesoCabecalhoList);
		templatesPdf.add(tabelaFretePercentualServicePdf);
	}
	
	
	public void addSubreportEspecificoKiloExcedente(TypedFlatMap parameters, List<TemplatePdf> templatesPdf, List<Map<String, String>> precos) {
		TemplatePdf tabelaFretePercentualServicePdf;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> produtoEspecificoList = new ArrayList<Map<String, String>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		dados = new HashMap<String, String>();

		int grupos = ((Integer) parameters.get("grupos") == null ? 1 : (Integer) parameters.get("grupos"));
		getDados(parameters);
		filtraDados(precos, precosList, grupos);
		filtraDadosSubgrupo(precos, produtoEspecificoList, grupos);
		
		for (int i = 0; i < grupos; i++) {
			for (Map map : precos) {

				getDados(map);

				if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
					List<Map<String, String>> generalidadeGrupo = null;
					generalidadeGrupo = (List) getGeneralidades(map).get(i);
					Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
					g.put("grupo" + i, normalizaGeneralidades(generalidadeGrupo));
					generalidadesList.add(g);
					break;
				}
			}

			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
					Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
					dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
					if (!dificuldadeEntrega.isEmpty()) {
						List<Map<String, String>> dificuldadeGrupo = null;
						dificuldadeGrupo = (List) dificuldadeEntrega.get(i);

						dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
						d.put("grupo" + i, dificuldadeEntrega);
						dificuldadeEntregaList.add(d);
					}
					break;
				}
			}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
					if (!servicosAdicionais.isEmpty()) {

						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);
						;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}

			}

			for (Map map : precos) {
				if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}
			}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);
						;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}
			}
		}
		
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		addSimulacaoParameters(parameters);

		tabelaFretePercentualServicePdf = new EmitirFreteAereoEspecificaKiloExcedentePDF(grupos, dados, precosList, produtoEspecificoList, generalidadesList, servicosAdicionaisList, dificuldadeEntregaList);
		templatesPdf.add(tabelaFretePercentualServicePdf);
	}
	
	
	public void addSubreportTabelaFretePercentual(TypedFlatMap parameters,
			List<TemplatePdf> templatesPdf, List<Map<String, String>> precos) {
		TemplatePdf tabelaFretePercentualServicePdf;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> formalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		dados = new HashMap<String, String>();
		
		int grupos = ((Integer) parameters.get("grupos") == null ? 1
				: (Integer) parameters.get("grupos"));
		getDados(parameters);
		filtraDados(precos, precosList, grupos);

		for (int i = 0; i < grupos; i++) {
			for (Map map : precos) {
				
				getDados(map);
				
				if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
					List<Map<String, String>> generalidadeGrupo = null;
					generalidadeGrupo = (List) getGeneralidades(map).get(i);
					Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
					g.put("grupo" + i, normalizaGeneralidades(generalidadeGrupo));
					generalidadesList.add(g);
					break;
		}
	}

			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
					Map<String, List<Map<String, String>>> f = new HashMap<String, List<Map<String, String>>>();
					List<Map<String, String>> formalidadesGrupo = normalizaFormalidades((List<Map<String, String>>) map
							.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
					f.put("grupo" + i, formalidadesGrupo);
					formalidadesList.add(f);
					break;
				}
			}


			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
					Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
					dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
					if (!dificuldadeEntrega.isEmpty()) {
						List<Map<String, String>> dificuldadeGrupo = null;
						dificuldadeGrupo = (List) dificuldadeEntrega.get(i);
						
						dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
						d.put("grupo" + i, dificuldadeEntrega);
						dificuldadeEntregaList.add(d);
			}
					break;
		}
	}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
					if (!servicosAdicionais.isEmpty()) {

						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}

			}

			for (Map map : precos) {
				if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map
							.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
		}
					break;
	}
			}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map
							.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}
			}
		}
		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);

		addSimulacaoParameters(parameters);
		
		tabelaFretePercentualServicePdf = new EmitirTabelaFreteMinimoRotaDifenciadaClientePDF(
				grupos, dados, precosList, generalidadesList,
				formalidadesList, servicosAdicionaisList,
				dificuldadeEntregaList, legendas);
		templatesPdf.add(tabelaFretePercentualServicePdf);
	}

	public void addSubReportTabelaPercentual(TypedFlatMap parameters, List<TemplatePdf> templatesPdf, List<Map<String, String>> precos) {
		TemplatePdf tabelaFretePercentualServicePdf;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> formalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		dados = new HashMap<String, String>();
		
		int grupos = ((Integer) parameters.get("grupos") == null ? 1 : (Integer) parameters.get("grupos"));

		getDados(parameters);
		
		filtraDados(precos, precosList, grupos);

		for (int i = 0; i < grupos; i++) {
			
			for (Map map : precos) {
				
				getDados(map);
				
				if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
					List<Map<String, String>> generalidadeGrupo = null;
					generalidadeGrupo = (List) getGeneralidades(map).get(i);
					Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
					g.put("grupo" + i, normalizaGeneralidades(generalidadeGrupo));
					generalidadesList.add(g);
					break;
			}
		}

			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
					Map<String, List<Map<String, String>>> f = new HashMap<String, List<Map<String, String>>>();
					List<Map<String, String>> formalidadesGrupo = normalizaFormalidades((List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
					f.put("grupo" + i, formalidadesGrupo);
					formalidadesList.add(f);
					break;
	}
			}

			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
					Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
					dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
					if (!dificuldadeEntrega.isEmpty()) {
						List<Map<String, String>> dificuldadeGrupo = null;
						dificuldadeGrupo = (List) dificuldadeEntrega.get(i);
						
						dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
						d.put("grupo" + i, dificuldadeEntrega);
						dificuldadeEntregaList.add(d);
			}
					break;
		}

	}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
					if (!servicosAdicionais.isEmpty()) {

						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);
						;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
			}
					break;
		}
	}

			for (Map map : precos) {

				if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);
						;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
		}
					break;
	}
			}
	
			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);
						;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
		}
					break;
				}
			}
		}

		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);
		
		addSimulacaoParameters(parameters);
		boolean blPagaPesoExcedente = false;
		
		if (parameters.getBoolean("blPagaPesoExcedente") != null) {
			blPagaPesoExcedente = parameters.getBoolean("blPagaPesoExcedente");
		}

		// LMS-4526 - Busca mapeamento por grupo para cabeçalho
		// "CIF expedido FOB recebido" e produz relatório PDF
		Map<String, Boolean> mapCifFob = (Map<String, Boolean>) parameters.get("mapCifFob");
		tabelaFretePercentualServicePdf = new EmitirTabelaFreteMinimoRotaClientePDF(
				grupos,
				dados,
				precosList,
				generalidadesList,
				formalidadesList,
				servicosAdicionaisList,
				dificuldadeEntregaList,
				legendas,
				blPagaPesoExcedente,
				mapCifFob);
		templatesPdf.add(tabelaFretePercentualServicePdf);
			}

	public void addSubReportTabelaFretePercentual(TypedFlatMap parameters,
			List<TemplatePdf> templatesPdf, List<Map<String, String>> precos) {
		TemplatePdf tabelaFretePercentualServicePdf;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> formalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		dados = new HashMap<String, String>();
		
		int grupos = (Integer) parameters.get("grupos") != null ? (Integer) parameters.get("grupos") : 1;
		getDados(parameters);
		for (int i = 0; i < grupos; i++) {

			for (Map map : precos) {
				if (String.valueOf("grupo" + i).equals(String.valueOf(map.get("GRUPO")))) {
					precosList.add(map);
		}
	}
		}

		for (int i = 0; i < grupos; i++) {
	for (Map map : precos) {
				
				getDados(map);
				
				if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
					List<Map<String, String>> generalidadeGrupo = null;
					generalidadeGrupo = (List) getGeneralidades(map).get(i);
					Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
					g.put("grupo" + i, normalizaGeneralidades(generalidadeGrupo));
					generalidadesList.add(g);
					break;
	}

				
		}

			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
					Map<String, List<Map<String, String>>> f = new HashMap<String, List<Map<String, String>>>();
					List<Map<String, String>> formalidadesGrupo = normalizaFormalidades((List<Map<String, String>>) map
							.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
					f.put("grupo" + i, formalidadesGrupo);
					formalidadesList.add(f);
					break;
				}

			}


			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
					Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
					dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
					if (!dificuldadeEntrega.isEmpty()) {
						List<Map<String, String>> dificuldadeGrupo = null;
						dificuldadeGrupo = (List) dificuldadeEntrega.get(i);
						
						dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
						d.put("grupo" + i, dificuldadeEntrega);
						dificuldadeEntregaList.add(d);
		}
					break;
	}
			}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
					if (!servicosAdicionais.isEmpty()) {
						
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
	}
					break;
				}
			}

			for (Map map : precos) {

				if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map
							.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
	}
					break;
				}

	}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map
							.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
	}
					break;
				}
			}
		}

		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);
		// LMS-6170 - Busca mapeamento por grupo para cabeçalho
		// "CIF expedido FOB recebido" e produz relatório PDF
		Map<String, Boolean> mapCifFob = (Map<String, Boolean>) parameters.get("mapCifFob");
		
		addSimulacaoParameters(parameters);

		tabelaFretePercentualServicePdf = new EmitirTabelaFretePercentualClientePDF(grupos, dados, precosList, generalidadesList,formalidadesList, servicosAdicionaisList,dificuldadeEntregaList, legendas,mapCifFob);
		templatesPdf.add(tabelaFretePercentualServicePdf);
	}

	public void addSubReportTabelaMinimoProgressivoTaxaEmbutida(
			TypedFlatMap parameters, List<TemplatePdf> templatesPdf,
			List<Map<String, String>> precos) {
		TemplatePdf tabelaFreteMinimoProgressivoTaxaEmbutidaPDF;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		dados = new HashMap<String, String>();
		
		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		getDados(parameters);
		
		for (Map map : precos) {

			if (getGeneralidades(map) != null) {
				generalidades.addAll(getGeneralidades(map));
				generalidades = normalizaGeneralidades(generalidades);
			}

			getDados(map);

			if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
				formalidades.addAll((List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
				formalidades = normalizaFormalidades(formalidades);
			}

			if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
				dificuldadeEntrega = (List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
				dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeEntrega);
			}


			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {

				servicosAdicionais = (List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
				if (!servicosAdicionais.isEmpty()) {
					servicosList = normalizaServicosAdicionais(servicosAdicionais);
				}

			}

			if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {

				servicosAdicionais = (List<Map<String, String>>) map
						.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
				if (!servicosAdicionais.isEmpty()) {
					servicosList = normalizaServicosAdicionais(servicosAdicionais);
				}
			}

			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {

				servicosAdicionais = (List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
				if (!servicosAdicionais.isEmpty()) {
					servicosList = normalizaServicosAdicionais(servicosAdicionais);
				}

			}

		}

		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);

		dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeEntrega);

		addSimulacaoParameters(parameters);
		
		tabelaFreteMinimoProgressivoTaxaEmbutidaPDF = new EmitirTabelaMinimoProgressivaClientePDF(dados, precos, generalidades, formalidades,
				servicosList, dificuldadeEntrega, legendas);
		templatesPdf.add(tabelaFreteMinimoProgressivoTaxaEmbutidaPDF);
	}



	public void addReportTabelaMinimoProgressivoTarifa(TypedFlatMap parameters, List<TemplatePdf> templatesPdf,List<Map<String, String>> precos) {
		TemplatePdf tabelaFreteMinimoProgressivoTarifaPDF;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		dados = new HashMap<String, String>();
		
		getDados(parameters);

		for (Map map : precos) {
			
			if (getGeneralidades(map) != null) {
				generalidades.addAll(getGeneralidades(map));
				if (!generalidades.isEmpty()) {
					//TODO NÃO MUDAR
					generalidades = normalizaGeneralidades((List)generalidades);
	}

			}
		

			if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
				formalidades.addAll((List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
				if (!formalidades.isEmpty()) {
					formalidades = normalizaFormalidades(formalidades);
				}
			}

			if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
				dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
				if (!dificuldadeEntrega.isEmpty()) {
					dificuldadeEntrega = normalizaDificuldadeEntrega((List)dificuldadeEntrega);
				}
			}

			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {

				servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);

				if (!servicosAdicionais.isEmpty()) {
					servicosList = normalizaServicosAdicionais((List)servicosAdicionais);
				}

			}

			if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {

				servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);

				if (!servicosAdicionais.isEmpty() && servicosList.isEmpty()) {
					servicosList = (normalizaServicosAdicionais((List)servicosAdicionais));
				}
			}

			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null && servicosList.isEmpty()) {

				servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);

				if (!servicosAdicionais.isEmpty()) {
					servicosList.addAll(normalizaServicosAdicionais((List)servicosAdicionais.get(0)));
				}
			}

		}

		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);

		addSimulacaoParameters(parameters);

		tabelaFreteMinimoProgressivoTarifaPDF = new EmitirTabelaMinimoProgressivaClientePDF(
				dados, precos, generalidades, formalidades,
				servicosList, dificuldadeEntrega, legendas);

		templatesPdf.add(tabelaFreteMinimoProgressivoTarifaPDF);
	}

	public Map<String, String> getDados(Map map) {
		
		if(dados == null || dados.isEmpty()){
			if(dados.get("IDENTIFICACAO") == null){
				dados = null;
			}
			dados = (Map<String, String>) map.get("DADOS");
			if(dados == null || dados.isEmpty()){
				dados =	getTabelasClienteService().montaHeader((Long)map.get("idCliente"), (Long)map.get("idContato"), jdbcTemplate);
			}
		}if(dados.get("IDENTIFICACAO") == null || "".equals(dados.get("IDENTIFICACAO"))){
			dados = null;
			dados = (Map<String, String>) map.get("DADOS");
			if(dados == null || dados.isEmpty()){
				dados =	getTabelasClienteService().montaHeader((Long)map.get("idCliente"), (Long)map.get("idContato"), jdbcTemplate);
			}
		}
		if(map.get("OBSERVACAO") != null){			
			dados.put("obs", MapUtilsPlus.getString(map, "OBSERVACAO"));
		}
		return dados;
	}

	public void addReportTabelaFretePercentual(TypedFlatMap parameters,
			List<TemplatePdf> templatesPdf, List<Map<String, String>> precos) {
		TemplatePdf tabelaFretePercentualServicePdf;
		List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
		List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
		List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

		List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
		List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> formalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
		List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
		dados = new HashMap<String, String>();
		
		int grupos = ((Integer) parameters.get("grupos") == null ? 1
				: (Integer) parameters.get("grupos"));

		filtraDados(precos, precosList, grupos);

		for (int i = 0; i < grupos; i++) {
			
			for (Map map : precos) {
				
				getDados(map);
				
				if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
					List<Map<String, String>> generalidadeGrupo = null;
					generalidadeGrupo = (List) getGeneralidades(map).get(i);
					Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
					g.put("grupo" + i, normalizaGeneralidades(generalidadeGrupo));
					generalidadesList.add(g);
					break;
				}
				
				
			}

			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
					Map<String, List<Map<String, String>>> f = new HashMap<String, List<Map<String, String>>>();
					List<Map<String, String>> formalidadesGrupo = normalizaFormalidades((List<Map<String, String>>) map
							.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
					f.put("grupo" + i, formalidadesGrupo);
					formalidadesList.add(f);
					break;
				}

			}


			for (Map map : precos) {

				if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
					Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
					dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
					if (!dificuldadeEntrega.isEmpty()) {
						List<Map<String, String>> dificuldadeGrupo = null;
						dificuldadeGrupo = (List) dificuldadeEntrega.get(i);
						
						dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
						d.put("grupo" + i, dificuldadeEntrega);
						dificuldadeEntregaList.add(d);
					}
					break;
				}

			}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
					if (!servicosAdicionais.isEmpty()) {
						
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}
			}

			for (Map map : precos) {

				if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map
							.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}
			}

			for (Map map : precos) {
				if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
					Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
					servicosAdicionais = (List<Map<String, String>>) map
							.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
					if (!servicosAdicionais.isEmpty()) {
						List<Map<String, String>> servicosAdicionaisGrupo = null;
						servicosAdicionaisGrupo = (List) servicosAdicionais.get(i);;
						servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
						sa.put("grupo" + i, servicosAdicionais);
						servicosAdicionaisList.add(sa);
					}
					break;
				}
			}
		}

		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);

		addSimulacaoParameters(parameters);
		
		// LMS-6170 - Busca mapeamento por grupo para cabeçalho
		// "CIF expedido FOB recebido" e produz relatório PDF
		Map<String, Boolean> mapCifFob = (Map<String, Boolean>) parameters.get("mapCifFob");
		
		tabelaFretePercentualServicePdf = new EmitirTabelaFreteMinimoProgressivoRotaClientePDF(
				grupos, dados, precosList, generalidadesList,
				formalidadesList, servicosAdicionaisList,
				dificuldadeEntregaList, legendas, mapCifFob
				);
		
		templatesPdf.add(tabelaFretePercentualServicePdf);

	}

	private void addSimulacaoParameters(TypedFlatMap parameters) {
		if(parameters.containsKey("simulacao") && parameters.get("simulacao") != null){
			Simulacao simulacao = (Simulacao) parameters.get("simulacao");
			
			
			String dtSimulacao = simulacao.getDtSimulacao().toString("dd/MM/yyyy",LocaleContextHolder.getLocale());
			
			dados.put("SG_FILIAL", simulacao.getFilial().getSgFilial());
			dados.put("NR_SIMULACAO", simulacao.getNrSimulacao().toString());
	        dados.put("DT_SIMULACAO", dtSimulacao);
	        dados.put("TP_SIMULACAO", simulacao.getTpSituacaoAprovacao().getDescriptionAsString());
	        dados.put("ABRANGENCIA", parameters.getString("abrangencia"));
	        dados.put("LABEL_TIPO_PROPOSTA", getLabelTipoProposta(simulacao));
	        dados.put("sgAeroportoOrigem", parameters.getString("sgAeroportoOrigem"));
	        
	        if(simulacao.getProdutoEspecifico() != null){
	        	String nrTarifaEspecifica = "000" + simulacao.getProdutoEspecifico().getNrTarifaEspecifica();
	        	nrTarifaEspecifica = nrTarifaEspecifica.substring(nrTarifaEspecifica.length()-3, nrTarifaEspecifica.length());
	        	dados.put("dsProdutoEspecifico", "TE " + nrTarifaEspecifica);
	        }
		}
	}
	
	private String getLabelTipoProposta(Simulacao simulacao){
		if(simulacao.getTpGeracaoProposta() != null){
			if(ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_VALOR_KG.equals(simulacao.getTpGeracaoProposta().getValue())){
				return VALOR_POR_KG;
			}else if(ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_EXCEDENTE.equals(simulacao.getTpGeracaoProposta().getValue())){
				return VALOR_EXCEDENTE;
			}else if(ConstantesVendas.TP_PROPOSTA_CONVENCIONAL.equals(simulacao.getTpGeracaoProposta().getValue())){
				return CONVENCIONAL;
			}
		}
		return "";
	}
	
	private void filtraSubReports(
			List<Map<String, String>> precos,
			List<Map<String, List<Map<String, String>>>> generalidadesList,
			List<Map<String, List<Map<String, String>>>> formalidadesList,
			List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList,
			List<Map<String, List<Map<String, String>>>> servicosAdicionaisList,
			int i, Map<String, String> dados) {
		List<Map<String, String>> dificuldadeEntrega;
		List<Map<String, String>> servicosAdicionais;
		dados = new HashMap<String, String>();
		
		for (Map map : precos) {
			
			getDados(map);
			
			if (getGeneralidades(map) != null && !getGeneralidades(map).isEmpty()) {
				List<Map<String, String>> generalidadeGrupo = null;
				generalidadeGrupo = (List) getGeneralidades(map);
				Map<String, List<Map<String, String>>> g = new HashMap<String, List<Map<String, String>>>();
				g.put("grupo" + i, normalizaGeneralidades(generalidadeGrupo));
				generalidadesList.add(g);
				break;
			}
			
			
		}

		for (Map map : precos) {

			if (map.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES) != null) {
				Map<String, List<Map<String, String>>> f = new HashMap<String, List<Map<String, String>>>();
				List<Map<String, String>> formalidadesGrupo = normalizaFormalidades((List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES));
				f.put("grupo" + i, formalidadesGrupo);
				formalidadesList.add(f);
				break;
			}

		}


		for (Map map : precos) {

			if (map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA) != null) {
				Map<String, List<Map<String, String>>> d = new HashMap<String, List<Map<String, String>>>();
				dificuldadeEntrega = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
				if (!dificuldadeEntrega.isEmpty()) {
					List<Map<String, String>> dificuldadeGrupo = null;
					dificuldadeGrupo = (List) dificuldadeEntrega;
					
					dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeGrupo);
					d.put("grupo" + i, dificuldadeEntrega);
					dificuldadeEntregaList.add(d);
				}
				break;
			}

		}

		for (Map map : precos) {
			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD) != null) {
				Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
				if (!servicosAdicionais.isEmpty()) {
					
					List<Map<String, String>> servicosAdicionaisGrupo = null;
					servicosAdicionaisGrupo = (List) servicosAdicionais;
					
					servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
					sa.put("grupo" + i, servicosAdicionais);
					servicosAdicionaisList.add(sa);
				}
				break;
			}

		}

		for (Map map : precos) {
			if (map.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS) != null) {
				Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map
						.get(TabelasClienteUtil.KEY_PARAMETER_SERVICOSADICIONAIS);
				if (!servicosAdicionais.isEmpty()) {
					List<Map<String, String>> servicosAdicionaisGrupo = null;
					servicosAdicionaisGrupo = (List) servicosAdicionais;
					
					servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
					sa.put("grupo" + i, servicosAdicionais);
					servicosAdicionaisList.add(sa);
				}
				break;
			}

		}

		for (Map map : precos) {

			if (map.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS) != null) {
				Map<String, List<Map<String, String>>> sa = new HashMap<String, List<Map<String, String>>>();
				servicosAdicionais = (List<Map<String, String>>) map
						.get(TabelasClienteService.KEY_PARAMETER_SERVICOSCONTRATADOS);
				if (!servicosAdicionais.isEmpty()) {
					List<Map<String, String>> servicosAdicionaisGrupo = null;
					servicosAdicionaisGrupo = (List) servicosAdicionais;
					servicosAdicionais = normalizaServicosAdicionais(servicosAdicionaisGrupo);
					sa.put("grupo" + i, servicosAdicionais);
					servicosAdicionaisList.add(sa);
				}
				break;
			}

		}
	}
	
	private void filtraDadosFaixaPesoCabecalho(List<Map<String, String>> precos, List<Map<String, Object>> grupoFaixaPesoCabecalhoList, int grupos) {
		for (int i = 0; i < grupos; i++) {

			for (Map map : precos) {
				if (String.valueOf("grupofaixapesocabecalho" + i).equals(String.valueOf(map.get("GRUPOFAIXAPESOCABECALHO"))) || String.valueOf("grupofaixapesocabecalho" + i).equals(String.valueOf(map.get("grupofaixapesocabecalho")))) {
					grupoFaixaPesoCabecalhoList.add(map);
				}
			}
		}
	}
	
	private void filtraDadosFaixaPeso(List<Map<String, String>> precos, List<Map<String, Object>> grupoFaixaPesoList, int grupos) {
		for (int i = 0; i < grupos; i++) {
			
			for (Map map : precos) {
				if (String.valueOf("grupofaixapeso" + i).equals(String.valueOf(map.get("GRUPOFAIXAPESO"))) || String.valueOf("grupofaixapeso" + i).equals(String.valueOf(map.get("grupofaixapeso")))) {
					grupoFaixaPesoList.add(map);
				}
			}
		}
	}

	private void filtraDadosSubgrupo(List<Map<String, String>> precos, List<Map<String, String>> subgrupoList, int grupos) {
		for (int i = 0; i < grupos; i++) {

			for (Map map : precos) {
				if (String.valueOf("subgrupo" + i).equals(String.valueOf(map.get("SUBGRUPO"))) || String.valueOf("subgrupo" + i).equals(String.valueOf(map.get("subgrupo")))) {
					subgrupoList.add(map);
				}
			}
		}
	}
	
	private void filtraDados(List<Map<String, String>> precos,
			List<Map<String, String>> precosList, int grupos) {
		for (int i = 0; i < grupos; i++) {

			for (Map map : precos) {
				if (String.valueOf("grupo" + i).equals(String.valueOf(map.get("GRUPO"))) || String.valueOf("grupo" + i).equals(String.valueOf(map.get("grupo")))) {
					precosList.add(map);
				}
			}
		}
	}

	public List<Map<String, String>> normalizaServicosAdicionais(
			List<Map<String, String>> servicosAdicionais) {
		if (servicosAdicionais.size() == 0) {
			return servicosAdicionais;
			}
		List<Map<String, String>> dificuldadeEntregaNova = new ArrayList<Map<String,String>>();

		Map<String, String> mapTextos = new HashMap<String, String>();

		mapTextos.put("label", TemplatePdf.getDefaultResourceBundle().getString("servAdicionaisTexto"));
		mapTextos.put(TemplatePdf.ALIGN, TemplatePdf.ALIGN_CENTER);
		
		//flag para alinhar o texto + valor a esquerda.
		for (Map<String, String> map : servicosAdicionais) {
			map.put(TemplatePdf.ALIGN, TemplatePdf.ALIGN_LEFT);
		}
		//adiciona texto central no inicio do relatorio de serv.ad.
		dificuldadeEntregaNova.add(mapTextos);
		dificuldadeEntregaNova.addAll(servicosAdicionais);
		return dificuldadeEntregaNova;
	}

	public List<Map<String, String>> normalizaValoresAereo(
			List<Map<String, String>> precos) {
		List<Map<String, String>> valores = new ArrayList<Map<String, String>>();
		Map<String, String> mapValue = null;
		String sigla = "";
		for (Map<String, String> map1 : precos) {
			mapValue = new HashedMap();
			mapValue.put("sigla", String.valueOf(map1.get("SIGLA")));
			mapValue.put("tarifa", String.valueOf(map1.get("CD_TARIFA")));
			mapValue.put("destino", String.valueOf(map1.get("DESTINO")));
			mapValue.put("origem", String.valueOf(map1.get("ORIGEM")));
			mapValue.put("valor", String.valueOf(map1.get("VALOR_FIXO")));
			mapValue.put("fretePorKg", String.valueOf(map1.get("FRETEPORKG")));
			mapValue.put("advalorem", String.valueOf(map1.get("VL_ADVALOREM")));
			mapValue.put(map1.get("C_HEADER"),
					String.valueOf(map1.get("VALOR_FIXO")));
			valores.add(mapValue);
		}
		return valores;
	}

	public List<Map<String, String>> getFormalidades(TypedFlatMap parameters) {
		List<Map<String, String>> formalidades = (List<Map<String, String>>) parameters	.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES);
		return formalidades;
	}

	public List<Map<String, String>> getLegendaGeneralidade(
			TypedFlatMap parameters) {
		List<Map<String, String>> legendaGeneralidades = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_LEGENDA_GENERALIDADE);
		return legendaGeneralidades;
	}

	public List<Map<String, String>> getTaxaEntrega(TypedFlatMap parameters) {
		List<Map<String, String>> entrega = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_ENTREGA);
		return entrega;
	}

	public List<Map<String, String>> getTaxaColeta(TypedFlatMap parameters) {
		List<Map<String, String>> coleta = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_COLETA);
		return coleta;
	}

	public Set getCabecalhoTaxaCombustivel(TypedFlatMap parameters) {
		Set taxaCombustivelCabecalho = (Set) parameters
				.get(TabelasClienteService.KEY_PARAMETER_TAXA_COMBUSTIVEL+ "CABECALHO");
		return taxaCombustivelCabecalho;
	}

	public List<Map<String, String>> getTaxaTerrestre(TypedFlatMap parameters) {
		List<Map<String, String>> taxaTerrestre = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_TAXA_TERRESTRE);
		return taxaTerrestre;
	}

	public List<Map<String, String>> getTaxaCombustivel(TypedFlatMap parameters) {
		List<Map<String, String>> taxaCombustivel = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_TAXA_COMBUSTIVEL);
		return taxaCombustivel;
	}

	public List<Map<String, String>> getAereo(TypedFlatMap parameters) {
		List<Map<String, String>> aereo = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_AEREO);
		return aereo;
	}

	public List<Map<String, String>> getCabecalho(TypedFlatMap parameters) {
		List<Map<String, String>> cabecalho = (List<Map<String, String>>) parameters
				.get("HEADER");
		return cabecalho;
	}

	public List<Map<String, String>> getLegendas(TypedFlatMap parameters) {
		List<Map<String, String>> legendas = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_LEGENDAS);
		return legendas;
	}

	public List<Map<String, String>> getDificuldadeEntrega(
			TypedFlatMap parameters) {
		List<Map<String, String>> dificuldadeEntrega = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_DIFICULDADE_ENTREGA);
		return dificuldadeEntrega;
	}

	public List<Map<String, String>> getServicosAdicionais(
			TypedFlatMap parameters) {
		List<Map<String, String>> servicosAdicionais = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_SERVICOSAD);
		return servicosAdicionais;
	}

	public List<Map<String, String>> getFormalidadesAereo(
			TypedFlatMap parameters) {
		List<Map<String, String>> formalidades = (List<Map<String, String>>) parameters
				.get(TabelasClienteService.KEY_PARAMETER_FORMALIDADES_AEREO);
		return formalidades;
	}

	public List<Map<String, String>> getGeneralidades(Map parameters) {
		List<Map<String, String>> generalidades = (List<Map<String, String>>) parameters.get(TabelasClienteService.KEY_PARAMETER_GENERALIDADES);
		return generalidades;
	}

	public List<Map<String, String>> normalizaDificuldadeEntrega(
			List<Map<String, String>> dificuldadeEntrega) {
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		
				
		if(CollectionUtils.isNotEmpty(dificuldadeEntrega)){
			if(MapUtils.isNotEmpty(dificuldadeEntrega.get(0)) && (dificuldadeEntrega.get(0).containsKey("linha") || dificuldadeEntrega.get(0).containsKey("label"))){
				return dificuldadeEntrega;
			}
			
			Map<String, String> map = new HashMap();
			map.put("linha", TemplatePdf.getDefaultResourceBundle().getString("dificuldadeEntregaTexto"));
			result.add(map);
				
			if (MapUtils.isNotEmpty(dificuldadeEntrega.get(0)) && StringUtils.isNotBlank(dificuldadeEntrega.get(0).get("OB_TABELA_PRECO"))) {
				Map<String, String> mapObs = new HashMap();
				mapObs.put("linha", dificuldadeEntrega.get(0).remove("OB_TABELA_PRECO"));
				
				result.add(mapObs);
				}
				
			result.addAll(normalizaGeneralidades(dificuldadeEntrega));
			}
		return result;
			}

	public List<Map<String, String>> normalizaFormalidades(
			List<Map<String, String>> formalidades) {
		List<Map<String, String>> formalidade = new ArrayList<Map<String, String>>();

		
		if (CollectionUtils.isNotEmpty(formalidades)) {
			Map map = formalidades.get(0);
			if(map.containsKey("label") && map.containsKey("valor")){
				return formalidades;
			}
			Map<String, String> mapCubagem = new HashMap<String, String>();
			StringBuilder fatorCubagem = new StringBuilder(configuracoesFacade.getMensagem("fatorCubagemCamelCase")).append(":");
			mapCubagem.put("label", fatorCubagem.toString());
			mapCubagem.put("valor", MapUtilsPlus.getString(map, "FATOR_CUBAGEM"));
			formalidade.add(mapCubagem);

			Map<String, String> mapDensidade = new HashMap<String, String>();
			StringBuilder fatorDensidade = new StringBuilder(configuracoesFacade.getMensagem("fatorDensidadeCamelCase")).append(":");
			mapDensidade.put("label", fatorDensidade.toString());
			mapDensidade.put("valor", MapUtilsPlus.getString(map, "FATOR_DENSIDADE"));
			formalidade.add(mapDensidade);

			//LIMITES
			if(map.get("NR_LIMITE_METRAGEM_CUBICA") != null){
				Map<String, String> mapLimiteMetragemCubica = new HashMap<String, String>();
				StringBuilder limiteMetragemCubica = new StringBuilder(configuracoesFacade.getMensagem("limiteMetragemCubicaCamelCase")).append(":");
				mapLimiteMetragemCubica.put("label", limiteMetragemCubica.toString());
				mapLimiteMetragemCubica.put("valor", "Até " + String.format("%.3f", MapUtilsPlus.getBigDecimal(map, "NR_LIMITE_METRAGEM_CUBICA")) + " m³");
				formalidade.add(mapLimiteMetragemCubica);
			}
			
			if(map.get("NR_LIMITE_QUANT_VOLUME") != null){
				Map<String, String> mapLimiteQuantVolume = new HashMap<String, String>();
				StringBuilder limiteQuantVolume = new StringBuilder(configuracoesFacade.getMensagem("limiteQuantVolumeCamelCase")).append(":");
				mapLimiteQuantVolume.put("label", limiteQuantVolume.toString());
				mapLimiteQuantVolume.put("valor", "Até " + MapUtilsPlus.getBigDecimal(map, "NR_LIMITE_QUANT_VOLUME").toString() + " vol(s)");
				formalidade.add(mapLimiteQuantVolume);
			}

			Map<String, String> mapPeriodicidade = new HashMap<String, String>();
			mapPeriodicidade.put("label", configuracoesFacade.getMensagem("frequenciaFaturamentoCamelCase")+":");
			mapPeriodicidade.put("valor",MapUtilsPlus.getString(map, "PERIODICIDADE"));
			formalidade.add(mapPeriodicidade);
			
			Map<String, String> mapViegencia = new HashMap<String, String>();
			mapViegencia.put("label", TemplatePdf.getDefaultResourceBundle().getString("vigencia"));
			
			if(map.get("DT_VIGENCIA_INICIAL") instanceof Timestamp){
				mapViegencia.put("valor",FORMATTER.format((Timestamp) map.get("DT_VIGENCIA_INICIAL")));
			} else {
				mapViegencia.put("valor", (String) map.get("DT_VIGENCIA_INICIAL"));
			}
			formalidade.add(mapViegencia);

			Map<String, String> mapPrazoCobranca = new HashMap<String, String>();
			mapPrazoCobranca.put("label",  configuracoesFacade.getMensagem("prazoPagamentoCamelCase")+":");
			mapPrazoCobranca.put("valor", MapUtilsPlus.getString(map, "NR_PRAZO_COBRANCA") + " dias");
			formalidade.add(mapPrazoCobranca);

			Map<String, String> mapDescricao = new HashMap<String, String>();
			mapDescricao.put("label", TemplatePdf.getDefaultResourceBundle().getString("tipoTabela"));
			mapDescricao.put("valor",MapUtilsPlus.getString(map, "TIPO_TABELA"));
			formalidade.add(mapDescricao);
			
			Map<String, String> mapDivisao = new HashMap<String, String>();
			mapDivisao.put("label", TemplatePdf.getDefaultResourceBundle().getString("divisao"));
			mapDivisao.put("valor",MapUtilsPlus.getString(map, "NM_DIVISAO"));
			formalidade.add(mapDivisao);

			Map<String, String> mapTipoReajuste = new HashMap<String, String>();
			mapTipoReajuste.put("label", TemplatePdf.getDefaultResourceBundle().getString("tipoReajuste"));
			String reajuste = "";
			if("N".equals(MapUtilsPlus.getString(map, "TP_REAJUSTE"))){
				reajuste = TemplatePdf.getDefaultResourceBundle().getString("manual");
			}else{
				reajuste = TemplatePdf.getDefaultResourceBundle().getString("automatico");
			}
			
			mapTipoReajuste.put("valor",reajuste);
			formalidade.add(mapTipoReajuste);
			
			
			Map<String, String> mapicms = new HashMap<String, String>();
			mapicms.put("linha", TemplatePdf.getDefaultResourceBundle().getString("icmsNaoInclusoObs"));
			formalidade.add(mapicms);
			

			Map<String, String> mapResponsabilidae = new HashMap<String, String>();
			mapResponsabilidae.put("linha", TemplatePdf.getDefaultResourceBundle().getString("dadosCadastraisObs"));
			formalidade.add(mapResponsabilidae);
			
			
			if(map.get("DAC") != null){				
				Map<String, String> dac = new HashMap<String, String>();
				dac.put("linha", TemplatePdf.getDefaultResourceBundle().getString("dacOBs"));
				formalidade.add(dac);
			}
			
			if(map.get("SUFRAMA") != null){				
				Map<String, String> suframa = new HashMap<String, String>();
				suframa.put("linha", TemplatePdf.getDefaultResourceBundle().getString("cargaManausObs"));
				formalidade.add(suframa);
			}
			
			if(map.get("SUFRAMA") != null){				
				Map<String, String> redespachofluvial = new HashMap<String, String>();
				redespachofluvial.put("linha", TemplatePdf.getDefaultResourceBundle().getString("redespachoObs"));
				formalidade.add(redespachofluvial);
			}
			
			if(map.get("nfdatatranporte") != null){				
				Map<String, String> nfdatatranporte = new HashMap<String, String>();
				nfdatatranporte.put("linha", TemplatePdf.getDefaultResourceBundle().getString("nfDataTransporteObs"));
				formalidade.add(nfdatatranporte);
			}
			
			List<Map<String, String>> retirar = new ArrayList<Map<String,String>>();
			for (Map<String, String> f : formalidade) {
				if(f.get("linha") == null && f.get("valor") == null || "".equals(f.get("valor"))){
					retirar.add(f);
				}
			}
			formalidade.removeAll(retirar);

		}
		return formalidade;
	}

	public List<Map<String, String>> normalizaGeneralidades(List<Map<String, String>> generalidades) {
		String vlDevolucao = "";
		String vlReentrega = "";
		String vlmnimo = "";
		String nmnimo = "";
		List<Map<String, String>> subGeneralidades = new ArrayList<Map<String,String>>();
		
		if (generalidades != null && !generalidades.isEmpty()) {
			for (Map m : generalidades) {
				String oN = (String)m.remove("NOMEGENERALIDADES");
				String oV = (String)m.remove("VALORGENERALIDADES");
				String oM = (String)m.remove("MINIMOGENERALIDADES");
				if (oN != null || oV != null) {
					Map<String, String> map = new HashMap();
					map.put("label", oN);
					map.put("valor", oV);
					subGeneralidades.add(map);
				}
				if (m.get("DEVOLUCAO") != null) {
					vlDevolucao = (String) m.get("DEVOLUCAO");
			Map<String, String> devolucao = new HashMap();
			devolucao.put("label", TemplatePdf.getDefaultResourceBundle().getString("devolucao"));
			devolucao.put("valor", vlDevolucao);
					subGeneralidades.add(devolucao);
				}
				if (m.get("REENTREGA") != null) {
					vlReentrega = (String) m.get("REENTREGA");
			Map<String, String> reentrega = new HashMap();
			reentrega.put("label", TemplatePdf.getDefaultResourceBundle().getString("reentrega"));
			reentrega.put("valor", vlReentrega);
					subGeneralidades.add(reentrega);
		}
				if (m.get("VALORMINIMO") != null) {
					vlmnimo = (String) m.get("VALORMINIMO");
					nmnimo = (String)m.get("NOMEMINIMO");
					Map<String, String> minimo = new HashMap();
					minimo.put("label", nmnimo);
					minimo.put("valor", vlmnimo);
					subGeneralidades.add(minimo);
	}
			}
		}
		return subGeneralidades;
	}

	public List<Map<String, String>> normalizaValores(List<Map<String, String>> precos) {
		
		List<Map<String, String>> valores = new ArrayList<Map<String, String>>();
		Map<String, String> mapValue = null;
		String sigla = "";
		for (Map<String, String> map1 : precos) {
			if (!sigla.equals(map1.get("SIGLA"))) {
				mapValue = new HashedMap();
				sigla = map1.get("SIGLA");
				mapValue.put("sigla", sigla);
				mapValue.put("taxaMinima",
						String.valueOf(map1.get("TAXA_MINIMA")));
				mapValue.put("destino", String.valueOf(map1.get("DESTINO")));
				mapValue.put("origem", String.valueOf(map1.get("ORIGEM")));
				mapValue.put("ciaAerea", String.valueOf(map1.get("CIA_AEREA")));
				mapValue.put("subtipo", String.valueOf(map1.get("SUBTIPO")));
				valores.add(mapValue);
			}
			if (map1.get("C_HEADER") != null) {
				mapValue.put(String.valueOf(map1.get("C_HEADER")),
						String.valueOf(map1.get("DEST_X_FAIXA")));
			}

			if (map1.get("PRODUTO_ESPECIFICO") != null) {
				mapValue.put(String.valueOf(map1.get("PRODUTO_ESPECIFICO")),
						String.valueOf(map1.get("DEST_X_FAIXA")));
			}
			sigla = map1.get("SIGLA");
		}
		return valores;
	}

	public TypedFlatMap validateImpressao(TypedFlatMap data) {
		if (data.get("idAeroportoOrigem") != null
				&& data.get("idAeroportoDestino") != null) {
			throw new BusinessException("LMS-01040",
					new Object[] { data.getString("label") });
		}
		return data;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void addSubReporttabelaFreteDiferenciada(TypedFlatMap parameters,
			List<TemplatePdf> templatesPdf, List<Map<String, String>> precos) {
			TemplatePdf tabelaFretePercentualServicePdf;
			List<Map<String, String>> generalidades = new ArrayList<Map<String, String>>();
			List<Map<String, String>> formalidades = new ArrayList<Map<String, String>>();
			List<Map<String, String>> dificuldadeEntrega = new ArrayList<Map<String, String>>();
			List<Map<String, String>> servicosAdicionais = new ArrayList<Map<String, String>>();

			List<Map<String, String>> servicosList = new ArrayList<Map<String, String>>();
			List<Map<String, String>> precosList = new ArrayList<Map<String, String>>();
			List<Map<String, List<Map<String, String>>>> generalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
			List<Map<String, List<Map<String, String>>>> formalidadesList = new ArrayList<Map<String, List<Map<String, String>>>>();
			List<Map<String, List<Map<String, String>>>> dificuldadeEntregaList = new ArrayList<Map<String, List<Map<String, String>>>>();
			List<Map<String, List<Map<String, String>>>> servicosAdicionaisList = new ArrayList<Map<String, List<Map<String, String>>>>();
			int grupos = ((Integer) parameters.get("grupos") == null ? 1 : (Integer) parameters.get("grupos"));

			dados = new HashMap<String, String>();
			
			filtraDados(precos, precosList, grupos);

			for (int i = 0; i < grupos; i++) {
				filtraSubReports(precos, generalidadesList, formalidadesList,
						dificuldadeEntregaList, servicosAdicionaisList, i, dados);
			}

			List<Map<String, String>> legendas = getLegendas(parameters);
			List<Map<String, String>> cabecalho = getCabecalho(parameters);
			List<Map<String, String>> aereo = getAereo(parameters);
			List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
			Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);

			tabelaFretePercentualServicePdf = new EmitirTabelaFreteMinimoProgressivoTarifaRotaClientePDF(
					grupos, dados, precosList, generalidadesList,
					formalidadesList, servicosAdicionaisList,
					dificuldadeEntregaList, legendas);
			templatesPdf.add(tabelaFretePercentualServicePdf);
		
	}

	public void addSubReportEmitirTabelaFreteAereoPadrao(TypedFlatMap parameters, List<TemplatePdf> templatesPdf,List<Map<String, String>> precos) {		TemplatePdf emitirTabelaFreteAereo;
		List<Map<String, String>> generalidades = getGeneralidades(parameters);
		List<Map<String, String>> formalidades = getFormalidades(parameters);
		List<Map<String, String>> servicosAdicionais = getServicosAdicionais(parameters);
		List<Map<String, String>> dificuldadeEntrega = getDificuldadeEntrega(parameters);
		List<Map<String, String>> legendas = getLegendas(parameters);
		List<Map<String, String>> cabecalho = getCabecalho(parameters);
		List<Map<String, String>> aereo = getAereo(parameters);
		List<Map<String, String>> taxaCombustivel = getTaxaCombustivel(parameters);
		List<Map<String, String>> taxaTerrestre = getTaxaTerrestre(parameters);
		Set taxaCombustivelCabecalho = getCabecalhoTaxaCombustivel(parameters);				
		List<Map<String, String>> coleta = getTaxaColeta(parameters);
		List<Map<String, String>> entrega = getTaxaEntrega(parameters);
		List<Map<String, String>> legendaGeneralidades = getLegendaGeneralidade(parameters);											
		List<Map<String, String>> valores = normalizaValores(precos);
		Map<String,String> dados = getDados(parameters);
		dados = new HashMap<String, String>();
	
		generalidades = normalizaGeneralidades(generalidades);
		
		List<Map<String, String>> formalidade = normalizaFormalidades(formalidades);
		
		dificuldadeEntrega = normalizaDificuldadeEntrega(dificuldadeEntrega);
		
		emitirTabelaFreteAereo = new EmitirTabelaFreteAereaPDF(cabecalho,dados,valores, generalidades, formalidade,servicosAdicionais,dificuldadeEntrega,legendas,aereo,taxaCombustivel,taxaCombustivelCabecalho,coleta,entrega,legendaGeneralidades,taxaTerrestre);
	
		templatesPdf.add(emitirTabelaFreteAereo);
		
	}	
		
	/*
	 * Foi feito da mesma maneira que faz a classe ReportExecutionManager, pois os metodos são private, e nesse caso não envolve jasper
	 * 
	 */
	public URL getReportLocaleStore(File reportFile) {
		try {
			String reportHostUrl = ADSMInitArgs.ADSM_REPORT_HOST.getValue();
			if (StringUtils.isBlank(reportHostUrl)) {
				reportHostUrl = "http://lms02a/lmsa"; 
			}
	
			ReportUploader reportUploader = new ReportUploader(jdbcTemplate);
	
			long expireTime = System.currentTimeMillis() + (1000 * 60 * ( 48 + 60 ));
	
			final String reportLocator = reportUploader.upload(reportFile,
					expireTime);
	
			String ts = Base64Util.encode(Long.toString(expireTime).getBytes());

			String locatorStr = "locator="
					+ java.net.URLEncoder.encode(reportLocator, "UTF-8")
					+ "&ts=" + java.net.URLEncoder.encode(ts, "UTF-8");
			String stringUrl = reportHostUrl+"/viewBatchReport?"+locatorStr;
				return new URL(stringUrl);			
		} catch (UnsupportedEncodingException e) {
			throw new InfrastructureException(e);
		} catch (MalformedURLException e) {
			throw new InfrastructureException(e);
		}
	}
	public TabelaServicoAdicionalService getTabelaServicoAdicionalService() {
	    return tabelaServicoAdicionalService;
	}
	public void setTabelaServicoAdicionalService(TabelaServicoAdicionalService tabelaServicoAdicionalService) {
	    this.tabelaServicoAdicionalService = tabelaServicoAdicionalService;
	}
		
	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}


	/**
	 * @return the configuracoesFacade
	 */
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}


	/**
	 * @param configuracoesFacade the configuracoesFacade to set
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}		
}