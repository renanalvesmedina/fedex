package com.mercurio.lms.sim.swt.action;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.entrega.model.AgendamentoMonitCCT;
import com.mercurio.lms.expedicao.edi.model.service.NotaFiscalEDIService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalEletronicaService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.EventoMonitoramentoCCT;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.sim.model.service.AgendamentoMonitCCTService;
import com.mercurio.lms.sim.model.service.ClienteUsuarioCCTService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoMonitoramentoCCTService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.sim.reports.GerarDanfeService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class MonitoramentoNotasFiscaisCCTAction extends CrudAction {

	private ClienteService clienteService;
	private FilialService filialService;
	private EventoMonitoramentoCCTService eventoMonitoramentoCCTService;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
	private DomainValueService domainValueService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private NotaFiscalEDIService notaFiscalEDIService;
	private UsuarioLMSService usuarioLMSService;
	private UsuarioADSMService usuarioADSMService;
	private EventoDocumentoServicoService eventoDocumentoService;
	private AgendamentoMonitCCTService agendamentoMonitCCTService;
	private ClienteUsuarioCCTService clienteUsuarioCCTService;
	private ReportExecutionManager reportExecutionManager;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	
	final com.mercurio.adsm.framework.security.model.Usuario currentUser = SessionContext.getUser();
	final Locale currentUserLocale = (currentUser != null && currentUser.getLocale() != null) ? currentUser.getLocale() : new Locale("pt","BR");

	/**
	 * Busca dados da grid
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return this.getService().findPaginated(criteria , FindDefinition.createFindDefinition(criteria));
	}

	/**
	 * Busca a quantidade de dados da grid
	 */
	public Integer getRowCount(TypedFlatMap criteria) {		
		return this.getService().getRowCount(criteria);
	}	

	public void validateMonitoramentosSelecionados(TypedFlatMap parameters){
		getService().validateMonitoramentosSelecionados(parameters);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public Map findById(Long id) {
		String[] fetches = new String[] { "clienteRemetente",
				"clienteRemetente.pessoa", "clienteDestinatario",
				"clienteDestinatario.pessoa",
				"doctoServico",
				"doctoServico.filialByIdFilialOrigem",
				"doctoServico.filialByIdFilialDestino", "doctoServico.servico",
				"doctoServico.filialByIdFilialDestino.pessoa",
				"clienteDestinatario.filialByIdFilialAtendeOperacional",
		"clienteDestinatario.filialByIdFilialAtendeOperacional.pessoa" };
		MonitoramentoCCT monitoramentoCCT = this.getService().findById(id, fetches);

		Map<String, Object> retorno = new HashMap<String, Object>();

		if(monitoramentoCCT.getClienteRemetente() != null && monitoramentoCCT.getClienteRemetente().getPessoa() != null) {
			retorno.put("idClienteRemetente", monitoramentoCCT.getClienteRemetente().getIdCliente());
			retorno.put("nrIdentificacaoRemetente", monitoramentoCCT.getClienteRemetente().getPessoa().getNrIdentificacaoFormatado());
			retorno.put("nmPessoaRemetente", monitoramentoCCT.getClienteRemetente().getPessoa().getNmPessoa());
		}

		if(monitoramentoCCT.getClienteDestinatario() != null && monitoramentoCCT.getClienteDestinatario().getPessoa() != null) {
			retorno.put("idClienteDestinatario", monitoramentoCCT.getClienteDestinatario().getIdCliente());
			retorno.put("nrIdentificacaoDestinatario", monitoramentoCCT.getClienteDestinatario().getPessoa().getNrIdentificacaoFormatado());
			retorno.put("nmPessoaDestinatario", monitoramentoCCT.getClienteDestinatario().getPessoa().getNmPessoa()); 
		}

		retorno.put("idMonitoramentoCCT", id);
		retorno.put("nrNotaFiscal", monitoramentoCCT.getNrChave() == null ? "" : monitoramentoCCT.getNrChave().substring(25, 34));
		retorno.put("txtSituacao", monitoramentoCCT.getTpSituacaoNotaFiscalCCT().getDescriptionAsString()); 
		retorno.put("sgSituacao", monitoramentoCCT.getTpSituacaoNotaFiscalCCT().getValue()); 
		retorno.put("nrChaveNFe", monitoramentoCCT.getNrChave());
		retorno.put("nrChaveNFeDevolucao", monitoramentoCCT.getNrChaveDevolucao());
		retorno.put("dtSolicitacaoICMS", monitoramentoCCT.getDtSolicitacaoICMS());
		retorno.put("dtPagamentoICMS", monitoramentoCCT.getDtPagamentoICMS());

		List<EventoMonitoramentoCCT> eventoMonitoramento = this.eventoMonitoramentoCCTService.findByIdMonitoramentoCCT(id, false, new String[] {});
		if(!eventoMonitoramento.isEmpty()) {
			retorno.put("dhImportacao", JTFormatUtils.format(eventoMonitoramento.get(eventoMonitoramento.size()-1).getDhInclusao(), "dd/MM/yyyy"));
			retorno.put("txtComentario", eventoMonitoramento.get(0).getDsComentario());
		}else {
			retorno.put("dhImportacao", "");
		}

		List<Map> eMon = this.eventoMonitoramentoCCTService.findNmUsuarioEventoByIdMonitoramentoCCT(id);
		if (!eMon.isEmpty() && eMon.get(0).get("nmUsuario") != null) {	
			retorno.put("nmUsuario", eMon.get(0).get("nmUsuario"));				
		}else {
			retorno.put("nmUsuario", "");
		}
		
		List<EventoMonitoramentoCCT> eventosCofirmacaoAgendamento = this.eventoMonitoramentoCCTService.findMonitoramentoCCTByConfirmacaoAgendamento(id);
		if(!eventosCofirmacaoAgendamento.isEmpty() && eventosCofirmacaoAgendamento.get(0).getDhInclusao() != null){
			retorno.put("dtConfAgendamento", eventosCofirmacaoAgendamento.get(0).getDhInclusao());
		}else{
			retorno.put("dtConfAgendamento", "");
		}

		List<AgendamentoMonitCCT> agendMonitCCT = agendamentoMonitCCTService.findAgendamentoMonitCCTByMonitCCT(id);
		if(!agendMonitCCT.isEmpty() && agendMonitCCT.get(0) != null) {
			retorno.put("dtAgendamento", agendMonitCCT.get(0).getAgendamentoEntrega().getDtAgendamento());
		}

		if(monitoramentoCCT.getDoctoServico() != null) {
			retorno.put("idDoctoServico", monitoramentoCCT.getDoctoServico().getIdDoctoServico());
			
			if(monitoramentoCCT.getDoctoServico().getFilialByIdFilialOrigem() != null) {
				retorno.put("sgFilialOrigemDoctoServico", monitoramentoCCT.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
			}

			if(monitoramentoCCT.getDoctoServico().getFilialByIdFilialDestino() != null) {
				retorno.put("idFilialDestino", monitoramentoCCT.getDoctoServico().getFilialByIdFilialDestino().getIdFilial());
				retorno.put("sgFilialDestino", monitoramentoCCT.getDoctoServico().getFilialByIdFilialDestino().getSgFilial());
				retorno.put("nmFantasiaDestino", monitoramentoCCT.getDoctoServico().getFilialByIdFilialDestino().getPessoa().getNmFantasia());
			}

			if(monitoramentoCCT.getDoctoServico().getServico() != null && monitoramentoCCT.getDoctoServico().getServico().getTpModal() != null) {
				retorno.put("txtModal", monitoramentoCCT.getDoctoServico().getServico().getTpModal().getDescriptionAsString());
			}
			
			List<EventoDocumentoServico> eventosDoctoServico = 
				eventoDocumentoService.findEventoDoctoServico(monitoramentoCCT.getDoctoServico().getIdDoctoServico(), (short) 21);
			if(!eventosDoctoServico.isEmpty() && eventosDoctoServico.get(0) != null){
				retorno.put("dtEntrega", eventosDoctoServico.get(0).getDhEvento());
			}			

			retorno.put("tpDocumentoServico", monitoramentoCCT.getDoctoServico().getTpDocumentoServico().getDescriptionAsString());
			retorno.put("nrConhecimento", monitoramentoCCT.getDoctoServico().getNrDoctoServico());
			retorno.put("dtDPE", monitoramentoCCT.getDoctoServico().getDtPrevEntrega());
			if(monitoramentoCCT.getDoctoServico().getDhEmissao() != null){
				retorno.put("dtEmissaoDoctoServico", JTFormatUtils.format(monitoramentoCCT.getDoctoServico().getDhEmissao(), "dd/MM/yyyy HH:mm"));
			}else{
				retorno.put("dtEmissaoDoctoServico", "");
			}
			
			if(monitoramentoCCT.getDoctoServico().getIdDoctoServico() != null){
				List<Map> infoAwb = this.getService().findInformacoesAwbByDoctoServico(monitoramentoCCT.getDoctoServico().getIdDoctoServico());
				if(!(infoAwb.isEmpty())){
					retorno.put("serieAwb", infoAwb.get(0).get("sgEmpresa").toString() + " " +
											AwbUtils.getNrAwbFormated(infoAwb.get(0).get("dsSerie").toString(), 
														 Long.valueOf(infoAwb.get(0).get("nrAwb").toString()), 
													  Integer.valueOf(infoAwb.get(0).get("dvAwb").toString())));
				}else{
					retorno.put("serieAwb", "");
				}
			}	
			
		}else {
			retorno.put("idDoctoServico", "");
			if (monitoramentoCCT.getClienteDestinatario() != null) {
				retorno.put("idFilialDestino", monitoramentoCCT.getClienteDestinatario().getFilialByIdFilialAtendeOperacional().getIdFilial());
				retorno.put("sgFilialDestino", monitoramentoCCT.getClienteDestinatario().getFilialByIdFilialAtendeOperacional().getSgFilial());
				retorno.put("nmFantasiaDestino", monitoramentoCCT.getClienteDestinatario().getFilialByIdFilialAtendeOperacional().getPessoa().getNmFantasia());		
			}
		}
		
		if(monitoramentoCCT.getNrChave() != null){
			String pedido = this.getService().findInfoPedido(monitoramentoCCT.getNrChave());
			retorno.put("nrPedido", pedido);
			
			String dataEmissaoNfe = notaFiscalEletronicaService.findValorEspecificoFromNfeXMLEDI(monitoramentoCCT.getNrChave(), "ide", "dEmi");
			if("".equals(dataEmissaoNfe)){
				dataEmissaoNfe = notaFiscalEletronicaService.findValorEspecificoFromNfeXMLEDI(monitoramentoCCT.getNrChave(), "ide", "dhEmi");
			}
			
			if(!"".equals(dataEmissaoNfe) && dataEmissaoNfe != null){
				String dataEmissao = "";
				try {
					dataEmissao = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(dataEmissaoNfe));
					retorno.put("dtEmissaoNfe", dataEmissao);
				} catch (ParseException e) {
					retorno.put("dtEmissaoNfe", dataEmissao);
				}
			}else{
				retorno.put("dtEmissaoNfe", "");
			}
		}

		return retorno;
	}

	@SuppressWarnings("rawtypes")
	public List findItensNFe(TypedFlatMap criteria) {
		List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>();
		
		List<NotaFiscalEdiItem> itens = this.notaFiscalEletronicaService.findNfeItensByNrChave(criteria.getString("nrChaveNFe"));
		for (NotaFiscalEdiItem notaFiscalEdiItem : itens) {
			    Map<String, Object> map = new HashMap<String, Object>();
			    map.put("item", getValorString(notaFiscalEdiItem.getNumeroItem()));
			    map.put("descricao", getValorString(notaFiscalEdiItem.getDescricaoItem()));
			    map.put("codigo", getValorString(notaFiscalEdiItem.getCodItem()));
			    if(notaFiscalEdiItem.getQtdeItem() != null){
			    	map.put("quantidade", FormatUtils.formatBigDecimalWithPattern(notaFiscalEdiItem.getQtdeItem(), "#,##0.000") );
			    }
			    if(notaFiscalEdiItem.getVlUnidadeItem() != null){
			    	map.put("valorUnitario", FormatUtils.formatBigDecimalWithPattern(notaFiscalEdiItem.getVlUnidadeItem(), "#,##0.0000") );
			    }
			    if(notaFiscalEdiItem.getVlTotalItem() != null){
			    	map.put("valorTotal", FormatUtils.formatBigDecimalWithPattern(notaFiscalEdiItem.getVlTotalItem(), "#,##0.00") );
			    }
			    
			    retorno.add(map);
		}
		
		return retorno;
	}

	/**
	 * Necessário retornar campos como string devido a exibição em tela.
	 * 
	 * @param obj
	 * @return
	 */
	private String getValorString(Object obj){
		if(obj != null){
			return obj.toString();
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List findEventoMonitoramentoCCT(TypedFlatMap criteria) {
		return this.eventoMonitoramentoCCTService.findEventoMonitoramentoCCT(criteria);
	}

	/**
	 * Busca dados da grid da popup ocorrencias
	 */
	public List findManifestoEntregaByMonitoramentoCCT(TypedFlatMap criteria) {
		return this.getService().findManifestoEntregaByMonitoramentoCCT(criteria);
	}
	
	public List findClienteCCTByUsuario(Map criteria){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Usuario usuario = SessionUtils.getUsuarioLogado();
		criteria = clienteUsuarioCCTService.findClienteCCTByUsuario(usuario.getIdUsuario());
			if(criteria.containsKey("nrIdentificacao")){
				result = findLookupCliente(criteria) ;
			}
		return result;
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findLookupCliente(Map criteria) {
		Map<String, Object> pessoa = new HashMap<String, Object>();
		pessoa.put("nrIdentificacao", criteria.remove("nrIdentificacao"));
		pessoa.put("nmFantasia", criteria.remove("nmFantasia"));
		criteria.put("pessoa", pessoa);
	    List<Cliente> clientes = clienteService.findLookup(criteria);
	    List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		if (clientes != null && !clientes.isEmpty()) {
			for(Cliente cliente : clientes) {
				Map<String, Object> mapCliente = new HashMap<String, Object>();
				mapCliente.put("idCliente",cliente.getIdCliente());
				mapCliente.put("nmPessoa", cliente.getPessoa().getNmPessoa());
				// já vem formatado do clienteService.
				mapCliente.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				result.add(mapCliente);
			}
			return result;
		}
		return result;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findLookupFilial(Map criteria) {
		List list = filialService.findLookupFilial(criteria);
		List retorno = new ArrayList();
		if (list.size()>0){
			Map map = (Map)list.get(0);
			map.put("idFilial", map.get("idFilial"));
			map.put("nmFantasia", (String)((Map)map.get("pessoa")).get("nmFantasia"));
			map.put("sgFilial", (String)map.get("sgFilial"));			
			retorno.add(map);
		}		
		return retorno;
	}

	public Map validateChaveNFeDevolucao(TypedFlatMap data){

		Map retorno = new HashMap();
		
		NotaFiscalEdi notaFiscalEdi  = notaFiscalEletronicaService.findNfeByNrChave(data.get("nrChaveNFeDevolucao").toString());

		if (notaFiscalEdi == null) {
			throw new BusinessException("LMS-10081");
		}

		boolean isRemetenteValido = validateClienteByIdAndCNPJ(data.getLong("idClienteRemetente"), notaFiscalEdi.getCnpjDest());
		retorno.put("isCLienteRemetenteValido", isRemetenteValido);		

		boolean isDestinatarioValido = validateClienteByIdAndCNPJ(data.getLong("idClienteDestinatario"), notaFiscalEdi.getCnpjReme());
		retorno.put("isCLienteDestinatarioValido", isDestinatarioValido);	

		return retorno;
	}

	private boolean validateClienteByIdAndCNPJ(Long idCliente, Long cnpj) {
		String cnpjString = notaFiscalEDIService.getValidatedNrIdentificacao(cnpj.toString());
		Cliente cliente = clienteService.findById(idCliente);

		if(cliente.getPessoa().getNrIdentificacao() != null && cliente.getPessoa().getNrIdentificacao().equals(cnpjString)){
			return true;
		} else {
			return false;
		}
		
	}
	
	public String executeDownloadDanfe(TypedFlatMap criteria) throws Exception{
		String xml = notaFiscalEletronicaService.findNfeXMLFromEDIToString(criteria.getString("nrChaveNFe"));
		GerarDanfeService gerarDanfeService = new GerarDanfeService(currentUserLocale, null);
		File danfe = gerarDanfeService.execute(xml, currentUserLocale);
		return reportExecutionManager.generateReportLocator(danfe);
	}
	
	public ResultSetPage findPaginatedBloqueiosLiberacoes(TypedFlatMap criteria){
		if(!"".equals(criteria.get("idDoctoServico").toString())){
			return ocorrenciaDoctoServicoService.findPaginatedBloqueiosLiberacoesByIdDoctoServ(criteria.getLong("idDoctoServico"));
		}else{
			return ocorrenciaDoctoServicoService.findPaginatedBloqueiosLiberacoesByIdDoctoServ(LongUtils.ZERO);
		}
	}

	public List getTpSituacaoDomainValue(Map criteria){		
		return getDomainValueService().findByDomainNameAndValues("DM_SITUACAO_NF_CCT", (List)criteria.get("listSituacoes"));
	}

	public void storeTabDevolucao(Map parameters){
		parameters.put("idUsuario", SessionUtils.getUsuarioLogado().getIdUsuario());
		monitoramentoNotasFiscaisCCTService.storeTabDevolucao(parameters);
	}


	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setService(MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.defaultService = monitoramentoNotasFiscaisCCTService;
	}

	public MonitoramentoNotasFiscaisCCTService getService() {
		return (MonitoramentoNotasFiscaisCCTService) this.defaultService;
	}

	public EventoMonitoramentoCCTService getEventoMonitoramentoCCTService() {
		return eventoMonitoramentoCCTService;
	}

	public void setEventoMonitoramentoCCTService(
			EventoMonitoramentoCCTService eventoMonitoramentoCCTService) {
		this.eventoMonitoramentoCCTService = eventoMonitoramentoCCTService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public MonitoramentoNotasFiscaisCCTService getMonitoramentoNotasFiscaisCCTService() {
		return monitoramentoNotasFiscaisCCTService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(
			MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}

	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public NotaFiscalEletronicaService getNotaFiscalEletronicaService() {
		return notaFiscalEletronicaService;
	}

	public UsuarioADSMService getUsuarioADSMService() {
		return usuarioADSMService;
	}

	public void setUsuarioADSMService(UsuarioADSMService usuarioADSMService) {
		this.usuarioADSMService = usuarioADSMService;
	}

	public void setEventoDocumentoService(
			EventoDocumentoServicoService eventoDocumentoService) {
		this.eventoDocumentoService = eventoDocumentoService;
	}

	public void setAgendamentoMonitCCTService(
			AgendamentoMonitCCTService agendamentoMonitCCTService) {
		this.agendamentoMonitCCTService = agendamentoMonitCCTService;
	}

	public void setNotaFiscalEDIService(NotaFiscalEDIService notaFiscalEDIService) {
		this.notaFiscalEDIService = notaFiscalEDIService;
	}
	
	public ClienteUsuarioCCTService getClienteUsuarioCCTService() {
		return clienteUsuarioCCTService;
	}
	public void setClienteUsuarioCCTService(
			ClienteUsuarioCCTService clienteUsuarioCCTService) {
		this.clienteUsuarioCCTService = clienteUsuarioCCTService;
	}

	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public OcorrenciaDoctoServicoService getOcorrenciaDoctoServicoService() {
		return ocorrenciaDoctoServicoService;
	}

	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
}