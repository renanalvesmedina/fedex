package com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto;

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
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.PaginationDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.DescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.DescontoRfcService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto.DescontoRfcDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.dto.DescontoRfcFilterDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.helper.DescontoHelper;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.desconto.helper.DescontoRfcRestPopulateHelper;

/**
 * Rest responsável pelo controle da tela manter descontos.
 * 
 */
@Path("/fretecarreteirocoletaentrega/descontoRfc")
public class DescontoRfcRest extends LmsBaseCrudReportRest<DescontoRfcDTO, DescontoRfcDTO, DescontoRfcFilterDTO> {

	@InjectInJersey 
	private DescontoRfcService descontoRfcService;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	@InjectInJersey
	private ProprietarioService proprietarioService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;
	
	@Override
	protected List<DescontoRfcDTO> find(DescontoRfcFilterDTO filter) {	
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		TypedFlatMap filterMap = DescontoRfcRestPopulateHelper.getFilterMap(criteria, filter);
		
		return DescontoRfcRestPopulateHelper.getListDescontoRfcDTO(descontoRfcService.findPaginatedCustom(filterMap));
	}
	
	@Override
	protected Integer count(DescontoRfcFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);
		
		TypedFlatMap filterMap = DescontoRfcRestPopulateHelper.getFilterMap(criteria, filter);
		
		return descontoRfcService.getRowCountCustom(filterMap);
	}
	
	@Override
	protected DescontoRfcDTO findById(Long id) {
    	return DescontoRfcRestPopulateHelper.getDescontoRfcDTO(descontoRfcService.findById(id), false);
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
	public DescontoRfcDTO findByIdProcesso(@QueryParam("id") Long id) {				
		return DescontoRfcRestPopulateHelper.getDescontoRfcDTO(descontoRfcService.findById(id), true);
    }   
	
	@Override
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> findDataForReport(DescontoRfcFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		return DescontoRfcRestPopulateHelper.getListForReport(descontoRfcService.findPaginatedCustom(DescontoRfcRestPopulateHelper.getFilterMap(criteria, filter)));
	}
		
	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("sigla", "sgFilial"));
		list.add(getColumn("filial", "nmFantasia"));
		list.add(getColumn("numeroDesconto", "nrDescontoRfc"));		
		list.add(getColumn("motivo", "dsTipoDescontoRfc"));
		list.add(getColumn("valorTotal", "vlTotalDesconto"));
		list.add(getColumn("dataInicio", "dtInicioDesconto"));
		list.add(getColumn("valorSaldoDevedor", "vlSaldoDevedor"));		
		list.add(getColumn("situacao", "tpSituacao"));		
		list.add(getColumn("nomeProprietario", "nmPessoa"));
		list.add(getColumn("tipo", "tpIdentificacao"));
		list.add(getColumn("identificacao", "nrIdentificacao"));
		list.add(getColumn("tipoProprietario", "tpProprietario"));
		list.add(getColumn("frota", "nrFrota"));
		list.add(getColumn("placa", "nrIdentificador"));
		list.add(getColumn("rim", "nrReciboIndenizacao"));
		list.add(getColumn("semiReboque", "nrIdentificacaoSemiReboque"));
		list.add(getColumn("dataAtualizacao", "dtAtualizacao"));
		
		return list;
	}
	
	/**
	 * Retorna os tipos de descontos rfc existentes.
	 * 
	 * @return Response
	 */
	@GET
	@Path("populateTipoDescontoRfc")
	public Response populateTipoDescontoRfc() {
		return Response.ok(descontoRfcService.findAllTipoDescontoRfc()).build(); 
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
	public Response findAnexoDescontoRfcById(@QueryParam("id") Long id) {
		Map<String, Object> result = new HashMap<>();
		result.put("table", "ANEXO_DESCONTO_RFC");
		result.put("blobColumn", "DC_ARQUIVO");
		result.put("idColumn", "ID_ANEXO_DESCONTO_RFC");
		result.put("id", id);

		return Response.ok(result).build();
	}
	
	/**
	 * Retorna os anexos vinculados ao desconto.
	 * 
	 * @param filtro
	 * 
	 * @return Response
	 */
	@POST
	@Path("/findAnexos")
	public Response findAnexos(Map<String, Object> filtro) {		
		Long idDesconto = MapUtils.getLong(MapUtils.getMap(filtro, "filtros"), "idDesconto");
		
		if(idDesconto == null){
			return Response.ok().build();	
		}
		
		List<Map<String, Object>> listAnexoDescontoRfc = descontoRfcService.findAnexoDescontoRfcByIdDescontoRfc(idDesconto);
		
		PaginationDTO pagination = new PaginationDTO();
		pagination.setList(listAnexoDescontoRfc);
		pagination.setQtRegistros(listAnexoDescontoRfc.size());
		
		return Response.ok(pagination).build();		
	}
		
	/**
	 * Remove um ou mais itens da tabela de arquivos de anexos.
	 * 
	 * @param ids
	 */
	@POST
	@Path("removeAnexoDescontoRfcByIds")
	public void removeAnexoDescontoRfcByIds(List<Long> ids) {
		descontoRfcService.removeByIdsAnexoDescontoRfc(ids);
	}
	
	/**
	 * Como esta tela não é um CRUD de manipulação padrão, este método não deve
	 * ser utilizado.
	 * 
	 * @see DescontoRfcRest#store(FormDataMultiPart)
	 */
	@Override
	protected Long store(DescontoRfcDTO descontoRfcDTO) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Não é possivel excluir um desconto.
	 */
	@Override
	protected void removeById(Long id) {
		throw new UnsupportedOperationException();
    }       

	/**
	 *  Não é possivel excluir um desconto.
	 */
	@Override
	protected void removeByIds(List<Long> ids) {
		throw new UnsupportedOperationException();
    }
	
	/**
	 * Prepara os anexos do desconto para serem persistidos.
	 * 
	 * @param formDataMultiPart
	 * @param files
	 * 
	 * @return List<AnexoDescontoRfc> 
	 * 
	 * @throws IOException
	 */
	private List<AnexoDescontoRfc> getListAnexoDescontoRfc(
			FormDataMultiPart formDataMultiPart, List<Map<String, Object>> files)
			throws IOException {
		List<AnexoDescontoRfc> listAnexoDescontoRfc = new ArrayList<AnexoDescontoRfc>();
		
		if(files == null || files.isEmpty()){
			return listAnexoDescontoRfc;
		}
				
		for (int i = 0; i < files.size(); i++) {
			AnexoDescontoRfc anexoDesconto = DescontoRfcRestPopulateHelper.getAnexoDescontoRfc(
					(Map<String, Object>) files.get(i),
					getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo_" + i));
			
			listAnexoDescontoRfc.add(anexoDesconto);
		}
		
		return listAnexoDescontoRfc;
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
		DescontoRfcDTO descontoRfcDTO = getModelFromForm(formDataMultiPart, DescontoRfcDTO.class, "data");	
		
		DescontoRfc descontoRfc = descontoRfcService.storeDescontoRfc(
				DescontoRfcRestPopulateHelper.getDescontoRfc(descontoRfcDTO), 
				getListAnexoDescontoRfc(formDataMultiPart, descontoRfcDTO.getFiles()));
				
		return Response.ok(findById(descontoRfc.getIdDescontoRfc())).build();
    }
    
	/**
	 * Gera parcelas de desconto.
	 * 
	 * @param criteria
	 * @return Response
	 */
	@POST
	@Path("/generateParcelas")
	public Response generateParcelas(DescontoRfcDTO descontoRfcDTO) {
		DescontoHelper descontoHelper = new DescontoHelper();
		descontoHelper.setParametroGeralService(parametroGeralService);
		descontoHelper.setConfiguracoesFacade(configuracoesFacade);
		
		return Response.ok(descontoHelper.gerarParcelas(descontoRfcDTO)).build();		
	}
	
	/**
	 * Verifica se é possivel cancelar o desconto.
	 * 
	 * @param idDescontoRfc
	 * 
	 * @return Response
	 */
	@GET
	@Path("/isCancelarDesconto")
	public Response isCancelarDesconto(@QueryParam("idDescontoRfc") Long idDescontoRfc){
		return Response.ok(descontoRfcService.validateCancelarDesconto(idDescontoRfc)).build();
	}
	
	/**
	 * Cancela um desconto.
	 * 
	 * @param bean
	 * 
	 * @return DescontoRfcDTO
	 */
	@GET
	@Path("/cancelarDesconto")
	public DescontoRfcDTO cancelarDesconto(@QueryParam("idDescontoRfc") Long idDescontoRfc) {		
		descontoRfcService.cancelarDesconto(idDescontoRfc);
		
		return findById(idDescontoRfc);
	}
	
	/**
	 * Procura um proprietário.
	 * 
	 * @param id
	 * 
	 * @return Response
	 */
	@GET
	@Path("/findProprietario")
	public Response findProprietario(@QueryParam("id") Long id) {
		Proprietario proprietario = proprietarioService.findById(id);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("dtVigenciaInicial", proprietario.getDtVigenciaInicial());
		map.put("dtVigenciaFinal", proprietario.getDtVigenciaFinal());
		map.put("tpSituacao", proprietario.getTpSituacao());
		
		return Response.ok(map).build();
	}
}