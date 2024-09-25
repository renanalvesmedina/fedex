package com.mercurio.lms.vendas.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.tabelaprecos.report.EmitirTabelaEcommerceDiferenciadaService;
import com.mercurio.lms.vendas.model.dao.EmitirTabelasClienteDAO;
import com.mercurio.lms.vendas.model.service.TabelasClienteService;
import com.mercurio.lms.vendas.util.GroupDataSource;

/**
 * 30.03.02.37[F/P] Tabela Fob / Pacotinho
 * 
 * @spring.bean id="lms.vendas.report.emitirTabelaFobService"
 * @spring.property name="reportName" value="/com/mercurio/lms/vendas/report/emitirTabelaFob.vm"
 * @spring.property name="numberOfCrosstabs" value="1"
 * @spring.property name="crossTabLefts" value="280"
 * @spring.property name="crossTabBandWidths" value="396"
 * @spring.property name="numbersOfCrossTabColumns" value="20"
 */
public class EmitirTabelaFobService extends ReportServiceSupport { 

	/* Nome da Tebela temporaria */
	private static final String NOME_TABELA = "TMP_E_DIFERENCIADAS";
    private EmitirTabelaEcommerceDiferenciadaService tabelaEcommerceDiferenciadaService; 
    private EnderecoPessoaService enderecoPessoaService;
    private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private TabelasClienteService tabelasClienteService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		return null;
	}
	
	public List<Map<String, String>> findDados(TypedFlatMap parameters) throws Exception {
		List<Map> data = getEmitirTabelasClienteDAO().findTabelaFobPacotinho(parameters, ConstantesExpedicao.TP_FRETE_FOB);
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		String tipo = ConstantesExpedicao.TP_FRETE_FOB;
		if(data == null || data.isEmpty()){
			data = getEmitirTabelasClienteDAO().findTabelaFobPacotinho(parameters, ConstantesExpedicao.TP_FRETE_PACOTINHO);
			tipo = ConstantesExpedicao.TP_FRETE_PACOTINHO;
		}

		TypedFlatMap parametros = new TypedFlatMap();
		for (Map map : data) {
			parametros.put("tpTabelaPreco", tipo);
			parametros.put("idCliente", MapUtils.getLong(map,"idCliente"));
			parametros.put("idDivisao", MapUtils.getLong(map,"idDivisao"));
			parametros.put("idTabelaDivisao", MapUtils.getLong(map,"idTabelaDivisao"));
			parametros.put("tabelaPreco.idTabelaPreco", MapUtils.getLong(map,"idTabelaPreco"));
			parametros.put("isTabelaNova", MapUtils.getBoolean(parameters, "tabelaNova"));
			
		getJdbcTemplate().execute("DELETE FROM "+ NOME_TABELA);

		Long idTabelaPreco = MapUtils.getLong(parameters,"tabelaPreco.idTabelaPreco");
    	if(idTabelaPreco == null){
    		JRReportDataObject jr = executeQuery("SELECT * FROM " + NOME_TABELA,parameters);
    		parameters.put(ReportServiceSupport.CT_NUMBER_OF_COLS, new Integer[]{Integer.valueOf(1)});
    		jr.setParameters(parameters);
    		return null ;
    	}

    	/** Verifica Tipo de Tabela FOB/Pacotinho */
    	String tpTabelaPreco = MapUtils.getString(parameters, "tpTabelaPreco");
    	Long idCliente = MapUtils.getLong(parameters, "idCliente");
    	EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idCliente);
    	    	
    	if(ConstantesExpedicao.TP_FRETE_FOB.equals(tpTabelaPreco)) {
    		TipoLocalizacaoMunicipio tlm = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipioFob(enderecoPessoa.getMunicipio().getIdMunicipio());
    		parameters.put("idTipoLocalizacaoMunicipioDestino", tlm.getIdTipoLocalizacaoMunicipio());
    		parameters.put("idUnidadeFederativaDestino", enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
    		parameters.put("dsTipoLocalizacaoMunicipio", tlm.getDsTipoLocalizacaoMunicipio());
    	} else if(ConstantesExpedicao.TP_FRETE_PACOTINHO.equals(tpTabelaPreco)) {
    		TipoLocalizacaoMunicipio tlm = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipioFob(enderecoPessoa.getMunicipio().getIdMunicipio());
    		parameters.put("idTipoLocalizacaoMunicipioOrigem", tlm.getIdTipoLocalizacaoMunicipio());
    		parameters.put("idUnidadeFederativaOrigem", enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
    		parameters.put("dsTipoLocalizacaoMunicipio", tlm.getDsTipoLocalizacaoMunicipio());
    	}

		JRReportDataObject jr = tabelaEcommerceDiferenciadaService.execute(parameters);
		Map reportParameter = jr.getParameters();
		
		//HEADER
		List listaHeader = (List) getTabelasClienteService().montaHeader(reportParameter, getJdbcTemplate(),TabelasClienteService.RETORNO_LIST);
		Integer[] size = (Integer[]) reportParameter.get(ReportServiceSupport.CT_NUMBER_OF_COLS);
		reportParameter.put("HEADER",new GroupDataSource(JRMapCollectionDataSource.class, listaHeader, size[0]));

		parameters.put(idTabelaPreco, reportParameter);
		result.add(parameters);
		result.add(reportParameter);
		result.addAll(listaHeader);
	}
		return result;
	}

	public void setTabelaEcommerceDiferenciadaService(EmitirTabelaEcommerceDiferenciadaService tabelaEcommerceDiferenciadaService) {
		this.tabelaEcommerceDiferenciadaService = tabelaEcommerceDiferenciadaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setTipoLocalizacaoMunicipioService(
			TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
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
	public TabelasClienteService getTabelasClienteService() {
		return tabelasClienteService;
	}

	public void setTabelasClienteService(TabelasClienteService tabelasClienteService) {
		this.tabelasClienteService = tabelasClienteService;
	}
	private EmitirTabelasClienteDAO emitirTabelasClienteDAO;

}