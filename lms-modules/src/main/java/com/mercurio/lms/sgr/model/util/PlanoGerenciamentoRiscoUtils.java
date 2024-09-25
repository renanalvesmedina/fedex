package com.mercurio.lms.sgr.model.util;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.TP_INDICADOR_POR_VALOR;

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.ParametroFilial;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.Seguradora;
import com.mercurio.lms.sgr.dto.DoctoServicoDTO;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.util.ConstantesGerRisco;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;

/**
 * LMS-6850 - Classe utilitária para o Plano de Gerenciamento de Risco.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 * 
 */
public class PlanoGerenciamentoRiscoUtils {

	private static final String LINE_SEPARATOR =  VMProperties.LINE_SEPARATOR.getValue();

	private ConfiguracoesFacade configuracoesFacade;
	private ConversaoMoedaService conversaoMoedaService;

	/*
	 * Busca de parâmetros gerais e de filiais
	 */

	/**
	 * Busca valor do {@link ParametroGeral}, ignorando qualquer exceção
	 * lançada.
	 * 
	 * @param nmParametro
	 *            Nome para busca do {@link ParametroGeral}.
	 * @return Retorna valor do {@link ParametroGeral}.
	 */
	public boolean findParametroGeral(String nmParametro) {
		return "S".equals(configuracoesFacade.getValorParametroWithoutException(nmParametro));
	}

	/**
	 * Busca valor do {@link ParametroFilial}.
	 * 
	 * @param nmParametro
	 *            Nome para busca do {@link ParametroFilial}.
	 * @return Retorna valor do {@link ParametroFilial}.
	 */
	public boolean findParametroFilial(String nmParametro) {
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		return "S".equals(configuracoesFacade.getValorParametro(idFilial, nmParametro));
	}

	/*
	 * Produção e tratamentos de mensagens diversas
	 */

	/**
	 * Busca mensagem específica.
	 * 
	 * @param chave
	 *            Identificador da mensagem.
	 * @return Mensagem.
	 */
	public String getMensagem(String chave) {
		return configuracoesFacade.getMensagem(chave);
	}

	/**
	 * Busca mensagem específica e preenche argumentos com os seguintes dados do
	 * {@link DoctoServico}:
	 * <ul>
	 * <li>Número de identificação do {@link Cliente} remetente;
	 * <li>Número de identificação do {@link Cliente} destinatário;
	 * <li>Descrição do tipo de abrangência (nacional/internacional);
	 * <li>Descrição da {@link NaturezaProduto};
	 * <li>Descrição do tipo de operação (coleta/entrega/viagem);
	 * <li>Sigla da {@link Filial} de origem;
	 * <li>Sigla da {@link Filial} de destino.
	 * </ul>
	 * 
	 * @param chave
	 *            Identificador da mensagem.
	 * @param documento
	 *            Dados do {@link DoctoServico} para argumentos da mensagem.
	 * @return Mensagem preenchida com dados de {@link DoctoServico}.
	 */
	public String getMensagemDocumento(String chave, DoctoServicoDTO documento) {
		Object[] argumentos = new Object[] {
				documento.getNrIdentificacaoRemetente(),
				documento.getNrIdentificacaoDestinatario(),
				ConstantesGerRisco.stringTpAbrangencia(documento.getTpAbrangencia()),
				documento.getDsNaturezaProduto(),
				ConstantesGerRisco.stringTpOperacao(documento.getTpOperacao()),
				documento.getSgFilialOrigem(),
				documento.getSgFilialDestino()
		};
		return configuracoesFacade.getMensagem(chave, argumentos);
	}

	/**
	 * Busca mensagem específica e preenche argumentos com a descrição do
	 * {@link EnquadramentoRegra} e com o valor de mercadoria.
	 * 
	 * @param chave
	 *            Identificador da mensagem.
	 * @param regra
	 *            {@link EnquadramentoRegra} para argumentos da mensagem.
	 * @param vlMercadoria
	 *            Valor de mercadoria para argumento da mensagem.
	 * @return Mensagem preenchida com dados do {@link EnquadramentoRegra} e com
	 *         o valor da mercadoria.
	 */
	public String getMensagemRegra(String chave, EnquadramentoRegra regra, BigDecimal vlMercadoria) {
		String simbolo = regra.getMoeda().getDsSimbolo();
		Object[] argumentos = new Object[] {
				regra.getDsEnquadramentoRegra().getValue(),
				FormatUtils.formatValorComIndicador(TP_INDICADOR_POR_VALOR, vlMercadoria, simbolo)
		};
		return configuracoesFacade.getMensagem(chave, argumentos);
	}

	/**
	 * Busca mensagem específica e preenche argumentos com nome da
	 * {@link Seguradora}, número da {@link ApoliceSeguro} ou do
	 * {@link SeguroCliente}, valor limite para o {@link ControleCarga} e valor
	 * total da mercadoria.
	 * 
	 * @param chave
	 *            Identificador da mensagem.
	 * @param regra
	 *            {@link EnquadramentoRegra} com referência para
	 *            {@link ApoliceSeguro} ou {@link SeguroCliente}.
	 * @param vlMercadoria
	 *            Valor total da mercadoria.
	 * @return Mensagem preenchida com dados da {@link Seguradora}, da
	 *         {@link ApoliceSeguro} ou do {@link SeguroCliente}, valor limite
	 *         para o {@link ControleCarga} e valor total da mercadoria.
	 */
	public String getMensagemApolice(String chave, EnquadramentoRegra regra, BigDecimal vlMercadoria) {
		String nmSeguradora = "";
		if (regra.getSeguradora() != null && regra.getSeguradora().getPessoa() != null) {
			nmSeguradora = regra.getSeguradora().getPessoa().getNmPessoa();
		}
		if (regra.getNrApolice() != null) {
			nmSeguradora += " / " + regra.getNrApolice();
		}
		String simbolo = regra.getMoeda().getDsSimbolo();
		BigDecimal vlLimite = regra.getVlLimiteControleCarga();
		Object[] argumentos = new Object[] {
				nmSeguradora,
				FormatUtils.formatValorComIndicador(TP_INDICADOR_POR_VALOR, vlLimite, simbolo),
				FormatUtils.formatValorComIndicador(TP_INDICADOR_POR_VALOR, vlMercadoria, simbolo)
		};
		return configuracoesFacade.getMensagem(chave, argumentos);
	}

	/**
	 * Busca mensagem específica e preenche argumentos com a descrições do
	 * {@link EnquadramentoRegra} e da {@link NaturezaProduto}.
	 * 
	 * @param chave
	 *            Identificador da mensagem.
	 * @param regra
	 *            {@link EnquadramentoRegra} para argumentos da mensagem.
	 * @param naturezaProduto
	 *            {@link NaturezaProduto} para argumentos da mensagem.
	 * @return Mensagem preenchida com descrições do {@link EnquadramentoRegra}
	 *         e da {@link NaturezaProduto}.
	 */
	public String getMensagemNatureza(String chave, EnquadramentoRegra regra, NaturezaProduto naturezaProduto) {
		Object[] argumentos = new Object[] {
				naturezaProduto.getDsNaturezaProduto().getValue(),
				regra.getDsEnquadramentoRegra().getValue()
		};
		return configuracoesFacade.getMensagem(chave, argumentos);
	}

	/**
	 * Concatena mensagens linha a linha.
	 * 
	 * @param mensagens
	 *            {@link Collection} de mensagens a concatenar.
	 * @return {@link String} com mensagens concatenadas linha a linha.
	 */
	public String join(Collection<String> mensagens) {
		return StringUtils.join(mensagens.iterator(), LINE_SEPARATOR);
	}

	/*
	 * Conversão de moedas
	 */

	/**
	 * Converte o valor do {@link Pais} e {@link Moeda} de origem para
	 * {@link Pais} e {@link Moeda} de destino, considerando determinada
	 * {@link YearMonthDay}.
	 * 
	 * @param idPaisOrigem
	 *            Id do {@link Pais} de origem.
	 * @param idMoedaOrigem
	 *            Id da {@link Moeda} de origem.
	 * @param idPaisDestino
	 *            Id do {@link Pais} de destino.
	 * @param idMoedaDestino
	 *            Id da {@link Moeda} de destino.
	 * @param dtCotacao
	 *            {@link YearMonthDay} para conversão.
	 * @param vlMoeda
	 *            Valor a converter.
	 * @return Valor convertido.
	 */
	public BigDecimal findConversaoMoeda(
			Long idPaisOrigem, Long idMoedaOrigem, Long idPaisDestino, Long idMoedaDestino, YearMonthDay dtCotacao, BigDecimal vlMoeda) {
		return conversaoMoedaService.findConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino, dtCotacao, vlMoeda);
	}

	/*
	 * Injeção de Dependências
	 */

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

}
