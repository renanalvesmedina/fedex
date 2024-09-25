package com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro;

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
import org.joda.time.DateTime;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoDescontoRfc;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoTabelaFreteCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFreteCarreteiroCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TabelaFreteCarreteiroCeService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto.TabelaFcValoresRestDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto.TabelaFreteCarreteiroCeFilterDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto.TabelaFreteCarreteiroCeRestDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.helper.TabelaFreteCarreteiroCeRestPopulateHelper;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.workflow.model.service.MensagemService;

/**
 * Rest responsável pelo controle da tela manter tabela frete carreteiro
 * coleta/entrega.
 * 
 */
@Path("/fretecarreteirocoletaentrega/tabelaFreteCarreteiroCe")
public class TabelaFreteCarreteiroCeRest extends LmsBaseCrudReportRest<TabelaFreteCarreteiroCeRestDTO, TabelaFreteCarreteiroCeRestDTO, TabelaFreteCarreteiroCeFilterDTO> {

	@InjectInJersey
	private TabelaFreteCarreteiroCeService tabelaFreteCarreteiroCeService;
	
	@InjectInJersey
	private TipoMeioTransporteService tipoMeioTransporteService;
	
	@InjectInJersey
	private MensagemService mensagemService;
		
	@Override
	protected List<TabelaFreteCarreteiroCeRestDTO> find(TabelaFreteCarreteiroCeFilterDTO filter) {	
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		TypedFlatMap filterMap = TabelaFreteCarreteiroCeRestPopulateHelper.getFilterMap(criteria, filter);
		
		return TabelaFreteCarreteiroCeRestPopulateHelper.getListTabelaFreteCarreteiroCeDTO(tabelaFreteCarreteiroCeService.findPaginatedCustom(filterMap));
	}
	
	@Override
	protected Integer count(TabelaFreteCarreteiroCeFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);
		
		TypedFlatMap filterMap = TabelaFreteCarreteiroCeRestPopulateHelper.getFilterMap(criteria, filter);
		
		return tabelaFreteCarreteiroCeService.getRowCountCustom(filterMap);
	}
	
	@Override
	protected TabelaFreteCarreteiroCeRestDTO findById(Long id) {
		TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO = TabelaFreteCarreteiroCeRestPopulateHelper.getTabelaFreteCarreteiroCeRestDTO(tabelaFreteCarreteiroCeService.findById(id));
		
		defineAlertMessage(tabelaFreteCarreteiroCeDTO);
		
    	return tabelaFreteCarreteiroCeDTO;
	}  
	
	@Override
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> findDataForReport(TabelaFreteCarreteiroCeFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		return TabelaFreteCarreteiroCeRestPopulateHelper.getListForReport(tabelaFreteCarreteiroCeService.findReportData(TabelaFreteCarreteiroCeRestPopulateHelper.getFilterMap(criteria, filter)));
	}
		
	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("sigla", "sgFilial"));
		list.add(getColumn("filial", "nmFantasia"));
		list.add(getColumn("numeroTabelaFrete", "nrTabelaFreteCarreteiroCe"));		
		list.add(getColumn("operacao", "tpOperacao"));
		list.add(getColumn("vinculo", "tpVinculo"));
		list.add(getColumn("vigenciaInicial", "dhVigenciaInicial"));
		list.add(getColumn("vigenciaFinal", "dhVigenciaFinal"));
		list.add(getColumn("dataAtualizacao", "dtAtualizacao"));
		list.add(getColumn("numeroRota", "nrRotaColetaEntrega"));
		list.add(getColumn("nomeRota", "dsRotaColetaEntrega"));
		list.add(getColumn("tipo", "tpIdentificacaoProprietario"));
		list.add(getColumn("identificacao", "nrIdentificacaoProprietarioFormatado"));
		list.add(getColumn("proprietario", "nmPessoaProprietario"));		
		list.add(getColumn("tipo", "tpIdentificacaoCliente"));
		list.add(getColumn("identificacao", "nrIdentificacaoClienteFormatado"));
		list.add(getColumn("cliente", "nmPessoaCliente"));
		list.add(getColumn("municipio", "nmMunicipio"));
		list.add(getColumn("tipoTransporte", "dsTipoMeioTransporte"));
		list.add(getColumn("frota", "nrFrotaMeioTransporte"));
		list.add(getColumn("placa", "nrIdentificadorMeioTransporte"));
		
		return list;
	}
		
	@Override
	protected Long store(TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO) {		
		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = TabelaFreteCarreteiroCeRestPopulateHelper.getTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCeDTO);
		
		tabelaFreteCarreteiroCe = tabelaFreteCarreteiroCeService.storeTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
				
		return tabelaFreteCarreteiroCe.getIdTabelaFreteCarreteiroCe();
	}
	
	@POST
	@Path("storeTabelaFcValores")
	public Response storeTabelaFcValores(TabelaFcValoresRestDTO tabelaFcValoresRestDTO) {		
		TabelaFcValores tabelaFcValores = TabelaFreteCarreteiroCeRestPopulateHelper.getTabelaFcValores(tabelaFcValoresRestDTO);
		
		tabelaFreteCarreteiroCeService.storeTabelaFcValores(tabelaFcValores, tabelaFcValores.getTabelaFreteCarreteiroCe());
				
		return findTabelaFcValores(tabelaFcValores.getIdTabelaFcValores());
	}
	
	/**
	 * Remove um ou mais itens da tabela de valores.
	 * 
	 * @param ids
	 */
	@POST
	@Path("removeTabelaValoresByIds")
	public Response removeTabelaValoresByIds(List<Long> ids) {
		tabelaFreteCarreteiroCeService.removeCascadeTabelaValoresByIds(ids);
		
		return Response.ok().build();
	}
	
	/**
	 * Remove um item da tabela de valores.
	 * 
	 * @param id
	 */
	@GET
	@Path("removeTabelaValoresById")
	public Response removeTabelaValoresById(@QueryParam("id") Long id) {		
		tabelaFreteCarreteiroCeService.removeCascadeTabelaValoresById(id);
		
		return Response.ok().build();
	}

	@Override
	protected void removeById(Long id) {
		tabelaFreteCarreteiroCeService.removeCascadeById(id);
    }       

	/**
	 *  Não é possivel excluir uma tabela de frete carreteiro.
	 */
	@Override
	protected void removeByIds(List<Long> ids) {
		throw new UnsupportedOperationException();
    }
	
	/**
	 * Pesquisa por uma tabela de valores existentes.
	 * 
	 * @param id
	 * @return Response
	 */
	@GET
	@Path("findTabelaFcValores")
	public Response findTabelaFcValores(@QueryParam("id") Long id){
		return Response.ok(TabelaFreteCarreteiroCeRestPopulateHelper.getTabelaFcValoresRestDTO(tabelaFreteCarreteiroCeService.findTabelaFcValores(id))).build(); 
	}
	
	/**
	 * Pesquisa por uma tabela de valores geral existente.
	 * 
	 * @param id
	 * @return Response
	 */
	@GET
	@Path("findTabelaFcValoresGeral")
	public Response findTabelaFcValoresGeral(@QueryParam("id") Long idTabelaFreteCarreteiroCe){
		return Response.ok(TabelaFreteCarreteiroCeRestPopulateHelper.getTabelaFcValoresRestDTO(tabelaFreteCarreteiroCeService.findTabelaFcValoresGeral(idTabelaFreteCarreteiroCe))).build(); 
	}
	
	@GET
	@Path("findProprietarioByIdMeioTransporte")
	public Response findProprietarioByIdMeioTransporte(@QueryParam("id") Long id){
		Map<String,String> retorno  = tabelaFreteCarreteiroCeService.getProprietarioByMeioTransporte(id);
		return Response.ok(retorno).build(); 
	}	
	
	/**
	 * Retorna os itens da tabela de valor existentes.
	 * 
	 * @return Response
	 */
	@POST
	@Path("findListTabelaFcValores")
	public Response findListTabelaFcValores(TabelaFreteCarreteiroCeFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);
		
		criteria.put("idTabelaFreteCarreteiroCe", filter.getIdTabelaFreteCarreteiroCe());
		criteria.put("blTipo", filter.getBlTipo().getValue());
				
		Integer rowCount = tabelaFreteCarreteiroCeService.getRowCountListTabelaFcValores(criteria);
		
		if(rowCount > 0){
			return getReturnFind(
					TabelaFreteCarreteiroCeRestPopulateHelper.getListTabelaFcValoresRestDTO(tabelaFreteCarreteiroCeService.findListTabelaFcValores(criteria)),
					rowCount);
		}
		
		return Response.ok().build();
	}
	
	/**
	 * Retorna os tipos de meio de transportes existentes.
	 * 
	 * @return Response
	 */
	@GET
	@Path("populateTipoMeioTransporte")
	public Response populateTipoMeioTransporte() {
		return Response.ok(tipoMeioTransporteService.findCombo(null)).build(); 
	}
	
	/**
	 * Remove um ou mais itens da tabela de valores.
	 * 
	 * @param ids
	 */
	@POST
	@Path("inativar")
	public TabelaFreteCarreteiroCeRestDTO inativar(TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeRestDTO) {
		Long idTabelaFreteCarreteiroCe = tabelaFreteCarreteiroCeRestDTO.getIdTabelaFreteCarreteiroCe();
		
		tabelaFreteCarreteiroCeService.storeDhVigenciaFinal(idTabelaFreteCarreteiroCe, tabelaFreteCarreteiroCeRestDTO.getDhVigenciaFinal());
		
		return findById(idTabelaFreteCarreteiroCe);
	}
	
	@POST
	@Path("checkZeroTabelaFcValores")
	public Response checkZeroTabelaFcValores(TabelaFcValoresRestDTO tabelaFcValoresRestDTO){		
		return Response.ok(TabelaFreteCarreteiroCeRestPopulateHelper.isZeroTabelaFcValores(tabelaFcValoresRestDTO)).build(); 
	}
	
	/**
	 * Executa o clone de uma tabela de frete carreteiro.
	 * 
	 * @param id
	 */
	@GET
	@Path("clonarTabelaFreteCarreteiroCe")
	public TabelaFreteCarreteiroCeRestDTO clonarTabelaFreteCarreteiroCe(@QueryParam("id") Long idTabelaFreteCarreteiroCe) {
		return findById(tabelaFreteCarreteiroCeService.executeClonarTabelaFreteCarreteiroCe(idTabelaFreteCarreteiroCe));
	}
	
	/**
	 * Retorna os anexos vinculados a tabela de frete.
	 * 
	 * @param filtro
	 * 
	 * @return Response
	 */
	@POST
	@Path("/findAnexos")
	public Response findAnexos(Map<String, Object> filtro) {		
		Long idTabelaFreteCarreteiroCe = MapUtils.getLong(MapUtils.getMap(filtro, "filtros"), "idTabelaFreteCarreteiroCe");
		
		if(idTabelaFreteCarreteiroCe == null){
			return Response.ok().build();	
		}
		
		List<Map<String, Object>> listAnexoNotaCredito = tabelaFreteCarreteiroCeService.findAnexoTabelaFreteCeByIdAnexoTabelaFreteCarreteiroCe(idTabelaFreteCarreteiroCe);
				
		return getReturnFind(listAnexoNotaCredito, listAnexoNotaCredito.size());		
	}
	
	/**
	 * Remove um ou mais itens da tabela de arquivos de anexos.
	 * 
	 * @param ids
	 */
	@POST
	@Path("removeAnexoTabelaFreteRfcByIds")
	public void removeAnexoTabelaFreteRfcByIds(List<Long> ids) {
		tabelaFreteCarreteiroCeService.removeByIdsAnexoTabelaFreteCe(ids);
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
		result.put("table", "ANEXO_TABELA_FRETE_CE");
		result.put("blobColumn", "DC_ARQUIVO");
		result.put("idColumn", "ID_ANEXO_TABELA_FRETE_CE");
		result.put("id", id);

		return Response.ok(result).build();
	}
	
	/**
	 * Executa o store dos anexos.
	 * 
	 * @param formDataMultiPart
	 * @return Response
	 * 
	 * @throws IOException 
	 */	
	@POST
	@Path("storeAnexos")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response storeAnexos(FormDataMultiPart formDataMultiPart) throws IOException {
		TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeRestDTO = getModelFromForm(formDataMultiPart, TabelaFreteCarreteiroCeRestDTO.class, "data");	
		
		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = tabelaFreteCarreteiroCeService.storeAnexos(
				tabelaFreteCarreteiroCeRestDTO.getIdTabelaFreteCarreteiroCe(), 
				getListAnexoTabelaFreteCe(formDataMultiPart, tabelaFreteCarreteiroCeRestDTO.getFiles()));
				
		return Response.ok(findById(tabelaFreteCarreteiroCe.getIdTabelaFreteCarreteiroCe())).build();
    }
	
	/**
	 * Prepara os anexos da tabela de frete para serem persistidos.
	 * 
	 * @param formDataMultiPart
	 * @param files
	 * 
	 * @return List<AnexoTabelaFreteCe> 
	 * 
	 * @throws IOException
	 */
	private List<AnexoTabelaFreteCe> getListAnexoTabelaFreteCe(
			FormDataMultiPart formDataMultiPart, List<Map<String, Object>> files)
			throws IOException {
		List<AnexoTabelaFreteCe> listAnexoTabelaFreteCe = new ArrayList<AnexoTabelaFreteCe>();
		
		if(files == null || files.isEmpty()){
			return listAnexoTabelaFreteCe;
		}
				
		for (int i = 0; i < files.size(); i++) {
			AnexoTabelaFreteCe anexoTabelaFreteCe = TabelaFreteCarreteiroCeRestPopulateHelper.getAnexoTabelaFreteCe(
					(Map<String, Object>) files.get(i),
					getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo_" + i));
			
			listAnexoTabelaFreteCe.add(anexoTabelaFreteCe);
		}
		
		return listAnexoTabelaFreteCe;
	}	
	
	/**
	 * Define um alerta personalizado sobre o estado atual da tabela de frete.
	 * 
	 * @param tabelaFreteCarreteiroCeDTO
	 */
	private void defineAlertMessage(TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO){
		DateTime dhVigenciaInicial = tabelaFreteCarreteiroCeDTO.getDhVigenciaInicial();
		DateTime dhVigenciaFinal = tabelaFreteCarreteiroCeDTO.getDhVigenciaFinal();
		
		/*
		 * Caso não tenha vigência definida.
		 */
		if(dhVigenciaInicial == null){		
			if(tabelaFreteCarreteiroCeDTO.getIdTabelaClonada() == null){
				setMessageType(tabelaFreteCarreteiroCeDTO, "draft", "bullhorn", "LMS-25112", null);
			} else {
				StringBuilder tabelaClonada = new StringBuilder();
				tabelaClonada.append(tabelaFreteCarreteiroCeDTO.getSgFilialTabelaClonada());
				tabelaClonada.append(" ");
				tabelaClonada.append(FormatUtils.formatLongWithZeros(tabelaFreteCarreteiroCeDTO.getNrTabelaClonada(), "0000000000"));
				
				setMessageType(tabelaFreteCarreteiroCeDTO, "clone", "sheep", "LMS-25125", tabelaClonada.toString());
			}						
		} else {		
			DateTime dtAtual = JTDateTimeUtils.getDataHoraAtual();
		
			/*
			 * Caso tenha apenas vigência inicial definida ou intervalo de
			 * vigência.
			 */
			if(dhVigenciaInicial.compareTo(dtAtual) > 0){
				setMessageType(tabelaFreteCarreteiroCeDTO, "info", "time", "LMS-25113", JTFormatUtils.format(dhVigenciaInicial));				
			} else if(dhVigenciaInicial.compareTo(dtAtual) < 0 && (dhVigenciaFinal == null || dhVigenciaFinal.compareTo(dtAtual) >= 0)){
				String params = mensagemService.getMessage("indefinidamente");
				
				if(dhVigenciaFinal != null){
					params = mensagemService.getMessage("ate") + " " + JTFormatUtils.format(dhVigenciaFinal);					
				}
				
				setMessageType(tabelaFreteCarreteiroCeDTO, "success", "ok", "LMS-25114", params);				
			} else if(dhVigenciaInicial.compareTo(dtAtual) < 0 && dhVigenciaFinal.compareTo(dtAtual) <= 0){				
				setMessageType(tabelaFreteCarreteiroCeDTO, "old", "lock", "LMS-25115", JTFormatUtils.format(dhVigenciaFinal));
			}
		}
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
	private void setMessageType(TabelaFreteCarreteiroCeRestDTO dto, String type, String icon, String lms, String params) {
		dto.setMessageType("bg-" + type);
		dto.setMessageTypeIcon("glyphicon-" + icon);
		dto.setMessageText(mensagemService.getMessage(lms, new Object[]{ params }));
	}
}