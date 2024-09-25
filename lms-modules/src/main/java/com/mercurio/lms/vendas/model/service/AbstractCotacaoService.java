package com.mercurio.lms.vendas.model.service;

import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;

public abstract class AbstractCotacaoService extends CrudService<Cotacao, Long> {
	protected EnderecoPessoaService enderecoPessoaService;
	protected InscricaoEstadualService inscricaoEstadualService;
	protected MunicipioService municipioService;
	protected ClienteService clienteService;
	protected ConfiguracoesFacade configuracoesFacade;
	protected PpeService ppeService;

	protected void validateDadosCliente(Cotacao cotacao, CalculoFrete calculoFrete) {
		Pessoa pessoa = null;
		Long idPessoa = null;
		EnderecoPessoa enderecoPessoa = null;

		DoctoServicoDadosCliente dadosCliente = calculoFrete.getDadosCliente();

		/** Valida Dados do REMETENTE */
		Cliente clienteRemetente = cotacao.getClienteByIdClienteSolicitou();
		if(clienteRemetente != null) {
			pessoa = clienteRemetente.getPessoa();
			idPessoa = clienteRemetente.getIdCliente();
			if(idPessoa != null) {
				if(pessoa != null && pessoa.getEnderecoPessoa() != null) {
					enderecoPessoa = pessoa.getEnderecoPessoa();
				} else {
					enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
				}
				dadosCliente.setIdClienteRemetente(idPessoa);
			}
		}

		/** 
		 * Valida Municipio de ORIGEM
		 * Regra: Quando remetente cadastrado, substitui o municipio de origem da cotação pelo municio do cliente remetente
		 * */
		Municipio municipio = cotacao.getMunicipioByIdMunicipioOrigem();
		if( municipio == null && enderecoPessoa != null) {
			municipio = enderecoPessoa.getMunicipio();
		}
		dadosCliente.setIdMunicipioRemetente(municipio.getIdMunicipio());
		if(municipio.getUnidadeFederativa() == null || municipio.getUnidadeFederativa().getIdUnidadeFederativa() == null) {
			municipio = municipioService.findById(municipio.getIdMunicipio());
		}
		dadosCliente.setIdUfRemetente(municipio.getUnidadeFederativa().getIdUnidadeFederativa());

		/** IE do Remetente */
		InscricaoEstadual inscricaoEstadual = cotacao.getInscricaoEstadualRemetente();
		if(idPessoa != null && (inscricaoEstadual == null || inscricaoEstadual.getIdInscricaoEstadual() == null)) {
			//Caso o cotacao nao possua IE, busca a Padrao do Cliente
			inscricaoEstadual = inscricaoEstadualService.findByPessoaIndicadorPadrao(idPessoa, Boolean.TRUE);
		}
		if(inscricaoEstadual != null) {
			dadosCliente.setIdInscricaoEstadualRemetente(inscricaoEstadual.getIdInscricaoEstadual());
		}

		/** Restrição ROTA ORIGEM */
		RestricaoRota restricaoRotaOrigem = calculoFrete.getRestricaoRotaOrigem();
		if( cotacao.getFilial() == null ){
		restricaoRotaOrigem.setIdFilial(SessionUtils.getFilialSessao().getIdFilial());
		}else{
			restricaoRotaOrigem.setIdFilial(cotacao.getFilial().getIdFilial());
		}
		if (cotacao.getFilialByIdFilialOrigem() != null) {
			restricaoRotaOrigem.setIdFilial(cotacao.getFilialByIdFilialOrigem().getIdFilial());
		}
		restricaoRotaOrigem.setIdZona(municipio.getUnidadeFederativa().getPais().getZona().getIdZona());
		restricaoRotaOrigem.setIdPais(municipio.getUnidadeFederativa().getPais().getIdPais());
		restricaoRotaOrigem.setIdUnidadeFederativa(municipio.getUnidadeFederativa().getIdUnidadeFederativa());
		restricaoRotaOrigem.setIdMunicipio(municipio.getIdMunicipio());

		if(cotacao.getAeroportoByIdAeroportoOrigem() != null) {
			restricaoRotaOrigem.setIdAeroporto(cotacao.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		}

		Long idTipoLocalizacao = calculoFrete.getDadosCliente().getIdTipoLocalizacaoOrigem();
		if(idTipoLocalizacao == null) {
			idTipoLocalizacao = findIdTipoLocalizacao(restricaoRotaOrigem.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepColeta());
		}
		
		Long idTipoLocalizacaoComercial = findIdTipoLocalizacaoComercial(restricaoRotaOrigem.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepColeta());
		
		restricaoRotaOrigem.setIdTipoLocalizacao(idTipoLocalizacao);
		restricaoRotaOrigem.setIdTipoLocalizacaoTarifa(idTipoLocalizacao);
		restricaoRotaOrigem.setIdTipoLocalizacaoComercial(idTipoLocalizacaoComercial);
		
		
		/** Valida Dados do DESTINATÁRIO */
		Cliente clienteDestinatario = cotacao.getClienteByIdClienteDestino();
		if(clienteDestinatario != null) {
			idPessoa = clienteDestinatario.getIdCliente();
			pessoa = clienteDestinatario.getPessoa();
			if(idPessoa != null) {
				if(pessoa != null && pessoa.getEnderecoPessoa() != null) {
					enderecoPessoa = pessoa.getEnderecoPessoa();
				} else {
					enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
				}
				dadosCliente.setIdClienteDestinatario(idPessoa);
			}
			dadosCliente.setBlDificuldadeEntregaDestinatario(clienteDestinatario.getBlDificuldadeEntrega());
			
			// Taxas de dificuldade de entrega
			dadosCliente.setBlAgendamentoEntregaDestinatario(clienteDestinatario.getBlAgendamentoEntrega());
			dadosCliente.setBlCustoDescargaDestinatario(clienteDestinatario.getBlCustoDescarga());
			dadosCliente.setBlPaletizacaoDestinatario(clienteDestinatario.getBlPaletizacao());
			dadosCliente.setBlVeiculoDedicadoDestinatario(clienteDestinatario.getBlVeiculoDedicado());
		}

		/** Valida Municipio de DESTINO */
		municipio = cotacao.getMunicipioByIdMunicipioDestino();
		if(municipio == null && enderecoPessoa != null) {
			municipio = enderecoPessoa.getMunicipio();
		}
		dadosCliente.setIdMunicipioDestinatario(municipio.getIdMunicipio());
		if(municipio.getUnidadeFederativa() == null) {
			municipio = municipioService.findById(municipio.getIdMunicipio());
		}
		dadosCliente.setIdUfDestinatario(municipio.getUnidadeFederativa().getIdUnidadeFederativa());

		/** IE Destinatario */
		inscricaoEstadual = cotacao.getInscricaoEstadualDestinatario();
		if(idPessoa != null && (inscricaoEstadual == null || inscricaoEstadual.getIdInscricaoEstadual() == null)) {
			//Caso o cotacao nao possua IE, busca a Padrao do Cliente
			inscricaoEstadual = inscricaoEstadualService.findByPessoaIndicadorPadrao(idPessoa, Boolean.TRUE);
		}
		if(inscricaoEstadual != null) {
			dadosCliente.setIdInscricaoEstadualDestinatario(inscricaoEstadual.getIdInscricaoEstadual());
		}

		/** Restrição ROTA DESTINO */

		RestricaoRota restricaoRotaDestino = calculoFrete.getRestricaoRotaDestino();
		if (cotacao.getFilialByIdFilialDestino() != null) {
			restricaoRotaDestino.setIdFilial(cotacao.getFilialByIdFilialDestino().getIdFilial());
		}
		
		restricaoRotaDestino.setIdZona(municipio.getUnidadeFederativa().getPais().getZona().getIdZona());
		restricaoRotaDestino.setIdPais(municipio.getUnidadeFederativa().getPais().getIdPais());
		restricaoRotaDestino.setIdUnidadeFederativa(municipio.getUnidadeFederativa().getIdUnidadeFederativa());
		restricaoRotaDestino.setIdMunicipio(municipio.getIdMunicipio());
		
		if(cotacao.getAeroportoByIdAeroportoDestino() != null) {
			restricaoRotaDestino.setIdAeroporto(cotacao.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		}

		idTipoLocalizacao = calculoFrete.getDadosCliente().getIdTipoLocalizacaoDestino();
		if(idTipoLocalizacao == null) {
			idTipoLocalizacao = findIdTipoLocalizacao(restricaoRotaDestino.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepEntrega());
		}
		
		idTipoLocalizacaoComercial = findIdTipoLocalizacaoComercial(restricaoRotaDestino.getIdMunicipio(), calculoFrete.getIdServico(), calculoFrete.getNrCepEntrega());
		
		restricaoRotaDestino.setIdTipoLocalizacao(idTipoLocalizacao);
		restricaoRotaDestino.setIdTipoLocalizacaoTarifa(idTipoLocalizacao);
		restricaoRotaDestino.setIdTipoLocalizacaoComercial(idTipoLocalizacaoComercial);
		
		/** Dados da Filial do USUÁRIO LOGADO */
		Filial filialUL = SessionUtils.getFilialSessao();
		idPessoa = filialUL.getIdFilial();
		enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
		dadosCliente.setIdUfFilialUsuario(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());

		/** Tipo Situacao Tributaria */
		dadosCliente.setTpSituacaoTributariaRemetente(cotacao.getTpSitTributariaRemetente().getValue());
		dadosCliente.setTpSituacaoTributariaDestinatario(cotacao.getTpSitTributariaDestinatario().getValue());
		dadosCliente.setTpSituacaoTributariaResponsavel(cotacao.getTpSitTributariaResponsavel().getValue());

		cotacao.setDadosCliente(dadosCliente);
		calculoFrete.setDadosCliente(dadosCliente);

		calculoFrete.setRestricaoRotaOrigem(restricaoRotaOrigem);
		calculoFrete.setRestricaoRotaDestino(restricaoRotaDestino);
	}

	private Long findIdTipoLocalizacao(Long idMunicipio, Long idServico, String nrCep) {
		Map<String, Object> atendimento = ppeService.findAtendimentoMunicipio(idMunicipio, idServico, Boolean.TRUE, null, nrCep, null, null, null, null, null, "N", null,null);
		return (Long) atendimento.get("idTipoLocalizacaoMunicipio");
	}
	
	private Long findIdTipoLocalizacaoComercial(Long idMunicipio, Long idServico, String nrCep) {
		Map<String, Object> atendimento = ppeService.findAtendimentoMunicipio(idMunicipio, idServico, Boolean.TRUE, null, nrCep, null, null, null, null, null, "N", null,null);
		return (Long) atendimento.get("idTipoLocalizacaoComercial");
	}
	
	protected void populateDadosCliente(Cotacao cotacao, CalculoFrete calculoFrete) {
		//Busca os dados dos clientes para o calculo
		DoctoServicoDadosCliente dadosCliente = cotacao.getDadosCliente();
		Long idPessoa = null;
		EnderecoPessoa enderecoPessoa = null;

		//*** Dados do Remetente
		if(cotacao.getClienteByIdClienteSolicitou() != null) {
			idPessoa = cotacao.getClienteByIdClienteSolicitou().getIdCliente();
			enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);

			dadosCliente.setIdClienteRemetente(idPessoa);
			dadosCliente.setIdMunicipioRemetente(enderecoPessoa.getMunicipio().getIdMunicipio());
			dadosCliente.setIdUfRemetente(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());

			InscricaoEstadual ieRemetente = inscricaoEstadualService.findByPessoaIndicadorPadrao(idPessoa, Boolean.TRUE);
			if (ieRemetente != null) {
				dadosCliente.setIdInscricaoEstadualRemetente(ieRemetente.getIdInscricaoEstadual());
			}
		}

		//*** Dados do Destinatário
		if(cotacao.getClienteByIdClienteDestino() != null) {
			idPessoa = cotacao.getClienteByIdClienteDestino().getIdCliente();
			enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);

			dadosCliente.setIdClienteDestinatario(idPessoa);
			dadosCliente.setIdMunicipioDestinatario(enderecoPessoa.getMunicipio().getIdMunicipio());
			dadosCliente.setIdUfDestinatario(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
			dadosCliente.setBlDificuldadeEntregaDestinatario(cotacao.getClienteByIdClienteDestino().getBlDificuldadeEntrega());

			// Taxas de dificuldade de entrega
			dadosCliente.setBlAgendamentoEntregaDestinatario(cotacao.getClienteByIdClienteDestino().getBlAgendamentoEntrega());
			dadosCliente.setBlCustoDescargaDestinatario(cotacao.getClienteByIdClienteDestino().getBlCustoDescarga());
			dadosCliente.setBlPaletizacaoDestinatario(cotacao.getClienteByIdClienteDestino().getBlPaletizacao());
			dadosCliente.setBlVeiculoDedicadoDestinatario(cotacao.getClienteByIdClienteDestino().getBlVeiculoDedicado());

			InscricaoEstadual ieDestinatario = inscricaoEstadualService.findByPessoaIndicadorPadrao(idPessoa, Boolean.TRUE);
			if (ieDestinatario != null) {
				dadosCliente.setIdInscricaoEstadualDestinatario(ieDestinatario.getIdInscricaoEstadual());
			}
		}

		//*** Dados da Filial do Usuário Logado
		Filial filialUL = SessionUtils.getFilialSessao();
		idPessoa = filialUL.getIdFilial();
		enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
		dadosCliente.setIdUfFilialUsuario(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());

		dadosCliente.setTpSituacaoTributariaRemetente(cotacao.getTpSitTributariaRemetente().getValue());
		dadosCliente.setTpSituacaoTributariaDestinatario(cotacao.getTpSitTributariaDestinatario().getValue());
		dadosCliente.setTpSituacaoTributariaResponsavel(cotacao.getTpSitTributariaResponsavel().getValue());

		calculoFrete.setDadosCliente(dadosCliente);
	}

	protected void validateAtendimentoEmpresaTipoFrete(Cotacao cotacao) {
		Cliente clienteRemetente = cotacao.getClienteByIdClienteSolicitou();
		if (clienteRemetente != null && Boolean.TRUE.equals(clienteRemetente.getBlAceitaFobGeral())) {
			return;
		}

		if(ConstantesExpedicao.TP_FRETE_FOB.equals(cotacao.getTpFrete().getValue())) {
			String valorParametro = (String)configuracoesFacade.getValorParametro("FILIAL_SEM_FOB");

			String strIdFilial = cotacao.getFilialByIdFilialDestino().getIdFilial().toString();
			String[] strIdsFilial = valorParametro.split(";");

        	for (int i = 0; i < strIdsFilial.length; i++) {
				if ( strIdsFilial[i].trim().equals(strIdFilial) ) {
					throw new BusinessException("LMS-04289");
	}
        	}
		}
	}
	
	protected void validateVolumes(Cotacao cotacao) {
		if (cotacao.getQtVolumes() == null || cotacao.getQtVolumes() <= 0) {
			throw new BusinessException("LMS-01207");
		}
	}
	
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
}