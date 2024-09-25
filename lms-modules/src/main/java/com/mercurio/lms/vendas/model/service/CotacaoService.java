package com.mercurio.lms.vendas.model.service;

import static com.mercurio.lms.util.BigDecimalUtils.ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.DocumentoServicoFacade;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.expedicao.model.DoctoServicoDadosCliente;
import com.mercurio.lms.expedicao.model.ParcelaServico;
import com.mercurio.lms.expedicao.model.ParcelaServicoAdicional;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.model.service.CalculoFreteService;
import com.mercurio.lms.expedicao.model.service.CalculoParcelaFreteService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DimensaoService;
import com.mercurio.lms.expedicao.model.service.DpeService;
import com.mercurio.lms.expedicao.model.service.EmbarqueValidationService;
import com.mercurio.lms.expedicao.model.service.ServAdicionalDocServService;
import com.mercurio.lms.expedicao.util.CalculoFreteUtils;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.GrupoClassificacao;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.GrupoClassificacaoFilialService;
import com.mercurio.lms.municipios.model.service.McdService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.model.service.GeneralidadeService;
import com.mercurio.lms.tabelaprecos.model.service.LimiteDescontoService;
import com.mercurio.lms.tabelaprecos.model.service.MunicipioGrupoRegiaoService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.util.ConstantesTabelaPrecos;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.DiaUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.ParcelaCotacao;
import com.mercurio.lms.vendas.model.PromotorCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.dao.CotacaoDAO;
import com.mercurio.lms.vendas.util.ClienteUtils;
import com.mercurio.lms.vendas.util.ConstantesCotacao;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;
import com.mercurio.lms.vendas.util.VendasUtils;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.AcaoService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.vendas.cotacaoService"
 */
public class CotacaoService extends AbstractCotacaoService {
	
	private static final BigDecimal TWENTY_FIVE = new BigDecimal(25);
	private static final BigDecimal FIVE = new BigDecimal(5);
	private static final Long FAIXA_1 = 1l;
	private static final Long FAIXA_2 = 2l;
	private static final Long FAIXA_3 = 3l;
	private static final Long FAIXA_4 = 4l;

	private static final BigDecimal FIFTY = BigDecimal.valueOf(50.00);
	private static final BigDecimal FORTY = new BigDecimal(40.00);
	private static final BigDecimal THIRTY = BigDecimal.valueOf(30.00);
	private static final BigDecimal TWENTY = BigDecimal.valueOf(20.00);
	
	private static final String FIXO = "F";
	private static final String VALOR = "V";
	private static final String TABELA = "T";
	private static final String DESCONTO = "D";
	private static final String ACRESCIMO = "A";
	private static final String FAIXAPESO = "X";
	private static final String FRACAOQUILO = "Q";
	
	private static final char SITUACAO_APROVACAO_APROVADA = 'A';
	private static final char SITUACAO_EM_APROVACAO = 'E';

	public static final String SITUACAO_COTACAO_APROVADA = "A";
	public static final String SITUACAO_COTACAO_REPROVADA = "R";
	public static final String SITUACAO_COTACAO_COTADA = "T";
	public static final String SITUACAO_COTACAO_PERDIDA = "P";
	public static final String SITUACAO_COTACAO_EFETIVADA = "E";

	
	private McdService mcdService;
	private DomainValueService domainValueService;
	private ConhecimentoService conhecimentoService;
	private DocumentoServicoFacade documentoServicoFacade;
	private MunicipioFilialService municipioFilialService;
	private ProibidoEmbarqueService proibidoEmbarqueService;
	private LiberacaoEmbarqueService liberacaoEmbarqueService;
	private GeneralidadeClienteService generalidadeClienteService;
	private LimiteDescontoService limiteDescontoService;
	private PerfilUsuarioService perfilUsuarioService;
	private GrupoClassificacaoFilialService grupoClassificacaoFilialService;
	private ParcelaPrecoService parcelaPrecoService;
	private GeneralidadeService generalidadeService;
	private WorkflowPendenciaService workflowPendenciaService;
	private AcaoService acaoService;
	private TaxaClienteService taxaClienteService;
	private DiaUtils diaUtils;
	private FilialService filialService;
	private TabelaPrecoService tabelaPrecoService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private CalculoFreteService calculoFreteService;
	private DivisaoClienteService divisaoClienteService;		
	private ParcelaCotacaoService parcelaCotacaoService;
	private ParametroClienteService parametroClienteService;
	private DimensaoService dimensaoService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private ServAdicionalDocServService servAdicionalDocServService;	
	private CalculoParcelaFreteService calculoParcelaFreteService;	
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private ParametroGeralService parametroGeralService;
	private DpeService dpeService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ValorFaixaProgressivaPropostaService valorFaixaProgressivaPropostaService;
	private PromotorClienteService promotorClienteService;
	private MunicipioGrupoRegiaoService municipioGrupoRegiaoService;
	private EmbarqueValidationService embarqueValidationService;
	
	public DpeService getDpeService() {
		return dpeService;
	}

	public void setDpeService(DpeService dpeService) {
		this.dpeService = dpeService;
	}

	class FaixaAprovacao {
		private Long faixaAprovacao = null;
		
		public FaixaAprovacao(Long faixaAprovacao) {
			super();
			this.faixaAprovacao = faixaAprovacao;
		}

		public Long getFaixaAprovacao() {
			return faixaAprovacao;
		}

		public void setFaixaAprovacao(Long faixaAprovacao) {
			this.faixaAprovacao = faixaAprovacao;
		}
	}
	
	
	/**
	 * Recupera uma instância de <code>Cotacao</code> a partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Cotacao findById(Long id) {
		return (Cotacao) super.findById(id);
	}

	public Cotacao findById(Long id, boolean initializeLazyProperties) {
		Cotacao c = (Cotacao)super.findByIdInitLazyProperties(id, initializeLazyProperties);
        if(initializeLazyProperties){
            c.getDimensoes().size();
            c.getServico().getTpModal();
            c.getLiberacoesDoctoServico().size();
            c.getClienteByIdClienteSolicitou().getBlAceitaFobGeral();
	}
        return c;
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Cotacao bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setCotacaoDAO(CotacaoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private CotacaoDAO getCotacaoDAO() {
		return (CotacaoDAO) getDao();
	}

	public ResultSetPage findCotacaoPaginated(TypedFlatMap criteria) {
		return getCotacaoDAO().findCotacaoPaginated(criteria);
	}

	public List<Object[]> findCotacoesByUserAndTipoSituacaoPaginated(TypedFlatMap criteria) {
		return getCotacaoDAO().findCotacoesByUserAndTipoSituacaoPaginated(criteria);
	}

	public Integer getRowCountCotacao(TypedFlatMap criteria) {
		return getCotacaoDAO().getRowCountCotacao(criteria);
	}

	public List findCotacoes(Long idCliente, String tpDocumentoCotacao) {
		return getCotacaoDAO().findCotacoes(idCliente, tpDocumentoCotacao);
	}

	public List findCotacaoByIdClienteTpSituacaoNotas(Long idCliente,
			String tpSituacao, List nrNotas) {
		return getCotacaoDAO().findCotacaoByIdClienteTpSituacaoNotas(idCliente,
				tpSituacao, nrNotas);
	}

	public Map findByIdCtrc(Long idCotacao) {
		return getCotacaoDAO().findByIdCtrc(idCotacao);
	}

	public List findIdsByIdDoctoServico(Long idDoctoServico) {
		return getCotacaoDAO().findIdsByIdDoctoServico(idDoctoServico);
	}

	public Cotacao findByIdDoctoServico(Long idDoctoServico) {
		Map filter = new HashMap(1);
		Map id = new HashMap(1);
		id.put("idDoctoServico", idDoctoServico);
		filter.put("doctoServico", id);
		List result = find(filter);
		if (!result.isEmpty()) {
			return (Cotacao) result.get(0);
		}
		return null;
	}
	
	public List findDivisaoCliente(Cotacao cotacao) {
		Cliente cliente = this.getClienteBaseCalculo(cotacao);
		List<DivisaoCliente> result = null;
		if (cliente != null
				&& ClienteUtils.isParametroClienteEspecial(cliente
						.getTpCliente().getValue())) {
			result = divisaoClienteService
					.findDivisaoClienteByIdServico(cliente.getIdCliente(),
							cotacao.getServico().getIdServico());
		}
		return result;
	}
	
	public Map findTabelaPrecoByDivisaoCliente(Long idDivisaoCliente,
			Long idServico, String tpModal, String tpAbrangencia) {
		Map result = new HashMap();
		TabelaPreco tabelaPreco = null;
		if( idDivisaoCliente != null){
			List tabelas = tabelaDivisaoClienteService
					.findByDivisaoCliente(idDivisaoCliente);
			if( !tabelas.isEmpty() ){
				TabelaDivisaoCliente tabelaCliente = (TabelaDivisaoCliente) tabelas
						.get(0);
				tabelaPreco = tabelaCliente.getTabelaPreco();
			}
		}else{
			CalculoFrete c = new CalculoFrete();
			c.setIdServico(idServico);
			c.setTpModal(tpModal);
			c.setTpAbrangencia(tpAbrangencia);
			tabelaPreco = calculoFreteService.findTabelaPrecoMercurio(c);
		}
		if( tabelaPreco != null ){
			StringBuilder sb = new StringBuilder();
			sb.append(tabelaPreco.getTipoTabelaPreco()
					.getTpTipoTabelaPrecoNrVersao());
			sb.append("-");
			sb.append(tabelaPreco.getSubtipoTabelaPreco()
					.getTpSubtipoTabelaPreco());
			result.put("idTabelaPreco",tabelaPreco.getIdTabelaPreco());
			result.put("nmTabelaPreco",sb.toString());
		}
		return result;
	}

	public String executeWorkflow(List<Long> idsCotacao,
			List<String> tpSituacoesWorkflow) {
		if (idsCotacao != null && tpSituacoesWorkflow != null) {
			if (idsCotacao.size() != tpSituacoesWorkflow.size()) {
				return configuracoesFacade.getMensagem("LMS-01110");
			}
			for(int i = 0; i < idsCotacao.size(); i++) {
				Long idSimulacao = idsCotacao.get(i);
				String tpSituacaoAprovacao = tpSituacoesWorkflow.get(i);
				Cotacao cotacao = findById(idSimulacao);
				cotacao.setTpSituacaoAprovacao(new DomainValue(
						tpSituacaoAprovacao));
				if ("A".equals(tpSituacaoAprovacao)) {
					List<Acao> acoes = acaoService
							.findByIdPendenciaTpSituacaoAcao(cotacao
									.getPendencia().getIdPendencia(), "A");
					if (acoes != null && acoes.size() > 0) {
						Acao acao = acoes.get(0);
						cotacao.setUsuarioByIdUsuarioAprovou(acao.getUsuario());
						cotacao.setDtAprovacao(acao.getDhAcao()
								.toYearMonthDay());
						
						if(cotacao.getTpSituacaoAprovacaoDimensoes() == null 
								|| (cotacao.getTpSituacaoAprovacaoDimensoes() != null && SITUACAO_COTACAO_APROVADA.equals(cotacao.getTpSituacaoAprovacaoDimensoes().getValue()))){
							cotacao.setTpSituacao(new DomainValue(SITUACAO_COTACAO_APROVADA));
						}
						
						cotacao.setDtValidade(findNextDiaUtil());
					}
				} else {
					cotacao.setUsuarioByIdUsuarioAprovou(null);
					cotacao.setDtAprovacao(null);
					preparaCotacaoReprovada(cotacao, null,
							SITUACAO_COTACAO_REPROVADA.charAt(0));
				}
				store(cotacao);
			}
		}
		return null;
	}
	
	/**
	 * Verificar se será emitido NFT ou Conhecimento, conforme as regras de
	 * tributação e parametrização da filial, para definir se na cotação será
	 * calculado impostos de ICMS (no caso do CTRC), ou de ISS (caso de NFT).
	 * 
	 * @author Andre Valadas
	 * @param cotacao
	 */
	public void defineDocumentoCotacao(Cotacao cotacao) {
		Municipio municipioOrigem = municipioService.findById(cotacao
				.getMunicipioByIdMunicipioOrigem().getIdMunicipio());
		Municipio municipioDestino = municipioService.findById(cotacao
				.getMunicipioByIdMunicipioDestino().getIdMunicipio());
		/** Por padrão: Cotacão de CTRC(ICMS) */
		cotacao.setTpDocumentoCotacao(new DomainValue(
				ConstantesExpedicao.CONHECIMENTO_NACIONAL));

		/**
		 * Verifica Se municipios de Origem e Destino são iguais ou, Se UFs São
		 * iguais e a UF de Origem incide o imposto ISS, Em caso afirmativo
		 * segue a validação para Cotacao de uma NFT
		 */
		if (municipioOrigem.getIdMunicipio().equals(
				municipioDestino.getIdMunicipio())
				|| (municipioOrigem
						.getUnidadeFederativa()
						.getIdUnidadeFederativa()
						.equals(municipioDestino.getUnidadeFederativa()
								.getIdUnidadeFederativa()) && municipioOrigem
						.getUnidadeFederativa().getBlIncideIss())) {
			/**
			 * Verificar se a filial que atende o Município de origem pode
			 * emitir nota fiscal de transporte
			 */
			String blEmiteNFT = (String) configuracoesFacade.getValorParametro(
					cotacao.getFilialByIdFilialOrigem().getIdFilial(),
					ConstantesExpedicao.NM_PARAMETRO_IMPRIME_NF_TRANSP);
			if("S".equals(blEmiteNFT)) {
				cotacao.setTpDocumentoCotacao(new DomainValue(
						ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE));
				/**
				 * Não é possível cotar Frete e Serviços Adicionais nesta
				 * cotação, pois se configura em uma emissão de Nota-fiscal.
				 * Será necessário cotar separadamente Frete e Serviços
				 * Adicionais.
				 */
				List servicosAdicionais = cotacao.getServicoAdicionalClientes();
				if(servicosAdicionais != null && !servicosAdicionais.isEmpty()) {
					throw new BusinessException("LMS-01026");
				}
			}
		}
	}
	
	
	
	/**
	 * Esse método tem por objetivo validar os dados informados na tela Gerar
	 * Cotação.
	 * 
	 * autor Julio Cesar Fernandes Corrêa 19/01/2006
	 * 
	 * @param cotacao
	 * @return
	 */
	public void validateCotacao(Cotacao cotacao) {
		/* Regra ValidaCotacao.1 */
		verifyObrigatoriadadeDimensoes(cotacao);
		/* Regra ValidaCotacao.2 */
		validateServicosAdicionais(cotacao);

		/* Regra ValidaCotacao.8 */
		validateFreteCortesia(cotacao);

		/* Regra ValidaCotacao.5 */
		validateLimitePesoFreteCortesia(cotacao);
		/* Regra ValidaCotacao.6 */
		validatePesoCubadoFreteCortesia(cotacao);
		/* Regra ValidaCotacao.7 */
		validateEmbarqueProibidoCliente(cotacao);
		
		if(hasToValidateEmbarqueProibido(cotacao)){
			validateEmbarqueProibidoMunicipio(cotacao);
		}
		
		validateAtendimentoEmpresaTipoFrete(cotacao);		
		
		validateVolumes(cotacao);
	}
	
		
	private boolean hasToValidateEmbarqueProibido(Cotacao cotacao) {
		Cliente clienteResponsavel =  cotacao.getClienteByIdCliente();
		return	embarqueValidationService.hasToValidateEmbarqueProibido(cotacao.getServico(),	clienteResponsavel,		cotacao.getTpFrete());
	}
	

	/**
	 * Regra ValidaCotacao.1 Verificação da obrigatoriedade da informação das
	 * dimensões: - Verificar se o para o modal do frete que está sendo
	 * informado existe um parâmetro geral indicando a obrigatoriedade da
	 * informação das dimensões (ParametroGeral.nmParametroGeral =
	 * "DIMENSAO OBRIGATORIA_" + Servico.tpModal e ParametroGeral.dsConteudo =
	 * "S"). Se existir, verificar se a lista dimensões (Cotacao.dimensoes) não
	 * está vazia, se não estiver, visualizar a mensagem LMS-04061; - Verificar
	 * se para a filial que está gerando a Cotação (Filial na sessão) existe um
	 * parâmetro indicando a obrigatoriedade da informação das dimensões
	 *	  (ParametroFilial.nmParametroFilial = "DIMENSAO_OBRIGATORIA" e 
	 * ConteudoParametroFilial.vlConteudoParametroFilial = "S"). Se existir,
	 * verificar se a lista dimensões (Cotacao.dimensoes) não está vazia, se não
	 * estiver, visualizar a mensagem LMS-04061;
	 *
	 * autor Julio Cesar Fernandes Corrêa 19/01/2006
	 * 
	 * @param c
	 */
	private void verifyObrigatoriadadeDimensoes(Cotacao c) {
		List dimensoes = c.getDimensoes();
		if(dimensoes != null && !dimensoes.isEmpty()) {
			return;
		}
		String tpModal = c.getServico().getTpModal().getValue();
		if(ConstantesExpedicao.MODAL_AEREO.equalsIgnoreCase(tpModal)) {
			throw new BusinessException("LMS-04061");
		}
		String conteudoGeral = (String) configuracoesFacade
				.getValorParametro("DIMENSAO_OBRIGATORIA_"
						+ ConstantesExpedicao.MODAL_RODOVIARIO);
		if("S".equalsIgnoreCase(conteudoGeral)) {
			throw new BusinessException("LMS-04061");
		}
		String conteudoFilial = (String) configuracoesFacade.getValorParametro(
				SessionUtils.getFilialSessao().getIdFilial(),
				"DIMENSAO_OBRIGATORIA");
		if("S".equals(conteudoFilial)) {
			throw new BusinessException("LMS-04061");
		}
	}

	/**
	 * Regra ValidaCotacao.2 Validação da informação dos Serviços Adicionais: -
	 * Se o campo "Serviços Adicionais" (Cotacao.blServicosAdicionais) for
	 * informado é preciso que seja informado pelo menos um serviço adicional
	 * (Cotacao.servAdicionalDocServs), se nenhum serviço estiver informado
	 * visualizar mensagem LMS-04062; - Pelos menos um dos campos "Frete"
	 * (Cotacao.blFrete) e "Serviços Adicionais" (Cotacao.blServicosAdicionais)
	 * deve estar preenchido, caso contrário visualizar mensagem LMS-04063.
	 * 
	 * autor Julio Cesar Fernandes Corrêa 02/12/2005
	 * 
	 * @param ds
	 */
	private void validateServicosAdicionais(Cotacao c) {
		if(Boolean.TRUE.equals(c.getBlServicosAdicionais()) 
				&& (c.getServAdicionalDocServs() == null || c
						.getServAdicionalDocServs().isEmpty())) {
			throw new BusinessException("LMS-04062");
		}
		if(Boolean.FALSE.equals(c.getBlServicosAdicionais()) 
			&& Boolean.FALSE.equals(c.getBlFrete())) {
			throw new BusinessException("LMS-04063");
		}
	}

	/**
	 * Valida Frete Cortesia
	 * 
	 * @param cotacao
	 */
	private void validateFreteCortesia(Cotacao cotacao) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(
				cotacao.getLiberacoesDoctoServico(),
				ConstantesExpedicao.LIBERACAO_CALCULO_CORTESIA)) {
			return;
		}
		if (ConstantesExpedicao.CALCULO_CORTESIA.equalsIgnoreCase(cotacao
				.getTpCalculo().getValue())) {
			throw new BusinessException("LMS-04050");
		}
	}
	
	/**
	 * Regra ValidaCotacao.5 Verificação do limite de peso para Frete Cortesia:
	 * - Se foi informado que o tipo de cálculo é Cortesia (G), verificar se
	 * existe um parâmetro geral indicando o limite de peso para Frete Cortesia
	 * (PARAMETRO_GERAL.NM_PARAMETRO_GERAL = "LIMITE_PESO_CORTESIA"). Se
	 * existir, verificar se o peso real informado(Cotacao.psReal) não é maior
	 * que o valor cadastrado em PARAMETRO_GERAL.DS_CONTEUDO. Se for maior
	 * visualizar mensagem LMS-04076.
	 * 
	 * autor Julio Cesar Fernandes Corrêa 08/12/2005
	 * 
	 * @param ds
	 */
	private void validateLimitePesoFreteCortesia(Cotacao cotacao) {
		if (ConstantesExpedicao.CALCULO_CORTESIA.equalsIgnoreCase(cotacao
				.getTpCalculo().getValue())) {
			BigDecimal limite = (BigDecimal) configuracoesFacade
					.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_PESO_CORTESIA);
			if(limite != null) {
				BigDecimal psReal = cotacao.getPsReal();
				if(CompareUtils.gt(psReal, limite)) {
					throw new BusinessException("LMS-04076");
				}
			}
		}
	}

	/**
	 * Esse metodo tem por objetivo verificar se o Peso Cubado para o frete
	 * Cortesia é menor ou igual a o do pré-configurado no sistema.
	 * 
	 * autor Giuliano Costa 27/07/2006
	 * 
	 * @param c
	 * @return
	 */
	private void validatePesoCubadoFreteCortesia(Cotacao cotacao){
		DomainValue tpCalculo = cotacao.getTpCalculo();

		if(ConstantesExpedicao.CALCULO_CORTESIA.equals(tpCalculo.getValue())){//Cortesia
			BigDecimal limitePsCubado = (BigDecimal) configuracoesFacade
					.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_PESO_CUBADO_CORTESIA);
			BigDecimal psCubado = cotacao.getPsCubado();

			if((psCubado == null) || CompareUtils.gt(psCubado, limitePsCubado) ) {
				throw new BusinessException("LMS-01107");
			}
		}
	}

	/**
	 * Valida Embarque Proibido Cliente
	 * 
	 * @param cotacao
	 */
	private void validateEmbarqueProibidoCliente(Cotacao cotacao) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(
				cotacao.getLiberacoesDoctoServico(),
				ConstantesExpedicao.LIBERACAO_CLIENTE_EMBARQUE_PROIBIDO)) {
			return;
		}
		
		//LMS-3597 cliente devedor recebe o responsável pelo frete
		Cliente clienteDevedor = cotacao.getClienteByIdCliente();
		if (cotacao.getClienteByIdCliente() != null) {
			Integer nrRowCountClienteEmbarqueProibido = proibidoEmbarqueService
					.getRowCountProibidoByIdCliente(clienteDevedor.getIdCliente());
			if (nrRowCountClienteEmbarqueProibido.intValue() > 0) {
				throw new BusinessException("LMS-04053");
			}
		}
	}

	/**
	 * Valida Embarque Proibido Municipio
	 * 
	 * @param cotacao
	 */
	private void validateEmbarqueProibidoMunicipio(Cotacao cotacao) {
		if (ConhecimentoUtils.verifyLiberacaoEmbarque(
				cotacao.getLiberacoesDoctoServico(),
				ConstantesExpedicao.LIBERACAO_MUNICIPIO_EMBARQUE_PROIBIDO)) {
			return;
		}
		Municipio municipioEntrega = cotacao.getMunicipioByIdMunicipioEntrega();
        if( municipioEntrega == null ){
            municipioEntrega = cotacao.getMunicipioByIdMunicipioDestino();
        }
		if (municipioFilialService.getRowCountByIdMunicipioBlRecebeColeta(
				municipioEntrega.getIdMunicipio(), Boolean.FALSE).intValue() > 0) {
			Filial filialOrigem = cotacao.getFilialByIdFilialOrigem();
			Filial filialDestino = cotacao.getFilialByIdFilialDestino();
			
			if(filialOrigem.getIdFilial() == filialDestino.getIdFilial()){
				return;
			}
			
			if(cotacao.getTpFrete().getValue().equalsIgnoreCase(ConstantesExpedicao.TP_FRETE_FOB) && 
					liberacaoEmbarqueService.checkLiberacaoFOBDirigido(cotacao)){
				return;
			}
			
			//LMS-3597 cliente devedor recebe o responsável pelo frete
			Cliente clienteDevedor = cotacao.getClienteByIdCliente();
			if(clienteDevedor == null) {
				throw new BusinessException("LMS-04054");
			}
			Integer nrRowCountMunicipioEmbarqueProibido = liberacaoEmbarqueService
					.getRowCountByIdMunicipioIdClienteModal(
							municipioEntrega.getIdMunicipio(),
							clienteDevedor.getIdCliente(), cotacao.getServico()
									.getTpModal().getValue());
			
			if (nrRowCountMunicipioEmbarqueProibido.intValue() < 1) {
				Integer nrRowCountMunicipioEmbarqueProibidoModalNull = liberacaoEmbarqueService
						.getRowCountByIdMunicipioIdClienteModal(
								municipioEntrega.getIdMunicipio(),
								clienteDevedor.getIdCliente(), null);
			
				if ( nrRowCountMunicipioEmbarqueProibidoModalNull.intValue() < 1 ) {
					ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), "bloqCredEmbProib", false, true);

					 if (conteudoParametroFilial != null && "S".equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
						 throw new BusinessException("LMS-04508");
					 }
					 
				throw new BusinessException("LMS-04054");
			}
		}
	}
		
	}

	/**
	 * Esse método tem por objetivo chamar os métodos de cálculo do frete e dos
	 * serviços adicionais, efetuando o cálculo do valor da Cotação.
	 */
	public void findCalculoCotacao(Cotacao cotacao, CalculoFrete calculoFrete) {
		/* Regra CalculaCotacao.1 */
		try{
			calculaPesoCubado(cotacao, calculoFrete);
			calculaFreteCotacao(cotacao, calculoFrete);
			CalculoFreteUtils.copyResult(cotacao, calculoFrete);
		}catch(RuntimeException e){
			cotacao.setDivisaoCliente(null);
			throw e;
		}
	}

	public void findCalculoCotacaoByEdicao(Cotacao cotacao,
			CalculoFrete calculoFrete) {
		calculoFrete.setBlEditaCotacao(Boolean.TRUE);
		calculaPesoCubado(cotacao, calculoFrete);
		calculaFreteCotacao(cotacao, calculoFrete);
		CalculoFreteUtils.copyResult(cotacao, calculoFrete);
		calculoFrete.setBlEditaCotacao(Boolean.FALSE);
	}

	/**
	 * Esse método tem por objetivo chamar os métodos de cálculo do frete e dos
	 * serviços adicionais, efetuando o recálculo do valor da Cotação baseado
	 * numa nova parametrização.
	 */
	public void findRecalculoCotacao(Cotacao cotacao, CalculoFrete calculoFrete) {
		try{
			documentoServicoFacade.executeCalculoCotacao(calculoFrete);
			CalculoFreteUtils.copyResult(cotacao, calculoFrete);
		}catch(BusinessException be){
			throw be;
		}
	}

	
	public ParametroCliente getParametroClientePadraoCotacao(ParametroCliente parametroBase){
		if (parametroBase == null){
			parametroBase = ParametroClienteUtils.getParametroClientePadrao();
		}
		ParametroCliente parametroCliente = parametroBase;

		Object conteudoParametro = parametroGeralService.findConteudoByNomeParametro("PercentualCobrancaReentrega", false);
		if (conteudoParametro != null){
			parametroCliente.setPcCobrancaReentrega((BigDecimal) conteudoParametro);
		}
		
		conteudoParametro = parametroGeralService.findConteudoByNomeParametro("PercentualCobrancaDevolucao", false);
		if (conteudoParametro != null){
			parametroCliente.setPcCobrancaDevolucoes((BigDecimal) conteudoParametro);
		}
		
		return parametroCliente;

	}
	
	/**
	 * Calculo do frete da cotação após confirmação dos dados e rotinas de
	 * validação. Esse método é executado uma única vez, sendo que após os
	 * ajustes de parametrização deve-se chamar outro método.
	 */
	private void calculaFreteCotacao(Cotacao cotacao, CalculoFrete calculoFrete) {
		if(!BooleanUtils.isTrue(calculoFrete.getRecalculoFrete())){
			if(calculoFrete.getParametroCliente() == null) {
				ParametroCliente parametroCliente = getParametroClientePadraoCotacao(null);
				parametroCliente.setTabelaPreco(calculoFrete.getTabelaPreco());
				calculoFrete.setParametroCliente(parametroCliente);
			}
		}
		String cdParcelaPreco = null;
		ParcelaServico freteParcela = null;
		List<ParcelaServico>list = calculoFrete.getParcelasGerais();
		if( list != null && list.size() > 0 ){
			calculoFreteService.findPesoReferenciaExterno(calculoFrete);
			for( ParcelaServico p: list ){
				cdParcelaPreco = p.getParcelaPreco().getCdParcelaPreco();
				freteParcela = null;
				
				if( !calculoFrete.getParcelas().contains(p) ){
					calculoFrete.getParcelas().add(p);
				}
				if( ConstantesExpedicao.CD_FRETE_PESO.equals(cdParcelaPreco) ){
					freteParcela = calculoParcelaFreteService.findParcelaFretePeso(calculoFrete);
				} else if (ConstantesExpedicao.CD_FRETE_VALOR.equals(cdParcelaPreco)) {
					freteParcela = calculoParcelaFreteService.findParcelaFreteValor(calculoFrete);
				}else if( ConstantesExpedicao.CD_GRIS.equals(cdParcelaPreco) ){
					freteParcela = calculoParcelaFreteService.findParcelaGRIS(calculoFrete);
				} else if (ConstantesExpedicao.CD_PEDAGIO.equals(cdParcelaPreco)) {
					freteParcela = calculoParcelaFreteService.findParcelaPedagio(calculoFrete);
				}else if( ConstantesExpedicao.CD_TRT.equals(cdParcelaPreco) ){
					freteParcela = calculoParcelaFreteService.findParcelaTRT(calculoFrete);
				}else if( ConstantesExpedicao.CD_EMEX.equals(cdParcelaPreco) ){
					freteParcela = calculoParcelaFreteService.findParcelaEMEX(calculoFrete);
				}else{
					//Taxas 
					Matcher m = Pattern.compile("coleta",
							Pattern.CASE_INSENSITIVE).matcher(cdParcelaPreco);
					if(m.find()) {
						m = Pattern.compile("interior",
								Pattern.CASE_INSENSITIVE).matcher(cdParcelaPreco);
						Boolean blColetaInterior = m.find(); 

						m = Pattern.compile("emergencia",
								Pattern.CASE_INSENSITIVE).matcher(cdParcelaPreco);
						Boolean blColetaEmergencia = m.find();

						freteParcela = calculoParcelaFreteService.findParcelaTaxaColetaEntrega(calculoFrete,"Coleta", blColetaInterior,blColetaEmergencia);
					}else{			
						//Taxa Entrega
						m = Pattern.compile("entrega", Pattern.CASE_INSENSITIVE).matcher(cdParcelaPreco);
						if(m.find()) {
							m = Pattern.compile("interior",
									Pattern.CASE_INSENSITIVE).matcher(
									cdParcelaPreco);
							Boolean blEntregaInterior = m.find(); 

							m = Pattern.compile("emergencia",Pattern.CASE_INSENSITIVE).matcher(cdParcelaPreco);
							Boolean blEntregaEmergencia = m.find();
							freteParcela = calculoParcelaFreteService.findParcelaTaxaColetaEntrega(calculoFrete,"Entrega", blEntregaInterior,blEntregaEmergencia);
						} else if (ConstantesExpedicao.CD_TAXA_TERRESTRE.equals(cdParcelaPreco)) {
							freteParcela = calculoParcelaFreteService.findParcelaTaxaTerrestre(calculoFrete);
						} else if (ConstantesExpedicao.CD_TAXA_COMBUSTIVEL.equals(cdParcelaPreco)) {
							freteParcela = calculoParcelaFreteService.findParcelaTaxaCombustivel(calculoFrete);
						} else if (ConstantesExpedicao.CD_TAXA_SUFRAMA.equals(cdParcelaPreco)) {
							freteParcela = calculoParcelaFreteService.findParcelaTaxaSuframa(calculoFrete);
						}
					}
				}

				if( freteParcela != null ){
					p.setVlBrutoParcela(freteParcela.getVlBrutoParcela());
					p.setVlUnitarioParcela(freteParcela.getVlUnitarioParcela());
				}
			}
		}

		if (calculoFrete.getGeneralidades() != null
				&& calculoFrete.getGeneralidades().size() > 0) {
			List<ParcelaServico> generalidades = calculoParcelaFreteService
					.executeCalculoParcelasGeneralidade(calculoFrete);
			for( ParcelaServico p : generalidades ){
				cdParcelaPreco = p.getParcelaPreco().getCdParcelaPreco();
				freteParcela = null;
				freteParcela = calculoFrete.getGeneralidade(cdParcelaPreco);
				if( freteParcela != null ){
					p.setVlBrutoParcela(freteParcela.getVlBrutoParcela());
				}
			}
		}
		
		if (calculoFrete.getServicosAdicionais() != null
				&& calculoFrete.getServicosAdicionais().size() > 0) {
			List<ParcelaServicoAdicional> servicosAdicionais = calculoParcelaFreteService
					.findParcelasServicoAdicional(calculoFrete);
			for( ParcelaServico p : servicosAdicionais ){
				cdParcelaPreco = p.getParcelaPreco().getCdParcelaPreco();
				freteParcela = null;
				freteParcela = calculoFrete.getServicoAdicional(cdParcelaPreco);
				if( freteParcela != null ){
					p.setVlBrutoParcela(freteParcela.getVlBrutoParcela());
				}
			}
		}
		documentoServicoFacade.executeCalculoCotacao(calculoFrete);
		if(calculoFrete.getParametroCliente() == null) {
			ParametroCliente parametroCliente = ParametroClienteUtils
					.getParametroClientePadrao();
			parametroCliente.setTabelaPreco(calculoFrete.getTabelaPreco());
			calculoFrete.setParametroCliente(parametroCliente);
		}
		
		calculoFrete.getParametroCliente().setTabelaPreco(
				calculoFrete.getTabelaPreco());
	}

	private ParametroCliente findParametroClienteByCalculoFrete(
			CalculoFrete calculoFrete) {
		if(calculoFrete.getIdDivisaoCliente() != null
				&& BooleanUtils.isFalse(calculoFrete
						.getBlCalculoFreteTabelaCheia())
				&& ClienteUtils.isParametroClienteEspecial(calculoFrete
						.getClienteBase().getTpCliente().getValue())) {
			ParametroCliente parametroCliente;
			parametroCliente = calculoFreteService.findParametroCliente(
					calculoFrete.getIdDivisaoCliente(),
					calculoFrete.getIdServico(),
					calculoFrete.getRestricaoRotaOrigem(),
					calculoFrete.getRestricaoRotaDestino());
			String[] tpsLocalizacaoCapital = StringUtils
					.split((String) configuracoesFacade
							.getValorParametro("IDTipoLocalizacaoCapital"), ";");
			Long idTpLocalizacaoCapital = Long
					.valueOf(tpsLocalizacaoCapital[0]);
			Long idTpLocalizacaoGrandeCapital = Long
					.valueOf(tpsLocalizacaoCapital[1]);
			
					/**
			 * se tipo localizacao do cliente for c ou gc e se nao encontrou
			 * rota ou tp localização rota for null for igual a null
			 * 
			 * se cliente é da capital, procura na grande capital se cliente e
			 * da grande capital procura no interior
					 */
			if (calculoFrete.getRestricaoRotaDestino().getIdTipoLocalizacao() != null
					&& ArrayUtils.contains(tpsLocalizacaoCapital, calculoFrete
							.getRestricaoRotaDestino().getIdTipoLocalizacao()
							.toString())
					&& (parametroCliente == null || parametroCliente
							.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino() == null)) {
				Long idNovaLocalizacao = 0L;
				if (CompareUtils.eq(calculoFrete.getRestricaoRotaDestino()
						.getIdTipoLocalizacao(), idTpLocalizacaoCapital)) {
					idNovaLocalizacao =  idTpLocalizacaoGrandeCapital;
				} else if (CompareUtils.eq(calculoFrete
						.getRestricaoRotaDestino().getIdTipoLocalizacao(),
						idTpLocalizacaoGrandeCapital)) {
					idNovaLocalizacao = LongUtils.getLong(configuracoesFacade
							.getValorParametro("IDTipoLocalizacaoInterior"));
						}
			
				calculoFrete.getRestricaoRotaDestino().setIdTipoLocalizacao(
						idNovaLocalizacao);
			
				parametroCliente = calculoFreteService.findParametroCliente(
						calculoFrete.getIdDivisaoCliente(),
						calculoFrete.getIdServico(),
						calculoFrete.getRestricaoRotaOrigem(),
						calculoFrete.getRestricaoRotaDestino());
			}
			return parametroCliente;
		}
		return null;
	}
	
	/**
	 * Regra CalculaCotacao.1 Calculo do peso aforado, baseado numa lista de
	 * dimensões e no modal: - Somar as Multiplicações dos valores informados
	 * (Altura, Largura, Comprimento e Quantidade) nas dimensões; - Se o modal
	 * do serviço que foi selecionado no campo Serviço for Rodoviário (R)
	 * dividir o valor obtido PARAMETRO_GERAL.DS_CONTEUDO para o fator peso x
	 * metragem cúbica (PARAMETRO_GERAL.NM_PARAMETRO_GERAL =
	 * "PESO_METRAGEM_ CUBICA_RODOVIARIO"). - Se o modal do serviço que foi
	 * selecionado no campo Serviço for Aéreo (A) dividir o valor obtido
	 * PARAMETRO_GERAL.DS_CONTEUDO para o fator peso x metragem cúbica
	 * (PARAMETRO_GERAL.NM_PARAMETRO_GERAL = "PESO_METRAGEM_ CUBICA_AEREO"). - O
	 * resultado das operações acima será o peso aforado.
	 * 
	 * autor Julio Cesar Fernandes Corrêa 20/01/2006
	 * 
	 * @param cotacao
	 */
	private void calculaPesoCubado(Cotacao cotacao, CalculoFrete calculoFrete) {
		BigDecimal psCubado = conhecimentoService.calculaPsAforado(cotacao
				.getServico().getTpModal().getValue(), cotacao.getDimensoes(),
				calculoFrete);
		if(psCubado != null) {
			cotacao.setPsCubado(psCubado);
			calculoFrete.setPsCubadoInformado(psCubado);
		}
		BigDecimal metragemCubicaCotacao = this.calculaMetragemCubicaCotacao(cotacao.getDimensoes());
		calculoFrete.setMetragemCubicaCotacao(metragemCubicaCotacao);
	}
	
	public BigDecimal calculaMetragemCubicaCotacao(List dimensoes) {
		BigDecimal metragemCubicaCotacao = BigDecimalUtils.ZERO;
		if (dimensoes != null && !dimensoes.isEmpty()) {
			double somaTotal = 0.0;
			for (Iterator iter = dimensoes.iterator(); iter.hasNext();) {
				Dimensao d = (Dimensao) iter.next();
				somaTotal += d.getNrAltura().doubleValue()
						* d.getNrLargura().doubleValue()
						* d.getNrComprimento().doubleValue()
						* d.getNrQuantidade().doubleValue();
			}
			metragemCubicaCotacao = BigDecimalUtils.divide(new BigDecimal(somaTotal), new BigDecimal("1000000"));
		}
		return metragemCubicaCotacao;
	}	
	

	/**
	 * Regra ValidaCotacao 4.1 - Situações que o cliente possui Fob Dirigido.
	 * 
	 * @param cotacao
	 * @return <ClienteBaseCalculo>
	 */
	private Cliente getClienteBaseCalculo(Cotacao cotacao) {
		Cliente clienteRemetente = cotacao.getClienteByIdClienteSolicitou();
		Cliente clienteResponsavel = cotacao.getClienteByIdCliente();
		Cliente clienteDestino = cotacao.getClienteByIdClienteDestino();
		
		if (ClienteUtils.hasCliente(clienteRemetente) && ClienteUtils.hasCliente(clienteResponsavel)) {
			if (ConstantesExpedicao.TP_FRETE_FOB.equals(cotacao.getTpFrete().getValue()) &&	
					ClienteUtils.hasCliente(clienteDestino) && clienteResponsavel.getIdCliente().equals(clienteDestino.getIdCliente())) {
				Cliente clienteMatrizOrResponsavel = ClienteUtils.getClienteTpClienteFilialClienteEspecial(clienteResponsavel);
				if (ClienteUtils.isParametroClienteEspecial(clienteRemetente.getTpCliente().getValue()) &&
						ClienteUtils.isClienteFobDirigidoRodoOrAereo(cotacao.getServico().getTpModal().getValue(), clienteRemetente.getBlFobDirigido(), clienteRemetente.getBlFobDirigidoAereo())) {
					
					List<PromotorCliente> listPromotorCliente = promotorClienteService.findByIdClienteAndTpModalAndDtAtual(clienteResponsavel.getIdCliente(), cotacao.getServico().getTpModal().getValue(), JTDateTimeUtils.getDataHoraAtual());
					List<TabelaPreco> listTabelaPreco = tabelaPrecoService.findTabelaPrecoByIdCliente(clienteMatrizOrResponsavel.getIdCliente(), ConstantesTabelaPrecos.TP_TABELA_PRECO_REFERENCIAL_TNT, ConstantesTabelaPrecos.SUB_TP_TABELA_FOB, JTDateTimeUtils.getDataHoraAtual());
					
					if ((ClienteUtils.isClienteEventualOrPotencial(clienteResponsavel.getTpCliente().getValue()) || 
							(ClienteUtils.isParametroClienteEspecial(clienteResponsavel.getTpCliente().getValue()) && !CollectionUtils.isEmpty(listTabelaPreco))) 
							&& !CollectionUtils.isEmpty(listPromotorCliente)) {
						return clienteResponsavel;
					} else {
						ParametroCliente paramCliente = getParametroCliente(cotacao, clienteMatrizOrResponsavel);
						List<TabelaPreco> listTabelaPrecoD = tabelaPrecoService.findTabelaPrecoByIdCliente(clienteMatrizOrResponsavel.getIdCliente(), ConstantesTabelaPrecos.TP_TABELA_PRECO_DIFERENCIADA, null, JTDateTimeUtils.getDataHoraAtual());
						
						if (CollectionUtils.isEmpty(listTabelaPrecoD) && paramCliente == null) {
							return clienteRemetente;
						}
					}
				} 
			} else if (ConstantesExpedicao.TP_FRETE_CIF.equals(cotacao.getTpFrete().getValue())) {
				if (!clienteResponsavel.getIdCliente().equals(clienteRemetente.getIdCliente())) {
					if (ClienteUtils.isParametroClienteEspecial(clienteResponsavel.getTpCliente().getValue()) &&
							ClienteUtils.isClienteFobDirigidoRodoOrAereo(cotacao.getServico().getTpModal().getValue(), clienteResponsavel.getBlFobDirigido(), clienteResponsavel.getBlFobDirigidoAereo())) {
						if (ClienteUtils.isParametroClienteEspecial(clienteRemetente.getTpCliente().getValue())) {
							return clienteRemetente;
						}
					}
				}
			}
		}

		return clienteResponsavel;
	}
	
	private ParametroCliente getParametroCliente(Cotacao cotacao, Cliente cliente) {
		ParametroCliente paramCliente = null;
		Long idCliente = cliente.getIdCliente();
		List<DivisaoCliente> l = divisaoClienteService.findByIdClienteAndTpModal(idCliente, cotacao.getServico().getTpModal().getValue());
		
		if (!CollectionUtils.isEmpty(l)) {
			for (DivisaoCliente divisaoCliente : l) {
				CalculoFrete calculoFrete = populateCalculoFrete(cotacao, divisaoCliente);
				Long idDivisaoCliente = divisaoCliente.getIdDivisaoCliente();
				paramCliente = calculoFreteService.findParametroCliente(
						idDivisaoCliente,
						cotacao.getServico().getIdServico(),
						calculoFrete.getRestricaoRotaOrigem(),
						calculoFrete.getRestricaoRotaDestino());
				
				if (paramCliente != null ) {
					break;
				}
			}
		}
		
		return paramCliente;
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
	
	private CalculoFrete populateCalculoFrete(Cotacao cotacao, DivisaoCliente divisaoCliente) {
		TabelaPreco tp = tabelaPrecoService.findTabelaPrecoByIdDivisaoCliente(divisaoCliente.getIdDivisaoCliente(), cotacao.getServico().getIdServico());
		
		CalculoFrete calculoFrete = new CalculoFrete();
		calculoFrete.setTpCalculo(cotacao.getTpCalculo().getValue());
		calculoFrete.setTpConhecimento(cotacao.getTpDocumentoCotacao().getValue());
		calculoFrete.setIdServico(cotacao.getServico().getIdServico());
		calculoFrete.setNrCepColeta(cotacao.getMunicipioByIdMunicipioOrigem().getNrCep());
		calculoFrete.setNrCepEntrega(cotacao.getMunicipioByIdMunicipioEntrega().getNrCep());
		calculoFrete.setRestricaoRotaOrigem(new RestricaoRota());
		calculoFrete.getRestricaoRotaOrigem().setIdMunicipio(cotacao.getMunicipioByIdMunicipioOrigem().getIdMunicipio());
		calculoFrete.getRestricaoRotaOrigem().setIdFilial(cotacao.getFilialByIdFilialOrigem().getIdFilial());
		calculoFrete.getRestricaoRotaOrigem().setIdGrupoRegiao(getIdGrupoRegiao(tp, cotacao.getMunicipioByIdMunicipioOrigem()));
		calculoFrete.getRestricaoRotaOrigem().setIdAeroporto(getAeroporto(cotacao.getFilialByIdFilialOrigem(), cotacao.getServico().getTpModal().getValue()));
		
		calculoFrete.setRestricaoRotaDestino(new RestricaoRota());
		calculoFrete.getRestricaoRotaDestino().setIdMunicipio(cotacao.getMunicipioByIdMunicipioEntrega().getIdMunicipio());
		calculoFrete.getRestricaoRotaDestino().setIdFilial(cotacao.getFilialByIdFilialDestino().getIdFilial());
		calculoFrete.getRestricaoRotaDestino().setIdGrupoRegiao(getIdGrupoRegiao(tp, cotacao.getMunicipioByIdMunicipioDestino()));
		calculoFrete.getRestricaoRotaDestino().setIdAeroporto(getAeroporto(cotacao.getFilialByIdFilialDestino(), cotacao.getServico().getTpModal().getValue()));
		
		calculoFreteService.setParametrosCalculoCommon(calculoFrete);
		return calculoFrete;
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
	
	private CalculoFrete newCalculoFrete(DomainValue tpDocumentoCotacao){
		CalculoFrete result = ExpedicaoUtils.newCalculoFrete(tpDocumentoCotacao);
		ExpedicaoUtils.setCalculoFreteInSession(result);
		return result;
	}
	
	public CalculoFrete executeConfigureCalculoFrete(Cotacao cotacao,
			Boolean novoCalculo) {
		DomainValue tpDocumentoCotacao = cotacao.getTpDocumentoCotacao();
		CalculoFrete calculoFrete = ExpedicaoUtils
				.getCalculoFreteInSession(false);
		if( novoCalculo ){
			calculoFrete = null;
		}
		if( calculoFrete == null ){
			calculoFrete = newCalculoFrete(tpDocumentoCotacao);
		}
		calculoFrete.setServAdicionalDoctoServico(cotacao
				.getServAdicionalDocServs());

		Boolean calculaSA = calculoFrete.getServAdicionalDoctoServico() != null
				&& calculoFrete.getServAdicionalDoctoServico().size() > 0;
		cotacao.setBlServicosAdicionais(calculaSA);
		calculoFrete.setBlCalculaServicosAdicionais(calculaSA);

		calculoFrete.setTabelaPrecoRecalculo(calculoFrete.getTabelaPreco());

		calculoFrete.setBlCotacao(Boolean.TRUE);
		String tpCalculo = cotacao.getTpCalculo().getValue();
		calculoFrete.setIdCotacao(cotacao.getIdCotacao());

		calculoFrete.setBlCalculaParcelas(cotacao.getBlFrete());
		calculoFrete.setTpConhecimento(ConstantesExpedicao.CONHECIMENTO_NORMAL);

		//FIXME: Mudar maneira de setar o TpDocumentoServico
		Conhecimento doctoServico = new Conhecimento();
		doctoServico.setTpDocumentoServico(tpDocumentoCotacao);
		doctoServico.setTpDoctoServico(tpDocumentoCotacao);
		/** Filial */
		doctoServico.setFilialByIdFilialOrigem(SessionUtils.getFilialSessao());
		doctoServico.setNaturezaProduto(cotacao.getNaturezaProduto());
		doctoServico.setClienteByIdClienteRemetente(cotacao.getClienteByIdClienteSolicitou());
		
		calculoFrete.setDoctoServico(doctoServico);

		calculoFrete.setTpCalculo(tpCalculo);
		calculoFrete.setPsRealInformado(cotacao.getPsReal());
		calculoFrete.setPsCubadoInformado(cotacao.getPsCubado());
		calculoFrete.setClienteBase(this.getClienteBaseCalculo(cotacao));
		calculoFrete.setVlMercadoria(cotacao.getVlMercadoria());
		calculoFrete.setIdServico(cotacao.getServico().getIdServico());
		calculoFrete.setServAdicionalDoctoServico(cotacao
				.getServAdicionalDocServs());
		if(cotacao.getDivisaoCliente() != null) {
			calculoFrete.setIdDivisaoCliente(cotacao.getDivisaoCliente()
					.getIdDivisaoCliente());
		}
		
		DateTime dhGeracao = new DateTime();
		if( cotacao.getDtGeracaoCotacao() != null ){
			dhGeracao = cotacao.getDtGeracaoCotacao().toDateTimeAtCurrentTime();
		}
		calculoFrete.setDhEmissaoDocRecalculo(dhGeracao);
		
		calculoFrete.setTpAbrangencia(cotacao.getServico().getTpAbrangencia()
				.getValue());
		calculoFrete.setTpModal(cotacao.getServico().getTpModal().getValue());
		calculoFrete.setTpFrete(cotacao.getTpFrete().getValue());
		if(cotacao.getProdutoEspecifico() != null) {
			calculoFrete.setIdProdutoEspecifico(cotacao.getProdutoEspecifico()
					.getIdProdutoEspecifico());
		}
		calculoFrete.setBlColetaEmergencia(cotacao.getBlColetaEmergencia());
		calculoFrete.setBlEntregaEmergencia(cotacao.getBlEntregaEmergencia());
		
		DoctoServicoDadosCliente dadosCliente = cotacao.getDadosCliente();
		if( dadosCliente == null ){
			dadosCliente = new DoctoServicoDadosCliente();
		}
		EnderecoPessoa enderecoPessoa = enderecoPessoaService
				.findEnderecoPessoaPadrao(cotacao.getFilialByIdFilialOrigem()
						.getIdFilial());
		dadosCliente.setIdUfFilialRemetente(enderecoPessoa.getMunicipio()
				.getUnidadeFederativa().getIdUnidadeFederativa());
		dadosCliente.setBlMercadoriaExportacao(cotacao
				.getBlMercadoriaExportacao());
		calculoFrete.setDadosCliente(dadosCliente);

		/*Devedor*/
		List<DevedorDocServ> devedor = new ArrayList<DevedorDocServ>();
		DevedorDocServ dds = new DevedorDocServ();
		dds.setCliente(cotacao.getClienteByIdCliente());
		devedor.add(dds);
		calculoFrete.getDoctoServico().setDevedorDocServs(devedor);
		
		/** Seta Dados Cliente */
		validateDadosCliente(cotacao, calculoFrete);

		Long idTarifa = findTarifaPreco(cotacao);
		if( idTarifa != null){
			TarifaPreco tp = new TarifaPreco();
			tp.setIdTarifaPreco(idTarifa);
			calculoFrete.setTarifaPreco(tp);
		}
		calculoFrete.setIdTarifa(idTarifa);

		if (cotacao.getQtVolumes() != null) {
			calculoFrete.setQtVolumes(Integer.valueOf(cotacao.getQtVolumes().toString()));
		}

		return calculoFrete;
	}

	public Boolean findAlteracaoCotacao(){
		CalculoFrete calculoFrete = ExpedicaoUtils
				.getCalculoFreteInSession(false);
		if( calculoFrete != null ){
			Boolean recalculo = Boolean.TRUE.equals(calculoFrete
					.getBlRecalculoCotacao())
					|| Boolean.TRUE.equals(calculoFrete.getRecalculoFrete());
			calculoFrete.setRecalculoFrete(recalculo);
			calculoFrete.setBlRecalculoCotacao(recalculo);
			calculoFrete.setBlCalculaServicosAdicionais(recalculo);
			return recalculo;
		}else{
			return true;
		}
		
	}

	/**
	 * Busca a tarifa preço baseada no município de coleta, município de entrega
	 * e no serviço.
	 * 
	 * autor Julio Cesar Fernandes Corrêa 03/01/2006
	 * 
	 * @param ds
	 * @return idTarifaPreco ID da tarifa preço
	 */
	private Long findTarifaPreco(Cotacao cotacao) {
		Long result = null;
		
		TarifaPreco tarifaPreco = null;
		CalculoFrete calculoFrete = ExpedicaoUtils
				.getCalculoFreteInSession(false);
		if (BooleanUtils.isTrue(calculoFrete.getRecalculoFrete())) {
			String tpTabelaPreco = calculoFrete.getTabelaPreco()
					.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();
			String sbTpTabelaPreco = calculoFrete.getTabelaPreco()
					.getSubtipoTabelaPreco().getTpSubtipoTabelaPreco();
			if ("D".equals(tpTabelaPreco) || "@".equals(tpTabelaPreco)
					|| "B".equals(tpTabelaPreco) || "I".equals(tpTabelaPreco)
					|| "F".equals(sbTpTabelaPreco)
					|| "P".equals(sbTpTabelaPreco)) {
		
				/*
				 * Adiciona informarções a rota de origem para a consulta da
				 * TarifaPreco
				 */
				RestricaoRota restricaoRotaOrigem = calculoFrete
						.getRestricaoRotaOrigem();
				restricaoRotaOrigem.setSbTabelaPreco(sbTpTabelaPreco);

				String dsLocalComercial = tipoLocalizacaoMunicipioService
						.findById(
								restricaoRotaOrigem
										.getIdTipoLocalizacaoComercial())
						.getDsTipoLocalizacaoMunicipio().getValue();

				Boolean isLocalComercialCapital = "Capital"
						.equals(dsLocalComercial);
				restricaoRotaOrigem
						.setIsLocalComercialCapital(isLocalComercialCapital);

				/* Grupo Regiao Origem */
				GrupoRegiao grupoRegiaoOrigem = calculoFrete
						.getGrupoRegiaoOrigem();
				if (grupoRegiaoOrigem != null) {
					restricaoRotaOrigem.setIdGrupoRegiao(grupoRegiaoOrigem
							.getIdGrupoRegiao());
				}

				/*
				 * Adiciona informações a rota de destino para a consulta da
				 * TarifaPreco
				 */
				RestricaoRota restricaoRotaDestino = calculoFrete
						.getRestricaoRotaDestino();
				restricaoRotaDestino.setSbTabelaPreco(sbTpTabelaPreco);

				dsLocalComercial = tipoLocalizacaoMunicipioService
						.findById(
								restricaoRotaDestino
										.getIdTipoLocalizacaoComercial())
						.getDsTipoLocalizacaoMunicipio().getValue();

				isLocalComercialCapital = "Capital".equals(dsLocalComercial);
				restricaoRotaDestino
						.setIsLocalComercialCapital(isLocalComercialCapital);

				/* Grupo Regiao Destino */
				GrupoRegiao grupoRegiaoDestino = calculoFrete
						.getGrupoRegiaoDestino();
				if (grupoRegiaoDestino != null) {
					restricaoRotaDestino.setIdGrupoRegiao(grupoRegiaoDestino
							.getIdGrupoRegiao());
				}

				/*
				 * Correção para obter a tarifa correta através da Rota. ZONA -
				 * PAIS - UNIDADE FEDERATIVA - GRUPO REGIÃO - FILIAL - MUNICIPIO
				 * Referente ao JIRA LMS-416
				 */
				TarifaPrecoRota tarifaPrecoRota = calculoFreteService.getRestricaoRotaService()
						.findTarifaPrecoRota(calculoFrete);
				if( tarifaPrecoRota != null  ){
					tarifaPreco = tarifaPrecoRota.getTarifaPreco();
				}

				if (tarifaPreco != null) {
					result = tarifaPreco.getIdTarifaPreco();
				}
			} else {
				Long idMunicipioOrigem = calculoFrete.getRestricaoRotaOrigem()
						.getIdMunicipio();
				Long idMunicipioDestino = calculoFrete
						.getRestricaoRotaDestino().getIdMunicipio();
		Long idServico = cotacao.getServico().getIdServico(); 
		if( calculoFrete.getTabelaPreco() != null ){
			/*CQPRO00025223 - Vendas Brasil*/
					String paramDtImplantacao = (String) configuracoesFacade
							.getValorParametro("IMPLANTACAO_VENDA_BRASIL");
			if(paramDtImplantacao == null){
						throw new BusinessException(
								"Parametro IMPLANTACAO_VENDA_BRASIL não encontrado");
			}
			
					YearMonthDay dtImplantacaoVendasBrasil = JTDateTimeUtils
							.convertDataStringToYearMonthDay(paramDtImplantacao);
			if(dtImplantacaoVendasBrasil != null){
					
						/*
						 * Se a tabela utilizada possuir a data de vigência
						 * inicial maior ou igual a data no parâmetro geral
						 * IMPLANTACAO_VENDA_BRASIL deverá ser utilizado a
						 * tarifa atual senão a tarifa antiga
						 */
						TarifaPreco[] tarifas = mcdService.findTarifasPrecoLMS(
								idMunicipioOrigem, idMunicipioDestino,
								idServico);
						if (JTDateTimeUtils.comparaData(calculoFrete
								.getTabelaPreco().getDtVigenciaInicial(),
								dtImplantacaoVendasBrasil) >= 0) {
					result = tarifas[0].getIdTarifaPreco();
				}else{
					result = tarifas[1].getIdTarifaPreco();
				}
			}else{
						result = mcdService.findTarifaMunicipios(cotacao
								.getMunicipioByIdMunicipioOrigem()
								.getIdMunicipio(), cotacao
								.getMunicipioByIdMunicipioDestino()
								.getIdMunicipio(), cotacao.getServico()
								.getIdServico());
			}
		}
			}
		}
		return result;
	}

	public java.io.Serializable storeCotacao(Cotacao cotacao,
			CalculoFrete calculoFrete) {
		if( "E".equals(cotacao.getTpSituacao())){
			throw new BusinessException("LMS-01198");
		}
		this.flush();
		prepareDimensoes(cotacao);
		prepareParcelas(cotacao);
		prepareServicos(cotacao);
		prepareServicosAdicionaisCliente(cotacao);
		
		if( cotacao.getIdCotacao() == null ){
			cotacao.setNrCotacao(findProximoNroCotacao());
			cotacao.setFilial(SessionUtils.getFilialSessao());
		}
		cotacao.setBlIndicadorSeguro(Boolean.FALSE);
		
		cotacao.setUsuarioByIdUsuarioAprovou(SessionUtils.getUsuarioLogado());
		cotacao.setUsuarioByIdUsuarioRealizou(SessionUtils.getUsuarioLogado());
		prepareParametroCliente(cotacao, calculoFrete);
		storePendenciaAprovacaoCotacao(cotacao,calculoFrete, true);
		if (cotacao.getServico() != null && cotacao.getServico().getTpModal() != null
				&& ConstantesExpedicao.MODAL_AEREO.equals(cotacao.getServico().getTpModal().getValue())) {
			validarDimensoes(cotacao);
		}
		return store(cotacao);
	}

	
	
	
	private void validarDimensoes(Cotacao cotacao) {
		
		if (cotacao.getPendenciaDimensoes() != null && cotacao.getPendenciaDimensoes().getTpSituacaoPendencia() != null
				&& ConstantesWorkflow.EM_APROVACAO.equals(cotacao.getPendenciaDimensoes().getTpSituacaoPendencia().getValue())) {
			workflowPendenciaService.cancelPendencia(cotacao.getPendenciaDimensoes().getIdPendencia());
		}
		
		cotacao.setPendenciaDimensoes(null);
		cotacao.setTpSituacaoAprovacaoDimensoes(new DomainValue(SITUACAO_COTACAO_APROVADA));
		cotacao.setUsuarioByIdUsuarioAprovouDimensoes(null);
		
		if(cotacao.getDimensoes() != null && !cotacao.getDimensoes().isEmpty()){
			Pendencia pendencia = null;

			for (Dimensao dimensaoCotacao : cotacao.getDimensoes()) {
				if (dimensaoMaior(dimensaoCotacao)) {
					
					if(cotacao.getIdCotacao() == null){
						this.store(cotacao);
					}
					
					pendencia = gerarWorkflowDimensao(dimensaoCotacao, cotacao);
					break;
				}
			}
			
			if(pendencia != null){
				cotacao.setPendenciaDimensoes(pendencia);
				cotacao.setTpSituacaoAprovacaoDimensoes(pendencia.getTpSituacaoPendencia());
				cotacao.setUsuarioByIdUsuarioAprovouDimensoes(null);
				cotacao.setTpSituacao(new DomainValue(SITUACAO_COTACAO_COTADA));
				
			}
		}
	}

	@SuppressWarnings("deprecation")
	private Pendencia gerarWorkflowDimensao(Dimensao dimensao, Cotacao cotacao) {
		Filial filial = SessionUtils.getFilialSessao();
		DateTime dataHoraAtualFilialLogada = JTDateTimeUtils.getDataHoraAtual(filial);
		String sgFilial ="";
		String nrCotacao ="";
		if(cotacao.getFilial()!=null && cotacao.getFilial().getSgFilial()!=null){
			sgFilial = cotacao.getFilial().getSgFilial();
		}
		if(cotacao.getNrCotacao()!=null){
			nrCotacao = cotacao.getNrCotacao().toString();
		}
		Object[] arguments = { sgFilial, nrCotacao };
		String dssProcesso = configuracoesFacade.getMensagem("LMS-01267", arguments);
		Pendencia pendenciaWorkFlow = workflowPendenciaService.generatePendencia(filial.getIdFilial(),
				Short.valueOf(ConstantesCotacao.ID_WORKFLOW_COTACAO_DIMENSOES_AERONAVES),
				cotacao.getIdCotacao(), dssProcesso, dataHoraAtualFilialLogada,SessionUtils.getUsuarioLogado(), SessionUtils.getEmpresaSessao().getIdEmpresa());
				
		return pendenciaWorkFlow;
	}

	private boolean dimensaoMaior(Dimensao dimensaoCotacao) {
		int[] intsParam = inicializaIntsParam();
		int[] dimCotacao = { dimensaoCotacao.getNrAltura(), dimensaoCotacao.getNrLargura(),
				dimensaoCotacao.getNrComprimento() };
		Arrays.sort(intsParam);
		Arrays.sort(dimCotacao);
		for (int i = dimCotacao.length-1; i >= 0; i--) {
			if (dimCotacao[i] > intsParam[i]) {
				return true;
			}
		}
		return false;
	}

	private int[] inicializaIntsParam() {
		ParametroGeral parametroAlturaAeronave = parametroGeralService
				.findByNomeParametro(ConstantesCotacao.PARAM_DIMENSOES_AERONAVE_ALTURA);
		Integer altura = 0;
		if (parametroAlturaAeronave != null) {
			altura = Integer.valueOf(parametroAlturaAeronave.getDsConteudo());
		}

		ParametroGeral parametroLarguraAeronave = parametroGeralService
				.findByNomeParametro(ConstantesCotacao.PARAM_DIMENSOES_AERONAVE_LARGURA);
		Integer largura = 0;
		if (parametroLarguraAeronave != null) {
			largura = Integer.valueOf(parametroLarguraAeronave.getDsConteudo());
		}

		ParametroGeral comprimentoAeronave = parametroGeralService
				.findByNomeParametro(ConstantesCotacao.PARAM_DIMENSOES_AERONAVE_COMPRIMENTO);
		Integer comprimento = 0;
		if (comprimentoAeronave != null) {
			comprimento = Integer.valueOf(comprimentoAeronave.getDsConteudo());
		}
		int[] intsParam = { altura, largura, comprimento };
		
		return intsParam;
	}

	private void compareItems(List oldList, List newList, List<Long> removeIds,
			CompareItens comp) {
		List auxList = new ArrayList();
		if(oldList != null && newList != null) {
			int i = 0;
			for (Iterator iter = newList.iterator(); iter.hasNext();) {
				Object o = iter.next();
				Object oOld = null;
				if( i < oldList.size() ){
					oOld = oldList.get(i);
					auxList.add(oOld);
					i++;
				}else{
				}
				comp.setValores(o, oOld);
			}
			oldList.removeAll(auxList);
			for( Object t: oldList){
				comp.addIdRemove(t, removeIds);
			}
		}
	}

	interface CompareItens{
		public void setValores(Object newItem,Object oldItem);

		public void addIdRemove(Object oldItem,List<Long> removeIds);
	}

	private void prepareParcelas(final Cotacao cotacao) {
		List lista1 = parcelaCotacaoService.findByCotacao(cotacao
				.getIdCotacao());
		List lista2 = cotacao.getParcelaCotacoes();
		
		Collections.sort(lista1, new Comparator<ParcelaCotacao>(){
			public int compare(ParcelaCotacao o1, ParcelaCotacao o2) {
				return o1.getParcelaPreco().getIdParcelaPreco()
						.compareTo(o2.getParcelaPreco().getIdParcelaPreco());
			}
		});
		
		Collections.sort(lista2, new Comparator<ParcelaCotacao>(){
			public int compare(ParcelaCotacao o1, ParcelaCotacao o2) {
				return o1.getParcelaPreco().getIdParcelaPreco()
						.compareTo(o2.getParcelaPreco().getIdParcelaPreco());
			}
		});

		ArrayList<Long>listIds = new ArrayList<Long>();
		
		compareItems(lista1,lista2,listIds,new CompareItens(){
			public void setValores(Object newItem,Object oldItem){
				Long id = null;
				if( oldItem != null ){
					id = ((ParcelaCotacao)oldItem).getIdParcelaCotacao();
				}
				((ParcelaCotacao)newItem).setIdParcelaCotacao(id);
				((ParcelaCotacao)newItem).setCotacao(cotacao);
			}

			public void addIdRemove(Object oldItem,List<Long> removeIds){
				removeIds.add(((ParcelaCotacao)oldItem).getIdParcelaCotacao());
			}
		});
		parcelaCotacaoService.removeByIds(listIds);
	}

	private void prepareServicos(final Cotacao cotacao) {
		List lista1 = servAdicionalDocServService.findByCotacao(cotacao
				.getIdCotacao());
		List lista2 = cotacao.getServAdicionalDocServs();
		ArrayList<Long>listIds = new ArrayList<Long>();
		
		compareItems(lista1,lista2,listIds,new CompareItens(){
			public void setValores(Object newItem,Object oldItem){
				Long id = null;
				if( oldItem != null ){
					id = ((ServAdicionalDocServ) oldItem)
							.getIdServAdicionalDocServ();
				}
				((ServAdicionalDocServ)newItem).setIdServAdicionalDocServ(id);
				((ServAdicionalDocServ)newItem).setCotacao(cotacao);
			}

			public void addIdRemove(Object oldItem,List<Long> removeIds){
				removeIds.add(((ServAdicionalDocServ) oldItem)
						.getIdServAdicionalDocServ());
			}
		});
		servAdicionalDocServService.removeByIds(listIds);
	}
	
	private void prepareDimensoes(final Cotacao cotacao) {
		List lista = dimensaoService.findByIdCotacao(cotacao.getIdCotacao());
		List dimensoes = cotacao.getDimensoes();
		ArrayList<Long>listIds = new ArrayList<Long>();
		
		compareItems(lista,dimensoes,listIds,new CompareItens(){
			public void setValores(Object newItem,Object oldItem){
				Long id = null;
				if( oldItem != null ){
					id = ((Dimensao)oldItem).getIdDimensao();
				}
				((Dimensao)newItem).setIdDimensao(id);
				((Dimensao)newItem).setCotacao(cotacao);
			}

			public void addIdRemove(Object oldItem,List<Long> removeIds){
				removeIds.add(((Dimensao)oldItem).getIdDimensao());
			}
		});
		dimensaoService.removeByIds(listIds);
	}
	
	private void prepareServicosAdicionaisCliente(final Cotacao cotacao) {
		List<Map<String, Object>> lista1 = servicoAdicionalClienteService
				.findServicosByIdCotacao(cotacao.getIdCotacao());
		List lista2 = cotacao.getServicoAdicionalClientes();

		ArrayList<Long>listIds = new ArrayList<Long>();
		compareItems(lista1,lista2,listIds,new CompareItens(){
			public void setValores(Object newItem,Object oldItem){
				Long id = null;
				if( oldItem != null ){
					id = (Long) ((Map) oldItem)
							.get("idServicoAdicionalCliente");
				}
				((ServicoAdicionalCliente) newItem)
						.setIdServicoAdicionalCliente(id);
				((ServicoAdicionalCliente)newItem).setCotacao(cotacao);
			}

			public void addIdRemove(Object oldItem,List<Long> removeIds){
				Long id = (Long) ((Map) oldItem)
						.get("idServicoAdicionalCliente");
				removeIds.add(id);
			}
		});
		servicoAdicionalClienteService.removeByIds(listIds);
	}

	/**
	 * Aramazena a parametrização do cliente feita para a cotação que está sendo
	 * armazenada autor Julio Cesar Fernandes Corrêa 27/01/2006
	 * 
	 * @param c
	 * @param calculo
	 */
	private void prepareParametroCliente(Cotacao cotacao,
			CalculoFrete calculoFrete) {
		ParametroCliente parametroCliente = calculoFrete.getParametroCliente();
		if(parametroCliente == null) {
			return;
		}
		
		Long idParametro = null;
		if( cotacao.getIdCotacao() != null ){
			TypedFlatMap par = parametroClienteService.findByIdCotacao(cotacao
					.getIdCotacao());
			idParametro = par.getLong("idParametroCliente");
		}
		
		parametroCliente.setIdParametroCliente(idParametro);
		parametroCliente.setCotacao(cotacao);
		parametroCliente.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
		parametroCliente.setTpSituacaoParametro(new DomainValue("C"));
		parametroCliente.setTabelaDivisaoCliente(null);
		parametroCliente.setClienteByIdClienteRedespacho(null);
		parametroCliente.setFilialByIdFilialDestino(null);
		parametroCliente.setFilialByIdFilialMercurioRedespacho(null);
		parametroCliente.setFilialByIdFilialOrigem(null);
		parametroCliente.setZonaByIdZonaDestino(null);
		parametroCliente.setZonaByIdZonaOrigem(null);
		parametroCliente.setPaisByIdPaisDestino(null);
		parametroCliente.setPaisByIdPaisOrigem(null);
		parametroCliente
				.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(null);
		parametroCliente
				.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(null);
		parametroCliente.setUnidadeFederativaByIdUfDestino(null);
		parametroCliente.setUnidadeFederativaByIdUfOrigem(null);
		parametroCliente.setAeroportoByIdAeroportoDestino(null);
		parametroCliente.setAeroportoByIdAeroportoOrigem(null);
		parametroCliente.setMunicipioByIdMunicipioDestino(null);
		parametroCliente.setMunicipioByIdMunicipioOrigem(null);
		parametroCliente.setSimulacao(null);
		parametroCliente.setPcReajFretePeso(null);
		parametroCliente.setPcReajVlMinimoFreteQuilo(null);
		parametroCliente.setPcReajVlFreteVolume(null);
		parametroCliente.setPcReajTarifaMinima(null);
		parametroCliente.setPcReajVlTarifaEspecifica(null);
		parametroCliente.setPcReajAdvalorem(null);
		parametroCliente.setPcReajAdvalorem2(null);
		parametroCliente.setPcReajVlMinimoFretePercen(null);
		parametroCliente.setPcReajVlToneladaFretePerc(null);
		parametroCliente.setPcReajMinimoGris(null);
		parametroCliente.setDtVigenciaFinal(null);
		parametroCliente.setDsEspecificacaoRota(null);

		if(parametroCliente.getTpIndicadorPercentualTde() == null)
			parametroCliente.setTpIndicadorPercentualTde(new DomainValue("T"));
		if(parametroCliente.getTpIndicadorMinimoTde() == null)
			parametroCliente.setTpIndicadorMinimoTde(new DomainValue("T"));
		if(parametroCliente.getVlPercentualTde() == null)
			parametroCliente.setVlPercentualTde(BigDecimal.ZERO);
		if(parametroCliente.getVlMinimoTde() == null)
			parametroCliente.setVlMinimoTde(BigDecimal.ZERO);
		
		if(parametroCliente.getBlPagaCubagem() == null){
			parametroCliente.setBlPagaCubagem(Boolean.TRUE);
		}
		
		if (parametroCliente.getPcPagaCubagem() == null){
			parametroCliente.setPcPagaCubagem(new BigDecimal(100));
		}

		//Este cadastro não utiliza ValorFaixaProgressivaProposta, se passar a utilizar isto deve ser modificado.
		parametroCliente.setValoresFaixaProgressivaProposta(null);
		
		prepareTaxaCliente(parametroCliente, calculoFrete.getTaxas());
		prepareGeneralidadeCliente(parametroCliente,
				calculoFrete.getGeneralidades());

		List<ParametroCliente> parametrosCliente = new ArrayList<ParametroCliente>(
				1);
		parametrosCliente.add(parametroCliente);
		cotacao.setParametroClientes(parametrosCliente);
	}

	/**
	 * Armazena as taxas cliente vinculadas a parametrização do cliente. autor
	 * Julio Cesar Fernandes Corrêa 27/01/2006
	 * 
	 * @param pc
	 * @param parcelas
	 */
	private void prepareTaxaCliente(final ParametroCliente pc,
			List<ParcelaServico> parcelas) {

		List<Map> lista1 = taxaClienteService.findByIdParametroCliente(pc
				.getIdParametroCliente());
		final List<TaxaCliente> taxasCliente = new ArrayList<TaxaCliente>();
		ArrayList<Long>listIds = new ArrayList<Long>();
		
		if(parcelas != null) {
			compareItems(lista1,parcelas,listIds,new CompareItens(){
				public void setValores(Object newItem,Object oldItem){
					ParcelaServico ps = (ParcelaServico)newItem;
					ParcelaPreco pp = ps.getParcelaPreco();
					TaxaCliente taxaCliente = (TaxaCliente)ps.getParametro();
					if(taxaCliente != null) {
						Long id = null;
						if( oldItem != null ){
							id = MapUtils.getLong((Map) oldItem,
									"idTaxaCliente");
						}else{
							taxaCliente.setIdTaxaCliente(null);
						}
						taxaCliente.setIdTaxaCliente(id);
						taxaCliente.setParcelaPreco(pp);
						taxaCliente.setParametroCliente(pc);

						taxasCliente.add(taxaCliente);
					}
				}

				public void addIdRemove(Object oldItem,List<Long> removeIds){
					Long id = (Long)((Map)oldItem).get("idTaxaCliente");
					removeIds.add(id);
				}
			});
		}
		
		taxaClienteService.removeByIds(listIds);
		pc.setTaxaClientes(taxasCliente);
	}

	/**
	 * Armazena as generalidades cliente vinculadas a parametrização do cliente.
	 * autor Julio Cesar Fernandes Corrêa 27/01/2006
	 * 
	 * @param pc
	 * @param parcelas
	 */
	private void prepareGeneralidadeCliente(final ParametroCliente pc,
			List<ParcelaServico> parcelas) {
		List lista1 = generalidadeClienteService.findByIdParametroCliente(pc
				.getIdParametroCliente());

		final List generalidadesCliente = new ArrayList<GeneralidadeCliente>();
		;
		ArrayList<Long>listIds = new ArrayList<Long>();
		if(parcelas != null) {			
			compareItems(lista1,parcelas,listIds,new CompareItens(){
				public void setValores(Object newItem,Object oldItem){
					ParcelaServico ps = (ParcelaServico)newItem;
					ParcelaPreco pp = ps.getParcelaPreco();
					GeneralidadeCliente generalidadeCliente = (GeneralidadeCliente) ps
							.getParametro();
					if(generalidadeCliente != null) {
						Long id = null;
						if( oldItem != null ){
							id = MapUtils.getLong((Map) oldItem,
									"idGeneralidadeCliente");
						}
						generalidadeCliente.setIdGeneralidadeCliente(id);
						generalidadeCliente.setParcelaPreco(pp);
						generalidadeCliente.setParametroCliente(pc);
						generalidadesCliente.add(generalidadeCliente);
					}
				}

				public void addIdRemove(Object oldItem,List<Long> removeIds){
					Long id = (Long) ((Map) oldItem)
							.get("idGeneralidadeCliente");
					removeIds.add(id);
				}
			});
		}
		generalidadeClienteService.removeByIds(listIds);
		pc.setGeneralidadeClientes(generalidadesCliente);
	}

	private Integer findProximoNroCotacao() {
		return configuracoesFacade.incrementaParametroSequencial(
				SessionUtils.getFilialSessao().getIdFilial(), 
				ConstantesVendas.NR_ULTIMA_COTACAO , true).intValue();
	}

	public Map findCotacaoById(Long id) {
		Map map = getCotacaoDAO().findCotacaoById(id);
		if(map == null)
			throw new BusinessException("objectNotFoundException");
		return map;
	}

	public CalculoFrete findDadosCalculoCotacao(Long idCotacao) {

		Cotacao cotacao = VendasUtils.getCotacaoInSession();
		
		CalculoFrete calculoFrete = newCalculoFrete(cotacao
				.getTpDocumentoCotacao());

		cotacao.setBlFrete(Boolean.TRUE);
		
		TabelaPreco tp = tabelaPrecoService.findByIdTabelaPrecoNotLazy(cotacao
				.getTabelaPreco().getIdTabelaPreco());
		calculoFrete.setTabelaPreco(tp);
		cotacao.setTabelaPreco(tp);
		
		Boolean calculaSA = calculoFrete.getServAdicionalDoctoServico() != null
				&& calculoFrete.getServAdicionalDoctoServico().size() > 0;
		cotacao.setBlServicosAdicionais(calculaSA);
		calculoFrete.setBlCalculaServicosAdicionais(calculaSA);
		calculoFrete.setRecalculoFrete(Boolean.TRUE);
		
		List<ParametroCliente> pc = parametroClienteService
				.findParametroByIdCotacao(idCotacao);
		if( pc != null && pc.size() > 0 ){
			calculoFrete.setParametroCliente(pc.get(0));
		}
		calculoFrete.setDhEmissaoDocRecalculo(cotacao.getDtGeracaoCotacao()
				.toDateTimeAtCurrentTime());
		ExpedicaoUtils.setCalculoFreteInSession(calculoFrete);
		calculoFrete = executeConfigureCalculoFrete(cotacao,false);
		calculoFrete.setBlEditaCotacao(Boolean.TRUE);
		findCalculoCotacao(cotacao, calculoFrete);
		calculoFrete.setBlEditaCotacao(Boolean.FALSE);
		calculoFrete.setRecalculoFrete(Boolean.FALSE);
		calculoFrete.setBlRecalculoCotacao(Boolean.FALSE);
		return calculoFrete;
	}

	public YearMonthDay findNextDiaUtil(){
		YearMonthDay now = YearMonthDay.fromCalendarFields(Calendar
				.getInstance());
		Long idMunicipio = SessionUtils.getFilialSessao().getPessoa()
				.getEnderecoPessoa().getMunicipio().getIdMunicipio();
		BigDecimal diasParam = (BigDecimal) configuracoesFacade
				.getValorParametro(ConstantesVendas.DIAS_VALIDADE_COTACAO);
		int dias = 2;
		if( diasParam != null ){
			dias = diasParam.intValue();
		}
		for(int i = 0; i < dias; i++){
			now = diaUtils.getNextDiaUtil(now, idMunicipio); 
		}
		return now;
	}
	
	public Cotacao storeCotacaoReprovada(Long idCotacao, String dsMotivo) {
		Cotacao cotacao = findById(idCotacao);
		preparaCotacaoReprovada(cotacao, dsMotivo,
				SITUACAO_COTACAO_PERDIDA.charAt(0));
		if (cotacao.getPendencia() != null){
			workflowPendenciaService.cancelPendencia(cotacao.getPendencia());
		}
		store(cotacao);
		return cotacao;
	}
	
	private void preparaCotacaoReprovada(Cotacao cotacao, String dsMotivo,
			char status) {
		cotacao.setTpSituacao(domainValueService.findDomainValueByValue(
				"DM_STATUS_COTACAO", Character.toString(status)));
		cotacao.setDsMotivo(dsMotivo);
	}

	public void removeCotacaoByIds(List ids) {
		if(!validateExclusao(ids)) {
			throw new BusinessException("LMS-01103");
		}
		getCotacaoDAO().removeByIds(ids, true);
	}
	
	public boolean validateExclusao(List ids) {
		return getCotacaoDAO().validateExclusao(ids); 
	}

	public Short validateCalculoPpe(Long idServico, Long idMunicipioOrigem,
			Long idMunicipioDestino) {
		Map<String, Object> prazos = new HashMap<String, Object>();
		prazos = ppeService.executeCalculoPPE(idMunicipioOrigem,
				idMunicipioDestino, idServico, null, null, null, null, null,
				"N");
		Long nrHorasPPE = (Long) prazos.get("nrPrazo");
		nrHorasPPE = JTDateTimeUtils.getDaysFromHours(nrHorasPPE.longValue());
		BigDecimal nrPPE = new BigDecimal(nrHorasPPE);
		nrPPE = nrPPE.add(new BigDecimal(0.9));
		return Short.valueOf((short)nrPPE.longValue());
	}

	public Short executeCalcularPpeClienteEspecial(String nrIdentificacaoClienteRemetente,
			String nrIdentificacaoClienteDestinatario, String nrIdentificacaoClienteDevedorFrete,
			Long idServico, Long idMunicipioFilialOrigem,
			Long idMunicipioDestino, Long idFilialOrigem, Long idFilialDestino) {
		
		Cliente clienteRemetente = clienteService.findByNrIdentificacao(nrIdentificacaoClienteRemetente);
		Cliente clienteDestinatario = clienteService.findByNrIdentificacao(nrIdentificacaoClienteDestinatario);
		Cliente clienteDevedorFrete= clienteService.findByNrIdentificacao(nrIdentificacaoClienteDevedorFrete);
		
		String cepOrigem = null;
		EnderecoPessoa enderecoPessoaRemetente = enderecoPessoaService.findEnderecoPessoaPadrao(clienteRemetente.getPessoa().getIdPessoa());
		if (enderecoPessoaRemetente != null){
			cepOrigem = enderecoPessoaRemetente.getNrCep();
		}
		
		String cepDestino = null;
		EnderecoPessoa enderecoPessoaDestinatario = enderecoPessoaService.findEnderecoPessoaPadrao(clienteDestinatario.getPessoa().getIdPessoa());
		if (enderecoPessoaDestinatario != null){
			cepDestino = enderecoPessoaDestinatario.getNrCep();
		}

		Map<String, Object> dpe = dpeService.executeCalculoDPE(clienteRemetente, clienteDestinatario,
				clienteDevedorFrete, null, null, null, idServico, idMunicipioFilialOrigem,
				idFilialOrigem, idFilialDestino, idMunicipioDestino, cepOrigem, cepDestino, null);

		return Short.valueOf(dpe.get("nrPrazo").toString());
	}

	/**
	 * Método que retorna uma Cotação a partir do seu ID.
	 * 
	 * @param idCotacao
	 * @return
	 */
	public Cotacao findCotacaoByIdCotacao(Long idCotacao) {		
		return this.getCotacaoDAO().findCotacaoByIdCotacao(idCotacao);
	}

	/**
	 * Método que retorna uma Cotação a partir do ID da Filial Origem e do
	 * Número da Cotação.
	 * 
	 * @param idFilialOrigem
	 * @param nrCotacao
	 * @return Cotacao
	 */
	public Cotacao findCotacaoByIdFilialOrigemByNrCotacao(Long idFilialOrigem,
			Integer nrCotacao) {
		return this.getCotacaoDAO().findCotacaoByIdFilialOrigemByNrCotacao(
				idFilialOrigem, nrCotacao);
	}

	public void setDocumentoServicoFacade(
			DocumentoServicoFacade documentoServicoFacade) {
		this.documentoServicoFacade = documentoServicoFacade;
	}

	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setMcdService(McdService mcdService) {
		this.mcdService = mcdService;
	}

	public void setMunicipioFilialService(
			MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public void setLiberacaoEmbarqueService(
			LiberacaoEmbarqueService liberacaoEmbarqueService) {
		this.liberacaoEmbarqueService = liberacaoEmbarqueService;
	}

	public void setProibidoEmbarqueService(
			ProibidoEmbarqueService proibidoEmbarqueService) {
		this.proibidoEmbarqueService = proibidoEmbarqueService;
	}
	
	public List<Map> findPocLegacyConnection(Map<String, Long> received) {
		Long idDepot = received.get("depotid");
		Long idLast = received.get("lastid");
		List<Cotacao> itens = getCotacaoDAO().findPocByDepotAndLastID(idDepot,
				idLast);
		List<Map> retorno = new ArrayList<Map>();
		for (Cotacao cotacao : itens) {
			Map item = new HashMap();
			item.put("lastid", cotacao.getIdCotacao());
			item.put("OPCOT_NOTA", cotacao.getNrNotaFiscal());
			item.put("OPCOT_NRO", cotacao.getNrCotacao());
			item.put("OPCOT_FLOR", cotacao.getFilialByIdFilialOrigem()
					.getSgFilial());
			item.put("OPCOT_FLDT", cotacao.getFilialByIdFilialDestino()
					.getSgFilial());
			item.put("OPCOT_MUOR", cotacao.getMunicipioByIdMunicipioOrigem()
					.getNrCep().substring(0, 5));
			item.put("OPCOT_MUNF", cotacao.getMunicipioByIdMunicipioDestino()
					.getNrCep().substring(0, 5));
			item.put("OPCOT_PESO", cotacao.getPsReal());
			item.put("OPCOT_VMER", cotacao.getVlMercadoria());
			if (cotacao.getClienteByIdCliente() != null
					&& cotacao.getClienteByIdCliente().getPessoa() != null)
				item.put("OPCOT_CLI", cotacao.getClienteByIdCliente()
						.getPessoa().getNrIdentificacao());
			retorno.add(item);
		}
		return retorno;
		
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	
	private long ajustaFaixaAprovacao(long faixa){
		final Long[] faixasExcessao = {FAIXA_3,FAIXA_4};
		if( ArrayUtils.contains(faixasExcessao, faixa) ){
			return FAIXA_2;
		}
		return faixa;
	}
	
	public void storePendenciaAprovacaoCotacao(Cotacao cotacao,
			CalculoFrete calculoFrete, boolean blAprova) {
		Boolean alteracaoParams = validateAlteracaoParametros(calculoFrete);
		if( alteracaoParams ){
			if( cotacao.getIdCotacao() != null ){
				return;
			}
		}
		if (cotacao.getTpSituacaoAprovacao() == null && blAprova){
			if( alteracaoParams ){
				configuraAprovacaoCotacao(cotacao);
			}else if(!validateLimiteDescontoCotacao(cotacao)){
				long nrEvento = 130;
				if (cotacao.getServico().getTpModal().getValue().equals("A")){
					long faixaAereo = ajustaFaixaAprovacao(verificaFaixaAprovacaoDescontoCotacaoAereo(cotacao));
					nrEvento += faixaAereo;
				}else{
					long faixaRodoviario = ajustaFaixaAprovacao(verificaFaixaAprovacaoDescontoCotacaoRodoviario(cotacao));
					if (checkPerfilTelevendas()){
						if (faixaRodoviario > 1){
							throw new BusinessException("LMS-30058");
						}else{
							faixaRodoviario +=10;
						}
					}
					nrEvento += faixaRodoviario;
				}
				
				cotacao.setTpSituacao(new DomainValue(SITUACAO_COTACAO_COTADA));
				this.store(cotacao);
				
				String nrProposta = cotacao.getFilial().getSgFilial()
						+ FormatUtils.fillNumberWithZero(cotacao.getNrCotacao()
								.toString(), 6);
				Pendencia pendencia = workflowPendenciaService
						.generatePendencia(SessionUtils.getFilialSessao()
								.getIdFilial(), (short) nrEvento, cotacao
								.getIdCotacao(), configuracoesFacade
								.getMensagem("aprovacaoCotacao",
										new Object[] { nrProposta }),
						JTDateTimeUtils.getDataHoraAtual());
				
				cotacao.setPendencia(pendencia);
				cotacao.setTpSituacaoAprovacao(pendencia
						.getTpSituacaoPendencia());
				cotacao.setUsuarioByIdUsuarioAprovou(null);
				cotacao.setTpSituacao(new DomainValue(SITUACAO_COTACAO_COTADA));
			}else{
				configuraAprovacaoCotacao(cotacao);
			}
		}else if(cotacao.getTpSituacaoAprovacao()!= null){
			if (SITUACAO_EM_APROVACAO == cotacao.getTpSituacaoAprovacao()
					.getValue().charAt(0)) {
			if( cotacao.getPendencia() != null ){
					this.store(cotacao);
					workflowPendenciaService.cancelPendencia(cotacao
							.getPendencia().getIdPendencia());
				}
			}
			// reseta a situação da aprovação e chama novamente a verificação
			// das faixas de desconto
			cotacao.setDtAprovacao(null);
			cotacao.setTpSituacaoAprovacao(null);
			cotacao.setUsuarioByIdUsuarioAprovou(null);
			cotacao.setPendencia(null);
			storePendenciaAprovacaoCotacao(cotacao, calculoFrete, blAprova);
		}
			
	}
	
	private void configuraAprovacaoCotacao(Cotacao cotacao){
		cotacao.setPendencia(null);
		cotacao.setTpSituacaoAprovacao(new DomainValue(String
				.valueOf(SITUACAO_APROVACAO_APROVADA)));
		cotacao.setTpSituacao(new DomainValue(SITUACAO_COTACAO_APROVADA));
		cotacao.setUsuarioByIdUsuarioAprovou(SessionUtils.getUsuarioLogado());
		cotacao.setDtValidade(findNextDiaUtil());
		cotacao.setDtAprovacao(JTDateTimeUtils.getDataAtual());
	}
	
	private long verificaFaixaAprovacaoDescontoCotacaoRodoviario(Cotacao cotacao) {

		TabelaDivisaoCliente tabelaDivisaoCliente = getTabelaDivisaoCliente(cotacao);
		if (tabelaDivisaoCliente != null ){
			BigDecimal fatorCubagemPadrao = (BigDecimal) configuracoesFacade
					.getValorParametro("FATOR_CUBAGEM_PADRAO_RODO");
			if( tabelaDivisaoCliente.getNrFatorCubagem() != null ){
				if (tabelaDivisaoCliente.getNrFatorCubagem().compareTo(
						new BigDecimal(200)) < 0) {
					return FAIXA_4;
				} else if (tabelaDivisaoCliente.getNrFatorCubagem().compareTo(
						new BigDecimal(250)) < 0) {
					return FAIXA_3;
				} else if (fatorCubagemPadrao != null
						&& tabelaDivisaoCliente.getNrFatorCubagem().compareTo(
								fatorCubagemPadrao) < 0) {
					return FAIXA_2;
				}
			}
		}
		
		FaixaAprovacao faixaAtual = new FaixaAprovacao(FAIXA_1);
		
		CalculoFrete calculoFrete = ExpedicaoUtils.getCalculoFreteInSession();
		ParametroCliente parametro = calculoFrete.getParametroCliente();
		
		if (parametro !=null){
			//Grupo frete peso
			if (findFaixaAprovacaoGrupoFretePesoRodoviario(parametro,
					faixaAtual)) {
				return faixaAtual.getFaixaAprovacao();
			}
			
			//Grupos Frete valor e Frete percentual
			if (findFaixaAprovacaoGrupoFreteValorPercentualRodoviario(
					parametro, faixaAtual)) {
				return faixaAtual.getFaixaAprovacao();
			}
			
			//Grupo GRIS
			if (findFaixaAprovacaoGrupoGrisRodoviario(parametro, faixaAtual)){
				return faixaAtual.getFaixaAprovacao();
			}
			
			//Grupo pedagio
			if (findFaixaAprovacaoGrupoPedagioRodoviario(parametro, faixaAtual)){
				return faixaAtual.getFaixaAprovacao();
			}
			
			//Grupo Total do frete
			if (findFaixaAprovacaoGrupoTotalFreteRodoviario(parametro,
					faixaAtual)) {
				return faixaAtual.getFaixaAprovacao();
			}
			
			if (findFaixaAprovacaoGeneralidadesRodoviario(parametro, faixaAtual)){
				return faixaAtual.getFaixaAprovacao();
			}
			
			if (findFaixaAprovacaoTdeTrtRodoviario(parametro, faixaAtual)){
				return faixaAtual.getFaixaAprovacao();
			}
			
			List<TaxaCliente> taxas = parametro.getTaxaClientes();
			if (taxas != null){
				for (TaxaCliente taxaCliente : taxas) {
					if (!TABELA.equals(taxaCliente.getTpTaxaIndicador()
							.getValue())) {
						return FAIXA_4;
					}
				}
			}
			
			List<ServicoAdicionalCliente> servicosAdicionais = cotacao
					.getServicoAdicionalClientes();
			 if (servicosAdicionais!= null && !servicosAdicionais.isEmpty()){
				 for (ServicoAdicionalCliente servicoAdicionalCliente : servicosAdicionais) {
					if (!servicoAdicionalCliente.getTpIndicador().getValue()
							.equals(TABELA)) {
						return FAIXA_2;
					}
				}
			 }
			
		}
		return faixaAtual.getFaixaAprovacao();
	}
	
	private boolean findFaixaAprovacaoGrupoFretePesoRodoviario(
			final ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		String[]indicadoresTDA = new String[]{TABELA,DESCONTO,ACRESCIMO};
		//Verificações gerais da faixa 4
		if (CompareUtils.ne(TABELA, parametro.getTpIndicadorMinFretePeso()
				.getValue())
				|| CompareUtils.ne(ZERO, parametro.getVlMinimoFreteQuilo())
				|| CompareUtils.ne(ZERO, parametro.getVlFreteVolume())
				|| parametro.getBlPagaPesoExcedente()
				|| checkIndicadorNotIn(parametro
						.getTpIndicadorPercMinimoProgr().getValue(),
						indicadoresTDA)) {
			faixaAtual.setFaixaAprovacao(FAIXA_4);
			return true;
		}
		
		//Faixas de desconto
		//Percentual Minimo progressivo
		Long faixa = findFaixaDescontoIndicador(parametro
				.getTpIndicadorPercMinimoProgr().getValue(),
				parametro.getVlPercMinimoProgr(), FIFTY, FORTY, THIRTY);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		//Frete peso
		faixa = findFaixaDescontoIndicador(parametro.getTpIndicadorFretePeso()
				.getValue(), parametro.getVlFretePeso(), FIFTY, FORTY, THIRTY);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		return false;
	}
	
	private boolean findFaixaAprovacaoTdeTrtAereo(ParametroCliente parametro,
			FaixaAprovacao faixaAtual) {
		if (!parametro.getTpIndicadorPercentualTde().getValue().equals(TABELA)
				|| !parametro.getTpIndicadorMinimoTde().getValue()
						.equals(TABELA)
				|| !parametro.getTpIndicadorPercentualTrt().getValue()
						.equals(TABELA)
				|| !parametro.getTpIndicadorMinimoTrt().getValue()
						.equals(TABELA)) {
			faixaAtual.setFaixaAprovacao(FAIXA_2);
			return true;
		}
		return false;
	}
	
	private boolean findFaixaAprovacaoTdeTrtRodoviario(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		String[]indicadoresTDA = new String[]{TABELA,DESCONTO,ACRESCIMO};
		
		//Valor percentual TDE
		if (checkIndicadorNotIn(parametro.getTpIndicadorPercentualTde()
				.getValue(), indicadoresTDA)) {
			faixaAtual.setFaixaAprovacao(FAIXA_3);
			return true;
		}
		
		Long faixa = findFaixaDescontoIndicador(parametro
				.getTpIndicadorPercentualTde().getValue(),
				parametro.getVlPercentualTde(), null, BigDecimal.TEN, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaValorIndicador(parametro.getTpIndicadorPercentualTde()
				.getValue(), parametro.getVlPercentualTde(), parametro
				.getTabelaPreco().getIdTabelaPreco(),
				ConstantesExpedicao.CD_TDE, null, BigDecimal.TEN, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		// Valor minimo TDE
		if (checkIndicadorNotIn(parametro.getTpIndicadorMinimoTde().getValue(),
				indicadoresTDA)) {
			faixaAtual.setFaixaAprovacao(FAIXA_3);
			return true;
		}
		
		faixa = findFaixaDescontoIndicador(parametro.getTpIndicadorMinimoTde()
				.getValue(), parametro.getVlMinimoTde(), null, BigDecimal.TEN,
				null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaValorIndicador(parametro.getTpIndicadorMinimoTde()
				.getValue(), parametro.getVlMinimoTde(), parametro
				.getTabelaPreco().getIdTabelaPreco(),
				ConstantesExpedicao.CD_TDE, null, BigDecimal.TEN, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		//Valor percentual TRT
		if (checkIndicadorNotIn(parametro.getTpIndicadorPercentualTrt()
				.getValue(), indicadoresTDA)) {
			faixaAtual.setFaixaAprovacao(FAIXA_3);
			return true;
		}
		
		faixa = findFaixaDescontoIndicador(parametro
				.getTpIndicadorPercentualTrt().getValue(),
				parametro.getVlPercentualTrt(), null, BigDecimal.TEN, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaValorIndicador(parametro.getTpIndicadorPercentualTrt()
				.getValue(), parametro.getVlPercentualTrt(), parametro
				.getTabelaPreco().getIdTabelaPreco(),
				ConstantesExpedicao.CD_TRT, null, BigDecimal.TEN, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		// Valor minimo TRT
		if (checkIndicadorNotIn(parametro.getTpIndicadorMinimoTrt().getValue(),
				indicadoresTDA)) {
			faixaAtual.setFaixaAprovacao(FAIXA_3);
			return true;
		}
		
		faixa = findFaixaDescontoIndicador(parametro.getTpIndicadorMinimoTrt()
				.getValue(), parametro.getVlMinimoTde(), null, BigDecimal.TEN,
				null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaValorIndicador(parametro.getTpIndicadorMinimoTrt()
				.getValue(), parametro.getVlMinimoTrt(), parametro
				.getTabelaPreco().getIdTabelaPreco(),
				ConstantesExpedicao.CD_TRT, null, BigDecimal.TEN, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		return false;
	}
	
	private boolean findFaixaAprovacaoGeneralidadesRodoviario(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		List<GeneralidadeCliente> generalidades = parametro
				.getGeneralidadeClientes();
		for (GeneralidadeCliente generalidade : generalidades) {
			
			//Despacho
			if (generalidade.getParcelaPreco().getCdParcelaPreco()
					.equals(ConstantesExpedicao.CD_DESPACHO)) {
				String[] indicadores = new String[] { TABELA, DESCONTO, VALOR,
						ACRESCIMO };
				if (checkIndicadorNotIn(generalidade.getTpIndicador()
						.getValue(), indicadores)) {
					faixaAtual.setFaixaAprovacao(FAIXA_4);
					return true;
				}
				Long faixa = findFaixaDescontoIndicador(generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), FIFTY, null, null);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
				
				faixa = findFaixaValorIndicador(generalidade.getTpIndicador()
						.getValue(), generalidade.getVlGeneralidade(),
						parametro.getTabelaPreco().getIdTabelaPreco(),
						generalidade.getParcelaPreco().getCdParcelaPreco(),
						FIFTY, null,null);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
			}
			
			//TAD
			if (generalidade.getParcelaPreco().getCdParcelaPreco()
					.equals(ConstantesExpedicao.CD_TAD)) {
				String[] indicadores = new String[] { TABELA, DESCONTO,
						FRACAOQUILO, ACRESCIMO };
				if (checkIndicadorNotIn(generalidade.getTpIndicador()
						.getValue(), indicadores)) {
					faixaAtual.setFaixaAprovacao(FAIXA_4);
					return true;
				}
				Long faixa = findFaixaDescontoIndicador(generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), FIFTY, FORTY, THIRTY);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
				
				faixa = findFaixaIndicador(FRACAOQUILO, generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), parametro
								.getTabelaPreco().getIdTabelaPreco(),
						generalidade.getParcelaPreco().getCdParcelaPreco(),
						FIFTY, FORTY, THIRTY);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
			}
			
			//TAS
			if (generalidade.getParcelaPreco().getCdParcelaPreco()
					.equals(ConstantesExpedicao.CD_TAS)) {
				String[] indicadores = new String[] { TABELA, DESCONTO,
						FRACAOQUILO, ACRESCIMO };
				if (checkIndicadorNotIn(generalidade.getTpIndicador()
						.getValue(), indicadores)) {
					faixaAtual.setFaixaAprovacao(FAIXA_4);
					return true;
				}
				Long faixa = findFaixaDescontoIndicador(generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), TWENTY,
						BigDecimal.TEN, null);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
				
				faixa = findFaixaIndicador(FRACAOQUILO, generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), parametro
								.getTabelaPreco().getIdTabelaPreco(),
						generalidade.getParcelaPreco().getCdParcelaPreco(),
						TWENTY, BigDecimal.TEN, null);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
			}
			
			//Taxa SUFRAMA
			if (generalidade.getParcelaPreco().getCdParcelaPreco()
					.equals(ConstantesExpedicao.CD_TAXA_SUFRAMA)) {
				String[] indicadores = new String[] { TABELA, DESCONTO,
						FRACAOQUILO, ACRESCIMO };
				if (checkIndicadorNotIn(generalidade.getTpIndicador()
						.getValue(), indicadores)) {
					faixaAtual.setFaixaAprovacao(FAIXA_4);
					return true;
				}
				Long faixa = findFaixaDescontoIndicador(generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), THIRTY, TWENTY, null);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
				
				faixa = findFaixaIndicador(FRACAOQUILO, generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), parametro
								.getTabelaPreco().getIdTabelaPreco(),
						generalidade.getParcelaPreco().getCdParcelaPreco(),
						THIRTY, TWENTY, null);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
			}
			
			//Seguro Fluvial
			if (generalidade.getParcelaPreco().getCdParcelaPreco()
					.equals(ConstantesExpedicao.CD_SEGURO_FLUVIAL)) {
				String[] indicadores = new String[] { TABELA, DESCONTO,
						FRACAOQUILO, ACRESCIMO };
				if (checkIndicadorNotIn(generalidade.getTpIndicador()
						.getValue(), indicadores)) {
					faixaAtual.setFaixaAprovacao(FAIXA_4);
					return true;
				}
				Long faixa = findFaixaDescontoIndicador(generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), FIFTY, FORTY, THIRTY);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
				
				faixa = findFaixaIndicador(FRACAOQUILO, generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), parametro
								.getTabelaPreco().getIdTabelaPreco(),
						generalidade.getParcelaPreco().getCdParcelaPreco(),
						FIFTY, FORTY, THIRTY);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
			}
			
			//Taxa seguranca balsa
			if (generalidade.getParcelaPreco().getCdParcelaPreco()
					.equals(ConstantesExpedicao.CD_TAXA_FLUVIAL_BALSA)) {
				String[] indicadores = new String[]{TABELA,DESCONTO,VALOR};
				if (checkIndicadorNotIn(generalidade.getTpIndicador()
						.getValue(), indicadores)) {
					faixaAtual.setFaixaAprovacao(FAIXA_3);
					return true;
				}
				Long faixa = findFaixaDescontoIndicador(generalidade
						.getTpIndicador().getValue(),
						generalidade.getVlGeneralidade(), null, FORTY, null);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
				
				faixa = findFaixaIndicador(VALOR, generalidade.getTpIndicador()
						.getValue(), generalidade.getVlGeneralidade(),
						parametro.getTabelaPreco().getIdTabelaPreco(),
						generalidade.getParcelaPreco().getCdParcelaPreco(),
						null, FORTY, null);
				if (faixa > 0){
					faixaAtual.setFaixaAprovacao(faixa);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean findFaixaAprovacaoGrupoFreteValorPercentualRodoviario(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		String[]indicadoresTDA = new String[]{TABELA,DESCONTO,ACRESCIMO};
		if(CompareUtils.ne(parametro.getPcFretePercentual(),ZERO)
				|| CompareUtils.ne(TABELA, parametro
						.getTpIndicadorValorReferencia().getValue())
				|| checkIndicadorNotIn(parametro.getTpIndicadorAdvalorem()
						.getValue(), indicadoresTDA)
				|| checkIndicadorNotIn(parametro.getTpIndicadorAdvalorem2()
						.getValue(), indicadoresTDA)) {
			faixaAtual.setFaixaAprovacao(FAIXA_4);
			return true;
		}
		
		Long faixa = findFaixaDescontoIndicador(parametro
				.getTpIndicadorAdvalorem().getValue(),
				parametro.getVlAdvalorem(), FIFTY, FORTY, THIRTY);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaDescontoIndicador(parametro.getTpIndicadorAdvalorem2()
				.getValue(), parametro.getVlAdvalorem2(), FIFTY, FORTY, THIRTY);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		return false;
	}
	
	private boolean findFaixaAprovacaoGrupoFreteValorPercentualAereo(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		String[]indicadoresTDA = new String[]{TABELA,DESCONTO,ACRESCIMO};
		if(CompareUtils.ne(parametro.getPcFretePercentual(),ZERO)
				|| CompareUtils.ne(TABELA, parametro
						.getTpIndicadorValorReferencia().getValue())
				|| checkIndicadorNotIn(parametro.getTpIndicadorAdvalorem()
						.getValue(), indicadoresTDA)
				|| checkIndicadorNotIn(parametro.getTpIndicadorAdvalorem2()
						.getValue(), indicadoresTDA)) {
			faixaAtual.setFaixaAprovacao(FAIXA_2);
			return true;
		}
		
		Long faixa = findFaixaDescontoIndicador(parametro
				.getTpIndicadorAdvalorem().getValue(),
				parametro.getVlAdvalorem(), null, null, TWENTY_FIVE);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaDescontoIndicador(parametro.getTpIndicadorAdvalorem2()
				.getValue(), parametro.getVlAdvalorem2(), null, null,
				TWENTY_FIVE);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		return false;
	}
	
	private boolean findFaixaAprovacaoGrupoGrisRodoviario(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		String[] indicadores = new String[] { TABELA, DESCONTO, VALOR,
				ACRESCIMO };
		if (checkIndicadorNotIn(parametro.getTpIndicadorPercentualGris()
				.getValue(), indicadores)) {
			faixaAtual.setFaixaAprovacao(FAIXA_4);
			return true;
		}
		
		Long faixa = findFaixaDescontoIndicador(parametro
				.getTpIndicadorPercentualGris().getValue(),
				parametro.getVlPercentualGris(), FIFTY, null, THIRTY);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}

		faixa = findFaixaValorIndicador(parametro
				.getTpIndicadorPercentualGris().getValue(),
				parametro.getVlPercentualGris(), parametro.getTabelaPreco()
						.getIdTabelaPreco(), ConstantesExpedicao.CD_GRIS,
				FIFTY, null, THIRTY);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaDescontoIndicador(parametro.getTpIndicadorMinimoGris()
				.getValue(), parametro.getVlMinimoGris(), FIFTY, null, THIRTY);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaValorIndicador(parametro.getTpIndicadorMinimoGris()
				.getValue(), parametro.getVlMinimoGris(), parametro
				.getTabelaPreco().getIdTabelaPreco(),
				ConstantesExpedicao.CD_GRIS, FIFTY, null, THIRTY);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		return false;
	}
	
	private boolean findFaixaAprovacaoGrupoGrisAereo(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		if (!parametro.getTpIndicadorPercentualGris().getValue().equals(TABELA)
				|| parametro.getTpIndicadorMinimoGris().getValue()
						.equals(TABELA)) {
			faixaAtual.setFaixaAprovacao(FAIXA_2);
			return true;
		}
		
		return false;
	}
	
	private boolean findFaixaAprovacaoGrupoPedagioRodoviario(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		String[] indicadores = new String[] { TABELA, DESCONTO, VALOR, FIXO,
				FAIXAPESO, FRACAOQUILO, ACRESCIMO };
		if (checkIndicadorNotIn(parametro.getTpIndicadorPedagio().getValue(),
				indicadores)) {
			faixaAtual.setFaixaAprovacao(FAIXA_4);
			return true;
		}
		
		Long faixa = findFaixaDescontoIndicador(parametro
				.getTpIndicadorPedagio().getValue(), parametro.getVlPedagio(),
				TWENTY, null, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaValorIndicador(parametro.getTpIndicadorPedagio()
				.getValue(), parametro.getVlPedagio(), parametro
				.getTabelaPreco().getIdTabelaPreco(),
				ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO, TWENTY, null, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaIndicador(FIXO, parametro.getTpIndicadorPedagio()
				.getValue(), parametro.getVlPedagio(), parametro
				.getTabelaPreco().getIdTabelaPreco(),
				ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO, TWENTY, null, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaIndicador(FRACAOQUILO, parametro
				.getTpIndicadorPedagio().getValue(), parametro.getVlPedagio(),
				parametro.getTabelaPreco().getIdTabelaPreco(),
				ConstantesExpedicao.CD_PEDAGIO_FRACAO, TWENTY, null, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaIndicador(FAIXAPESO, parametro.getTpIndicadorPedagio()
				.getValue(), parametro.getVlPedagio(), parametro
				.getTabelaPreco().getIdTabelaPreco(),
				ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO, TWENTY, null, null);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		return false;
	}
	
	private boolean findFaixaAprovacaoGrupoTotalFreteRodoviario(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		if (parametro.getPcDescontoFreteTotal() != null
				&& CompareUtils.gt(parametro.getPcDescontoFreteTotal(), ZERO)) {
			faixaAtual.setFaixaAprovacao(FAIXA_4);
			return true;
		}
		BigDecimal pcCobrancaReentrega = (BigDecimal) configuracoesFacade
				.getValorParametro("PercentualCobrancaReentrega");
		if (parametro.getPcCobrancaReentrega() != null
				&& CompareUtils.gt(parametro.getPcCobrancaReentrega(), ZERO)
				&& CompareUtils.lt(parametro.getPcCobrancaReentrega(),
						pcCobrancaReentrega)) {
			faixaAtual.setFaixaAprovacao(FAIXA_4);
			return false;
		}
		
		BigDecimal pcCobrancaDevolucao = (BigDecimal) configuracoesFacade
				.getValorParametro("PercentualCobrancaDevolucao");
		if (parametro.getPcCobrancaDevolucoes() != null
				&& CompareUtils.gt(parametro.getPcCobrancaDevolucoes(), ZERO)
				&& CompareUtils.lt(parametro.getPcCobrancaDevolucoes(),
						pcCobrancaDevolucao)) {
			faixaAtual.setFaixaAprovacao(FAIXA_4);
			return false;
		}
		
		return false;
	}
	
	private Long findFaixaDescontoIndicador(String indicador, BigDecimal valor,
			BigDecimal faixa4, BigDecimal faixa3, BigDecimal faixa2) {
		if (DESCONTO.equals(indicador) &&valor != null) {
			if (faixa4!= null && CompareUtils.gt(valor, faixa4)){
				return FAIXA_4;
			}
			if (faixa3!= null && CompareUtils.gt(valor, faixa3)){
				return FAIXA_3;
			}
			if (faixa2!= null && CompareUtils.gt(valor, faixa2)){
				return FAIXA_2;
			}
		}
		return 0L;
	}
	
	private Long findFaixaValorIndicador(String indicador, BigDecimal valor,
			Long idTabelaPreco, String cdParcelaPreco, BigDecimal faixa4,
			BigDecimal faixa3, BigDecimal faixa2) {
		return findFaixaIndicador(VALOR, indicador, valor, idTabelaPreco,
				cdParcelaPreco, faixa4, faixa3, faixa2);
	}
	
	private Long findFaixaIndicador(String indicador,
			String indicadorParametro, BigDecimal valor, Long idTabelaPreco,
			String cdParcelaPreco, BigDecimal faixa4, BigDecimal faixa3,
			BigDecimal faixa2) {
		if (indicador.equals(indicadorParametro) && valor != null) {
			BigDecimal vlGeneralidade = this.findValorParcela(idTabelaPreco,
					cdParcelaPreco);
			BigDecimal vlDesconto = null;
			if (faixa2 != null){
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, faixa2);
				if (CompareUtils.lt(valor, vlDesconto)){
					return FAIXA_2;
				}	
			}
			if (faixa3 != null){
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, faixa3);
				if (CompareUtils.lt(valor, vlDesconto)){
					return FAIXA_3;
				}	
			}
			if (faixa4 != null){
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade, faixa4);
				if(CompareUtils.lt(valor, vlDesconto)){
					return FAIXA_4;
				}	
			}			
		}
		return 0L;
	}
	
	private boolean checkIndicadorNotIn(String indicador, String[] tpIndicadores ){
		List<String> TDAList = Arrays.asList(tpIndicadores);
		if (!TDAList.contains(indicador)){
			return true;
		}
		return false;
	}
	
	private TabelaDivisaoCliente getTabelaDivisaoCliente(Cotacao cotacao){
		if (cotacao.getDivisaoCliente() != null){
			List<TabelaDivisaoCliente> tabelaDivisaoClientes = tabelaDivisaoClienteService
					.findByDivisaoCliente(cotacao.getDivisaoCliente()
							.getIdDivisaoCliente());
			if (tabelaDivisaoClientes != null
					&& !tabelaDivisaoClientes.isEmpty()) {
				return (TabelaDivisaoCliente)tabelaDivisaoClientes.get(0);
			}
		}
		return null;
	}

	private long verificaFaixaAprovacaoDescontoCotacaoAereo(Cotacao cotacao) {
		List<ParametroCliente> parametros = cotacao.getParametroClientes(); 
		ParametroCliente parametro = null;
		if( parametros != null && !parametros.isEmpty() ){
			parametro = parametros.get(0);
		}
		FaixaAprovacao faixaAtual = new FaixaAprovacao(FAIXA_1);
		if (parametro!=null){
			//Grupo frete peso
			if (findFaixaAprovacaoGrupoFretePesoAereo(parametro, faixaAtual)){
				return faixaAtual.getFaixaAprovacao();
			}
			
			//Grupos Frete valor e Frete percentual
			if (findFaixaAprovacaoGrupoFreteValorPercentualAereo(parametro,
					faixaAtual)) {
				return faixaAtual.getFaixaAprovacao();
			}
			
			//Grupo GRIS
			if (findFaixaAprovacaoGrupoGrisAereo(parametro, faixaAtual)){
				return faixaAtual.getFaixaAprovacao();
			}
			
			//Grupo pedagio
			if (!parametro.getTpIndicadorPedagio().getValue().equals(TABELA)){
				faixaAtual.setFaixaAprovacao(FAIXA_2);
				return faixaAtual.getFaixaAprovacao();
			}
			
			//Grupo Total do frete
			if (findFaixaAprovacaoGrupoTotalFreteAereo(parametro, faixaAtual)){
				return faixaAtual.getFaixaAprovacao();
			}
			
			if (findFaixaAprovacaoTdeTrtAereo(parametro, faixaAtual)){
				return faixaAtual.getFaixaAprovacao();
			}
			
			for (GeneralidadeCliente generalidade : (List<GeneralidadeCliente>) parametro
					.getGeneralidadeClientes()) {
				if (!generalidade.getTpIndicador().getValue().equals(TABELA)){
					return FAIXA_2;
				}
			}
			
			List<TaxaCliente> taxas = parametro.getTaxaClientes();
			String[] indicadores = new String[]{TABELA,DESCONTO,ACRESCIMO}; 
			if (taxas != null){
				for (TaxaCliente taxaCliente : taxas) {
					if (checkIndicadorNotIn(taxaCliente.getTpTaxaIndicador()
							.getValue(), indicadores)) {
						return FAIXA_2;
					}
					if (taxaCliente.getTpTaxaIndicador().getValue()
							.equals(DESCONTO)) {
						if (CompareUtils.gt(taxaCliente.getVlTaxa(),TWENTY)){
							return FAIXA_2;
						}
					}
					if (CompareUtils.gt(taxaCliente.getVlExcedente(),
							BigDecimal.ZERO)
							|| CompareUtils.gt(taxaCliente.getPsMinimo(),
									BigDecimal.ZERO)) {
						return FAIXA_2;
					}
				}
			}
			
			List<ServicoAdicionalCliente> servicosAdicionais = cotacao
					.getServicoAdicionalClientes();
			if (servicosAdicionais!= null && !servicosAdicionais.isEmpty()){
				for (ServicoAdicionalCliente servicoAdicionalCliente : servicosAdicionais) {
					if (!servicoAdicionalCliente.getTpIndicador().getValue()
							.equals(TABELA)) {
						return FAIXA_2;
					}
				}
			}
		}
		
		return FAIXA_1;
	}
	
	private boolean findFaixaAprovacaoGrupoTotalFreteAereo(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		if (parametro.getPcDescontoFreteTotal() != null
				&& CompareUtils.gt(parametro.getPcDescontoFreteTotal(), ZERO)) {
			faixaAtual.setFaixaAprovacao(FAIXA_2);
			return true;
		}
		BigDecimal pcCobrancaReentrega = (BigDecimal) configuracoesFacade
				.getValorParametro("PercentualCobrancaReentrega");
		if (parametro.getPcCobrancaReentrega() != null
				&& CompareUtils.lt(parametro.getPcCobrancaReentrega(),
						pcCobrancaReentrega)) {
			faixaAtual.setFaixaAprovacao(FAIXA_2);
			return false;
		}
		
		BigDecimal pcCobrancaDevolucao = (BigDecimal) configuracoesFacade
				.getValorParametro("PercentualCobrancaDevolucao");
		if (parametro.getPcCobrancaDevolucoes() != null
				&& CompareUtils.lt(parametro.getPcCobrancaDevolucoes(),
						pcCobrancaDevolucao)) {
			faixaAtual.setFaixaAprovacao(FAIXA_2);
			return false;
		}
		
		return false;
	}

	private boolean findFaixaAprovacaoGrupoFretePesoAereo(
			ParametroCliente parametro, FaixaAprovacao faixaAtual) {
		String[]indicadoresTDA = new String[]{TABELA,DESCONTO,ACRESCIMO};
		//Verificações gerais da faixa 4
		if (CompareUtils.ne(TABELA, parametro.getTpIndicadorMinFretePeso()
				.getValue())
				|| CompareUtils.ne(ZERO, parametro.getVlMinimoFreteQuilo())
				|| CompareUtils.ne(ZERO, parametro.getVlFreteVolume())
				|| CompareUtils.ne(ZERO, parametro.getPcPagaCubagem())
				|| parametro.getBlPagaPesoExcedente()
				|| parametro.getBlPagaCubagem()
				|| checkIndicadorNotIn(
						parametro.getTpTarifaMinima().getValue(),
						indicadoresTDA)
				|| checkIndicadorNotIn(parametro.getTpIndicVlrTblEspecifica()
						.getValue(), indicadoresTDA)
				|| checkIndicadorNotIn(parametro
						.getTpIndicadorPercMinimoProgr().getValue(),
						indicadoresTDA)) {
			
			faixaAtual.setFaixaAprovacao(FAIXA_2);
			return true;
		}
		
		//Faixas de desconto
		//Percentual Minimo progressivo
		Long faixa = findFaixaDescontoIndicador(parametro
				.getTpIndicadorPercMinimoProgr().getValue(),
				parametro.getVlPercMinimoProgr(), null, null, FIVE);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		
		faixa = findFaixaDescontoIndicador(parametro.getTpIndicadorFretePeso()
				.getValue(), parametro.getVlFretePeso(), null, null, FIVE);
		if (faixa > 0){
			faixaAtual.setFaixaAprovacao(faixa);
			return true;
		}
		return false;
	}

	/**
	 * Valida se algum valor informado na Cotação está fora dos limites de
	 * desconto permitidos. Se algum valor estiver fora do limite, é preciso
	 * gerar uma pendência de aprovação da proposta no workkflow.
	 * 
	 * @param idCotacao
	 * @return
	 */
	public boolean validateLimiteDescontoCotacao(Cotacao cotacao){
		
		if (cotacao.getTabelaPreco()==null){
			return true;
		}
		
		List<ParametroCliente> parametrosCliente = cotacao
				.getParametroClientes();
		
		if (parametrosCliente != null && parametrosCliente.size() > 0) {
			String tipoTabelaPreco = cotacao.getTabelaPreco()
					.getTipoTabelaPreco().getTpTipoTabelaPreco().getValue();
			Long subtipoTabelaPreco = cotacao.getTabelaPreco()
					.getSubtipoTabelaPreco().getIdSubtipoTabelaPreco();
			
			for (ParametroCliente parametroCliente : parametrosCliente) {
				if (!validateGrupoFretePeso(tipoTabelaPreco,
						subtipoTabelaPreco, parametroCliente)) {
					return false;
				}
				if (!validateGrupoFreteValor(tipoTabelaPreco,
						subtipoTabelaPreco, parametroCliente)) {
					return false;
				}
				
				if (!validateGrupoGris(cotacao.getTabelaPreco()
						.getIdTabelaPreco(), tipoTabelaPreco,
						subtipoTabelaPreco, parametroCliente)) {
					return false;
				}
				
				if (!validateGrupoPedagio(cotacao.getTabelaPreco()
						.getIdTabelaPreco(), tipoTabelaPreco,
						subtipoTabelaPreco, parametroCliente)) {
					return false;
				}
				
				if (!validateTotalFrete(tipoTabelaPreco, subtipoTabelaPreco,
						parametroCliente)) {
					return false;
				}
				
				if (!validateGeneralidades(cotacao.getTabelaPreco()
						.getIdTabelaPreco(), tipoTabelaPreco,
						subtipoTabelaPreco, parametroCliente)) {
					return false;
				}

				if (!validateGrupoTrt(cotacao.getTabelaPreco()
						.getIdTabelaPreco(), tipoTabelaPreco,
						subtipoTabelaPreco, parametroCliente)) {
					return false;
				}
				
				if (!validateGrupoTde(cotacao.getTabelaPreco()
						.getIdTabelaPreco(), tipoTabelaPreco,
						subtipoTabelaPreco, parametroCliente)) {
					return false;
				}
			}
		}
		
		return true;
	}

	private boolean validateGrupoFretePeso(String tipoTabelaPreco,
			Long subtipoTabelaPreco, ParametroCliente pc) {
		BigDecimal limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
				ConstantesExpedicao.CD_FRETE_PESO, null, subtipoTabelaPreco,
				tipoTabelaPreco, "P", "A", SessionUtils.getUsuarioLogado()
						.getIdUsuario(), null, null);
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
						|| (CompareUtils.ne(pc.getPcPagaCubagem(), ZERO) && CompareUtils
								.ne(pc.getPcPagaCubagem(),
										BigDecimalUtils.HUNDRED))
						|| !TABELA.equals(pc.getTpIndicVlrTblEspecifica()
								.getValue())
							|| CompareUtils.ne(pc.getVlTblEspecifica(), ZERO)) {
						return false;
					}
				if (DESCONTO.equals(pc.getTpIndicadorPercMinimoProgr()
						.getValue())) {
					if (CompareUtils.gt(pc.getVlPercMinimoProgr(),
							limiteDesconto)) {
							return false;
						}
					}
				if (!TABELA.equals(pc.getTpIndicadorFretePeso().getValue())
						&& !DESCONTO.equals(pc.getTpIndicadorFretePeso()
								.getValue())
						&& !ACRESCIMO.equals(pc.getTpIndicadorFretePeso()
								.getValue())) {
						return false;
					}
					if (DESCONTO.equals(pc.getTpIndicadorFretePeso().getValue())) {
						if (CompareUtils.gt(pc.getVlFretePeso(), limiteDesconto)) {
							return false;
						}
					}
				}
			} else {
			limiteDesconto = findLimiteDescontoParcela(
					ConstantesExpedicao.CD_FRETE_PESO, null, tipoTabelaPreco,
					subtipoTabelaPreco);
				if (limiteDesconto != null) {
					if (!TABELA.equals(pc.getTpIndicadorMinFretePeso().getValue())
							|| CompareUtils.ne(pc.getVlMinFretePeso(), ZERO)
							|| CompareUtils.ne(pc.getVlMinimoFreteQuilo(), ZERO)
							|| CompareUtils.ne(pc.getVlFreteVolume(), ZERO)
							|| !TABELA.equals(pc.getTpTarifaMinima().getValue())
							|| CompareUtils.ne(pc.getVlTarifaMinima(), ZERO)
							|| Boolean.TRUE.equals(pc.getBlPagaPesoExcedente())
							|| Boolean.FALSE.equals(pc.getBlPagaCubagem())
						|| (pc.getPcPagaCubagem() != null
								&& CompareUtils.ne(pc.getPcPagaCubagem(), ZERO) && CompareUtils
								.ne(pc.getPcPagaCubagem(),
										BigDecimalUtils.HUNDRED))
						|| !TABELA.equals(pc.getTpIndicVlrTblEspecifica()
								.getValue())
							|| CompareUtils.ne(pc.getVlTblEspecifica(), ZERO)) {
						return false;
					}
				if (DESCONTO.equals(pc.getTpIndicadorPercMinimoProgr()
						.getValue())) {
					if (CompareUtils.gt(pc.getVlPercMinimoProgr(),
							limiteDesconto)) {
							return false;
						}
					}
					if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
					if (!TABELA.equals(pc.getTpIndicadorFretePeso().getValue())
							&& !DESCONTO.equals(pc.getTpIndicadorFretePeso()
									.getValue())
							&& !ACRESCIMO.equals(pc.getTpIndicadorFretePeso()
									.getValue())) {
							return false;
						}
					}
					if (DESCONTO.equals(pc.getTpIndicadorFretePeso().getValue())) {
						if (CompareUtils.gt(pc.getVlFretePeso(), limiteDesconto)) {
							return false;
						}
					}
				}
			}
			return true;
	}
	
	private boolean validateGrupoFreteValor(String tipoTabelaPreco,
			Long subtipoTabelaPreco, ParametroCliente pc) {
		BigDecimal limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
				ConstantesExpedicao.CD_FRETE_VALOR, null, subtipoTabelaPreco,
				tipoTabelaPreco, "P", "A", SessionUtils.getUsuarioLogado()
						.getIdUsuario(), null, null);
			if (limiteDesconto != null) {
				if (CompareUtils.ne(limiteDesconto, BigDecimalUtils.HUNDRED)) {
				if (!TABELA.equals(pc.getTpIndicadorValorReferencia()
						.getValue())
						|| CompareUtils.ne(pc.getVlValorReferencia(), ZERO)
						|| CompareUtils.ne(pc.getPcFretePercentual(), ZERO)
						|| CompareUtils.ne(pc.getVlMinimoFretePercentual(),
								ZERO)
						|| CompareUtils.ne(pc.getVlToneladaFretePercentual(),
								ZERO)
						|| CompareUtils.ne(pc.getPsFretePercentual(), ZERO)) {
						return false;
					}
				if (!TABELA.equals(pc.getTpIndicadorAdvalorem().getValue())
						&& !DESCONTO.equals(pc.getTpIndicadorAdvalorem()
								.getValue())
						&& !ACRESCIMO.equals(pc.getTpIndicadorAdvalorem()
								.getValue())) {
						return false;
					}
				if (!TABELA.equals(pc.getTpIndicadorAdvalorem2().getValue())
						&& !DESCONTO.equals(pc.getTpIndicadorAdvalorem2()
								.getValue())
						&& !ACRESCIMO.equals(pc.getTpIndicadorAdvalorem2()
								.getValue())) {
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
			limiteDesconto = findLimiteDescontoParcela(
					ConstantesExpedicao.CD_FRETE_VALOR, null, tipoTabelaPreco,
					subtipoTabelaPreco);
				if (limiteDesconto != null) {
				if (!TABELA.equals(pc.getTpIndicadorValorReferencia()
						.getValue())
						|| CompareUtils.ne(pc.getVlValorReferencia(), ZERO)
						|| CompareUtils.ne(pc.getPcFretePercentual(), ZERO)
						|| CompareUtils.ne(pc.getVlMinimoFretePercentual(),
								ZERO)
						|| CompareUtils.ne(pc.getVlToneladaFretePercentual(),
								ZERO)
						|| CompareUtils.ne(pc.getPsFretePercentual(), ZERO)) {
						return false;
					}
					if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
					if (!TABELA.equals(pc.getTpIndicadorAdvalorem().getValue())
							&& !DESCONTO.equals(pc.getTpIndicadorAdvalorem()
									.getValue())
							&& !ACRESCIMO.equals(pc.getTpIndicadorAdvalorem()
									.getValue())) {
							return false;
						}
					if (!TABELA
							.equals(pc.getTpIndicadorAdvalorem2().getValue())
							&& !DESCONTO.equals(pc.getTpIndicadorAdvalorem2()
									.getValue())
							&& !ACRESCIMO.equals(pc.getTpIndicadorAdvalorem2()
									.getValue())) {
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
	
	private boolean validateGrupoGris(Long idTabelaPreco,
			String tipoTabelaPreco, Long subtipoTabelaPreco, ParametroCliente pc) {
		BigDecimal vlDesconto = ZERO;
		BigDecimal limiteDesconto = findLimiteDescontoParcela(
				ConstantesExpedicao.CD_GRIS, null, tipoTabelaPreco,
				subtipoTabelaPreco);
		
		if (limiteDesconto != null){
			limiteDesconto = BigDecimalUtils.HUNDRED;
			
		}
		if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
			if (!TABELA.equals(pc.getTpIndicadorPercentualGris().getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorPercentualGris()
							.getValue())
					&& !VALOR.equals(pc.getTpIndicadorPercentualGris()
							.getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorPercentualGris()
							.getValue())) {
				return false;
			}
			if (!TABELA.equals(pc.getTpIndicadorMinimoGris().getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorMinimoGris()
							.getValue())
					&& !VALOR.equals(pc.getTpIndicadorMinimoGris().getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorMinimoGris()
							.getValue())) {
				return false;
			}
		}
		if (DESCONTO.equals(pc.getTpIndicadorPercentualGris().getValue())) {
			if (CompareUtils.gt(pc.getVlPercentualGris(), limiteDesconto)) {
				return false;
			}
		}
		if (VALOR.equals(pc.getTpIndicadorPercentualGris().getValue())) {
			ParcelaPreco parcelaPreco = parcelaPrecoService
					.findByCdParcelaPreco(ConstantesExpedicao.CD_GRIS);
			Generalidade generalidade = generalidadeService.findGeneralidade(
					idTabelaPreco, parcelaPreco.getIdParcelaPreco());

			vlDesconto = BigDecimalUtils.desconto(
					generalidade.getVlGeneralidade(), limiteDesconto);
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
			ParcelaPreco parcelaPreco = parcelaPrecoService
					.findByCdParcelaPreco(ConstantesExpedicao.CD_GRIS);
			Generalidade generalidade = generalidadeService.findGeneralidade(
					idTabelaPreco, parcelaPreco.getIdParcelaPreco());

			vlDesconto = BigDecimalUtils.desconto(generalidade.getVlMinimo(),
					limiteDesconto);
			if (CompareUtils.lt(pc.getVlMinimoGris(), vlDesconto)) {
				return false;
			}
		}

		return true;
	}
	
	private boolean validateGrupoPedagio(Long idTabelaPreco,
			String tipoTabelaPreco, Long subtipoTabelaPreco, ParametroCliente pc) {
		BigDecimal vlDesconto = ZERO;
		BigDecimal limiteDesconto = findLimiteDescontoParcela(
				ConstantesExpedicao.CD_PEDAGIO, null, tipoTabelaPreco,
				subtipoTabelaPreco);
		
		if (limiteDesconto == null) {
			limiteDesconto = BigDecimalUtils.HUNDRED;
		}
		if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
			if (!TABELA.equals(pc.getTpIndicadorPedagio().getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorPedagio().getValue())
					&& !VALOR.equals(pc.getTpIndicadorPedagio().getValue())
					&& !FIXO.equals(pc.getTpIndicadorPedagio().getValue())
					&& !FAIXAPESO.equals(pc.getTpIndicadorPedagio().getValue())
					&& !FRACAOQUILO.equals(pc.getTpIndicadorPedagio()
							.getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorPedagio().getValue())) {
				return false;
			}
		}
		if (DESCONTO.equals(pc.getTpIndicadorPedagio().getValue())) {
			if (CompareUtils.gt(pc.getVlPedagio(), limiteDesconto)) {
				return false;
			}
		}
		if (VALOR.equals(pc.getTpIndicadorPedagio().getValue())) {			
			BigDecimal vlGeneralidade = this.findValorParcela(idTabelaPreco,
					ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);

			vlDesconto = BigDecimalUtils.desconto(vlGeneralidade,
					limiteDesconto);
			if (CompareUtils.lt(pc.getVlPedagio(), vlDesconto)) {
				return false;
			}
		}
		if (FIXO.equals(pc.getTpIndicadorPedagio().getValue())) {						
			BigDecimal vlGeneralidade = this.findValorParcela(idTabelaPreco,
					ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);

			vlDesconto = BigDecimalUtils.desconto(vlGeneralidade,
					limiteDesconto);
			if (CompareUtils.ne(vlGeneralidade, ZERO)
					&& CompareUtils.lt(pc.getVlPedagio(), vlDesconto)) {
				return false;
			}						
		}
		if (FRACAOQUILO.equals(pc.getTpIndicadorPedagio().getValue())) {
			BigDecimal vlGeneralidade = this.findValorParcela(idTabelaPreco,
					ConstantesExpedicao.CD_PEDAGIO_FRACAO);

			vlDesconto = BigDecimalUtils.desconto(vlGeneralidade,
					limiteDesconto);
			if (CompareUtils.ne(vlGeneralidade, ZERO)
					&& CompareUtils.lt(pc.getVlPedagio(), vlDesconto)) {
				return false;
			}				
		}
		if (FAIXAPESO.equals(pc.getTpIndicadorPedagio().getValue())) {
			BigDecimal vlGeneralidade = this.findValorParcela(idTabelaPreco,
					ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);

			vlDesconto = BigDecimalUtils.desconto(vlGeneralidade,
					limiteDesconto);
			if (CompareUtils.ne(vlGeneralidade, ZERO)
					&& CompareUtils.lt(pc.getVlPedagio(), vlDesconto)) {
				return false;
			}				
		}

		return true;
	}
	
	private boolean validateTotalFrete(String tipoTabelaPreco,
			Long subtipoTabelaPreco, ParametroCliente pc) {
		BigDecimal limiteDesconto = findLimiteDescontoParcela(
				ConstantesExpedicao.CD_TOTAL_FRETE, null, tipoTabelaPreco,
				subtipoTabelaPreco);
		if (limiteDesconto == null) {
			limiteDesconto = BigDecimalUtils.HUNDRED;
		}
		if (CompareUtils.gt(pc.getPcDescontoFreteTotal(), limiteDesconto)) {
			return false;
		}

		BigDecimal pcCobrancaReentrega = (BigDecimal) configuracoesFacade
				.getValorParametro("PercentualCobrancaReentrega");
		if (CompareUtils.ne(pc.getPcCobrancaReentrega(), ZERO)
				&& CompareUtils.lt(pc.getPcCobrancaReentrega(),
						pcCobrancaReentrega)) {
			return false;
		}
		BigDecimal pcCobrancaDevolucoes = (BigDecimal) configuracoesFacade
				.getValorParametro("PercentualCobrancaDevolucao");
		if (CompareUtils.ne(pc.getPcCobrancaDevolucoes(), ZERO)
				&& CompareUtils.lt(pc.getPcCobrancaDevolucoes(),
						pcCobrancaDevolucoes)) {
			return false;
		}
		return true;
	}

	private boolean validateGeneralidades(Long idTabelaPreco,
			String tipoTabelaPreco, Long subtipoTabelaPreco, ParametroCliente pc) {
		BigDecimal vlDesconto = ZERO;
		List<GeneralidadeCliente> generalidades = generalidadeClienteService
				.findByIdParametroClienteProposta(pc.getIdParametroCliente());
		for(GeneralidadeCliente generalidadeCliente : generalidades) {
			BigDecimal limiteDesconto = findLimiteDescontoParcela(null,
					generalidadeCliente.getParcelaPreco().getIdParcelaPreco(),
					tipoTabelaPreco, subtipoTabelaPreco);
			if (limiteDesconto == null) {
				limiteDesconto = BigDecimalUtils.HUNDRED;
			}
			if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
				if (!TABELA.equals(generalidadeCliente.getTpIndicador()
						.getValue())
						&& !DESCONTO.equals(generalidadeCliente
								.getTpIndicador().getValue())
						&& !ACRESCIMO.equals(generalidadeCliente
								.getTpIndicador().getValue())
						&& ((!VALOR.equals(generalidadeCliente.getTpIndicador()
								.getValue()) && ConstantesExpedicao.CD_DESPACHO
								.equals(generalidadeCliente.getParcelaPreco()
										.getCdParcelaPreco())) || (!FRACAOQUILO
								.equals(generalidadeCliente.getTpIndicador()
										.getValue()) && ConstantesExpedicao.CD_TAD
								.equals(generalidadeCliente.getParcelaPreco()
										.getCdParcelaPreco())))) {
					return false;
				}
			}
			if (DESCONTO
					.equals(generalidadeCliente.getTpIndicador().getValue())) {
				if (CompareUtils.gt(generalidadeCliente.getVlGeneralidade(),
						limiteDesconto)) {
					return false;
				}
			}
			if (VALOR.equals(generalidadeCliente.getTpIndicador().getValue())) {
				BigDecimal vlGeneralidade = this.findValorParcela(
						idTabelaPreco, ConstantesExpedicao.CD_DESPACHO);
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade,
						limiteDesconto);
				if (CompareUtils.lt(generalidadeCliente.getVlGeneralidade(),
						vlDesconto)) {
					return false;
		}
			}
			if (FRACAOQUILO.equals(generalidadeCliente.getTpIndicador()
					.getValue())) {
				BigDecimal vlGeneralidade = this.findValorParcela(
						idTabelaPreco, ConstantesExpedicao.CD_TAD);
				vlDesconto = BigDecimalUtils.desconto(vlGeneralidade,
						limiteDesconto);
				if (CompareUtils.lt(generalidadeCliente.getVlGeneralidade(),
						vlDesconto)) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean validateGrupoTrt(Long idTabelaPreco,
			String tipoTabelaPreco, Long subtipoTabelaPreco, ParametroCliente pc) {
		BigDecimal limiteDesconto = findLimiteDescontoParcela(
				ConstantesExpedicao.CD_TRT, null, tipoTabelaPreco,
				subtipoTabelaPreco);
		if (limiteDesconto == null) {
			limiteDesconto = BigDecimalUtils.HUNDRED;
		}
		if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
			if (!TABELA.equals(pc.getTpIndicadorPercentualTrt().getValue())
					&& !VALOR.equals(pc.getTpIndicadorPercentualTrt()
							.getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorPercentualTrt()
							.getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorPercentualTrt()
							.getValue())
					&& !TABELA.equals(pc.getTpIndicadorMinimoTrt().getValue())
					&& !VALOR.equals(pc.getTpIndicadorMinimoTrt().getValue())
					&& !DESCONTO
							.equals(pc.getTpIndicadorMinimoTrt().getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorMinimoTrt()
							.getValue())) {
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
			BigDecimal vlDesconto = this.findValorParcela(idTabelaPreco,
					ConstantesExpedicao.CD_TRT);
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
			BigDecimal vlDesconto = this.findValorMinimoParcela(idTabelaPreco,
					ConstantesExpedicao.CD_TRT);
			vlDesconto = BigDecimalUtils.desconto(vlDesconto, limiteDesconto);
			if (CompareUtils.lt(pc.getVlMinimoTrt(), vlDesconto)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean validateGrupoTde(Long idTabelaPreco,
			String tipoTabelaPreco, Long subtipoTabelaPreco, ParametroCliente pc) {
		BigDecimal limiteDesconto = findLimiteDescontoParcela(
				ConstantesExpedicao.CD_TDE, null, tipoTabelaPreco,
				subtipoTabelaPreco);
		if (limiteDesconto == null) {
			limiteDesconto = BigDecimalUtils.HUNDRED;
		}
		if (CompareUtils.lt(limiteDesconto, BigDecimalUtils.HUNDRED)) {
			if (!TABELA.equals(pc.getTpIndicadorPercentualTde().getValue())
					&& !VALOR.equals(pc.getTpIndicadorPercentualTde()
							.getValue())
					&& !DESCONTO.equals(pc.getTpIndicadorPercentualTde()
							.getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorPercentualTde()
							.getValue())
					&& !TABELA.equals(pc.getTpIndicadorMinimoTde().getValue())
					&& !VALOR.equals(pc.getTpIndicadorMinimoTde().getValue())
					&& !DESCONTO
							.equals(pc.getTpIndicadorMinimoTde().getValue())
					&& !ACRESCIMO.equals(pc.getTpIndicadorMinimoTde()
							.getValue())) {
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
			BigDecimal vlDesconto = this.findValorParcela(idTabelaPreco,
					ConstantesExpedicao.CD_TDE);
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
			BigDecimal vlDesconto = this.findValorMinimoParcela(idTabelaPreco,
					ConstantesExpedicao.CD_TDE);
			vlDesconto = BigDecimalUtils.desconto(vlDesconto, limiteDesconto);
			if (CompareUtils.lt(pc.getVlMinimoTde(), vlDesconto)) {
				return false;
			}
		}
		return true;
	}
	
	private BigDecimal findValorMinimoParcela(Long idTabelaPreco,
			String cdParcelaPreco) {
		ParcelaPreco parcelaPreco = parcelaPrecoService
				.findByCdParcelaPreco(cdParcelaPreco);
		Generalidade generalidade = generalidadeService.findGeneralidade(
				idTabelaPreco, parcelaPreco.getIdParcelaPreco());
		return generalidade.getVlMinimo();
	}
	
	/**
	 * Obtem o valor da parcela  
	 */
	private BigDecimal findValorParcela(Long idTabelaPreco,
			String cdParcelaPreco) {
		ParcelaPreco parcelaPreco = parcelaPrecoService
				.findByCdParcelaPreco(cdParcelaPreco);
		Generalidade generalidade = generalidadeService.findGeneralidade(
				idTabelaPreco, parcelaPreco.getIdParcelaPreco());
		return generalidade.getVlGeneralidade();
	}
	
	/**
	 * Método comum entre as rotinas que buscam limiteDesconto
	 * 
	 * @param cdParcelaFrete
	 * @param idParcelaPreco
	 * @param tabelaPreco
	 * @return <b>limiteDesconto</b>
	 */
	private BigDecimal findLimiteDescontoParcela(String cdParcelaFrete,
			Long idParcelaPreco, String tipoTabelaPreco,
			Long idSubtipoTabelaPreco) {
		
		BigDecimal limiteDesconto = null;
		
		Filial filialUsuario = filialService.findFilialPadraoByUsuarioEmpresa(
				SessionUtils.getUsuarioLogado(),
				SessionUtils.getEmpresaSessao());
		
		if (!ConstantesExpedicao.CD_FRETE_PESO.equals(cdParcelaFrete)
				&& !ConstantesExpedicao.CD_FRETE_VALOR.equals(cdParcelaFrete)) {
		
			limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
					cdParcelaFrete, idParcelaPreco, idSubtipoTabelaPreco,
					tipoTabelaPreco, "P", "A", SessionUtils.getUsuarioLogado()
							.getIdUsuario(), null, null);
		}
		
		if (limiteDesconto == null) {
			
			limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
					cdParcelaFrete, idParcelaPreco, idSubtipoTabelaPreco,
					tipoTabelaPreco, "P", "A", null,
					filialUsuario.getIdFilial(), null);
		}
		
		if (limiteDesconto == null) {
			
			Long idGrupoClassificacao = getGrupoClassificacaoParametroGeral();
			Long idDivisaoGrupoClassificacao = grupoClassificacaoFilialService
					.findIdDivisaoByFilialGrupoClassificacao(
							filialUsuario.getIdFilial(), idGrupoClassificacao);
			if (idDivisaoGrupoClassificacao != null) {
				limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
						cdParcelaFrete, idParcelaPreco, idSubtipoTabelaPreco,
						tipoTabelaPreco, "P", "A", null, null,
					idDivisaoGrupoClassificacao);
			}
		}
		
		if (limiteDesconto == null) {
			
			int index = 0; 
			
			List<PerfilUsuario> perfilUsuario = perfilUsuarioService
					.findByIdUsuarioPerfilUsuario(SessionUtils
							.getUsuarioLogado().getIdUsuario());
			
			Long[] idsPerfil = new Long[perfilUsuario.size()];
			for(PerfilUsuario perfil : perfilUsuario){
				idsPerfil[index] = perfil.getPerfil().getIdPerfil();
				index++;
			}
			
			limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
					cdParcelaFrete, idParcelaPreco, idSubtipoTabelaPreco,
					tipoTabelaPreco, "P", "A", null, null, null, idsPerfil);
			
		}
				
		if (limiteDesconto == null) {
			
			limiteDesconto = limiteDescontoService.findPcLimiteDesconto(
					cdParcelaFrete, idParcelaPreco, idSubtipoTabelaPreco,
					tipoTabelaPreco, "P", "A", null, null, null, new Long[] {});
			
		}
		if (limiteDesconto == null) {
			limiteDesconto = ZERO;
		}
		return limiteDesconto;
	}
	
	public boolean validateAlteracaoParametros(CalculoFrete calculoFrete){
		ParametroCliente paramCotacao = findParametroClienteByCalculoFrete(calculoFrete);
		ParametroCliente paramCalculo = calculoFrete.getParametroCliente();

		if( paramCotacao == null ){
			paramCotacao = ParametroClienteUtils.getParametroClientePadrao();
		}
		
		if (CompareUtils.neNull(paramCotacao.getVlMinFretePeso(),
				paramCalculo.getVlMinFretePeso())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlPercMinimoProgr(),
				paramCalculo.getVlPercMinimoProgr())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlFretePeso(),
				paramCalculo.getVlFretePeso())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlMinimoFreteQuilo(),
				paramCalculo.getVlMinimoFreteQuilo())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlTarifaMinima(),
				paramCalculo.getVlTarifaMinima())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlFreteVolume(),
				paramCalculo.getVlFreteVolume())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlTblEspecifica(),
				paramCalculo.getVlTblEspecifica())) {
			return false;
		}

		//Frete Valor
		if (CompareUtils.neNull(paramCotacao.getVlAdvalorem(),
				paramCalculo.getVlAdvalorem())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlAdvalorem2(),
				paramCalculo.getVlAdvalorem2())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlValorReferencia(),
				paramCalculo.getVlValorReferencia())) {
			return false;
		}

		//Frete Percentual
		if (CompareUtils.neNull(paramCotacao.getPcFretePercentual(),
				paramCalculo.getPcFretePercentual())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlMinimoFretePercentual(),
				paramCalculo.getVlMinimoFretePercentual())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlToneladaFretePercentual(),
				paramCalculo.getVlToneladaFretePercentual())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getPsFretePercentual(),
				paramCalculo.getPsFretePercentual())) {
			return false;
		}

		//Gris
		if (CompareUtils.neNull(paramCotacao.getVlMinimoGris(),
				paramCalculo.getVlMinimoGris())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlPercentualGris(),
				paramCalculo.getVlPercentualGris())) {
			return false;
		}

		//Trt
		if (CompareUtils.neNull(paramCotacao.getVlMinimoTrt(),
				paramCalculo.getVlMinimoTrt())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlPercentualTrt(),
				paramCalculo.getVlPercentualTrt())) {
			return false;
		}

		//Pedagio
		if (CompareUtils.neNull(paramCotacao.getVlPedagio(),
				paramCalculo.getVlPedagio())) {
			return false;
		}

		//Tde
		if (CompareUtils.neNull(paramCotacao.getVlMinimoTde(),
				paramCalculo.getVlMinimoTde())) {
			return false;
		}
		if (CompareUtils.neNull(paramCotacao.getVlPercentualTde(),
				paramCalculo.getVlPercentualTde())) {
			return false;
		}

		//Total Frete
		if (CompareUtils.neNull(paramCotacao.getPcDescontoFreteTotal(),
				paramCalculo.getPcDescontoFreteTotal())) {
			return false;
		}
		
		if( !validateAlteracaoGeneralidade(calculoFrete)){
			return false;
		}

		if( !validateAlteracaoTaxas(calculoFrete)){
			return false;
		}

		if( !validateAlteracaoServicoAdicional(calculoFrete)){
			return false;
		}
		
		return true;
	}
	
	private Boolean validateAlteracaoGeneralidade(CalculoFrete calculoFrete){
		List<ParcelaServico>generalidades  = calculoFrete.getGeneralidades();
		if( generalidades != null ){
			for( ParcelaServico pc : generalidades ){
				GeneralidadeCliente generalidadeCliente = (GeneralidadeCliente) pc
						.getParametro();
				if (generalidadeCliente != null
						&& generalidadeCliente.getBlAlterou()) {
					return false;
				}
			}
		}
		return true;
	}

	private Boolean validateAlteracaoTaxas(CalculoFrete calculoFrete){
		List<ParcelaServico>taxas  = calculoFrete.getTaxas();
		if( taxas != null ){
			for( ParcelaServico pc : taxas){
				TaxaCliente taxaCliente = (TaxaCliente)pc.getParametro();
				if( taxaCliente != null && taxaCliente.getBlAlterou() ){
					return false;
				}
			}
		}
		return true;
	}

	private Boolean validateAlteracaoServicoAdicional(CalculoFrete calculoFrete){
		List<ParcelaServicoAdicional> serv = calculoFrete
				.getTodosServicosAdicionais();
		if( serv != null ){
			for( ParcelaServicoAdicional pc : serv ){
				ServicoAdicionalCliente servCliente = (ServicoAdicionalCliente) pc
						.getParametro();
				if( servCliente != null && servCliente.getBlAlterou() ){
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkPerfilTelevendas(){
		String perfilTelevendas = (String) configuracoesFacade
				.getValorParametro("Perfil_Televendas");
		List<PerfilUsuario> list = perfilUsuarioService
				.findByIdUsuarioPerfilUsuario(SessionUtils.getUsuarioLogado()
						.getIdUsuario());
		for (PerfilUsuario perfilUsuario : list) {
			if (perfilTelevendas
					.equals(perfilUsuario.getPerfil().getDsPerfil())) {
				return true;
			}
		}		
		return false;
	}	
	
	private Long getGrupoClassificacaoParametroGeral() {
		return Long.valueOf(
				((BigDecimal) configuracoesFacade
						.getValorParametro(GrupoClassificacao.ID_GRUPO_CLASSIFICACAO_DESCONTOS))
						.longValue());
	}

	public List<Map<String, Object>> findCotacaoSuggest(String sgFilial, Long nrCotacao, Long idEmpresa) {
		return getCotacaoDAO().findCotacaoSuggest(sgFilial, nrCotacao, idEmpresa);
	}
	
	public void setParcelaCotacaoService(
			ParcelaCotacaoService parcelaCotacaoService) {
		this.parcelaCotacaoService = parcelaCotacaoService;
	}

	public void setParametroClienteService(
			ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}

	public LimiteDescontoService getLimiteDescontoService() {
		return limiteDescontoService;
	}

	public void setLimiteDescontoService(
			LimiteDescontoService limiteDescontoService) {
		this.limiteDescontoService = limiteDescontoService;
	}

	public void setPerfilUsuarioService(
			PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}

	public void setGrupoClassificacaoFilialService(
			GrupoClassificacaoFilialService grupoClassificacaoFilialService) {
		this.grupoClassificacaoFilialService = grupoClassificacaoFilialService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	public void setGeneralidadeService(GeneralidadeService generalidadeService) {
		this.generalidadeService = generalidadeService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setGeneralidadeClienteService(
			GeneralidadeClienteService generalidadeClienteService) {
		this.generalidadeClienteService = generalidadeClienteService;
	}

	public void setDiaUtils(DiaUtils diaUtils) {
		this.diaUtils = diaUtils;
	}

	public void setDivisaoClienteService(
			DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setTabelaDivisaoClienteService(
			TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	public void setTaxaClienteService(TaxaClienteService taxaClienteService) {
		this.taxaClienteService = taxaClienteService;
	}

	public void setDimensaoService(DimensaoService dimensaoService) {
		this.dimensaoService = dimensaoService;
	}

	public void setServicoAdicionalClienteService(
			ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}

	public void setServAdicionalDocServService(
			ServAdicionalDocServService servAdicionalDocServService) {
		this.servAdicionalDocServService = servAdicionalDocServService;
	}

	public void setCalculoFreteService(CalculoFreteService calculoFreteService) {
		this.calculoFreteService = calculoFreteService;
	}

	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}

	public void setCalculoParcelaFreteService(
			CalculoParcelaFreteService calculoParcelaFreteService) {
		this.calculoParcelaFreteService = calculoParcelaFreteService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setTipoLocalizacaoMunicipioService(
			TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	public void executeWorkflowDimensoesAeronave(Long idCotacao, String situacao) {
		Map cotacaoMap = getCotacaoDAO().findCotacaoById(idCotacao);
		Map mapPendencia = MapUtilsPlus.getMap(cotacaoMap, "pendenciaDimensoes", null);
		Long idPendenciaDimensoes = MapUtilsPlus.getLong(mapPendencia, "idPendencia");
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idCotacao", idCotacao);
		parametersValues.put("tpSituacaoAprovacaoDimensoes", situacao);
		
		if (ConstantesWorkflow.APROVADO.equals(situacao)) {
			List<Acao> l = (List<Acao>)acaoService.findByIdPendenciaTpSituacaoAcao(idPendenciaDimensoes, "A");
			Acao a = l.get(0);
			parametersValues.put("idUsuarioAprovouDimensoes", a.getUsuario().getIdUsuario());
			parametersValues.put("dtAprovacaoDimensoes", a.getDhAcao().toYearMonthDay());
			
			Map tpSituacaoAprovacaoMap = MapUtilsPlus.getMap(cotacaoMap, "tpSituacaoAprovacao", null);
			if (ConstantesWorkflow.APROVADO.equals(MapUtilsPlus.getString(tpSituacaoAprovacaoMap, "value"))) {
				parametersValues.put("tpSituacao", ConstantesWorkflow.APROVADO);
			}
			
		} else {
			parametersValues.put("idUsuarioAprovouDimensoes", null);
			parametersValues.put("dtAprovacaoDimensoes", null);
			
			if (ConstantesWorkflow.REPROVADO.equals(situacao)) {
				parametersValues.put("tpSituacao", ConstantesWorkflow.REPROVADO);
			}
		}
		
		getCotacaoDAO().updateCotacaoByWorkflowDimensoes(parametersValues);
	}

	public ValorFaixaProgressivaPropostaService getValorFaixaProgressivaPropostaService() {
		return valorFaixaProgressivaPropostaService;
	}

	public void setValorFaixaProgressivaPropostaService(
			ValorFaixaProgressivaPropostaService valorFaixaProgressivaPropostaService) {
		this.valorFaixaProgressivaPropostaService = valorFaixaProgressivaPropostaService;
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
	
	public void setEmbarqueValidationService(EmbarqueValidationService embarqueValidationService) {
		this.embarqueValidationService = embarqueValidationService;
	}
	
}