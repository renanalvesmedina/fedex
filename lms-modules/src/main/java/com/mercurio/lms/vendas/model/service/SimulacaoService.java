package com.mercurio.lms.vendas.model.service;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_DESPACHO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_SEGURO_FLUVIAL;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_TAD;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_TAS;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_TAXA_COMBUSTIVEL;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_TAXA_FLUVIAL_BALSA;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_TAXA_SUFRAMA;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_TAXA_TERRESTRE;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.MODAL_AEREO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.MODAL_RODOVIARIO;
import static com.mercurio.lms.util.BigDecimalUtils.ZERO;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.util.ListToMapConverter;
import com.mercurio.adsm.framework.util.RowMapper;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.FuncionarioService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.GrupoClassificacao;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.GrupoClassificacaoFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.SubtipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.service.GeneralidadeService;
import com.mercurio.lms.tabelaprecos.model.service.LimiteDescontoService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.ValorFaixaProgressivaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DestinoProposta;
import com.mercurio.lms.vendas.model.DiferencaCapitalInterior;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.HistoricoEfetivacao;
import com.mercurio.lms.vendas.model.MotivoReprovacao;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.PipelineCliente;
import com.mercurio.lms.vendas.model.Proposta;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.ValorFaixaProgressivaProposta;
import com.mercurio.lms.vendas.model.dao.SimulacaoDAO;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.SimulacaoUtils;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.AcaoService;
import com.mercurio.lms.workflow.model.service.GerarEmailMensagemAvisoService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.simulacaoService"
 */
public class SimulacaoService extends CrudService<Simulacao, Long> {
	
	private static final Integer VALOR_MAXIMO_DS_PENDENCIA = 4000;
	private static final String ID_SIMULACAO = "idSimulacao";
	private static final String DM_SITUACAO_SIMULACAO = "DM_SITUACAO_SIMULACAO";
	private static final String TP_SITUACAO_EFETIVACAO_REPROVADA = "H";
	
	private static final String NM_PARCELA_FRETE_QUILO = "Frete Quilo";
	private static final String DS_TP_LOCALIZACAO_CAPITAL_INTERIORES = "Capital/Interiores";
	private static final String TP_LOCALIZACAO_PROPOSTA = "P";
	private static final String[] TIPOS_PROPOSTA_VALIDOS = new String[] { ConstantesVendas.TP_PROPOSTA_CAPITAL_INTERIOR,
			ConstantesVendas.TP_PROPOSTA_CAPITAL_INTERIOR_PERCENTUAL, ConstantesVendas.TP_PROPOSTA_PROMOCIONAL,
			ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_EXCEDENTE, ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_VALOR_KG, ConstantesVendas.TP_PROPOSTA_CONVENCIONAL };	
	
	private static final Short SHORT_ONE = Short.valueOf("1");
	private static final Short SHORT_TWO = Short.valueOf("2");
	private static final Short SHORT_THREE = Short.valueOf("3");
	private static final Short SHORT_FOUR = Short.valueOf("4");
	private static final BigDecimal FIFTY = BigDecimal.valueOf(50.00);
	private static final BigDecimal FOURTY = BigDecimal.valueOf(40.00);
	private static final BigDecimal THIRTY = BigDecimal.valueOf(30.00);
	private static final BigDecimal TWENTY_FIVE = BigDecimal.valueOf(25.00);
	private static final BigDecimal TWENTY = BigDecimal.valueOf(20.00);
	
	private static final String FIXO = "F";
	private static final String VALOR = "V";
	private static final String TABELA = "T";
	private static final String DESCONTO = "D";
	private static final String ACRESCIMO = "A";
	private static final String FAIXAPESO = "X";
	private static final String FRACAOQUILO = "Q";

	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
	
	private WorkflowPendenciaService workflowPendenciaService;
	private TabelaPrecoService tabelaPrecoService;
	private LimiteDescontoService limiteDescontoService;
	private ConfiguracoesFacade configuracoesFacade;
	private GrupoClassificacaoFilialService grupoClassificacaoFilialService;
	private ParametroClienteService parametroClienteService;
	private GeneralidadeClienteService generalidadeClienteService;
	private GeneralidadeService generalidadeService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private AcaoService acaoService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private VigenciaService vigenciaService;
	private DivisaoClienteService divisaoClienteService;
	private TaxaClienteService taxaClienteService;
	private FilialService filialService;
	private PropostaService propostaService;
	private DestinoPropostaService destinoPropostaService;
	private HistoricoReajusteClienteService historicoReajusteClienteService;
	private ParcelaPrecoService parcelaPrecoService;
	private ParametroGeralService parametroGeralService; 
	private PerfilUsuarioService perfilUsuarioService;
	private DiferencaCapitalInteriorService diferencaCapitalInteriorService;
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	private SimulacaoAnexoService simulacaoAnexoService;
	private DomainValueService domainValueService; 
	private UsuarioLMSService usuarioLMSService;
	private FuncionarioService funcionarioService;
	private ComiteNivelMarkupService comiteNivelMarkupService;
	private EnderecoPessoaService enderecoPessoaService;
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private HistoricoEfetivacaoService historicoEfetivacaoService;
	private MotivoReprovacaoService motivoReprovacaoService;
	private ValorFaixaProgressivaService valorFaixaProgressivaService;
	private GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService;
	private PaisService paisService;
	private ValorFaixaProgressivaPropostaService valorFaixaProgressivaPropostaService;
	private AeroportoService aeroportoService;

	public List<Map<String, Object>> findRelatorioEfetivacaoProposta(Map<String, Object> parameters) {
		return getSimulacaoDAO().findRelatorioEfetivacaoProposta(parameters);
	}
	
	
	/**
	 * Recupera uma instância de <code>Simulacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Simulacao findById(Long id) {
		return (Simulacao)super.findById(id);
	}

	public TypedFlatMap executeSolicitarEfetivacaoProposta(TypedFlatMap map){
		TypedFlatMap result = new TypedFlatMap();
		
		DateTime dhSolicitacao = new DateTime();
		Long idSimulacao = map.getLong(ID_SIMULACAO);

		map.put("simulacao.idSimulacao", idSimulacao);
		List anexos = simulacaoAnexoService.find(map);
		if(CollectionUtils.isEmpty(anexos)){
			throw new BusinessException("LMS-01234");
		}
		
		DomainValue domainValue = domainValueService.findDomainValueByValue(DM_SITUACAO_SIMULACAO, "M");
		
		Simulacao simulacao = this.findById(idSimulacao);
		simulacao.setTpSituacaoAprovacao(domainValue);

		Long id = (Long) this.store(simulacao);
		
		if(id != null){
			HistoricoEfetivacao historicoEfetivacao = new HistoricoEfetivacao();
			historicoEfetivacao.setSimulacao(simulacao);
			

			historicoEfetivacao.setDhSolicitacao(dhSolicitacao);
			
			Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
			UsuarioLMS usuarioLMS = usuarioLMSService.findById(usuarioLogado.getIdUsuario());
			
			historicoEfetivacao.setUsuarioSolicitacao(usuarioLMS);
			
			historicoEfetivacaoService.store(historicoEfetivacao);
			
			SimulacaoUtils.setSimulacaoInSession(simulacao);
		}
		
		result.put("dhSolicitacao", dhSolicitacao);
		result.put("tpSituacaoAprovacao.description", domainValue.getDescription());
		result.put("tpSituacaoAprovacao.value", domainValue.getValue());
		result.put("tpSituacaoAprovacaoDomain", domainValue);
		
		return result;
	}
	
	/**
	 * Busca os dados da simulacao e caso o valor de <code>saveSimulacaoInSession</code>
	 * seja <code>true</code> salva a simulacao na sessao.
	 * 
	 * @param idSimulacao
	 * @param saveSimulacaoInSession
	 * @return
	 */
	public TypedFlatMap findDadosById(Long idSimulacao) {
		TypedFlatMap result = new TypedFlatMap();
		Simulacao simulacao = findById(idSimulacao);

		if (simulacao.getTabelaPreco() != null) {
			result.put("tabelaPreco.idTabelaPreco", simulacao.getTabelaPreco().getIdTabelaPreco());
			result.put("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco.value", simulacao.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue());
			result.put("tabelaPreco.dsDescricao", simulacao.getTabelaPreco().getDsDescricao());
			result.put("tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco", simulacao.getTabelaPreco().getSubtipoTabelaPreco().getIdSubtipoTabelaPreco());
			result.put("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco", simulacao.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
			result.put("tabelaPreco.tipoTabelaPreco.nrVersao", simulacao.getTabelaPreco().getTipoTabelaPreco().getNrVersao());
			result.put("tabelaPreco.moeda.sgMoeda", simulacao.getTabelaPreco().getMoeda().getSgMoeda());
			result.put("tabelaPreco.moeda.dsSimbolo", simulacao.getTabelaPreco().getMoeda().getDsSimbolo());
		}
		
		if (simulacao.getTabelaPrecoFob() != null) {
			result.put("tabelaPrecoFob.idTabelaPreco", simulacao.getTabelaPrecoFob().getIdTabelaPreco());
			result.put("tabelaPrecoFob.tipoTabelaPreco.tpTipoTabelaPreco.value", simulacao.getTabelaPrecoFob().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue());
			result.put("tabelaPrecoFob.dsDescricao", simulacao.getTabelaPrecoFob().getDsDescricao());
			result.put("tabelaPrecoFob.subtipoTabelaPreco.idSubtipoTabelaPreco", simulacao.getTabelaPrecoFob().getSubtipoTabelaPreco().getIdSubtipoTabelaPreco());
			result.put("tabelaPrecoFob.subtipoTabelaPreco.tpSubtipoTabelaPreco", simulacao.getTabelaPrecoFob().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
			result.put("tabelaPrecoFob.tipoTabelaPreco.nrVersao", simulacao.getTabelaPrecoFob().getTipoTabelaPreco().getNrVersao());
		}
		
		if(simulacao.getPendenciaAprovacao() != null){
			result.put("idPendenciaAprovacao", simulacao.getPendenciaAprovacao().getIdPendencia());
		}
		
		result.put("filial.idFilial", simulacao.getFilial().getIdFilial());
		result.put("filial.sgFilial", simulacao.getFilial().getSgFilial());
		result.put("nrSimulacao", simulacao.getNrSimulacao());
		result.put("nrFatorCubagem", simulacao.getNrFatorCubagem());
		result.put("nrFatorDensidade", simulacao.getNrFatorDensidade());
		result.put("nrLimiteMetragemCubica", simulacao.getNrLimiteMetragemCubica());
		result.put("nrLimiteQuantVolume", simulacao.getNrLimiteQuantVolume());
		result.put("blEfetivada", simulacao.getBlEfetivada());
		result.put("cliente.idCliente", simulacao.getClienteByIdCliente().getIdCliente());
		result.put("cliente.pessoa.nrIdentificacao", simulacao.getClienteByIdCliente().getPessoa().getNrIdentificacao());
		result.put("cliente.pessoa.tpIdentificacao.value", simulacao.getClienteByIdCliente().getPessoa().getTpIdentificacao().getValue());
		result.put("cliente.pessoa.nmPessoa", simulacao.getClienteByIdCliente().getPessoa().getNmPessoa());
		result.put("servico.idServico", simulacao.getServico().getIdServico());
		result.put("servico.dsServico", simulacao.getServico().getDsServico().getValue());
		result.put("servico.tpModal.value", simulacao.getServico().getTpModal().getValue());
		result.put("servico.tpAbrangencia.value", simulacao.getServico().getTpAbrangencia().getValue());
		result.put("blPagaFreteTonelada", simulacao.getBlPagaFreteTonelada());
		result.put("blEmiteCargaCompleta", simulacao.getBlEmiteCargaCompleta());
		result.put("obProposta", simulacao.getObProposta());
		result.put("dtSimulacao", simulacao.getDtSimulacao());
		result.put("tpSimulacao.value", simulacao.getTpSimulacao().getValue());

		DomainValue tpPeriodicidadeFaturamento = simulacao.getTpPeriodicidadeFaturamento();
		if (tpPeriodicidadeFaturamento != null) {
			result.put("simulacao.tpPeriodicidadeFaturamento", tpPeriodicidadeFaturamento.getValue());
		}
		
		result.put("nrDiasPrazoPagamento", simulacao.getNrDiasPrazoPagamento());
		result.put("dtValidadeProposta", simulacao.getDtValidadeProposta());
		result.put("dtTabelaVigenciaInicial", simulacao.getDtTabelaVigenciaInicial());
		result.put("dtAceiteCliente", simulacao.getDtAceiteCliente());
		result.put("dtAprovacao", simulacao.getDtAprovacao());
		result.put("dtEmissaoTabela", simulacao.getDtEmissaoTabela());
		result.put("dtEfetivacao", simulacao.getDtEfetivacao());
		
		HistoricoEfetivacao historicoEfetivacao = historicoEfetivacaoService.findLastHistoricoByIdSimulacao(idSimulacao);
		if(historicoEfetivacao != null){
			result.put("dhSolicitacao", historicoEfetivacao.getDhSolicitacao());
			result.put("dhReprovacao", historicoEfetivacao.getDhReprovacao());
			result.put("dsMotivo", historicoEfetivacao.getDsMotivo());
		}
		
		if (simulacao.getUsuarioByIdUsuarioAprovou() != null) {
			result.put("usuarioByIdUsuarioAprovou.nrMatricula", simulacao.getUsuarioByIdUsuarioAprovou().getNrMatricula());
			result.put("usuarioByIdUsuarioAprovou.nmUsuario", simulacao.getUsuarioByIdUsuarioAprovou().getNmUsuario());
		}
		
		if(simulacao.getUsuarioByIdUsuarioEfetivou() != null){
			result.put("usuarioByIdUsuarioEfetivou.nrMatricula", simulacao.getUsuarioByIdUsuarioEfetivou().getNrMatricula());
			result.put("usuarioByIdUsuarioEfetivou.nmUsuario", simulacao.getUsuarioByIdUsuarioEfetivou().getNmUsuario());
		}
		
		// LMS-6191 - inclui situação de aprovação da proposta no mapa 
		DomainValue tpSituacaoAprovacao = simulacao.getTpSituacaoAprovacao();
		if (tpSituacaoAprovacao != null) {
			result.put("simulacao.tpSituacaoAprovacao.value", tpSituacaoAprovacao.getValue());
			result.put("simulacao.tpSituacaoAprovacao.description", tpSituacaoAprovacao.getDescription());
		}

		if(simulacao.getPromotor() != null) {
			result.put("nrMatriculaPromotor", simulacao.getPromotor().getNrMatricula());
			result.put("nmPromotor", simulacao.getPromotor().getNmFuncionario());
		}

		if(simulacao.getTpDiferencaAdvalorem() != null){
			result.put("tpDiferencaAdvalorem", simulacao.getTpDiferencaAdvalorem().getValue());
		}
		
		if (simulacao.getDivisaoCliente() != null) {
			result.put("divisaoCliente.idDivisaoCliente", simulacao.getDivisaoCliente().getIdDivisaoCliente());
			result.put("divisaoCliente.dsDivisaoCliente", simulacao.getDivisaoCliente().getDsDivisaoCliente());
		}
		if(simulacao.getTpGeracaoProposta() != null) {
			result.put("tpGeracaoProposta", simulacao.getTpGeracaoProposta().getValue());
		}
		
		if (simulacao.getProdutoEspecifico() != null){
			result.put("produtoEspecifico.idProdutoEspecifico", simulacao.getProdutoEspecifico().getIdProdutoEspecifico());
		}
		return result;
	}

	public TypedFlatMap findDadosByIdParametroCliente(Long idParametroCliente) {
		return getSimulacaoDAO().findDadosByIdParametroCliente(idParametroCliente);
	}

	/**
	 * Apaga uma entidade através do Id.
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		Simulacao simulacao = findById(id);
		
		/*Remove os parametros_cliente de uma proposta Capital/Interior caso existir*/ 
		if("C".equals(simulacao.getTpGeracaoProposta().getValue()) || "P".equals(simulacao.getTpGeracaoProposta().getValue())
				|| "O".equals(simulacao.getTpGeracaoProposta().getValue())){
			Proposta proposta = propostaService.findByIdSimulacao(id);		
			Long idParametroCliente = null;
			if(proposta != null && proposta.getParametroCliente() != null 
					&& proposta.getParametroCliente().getIdParametroCliente() != null){
				idParametroCliente = proposta.getParametroCliente().getIdParametroCliente();
				proposta.setParametroCliente(null);
				propostaService.store(proposta);
				parametroClienteService.removeById(idParametroCliente, true);
			}
		}
				
		validateExclusao(simulacao);
		if(simulacao.getPendenciaAprovacao() != null) {
			simulacao.getPendenciaAprovacao().setTpSituacaoPendencia(new DomainValue("E"));
			simulacao.getPendenciaAprovacao().getOcorrencia().setTpSituacaoOcorrencia(new DomainValue("P"));
			workflowPendenciaService.cancelPendencia(simulacao.getPendenciaAprovacao());
		}
		
		/** Exclui caso esxita alguma PROPOSTA relacionada */
		destinoPropostaService.removeByIdSimulacao(id);
		propostaService.removeByIdSimulacao(id);
		getSimulacaoDAO().removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (int i = 0; i < ids.size(); i++) {
			removeById((Long) ids.get(i));
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Simulacao bean) {
		return super.store(bean);
	}

	public void storeProposta(Simulacao simulacao, ParametroCliente pc) {
		/** Verifica se parametro na proposta */
		Proposta proposta = null;
		boolean blAtualizaPendencia = true;
		if(!LongUtils.hasValue(pc.getIdParametroCliente())) {
			/** Valida se TipoGeraçãoProposta == C (Capital/Interior) */
			DomainValue tpGeracaoProposta = simulacao.getTpGeracaoProposta();
			if(tpGeracaoProposta != null && ("C".equals(tpGeracaoProposta.getValue()) || "P".equals(tpGeracaoProposta.getValue()))) {
				proposta = propostaService.findByIdSimulacao(simulacao.getIdSimulacao());
				if(proposta == null) {
					throw new BusinessException("LMS-01031");
				}
				pc.setProposta(proposta);
			} else {
				pc.setSimulacao(simulacao);
			} 
		} else {
			blAtualizaPendencia = needAtualizacaoPendencia(simulacao, pc);
		}
		
		parametroClienteService.validateInclusaoSimulacao(pc, "P", "LMS-01115");
		parametroClienteService.store(pc);

		if(blAtualizaPendencia) {
			getSimulacaoDAO().getSessionFactory().getCurrentSession().flush();
			storePendenciaAprovacaoProposta(simulacao.getIdSimulacao(), false);
		}

		/** Salva Proposta com ParametroCliente relacionado */
		if(proposta != null) {
			proposta.setParametroCliente(pc);
			propostaService.store(proposta);
		}
	}

	public void storeProposta(Simulacao simulacao, Long idTabelaPrecoNova, Long idTabelaPrecoAntiga) {
		boolean atualizaPendencia = true;
		if(simulacao.getIdSimulacao() == null) {
			Long nrSimulacao = configuracoesFacade.incrementaParametroSequencial(simulacao.getFilial().getIdFilial(), "NR_SIMULACAO", true);
			simulacao.setNrSimulacao(nrSimulacao);
		} else {
			atualizaPendencia = needAtualizacaoPendencia(simulacao);
		}

		if (idTabelaPrecoNova != null && idTabelaPrecoAntiga != null) {
			generalidadeClienteService.removeByTabelasPreco(idTabelaPrecoNova, idTabelaPrecoAntiga, simulacao.getIdSimulacao());
			taxaClienteService.removeByTabelasPreco(idTabelaPrecoNova, idTabelaPrecoAntiga, simulacao.getIdSimulacao());
			servicoAdicionalClienteService.removeByTabelasPreco(idTabelaPrecoNova, idTabelaPrecoAntiga, simulacao.getIdSimulacao());
		}
		
		Boolean rotinaAereoPromociomal = isRotinaAereoPromociomal(simulacao);
		
		if(rotinaAereoPromociomal){
			simulacao.setTpDiferencaAdvalorem(new DomainValue("P"));
		}
		
		store(simulacao);

		if(rotinaAereoPromociomal){
			executeRotinaAereo(simulacao);
		}
		
		if(atualizaPendencia) {
			storePendenciaAprovacaoProposta(simulacao, false);
		}
	}

	private Boolean isRotinaAereoPromociomal(Simulacao simulacao){
		if(simulacao.getIdSimulacao() == null && ConstantesVendas.TP_PROPOSTA_PROMOCIONAL.equals(simulacao.getTpGeracaoProposta().getValue())){
			return Boolean.TRUE;
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * 
	 * @param simulacao
	 */
	public Proposta executeRotinaAereo(Simulacao simulacao){
		ParametroCliente parametroCliente = generateParametroClienteAereo(simulacao);
		parametroClienteService.store(parametroCliente);

		Proposta proposta = getPropostaAereo(simulacao, parametroCliente);
		propostaService.store(proposta);
		
		parametroCliente.setProposta(proposta);
		parametroClienteService.store(parametroCliente);
		
		return proposta;
	}

	private ParametroCliente generateParametroClienteAereo(Simulacao simulacao) {
		DomainValue domainTabela = new DomainValue("T");
		DomainValue domainValor = new DomainValue("V");
		DomainValue domainDesconto = new DomainValue("D");
		
		ParametroCliente parametroCliente = new ParametroCliente();
		
		Pais pais = paisService.findPaisBySgPais(ConstantesAmbiente.SG_PAIS_BRASIL);
		parametroCliente.setPaisByIdPaisOrigem(pais);
		parametroCliente.setPaisByIdPaisDestino(pais);
		parametroCliente.setZonaByIdZonaOrigem(pais.getZona());
		parametroCliente.setZonaByIdZonaDestino(pais.getZona());
		
		parametroCliente.setDtVigenciaInicial(new YearMonthDay());
		parametroCliente.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);
		
		parametroCliente.setTpIndicadorPercentualGris(domainTabela);
		parametroCliente.setVlPercentualGris(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorMinimoGris(domainTabela);
		parametroCliente.setVlMinimoGris(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorPedagio(domainTabela);
		parametroCliente.setVlPedagio(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorMinFretePeso(domainTabela);
		parametroCliente.setVlMinFretePeso(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorPercMinimoProgr(domainDesconto);
		parametroCliente.setVlPercMinimoProgr(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorFretePeso(domainTabela);
		parametroCliente.setVlFretePeso(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorAdvalorem(domainTabela);
		parametroCliente.setVlAdvalorem(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorAdvalorem2(domainTabela);
		parametroCliente.setVlAdvalorem2(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorValorReferencia(domainTabela);
		parametroCliente.setVlValorReferencia(BigDecimal.ZERO);
		parametroCliente.setVlMinimoFreteQuilo(BigDecimal.ZERO);
		parametroCliente.setPcFretePercentual(BigDecimal.ZERO);
		parametroCliente.setVlMinimoFretePercentual(BigDecimal.ZERO);
		parametroCliente.setVlToneladaFretePercentual(BigDecimal.ZERO);
		parametroCliente.setPsFretePercentual(BigDecimal.ZERO);
		parametroCliente.setPcDescontoFreteTotal(BigDecimal.ZERO);
		parametroCliente.setTpIndicVlrTblEspecifica(domainTabela);
		parametroCliente.setVlTblEspecifica(BigDecimal.ZERO);
		parametroCliente.setVlFreteVolume(BigDecimal.ZERO);
		parametroCliente.setBlPagaCubagem(true);
		parametroCliente.setPcPagaCubagem(BigDecimal.ZERO);
		parametroCliente.setTpTarifaMinima(domainTabela);
		parametroCliente.setVlTarifaMinima(BigDecimal.ZERO);
		BigDecimal pcr = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("PercentualCobrancaReentrega",false);
		parametroCliente.setPcCobrancaReentrega(pcr);
		BigDecimal pcd = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("PercentualCobrancaDevolucao",false);
		parametroCliente.setPcCobrancaDevolucoes(pcd);
		parametroCliente.setTpSituacaoParametro(new DomainValue("P"));
		parametroCliente.setTpIndicadorPercentualTde(domainTabela);
		parametroCliente.setVlPercentualTde(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorMinimoTde(domainTabela);
		parametroCliente.setVlMinimoTde(BigDecimal.ZERO);
		parametroCliente.setTabelaPreco(simulacao.getTabelaPreco());
		parametroCliente.setTpIndicadorPercentualTrt(domainTabela);
		parametroCliente.setVlPercentualTrt(BigDecimal.ZERO);
		parametroCliente.setTpIndicadorMinimoTrt(domainTabela);
		parametroCliente.setVlMinimoTrt(BigDecimal.ZERO);
		parametroCliente.setDsEspecificacaoRota(configuracoesFacade.getMensagem("registroAutomatico"));

		if(ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_EXCEDENTE.equals(simulacao.getTpGeracaoProposta().getValue())){
			parametroCliente.setBlPagaPesoExcedente(true);
			parametroCliente.setTpIndicadorMinFretePeso(new DomainValue("P"));
			parametroCliente.setVlMinimoFreteQuilo(BigDecimal.ONE);
			parametroCliente.setTpIndicadorFretePeso(domainValor);
		} else {
			parametroCliente.setBlPagaPesoExcedente(false);
		}
		
		return parametroCliente;
	}

	private Proposta getPropostaAereo(Simulacao simulacao, ParametroCliente parametroCliente){
		Proposta proposta = new Proposta();
		proposta.setTpIndicadorMinFretePeso(new DomainValue("T"));
		proposta.setVlMinFretePeso(BigDecimal.ZERO);
		proposta.setTpIndicadorFreteMinimo(new DomainValue("D"));
		proposta.setVlFreteMinimo(BigDecimal.ZERO);
		proposta.setTpIndicadorFretePeso(new DomainValue("D"));
		proposta.setVlFretePeso(BigDecimal.ZERO);
		proposta.setBlPagaPesoExcedente(false);
		proposta.setBlPagaCubagem(true);
		proposta.setPcPagaCubagem(BigDecimalUtils.HUNDRED);
		proposta.setPcDiferencaFretePeso(BigDecimal.ZERO);
		proposta.setTpIndicadorAdvalorem(new DomainValue("T"));
		proposta.setVlAdvalorem(BigDecimal.ZERO); 
		proposta.setPcDiferencaAdvalorem(BigDecimal.ZERO);
		proposta.setBlFreteExpedido(true);
		proposta.setBlFreteRecebido(false);
		proposta.setSimulacao(simulacao);
		proposta.setParametroCliente(parametroCliente);
		proposta.setUnidadeFederativaByIdUfOrigem(findUnidadeFederativaCliente(simulacao.getClienteByIdCliente()));
		
		TipoLocalizacaoMunicipio capitalInteriores = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio(DS_TP_LOCALIZACAO_CAPITAL_INTERIORES, TP_LOCALIZACAO_PROPOSTA);
		proposta.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(capitalInteriores);

		return proposta;
	}
	
	private UnidadeFederativa findUnidadeFederativaCliente(Cliente cliente){
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(cliente.getIdCliente());
		if(enderecoPessoa == null){
			throw new BusinessException("LMS-00069");
		}
		return enderecoPessoa.getMunicipio().getUnidadeFederativa();
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getSimulacaoDAO().findPaginated(criteria, def);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getSimulacaoDAO().getRowCount(criteria);
	}

	public ResultSetPage findPaginatedProc(TypedFlatMap criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getSimulacaoDAO().findPaginatedProc(criteria, def);
	}

	public Integer getRowCountProc(TypedFlatMap criteria) {
		return getSimulacaoDAO().getRowCountProc(criteria);
	}

	public void validaUpdateSimulacao(Simulacao simulacao) {
		validateOperation(simulacao, "LMS-30035", "LMS-30036");
	}

	public void validateAprovacaoProposta(Simulacao simulacao) {
		validateOperation(simulacao, "LMS-30037", "LMS-30036");//ESSE AQUI
		validateSimulacaoProposta(simulacao);
	}

	public void validateSimulacaoProposta(Simulacao simulacao) {
		if (simulacao.getTabelaPreco() != null 
				&& !"P".equals(simulacao.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco())) {

			/** Valida se existe algum parametro cadastrado para esse Simulação */
			Boolean existParametro = Boolean.FALSE;
			if (simulacao.getTpGeracaoProposta() != null && ArrayUtils.contains(TIPOS_PROPOSTA_VALIDOS, simulacao.getTpGeracaoProposta().getValue())) {
				Proposta proposta = propostaService.findByIdSimulacao(simulacao.getIdSimulacao());
				if(proposta == null) {
					throw new BusinessException("LMS-01031");
				}
				existParametro = parametroClienteService.existParametroByIdProposta(proposta.getIdProposta()) && proposta.getParametroCliente() != null;
			} else {
				existParametro = parametroClienteService.existParametroByIdSimulacao(simulacao.getIdSimulacao());
			}
			if (!existParametro) {
				throw new BusinessException("LMS-30048");
			}
		}
		if (simulacao.getDtValidadeProposta() == null) {
			throw new BusinessException("LMS-04109", new Object[] {configuracoesFacade.getMensagem("dataValidadeProposta")});
		}
	}

	public void validateExclusao(Simulacao simulacao) {
		validateOperation(simulacao, "LMS-30033", "LMS-30034");
	}

	private boolean validateFatorCubagemPadraoRodo(Simulacao simulacao){
		/*CQPRO00025225*/
		if(simulacao.getServico() != null && "R".equals(simulacao.getServico().getTpModal().getValue())){
			BigDecimal fatorCubagemPadraoRodo = (BigDecimal)configuracoesFacade.getValorParametro("FATOR_CUBAGEM_PADRAO_RODO");
			if(fatorCubagemPadraoRodo != null  && simulacao.getNrFatorCubagem() != null && CompareUtils.lt(BigDecimalUtils.defaultBigDecimal(simulacao.getNrFatorCubagem()), fatorCubagemPadraoRodo) ){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Validações referente a ParametroCliente
	 * @param idSimulacao
	 * @return
	 */
	private boolean validateLimiteDescontoProposta(Long idSimulacao) {
		
		Simulacao simulacao = findById(idSimulacao);
		
		if(!validateFatorCubagemPadraoRodo(simulacao)){
			return false;
		}
				
		if(!validateLimiteCubagem(simulacao)){
			return false;
		}
		
		// apenas realiza a validação dos limites de desconto caso a tabela
		// de preço tenha sido preenchida
		if (simulacao.getTabelaPreco() == null) {
			return true;
		}
		TypedFlatMap tabelaPreco = tabelaPrecoService.findTiposByIdTabelaPreco(simulacao.getTabelaPreco().getIdTabelaPreco());

		/** Valida ParametrosCliente da Proposta/Simulacao */
		List<ParametroCliente> parametros = null;
		if("C".equals(simulacao.getTpGeracaoProposta().getValue())) {
			Proposta proposta = propostaService.findByIdSimulacao(idSimulacao);
			parametros = parametroClienteService.findParametroByIdProposta(proposta.getIdProposta());
		} else if(simulacao.getTpGeracaoProposta() != null && "P".equals(simulacao.getTpGeracaoProposta().getValue())) {
			return false;//Se o tipo de geração da proposta for “Capital / Interior – Percentual” retornar false.
		} else if(simulacao.getNrFatorDensidade() != null){
			return false;//Se o campo “Fator densidade” estiver preenchido com valor, então retornar false.
		} else {
			parametros = simulacao.getParametroClientes();
		}
		if (parametros != null && !parametros.isEmpty()) {
			for (ParametroCliente parametroCliente : parametros) {
				if ("P".equals(parametroCliente.getTpSituacaoParametro().getValue())) {
					if (!validateGrupoFretePeso(tabelaPreco, parametroCliente)) {
						return false;
					}
					if (!validateGrupoFreteValor(tabelaPreco, parametroCliente)) {
						return false;
					}
					if (!validateGrupoGris(tabelaPreco, parametroCliente, simulacao)) {
						return false;
					}
					if (!validateGrupoPedagio(tabelaPreco, parametroCliente, simulacao)) {
						return false;
					}
					if (!validateTotalFrete(tabelaPreco, parametroCliente)) {
						return false;
					}
					if (!validateGeneralidades(tabelaPreco, parametroCliente, simulacao)) {
						return false;
					}
					//8. Grupo TDE e Grupo TRT
					if (!validateGrupoTrt(tabelaPreco, parametroCliente, simulacao)) {
						return false;
					}
					if (!validateGrupoTde(tabelaPreco, parametroCliente, simulacao)) {
						return false;
					}
				}
			}
		}

		/** Valida Servicos Adicionais da Proposta */
		List<ServicoAdicionalCliente> servicoAdicionalClientes = simulacao.getServicoAdicionalClientes();
		if (servicoAdicionalClientes != null && !servicoAdicionalClientes.isEmpty()) {
			for (ServicoAdicionalCliente servicoAdicionalCliente : servicoAdicionalClientes) {
				BigDecimal limiteDesconto = findLimiteDescontoParcela(null, servicoAdicionalCliente.getParcelaPreco().getIdParcelaPreco(), tabelaPreco);
				if (limiteDesconto == null) {
					limiteDesconto = BigDecimalUtils.HUNDRED;
				}
				if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
					if (!TABELA.equals(servicoAdicionalCliente.getTpIndicador().getValue()) &&
						!DESCONTO.equals(servicoAdicionalCliente.getTpIndicador().getValue()) &&
						!ACRESCIMO.equals(servicoAdicionalCliente.getTpIndicador().getValue())) {
						return false;
					}
				}
				if (DESCONTO.equals(servicoAdicionalCliente.getTpIndicador().getValue()) 
						&& CompareUtils.gt(servicoAdicionalCliente.getVlValor(), limiteDesconto)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Validações referente a Limites de Cubagem da Simulacao
	 * 
	 * @param Simulacao
	 * @return <code>false</code> Caso a Simulacao possuir Limitadores
	 */
	public boolean validateLimiteCubagem(Simulacao simulacao) {
		if(simulacao.getNrLimiteMetragemCubica() != null ||
				simulacao.getNrLimiteQuantVolume() != null){
			return false;
		}
		return true;
	}
	
	/**
	 * Validações referente a DestinoProposta
	 * @param idSimulacao
	 * @return
	 */
	public boolean validateLimiteDescontoDestinosProposta(Long idSimulacao) {
		Simulacao simulacao = findById(idSimulacao);
		// apenas realiza a validação dos limites de desconto caso a tabela
		// de preço tenha sido preenchida
		if (simulacao.getTabelaPreco() == null
				|| simulacao.getTpGeracaoProposta() == null 
				|| !"C".equals(simulacao.getTpGeracaoProposta().getValue())) {
			return true;
		}
		TypedFlatMap tabelaPreco = tabelaPrecoService.findTiposByIdTabelaPreco(simulacao.getTabelaPreco().getIdTabelaPreco());

		/** Valida Destinos da Proposta */
		List<DestinoProposta> destinosProposta = destinoPropostaService.findDestinosPropostaByIdSimulacao(idSimulacao);
		if (destinosProposta != null && !destinosProposta.isEmpty()) {
			for (DestinoProposta destinoProposta : destinosProposta) {
				if (!validateGrupoFretePeso(tabelaPreco, destinoProposta)) {
					return false;
				}
				if (!validateGrupoFreteValor(tabelaPreco, destinoProposta)) {
					return false;
				}
			}
		}
		return true;
	}

	public String executeWorkflow(List<Long> idsSimulacao, List<String> tpSituacoesWorkflow) {
		if (idsSimulacao != null && tpSituacoesWorkflow != null) {
			if (idsSimulacao.size() != tpSituacoesWorkflow.size()) {
				return configuracoesFacade.getMensagem("LMS-01110");
			}
			for(int i = 0; i < idsSimulacao.size(); i++) {
				Long idSimulacao = idsSimulacao.get(i);
				String tpSituacaoAprovacao = tpSituacoesWorkflow.get(i);
				Simulacao simulacao = findById(idSimulacao);
				simulacao.setTpSituacaoAprovacao(new DomainValue(tpSituacaoAprovacao));
				if ("A".equals(tpSituacaoAprovacao)) {
					List<Acao> acoes = acaoService.findByIdPendenciaTpSituacaoAcao(simulacao.getPendenciaAprovacao().getIdPendencia(), "A");
					if (acoes != null && !acoes.isEmpty()) {
						Acao acao = acoes.get(0);
						simulacao.setUsuarioByIdUsuarioAprovou(acao.getUsuario());
						simulacao.setDtAprovacao(acao.getDhAcao().toYearMonthDay());
					}
				} else {
					simulacao.setUsuarioByIdUsuarioAprovou(null);
					simulacao.setDtAprovacao(null);
				}
				store(simulacao);
			}
		}
		return null;
	}
	
	public TypedFlatMap executeAprovacaoProposta() {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		this.validateAprovacaoProposta(simulacao);
		this.storePendenciaAprovacaoProposta(simulacao, true);
		simulacao = this.findById(simulacao.getIdSimulacao());
		SimulacaoUtils.setSimulacaoInSession(simulacao);

		TypedFlatMap result = new TypedFlatMap();
		if (simulacao.getTpSituacaoAprovacao() != null) {
			String value = simulacao.getTpSituacaoAprovacao().getValue();
			String description = domainValueService.findDomainValueDescription(DM_SITUACAO_SIMULACAO, value);

			result.put("tpSituacaoAprovacao.description", description);
			result.put("tpSituacaoAprovacao.value", value);
		}
		if (simulacao.getUsuarioByIdUsuarioAprovou() != null) {
			result.put("usuarioByIdUsuarioAprovou.nrMatricula", simulacao.getUsuarioByIdUsuarioAprovou().getNrMatricula());
			result.put("usuarioByIdUsuarioAprovou.nmUsuario", simulacao.getUsuarioByIdUsuarioAprovou().getNmUsuario());
		}
		result.put("pendencia.idPendencia", "");
		if (simulacao.getPendenciaAprovacao() != null) {
			Long idPendencia = simulacao.getPendenciaAprovacao().getIdPendencia();
			result.put("pendencia.idPendencia", idPendencia);
		}
		result.put("dtAprovacao", simulacao.getDtAprovacao());
		return result;
	}
	
	public void executeReprovarEfetivacao(TypedFlatMap parameters) {
		Long idSimulacao = parameters.getLong(ID_SIMULACAO);
		Simulacao simulacao = this.findById(idSimulacao);
		simulacao.setTpSituacaoAprovacao(domainValueService.findDomainValueByValue(DM_SITUACAO_SIMULACAO, TP_SITUACAO_EFETIVACAO_REPROVADA));
		HistoricoEfetivacao historicoEfetivacao = getHistoricoEfetivacaoService().findLastHistoricoByIdSimulacao(idSimulacao);
		if (historicoEfetivacao.getDhReprovacao() == null) {
			Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
			UsuarioLMS usuarioLMS = usuarioLMSService.findById(usuarioLogado.getIdUsuario());
			historicoEfetivacao.setUsuarioReprovador(usuarioLMS);
			historicoEfetivacao.setDhReprovacao(new DateTime());
			historicoEfetivacao.setDsMotivo(parameters.getString("observacaoReprovacao"));
			historicoEfetivacao.setSimulacao(simulacao);
			
			if(parameters.containsKey("idMotivoReprovacao")){
				Long idMotivoReprovacao = MapUtils.getLong(parameters, "idMotivoReprovacao");
				MotivoReprovacao motivo = motivoReprovacaoService.findById(idMotivoReprovacao);
				historicoEfetivacao.setMotivoReprovacao(motivo);
			}
			
			this.store(simulacao);
			SimulacaoUtils.setSimulacaoInSession(simulacao);
			historicoEfetivacaoService.store(historicoEfetivacao);
			gerarEmailReprovacaoEfetivacao(historicoEfetivacao);
		}
	}
	
	private void gerarEmailReprovacaoEfetivacao(HistoricoEfetivacao historico){
		String remetente =  (String)configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		String destinatario = null; 
		StringBuilder assunto = new StringBuilder();
		StringBuilder corpo =  new StringBuilder();
		if(historico != null){
			if(historico.getUsuarioSolicitacao() != null && historico.getUsuarioSolicitacao().getUsuarioADSM() != null){
				destinatario = historico.getUsuarioSolicitacao().getUsuarioADSM().getDsEmail();
			} else {
				return;
			}
			
			if(historico.getSimulacao() != null && historico.getSimulacao().getFilial() != null){
				Object[] arguments = new Object[] {
						historico.getSimulacao().getFilial().getSgFilial(),
						historico.getSimulacao().getNrSimulacao().toString()
				};
				assunto.append(MessageFormat.format(configuracoesFacade.getMensagem("assuntoEmailReprovacaoEfetivacao"), arguments));
				corpo.append(MessageFormat.format(configuracoesFacade.getMensagem("textoEmailReprovacaoEfetivacao"), arguments));
				
			} else {
				return;
			}
			if(historico.getMotivoReprovacao() != null){
				corpo.append(historico.getMotivoReprovacao().getDsMotivoReprovacao())
				.append(LINE_SEPARATOR);
			}

			corpo.append(historico.getDsMotivo());
			
			gerarEmailMensagemAvisoService.sendEmail(destinatario, assunto.toString(),remetente, corpo.toString());
		} else {
			return;
		}
	}

	/**
	 * Verifica a situação da proposta
	 * @param idSimulacao
	 */
	public void validateSituacaoProposta(Long idSimulacao){
		Simulacao simulacao = findById(idSimulacao);
		if(Boolean.TRUE.equals(simulacao.getBlEfetivada())) {
			throw new BusinessException("LMS-01125");
		}
		if ((simulacao.getTpSituacaoAprovacao()==null)||(!simulacao.getTpSituacaoAprovacao().getValue().equals("M"))) {
			throw new BusinessException("LMS-01111");
		}
	}

	/**
	 * Atualiza a simulação para proposta efetivada
	 * @param idSimulacao
	 * @param idDivisaoCliente
	 */
	public java.io.Serializable updatePropostaEfetivadaFromSimulacao(Long idSimulacao, Long idDivisaoCliente, ParametroCliente parametroCliente) {
		Simulacao simulacao = findById(idSimulacao);
		simulacao.setBlEfetivada(Boolean.TRUE);
		simulacao.setDtEfetivacao(JTDateTimeUtils.getDataAtual());
		simulacao.setDtTabelaVigenciaInicial(parametroCliente.getDtVigenciaInicial());
		//LMS-6190
		simulacao.setUsuarioByIdUsuarioEfetivou(SessionUtils.getUsuarioLogado());
		DomainValue dominio = configuracoesFacade.getDomainValue("DM_SITUACAO_SIMULACAO", "F");
		simulacao.setTpSituacaoAprovacao(dominio);
		
		DivisaoCliente divisaoCliente = new DivisaoCliente();
		divisaoCliente.setIdDivisaoCliente(idDivisaoCliente);
		simulacao.setDivisaoCliente(divisaoCliente);
		parametroCliente.setSimulacao(simulacao);

		return store(simulacao);
	}

	public ResultSetPage findPaginatedParametros(TypedFlatMap criteria) {
		return getSimulacaoDAO().findPaginatedParametros(criteria);
	}

	public Integer getRowCountParametros(TypedFlatMap criteria) {
		return getSimulacaoDAO().getRowCountParametros(criteria);
	}

	public Simulacao storeEfetivacaoProposta(Long idSimulacao, Long idDivisaoCliente, YearMonthDay dtInicioVigencia,
			Boolean blConfirmaEfetivarProposta) {
		Long idTabelaDivisaoCliente = null;
		if(idDivisaoCliente == null) {
			throw new BusinessException("LMS-01121");
		}

		//Verifica a situação da proposta - 1
		validateSituacaoProposta(idSimulacao);
		//Verifica a data de início da vigência - 4
		vigenciaService.validateInicioVigencia(dtInicioVigencia, "LMS-00006");

		List<DestinoProposta> destinosProposta = destinoPropostaService.findDestinosPropostaByIdSimulacao(idSimulacao);
		if (destinosProposta != null){
			for (DestinoProposta destinoProposta:destinosProposta){
				if (destinoProposta.getVlAdvalorem().add(destinoProposta.getPcDiferencaAdvalorem()).compareTo(BigDecimal.ZERO) < 0){
					throw new BusinessException("LMS-30064");
				}
			}
		}

		//Identificar tabela da divisão do cliente - 3
		Simulacao simulacao = findById(idSimulacao);
		TabelaPreco tabelaPreco = simulacao.getTabelaPreco();

		/** Verificar se existe vínculo entre a divisão, tabela e serviço informados na proposta */
		Long idTabelaPrecoAtual = LongUtils.ZERO;
		TabelaDivisaoCliente tabelaDivisaoCliente = tabelaDivisaoClienteService.findTabelaDivisaoCliente(idDivisaoCliente, simulacao.getServico().getIdServico());
		if(tabelaDivisaoCliente == null) {
			tabelaDivisaoCliente = new TabelaDivisaoCliente();
			tabelaDivisaoCliente.setServico(simulacao.getServico());
			tabelaDivisaoCliente.setBlObrigaDimensoes(FALSE);
			tabelaDivisaoCliente.setTpPesoCalculo(new DomainValue("A"));

			DivisaoCliente divisaoCliente = divisaoClienteService.findById(idDivisaoCliente);
			tabelaDivisaoCliente.setDivisaoCliente(divisaoCliente);

			if (tabelaPreco == null) {
				tabelaPreco = tabelaPrecoService.findTabelaVigente("M", "X", TRUE, JTDateTimeUtils.getDataAtual());
			}
			
			/*CQPRO00025225*/
			tabelaDivisaoCliente.setNrFatorCubagem(simulacao.getNrFatorCubagem());
			
			/*TABELA_DIVISAO_CLIENTE.NR_FATOR_DENSIDADE = <Campo SIMULACAO.NR_FATOR_DENSIDADE >*/
			tabelaDivisaoCliente.setNrFatorDensidade(simulacao.getNrFatorDensidade());
		} else {
			// LMS-6168 - verifica tabelas de preço diferentes e confirmação
			if (!blConfirmaEfetivarProposta && !tabelaPreco.equals(tabelaDivisaoCliente.getTabelaPreco())) {
				String arg0 = stringTipoTabelaPreco(tabelaPreco);
				String arg1 = stringTipoTabelaPreco(tabelaDivisaoCliente.getTabelaPreco());
				throw new BusinessException("LMS-01233", new Object[] { arg0, arg1 });
			}
			
			/**
			 * Gera Historido referente ao Reajuste
			 * #Quest 11147
			 */
			historicoReajusteClienteService.generateHistoricoReajusteCliente(
				tabelaDivisaoCliente.getIdTabelaDivisaoCliente()
				,tabelaDivisaoCliente.getTabelaPreco().getIdTabelaPreco()
				,tabelaPreco.getIdTabelaPreco()
				,ZERO
				,"P");

			/** Armazena ID da TabelaPreco Atual */
			idTabelaPrecoAtual = tabelaDivisaoCliente.getTabelaPreco().getIdTabelaPreco();
			
				tabelaDivisaoCliente.setNrFatorCubagem(simulacao.getNrFatorCubagem());
			
			
			/* Se existir o registro(TABELA_DIVISAO_CLIENTE) e SIMULACAO.NR_FATOR_DENSIDADE 
			 * for diferente de TABELA_DIVISAO_CLIENTE.NR_FATOR_DENSIDADE, 
			 * atualizar a informação conforme abaixo na tabela TABELA_DIVISAO_CLIENTE
			 */
			if (BigDecimalUtils.hasValue(simulacao.getNrFatorDensidade()) && !simulacao.getNrFatorDensidade().equals(tabelaDivisaoCliente.getNrFatorDensidade()))
				tabelaDivisaoCliente.setNrFatorDensidade(simulacao.getNrFatorDensidade());		
		}

		tabelaDivisaoCliente.setNrLimiteMetragemCubica(simulacao.getNrLimiteMetragemCubica());
		tabelaDivisaoCliente.setNrLimiteQuantVolume(simulacao.getNrLimiteQuantVolume());
		tabelaDivisaoCliente.setTabelaPreco(tabelaPreco);
		tabelaDivisaoCliente.setTabelaPrecoFob(simulacao.getTabelaPrecoFob());
		tabelaDivisaoCliente.setBlPagaFreteTonelada(simulacao.getBlPagaFreteTonelada());

		TabelaPreco tabelaPrecoFob = simulacao.getTabelaPrecoFob();
		String tpTabelaPreco = tabelaPreco.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();
		/** Se a tabela informada em ID_TABELA_PRECO for uma tabela do tipo “M” ou “R” e estiver vigente */
		if (("M".equals(tpTabelaPreco) || "R".equals(tpTabelaPreco))
				&& JTVigenciaUtils.isVigente(tabelaPreco, JTDateTimeUtils.getDataAtual())) {

			if (tabelaPrecoFob == null) {
				tabelaDivisaoCliente.setBlAtualizacaoAutomatica(TRUE);
			} else {
				String tpTabelaPrecoFob = tabelaPrecoFob.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();
				Integer nrVersao = tabelaPreco.getTipoTabelaPreco().getNrVersao();
				Integer nrVersaoFob = tabelaPrecoFob.getTipoTabelaPreco().getNrVersao();

				if (tpTabelaPreco.equals(tpTabelaPrecoFob) && nrVersao.equals(nrVersaoFob)) {
					tabelaDivisaoCliente.setBlAtualizacaoAutomatica(TRUE);
				} else {
					tabelaDivisaoCliente.setBlAtualizacaoAutomatica(FALSE);
				}
			}
		} else {
			tabelaDivisaoCliente.setBlAtualizacaoAutomatica(FALSE);
		}

		// LMS-4415
		tabelaDivisaoCliente.setBlPesoAferido(true);
		tabelaDivisaoCliente.setBlPesoCubadoAferido(true);
		tabelaDivisaoCliente.setBlPesoCubadoDeclarado(true);
		tabelaDivisaoCliente.setBlPesoDeclarado(true);
		
		// LMS-3635
		tabelaDivisaoCliente.setBlImpBaseRefaturamento(true);
		tabelaDivisaoCliente.setBlImpBaseReentrega(true);
		tabelaDivisaoCliente.setBlImpBaseDevolucao(true);
		
		tabelaDivisaoClienteService.store(tabelaDivisaoCliente);
		idTabelaDivisaoCliente = tabelaDivisaoCliente.getIdTabelaDivisaoCliente();

		/** Verifica se o subtipo é diferente de P (Solicitado pela Lidiane) */
		if (!"O".equals(simulacao.getTpGeracaoProposta().getValue()) && simulacao.getTabelaPreco() != null
				&& !"P".equals(tabelaDivisaoCliente.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco())) {

			/** Se mudar TabelaPreco, fechar todos os parametros relacionados a TabelaDivisaoCliente */
			if(!idTabelaPrecoAtual.equals(tabelaPreco.getIdTabelaPreco())) {
				parametroClienteService.storeCloseParametroAtivo(idTabelaDivisaoCliente, dtInicioVigencia);
			}

			/** Valida se foi incluido algum parametro na Proposta Atual */
			List<Long> idParametros = parametroClienteService.findIdsParametrosByIdSimulacao(idSimulacao);
			if(idParametros.isEmpty()) {
				throw new BusinessException("LMS-01126");
			}

			/** Atualiza Parametros */
			for(Long idParametroClienteNew : idParametros) {
				/** Valida situação do parametro - 2 */
				parametroClienteService.validateSituacaoParametrizacao(idParametroClienteNew, "LMS-01112");
				/** Busca Parametro anterior de MESMA ROTA se existir */
				Long idParametroClienteVigente = parametroClienteService.findParametrizacaoAtivaByDtVigencia(idParametroClienteNew, idTabelaDivisaoCliente, dtInicioVigencia);
				/** Fecha parametrização ANTERIOR e ativa NOVO PARAMETRO- 5 */
				parametroClienteService.storeParametroAtivo(idParametroClienteVigente, idParametroClienteNew, idTabelaDivisaoCliente, dtInicioVigencia, idSimulacao, idDivisaoCliente);
			}
			
			//LMS-5565
			generateServicoAdicionalCliente(simulacao, tabelaDivisaoCliente);
			
		} else {
			simulacao.setDtTabelaVigenciaInicial(dtInicioVigencia);
			simulacao.setBlEfetivada(TRUE);
			simulacao.setDtEfetivacao(JTDateTimeUtils.getDataAtual());
			//LMS-6190
			simulacao.setUsuarioByIdUsuarioEfetivou(SessionUtils.getUsuarioLogado());
			DomainValue dominio = configuracoesFacade.getDomainValue("DM_SITUACAO_SIMULACAO", "F");
			simulacao.setTpSituacaoAprovacao(dominio);
			
			store(simulacao);
		}

		this.parametroClienteService.removeParametroClienteDataSuperiorReajuste(tabelaDivisaoCliente.getIdTabelaDivisaoCliente());
		
		return simulacao;
	}


	/**
	 * LMS-6168 - Auxiliar para gerar string com identificação de tabela de
	 * preço conforme padrão da aplicação, incluindo tipo, versão e subtipo.
	 * 
	 * @param tabelaPreco
	 * @return String no formato T99-X
	 */
	private String stringTipoTabelaPreco(TabelaPreco tabelaPreco) {
		TipoTabelaPreco tipoTabelaPreco = tabelaPreco.getTipoTabelaPreco();
		SubtipoTabelaPreco subtipoTabelaPreco = tabelaPreco.getSubtipoTabelaPreco();
		String string = tipoTabelaPreco.getTpTipoTabelaPreco().getValue()
				+ tipoTabelaPreco.getNrVersao()
				+ "-"
				+ subtipoTabelaPreco.getTpSubtipoTabelaPreco();
		return string;
	}

	/**
	 * Controla serviço adiciona cliente.
	 * @param simulacao
	 * @param idTabelaPrecoAtual
	 * @param tabelaDivisaoCliente
	 */
	private void generateServicoAdicionalCliente(Simulacao simulacao, TabelaDivisaoCliente tabelaDivisaoCliente) {
		List<Long> listaIdsTabelaDivisaoCliente = new ArrayList<Long>();
		listaIdsTabelaDivisaoCliente.add(tabelaDivisaoCliente.getIdTabelaDivisaoCliente());
		servicoAdicionalClienteService.removeByTabelasDivisaoCliente(listaIdsTabelaDivisaoCliente);
		
		for (ServicoAdicionalCliente servicoAdicionalCliente : simulacao.getServicoAdicionalClientes()) {
			ServicoAdicionalCliente novoServicoAdicionalCliente = servicoAdicionalCliente.clone();
			novoServicoAdicionalCliente.setBlAlterou(null);
			novoServicoAdicionalCliente.setCotacao(null);
			novoServicoAdicionalCliente.setSimulacao(null);
			novoServicoAdicionalCliente.setTabelaDivisaoCliente(tabelaDivisaoCliente);
			novoServicoAdicionalCliente.setVlValorFormatado(null);
			servicoAdicionalClienteService.store(novoServicoAdicionalCliente);
		}
	}

	public void storePendenciaAprovacaoProposta(Simulacao simulacao, boolean blAprova) {
		DomainValue tpSituacaoAprovacao = simulacao.getTpSituacaoAprovacao();
		if (blAprova && ConstantesExpedicao.MODAL_AEREO.equals(simulacao.getServico().getTpModal().getValue())) {
			executeValidacaoPropostaAerea(simulacao, blAprova);
		} else if ((tpSituacaoAprovacao == null || "I".equals(tpSituacaoAprovacao.getValue()) || "H"
				.equals(tpSituacaoAprovacao.getValue())) && blAprova == true) {
			executeValidacaoProposta(simulacao, blAprova);
		} else {
			storeCancelaPendenciaProposta(simulacao);
		}
	}

	/**
	 * validação incluida  para verificar a geração e situação do workflow  
	 * 
	 * @param simulacao
	 * @param blAprova
	 */
	private void executeValidacaoPropostaAerea(Simulacao simulacao, Boolean blAprova) {
		//7665
		verificaSituacaoAprovacao(simulacao);
		if(simulacao.getPendenciaAprovacao() == null){
			verificarLimiteDesconto(simulacao);
		}
		
		if(ArrayUtils.contains(TIPOS_PROPOSTA_VALIDOS, simulacao.getTpGeracaoProposta().getValue())) {
			destinoPropostaService.generateParametrosProposta(simulacao.getIdSimulacao());
		}
	}	

	/**
	 * Enquandra o pcVariacao do markupInvalido calculado em "até" determinado pcVariacao cadastrado nos níveis de markup.
	 * Retorna texto com a descrição das parcelas inválidas e nrTipoEvento de maior prioridade entres elas. 
	 * 
	 * @param simulacao
	 * @param markupsInvalidos
	 * @return
	 */
	private Map<String, Object> validateNiveisMarkup(Simulacao simulacao, List<TypedFlatMap> markupsInvalidos){
		Map<String, Object> retorno = new HashMap<String, Object>();
		
		if(markupsInvalidos != null && !markupsInvalidos.isEmpty() && simulacao.getTabelaPreco().getTabelaPrecoCustoTnt() != null){
			Short nrTipoEventoParcela = null;
			BigDecimal pcVariacaoCalculado = null;
			BigDecimal pcVariacaoNivelMarkup = null;
			Short nrTipoEventoMaiorPrioridade = null;
			StringBuilder dsPendenciaMarkup = new StringBuilder();
			Set<Short> nrTipoEventosEncontrados = new HashSet<Short>();
			
			List<Map<String, Object>> niveisMarkup = comiteNivelMarkupService.findNiveisMarkupVigentesNaoIsentoByIdTabelaPreco(simulacao.getTabelaPreco().getTabelaPrecoCustoTnt().getIdTabelaPreco());
			for (TypedFlatMap  markupsInvalidoMap : markupsInvalidos) {
				for (Map<String, Object> nivelMarkupMap : niveisMarkup) {
					if(markupsInvalidoMap != null && markupsInvalidoMap.getLong("idParcela").equals(nivelMarkupMap.get("idParcelaPreco"))){
					
						pcVariacaoCalculado = (BigDecimal) markupsInvalidoMap.get("pcVariacao");
						pcVariacaoNivelMarkup = (BigDecimal) nivelMarkupMap.get("pcVariacao");
						
						if(pcVariacaoCalculado != null && pcVariacaoNivelMarkup != null && pcVariacaoCalculado.compareTo(pcVariacaoNivelMarkup) <= 0){
							
							if("S".equals(nivelMarkupMap.get("blIsento"))){
								nrTipoEventoParcela = null;
								
							} else {
								nrTipoEventoParcela = (Short) nivelMarkupMap.get("nrTipoEvento");
							}
	
							/*
							 * A lista de niveisMarkup está ordenada por pcVariacao
							 * crescente, logo o primeiro registro que encontrar com
							 * pcVariacao menor ou igual é o registro que deve ser
							 * utilizado.
							 */
							break;
						}						
					}
				}
				
				if(nrTipoEventoParcela != null){
					dsPendenciaMarkup.append(markupsInvalidoMap.getString("dsParcela"));
					dsPendenciaMarkup.append("\n");
					nrTipoEventosEncontrados.add(nrTipoEventoParcela);
					nrTipoEventoParcela = null;
				}
			}
			
			if(!nrTipoEventosEncontrados.isEmpty()){
				nrTipoEventoMaiorPrioridade = findNrTipoEventoMaiorPrioridade(nrTipoEventosEncontrados); 
				retorno.put("nrTipoEventoMaiorPrioridade", nrTipoEventoMaiorPrioridade);
				retorno.put("dsPendenciaMarkup", dsPendenciaMarkup.toString());
			}
		}
		
		return retorno;
	}
	
	/**
	 * Lê os números dos eventos e suas prioridades do parâmetro geral, e
	 * retorna o número de evento de maior prioridade, ou seja, com valor de
	 * prioridade mais baixo.
	 * 
	 * @param nrTipoEventosEncontrados
	 * @return nrTipoEventoMaiorPrioridade
	 */
	private Short findNrTipoEventoMaiorPrioridade(Set<Short> nrTipoEventosEncontrados){
		Map<Short, Integer> eventoPrioridadeMap = new HashMap<Short, Integer>();

		String prioridadesNrEventosWorkflowString = (String) configuracoesFacade.getValorParametro("PRIORIDADE_EVENTO_WORKFLOW");
		String[] eventosPrioridade = prioridadesNrEventosWorkflowString.split(";");
		for (String eventoPrioridadeString : eventosPrioridade) {
			String[] eventoMaisPrioridade = eventoPrioridadeString.split("-");
			eventoPrioridadeMap.put(Short.valueOf(eventoMaisPrioridade[0]), Integer.valueOf(eventoMaisPrioridade[1]));
		}
		
		Short nrTipoEventoMaiorPrioridade = null;
		for (Short nrTipoEvento : nrTipoEventosEncontrados) {
			if(eventoPrioridadeMap.get(nrTipoEvento) != null){
				if(nrTipoEventoMaiorPrioridade == null){
					nrTipoEventoMaiorPrioridade = nrTipoEvento;
					
				} else if(eventoPrioridadeMap.get(nrTipoEvento).compareTo(eventoPrioridadeMap.get(nrTipoEventoMaiorPrioridade)) < 0){
					nrTipoEventoMaiorPrioridade = nrTipoEvento;
				}
			}
		}
		
		return nrTipoEventoMaiorPrioridade;
	}
	
	/**
	 * se limite estiver abaixo do parametro
	 * cria a pendencia senao atualiza os campos da simulação
	 */
	private void verificarLimiteDesconto(Simulacao simulacao) {
		List<TypedFlatMap> markupsInvalidos = validateMarkupProposta(simulacao);
		Map<String, Object> niveisMarkupMap = validateNiveisMarkup(simulacao, markupsInvalidos);
		Boolean generateWkByFatorCubagem = validateLimiteDescontoPropostaAereo(simulacao.getNrFatorCubagem()); 

		if(generateWkByFatorCubagem || !niveisMarkupMap.isEmpty()){
			Short nrTipoEvento = null;
			StringBuilder dsProcesso = new StringBuilder(configuracoesFacade.getMensagem("proposta"));
			dsProcesso.append(" ");
			dsProcesso.append(simulacao.getFilial().getSgFilial());
			dsProcesso.append(" ");
			dsProcesso.append(simulacao.getNrSimulacao());
			dsProcesso.append("\n");

			//Deve ser gerado somente um WK. O WK para fator de cubagem tem prioridade sobre os WK´s de markup.
			if(generateWkByFatorCubagem && !niveisMarkupMap.isEmpty()){
				nrTipoEvento = ConstantesVendas.ID_WORKFLOW_ALTERACAO_FATOR_CUBAGEM_AEREO;
				dsProcesso.append(configuracoesFacade.getMensagem("LMS-30081", new Object[]{simulacao.getNrFatorCubagem()}));
				dsProcesso.append("\n");
				dsProcesso.append(niveisMarkupMap.get("dsPendenciaMarkup"));
				
			} else if(generateWkByFatorCubagem && niveisMarkupMap.isEmpty()){
				nrTipoEvento = ConstantesVendas.ID_WORKFLOW_ALTERACAO_FATOR_CUBAGEM_AEREO;
				dsProcesso.append(configuracoesFacade.getMensagem("LMS-30081", new Object[]{simulacao.getNrFatorCubagem()}));
				
			} else {
				nrTipoEvento = (Short) niveisMarkupMap.get("nrTipoEventoMaiorPrioridade");
				dsProcesso.append(niveisMarkupMap.get("dsPendenciaMarkup"));
			}
			
			storePendenciaAprovacaoPropostaAereo(simulacao.getIdSimulacao(), nrTipoEvento, dsProcesso.toString());
			
		}else{
			atualizarCamposAprovacao(simulacao.getIdSimulacao());
		}
	}

    private List<TypedFlatMap> validateMarkupProposta(Simulacao simulacao){
        List<TypedFlatMap> markupsInvalidos = new ArrayList<TypedFlatMap>();
        
        
        TabelaPreco tabelaCustoMarkup = simulacao.getTabelaPreco().getTabelaPrecoCustoTnt();
        
        if (tabelaCustoMarkup != null){
            Proposta proposta = propostaService.findByIdSimulacao(simulacao.getIdSimulacao());
            List<Map<String, Object>> markupParcelasProposta = getSimulacaoDAO().findParcelasMarkupProposta(tabelaCustoMarkup.getIdTabelaPreco(),proposta.getIdProposta(),true);
            if (markupParcelasProposta == null || markupParcelasProposta.isEmpty()){
                markupParcelasProposta = getSimulacaoDAO().findParcelasMarkupProposta(tabelaCustoMarkup.getIdTabelaPreco(), proposta.getIdProposta(),false);
            }
            
            
            List<DestinoProposta> destinosProposta  = destinoPropostaService.findDestinosPropostaByIdSimulacao(simulacao.getIdSimulacao());
            String sgUFOrigem = proposta.getUnidadeFederativaByIdUfOrigem().getSgUnidadeFederativa();
            String aeroportoOrigem = aeroportoService.findAeroportoAtendeCliente(simulacao.getClienteByIdCliente().getIdCliente()).getSgAeroporto();
            String dsRota = sgUFOrigem+" - "+aeroportoOrigem+" > ";
            
            for(DestinoProposta destinoProposta : destinosProposta){
                List<TypedFlatMap> markupsDestino = validateMarkupDestinoProposta(destinoProposta,markupParcelasProposta,dsRota);
                if (markupsDestino != null && !markupsDestino.isEmpty()){
                    markupsInvalidos.addAll(markupsDestino);
                }
            }
            
            
            List<ParametroCliente> parametros = parametroClienteService.findParametroByIdProposta(proposta.getIdProposta());
            if (parametros != null){
                for (ParametroCliente parametro: parametros){
                    validateMarkupParametroCliente(parametro, markupParcelasProposta, markupsInvalidos);
                }
            }
            
            List<ServicoAdicionalCliente> servicoAdicionalClientes = servicoAdicionalClienteService.findByTabelaSimulacaoCliente(simulacao.getIdSimulacao());
            for (ServicoAdicionalCliente servicoAdicionalCliente: servicoAdicionalClientes){
                Map<String,Object> parcelaMarkup = findParcelaMarkup(markupParcelasProposta, servicoAdicionalCliente.getParcelaPreco().getCdParcelaPreco(), null);
                if (parcelaMarkup != null){
                    TypedFlatMap markup = validateMarkup(servicoAdicionalCliente.getVlValor(), parcelaMarkup, null);
                    if (markup != null){
                        markupsInvalidos.add(markup);
                    }
                }   
            }
        }
        return markupsInvalidos;
    }
    

    private void validateMarkupParametroCliente(ParametroCliente parametro, List<Map<String, Object>> markupParcelasProposta,
            List<TypedFlatMap> markupsInvalidos) {
        
        List<GeneralidadeCliente> generalidades = parametro.getGeneralidadeClientes();
        for (GeneralidadeCliente generalidade:generalidades){
            Map<String,Object> parcelaMarkup = findParcelaMarkup(markupParcelasProposta, generalidade.getParcelaPreco().getCdParcelaPreco(), null);
            if (parcelaMarkup != null){
                TypedFlatMap markup = validateMarkup(generalidade.getVlGeneralidade(), parcelaMarkup, null);
                if (markup != null){
                    markupsInvalidos.add(markup);
                }
            }
        }
        
        List<TaxaCliente> taxasCliente = parametro.getTaxaClientes();
        for(TaxaCliente taxaCliente: taxasCliente){
            Map<String,Object> parcelaMarkup = findParcelaMarkup(markupParcelasProposta, taxaCliente.getParcelaPreco().getCdParcelaPreco(), null);
            if (parcelaMarkup != null){
                TypedFlatMap markup = validateMarkup(taxaCliente.getVlTaxa(), parcelaMarkup, null);
                if (markup != null){
                    markupsInvalidos.add(markup);
                }
            }
        }
    }


    private List<TypedFlatMap> validateMarkupDestinoProposta(DestinoProposta destinoProposta,
            List<Map<String, Object>> markupParcelasProposta, String dsRota) {
        List<TypedFlatMap> markupsInvalidos = new ArrayList<TypedFlatMap>();
        
        
        String dsRotaCompleta = dsRota + destinoProposta.getUnidadeFederativa().getSgUnidadeFederativa() + 
                " - "+ destinoProposta.getAeroportoByIdAeroporto().getSgAeroporto();
        
        
        //Frete peso - faixa progressiva
        validateMarkupParcelasFretePeso(destinoProposta,markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        
        //ProdutoEspecifico
        validateMarkupParcelasProdutoEspecifico(destinoProposta,markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        
        //Precofrete
        validateMarkupParcelasPrecoFrete(destinoProposta,markupParcelasProposta,dsRotaCompleta,markupsInvalidos );
        
        //Taxas e parcelas sem rotaPreco
        validateMarkupParcela(ConstantesExpedicao.CD_ADVALOREM_1, destinoProposta.getVlAdvalorem(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        validateMarkupParcela(ConstantesExpedicao.CD_ADVALOREM_2, destinoProposta.getVlAdvalorem2(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        
        validateMarkupParcela(ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_CONVENCIONAL, destinoProposta.getVlTaxaColetaInteriorConvencional(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        validateMarkupParcela(ConstantesExpedicao.CD_TAXA_COLETA_INTERIOR_EMERGENCIA, destinoProposta.getVlTaxaColetaInteriorEmergencial(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        validateMarkupParcela(ConstantesExpedicao.CD_TAXA_COLETA_URBANA_CONVENCIONAL, destinoProposta.getVlTaxaColetaUrbanaConvencional(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        validateMarkupParcela(ConstantesExpedicao.CD_TAXA_COLETA_URBANA_EMERGENCIA, destinoProposta.getVlTaxaColetaUrbanaEmergencial(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        
        validateMarkupParcela(ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_CONVENCIONAL, destinoProposta.getVlTaxaEntregaInteriorConvencional(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        validateMarkupParcela(ConstantesExpedicao.CD_TAXA_ENTREGA_INTERIOR_EMERGENCIA, destinoProposta.getVlTaxaEntregaInteriorEmergencial(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        validateMarkupParcela(ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_CONVENCIONAL, destinoProposta.getVlTaxaEntregaUrbanaConvencional(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        validateMarkupParcela(ConstantesExpedicao.CD_TAXA_ENTREGA_URBANA_EMERGENCIA, destinoProposta.getVlTaxaEntregaUrbanaEmergencial(),markupParcelasProposta,dsRotaCompleta,markupsInvalidos);
        
        return markupsInvalidos;
    }
    

    private TypedFlatMap validateMarkup(BigDecimal vlParcela, Map<String, Object> mapParcela, String dsRotaCompleta){
        BigDecimal vlCalculado = MapUtilsPlus.getBigDecimal(mapParcela, "VL_MARKUP_CALCULADO");
        if (vlCalculado != null && vlParcela.compareTo(vlCalculado) < 0){
            
            /*
             * foi modificado para double pois as operações de arredondamento do BigDecimal não aceitam variações muito pequenas
             * resultando em  java.lang.ArithmeticException, caso não houvesse arradondamento.
             */
            double variacao = vlCalculado.doubleValue() -  vlParcela.doubleValue();
            double percentual = (variacao /vlCalculado.doubleValue()) * BigDecimalUtils.HUNDRED.doubleValue();
            BigDecimal pcVariacao = new BigDecimal(percentual);
            TypedFlatMap result = new TypedFlatMap();
            result.put("pcVariacao", pcVariacao);
            result.put("idParcela", MapUtilsPlus.getLong(mapParcela, "ID_PARCELA_PRECO"));
            if (dsRotaCompleta != null){
                result.put("dsParcela", configuracoesFacade.getMensagem("LMS-30083", new String[]{
                        dsRotaCompleta,
                        MapUtilsPlus.getString(mapParcela, "DS_PARCELA"),
                        FormatUtils.formatBigDecimalWithPattern(vlParcela, "#,##0.00"),
                        FormatUtils.formatBigDecimalWithPattern(pcVariacao, "#0.00"),
                }));
            }else{
                result.put("dsParcela", configuracoesFacade.getMensagem("LMS-30085", new String[]{
                        MapUtilsPlus.getString(mapParcela, "DS_PARCELA"),
                        FormatUtils.formatBigDecimalWithPattern(vlParcela, "#,##0.00"),
                        FormatUtils.formatBigDecimalWithPattern(pcVariacao, "#0.00"),
                }));
            }
            return result;
        }
        return null;
    }
    
    private void validateMarkupParcelasPrecoFrete(DestinoProposta destinoProposta,
            List<Map<String, Object>> markupParcelasProposta, String dsRotaCompleta, List<TypedFlatMap> markupsInvalidos) {
        
        //IDFreteQuilo
        if (!BigDecimal.ZERO.equals(destinoProposta.getVlFretePeso())){
        	
        	Map<String, Object> mapParcelaValidacao = null;
        	String tpGeracaoProposta = destinoProposta.getProposta().getSimulacao().getTpGeracaoProposta().getValue();
        	
            if (ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_EXCEDENTE.equals(tpGeracaoProposta)
            		|| ConstantesVendas.TP_PROPOSTA_MINIMO_MAIS_VALOR_KG.equals(tpGeracaoProposta) ){
				mapParcelaValidacao = findMarkupFreteQuiloMinimoExcedenteMinimoKg(destinoProposta, markupParcelasProposta);
            } else {
            	mapParcelaValidacao = findParcelaMarkup(markupParcelasProposta, ConstantesExpedicao.CD_FRETE_QUILO, destinoProposta.getRotaPreco().getIdRotaPreco());
            }
        	
            if(mapParcelaValidacao != null){
	            TypedFlatMap markupFreteQuilo = validateMarkup(destinoProposta.getVlFretePeso(), mapParcelaValidacao, dsRotaCompleta);
	            if (markupFreteQuilo != null){
	                markupsInvalidos.add(markupFreteQuilo);
	            }
            }
        }
        
        //IDTarifaMinima
        TypedFlatMap markupTarifaMinima = validateMarkup(destinoProposta.getVlFreteMinimo(),findParcelaMarkup(markupParcelasProposta, ConstantesExpedicao.CD_TARIFA_MINIMA, destinoProposta.getRotaPreco().getIdRotaPreco()),dsRotaCompleta);
        if (markupTarifaMinima != null){
            markupsInvalidos.add(markupTarifaMinima);
        }
    }


    /**
     * 
     * @param destinoProposta
     * @param markupParcelasProposta
     * @return
     */
	private Map<String, Object> findMarkupFreteQuiloMinimoExcedenteMinimoKg(DestinoProposta destinoProposta, List<Map<String, Object>> markupParcelasProposta) {
		Map<String, Object> mapParcela = null;
		
		ValorFaixaProgressiva valorFaixaProgressiva = valorFaixaProgressivaService.findMenorFaixaProgressivaByTabelaPrecoRotaPreco(
				destinoProposta.getProposta().getSimulacao().getTabelaPreco().getIdTabelaPreco(), destinoProposta.getRotaPreco().getIdRotaPreco());
	
		for (Map<String, Object> map : markupParcelasProposta) {
			if (ConstantesExpedicao.CD_FRETE_PESO.equals(map.get("CD_PARCELA_PRECO")) && map.get("VL_FAIXA_PROGRESSIVA") != null
					&& ((BigDecimal)map.get("VL_FAIXA_PROGRESSIVA")).compareTo(valorFaixaProgressiva.getFaixaProgressiva().getVlFaixaProgressiva()) == 0) {
				mapParcela = new HashMap<String, Object>(map);
				mapParcela.put("DS_PARCELA", NM_PARCELA_FRETE_QUILO);
				break;
			}
		}
		
		return mapParcela;
	};

    private void validateMarkupParcelasProdutoEspecifico(DestinoProposta destinoProposta,
            List<Map<String, Object>> markupParcelasProposta, String dsRotaCompleta, List<TypedFlatMap> markupsInvalidos) {
        List<ValorFaixaProgressivaProposta> valoresFaixaProgressivaProposta = valorFaixaProgressivaPropostaService.findByDestinoPropostaProdutoEspecifico(destinoProposta);
        for(ValorFaixaProgressivaProposta valor:valoresFaixaProgressivaProposta){
            for (Map<String,Object> map : markupParcelasProposta){
                Long idProdutoEspecifico = MapUtilsPlus.getLong(map, "ID_PRODUTO_ESPECIFICO");
                Long idRotaPreco = MapUtilsPlus.getLong(map, "ID_ROTA_PRECO");
                if (idProdutoEspecifico == null || idRotaPreco == null){
                    continue;
                }

                if (idProdutoEspecifico.equals(valor.getFaixaProgressivaProposta().getProdutoEspecifico().getIdProdutoEspecifico()) &&
                        idRotaPreco.equals(destinoProposta.getRotaPreco().getIdRotaPreco())){
                    TypedFlatMap markupMap = validateMarkup(valor.getVlFixo(), map,dsRotaCompleta); 
                    if (markupMap!=null){
                        markupsInvalidos.add(markupMap);
                    }
                }
            }
        }
    }
    
    private void validateMarkupParcelasFretePeso(DestinoProposta destinoProposta,
            List<Map<String, Object>> markupParcelasProposta, String dsRotaCompleta, List<TypedFlatMap> markupsInvalidos) {
        
        Collections.sort(markupParcelasProposta,new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                BigDecimal d1 = (BigDecimal)o1.get("VL_FAIXA_PROGRESSIVA");
                BigDecimal d2 = (BigDecimal)o2.get("VL_FAIXA_PROGRESSIVA");
                if (d1 != null && d2 != null){
                    return d1.compareTo(d2);
                }
                return 0;
            }

        });
        
        List<ValorFaixaProgressivaProposta> valoresFaixaProgressivaProposta = valorFaixaProgressivaPropostaService.findByDestinoPropostaFretePeso(destinoProposta);
        for(ValorFaixaProgressivaProposta valor:valoresFaixaProgressivaProposta){
            for (Map<String,Object> map : markupParcelasProposta){
                BigDecimal vlFaixaProgressiva = MapUtilsPlus.getBigDecimal(map, "VL_FAIXA_PROGRESSIVA");
                Long idRotaPreco = MapUtilsPlus.getLong(map, "ID_ROTA_PRECO");
                if (vlFaixaProgressiva == null || idRotaPreco == null){
                    continue;
                }

                int compare = vlFaixaProgressiva.compareTo(valor.getFaixaProgressivaProposta().getVlFaixa());
                
                if (compare >= 0 &&
                        idRotaPreco.equals(destinoProposta.getRotaPreco().getIdRotaPreco())){
                    markupsInvalidos.add(validateMarkup(valor.getVlFixo(), map,dsRotaCompleta));
                    break;
                }
            }
        }
    }

    private void validateMarkupParcela(String cdParcelaPreco, BigDecimal vlParcela,
            List<Map<String, Object>> markupParcelasProposta, String dsRota, List<TypedFlatMap> markupsInvalidos) {
        Map<String,Object> mapParcela = findParcelaMarkup(markupParcelasProposta, cdParcelaPreco, null);
        if (mapParcela != null){
             TypedFlatMap markupMap = validateMarkup(vlParcela, mapParcela, dsRota);
             if (markupMap!=null){
                 markupsInvalidos.add(markupMap);
             }
        }
    }

    private Map<String, Object> findParcelaMarkup(List<Map<String, Object>> markupParcelasProposta, String cdParcelaPreco, Long idRotaPreco){
        for (Map<String, Object> mapParcelas: markupParcelasProposta){
            Long idRotaPrecoMarkup = MapUtilsPlus.getLong(mapParcelas, "ID_ROTA_PRECO");
            if (!(idRotaPreco != null && idRotaPrecoMarkup != null && !idRotaPreco.equals(idRotaPrecoMarkup))){
                if (cdParcelaPreco.equals((String)mapParcelas.get("CD_PARCELA_PRECO"))){
                    return mapParcelas;
                }
            }
        }
        return null;
    }
	

	/**
	 * verifica a situação do workflow e atualiza os campos de Simulação 
	 * @param simulacao
	 */
	private void verificaSituacaoAprovacao(Simulacao simulacao) {
		if (simulacao.getTpSituacaoAprovacao() != null) {
			if (ConstantesVendas.TP_SITUACAO_APROVADO.equals(simulacao.getTpSituacaoAprovacao().getValue())
					|| ConstantesVendas.TP_SITUACAO_REPROVADO
							.equals(simulacao.getTpSituacaoAprovacao().getValue())
					|| ConstantesVendas.TP_SITUACAO_CANCELADO
							.equals(simulacao.getTpSituacaoAprovacao().getValue())) {
				simulacao = findById(simulacao.getIdSimulacao());
				limparCamposAprovacao(simulacao);
			} else if (ConstantesVendas.TP_SITUACAO_EM_APROVACAO.equals(simulacao.getTpSituacaoAprovacao()
					.getValue())) {
				simulacao = findById(simulacao.getIdSimulacao());
				Pendencia pendencia = simulacao.getPendenciaAprovacao();
				if (pendencia != null) {
					workflowPendenciaService.cancelPendencia(pendencia);
				}
				limparCamposAprovacao(simulacao);
			}
		}
	}

	/**  atualiza os campos relativos a aprovacao de pendencia na tabela simulacao
	 */
	private void limparCamposAprovacao(Simulacao simulacao) {
		simulacao.setPendenciaAprovacao(null);
		simulacao.setTpSituacaoAprovacao(null);
		simulacao.setUsuarioByIdUsuarioAprovou(null);
		simulacao.setDtAprovacao(null);
		store(simulacao);
	}
	
	/**
	 * atualiza os campos da simulação apos gerar a pendencia de cubagem
	 * @param simulacao
	 */
	private void atualizarCamposAprovacao(Long idSimulacao) {
		Simulacao simulacao = findById(idSimulacao);
		simulacao.setPendenciaAprovacao(null);
		simulacao.setTpSituacaoAprovacao(new DomainValue(ConstantesVendas.TP_SITUACAO_APROVADO));
		simulacao.setUsuarioByIdUsuarioAprovou(SessionUtils.getUsuarioLogado());
		simulacao.setDtAprovacao(JTDateTimeUtils.getDataAtual());
		store(simulacao);
		SimulacaoUtils.setSimulacaoInSession(simulacao);
	}
	
	/**
	 * Gera pendencia para prosposta do aéreo. 
	 * @param simulacao
	 */
	private void storePendenciaAprovacaoPropostaAereo(Long  idSimulacao, Short nrTipoEvento, String dsProcesso) {
		if(dsProcesso != null && dsProcesso.length() >= VALOR_MAXIMO_DS_PENDENCIA){
			dsProcesso = dsProcesso.substring(0, VALOR_MAXIMO_DS_PENDENCIA - 5);
			dsProcesso += "...";
		}
		
		Simulacao simulacao = findById(idSimulacao);
		
		
		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
		pendenciaHistoricoDTO.setIdFilial(simulacao.getFilial().getIdFilial());
		pendenciaHistoricoDTO.setNrTipoEvento(nrTipoEvento);
		pendenciaHistoricoDTO.setIdProcesso(simulacao.getIdSimulacao());
		pendenciaHistoricoDTO.setDsProcesso(dsProcesso);
		pendenciaHistoricoDTO.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.SIMULACAO);
		pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.PCAE);
		pendenciaHistoricoDTO.setDsObservacao(configuracoesFacade.getMensagem("naoSeAplica"));
		
		Pendencia pendenciaWorkFlow = workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
		
		simulacao.setPendenciaAprovacao(pendenciaWorkFlow);
		simulacao.setTpSituacaoAprovacao(pendenciaWorkFlow.getTpSituacaoPendencia());
		simulacao.setUsuarioByIdUsuarioAprovou(null);
		simulacao.setDtAprovacao(null);
		store(simulacao);

		SimulacaoUtils.setSimulacaoInSession(simulacao);
	}

	/**
	 * retorna true se fator cubagem é maior que o parametro fator cubagem padrao aereo  
	 * 
	 * @param nrFatorCubagem
	 * @return
	 */
	private boolean validateLimiteDescontoPropostaAereo(BigDecimal nrFatorCubagem) {
		if (nrFatorCubagem == null) {
			return false;
		}
		ParametroGeral pgFatorCubagemPadraoAereo = parametroGeralService.findByNomeParametro(ConstantesExpedicao.NM_PARAMETRO_FATOR_CUBAGEM_PADRAO_AEREO);
		BigDecimal fatorCubagemPadraoAereo = new BigDecimal(pgFatorCubagemPadraoAereo.getDsConteudo());
		return CompareUtils.lt(BigDecimalUtils.defaultBigDecimal(nrFatorCubagem), fatorCubagemPadraoAereo);
	}


	public void storeCancelaPendenciaProposta(Simulacao simulacao) {
		DomainValue tpSituacaoAprovacao = simulacao.getTpSituacaoAprovacao();
		if (tpSituacaoAprovacao != null && !("I".equals(tpSituacaoAprovacao.getValue()))) {
			Pendencia pendencia = simulacao.getPendenciaAprovacao();
			if(pendencia != null) {
				pendencia.setTpSituacaoPendencia(new DomainValue("E"));
				pendencia.getOcorrencia().setTpSituacaoOcorrencia(new DomainValue("P"));
				workflowPendenciaService.cancelPendencia(pendencia);
			}
			updateSimulacao(simulacao.getIdSimulacao(), false);
		}
	}

	/*
	 * METODOS PRIVADOS
	 */
	private boolean needAtualizacaoPendencia(Simulacao simulacao) {
		Simulacao s = (Simulacao) getSimulacaoDAO().getHibernateTemplate().load(Simulacao.class, simulacao.getIdSimulacao());

		String tpPeriodicidadeFaturamentoAnterior = null;
		if(s.getTpPeriodicidadeFaturamento() != null) {
			tpPeriodicidadeFaturamentoAnterior = s.getTpPeriodicidadeFaturamento().getValue();
		}
		String tpPeriodicidadeFaturamentoAtual = null;
		if(simulacao.getTpPeriodicidadeFaturamento() != null) {
			tpPeriodicidadeFaturamentoAtual = simulacao.getTpPeriodicidadeFaturamento().getValue();
		}
		Long idTabelaPrecoAnterior = null;
		if (s.getTabelaPreco() != null) {
			idTabelaPrecoAnterior = s.getTabelaPreco().getIdTabelaPreco();
		}
		Long idTabelaPrecoAtual = null;
		if (simulacao.getTabelaPreco() != null) {
			idTabelaPrecoAtual = simulacao.getTabelaPreco().getIdTabelaPreco();
		}
		
		boolean isEqual = new EqualsBuilder()
			.append(s.getClienteByIdCliente().getIdCliente(), simulacao.getClienteByIdCliente().getIdCliente())
			.append(idTabelaPrecoAnterior, idTabelaPrecoAtual)
			.append(s.getServico().getIdServico(), simulacao.getServico().getIdServico())
			.append(s.getNrDiasPrazoPagamento(), simulacao.getNrDiasPrazoPagamento())
			.append(s.getDtAprovacao(), simulacao.getDtAprovacao())
			.append(tpPeriodicidadeFaturamentoAnterior, tpPeriodicidadeFaturamentoAtual)
			.isEquals();
		getSimulacaoDAO().getAdsmHibernateTemplate().evict(s);
		return !isEqual;
	}

	private void validateOperation(Simulacao simulacao, String filialMessage, String efetivadaMessage) {
		if (!simulacao.getFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
			throw new BusinessException(filialMessage);
		}
		if (Boolean.TRUE.equals(simulacao.getBlEfetivada())) {
			throw new BusinessException(efetivadaMessage);
		}
	}

	private void storePendenciaAprovacaoProposta(Long idSimulacao, boolean blAprova) {
		Simulacao simulacao = findById(idSimulacao);
		storePendenciaAprovacaoProposta(simulacao, blAprova);
	}

	private void executeValidacaoProposta(Simulacao simulacao, boolean blAprova) {
		/** Valida DestinosProposta e ParametrosCliente */
		if (!validateLimiteDescontoDestinosProposta(simulacao.getIdSimulacao())
				|| !validateLimiteDescontoProposta(simulacao.getIdSimulacao())) {
			generatePendencia(simulacao.getIdSimulacao());
		} else {
			updateSimulacao(simulacao.getIdSimulacao(), blAprova);
		}
		
		if(ArrayUtils.contains(TIPOS_PROPOSTA_VALIDOS, simulacao.getTpGeracaoProposta().getValue())) {
			destinoPropostaService.generateParametrosProposta(simulacao.getIdSimulacao());
		}
	}

	/**
	 * Se a faixa de aprovação for maior que 1 e perfil for televendas mostra a 
	 * mensagem LMS-30058
	 * 
	 * Adiciona + 10 quando o perfil for televendas
	 * 
	 * @param tpModal
	 * @param nrFaixaAprovacao
	 * @return short
	 */
	private short findNrFaixaAprovacaoPerfil(String tpModal, short nrFaixaAprovacao){
		Boolean perfiTelevendas = executePerfilTelevendas();
		if (MODAL_RODOVIARIO.equals(tpModal) && nrFaixaAprovacao > 1 && perfiTelevendas) {
			throw new BusinessException("LMS-30058");
		}			
		if(perfiTelevendas){
			nrFaixaAprovacao += 10;
		}	
		if(nrFaixaAprovacao == 3 || nrFaixaAprovacao == 4){
			nrFaixaAprovacao -= 1;
		}	
		return nrFaixaAprovacao;
	}
	
	private void generatePendencia(Long idSimulacao) {
		Simulacao simulacao = findById(idSimulacao);

		short nrFaixaAprovacao = 0;
		int nrTipoEvento = 3020;
		if (MODAL_AEREO.equals(simulacao.getServico().getTpModal())) {
			nrFaixaAprovacao = findFaixaAprovacaoDescontoPropostaAereo(idSimulacao);
			nrTipoEvento += nrFaixaAprovacao;
		} else if("C".equals(simulacao.getTpGeracaoProposta().getValue())) {
			/** Gera DestinosProposta para TipoGeraçãoProposta == C (Capital/Interior) */
			nrFaixaAprovacao = findFaixaAprovacaoDescontoDestinosPropostaRodoviario(idSimulacao);
			nrTipoEvento += findNrFaixaAprovacaoPerfil(simulacao.getServico().getTpModal().getValue(), nrFaixaAprovacao);
		} else {
			nrFaixaAprovacao = findFaixaAprovacaoDescontoPropostaRodoviario(idSimulacao);									
			nrTipoEvento += findNrFaixaAprovacaoPerfil(simulacao.getServico().getTpModal().getValue(), nrFaixaAprovacao);
		}

		String nrProposta = SimulacaoUtils.formatNrSimulacao(simulacao.getFilial().getSgFilial(), ""+simulacao.getNrSimulacao());
		Pendencia pendencia = workflowPendenciaService.generatePendencia(
				SessionUtils.getFilialSessao().getIdFilial(), 
				(short) nrTipoEvento, 
				simulacao.getIdSimulacao(),
				configuracoesFacade.getMensagem("aprovacaoProposta", new Object[]{nrProposta}),
				JTDateTimeUtils.getDataHoraAtual());

		simulacao.setPendenciaAprovacao(pendencia);
		simulacao.setTpSituacaoAprovacao(pendencia.getTpSituacaoPendencia());
		simulacao.setUsuarioByIdUsuarioAprovou(null);
		simulacao.setDtAprovacao(null);
		store(simulacao);
	}

	private boolean executePerfilTelevendas(){		
		String perfilTelevendas  = (String)configuracoesFacade.getValorParametro("Perfil_Televendas");
		List<PerfilUsuario> list = perfilUsuarioService.findByIdUsuarioPerfilUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		for (PerfilUsuario perfilUsuario : list) {
			if(perfilTelevendas.equals(perfilUsuario.getPerfil().getDsPerfil())){
				return true;
			}
		}		
		return false;
	}

	private void updateSimulacao(Long idSimulacao, boolean blAprova) {
		Simulacao simulacao = findById(idSimulacao);
		simulacao.setPendenciaAprovacao(null);
		simulacao.setTpSituacaoAprovacao(new DomainValue("I"));
		simulacao.setUsuarioByIdUsuarioAprovou(null);
		simulacao.setDtAprovacao(null);
		if (blAprova) {
			simulacao.setTpSituacaoAprovacao(new DomainValue("A"));
			simulacao.setUsuarioByIdUsuarioAprovou(SessionUtils.getUsuarioLogado());
			simulacao.setDtAprovacao(JTDateTimeUtils.getDataAtual());
		}
		store(simulacao);
		SimulacaoUtils.setSimulacaoInSession(simulacao);
	}

	/**
	 * Método comum entre as rotinas que buscam limiteDesconto
	 * @param cdParcelaFrete
	 * @param idParcelaPreco
	 * @param tabelaPreco
	 * @return <b>limiteDesconto</b>
	 */
	private BigDecimal findLimiteDescontoParcela(String cdParcelaFrete, Long idParcelaPreco, TypedFlatMap tabelaPreco) {
		
		BigDecimal limiteDesconto = null;
		
		Long idSubtipoTabelaPreco = tabelaPreco.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco");
		
		String tpTipoTabelaPreco = tabelaPreco.getString("tipoTabelaPreco.tpTipoTabelaPreco.value");
		
		Filial filialUsuario = filialService.findFilialPadraoByUsuarioEmpresa(SessionUtils.getUsuarioLogado(), SessionUtils.getEmpresaSessao());
		
		if (!ConstantesExpedicao.CD_FRETE_PESO.equals(cdParcelaFrete) && !ConstantesExpedicao.CD_FRETE_VALOR.equals(cdParcelaFrete)) {
			
			limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
				cdParcelaFrete,
				idParcelaPreco,
				tabelaPreco.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco"),
				tabelaPreco.getString("tipoTabelaPreco.tpTipoTabelaPreco.value"),
				"P",
				"A",
				SessionUtils.getUsuarioLogado().getIdUsuario(),
				null,
				null);
		}
		
		if (limiteDesconto == null) {
			
			limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
				cdParcelaFrete,
				idParcelaPreco,
				idSubtipoTabelaPreco,
				tpTipoTabelaPreco,
				"P",
				"A",
				null,
				filialUsuario.getIdFilial(),
				null);
		}
		
		if (limiteDesconto == null) {
			
			Long idGrupoClassificacao = getGrupoClassificacaoParametroGeral();
			Long idDivisaoGrupoClassificacao = grupoClassificacaoFilialService.findIdDivisaoByFilialGrupoClassificacao(filialUsuario.getIdFilial(), idGrupoClassificacao);
			if (idDivisaoGrupoClassificacao != null) {
				limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
					cdParcelaFrete,
					idParcelaPreco,
					idSubtipoTabelaPreco,
					tpTipoTabelaPreco,
					"P",
					"A",
					null,
					null,
					idDivisaoGrupoClassificacao);
			}
		}
		
		if (limiteDesconto == null) {
			
			int index = 0; 
			
			List<PerfilUsuario> perfilUsuario = perfilUsuarioService
				.findByIdUsuarioPerfilUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
			
			Long[] idsPerfil = new Long[perfilUsuario.size()];
			for(PerfilUsuario perfil : perfilUsuario){
				idsPerfil[index] = perfil.getPerfil().getIdPerfil();
				index++;
			}/*for*/
			
			limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
				cdParcelaFrete,
				idParcelaPreco,
				idSubtipoTabelaPreco,
				tpTipoTabelaPreco,
				"P",
				"A",
				null,
				null,
					null,
					idsPerfil);				
			
		}
				
		if (limiteDesconto == null) {
			
			limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
				cdParcelaFrete,
				idParcelaPreco,
				idSubtipoTabelaPreco,
				tpTipoTabelaPreco,
				"P",
				"A",
				null,
				null,
				null, 
				new Long[]{});
			
		}
		
		return limiteDesconto;
	}

	private boolean validateGrupoFretePeso(TypedFlatMap tabelaPreco, ParametroCliente pc) {
		BigDecimal limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
			ConstantesExpedicao.CD_FRETE_PESO,
			null,
			tabelaPreco.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco"),
			tabelaPreco.getString("tipoTabelaPreco.tpTipoTabelaPreco.value"),
			"P",
			"A",
			SessionUtils.getUsuarioLogado().getIdUsuario(),
			null,
			null);
		if (limiteDesconto != null) {
			if (CompareUtils.ne(limiteDesconto, BigDecimalUtils.HUNDRED)) {
				if (!TABELA.equals(pc.getTpIndicadorMinFretePeso().getValue())
						|| CompareUtils.ne(pc.getVlMinFretePeso(), ZERO)
						|| CompareUtils.ne(pc.getVlMinimoFreteQuilo(), ZERO)
						|| CompareUtils.ne(pc.getVlFreteVolume(), ZERO)
						|| !TABELA.equals(pc.getTpTarifaMinima().getValue())
						|| CompareUtils.ne(pc.getVlTarifaMinima(), ZERO)
						|| Boolean.TRUE.equals(pc.getBlPagaPesoExcedente())
						|| Boolean.FALSE.equals(pc.getBlPagaCubagem())
						|| CompareUtils.ne(pc.getPcPagaCubagem(), BigDecimalUtils.HUNDRED)
						|| !TABELA.equals(pc.getTpIndicVlrTblEspecifica().getValue())
						|| CompareUtils.ne(pc.getVlTblEspecifica(), ZERO)) {
					return false;
				}
				if (DESCONTO.equals(pc.getTpIndicadorPercMinimoProgr().getValue())) {
					if (CompareUtils.gt(pc.getVlPercMinimoProgr(), limiteDesconto)) {
						return false;
					}
				}
				if (!TABELA.equals(pc.getTpIndicadorFretePeso().getValue()) &&
					!DESCONTO.equals(pc.getTpIndicadorFretePeso().getValue()) &&
					!ACRESCIMO.equals(pc.getTpIndicadorFretePeso().getValue())) {
					return false;
				}
				if (DESCONTO.equals(pc.getTpIndicadorFretePeso().getValue())) {
					if (CompareUtils.gt(pc.getVlFretePeso(), limiteDesconto)) {
						return false;
					}
				}
			}
		} else {
			limiteDesconto = findLimiteDescontoParcela(ConstantesExpedicao.CD_FRETE_PESO, null, tabelaPreco);
			if (limiteDesconto != null) {
				if (!TABELA.equals(pc.getTpIndicadorMinFretePeso().getValue())
						|| CompareUtils.ne(pc.getVlMinFretePeso(), ZERO)
						|| CompareUtils.ne(pc.getVlMinimoFreteQuilo(), ZERO)
						|| CompareUtils.ne(pc.getVlFreteVolume(), ZERO)
						|| !TABELA.equals(pc.getTpTarifaMinima().getValue())
						|| CompareUtils.ne(pc.getVlTarifaMinima(), ZERO)
						|| Boolean.TRUE.equals(pc.getBlPagaPesoExcedente())
						|| Boolean.FALSE.equals(pc.getBlPagaCubagem())
						|| CompareUtils.ne(pc.getPcPagaCubagem(), BigDecimalUtils.HUNDRED)
						|| !TABELA.equals(pc.getTpIndicVlrTblEspecifica().getValue())
						|| CompareUtils.ne(pc.getVlTblEspecifica(), ZERO)) {
					return false;
				}
				if (DESCONTO.equals(pc.getTpIndicadorPercMinimoProgr().getValue())
						&& CompareUtils.gt(pc.getVlPercMinimoProgr(), limiteDesconto)) {
					return false;
				}
				if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
					if (!TABELA.equals(pc.getTpIndicadorFretePeso().getValue()) &&
						!DESCONTO.equals(pc.getTpIndicadorFretePeso().getValue()) &&
						!ACRESCIMO.equals(pc.getTpIndicadorFretePeso().getValue())) {
						return false;
					}
				}
				if (DESCONTO.equals(pc.getTpIndicadorFretePeso().getValue()) 
						&& CompareUtils.gt(pc.getVlFretePeso(), limiteDesconto)) {
					return false;
				}
			}
		}
		return true;
	}
	private boolean validateGrupoFretePeso(TypedFlatMap tabelaPreco, DestinoProposta dp) {
		BigDecimal limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
			ConstantesExpedicao.CD_FRETE_PESO,
			null,
			tabelaPreco.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco"),
			tabelaPreco.getString("tipoTabelaPreco.tpTipoTabelaPreco.value"),
			"P",
			"A",
			SessionUtils.getUsuarioLogado().getIdUsuario(),
			null,
			null);

		if (limiteDesconto != null) {
			if (CompareUtils.ne(limiteDesconto, BigDecimalUtils.HUNDRED)) {
				if(validateIndicadorDesconto(dp.getTpIndicadorFreteMinimo().getValue(), dp.getVlFreteMinimo(), limiteDesconto)
					|| validateIndicadorDesconto(dp.getTpIndicadorFretePeso().getValue(), dp.getVlFretePeso(), limiteDesconto)) {
					return false;
				}
				
				return validadeDiferencaFretePeso(dp);				
			}
		} else {
			limiteDesconto = findLimiteDescontoParcela(ConstantesExpedicao.CD_FRETE_PESO, null, tabelaPreco);
			if (limiteDesconto != null && CompareUtils.ne(limiteDesconto, BigDecimalUtils.HUNDRED)) {
				if(validateIndicadorDesconto(dp.getTpIndicadorFreteMinimo().getValue(), dp.getVlFreteMinimo(), limiteDesconto)
					|| validateIndicadorDesconto(dp.getTpIndicadorFretePeso().getValue(), dp.getVlFretePeso(), limiteDesconto)) {
					return false;
				}
				return validadeDiferencaFretePeso(dp);
			}
		}
		return true;
	}

	/**
	 * Obtem a DiferencaCapitalInterior 
	 * 
	 * Verifica se pc DestinoProposta.PcDiferencaFretePeso é maior ou igual ao
	 * DiferencaCapitalInterior.PcDiferencaMinima 
	 * 
	 * @param dp
	 * @return boolean
	 */
	private boolean validadeDiferencaFretePeso(DestinoProposta dp){		
		DiferencaCapitalInterior diferencaCapitalInterior = diferencaCapitalInteriorService.findByUF(null, dp.getUnidadeFederativa().getIdUnidadeFederativa());
		return diferencaCapitalInterior != null && CompareUtils.ge(dp.getPcDiferencaFretePeso(), diferencaCapitalInterior.getPcDiferencaMinima());
	}
	
	private boolean validateGrupoFreteValor(TypedFlatMap tabelaPreco, ParametroCliente pc) {
		BigDecimal limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
			ConstantesExpedicao.CD_FRETE_VALOR,
			null,
			tabelaPreco.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco"),
			tabelaPreco.getString("tipoTabelaPreco.tpTipoTabelaPreco.value"),
			"P",
			"A",
			SessionUtils.getUsuarioLogado().getIdUsuario(),
			null,
			null);
		if (limiteDesconto != null) {
			if (CompareUtils.ne(limiteDesconto, BigDecimalUtils.HUNDRED)) {
				if (!TABELA.equals(pc.getTpIndicadorValorReferencia().getValue()) ||
					CompareUtils.ne(pc.getVlValorReferencia(), ZERO) ||
					CompareUtils.ne(pc.getPcFretePercentual(), ZERO) ||
					CompareUtils.ne(pc.getVlMinimoFretePercentual(), ZERO) ||
					CompareUtils.ne(pc.getVlToneladaFretePercentual(), ZERO) ||
					CompareUtils.ne(pc.getPsFretePercentual(), ZERO)) {
					return false;
				}
				if (!TABELA.equals(pc.getTpIndicadorAdvalorem().getValue()) &&
					!DESCONTO.equals(pc.getTpIndicadorAdvalorem().getValue()) &&
					!ACRESCIMO.equals(pc.getTpIndicadorAdvalorem().getValue())) {
					return false;
				}
				if (!TABELA.equals(pc.getTpIndicadorAdvalorem2().getValue()) &&
					!DESCONTO.equals(pc.getTpIndicadorAdvalorem2().getValue()) &&
					!ACRESCIMO.equals(pc.getTpIndicadorAdvalorem2().getValue())) {
					return false;
				}
				if (DESCONTO.equals(pc.getTpIndicadorAdvalorem().getValue())) {
					if (CompareUtils.gt(pc.getVlAdvalorem(), limiteDesconto)) {
						return false;
					}
				}
				if (DESCONTO.equals(pc.getTpIndicadorAdvalorem2().getValue())) {
					if (CompareUtils.gt(pc.getVlAdvalorem2(), limiteDesconto)) {
						return false;
					}
				}
			}
		} else {
			limiteDesconto = findLimiteDescontoParcela(ConstantesExpedicao.CD_FRETE_VALOR, null, tabelaPreco);
			if (limiteDesconto != null) {
				if (!TABELA.equals(pc.getTpIndicadorValorReferencia().getValue()) ||
					CompareUtils.ne(pc.getVlValorReferencia(), ZERO) ||
					CompareUtils.ne(pc.getPcFretePercentual(), ZERO) ||
					CompareUtils.ne(pc.getVlMinimoFretePercentual(), ZERO) ||
					CompareUtils.ne(pc.getVlToneladaFretePercentual(), ZERO) ||
					CompareUtils.ne(pc.getPsFretePercentual(), ZERO)) {
					return false;
				}
				if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
					if (!TABELA.equals(pc.getTpIndicadorAdvalorem().getValue()) &&
						!DESCONTO.equals(pc.getTpIndicadorAdvalorem().getValue()) &&
						!ACRESCIMO.equals(pc.getTpIndicadorAdvalorem().getValue())) {
						return false;
					}
					if (!TABELA.equals(pc.getTpIndicadorAdvalorem2().getValue()) &&
						!DESCONTO.equals(pc.getTpIndicadorAdvalorem2().getValue()) &&
						!ACRESCIMO.equals(pc.getTpIndicadorAdvalorem2().getValue())) {
						return false;
					}
				}
				if (DESCONTO.equals(pc.getTpIndicadorAdvalorem().getValue())) {
					if (CompareUtils.gt(pc.getVlAdvalorem(), limiteDesconto)) {
						return false;
					}
				}
				if (DESCONTO.equals(pc.getTpIndicadorAdvalorem2().getValue())) {
					if (CompareUtils.gt(pc.getVlAdvalorem2(), limiteDesconto)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	private boolean validateGrupoFreteValor(TypedFlatMap tabelaPreco, DestinoProposta dp) {
		BigDecimal limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
			ConstantesExpedicao.CD_FRETE_VALOR,
			null,
			tabelaPreco.getLong("subtipoTabelaPreco.idSubtipoTabelaPreco"),
			tabelaPreco.getString("tipoTabelaPreco.tpTipoTabelaPreco.value"),
			"P",
			"A",
			SessionUtils.getUsuarioLogado().getIdUsuario(),
			null,
			null);
		if (limiteDesconto != null) {
			if (CompareUtils.ne(limiteDesconto, BigDecimalUtils.HUNDRED)) {
				if (!TABELA.equals(dp.getTpIndicadorAdvalorem().getValue()) &&
					!DESCONTO.equals(dp.getTpIndicadorAdvalorem().getValue()) &&
					!ACRESCIMO.equals(dp.getTpIndicadorAdvalorem().getValue())) {
					return false;
				}
			}
		} else {
			limiteDesconto = findLimiteDescontoParcela(ConstantesExpedicao.CD_FRETE_VALOR, null, tabelaPreco);
			if (limiteDesconto != null) {
				if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
					if (!TABELA.equals(dp.getTpIndicadorAdvalorem().getValue()) &&
						!DESCONTO.equals(dp.getTpIndicadorAdvalorem().getValue()) &&
						!ACRESCIMO.equals(dp.getTpIndicadorAdvalorem().getValue())) {
						return false;
					}
				}
			}
		}
		if (limiteDesconto != null) {
			if (DESCONTO.equals(dp.getTpIndicadorAdvalorem().getValue())) {
				if (CompareUtils.gt(dp.getVlAdvalorem(), limiteDesconto)) {
					return false;
				}
			}
		}
		
		DiferencaCapitalInterior dci = diferencaCapitalInteriorService.findPercCapitalInterior(dp.getUnidadeFederativa().getIdUnidadeFederativa());
		if(CompareUtils.gt(dci.getPcDiferencaMinimaAdvalorem(), dp.getVlAdvalorem())){
			return false;
		}
		
		return true;
	}
	
	private boolean validateGrupoGris(TypedFlatMap tabelaPreco, ParametroCliente pc, Simulacao simulacao) {
		BigDecimal vlDesconto = ZERO;
		BigDecimal limiteDesconto = findLimiteDescontoParcela(ConstantesExpedicao.CD_GRIS, null, tabelaPreco);

		if (limiteDesconto == null) {
			limiteDesconto = BigDecimalUtils.HUNDRED;
		}
		if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
			if (!TABELA.equals(pc.getTpIndicadorPercentualGris().getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorPercentualGris().getValue())
					&& !VALOR.equals(pc.getTpIndicadorPercentualGris().getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorPercentualGris().getValue())) {
				return false;
			}
			if (!TABELA.equals(pc.getTpIndicadorMinimoGris().getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorMinimoGris().getValue())
					&& !VALOR.equals(pc.getTpIndicadorMinimoGris().getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorMinimoGris().getValue())) {
				return false;
			}
		}
		if (DESCONTO.equals(pc.getTpIndicadorPercentualGris().getValue())) {
			if (CompareUtils.gt(pc.getVlPercentualGris(), limiteDesconto)) {
				return false;
			}
		}
		if (VALOR.equals(pc.getTpIndicadorPercentualGris().getValue())) {
			ParcelaPreco parcelaPreco = parcelaPrecoService.findByCdParcelaPreco(ConstantesExpedicao.CD_GRIS);
			Generalidade generalidade = generalidadeService.findGeneralidade(simulacao.getTabelaPreco().getIdTabelaPreco(),parcelaPreco.getIdParcelaPreco());

			vlDesconto = BigDecimalUtils.desconto(generalidade.getVlGeneralidade(), limiteDesconto);
			if (CompareUtils.lt(pc.getVlPercentualGris(),vlDesconto)) {
				return false;
			}
		}

		if (DESCONTO.equals(pc.getTpIndicadorMinimoGris().getValue())) {
			if (CompareUtils.gt(pc.getVlMinimoGris(), limiteDesconto)) {
				return false;
			}
		}
		if (VALOR.equals(pc.getTpIndicadorMinimoGris().getValue())) {
			ParcelaPreco parcelaPreco = parcelaPrecoService.findByCdParcelaPreco(ConstantesExpedicao.CD_GRIS);
			Generalidade generalidade = generalidadeService.findGeneralidade(simulacao.getTabelaPreco().getIdTabelaPreco(),parcelaPreco.getIdParcelaPreco());

			vlDesconto = BigDecimalUtils.desconto(generalidade.getVlMinimo(), limiteDesconto);				
			if (CompareUtils.lt(pc.getVlMinimoGris(), vlDesconto)) {
				return false;
			}
		}
		return true;
	}

	private boolean validateGrupoPedagio(TypedFlatMap tabelaPreco, ParametroCliente pc, Simulacao simulacao) {
		BigDecimal vlDesconto = ZERO;
		BigDecimal limiteDesconto = findLimiteDescontoParcela(ConstantesExpedicao.CD_PEDAGIO, null, tabelaPreco);
		
		if (limiteDesconto == null) {
			limiteDesconto = BigDecimalUtils.HUNDRED;
		}
		if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
			if (!TABELA.equals(pc.getTpIndicadorPedagio().getValue()) &&
				!DESCONTO.equals(pc.getTpIndicadorPedagio().getValue()) &&
				!VALOR.equals(pc.getTpIndicadorPedagio().getValue()) &&
				!FIXO.equals(pc.getTpIndicadorPedagio().getValue()) &&
				!FAIXAPESO.equals(pc.getTpIndicadorPedagio().getValue()) &&
				!FRACAOQUILO.equals(pc.getTpIndicadorPedagio().getValue()) &&
				!ACRESCIMO.equals(pc.getTpIndicadorPedagio().getValue())) {
				return false;
			}
		}
		if (DESCONTO.equals(pc.getTpIndicadorPedagio().getValue())) {
			if (CompareUtils.gt(pc.getVlPedagio(), limiteDesconto)) {
				return false;
			}
		}
		if (VALOR.equals(pc.getTpIndicadorPedagio().getValue())) {			
			BigDecimal vlGeneralidade = this.findValorParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);

			vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, limiteDesconto);		
			if (CompareUtils.lt(pc.getVlPedagio(), vlDesconto)) {
				return false;
			}
		}
		if (FIXO.equals(pc.getTpIndicadorPedagio().getValue())) {						
			BigDecimal vlGeneralidade = this.findValorParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);

			vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, limiteDesconto);
			if (CompareUtils.ne(vlGeneralidade, ZERO) && CompareUtils.lt(pc.getVlPedagio(), vlDesconto)) {
				return false;
			}						
		}
		if (FRACAOQUILO.equals(pc.getTpIndicadorPedagio().getValue())) {
			BigDecimal vlGeneralidade = this.findValorParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_FRACAO);

			vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, limiteDesconto);
			if (CompareUtils.ne(vlGeneralidade, ZERO) && CompareUtils.lt(pc.getVlPedagio(), vlDesconto)) {
				return false;
			}				
		}
		if (FAIXAPESO.equals(pc.getTpIndicadorPedagio().getValue())) {
			BigDecimal vlGeneralidade = this.findValorParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);

			vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, limiteDesconto);
			if (CompareUtils.ne(vlGeneralidade, ZERO) && CompareUtils.lt(pc.getVlPedagio(), vlDesconto)) {
				return false;
			}				
		}

		return true;
	}

	/**
	 * Obtem o valor da parcela  
	 */
	private BigDecimal findValorParcela(Long idTabelaPreco, String cdParcelaPreco){
		ParcelaPreco parcelaPreco = parcelaPrecoService.findByCdParcelaPreco(cdParcelaPreco);
		Generalidade generalidade = generalidadeService.findGeneralidade(idTabelaPreco,parcelaPreco.getIdParcelaPreco());
		return generalidade.getVlGeneralidade();
	}

	/**
	 * Obtem o valor MINIMO da parcela  
	 */
	private BigDecimal findValorMinimoParcela(Long idTabelaPreco, String cdParcelaPreco){
		ParcelaPreco parcelaPreco = parcelaPrecoService.findByCdParcelaPreco(cdParcelaPreco);
		Generalidade generalidade = generalidadeService.findGeneralidade(idTabelaPreco,parcelaPreco.getIdParcelaPreco());
		return generalidade.getVlMinimo();
	}

	private boolean validateTotalFrete(TypedFlatMap tabelaPreco, ParametroCliente pc) {
		BigDecimal limiteDesconto = findLimiteDescontoParcela(ConstantesExpedicao.CD_TOTAL_FRETE, null, tabelaPreco);
		if (limiteDesconto == null) {
			limiteDesconto = BigDecimalUtils.HUNDRED;
		}
		if (CompareUtils.gt(pc.getPcDescontoFreteTotal(), limiteDesconto)) {
			return false;
		}

		BigDecimal pcCobrancaReentrega = (BigDecimal)configuracoesFacade.getValorParametro("PercentualCobrancaReentrega");
		if (CompareUtils.lt(pc.getPcCobrancaReentrega(), pcCobrancaReentrega)) {
			return false;
		}
		BigDecimal pcCobrancaDevolucoes = (BigDecimal)configuracoesFacade.getValorParametro("PercentualCobrancaDevolucao");
		if (CompareUtils.lt(pc.getPcCobrancaDevolucoes(), pcCobrancaDevolucoes)) {
			return false;
		}
		return true;
	}

	private boolean validateGeneralidades(TypedFlatMap tabelaPreco, ParametroCliente pc, Simulacao simulacao) {
		BigDecimal vlDesconto = ZERO;
		List<GeneralidadeCliente> generalidades = generalidadeClienteService.findByIdParametroClienteProposta(pc.getIdParametroCliente());
		for(GeneralidadeCliente generalidadeCliente : generalidades) {
			BigDecimal limiteDesconto = findLimiteDescontoParcela(null, generalidadeCliente.getParcelaPreco().getIdParcelaPreco(), tabelaPreco);
			if (limiteDesconto == null) {
				limiteDesconto = BigDecimalUtils.HUNDRED;
			}
			if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
				if (!TABELA.equals(generalidadeCliente.getTpIndicador().getValue())
						&& !DESCONTO.equals(generalidadeCliente.getTpIndicador().getValue())
						&& !ACRESCIMO.equals(generalidadeCliente.getTpIndicador().getValue())
						&& ((!VALOR.equals(generalidadeCliente.getTpIndicador().getValue())
								&& ConstantesExpedicao.CD_DESPACHO.equals(generalidadeCliente.getParcelaPreco().getCdParcelaPreco()))
							|| (!FRACAOQUILO.equals(generalidadeCliente.getTpIndicador().getValue())
								&& ConstantesExpedicao.CD_TAD.equals(generalidadeCliente.getParcelaPreco().getCdParcelaPreco())))) {
					return false;
				}
			}
			if (DESCONTO.equals(generalidadeCliente.getTpIndicador().getValue())) {
				if (CompareUtils.gt(generalidadeCliente.getVlGeneralidade(), limiteDesconto)) {
					return false;
				}
			}
			if (VALOR.equals(generalidadeCliente.getTpIndicador().getValue())) {
				BigDecimal vlGeneralidade = this.findValorParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_DESPACHO);
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, limiteDesconto);
				if (CompareUtils.lt(generalidadeCliente.getVlGeneralidade(), vlDesconto)) {
					return false;
		}
			}
			if (FRACAOQUILO.equals(generalidadeCliente.getTpIndicador().getValue())) {
				BigDecimal vlGeneralidade = this.findValorParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_TAD);
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, limiteDesconto);
				if (CompareUtils.lt(generalidadeCliente.getVlGeneralidade(), vlDesconto)) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean validateGrupoTrt(TypedFlatMap tabelaPreco, ParametroCliente pc, Simulacao simulacao) {
		BigDecimal limiteDesconto = findLimiteDescontoParcela(ConstantesExpedicao.CD_TRT, null, tabelaPreco);
		if (limiteDesconto == null) {
			limiteDesconto = BigDecimalUtils.HUNDRED;
		}
		if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
			if (!TABELA.equals(pc.getTpIndicadorPercentualTrt().getValue())
					&& !VALOR.equals(pc.getTpIndicadorPercentualTrt().getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorPercentualTrt().getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorPercentualTrt().getValue())
					&& !TABELA.equals(pc.getTpIndicadorMinimoTrt().getValue())
					&& !VALOR.equals(pc.getTpIndicadorMinimoTrt().getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorMinimoTrt().getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorMinimoTrt().getValue())) {
				return false;
			}
		}
		/** Percentual TRT */
		if (DESCONTO.equals(pc.getTpIndicadorPercentualTrt().getValue())) {
			if (CompareUtils.gt(pc.getVlPercentualTrt(), limiteDesconto)) {
				return false;
			}
		}
		if (VALOR.equals(pc.getTpIndicadorPercentualTrt().getValue())) {
			BigDecimal vlDesconto = this.findValorParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_TRT);
			vlDesconto = BigDecimalUtils.desconto(vlDesconto, limiteDesconto);
			if (CompareUtils.lt(pc.getVlPercentualTrt(), vlDesconto)) {
				return false;
			}
		}

		/** Minimo TRT */
		if (DESCONTO.equals(pc.getTpIndicadorMinimoTrt().getValue())) {
			if (CompareUtils.gt(pc.getVlMinimoTrt(), limiteDesconto)) {
				return false;
			}
		}
		if (VALOR.equals(pc.getTpIndicadorMinimoTrt().getValue())) {
			BigDecimal vlDesconto = this.findValorMinimoParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_TRT);
			vlDesconto = BigDecimalUtils.desconto(vlDesconto, limiteDesconto);
			if (CompareUtils.lt(pc.getVlMinimoTrt(), vlDesconto)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean validateGrupoTde(TypedFlatMap tabelaPreco, ParametroCliente pc, Simulacao simulacao) {
		BigDecimal limiteDesconto = findLimiteDescontoParcela(ConstantesExpedicao.CD_TDE, null, tabelaPreco);
		if (limiteDesconto == null) {
			limiteDesconto = BigDecimalUtils.HUNDRED;
		}
		if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
			if (!TABELA.equals(pc.getTpIndicadorPercentualTde().getValue())
					&& !VALOR.equals(pc.getTpIndicadorPercentualTde().getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorPercentualTde().getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorPercentualTde().getValue())
					&& !TABELA.equals(pc.getTpIndicadorMinimoTde().getValue())
					&& !VALOR.equals(pc.getTpIndicadorMinimoTde().getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorMinimoTde().getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorMinimoTde().getValue())) {
				return false;
			}
		}
		/** Percentual TDE */
		if (DESCONTO.equals(pc.getTpIndicadorPercentualTde().getValue())) {
			if (CompareUtils.gt(pc.getVlPercentualTde(), limiteDesconto)) {
				return false;
			}
		}
		if (VALOR.equals(pc.getTpIndicadorPercentualTde().getValue())) {
			BigDecimal vlDesconto = this.findValorParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_TDE);
			vlDesconto = BigDecimalUtils.desconto(vlDesconto, limiteDesconto);
			if (CompareUtils.lt(pc.getVlPercentualTde(), vlDesconto)) {
				return false;
			}
		}

		/** Minimo TDE */
		if (DESCONTO.equals(pc.getTpIndicadorMinimoTde().getValue())) {
			if (CompareUtils.gt(pc.getVlMinimoTde(), limiteDesconto)) {
				return false;
			}
		}
		if (VALOR.equals(pc.getTpIndicadorMinimoTde().getValue())) {
			BigDecimal vlDesconto = this.findValorMinimoParcela(simulacao.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_TDE);
			vlDesconto = BigDecimalUtils.desconto(vlDesconto, limiteDesconto);
			if (CompareUtils.lt(pc.getVlMinimoTde(), vlDesconto)) {
				return false;
			}
		}
		return true;
	}

	private Long getGrupoClassificacaoParametroGeral() {
		return Long.valueOf(((BigDecimal)configuracoesFacade.getValorParametro(GrupoClassificacao.ID_GRUPO_CLASSIFICACAO_DESCONTOS)).longValue());
	}
	
	private Short findFaixaAprovacaoDescontoPropostaRodoviario(Long idSimulacao) {
		Simulacao simulacao = findById(idSimulacao);
		if (simulacao != null){
			if (simulacao.getTpGeracaoProposta() != null && "P".equals(simulacao.getTpGeracaoProposta().getValue()))
			return SHORT_FOUR;//Se o tipo de geração da proposta for “Capital / Interior - Percentual” retornar o valor 4.
			else if (simulacao.getNrFatorDensidade() != null){
				return SHORT_FOUR;//Se o campo “Fator densidade” estiver preenchido(informado), então retornar o valor 4.
			}		
		}		
		
		Short faixa = SHORT_ONE;//23

		Short faixaLimitadores = findFaixaLimitadores(simulacao);
		if (faixaLimitadores.compareTo(faixa) > 0) {
			faixa = faixaLimitadores;
		}
		
		Short faixaServico = findFaixaServicos(simulacao);
		if (faixaServico.compareTo(faixa) > 0) {
			faixa = faixaServico;
		}
		
		Short faixaParamentro  = findFaixaParametros(idSimulacao);
		if (faixaParamentro.compareTo(faixa) > 0) {
			faixa = faixaParamentro;
		}

		/** NIVEL 1 **/
		return faixa; //23
	}

	private Short findFaixaLimitadores(Simulacao simulacao){
		Short faixaLimitadores = SHORT_ONE;
		if (simulacao != null){
			if(isLimiteMetragemCubicaPadrao(simulacao) || isLimiteQuantVolumePadrao(simulacao)){
				faixaLimitadores = SHORT_FOUR;
			}else{
				faixaLimitadores = SHORT_THREE;
			}
		}
		return faixaLimitadores;
	}
	
	private boolean isLimiteMetragemCubicaPadrao(Simulacao simulacao){
		BigDecimal parametroLimiteMetragem = (BigDecimal) parametroGeralService.
				findConteudoByNomeParametro("LIMITE_METRAGEM_CUBICA_PADRAO",false);
		
		if(simulacao.getNrLimiteMetragemCubica() != null && simulacao.getNrLimiteMetragemCubica().compareTo(parametroLimiteMetragem) > 0){
			return true;
		}
		return false;
	}
	
	private boolean isLimiteQuantVolumePadrao(Simulacao simulacao){
		BigDecimal parametroLimiteQuantVolume = (BigDecimal) parametroGeralService.
				findConteudoByNomeParametro("LIMITE_QUANT_VOLUME_PADRAO",false);
		
		if(simulacao.getNrLimiteQuantVolume() != null && simulacao.getNrLimiteQuantVolume().compareTo(parametroLimiteQuantVolume) > 0){
			return true;
		}
		return false;
	}
	
	private Short findFaixaServicos(Simulacao simulacao) {
		TabelaPreco tabelaPreco =  simulacao.getTabelaPreco();
		List<ServicoAdicionalCliente> servicos = simulacao.getServicoAdicionalClientes();
		
		Short faixaServico = SHORT_ONE;
		for (ServicoAdicionalCliente servico : servicos) {
			List<TabelaPrecoParcela> listaTabelaPrecoParcela = tabelaPrecoParcelaService.findByIdTabelaPrecoCdParcelaPreco(tabelaPreco.getIdTabelaPreco(), servico.getParcelaPreco().getCdParcelaPreco());
			if (listaTabelaPrecoParcela != null && !listaTabelaPrecoParcela.isEmpty()) {
				TabelaPrecoParcela tabelaPrecoParcela = listaTabelaPrecoParcela.get(0);
				BigDecimal vlServico = tabelaPrecoParcela.getValorServicoAdicional().getVlServico();
				
				//faixa - 4
				// 8.5 Serviço adicional
				if (ConstantesVendas.TP_INDICADOR_DESCONTO.equals(servico.getTpIndicador().getValue()) 
						&& servico.getVlValor().compareTo(FIFTY) > 0) {
					if (SHORT_FOUR.compareTo(faixaServico) > 0) {
						faixaServico = SHORT_FOUR;
						break;
					}
				}
	
				BigDecimal vlServicoPercent5 = vlServico.subtract(BigDecimalUtils.percentValue(vlServico, FIFTY, 2));
				if (ConstantesVendas.TP_INDICADOR_VALOR.equals(servico.getTpIndicador().getValue()) 
						&& servico.getVlValor().compareTo(vlServicoPercent5) < 0) {
					if (SHORT_FOUR.compareTo(faixaServico) > 0) {
						faixaServico = SHORT_FOUR;
						break;
					}
				}
				
	
				//faixa - 3
				// 14.5 Serviço adicional
				if (ConstantesVendas.TP_INDICADOR_DESCONTO.equals(servico.getTpIndicador().getValue()) 
						&& servico.getVlValor().compareTo(FOURTY) > 0) {
					if (SHORT_THREE.compareTo(faixaServico) > 0) {
						faixaServico = SHORT_THREE;
						continue;
					}
				}
				
				BigDecimal vlServicoPercent4 = vlServico.subtract(BigDecimalUtils.percentValue(vlServico, FOURTY, 2));
				if (ConstantesVendas.TP_INDICADOR_VALOR.equals(servico.getTpIndicador().getValue()) 
						&& servico.getVlValor().compareTo(vlServicoPercent4) < 0) {
					if (SHORT_THREE.compareTo(faixaServico) > 0) {
						faixaServico = SHORT_THREE;
						continue;
					}
				}
				
				
				//faixa - 2
				// 22. servicos adicionais
				if (ConstantesVendas.TP_INDICADOR_DESCONTO.equals(servico.getTpIndicador().getValue()) 
						&& servico.getVlValor().compareTo(THIRTY) > 0) {
					if (SHORT_TWO.compareTo(faixaServico) > 0) {
						faixaServico = SHORT_TWO;
						continue;
					}
				}
				
				BigDecimal vlServicoPercent3 = vlServico.subtract(BigDecimalUtils.percentValue(vlServico, THIRTY, 2));
				if (ConstantesVendas.TP_INDICADOR_VALOR.equals(servico.getTpIndicador().getValue()) 
						&& servico.getVlValor().compareTo(vlServicoPercent3) < 0) {
					if (SHORT_TWO.compareTo(faixaServico) > 0) {
						faixaServico = SHORT_TWO;
					}
				}
			}
		}
		return faixaServico;
	}

	private Short findFaixaParametros(Long idSimulacao) {
		List<ParametroCliente> parametros = parametroClienteService.findParametroByIdSimulacao(idSimulacao);
		if (parametros != null) {
			for (ParametroCliente parametro : parametros) {
				/** NIVEL 4 **/
				// 2 grupo frete peso DIFERENCIADO
				if (!validateGrupoFretePesoRodoviario(parametro)
						// 3. grupo frete valor e frete percentual DIFERENCIADO
						|| !validateGrupoFreteValorFretePercentualRodoviario(parametro)
						// 4. grupo gris DIFERENCIADO
						|| !validateGrupoGrisRodoviario(parametro, FIFTY)
						// 5. grupo pedagio DIFERENCIADO
						|| !validateGrupoPedagio(parametro)
						// 7. grupo total do frete DIFERENCIADO
						|| !validateGrupoTotalFreteDiferenciado(parametro) ) {
					return SHORT_FOUR;
				}
				// 8. generalidades DIFERENCIADO
				List<GeneralidadeCliente> generalidades = parametro.getGeneralidadeClientes();
				for (GeneralidadeCliente generalidade : generalidades) {
					if(!validateGeneralidadeInformada(generalidade)){
						return SHORT_FOUR;
						}
					}
				// 9. taxas
				List<TaxaCliente> taxas = parametro.getTaxaClientes();
				for (TaxaCliente taxa : taxas) {
					if (!TABELA.equals(taxa.getTpTaxaIndicador().getValue())) {
						return SHORT_FOUR;
					}
				}
				
				
				/** NIVEL 3 **/
				// 11. grupo frete peso
				if (!validateGrupoFretePeso(parametro, MODAL_RODOVIARIO, FOURTY)
						// 12. grupo frete valor e frete percentual
						|| !validateGrupoFreteValorFretePercentual(parametro, MODAL_RODOVIARIO, FOURTY)
						|| !validateTDEeTRT(parametro)){
					return SHORT_THREE;
				}
				// 15. generalidades
				String cdParcelaPreco = null;
				for (GeneralidadeCliente generalidade : generalidades) {
					cdParcelaPreco = generalidade.getParcelaPreco().getCdParcelaPreco();
					if(CD_TAD.equals(cdParcelaPreco) && !validateGeneralidades(generalidade, FOURTY)
							|| CD_TAS.equals(cdParcelaPreco) && !validateGeneralidades(generalidade, BigDecimal.TEN) 
							|| CD_TAXA_SUFRAMA.equals(cdParcelaPreco) && !validateGeneralidades(generalidade, TWENTY)
							|| CD_SEGURO_FLUVIAL.equals(cdParcelaPreco) && !validateGeneralidades(generalidade, FOURTY)
							|| CD_TAXA_FLUVIAL_BALSA.equals(cdParcelaPreco) && !validateGeneralidades(generalidade, FOURTY)){
						return SHORT_THREE;
					}					
				
				}

				/** NIVEL 2 **/
				// 16. grupo frete peso
				if (validateIndicadorDesconto(parametro.getTpIndicadorPercMinimoProgr().getValue(), parametro.getVlPercMinimoProgr(), THIRTY)
						|| validateIndicadorDesconto(parametro.getTpIndicadorFretePeso().getValue(), parametro.getVlFretePeso(), THIRTY)
						// 17. grupo frete valor e frete percentual
						|| !validateGrupoFreteValorFretePercentual(parametro, MODAL_RODOVIARIO, THIRTY)
						// 18. grupo gris
						|| validateIndicadorDesconto(parametro.getTpIndicadorPercentualGris().getValue(), parametro.getVlPercentualGris(), THIRTY)
						|| validateIndicadorTDA(parametro.getTpIndicadorMinimoGris().getValue(), parametro.getVlMinimoGris(), THIRTY)
						// 19. Grupo TDE e Grupo TRT 
						|| !validateGruposTDEeTRT(parametro, THIRTY)
						// 20. grupo pedagio
						|| !validateGrupoPedagioNivel2(parametro, THIRTY)) {
						return SHORT_TWO;
					}
				// 21. generalidades
				for (GeneralidadeCliente generalidade : generalidades) {
					if(!validateGeneralidades(generalidade, THIRTY)){
						return SHORT_TWO;
				}
				}

					}
				}
		return SHORT_ONE;
				}

	private boolean validateTDEeTRT(ParametroCliente parametro){
		return validateTDE(parametro) && validateTRT(parametro);
	}
	
	private boolean validateTRT(ParametroCliente parametro){
		
		if(!TABELA.equals(parametro.getTpIndicadorPercentualTrt().getValue()) 
				&& !DESCONTO.equals(parametro.getTpIndicadorPercentualTrt().getValue()) 
				&& !VALOR.equals(parametro.getTpIndicadorPercentualTrt().getValue()) 
				&& !ACRESCIMO.equals(parametro.getTpIndicadorPercentualTrt().getValue())){
			return false;
		}		
		
		if (validateIndicadorDesconto(parametro.getTpIndicadorPercentualTrt().getValue(), parametro.getVlPercentualTrt(), BigDecimal.TEN)) {
			return false;
		}			
		
		if(VALOR.equals(parametro.getTpIndicadorPercentualTrt().getValue())){
			BigDecimal vlParcela = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TDE);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlParcela, BigDecimal.TEN);				
			if(CompareUtils.lt(parametro.getVlPercentualTrt(), vlDesconto)){
				return false;
			}
		}
		
		if(!TABELA.equals(parametro.getTpIndicadorMinimoTrt().getValue()) 
				&& !DESCONTO.equals(parametro.getTpIndicadorMinimoTrt().getValue()) 
				&& !VALOR.equals(parametro.getTpIndicadorMinimoTrt().getValue()) 
				&& !ACRESCIMO.equals(parametro.getTpIndicadorMinimoTrt().getValue())){
			return false;
		}	
		
		if (validateIndicadorDesconto(parametro.getTpIndicadorMinimoTrt().getValue(), parametro.getVlMinimoTrt(), BigDecimal.TEN)) {
			return false;
		}	
		
		if(VALOR.equals(parametro.getTpIndicadorMinimoTrt().getValue())){
			BigDecimal vlParcela  = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TRT);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlParcela, BigDecimal.TEN);				
			if(CompareUtils.lt(parametro.getVlMinimoTrt(), vlDesconto)){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Valida TDE - Vendas Brasil 
	 * @param parametro
	 * @return
	 */
	private boolean validateTDE(ParametroCliente parametro){
		
		if(!TABELA.equals(parametro.getTpIndicadorPercentualTde().getValue()) 
				&& !DESCONTO.equals(parametro.getTpIndicadorPercentualTde().getValue()) 
				&& !VALOR.equals(parametro.getTpIndicadorPercentualTde().getValue()) 
				&& !ACRESCIMO.equals(parametro.getTpIndicadorPercentualTde().getValue())){
			return false;
		}		
		
		if (validateIndicadorDesconto(parametro.getTpIndicadorPercentualTde().getValue(), parametro.getVlPercentualTde(), BigDecimal.TEN)) {
			return false;
		}			
		
		if(VALOR.equals(parametro.getTpIndicadorPercentualTde().getValue())){
			BigDecimal vlParcela = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TDE);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlParcela, BigDecimal.TEN);				
			if(CompareUtils.lt(parametro.getVlPercentualTde(), vlDesconto)){
				return false;
			}
		}
		
		if(!TABELA.equals(parametro.getTpIndicadorMinimoTde().getValue()) 
				&& !DESCONTO.equals(parametro.getTpIndicadorMinimoTde().getValue()) 
				&& !VALOR.equals(parametro.getTpIndicadorMinimoTde().getValue()) 
				&& !ACRESCIMO.equals(parametro.getTpIndicadorMinimoTde().getValue())){
			return false;
		}	
		
		if (validateIndicadorDesconto(parametro.getTpIndicadorMinimoTde().getValue(), parametro.getVlMinimoTde(), BigDecimal.TEN)) {
			return false;
		}	
		
		if(VALOR.equals(parametro.getTpIndicadorMinimoTde().getValue())){
			BigDecimal vlParcela = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TDE);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlParcela, BigDecimal.TEN);				
			if(CompareUtils.lt(parametro.getVlMinimoTde(), vlDesconto)){
				return false;
			}
		}
		
		return true;
	}
	
	private boolean verificaNivelUF(DestinoProposta destinoProposta, String pGeralUF){
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(pGeralUF, false);		
		String dsConteudo = parametroGeral.getDsConteudo();
		if(StringUtils.isNotBlank(dsConteudo)){
			String[] ufs = dsConteudo.trim().split(";");
			List<String> list = Arrays.asList(ufs);
			if(list.contains(destinoProposta.getUnidadeFederativa().getSgUnidadeFederativa())){
				return true;
					}
				}

		return false;
			}

	private boolean validateDiferencaMinimaFretePeso(DestinoProposta destinoProposta, String pGeralValue){
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(pGeralValue, false);
		String dsConteudo = parametroGeral.getDsConteudo();
		if(StringUtils.isNotBlank(dsConteudo)){
			BigDecimal vlConteudo = new BigDecimal(dsConteudo.trim());
			if(CompareUtils.lt(destinoProposta.getPcDiferencaFretePeso(), vlConteudo)){
				return true;
		}
	}

		return false;
	}
	
	private boolean validateUFDestinoRota(DestinoProposta destinoProposta, Long idSimulacao){
		
		if(validateIndicadorDesconto(destinoProposta.getTpIndicadorFreteMinimo().getValue(), destinoProposta.getVlFreteMinimo(), FIFTY) 
				|| validateIndicadorDesconto(destinoProposta.getTpIndicadorFretePeso().getValue(), destinoProposta.getVlFretePeso(), FIFTY) 
				|| validateIndicadorDesconto(destinoProposta.getTpIndicadorAdvalorem().getValue(), destinoProposta.getVlAdvalorem(), FIFTY) ){
			return false;
		}

		/** Valida os Niveis parametrizados das UFs com o Minimo de Frete Peso */
		return validateNivelDestinoProposta(destinoProposta, idSimulacao);
	}

	/** 
	 * Valida os Niveis parametrizados das UFs com o Minimo de Frete Peso
	 */
	private boolean validateNivelDestinoProposta(DestinoProposta destinoProposta, Long idSimulacao) {
		
		Long idUnidadeFederativa = SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
		
		Map<String, Object> mapDiferencaCapitalInterior = diferencaCapitalInteriorService.findByIdSimulacao(destinoProposta.getIdDestinoProposta(), idUnidadeFederativa);

		//Se não vier nada, busca novamente com o id da uf de origem nula.
		if (mapDiferencaCapitalInterior == null){
			mapDiferencaCapitalInterior = diferencaCapitalInteriorService.findByIdSimulacao(destinoProposta.getIdDestinoProposta(), null);
			}
		
		if (mapDiferencaCapitalInterior != null){
			BigDecimal pcDiferencaMinimaAdvalorem = (BigDecimal) mapDiferencaCapitalInterior.get("pcDiferencaMinimaAdvalorem");
			BigDecimal pcDiferencaMinima = (BigDecimal) mapDiferencaCapitalInterior.get("pcDiferencaMinima");
			
			if (destinoProposta.getPcDiferencaFretePeso().compareTo(pcDiferencaMinima) < 0){
				return false;
			}
			
			if (destinoProposta.getPcDiferencaAdvalorem().compareTo(pcDiferencaMinimaAdvalorem) < 0){
				return false;
			}
				}
		return true;
	}
	
	private Short findFaixaAprovacaoDescontoDestinosPropostaRodoviario(Long idSimulacao) {
		
		Proposta proposta = propostaService.findByIdSimulacao(idSimulacao);

		if (proposta.getSimulacao() != null && proposta.getSimulacao().getNrFatorDensidade() != null)
			return SHORT_FOUR;//Se o campo “Fator densidade” estiver preenchido(informado), então retornar o valor 4.
		
		/** NIVEL 4 **/
		/** 25 - Valida Proposta DIFERENCIADO */
		List<DestinoProposta> destinosProposta = destinoPropostaService.findDestinosPropostaByIdSimulacao(idSimulacao);
		for (DestinoProposta destinoProposta : destinosProposta) {
			if(!validateUFDestinoRota(destinoProposta, idSimulacao)) {
				return SHORT_FOUR;
			}
		}
		/** 26 Valida Parametros relacionados a Proposta */
		List<ParametroCliente> parametros = parametroClienteService.findParametroByIdProposta(proposta.getIdProposta());
		if (parametros != null) {
			for (ParametroCliente parametro : parametros) {
				// 4. grupo gris DIFERENCIADO
				if (!validateGrupoGrisRodoviario(parametro, FOURTY)
						// 5. grupo pedagio DIFERENCIADO
						|| !validateGrupoPedagio(parametro)
						// 7. grupo total do frete DIFERENCIADO
						|| !validateGrupoTotalFreteDiferenciado(parametro) ) {
					return SHORT_FOUR;
				}
				// 8. generalidades DIFERENCIADO
				List<GeneralidadeCliente> generalidades = parametro.getGeneralidadeClientes();
				for (GeneralidadeCliente generalidade : generalidades) {
					if(!validateGeneralidadeInformada(generalidade)){
						return SHORT_FOUR;
						}
					}
				// 9. taxas
				List<TaxaCliente> taxas = parametro.getTaxaClientes();
					for (TaxaCliente taxa : taxas) {
						if (!TABELA.equals(taxa.getTpTaxaIndicador().getValue())) {
						return SHORT_FOUR;
						}
					}
				}
							}

		Short faixaLimitadores = findFaixaLimitadores(proposta.getSimulacao());
		if(faixaLimitadores.compareTo(SHORT_ONE) > 0){
			return faixaLimitadores;
		}

		/** NIVEL 3 **/
		/** 27 - Valida Parametros relacionados a Proposta */
		for (DestinoProposta destinoProposta : destinosProposta) {
			if(validateIndicadorDesconto(destinoProposta.getTpIndicadorFreteMinimo().getValue(), destinoProposta.getVlFreteMinimo(), FOURTY)
					|| validateIndicadorDesconto(destinoProposta.getTpIndicadorFretePeso().getValue(), destinoProposta.getVlFretePeso(), FOURTY)
					|| validateIndicadorDesconto(destinoProposta.getTpIndicadorAdvalorem().getValue(), destinoProposta.getVlAdvalorem(), FOURTY)) {
				return SHORT_THREE;
						}
					}
		/** 28 - Valida Parametros relacionados a Proposta */
		for (ParametroCliente parametro : parametros) {
			// 13. grupo gris
			if (!validateGrupoGris(parametro, FOURTY)
					// 14. grupo pedagio
					|| !validateGrupoIndicadorPedagio(parametro, FOURTY)) {
				return SHORT_THREE;
				}
			// 15. generalidades
			List<GeneralidadeCliente> generalidades = parametro.getGeneralidadeClientes();
			for (GeneralidadeCliente generalidade : generalidades) {
				if(!validateGeneralidades(generalidade, FOURTY)){
					return SHORT_THREE;
			}
		}
		}

		/** NIVEL 2 **/
		/** 29 - Valida Parametros relacionados a Proposta */
		for (DestinoProposta destinoProposta : destinosProposta) {
			if(validateIndicadorDesconto(destinoProposta.getTpIndicadorFreteMinimo().getValue(), destinoProposta.getVlFreteMinimo(), THIRTY)
					|| validateIndicadorDesconto(destinoProposta.getTpIndicadorFretePeso().getValue(), destinoProposta.getVlFretePeso(), THIRTY)
					|| validateIndicadorDesconto(destinoProposta.getTpIndicadorAdvalorem().getValue(), destinoProposta.getVlAdvalorem(), THIRTY)) {
				return SHORT_TWO;
			}
		}
		/** 30 - Valida Parametros relacionados a Proposta */
		for (ParametroCliente parametro : parametros) {
			// 18. grupo gris
			if (validateIndicadorDesconto(parametro.getTpIndicadorPercentualGris().getValue(), parametro.getVlPercentualGris(), THIRTY)
					|| validateIndicadorTDA(parametro.getTpIndicadorMinimoGris().getValue(), parametro.getVlMinimoGris(), THIRTY)
					// 19. Grupo TDE e Grupo TRT
					|| !validateGruposTDEeTRT(parametro, THIRTY)
					// 20. grupo pedagio
					|| !validateGrupoPedagioNivel2(parametro, THIRTY)) {
				return SHORT_TWO;
			}
			// 21. generalidades
			List<GeneralidadeCliente> generalidades = parametro.getGeneralidadeClientes();
			for (GeneralidadeCliente generalidade : generalidades) {
				if(!validateGeneralidades(generalidade, THIRTY)){
						return SHORT_TWO;
					}
				}
			}

		/** NIVEL 1 **/
		return SHORT_ONE; //31
		}

	private Short findFaixaAprovacaoDescontoPropostaAereo(Long idSimulacao) {
		List<ParametroCliente> parametros = parametroClienteService.findParametroByIdSimulacao(idSimulacao);
		if (parametros != null) {
			for (ParametroCliente parametro : parametros) {
				if (!validateGrupoFretePeso(parametro, MODAL_AEREO, THIRTY)
						|| !validateGrupoFreteValorFretePercentual(parametro, MODAL_AEREO, TWENTY_FIVE)
						|| !TABELA.equals(parametro.getTpIndicadorPercentualGris().getValue())
						|| !TABELA.equals(parametro.getTpIndicadorMinimoGris().getValue())
						|| !TABELA.equals(parametro.getTpIndicadorPedagio().getValue())
						|| !TABELA.equals(parametro.getTpIndicadorPercentualTrt().getValue())
						|| !TABELA.equals(parametro.getTpIndicadorMinimoTrt().getValue())
						|| !TABELA.equals(parametro.getTpIndicadorPercentualTde().getValue())
						|| !TABELA.equals(parametro.getTpIndicadorMinimoTde().getValue())
						|| !validateGrupoTotalFrete(parametro)) {
					return SHORT_TWO;
				}
				List<GeneralidadeCliente> generalidades = parametro.getGeneralidadeClientes();
				for (GeneralidadeCliente generalidade : generalidades) {
					if (!TABELA.equals(generalidade.getTpIndicador().getValue())) {
						return SHORT_TWO;
					}
				}
				List<TaxaCliente> taxas = parametro.getTaxaClientes();
				for (TaxaCliente taxa : taxas) {
					String cdParcelaPreco = taxa.getParcelaPreco().getCdParcelaPreco();
					if (CD_TAXA_COMBUSTIVEL.equals(cdParcelaPreco) || CD_TAXA_TERRESTRE.equals(cdParcelaPreco)) {
						if (!TABELA.equals(taxa.getTpTaxaIndicador().getValue())) {
							return SHORT_TWO;
						}
					} else {
						if (validateIndicadorTDA(taxa.getTpTaxaIndicador().getValue(), taxa.getVlTaxa(), TWENTY)
								|| ZERO.compareTo(taxa.getPsMinimo()) != 0
								|| ZERO.compareTo(taxa.getVlExcedente()) != 0) {
							return SHORT_TWO;
						}
					}
				}
				List<ServicoAdicionalCliente> servicosAdicionais = parametro.getSimulacao().getServicoAdicionalClientes();
				for (ServicoAdicionalCliente servicoAdicional : servicosAdicionais) {
					if (!TABELA.equals(servicoAdicional.getTpIndicador().getValue())) {
						return SHORT_TWO;
					}
				}
			}
		}
		return SHORT_ONE;
	}
	
	private boolean validateGrupoFretePeso(ParametroCliente parametro, String tpModal, BigDecimal desconto) {
		
		if(CompareUtils.lt(parametro.getSimulacao().getNrFatorCubagem(), new BigDecimal(250))){
			return false;
		}		
		if (validateIndicadorDesconto(parametro.getTpIndicadorPercMinimoProgr().getValue(), parametro.getVlPercMinimoProgr(), desconto)
				|| validateIndicadorDesconto(parametro.getTpIndicadorFretePeso().getValue(), parametro.getVlFretePeso(), desconto)) {
			return false;
		}
		if (MODAL_AEREO.equals(tpModal)) {
			if (validateIndicadorTDA(parametro.getTpTarifaMinima().getValue(), parametro.getVlTarifaMinima(), BigDecimal.valueOf(5.00))
					|| validateIndicadorTDA(parametro.getTpIndicVlrTblEspecifica().getValue(), parametro.getVlTblEspecifica(), BigDecimal.valueOf(15.00))) {
				return false;
			}
		}
		return true;
	}

	private boolean validateGeneralidadeInformada(GeneralidadeCliente generalidade) {
		BigDecimal vlGeneralidade = null;
		BigDecimal vlDesconto = null; 
		String cdParcelaPreco = generalidade.getParcelaPreco().getCdParcelaPreco();

		if (CD_DESPACHO.equals(cdParcelaPreco)){
			if (validateIndicadorTDVA(generalidade.getTpIndicador().getValue(), generalidade.getVlGeneralidade(), FIFTY)) {
				return false;
			}
			if(VALOR.equals(generalidade.getTpIndicador().getValue())){
				vlGeneralidade = this.findValorParcela(generalidade.getParametroCliente().getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_DESPACHO);
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, FIFTY);					
				if(CompareUtils.lt(generalidade.getVlGeneralidade(), vlDesconto)){
					return false;
				}
			}
		} else if (CD_TAD.equals(cdParcelaPreco)) {
			if (!TABELA.equals(generalidade.getTpIndicador().getValue())
					&& !DESCONTO.equals(generalidade.getTpIndicador().getValue())
					&& !ACRESCIMO.equals(generalidade.getTpIndicador().getValue())
					&& !FRACAOQUILO.equals(generalidade.getTpIndicador().getValue())) {
				return false;
			}
			if (validateIndicadorDesconto(generalidade.getTpIndicador().getValue(), generalidade.getVlGeneralidade(), FIFTY)) {
				return false;
			}
			if(FRACAOQUILO.equals(generalidade.getTpIndicador().getValue())){
				vlGeneralidade = this.findValorParcela(generalidade.getParametroCliente().getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TAD);
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, FIFTY);				
				if(CompareUtils.lt(generalidade.getVlGeneralidade(), vlDesconto)){
					return false;
				}
			}
		} else if (CD_TAS.equals(cdParcelaPreco)) {	
			
			if (!TABELA.equals(generalidade.getTpIndicador().getValue())
					&& !DESCONTO.equals(generalidade.getTpIndicador().getValue())
					&& !VALOR.equals(generalidade.getTpIndicador().getValue())) {
			return false;
		}
			
			if (validateIndicadorDesconto(generalidade.getTpIndicador().getValue(), generalidade.getVlGeneralidade(), TWENTY)) {
				return false;
			}			
			if(FRACAOQUILO.equals(generalidade.getTpIndicador().getValue())){
				vlGeneralidade = this.findValorParcela(generalidade.getParametroCliente().getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TAS);
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, TWENTY);				
				if(CompareUtils.lt(generalidade.getVlGeneralidade(), vlDesconto)){
					return false;
				}
			}
			
		} else if (CD_TAXA_SUFRAMA.equals(cdParcelaPreco)) {			
			
			if (!TABELA.equals(generalidade.getTpIndicador().getValue())
					&& !DESCONTO.equals(generalidade.getTpIndicador().getValue())
					&& !VALOR.equals(generalidade.getTpIndicador().getValue())) {
				return false;
			}	
			
			if (validateIndicadorDesconto(generalidade.getTpIndicador().getValue(), generalidade.getVlGeneralidade(), THIRTY)) {
				return false;
			}							
			if(FRACAOQUILO.equals(generalidade.getTpIndicador().getValue())){
				vlGeneralidade = this.findValorParcela(generalidade.getParametroCliente().getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TAXA_SUFRAMA);
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, THIRTY);				
				if(CompareUtils.lt(generalidade.getVlGeneralidade(), vlDesconto)){
					return false;
				}
			}			

		} else if (CD_SEGURO_FLUVIAL.equals(cdParcelaPreco)) {			
			
			if (!TABELA.equals(generalidade.getTpIndicador().getValue())
					&& !DESCONTO.equals(generalidade.getTpIndicador().getValue())
					&& !VALOR.equals(generalidade.getTpIndicador().getValue())) {
				return false;
			}	
			
			if (validateIndicadorDesconto(generalidade.getTpIndicador().getValue(), generalidade.getVlGeneralidade(), FIFTY)) {
				return false;
			}	
			
			if(FRACAOQUILO.equals(generalidade.getTpIndicador().getValue())){
				vlGeneralidade = this.findValorParcela(generalidade.getParametroCliente().getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_SEGURO_FLUVIAL);
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, FIFTY);				
				if(CompareUtils.lt(generalidade.getVlGeneralidade(), vlDesconto)){
					return false;
				}
			}			
			
		} 
		return true;
	}
	
	private boolean validateGrupoTotalFreteDiferenciado(ParametroCliente parametro) {

		if(CompareUtils.gt(parametro.getPcDescontoFreteTotal(), ZERO)){
			return false;
		}
				
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(ConstantesExpedicao.PC_COBRANCA_REENTREGA, false);
		BigDecimal vlConteudo = new BigDecimal(parametroGeral.getDsConteudo());
		if(CompareUtils.lt(parametro.getPcCobrancaReentrega(), vlConteudo)){
			return false;
		}

		parametroGeral = parametroGeralService.findByNomeParametro(ConstantesExpedicao.PC_COBRANCA_DEVOLUCAO, false);
		vlConteudo = new BigDecimal(parametroGeral.getDsConteudo());
		if(CompareUtils.lt(parametro.getPcCobrancaDevolucoes(), vlConteudo)){
			return false;
		}		
		return true;
	}

	
	private boolean validateGrupoPedagio(ParametroCliente parametro) {
		
		if(!TABELA.equals(parametro.getTpIndicadorPedagio().getValue()) 
				&& !DESCONTO.equals(parametro.getTpIndicadorPedagio().getValue()) 
				&& !VALOR.equals(parametro.getTpIndicadorPedagio().getValue()) 
				&& !FIXO.equals(parametro.getTpIndicadorPedagio().getValue()) 
				&& !FAIXAPESO.equals(parametro.getTpIndicadorPedagio().getValue()) 
				&& !FRACAOQUILO.equals(parametro.getTpIndicadorPedagio().getValue()) 
				&& !ACRESCIMO.equals(parametro.getTpIndicadorPedagio().getValue())){
			return false;
		}
		if(validateIndicadorDesconto(parametro.getTpIndicadorPedagio().getValue(), parametro.getVlPedagio(), TWENTY)){
			return false;
		}
		
		if(VALOR.equals(parametro.getTpIndicadorPedagio().getValue())){			
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, TWENTY);
			if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
				return false;
			}				
		}

		if(FIXO.equals(parametro.getTpIndicadorPedagio().getValue())){			
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, TWENTY);
			if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
				return false;
			}							
		}
		
		if(FRACAOQUILO.equals(parametro.getTpIndicadorPedagio().getValue())){			
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_FRACAO);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, TWENTY);
			if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
				return false;
			}			
		}
		
		if(FAIXAPESO.equals(parametro.getTpIndicadorPedagio().getValue())){			
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, TWENTY);
			if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
				return false;
			}			
		}
		return true;
	}

	private boolean validateGrupoPedagioNivel2(ParametroCliente parametro, BigDecimal desconto) {
		if(validateIndicadorDesconto(parametro.getTpIndicadorPedagio().getValue(), parametro.getVlPedagio(), desconto)){
			return false;
		}

		if(VALOR.equals(parametro.getTpIndicadorPedagio().getValue())){			
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);
			if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
				return false;
			}				
		}

		if(CompareUtils.ne(parametro.getVlPedagio(), ZERO)) {
			if(FIXO.equals(parametro.getTpIndicadorPedagio().getValue())){			
				BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);
				BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);
				if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
					return false;
				}							
			}
			
			if(FRACAOQUILO.equals(parametro.getTpIndicadorPedagio().getValue())){			
				BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_FRACAO);
				BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);
				if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
					return false;
				}			
			}
			
			if(FAIXAPESO.equals(parametro.getTpIndicadorPedagio().getValue())){			
				BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);
				BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);
				if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
					return false;
				}			
			}
		}
		return true;
	}
	
	private boolean validateGrupoGrisRodoviario(ParametroCliente parametro, BigDecimal desconto) {
		if(validateIndicadorTDVA(parametro.getTpIndicadorPercentualGris().getValue(), parametro.getVlPercentualGris(), desconto)
				|| !validateGris(parametro, desconto)
				|| !validateMinimoGris(parametro, desconto)){
			return false;
		}
		return true;
	}

	private boolean validateGris(ParametroCliente parametro, BigDecimal desconto) {
		if(validateIndicadorDesconto(parametro.getTpIndicadorPercentualGris().getValue(), parametro.getVlPercentualGris(), desconto)){
			return false;
		}

		if(VALOR.equals(parametro.getTpIndicadorPercentualGris().getValue())){
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_GRIS);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);
			if(CompareUtils.lt(parametro.getVlPercentualGris(), vlDesconto)){
				return false;
			}			
		}
		return true;
	}
	private boolean validateMinimoGris(ParametroCliente parametro, BigDecimal desconto) {
		if(validateIndicadorDesconto(parametro.getTpIndicadorMinimoGris().getValue(), parametro.getVlMinimoGris(), desconto)){
			return false;
		}
		if(VALOR.equals(parametro.getTpIndicadorMinimoGris().getValue())){
			BigDecimal vlMinimoGris = this.findValorMinimoParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_GRIS);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlMinimoGris, desconto);
			if(CompareUtils.lt(parametro.getVlMinimoGris(), vlDesconto)){
				return false;
			}
		}

		return true;
	}
	
	private boolean validateGrupoFretePesoRodoviario(ParametroCliente parametro) {
		if(CompareUtils.lt(BigDecimalUtils.defaultBigDecimal(parametro.getSimulacao().getNrFatorCubagem()) , new BigDecimal(200)) 
				|| !TABELA.equals(parametro.getTpIndicadorMinFretePeso().getValue())
				|| CompareUtils.ne(parametro.getVlMinimoFreteQuilo(), ZERO)
				|| CompareUtils.ne(parametro.getVlFreteVolume(), ZERO) 
				|| parametro.getBlPagaPesoExcedente()
				|| validateIndicadorTDA(parametro.getTpIndicadorFretePeso().getValue(), parametro.getVlFretePeso(), FIFTY)
				|| validateIndicadorDesconto(parametro.getTpIndicadorPercMinimoProgr().getValue(), parametro.getVlPercMinimoProgr(), FIFTY)){
			return false;
		}
		return true;
	}

	
	private boolean validateGrupoFreteValorFretePercentual(ParametroCliente parametro, String tpModal, BigDecimal desconto) {
		if (validateIndicadorDesconto(parametro.getTpIndicadorAdvalorem().getValue(), parametro.getVlAdvalorem(), desconto)
				|| validateIndicadorDesconto(parametro.getTpIndicadorAdvalorem2().getValue(), parametro.getVlAdvalorem2(), desconto)) {
			return false;
		}
		return true;
	}
		
	private boolean validateGeneralidadeInformada(GeneralidadeCliente generalidade, BigDecimal desconto){
	
			if(validateIndicadorDesconto(generalidade.getTpIndicador().getValue(), generalidade.getVlGeneralidade(), desconto)){
			return false;
		}

		if(VALOR.equals(generalidade.getTpIndicador().getValue())){
			BigDecimal vlGeneralidade = this.findValorParcela(generalidade.getParametroCliente().getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_TAS);
				BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);	
				if(CompareUtils.lt(generalidade.getVlGeneralidade(), vlDesconto)){
					return false;
				}
			}	
		return true;
	}

	private boolean validateGeneralidades(GeneralidadeCliente generalidade, BigDecimal desconto){

		String cdParcelaPreco = generalidade.getParcelaPreco().getCdParcelaPreco();
		
		String[] semCalculo = new String[]{TABELA,DESCONTO,VALOR};
		
		if(!Arrays.asList(semCalculo).contains(cdParcelaPreco)){
					return false;
				}				
		
		return validateGeneralidadeInformada(generalidade,desconto);		
			}						
	
	
	private boolean validateGrupoIndicadorPedagio(ParametroCliente parametro, BigDecimal desconto){
		if(validateIndicadorDesconto(parametro.getTpIndicadorPedagio().getValue(), parametro.getVlPedagio(), desconto)){
			return false;
		}

		if(VALOR.equals(parametro.getTpIndicadorPedagio().getValue())){
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);	
			if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)) {
				return false;
			}
		}

		if(FIXO.equals(parametro.getTpIndicadorPedagio().getValue())){
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);	
			if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
				return false;
			}
		}

		if(FRACAOQUILO.equals(parametro.getTpIndicadorPedagio().getValue())){
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_PEDAGIO_FRACAO);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);	
			if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
				return false;
			}
		}		
		
		if(FAIXAPESO.equals(parametro.getTpIndicadorPedagio().getValue())){
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(), ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);			
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);	
			if(CompareUtils.lt(parametro.getVlPedagio(), vlDesconto)){
				return false;
			}
		}		
		return true;
	}
	
	private boolean validateGruposTDEeTRT(ParametroCliente parametro, BigDecimal desconto){
		if(!TABELA.equals(parametro.getTpIndicadorPercentualTde().getValue())
				|| !TABELA.equals(parametro.getTpIndicadorMinimoTde().getValue())
				|| !TABELA.equals(parametro.getTpIndicadorPercentualTrt().getValue())
				|| validateIndicadorDesconto(parametro.getTpIndicadorMinimoTrt().getValue(), parametro.getVlMinimoTrt(), desconto)){
			return false;
		}
		if(VALOR.equals(parametro.getTpIndicadorMinimoTrt().getValue())){
			BigDecimal vlGeneralidade = this.findValorParcela(parametro.getTabelaPreco().getIdTabelaPreco(),ConstantesExpedicao.CD_TRT);
			BigDecimal vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, desconto);
			if(CompareUtils.lt(parametro.getVlMinimoTrt(), vlDesconto)){
				return false;
			}			
		}
		return true;
	}

	private boolean validateGrupoGris(ParametroCliente parametro, BigDecimal desconto){
		if(!validateGris(parametro, desconto)
				|| !validateMinimoGris(parametro, desconto)){
			return false;
		}
		return true;
	}

	private boolean validateGrupoFreteValorFretePercentualRodoviario(ParametroCliente parametro) {

		if(CompareUtils.ne(parametro.getPcFretePercentual(), ZERO)
				|| !TABELA.equals(parametro.getTpIndicadorValorReferencia().getValue())
				|| validateIndicadorTDA(parametro.getTpIndicadorAdvalorem().getValue(), parametro.getVlAdvalorem(), FIFTY)
				|| validateIndicadorTDA(parametro.getTpIndicadorAdvalorem2().getValue(), parametro.getVlAdvalorem2(), FIFTY)){
			return false;
		}
		return true;
	}

	private boolean validateGrupoTotalFrete(ParametroCliente parametro) {
		if (ZERO.compareTo(parametro.getPcDescontoFreteTotal()) < 0) {
			return false;
		}
		BigDecimal pcCobrancaReentrega = (BigDecimal) configuracoesFacade.getValorParametro("PercentualCobrancaReentrega");
		if (parametro.getPcCobrancaReentrega().compareTo(pcCobrancaReentrega) > 0) {
			return false;
		}
		BigDecimal pcCobrancaDevolucao = (BigDecimal) configuracoesFacade.getValorParametro("PercentualCobrancaDevolucao");
		if (parametro.getPcCobrancaDevolucoes().compareTo(pcCobrancaDevolucao) > 0) {
			return false;
		}
		return true;
	}

	private boolean validateIndicadorTDA(String indicador, BigDecimal valor, BigDecimal desconto) {
		if (!TABELA.equals(indicador) && !DESCONTO.equals(indicador) && !ACRESCIMO.equals(indicador)) {
			return true;
		}
		return validateIndicadorDesconto(indicador, valor, desconto);
	}
	
	private boolean validateIndicadorTDVA(String indicador, BigDecimal valor, BigDecimal desconto) {
		if (!TABELA.equals(indicador) && !DESCONTO.equals(indicador) && !VALOR.equals(indicador) && !ACRESCIMO.equals(indicador)) {
			return true;
		}
		return validateIndicadorDesconto(indicador, valor, desconto);
	}
	
	private boolean validateIndicadorDesconto(String indicador, BigDecimal valor, BigDecimal desconto) {
		if (DESCONTO.equals(indicador) && CompareUtils.gt(valor, desconto)) {
			return true;
		}
		return false;
	}

	private boolean needAtualizacaoPendencia(Simulacao simulacao, ParametroCliente parametroCliente) {
		Simulacao s = (Simulacao) this.findById(simulacao.getIdSimulacao());
		if (!equals(s.getClienteByIdCliente(), simulacao.getClienteByIdCliente()) ||
			!equals(s.getServico(), simulacao.getServico()) ||
			!equals(s.getTabelaPreco(), simulacao.getTabelaPreco()))
		{
			return true;
		}
		/** Remove da sessao do hibernate */
		getSimulacaoDAO().getAdsmHibernateTemplate().evict(s);

		boolean result = false;
		ParametroCliente pc = (ParametroCliente) parametroClienteService.findById(parametroCliente.getIdParametroCliente());
		if(!equals(pc.getTpIndicadorPercentualGris(), parametroCliente.getTpIndicadorPercentualGris()) ||
			!equals(pc.getTpIndicadorMinimoGris(), parametroCliente.getTpIndicadorMinimoGris()) ||
			!equals(pc.getTpIndicadorPercentualTrt(), parametroCliente.getTpIndicadorPercentualTrt()) ||
			!equals(pc.getTpIndicadorMinimoTrt(), parametroCliente.getTpIndicadorMinimoTrt()) ||
			!equals(pc.getTpIndicadorPedagio(), parametroCliente.getTpIndicadorPedagio()) ||
			!equals(pc.getTpIndicadorMinFretePeso(), parametroCliente.getTpIndicadorMinFretePeso()) ||
			!equals(pc.getTpIndicadorPercMinimoProgr(), parametroCliente.getTpIndicadorPercMinimoProgr()) ||
			!equals(pc.getTpIndicadorFretePeso(), parametroCliente.getTpIndicadorFretePeso()) ||
			!equals(pc.getTpIndicadorAdvalorem(), parametroCliente.getTpIndicadorAdvalorem()) ||
			!equals(pc.getTpIndicadorAdvalorem2(), parametroCliente.getTpIndicadorAdvalorem2()) ||
			!equals(pc.getTpIndicadorValorReferencia(), parametroCliente.getTpIndicadorValorReferencia()) ||
			!equals(pc.getTpIndicVlrTblEspecifica(), parametroCliente.getTpIndicVlrTblEspecifica()) ||
			!equals(pc.getTpTarifaMinima(), parametroCliente.getTpTarifaMinima()) ||
			!equals(pc.getTpIndicadorMinimoTde(), parametroCliente.getTpIndicadorMinimoTde()) ||
			!equals(pc.getTpIndicadorPercentualTde(), parametroCliente.getTpIndicadorPercentualTde()) ||
			!equals(pc.getBlPagaCubagem(), parametroCliente.getBlPagaCubagem()) ||
			!equals(pc.getBlPagaPesoExcedente(), parametroCliente.getBlPagaPesoExcedente()) ||
			!equalsString(pc.getDsEspecificacaoRota(), parametroCliente.getDsEspecificacaoRota()) ||
			!decimalEquals(pc.getVlPercentualGris(), parametroCliente.getVlPercentualGris()) ||
			!decimalEquals(pc.getVlMinimoGris(), parametroCliente.getVlMinimoGris()) ||
			!decimalEquals(pc.getVlPercentualTrt(), parametroCliente.getVlPercentualTrt()) ||
			!decimalEquals(pc.getVlMinimoTrt(), parametroCliente.getVlMinimoTrt()) ||
			!decimalEquals(pc.getVlPedagio(), parametroCliente.getVlPedagio()) ||
			!decimalEquals(pc.getVlMinFretePeso(), parametroCliente.getVlMinFretePeso()) ||
			!decimalEquals(pc.getVlPercMinimoProgr(), parametroCliente.getVlPercMinimoProgr()) ||
			!decimalEquals(pc.getVlFretePeso(), parametroCliente.getVlFretePeso()) ||
			!decimalEquals(pc.getVlAdvalorem(), parametroCliente.getVlAdvalorem()) ||
			!decimalEquals(pc.getVlAdvalorem2(), parametroCliente.getVlAdvalorem2()) ||
			!decimalEquals(pc.getVlValorReferencia(), parametroCliente.getVlValorReferencia()) ||
			!decimalEquals(pc.getVlTblEspecifica(), parametroCliente.getVlTblEspecifica()) ||
			!decimalEquals(pc.getVlTarifaMinima(), parametroCliente.getVlTarifaMinima()) ||
			!decimalEquals(pc.getVlMinimoFreteQuilo(), parametroCliente.getVlMinimoFreteQuilo()) ||
			!decimalEquals(pc.getPcFretePercentual(), parametroCliente.getPcFretePercentual()) ||
			!decimalEquals(pc.getVlMinimoFretePercentual(), parametroCliente.getVlMinimoFretePercentual()) ||
			!decimalEquals(pc.getVlToneladaFretePercentual(), parametroCliente.getVlToneladaFretePercentual()) ||
			!decimalEquals(pc.getPsFretePercentual(), parametroCliente.getPsFretePercentual()) ||
			!decimalEquals(pc.getPcDescontoFreteTotal(), parametroCliente.getPcDescontoFreteTotal()) ||
			!decimalEquals(pc.getVlFreteVolume(), parametroCliente.getVlFreteVolume()) ||
			!decimalEquals(pc.getPcPagaCubagem(), parametroCliente.getPcPagaCubagem()) ||
			!decimalEquals(pc.getPcCobrancaReentrega(), parametroCliente.getPcCobrancaReentrega()) ||
			!decimalEquals(pc.getPcCobrancaDevolucoes(), parametroCliente.getPcCobrancaDevolucoes()) ||
			!decimalEquals(pc.getVlPercentualTde(), parametroCliente.getVlPercentualTde()) ||
			!decimalEquals(pc.getVlMinimoTde(), parametroCliente.getVlMinimoTde()) ||
			!equals(pc.getMunicipioByIdMunicipioOrigem(), parametroCliente.getMunicipioByIdMunicipioOrigem()) ||
			!equals(pc.getMunicipioByIdMunicipioDestino(), parametroCliente.getMunicipioByIdMunicipioDestino()) ||
			!equals(pc.getFilialByIdFilialOrigem(), parametroCliente.getFilialByIdFilialOrigem()) ||
			!equals(pc.getFilialByIdFilialDestino(), parametroCliente.getFilialByIdFilialDestino()) ||
			!equals(pc.getZonaByIdZonaOrigem(), parametroCliente.getZonaByIdZonaOrigem()) ||
			!equals(pc.getZonaByIdZonaDestino(), parametroCliente.getZonaByIdZonaDestino()) ||
			!equals(pc.getPaisByIdPaisOrigem(), parametroCliente.getPaisByIdPaisOrigem()) ||
			!equals(pc.getPaisByIdPaisDestino(), parametroCliente.getPaisByIdPaisDestino()) ||
			!equals(pc.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(), parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem()) ||
			!equals(pc.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(), parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino()) ||
			!equals(pc.getUnidadeFederativaByIdUfOrigem(), parametroCliente.getUnidadeFederativaByIdUfOrigem()) ||
			!equals(pc.getUnidadeFederativaByIdUfDestino(), parametroCliente.getUnidadeFederativaByIdUfDestino()) ||
			!equals(pc.getAeroportoByIdAeroportoOrigem(), parametroCliente.getAeroportoByIdAeroportoOrigem()) ||
			!equals(pc.getAeroportoByIdAeroportoDestino(), parametroCliente.getAeroportoByIdAeroportoDestino())) {

			result = true;
		}

		/** Remove da sessao do hibernate */
		parametroClienteService.evict(pc);
		return result;
	}
	
	public List findSimulacoesInPipelineClienteSimulacaoByPipelineCliente(PipelineCliente pipelineCliente) {
		return getSimulacaoDAO().findSimulacoesInPipelineClienteSimulacaoByPipelineCliente(pipelineCliente);
	}
	
	public Simulacao findSimulacaoEfetivadaByIdPipeLineCliente(PipelineCliente pipelineCliente) {
		return getSimulacaoDAO().findSimulacaoEfetivadaByIdPipeLineCliente(pipelineCliente);
	}
	
	public List<Map<String, Object>> findDataForReport(Map map) {
		List<Simulacao> list = this.find(map);

		List<Map<String, Object>> listReturn = new ListToMapConverter<Simulacao>().mapRows(list, new RowMapper<Simulacao>() {
			@Override
			public Map<String, Object> mapRow(Simulacao o) {
				Map<String, Object> toReturn = new HashMap<String, Object>();
				toReturn.put("dataSimulacao", o.getDtSimulacao());
				toReturn.put("numeroProposta", String.format("%s %s", o.getFilial().getSgFilial(), FormatUtils.formatLongWithZeros(o.getNrSimulacao(), "000000")));
				toReturn.put("nmCliente", o.getClienteByIdCliente().getPessoa().getNmPessoa());
				toReturn.put("tabelaPreco", (o.getTabelaPreco() != null ? String.format("%s%d-%s", o.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue(), o.getTabelaPreco().getTipoTabelaPreco().getNrVersao(), o.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco()) : ""));
				toReturn.put("tabelaPrecoFob", (o.getTabelaPrecoFob() != null ? String.format("%s%d-%s", o.getTabelaPrecoFob().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue(), o.getTabelaPrecoFob().getTipoTabelaPreco().getNrVersao(), o.getTabelaPrecoFob().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco()) : ""));
				toReturn.put("tpSituacaoAprovacao", o.getTpSituacaoAprovacao() != null ? o.getTpSituacaoAprovacao().getDescriptionAsString() : "");
				toReturn.put("efetivada", o.getBlEfetivada() ? "Sim": "Não");
				return toReturn;
			}
		});
		
		return listReturn;
	}

	private boolean equals(Object val1, Object val2) {
		if (val1 != null) {
			return val1.equals(val2);
		} else {
			return val2 == null;
		}
	}

	private boolean decimalEquals(BigDecimal val1, BigDecimal val2) {
		if (val1 != null && val2 != null) {
			return CompareUtils.eq(val1, val2);
		} else {
			return val1 == null && val2 == null;
		}
	}

	private boolean equalsString(String val1, String val2) {
		if (StringUtils.isNotBlank(val1) && StringUtils.isNotBlank(val2)) {
			return val1.equals(val2);
		} else {
			return StringUtils.isBlank(val1) && StringUtils.isBlank(val2);
		}
	}
	

	/*
	 * GETTERS E SETTERS
	 */
	public void setSimulacaoDAO(SimulacaoDAO dao) {
		setDao( dao );
	}
	private SimulacaoDAO getSimulacaoDAO() {
		return (SimulacaoDAO) getDao();
	}
	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	public void setLimiteDescontoService(LimiteDescontoService limiteDescontoService) {
		this.limiteDescontoService = limiteDescontoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setGrupoClassificacaoFilialService(GrupoClassificacaoFilialService grupoClassificacaoFilialService) {
		this.grupoClassificacaoFilialService = grupoClassificacaoFilialService;
	}
	public void setParametroClienteService(ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}
	public void setGeneralidadeClienteService(GeneralidadeClienteService generalidadeClienteService) {
		this.generalidadeClienteService = generalidadeClienteService;
	}
	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}
	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
	public void setTaxaClienteService(TaxaClienteService taxaClienteService) {
		this.taxaClienteService = taxaClienteService;
	}
	public void setServicoAdicionalClienteService(ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}
	public void setPropostaService(PropostaService propostaService) {
		this.propostaService = propostaService;
	}
	public void setDestinoPropostaService(DestinoPropostaService destinoPropostaService) {
		this.destinoPropostaService = destinoPropostaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setHistoricoReajusteClienteService(HistoricoReajusteClienteService historicoReajusteClienteService) {
		this.historicoReajusteClienteService = historicoReajusteClienteService;
	}

	public void setGeneralidadeService(GeneralidadeService generalidadeService) {
		this.generalidadeService = generalidadeService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public PerfilUsuarioService getPerfilUsuarioService() {
		return perfilUsuarioService;
	}

	public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}

	public DiferencaCapitalInteriorService getDiferencaCapitalInteriorService() {
		return diferencaCapitalInteriorService;
	}

	public void setDiferencaCapitalInteriorService(DiferencaCapitalInteriorService diferencaCapitalInteriorService) {
		this.diferencaCapitalInteriorService = diferencaCapitalInteriorService;
	}

	public Simulacao findByNrPropostaAndClienteAndIdFilial(Long nrProposta, Long idCliente, Long idFilial) {
		return getSimulacaoDAO().findByNrPropostaAndClienteAndIdFilial(nrProposta, idCliente, idFilial);
	}
	public TabelaPrecoParcelaService getTabelaPrecoParcelaService() {
		return tabelaPrecoParcelaService;
	}

	public void setTabelaPrecoParcelaService(
			TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}

	public FuncionarioService getFuncionarioService() {
		return funcionarioService;
	}

	public void setFuncionarioService(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	public HistoricoEfetivacaoService getHistoricoEfetivacaoService() {
		return historicoEfetivacaoService;
	}

	public void setHistoricoEfetivacaoService(
			HistoricoEfetivacaoService historicoEfetivacaoService) {
		this.historicoEfetivacaoService = historicoEfetivacaoService;
	}

	public void setSimulacaoAnexoService(SimulacaoAnexoService simulacaoAnexoService) {
		this.simulacaoAnexoService = simulacaoAnexoService;
	}
	
	public void setComiteNivelMarkupService(ComiteNivelMarkupService comiteNivelMarkupService) {
		this.comiteNivelMarkupService = comiteNivelMarkupService;
	}
	
	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	
	public void setTipoLocalizacaoMunicipioService(TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
	}
	
	public void setMotivoReprovacaoService(MotivoReprovacaoService motivoReprovacaoService) {
		this.motivoReprovacaoService = motivoReprovacaoService;
	}

	public void setGerarEmailMensagemAvisoService(GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService) {
		this.gerarEmailMensagemAvisoService = gerarEmailMensagemAvisoService;
	}
	
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}


    public void setValorFaixaProgressivaPropostaService(ValorFaixaProgressivaPropostaService valorFaixaProgressivaPropostaService) {
        this.valorFaixaProgressivaPropostaService = valorFaixaProgressivaPropostaService;
    }

	public void setValorFaixaProgressivaService(ValorFaixaProgressivaService valorFaixaProgressivaService) {
		this.valorFaixaProgressivaService = valorFaixaProgressivaService;
	}

    public void setAeroportoService(AeroportoService aeroportoService) {
        this.aeroportoService = aeroportoService;
    }


	public ValorFaixaProgressivaService getValorFaixaProgressivaService() {
		return valorFaixaProgressivaService;
	}

}
