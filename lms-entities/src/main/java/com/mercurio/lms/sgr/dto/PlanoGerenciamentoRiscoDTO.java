package com.mercurio.lms.sgr.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.YearMonthDay;
import org.springframework.util.StopWatch;

import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.ExigenciaFaixaValor;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.FaixaDeValor;
import com.mercurio.lms.sgr.model.TipoExigenciaGerRisco;
import com.mercurio.lms.sgr.util.EnquadramentoRegraObjectMapperUtil;
import com.mercurio.lms.sgr.util.PlanoGerenciamentoRiscoCollectionUtils;
import com.mercurio.lms.vendas.model.SeguroCliente;

/**
 * LMS-6850 - Data Transfer Object para resolução das regras no Plano de
 * Gerenciamento de Risco.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 *
 */
public class PlanoGerenciamentoRiscoDTO {

	private Long idControleCarga;
	private boolean blVerificacaoGeral;
	private boolean blRealizaBloqueios;

	private Long idMoedaDestino;
	private Long idPaisDestino;
	private YearMonthDay dataConversao;

	private boolean blPermitePGRDuplicado;
	private boolean blValidaMaximoCarregamento;
	private boolean blValidaCargaExclusivaCliente;
	private boolean blRequerLiberacaoCemop;
	private boolean blValidaNaturezaImpedida;
	private boolean blValidaExigenciasMonitoramento;
	private boolean blValidaExigenciasMotorista;
	private boolean blValidaExigenciasVeiculo; 
	private boolean blValidaExigenciasVirus; 
	
	private boolean blGerarWorkflowRegras;
	private boolean blGerarWorkflowExigencias;
	private boolean blGerarEnquadramentos;

	private Collection<DoctoServicoDTO> documentos;
	private Collection<DoctoServicoDTO> naoEncontrados;
	private Collection<DoctoServicoDTO> duplicados;
	private Collection<EnquadramentoRegra> faixaInexistente;

	private Collection<EnquadramentoRegra> regrasCliente;
	private Collection<EnquadramentoRegra> regrasGeral;

	private Map<EnquadramentoRegra, Collection<DoctoServicoDTO>> documentosRegra;
	private Map<EnquadramentoRegra, BigDecimal> totaisRegra;
	private Map<Long, BigDecimal> totaisNaturezaProduto;
	private Map<String, BigDecimal> totaisTpOperacao;

	private Collection<EnquadramentoRegraDTO> enquadramentos;

	private Collection<EnquadramentoRegra> limiteApolice;
	private Collection<EnquadramentoRegra> cargaExclusiva;
	private Collection<EnquadramentoRegra> liberacaoCemop;
	private Map<EnquadramentoRegra, Collection<NaturezaProduto>> naturezasImpedidas;

	private Map<EnquadramentoRegra, Collection<ExigenciaGerRiscoDTO>> exigenciasRegra;
	private Map<TipoExigenciaGerRisco, Collection<ExigenciaGerRiscoDTO>> exigenciasTipo;
	private Map<ExigenciaGerRisco, Collection<ExigenciaGerRiscoDTO>> exigenciasGrupo;
	private Collection<ExigenciaGerRiscoDTO> exigencias;

	private Collection<ExigenciaGerRiscoDTO> exigenciasVioladas;

	private Collection<String> mensagensEnquadramento;
	private Collection<String> mensagensWorkflowRegras;
	private Collection<String> mensagensWorkflowExigencias;

	private StopWatch cronometro;

	public Long getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(Long idControleCarga) {
		this.idControleCarga = idControleCarga;
	}

	public boolean isBlVerificacaoGeral() {
		return blVerificacaoGeral;
	}

	public void setBlVerificacaoGeral(boolean blVerificacaoGeral) {
		this.blVerificacaoGeral = blVerificacaoGeral;
	}

	public boolean isBlRealizaBloqueios() {
		return blRealizaBloqueios;
	}

	public void setBlRealizaBloqueios(boolean blRealizaBloqueios) {
		this.blRealizaBloqueios = blRealizaBloqueios;
	}

	public Long getIdMoedaDestino() {
		return idMoedaDestino;
	}

	public void setIdMoedaDestino(Long idMoedaDestino) {
		this.idMoedaDestino = idMoedaDestino;
	}

	public Long getIdPaisDestino() {
		return idPaisDestino;
	}

	public void setIdPaisDestino(Long idPaisDestino) {
		this.idPaisDestino = idPaisDestino;
	}

	public YearMonthDay getDataConversao() {
		return dataConversao;
	}

	public void setDataConversao(YearMonthDay dataConversao) {
		this.dataConversao = dataConversao;
	}

	public boolean isBlPermitePGRDuplicado() {
		return blPermitePGRDuplicado;
	}

	public void setBlPermitePGRDuplicado(boolean blPermitePGRDuplicado) {
		this.blPermitePGRDuplicado = blPermitePGRDuplicado;
	}

	public boolean isBlValidaMaximoCarregamento() {
		return blValidaMaximoCarregamento;
	}

	public void setBlValidaMaximoCarregamento(boolean blValidaMaximoCarregamento) {
		this.blValidaMaximoCarregamento = blValidaMaximoCarregamento;
	}

	public boolean isBlValidaCargaExclusivaCliente() {
		return blValidaCargaExclusivaCliente;
	}

	public void setBlValidaCargaExclusivaCliente(boolean blValidaCargaExclusivaCliente) {
		this.blValidaCargaExclusivaCliente = blValidaCargaExclusivaCliente;
	}

	public boolean isBlRequerLiberacaoCemop() {
		return blRequerLiberacaoCemop;
	}

	public void setBlRequerLiberacaoCemop(boolean blRequerLiberacaoCemop) {
		this.blRequerLiberacaoCemop = blRequerLiberacaoCemop;
	}

	public boolean isBlValidaNaturezaImpedida() {
		return blValidaNaturezaImpedida;
	}

	public void setBlValidaNaturezaImpedida(boolean blValidaNaturezaImpedida) {
		this.blValidaNaturezaImpedida = blValidaNaturezaImpedida;
	}

	public Collection<DoctoServicoDTO> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(Collection<DoctoServicoDTO> documentos) {
		this.documentos = documentos;
	}

	public Collection<DoctoServicoDTO> getNaoEncontrados() {
		return naoEncontrados;
	}

	/**
	 * Inclui {@link DoctoServicoDTO} na coleção "não encontrados", para quando
	 * não for encontrado um {@link EnquadramentoRegra} compatível de acordo com
	 * as regras de filtro.
	 * 
	 * @param documento
	 */
	public void addNaoEncontrado(DoctoServicoDTO documento) {
		naoEncontrados = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(naoEncontrados, documento);
	}

	public Collection<DoctoServicoDTO> getDuplicados() {
		return duplicados;
	}

	/**
	 * Inclui {@link DoctoServicoDTO} na coleção "duplicados", para quando for
	 * encontrado mais de um {@link EnquadramentoRegra} compatível de acordo com
	 * as regras de filtro.
	 * 
	 * @param documento
	 */
	public void addDuplicado(DoctoServicoDTO documento) {
		duplicados = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(duplicados, documento);
	}

	public Collection<EnquadramentoRegra> getFaixaInexistente() {
		return faixaInexistente;
	}

	/**
	 * Inclui {@link EnquadramentoRegra} na coleção "faixa inexistente", para
	 * quando não for encontrado uma {@link FaixaDeValor} compatível com o valor
	 * total dos {@link DoctoServico}s enquadrados.
	 * 
	 * @param regra
	 */
	public void addFaixaInexistente(EnquadramentoRegra regra) {
		faixaInexistente = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(faixaInexistente, regra);
	}

	public Collection<EnquadramentoRegra> getRegrasCliente() {
		return regrasCliente;
	}

	public void setRegrasCliente(Collection<EnquadramentoRegra> regrasCliente) {
		this.regrasCliente = regrasCliente;
	}

	public Collection<EnquadramentoRegra> getRegrasGeral() {
		return regrasGeral;
	}

	public void setRegrasGeral(Collection<EnquadramentoRegra> regrasGeral) {
		this.regrasGeral = regrasGeral;
	}

	/**
	 * Retorna os {@link EnquadramentoRegra}s encontrados no processo Plano de
	 * Gerenciamento de Riscos.
	 * 
	 * @return
	 */
	public Collection<EnquadramentoRegra> getRegrasEnquadramento() {
		if (documentosRegra != null) {
			return documentosRegra.keySet();
		}
		return Collections.emptySet();
	}

	public Map<EnquadramentoRegra, Collection<DoctoServicoDTO>> getDocumentosRegra() {
		return documentosRegra;
	}

	/**
	 * Inclui {@link DoctoServicoDTO} no {@link Map} organizado por
	 * {@link EnquadramentoRegra}. Cada {@link EnquadramentoRegra} relaciona uma
	 * {@link Collection} de {@link DoctoServicoDTO}s de acordo com as regras de
	 * filtro.
	 * 
	 * @param regra
	 * @param documento
	 */
	public void putDocumentoRegra(EnquadramentoRegra regra, DoctoServicoDTO documento) {
		documentosRegra = PlanoGerenciamentoRiscoCollectionUtils.initAndPut(documentosRegra, regra, documento);
	}

	/**
	 * Retorna os {@link DoctoServicoDTO}s relacionados a determinado
	 * {@link EnquadramentoRegra}, ou uma {@link Collection} vazia se não houver
	 * {@link DoctoServicoDTO} relacionado.
	 * 
	 * @param regra
	 * @return
	 */
	public Collection<DoctoServicoDTO> getDocumentosRegra(EnquadramentoRegra regra) {
		return PlanoGerenciamentoRiscoCollectionUtils.getCollection(documentosRegra, regra);
	}

	public Map<EnquadramentoRegra, BigDecimal> getTotaisRegra() {
		return totaisRegra;
	}

	/**
	 * Soma o {@code vlMercadoria} no {@link Map} organizado por
	 * {@link EnquadramentoRegra}.
	 * 
	 * @param regra
	 * @param vlMercadoria
	 */
	public void sumTotalRegra(EnquadramentoRegra regra, BigDecimal vlMercadoria) {
		totaisRegra = PlanoGerenciamentoRiscoCollectionUtils.initAndSum(totaisRegra, regra, vlMercadoria);
	}

	/**
	 * Retorna o valor totalizado para determinado {@link EnquadramentoRegra}.
	 * 
	 * @param regra
	 * @return
	 */
	public BigDecimal getTotalRegra(EnquadramentoRegra regra) {
		return PlanoGerenciamentoRiscoCollectionUtils.getBigDecimal(totaisRegra, regra);
	}

	public Map<Long, BigDecimal> getTotaisNaturezaProduto() {
		return totaisNaturezaProduto;
	}

	/**
	 * Soma o {@code vlMercadoria} no {@link Map} organizado por
	 * {@link NaturezaProduto}.
	 * 
	 * @param idNaturezaProduto
	 * @param vlMercadoria
	 */
	public void sumTotalNaturezaProduto(Long idNaturezaProduto, BigDecimal vlMercadoria) {
		totaisNaturezaProduto = PlanoGerenciamentoRiscoCollectionUtils.initAndSum(totaisNaturezaProduto, idNaturezaProduto, vlMercadoria);
	}

	/**
	 * Retorna o valor totalizado para determinada {@link NaturezaProduto}.
	 * 
	 * @param idNaturezaProduto
	 * @return
	 */
	public BigDecimal getTotalNaturezaProduto(long idNaturezaProduto) {
		return PlanoGerenciamentoRiscoCollectionUtils.getBigDecimal(totaisNaturezaProduto, idNaturezaProduto);
	}

	public Map<String, BigDecimal> getTotaisTpOperacao() {
		return totaisTpOperacao;
	}

	/**
	 * Soma o {@code vlMercadoria} no {@link Map} organizado por tipo de
	 * operação.
	 * 
	 * @param tpOperacao
	 * @param vlMercadoria
	 */
	public void sumTotalTpOperacao(String tpOperacao, BigDecimal vlMercadoria) {
		totaisTpOperacao = PlanoGerenciamentoRiscoCollectionUtils.initAndSum(totaisTpOperacao, tpOperacao, vlMercadoria);
	}

	/**
	 * Retorna o valor totalizado para determinado tipo de operação.
	 * 
	 * @param tpOperacao
	 * @return
	 */
	public BigDecimal getTotalTpOperacao(String tpOperacao) {
		return PlanoGerenciamentoRiscoCollectionUtils.getBigDecimal(totaisTpOperacao, tpOperacao);
	}

	/**
	 * Retorna o valor total dos {@link DoctoServicoDTO} relacionados no
	 * {@link ControleCarga}.
	 * 
	 * @return
	 */
	public BigDecimal getTotalDocumentos() {
		BigDecimal vlMercadoria = BigDecimal.ZERO;
		if (getTotaisTpOperacao() != null) {
			for (BigDecimal totalTpOperacao : getTotaisTpOperacao().values()) {
				vlMercadoria = vlMercadoria.add(totalTpOperacao);
			}
		}
		return vlMercadoria;
	}

	public Collection<EnquadramentoRegraDTO> getEnquadramentos() {
		return enquadramentos;
	}

	/**
	 * Inclui resultado do enquadramento de regras referenciando
	 * {@link EnquadramentoRegra}, {@link FaixaDeValor} e valor total da
	 * mercadoria.
	 * 
	 * @param enquadramento
	 */
	public void addEnquadramento(EnquadramentoRegraDTO enquadramento) {
		enquadramentos = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(enquadramentos, enquadramento);
	}

	public Collection<EnquadramentoRegra> getLimiteApolice() {
		return limiteApolice;
	}

	/**
	 * Inclui {@link EnquadramentoRegra} na coleção "limite apólice", para
	 * quando o valor total dos {@link DoctoServico} enquadrados for superior ao
	 * valor limite para {@link ControleCarga} especificado na
	 * {@link ApoliceSeguro} ou no {@link SeguroCliente}.
	 * 
	 * @param regra
	 */
	public void addLimiteApolice(EnquadramentoRegra regra) {
		limiteApolice = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(limiteApolice, regra);
	}

	public Collection<EnquadramentoRegra> getCargaExclusiva() {
		return cargaExclusiva;
	}

	/**
	 * Inclui {@link EnquadramentoRegra} na coleção "carga exclusiva", para
	 * quando na {@link FaixaDeValor} de enquadradamento houver a opção
	 * {@code blExclusivaCliente} e no mesmo {@link ControleCarga} forem
	 * encontrados outros {@link EnquadramentoRegra}s que não referenciem a
	 * mesma {@link ApoliceSeguro} ou {@link SeguroCliente}.
	 * 
	 * @param regra
	 */
	public void addCargaExclusiva(EnquadramentoRegra regra) {
		cargaExclusiva = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(cargaExclusiva, regra);
	}

	public Collection<EnquadramentoRegra> getLiberacaoCemop() {
		return liberacaoCemop;
	}

	/**
	 * Inclui {@link EnquadramentoRegra} na coleção "liberação CEMOP", para
	 * quando na {@link FaixaDeValor} de enquadradamento houver a opção
	 * {@code blRequerLiberacaoCemop}.
	 * 
	 * @param regra
	 */
	public void addLiberacaoCemop(EnquadramentoRegra regra) {
		liberacaoCemop = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(liberacaoCemop, regra);
	}

	public Map<EnquadramentoRegra, Collection<NaturezaProduto>> getNaturezasImpedidas() {
		return naturezasImpedidas;
	}

	/**
	 * Inclui {@link NaturezaProduto} no {@link Map} organizado por
	 * {@link EnquadramentoRegra}, referentes à verificação de naturezas
	 * impedidas para determinada regra e de valor de mercadoria superior ao
	 * limite ou de limite não especificado.
	 * 
	 * @param regra
	 * @param naturezaProduto
	 */
	public void putNaturezaImpedida(EnquadramentoRegra regra, NaturezaProduto naturezaProduto) {
		naturezasImpedidas = PlanoGerenciamentoRiscoCollectionUtils.initAndPut(naturezasImpedidas, regra, naturezaProduto);
	}

	public Map<EnquadramentoRegra, Collection<ExigenciaGerRiscoDTO>> getExigenciasRegra() {
		return exigenciasRegra;
	}

	/**
	 * Inclui {@link ExigenciaGerRiscoDTO} no {@link Map} organizado por
	 * {@link EnquadramentoRegra}.
	 * 
	 * @param regra
	 * @param exigencia
	 */
	public void putExigenciaRegra(EnquadramentoRegra regra, ExigenciaGerRiscoDTO exigencia) {
		exigenciasRegra = PlanoGerenciamentoRiscoCollectionUtils.initAndPut(exigenciasRegra, regra, exigencia);
	}

	/**
	 * Retorna os {@link ExigenciaGerRiscoDTO}s relacionados a determinada
	 * {@link EnquadramentoRegra}, ou uma {@link Collection} vazia se não houver
	 * {@link ExigenciaGerRiscoDTO} relacionado.
	 * 
	 * @param regra
	 * @return
	 */
	public Collection<ExigenciaGerRiscoDTO> getExigenciasRegra(EnquadramentoRegra regra) {
		return PlanoGerenciamentoRiscoCollectionUtils.getCollection(exigenciasRegra, regra);
	}

	public Map<TipoExigenciaGerRisco, Collection<ExigenciaGerRiscoDTO>> getExigenciasTipo() {
		return exigenciasTipo;
	}

	/**
	 * Inclui {@link ExigenciaGerRiscoDTO} no {@link Map} organizado por
	 * {@link TipoExigenciaGerRisco}.
	 * 
	 * @param tipo
	 * @param exigencia
	 */
	public void putExigenciaTipo(TipoExigenciaGerRisco tipo, ExigenciaGerRiscoDTO exigencia) {
		exigenciasTipo = PlanoGerenciamentoRiscoCollectionUtils.initAndPut(exigenciasTipo, tipo, exigencia);
	}

	/**
	 * Retorna os {@link ExigenciaGerRiscoDTO}s relacionados a determinado
	 * {@link TipoExigenciaGerRisco}, ou uma {@link Collection} vazia se não
	 * houver {@link ExigenciaGerRiscoDTO} relacionado.
	 * 
	 * @param tipo
	 * @return
	 */
	public Collection<ExigenciaGerRiscoDTO> getExigenciasTipo(TipoExigenciaGerRisco tipo) {
		return PlanoGerenciamentoRiscoCollectionUtils.getCollection(exigenciasTipo, tipo);
	}

	public Map<ExigenciaGerRisco, Collection<ExigenciaGerRiscoDTO>> getExigenciasGrupo() {
		return exigenciasGrupo;
	}

	/**
	 * Inclui {@link ExigenciaGerRiscoDTO} no {@link Map} organizado por
	 * {@link ExigenciaGerRisco}.
	 * 
	 * @param grupo
	 * @param exigencia
	 */
	public void putExigenciaGrupo(ExigenciaGerRisco grupo, ExigenciaGerRiscoDTO exigencia) {
		exigenciasGrupo = PlanoGerenciamentoRiscoCollectionUtils.initAndPut(exigenciasGrupo, grupo, exigencia);
	}

	/**
	 * Retorna os {@link ExigenciaGerRiscoDTO}s relacionados a determinado
	 * {@link ExigenciaGerRisco}, ou uma {@link Collection} vazia se não houver
	 * {@link ExigenciaGerRiscoDTO} relacionado.
	 * 
	 * @param grupo
	 * @return
	 */
	public Collection<ExigenciaGerRiscoDTO> getExigenciasGrupo(ExigenciaGerRisco grupo) {
		return PlanoGerenciamentoRiscoCollectionUtils.getCollection(exigenciasGrupo, grupo);
	}

	public Collection<ExigenciaGerRiscoDTO> getExigencias() {
		return exigencias;
	}

	/**
	 * Inclui resultado da busca e resolução de exigências referenciando
	 * {@link ExigenciaGerRisco}, {@link TipoExigenciaGerRisco} e parâmetros
	 * incluídos na {@link ExigenciaFaixaValor}.
	 * 
	 * @param exigencia
	 */
	public void addExigencia(ExigenciaGerRiscoDTO exigencia) {
		exigencias = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(exigencias, exigencia);
	}

	/**
	 * Retorna as {@link NaturezaProduto}s impedidas para determinado
	 * {@link EnquadramentoRegra}, ou uma {@link Collection} vazia se não houver
	 * {@link NaturezaProduto} impedida.
	 * 
	 * @param regra
	 * @return
	 */
	public Collection<NaturezaProduto> getNaturezasImpedidas(EnquadramentoRegra regra) {
		return PlanoGerenciamentoRiscoCollectionUtils.getCollection(naturezasImpedidas, regra);
	}

	public Collection<String> getMensagensEnquadramento() {
		return mensagensEnquadramento;
	}

	/**
	 * Inclui uma mensagem sobre as verificações no enquadramento de regras do
	 * Plano de Gerenciamento de Riscos.
	 * 
	 * @param mensagem
	 */
	public void addMensagemEnquadramento(String mensagem) {
		mensagensEnquadramento = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(mensagensEnquadramento, mensagem);
	}

	public Collection<String> getMensagensWorkflowRegras() {
		return mensagensWorkflowRegras;
	}

	/**
	 * Inclui uma mensagem para workflow na resolução de exigências de regras do
	 * Plano de Gerenciamento de Riscos.
	 * 
	 * @param mensagem
	 */
	public void addMensagemWorkflowRegras(String mensagem) {
		mensagensWorkflowRegras = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(mensagensWorkflowRegras, mensagem);
	}

	public Collection<String> getMensagensWorkflowExigencias() {
		return mensagensWorkflowExigencias;
	}

	/**
	 * Inclui uma mensagem para workflow na resolução de exigências de regras do
	 * Plano de Gerenciamento de Riscos.
	 * 
	 * @param mensagem
	 */
	public void addMensagemWorkflowExigencias(String mensagem) {
		mensagensWorkflowExigencias = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(mensagensWorkflowExigencias, mensagem);
	}

	/**
	 * Inicia temporização para tarefa específica.
	 * 
	 * @param tarefa
	 */
	public void startCronometro(String tarefa) {
		if (cronometro == null) {
			cronometro = new StopWatch();
		}
		cronometro.start(tarefa);
	}

	/**
	 * Encerra temporização de tarefa.
	 */
	public void stopCronometro() {
		cronometro.stop();
	}

	public StopWatch getCronometro() {
		return cronometro;
	}

	/**
	 * Produz resumo dos resultados obtidos com o {@link StopWatch}, destacando
	 * o tempo total e os tempos parciais de cada tarefa.
	 * 
	 * @return
	 */
	public String getCronometroString() {
		if (cronometro == null) {
			return null;
		}
		return cronometro.toString()
				.replaceFirst("^StopWatch '': running time \\(millis\\) = (\\d+)", "$1ms (")
				.replaceAll("; \\[(\\w+)\\] took (\\d+) = \\d+%", "$1: $2ms, ")
				.replaceFirst(", $", ")");
	}

	@Override
	public String toString() {
		ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
		return new ToStringBuilder(this)
				.append("idControleCarga", idControleCarga)
				.append("documentos.size", documentos != null ? documentos.size() : null)
				.append("cronometro", getCronometroString())
				.toString();
	}

	/**
	 * Método auxiliar para geração de JSON compacto a partir de
	 * {@link PlanoGerenciamentoRiscoDTO}.
	 * 
	 * @return
	 */
	public String writeAsString() {
		return writeAsString(false);
	}

	/**
	 * Método auxiliar para geração de JSON formatado ou compacto a partir do
	 * {@link PlanoGerenciamentoRiscoDTO}.
	 * 
	 * @param prettyPrint
	 * @return
	 */
	public String writeAsString(boolean prettyPrint) {
		return EnquadramentoRegraObjectMapperUtil.writeAsString(prettyPrint, this);
	}
	
	public Collection<ExigenciaGerRiscoDTO> getExigenciasVioladas() {
		return exigenciasVioladas;
	}
	
	/**
	 * Inclui {@link ExigenciaGerRiscoDTO} na coleção "exigências violadas", para quando
	 * for encontrada alguma inconformidade com a exigência.
	 * 
	 * @param exigencia
	 */
	public void addExigenciasVioladas(ExigenciaGerRiscoDTO exigencia) {
		exigenciasVioladas = PlanoGerenciamentoRiscoCollectionUtils.initAndAdd(exigenciasVioladas, exigencia);
	}

	public void setExigencias(Collection<ExigenciaGerRiscoDTO> exigencias) {
		this.exigencias = exigencias;
	}

	public boolean isBlValidaExigenciasMonitoramento() {
		return blValidaExigenciasMonitoramento;
	}

	public void setBlValidaExigenciasMonitoramento(boolean blValidaExigenciasMonitoramento) {
		this.blValidaExigenciasMonitoramento = blValidaExigenciasMonitoramento;
	}

	public boolean isBlValidaExigenciasMotorista() {
		return blValidaExigenciasMotorista;
	}

	public void setBlValidaExigenciasMotorista(boolean blValidaExigenciasMotorista) {
		this.blValidaExigenciasMotorista = blValidaExigenciasMotorista;
	}

	public boolean isBlValidaExigenciasVeiculo() {
		return blValidaExigenciasVeiculo;
	}

	public void setBlValidaExigenciasVeiculo(boolean blValidaExigenciasVeiculo) {
		this.blValidaExigenciasVeiculo = blValidaExigenciasVeiculo;
	}

	public boolean isBlGerarWorkflowRegras() {
		return blGerarWorkflowRegras;
	}

	public void setBlGerarWorkflowRegras(boolean blGerarWorkflowRegras) {
		this.blGerarWorkflowRegras = blGerarWorkflowRegras;
	}

	public boolean isBlGerarWorkflowExigencias() {
		return blGerarWorkflowExigencias;
	}

	public void setBlGerarWorkflowExigencias(boolean blGerarWorkflowExigencias) {
		this.blGerarWorkflowExigencias = blGerarWorkflowExigencias;
	}

	public boolean isBlGerarEnquadramentos() {
		return blGerarEnquadramentos;
	}

	public void setBlGerarEnquadramentos(boolean blGerarEnquadramentos) {
		this.blGerarEnquadramentos = blGerarEnquadramentos;
	}

	public boolean isBlValidaExigenciasVirus() {
		return blValidaExigenciasVirus;
	}

	public void setBlValidaExigenciasVirus(boolean blValidaExigenciasVirus) {
		this.blValidaExigenciasVirus = blValidaExigenciasVirus;
	}

}
