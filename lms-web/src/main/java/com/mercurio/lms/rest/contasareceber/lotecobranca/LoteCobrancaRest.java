package com.mercurio.lms.rest.contasareceber.lotecobranca;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemLoteCobrancaTerceira;
import com.mercurio.lms.contasreceber.model.LoteCobrancaTerceira;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagem;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemDetalhe;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemEvento;
import com.mercurio.lms.contasreceber.model.MonitoramentoMensagemFatura;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.contasreceber.model.service.ItemLoteCobrancaTerceiraService;
import com.mercurio.lms.contasreceber.model.service.LoteCobrancaTerceiraService;
import com.mercurio.lms.contasreceber.model.service.LoteSerasaService;
import com.mercurio.lms.contasreceber.model.service.MonitoramentoMensagemFaturaService;
import com.mercurio.lms.contasreceber.model.service.MonitoramentoMensagemService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contasareceber.excecaonegativacao.dto.FaturaDTO;
import com.mercurio.lms.rest.contasareceber.lotecobranca.dto.HistoricoMensagemDTO;
import com.mercurio.lms.rest.contasareceber.lotecobranca.dto.HistoricoMensagemDetalheDTO;
import com.mercurio.lms.rest.contasareceber.lotecobranca.dto.ItemLoteCobrancaDTO;
import com.mercurio.lms.rest.contasareceber.lotecobranca.dto.LoteCobrancaDTO;
import com.mercurio.lms.rest.contasareceber.lotecobranca.dto.LoteCobrancaFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.util.ArquivoUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

@Path("/contasareceber/loteCobranca") 
public class LoteCobrancaRest extends BaseCrudRest<LoteCobrancaDTO, LoteCobrancaDTO, LoteCobrancaFilterDTO> {

	private Logger log = LogManager.getLogger(this.getClass());

	@InjectInJersey private LoteCobrancaTerceiraService loteCobrancaTerceiraService;
	@InjectInJersey private ItemLoteCobrancaTerceiraService itemLoteCobrancaTerceiraService;
	@InjectInJersey private ParametroGeralService parametroGeralService;
	@InjectInJersey private FilialService filialService;
	@InjectInJersey private FaturaService faturaService;
	@InjectInJersey private ClienteService clienteService;
	@InjectInJersey private ConfiguracoesFacade configuracoesFacade;
	@InjectInJersey private LoteSerasaService loteSerasaService;
	@InjectInJersey private MonitoramentoMensagemService monitoramentoMensagemService;
	@InjectInJersey private MonitoramentoMensagemFaturaService monitoramentoMensagemFaturaService;

	@GET
	@Path("nrLote")
	public BigDecimal getNrLote() {
		return getProximoNumeroLote();
	}

	@POST
	@Path("importarFaturas")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response importarFaturas(FormDataMultiPart formDataMultiPart) throws IOException{
		String arquivo = getCharacterLobUserTypeFromForm(formDataMultiPart, "arquivo");
		Long id = getModelFromForm(formDataMultiPart, Long.class, "id");
		try {
			List<String> errors =  loteCobrancaTerceiraService.executeImportaArquivo(arquivo,id);
			return Response.ok(errors).build();
		} catch (BusinessException e) {
				throw e;
		} catch (Exception e) {
			throw new BusinessException("LMS-36193",e);
		}
	}
	
	@GET
	@Path("baixarArquivo/{id}")
	public Response baixarArquivo(@PathParam("id")Long idLoteCobranca) throws IOException {
		Map<String, Object> result = new HashMap<>();
		result.put("table", "LOTE_COBRANCA_TERCEIRA");
		result.put("blobColumn", "DC_ARQUIVO");
		result.put("idColumn", "ID_LOTE_COBRANCA_TERCEIRA");
		result.put("id", idLoteCobranca);

		return Response.ok(result).build();
	}
	
	@GET
	@Path("enviarLote/{id}")
	public Response enviarLote(@PathParam("id")Long idLoteCobranca) {
		LoteCobrancaTerceira loteCobranca = loteCobrancaTerceiraService.findLoteCobrancaById(idLoteCobranca);
		if(loteCobranca.getTpLote() != null && !"E".equalsIgnoreCase(loteCobranca.getTpLote().getValue())) {
			throw new BusinessException("LMS-36324");
		}
		
		loteCobranca.setDhEnvio(JTDateTimeUtils.getDataHoraAtual());
		loteCobrancaTerceiraService.store(loteCobranca);
		
		loteCobrancaTerceiraService.executeGeraArquivoLoteCobrancaTerceira(idLoteCobranca);
		
		MonitoramentoMensagem monitMensagem = new MonitoramentoMensagem();
		monitMensagem.setTpModeloMensagem(new DomainValue("GL"));
		monitMensagem.setTpEnvioMensagem(new DomainValue("E"));
		monitMensagem.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		monitMensagem.setDsParametro("{\":1\":\""+idLoteCobranca+"\"}");
		String de = parametroGeralService.findByNomeParametro("REM_EMAIL_LOTE_COB_TERC").getDsConteudo();
		String para = parametroGeralService.findByNomeParametro("DEST_EMAIL_LOTE_COB_TERC").getDsConteudo();
		monitMensagem.setDsDestinatario("{\"de\":\""+de+"\",\"para\":\""+para+"\"}");
		Long idMonitoramento = (Long) monitoramentoMensagemService.store(monitMensagem);
		
		Map criteria = new HashMap<String, Object>();
		criteria.put("loteCobrancaTerceira.idLoteCobrancaTerceira", idLoteCobranca);
		List<ItemLoteCobrancaTerceira> list = itemLoteCobrancaTerceiraService.find(criteria);
		for (ItemLoteCobrancaTerceira item : list) {
			MonitoramentoMensagemFatura monitoramentoMensagemFatura = new MonitoramentoMensagemFatura();
			monitoramentoMensagemFatura.setMonitoramentoMensagem(monitoramentoMensagemService.findByIdMonitoramentoMensagem(idMonitoramento));
			monitoramentoMensagemFatura.setFatura(faturaService.findById(item.getFatura().getIdFatura()));
			monitoramentoMensagemFaturaService.store(monitoramentoMensagemFatura);
		}
		monitoramentoMensagemService.saveEventoMensagem(idMonitoramento, "E", "LMS-36310");
		
		return Response.ok().build();
		
	}

	@GET
	@Path("findHistoricoMensagem/{id}")
	public Response findHistoricoMensagem(@PathParam("id")Long idLoteCobranca) {
		List<MonitoramentoMensagem> monitoramentos = monitoramentoMensagemService.findHistoricoMensagensByLoteCobrancaId(idLoteCobranca);
		List<HistoricoMensagemDTO> historicos = new ArrayList<HistoricoMensagemDTO>();

		for (MonitoramentoMensagem monitMsg : monitoramentos) {
			MonitoramentoMensagemDetalhe monitMsgDetalhe = monitoramentoMensagemService.findMonitoramentoDetalheByMonitoramentoId(monitMsg.getIdMonitoramentoMensagem());
			HistoricoMensagemDTO hist = new HistoricoMensagemDTO();

			Map<String, String> emails = emailsUnmarshal(monitMsg.getDsDestinatario());

			hist.setTpMensagem(monitMsg.getTpModeloMensagem());
			hist.setTpEnvio(monitMsg.getTpEnvioMensagem());
			hist.setPara(emails.get("para"));
			hist.setDhInclusao(JTDateTimeUtils.formatDateTimeToString(monitMsg.getDhInclusao()));
			hist.setDhProcessamento(JTDateTimeUtils.formatDateTimeToString(monitMsg.getDhProcessamento()));
			if ( monitMsgDetalhe != null ){
			hist.setDhEnvio(JTDateTimeUtils.formatDateTimeToString(monitMsgDetalhe.getDhEnvio()));
			hist.setDhRecebimento(JTDateTimeUtils.formatDateTimeToString(monitMsgDetalhe.getDhRecebimento()));
			hist.setDhDevolucao(JTDateTimeUtils.formatDateTimeToString(monitMsgDetalhe.getDhDevolucao()));
			hist.setDhErro(JTDateTimeUtils.formatDateTimeToString(monitMsgDetalhe.getDhErro()));
			}
			hist.setIdMonitoramentoMensagemConteudo(monitMsg.getIdMonitoramentoMensagem());
			hist.setIdMonitoramentoMensagemEvento(monitMsg.getIdMonitoramentoMensagem());

			historicos.add(hist);
		}

		return Response.ok(historicos).build();
	}

	@GET
	@Path("findHistoricoMensagemEvento/{id}")
	public Response findHistoricoMensagemEvento(@PathParam("id")Long idMonitoramentoMensagem) {
		List<MonitoramentoMensagemEvento> monitEvent = monitoramentoMensagemService.findMonitoramentoEventosMensagem(idMonitoramentoMensagem);
		List<HistoricoMensagemDetalheDTO> eventos = new ArrayList<HistoricoMensagemDetalheDTO>();

		for (MonitoramentoMensagemEvento evento : monitEvent) {
			HistoricoMensagemDetalheDTO eventoDTO = new HistoricoMensagemDetalheDTO(); 
			eventoDTO.setTpEvento(evento.getTpEvento());
			String descricao = evento.getDsEvento();
			String message =  configuracoesFacade.getMensagem(descricao.trim());
			if ( message != null ){
				descricao = message;
			}
			eventoDTO.setDescricao(descricao);
			eventoDTO.setDhEvento(JTDateTimeUtils.formatDateTimeToString(evento.getDhEvento()));

			eventos.add(eventoDTO);
		}

		return Response.ok(eventos).build();
	}

	@GET
	@Path("findHistoricoMensagemConteudo/{id}")
	public Response findHistoricoMensagemConteudo(@PathParam("id")Long idMonitoramentoMensagem) {
		MonitoramentoMensagemDetalhe monitMsgDetalhe = monitoramentoMensagemService.findMonitoramentoConteudoMensagem(idMonitoramentoMensagem);
		List<HistoricoMensagemDetalheDTO> conteudos = new ArrayList<HistoricoMensagemDetalheDTO>();

		if ( monitMsgDetalhe == null || monitMsgDetalhe.getDcMensagem() == null ){
			return Response.ok(conteudos).build();
		}

		HistoricoMensagemDetalheDTO conteudoDTO = new HistoricoMensagemDetalheDTO(); 
		conteudoDTO.setDcMensagem( monitMsgDetalhe.getDcMensagem().replace("<img", "p").replace("</img>", "</p>") );

		conteudos.add(conteudoDTO);

		return Response.ok(conteudos).build();
	}


	@SuppressWarnings("unchecked")
	private Map<String, String> emailsUnmarshal(String emailsJson) {
		Map<String, String> parametro = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			parametro = mapper.readValue(emailsJson, Map.class);
		} catch (JsonParseException e) {
			log.error(e);
		} catch (JsonMappingException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
		return parametro;
	}

	/* ItemLoteCobrancaTerceira */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@POST
	@Path("/findItensLoteCobranca")
	public Response findItensLoteCobranca(@QueryParam("id") Long idLoteCobranca) {
		Map criteria = new HashMap<String, Object>();
		criteria.put("loteCobrancaTerceira.idLoteCobrancaTerceira", idLoteCobranca);
		List<ItemLoteCobrancaTerceira> list = itemLoteCobrancaTerceiraService.find(criteria);
		if (list.isEmpty())
			return Response.noContent().build();

		List<Map<String, Object>> ll = new ArrayList<Map<String, Object>>();
		for (ItemLoteCobrancaTerceira dado : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dado.getIdItemLoteCobrancaTerceira());

			Fatura fatura = faturaService.findById(dado.getFatura().getIdFatura());
			Filial filial = filialService.findById(fatura.getFilialByIdFilial().getIdFilial());
			map.put("fatura", filial.getSgFilial() + " " + fatura.getNrFatura());
			Cliente cliente = clienteService.findByIdComPessoa(fatura.getCliente().getIdCliente());
			map.put("cliente", cliente.getPessoa().getNrIdentificacao() + " - " + cliente.getPessoa().getNmPessoa());

			map.put("vl_saldo", FormatUtils.formatDecimal("#,##0.00###", loteSerasaService.findValorSaldo(fatura), true));
			
			if(fatura.getDtEmissao() != null) {
				map.put("dt_emissao", JTFormatUtils.format(fatura.getDtEmissao(), "dd/MM/yyyy"));
			}

			if(fatura.getDtVencimento() != null) {
				map.put("dt_vencimento", JTFormatUtils.format(fatura.getDtVencimento(), "dd/MM/yyyy"));
			}

			map.put("situacao", fatura.getTpSituacaoFatura());

			if(dado.getDtPagamento() != null) {
				map.put("dt_pagamento", JTFormatUtils.format(dado.getDtPagamento(), "dd/MM/yyyy"));
			}

			if(dado.getDtDevolucao() != null) {
				map.put("dt_devolucao", JTFormatUtils.format(dado.getDtDevolucao(), "dd/MM/yyyy"));
			}

			map.put("motivo", dado.getDsMotivo());

			ll.add(map);
		}
		Integer count = list.size();
		return getReturnFind(ll, count);
	}
	
	@POST
	@Path("/findItemLoteCobranca")
	public Response findItemLoteCobranca(@QueryParam("id") Long id) {
		ItemLoteCobrancaTerceira itemLoteCobrancaTerceira = itemLoteCobrancaTerceiraService.findItemLoteCobrancaById(id);
		if (itemLoteCobrancaTerceira == null)
			return Response.noContent().build();

		ItemLoteCobrancaDTO dto = new ItemLoteCobrancaDTO();
//		LoteCobrancaTerceira loteCobranca = loteCobrancaTerceiraService.findLoteCobrancaById(itemLoteCobrancaTerceira.getLoteCobrancaTerceira().getIdLoteCobrancaTerceira());
		dto.setIdLoteCobranca(itemLoteCobrancaTerceira.getLoteCobrancaTerceira().getIdLoteCobrancaTerceira());
		dto.setId(itemLoteCobrancaTerceira.getIdItemLoteCobrancaTerceira());

		Fatura fatura = faturaService.findById(itemLoteCobrancaTerceira.getFatura().getIdFatura());
		FaturaDTO faturaDTO = new FaturaDTO(fatura.getIdFatura(), String.valueOf(fatura.getNrFatura()));
		dto.setFatura(faturaDTO);

		Filial filial = filialService.findById(fatura.getFilialByIdFilial().getIdFilial());
		FilialSuggestDTO filialDTO = new FilialSuggestDTO();
		filialDTO.setId(filial.getIdFilial());
		filialDTO.setIdFilial(filial.getIdFilial());
		filialDTO.setSgFilial(filial.getSgFilial());
		dto.setFilial(filialDTO);

		dto.setMotivo(itemLoteCobrancaTerceira.getDsMotivo());
		dto.setHistorico(itemLoteCobrancaTerceira.getDsHistorico());
		dto.setObservacao(itemLoteCobrancaTerceira.getDsObservacao());
		dto.setNrProcesso(itemLoteCobrancaTerceira.getNrProcesso());
		dto.setDtPagamento(itemLoteCobrancaTerceira.getDtPagamento());
		dto.setDtDevolucao(itemLoteCobrancaTerceira.getDtDevolucao());
		dto.setVlPagamento(itemLoteCobrancaTerceira.getVlPagamento());
		dto.setVlProtesto(itemLoteCobrancaTerceira.getVlProtesto());
		dto.setVlCredito(itemLoteCobrancaTerceira.getVlCredito());
		dto.setVlJuros(itemLoteCobrancaTerceira.getVlJuros());
		dto.setVlMulta(itemLoteCobrancaTerceira.getVlMulta());
		dto.setVlContrato(itemLoteCobrancaTerceira.getVlContrato());

		return Response.ok(dto).build();
	}

	double retrieveZeroIfNull(Double value){
		return ( value == null ? 0D : value);
	}
	@POST
	@Path("storeItemLoteCobranca")
	public Response storeItemLoteCobranca(ItemLoteCobrancaDTO bean) {
		bean.setVlContrato(retrieveZeroIfNull(bean.getVlContrato()));
		bean.setVlCredito(retrieveZeroIfNull(bean.getVlCredito()));
		bean.setVlJuros(retrieveZeroIfNull(bean.getVlJuros()));
		bean.setVlMulta(retrieveZeroIfNull(bean.getVlMulta()));
		bean.setVlPagamento(retrieveZeroIfNull(bean.getVlPagamento()));
		bean.setVlProtesto(retrieveZeroIfNull(bean.getVlProtesto()));
		
		ItemLoteCobrancaTerceira itemLoteCobrancaTerceira = null;
		if(bean.getId() == null) {
			itemLoteCobrancaTerceira = new ItemLoteCobrancaTerceira();
		} else {
			itemLoteCobrancaTerceira = itemLoteCobrancaTerceiraService.findItemLoteCobrancaById(bean.getId());
		}

		LoteCobrancaTerceira loteCobrancaTerceira = loteCobrancaTerceiraService.findLoteCobrancaById(bean.getIdLoteCobranca());

		itemLoteCobrancaTerceira.setLoteCobrancaTerceira(loteCobrancaTerceira);
		Fatura fatura = faturaService.findById(bean.getFatura().getId());
		
		if("P".equals(loteCobrancaTerceira.getTpLote())) {
			fatura.setDhPagtoCobTerceira(JTDateTimeUtils.getDataHoraAtual());
		} else if("D".equals(loteCobrancaTerceira.getTpLote())) {
			fatura.setDhDevolCobTerceira(JTDateTimeUtils.getDataHoraAtual());
		}
		
		itemLoteCobrancaTerceira.setFatura(fatura);

		if ("E".equals(retrieveValueFromDomain(loteCobrancaTerceira.getTpLote())) && (!"BL".equals(retrieveValueFromDomain(fatura.getTpSituacaoFatura())) || (JTDateTimeUtils.comparaData(fatura.getDtVencimento(), JTDateTimeUtils.getDataAtual()) > 0) || "R".equals(retrieveValueFromDomain(fatura.getTpSituacaoAprovacao())) || "E".equals(retrieveValueFromDomain(fatura.getTpSituacaoAprovacao())))) {
			throw new BusinessException("LMS-36326", new Object[]{fatura.getFilialByIdFilial().getSgFilial(), fatura.getNrFatura()});
		}

		if ((bean.getDtPagamento() != null && !"P".equals(loteCobrancaTerceira.getTpLote().getValue())) || (bean.getDtPagamento() == null && "P".equals(loteCobrancaTerceira.getTpLote().getValue()))) {
			throw new BusinessException("LMS-36327");
		}

		if ((bean.getDtDevolucao() != null && !"D".equals(loteCobrancaTerceira.getTpLote().getValue())) || (bean.getDtDevolucao() == null && "D".equals(loteCobrancaTerceira.getTpLote().getValue()))) {
			throw new BusinessException("LMS-36328");
		}

		return Response.ok(itemLoteCobrancaTerceiraService.store(bean.build(itemLoteCobrancaTerceira)))
				.build();
	}

	String retrieveValueFromDomain(DomainValue d){
		if ( d == null || d.getValue() == null ) return "";
		
		return d.getValue();
	}
	
	@GET
	@Path("findCustomById/{id}")
	public LoteCobrancaDTO findCustomById(@PathParam("id")Long id) {
		return findById(id);
	}

	@Override 
	protected LoteCobrancaDTO findById(Long id) { 
		LoteCobrancaTerceira entity = loteCobrancaTerceiraService.findLoteCobrancaById(id);

		LoteCobrancaDTO loteCobrancaDTO = new LoteCobrancaDTO();
		loteCobrancaDTO.setId(entity.getIdLoteCobrancaTerceira());
		loteCobrancaDTO.setIdLoteCobranca(entity.getIdLoteCobrancaTerceira());
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(entity.getUsuario().getIdUsuario());
		usuarioDTO.setNmUsuario(entity.getUsuario().getUsuarioADSM().getNmUsuario());
		loteCobrancaDTO.setUsuario(usuarioDTO);

		loteCobrancaDTO.setNrLote(entity.getNrLote());
		loteCobrancaDTO.setDescricao(entity.getDsLote());
		loteCobrancaDTO.setTpLote(entity.getTpLote());
		loteCobrancaDTO.setDtAlteracaoFormatada(JTFormatUtils.format(entity.getDhAlteracao(), "dd/MM/yyyy HH:mm"));
		
		if ( entity.getDhEnvio() != null ){
			loteCobrancaDTO.setDtEnvioFormatada(JTFormatUtils.format(entity.getDhEnvio(), "dd/MM/yyyy HH:mm"));
		}

		return loteCobrancaDTO; 
	} 

	private BigDecimal getProximoNumeroLote() {
		return new BigDecimal(Integer.parseInt(parametroGeralService.findByNomeParametro("NR_LOTE_COBRANCA_TERCEIRA").getDsConteudo()) + 1);
	}

	@Override 
	protected Long store(LoteCobrancaDTO bean) { 
		LoteCobrancaTerceira loteCobrancaTerceira = null;
		if(bean.getId() == null) {
			loteCobrancaTerceira = new LoteCobrancaTerceira();
			BigDecimal nrLote = getProximoNumeroLote();
			parametroGeralService.storeValorParametro("NR_LOTE_COBRANCA_TERCEIRA", nrLote);
			loteCobrancaTerceira.setNrLote(String.valueOf(nrLote));
		} else {
			loteCobrancaTerceira = loteCobrancaTerceiraService.findLoteCobrancaById(bean.getId());
		}
		loteCobrancaTerceira.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());

		return (Long) loteCobrancaTerceiraService.store(bean.build(loteCobrancaTerceira)); 
	} 

	@Override 
	protected void removeById(Long id) { 
		remove(id);
	} 
	
	private void remove(Long id) {
		LoteCobrancaTerceira loteCobranca = loteCobrancaTerceiraService.findLoteCobrancaById(id);
		if(loteCobranca.getDhEnvio() != null) {
			throw new BusinessException("LMS-36272");
		}
	
		itemLoteCobrancaTerceiraService.removeByIdLoteCobranca(id);
		loteCobrancaTerceiraService.removeById(id);
	}

	@POST
	@Path("removeFaturasByIds")
	public void removeFaturasByIds(List<Long> ids) {
		itemLoteCobrancaTerceiraService.removeFaturasByIds(ids);
	}
	
	@Override
	protected void removeByIds(List<Long> ids) {
		for(Long id : ids){
				remove(id);
			}
		}

	private LoteCobrancaDTO getLoteCobrancaDTO(LoteCobrancaTerceira l) {
		LoteCobrancaDTO loteCobrancaDTO = new LoteCobrancaDTO();

		loteCobrancaDTO.setId(l.getIdLoteCobrancaTerceira());
		loteCobrancaDTO.setIdLoteCobranca(l.getIdLoteCobrancaTerceira());

		loteCobrancaDTO.setDtAlteracao(l.getDhAlteracao());
		if (l.getDhAlteracao() != null) { 
			loteCobrancaDTO.setDtAlteracaoFormatada(JTFormatUtils.format(l.getDhAlteracao(), "dd/MM/yyyy HH:mm"));
		}

		loteCobrancaDTO.setDtEnvio(l.getDhEnvio());
		if (l.getDhEnvio() != null) { 
			loteCobrancaDTO.setDtEnvioFormatada(JTFormatUtils.format(l.getDhEnvio(), "dd/MM/yyyy HH:mm"));
		}

		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(l.getUsuario().getIdUsuario());
		usuarioDTO.setNmUsuario(l.getUsuario().getUsuarioADSM().getNmUsuario());
		loteCobrancaDTO.setUsuario(usuarioDTO);

		loteCobrancaDTO.setTpLote(l.getTpLote());
		loteCobrancaDTO.setDescricao(l.getDsLote());
		loteCobrancaDTO.setNrLote(l.getNrLote());

		return loteCobrancaDTO;
	}

	@Override 
	protected List<LoteCobrancaDTO> find(LoteCobrancaFilterDTO filter) { 
		List<ItemLoteCobrancaTerceira> itensLoteCobrancaTerceiraBD = itemLoteCobrancaTerceiraService.findAll(convertFilterToTypedFlatMap(filter));
		List<LoteCobrancaTerceira> lotesCobrancaTerceiraBD = loteCobrancaTerceiraService.findAll(convertFilterToTypedFlatMap(filter));
		List<LoteCobrancaDTO> loteCobrancasDTO = new ArrayList<LoteCobrancaDTO>();

		if (filter.getFatura() != null) {
			for(ItemLoteCobrancaTerceira i : itensLoteCobrancaTerceiraBD) {
				LoteCobrancaTerceira loteCobrancaTerceira = i.getLoteCobrancaTerceira();
				loteCobrancasDTO.add(getLoteCobrancaDTO(loteCobrancaTerceira));
			}
		}else {
			for(LoteCobrancaTerceira l : lotesCobrancaTerceiraBD) {
				loteCobrancasDTO.add(getLoteCobrancaDTO(l));
			}
		}

		return loteCobrancasDTO;  
	} 

	private TypedFlatMap convertFilterToTypedFlatMap(LoteCobrancaFilterDTO filter) {
		TypedFlatMap tfm = super.getTypedFlatMapWithPaginationInfo(filter);
		if (filter.getId() != null) tfm.put("id_item_lote_cobranca_terceira", filter.getId());
		if (filter.getFilial() != null) tfm.put("id_filial", filter.getFilial().getId());
		if (filter.getFatura() != null) tfm.put("id_fatura", filter.getFatura().getId());
		if (filter.getTpLote() != null) tfm.put("tp_lote", filter.getTpLote());
		if (filter.getTpLoteEnviado() != null) tfm.put("tp_lote_enviado", filter.getTpLoteEnviado());
		if (filter.getNrLote() != null) tfm.put("nr_lote", filter.getNrLote());
		if (filter.getDescricao() != null) tfm.put("descricao", filter.getDescricao());
		if (filter.getDtAlteracaoLoteInicial() != null) tfm.put("dt_alteracao_inicial", filter.getDtAlteracaoLoteInicial());
		if (filter.getDtAlteracaoLoteFinal() != null) tfm.put("dt_alteracao_final", filter.getDtAlteracaoLoteFinal());
		if (filter.getDtEnvioLoteInicial() != null) tfm.put("dt_envio_inicial", filter.getDtEnvioLoteInicial());
		if (filter.getDtEnvioLoteFinal() != null) tfm.put("dt_envio_final", filter.getDtEnvioLoteFinal());

		return tfm;
	}

	@Override 
	protected Integer count(LoteCobrancaFilterDTO filter) {  
		return 1; 
	} 


} 
