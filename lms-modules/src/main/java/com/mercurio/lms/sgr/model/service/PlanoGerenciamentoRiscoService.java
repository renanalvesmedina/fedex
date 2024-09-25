package com.mercurio.lms.sgr.model.service;

import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_AGRUPAMENTO_ACUMULADO;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_CRITERIO_DESTINO;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_CRITERIO_ORIGEM;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_INTEGRANTE_FRETE_DESTINATARIO;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_INTEGRANTE_FRETE_REMETENTE;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_COLETA;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_ENTREGA;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_PEDIDO_COLETA_AEROPORTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.ParametroFilial;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.Seguradora;
import com.mercurio.lms.seguros.model.TipoSeguro;
import com.mercurio.lms.sgr.dto.DoctoServicoDTO;
import com.mercurio.lms.sgr.dto.EnquadramentoRegraDTO;
import com.mercurio.lms.sgr.dto.ExigenciaGerRiscoDTO;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.ClienteEnquadramento;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.ExigenciaFaixaValor;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.FaixaDeValor;
import com.mercurio.lms.sgr.model.FaixaValorNaturezaImpedida;
import com.mercurio.lms.sgr.model.TipoExigenciaGerRisco;
import com.mercurio.lms.sgr.model.dao.PlanoGerenciamentoRiscoDAO;
import com.mercurio.lms.sgr.model.util.EnquadramentoRegraCollectionUtils;
import com.mercurio.lms.sgr.model.util.PlanoGerenciamentoRiscoUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * LMS-6850 - Classe servi�o para o Plano de Gerenciamento de Risco.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 * 
 */
public class PlanoGerenciamentoRiscoService {

	private static final int TAM_MAXIMO_DS_PROCESSO = 1000;
	//A constante 982 foi calculada pelo m�ximo de caracteres do campo dsProcesso (1000) menos a string " - Justificativa: "(18)
	private static final int TAM_DS_PROCESSO_JUSTIFICATIVA = 982;
	private static final String PA_SGR_VAL_EXIST_PGR = "SGR_VAL_EXIST_PGR";

	private static final Logger LOGGER = LogManager.getLogger(PlanoGerenciamentoRiscoService.class);

	private PlanoGerenciamentoRiscoUtils utils;
	private PlanoGerenciamentoRiscoDAO dao;
	private WorkflowPendenciaService workflowPendenciaService;
	private ControleCargaService controleCargaService;

	/*
	 * Resolu��o do PGR
	 */

	/**
	 * Encontra moeda e pais, e chama o m�todo "pai".
	 * 
	 * @see #generateEnquadramentoRegra(Long, Long, Long, boolean, boolean)
	 */
	public PlanoGerenciamentoRiscoDTO generateEnquadramentoRegra(Long idControleCarga, Boolean blVerificacaoGeral, Boolean blRealizaBloqueios) {
		Object[] moedaPais = controleCargaService.findMoedaPaisByControleCarga(idControleCarga);
		Long idMoeda = (Long) moedaPais[0];
		Long idPais = (Long) moedaPais[1];
		return generateEnquadramentoRegra(idControleCarga, idMoeda, idPais, blVerificacaoGeral, blRealizaBloqueios);
	}

	/**
	 * Processa o {@link PlanoGerenciamentoRiscoDTO} e retorna {@link String}
	 * JSON correspondente.
	 * 
	 * @param idControleCarga
	 *            Id do {@link ControleCarga}.
	 * @return {@String} JSON do {@link PlanoGerenciamentoRiscoDTO}.
	 */
	public String generateEnquadramentoRegraJson(Long idControleCarga) {
		PlanoGerenciamentoRiscoDTO plano = generateEnquadramentoRegra(idControleCarga, true, false);
		generateExigenciasGerRisco(plano);
		////
		plano.addMensagemEnquadramento("mensagem 1");
		plano.addMensagemEnquadramento("mensagem 2");
		plano.addMensagemEnquadramento("mensagem 3");
		plano.addMensagemEnquadramento("mensagem 4");
		plano.addMensagemWorkflowRegras("mensagem 11");
		plano.addMensagemWorkflowRegras("mensagem 22");
		plano.addMensagemWorkflowRegras("mensagem 33");
		plano.addMensagemWorkflowExigencias("mm");
		////
		return plano.writeAsString();
	}

	/**
	 * A partir de um {@link ControleCarga} processa o equadramentos das
	 * {@link EnquadramentoRegra}s e das {@link FaixaDeValor}es, seguido da
	 * resolu��o das {@link ExigenciaGerRisco}s, retornando os resultados
	 * completos para o Plano de Gerenciamento de Risco estruturado em um
	 * {@link PlanoGerenciamentoRiscoDTO}, que inclui:
	 * <ul>
	 * <li>Argumentos de entrada para o m�todo;
	 * <li>{@link ParametroGeral}(is) e de {@link ParametroFilial}(ais)
	 * utilizados no processamento;
	 * <li>{@link DoctoServicoDTO}s relacionados ao {@link ControleCarga}
	 * resgatados da base de dados;
	 * <li>{@link EnquadramentoRegra}s vigentes de {@link Cliente}s e gerais
	 * resgatadas da base de dados;
	 * <li>Resultados intermedi�rios e finais sobre o enquadramento das
	 * {@link EnquadramentoRegra}s e {@link FaixaDeValor}es;
	 * <li>Estruturas de dados para situa��es de bloqueio e de workflow para o
	 * enquadramento de regras;
	 * <li>Resultados intermedi�rios e finais sobre a resolu��o das
	 * {@link ExigenciaGerRisco}s;
	 * <li>Estruturas de dados para situa��es de workflow para a resolu��o de
	 * exig�ncias;
	 * <li>Mensagens textuais sobre situa��es de bloqueios e de workflow;
	 * <li>Dados para avalia��o de desempenho sobre cada etapa do processamento.
	 * </ul>
	 * 
	 * @param idControleCarga
	 *            Id do {@link ControleCarga} para busca dos
	 *            {@link DoctoServicoDTO}s.
	 * @param idMoeda
	 *            Id da {@link Moeda} para convers�o dos valores.
	 * @param idPais
	 *            Id do {@link Pais} relativo � convers�o de {@link Moeda}.
	 * @param blVerificacaoGeral
	 *            Se {@code true} processa enquadramento para valor total to
	 *            {@link ControleCarga}, caso {@code false} processa apenas o
	 *            enquadramento para {@link DoctoServicoDTO}s individuais. soma
	 *            os valores de todos os {@link DoctoServico}s do
	 *            {@link ControleCarga} para obter um �nico
	 *            {@link EnquadramentoRegra}, caso {@code false} verifica
	 *            isoladamente cada {@link DoctoServico} obtendo um
	 *            {@link EnquadramentoRegra} para cada um.
	 * @param blRealizaBloqueios
	 *            Se {@code true} realiza o bloqueio para as valida��es da
	 *            rotina, caso {@code false} n�o realiza qualquer bloqueio.
	 * @return {@link PlanoGerenciamentoRiscoDTO} contendo todos os par�metros
	 *         de entrada e resultados intermedi�rios e finais do Plano de
	 *         Gerenciamento de Risco.
	 * 
	 * @see #generateEnquadramentoRegra(Long, Long, Long, boolean, boolean)
	 * @see #generateExigenciasGerRisco(PlanoGerenciamentoRiscoDTO)
	 */
	public PlanoGerenciamentoRiscoDTO generateExigenciasGerRisco(
			Long idControleCarga, Long idMoeda, Long idPais, boolean blVerificacaoGeral, boolean blRealizaBloqueios) {
		PlanoGerenciamentoRiscoDTO plano = generateEnquadramentoRegra(idControleCarga, idMoeda, idPais, blVerificacaoGeral, blRealizaBloqueios);
		generateExigenciasGerRisco(plano);
		return plano;
	}

	/**
	 * A partir de um {@link ControleCarga} gera o enquadramento de regras do
	 * Plano de Gerenciamento de Risco:
	 * <ol>
	 * <li>Prepara DTO para resultados do enquadramento;
	 * <li>Busca {@link DoctoServicoDTO}s relacionados a determinado
	 * {@link ControleCarga};
	 * <li>Busca {@link ParametroGeral}(is) e {@link ParametroFilial}(ais)
	 * necess�rios ao processamento;
	 * <li>Busca {@link EnquadramentoRegra}s de {@link Cliente}s vigentes;
	 * <li>Busca {@link EnquadramentoRegra}s gerais vigentes;
	 * <li>Executa o enquadramento de regras.
	 * </ol>
	 * 
	 * @param idControleCarga
	 * @param idMoeda
	 * @param idPais
	 * @param blVerificacaoGeral
	 * @param blRealizaBloqueios
	 * @return
	 * 
	 * @see #generateExigenciasGerRisco(Long, Long, Long, boolean, boolean)
	 */
	public PlanoGerenciamentoRiscoDTO generateEnquadramentoRegra(
			Long idControleCarga, Long idMoeda, Long idPais, boolean blVerificacaoGeral, boolean blRealizaBloqueios) {
		// Prepara DTO para resultados do enquadramento
		PlanoGerenciamentoRiscoDTO plano = new PlanoGerenciamentoRiscoDTO();
		plano.startCronometro("parametros");
		plano.setIdControleCarga(idControleCarga);
		plano.setIdMoedaDestino(idMoeda);
		plano.setIdPaisDestino(idPais);
		plano.setBlVerificacaoGeral(BooleanUtils.isTrue(blVerificacaoGeral));
		plano.setBlRealizaBloqueios(BooleanUtils.isTrue(blRealizaBloqueios));

		// Busca par�metros gerais e de filial
		plano.setBlPermitePGRDuplicado(utils.findParametroGeral("PGR_PERM_PGR_DUPL"));
		plano.setBlValidaMaximoCarregamento(utils.findParametroFilial("PGR_VAL_MAX_CARR"));
		plano.setBlValidaCargaExclusivaCliente(utils.findParametroFilial("PGR_VAL_CARG_EXCL"));
		plano.setBlRequerLiberacaoCemop(utils.findParametroFilial("PGR_VAL_FAIXA_CEMOP"));
		plano.setBlValidaNaturezaImpedida(utils.findParametroFilial("PGR_VAL_NATUREZA_IMP"));
		plano.stopCronometro();

		// Busca documentos do controle de carga
		plano.startCronometro("documentos");
		plano.setDocumentos(findDoctoServico(idControleCarga));
		plano.stopCronometro();

		// Busca regras de clientes e regras gerais
		plano.startCronometro("regras");
		plano.setRegrasCliente(findEnquadramentoRegra(false));
		if (plano.isBlVerificacaoGeral()) {
			plano.setRegrasGeral(findEnquadramentoRegra(true));
		}
		plano.stopCronometro();

		// Executa enquadramento de faixa sobre DTO
		executeEnquadramentoRegra(plano);
		return plano;
	}

	/**
	 * A partir do {@link PlanoGerenciamentoRiscoDTO} identifica quais
	 * {@link EnquadramentoRegra}s e {@link FaixaDeValor}es devem ser aplicadas,
	 * procedendo os seguintes passos:
	 * <ol>
	 * <li>Realiza a convers�o dos valores para a {@link Moeda} com o
	 * {@link Pais} correspondente, considerando o {@link Pais} de origem de
	 * cada {@link DoctoServicoDTO};
	 * <li>Para cada {@link DoctoServicoDTO}, filtra os
	 * {@link EnquadramentoRegra}s compat�veis de acordo com as regras de
	 * enquadramento;
	 * <li>Conforme par�metro {@code blVerificacaoGeral}, processa enquadramento
	 * para valor total do {@link ControleCarga};
	 * <li>Produz mensagens relativas ao processo de enquadramento de regras;
	 * <li>Conforme par�metro {@code blRealizaBloqueios}, trata exce��es no
	 * processo de enquadramento de regras apresentando mensagens e
	 * interrompendo o processo;
	 * <li>Conforme {@link ParametroFilial} "PGR_VAL_MAX_CARR", processa
	 * verifica��o do limite m�ximo de carregamento para {@link ApoliceSeguro}s
	 * e {@link SeguroCliente}s vinculadas a {@link EnquadramentoRegra}
	 * encontradas;
	 * <li>Conforme {@link ParametroFilial} "PGR_VAL_CARG_EXCL", processa
	 * verifica��o de carga exclusiva do cliente conforme atributo
	 * {@code blExclusivaCliente} das {@link FaixaDeValor}es obtidas no
	 * enquadramento;
	 * <li>Conforme {@link ParametroFilial} "PGR_VAL_FAIXA_CEMOP", processa
	 * verifica��o das {@link FaixaDeValor}es obtidas no enquadramento conforme
	 * atributo {@code blRequerLiberacaoCemop};
	 * <li>Produz mensagens relativas ao workflow necess�rio ao processo.
	 * </ol>
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} com par�metros de entrada e
	 *            para resultados do processo de enquadramento de regras.
	 */
	void executeEnquadramentoRegra(PlanoGerenciamentoRiscoDTO plano) {
		// Converte moeda origem/destino dos documentos
		plano.startCronometro("enquadramento");
		executeConversaoMoeda(plano);
		
		// Enquadramento de documentos, verifica��o geral do controle de carga e enquadramento de faixas de valores 
		for (DoctoServicoDTO documento : plano.getDocumentos()) {
			executeEnquadramentoDocumento(documento, plano);
		}
		if (plano.isBlVerificacaoGeral()) {
			executeVerificacaoGeral(plano);
		}
		executeEnquadramentoFaixa(plano);

		// Limite m�ximo de carregamento, carga exclusiva do cliente e libera��o do CEMOP para faixa de valor
		if (plano.isBlValidaMaximoCarregamento()) {
			executeLimiteControleCarga(plano);
		}
		if (!CollectionUtils.isEmpty(plano.getEnquadramentos())) {
			if (plano.isBlValidaCargaExclusivaCliente()) {
				executeCargaExclusiva(plano);
			}
			if (plano.isBlRequerLiberacaoCemop()) {
				executeFaixaLiberacaoCemop(plano);
			}
			if (plano.isBlValidaNaturezaImpedida()) {
				executeNaturezaImpedida(plano);
			}
		}
		plano.stopCronometro();

		// Mensagens geradas no enquadramentos de regras e de workflow
		plano.startCronometro("mensagens");
		if (plano.isBlRealizaBloqueios()) {
			executeMensagensEnquadramento(plano);
			executeMensagensWorkflowRegras(plano);
		}
		plano.stopCronometro();
	}

	/**
	 * Processa {@link DoctoServicoDTO}s buscando {@link EnquadramentoRegra}s
	 * compat�veis conforme regras de filtro. Filtra o conjunto de regras de
	 * clientes encontrando as compat�veis e identificando situa��es de regras
	 * n�o encontradas ou regras m�ltiplas. Totaliza os valores de mercadoria
	 * por {@link EnquadramentoRegra}, {@link NaturezaProduto} e tipo de
	 * opera��o.
	 * 
	 * @param documento
	 *            {@link DoctoServicoDTO}s para enquadramento.
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} resultado do enquadramento.
	 * 
	 * @see EnquadramentoDocumentoTest
	 */
	void executeEnquadramentoDocumento(DoctoServicoDTO documento, PlanoGerenciamentoRiscoDTO plano) {
		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>(plano.getRegrasCliente());
		filterEnquadramentoRegra(documento, regras);
		if (regras.isEmpty()) {
			plano.addNaoEncontrado(documento);
		} else if (regras.size() > 1) {
			plano.addDuplicado(documento);
		}

		BigDecimal vlMercadoria = documento.getVlMercadoria();
		for (EnquadramentoRegra regra : regras) {
			plano.putDocumentoRegra(regra, documento);
			plano.sumTotalRegra(regra, vlMercadoria);
		}
		plano.sumTotalNaturezaProduto(documento.getIdNaturezaProduto(), vlMercadoria);
		plano.sumTotalTpOperacao(documento.getTpOperacao(), vlMercadoria);
		LOGGER.debug(plano);
	}

	/**
	 * Processa verifica��o geral do {@link ControleCarga} buscando
	 * {@link EnquadramentoRegra}s compat�veis conforme regras de filtro.
	 * Totaliza os valores de mercadorias de todo o {@link ControleCarga},
	 * somando os subtotais por tipo de opera��o. Filtra o conjunto de regras
	 * gerais (n�o de clientes) encontrando as compat�veis.
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} para verifica��o.
	 * 
	 * @see VerificacaoGeralTest
	 */
	void executeVerificacaoGeral(PlanoGerenciamentoRiscoDTO plano) {
		if (plano.getDocumentos().isEmpty()) {
			return;
		}
		DoctoServicoDTO documento = new DoctoServicoDTO(plano.getDocumentos().iterator().next());
		BigDecimal vlMercadoria = plano.getTotalDocumentos();
		documento.setVlMercadoria(vlMercadoria);

		List<EnquadramentoRegra> regras = new ArrayList<EnquadramentoRegra>(plano.getRegrasGeral());
		filterEnquadramentoRegra(documento, regras);
		if (regras.size() > 1) {
			plano.addDuplicado(documento);
		}
		for (EnquadramentoRegra regra : regras) {
			plano.putDocumentoRegra(regra, documento);
			plano.sumTotalRegra(regra, vlMercadoria);
		}
	}

	/**
	 * Processa {@link EnquadramentoRegra}s encontrando {@link FaixaDeValor} de
	 * acordo com valor total de mercadoria e condi��o de coleta/entrega
	 * exclusiva aeroporto. Inclui {@link FaixaDeValor} nos resultado do
	 * processamento para o Plano de Gerenciamento de Risco. Caso nenhuma
	 * {@link FaixaDeValor} compat�vel for encontrada produz mensagem
	 * (LMS-11345) identificando o {@link EnquadramentoRegra} e o valor total da
	 * mercadoria.
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} para busca de
	 *            {@link FaixaDeValor}es.
	 */
	private void executeEnquadramentoFaixa(PlanoGerenciamentoRiscoDTO plano) {
		for (EnquadramentoRegra regra : plano.getRegrasEnquadramento()) {
			BigDecimal vlMercadoria = plano.getTotalRegra(regra);
			boolean blExclusivaAeroporto = getBlExclusivaAeroporto(plano.getDocumentosRegra(regra));
			FaixaDeValor faixa = getFaixaDeValor(plano, regra, vlMercadoria, blExclusivaAeroporto);
			if (faixa == null) {
				plano.addFaixaInexistente(regra);
			} else {
				plano.addEnquadramento(new EnquadramentoRegraDTO(regra, faixa, vlMercadoria));
			}
		}
	}

	/**
	 * Verifica se algum {@link DoctoServicoDTO} � de coleta/entrega aeroporto,
	 * de acordo com as seguintes regras:
	 * <ul>
	 * <li>Tipo de opera��o "coleta" e possui AWB; ou
	 * <li>Tipo de opera��o "entrega" e tipo de pedido de coleta � "aeroporto".
	 * </ul>
	 * 
	 * @param documentos
	 *            {@link DoctoServicoDTO}s para verifica��o.
	 * @return {@code true} se algum {@link DoctoServicoDTO} for de
	 *         coleta/entrega aeroporto, {@code false} caso contr�rio.
	 */
	private boolean getBlExclusivaAeroporto(Collection<DoctoServicoDTO> documentos) {
		for (DoctoServicoDTO documento : documentos) {
			String tpOperacao = documento.getTpOperacao();
			if (TP_OPERACAO_ENTREGA.equals(tpOperacao) && documento.getIdAwb() != null
					|| TP_OPERACAO_COLETA.equals(tpOperacao) && TP_PEDIDO_COLETA_AEROPORTO.equals(documento.getTpPedidoColeta())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Encontra {@link FaixaDeValor} para enquadramento de um valor espec�fico e
	 * com op��o para "exclusiva aeroporto". Se o par�metro
	 * {@code blExclusivaAeroporto} for {@code false} somente ser�o consideradas
	 * as {@link FaixaDeValor}es n�o exclusiva para coleta/entrega aeroporto,
	 * caso contr�rio todas as {@link FaixaDeValor}es s�o consideradas.
	 * 
	 * @param faixas
	 *            {@link FaixaDeValor}es para processamento.
	 * @param vlMercadoria
	 *            Valor total da mercadoria para busca da {@link FaixaDeValor}.
	 * @param blExclusivaAeroporto
	 *            Determina inclus�o na busca de {@link FaixaDeValor}es
	 *            exclusivas para coleta/entrega aeroporto.
	 * @return {@link FaixaDeValor} conforme valor total e op��o
	 *         "exclusiva aeroporto", ou {@code null} se n�o houver
	 *         {@link FaixaDeValor} compat�vel.
	 */
	private FaixaDeValor getFaixaDeValor(
			PlanoGerenciamentoRiscoDTO plano, EnquadramentoRegra regra, BigDecimal vlMercadoria, boolean blExclusivaAeroporto) {
		if (!CollectionUtils.isEmpty(regra.getFaixaDeValors())) {
			Long idMoeda = plano.getIdMoedaDestino();
			Long idPais = plano.getIdPaisDestino();
			YearMonthDay dataConversao = plano.getDataConversao();
			for (FaixaDeValor faixa : regra.getFaixaDeValors()) {
				if (faixa.getBlExclusivaAeroporto() && !blExclusivaAeroporto) {
					continue;
				}
				Long idMoedaOrigem = regra.getMoeda().getIdMoeda();
				BigDecimal vlMinimo = utils.findConversaoMoeda(
						idPais, idMoedaOrigem, idPais, idMoeda, dataConversao, faixa.getVlLimiteMinimo());
				BigDecimal vlMaximo = utils.findConversaoMoeda(
						idPais, idMoedaOrigem, idPais, idMoeda, dataConversao, faixa.getVlLimiteMaximo());
				if (CompareUtils.le(vlMinimo, vlMercadoria) && (vlMaximo == null || CompareUtils.ge(vlMaximo, vlMercadoria))) {
					Hibernate.initialize(faixa.getExigenciaFaixaValors());
					return faixa;
				}
			}
		}
		return null;
	}

	/**
	 * Verifica procedimentos do Plano de Gerenciamento de Risco, produzindo
	 * mensagens para as seguintes situa��es:
	 * <ul>
	 * <li>Nenhum {@link EnquadramentoRegra} encontrado para
	 * {@link DoctoServicoDTO} (LMS-11340 e LMS-11341);
	 * <li>M�ltiplos {@link EnquadramentoRegra}s encontrados para
	 * {@link DoctoServicoDTO} (LMS-11342 e LMS-11341);
	 * <li>Nenhuma {@link FaixaDeValor} encontrada no {@link EnquadramentoRegra}
	 * para o valor total da mercadoria e condi��o de coleta/entrega aeroporto
	 * (LMS-11338 e LMS-11345).
	 * </ul>
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} para verifica��o.
	 */
	private void executeMensagensEnquadramento(PlanoGerenciamentoRiscoDTO plano) {
		if (!CollectionUtils.isEmpty(plano.getNaoEncontrados())) {
			plano.addMensagemEnquadramento(utils.getMensagem("LMS-11340"));
			for (DoctoServicoDTO documento : plano.getNaoEncontrados()) {
				plano.addMensagemEnquadramento(utils.getMensagemDocumento("LMS-11341", documento));
			}
		}
		if (!CollectionUtils.isEmpty(plano.getDuplicados()) && !plano.isBlPermitePGRDuplicado()) {
			plano.addMensagemEnquadramento(utils.getMensagem("LMS-11342"));
			for (DoctoServicoDTO documento : plano.getDuplicados()) {
				plano.addMensagemEnquadramento(utils.getMensagemDocumento("LMS-11341", documento));
			}
		}
		if (!CollectionUtils.isEmpty(plano.getFaixaInexistente())) {
			plano.addMensagemEnquadramento(utils.getMensagem("LMS-11338"));
			for (EnquadramentoRegra regra : plano.getFaixaInexistente()) {
				plano.addMensagemEnquadramento(utils.getMensagemRegra("LMS-11345", regra, plano.getTotalRegra(regra)));
			}
		}
	}

	/**
	 * Para cada {@link EnquadramentoRegra} verifica se o valor total est� acima
	 * do valor limite para o {@link ControleCarga} das respectivas
	 * {@link ApoliceSeguro} ou {@link SeguroCliente}, neste caso registrando o
	 * {@link EnquadramentoRegra} na cole��o "limite ap�lice".
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} para verifica��o.
	 */
	private void executeLimiteControleCarga(PlanoGerenciamentoRiscoDTO plano) {
		BigDecimal vlMercadoria = plano.getTotalDocumentos();
		for (EnquadramentoRegra regra : plano.getRegrasEnquadramento()) {
			BigDecimal vlLimite = regra.getVlLimiteControleCarga();
			if (vlLimite != null && CompareUtils.lt(vlLimite, vlMercadoria)) {
				plano.addLimiteApolice(regra);
			}
		}
	}

	/**
	 * Para cada {@link EnquadramentoRegra} em que a {@link FaixaDeValor} de
	 * enquadramento tiver a op��o {@code blExclusivaCliente}, verifica se
	 * existem outros {@link EnquadramentoRegra}s que n�o referenciem a mesma
	 * {@link ApoliceSeguro} ou {@link SeguroCliente}, neste caso registrando o
	 * {@link EnquadramentoRegra} na cole��o "carga exclusiva".
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} para verifica��o.
	 */
	private void executeCargaExclusiva(PlanoGerenciamentoRiscoDTO plano) {
		for (EnquadramentoRegraDTO enquadramento : plano.getEnquadramentos()) {
			if (BooleanUtils.isTrue(enquadramento.getFaixaDeValor().getBlExclusivaCliente())) {
				EnquadramentoRegra regra = enquadramento.getEnquadramentoRegra();
				if (!isCargaExclusiva(regra, plano.getRegrasEnquadramento())) {
					plano.addCargaExclusiva(regra);
				}
			}
		}
	}

	/**
	 * Verifica se todos os {@link EnquadramentoRegra}s tem a mesma
	 * {@link ApoliceSeguro} ou a mesma {@link SeguroCliente}, comparando
	 * {@link TipoSeguro}, {@link Seguradora} e n�mero/descri��o da ap�lice.
	 * 
	 * @param regra
	 *            {@link EnquadramentoRegra} para verifica��o.
	 * @param regras
	 *            {@link EnquadramentoRegra}s para verifica��o.
	 * @return {@code true} se todos os {@link EnquadramentoRegra}s tiverem a
	 *         mesma {@link ApoliceSeguro} ou o mesmo {@link SeguroCliente},
	 *         caso contr�rio retorna {@code false}.
	 */
	private boolean isCargaExclusiva(EnquadramentoRegra regra, Collection<EnquadramentoRegra> regras) {
		for (EnquadramentoRegra regra2 : regras) {
			if (!regra.equalsApolice(regra2)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Para cada {@link EnquadramentoRegra} em que a {@link FaixaDeValor} de
	 * enquadramento tiver a op��o {@code blRequerLiberacaoCemop}, registra o
	 * {@link EnquadramentoRegra} na cole��o "libera��o CEMOP".
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} para verifica��o.
	 */
	private void executeFaixaLiberacaoCemop(PlanoGerenciamentoRiscoDTO plano) {
		for (EnquadramentoRegraDTO enquadramento : plano.getEnquadramentos()) {
			if (BooleanUtils.isTrue(enquadramento.getFaixaDeValor().getBlRequerLiberacaoCemop())) {
				plano.addLiberacaoCemop(enquadramento.getEnquadramentoRegra());
			}
		}
	}

	/**
	 * Para cada {@link EnquadramentoRegra} em que a {@link FaixaDeValor} de
	 * enquadramento tiver {@link NaturezaProduto} impedidas, caso exista valor limite
	 * na {@link FaixaValorNaturezaImpedida}, se valor totalizado
	 * naquela {@link NaturezaProduto} � superior. Se n�o houver valor limite ou se o valor totalizado for superior ao limite, registra
	 * na cole��o "natureza impedida" o {@link EnquadramentoRegra} relacionado � {@link NaturezaProduto} impedida.
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} para verifica��o.
	 */
	private void executeNaturezaImpedida(PlanoGerenciamentoRiscoDTO plano) {
		for (EnquadramentoRegraDTO enquadramento : plano.getEnquadramentos()) {
			EnquadramentoRegra regra = enquadramento.getEnquadramentoRegra();
			FaixaDeValor faixa = enquadramento.getFaixaDeValor();
			for (FaixaValorNaturezaImpedida naturezaImpedida : faixa.getNaturezasImpedidas()) {
				NaturezaProduto naturezaProduto = naturezaImpedida.getNaturezaProduto();
				BigDecimal vlTotal = plano.getTotalNaturezaProduto(naturezaProduto.getIdNaturezaProduto());
				BigDecimal vlLimite = naturezaImpedida.getVlLimitePermitido();
				if (CompareUtils.gt(vlTotal, vlLimite == null ? BigDecimal.ZERO : vlLimite)) {
					plano.putNaturezaImpedida(regra, naturezaProduto);
				}
			}
		}
	}

	/**
	 * Verifica workflow do Plano de Gerenciamento de Risco, produzindo
	 * mensagens para as seguintes situa��es:
	 * <ul>
	 * <li>Ultrapassado o valor limite especificado na {@link ApoliceSeguro} ou
	 * no {@link SeguroCliente} (LMS-11344);
	 * <li>Impedimento para carga exclusiva de acordo com {@link FaixaDeValor}
	 * do enquadramento (LMS-11343);
	 * <li>{@link FaixaDeValor} do enquadramento requer libera��o do CEMOP
	 * (LMS-11346);
	 * <li>Impedimento para {@link NaturezaProduto} de acordo com
	 * {@link FaixaDeValor} do enquadramento (LMS-11334).
	 * </ul>
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} para verifica��o.
	 */
	private void executeMensagensWorkflowRegras(PlanoGerenciamentoRiscoDTO plano) {
		if (!CollectionUtils.isEmpty(plano.getLimiteApolice())) {
			for (EnquadramentoRegra regra : plano.getLimiteApolice()) {
				BigDecimal vlMercadoria = plano.getTotalDocumentos();
				plano.addMensagemWorkflowRegras(utils.getMensagemApolice("LMS-11344", regra, vlMercadoria));
			}
		}
		if (!CollectionUtils.isEmpty(plano.getCargaExclusiva())) {
			for (EnquadramentoRegra regra : plano.getCargaExclusiva()) {
				plano.addMensagemWorkflowRegras(utils.getMensagemApolice("LMS-11343", regra, null));
			}
		}
		if (!CollectionUtils.isEmpty(plano.getLiberacaoCemop())) {
			for (EnquadramentoRegra regra : plano.getLiberacaoCemop()) {
				BigDecimal vlMercadoria = plano.getTotalRegra(regra);
				plano.addMensagemWorkflowRegras(utils.getMensagemRegra("LMS-11346", regra, vlMercadoria));
			}
		}
		if (plano.getNaturezasImpedidas() != null && !CollectionUtils.isEmpty(plano.getNaturezasImpedidas().keySet())) {
			for (EnquadramentoRegra regra : plano.getNaturezasImpedidas().keySet()) {
				for (NaturezaProduto naturezaProduto : plano.getNaturezasImpedidas(regra)) {
					plano.addMensagemWorkflowRegras(utils.getMensagemNatureza("LMS-11334", regra, naturezaProduto));
				}
			}
		}
	}

	/*
	 * Exig�ncias de Faixas de Valor
	 */

	/**
	 * A partir do conjunto de {@link ExigenciaFaixaValor}es processa a
	 * resolu��o de {@link ExigenciaGerRiscoDTO}s preparando as estruturas de
	 * dados de relacionamentos com {@link EnquadramentoRegra},
	 * {@link TipoExigenciaGerRisco} e {@link ExigenciaGerRisco}.
	 * 
	 * @param plano
	 *            {@link PlanoGerenciamentoRiscoDTO} para resolu��o de
	 *            {@link ExigenciaGerRiscoDTO}s.
	 * 
	 * @see #generateExigenciasGerRisco(Long, Long, Long, boolean, boolean)
	 */
	public void generateExigenciasGerRisco(PlanoGerenciamentoRiscoDTO plano) {
		plano.startCronometro("exigencias");
		Collection<EnquadramentoRegraDTO> enquadramentos = plano.getEnquadramentos();
		if (enquadramentos == null) {
			return;
		}
		List<ExigenciaGerRiscoDTO> dtos = new ArrayList<ExigenciaGerRiscoDTO>();
		for (ExigenciaFaixaValor exigencia : findExigenciaFaixaValor(enquadramentos)) {
			ExigenciaGerRiscoDTO dto = new ExigenciaGerRiscoDTO(exigencia);
			plano.putExigenciaRegra(dto.getEnquadramentoRegra(), dto);
			dtos.add(dto);
		}
		if (dtos.isEmpty()) {
			return;
		}
		reduceExigenciasGerRisco(dtos);
		for (ExigenciaGerRiscoDTO dto : dtos) {
			plano.addExigencia(dto);
			plano.putExigenciaTipo(dto.getTipoExigenciaGerRisco(), dto);
			plano.putExigenciaGrupo(dto.getExigenciaGerRisco(), dto);
		}
		plano.stopCronometro();
	}

	/**
	 * Processa a resolu��o de um conjunto de {@link ExigenciaFaixaValor}es de
	 * acordo com as regras do Plano de Gerenciamento de Riscos, cruzando cada
	 * par de {@link ExigenciaFaixaValor} enquanto houver redu��o.
	 * 
	 * @param dtos
	 *            {@link ExigenciaGerRiscoDTO}s para cruzamento e redu��o.
	 * 
	 * @see ExigenciasGerRiscoTest
	 */
	void reduceExigenciasGerRisco(List<ExigenciaGerRiscoDTO> dtos) {
		boolean repeat;
		do {
			repeat = false;
			for (int n = 0; n < dtos.size(); ++n) {
				ExigenciaGerRiscoDTO dto1 = dtos.get(n);
				for (int m = n + 1; m < dtos.size(); ++m) {
					ExigenciaGerRiscoDTO dto2 = dtos.get(m);
					List<ExigenciaGerRiscoDTO> result = reduceExigenciasGerRisco(dto1, dto2);
					if (!result.isEmpty()) {
						for (ExigenciaGerRiscoDTO dto : result) {
							if (!dto.equals(dto1) && !dto.equals(dto2)) {
								dtos.add(dto);
							}
						}
						if (!result.contains(dto2)) {
							dtos.set(m, null);
							repeat = true;
						}
						if (!result.contains(dto1)) {
							dtos.set(n, null);
							repeat = true;
							break;
						}
					}
				}
			}
			CollectionUtils.filter(dtos, PredicateUtils.notNullPredicate());
		} while(repeat);
	}

	/**
	 * Executa a redu��o sobre dois {@link ExigenciaGerRiscoDTO}s com as
	 * seguintes verifica��es em sequ�ncia:
	 * <ol>
	 * <li>N�o h� redu��o se qualquer {@link ExigenciaGerRiscoDTO} for igual a
	 * {@code null};
	 * <li>N�o h� redu��o para {@link ExigenciaGerRisco}s relacionadas a mesma
	 * {@link EnquadramentoRegra};
	 * <li>N�o h� redu��o para {@link ExigenciaGerRisco}s de <u>diferentes</u>
	 * {@link TipoExigenciaGerRisco};
	 * <li>N�o h� redu��o para {@link ExigenciaGerRisco}s iguais e refer�ncias
	 * para {@link Filial}(ais) diferentes;
	 * <li>{@link ExigenciaGerRisco}s iguais e com op��o {@code blAreaRisco} s�o
	 * reduzidas para a de maior valor no atributo {@code vlKmFranquia};
	 * <li>{@link ExigenciaGerRisco}s iguais s�o reduzidas tendo o atributo
	 * {@code qtExigida} somado ou de maior valor, conforme o atributo
	 * {@code tpCriterioAgrupamento};
	 * <li>{@link ExigenciaGerRisco}s diferentes s�o reduzidas de acordo com os
	 * atributos {@code nrNivel} e {@code qtExigida}, podendo resultar na
	 * {@link ExigenciaGerRisco} de n�vel mais baixo (maior prioridade) ou
	 * ambas, neste caso calculando a diferen�a no atributo {@code qtExigida} da
	 * {@link ExigenciaGerRisco} de nivel mais alto (menor prioridade).
	 * </ol>
	 * 
	 * @param dto1
	 * @param dto2
	 * @return Uma ou duas {@link ExigenciaGerRiscoDTO}s de acordo com a
	 *         redu��o, ou {@code null} se n�o houver redu��o.
	 * 
	 * @see ExigenciasGerRiscoTest
	 */
	List<ExigenciaGerRiscoDTO> reduceExigenciasGerRisco(ExigenciaGerRiscoDTO dto1, ExigenciaGerRiscoDTO dto2) {
		List<ExigenciaGerRiscoDTO> result = new ArrayList<ExigenciaGerRiscoDTO>();
		if (dto1 != null && dto2 != null
				&& !dto1.equalsEnquadramentoRegra(dto2) && dto1.equalsTipoExigenciaGerRisco(dto2)) {
			if (dto1.equalsExigenciaGerRisco(dto2)) {
				if (dto1.equalsFilialInicio(dto2)) {
					reduceExigenciasGerRiscoAgrupamento(dto1, dto2, result);
				}
			} else if (dto1.getTipoExigenciaGerRisco().getBlControleNivel()) {
				reduceExigenciasGerRiscoNivel(dto1, dto2, result);
			}
		}
		return result;
	}

	/**
	 * Executa a redu��o sobre dois {@link ExigenciaGerRiscoDTO}s de mesmas
	 * {@link ExigenciaGerRisco} e relacionando a mesma {@link Filial}, se
	 * existir. Para exig�ncias de �rea de risco considera a maior quilometragem
	 * de franquia. Considera a quantidade acumulada ou maior conforme o
	 * atributo {@code tpCriterioAgrupamento}.
	 * 
	 * @param dto1
	 * @param dto2
	 * @param result
	 *            {@link List} para registrar o resultado da redu��o.
	 */
	private void reduceExigenciasGerRiscoAgrupamento(
			ExigenciaGerRiscoDTO dto1, ExigenciaGerRiscoDTO dto2, List<ExigenciaGerRiscoDTO> result) {
		if (dto1.getExigenciaGerRisco().getBlAreaRisco()) {
			if (dto1.getQtExigida() == dto2.getQtExigida()) {
				if (dto1.getVlKmFranquia() >= dto2.getVlKmFranquia()) {
					result.add(dto1);
				} else {
					result.add(dto2);
				}
			} else {
				result.add(dto1.getQtExigida() > dto2.getQtExigida() ? dto1 : dto2);
			}
		}

		if (dto1.getExigenciaGerRisco().getBlAreaRisco()) {
			
		} else {
			if (TP_AGRUPAMENTO_ACUMULADO.equals(dto1.getExigenciaGerRisco().getTpCriterioAgrupamento().getValue())) {
				result.add(dto2.getQtExigida() == 0
						? dto1 : new ExigenciaGerRiscoDTO(dto1, dto1.getQtExigida() + dto2.getQtExigida()));
			} else {
				result.add(dto1.getQtExigida() >= dto2.getQtExigida() ? dto1 : dto2);
			}
		}
	}

	/**
	 * Executa a redu��o sobre dois {@link ExigenciaGerRiscoDTO}s com a op��o
	 * {@code blControlaNivel}. Considera o {@link ExigenciaGerRiscoDTO} de
	 * menor {@code nrNivel}, verificando a seguir a diferen�a nas nos valores
	 * do atributo {@code qtExigida}.
	 * 
	 * @param dto1
	 * @param dto2
	 * @param result
	 *            {@link List} para registrar o resultado da redu��o.
	 */
	private void reduceExigenciasGerRiscoNivel(
			ExigenciaGerRiscoDTO dto1, ExigenciaGerRiscoDTO dto2, List<ExigenciaGerRiscoDTO> result) {
		boolean b = dto1.getExigenciaGerRisco().getNrNivel().compareTo(dto2.getExigenciaGerRisco().getNrNivel()) <= 0;
		ExigenciaGerRiscoDTO dto1b = b ? dto1 : dto2;
		ExigenciaGerRiscoDTO dto2b = b ? dto2 : dto1;
		result.add(dto1b);
		if (dto1b.getQtExigida() < dto2b.getQtExigida()) {
			result.add(new ExigenciaGerRiscoDTO(dto2, dto2b.getQtExigida() - dto1b.getQtExigida()));
		}
	}
	
	/*
	 * Documentos de Servi�o
	 */

	/**
	 * Busca {@link DoctoServico}s associados a um {@link ControleCarga}
	 * produzindo {@link List} de {@link DoctoServicoDTO} com os resultados
	 * obtidos.
	 * 
	 * @param idControleCarga
	 *            Id do {@link ControleCarga}.
	 * @return {@link DoctoServicoDTO}s com dados dos {@link DoctoServico}s.
	 */
	private List<DoctoServicoDTO> findDoctoServico(Long idControleCarga) {
		boolean pgr_regra_fedex = utils.findParametroGeral("PGR_REGRAS_FEDEX");
		List<Object[]> dados = dao.findDoctoServico(idControleCarga, pgr_regra_fedex);
		List<DoctoServicoDTO> dtos = new ArrayList<DoctoServicoDTO>(dados.size());
		for (Object[] data : dados) {
			dtos.add(new DoctoServicoDTO(data));
		}
		return dtos;
	}

	/**
	 * Realiza a convers�o do atributo {@code vlMercadoria} de cada
	 * {@link DoctoServicoDTO} para {@link Moeda} e {@link Pais} espec�ficos,
	 * considerando {@link Moeda} e {@link Pais} de origem de cada
	 * {@link DoctoServico} bem como a data atual para cota��o.
	 * 
	 * @param plano
	 *            DTO para Plano de Gerenciamento de Risco.
	 */
	private void executeConversaoMoeda(PlanoGerenciamentoRiscoDTO plano) {
		YearMonthDay dataConversao = JTDateTimeUtils.getDataAtual();
		plano.setDataConversao(dataConversao);

		Long idMoedaDestino = plano.getIdMoedaDestino();
		Long idPaisDestino = plano.getIdPaisDestino();
		if (idMoedaDestino == null || idPaisDestino == null) {
			return;
		}

		for (DoctoServicoDTO documento : plano.getDocumentos()) {
			Long idMoedaOrigem = documento.getIdMoeda();
			Long idPaisOrigem = documento.getIdPais();
			if (!idMoedaDestino.equals(idMoedaOrigem) || !idPaisDestino.equals(idPaisOrigem)) {
				BigDecimal vlMercadoria = documento.getVlMercadoria();
				vlMercadoria = utils.findConversaoMoeda(
						idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino, dataConversao, vlMercadoria);
				documento.setVlMercadoria(vlMercadoria);
			}
		}
	}

	/*
	 * Enquadramento de Regras
	 */

	/**
	 * Busca {@link EnquadramentoRegra}s vigentes e do tipo "regra geral" ou
	 * n�o, conforme par�metro {@code blRegraGeral}. A carga de atributos de
	 * relacionamentos many-to-one e many-to-many � imediata, garantindo a
	 * disponibilidade dos atributos:
	 * <ul>
	 * <li>{@code moeda} (many-to-one {@link Moeda});
	 * <li>{@code naturezaProduto} (many-to-one {@link NaturezaProduto});
	 * <li>{@code municipioEnquadramentosOrigem} (many-to-many
	 * {@link Municipio});
	 * <li>{@code municipioEnquadramentosDestino} (many-to-many
	 * {@link Municipio});
	 * <li>{@code paisEnquadramentosOrigem} (many-to-many {@link Pais});
	 * <li>{@code paisEnquadramentosDestino} (many-to-many {@link Pais});
	 * <li>{@code unidadeFederativaEnquadramentosOrigem} (many-to-many
	 * {@link UnidadeFederativa});
	 * <li>{@code unidadeFederativaEnquadramentosDestino} (many-to-many
	 * {@link UnidadeFederativa});
	 * <li>{@code filialEnquadramentosOrigem} (many-to-many {@link Filial});
	 * <li>{@code filialEnquadramentosDestino} (many-to-many {@link Filial});
	 * <li>{@code clienteEnquadramentos} (many-to-many
	 * {@link ClienteEnquadramento});
	 * <li>{@code apoliceSeguro} (many-to-one {@link ApoliceSeguro});
	 * <li>{@code seguroCliente} (many-to-one {@link SeguroCliente});
	 * <li>{@code faixaDeValors} (many-to-many {@link FaixaDeValor}).
	 * </ul>
	 * 
	 * @param blRegraGeral
	 *            Especifica busca de {@link EnquadramentoRegra}s tipo
	 *            "regra geral" ou n�o.
	 * @return {@link EnquadramentoRegra}s vigentes e do tipo "regra geral" ou
	 *         n�o, conforme par�metro.
	 */
	private List<EnquadramentoRegra> findEnquadramentoRegra(boolean blRegraGeral) {
		return dao.findEnquadramentoRegra(blRegraGeral);
	}

	/**
	 * Busca as {@link ExigenciaFaixaValor} de determinadas {@link FaixaDeValor}
	 * referenciadas nos {@link EnquadramentoRegraDTO} obtidos no processo de
	 * enquadramento de regras do Plano de Gerenciamento de Risco.
	 * 
	 * @param enquadramentos
	 *            {@link EnquadramentoRegraDTO} para busca dos
	 *            {@link ExigenciaFaixaValor}.
	 * @return {@link ExigenciaFaixaValor} de determinadas {@link FaixaDeValor}.
	 */
	private List<ExigenciaFaixaValor> findExigenciaFaixaValor(Collection<EnquadramentoRegraDTO> enquadramentos) {
		LOGGER.debug(enquadramentos);
		List<Long> ids = new ArrayList<Long>(enquadramentos.size());
		for (EnquadramentoRegraDTO enquadramento : enquadramentos) {
			Long idFaixaDeValor = enquadramento.getFaixaDeValor().getIdFaixaDeValor();
			ids.add(idFaixaDeValor);
		}
		List<ExigenciaFaixaValor> exigencias = dao.findExigenciaFaixaValor(ids);
		LOGGER.debug(exigencias);
		return exigencias;
	}

	/*
	 * Filtros do PGR
	 */

	/**
	 * A partir de um {@link DoctoServicoDTO}, filtra
	 * {@link EnquadramentoRegra}s verificando as seguintes condi��es:
	 * <ol>
	 * <li>{@link Cliente} remetente, destinat�rio ou indeterminado;
	 * <li>Tipo de abrang�ncia espec�fico ou indeterminado;
	 * <li>{@link NaturezaProduto} espec�fica ou indeterminada;
	 * <li>Tipo de opera��o espec�fico ou indeterminado;
	 * <li>{@link Pais} de origem espec�fico ou indeterminado;
	 * <li>{@link UnidadeFederativa} de origem espec�fica ou indeterminada;
	 * <li>{@link Municipio} de origem espec�fico ou indeterminado;
	 * <li>{@link Filial} de origem espec�fica ou indeterminada;
	 * <li>{@link Pais} de destino espec�fico ou indeterminado;
	 * <li>{@link UnidadeFederativa} de destino espec�fica ou indeterminada;
	 * <li>{@link Municipio} de destino espec�fico ou indeterminado;
	 * <li>{@link Filial} de destino espec�fica ou indeterminada.
	 * </ol>
	 * Cada uma das condi��es acima � verificada em sequ�ncia.
	 * 
	 * @param documento
	 *            {@link DoctoServicoDTO} para filtro.
	 * @param regras
	 *            {@link EnquadramentoRegra}s a filtrar.
	 * @return {@link EnquadramentoRegra}s conforme aplica��o de filtros.
	 * 
	 * @see EnquadramentoRegraTest
	 */
	void filterEnquadramentoRegra(DoctoServicoDTO documento, Collection<EnquadramentoRegra> regras) {
		LOGGER.debug(documento);
		filterEnquadramentoRegraCliente(documento.getIdClienteRemetente(), documento.getIdClienteDestinatario(), regras);
		EnquadramentoRegraCollectionUtils.filterValorDominio(regras, "TpAbrangencia", documento.getTpAbrangencia());
		EnquadramentoRegraCollectionUtils.filterIdAtributo(regras, "NaturezaProduto", documento.getIdNaturezaProduto());
		EnquadramentoRegraCollectionUtils.filterValorDominio(regras, "TpOperacao", documento.getTpOperacao());
		EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_ORIGEM, "Pais", documento.getIdPaisOrigem());
		EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_ORIGEM, "UnidadeFederativa", documento.getIdUnidadeFederativaOrigem());
		EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_ORIGEM, "Municipio", documento.getIdMunicipioOrigem());
		EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_ORIGEM, "Filial", documento.getIdFilialOrigem());
		EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_DESTINO, "Pais", documento.getIdPaisDestino());
		EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_DESTINO, "UnidadeFederativa", documento.getIdUnidadeFederativaDestino());
		EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_DESTINO, "Municipio", documento.getIdMunicipioDestino());
		EnquadramentoRegraCollectionUtils.filterCriterioEnquadramento(regras, TP_CRITERIO_DESTINO, "Filial", documento.getIdFilialDestino());
		LOGGER.debug(regras);
	}

	/**
	 * Filtra {@link EnquadramentoRegra}s verificando no relacionamento
	 * many-to-many com {@link ClienteEnquadramento}s uma das seguintes
	 * condi��es:
	 * <ol>
	 * <li>{@link Cliente} conforme par�metro {@code idRemetente} e
	 * {@code tpIntegranteFrete} igual a {@code 'R'} (remetente) ou {@code 'A'}
	 * (remetente/destinat�rio);
	 * <li>{@link Cliente} conforme par�metro {@code idDestinatario} e
	 * {@code tpIntegranteFrete} igual a {@code 'D'} (destinat�rio) ou
	 * {@code 'A'} (remetente/destinat�rio);
	 * <li>{@link EnquadramentoRegra}s sem relacionamentos com
	 * {@link ClienteEnquadramento}.
	 * </ol>
	 * Cada uma das condi��es acima � verificada at� que a primeira seja
	 * atendida, procedendo o filtro no conte�do da {@link Collection} no
	 * par�metro {@code regras}.
	 * 
	 * @param idRemetente
	 *            Id do {@link Cliente} remetente (para condi��o 1).
	 * @param idDestinatario
	 *            Id do {@link Cliente} destinat�rio (para condi��o 2).
	 * @param regras
	 *            {@link EnquadramentoRegra}s a filtrar.
	 */
	private void filterEnquadramentoRegraCliente(Long idRemetente, Long idDestinatario, Collection<EnquadramentoRegra> regras) {
		if (EnquadramentoRegraCollectionUtils.filterClienteEnquadramento(regras, idRemetente, TP_INTEGRANTE_FRETE_REMETENTE)
				|| EnquadramentoRegraCollectionUtils.filterClienteEnquadramento(regras, idDestinatario, TP_INTEGRANTE_FRETE_DESTINATARIO)) {
			return;
		}
		EnquadramentoRegraCollectionUtils.filterClienteEnquadramento(regras);
	}

	public PlanoGerenciamentoRiscoDTO executeVerificarEnquadramentoRegra(Long idControleCarga) {
		PlanoGerenciamentoRiscoDTO plano = new PlanoGerenciamentoRiscoDTO();
		if (utils.findParametroFilial(PA_SGR_VAL_EXIST_PGR)) {
			plano = this.generateEnquadramentoRegra(idControleCarga, Boolean.TRUE, Boolean.TRUE);
		}
		return plano;
	}

	public boolean validateDeveGerarWorkflowRegras(PlanoGerenciamentoRiscoDTO planoPGR) {
		planoPGR.setBlGerarWorkflowRegras(validateDeveGerarWorkflow(planoPGR, CampoHistoricoWorkflow.PGR1));
		return planoPGR.isBlGerarWorkflowRegras();
	}

	public boolean validateDeveGerarWorkflowExigencias(PlanoGerenciamentoRiscoDTO planoPGR) {
		planoPGR.setBlGerarWorkflowExigencias(validateDeveGerarWorkflow(planoPGR, CampoHistoricoWorkflow.PGR2));
		return planoPGR.isBlGerarWorkflowExigencias();
	}
	
	/**
	 * Retorna <b>true</b> se deve gerar workflow para um controle de carga.
	 * 
	 * @param idControleCarga
	 * @param plano
	 * @return
	 */
	public boolean validateDeveGerarWorkflow(PlanoGerenciamentoRiscoDTO planoPGR, CampoHistoricoWorkflow campo) {
		DomainValue tpSituacaoPendencia = findTpSituacaoPendenciaPGRControleCarga(planoPGR.getIdControleCarga(), campo);
		if (tpSituacaoPendencia == null || ConstantesWorkflow.REPROVADO.equals(tpSituacaoPendencia.getValue())
				 || ConstantesWorkflow.CANCELADO.equals(tpSituacaoPendencia.getValue())) {
			return true;
		} else {
			if (ConstantesWorkflow.EM_APROVACAO.equals(tpSituacaoPendencia.getValue())) {
				throw new BusinessException("LMS-05332");
			}
		}
		//se n�o entrou em nenhum if acima, est� APROVADO.
		return false;
	}
	
	/**
	 * Retorna o tipo de situa��o de pendencia do Controle de Carga, se houver
	 * 
	 * @param idControleCarga
	 * 				Id do {@link ControleCarga} para verifica��o.
	 * 
	 */
	public DomainValue findTpSituacaoPendenciaPGRControleCarga(Long idControleCarga, CampoHistoricoWorkflow campo) {
		return dao.findTpSituacaoPendencia(idControleCarga, campo);
	}
	
	/**
	 * Cria��o de pend�ncia workflow para emiss�o de controle de carga
	 * 
	 * @param idControleCarga
	 * @param mensagemWorkflow
	 * @param justificativa
	 */
	
	public void generatePendenciaWorkflowEmissaoControleCarga(Long idControleCarga, String mensagemWorkflow,
			String justificativa) {
		this.generatePendenciaWorkflow(idControleCarga, 
				mensagemWorkflow, 
				justificativa, 
				CampoHistoricoWorkflow.PGR2,	
				ConstantesWorkflow.NR504_APROVACAO_EMISSAO_CONTROLE_CARGA);
	}
	/**
	 * Cria��o de pend�ncia workflow para finaliza��o do carregamento
	 * 
	 * @param idControleCarga
	 * @param mensagemWorkflow
	 * @param justificativa
	 */
	
	public void generatePendenciaWorkflowFinalizarCarregamento(Long idControleCarga, String mensagemWorkflow,
			String justificativa) {
		this.generatePendenciaWorkflow(idControleCarga, 
									   mensagemWorkflow, 
									   justificativa, 
									   CampoHistoricoWorkflow.PGR1,	
									   ConstantesWorkflow.NR503_APROVACAO_FINALIZAR_CARREGAMENTO);
	}

	private void generatePendenciaWorkflow(Long idControleCarga, String mensagemWorkflow, String justificativa,
			CampoHistoricoWorkflow campoHistoricoWorkflow, Short nrTipoEvento) {
		
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		DateTime dhLiberacao = JTDateTimeUtils.getDataHoraAtual();
		String dsProcesso = this.generateDsProcessoWorkflowCarregamento(mensagemWorkflow, justificativa);
		String dsObservacao = justificativa;

		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
		pendenciaHistoricoDTO.setIdFilial(idFilial);
		pendenciaHistoricoDTO.setNrTipoEvento(nrTipoEvento);
		pendenciaHistoricoDTO.setIdProcesso(idControleCarga);
		pendenciaHistoricoDTO.setDsProcesso(dsProcesso);
		pendenciaHistoricoDTO.setDhLiberacao(dhLiberacao);
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.CONTROLE_CARGA);
		pendenciaHistoricoDTO.setCampoHistoricoWorkflow(campoHistoricoWorkflow);
		pendenciaHistoricoDTO.setDsVlAntigo(null);
		pendenciaHistoricoDTO.setDsVlNovo(null);
		pendenciaHistoricoDTO.setDsObservacao(dsObservacao);

		workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
	}

	/**
	 * M�todo que retorna a mensagem de descria��o do processo de workflow,
	 * 
	 * @param mensagemWorkflow
	 * @param justificativa
	 * @return
	 */
	private String generateDsProcessoWorkflowCarregamento(String mensagemWorkflow, String justificativa) {
		StringBuilder sb = new StringBuilder();
		if ((mensagemWorkflow.length() + justificativa.length()) > TAM_MAXIMO_DS_PROCESSO) {
			String separadorJustificativa = " - Justificativa: ";
			sb.append(justificativa.substring(0, TAM_DS_PROCESSO_JUSTIFICATIVA - justificativa.length()))
			.append(separadorJustificativa)
			.append(justificativa);
		} else {
			sb.append(mensagemWorkflow);
			sb.append("- Justificativa: ");
			sb.append(justificativa);
		}
		return sb.toString();
	}
	
	/*
	 * Inje��o de Depend�ncias
	 */

	public void setPlanoGerenciamentoRiscoUtils(PlanoGerenciamentoRiscoUtils planoGerenciamentoRiscoUtils) {
		this.utils = planoGerenciamentoRiscoUtils;
	}
	
	public void setPlanoGerenciamentoRiscoDAO(PlanoGerenciamentoRiscoDAO planoGerenciamentoRiscoDAO) {
		this.dao = planoGerenciamentoRiscoDAO;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	
}
