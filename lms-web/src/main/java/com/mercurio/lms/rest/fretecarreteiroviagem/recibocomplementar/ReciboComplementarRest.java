package com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar;

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
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.ResponseDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.fretecarreteirocoletaentrega.report.EmitirReciboService;
import com.mercurio.lms.fretecarreteiroviagem.model.AnexoReciboFc;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.AnexoReciboFcService;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.dto.ReciboComplementarDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.dto.ReciboComplementarFilterDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.helper.ReciboComplementarRestHelper;
import com.mercurio.lms.rest.fretecarreteiroviagem.recibocomplementar.helper.ReciboComplementarRestPopulateHelper;
import com.mercurio.lms.rest.utils.ExportUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Rest responsável pelo controle da tela manter recibo complementar.
 *
 */
@Path("/fretecarreteiroviagem/manterReciboComplementar")
public class ReciboComplementarRest extends LmsBaseCrudReportRest<ReciboComplementarDTO, ReciboComplementarDTO, ReciboComplementarFilterDTO> {
				
	@InjectInJersey
	private com.mercurio.lms.fretecarreteiroviagem.report.EmitirReciboService emitirReciboViagemService;	
	
	@InjectInJersey
	protected ReciboFreteCarreteiroService reciboFreteCarreteiroService;
		
	@InjectInJersey
	protected AnexoReciboFcService anexoReciboFcService;
	
	@InjectInJersey
	protected ConfiguracoesFacade configuracoesFacade;
	
	@InjectInJersey
	protected ReportExecutionManager reportExecutionManager;

	@InjectInJersey
	protected EmitirReciboService emitirReciboService;
		
	/**
	 * Retorna listagem de registros de acordo com o filtro informado.
	 * 
	 * @param filtro
	 * @return Response
	 */
	@Override
	public List<ReciboComplementarDTO> find(ReciboComplementarFilterDTO filter) {	
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		TypedFlatMap filterMap = ReciboComplementarRestPopulateHelper.getFilterMap(criteria, filter);
		
		return ReciboComplementarRestPopulateHelper.getListReciboComplementarDTO(reciboFreteCarreteiroService.findPaginatedReciboComplementar(filterMap));
	}
	
	@Override
	protected Integer count(ReciboComplementarFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);
		
		TypedFlatMap filterMap = ReciboComplementarRestPopulateHelper.getFilterMap(criteria, filter);
		
		return reciboFreteCarreteiroService.getRowCountComplementar(filterMap);
	}
	
	@Override
	protected ReciboComplementarDTO findById(Long id) {		
    	return ReciboComplementarRestPopulateHelper.getReciboComplementarDTO(reciboFreteCarreteiroService.findByIdComplementar(id), false);
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
	public ReciboComplementarDTO findByIdProcesso(@QueryParam("id") Long id) {		
		return ReciboComplementarRestPopulateHelper.getReciboComplementarDTO(reciboFreteCarreteiroService.findByIdComplementar(id), true);
    }   
	
	@Override
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> findDataForReport(ReciboComplementarFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		return ReciboComplementarRestPopulateHelper.getListForReport(reciboFreteCarreteiroService.findPaginatedReciboComplementar(ReciboComplementarRestPopulateHelper.getFilterMap(criteria, filter)));
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> findDataForFullReport(ReciboComplementarFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		return reciboFreteCarreteiroService.findReciboComplementarReport(ReciboComplementarRestPopulateHelper.getFilterMap(criteria, filter)).getList();
	}
	
	/**
	 * 
	 * @return List<Map<String, String>>
	 */
	private List<Map<String, String>> getColumnsFullReport() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("numeroReciboOriginal", "nrReciboFreteCarreteiroOriginal"));        				             
		list.add(getColumn("dataHoraEmissao", "dtEmissaoOriginal"));
		list.add(getColumn("cpfCnpjProprietario", "proprietarioIdentificacaoOriginal"));
		list.add(getColumn("nomeProprietario", "proprietarioNomeOriginal"));
		list.add(getColumn("frota", "meioTransporteNrFrotaOriginal"));
		list.add(getColumn("placa", "meioTransporteNrIdentificadorOriginal"));
		list.add(getColumn("valorLiquido", "vlLiquidoOriginal"));
		list.add(getColumn("valorBruto", "vlBrutoOriginal"));
		list.add(getColumn("observacao", "observacaoOriginal"));
		list.add(getColumn("criadorEmissor", "criadorEmissorOriginal"));
		list.add(getColumn("ultimoAprovador", "ultimoAprovadorOriginal"));
		list.add(getColumn("situacaoRecibo", "situacaoReciboOriginal"));
		list.add(getColumn("dataPagamentoReal", "dtPgtoRealOriginal"));
		list.add(getColumn("dataPagamentoProgramada", "dtPgtoProgramadaOriginal"));
		list.add(getColumn("numeroRecibo", "nrReciboFreteCarreteiro"));        				             
		list.add(getColumn("dataHoraEmissao", "dtEmissao"));
		list.add(getColumn("cpfCnpjProprietario", "proprietarioIdentificacao"));
		list.add(getColumn("nomeProprietario", "proprietarioNome"));
		list.add(getColumn("frota", "meioTransporteNrFrota"));
		list.add(getColumn("placa", "meioTransporteNrIdentificador"));
		list.add(getColumn("valorLiquido", "vlLiquido"));
		list.add(getColumn("valorBruto", "vlBruto"));
		list.add(getColumn("observacao", "observacao"));
		list.add(getColumn("criadorEmissor", "criadorEmissor"));
		list.add(getColumn("ultimoAprovador", "ultimoAprovador"));
		list.add(getColumn("situacaoRecibo", "situacaoRecibo"));
		list.add(getColumn("dataPagamentoReal", "dtPgtoReal"));
		list.add(getColumn("dataPagamentoProgramada", "dtPgtoProgramada")); 
		
		return list;
	}
	
	/**
	 * Cria e retona o relatorio.
	 * 
	 * @param filter
	 * 
	 * @return Response
	 */
	@POST
	@Path("/exportFullCsv")
	public Response exportFullCsv(ReciboComplementarFilterDTO filter) {
		if (!isValidLimit(filter, ROW_LIMIT, count(filter))) {
			return getException();
		}
		
		List<Map<String, Object>> list = findDataForFullReport(filter);
		ResponseDTO responseDTO = new ResponseDTO();
		
		if (list.isEmpty()) {
			responseDTO.setInfo(getLabel("grid.paginacao.nenhum-registro").replace("<BR>", ""));
			return Response.ok(responseDTO).build();
		}
		
		try {
			responseDTO.setFileName(ExportUtils.exportCsv(getReportOutputDir(), "CSV", list, getColumnsFullReport()));
		} catch (Exception e) {
			throw new BusinessException("fileReportError");
		}
		
		return Response.ok(responseDTO).build();
	}
		
	private File getReportOutputDir(){		
		return reportExecutionManager.getReportOutputDir();
	}
	
	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("tipoRecibo" , "tpReciboFreteCarreteiro"));
		list.add(getColumn("filial" , "sgFilialComplementado"));
		list.add(getColumn("recibo" , "nrReciboFreteCarreteiroComplementado"));   				             
		list.add(getColumn("filial" , "sgFilialRecibo"));
		list.add(getColumn("reciboComplementar" , "nrReciboFreteCarreteiro"));
		list.add(getColumn("dataEmissao" , "dhEmissao"));
		list.add(getColumn("situacao" , "tpSituacaoRecibo"));
		list.add(getColumn("filial" , "sgFilialControleCarga"));
		list.add(getColumn("controleCarga" , "nrControleCarga"));
		list.add(getColumn("tipo" , "tpIdentificacaoProprietario"));
		list.add(getColumn("identificacao" , "nrIdentificacaoProprietario"));
		list.add(getColumn("proprietario" , "nmPessoaProprietario"));
		list.add(getColumn("frota" , "nrFrota"));
		list.add(getColumn("placa" , "nrIdentificadorMeioTransporte"));
		list.add(getColumn("dataPagamentoReal" , "dtPgtoReal"));
		list.add(getColumn("moeda" , "sgMoeda"));
		list.add(getColumn("simbolo" , "dsSimbolo"));
		list.add(getColumn("valorLiquido" , "vlLiquido"));
		
		return list;
	}
	
	/**
	 * Emite um relatório de um recibo complementar.
	 * 
	 * @param id
	 * @return Response
	 * @throws Exception
	 */
	@GET
	@Path("emitirRecibo")
	public Response emitir(@QueryParam("id") Long id) throws Exception {		
		TypedFlatMap reportCriteria = new TypedFlatMap();
		boolean blReemissao = reciboFreteCarreteiroService.storeValidateEmissaoReciboColetaEntrega(id);
		reportCriteria.put(id, new Object[]{Boolean.valueOf(blReemissao)});
		
		changeWorkflow(id, blReemissao);
		
		Map<String, String> retorno = new HashMap<String, String>();
		String fileName = reportExecutionManager.generateReportLocator(emitirReciboService, reportCriteria);
		retorno.put("fileName", fileName);
		
		return Response.ok(retorno).build();
	}

	/**
	 * Se é re-emissão deve validar o status do workflow.
	 * 
	 * @param id
	 * @param blReemissao
	 */
	private void changeWorkflow(Long id, boolean blReemissao) {
		if(!blReemissao){
			return;
		}
		
		ReciboFreteCarreteiro reciboFreteCarreteiro = reciboFreteCarreteiroService.findById(id);
		
		/*
		 * Se estiver em aguardando assinaturas deve sempre alterar para emitido. 
		 */
		if("AA".equals(reciboFreteCarreteiro.getTpSituacaoRecibo().getValue())){			
			reciboFreteCarreteiro.setTpSituacaoRecibo(new DomainValue("EM"));
			reciboFreteCarreteiroService.store(reciboFreteCarreteiro);
		}		
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
	@Path("/findAnexoReciboFcById")
	public Response findAnexoReciboFcById(@QueryParam("id") Long id) {
		Map<String, Object> result = new HashMap<>();
		result.put("table", "ANEXO_RECIBO_FC");
		result.put("blobColumn", "DC_ARQUIVO");
		result.put("idColumn", "ID_ANEXO_RECIBO_FC");
		result.put("id", id);

		return Response.ok(result).build();
	}
	
	/**
	 * Retorna os anexos vinculados ao recibo.
	 * 
	 * @param filter
	 * 
	 * @return Response
	 */	
	@POST
	@Path("/findAnexos")
	@SuppressWarnings("unchecked")
	public Response findAnexos(Map<String, Object> filter) {		
		Long idReciboFreteCarreteiro = MapUtils.getLong(MapUtils.getMap(filter, "filter"), "idReciboFreteCarreteiro");
		
		if(idReciboFreteCarreteiro == null){
			return Response.ok().build();	
		}
		
		List<Map<String, Object>> listAnexoReciboFc = anexoReciboFcService.findItensByIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
				
		return getReturnFind(listAnexoReciboFc, listAnexoReciboFc.size());		
	}
	
	/**
	 * Executa o count de anexos cadastrados.
	 * 
	 * @param idReciboFreteCarreteiro
	 * 
	 * @return Response
	 */	
	@GET
	@Path("/countAnexos")
	public Response countAnexos(@QueryParam("id") Long idReciboFreteCarreteiro) {		
		return Response.ok(anexoReciboFcService.getRowCountItensByIdReciboFreteCarreteiro(idReciboFreteCarreteiro)).build();		
	}
	
	/**
	 * Remove um ou mais itens da tabela de arquivos de anexos.
	 * 
	 * @param ids
	 */
	@POST
	@Path("removeAnexoReciboFcByIds")
	public void removeAnexoReciboFcByIds(List<Long> ids) {
		anexoReciboFcService.removeByIds(ids);
	}
	
	/**
	 * Como esta tela não é um CRUD de manipulação padrão, este método não deve
	 * ser utilizado.
	 * 
	 * @see ReciboComplementarRest#store(FormDataMultiPart)
	 */
	@Override
	protected Long store(ReciboComplementarDTO reciboFreteCarreteiroDTO) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Não é possivel excluir um recibo.
	 */
	@Override
	protected void removeById(Long id) {
		throw new UnsupportedOperationException();
    }       

	/**
	 *  Não é possivel excluir um recibo.
	 */
	@Override
	protected void removeByIds(List<Long> ids) {
		throw new UnsupportedOperationException();
    }
	
	/**
	 * Prepara os anexos do recibo para serem persistidos.
	 * 
	 * @param formDataMultiPart
	 * @param files
	 * 
	 * @return List<AnexoReciboFc> 
	 * 
	 * @throws IOException
	 */
	private List<AnexoReciboFc> getListAnexoReciboFc(
			FormDataMultiPart formDataMultiPart, List<Map<String, Object>> files)
			throws IOException {
		List<AnexoReciboFc> listAnexoReciboFc = new ArrayList<AnexoReciboFc>();
		
		if(files == null || files.isEmpty()){
			return listAnexoReciboFc;
		}
				
		for (int i = 0; i < files.size(); i++) {
			AnexoReciboFc anexoReciboFc = ReciboComplementarRestPopulateHelper.getAnexoReciboComplementar(
					(Map<String, Object>) files.get(i),
					getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo_" + i));
			
			listAnexoReciboFc.add(anexoReciboFc);
		}
		
		return listAnexoReciboFc;
	}
	
	/**
	 * Executa o store das alterações.
	 * 
	 * @param formDataMultiPart
	 * @return Response
	 * 
	 * @throws IOException 
	 */	
	@POST
	@Path("store")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response store(FormDataMultiPart formDataMultiPart) throws IOException {
		ReciboComplementarDTO reciboComplementarDTO = getModelFromForm(formDataMultiPart, ReciboComplementarDTO.class, "data");
		ReciboFreteCarreteiro reciboFreteCarreteiro = ReciboComplementarRestPopulateHelper.getReciboFreteCarreteiro(reciboComplementarDTO);
		
		ReciboComplementarRestHelper.validaReciboComplementar(reciboComplementarDTO, reciboFreteCarreteiro);
		
		reciboFreteCarreteiro = reciboFreteCarreteiroService.storeReciboComplementar(
				reciboFreteCarreteiro, 
				getListAnexoReciboFc(formDataMultiPart, reciboComplementarDTO.getFiles()));
		
		return findByStore(reciboFreteCarreteiro.getIdReciboFreteCarreteiro(), reciboComplementarDTO.getWorkflow());
    }
		
	/**
	 * Identifica o tipo de retorno do objeto de tela de acordo com o lugar que
	 * o store foi chamado.
	 * 
	 * @param idReciboFreteCarreteiro
	 * @param workflow
	 * 
	 * @return Response
	 */
	private Response findByStore(Long idReciboFreteCarreteiro, Boolean workflow){
		ReciboComplementarDTO reciboComplementarDTO = null;
		
		if(workflow != null && workflow){
			reciboComplementarDTO = findByIdProcesso(idReciboFreteCarreteiro);
		} else {
			reciboComplementarDTO = findById(idReciboFreteCarreteiro);
		}
		
		return Response.ok(reciboComplementarDTO).build();
	}
	
	/**
	 * Emite um relatório de um recibo de viagem.
	 * 
	 * @param id
	 * 
	 * @return Response
	 * @throws Exception
	 */
	@GET
	@Path("emitirReciboViagem")
	public Response emitirReciboViagem(@QueryParam("id") Long id) throws Exception {
		TypedFlatMap reportCriteria = new TypedFlatMap();

		reportCriteria.put("idReciboFreteCarreteiro",id);
		reportCriteria.put("blReemissao", reciboFreteCarreteiroService.storeValidateEmissaoReciboViagem(id));
		reportCriteria.put("blAdiantamento", reciboFreteCarreteiroService.validateIfBlAdiantamento(id));
		
    	Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("fileName", this.reportExecutionManager.generateReportLocator(emitirReciboViagemService, reportCriteria));
		
		return Response.ok(retorno).build();
	}
	
	/**
	 * Executa o cancelamento de um recibo complementar.
	 * 
	 * @param bean
	 * @return Response
	 */
	@GET
	@Path("cancelar")
	public Response cancelar(@QueryParam("id") Long id){    	    	
    	reciboFreteCarreteiroService.storeCancelarRecibo(id);
    	    	
    	return Response.ok(findById(id)).build();
	}	
	
	/**
	 * Retorna os tipos de moedas existentes.
	 * 
	 * @return Response
	 */
	@GET
	@Path("populateMoedas")
	public Response populateMoedas() {
		List<Map<String,Object>> retorno = new ArrayList<Map<String,Object>>();
		List<MoedaPais> moedas = configuracoesFacade.getMoedasPais(SessionUtils.getPaisSessao().getIdPais(), Boolean.TRUE);
		
		for (MoedaPais moedaPais : moedas) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sigla", moedaPais.getMoeda().getSgMoeda() + " " + moedaPais.getMoeda().getDsSimbolo());
			map.put("id", moedaPais.getIdMoedaPais());
			
			retorno.add(map);
		}
		
		return Response.ok(retorno).build(); 
	}
}