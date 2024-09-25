package com.mercurio.lms.services.coleta.helper;

import static com.mercurio.lms.util.BigDecimalUtils.ZERO;
import static java.lang.Boolean.FALSE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.layoutNfse.model.rps.Endereco;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.util.ConstantesVendas;

import br.com.tntbrasil.integracao.domains.pedidocoleta.PedidoColetaDMN;

public class PedidoColetaIntegracaoRestPopulateHelper implements Serializable {

	private static final long serialVersionUID = 2683421477754928254L;
	
	public TipoEnderecoPessoa getTipoEnderecoColeta() {
		TipoEnderecoPessoa tipoEndereco = new TipoEnderecoPessoa();
		tipoEndereco.setTpEndereco(new DomainValue("COL"));
		return tipoEndereco;
	}
	
	public EnderecoPessoa getEnderecoColetaCliente(EnderecoPessoa endereco, String cep, String logradouro, String numero) {
		endereco = endereco == null ? new EnderecoPessoa() : endereco;
		endereco.setNrCep(cep);
		endereco.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
		endereco.setDsEndereco(logradouro);
		endereco.setNrEndereco(numero);
		endereco.setBlEnderecoMigrado(false);
		endereco.setTipoEnderecoPessoas(new ArrayList<TipoEnderecoPessoa>());
		return endereco;
	}

	public TelefoneEndereco getTelefoneEnderecoColeta(String ddd, String numeroTelefone) {
		TelefoneEndereco telefone = new TelefoneEndereco();
		telefone.setTpTelefone(new DomainValue("C")); 
		telefone.setTpUso(new DomainValue("FO"));
		telefone.setNrDdd(ddd);
		telefone.setNrTelefone(numeroTelefone);
		return telefone;
	}

	public Cliente getCliente(Filial filial, String documento, String nome) {
		Cliente cliente = new Cliente();
		cliente.setTpCliente(new DomainValue(ConstantesVendas.CLIENTE_POTENCIAL));
		cliente.setTpSituacao(new DomainValue(ConstantesVendas.SITUACAO_ATIVO));
		cliente.setCliente(cliente);
		
		cliente.setFilialByIdFilialCobranca(filial);
		cliente.setFilialByIdFilialAtendeOperacional(filial);
		cliente.setFilialByIdFilialAtendeComercial(filial);

		cliente.setBlGeraReciboFreteEntrega(Boolean.FALSE);
		cliente.setBlPermanente(Boolean.FALSE);
		cliente.setBlResponsavelFrete(Boolean.FALSE);
		cliente.setBlBaseCalculo(Boolean.FALSE);
		cliente.setBlCobraReentrega(Boolean.TRUE);
		cliente.setBlCobraDevolucao(Boolean.TRUE);
		cliente.setBlColetaAutomatica(Boolean.FALSE);
		cliente.setBlFobDirigido(Boolean.FALSE);
		cliente.setBlFobDirigidoAereo(Boolean.FALSE);
		cliente.setBlPesoAforadoPedagio(Boolean.FALSE);
		cliente.setBlIcmsPedagio(Boolean.FALSE);
		cliente.setBlIndicadorProtesto(Boolean.FALSE);
		cliente.setBlMatriz(Boolean.FALSE);
		cliente.setBlCobrancaCentralizada(Boolean.FALSE);
		cliente.setBlFaturaDocsEntregues(Boolean.FALSE);
		cliente.setBlPreFatura(Boolean.FALSE);
		cliente.setBlRessarceFreteFob(Boolean.FALSE);
		cliente.setBlEmiteBoletoCliDestino(Boolean.FALSE);
		cliente.setBlAgrupaFaturamentoMes(Boolean.FALSE);
		cliente.setBlAgrupaNotas(Boolean.FALSE);
		cliente.setBlCadastradoColeta(Boolean.FALSE);
		cliente.setBlOperadorLogistico(Boolean.FALSE);
		cliente.setBlFronteiraRapida(Boolean.FALSE);
		cliente.setBlAgendamentoPessoaFisica(Boolean.FALSE);
		cliente.setBlAgendamentoPessoaJuridica(Boolean.FALSE);
		cliente.setBlFaturaDocsConferidos(FALSE);
		cliente.setDtGeracao(JTDateTimeUtils.getDataAtual());
		cliente.setPcDescontoFreteCif(ZERO);
		cliente.setPcDescontoFreteFob(ZERO);
		cliente.setNrCasasDecimaisPeso(Short.valueOf("2"));
		cliente.setBlObrigaRecebedor(Boolean.FALSE);
		cliente.setTpDificuldadeColeta(new DomainValue("0"));
		cliente.setTpDificuldadeEntrega(new DomainValue("0"));
		cliente.setTpDificuldadeClassificacao(new DomainValue("0"));
		cliente.setTpFrequenciaVisita(new DomainValue("M"));
		cliente.setTpFormaArredondamento(new DomainValue("P"));
		cliente.setTpCobranca(new DomainValue("4"));
		cliente.setPcJuroDiario(ZERO);
		cliente.setBlFaturaDocReferencia(FALSE);
		cliente.setBlDificuldadeEntrega(FALSE);
		cliente.setBlRetencaoComprovanteEntrega(FALSE);
		cliente.setBlDivulgaLocalizacao(FALSE);
		cliente.setBlPermiteCte(FALSE);
		cliente.setBlObrigaSerie(FALSE);
		cliente.setBlSeparaFaturaModal(FALSE);
		cliente.setBlMtzLiberaRIM(FALSE);
		cliente.setBlCadastradoColeta(Boolean.TRUE);
		//
		cliente.setDefaultTpFormaCobranca();
		cliente.setDefaultBlEmissaoDiaNaoUtil();
		cliente.setDefaultBlEmissaoSabado();
		cliente.setDefaultBldesconsiderarDificuldade();
		cliente.setDefaultBlGerarParcelaFreteValorLiquido();
		//
		return cliente;
	}

    public InscricaoEstadual getInscricaoEstadual(Pessoa pessoa, String numeroIE, UnidadeFederativa uf) {
    	InscricaoEstadual inscricaoEstadual = new InscricaoEstadual();
    	
    	inscricaoEstadual.setNrInscricaoEstadual(numeroIE);
    	inscricaoEstadual.setPessoa(pessoa);
    	inscricaoEstadual.setUnidadeFederativa(uf);
    	inscricaoEstadual.setBlIndicadorPadrao(Boolean.TRUE);
    	inscricaoEstadual.setTpSituacao(new DomainValue("A"));
    	inscricaoEstadual.setTiposTributacaoIe(recurperaListaTiposTributacaoIe(inscricaoEstadual));

    	return inscricaoEstadual;
	}
	private List<TipoTributacaoIE> recurperaListaTiposTributacaoIe(InscricaoEstadual inscricaoEstadual) {
		TipoTributacaoIE tipoTributacaoIE = new TipoTributacaoIE();
		
        if ("ISENTO".equalsIgnoreCase(inscricaoEstadual.getNrInscricaoEstadual())){
            tipoTributacaoIE.setTpSituacaoTributaria(new DomainValue("NC"));
        }else{
            tipoTributacaoIE.setTpSituacaoTributaria(new DomainValue("CO"));
        }
        
        tipoTributacaoIE.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
		tipoTributacaoIE.setInscricaoEstadual(inscricaoEstadual);
		tipoTributacaoIE.setBlAceitaSubstituicao(Boolean.TRUE);
		tipoTributacaoIE.setBlIncentivada(Boolean.FALSE);
		tipoTributacaoIE.setBlIsencaoExportacoes(Boolean.FALSE);
		
		List<TipoTributacaoIE> list = new ArrayList<TipoTributacaoIE>();
		list.add(tipoTributacaoIE);
		
		return list;
	}	
	
	private static final Moeda MOEDA_PEDIDO = new Moeda(1L);
	private static final String TIPO_MODO_PEDIDO_COLETA = "TE";
	private static final String TIPO_PEDIDO_COLETA = "NO";
	private static final String TIPO_STATUS_COLETA = "AB";
	private static final Integer VERSAO_COLETA = Integer.valueOf(1);
	private static final BigDecimal VL_TOTAL_INFORMADO = BigDecimal.ZERO;
	private static final BigDecimal VL_TOTAL_VERIFICADO = BigDecimal.ZERO;
	private static final Boolean CLIENTE_LIBERADO_MANUALMENTE = false;
	private static final Boolean ALTERADO_POS_PROGRAMACAO = false;
	private static final Boolean PRODUTO_DIFERENCIADO = false;
	private static final Boolean PEDIDO_INTEGRACAO = true;
	private static final String NOME_SOLICITANTE = "FedEx";
	
	@SuppressWarnings("deprecation")
	public PedidoColeta getPedidoColeta(Long numeroColetaFilial, Cliente cliente, EnderecoPessoa enderecoColetaCliente, String tipoLogradouro, Filial filial, Municipio municipio, Usuario usuario, RotaIntervaloCep rotaColetaEntrega, PedidoColetaDMN pedido) {
		PedidoColeta result = new PedidoColeta();
		
		result.setFilialByIdFilialSolicitante(filial);
		result.setFilialByIdFilialResponsavel(filial);
		result.setCliente(cliente);
		result.setMunicipio(municipio);
		
		result.setUsuario(usuario);
		
		result.setMoeda(MOEDA_PEDIDO);
		
		result.setNrColeta(numeroColetaFilial);
		
		result.setDsInfColeta(pedido.getNumeroColeta().toString());
		
		result.setNrDddCliente(pedido.getDddTelefoneContato());
		result.setNrTelefoneCliente(pedido.getNumeroTelefoneContato());
		result.setNrCep(pedido.getCepColeta());
		result.setDhPedidoColeta(pedido.getDataHoraSolicitacaoColeta());
		result.setDhColetaDisponivel(pedido.getDataHoraLimiteRealizacaoColeta());
		
		result.setNmSolicitante(NOME_SOLICITANTE);
		
		TimeOfDay hrLimiteColeta = new TimeOfDay(
				pedido.getDataHoraLimiteRealizacaoColeta().getHourOfDay(),
				pedido.getDataHoraLimiteRealizacaoColeta().getMinuteOfHour(),
				pedido.getDataHoraLimiteRealizacaoColeta().getChronology()
				);
		result.setHrLimiteColeta(hrLimiteColeta);
		
		DateTime dtaux = pedido != null && pedido.getDataHoraLimiteRealizacaoColeta() != null ?
				pedido.getDataHoraLimiteRealizacaoColeta() : null;
		result.setDtPrevisaoColeta(dtaux != null ? dtaux.toYearMonthDay() : null);
		
		result.setTpModoPedidoColeta(new DomainValue(TIPO_MODO_PEDIDO_COLETA));
		result.setTpPedidoColeta(new DomainValue(TIPO_PEDIDO_COLETA));
		result.setTpStatusColeta(new DomainValue(TIPO_STATUS_COLETA));
		
//		result.setEdColeta(pedido.getTipoLogradouro()+ " " + pedido.getLogradouroColeta() + ", " + pedido.getNumeroLogradouroColeta());
		result.setEdColeta(
				(tipoLogradouro != null ? (tipoLogradouro + " ") : "") +
						enderecoColetaCliente.getDsEndereco() + ", " +
						enderecoColetaCliente.getNrEndereco()
				);
		result.setDsBairro(null);
		result.setNrEndereco(enderecoColetaCliente.getNrEndereco());
		result.setDsComplementoEndereco(null);
		
		result.setNmContatoCliente(pedido.getNomeContatoCliente());
		result.setVersao(VERSAO_COLETA);
		result.setVlTotalInformado(VL_TOTAL_INFORMADO);
		result.setVlTotalVerificado(VL_TOTAL_VERIFICADO);
		
		result.setQtTotalVolumesInformado(pedido.getQuantidadeTotalVolumes().intValue());
		result.setQtTotalVolumesVerificado(pedido.getQuantidadeTotalVolumes().intValue());
		result.setPsTotalInformado(pedido.getPesoTotal());
		result.setPsTotalVerificado(pedido.getPesoTotal());
		
		result.setBlClienteLiberadoManual(CLIENTE_LIBERADO_MANUALMENTE);
		result.setBlAlteradoPosProgramacao(ALTERADO_POS_PROGRAMACAO);
		
		if (rotaColetaEntrega != null) {
			result.setRotaColetaEntrega(rotaColetaEntrega.getRotaColetaEntrega());
		}
		result.setRotaIntervaloCep(rotaColetaEntrega);
		
		result.setObPedidoColeta(pedido.getObservacao());
		result.setEnderecoPessoa(cliente.getPessoa().getEnderecoPessoa());
		
		result.setBlProdutoDiferenciado(PRODUTO_DIFERENCIADO);
		result.setBlIntegracaoFedex(PEDIDO_INTEGRACAO);
		result.setDhIntegracaoFedex(new DateTime());

		return result;
	}

	public DetalheColeta getDetalheColeta(PedidoColeta pedido, Municipio municipio, BigDecimal pesoMercadoria, BigDecimal quantidadeVolumes, Filial filialColeta) {
		DetalheColeta result = new DetalheColeta();
		
		result.setPedidoColeta(pedido);
		Servico servico = new Servico();
		servico.setIdServico(1L);
		result.setServico(servico);
		result.setMunicipio(municipio);
		NaturezaProduto natureza = new NaturezaProduto();
		natureza.setIdNaturezaProduto(65L);
		result.setNaturezaProduto(natureza);
		Moeda moeda = new Moeda(1L);
		result.setMoeda(moeda);
		result.setTpFrete(new DomainValue("C"));
		result.setVlMercadoria(BigDecimal.ZERO);
		result.setQtVolumes(quantidadeVolumes.intValue());
		result.setPsMercadoria(pesoMercadoria);
		result.setPsAforado(BigDecimal.ZERO);
		result.setFilial(filialColeta);
		
		return result;
	}
		
	public EventoColeta getEventoColeta(PedidoColeta pedidoColeta, Usuario usuarioIntegracao, String descricao) {
	    EventoColeta result = new EventoColeta();
	    OcorrenciaColeta ocorrencia = new OcorrenciaColeta();
	    ocorrencia.setIdOcorrenciaColeta(87L);
	    result.setOcorrenciaColeta(ocorrencia);
	    result.setPedidoColeta(pedidoColeta);
	    result.setUsuario(usuarioIntegracao);
	    result.setTpEventoColeta(new DomainValue("SO"));
	    result.setDhEvento(new DateTime());
	    result.setDsDescricao(descricao);
	    return result;
	}
	
}
