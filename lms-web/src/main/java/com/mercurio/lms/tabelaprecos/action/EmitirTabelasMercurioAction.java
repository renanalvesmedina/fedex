package com.mercurio.lms.tabelaprecos.action;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.report.EmitirTabelaDensidadeService;
import com.mercurio.lms.tabelaprecos.report.EmitirTabelaEcommerceDiferenciadaAereaService;
import com.mercurio.lms.tabelaprecos.report.EmitirTabelaEcommerceDiferenciadaService;
import com.mercurio.lms.tabelaprecos.report.EmitirTabelaFreteAereaService;
import com.mercurio.lms.tabelaprecos.report.EmitirTabelaFreteCiaAereaService;
import com.mercurio.lms.tabelaprecos.report.EmitirTabelasMinimoProgressivoService;
import com.mercurio.lms.vendas.model.service.EmitirTabelasClientesService;
import com.mercurio.lms.vendas.util.EmitirTabelaDensidadePDF;
import com.mercurio.lms.vendas.util.EmitirTabelaEcommerceDiferenciadaPDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteAereaEcommerceDiferenciadaPDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteAereaPDF;
import com.mercurio.lms.vendas.util.EmitirTabelaFreteCiaAereaPDF;
import com.mercurio.lms.vendas.util.EmitirTabelaMinimoProgressivaPDF;
import com.mercurio.lms.vendas.util.TemplatePdf;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.tabelaprecos.emitirTabelasMercurioAction"
 */
public class EmitirTabelasMercurioAction  {
	private TabelaPrecoService tabelaPrecoService;
	private AeroportoService aeroportoService;
	private EmitirTabelasMinimoProgressivoService tabelasMinimoProgressivo;
	private EmitirTabelaDensidadeService emitirTabelaDensidadeService;
	private EmitirTabelaEcommerceDiferenciadaService tabelaEcommerceDiferenciadaService;	
	private EmitirTabelaEcommerceDiferenciadaAereaService tabelaEcommerceDiferenciadaAreaService;
	private EmitirTabelaFreteCiaAereaService emitirTabelaFreteCiaAereaService;
	private EmitirTabelaFreteAereaService emitirTabelaFreteAereaService;	
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService; 
	private EmitirTabelasClientesService emitirTabelasClientesService; 
	
	public EmitirTabelasClientesService getEmitirTabelasClientesService() {
		return emitirTabelasClientesService;
	}

	public void setEmitirTabelasClientesService(
			EmitirTabelasClientesService emitirTabelasClientesService) {
		this.emitirTabelasClientesService = emitirTabelasClientesService;
	}

	public List findLookup(Map criteria) {
		return this.tabelaPrecoService.findLookup(criteria);
	}
	
	public List findLookupAeroporto(Map criteria){
		return this.aeroportoService.findLookupAeroporto(criteria);
	}

	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}

	/**
	 * Executa o m�todo <code>execute</code> da instancia atribuida via getter . 
	 * @param parameters
	 * @return
	 */
		
	public TypedFlatMap execute(TypedFlatMap parameters) throws Exception {
		TemplatePdf lista = null;
		Boolean isCapital = parameters.getBoolean("isCapital", Boolean.FALSE);
		if(isCapital){			
			TipoLocalizacaoMunicipio capital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Capital", "C");	
			parameters.put("idTipoLocalizacaoMunicipioOrigem", capital.getIdTipoLocalizacaoMunicipio());
			parameters.put("dsTipoLocalizacaoMunicipio", "Capital");
		}else{
			parameters.put("dsTipoLocalizacaoMunicipio", "Interior");
		}
				
		TypedFlatMap tabelaPreco = tabelaPrecoService.findByIdMap(parameters.getLong("tabelaPreco.idTabelaPreco"));
		String tpTabelaPreco = tabelaPreco.getString("tipoTabelaPreco.tpTipoTabelaPreco.value");
		String tpSubtipoTabelaPreco = tabelaPreco.getString("subtipoTabelaPreco.tpSubtipoTabelaPreco");
		Long idServicoTpTabelaPreco = tabelaPreco.getLong("tipoTabelaPreco.servico.idServico");
		String tpCalculoFretePeso = tabelaPreco.getString("tpCalculoFretePeso.value");

		/*Seta nos parametros o tipo da tabela*/
		parameters.put("tpTabelaPreco", tpSubtipoTabelaPreco);
		parameters.put("idServico", idServicoTpTabelaPreco);
		
		
		if (tabelaPreco == null || tpTabelaPreco == null) {
			return null;
		}
		Boolean emitirDensidades = parameters.getBoolean("emitirDensidades");
		if(emitirDensidades == null || Boolean.FALSE.equals(parameters.getBoolean("emitirDensidades"))) {
			if(tpTabelaPreco.equals("C")){
				List<Map<String, String>> precos = emitirTabelaFreteCiaAereaService.findDados(parameters);
				if(precos != null && !precos.isEmpty()){
					List<Map<String, String>> generalidades = emitirTabelasClientesService.getGeneralidades(parameters);
					List<Map<String, String>> formalidades = emitirTabelasClientesService.getFormalidades(parameters);
					List<Map<String, String>> servicosAdicionais = emitirTabelasClientesService.getServicosAdicionais(parameters);
					List<Map<String, String>> dificuldadeEntrega = emitirTabelasClientesService.getDificuldadeEntrega(parameters);
					List<Map<String, String>> legendas = emitirTabelasClientesService.getLegendas(parameters);
					List<Map<String, String>> cabecalho = emitirTabelasClientesService.getCabecalho(parameters);
					List<Map<String, String>> aereo = emitirTabelasClientesService.getAereo(parameters);
					List<Map<String, String>> taxaCombustivel = emitirTabelasClientesService.getTaxaCombustivel(parameters);
					Set taxaCombustivelCabecalho = emitirTabelasClientesService.getCabecalhoTaxaCombustivel(parameters);
					
					List<Map<String, String>> valores = emitirTabelasClientesService.normalizaValores(precos);
					
					generalidades = emitirTabelasClientesService.normalizaGeneralidades(generalidades);
					
					List<Map<String, String>> formalidade = emitirTabelasClientesService.normalizaFormalidades(formalidades);
					
					dificuldadeEntrega =  emitirTabelasClientesService.normalizaDificuldadeEntrega(dificuldadeEntrega);
					servicosAdicionais = emitirTabelasClientesService.normalizaServicosAdicionais(servicosAdicionais);
					
					lista = new EmitirTabelaFreteCiaAereaPDF(cabecalho,String.valueOf(parameters.get("usuarioEmissor")),valores, generalidades, formalidade,servicosAdicionais,dificuldadeEntrega,legendas,aereo,taxaCombustivel,taxaCombustivelCabecalho);							
				}
				
			}else if(tpTabelaPreco.equals("A")){
				List<Map<String, String>> precos = emitirTabelaFreteAereaService.findDados(parameters);
				if(precos != null && !precos.isEmpty()){
					List<Map<String, String>> generalidades = emitirTabelasClientesService.getGeneralidades(parameters);
					List<Map<String, String>> formalidades = emitirTabelasClientesService.getFormalidades(parameters);
					List<Map<String, String>> servicosAdicionais = emitirTabelasClientesService.getServicosAdicionais(parameters);
					List<Map<String, String>> dificuldadeEntrega = emitirTabelasClientesService.getDificuldadeEntrega(parameters);
					List<Map<String, String>> legendas = emitirTabelasClientesService.getLegendas(parameters);
					List<Map<String, String>> cabecalho = emitirTabelasClientesService.getCabecalho(parameters);
					List<Map<String, String>> aereo = emitirTabelasClientesService.getAereo(parameters);
					List<Map<String, String>> taxaCombustivel = emitirTabelasClientesService.getTaxaCombustivel(parameters);
					List<Map<String, String>> taxaTerrestre = emitirTabelasClientesService.getTaxaTerrestre(parameters);
					Set taxaCombustivelCabecalho = emitirTabelasClientesService.getCabecalhoTaxaCombustivel(parameters);				
					List<Map<String, String>> coleta = emitirTabelasClientesService.getTaxaColeta(parameters);
					List<Map<String, String>> entrega = emitirTabelasClientesService.getTaxaEntrega(parameters);
					List<Map<String, String>> legendaGeneralidades = emitirTabelasClientesService.getLegendaGeneralidade(parameters);											
					List<Map<String, String>> valores = emitirTabelasClientesService.normalizaValores(precos);
					
					generalidades = emitirTabelasClientesService.normalizaGeneralidades(generalidades);
					
					List<Map<String, String>> formalidade = emitirTabelasClientesService.normalizaFormalidades(formalidades);
					
					dificuldadeEntrega = emitirTabelasClientesService.normalizaDificuldadeEntrega(dificuldadeEntrega);
					servicosAdicionais = emitirTabelasClientesService.normalizaServicosAdicionais(servicosAdicionais);
					
					
					lista = new EmitirTabelaFreteAereaPDF(cabecalho,String.valueOf(parameters.get("usuarioEmissor")),valores, generalidades, formalidade,servicosAdicionais,dificuldadeEntrega,legendas,aereo,taxaCombustivel,taxaCombustivelCabecalho,coleta,entrega,legendaGeneralidades,taxaTerrestre);							
				}
			}else if(tpTabelaPreco.equals("@") 
					|| tpTabelaPreco.equals("D") 
					|| tpTabelaPreco.equals("F") 
					|| ( (tpTabelaPreco.equals("M") || tpTabelaPreco.equals("T") ) 
							&& (tpSubtipoTabelaPreco.equals("F") || tpSubtipoTabelaPreco.equals("P")))){
				String tpModal = tabelaPreco.getString("tipoTabelaPreco.servico.tpModal");
				if(StringUtils.isBlank(tpModal)){
					return null;
				}else if(tpModal.equals("R")){//rodoviario
					List<Map<String, String>> precos = tabelaEcommerceDiferenciadaService.findDados(parameters);
					if(precos != null && !precos.isEmpty()){
						List<Map<String, String>> generalidades = emitirTabelasClientesService.getGeneralidades(parameters);
						List<Map<String, String>> formalidades = emitirTabelasClientesService.getFormalidades(parameters);
						List<Map<String, String>> servicosAdicionais = emitirTabelasClientesService.getServicosAdicionais(parameters);
						List<Map<String, String>> dificuldadeEntrega = emitirTabelasClientesService.getDificuldadeEntrega(parameters);
						List<Map<String, String>> legendas = emitirTabelasClientesService.getLegendas(parameters);
						
						generalidades = emitirTabelasClientesService.normalizaGeneralidades(generalidades);
						
						List<Map<String, String>> formalidade = emitirTabelasClientesService.normalizaFormalidades(formalidades);
						
						dificuldadeEntrega = emitirTabelasClientesService.normalizaDificuldadeEntrega(dificuldadeEntrega);
						servicosAdicionais = emitirTabelasClientesService.normalizaServicosAdicionais(servicosAdicionais);
						
						boolean pesoExcedente = false;
						if(tpCalculoFretePeso != null && tpCalculoFretePeso.equals("E")){
							pesoExcedente= true;
					}
						
						lista = new EmitirTabelaEcommerceDiferenciadaPDF(String.valueOf(parameters.get("usuarioEmissor")),precos, generalidades, formalidade,servicosAdicionais,dificuldadeEntrega,legendas,pesoExcedente);									
					}
				}else if(tpModal.equals("A")){//aereo
					List<Map<String, String>> precos = tabelaEcommerceDiferenciadaAreaService.findDados(parameters);
					if(precos != null && !precos.isEmpty()){
						List<Map<String, String>> generalidades = emitirTabelasClientesService.getGeneralidades(parameters);
						List<Map<String, String>> formalidades = emitirTabelasClientesService.getFormalidadesAereo(parameters);
						List<Map<String, String>> servicosAdicionais = emitirTabelasClientesService.getServicosAdicionais(parameters);
						List<Map<String, String>> dificuldadeEntrega = emitirTabelasClientesService.getDificuldadeEntrega(parameters);
						List<Map<String, String>> legendas = emitirTabelasClientesService.getLegendas(parameters);
						List<Map<String, String>> cabecalho = emitirTabelasClientesService.getCabecalho(parameters);
						List<Map<String, String>> aereo = emitirTabelasClientesService.getAereo(parameters);
						List<Map<String, String>> taxaCombustivel = emitirTabelasClientesService.getTaxaCombustivel(parameters);
						List<Map<String, String>> taxaTerrestre = emitirTabelasClientesService.getTaxaTerrestre(parameters);
						Set taxaCombustivelCabecalho = emitirTabelasClientesService.getCabecalhoTaxaCombustivel(parameters);
						
						List<Map<String, String>> coleta = emitirTabelasClientesService.getTaxaColeta(parameters);
						List<Map<String, String>> entrega = emitirTabelasClientesService.getTaxaEntrega(parameters);
						List<Map<String, String>> legendaGeneralidades = emitirTabelasClientesService.getLegendaGeneralidade(parameters);								
						
						List<Map<String, String>> valores = emitirTabelasClientesService.normalizaValoresAereo(precos);
						
						generalidades = emitirTabelasClientesService.normalizaGeneralidades(generalidades);
						
						List<Map<String, String>> formalidade = emitirTabelasClientesService.normalizaFormalidades(formalidades);
						
						dificuldadeEntrega = emitirTabelasClientesService.normalizaDificuldadeEntrega(dificuldadeEntrega);
						servicosAdicionais = emitirTabelasClientesService.normalizaServicosAdicionais(servicosAdicionais);
						
						
						lista = new EmitirTabelaFreteAereaEcommerceDiferenciadaPDF(cabecalho,String.valueOf(parameters.get("usuarioEmissor")),valores, generalidades, formalidade,servicosAdicionais,dificuldadeEntrega,legendas,aereo,taxaCombustivel,taxaCombustivelCabecalho,coleta,entrega,legendaGeneralidades,taxaTerrestre);												
				}
				}
			} else if(tpTabelaPreco.equals("R") || tpTabelaPreco.equals("M") || tpTabelaPreco.equals("T") || tpTabelaPreco.equals("E")){
				List<Map<String, String>> precos = tabelasMinimoProgressivo.findDados(parameters);
				if(precos != null && !precos.isEmpty()){
					List<Map<String, String>> generalidades = emitirTabelasClientesService.getGeneralidades(parameters);
					List<Map<String, String>> formalidades = emitirTabelasClientesService.getFormalidades(parameters);
					List<Map<String, String>> servicosAdicionais = emitirTabelasClientesService.getServicosAdicionais(parameters);
					List<Map<String, String>> dificuldadeEntrega = emitirTabelasClientesService.getDificuldadeEntrega(parameters);
					List<Map<String, String>> legendas = emitirTabelasClientesService.getLegendas(parameters);
					
					generalidades = emitirTabelasClientesService.normalizaGeneralidades(generalidades);
					
					List<Map<String, String>> formalidade = emitirTabelasClientesService.normalizaFormalidades(formalidades);
					
					dificuldadeEntrega = emitirTabelasClientesService.normalizaDificuldadeEntrega(dificuldadeEntrega);
					servicosAdicionais = emitirTabelasClientesService.normalizaServicosAdicionais(servicosAdicionais);
					
					lista = new EmitirTabelaMinimoProgressivaPDF(String.valueOf(parameters.get("usuarioEmissor")),precos, generalidades, formalidade,servicosAdicionais,dificuldadeEntrega,legendas);					
			}
			}
		} else if ( (tpTabelaPreco.equals("R") || tpTabelaPreco.equals("M") || tpTabelaPreco.equals("T") ) && parameters.getBoolean("emitirDensidades").booleanValue()){
			List<Map<String, String>> precos = emitirTabelaDensidadeService.findDados(parameters);
			if(precos != null && !precos.isEmpty()){
				List<Map<String, String>> generalidades = emitirTabelasClientesService.getGeneralidades(parameters);
				List<Map<String, String>> formalidades = emitirTabelasClientesService.getFormalidades(parameters);
				List<Map<String, String>> servicosAdicionais = emitirTabelasClientesService.getServicosAdicionais(parameters);
				List<Map<String, String>> dificuldadeEntrega = emitirTabelasClientesService.getDificuldadeEntrega(parameters);
				List<Map<String, String>> legendas = emitirTabelasClientesService.getLegendas(parameters);
				
				generalidades = emitirTabelasClientesService.normalizaGeneralidades(generalidades);
				
				List<Map<String, String>> formalidade = emitirTabelasClientesService.normalizaFormalidades(formalidades);
				
				dificuldadeEntrega = emitirTabelasClientesService.normalizaDificuldadeEntrega(dificuldadeEntrega);
				servicosAdicionais = emitirTabelasClientesService.normalizaServicosAdicionais(servicosAdicionais);
				
				lista = new EmitirTabelaDensidadePDF(String.valueOf(parameters.get("usuarioEmissor")),precos, generalidades, formalidade,servicosAdicionais,dificuldadeEntrega,legendas);				
		}
	}

		if(lista == null){
			File file = File.createTempFile("empty-" + System.currentTimeMillis(), ".pdf", new ReportExecutionManager().generateOutputDir());
			parameters.put("_value", file);
		}else{
			parameters.put("_value", lista.getFile());
		}
		
		return parameters;
	}


	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	public void setTabelasMinimoProgressivo(EmitirTabelasMinimoProgressivoService tabelasMinimoProgressivo) {
		this.tabelasMinimoProgressivo = tabelasMinimoProgressivo;
	}
	public void setEmitirTabelaDensidadeService(EmitirTabelaDensidadeService emitirTabelaDensidadeService) {		
		this.emitirTabelaDensidadeService = emitirTabelaDensidadeService;
	}
	public void setTabelaEcommerceDiferenciadaService(EmitirTabelaEcommerceDiferenciadaService tabelaEcommerceDiferenciadaService) {
		this.tabelaEcommerceDiferenciadaService = tabelaEcommerceDiferenciadaService;
	}
	public void setEmitirTabelaFreteAereaService(EmitirTabelaFreteAereaService emitirTabelaFreteAereaService) {
		this.emitirTabelaFreteAereaService = emitirTabelaFreteAereaService;
	}
	public void setEmitirTabelaFreteCiaAereaService(EmitirTabelaFreteCiaAereaService emitirTabelaFreteCiaAereaService) {
		this.emitirTabelaFreteCiaAereaService = emitirTabelaFreteCiaAereaService;
	}
	public void setTabelaEcommerceDiferenciadaAreaService(EmitirTabelaEcommerceDiferenciadaAereaService tabelaEcommerceDiferenciadaAreaService) {
		this.tabelaEcommerceDiferenciadaAreaService = tabelaEcommerceDiferenciadaAreaService;
	}

	public TipoLocalizacaoMunicipioService getTipoLocalizacaoMunicipioService() {
		return tipoLocalizacaoMunicipioService;
	}

	public void setTipoLocalizacaoMunicipioService(
			TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
	}
}