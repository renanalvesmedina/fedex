package com.mercurio.lms.rest.fretecarreteirocoletaentrega.gerarnotacreditopadrao;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.CalculoTabelaFreteCarreteiroCeService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.GerarNotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoPadraoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.report.EmitirNotasCreditoColetaEntregaService;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.gerarnotacreditopadrao.dto.GerarNotaCreditoPadraoFilterDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.gerarnotacreditopadrao.dto.GerarNotaCreditoPadraoRestDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.gerarnotacreditopadrao.helper.GerarNotaCreditoPadraoRestPopulateHelper;

/**
 * Rest responsável pelo controle da tela gerar nota de crédito.
 * 
 */
@Path("/fretecarreteirocoletaentrega/gerarNotaCreditoPadrao")
public class GerarNotaCreditoPadraoRest extends BaseCrudRest<GerarNotaCreditoPadraoRestDTO, GerarNotaCreditoPadraoRestDTO, GerarNotaCreditoPadraoFilterDTO> {
	
	private static final String PARAMETRO_ATIVA_CALCULO_PADRAO = "ATIVA_CALCULO_PADRAO";
	
	@InjectInJersey	
	private NotaCreditoPadraoService notaCreditoPadraoService;
	
	@InjectInJersey	
	private ConteudoParametroFilialService conteudoParametroFilialService;
	
	@InjectInJersey
	private EmitirNotasCreditoColetaEntregaService emitirNotasCreditoColetaEntregaService;
	
	@InjectInJersey
	private ReportExecutionManager reportExecutionManager;
	
	@InjectInJersey
	private CalculoTabelaFreteCarreteiroCeService calculoTabelaFreteCarreteiroCeService;
	
	@InjectInJersey
	private GerarNotaCreditoService gerarNotaCreditoService;
	
	
	
	@Override
	protected List<GerarNotaCreditoPadraoRestDTO> find(GerarNotaCreditoPadraoFilterDTO filter) {		
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		TypedFlatMap filterMap = GerarNotaCreditoPadraoRestPopulateHelper.getFilterMap(criteria, filter);
		
		return GerarNotaCreditoPadraoRestPopulateHelper.getListGerarNotaCreditoPadraoRestDTO(notaCreditoPadraoService.findGerarNotaCredito(filterMap));
	}

	@Override
	protected Integer count(GerarNotaCreditoPadraoFilterDTO filter) {
		validateFind(filter);
		
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);
		TypedFlatMap filterMap = GerarNotaCreditoPadraoRestPopulateHelper.getFilterMap(criteria, filter);
		
		return notaCreditoPadraoService.getRowCountGerarNotaCredito(filterMap);
	}
	
	/**
	 * Apenas é possível executar a consulta nas filiais que o parâmetro
	 * <b>ATIVA_CALCULO_PADRAO</b> esteja ativo.
	 * 
	 * @param filter
	 */
	private void validateFind(GerarNotaCreditoPadraoFilterDTO filter){
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(filter.getFilial().getIdFilial(), 
				PARAMETRO_ATIVA_CALCULO_PADRAO, false, true);
		
		if (conteudoParametroFilial == null || "N".equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			throw new BusinessException("LMS-25121");
		}
	}
	
	/**
	 * Emite a nota de crédito.
	 * 
	 * @param id
	 * @return Response
	 * @throws Exception 
	 */
	@GET
	@Path("/emitir")
	public Response emitir(@QueryParam("id") Long idNotaCredito) throws Exception {
		return createReport(emitirNotasCreditoColetaEntregaService.executeNotaCreditoPadraoReport(idNotaCredito, false));
	}
	
	/**
	 * Gera visualização do relatório. 
	 * 
	 * @param reportFile
	 * @return Response
	 * 
	 * @throws Exception
	 */
	private Response createReport(File reportFile) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		
		if(reportFile != null){
			result.put("fileName", reportExecutionManager.generateReportLocator(reportFile));
		}	
		
		return Response.ok(result).build();		
	}
	
	/**
	 * Gera a nota de crédito.
	 * 
	 * @param id
	 * @return Response
	 */
	@GET
	@Path("/gerar")
	public Response gerar(@QueryParam("id") Long idControleCarga,
			@QueryParam("tpGerarNotaCredito") String tpGerarNotaCredito)
			throws Exception {
		NotaCredito notaCredito = null;
		
		if("P".equals(tpGerarNotaCredito)){
			notaCredito = gerarNotaCreditoService.execute(idControleCarga);	
		} else {
			notaCredito = calculoTabelaFreteCarreteiroCeService.executeNotaColeta(idControleCarga);			
		}
		
		Map<String,Long> result = new HashMap<String, Long>();
		
		if("P".equals(notaCredito.getControleCarga().getProprietario().getTpProprietario().getValue())){
			result.put("proprio", 1L);
		}else if(notaCredito.getIdNotaCredito()!= null){
			result.put("idNotaCredito", notaCredito.getIdNotaCredito());				
		}
		return Response.ok(result).build();
	}
	
	
	/**
	 * Ação não suportada.
	 */
	@Override
	protected GerarNotaCreditoPadraoRestDTO findById(Long id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ação não suportada.
	 */
	@Override
	protected Long store(GerarNotaCreditoPadraoRestDTO bean) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Ação não suportada. 
	 */
	@Override
	protected void removeById(Long id) {	
		throw new UnsupportedOperationException();
	}

	/**
	 * Ação não suportada.
	 */
	@Override
	protected void removeByIds(List<Long> ids) {
		throw new UnsupportedOperationException();
	}
}