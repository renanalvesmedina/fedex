package com.mercurio.lms.expedicao.model.service;

import static java.lang.Boolean.FALSE;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.hibernate.Hibernate;
import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.expedicao.DocumentoServicoFacade;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoServico;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.service.McdService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.MunicipioGrupoRegiaoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;
import com.mercurio.lms.tributos.model.service.BuscarCfopService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ObservacaoConhecimento;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.PromotorCliente;
import com.mercurio.lms.vendas.model.SegmentoMercado;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.model.service.PromotorClienteService;
import com.mercurio.lms.vendas.util.ClienteUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;

public abstract class AbstractConhecimentoService {
	protected DocumentoServicoFacade documentoServicoFacade;
	protected ConfiguracoesFacade configuracoesFacade;
	protected TipoCustoService tipoCustoService;
	protected EnderecoPessoaService enderecoPessoaService;
	protected DevedorDocServService devedorDocServService;
	protected ClienteService clienteService;
	protected BuscarCfopService buscarCfopService;
	protected RotaColetaEntregaService rotaColetaEntregaService;
	protected McdService mcdService;
	protected PpeService ppeService;
	protected UnidadeFederativaService unidadeFederativaService;
	protected DivisaoClienteService divisaoClienteService;
	protected ConhecimentoService conhecimentoService;
	protected CalculoFreteService calculoFreteService;
	protected TabelaPrecoService tabelaPrecoService;
	protected PromotorClienteService promotorClienteService;
	protected MunicipioGrupoRegiaoService municipioGrupoRegiaoService;

	protected abstract void executeCalculo(Conhecimento conhecimento, CalculoFrete calculoFrete);

	protected abstract void validateCotacaoByCTRC(Conhecimento conhecimento, CalculoFrete calculoFrete);

	protected Filial obtemFilial(DoctoServicoDadosCliente dsc){
		if(dsc.getFilialTransacao() == null){
			return SessionUtils.getFilialSessao();
		}else{
			return dsc.getFilialTransacao();
		}
	}

	/**
	 * Dados do Cliente para Calculo do Frete e Tributacao.
	 *
	 * @param conhecimento
	 * @param calculoFrete
	 * @author Andre Valadas
	 */
	protected void validateDadosCliente(Conhecimento conhecimento, CalculoFrete calculoFrete) {
		calculoFrete.setDoctoServico(conhecimento);

		DoctoServicoDadosCliente dadosCliente = conhecimento.getDadosCliente();
		Pessoa pessoa = null;
		Long idPessoa = null;

		//*** Dados do Remetente
		idPessoa = conhecimento.getClienteByIdClienteRemetente().getIdCliente();
		pessoa = conhecimento.getClienteByIdClienteRemetente().getPessoa();
		EnderecoPessoa enderecoPessoa = getEnderecoPessoaParaValidacaoDadosCliente(pessoa, idPessoa);

		dadosCliente.setIdClienteRemetente(idPessoa);
		dadosCliente.setIdMunicipioRemetente(enderecoPessoa.getMunicipio().getIdMunicipio());
		dadosCliente.setIdUfRemetente(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
		//Busca IE do Conhecimento
		if (conhecimento.getInscricaoEstadualRemetente() != null) {
			dadosCliente.setIdInscricaoEstadualRemetente(conhecimento.getInscricaoEstadualRemetente().getIdInscricaoEstadual());
		}
		//Caso o Conhecimento nao possua IE, busca a Padrao do Cliente
		if (dadosCliente.getIdInscricaoEstadualRemetente() == null) {
			InscricaoEstadual inscricaoEstadual = getInscricaoEstadualService().findByPessoaIndicadorPadrao(idPessoa, Boolean.TRUE);
			if (inscricaoEstadual != null) {
				dadosCliente.setIdInscricaoEstadualRemetente(inscricaoEstadual.getIdInscricaoEstadual());
			}
		}
		//*** Restrição Rota Origem
		RestricaoRota restricaoRotaOrigem = calculoFrete.getRestricaoRotaOrigem();
		restricaoRotaOrigem.setIdFilial(obtemFilial(conhecimento.getDadosCliente()).getIdFilial());
		if (conhecimento.getFilialByIdFilialOrigem() != null) {
			restricaoRotaOrigem.setIdFilial(conhecimento.getFilialByIdFilialOrigem().getIdFilial());
		}
		Municipio municipioOrigem  = conhecimento.getMunicipioByIdMunicipioColeta();
		if (municipioOrigem == null) {
			municipioOrigem = enderecoPessoa.getMunicipio();
		}
		restricaoRotaOrigem.setIdMunicipio(municipioOrigem.getIdMunicipio());
		if(conhecimento.getAeroportoByIdAeroportoOrigem() != null) {
			restricaoRotaOrigem.setIdAeroporto(conhecimento.getAeroportoByIdAeroportoOrigem().getIdAeroporto());
		}

		//*** Dados do Destinatário
		Cliente clienteDestinatario = conhecimento.getClienteByIdClienteDestinatario();
		idPessoa = clienteDestinatario.getIdCliente();
		Long idEnderecoPessoa = clienteDestinatario.getPessoa().getEnderecoPessoa().getIdEnderecoPessoa();

		enderecoPessoa = enderecoPessoaService.findById(idEnderecoPessoa);

		enderecoPessoaService.attach(enderecoPessoa);

		dadosCliente.setIdClienteDestinatario(idPessoa);
		dadosCliente.setIdMunicipioDestinatario(enderecoPessoa.getMunicipio().getIdMunicipio());
		dadosCliente.setIdUfDestinatario(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
		dadosCliente.setBlDificuldadeEntregaDestinatario(clienteDestinatario.getBlDificuldadeEntrega());
		conhecimento.setClienteByIdClienteDestinatario(clienteDestinatario);

		// Taxas de dificuldade de entrega
		dadosCliente.setBlAgendamentoEntregaDestinatario(clienteDestinatario.getBlAgendamentoEntrega());
		dadosCliente.setBlCustoDescargaDestinatario(clienteDestinatario.getBlCustoDescarga());
		dadosCliente.setBlPaletizacaoDestinatario(clienteDestinatario.getBlPaletizacao());
		dadosCliente.setBlVeiculoDedicadoDestinatario(clienteDestinatario.getBlVeiculoDedicado());

		//Busca IE do Conhecimento
		if (conhecimento.getInscricaoEstadualDestinatario() != null) {
			dadosCliente.setIdInscricaoEstadualDestinatario(conhecimento.getInscricaoEstadualDestinatario().getIdInscricaoEstadual());
		}
		//Caso o Conhecimento nao possua IE, busca a Padrao do Cliente
		if (dadosCliente.getIdInscricaoEstadualDestinatario() == null) {
			InscricaoEstadual inscricaoEstadual = getInscricaoEstadualService().findByPessoaIndicadorPadrao(idPessoa, Boolean.TRUE);
			if (inscricaoEstadual != null) {
				dadosCliente.setIdInscricaoEstadualDestinatario(inscricaoEstadual.getIdInscricaoEstadual());
			}
		}
		//*** Restrição Rota Destino
		RestricaoRota restricaoRotaDestino = calculoFrete.getRestricaoRotaDestino();
		Municipio municipioDestino  = conhecimento.getMunicipioByIdMunicipioEntrega();
		if (municipioDestino == null) {
			municipioDestino = enderecoPessoa.getMunicipio();
		}
		restricaoRotaDestino.setIdMunicipio(municipioDestino.getIdMunicipio());

		if (conhecimento.getFilialByIdFilialDestino() != null) {
			/*
			 * Adicionado esse codigo para buscar a filial original sem a filial substituta, pois o
			 * conhecimento.getFilialByIdFilialDestino() já está com a filia a ser desviada
			 * */
			Long idClienteAtendido = conhecimento.getClienteByIdClienteDestinatario().getIdCliente();
			SegmentoMercado segmentoMercado = clienteService.findSegmentoMercadoByIdCliente(idClienteAtendido);
			Filial filial = obtemFilial(conhecimento.getDadosCliente());
			Municipio municipioFilialOrigem = filial.getPessoa().getEnderecoPessoa().getMunicipio();
			Map atendimento = ppeService.findAtendimentoMunicipio(
					municipioDestino.getIdMunicipio(),
					conhecimento.getServico().getIdServico(),
					Boolean.FALSE,
					JTDateTimeUtils.getDataAtual(),
					conhecimento.getNrCepEntrega(),
					idClienteAtendido,
					conhecimento.getClienteByIdClienteRemetente().getIdCliente(),
					segmentoMercado.getIdSegmentoMercado(),
					municipioFilialOrigem.getUnidadeFederativa().getIdUnidadeFederativa(),
					filial.getIdFilial(),
					"N",
					conhecimento.getNaturezaProduto().getIdNaturezaProduto(),
					null
			);
			restricaoRotaDestino.setIdFilial(MapUtils.getLong(atendimento,"idFilial"));
		}

		if(conhecimento.getAeroportoByIdAeroportoDestino() != null) {
			restricaoRotaDestino.setIdAeroporto(conhecimento.getAeroportoByIdAeroportoDestino().getIdAeroporto());
		}

		//*** Dados do Consignatário
		if (conhecimento.getClienteByIdClienteConsignatario() != null) {
			idPessoa = conhecimento.getClienteByIdClienteConsignatario().getIdCliente();
			enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
			dadosCliente.setIdUfConsignatario(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
		}

		//*** Dados do Redespacho
		if (conhecimento.getClienteByIdClienteRedespacho() != null) {
			idPessoa = conhecimento.getClienteByIdClienteRedespacho().getIdCliente();
			enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
			dadosCliente.setIdUfRedespacho(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
		}

		//*** Dados da Filial do Usuário Logado
		enderecoPessoa = obtemFilial(conhecimento.getDadosCliente()).getPessoa().getEnderecoPessoa();
		dadosCliente.setIdUfFilialUsuario(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());

		dadosCliente.setNrCtrcSubcontratante(conhecimento.getNrCtrcSubcontratante());

		conhecimento.setDadosCliente(dadosCliente);
		calculoFrete.setDadosCliente(dadosCliente);

		calculoFrete.setRestricaoRotaOrigem(restricaoRotaOrigem);
		calculoFrete.setRestricaoRotaDestino(restricaoRotaDestino);
	}

	private EnderecoPessoa getEnderecoPessoaParaValidacaoDadosCliente(Pessoa pessoa, Long idCliente) {
		if(pessoa != null) {
			return pessoa.getEnderecoPessoa();
		}

		return enderecoPessoaService.findEnderecoPessoaPadrao(idCliente);
	}

	/**
	 * Dados do Cliente para Conhecimento.
	 *
	 * @param conhecimento
	 * @author Cleveland Júnior Soares
	 */
	protected void validateDadosCliente(Conhecimento conhecimento) {
		DoctoServicoDadosCliente dadosCliente = conhecimento.getDadosCliente();
		Pessoa pessoa = null;
		Long idPessoa = null;

		//*** Dados do Remetente
		idPessoa = conhecimento.getClienteByIdClienteRemetente().getIdCliente();
		pessoa = conhecimento.getClienteByIdClienteRemetente().getPessoa();
		EnderecoPessoa enderecoPessoa = this.getEnderecoPessoaParaValidacaoDadosCliente(pessoa, idPessoa);

		if(enderecoPessoa == null) {
			throw new BusinessException("LMS-01194", new Object[]{ pessoa != null? pessoa.getNrIdentificacao() : "" });
		}

		dadosCliente.setIdClienteRemetente(idPessoa);
		dadosCliente.setIdMunicipioRemetente(enderecoPessoa.getMunicipio().getIdMunicipio());
		dadosCliente.setIdUfRemetente(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
		//Busca IE do Conhecimento
		if (conhecimento.getInscricaoEstadualRemetente() != null) {
			dadosCliente.setIdInscricaoEstadualRemetente(conhecimento.getInscricaoEstadualRemetente().getIdInscricaoEstadual());
		}
		//Caso o Conhecimento nao possua IE, busca a Padrao do Cliente
		if (dadosCliente.getIdInscricaoEstadualRemetente() == null) {
			InscricaoEstadual inscricaoEstadual = getInscricaoEstadualService().findByPessoaIndicadorPadrao(idPessoa, Boolean.TRUE);
			if (inscricaoEstadual != null) {
				dadosCliente.setIdInscricaoEstadualRemetente(inscricaoEstadual.getIdInscricaoEstadual());
			}
		}

		//*** Dados do Destinatário
		Cliente clienteDestinatario = conhecimento.getClienteByIdClienteDestinatario();
		idPessoa = clienteDestinatario.getIdCliente();
		Long idEnderecoPessoa = clienteDestinatario.getPessoa().getEnderecoPessoa().getIdEnderecoPessoa();

		enderecoPessoa = enderecoPessoaService.findById(idEnderecoPessoa);

		if(enderecoPessoa == null) {
			throw new BusinessException("LMS-01194", new Object[]{ clienteDestinatario.getPessoa().getNrIdentificacao() });
		}
		enderecoPessoaService.attach(enderecoPessoa);

		dadosCliente.setIdClienteDestinatario(idPessoa);
		dadosCliente.setIdMunicipioDestinatario(enderecoPessoa.getMunicipio().getIdMunicipio());
		dadosCliente.setIdUfDestinatario(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
		dadosCliente.setBlDificuldadeEntregaDestinatario(clienteDestinatario.getBlDificuldadeEntrega());
		conhecimento.setClienteByIdClienteDestinatario(clienteDestinatario);

		// Taxas de dificuldade de entrega
		dadosCliente.setBlAgendamentoEntregaDestinatario(clienteDestinatario.getBlAgendamentoEntrega());
		dadosCliente.setBlCustoDescargaDestinatario(clienteDestinatario.getBlCustoDescarga());
		dadosCliente.setBlPaletizacaoDestinatario(clienteDestinatario.getBlPaletizacao());
		dadosCliente.setBlVeiculoDedicadoDestinatario(clienteDestinatario.getBlVeiculoDedicado());

		//Busca IE do Conhecimento
		if (conhecimento.getInscricaoEstadualDestinatario() != null) {
			dadosCliente.setIdInscricaoEstadualDestinatario(conhecimento.getInscricaoEstadualDestinatario().getIdInscricaoEstadual());
		}
		//Caso o Conhecimento nao possua IE, busca a Padrao do Cliente
		if (dadosCliente.getIdInscricaoEstadualDestinatario() == null) {
			InscricaoEstadual inscricaoEstadual = getInscricaoEstadualService().findByPessoaIndicadorPadrao(idPessoa, Boolean.TRUE);
			if (inscricaoEstadual != null) {
				dadosCliente.setIdInscricaoEstadualDestinatario(inscricaoEstadual.getIdInscricaoEstadual());
			}
		}

		//*** Dados do Consignatário
		if (conhecimento.getClienteByIdClienteConsignatario() != null) {
			idPessoa = conhecimento.getClienteByIdClienteConsignatario().getIdCliente();
			enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
			dadosCliente.setIdUfConsignatario(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
		}

		//*** Dados do Redespacho
		if (conhecimento.getClienteByIdClienteRedespacho() != null) {
			idPessoa = conhecimento.getClienteByIdClienteRedespacho().getIdCliente();
			enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
			dadosCliente.setIdUfRedespacho(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
		}

		//*** Dados da Filial do Usuário Logado
		Filial filialSessao = obtemFilial(dadosCliente);
		enderecoPessoa = filialSessao.getPessoa().getEnderecoPessoa();
		dadosCliente.setIdUfFilialUsuario(enderecoPessoa.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());

		dadosCliente.setNrCtrcSubcontratante(conhecimento.getNrCtrcSubcontratante());

		conhecimento.setDadosCliente(dadosCliente);
	}

	/**
	 * Regra ValidaCTRC.4
	 * Definição do Cliente base para cálculo do Frete:
	 *	-	Utilizar como cliente para base de cálculo o cliente indicado como Responsável do Frete, 
	 *		salvo nas exceções abaixo:
	 *			-	Se o Tipo do Frete for "FOB" (F) e o campo Responsável estiver preenchido 
	 *				com "Destinatário" (D), verificar se o remetente é um cliente especial 
	 *				(Cliente.tpCliente = "S") e tem Frete FOB Dirigido indicado (Cliente.blFobDirigido = true), 
	 *				se tiver, utilizar como cliente para base do cálculo do frete o cliente Remetente;
	 *			-	Se o Tipo do Frete for "CIF" e o campo Responsável estiver preenchido 
	 *				com "Outro Responsável" (O) e este é um cliente especial (Cliente.tpCliente = "S") e 
	 *				tem Frete FOB Dirigido indicado (Cliente.blFobDirigido = true), verificar se 
	 *				o cliente Remetente é um cliente especial (Cliente.tpCliente = "S"), se for 
	 *				utilizar como cliente para base do cálculo do frete o cliente Remetente.
	 * <p>
	 * autor Julio Cesar Fernandes Corrêa
	 *
	 * @param calculoServico
	 * @param conhecimento
	 * @since 02/12/2005
	 */
	protected void setClienteBaseCalculoFrete(CalculoServico calculoServico, Conhecimento conhecimento) {
		final Cliente remetente = clienteService.findByIdInitLazyProperties(conhecimento.getClienteByIdClienteRemetente().getIdCliente(), false);
		Hibernate.initialize(remetente.getPessoa().getEnderecoPessoa());
		conhecimento.setClienteByIdClienteRemetente(remetente);

		final Cliente destinatario = clienteService.findByIdInitLazyProperties(conhecimento.getClienteByIdClienteDestinatario().getIdCliente(), false);
		Hibernate.initialize(destinatario.getPessoa().getEnderecoPessoa());
		conhecimento.setClienteByIdClienteDestinatario(destinatario);

		final Cliente responsavel = clienteService.findByIdInitLazyProperties(((DevedorDocServ) conhecimento.getDevedorDocServs().get(0)).getCliente().getIdCliente(), false);
		Hibernate.initialize(responsavel.getPessoa().getEnderecoPessoa());
		((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).setCliente(responsavel);

		Cliente clienteBaseCalculo = buildClienteBaseCalculo(conhecimento);
		conhecimento.setClienteByIdClienteBaseCalculo(clienteBaseCalculo);
		calculoServico.setClienteBase(clienteBaseCalculo);
	}

	private Cliente buildClienteBaseCalculo(Conhecimento conhecimento) {
		if (conhecimento.getClienteByIdClienteBaseCalculo() != null) {
			return conhecimento.getClienteByIdClienteBaseCalculo();
		}

		return this.getClienteBaseCalculo(conhecimento);
	}

	private Long getAeroporto(Filial filial, String tpModal) {
		if (ConstantesExpedicao.MODAL_AEREO.equals(tpModal)) {
			Map<String, Object> aeroporto = calculoFreteService.findAeroportoByFilial(filial.getIdFilial());
			if (aeroporto != null && aeroporto.get("idAeroporto") != null) {
				return (Long) aeroporto.get("idAeroporto");
			}
		}
		return null;
	}

	protected Cliente getClienteBaseCalculo(Conhecimento conhecimento) {
		Cliente clienteBase = null;

		String tpDevedorFrete = conhecimento.getTpDevedorFrete().getValue();

		String tpModal = conhecimento.getServico().getTpModal().getValue();
		Cliente clienteResponsavel = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente();
		Cliente clienteRemetente = clienteService.findByIdInitLazyProperties(conhecimento.getClienteByIdClienteRemetente().getIdCliente(), false);
		if (ConstantesExpedicao.TP_FRETE_FOB.equals(conhecimento.getTpFrete().getValue())) {
//			LMSA-685
			clienteBase = getClienteBaseByTpFreteFOB(conhecimento, clienteBase,tpDevedorFrete, tpModal, clienteResponsavel, clienteRemetente);
		} else if (ConstantesExpedicao.TP_FRETE_CIF.equals(conhecimento.getTpFrete().getValue())) {
			clienteBase = getClienteBaseByTpDevedorOutroResponsavel(clienteBase, tpDevedorFrete, tpModal, clienteResponsavel, clienteRemetente);
		}

		Cliente clienteDestinatario = clienteService.findByIdInitLazyProperties(conhecimento.getClienteByIdClienteDestinatario().getIdCliente(), false);
		clienteBase = getClienteBaseWhenTpDevedorIsDestinatarioOrRemetente(clienteBase, tpModal, tpDevedorFrete, clienteResponsavel, clienteRemetente, clienteDestinatario);

		if (clienteBase != null) {
			conhecimento.getDadosCliente().setBlFobDirigido(Boolean.TRUE);
			return clienteBase;
		}
		return clienteResponsavel;
	}

	private Cliente getClienteBaseByTpDevedorOutroResponsavel(Cliente clienteBase, String tpDevedorFrete, String tpModal, Cliente clienteResponsavel, Cliente clienteRemetente) {
		if(ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(tpDevedorFrete) &&
				ClienteUtils.isParametroClienteEspecial(clienteResponsavel.getTpCliente().getValue()) &&
				ClienteUtils.isClienteFobDirigidoRodoOrAereo(tpModal, clienteResponsavel.getBlFobDirigido(), clienteResponsavel.getBlFobDirigidoAereo()) &&
				ClienteUtils.isParametroClienteEspecial(clienteRemetente.getTpCliente().getValue())) {
			clienteBase = clienteRemetente;
		}
		return clienteBase;
	}

	private Cliente getClienteBaseByTpFreteFOB(Conhecimento conhecimento, Cliente clienteBase, String tpDevedorFrete, String tpModal, Cliente clienteResponsavel, Cliente clienteRemetente) {
		if (ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(tpDevedorFrete)) {
			Cliente clienteMatrizOrResponsavel = ClienteUtils.getClienteTpClienteFilialClienteEspecial(clienteResponsavel);

			if (ClienteUtils.isParametroClienteEspecial(clienteRemetente.getTpCliente().getValue()) &&
					ClienteUtils.isClienteFobDirigidoRodoOrAereo(tpModal, clienteRemetente.getBlFobDirigido(), clienteRemetente.getBlFobDirigidoAereo())) {
				String tpCliente = clienteResponsavel.getTpCliente().getValue();
				List<TabelaPreco> listTabelaPreco = tabelaPrecoService.findTabelaPrecoByIdCliente(clienteMatrizOrResponsavel.getIdCliente(), ConstantesTabelaPrecos.TP_TABELA_PRECO_REFERENCIAL_TNT, ConstantesTabelaPrecos.SUB_TP_TABELA_FOB, JTDateTimeUtils.getDataHoraAtual());
				List<PromotorCliente> listPromotorCliente = promotorClienteService.findByIdClienteAndTpModalAndDtAtual(clienteResponsavel.getIdCliente(), tpModal, JTDateTimeUtils.getDataHoraAtual());
				if ((ClienteUtils.isClienteEventualOrPotencial(tpCliente) || (ClienteUtils.isParametroClienteEspecial(tpCliente) && !CollectionUtils.isEmpty(listTabelaPreco)))
						&& !CollectionUtils.isEmpty(listPromotorCliente)) {
					clienteBase = clienteResponsavel;
				} else {
//						LMSA-691
					List<TabelaPreco> listTabelaPrecoD = tabelaPrecoService.findTabelaPrecoByIdCliente(clienteMatrizOrResponsavel.getIdCliente(), ConstantesTabelaPrecos.TP_TABELA_PRECO_DIFERENCIADA, null, JTDateTimeUtils.getDataHoraAtual());
					ParametroCliente paramCliente = getParametroCliente(conhecimento, clienteMatrizOrResponsavel);
					if (CollectionUtils.isEmpty(listTabelaPrecoD) && paramCliente == null) {
						clienteBase = clienteRemetente;
					}
				}
			}
		}
		return clienteBase;
	}

	private ParametroCliente getParametroCliente(Conhecimento conhecimento, Cliente cliente) {
		ParametroCliente paramCliente = null;
		Long idCliente = cliente.getIdCliente();
		List<DivisaoCliente> l = divisaoClienteService.findByIdClienteAndTpModal(idCliente, conhecimento.getServico().getTpModal().getValue());

		if (!CollectionUtils.isEmpty(l)) {
			for (DivisaoCliente divisaoCliente : l) {
				CalculoFrete calculoFrete = populateCalculoFrete(conhecimento, divisaoCliente);
				Long idDivisaoCliente = divisaoCliente.getIdDivisaoCliente();
				paramCliente = calculoFreteService.findParametroCliente(
						idDivisaoCliente,
						conhecimento.getServico().getIdServico(),
						calculoFrete.getRestricaoRotaOrigem(),
						calculoFrete.getRestricaoRotaDestino());

				if (paramCliente != null ) {
					break;
				}
			}
		}

		return paramCliente;
	}

	private CalculoFrete populateCalculoFrete(Conhecimento conhecimento, DivisaoCliente divisaoCliente) {
		TabelaPreco tp = tabelaPrecoService.findTabelaPrecoByIdDivisaoCliente(divisaoCliente.getIdDivisaoCliente(), conhecimento.getServico().getIdServico());

		Long idMunicipioEntrega = conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio();
		Long idServico = conhecimento.getServico().getIdServico();
		Long idClienteAtendido = conhecimento.getClienteByIdClienteDestinatario().getIdCliente();
		Long idClienteRemetente = conhecimento.getClienteByIdClienteRemetente().getIdCliente();
		SegmentoMercado segmentoMercado = clienteService.findSegmentoMercadoByIdCliente(idClienteAtendido);
		Filial filial = obtemFilial(conhecimento.getDadosCliente());
		Municipio municipioFilialOrigem = filial.getPessoa().getEnderecoPessoa().getMunicipio();
		Long idUfFilialOrigem = municipioFilialOrigem.getUnidadeFederativa().getIdUnidadeFederativa();

		Map<String, Object> result = ppeService.getFilialAtendimento(
				idMunicipioEntrega,
				idServico,
				Boolean.FALSE,
				JTDateTimeUtils.getDataAtual(),
				conhecimento.getNrCepEntrega(),
				idClienteAtendido,
				idClienteRemetente,
				segmentoMercado.getIdSegmentoMercado(),
				idUfFilialOrigem,
				filial.getIdFilial(),
				"N");

		CalculoFrete calculoFrete = new CalculoFrete();

		calculoFrete.setTpCalculo(conhecimento.getTpCalculoPreco().getValue());
		calculoFrete.setTpConhecimento(conhecimento.getTpConhecimento().getValue());
		calculoFrete.setIdServico(idServico);
		calculoFrete.setNrCepColeta(conhecimento.getMunicipioByIdMunicipioColeta().getNrCep());
		calculoFrete.setNrCepEntrega(conhecimento.getMunicipioByIdMunicipioEntrega().getNrCep());

		calculoFrete.setRestricaoRotaOrigem(new RestricaoRota());
		calculoFrete.getRestricaoRotaOrigem().setIdMunicipio(conhecimento.getMunicipioByIdMunicipioColeta().getIdMunicipio());
		calculoFrete.getRestricaoRotaOrigem().setIdFilial(conhecimento.getFilialByIdFilialOrigem().getIdFilial());
		calculoFrete.getRestricaoRotaOrigem().setIdGrupoRegiao(getIdGrupoRegiao(tp, conhecimento.getMunicipioByIdMunicipioColeta()));
		calculoFrete.getRestricaoRotaOrigem().setIdAeroporto(getAeroporto(conhecimento.getFilialByIdFilialOrigem(), conhecimento.getServico().getTpModal().getValue()));

		calculoFrete.setRestricaoRotaDestino(new RestricaoRota());
		calculoFrete.getRestricaoRotaDestino().setIdMunicipio(idMunicipioEntrega);
		calculoFrete.getRestricaoRotaDestino().setIdFilial(LongUtils.getLong(result.get("idFilial")));
		calculoFrete.getRestricaoRotaDestino().setIdGrupoRegiao(getIdGrupoRegiao(tp, conhecimento.getMunicipioByIdMunicipioEntrega()));
		calculoFrete.getRestricaoRotaDestino().setIdAeroporto(getAeroporto(conhecimento.getFilialByIdFilialDestino(), conhecimento.getServico().getTpModal().getValue()));

		calculoFreteService.setParametrosCalculoCommon(calculoFrete);

		return calculoFrete;
	}

	private Long getIdGrupoRegiao(TabelaPreco tabelaPreco, Municipio municipio) {
		if ((tabelaPreco != null && tabelaPreco.getIdTabelaPreco() != null) &&
				(municipio != null && municipio.getIdMunicipio() != null)) {

			GrupoRegiao grupoRegiao = municipioGrupoRegiaoService.findGrupoRegiaoAtendimento(tabelaPreco.getIdTabelaPreco(), municipio.getIdMunicipio());
			if (grupoRegiao != null) {
				return grupoRegiao.getIdGrupoRegiao();
			}
		}

		return null;
	}

	private Cliente getClienteBaseWhenTpDevedorIsDestinatarioOrRemetente(Cliente clienteBase, String tpModal, String tpDevedorFrete, Cliente clienteResponsavel, Cliente clienteRemetente, Cliente clienteDestinatario) {
		if (FALSE.equals(clienteRemetente.getBlBaseCalculo()) &&
				FALSE.equals(ClienteUtils.isClienteFobDirigidoRodoOrAereo(tpModal, clienteRemetente.getBlFobDirigido(), clienteRemetente.getBlFobDirigidoAereo()))) {

			if (ConstantesExpedicao.TP_DEVEDOR_REMETENTE.equals(tpDevedorFrete) && !clienteResponsavel.getIdCliente().equals(clienteRemetente.getIdCliente())) {
				return clienteRemetente;
			} else if (ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(tpDevedorFrete) &&
					!clienteResponsavel.getIdCliente().equals(clienteDestinatario.getIdCliente())) {
				return clienteDestinatario;
			}
		}

		return clienteBase;
	}

	/**
	 * Seta o cliente base para cálculo do frete Cotacao
	 *
	 * @param calculoServico
	 * @param conhecimento
	 */
	protected void setClienteBaseCalculoFreteCotacao(CalculoServico calculoServico, Conhecimento conhecimento) {
		//setClienteBaseCalculoFrete(calculoServico, conhecimento); LMSA-4283
		Cliente clienteDevedor = null;
		if (ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(conhecimento.getTpDevedorFrete().getValue())){

			clienteDevedor = clienteService.findById(conhecimento.getClienteByIdClienteDestinatario().getIdCliente());
			conhecimento.setClienteByIdClienteDestinatario(clienteDevedor);
		}else{
			clienteDevedor = clienteService.findById(conhecimento.getClienteByIdClienteRemetente().getIdCliente());
			conhecimento.setClienteByIdClienteRemetente(clienteDevedor);
		}
		calculoServico.setClienteBase(clienteDevedor);
	}

	/**
	 * Método que chama rotina para localizar o número do cfop.
	 * <p>
	 * São utilizados o seguintes parâmetros do docto serviço: - ID da unidade
	 * federativa do rementente - ID da unidade federativa da filial do usuário
	 * logado - ID da unidade federativa do destinatário - ID da unidade
	 * federativa do consignatário - ID do cliente redespacho - ID do tomador do
	 * serviço (do remetente se o tipo do frete for CIF ou do destintário se o
	 * tipo do frete for FOB). - ID da inscrição estadual do tomador do serviço -
	 * Tipo do conhecimento - Indicador de que o conhecimento possui notas
	 * fiscais.
	 * <p>
	 * autor Julio Cesar Fernandes Corrêa 26/12/2005
	 *
	 * @param conhecimento
	 */
	protected void setNrCfop(Conhecimento conhecimento) {
		DoctoServicoDadosCliente dc = conhecimento.getDadosCliente();

		DevedorDocServ devedorDocServ = getDevedorDocServ(conhecimento);

		String tpCtrcParceria = null;
		if (conhecimento.getTpCtrcParceria() != null) {
			tpCtrcParceria = conhecimento.getTpCtrcParceria().getValue();
		}

		Long idUfDestino = dc.getIdUfDestinatario();
		Municipio municipioEntrega = conhecimento.getMunicipioByIdMunicipioEntrega();
		if (municipioEntrega != null) {
			idUfDestino = municipioEntrega.getUnidadeFederativa().getIdUnidadeFederativa();
		}

		Long idUFFilialOrigemDocumento = conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
		Long idUFMunicipioColeta = conhecimento.getMunicipioByIdMunicipioColeta().getUnidadeFederativa().getIdUnidadeFederativa();
		
		/*FIXME - Os 3 ultimos nulos devem ser preenchidos com os valores corretos para atender
		as novas regras do CFOP*/
		String nrCfop = buscarCfopService.findCFOP(
				dc.getIdUfRemetente()
				,idUFFilialOrigemDocumento
				,dc.getIdUfDestinatario()
				,idUfDestino
				,dc.getIdUfConsignatario()
				,tpCtrcParceria
				,devedorDocServ.getCliente().getIdCliente()
				,devedorDocServ.getInscricaoEstadual() == null ? null : devedorDocServ.getInscricaoEstadual().getIdInscricaoEstadual()
				,Boolean.FALSE
				,conhecimento.getTpConhecimento().getValue()
				,conhecimento.getTipoTributacaoIcms() != null ? conhecimento.getTipoTributacaoIcms().getIdTipoTributacaoIcms() : null
				,conhecimento.getFilialByIdFilialOrigem().getIdFilial()
				,conhecimento.getClienteByIdClienteRedespacho() != null ? conhecimento.getClienteByIdClienteRedespacho().getIdCliente() : null
				,!CollectionUtils.isEmpty(conhecimento.getNotaFiscalConhecimentos()) ? conhecimento.getNotaFiscalConhecimentos().get(0).getTpDocumento() : null,
				idUFMunicipioColeta
		);


		conhecimento.setNrCfop(Short.valueOf(nrCfop));
	}

	private DevedorDocServ getDevedorDocServ(Conhecimento conhecimento) {
		List listDevedorDocServ = conhecimento.getDevedorDocServs();
		if (listDevedorDocServ != null && !listDevedorDocServ.isEmpty()) {
			return (DevedorDocServ) listDevedorDocServ.get(listDevedorDocServ.size() - 1);
		}
		return null;
	}

	protected void setRotaColetaEntregaSugerida(Conhecimento conhecimento) {
		Long idCliente = null;
		Long idEnderecoPessoa = null;

		EnderecoPessoa enderecoPessoa = conhecimento.getEnderecoPessoa();
		if (enderecoPessoa != null) {
			idEnderecoPessoa = enderecoPessoa.getIdEnderecoPessoa();
		} else {
			Cliente cliente = getClienteDaRotaColetaEntrega(conhecimento);
			idCliente = cliente.getIdCliente();
			if (cliente.getPessoa().getEnderecoPessoa() != null) {
				idEnderecoPessoa = cliente.getPessoa().getEnderecoPessoa().getIdEnderecoPessoa();
			}
		}

		RotaIntervaloCep rotaIntervaloCep = rotaColetaEntregaService.findRotaAtendimentoCep(
				conhecimento.getNrCepEntrega(),
				idCliente,
				idEnderecoPessoa,
				conhecimento.getFilialByIdFilialDestino().getIdFilial(), //LMS-1321
				JTDateTimeUtils.getDataAtual()
		);

		boolean filialParceira = "P".equals(conhecimento.getFilialByIdFilialDestino().getEmpresa().getTpEmpresa().getValue());

		if(!filialParceira) {
			if (rotaIntervaloCep != null) {
				conhecimento.setRotaIntervaloCep(rotaIntervaloCep);

				RotaColetaEntrega rotaColetaEntrega = rotaIntervaloCep.getRotaColetaEntrega();
				conhecimento.setRotaColetaEntregaByIdRotaColetaEntregaSugerid(rotaColetaEntrega);
				conhecimento.setRotaColetaEntregaByIdRotaColetaEntregaReal(rotaColetaEntrega);
			} else {
				DevedorDocServ devedorDoctoServico = (DevedorDocServ) conhecimento.getDevedorDocServs().get(0);
				String tpCliente = devedorDoctoServico.getCliente().getTpCliente().getValue();

				if ((ConstantesVendas.CLIENTE_EVENTUAL.equals(tpCliente)
						|| ConstantesVendas.CLIENTE_POTENCIAL.equals(tpCliente))
						&& !ConstantesExpedicao.MODAL_AEREO.equals(conhecimento.getServico().getTpModal().getValue())
				) {

					throw new BusinessException("LMS-29118");
				}
			}
		}
	}

	private Cliente getClienteDaRotaColetaEntrega(Conhecimento conhecimento) {
		if (conhecimento.getClienteByIdClienteRedespacho() != null) {
			return conhecimento.getClienteByIdClienteRedespacho();
		}

		return conhecimento.getClienteByIdClienteDestinatario();
	}

	/**
	 * Verifica se o cliente responsável possui observação na tabela OBSERVACAO_CONHECIMENTO, se existir, gravar um registro na tabela OBSERVACAO_DOCTO_SERVICO 
	 *
	 * @param clienteResponsavel
	 * @param conhecimentoNovo
	 */
	protected void generateObsDoctoClienteByObsConhecimento(Cliente clienteResponsavel, Conhecimento conhecimentoNovo) {
		//Deve buscar o cliente novamente devido a session do hibernate
		clienteResponsavel = clienteService.findByIdInitLazyProperties(clienteResponsavel.getIdCliente(), false);
		ObservacaoConhecimento observacaoConhecimento = clienteResponsavel.getObservacaoConhecimento();
		if(observacaoConhecimento != null) {
			String dsObservacao = observacaoConhecimento.getDsObservacaoConhecimento();
			conhecimentoNovo.addObservacaoDoctoServico(ConhecimentoUtils.createObservacaoDocumentoServico(conhecimentoNovo, dsObservacao, Boolean.FALSE));
		}
	}

	/**
	 * Inclui a observacao geral no conhecimento
	 *
	 * @param conhecimento
	 */
	protected void addObservacaoGeral(Conhecimento conhecimento) {
		String dsObservacao = String.valueOf(configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_OBSERVACAO_GERAL));
		if (conhecimento.getObservacaoDoctoServicos() == null){
			conhecimento.setObservacaoDoctoServicos(new ArrayList<ObservacaoDoctoServico>());
		}
		conhecimento.getObservacaoDoctoServicos().add(0, ConhecimentoUtils.createObservacaoDocumentoServico(conhecimento, dsObservacao, Boolean.FALSE));
		this.addObservacaoProdutoPerigosoOuControlado(conhecimento);
	}

	private void addObservacaoProdutoPerigosoOuControlado(Conhecimento conhecimento) {
		Boolean existeNotaComProdutoPerigoso = this.verificarNotaComProdutoPerigoso(conhecimento.getNotaFiscalConhecimentos());
		Boolean existeNotaControladaPorAutoridades = this.verificarNotaControladaPorAutoridades(conhecimento.getNotaFiscalConhecimentos());

		if (existeNotaComProdutoPerigoso) {
			conhecimento.getObservacaoDoctoServicos()
					.add(addObservacaoGeralGenerica(conhecimento, ConstantesExpedicao.NM_PARAMETRO_DS_PRODUTO_PERIGOSO));
		}

		if (existeNotaControladaPorAutoridades) {
			conhecimento.getObservacaoDoctoServicos()
					.add(addObservacaoGeralGenerica(conhecimento, ConstantesExpedicao.NM_PARAMETRO_DS_PRODUTO_CONTROLADO));
		}
	}

	private ObservacaoDoctoServico addObservacaoGeralGenerica(Conhecimento conhecimento, String valorParametro) {
		String dsObservacao = String.valueOf(configuracoesFacade.getValorParametro(valorParametro));
		return ConhecimentoUtils.createObservacaoDocumentoServico(conhecimento, dsObservacao , Boolean.FALSE);
	}

	private Boolean verificarNotaControladaPorAutoridades(List<NotaFiscalConhecimento> listNotaFiscalConhecimento) {
		Boolean existeNotaComControle = Boolean.FALSE;

		for (NotaFiscalConhecimento nota: listNotaFiscalConhecimento) {
			Boolean existeControlePoliciaCivil = nota.getBlControladoPoliciaCivil();
			Boolean existeControlePoliciaFederal= (Boolean) nota.getBlControladoPoliciaFederal();
			Boolean existeControleExercito= (Boolean) nota.getBlControladoExercito();

			if (BooleanUtils.isTrue(existeControlePoliciaCivil) || BooleanUtils.isTrue(existeControlePoliciaFederal) || BooleanUtils.isTrue(existeControleExercito)) {
				existeNotaComControle = Boolean.TRUE;
				break;
			}
		}

		return existeNotaComControle;
	}

	private Boolean verificarNotaComProdutoPerigoso(List<NotaFiscalConhecimento> listNotaFiscalConhecimento) {
		Boolean existeNotaComProdutoPerigoso = Boolean.FALSE;
		for (NotaFiscalConhecimento notaFiscalConhecimento : listNotaFiscalConhecimento) {

			if (BooleanUtils.isTrue(notaFiscalConhecimento.getBlProdutoPerigoso())) {
				existeNotaComProdutoPerigoso = Boolean.TRUE;
				break;
			}
		}

		return existeNotaComProdutoPerigoso;
	}


	protected void addObservacaoSubcontratacao(Conhecimento conhecimento, FluxoFilial fluxoFilial) {
		if (conhecimento.getObservacaoDoctoServicos() == null){
			conhecimento.setObservacaoDoctoServicos(new ArrayList<ObservacaoDoctoServico>());
		}

		conhecimento.getObservacaoDoctoServicos().add(0, ConhecimentoUtils.createObservacaoDocumentoServico(conhecimento, getObservacaoManualCteSubcontratante(fluxoFilial), Boolean.FALSE));

	}

	private String getObservacaoManualCteSubcontratante(FluxoFilial fluxoFilial) {
		StringBuilder dsObservacaoManualCteSubcontratante = new StringBuilder("Transporte subcontratado com ")
				.append(fluxoFilial.getEmpresaSubcontratada().getNmPessoa())
				.append(" Cnpj:")
				.append(fluxoFilial.getEmpresaSubcontratada().getNrIdentificacao())
				.append(" e Inscrição Estadual:");

		InscricaoEstadual ie = getInscricaoEstadualService().findIeByIdPessoaAtivoPadrao(fluxoFilial.getEmpresaSubcontratada().getIdPessoa());
		if(ie != null){
			dsObservacaoManualCteSubcontratante.append(ie.getNrInscricaoEstadual());
			dsObservacaoManualCteSubcontratante.append(" ");
		}

		dsObservacaoManualCteSubcontratante.append(String.valueOf(configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_OBSERVAÇÃO_CTE_SUBCONTRATANTE)));

		return dsObservacaoManualCteSubcontratante.toString();
	}

	/**
	 * Define o Fluxo de Carga para o Conhecimento
	 *
	 * @param conhecimento
	 */
	public void setFluxoCarga(Conhecimento conhecimento, Boolean blBloqueiaSubcontratada) {
		Long idFilialDestino = conhecimento.getFilialByIdFilialDestino().getIdFilial();
		FluxoFilial fluxoFilial = mcdService.findFluxoEntreFiliais(
				conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
				idFilialDestino,
				conhecimento.getServico().getIdServico(),
				null
		);
		conhecimento.setFluxoFilial(fluxoFilial);

		if(validateFluxoFilialSubcontratacao(conhecimento, fluxoFilial, blBloqueiaSubcontratada)){
			conhecimento.setBlFluxoSubcontratacao(Boolean.TRUE);
			addObservacaoSubcontratacao(conhecimento, fluxoFilial);
		}
	}

	public DomainValue getTpCargaDocumento(final Conhecimento conhecimento) {
		boolean blPaletizacao = BooleanUtils.isTrue(conhecimento.getBlPaletizacao());
		if(blPaletizacao){
			return new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT);
		}

		for(NotaFiscalConhecimento nfc : conhecimento.getNotaFiscalConhecimentos()){
			if(BigDecimalUtils.hasValue(nfc.getPsMercadoria()) && BigDecimalUtils.hasValue(BigDecimal.valueOf(nfc.getQtVolumes()))){
				BigDecimal vlPesoMercPorVolume = BigDecimalUtils.divide(nfc.getPsMercadoria(), BigDecimal.valueOf(nfc.getQtVolumes()));
				BigDecimal vlLimitePesoVol = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_VL_LIMITE_PESO_VOL);
				if(vlPesoMercPorVolume.compareTo(vlLimitePesoVol) > 0){
					return new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT);
				}
			}

			//se alguma nota fiscal do documento possui algum volume com peso cubado e quant de volume maior que zero
			if(BigDecimalUtils.hasValue(nfc.getPsCubado()) && BigDecimalUtils.hasValue(BigDecimal.valueOf(nfc.getQtVolumes()))){
				BigDecimal vlPesoCubPorVolume = BigDecimalUtils.divide(nfc.getPsCubado(), BigDecimal.valueOf(nfc.getQtVolumes()));
				BigDecimal vlLimitePesoCubVol = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_VL_LIMITE_PS_CUB_VOL);
				// e maior que o parametrizado (ao efetuar a divisão do peso cubado declarado (NFC.PS_CUBADO) pela quantidade de volume (NFC.QT_VOLUMES) for maior que valor limite do parâmetro geral “VL_LIMITE_PS_CUB_VOL”,
				if(vlPesoCubPorVolume.compareTo(vlLimitePesoCubVol) > 0){
					// então atualizar o campo DS.TP_CARGA_DOCUMETO = ‘F’,
					return new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT);
				}
			}

		}

		// se não validar  se a quantidade de volume do CTE é maior que o parametrizado (DS.QT_VOLUMES > parâmetro geral “VL_LIMITE_QTDE_VOL”, então atualizar o campo DS.TP_CARGA_DOCUMETO = ‘F’
		BigDecimal vlLimiteQtdeVol = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_VL_LIMITE_QTDE_VOL);
		if(conhecimento.getQtVolumes().compareTo(vlLimiteQtdeVol.intValue()) > 0){
			return new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT);
		}

		if(BigDecimalUtils.hasValue(conhecimento.getPsCubadoDeclarado())){
			BigDecimal vlLimitePesoCub = (BigDecimal)configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_VL_LIMITE_PESO_CUB);
			if(conhecimento.getPsCubadoDeclarado().compareTo(vlLimitePesoCub) > 0){
				return new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_FREIGHT);
			}
		}

		return new DomainValue(ConstantesExpedicao.DM_TP_CARGA_DOCUMENTO_PARCEL);
	}

	private boolean validateFluxoFilialSubcontratacao(Conhecimento conhecimento, FluxoFilial fluxoFilial, Boolean blBloqueiaSubcontratada) {
		return !BooleanUtils.isTrue(blBloqueiaSubcontratada) &&
				conhecimento.getIdDoctoServico() == null &&
				validateTpConhecimentoFluxoSubcontratacao(conhecimento) &&
				BooleanUtils.isTrue(fluxoFilial.getBlFluxoSubcontratacao()) &&
				!BooleanUtils.isTrue(conhecimento.getClienteByIdClienteRemetente().getBlNaoPermiteSubcontratacao());
	}

	private boolean validateTpConhecimentoFluxoSubcontratacao(Conhecimento conhecimento) {
		return ConstantesExpedicao.CONHECIMENTO_NORMAL.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue()) ||
				ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue()) ||
				ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO_PARCIAL.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())||
				ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue());
	}

	protected Map getAtendimentoMunicipio(Conhecimento conhecimento, Long idClienteAtendido){

		Filial filialOrigem = obtemFilial(conhecimento.getDadosCliente());
		Municipio municipioFilialOrigem = filialOrigem.getPessoa().getEnderecoPessoa().getMunicipio();
		Long idUfOrigem = municipioFilialOrigem.getUnidadeFederativa().getIdUnidadeFederativa();

		SegmentoMercado segmentoMercado = clienteService.findSegmentoMercadoByIdCliente(idClienteAtendido);

		return ppeService.findAtendimentoMunicipio(
				conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio(),
				conhecimento.getServico().getIdServico(),
				Boolean.FALSE,
				JTDateTimeUtils.getDataAtual(),
				conhecimento.getNrCepEntrega(),
				idClienteAtendido,
				conhecimento.getClienteByIdClienteRemetente().getIdCliente(),
				segmentoMercado.getIdSegmentoMercado(),
				idUfOrigem,
				filialOrigem.getIdFilial(),
				"N",
				conhecimento.getNaturezaProduto().getIdNaturezaProduto(),
				null
		);
	}

	protected Map getAtendimentoMunicipio(Conhecimento conhecimento){

		Long idClienteAtendido = getIdClienteAtendido(conhecimento);

		if( (conhecimento.getClienteByIdClienteConsignatario() != null) && (!conhecimento.getTpConhecimento().getValue().equals(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO)) ){
			EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idClienteAtendido);
			conhecimento.setMunicipioByIdMunicipioEntrega(enderecoPessoa.getMunicipio());
			conhecimento.setNrCepEntrega(enderecoPessoa.getNrCep());
		}

		SegmentoMercado segmentoMercado = clienteService.findSegmentoMercadoByIdCliente(idClienteAtendido);

		Filial filial = obtemFilial(conhecimento.getDadosCliente());

		Municipio municipioFilialOrigem = filial.getPessoa().getEnderecoPessoa().getMunicipio();

		return ppeService.findAtendimentoMunicipio(
				conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio(),
				conhecimento.getServico().getIdServico(),
				Boolean.FALSE,
				JTDateTimeUtils.getDataAtual(),
				conhecimento.getNrCepEntrega(),
				idClienteAtendido,
				conhecimento.getClienteByIdClienteRemetente().getIdCliente(),
				segmentoMercado.getIdSegmentoMercado(),
				municipioFilialOrigem.getUnidadeFederativa().getIdUnidadeFederativa(),
				filial.getIdFilial(),
				"N",
				conhecimento.getNaturezaProduto().getIdNaturezaProduto(),
				Boolean.TRUE
		);
	}

	private Long getIdClienteAtendido(Conhecimento conhecimento) {
		if(conhecimento.getClienteByIdClienteConsignatario() != null) {
			return conhecimento.getClienteByIdClienteConsignatario().getIdCliente();
		}

		return conhecimento.getClienteByIdClienteDestinatario().getIdCliente();
	}

	/**
	 * Define a DivisaoCliente utilizado no objeto <b>DevedorDocServFat</b>
	 *
	 * @param cliente
	 * @param divisaoCliente
	 * @param servico
	 * @return
	 * @author Andre Valadas
	 */
	protected DivisaoCliente getDivisaoClienteDevedor(Cliente clienteBaseCalculo, Cliente cliente, DivisaoCliente divisaoCliente, Servico servico) {

		if (divisaoCliente != null) {
			if (clienteBaseCalculo.equals(cliente) ||  "F".equals(cliente.getTpCliente().getValue())) {
				return divisaoCliente;
			} else {
				DivisaoCliente divisao = clienteBaseCalculo.getDivisaoClienteResponsavel();
				if (divisao != null) {
					return divisao;
				} else {
					List<DivisaoCliente> divisoes = divisaoClienteService.findDivisaoCliente(cliente.getIdCliente(), servico.getTpModal(), servico.getTpAbrangencia());
					if(!divisoes.isEmpty()){
						return divisoes.get(0);
					}
				}
			}
		} else {
			List<DivisaoCliente> divisoes = divisaoClienteService.findDivisaoCliente(cliente.getIdCliente(), servico.getTpModal(), servico.getTpAbrangencia());
			if(!divisoes.isEmpty()){
				return divisoes.get(0);
			}
		}

		return divisaoCliente;
	}

	public void setDocumentoServicoFacade(DocumentoServicoFacade documentoServicoFacade) {
		this.documentoServicoFacade = documentoServicoFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setTipoCustoService(TipoCustoService tipoCustoService) {
		this.tipoCustoService = tipoCustoService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setBuscarCfopService(BuscarCfopService buscarCfopService) {
		this.buscarCfopService = buscarCfopService;
	}

	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	public void setMcdService(McdService mcdService) {
		this.mcdService = mcdService;
	}

	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	protected InscricaoEstadualService getInscricaoEstadualService() {
		return buscarCfopService.getInscricaoEstadualService();
	}

	public CalculoFreteService getCalculoFreteService() {
		return calculoFreteService;
	}

	public void setCalculoFreteService(CalculoFreteService calculoFreteService) {
		this.calculoFreteService = calculoFreteService;
	}

	public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public PromotorClienteService getPromotorClienteService() {
		return promotorClienteService;
	}

	public void setPromotorClienteService(
			PromotorClienteService promotorClienteService) {
		this.promotorClienteService = promotorClienteService;
	}

	public MunicipioGrupoRegiaoService getMunicipioGrupoRegiaoService() {
		return municipioGrupoRegiaoService;
	}

	public void setMunicipioGrupoRegiaoService(
			MunicipioGrupoRegiaoService municipioGrupoRegiaoService) {
		this.municipioGrupoRegiaoService = municipioGrupoRegiaoService;
	}
}
