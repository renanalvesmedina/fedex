package com.mercurio.lms.rest.fretecarreteirocoletaentrega.notacreditopadrao;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoNotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.CalculoTabelaFreteCarreteiroCeService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.EventoNotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.GerarNotaCreditoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoPadraoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.report.EmitirNotasCreditoColetaEntregaService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.notacreditopadrao.dto.NotaCreditoPadraoFilterDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.notacreditopadrao.dto.NotaCreditoPadraoRestDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.notacreditopadrao.helper.NotaCreditoPadraoRestPopulateHelper;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.MensagemService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Rest responsável pelo controle da tela manter nota de crédito.
 * 
 */
@Path("/fretecarreteirocoletaentrega/notaCreditoPadrao")
public class NotaCreditoPadraoRest
		extends
		LmsBaseCrudReportRest<NotaCreditoPadraoRestDTO, NotaCreditoPadraoRestDTO, NotaCreditoPadraoFilterDTO> {
	
	@InjectInJersey
	private NotaCreditoPadraoService notaCreditoPadraoService;	
		
	@InjectInJersey
	private EventoNotaCreditoService eventoNotaCreditoService;
	
	@InjectInJersey
	private DomainValueService domainValueService;	
	
	@InjectInJersey
	private EmitirNotasCreditoColetaEntregaService emitirNotasCreditoColetaEntregaService;
	
	@InjectInJersey
	private CalculoTabelaFreteCarreteiroCeService calculoTabelaFreteCarreteiroCeService;
	
	@InjectInJersey
	private GerarNotaCreditoService gerarNotaCreditoService;
	
	@InjectInJersey
	protected ReportExecutionManager reportExecutionManager;
	
	@InjectInJersey
	private MensagemService mensagemService;
	
	@Override
	protected List<NotaCreditoPadraoRestDTO> find(NotaCreditoPadraoFilterDTO filter) {	
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		TypedFlatMap filterMap = NotaCreditoPadraoRestPopulateHelper.getFilterMap(criteria, filter);
		
		return NotaCreditoPadraoRestPopulateHelper.getListNotaCreditoPadraoRestDTO(domainValueService, notaCreditoPadraoService.findPaginatedCustom(filterMap));
	}
	
	@Override
	protected Integer count(NotaCreditoPadraoFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);
		
		TypedFlatMap filterMap = NotaCreditoPadraoRestPopulateHelper.getFilterMap(criteria, filter);
		
		return notaCreditoPadraoService.getRowCountCustom(filterMap);
	}
	
	@Override
	protected NotaCreditoPadraoRestDTO findById(Long id) {
		NotaCredito notaCredito = notaCreditoPadraoService.findById(id);
		
		NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO = NotaCreditoPadraoRestPopulateHelper.getNotaCreditoPadraoRestDTO(notaCredito, false);
		
		defineAlertMessage(notaCredito, notaCreditoPadraoRestDTO);
		
    	return notaCreditoPadraoRestDTO;
	}  	

	/**
	 * Retorna um registro para a tela de detalhamento, a partir da tela manter
	 * ações do workflow, utilizando o id do processo.
	 * 
	 * @param id
	 * @return Response
	 */
	@GET
	@Path("/findByIdProcesso")
	public NotaCreditoPadraoRestDTO findByIdProcesso(@QueryParam("id") Long id) {				
		return NotaCreditoPadraoRestPopulateHelper.getNotaCreditoPadraoRestDTO(notaCreditoPadraoService.findById(id), true);
    }  
	
	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("filial", "sgFilial"));
		list.add(getColumn("numeroNota", "nrNotaCredito"));		
		list.add(getColumn("filial", "sgFilialReciboFreteCarreteiro"));
		list.add(getColumn("numeroRecibo", "nrReciboFreteCarreteiro"));
		list.add(getColumn("situacao", "tpSituacao"));
		list.add(getColumn("tipo", "tpIdentificacaoProprietario"));
		list.add(getColumn("identificacao", "nrIdentificacaoProprietarioFormatado"));
		list.add(getColumn("proprietario", "nmPessoaProprietario"));		
		list.add(getColumn("frota", "nrFrota"));
		list.add(getColumn("placa", "nrIdentificador"));
		list.add(getColumn("geracao", "dhGeracao"));
		list.add(getColumn("emissao", "dhEmissao"));
		
		return list;
	}

	@Override
	protected List<Map<String, Object>> findDataForReport(NotaCreditoPadraoFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		return NotaCreditoPadraoRestPopulateHelper.getListForReport(domainValueService, notaCreditoPadraoService.findReportData(NotaCreditoPadraoRestPopulateHelper.getFilterMap(criteria, filter)));
	}

	/**
	 * Executa o store das alterações.
	 * 
	 * @param formDataMultiPart
	 * @return Response
	 * 
	 * @throws IOException 
	 * @see NotaCreditoPadraoRest#store(FormDataMultiPart)
	 */	
	@POST
	@Path("store")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response store(FormDataMultiPart formDataMultiPart) throws IOException {
		NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO = getModelFromForm(formDataMultiPart, NotaCreditoPadraoRestDTO.class, "data");	
		
		NotaCredito notaCredito = notaCreditoPadraoService.storeNotaCredito(
				NotaCreditoPadraoRestPopulateHelper.getNotaCredito(notaCreditoPadraoRestDTO), 
				getListAnexoNotaCredito(formDataMultiPart, notaCreditoPadraoRestDTO.getFiles()));
				
		return Response.ok(findById(notaCredito.getIdNotaCredito())).build();
    }
	
	/**
	 * Como esta tela não é um CRUD de manipulação padrão, este método não deve
	 * ser utilizado.
	 */
	@Override
	protected Long store(NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Não é possivel excluir uma nota de crédito.
	 */
	@Override
	protected void removeById(Long id) {
		throw new UnsupportedOperationException();
    }

	/**
	 * Não é possivel excluir uma nota de crédito.
	 */
	@Override
	protected void removeByIds(List<Long> arg0) {
		throw new UnsupportedOperationException();		
	}
	
	/**
	 * Efetua o download de um arquivo de anexo.
	 * 
	 * @param id
	 * 
	 * @return Response
	 * @throws UnsupportedEncodingException
	 */
	@GET
	@Path("/findAnexoById")
	public Response findAnexoNotaCreditoById(@QueryParam("id") Long id) {
		Map<String, Object> result = new HashMap<>();
		result.put("table", "ANEXO_NOTA_CREDITO");
		result.put("blobColumn", "DC_ARQUIVO");
		result.put("idColumn", "ID_ANEXO_NOTA_CREDITO");
		result.put("id", id);

		return Response.ok(result).build();
	}
	
	/**
	 * Retorna os anexos vinculados a nota de crédito.
	 * 
	 * @param filtro
	 * 
	 * @return Response
	 */
	@POST
	@Path("/findAnexos")
	public Response findAnexos(Map<String, Object> filtro) {		
		Long idNotaCredito = MapUtils.getLong(MapUtils.getMap(filtro, "filtros"), "idNotaCredito");
		
		if(idNotaCredito == null){
			return Response.ok().build();	
		}
		
		List<Map<String, Object>> listAnexoNotaCredito = notaCreditoPadraoService.findAnexoNotaCreditoByIdNotaCredito(idNotaCredito);
				
		return getReturnFind(listAnexoNotaCredito, listAnexoNotaCredito.size());		
	}
		
	/**
	 * Remove um ou mais itens da tabela de arquivos de anexos.
	 * 
	 * @param ids
	 */
	@POST
	@Path("removeAnexoNotaCreditoByIds")
	public void removeAnexoNotaCreditoByIds(List<Long> ids) {
		notaCreditoPadraoService.removeByIdsAnexoNotaCredito(ids);
	}
	
	/**
	 * Retorna os eventos da nota de crédito.
	 * 
	 * @param filter
	 * @return Response
	 */
	@POST
	@Path("findEventos")
	public Response findEventos(NotaCreditoPadraoFilterDTO filter){		
		if(filter.getIdNotaCredito() == null){
			return Response.ok().build();	
		}
		
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);
		criteria.put("idNotaCredito", filter.getIdNotaCredito());
		
		Integer rowCount = eventoNotaCreditoService.getRowCountEventos(criteria);
		
		if(rowCount > 0){
			return getReturnFind(eventoNotaCreditoService.findPaginatedEventos(criteria).getList(), rowCount);		
		} else {
			return Response.ok().build();
		}	
	}

	/**
	 * Retorna os itens da nota de crédito agrupados pela sua tabela de frete
	 * padrão.
	 * 
	 * @param idNotaCredito
	 * 
	 * @return Map<String, Object>
	 */
	@POST
	@Path("findItens")
	public Map<String, Object> findItens(NotaCreditoPadraoFilterDTO filter){		
		if(filter.getIdNotaCredito() == null){
			return new HashMap<String, Object>();
		}		
				
		return notaCreditoPadraoService.findDataScreenReport(filter.getIdNotaCredito(), filter.getTpNotaCredito().getValue());
	}
	
	/**
	 * Emite relatório da nota de crédito padrão.
	 * 
	 * @param idNotaCredito
	 * 
	 * @throws Exception  
	 */
	@GET
	@Path("emitirNotaCredito")
	public Response emitirNotaCredito(@QueryParam("id") Long idNotaCredito)
			throws Exception {		
		return createReport(emitirNotasCreditoColetaEntregaService.executeNotaCreditoPadraoReport(idNotaCredito, false));
	}
	
	/**
	 * Re-gera da nota de crédito padrão de acordo com o seu tipo.
	 * 
	 * @param filter
	 * @return Response
	 * 
	 * @throws Exception
	 */
	@POST
	@Path("regerarNotaCredito")
	public Response regerarNotaCredito(NotaCreditoPadraoFilterDTO filter) throws Exception {
		Long idControleCarga = filter.getIdControleCarga();			
		String tpNotaCredito = filter.getTpNotaCredito().getValue();
		
		if ("E".equals(tpNotaCredito)) {
			gerarNotaCreditoService.execute(idControleCarga);
		} else if ("C".equals(tpNotaCredito) || "CP".equals(tpNotaCredito)) {
			calculoTabelaFreteCarreteiroCeService.executeNotaColeta(idControleCarga);
		}		
		
		return Response.ok().build();
	}
	
	/**
	 * Visualiza o relatório da nota de crédito padrão.
	 * 
	 * @param idNotaCredito
	 * 
	 * @throws Exception  
	 */
	@GET
	@Path("visualizarNotaCredito")
	public Response visualizarNotaCredito(@QueryParam("id") Long idNotaCredito)
			throws Exception {				
		return createReport(emitirNotasCreditoColetaEntregaService.executeNotaCreditoPadraoReport(idNotaCredito, true));
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
	 * Prepara os anexos da nota de crédito para serem persistidos.
	 * 
	 * @param formDataMultiPart
	 * @param files
	 * 
	 * @return List<AnexoNotaCredito> 
	 * 
	 * @throws IOException
	 */
	private List<AnexoNotaCredito> getListAnexoNotaCredito(
			FormDataMultiPart formDataMultiPart, List<Map<String, Object>> files)
			throws IOException {
		List<AnexoNotaCredito> listAnexoNotaCredito = new ArrayList<AnexoNotaCredito>();
		
		if(files == null || files.isEmpty()){
			return listAnexoNotaCredito;
		}
				
		for (int i = 0; i < files.size(); i++) {
			AnexoNotaCredito anexoNotaCredito = NotaCreditoPadraoRestPopulateHelper.getAnexoNotaCredito(
					(Map<String, Object>) files.get(i),
					getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo_" + i));
			
			listAnexoNotaCredito.add(anexoNotaCredito);
		}
		
		return listAnexoNotaCredito;
	}	
	
	/**
	 * Define um alerta personalizado sobre o estado atual da nota de crédito padrão.
	 * 
	 * @param notaCredito
	 */
	private void defineAlertMessage(NotaCredito notaCredito, NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO){
		Pendencia pendencia = notaCredito.getPendencia();
		
		if(pendencia == null){
			return;
		}
		
		DomainValue tpSituacaoAprovacao = notaCredito.getTpSituacaoAprovacao();
		DomainValue tpSituacaoPendencia = notaCredito.getPendencia().getTpSituacaoPendencia();
		
		if(isEventoDesconto(pendencia)){
			if("S".equals(tpSituacaoAprovacao.getValue())){		
				setMessageType(notaCreditoPadraoRestDTO, "warning", "bullhorn", "LMS-25130", null);
			}
		} else if(isEventoValorMaximo(pendencia)){
			if(ConstantesWorkflow.EM_APROVACAO.equals(tpSituacaoPendencia.getValue())){		
				setMessageType(notaCreditoPadraoRestDTO, "warning", "bullhorn", "LMS-25128", null);
			} else if(ConstantesWorkflow.REPROVADO.equals(tpSituacaoPendencia.getValue())){		
				setMessageType(notaCreditoPadraoRestDTO, "danger", "bullhorn", "LMS-25129", null);
			}
		}		
	}
	
	/**
	 * Verifica se existe um workflow em execução de valor máximo da nota de crédito.
	 * 
	 * @param pendencia
	 * @return boolean
	 */
	private boolean isEventoValorMaximo(Pendencia pendencia) {
		Short nrTipoEvento = pendencia.getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
		
		return nrTipoEvento.equals(ConstantesWorkflow.NR2509_NC_VL_MAIOR_PARAMETRO);
	}

	/**
	 * Verifica se existe um workflow em execução de valor desconto da nota de crédito.
	 * 
	 * @param pendencia
	 * @return boolean
	 */
	private boolean isEventoDesconto(Pendencia pendencia) {
		Short nrTipoEvento = pendencia.getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
		
		return nrTipoEvento.equals(ConstantesWorkflow.NR2507_DESC_NOTCRE);
	}

	/**
	 * Define formato e mensagem de tela.
	 * 
	 * @param dto
	 * @param type
	 * @param icon
	 * @param lms
	 * @param params
	 */
	private void setMessageType(NotaCreditoPadraoRestDTO dto, String type, String icon, String lms, String params) {
		dto.setMessageType("bg-" + type);
		dto.setMessageTypeIcon("glyphicon-" + icon);
		dto.setMessageText(mensagemService.getMessage(lms, new Object[]{ params }));
	}
}