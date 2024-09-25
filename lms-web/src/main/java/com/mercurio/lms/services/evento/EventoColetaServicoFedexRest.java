package com.mercurio.lms.services.evento;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoLogradouroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;

import br.com.tntbrasil.integracao.domains.sim.EventoColetaFedexDMN;


@Path("/evento/coleta/integracaoFedex") 
public class EventoColetaServicoFedexRest extends BaseRest {
	
	private static final String RETORNO_OK = "OK";
	private static final String RETORNO_IGNORAR_PEDIDO = "IGNORE";
	private static final String CD_INTEGRA_FDX = "CD_INTEGRA_FDX";

	@InjectInJersey
	ConfiguracoesFacade configuracoesFacade;
	
	@InjectInJersey
	PedidoColetaService pedidoColetaService;
	
	@InjectInJersey
	PessoaService pessoaService;
	
	@InjectInJersey
	EnderecoPessoaService enderecoPessoaService;
	
	@InjectInJersey
	TipoLogradouroService tipoLogradouroService;
	
	@InjectInJersey
	DetalheColetaService detalheColetaService;
	
	@InjectInJersey
	FilialService filialService;
		
	@SuppressWarnings("unchecked")
	@POST
	@Path("buscar")
	public Response buscarDadosEvento(EventoColetaFedexDMN eventoDMN) {
		  		
		PedidoColeta pedidoColeta = this.pedidoColetaService.findById(eventoDMN.getIdPedidoColeta());
		List<DetalheColeta> detalhesColeta = detalheColetaService.findDetalheColetaByIdPedidoColeta(pedidoColeta.getIdPedidoColeta());
		String nomeRemetente = pessoaService.findById(pedidoColeta.getCliente().getIdCliente()).getNmPessoa();
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findById(pedidoColeta.getEnderecoPessoa().getIdEnderecoPessoa());
		TipoLogradouro tipoLogradouro = tipoLogradouroService.findById(enderecoPessoa.getTipoLogradouro().getIdTipoLogradouro());
		Pessoa pessoa = pessoaService.findById(pedidoColeta.getFilialByIdFilialResponsavel().getIdFilial());
		
		if(!validateFilialParametrizado(eventoDMN.getIdFilialResponsavel())){
			eventoDMN.setStatusRetorno(RETORNO_IGNORAR_PEDIDO);
			return handleResponse(eventoDMN);
		}
		
		populaDominio(eventoDMN, pedidoColeta, detalhesColeta, nomeRemetente, enderecoPessoa, tipoLogradouro, pessoa);
		eventoDMN.setStatusRetorno(RETORNO_OK);
		
		return handleResponse(eventoDMN);
	}
	
	private void populaDominio(EventoColetaFedexDMN eventoColetaFedexDMN, PedidoColeta pedidoColeta, List<DetalheColeta> detalhesColeta, String nomeRemetente, EnderecoPessoa enderecoPessoa, TipoLogradouro tipoLogradouro, Pessoa pessoa){
		
		eventoColetaFedexDMN.setDataRealizacaoColeta(pedidoColeta.getDtPrevisaoColeta().toDateTimeAtMidnight());
		eventoColetaFedexDMN.setHoraLimiteRealizacaoColeta(pedidoColeta.getHrLimiteColeta().toDateTimeToday());
		eventoColetaFedexDMN.setTelefoneContato(pedidoColeta.getNrDddCliente()+pedidoColeta.getNrTelefoneCliente());
		eventoColetaFedexDMN.setFilialDestinoColeta(pedidoColeta.getFilialByIdFilialResponsavel().getCdFilialFedex());
		eventoColetaFedexDMN.setQtVolumes(somaVolumes(detalhesColeta));
		eventoColetaFedexDMN.setPeso(somaPeso(detalhesColeta));
		eventoColetaFedexDMN.setNomeRemetente(nomeRemetente);
		eventoColetaFedexDMN.setObservacao(pedidoColeta.getObPedidoColeta());
		eventoColetaFedexDMN.setObservacaoComplemento(pedidoColeta.getDsComplementoEndereco());
		eventoColetaFedexDMN.setNmContatoCliente(pedidoColeta.getNmContatoCliente());
		eventoColetaFedexDMN.setCep(pedidoColeta.getNrCep());
		eventoColetaFedexDMN.setEnderecoRemetente(tipoLogradouro.getDsTipoLogradouro().getValue() + " " + enderecoPessoa.getDsEndereco());
		eventoColetaFedexDMN.setNrEnderecoRemetente(enderecoPessoa.getNrEndereco());
		eventoColetaFedexDMN.setCnpjPagadorFrete(pessoa.getNrIdentificacao());
	}
	
	private Integer somaVolumes(List<DetalheColeta> detalhesColeta) {
		Integer total = 0;
		for (DetalheColeta detalheColeta : detalhesColeta) {
			total = total + detalheColeta.getQtVolumes();
		}
		return total;
	}
	
	private BigDecimal somaPeso(List<DetalheColeta> detalhesColeta) {
		BigDecimal total = BigDecimal.ZERO;
		for (DetalheColeta detalheColeta : detalhesColeta) {
			total = total.add(detalheColeta.getPsMercadoria());
		}
		return total;
	}
	
	boolean validateFilialParametrizado(Long idFilial) {
		Filial filial = filialService.findById(idFilial);
		List<String> cdsFiliais = asList((String)configuracoesFacade.getValorParametro(CD_INTEGRA_FDX));
		return CollectionUtils.isNotEmpty(cdsFiliais) && filial.getCodFilial()!= null && cdsFiliais.contains(filial.getCodFilial().toString());
	}

	private List<String> asList(String valorParametro) {
		if(valorParametro == null){
			return Collections.emptyList();
		}
		
		return Arrays.asList(valorParametro.split(";"));
	}
	
	private Response handleResponse(EventoColetaFedexDMN evento) {
		return Response.ok(evento).build();
	}
	
}
