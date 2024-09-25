package com.mercurio.lms.services.vendas;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.vendas.action.CotacaoFreteViaWebAction;

import br.com.tntbrasil.integracao.domains.webservice.CalculoFreteWebServiceRetorno;
import br.com.tntbrasil.integracao.domains.webservice.CotacaoWebService;
import br.com.tntbrasil.integracao.domains.webservice.ParcelasFreteWebService;
import br.com.tntbrasil.integracao.domains.webservice.ServicoAdicionalWebService;


@Path("/vendas/calculoFreteViaWeb")
public class CalculoFreteViaWebRest {

	@InjectInJersey
	CotacaoFreteViaWebAction cotacaoFreteViaWebAction;
	
	@POST
	@Path("calculoFrete")
	public CalculoFreteWebServiceRetorno calculoFrete(CotacaoWebService deCotacaoWebService ){
		com.mercurio.lms.expedicao.model.CalculoFreteWebServiceRetorno deCalculoFreteWebServiceRetorno = cotacaoFreteViaWebAction.calculoFrete(deParaCotacao(deCotacaoWebService));
		return deParaCotacaoRetorno(deCalculoFreteWebServiceRetorno);
	}

	private CalculoFreteWebServiceRetorno deParaCotacaoRetorno(
			com.mercurio.lms.expedicao.model.CalculoFreteWebServiceRetorno deCalculoFreteWebServiceRetorno) {
		CalculoFreteWebServiceRetorno  paraCalculoFreteWebServiceRetorno = new CalculoFreteWebServiceRetorno();
		
		paraCalculoFreteWebServiceRetorno.setErrorList(deCalculoFreteWebServiceRetorno.getErrorList());
		paraCalculoFreteWebServiceRetorno.setNmDestinatario(deCalculoFreteWebServiceRetorno.getNmDestinatario());
		paraCalculoFreteWebServiceRetorno.setNmMunicipioDestino(deCalculoFreteWebServiceRetorno.getNmMunicipioDestino());
		paraCalculoFreteWebServiceRetorno.setNmMunicipioOrigem(deCalculoFreteWebServiceRetorno.getNmMunicipioOrigem());
		paraCalculoFreteWebServiceRetorno.setNmRemetente(deCalculoFreteWebServiceRetorno.getNmRemetente());
		paraCalculoFreteWebServiceRetorno.setNrDDDFilialDestino(deCalculoFreteWebServiceRetorno.getNrDDDFilialDestino());
		paraCalculoFreteWebServiceRetorno.setNrDDDFilialOrigem(deCalculoFreteWebServiceRetorno.getNrDDDFilialOrigem());
		paraCalculoFreteWebServiceRetorno.setNrTelefoneFilialDestino(deCalculoFreteWebServiceRetorno.getNrTelefoneFilialDestino());
		paraCalculoFreteWebServiceRetorno.setNrTelefoneFilialOrigem(deCalculoFreteWebServiceRetorno.getNrTelefoneFilialOrigem());
		paraCalculoFreteWebServiceRetorno.setPrazoEntrega(deCalculoFreteWebServiceRetorno.getPrazoEntrega());
		paraCalculoFreteWebServiceRetorno.setVlDesconto(deCalculoFreteWebServiceRetorno.getVlDesconto());
		paraCalculoFreteWebServiceRetorno.setVlICMSubstituicaoTributaria(deCalculoFreteWebServiceRetorno.getVlICMSubstituicaoTributaria());
		paraCalculoFreteWebServiceRetorno.setVlImposto(deCalculoFreteWebServiceRetorno.getVlImposto());
		paraCalculoFreteWebServiceRetorno.setVlTotalCtrc(deCalculoFreteWebServiceRetorno.getVlTotalCtrc());
		paraCalculoFreteWebServiceRetorno.setVlTotalFrete(deCalculoFreteWebServiceRetorno.getVlTotalFrete());
		paraCalculoFreteWebServiceRetorno.setVlTotalServico(deCalculoFreteWebServiceRetorno.getVlTotalServico());
		
		if (deCalculoFreteWebServiceRetorno.getParcelas() != null) {
			deParaParcelas(deCalculoFreteWebServiceRetorno,	paraCalculoFreteWebServiceRetorno);
		}
		
		if (deCalculoFreteWebServiceRetorno.getServicosAdicionais() != null) {
			deParaServicos(deCalculoFreteWebServiceRetorno,	paraCalculoFreteWebServiceRetorno);
		}
		return paraCalculoFreteWebServiceRetorno;
	}

	private void deParaServicos(
			com.mercurio.lms.expedicao.model.CalculoFreteWebServiceRetorno deCalculoFreteWebServiceRetorno,
			CalculoFreteWebServiceRetorno paraCalculoFreteWebServiceRetorno) {
		List<ServicoAdicionalWebService> paraServicosAdicionais =  new ArrayList<ServicoAdicionalWebService>();
		
		for (com.mercurio.lms.expedicao.model.ServicoAdicionalWebService deServicoAdicionalWebService : deCalculoFreteWebServiceRetorno.getServicosAdicionais()) {
			ServicoAdicionalWebService paraServicosAdicional = new ServicoAdicionalWebService();
			
			paraServicosAdicional.setDsComplemento(deServicoAdicionalWebService.getDsComplemento());
			paraServicosAdicional.setNmServico(deServicoAdicionalWebService.getNmServico());
			paraServicosAdicional.setSgMoeda(deServicoAdicionalWebService.getSgMoeda());
			paraServicosAdicional.setVlServico(deServicoAdicionalWebService.getVlServico());
			
			paraServicosAdicionais.add(paraServicosAdicional);
		}
		
		paraCalculoFreteWebServiceRetorno.setServicosAdicionais(paraServicosAdicionais);
	}

	private void deParaParcelas(
			com.mercurio.lms.expedicao.model.CalculoFreteWebServiceRetorno deCalculoFreteWebServiceRetorno,
			CalculoFreteWebServiceRetorno paraCalculoFreteWebServiceRetorno) {
		List<ParcelasFreteWebService> paraParcelas =  new ArrayList<ParcelasFreteWebService>();
		
		for (com.mercurio.lms.expedicao.model.ParcelasFreteWebService deParcelasFreteWebService : deCalculoFreteWebServiceRetorno.getParcelas()) {
			paraParcelas.add(new ParcelasFreteWebService(deParcelasFreteWebService.getDsParcela(), deParcelasFreteWebService.getVlParcela()));
		}

		paraCalculoFreteWebServiceRetorno.setParcelas(paraParcelas);
	}

	private com.mercurio.lms.vendas.model.CotacaoWebService deParaCotacao(
			CotacaoWebService deCotacaoWebService) {
		com.mercurio.lms.vendas.model.CotacaoWebService paraCotacaoWebService = new com.mercurio.lms.vendas.model.CotacaoWebService();
		paraCotacaoWebService.setCdDivisaoCliente(deCotacaoWebService.getCdDivisaoCliente());
		paraCotacaoWebService.setCepDestino(deCotacaoWebService.getCepDestino());
		paraCotacaoWebService.setCepOrigem(deCotacaoWebService.getCepOrigem());
		paraCotacaoWebService.setLogin(deCotacaoWebService.getLogin());
		paraCotacaoWebService.setNrIdentifClienteDest(deCotacaoWebService.getNrIdentifClienteDest());
		paraCotacaoWebService.setNrIdentifClienteRem(deCotacaoWebService.getNrIdentifClienteRem());
		paraCotacaoWebService.setNrInscricaoEstadualDestinatario(deCotacaoWebService.getNrInscricaoEstadualDestinatario());
		paraCotacaoWebService.setNrInscricaoEstadualRemetente(deCotacaoWebService.getNrInscricaoEstadualRemetente());
		paraCotacaoWebService.setPsReal(deCotacaoWebService.getPsReal());
		paraCotacaoWebService.setSenha(deCotacaoWebService.getSenha());
		paraCotacaoWebService.setTpFrete(deCotacaoWebService.getTpFrete());
		paraCotacaoWebService.setTpPessoaDestinatario(deCotacaoWebService.getTpPessoaDestinatario());
		paraCotacaoWebService.setTpPessoaRemetente(deCotacaoWebService.getTpPessoaRemetente());
		paraCotacaoWebService.setTpServico(deCotacaoWebService.getTpServico());
		paraCotacaoWebService.setTpSituacaoTributariaDestinatario(deCotacaoWebService.getTpSituacaoTributariaDestinatario());
		paraCotacaoWebService.setTpSituacaoTributariaRemetente(deCotacaoWebService.getTpSituacaoTributariaRemetente());
		paraCotacaoWebService.setVlMercadoria(deCotacaoWebService.getVlMercadoria());
		return paraCotacaoWebService;
	}
}
