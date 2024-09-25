package com.mercurio.lms.rest.coleta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaDocumentoService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.entrega.model.service.RegistrarBaixaEntregasOnTimeService;
import com.mercurio.lms.entrega.model.service.RegistrarBaixaEntregasService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.rest.coleta.dto.ColetaPendenteDTO;
import com.mercurio.lms.rest.coleta.dto.ColetasEntregasAereasFilterDTO;
import com.mercurio.lms.rest.coleta.dto.EntregaRealizarDTO;
import com.mercurio.lms.rest.coleta.dto.OcorrenciaColetaDTO;
import com.mercurio.lms.rest.coleta.dto.OcorrenciaEntregaDTO;
import com.mercurio.lms.rest.coleta.dto.RetornarColetaDTO;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
 
@Path("/coleta/coletasEntregasAereas") 
public class ColetasEntregasAereasRest extends BaseRest {
	
	@InjectInJersey ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	@InjectInJersey PedidoColetaService pedidoColetaService;
	@InjectInJersey ConhecimentoService conhecimentoService;
	@InjectInJersey EventoService eventoService;
	@InjectInJersey RegistrarBaixaEntregasOnTimeService registrarBaixaEntrefaOnTimeService;
	@InjectInJersey RegistrarBaixaEntregasService registrarBaixaEntregasService;
	@InjectInJersey OcorrenciaColetaService ocorrenciaColetaService; 
	@InjectInJersey OcorrenciaEntregaService ocorrenciaEntregaService; 
	@InjectInJersey DoctoServicoService doctoServicoService;
	
	
	@POST
	@Path("findEntregasRealizar")
	public Response findEntregasRealizar(ColetasEntregasAereasFilterDTO filter) {
		TypedFlatMap criteria = populateFilters(filter);
		
		List<Map<String, Object>> list = manifestoEntregaDocumentoService.findEntregasRealizar(criteria);
		
		List<EntregaRealizarDTO> listEntregaARealizarDTO = new ArrayList<EntregaRealizarDTO>();
		for (Map<String, Object> map : list) {
			TypedFlatMap tfm = new TypedFlatMap(map);
			String preAwbAwb = AwbUtils.getPreAwbOrAwb(tfm.getDomainValue("tpStatusAwb"), tfm.getString("preAwbAwb"), tfm.getString("sgEmpresa"));
			StringBuilder nrDoctoServico = new StringBuilder();
			nrDoctoServico.append(tfm.getString("sgFilialOrigemDocto")).append(" ").append(tfm.getLong("nrDoctoServico"));
			EntregaRealizarDTO entregaRealizarDTO = new EntregaRealizarDTO();
			
			entregaRealizarDTO.setId(tfm.getLong("idDoctoServico"));
			entregaRealizarDTO.setNrDoctoServico(nrDoctoServico.toString());
			entregaRealizarDTO.setPreAwbAwb(preAwbAwb);
			entregaRealizarDTO.setOtd(tfm.getString("dtPrevEntrega"));
			entregaRealizarDTO.setNmCliente(tfm.getString("nmCliente"));
			entregaRealizarDTO.setEndereco(tfm.getString("endCliente"));
			entregaRealizarDTO.setVolume(tfm.getInteger("volumes"));
			entregaRealizarDTO.setPeso(tfm.getBigDecimal("peso"));
			entregaRealizarDTO.setValor(tfm.getBigDecimal("valor"));
			entregaRealizarDTO.setVeiculo(tfm.getString("veiculo"));
			entregaRealizarDTO.setTpStatusManifesto(tfm.getDomainValue("tpManifestoEntrega"));
			entregaRealizarDTO.setIsEntregaAeroporto(tfm.getLong("idFilialOrigemAwb").equals(SessionUtils.getFilialSessao().getIdFilial()));
			entregaRealizarDTO.setEntregaDireta(ConstantesExpedicao.TP_MANIFESTO_ENTREGA_DIRETA.equals(tfm.getDomainValue("tpManifestoEntrega").getValue()));
			
			listEntregaARealizarDTO.add(entregaRealizarDTO);
		}
		
		return getReturnFind(listEntregaARealizarDTO, listEntregaARealizarDTO.size());
	}
	
	@POST
	@Path("executarEntregasRealizar")
	public Response executarEntregasRealizar(List<EntregaRealizarDTO> entregasRealizarDTOs) {
		for (EntregaRealizarDTO entregaRealizarDTO : entregasRealizarDTOs) {
			Long idDoctoServico = entregaRealizarDTO.getId();
			
			Short cdOcorrenciaEntrega = ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA;
			if(entregaRealizarDTO.getIsEntregaAeroporto()){
				cdOcorrenciaEntrega = ConstantesEventosDocumentoServico.CD_OCORRENCIA_ENTREGA_REALIZADA_AEROPORTO;
			}
			
			Boolean isValidExistenciaPceDestinatario = Boolean.TRUE;
			Boolean isValidExistenciaPceRemetente = Boolean.TRUE;
			String nmRecebedor = entregaRealizarDTO.getNmRecebedor();
			String obManifesto = "";
			
			int cdLocalizacao = doctoServicoService.findCodigoLocalizacaoMercadoria(idDoctoServico);
			
			if(ConstantesSim.CD_MERCADORIA_RETORNADA_AGUARDANDO_DESCARGA.equals(Short.valueOf((short)cdLocalizacao))){
				List<ManifestoEntregaDocumento> manifestos = manifestoEntregaDocumentoService.findByIdDoctoServico(idDoctoServico);
				ManifestoEntregaDocumento med = manifestos.get(0);
				ManifestoEntrega manifestoEntrega = med.getManifestoEntrega();
				
				DateTime dhChegada = manifestoEntregaDocumentoService.findDhChegadaPortaria(manifestoEntrega.getIdManifestoEntrega());
				DateTime dhOcorrencia = dhChegada.minusMinutes(1);
				
				registrarBaixaEntregasService.executeConfirmation(manifestoEntrega.getIdManifestoEntrega(), idDoctoServico, cdOcorrenciaEntrega, nmRecebedor, obManifesto, isValidExistenciaPceRemetente, isValidExistenciaPceDestinatario, dhOcorrencia, new DomainValue(), Boolean.TRUE, org.apache.commons.lang3.StringUtils.EMPTY, null);
			}else{
				registrarBaixaEntrefaOnTimeService.executeConfirmation(idDoctoServico, cdOcorrenciaEntrega, nmRecebedor, obManifesto, isValidExistenciaPceRemetente, isValidExistenciaPceDestinatario, new DomainValue(), null);
			}
		}
		return Response.ok().build();
	}
	
	@POST
	@Path("executarColetasPendentes")
	public Response executarColetasPendentes(List<Map<String, Long>> ids) {
		pedidoColetaService.generateEventosColetaAereoByPedidoColetaAndAwb(ids);		
		return Response.ok().build();
	}
	
	@POST
	@Path("retornarColetasAeroporto")
	public Response retornarColetasAeroporto(RetornarColetaDTO retornarColetaDTO) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idsPedidoColeta.ids", retornarColetaDTO.getIdsPedido());
		criteria.put("idsPedidoColetaAndAwb", retornarColetaDTO.getIdsPedidoAndAwb());
		criteria.put("dtHoraOcorrencia", JTDateTimeUtils.formatDateTimeToString(retornarColetaDTO.getDtHrOcorrencia(), "yyyy-MM-dd HH:mm:ss"));
		criteria.put("ocorrenciaColeta.blIneficienciaFrota", retornarColetaDTO.getBlIneficienciaFrota().getValue());
		criteria.put("ocorrenciaColeta.idOcorrenciaColeta", retornarColetaDTO.getIdOcorrenciaColeta());
		criteria.put("cdEventoColetaAeroportoNaoExecutada", ConstantesEventosDocumentoServico.CD_EVENTO_COLETA_AEROPORTO_NAO_EXECUTADA);
		pedidoColetaService.generateRetornarColetaAerea(criteria);
		
		return Response.ok().build();
	}
	
	@POST
	@Path("retornarEntregasAeroporto")
	public Response retornarEntregasAeroporto(List<EntregaRealizarDTO> list) {
		
		for (EntregaRealizarDTO entregaRealizarDTO : list) {
			Long idDoctoServico = entregaRealizarDTO.getId();
			Short cdOcorrenciaEntrega = entregaRealizarDTO.getCdOcorrenciaEntrega();
			DateTime dhOcorrencia = entregaRealizarDTO.getDhOcorrencia();
			
			registrarBaixaEntrefaOnTimeService.executeConfirmation(idDoctoServico, cdOcorrenciaEntrega, "", "", Boolean.TRUE, Boolean.TRUE, dhOcorrencia, new DomainValue(), null);
		}
		
		return Response.ok().build();
	}
	
	@POST
	@Path("findColetasPendentes")
	public Response findColetasPendentes(ColetasEntregasAereasFilterDTO filter) {
		TypedFlatMap criteria = populateFilters(filter);
		
		List<Map<String, Object>> list  = pedidoColetaService.findPedicoColetasPendentes(criteria);
		
		List<ColetaPendenteDTO> listColetaPendenteDTO = new ArrayList<ColetaPendenteDTO>();
		for (Map<String, Object> map : list) {
			TypedFlatMap tfm = new TypedFlatMap(map);
			String preAwbAwb = AwbUtils.getPreAwbOrAwb(tfm.getDomainValue("tpStatusAwb"), tfm.getString("preAwbAwb"), tfm.getString("sgEmpresa"));
			StringBuilder coleta = new StringBuilder();
			coleta.append(tfm.getString("coleta.sgFilial")).append(" ").append(tfm.getString("coleta.nrColeta"));
			
			ColetaPendenteDTO coletaPendenteDTO = new ColetaPendenteDTO();
			coletaPendenteDTO.setId(tfm.getLong("idPedidoColeta"));
			coletaPendenteDTO.setIdAwb(tfm.getLong("idAwb"));
			coletaPendenteDTO.setColeta(coleta.toString());
			coletaPendenteDTO.setPreAwbAwb(preAwbAwb);
			coletaPendenteDTO.setEntregaDireta(BooleanUtils.isTrue(tfm.getBoolean("blEntregaDireta")));
			coletaPendenteDTO.setOtd(tfm.getString("dtPrevEntrega"));
			coletaPendenteDTO.setCliente(tfm.getString("cliente"));
			coletaPendenteDTO.setEndereco(tfm.getString("endereco"));
			coletaPendenteDTO.setVolume(tfm.getLong("volume"));
			coletaPendenteDTO.setPeso(tfm.getBigDecimal("peso"));
			coletaPendenteDTO.setValor(tfm.getBigDecimal("valor"));
			coletaPendenteDTO.setHorarioColeta(tfm.getDateTime("dhColetaDisponivel"));
			coletaPendenteDTO.setVeiculo(tfm.getString("veiculo"));
			coletaPendenteDTO.setEquipe(tfm.getString("equipe"));
			coletaPendenteDTO.setNrDocumentos(tfm.getInteger("nrDocumentos"));

			listColetaPendenteDTO.add(coletaPendenteDTO);
		}
		
		return getReturnFind(listColetaPendenteDTO, listColetaPendenteDTO.size());
	}
	
	@POST
	@Path("findEntregasRealizadas")
	public Response findEntregasRealizadas(ColetasEntregasAereasFilterDTO filter) {
		TypedFlatMap criteria = populateFilters(filter);

		List<EntregaRealizarDTO> listEntregaARealizadasDTO = new ArrayList<EntregaRealizarDTO>();
		List<Map<String, Object>> listEntrega = null;
		if (criteria.getBoolean("executeAmbos") || criteria.getBoolean("executeEntrega")) {
			listEntrega = manifestoEntregaDocumentoService.findEntregasRealizadas(criteria);
			listEntregaARealizadasDTO = this.convertColetasEntregasRealizadasList(listEntrega);
		}
		
		List<Map<String, Object>> listColeta = null;
		if (criteria.getBoolean("executeAmbos") || criteria.getBoolean("executeColeta")) {
			listColeta = pedidoColetaService.findPedicoColetasRealizadas(criteria);
			listEntregaARealizadasDTO.addAll(this.convertColetasEntregasRealizadasList(listColeta));
		}
		
		Collections.sort(listEntregaARealizadasDTO, new Comparator<EntregaRealizarDTO>() {
			@Override
			public int compare(EntregaRealizarDTO er1, EntregaRealizarDTO er2) {
				int compDoc = er1.getNrDoctoServico().compareTo(er2.getNrDoctoServico());
				return compDoc == 0 ? er1.getDtHrEvento().compareTo(er2.getDtHrEvento()) : compDoc;
			}
		});
		
		return getReturnFind(listEntregaARealizadasDTO, listEntregaARealizadasDTO.size());
	}
	
	@POST
	@Path("findOcorrenciaEntregaRetorno")	
	public Response findOcorrenciaEntregaRetorno() {
		List<OcorrenciaEntrega> listOcorrenciaEntrega = ocorrenciaEntregaService.findAllOcorrenciaEntregaAtivo();
		
		List<OcorrenciaEntregaDTO> listDto = new ArrayList<OcorrenciaEntregaDTO>();
		for (OcorrenciaEntrega oe : listOcorrenciaEntrega) {
			listDto.add(new OcorrenciaEntregaDTO(oe.getIdOcorrenciaEntrega(), oe.getCdOcorrenciaEntrega(), oe.getDsOcorrenciaEntrega().getValue()));
		}
		
		return Response.ok(listDto).build();
	}

	
	@POST
	@Path("findOcorrenciaColetaRetorno")
	public Response findOcorrenciaColetaRetorno(Map<String, Object> criteria) {
		List<OcorrenciaColeta> listOcorrenciaColeta = ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColetaRetornoColeta();
		
		List<OcorrenciaColetaDTO> listOcorrenciaColetaDTO = new ArrayList<OcorrenciaColetaDTO>();
		for (OcorrenciaColeta oc : listOcorrenciaColeta) {
			listOcorrenciaColetaDTO.add(new OcorrenciaColetaDTO(oc.getIdOcorrenciaColeta(), oc.getCodigo(), oc.getDsDescricaoCompleta().getValue()));
		}
		
		return Response.ok(listOcorrenciaColetaDTO).build();
	}
	
	private List<EntregaRealizarDTO> convertColetasEntregasRealizadasList(List<Map<String, Object>> list) {
		List<EntregaRealizarDTO> listEntregaARealizadasDTO = new ArrayList<EntregaRealizarDTO>();
		
		for (Map<String, Object> map : list) {
			TypedFlatMap tfm = new TypedFlatMap(map);
			String preAwbAwb = AwbUtils.getPreAwbOrAwb(tfm.getDomainValue("tpStatusAwb"), tfm.getString("preAwbAwb"), tfm.getString("sgEmpresa"));
			StringBuilder nrDoctoServico = new StringBuilder();
			nrDoctoServico.append(tfm.getString("sgFilialOrigemDocto")).append(" ").append(tfm.getLong("nrDoctoServico"));
			EntregaRealizarDTO entregaRealizadasDTO = new EntregaRealizarDTO();
			
			entregaRealizadasDTO.setNrDoctoServico(nrDoctoServico.toString());
			entregaRealizadasDTO.setPreAwbAwb(preAwbAwb);
			entregaRealizadasDTO.setOtd(tfm.getString("dtPrevEntrega"));
			entregaRealizadasDTO.setNmCliente(tfm.getString("nmCliente"));
			entregaRealizadasDTO.setEndereco(tfm.getString("endCliente"));
			entregaRealizadasDTO.setVolume(tfm.getInteger("volumes"));
			entregaRealizadasDTO.setPeso(tfm.getBigDecimal("peso"));
			entregaRealizadasDTO.setValor(tfm.getBigDecimal("valor"));
			entregaRealizadasDTO.setVeiculo(tfm.getString("veiculo"));
			entregaRealizadasDTO.setNrManifesto(tfm.getString("nrManifesto"));
			entregaRealizadasDTO.setSituacao(tfm.getString("dsEvento"));
			entregaRealizadasDTO.setDtHrEvento(tfm.getString("dhEvento"));
			
			listEntregaARealizadasDTO.add(entregaRealizadasDTO);
		}
		return listEntregaARealizadasDTO;
	}

	private TypedFlatMap populateFilters(ColetasEntregasAereasFilterDTO filter) {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("pageSize", filter.getQtRegistrosPagina());
		parameters.put("pageNumber", filter.getPagina());
		parameters.put("executeEntrega", Boolean.FALSE);
		parameters.put("executeColeta", Boolean.FALSE);
		parameters.put("executeAmbos", Boolean.FALSE);
		parameters.put("idFilialSessao", SessionUtils.getFilialSessao().getIdFilial());
		
		if (filter.getControleCarga() != null) {
			parameters.put("idControleCarga", filter.getControleCarga().getIdControleCarga());
			parameters.put("executeAmbos", Boolean.TRUE);
		}
		
		if (filter.getMeioTransporte() != null) {
			parameters.put("idMeioTransporte", filter.getMeioTransporte().getIdMeioTransporte());
			parameters.put("executeAmbos", Boolean.TRUE);
		}
		
		if (filter.getDoctoServico() != null) {
			parameters.put("idConhecimento", filter.getDoctoServico().getIdDoctoServico());
			parameters.put("executeAmbos", Boolean.TRUE);
		}
		
		if (filter.getManifestoColeta() != null && filter.getManifestoEntrega() != null) {
			parameters.put("idManifestoColeta", filter.getManifestoColeta().getIdManifestoColeta());
			parameters.put("idManifestoEntrega", filter.getManifestoEntrega().getIdManifestoEntrega());
			parameters.put("executeAmbos", Boolean.TRUE);
		} else {
			if (filter.getManifestoColeta() != null) {
				parameters.put("idManifestoColeta", filter.getManifestoColeta().getIdManifestoColeta());
				parameters.put("executeColeta", Boolean.TRUE);
				parameters.put("executeAmbos", Boolean.FALSE);
			}
		
			if (filter.getManifestoEntrega() != null) {
				parameters.put("idManifestoEntrega", filter.getManifestoEntrega().getIdManifestoEntrega());
				parameters.put("executeEntrega", Boolean.TRUE);
				parameters.put("executeAmbos", Boolean.FALSE);
			}
		}
		
		if (filter.getAwb() != null) {
			parameters.put("idAwb", filter.getAwb().getIdAwb());
			parameters.put("executeAmbos", Boolean.TRUE);		
		}
		
		return parameters;
	}
} 
