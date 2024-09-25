package com.mercurio.lms.pendencia.model.service;

import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.model.service.CalculoFreteService;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.pendencia.model.CalculoMda;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * @spring.bean id="lms.pendencia.calculoMdaService"
 */
public class CalculoMdaService {
	private CalculoFreteService calculoFreteService;
	private ClienteService clienteService;
	private FilialService filialService;
	private PpeService ppeService;
	private InscricaoEstadualService inscricaoEstadualService;

	public void executeCalculoMda(CalculoMda calculoMda) {
		Mda mda = calculoMda.getDoctoServico();

		CalculoFrete calculoFrete = new CalculoFrete();
		Conhecimento conhecimento = new Conhecimento();
		DoctoServicoDadosCliente dadosCliente = new DoctoServicoDadosCliente();

		calculoFrete.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_NORMAL);
		calculoFrete.setTpCalculo(ConstantesExpedicao.CALCULO_NORMAL);
		calculoFrete.setBlCalculaParcelas(Boolean.TRUE);
		calculoFrete.setBlCalculaServicosAdicionais(Boolean.FALSE);

		Servico servico = mda.getServico();
		calculoFrete.setIdServico(servico.getIdServico());
		calculoFrete.setTpModal(servico.getTpModal().getValue());
		calculoFrete.setTpAbrangencia(servico.getTpAbrangencia().getValue());

		conhecimento.setTpDocumentoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NACIONAL));
		conhecimento.setServico(servico);

		Filial filialOrigem = null;
		Municipio municipioOrigem = null;

		// Dados do Remetente
		Cliente clienteRemetente = mda.getClienteByIdClienteRemetente();
		if(clienteRemetente != null) {
			municipioOrigem = clienteRemetente.getPessoa().getEnderecoPessoa().getMunicipio();
			filialOrigem = getFilialAtendimento(municipioOrigem, servico);
		} else {
			filialOrigem = mda.getFilialByIdFilialOrigem();
			clienteRemetente = clienteService.findByIdInitLazyProperties(filialOrigem.getIdFilial(), false);
		}

		calculoFrete.getRestricaoRotaOrigem().setIdMunicipio(municipioOrigem.getIdMunicipio());
		calculoFrete.getRestricaoRotaOrigem().setIdFilial(filialOrigem.getIdFilial());

		dadosCliente.setIdClienteRemetente(clienteRemetente.getIdCliente());
		dadosCliente.setIdMunicipioRemetente(municipioOrigem.getIdMunicipio());
		dadosCliente.setIdUfRemetente(municipioOrigem.getUnidadeFederativa().getIdUnidadeFederativa());

		InscricaoEstadual inscricaoEstadualRemetente = inscricaoEstadualService.findByPessoaIndicadorPadrao(clienteRemetente.getIdCliente(), Boolean.TRUE);
		mda.setInscricaoEstadualRemetente(inscricaoEstadualRemetente);
		dadosCliente.setIdInscricaoEstadualRemetente(inscricaoEstadualRemetente.getIdInscricaoEstadual());

		// Dados do Destinatário
		Filial filialDestino = null;
		Municipio municipioDestino = null;

		Cliente clienteDestinatario = mda.getClienteByIdClienteDestinatario();
		if(clienteDestinatario != null) {
			municipioDestino = clienteDestinatario.getPessoa().getEnderecoPessoa().getMunicipio();
			filialDestino = getFilialAtendimento(municipioDestino, servico);
		} else {
			filialDestino = mda.getFilialByIdFilialDestino();
			clienteDestinatario = clienteService.findById(filialDestino.getIdFilial());
		}

		calculoFrete.getRestricaoRotaDestino().setIdMunicipio(municipioDestino.getIdMunicipio());
		calculoFrete.getRestricaoRotaDestino().setIdFilial(filialDestino.getIdFilial());

		dadosCliente.setIdClienteDestinatario(clienteDestinatario.getIdCliente());
		dadosCliente.setIdMunicipioDestinatario(municipioDestino.getIdMunicipio());
		dadosCliente.setIdUfDestinatario(municipioDestino.getUnidadeFederativa().getIdUnidadeFederativa());
		dadosCliente.setBlDificuldadeEntregaDestinatario(clienteDestinatario.getBlDificuldadeEntrega());

		// Taxas de dificuldade de entrega
		dadosCliente.setBlAgendamentoEntregaDestinatario(clienteDestinatario.getBlAgendamentoEntrega());
		dadosCliente.setBlCustoDescargaDestinatario(clienteDestinatario.getBlCustoDescarga());
		dadosCliente.setBlPaletizacaoDestinatario(clienteDestinatario.getBlPaletizacao());
		dadosCliente.setBlVeiculoDedicadoDestinatario(clienteDestinatario.getBlVeiculoDedicado());

		InscricaoEstadual inscricaoEstadualDestinatario = inscricaoEstadualService.findByPessoaIndicadorPadrao(clienteDestinatario.getIdCliente(), Boolean.TRUE);
		mda.setInscricaoEstadualDestinatario(inscricaoEstadualDestinatario);
		dadosCliente.setIdInscricaoEstadualDestinatario(inscricaoEstadualDestinatario.getIdInscricaoEstadual());

		//TODO: confirmar
		calculoFrete.setClienteBase(clienteRemetente);
		calculoFrete.setTpFrete(ConstantesExpedicao.TP_FRETE_CIF);
		calculoFrete.setIdDensidade(ConstantesExpedicao.ID_DENSIDADE_MERCURIO);

		calculoFrete.setPsRealInformado(mda.getPsReal());
		calculoFrete.setVlMercadoria(mda.getVlMercadoria());

		// Dados da Filial do Usuário Logado
		Filial filialSessao = SessionUtils.getFilialSessao();
		dadosCliente.setIdUfFilialUsuario(filialSessao.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());

		calculoFrete.setDadosCliente(dadosCliente);

		calculoFrete.setDoctoServico(conhecimento);

		calculoFreteService.executeCalculoCTRNormal(calculoFrete);

		CalculoFreteUtils.copyResult(mda, calculoFrete);
	}

	private Filial getFilialAtendimento(Municipio municipio, Servico servico) {
		Map atendimento = ppeService.findAtendimentoMunicipio(
				municipio.getIdMunicipio(),
				servico.getIdServico(),
				Boolean.FALSE,
				JTDateTimeUtils.getDataAtual(),
				null,
				null, 
				null,
				null,
				null,
				null,
				"N" ,
				null,
				null
			);
		return filialService.findById((Long)atendimento.get("idFilial"));
	}

	public void setCalculoFreteService(CalculoFreteService calculoFreteService) {
		this.calculoFreteService = calculoFreteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

}
