package com.mercurio.lms.facade.radar.impl;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.facade.radar.TrackingFacade;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.tracking.Depot;
import com.mercurio.lms.vendas.model.service.ClienteService;


/**
 * @author Adércio Reinan
 * @spring.bean id="lms.radar.trackingFacade"
 */
@ServiceSecurity
public class TrackingFacadeImpl implements TrackingFacade {
	
	private FilialService filialService;
	private EventoService eventoService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ClienteService clienteService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private RecursoMensagemService recursoMensagemService;
	
	@Override
	@SuppressWarnings("all")
	@MethodSecurity(processGroup = "radar.tracking", processName = "findXml", authenticationRequired=false)
	public TypedFlatMap findXml(TypedFlatMap criteria) {
		Long idMonitoramentoDoc = criteria.getLong("idMonitoramentoDoc");
		MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findById(idMonitoramentoDoc);
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("nrProtocolo", monitoramentoDocEletronico.getNrProtocolo());
		result.put("dsDadosDocumento", monitoramentoDocEletronico.getDsDadosDocumento());
		return result;
	}
	
	@Override
	@SuppressWarnings("all")
	@MethodSecurity(processGroup = "radar.tracking", processName = "findLocalizacaoFilialAtual", authenticationRequired=false)
	public TypedFlatMap findLocalizacaoFilialAtual(TypedFlatMap criteria) {
		Object[] result = filialService.findLocalizacaoFilialAtual(criteria);
		return parseResultFilialAtualToMap(result);
	}

	@Override
	@SuppressWarnings("all")
	@MethodSecurity(processGroup = "radar.tracking", processName = "findLocalizacaoFilialOrigemAndDestino", authenticationRequired=false)
	public TypedFlatMap findLocalizacaoFilialOrigemAndDestino(TypedFlatMap criteria) {
		Object[] result = filialService.findLocalizacaoFilialOrigemAndDestino(criteria);
		return parseResultFilialOrigemDestinoToMap(result);
	}
	
	@Override
	@SuppressWarnings("all")
	@MethodSecurity(processGroup = "radar.tracking", processName = "findEventoByIdDoctoServico", authenticationRequired=false)
	public TypedFlatMap findEventoByIdDoctoServico(TypedFlatMap criteria) {
		Object[] result = eventoService.findEventoByIdDoctoServico(criteria);
		TypedFlatMap map = new TypedFlatMap();
		if(result != null && result.length > 0){
			int i = 0;
			map.put("cdEvento", result[i++]);
			map.put("idManifestoDocumento", result[i++]);
			map.put("dhEvento", result[i++]);
			map.put("dhEmissaoDoc", result[i++]);
		}
		return map;
	}

	@Override
	@SuppressWarnings("all")
	@MethodSecurity(processGroup = "radar.tracking", processName = "findEventoNaoConformidade", authenticationRequired=false)
	public TypedFlatMap findEventoNaoConformidade(TypedFlatMap criteria) {
		Long idEvento = eventoService.findEventoNaoConformidade(criteria);
		TypedFlatMap map = new TypedFlatMap();
		if(idEvento != null){
			map.put("idEvento", idEvento);
		}
		return map;
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.tracking", processName = "findDepotByIdFilial", authenticationRequired=false)
	public TypedFlatMap findDepotByIdFilial(TypedFlatMap criteria) {
		Long idFilial = criteria.getLong("idFilial");
		Depot depot = filialService.findDepotByIdFilial(idFilial);
		return parseDepotToMap(depot);
	}
	
	@Override
	@MethodSecurity(processGroup = "radar.tracking", processName = "findInfoEventoAtualDoctoByIdDoctoServico", authenticationRequired=false)
	public TypedFlatMap findInfoEventoAtualDoctoByIdDoctoServico(TypedFlatMap criteria) {
		Long idDoctoServico = criteria.getLong("idDoctoServico");
		Object[] result = eventoDocumentoServicoService.findInfoEventoAtualDoctoByIdDoctoServico(idDoctoServico);
		return parseInfoEventoAtualDoctoToMap(result);
	}

	private TypedFlatMap parseInfoEventoAtualDoctoToMap(Object[] result) {
		TypedFlatMap map = new TypedFlatMap();
		int index = 0;
		if(result != null && result.length > 0){
			map.put("id", result[index++]);
			map.put("ctrc", result[index++]);
			map.put("evento", result[index++]);
			map.put("previsao", result[index++]);
			map.put("detalhe", result[index++]);
			map.put("idMonitoramentoDoc", result[index++]);
			map.put("dataColeta", result[index++]);
			map.put("dataEmissao", result[index++]);
			map.put("destino", result[index++]);
			map.put("finalizacao", result[index++]);
		}
		return map;
	}

	private TypedFlatMap parseDepotToMap(Depot depot) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idFilial", depot.getCode());
		tfm.put("nmFantasia", depot.getDescription());
		tfm.put("sgFilial", depot.getAcronym());
		tfm.put("dsTpLogradouro", depot.getStreet());
		tfm.put("dsEndereco", depot.getAddressDescription());
		tfm.put("nrEndereco", depot.getAddressNumber());
		tfm.put("dsComplemento", depot.getAddressComplement());
		tfm.put("dsBairro", depot.getDistrict());
		tfm.put("nrCep", depot.getZipCode());
		tfm.put("email", depot.getEmail());
		tfm.put("nrDDDTelefone", depot.getPhoneAreaCode());
		tfm.put("nrTelefone", depot.getPhoneNumber());
		tfm.put("nrDDDFax", depot.getFaxAreaCode());
		tfm.put("nrFax", depot.getFaxNumber());
		tfm.put("nrLatitude", depot.getNrLatitude());
		tfm.put("nrLongitude", depot.getNrLongitude());
		tfm.put("nrQualidade", depot.getNrQuality());
		tfm.put("idUf", depot.getState().getId());
		tfm.put("nmUf", depot.getState().getName());
		tfm.put("sgUf", depot.getState().getAcronym());
		tfm.put("idCidade", depot.getCity().getId());
		tfm.put("nmCidade", depot.getCity().getName());
		return tfm;
	}

	private TypedFlatMap parseResultFilialAtualToMap(Object[] result) {
		TypedFlatMap map = new TypedFlatMap();
		int index = 0;
		if(result != null && result.length > 0){
			map.put("idDoctoServico", result[index++]);
			map.put("idFilial", result[index++]);
			map.put("sgFilial", result[index++]);
			map.put("nmFantasia", result[index++]);
			map.put("localizacaoMercadoria", result[index++]);
			map.put("latitudeAtual", result[index++]);
			map.put("longitudeAtual", result[index++]);
			map.put("latitudeDestino", result[index++]);
			map.put("longitudeDestino", result[index++]);
			map.put("tpModal", result[index++]);
			map.put("idMotivoAberturaNc", result[index++]);
		}
		return map;
	}
	
	private TypedFlatMap parseResultFilialOrigemDestinoToMap(Object[] result) {
		TypedFlatMap map = new TypedFlatMap();
		int index = 0;
		if(result != null && result.length > 0){
			map.put("idDoctoServico", result[index++]);
			map.put("idFilialOrigem", result[index++]);
			map.put("sgFilialOrigem", result[index++]);
			map.put("nmFilialOrigem", result[index++]);
			map.put("latitudeOrigem", result[index++]);
			map.put("longitudeOrigem", result[index++]);
			map.put("idFilialDestino", result[index++]);
			map.put("sgFilialDestino", result[index++]);
			map.put("nmFilialDestino", result[index++]);
			map.put("latitudeDestino", result[index++]);
			map.put("longitudeDestino", result[index++]);
			map.put("tpModal", result[index++]);
		}
		return map;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	

	@Override
	@MethodSecurity(processGroup = "radar.tracking", processName = "findAllCustomers", authenticationRequired=false)
	public List<TypedFlatMap> findAllCustomers(TypedFlatMap criteria) {
		Long loggedUserId = criteria.getLong("logged_user_id");
		return parseResultfindAllCustomersToMap(clienteService.findCnpjAutorizadoByUser(loggedUserId));
	}
	
	private List<TypedFlatMap> parseResultfindAllCustomersToMap(List<Object[]> listFromResult) {
		List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>(listFromResult.size());
		TypedFlatMap map = null;
		for (final Object[] objResult : listFromResult) {
			map = new TypedFlatMap();
			map.put("id", objResult[0]);
			map.put("cnpj", objResult[1]);
			listRetorno.add(map);
		}

		return listRetorno;
	}
	
	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}


	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}
	
	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}
	
	public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}
	
	@Override
	@MethodSecurity
		(
			processGroup = "radar.tracking", 
			processName = "validatePessoaByNrIdentificacao", 
			authenticationRequired=false
		)
	public TypedFlatMap validatePessoaByNrIdentificacao(TypedFlatMap criteria) {
		TypedFlatMap map = new TypedFlatMap();
		String nrIdentificacao = criteria.getString("nrIdentificacao");
		String mensagem = "";
		
		boolean validatePessoa = clienteService.validatePessoaByNrIdentificacao(nrIdentificacao);
		
		if(!validatePessoa) {
			mensagem = recursoMensagemService.findByChave("RADAR-00030");
		}
		
		map.put("existe", validatePessoa);
		map.put("mensagem", mensagem);
		
		return map;
	}

}