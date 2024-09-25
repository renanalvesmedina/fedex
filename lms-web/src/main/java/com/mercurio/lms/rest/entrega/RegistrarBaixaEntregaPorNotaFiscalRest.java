package com.mercurio.lms.rest.entrega;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import org.joda.time.DateTime;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.ListToMapConverter;
import com.mercurio.adsm.framework.util.RowMapper;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.entrega.model.service.RegistrarBaixaEntregaPorNotaFiscalService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rest.coleta.dto.OcorrenciaEntregaDTO;
import com.mercurio.lms.rest.entrega.dto.EntregaNotaFiscalDTO;
import com.mercurio.lms.rest.entrega.dto.NotaFiscalDTO;
import com.mercurio.lms.rest.entrega.dto.RegistrarBaixaEntregaPorNotaFiscalDTO;
import com.mercurio.lms.rest.entrega.dto.RegistrarBaixaEntregaPorNotaFiscalFilterDTO;
import com.mercurio.lms.rest.entrega.dto.RegistrarBaixaEntregaPorNotaFiscalListDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
 
@Path("/entrega/registrarBaixaEntregaPorNotaFiscal") 
public class RegistrarBaixaEntregaPorNotaFiscalRest extends BaseRest {

	private static final String ID_ENTREGA_NOTA_FISCAL = "id_entrega_nota_fiscal";
	private static final String ID_MANIFESTO_ENTREGA = "id_manifesto_entrega";
	private static final String DH_OCORRENCIA = "dh_ocorrencia";
	private static final String ID_DOCTOSERVICO = "idDoctoServico";
	private static final String ID_MANIFESTO = "idManifesto";

	@InjectInJersey
	private RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService;
	
	@InjectInJersey
	private DoctoServicoService doctoServicoService;
	
	@InjectInJersey
	private FilialService filialService;
	
	@InjectInJersey
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	
	@InjectInJersey
	private DomainValueService domainValueService;

	@InjectInJersey
	private IntegracaoJwtService integracaoJwtService;
	

	@POST
	@Path("inicializarCamposList")
	public Response inicializarCamposList() {
		RegistrarBaixaEntregaPorNotaFiscalFilterDTO filter = new RegistrarBaixaEntregaPorNotaFiscalFilterDTO();

		Filial filial = SessionUtils.getFilialSessao();
		filter.setFilial(new FilialSuggestDTO());
		filter.getFilial().setIdFilial(filial.getIdFilial());
		filter.getFilial().setSgFilial(filial.getSgFilial());
		filter.getFilial().setNmFilial(filial.getPessoa().getNmFantasia());

		return Response.ok(filter).build();
	}
	

	@POST
	@Path("find")
	public Response findRest(RegistrarBaixaEntregaPorNotaFiscalFilterDTO filter) {
		List<RegistrarBaixaEntregaPorNotaFiscalListDTO> lista = find(filter);
		return getReturnFind(lista, lista.size());
	}

	protected List<RegistrarBaixaEntregaPorNotaFiscalListDTO> find(RegistrarBaixaEntregaPorNotaFiscalFilterDTO registrarBaixaEntregaFilterDTO) {
		TypedFlatMap criteria = getTypedFlatMap(registrarBaixaEntregaFilterDTO);
		ResultSetPage<Map<String, Object>>  resultSetPage = registrarBaixaEntregaPorNotaFiscalService.findPaginated(criteria);
		return convertToListDTO(resultSetPage);
	}

	private List<RegistrarBaixaEntregaPorNotaFiscalListDTO> convertToListDTO(ResultSetPage<Map<String, Object>> resultSetPage) {
		List<RegistrarBaixaEntregaPorNotaFiscalListDTO> retorno = new ArrayList<RegistrarBaixaEntregaPorNotaFiscalListDTO>();
		
		if(resultSetPage.getList() != null){
			for(Map<String, Object> map : resultSetPage.getList()){
				RegistrarBaixaEntregaPorNotaFiscalListDTO registrarBaixaEntregaListDTO = new RegistrarBaixaEntregaPorNotaFiscalListDTO();
				registrarBaixaEntregaListDTO.setIdDoctoServico((Long) map.get("id_docto_servico"));
				
				DomainValue dmTipoDocumentoServico = domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO", (String) map.get("tp_documento_servico"));
				registrarBaixaEntregaListDTO.setDoctoServico(dmTipoDocumentoServico.getDescription() + " " +(String) map.get("sg_filial") + " " + (Long) map.get("nr_docto_servico"));
				
				registrarBaixaEntregaListDTO.setIdManifesto(map.get(ID_MANIFESTO_ENTREGA) != null ? (Long) map.get(ID_MANIFESTO_ENTREGA) : (Long) map.get("id_manifesto_viagem"));
				registrarBaixaEntregaListDTO.setTpManifesto(map.get(ID_MANIFESTO_ENTREGA) != null ? "E" : "V");
				registrarBaixaEntregaListDTO.setManifestoEntrega(map.get("nr_manifesto_entrega") != null ? (String) map.get("sg_filial_manifesto") + "-" + (String) map.get("nr_manifesto_entrega") : "");
				registrarBaixaEntregaListDTO.setManifestoViagem(map.get("nr_manifesto_viagem") != null ? (String) map.get("sg_filial_manifesto") + "-" + (String) map.get("nr_manifesto_viagem") : "");
				registrarBaixaEntregaListDTO.setOcorrenciaEntrega((String) map.get("cd_ocorrencia_entrega") + "-" + (String) map.get("ds_ocorrencia_entrega"));
				registrarBaixaEntregaListDTO.setDhOcorrencia(JTFormatUtils.format(new DateTime((Timestamp) map.get(DH_OCORRENCIA)), "dd/MM/yyyy HH:mm ZZ"));
				registrarBaixaEntregaListDTO.setNmUsuario((String) map.get("nm_usuario"));
				retorno.add(registrarBaixaEntregaListDTO);
			}
		}
		return retorno;
	}
	
	private Integer getRowCountNotas(RegistrarBaixaEntregaPorNotaFiscalFilterDTO filter) {
		TypedFlatMap criteria = getTypedFlatMap(filter);
		criteria.put(ID_DOCTOSERVICO, filter.getIdDoctoServico());
		criteria.put("alteracao", filter.getAlteracao());
		criteria.put(ID_MANIFESTO, filter.getIdManifesto());
		return registrarBaixaEntregaPorNotaFiscalService.getRowCountNotasFiscais(criteria);
	}
	
	@POST
	@Path("findNotas")
	public Response findNotas(RegistrarBaixaEntregaPorNotaFiscalFilterDTO filter){
		TypedFlatMap criteria = getTypedFlatMap(filter);
		criteria.put(ID_DOCTOSERVICO, filter.getIdDoctoServico());
		criteria.put("alteracao", filter.getAlteracao());
		criteria.put(ID_MANIFESTO, filter.getIdManifesto());
		ResultSetPage<Map<String, Object>>  resultSetPage = registrarBaixaEntregaPorNotaFiscalService.findNotasFiscais(criteria);
		return getReturnFind(convertToListNotaFiscalDTO(resultSetPage), getRowCountNotas(filter));
	}
	
	private List<NotaFiscalDTO> convertToListNotaFiscalDTO(ResultSetPage<Map<String, Object>> resultSetPage) {
		List<NotaFiscalDTO> retorno = new ArrayList<>();
		if(resultSetPage.getList() != null){
			for(Map<String, Object> map : resultSetPage.getList()) {
				retorno.add(convertToNotaFiscalDTO(map));
			}
		}
		return retorno;
	}

	private NotaFiscalDTO convertToNotaFiscalDTO(Map<String, Object> map) {
		NotaFiscalDTO notaFiscalDTO = new NotaFiscalDTO();
		notaFiscalDTO.setIdNotaFiscal((Long) map.get("id_nota_fiscal_conhecimento"));
		notaFiscalDTO.setNrNotaFiscal(((Long) map.get("nr_nota_fiscal")).toString());
		notaFiscalDTO.setQtVolume((Integer) map.get("qt_volumes"));
		notaFiscalDTO.setPsMercadoria((BigDecimal) map.get("ps_mercadoria"));
		notaFiscalDTO.setVlTotal((BigDecimal) map.get("vl_total"));

		if (map.get(ID_ENTREGA_NOTA_FISCAL) != null) {
			EntregaNotaFiscalDTO entregaNotaFiscalDTO = new EntregaNotaFiscalDTO();
			entregaNotaFiscalDTO.setIdEntregaNotaFiscal((Long) map.get(ID_ENTREGA_NOTA_FISCAL));
			entregaNotaFiscalDTO.setQtVolumesEntregues((Integer) map.get("qt_volumes_entregues"));
			entregaNotaFiscalDTO.setDhOcorrenciaString(JTFormatUtils.format(new DateTime((Timestamp) map.get(DH_OCORRENCIA)), "dd/MM/yyyy HH:mm ZZ"));
			entregaNotaFiscalDTO.setDhOcorrencia(new DateTime((Timestamp) map.get(DH_OCORRENCIA)));
			entregaNotaFiscalDTO.setUsuario((String) map.get("nmUsuario"));

			if (map.get("id_ocorrencia_entrega") != null) {
				entregaNotaFiscalDTO.setIdOcorrenciaEntrega((Long) map.get("id_ocorrencia_entrega"));
				entregaNotaFiscalDTO.setCdOcorrenciaEntrega((Short) map.get("cd_ocorrencia_entrega"));
				entregaNotaFiscalDTO.setDsOcorrenciaEntrega((String) map.get("ds_ocorrencia_entrega"));
			}

			notaFiscalDTO.setEntregaNotaFiscalDTO(entregaNotaFiscalDTO);
		}
		return notaFiscalDTO;
	}
	
	@GET
	@Path("/{idDoctoServico}/{idManifesto}/{tpManifesto}/{alteracao}")
	public Response findByIdDetalhamento(@PathParam(ID_DOCTOSERVICO) Long idDoctoServico, @PathParam("idManifesto") Long idManifesto, @PathParam("tpManifesto") String tpManifesto, @PathParam("alteracao") String alteracao){
		
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		Filial filialDoctoServico = filialService.findById(doctoServico.getFilialByIdFilialOrigem().getIdFilial());
		
		RegistrarBaixaEntregaPorNotaFiscalDTO retorno = new RegistrarBaixaEntregaPorNotaFiscalDTO();
		retorno.setIdDoctoServico(idDoctoServico);
		retorno.setIdManifesto(idManifesto);
		retorno.setTpManifesto(tpManifesto);
		retorno.setDoctoServico(doctoServico.getTpDocumentoServico().getDescription()+ " " + filialDoctoServico.getSgFilial() + " " + doctoServico.getNrDoctoServico());
		
		return Response.ok(retorno).build();
	}
	
	@POST
	@Path("findOcorrenciaEntrega")	
	public Response findOcorrenciaEntrega() {
		List<OcorrenciaEntrega> listOcorrenciaEntrega = ocorrenciaEntregaService.findOcorrenciaEntregaRegistrarBaixaNotaAtivo();
		
		List<OcorrenciaEntregaDTO> listDto = new ArrayList<OcorrenciaEntregaDTO>();
		for (OcorrenciaEntrega oe : listOcorrenciaEntrega) {
			listDto.add(new OcorrenciaEntregaDTO(oe.getIdOcorrenciaEntrega(), oe.getCdOcorrenciaEntrega(), oe.getDsOcorrenciaEntrega().getValue()));
		}
		
		return Response.ok(listDto).build();
	}
	
	@POST
	@Path("registrarBaixaEntrega")
	public Response registrarBaixaEntrega(List<EntregaNotaFiscalDTO> listEntregaNotaFiscalDTO, @Context HttpHeaders headers) {
		
		validateQtVolumesEntregues(listEntregaNotaFiscalDTO);
		AutenticacaoDMN autenticacaoDMN = integracaoJwtService.findAutenticacaoDMN(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0));
		Filial filial = integracaoJwtService.getFilialSessao(integracaoJwtService.getIdFilialByToken(headers.getRequestHeader(ConstantesAmbiente.TOKEN).get(0)));
		registrarBaixaEntregaPorNotaFiscalService.executeRegistrarBaixaEntrega(listToMapConverter(listEntregaNotaFiscalDTO),
				integracaoJwtService.getUsuarioSessao(autenticacaoDMN), filial);
		
		return Response.ok().build();
	}

	private List<Map<String, Object>> listToMapConverter(List<EntregaNotaFiscalDTO> listEntregaNotaFiscalDTO) {
		return new ListToMapConverter<EntregaNotaFiscalDTO>().mapRows(listEntregaNotaFiscalDTO, new RowMapper<EntregaNotaFiscalDTO>() {
			@Override
			public Map<String, Object> mapRow(EntregaNotaFiscalDTO o) {
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("idEntregaNotaFiscal", o.getIdEntregaNotaFiscal());
				map.put(ID_DOCTOSERVICO, o.getIdDoctoServico());
				map.put(ID_MANIFESTO, o.getIdManifesto());
				map.put("idNotaFiscal", o.getIdNotaFiscal());
				map.put("tpManifesto", o.getTpManifesto());
				map.put("cdOcorrenciaEntrega", o.getCdOcorrenciaEntrega());
				map.put("idOcorrenciaEntregaAnterior", o.getIdOcorrenciaEntregaAnterior());
				map.put("dhOcorrencia", o.getDhOcorrencia());
				map.put("qtVolumesEntregues", o.getQtVolumesEntregues());
				return map;
			}
		});
	}
	
	private void validateQtVolumesEntregues(List<EntregaNotaFiscalDTO> listEntregaNotaFiscalDTO){
		for (EntregaNotaFiscalDTO entregaNotaFiscalDTO : listEntregaNotaFiscalDTO) {
			Short cdOcorrenciaEntrega = entregaNotaFiscalDTO.getCdOcorrenciaEntrega();
			if(cdOcorrenciaEntrega.equals(Short.valueOf("102"))) {
				Long idNotaFiscal = entregaNotaFiscalDTO.getIdNotaFiscal();
				Integer qtVolumesEntregues = entregaNotaFiscalDTO.getQtVolumesEntregues();
				registrarBaixaEntregaPorNotaFiscalService.validateQtVolumesEntregues(idNotaFiscal, qtVolumesEntregues);
			}
		}
	}
	
	private TypedFlatMap getTypedFlatMap(RegistrarBaixaEntregaPorNotaFiscalFilterDTO registrarBaixaEntregaFilterDTO) {
		TypedFlatMap toReturn = getTypedFlatMapWithPaginationInfo(registrarBaixaEntregaFilterDTO);
		
		if(registrarBaixaEntregaFilterDTO.getFilial() != null){
			toReturn.put("idFilial", registrarBaixaEntregaFilterDTO.getFilial().getIdFilial());
		}
		if(registrarBaixaEntregaFilterDTO.getControleCarga() != null){
			toReturn.put("idControleCarga", registrarBaixaEntregaFilterDTO.getControleCarga().getIdControleCarga());
		}
		if(registrarBaixaEntregaFilterDTO.getDoctoServico() != null){
			toReturn.put(ID_DOCTOSERVICO, registrarBaixaEntregaFilterDTO.getDoctoServico().getIdDoctoServico());
		}
		if(registrarBaixaEntregaFilterDTO.getManifestoEntrega() != null){
			toReturn.put("idManifestoEntrega", registrarBaixaEntregaFilterDTO.getManifestoEntrega().getIdManifestoEntrega());
		}
		if(registrarBaixaEntregaFilterDTO.getManifestoViagemNacional() != null){
			toReturn.put("idManifestoViagem", registrarBaixaEntregaFilterDTO.getManifestoViagemNacional().getIdManifestoViagem());
		}
		return toReturn;
	}
	
	private TypedFlatMap getTypedFlatMapWithPaginationInfo(RegistrarBaixaEntregaPorNotaFiscalFilterDTO filter) {
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("_currentPage", filter.getPagina() == null ? "1" : String.valueOf(filter.getPagina()));
		toReturn.put("_pageSize", filter.getQtRegistrosPagina() == null ? String.valueOf(ROW_LIMIT) : String.valueOf(filter.getQtRegistrosPagina()));
		return toReturn;
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
}
