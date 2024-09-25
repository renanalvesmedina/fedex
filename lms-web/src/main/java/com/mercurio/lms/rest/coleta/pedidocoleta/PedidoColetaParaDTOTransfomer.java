package com.mercurio.lms.rest.coleta.pedidocoleta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.PedidoColetaProduto;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.CotacaoDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.PedidoColetaDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.ProdutoDiferenciadoDTO;
import com.mercurio.lms.rest.coleta.pedidocoleta.dto.RotaColetaEntregaDTO;
import com.mercurio.lms.rest.configuracoes.ServicoAdicionalChosenDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class PedidoColetaParaDTOTransfomer {

	private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("HH:mm");
	
	private PedidoColetaParaDTOTransfomer() {
	}
	
	public static PedidoColetaParaDTOTransfomer getInstance() {
		return new PedidoColetaParaDTOTransfomer();
	}
	
	public PedidoColetaDTO transformaParaDTO(PedidoColeta pedidoColeta, ConversaoMoedaService conversaoMoedaService, RotaColetaEntregaService rotaColetaEntregaService, ConfiguracoesFacade configuracoesFacade) {
		
		Long idCliente = null;
		Long idEnderecoPessoa = null;
		
		PedidoColetaDTO dto = new PedidoColetaDTO();
		dto.setIdPedidoColeta(pedidoColeta.getIdPedidoColeta());
		dto.setId(pedidoColeta.getIdPedidoColeta());
		dto.setNrColeta(pedidoColeta.getNrColeta());
		dto.setNrDddCliente(pedidoColeta.getNrDddCliente());
		
		dto.setNrTelefoneCliente(pedidoColeta.getNrTelefoneCliente());
		dto.setDhPedidoColeta(pedidoColeta.getDhPedidoColeta());
		dto.setDhColetaDisponivel(pedidoColeta.getDhColetaDisponivel());
		dto.setDtPrevisaoColeta(pedidoColeta.getDtPrevisaoColeta());
		dto.setHrLimiteColeta(dateTimeFormatter.print(pedidoColeta.getHrLimiteColeta()));
		dto.setTpModoPedidoColeta(pedidoColeta.getTpModoPedidoColeta());
		dto.setTpPedidoColeta(pedidoColeta.getTpPedidoColeta());
		dto.setTpStatusColeta(pedidoColeta.getTpStatusColeta());
		dto.setNmSolicitante(pedidoColeta.getNmSolicitante());
		dto.setNmContatoCliente(pedidoColeta.getNmContatoCliente());
		dto.setVlTotalInformado(pedidoColeta.getVlTotalInformado());		
		dto.setQtTotalVolumesInformado(pedidoColeta.getQtTotalVolumesInformado());		
		dto.setPsTotalInformado(pedidoColeta.getPsTotalInformado());		
		dto.setPsTotalAforadoInformado(pedidoColeta.getPsTotalAforadoInformado());		
		dto.setBlClienteLiberadoManual(pedidoColeta.getBlClienteLiberadoManual());
		dto.setBlAlteradoPosProgramacao(pedidoColeta.getBlAlteradoPosProgramacao());
		dto.setObPedidoColeta(pedidoColeta.getObPedidoColeta());
		dto.setBlProdutoDiferenciado(pedidoColeta.getBlProdutoDiferenciado());
		dto.setDsInfColeta(pedidoColeta.getDsInfColeta());
		dto.setSituacaoAprovacao(pedidoColeta.getSituacaoAprovacao());
		
		dto.setEndereco(formataEndereco(null, pedidoColeta.getDsComplementoEndereco(), pedidoColeta.getEdColeta(), pedidoColeta.getNrEndereco(), pedidoColeta.getDsBairro(), pedidoColeta.getNrCep(), configuracoesFacade));
		dto.setNrCep(pedidoColeta.getNrCep());
		dto.setDsBairro(pedidoColeta.getDsBairro());
		dto.setDsComplementoEndereco(pedidoColeta.getDsComplementoEndereco());
		dto.setNrEndereco(pedidoColeta.getNrEndereco());
		dto.setEdColeta(pedidoColeta.getEdColeta());
		
		dto.setProdutosDiferenciados(new ArrayList<ProdutoDiferenciadoDTO>());
		if (pedidoColeta.getBlProdutoDiferenciado()) {
			for (PedidoColetaProduto pedidoColetaProduto : (List<PedidoColetaProduto>) pedidoColeta.getPedidoColetaProdutos()) {
				dto.getProdutosDiferenciados().add(getProdutoDiferenciadoDTO(pedidoColetaProduto));
			}
		}
		
		BigDecimal vlTotalVerificado = BigDecimalUtils.ZERO;
		Integer qtTotalVolumesVerificado = Integer.valueOf(0);
		BigDecimal psTotalVerificado = BigDecimalUtils.ZERO;
		BigDecimal psTotalAforadoVerificado = BigDecimalUtils.ZERO;		
		
		for (Iterator iter = pedidoColeta.getDetalheColetas().iterator(); iter.hasNext();) {
			DetalheColeta detalheColeta = (DetalheColeta) iter.next();
			BigDecimal valorMercadoriaConvertido = conversaoMoedaService.findConversaoMoeda(
													 SessionUtils.getPaisSessao().getIdPais(),
													 detalheColeta.getMoeda().getIdMoeda(),
													 SessionUtils.getPaisSessao().getIdPais(),
													 SessionUtils.getMoedaSessao().getIdMoeda(),
													 JTDateTimeUtils.getDataAtual(),
													 detalheColeta.getVlMercadoria());

			vlTotalVerificado = vlTotalVerificado.add(valorMercadoriaConvertido);
			psTotalVerificado = psTotalVerificado.add(detalheColeta.getPsMercadoria());
			psTotalAforadoVerificado = psTotalAforadoVerificado.add(detalheColeta.getPsAforado() == null ? BigDecimal.ZERO : detalheColeta.getPsAforado());
			qtTotalVolumesVerificado = Integer.valueOf(qtTotalVolumesVerificado.intValue() + detalheColeta.getQtVolumes().intValue());			
		}
		dto.setVlTotalVerificado(vlTotalVerificado);
		dto.setQtTotalVolumesVerificado(qtTotalVolumesVerificado);
		dto.setPsTotalVerificado(psTotalVerificado);
		dto.setPsTotalAforadoVerificado(psTotalAforadoVerificado);
		
		if (pedidoColeta.getUsuario() != null) {
			dto.setIdUsuario(pedidoColeta.getUsuario().getIdUsuario());
		}
		if (pedidoColeta.getFilialByIdFilialSolicitante() != null) {
			dto.setIdFilialByIdFilialSolicitante(pedidoColeta.getFilialByIdFilialSolicitante().getIdFilial());
		}
		if (pedidoColeta.getCliente() != null) {
			idCliente = pedidoColeta.getCliente().getIdCliente();
			dto.setCliente(getClienteDTO(pedidoColeta.getCliente()));
		}
		if (pedidoColeta.getEnderecoPessoa() != null) {
			idEnderecoPessoa = pedidoColeta.getEnderecoPessoa().getIdEnderecoPessoa();
			dto.setIdEnderecoPessoa(pedidoColeta.getEnderecoPessoa().getIdEnderecoPessoa());
		}		
		if (pedidoColeta.getMoeda() != null) {
			dto.setIdMoeda(pedidoColeta.getMoeda().getIdMoeda());
			dto.setSiglaSimbolo(pedidoColeta.getMoeda().getSiglaSimbolo());
		}
		if (pedidoColeta.getMunicipio() != null) {
			dto.setIdMunicipio(pedidoColeta.getMunicipio().getIdMunicipio());
			dto.setNmMunicipio(pedidoColeta.getMunicipio().getNmMunicipio());
			dto.setSgUnidadeFederativaMunicipio(pedidoColeta.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			dto.setNmUnidadeFederativaMunicipio(pedidoColeta.getMunicipio().getUnidadeFederativa().getNmUnidadeFederativa());
		}
		if (pedidoColeta.getFilialByIdFilialResponsavel() != null) {
			dto.setSgFilialColeta(pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial());
			dto.setFilialByIdFilialResponsavel(getFilialDTO(pedidoColeta.getFilialByIdFilialResponsavel()));
		}
		if (pedidoColeta.getRotaIntervaloCep() != null) {
			dto.setIdRotaIntervaloCep(pedidoColeta.getRotaIntervaloCep().getIdRotaIntervaloCep());
		}
		if (pedidoColeta.getRotaColetaEntrega() != null) {
			dto.setRotaColetaEntrega(getRotaColetaEntragaDTO(pedidoColeta.getRotaColetaEntrega()));
		}
		if (pedidoColeta.getCotacao() != null) {
			dto.setCotacao(getCotacaoDTO(pedidoColeta.getCotacao()));
		}
		
		// Busca horário de corte pela Rota de Intervalo de Cep.
		if (pedidoColeta.getNrCep() != null) {
			Long idFilialRotaAtendimento = pedidoColeta.getFilialByIdFilialSolicitante().getIdFilial();
			if(pedidoColeta.getRotaColetaEntrega() != null) {
				idFilialRotaAtendimento = pedidoColeta.getRotaColetaEntrega().getFilial().getIdFilial();
			}
			RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService.findRotaAtendimentoCep(
					pedidoColeta.getNrCep(), 
					idCliente, 
					idEnderecoPessoa,
					idFilialRotaAtendimento, //LMS-1321
					JTDateTimeUtils.getDataAtual());
			if(rotaIntervaloCep != null) {
				dto.setHorarioCorte(dateTimeFormatter.print(rotaIntervaloCep.getHrCorteSolicitacao()));
			}
		}

		List<ServicoAdicionalChosenDTO> listServicoAdicionalColeta = new ArrayList<ServicoAdicionalChosenDTO>();		
		dto.setServicosAdicionaisColetas(listServicoAdicionalColeta);
		
		return dto;
	}
	
	public String formataEndereco(String dsTipoLogradouro, String dsComplemento, String dsEndereco, String nrEndereco, String dsBairro, String nrCep, ConfiguracoesFacade configuracoesFacade) {

		String complemento = configuracoesFacade.getMensagem("complemento");
		String bairro = configuracoesFacade.getMensagem("bairro");
		String cep = configuracoesFacade.getMensagem("cep");

		StringBuffer endereco = new StringBuffer().append(dsTipoLogradouro == null ? "" : dsTipoLogradouro + " ")
												  .append(dsEndereco)
												  .append(", nº: ")
												  .append(nrEndereco == null ? "" : nrEndereco)
												  .append(dsComplemento == null ? "" : " / " + complemento + ": " + dsComplemento)
												  .append(dsBairro == null ? "" : "\n" + bairro + ": " + dsBairro)
												  .append("\n" + cep + ": " + nrCep);

		return endereco.toString();
	}

	private CotacaoDTO getCotacaoDTO(Cotacao cotacao) {
		CotacaoDTO dto = new CotacaoDTO();
		dto.setId(cotacao.getIdCotacao());
		dto.setSgfilialByIdFilialOrigem(cotacao.getFilialByIdFilialOrigem().getSgFilial());
		dto.setNrCotacao(cotacao.getNrCotacao());
		return dto;
	}

	private RotaColetaEntregaDTO getRotaColetaEntragaDTO(RotaColetaEntrega rotaColetaEntrega) {
		RotaColetaEntregaDTO dto = new RotaColetaEntregaDTO();
		dto.setId(rotaColetaEntrega.getIdRotaColetaEntrega());
		dto.setNrRota(rotaColetaEntrega.getNrRota());
		dto.setDsRota(rotaColetaEntrega.getDsRota());
		return dto;
	}

	private FilialSuggestDTO getFilialDTO(Filial filialByIdFilialResponsavel) {
		FilialSuggestDTO dto = new FilialSuggestDTO();
		dto.setId(filialByIdFilialResponsavel.getIdFilial());
		dto.setSgFilial(filialByIdFilialResponsavel.getSgFilial());
		dto.setNmFilial(filialByIdFilialResponsavel.getPessoa().getNmFantasia());
		return dto;
	}

	private ClienteSuggestDTO getClienteDTO(Cliente cliente) {
		ClienteSuggestDTO dto = new ClienteSuggestDTO();
		dto.setId(cliente.getIdCliente());
		dto.setIdCliente(cliente.getIdCliente());
		dto.setTpIdentificacao(cliente.getPessoa().getTpIdentificacao());
		dto.setNrIdentificacao(cliente.getPessoa().getNrIdentificacao());
//		dto.setNrIdentificacaoFormatado(FormatUtils.formatIdentificacao(cliente.getPessoa().getTpIdentificacao(), cliente.getPessoa().getNrIdentificacao()));
		dto.setNmPessoa(cliente.getPessoa().getNmPessoa());
		//dto.setTpCliente(cliente.getTpCliente().getDescriptionAsString());
		dto.setTpCliente(cliente.getTpCliente().getValue().toString());
		return dto;
	}
	
	private ProdutoDiferenciadoDTO getProdutoDiferenciadoDTO(PedidoColetaProduto pedidoColetaProduto) {
		ProdutoDiferenciadoDTO dto = new ProdutoDiferenciadoDTO();
		dto.setIdPedidoColetaProduto(pedidoColetaProduto.getIdPedidoColetaProduto());
		dto.setIdProduto(pedidoColetaProduto.getProduto().getIdProduto());
		dto.setDsProduto(pedidoColetaProduto.getProduto().getDsProduto().getValue());
		return dto;
	}
	
	public PedidoColeta transformaParaEntity(PedidoColetaDTO dto, PedidoColetaService pedidoColetaService, ClienteService clienteService, TelefoneEnderecoService telefoneEnderecoService) {
		
		PedidoColeta pedidoColeta = null;
		
		if (dto.getIdPedidoColeta() != null) {
			pedidoColeta = pedidoColetaService.findById(dto.getIdPedidoColeta());
		} else {
			pedidoColeta = new PedidoColeta();
		}
		
		Cliente cliente = clienteService.findById(dto.getCliente().getId());
		
		pedidoColeta.setNrColeta(dto.getNrColeta());
		pedidoColeta.setDhPedidoColeta(JTDateTimeUtils.getDataHoraAtual());
		
		pedidoColeta.setTpStatusColeta(dto.getTpStatusColeta());
		pedidoColeta.setTpModoPedidoColeta(dto.getTpModoPedidoColeta());
		if (pedidoColeta.getTpModoPedidoColeta().getValue().equals("BA")) {			
			pedidoColeta.setTpStatusColeta(new DomainValue("EX"));
		}
		
		pedidoColeta.setBlClienteLiberadoManual(dto.getBlClienteLiberadoManual());
		pedidoColeta.setBlAlteradoPosProgramacao(dto.getBlAlteradoPosProgramacao());
		pedidoColeta.setNmContatoCliente(dto.getNmContatoCliente());
		pedidoColeta.setNmSolicitante(dto.getNmSolicitante());
		
		if (dto.getTelefoneEndereco() != null){
			//Telefone novo
			pedidoColeta.setNrDddCliente(dto.getNrNovoDddCliente());
			pedidoColeta.setNrTelefoneCliente(dto.getNrNovoTelefoneCliente());
			
			DomainValue domainValueTpTelefone;
			if (cliente.getPessoa().getTpPessoa().getValue().equals("J")) {
				domainValueTpTelefone = new DomainValue("C");
			} else {
				domainValueTpTelefone = new DomainValue("R");			
			}
			TelefoneEndereco telefoneEndereco = new TelefoneEndereco();
			EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
			enderecoPessoa.setIdEnderecoPessoa(dto.getIdEnderecoPessoa());
			telefoneEndereco.setEnderecoPessoa(enderecoPessoa);
			telefoneEndereco.setNrDdd(dto.getNrNovoDddCliente());
			telefoneEndereco.setNrTelefone(dto.getNrNovoTelefoneCliente());
			telefoneEndereco.setPessoa(cliente.getPessoa());
			telefoneEndereco.setTpUso(new DomainValue("FO"));
			telefoneEndereco.setTpTelefone(domainValueTpTelefone);
			telefoneEnderecoService.store(telefoneEndereco);
		} else {
			pedidoColeta.setNrDddCliente(dto.getNrDddCliente());
			pedidoColeta.setNrTelefoneCliente(dto.getNrTelefoneCliente());
		}
		
		YearMonthDay dataPrevisaoColeta = dto.getDtPrevisaoColeta();
		
		if (JTDateTimeUtils.comparaData(dataPrevisaoColeta, JTDateTimeUtils.getDataAtual()) < 0) {
			throw new BusinessException("LMS-02111");
		}
		pedidoColeta.setDtPrevisaoColeta(dataPrevisaoColeta);
		
		pedidoColeta.setObPedidoColeta(dto.getObPedidoColeta());
		pedidoColeta.setTpPedidoColeta(dto.getTpPedidoColeta());
		pedidoColeta.setDhColetaDisponivel(dto.getDhColetaDisponivel());
		pedidoColeta.setHrLimiteColeta(TimeOfDay.fromDateFields(dateTimeFormatter.parseDateTime(dto.getHrLimiteColeta()).toDate()));
		pedidoColeta.setEdColeta(dto.getDsTipoLogradouro() + " " + dto.getEdColeta());
		
		if(dto.getNrEndereco() != null) {		
			pedidoColeta.setNrEndereco(dto.getNrEndereco().trim());
		}
		
		pedidoColeta.setDsComplementoEndereco(dto.getDsComplementoEndereco());
		pedidoColeta.setDsBairro(dto.getDsBairro());
		pedidoColeta.setNrCep(dto.getNrCep());
		
		pedidoColeta.setVlTotalInformado(dto.getVlTotalInformado());
		if(dto.getVlTotalInformado() == null) {		
			pedidoColeta.setVlTotalInformado(BigDecimalUtils.ZERO);
		}
		pedidoColeta.setVlTotalVerificado(dto.getVlTotalVerificado());
		if(dto.getVlTotalVerificado() == null) {
			pedidoColeta.setVlTotalVerificado(BigDecimalUtils.ZERO);
		}
		pedidoColeta.setQtTotalVolumesInformado(dto.getQtTotalVolumesInformado());
		pedidoColeta.setQtTotalVolumesVerificado(dto.getQtTotalVolumesVerificado());
		pedidoColeta.setPsTotalInformado(dto.getPsTotalInformado());
		if(dto.getPsTotalInformado() == null) {
			pedidoColeta.setPsTotalInformado(BigDecimalUtils.ZERO);
		}
		pedidoColeta.setPsTotalVerificado(dto.getPsTotalVerificado());
		if(dto.getPsTotalVerificado() == null) {
			pedidoColeta.setPsTotalVerificado(BigDecimalUtils.ZERO);
		}
		pedidoColeta.setPsTotalAforadoInformado(dto.getPsTotalAforadoInformado());
		if(dto.getPsTotalAforadoInformado() == null) {
			pedidoColeta.setPsTotalAforadoInformado(BigDecimalUtils.ZERO);
		}	
		pedidoColeta.setPsTotalAforadoVerificado(dto.getPsTotalAforadoVerificado());
		if(dto.getPsTotalAforadoVerificado() == null) {
			pedidoColeta.setPsTotalAforadoVerificado(BigDecimalUtils.ZERO);
		}			
		
		pedidoColeta.setCliente(cliente);
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		enderecoPessoa.setIdEnderecoPessoa(dto.getIdEnderecoPessoa());
		pedidoColeta.setEnderecoPessoa(enderecoPessoa);
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(dto.getIdMunicipio());
		pedidoColeta.setMunicipio(municipio);
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(dto.getIdUsuario());
		pedidoColeta.setUsuario(usuario);
		Filial filialSolicitante = new Filial(dto.getIdFilialByIdFilialSolicitante());
		pedidoColeta.setFilialByIdFilialSolicitante(filialSolicitante);
		Filial filialResponsavel = new Filial(dto.getFilialByIdFilialResponsavel().getId());
		pedidoColeta.setFilialByIdFilialResponsavel(filialResponsavel);
		Moeda moeda = new Moeda(dto.getIdMoeda());
		pedidoColeta.setMoeda(moeda);
		
		pedidoColeta.setBlProdutoDiferenciado(dto.getBlProdutoDiferenciado());
		pedidoColeta.setDsInfColeta(dto.getDsInfColeta());
		
		if (dto.getCotacao() != null) {
			Cotacao cotacao = new Cotacao();
			cotacao.setIdCotacao(dto.getCotacao().getId());
			pedidoColeta.setCotacao(cotacao);
		} else {
			pedidoColeta.setCotacao(null);
		}

		if (dto.getIdRotaIntervaloCep() != null) {
			RotaIntervaloCep rotaIntervaloCep = new RotaIntervaloCep();
			rotaIntervaloCep.setIdRotaIntervaloCep(dto.getIdRotaIntervaloCep());
			pedidoColeta.setRotaIntervaloCep(rotaIntervaloCep);
		} else {
			if (!"BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())){
				throw new BusinessException("LMS-02011");
			}
		}
		
		if (dto.getRotaColetaEntrega() != null) {
			RotaColetaEntrega rotaColetaEntrega = new RotaColetaEntrega();
			rotaColetaEntrega.setIdRotaColetaEntrega(dto.getRotaColetaEntrega().getId());
			pedidoColeta.setRotaColetaEntrega(rotaColetaEntrega);
		} else {
			if (!"BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue())){
				throw new BusinessException("LMS-02011");
			}		
		}
		
		return pedidoColeta;
	}

}
