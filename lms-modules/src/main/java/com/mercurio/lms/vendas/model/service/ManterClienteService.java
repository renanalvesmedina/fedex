/**
 * 
 */
package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.ConstantesMunicipios;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.WarningCollector;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.AnaliseCreditoCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.TrtCliente;
import com.mercurio.lms.vendas.model.dao.ClienteDAO;
import com.mercurio.lms.vendas.util.ClienteUtils;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;


/**
 * @author Luis Carlos Poletto
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.manterClienteService"
 */
public class ManterClienteService extends CrudService<Cliente, Long> {
	private static final String TP_FORMA_ARREDONDAMENTO_PADRAO = "P";
	private static final String TP_FREQUENCIA_VISITA_MENSAL = "M";
	private static final Long NR_REENTREGAS_COBRANCA_DEFAULT = 1L;
	private static final String TP_DIFICULDADE_COLETA_DEFAULT = "0";
	private static final String TP_DIFICULDADE_CLASSIFICACAO_DEFAULT= "0"; 
	private static final String TP_DIFICULDADE_ENTREGA_DEFAULT = "0";
	private static final String TP_COBRANCA_BOLETO = "3";
	private static final BigDecimal PC_JURO_DIARIO_DEFAULT = BigDecimal.ZERO;
	private static final DomainValue DM_INATIVO = new DomainValue("I");
	private static final DomainValue DM_ATIVO = new DomainValue("A");
	
	private ConfiguracoesFacade configuracoesFacade;
	private PessoaService pessoaService;
	private DivisaoClienteService divisaoClienteService;
	private ParametroClienteService parametroClienteService;
	private InscricaoEstadualService inscricaoEstadualService;
	private UnidadeFederativaService unidadeFederativaService;
	private ClienteService clienteService;
	private UsuarioService usuarioService;
	private FilialService filialService;

	private ClassificacaoClienteService classificacaoClienteService;
	private GerenciaRegionalService gerenciaRegionalService;
	private MunicipioRegionalClienteService municipioRegionalClienteService;
	private LiberacaoEmbarqueService liberacaoEmbarqueService;
	private ColetaAutomaticaClienteService coletaAutomaticaClienteService;
	private HorarioCorteClienteService horarioCorteClienteService;
	private CiaAereaClienteService ciaAereaClienteService;
	private ClienteRegiaoService clienteRegiaoService;
	private ClienteDespachanteService clienteDespachanteService;
	private DocumentoClienteService documentoClienteService;
	private EventoClienteService eventoClienteService;
	private OcorrenciaClienteService ocorrenciaClienteService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private AnaliseCreditoClienteService analiseCreditoClienteService;
	private PrazoVencimentoService prazoVencimentoService;
	private DiaFaturamentoService diaFaturamentoService;
	private ClienteOperadorLogisticoService clienteOperadorLogisticoService;
	private TrtClienteService trtClienteService;
	private WorkflowPendenciaService workflowPendenciaService; 
	private HistoricoWorkflowService historicoWorkflowService;
	private ParametroGeralService parametroGeralService;

	public Pessoa findPessoa(String nrIdentificacao, String tpIdentificacao) {
		if (configuracoesFacade.getPessoa(nrIdentificacao, tpIdentificacao, Cliente.class, true) != null) {
			throw new BusinessException("LMS-01022");
		}
		return configuracoesFacade.getPessoa(nrIdentificacao, tpIdentificacao);
	}

	public ResultSetPage<TypedFlatMap> findPaginated(TypedFlatMap criteria) {	
		
		if ( (criteria.get("pessoa.nmPessoa") == null || "".equals(criteria.get("pessoa.nmPessoa"))) &&
			 (criteria.get("nmFantasia") == null || "".equals(criteria.get("nmFantasia"))) &&
			 (criteria.get("nrConta") == null || "".equals(criteria.get("nrConta"))) &&
			 (criteria.get("pessoa.nrIdentificacao") == null || "".equals(criteria.get("pessoa.nrIdentificacao"))) ) {
			throw new BusinessException("LMS-01104");
		}
		
		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			criteria.put("pessoa.nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		}

		ResultSetPage<TypedFlatMap> rsp = getClienteDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List<TypedFlatMap> list = rsp.getList();

		String nrIdentificacaoFormatado = "";
		for(TypedFlatMap cliente : list) {
			nrIdentificacao = cliente.getString("pessoa.nrIdentificacao");
			if(!StringUtils.isBlank(nrIdentificacao)) {
				nrIdentificacaoFormatado = FormatUtils.formatIdentificacao(cliente.getString("pessoa.tpIdentificacao.value"), nrIdentificacao);
			} else {
				nrIdentificacaoFormatado = "";
			}
			cliente.put("pessoa.nrIdentificacaoFormatado", nrIdentificacaoFormatado);
		}
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			criteria.put("pessoa.nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		}
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getClienteDAO().getRowCount(criteria, def);
	}

	@Override
	public Cliente findById(Long id) {
		Cliente cliente = (Cliente) super.findById(id);
		cliente.setPessoa(cliente.getPessoa());
		if (cliente.getClienteMatriz() != null) {
			cliente.getClienteMatriz().setPessoa(cliente.getClienteMatriz().getPessoa());
		}
		return cliente;
	}

	/**
	 * Recupera uma instância de <code>Cliente</code> a partir do ID para detalhar a tela cliente.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Map<String, Object> findDadosIdentificacao(Long id) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) getClienteDAO().findDadosIdentificacao(id);
		if (!list.isEmpty()) {
			TypedFlatMap clienteMapped = AliasToTypedFlatMapResultTransformer.getInstance().transformeTupleMap(list.get(0));

			/** Regras que verificam a Analise de Credito do Cliente */
			if(ConstantesVendas.CLIENTE_ESPECIAL.equals(clienteMapped.getString("tpCliente.value"))) {
				Long idFilialComercial = clienteMapped.getLong("filialByIdFilialAtendeComercial.idFilial");
				/** Valida se o usuário tem permissão de acesso a filial comercial */
			    Boolean hasAccess = usuarioService.validateAcessoFilialRegionalUsuario(idFilialComercial);
				if(Boolean.TRUE.equals(hasAccess)) {
					/** Verifica Análise de Crédito se:
					 *  - A filial responsável comercial do cliente já estiver com o processo de análise de crédito implantando; 
					 *  - E se a data atual do sistema é maior ou igual dtImpAnaliseCreditoFilial. */
					YearMonthDay dtImpAnaliseCreditoFilial = (YearMonthDay) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialComercial, "DT_IMP_ANA_CREDITO", false);
					if(dtImpAnaliseCreditoFilial != null && CompareUtils.ge(JTDateTimeUtils.getDataAtual(), dtImpAnaliseCreditoFilial)) {
						/** Verifica se existe Analise Financeira */
						AnaliseCreditoCliente analiseCreditoCliente = analiseCreditoClienteService.findByIdCliente(clienteMapped.getLong("idCliente"));
						if(analiseCreditoCliente != null) {
							clienteMapped.put("analiseCreditoCliente.idAnaliseCreditoCliente", analiseCreditoCliente.getIdAnaliseCreditoCliente());
							clienteMapped.put("analiseCreditoCliente.tpSituacao", analiseCreditoCliente.getTpSituacao().getValue());
						}
					} else hasAccess = Boolean.FALSE;
			    }
				clienteMapped.put("hasAccess", hasAccess);
			}
			return clienteMapped;
		} else {
			return null;
		}
	}
	
	private void validateHistoricoWorkflow(List<Long> ids){
		for (Long id : ids) {
			if(historicoWorkflowService.validateHistoricoWorkflow(id, TabelaHistoricoWorkflow.CLIENTE)){
				Cliente cliente = this.findById(id);
				String nmNrPessoa = this.getNmNrPessoa(cliente);
				throw new BusinessException("LMS-01241", new String[]{nmNrPessoa});
			}
		}
	}
	
	private String getNmNrPessoa(Cliente cliente) {
		StringBuilder nmPessoa = new StringBuilder();
		if (cliente != null && cliente.getPessoa() != null) {
			nmPessoa.append(FormatUtils.formatIdentificacao(cliente.getPessoa()));
			nmPessoa.append(" - ");
			nmPessoa.append(cliente.getPessoa().getNmPessoa());
		}
		return nmPessoa.toString();
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 *  
	 */
	@Override
	@Transactional(propagation = Propagation.NEVER)
	public void removeById(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		validateHistoricoWorkflow(ids);
		clienteOperadorLogisticoService.removeByIdClienteOperador(id);
		getClienteDAO().removeById(id);
		try{
			pessoaService.removeById(id);	
		} catch (Exception e) {
			//ignora de Pessoa
		}
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List<Long> ids) {
		validateHistoricoWorkflow(ids);
		super.removeByIds(ids);
		for(Long id : ids) {
			try {
				clienteOperadorLogisticoService.removeByIdClienteOperador(id);
				pessoaService.removeById(id);
			} catch(Exception e) {
				//sem tratamentos, simplesmente ignora. Pois, provavelmente, há dependencias da Pessoa.
			}
		}
	}

	@Override
	public Serializable store(Cliente bean) {
		beforeStore(bean);
		if (bean.getIdCliente()!=null &&  Boolean.FALSE.equals( bean.getBlOperadorLogistico())){
			clienteOperadorLogisticoService.removeByIdClienteOperador(bean.getIdCliente());
		}
		bean.setDefaultTpFormaCobranca();
		getClienteDAO().store(bean);
		return bean.getIdCliente();
	}

	public java.io.Serializable storeConcluirCadastro(Cliente cliente) {

		beforeStore(cliente);

		// executar as regras de conclusão de cadastro
		validateConcluirCadastro(cliente);

		insertIEPadraoPessoaFisica(cliente.getPessoa());

		cliente.setDefaultTpFormaCobranca();
		
		// salvar
		getClienteDAO().store(cliente);

		return cliente.getIdCliente(); 	
	}

	@Override
	protected Cliente beforeStore(Cliente cliente) {

		//Regra modificada devido a requisição de número - 10671, onde define esses valores como zero.
		cliente.setPcDescontoFreteCif(BigDecimal.ZERO);
		cliente.setPcDescontoFreteFob(BigDecimal.ZERO);

		copiaClienteMatriz(cliente);

		// valida se empresa da filial responsavel eh do tipo Parceira
		validateRegionaisResponsaveis(cliente);

		//Selecionar "Emite boleto para cliente destino" ou "Gera recibo de frete na entrega" exclusivamente.
		if(Boolean.TRUE.equals(cliente.getBlEmiteBoletoCliDestino())
				&& Boolean.TRUE.equals(cliente.getBlGeraReciboFreteEntrega())
		) {
			throw new BusinessException("LMS-01140");
		}

		if (cliente.getNrReentregasCobranca() < 1 || cliente.getNrReentregasCobranca() > 9){
			throw new BusinessException("LMS-01171");
		}

		/* 
	 	   Regra modificada devido a requisição de número - 11782, onde define a obrigatoriedade do campo de faturamento previsto 
		   se o tipo de cliente for Especial. 
		*/
		if ( (ConstantesVendas.CLIENTE_ESPECIAL.equals(cliente.getTpCliente().getValue())) && (!BigDecimalUtils.hasValue(cliente.getVlFaturamentoPrevisto()))) {
			throw new BusinessException("LMS-00001", new Object[]{configuracoesFacade.getMensagem("faturamentoPrevisto")});
		}

		// atribuir valor para o boolean responsável de frete
		cliente.setBlResponsavelFrete(Boolean.TRUE);

		// atribui o id do usuario que fez o update
		cliente.setUsuarioByIdUsuarioAlteracao(SessionUtils.getUsuarioLogado());

		// se o responsável for nulo, atribuir ele mesmo
		if (cliente.getCliente() == null || cliente.getCliente().getIdCliente() == null ) {
			cliente.setCliente(cliente);
		}else {
			cliente.setCliente(findById(cliente.getCliente().getIdCliente()));
		}

		if (cliente.getClienteMatriz() != null && cliente.getClienteMatriz().getIdCliente() == null){
			cliente.setClienteMatriz(null);
		}

		cliente = super.beforeStore(cliente);
		
		// faz uma copia da pessoa, a fim de nao perder os dados que nao estao na tela
		pessoaService.store(cliente);
		getDao().flush();
		
		return cliente;
	}

	/**
	 * Verifica se a Empresa da Filial Responsável(Comercial, Operacional e Financeiro) é do tipo "Parceira"
	 * Caso contrário, é obrigatório informar a Regional Responsável da mesma.
	 * @author Andre Valadas 
	 * @param cliente
	 */
	private void validateRegionaisResponsaveis(Cliente cliente) {
		/** Filial Comercial */
		Map<String, Object> filialComercial = filialService.findSgFilialTpEmpresaByIdFilial(cliente.getFilialByIdFilialAtendeComercial().getIdFilial());
		DomainValue tpEmpresa = (DomainValue) filialComercial.get("tpEmpresa");
		if(!ConstantesMunicipios.TP_EMPRESA_PARCEIRO.equals(tpEmpresa.getValue())) {
			if(cliente.getRegionalComercial() == null) {
				throw new BusinessException("requiredField", new Object[]{new DefaultMessageSourceResolvable("regionalResponsavelComercial")});
			}
		}
		/** Filial Operacional */
		tpEmpresa = (DomainValue) filialComercial.get("tpEmpresa");
		if(!ConstantesMunicipios.TP_EMPRESA_PARCEIRO.equals(tpEmpresa.getValue())) {
			if(cliente.getRegionalOperacional() == null) {
				throw new BusinessException("requiredField", new Object[]{new DefaultMessageSourceResolvable("regionalResponsavelOperacional")});
			}
		}
		/** Filial Financeiro */
		if (ClienteUtils.isParametroClienteEspecial(cliente.getTpCliente().getValue())) {
			tpEmpresa = (DomainValue) filialComercial.get("tpEmpresa");
			if(!ConstantesMunicipios.TP_EMPRESA_PARCEIRO.equals(tpEmpresa.getValue())) {
				if(cliente.getRegionalFinanceiro() == null) {
					throw new BusinessException("requiredField", new Object[]{new DefaultMessageSourceResolvable("regionalResponsavelFinanceiro")});
				}
			}
		}
	}

	@Override
	protected Cliente beforeInsert(Cliente cliente) {
		Pessoa pessoa = cliente.getPessoa();

		String nrIdentificacao = pessoa.getNrIdentificacao();
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			Object pessoaExiste = configuracoesFacade.getPessoa(nrIdentificacao, pessoa.getTpIdentificacao().getValue(), Cliente.class, true);
			if(pessoaExiste != null) {
				throw new BusinessException("LMS-01022");
			}
		}

		// incluir pessoa se não existe
		pessoa.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());

		// atribui o id do usuário de inserção
		cliente.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());

		// atribui a data de inserção
		cliente.setDtGeracao(JTDateTimeUtils.getDataAtual());

		Long nrConta = this.getClienteDAO().findNewNrConta();
		if (nrConta != null) {
			cliente.setNrConta(nrConta);
		}

		return super.beforeInsert(cliente);
	}

	@Override
	protected Cliente beforeUpdate(Cliente cliente) {
		executeTpClienteChange(cliente, false);

		executeFilialSolicitadaChange(cliente);
		
		validateConcluirCadastro(cliente);

		return super.beforeUpdate(cliente);
	}

	/**
	 * Desativa a divisão e parametrizações de um cliente que deixa de ser 
	 * especial.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void executeDesativarParametrizacoes(Long idCliente) {
		List<DivisaoCliente> divisoes = divisaoClienteService.findDivisoesByIdCliente(idCliente);
		if (divisoes != null) {
			for (DivisaoCliente divisao : divisoes) {
				if(DM_ATIVO.equals(divisao.getTpSituacao())){
					executeDesativarParametrizacao(divisao);
				}
			}
		}
	}

	private void executeDesativarParametrizacao(DivisaoCliente divisaoCliente) {
		divisaoCliente.setTpSituacao(DM_INATIVO);
		divisaoClienteService.store(divisaoCliente);

		List<TabelaDivisaoCliente> tdcs = divisaoCliente.getTabelaDivisaoClientes();
		if (tdcs != null) {
            for(TabelaDivisaoCliente tdc : tdcs) {
                List<ParametroCliente> parametros = tdc.getParametroClientes();
                if(parametros != null) {
                    for(ParametroCliente pc : parametros) {
                        pc.setTpSituacaoParametro(DM_INATIVO);
                        pc.setDtVigenciaFinal(JTDateTimeUtils.getDataAtual().minusDays(1));
                        parametroClienteService.store(pc);
                    } // fim do for parametros
                } // fim do if parametros
            } // fim do for tdcs
        } // fim do if tdcs
	}

	/**
	 * Desativa os municipios TRT de um cliente que deixa de ser 
	 * especial ou filial de cliente especial.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void executeDesativarMunicipiosTrt(Long idCliente) {		
		/*-	Verificar se o cliente possui municípios com cobrança de TRT cadastrados e vigentes.*/
		List<TrtCliente> trtClientes = trtClienteService.findTrtVigenteByIdCliente(idCliente);
		/*-	Se possuir encerrar a vigência dos municípios com cobrança de TRT cadastrados para o cliente*/
		if (trtClientes != null && !trtClientes.isEmpty()) {
			for (TrtCliente trtCliente : trtClientes){				
				/*Se DT_VIGENCIA_INICIAL = sysdate (formatado sem hora)     
				 * 	 Então DATA_INICIAL = sysdate (formatado sem hora) – 1 
				 *      Senão DATA_INICIAL = DT_VIGENCIA_INICIAL*/
				YearMonthDay dtInicial;
				if (trtCliente.getDtVigenciaInicial().equals(JTDateTimeUtils.getDataAtual()))
					dtInicial = JTDateTimeUtils.getDataAtual().minusDays(1);
				else
					dtInicial = trtCliente.getDtVigenciaInicial();
				trtCliente.setDtVigenciaInicial(dtInicial);
				YearMonthDay dtFinal = JTDateTimeUtils.getDataAtual().minusDays(1);
				trtCliente.setDtVigenciaFinal(dtFinal);
			}
			trtClienteService.storeAll(trtClientes);
		}
	}

	/**
	 * Se tipo cliente igual a <code>Filial de Cliente Especial</code>
	 * realiza a cópias dos dados financeiros da matriz.
	 * @param cliente
	 */
	private void copiaClienteMatriz(Cliente cliente) {
		if(!ConstantesVendas.CLIENTE_FILIAL.equals(cliente.getTpCliente().getValue())) {
			return;
		}
		// Valida se cliente matriz foi informado
		if(cliente.getClienteMatriz() == null) {
			throw new BusinessException("requiredField", new Object[]{new DefaultMessageSourceResolvable("clienteMatriz")});
		}
		// Valida para que Cliente e Cliente matriz não sejam iguais.
		if( cliente.getIdCliente() != null	&& cliente.getIdCliente().equals(cliente.getClienteMatriz().getIdCliente()) ) {
			throw new BusinessException("LMS-01042");
		}

		/**
		 * Valida:
		 * 	Se a filial do usuário logado não for a Matriz<br>
		 *  E o ‘Cliente matriz’ estiver preenchido<br>
		 *   com uma identificação que não tenha os primeiros oito caracteres iguais à identificação do cliente,<br>
		 *  @thorw LMS-01170.
		 */
		boolean isFilialUsuarioMatriz = SessionUtils.isFilialSessaoMatriz();
		Cliente clienteMatriz = this.findById(cliente.getClienteMatriz().getIdCliente());
		if(!isFilialUsuarioMatriz) {
			if(!StringUtils.left(cliente.getPessoa().getNrIdentificacao(), 8).equals(StringUtils.left(clienteMatriz.getPessoa().getNrIdentificacao(),8))) {
				throw new BusinessException("LMS-01170");
			}
		}
		
		if (cliente.getCliente() == null) {
			if (clienteMatriz.equals(clienteMatriz.getCliente())) {
				cliente.setCliente(cliente);
			} else {
				cliente.setCliente(clienteMatriz.getCliente());
			}	
		}

		cliente.setBlGeraReciboFreteEntrega(clienteMatriz.getBlGeraReciboFreteEntrega());
		cliente.setBlIndicadorProtesto(clienteMatriz.getBlIndicadorProtesto());
		cliente.setPcDescontoFreteCif(clienteMatriz.getPcDescontoFreteCif());
		cliente.setPcDescontoFreteFob(clienteMatriz.getPcDescontoFreteFob());
		cliente.setMoedaByIdMoedaLimCred(clienteMatriz.getMoedaByIdMoedaLimCred());
		cliente.setMoedaByIdMoedaLimDoctos(clienteMatriz.getMoedaByIdMoedaLimDoctos());
		cliente.setMoedaByIdMoedaSaldoAtual(clienteMatriz.getMoedaByIdMoedaSaldoAtual());
		cliente.setCedente(clienteMatriz.getCedente());
		cliente.setBlEmiteBoletoCliDestino(clienteMatriz.getBlEmiteBoletoCliDestino());
		cliente.setVlLimiteCredito(clienteMatriz.getVlLimiteCredito());
		cliente.setNrDiasLimiteDebito(clienteMatriz.getNrDiasLimiteDebito());
		// alteracao de especificacao em 13/02/2007
		cliente.setVlSaldoAtual(BigDecimalUtils.ZERO);
		cliente.setPcJuroDiario(clienteMatriz.getPcJuroDiario());
		cliente.setVlLimiteDocumentos(clienteMatriz.getVlLimiteDocumentos());
		cliente.setTpCobranca(clienteMatriz.getTpCobranca());
		cliente.setTpMeioEnvioBoleto(clienteMatriz.getTpMeioEnvioBoleto());
		cliente.setBlAgrupaFaturamentoMes(clienteMatriz.getBlAgrupaFaturamentoMes());
		cliente.setTpPeriodicidadeTransf(clienteMatriz.getTpPeriodicidadeTransf());
		cliente.setBlRessarceFreteFob(clienteMatriz.getBlRessarceFreteFob());
		cliente.setBlPreFatura(clienteMatriz.getBlPreFatura());
		cliente.setBlFaturaDocsEntregues(clienteMatriz.getBlFaturaDocsEntregues());
		cliente.setBlCobrancaCentralizada(clienteMatriz.getBlCobrancaCentralizada());
		cliente.setBlFaturaDocsConferidos(clienteMatriz.getBlFaturaDocsConferidos());
		cliente.setBlFronteiraRapida(clienteMatriz.getBlFronteiraRapida());
		cliente.setBlFaturaDocReferencia(clienteMatriz.getBlFaturaDocReferencia());
		cliente.setBlNaoAtualizaDBI(clienteMatriz.getBlNaoAtualizaDBI());
		//Jira LMS-7784
		cliente.setBlSeparaFaturaModal(clienteMatriz.getBlSeparaFaturaModal());
		cliente.setBlCalculoArqPreFatura(clienteMatriz.getBlCalculoArqPreFatura());
		cliente.setBlEmiteDacteFaturamento(clienteMatriz.getBlEmiteDacteFaturamento());
		cliente.setBlMtzLiberaRIM(clienteMatriz.getBlMtzLiberaRIM());
		cliente.setObFatura(clienteMatriz.getObFatura());
		cliente.setBlEnviaDacteXmlFat(clienteMatriz.getBlEnviaDacteXmlFat());
		//LMSA-3959
		cliente.setBlDpeFeriado(clienteMatriz.getBlDpeFeriado());
		
		cliente.setClienteMatriz(clienteMatriz);

	}

	private void insertIEPadraoPessoaFisica(Pessoa pessoa){
		if ("F".equals(pessoa.getTpPessoa().getValue())){
			InscricaoEstadual inscricaoEstadual = new InscricaoEstadual();

			inscricaoEstadual.setPessoa(pessoa);
			inscricaoEstadual.setUnidadeFederativa(unidadeFederativaService.findByIdPessoa(pessoa.getIdPessoa()));
			inscricaoEstadual.setNrInscricaoEstadual("ISENTO");
			inscricaoEstadual.setBlIndicadorPadrao(Boolean.TRUE);
			inscricaoEstadual.setTpSituacao(new DomainValue(ConstantesVendas.SITUACAO_ATIVO));

			inscricaoEstadualService.storeInscricaoEstadual(inscricaoEstadual);
		}
	}

	/**
	 * Regras executadas na hora de concluir um cadastro
	 * 
	 * @param Cliente cliente da tela
	 * @param Long idPessoa que foi inserido
	 */
	private void validateConcluirCadastro(Cliente cliente) {
		String tpSituacao = cliente.getTpSituacao().getValue();
		if (ConstantesVendas.SITUACAO_INATIVO.equals(tpSituacao)) {
			return;
		}

		Long idCliente = cliente.getIdCliente();
		String tpCliente = cliente.getTpCliente().getValue();
		String tpPessoa = cliente.getPessoa().getTpPessoa().getValue();
		if("J".equals(tpPessoa)) {
			// Verificar se a pessoa tem inscrição estadual
			if (!pessoaService.verifyInscricaoEstadualPadraoAtiva(idCliente)) {
				new WarningCollector(configuracoesFacade.getMensagem("LMS-01021"));
				throw new BusinessException("LMS-01021");
			}
		}

		if (ClienteUtils.isParametroClienteEspecial(tpCliente)) {
			// Verificar se a pessoa tem telefone
			if (!pessoaService.verificaExistenciaTelefoneEndereco(idCliente)) {
				new WarningCollector(configuracoesFacade.getMensagem("LMS-01020"));
				throw new BusinessException("LMS-01020");
			}
			// Verificar se a pessoa tem contato
			if (!pessoaService.verificaExistenciaContato(idCliente)) {
				new WarningCollector(configuracoesFacade.getMensagem("LMS-01018"));
				throw new BusinessException("LMS-01018");
			}
			 
			if ( verificaNecessidadeContatoFaturamento() && !verificaExistenciaContatoFaturamento(idCliente) ){
				new WarningCollector(configuracoesFacade.getMensagem("LMS-01262"));
				throw new BusinessException("LMS-01262");
		}
		}

		if ("J".equals(tpPessoa)) {
			if (!pessoaService.verificaExistenciaEnderecoTipoEndereco(idCliente, "COM")) {
				new WarningCollector(configuracoesFacade.getMensagem("LMS-01158"));
				throw new BusinessException("LMS-01158");
			}
		} else if ("F".equals(tpPessoa)) {
			if (!pessoaService.verificaExistenciaEnderecoTipoEndereco(idCliente, "RES")) {
				new WarningCollector(configuracoesFacade.getMensagem("LMS-01158"));
				throw new BusinessException("LMS-01158");
			}
		}

		// Se o tipo de cliente for igual a "Especial"
		if (ConstantesVendas.CLIENTE_ESPECIAL.equals(tpCliente)) {
			if (divisaoClienteService.verificaExistenciaDivisaoCliente(idCliente).equals(Boolean.FALSE)) {
				new WarningCollector(configuracoesFacade.getMensagem("LMS-01108"));
				throw new BusinessException("LMS-01108");
			}

			/** Se Cliente Inativo, verifica se existe a Analise de Crédito */
			if (ConstantesVendas.SITUACAO_INCOMPLETO.equals(tpSituacao) 
					&& cliente.getTpClienteSolicitado() != null
					&& !cliente.getTpClienteSolicitado().getValue().equals(ConstantesVendas.CLIENTE_EVENTUAL)
					&& !cliente.getTpClienteSolicitado().getValue().equals(ConstantesVendas.CLIENTE_POTENCIAL)) {
				
				/** Verifica Análise de Crédito se:
				 *  - A filial responsável comercial do cliente já estiver com o processo de análise de crédito implantando; 
				 *  - E se a data atual do sistema é maior ou igual dtImpAnaliseCreditoFilial. */
				
				/* Verifica se existe prazo de vencimento para o cliente informado onde
				 * nrPrazoPagamentoSolicitado não é nulo*/
				List prazoVencimentos = prazoVencimentoService.findPrazoVencimentoSolicitado(idCliente);
				Boolean existePrazoSolicitado = (prazoVencimentos != null && !prazoVencimentos.isEmpty());    
				
				/* Verifica se existe Dia de faturamento com tpPeriodicidadeSolicitado não nulo para
				 * cliente*/
				List diasFaturamento = diaFaturamentoService.findDiaFaturamentoSolicitado(idCliente);
				Boolean existeDiaSolicitado = (diasFaturamento != null && !diasFaturamento.isEmpty());    
				
				YearMonthDay dtImpAnaliseCreditoFilial = (YearMonthDay) conteudoParametroFilialService.findConteudoByNomeParametro(cliente.getFilialByIdFilialAtendeComercial().getIdFilial(), "DT_IMP_ANA_CREDITO", false);
				if(dtImpAnaliseCreditoFilial != null 
						&& CompareUtils.ge(JTDateTimeUtils.getDataAtual(), dtImpAnaliseCreditoFilial)  
							&& cliente.getTpCobrancaSolicitado() != null 
								&& existePrazoSolicitado && existeDiaSolicitado) {
					AnaliseCreditoCliente analiseCreditoCliente = analiseCreditoClienteService.findByIdCliente(idCliente);
					if(analiseCreditoCliente == null 
							|| Boolean.FALSE.equals(analiseCreditoCliente.getBlCreditoLiberado())
							|| !"C".equals(analiseCreditoCliente.getTpSituacao().getValue())) {
						new WarningCollector(configuracoesFacade.getMensagem("LMS-01039"));
						throw new BusinessException("LMS-01039");
					}
				}
			}
		}

		/** Seta Cliente para ATIVO caso NAO ocorra exceções */
		cliente.setTpSituacao(new DomainValue(ConstantesVendas.SITUACAO_ATIVO));
	}

	private boolean verificaExistenciaContatoFaturamento(Long idCliente) {
		return pessoaService.verificaExistenciaContatoFaturamento(idCliente);
	}

	private boolean verificaNecessidadeContatoFaturamento() {
		ParametroGeral parametro = parametroGeralService.findByNomeParametro("BL_EXIGE_EMAIL_FAT_CAD_CLI");
		return parametro != null && "S".equalsIgnoreCase(parametro.getDsConteudo());
	}

	public Map<String, Boolean> validateWorkflowPendenciaAprovacaoCliente(Long idCliente) {
		List<String> camposCliente = new ArrayList<String>();
		camposCliente.add(CampoHistoricoWorkflow.TPCL.name());
		camposCliente.add(CampoHistoricoWorkflow.FCOM.name());
		camposCliente.add(CampoHistoricoWorkflow.FOPE.name());
		camposCliente.add(CampoHistoricoWorkflow.FCOB.name());
		
		return historicoWorkflowService.findPendenciaAprovacaoByTabelaCampos(idCliente, TabelaHistoricoWorkflow.CLIENTE, camposCliente);
	}
	
	public void executeAprovacaoWorkflowTpCliente(Long idCliente){
		Cliente cliente = this.findById(idCliente);
		String nmTrocaTpCliente = executeTpClienteChange(cliente, true);
		updateDadosClienteByTrocaTpCliente(cliente, nmTrocaTpCliente);
	}

	/**
	 * 
	 * @param nmTrocaTpCliente
	 */
	private void updateDadosClienteByTrocaTpCliente(Cliente cliente, String nmTrocaTpCliente){

		if(ConstantesVendas.ESPECIAL_PARA_PONTENCIAL_OU_EVENTUAL.equals(nmTrocaTpCliente)){
			updateDadosAbaComercial(cliente);
			updateDadosAbaOperacional(cliente);
			
		} else if(ConstantesVendas.FILIAL_PARA_PONTENCIAL_OU_EVENTUAL.equals(nmTrocaTpCliente)){
			updateDadosAbaIdentificacao(cliente);
			updateDadosAbaComercial(cliente);
			updateDadosAbaOperacional(cliente);
			
		} else if(ConstantesVendas.FILIAL_PARA_ESPECIAL.equals(nmTrocaTpCliente)){
			updateDadosAbaIdentificacao(cliente);
			cliente.setBlFobDirigido(true);
			cliente.setBlFobDirigidoAereo(false);
		}

		updateDadosAbaFinanceiro(cliente);
		
		cliente.setTpCliente(cliente.getTpClienteSolicitado());
		clienteService.store(cliente);
	}
	
	private void updateDadosAbaIdentificacao(Cliente cliente){
		cliente.setClienteMatriz(null);
	}
	
	private void updateDadosAbaComercial(Cliente cliente){
		cliente.setVlFaturamentoPrevisto(null);
		cliente.setBlOperadorLogistico(false);
		cliente.setBlPesoAforadoPedagio(false);
		cliente.setBlIcmsPedagio(false);
		cliente.setBlFobDirigido(false);
		cliente.setBlFobDirigidoAereo(false);
		cliente.setBlPermanente(false);
		cliente.setBlAceitaFobGeral(false);
		
		BigDecimal nrCasasDecimaisPeso = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesVendas.NR_CASAS_DECIMAIS_PESO);
		cliente.setNrCasasDecimaisPeso(nrCasasDecimaisPeso.shortValue());
		cliente.setTpFormaArredondamento(new DomainValue(TP_FORMA_ARREDONDAMENTO_PADRAO));
		cliente.setTpFrequenciaVisita(new DomainValue(TP_FREQUENCIA_VISITA_MENSAL));
		cliente.setBlCobraReentrega(true);
		cliente.setNrReentregasCobranca(NR_REENTREGAS_COBRANCA_DEFAULT);
		cliente.setBlCobraDevolucao(true);
		cliente.setMoedaByIdMoedaFatPrev(null);
		cliente.setBlDivulgaLocalizacao(true);
	}
	
	private void updateDadosAbaOperacional(Cliente cliente){
		cliente.setObservacaoConhecimento(null);
		cliente.setTpLocalEmissaoConReent(null);
		cliente.setBlAgendamentoPessoaFisica(false);
		cliente.setBlAgendamentoPessoaJuridica(false);
		cliente.setBlColetaAutomatica(false);
		cliente.setBlCadastradoColeta(false);
		cliente.setBlObrigaRecebedor(false);
		cliente.setBlDificuldadeEntrega(false);
		cliente.setBlRetencaoComprovanteEntrega(false);
		cliente.setBlVeiculoDedicado(false);
		cliente.setBlAgendamentoEntrega(false);
		cliente.setBlPaletizacao(false);
		cliente.setBlCustoDescarga(false);
		cliente.setBlConfAgendamento(false);
		cliente.setBlRecolheICMS(false);
		cliente.setBlPesagemOpcional(false);
		cliente.setBlPaleteFechado(false);
		cliente.setBlEtiquetaPorVolume(false);
		cliente.setBlPermiteEmbarqueRodoNoAereo(false);
		cliente.setBlSeguroDiferenciadoAereo(false);
		cliente.setBlClienteCCT(false);
		
		
		cliente.setTpAgrupamentoEDI(null);
		cliente.setInformacaoDoctoCliente(null);
		cliente.setTpOrdemEmissaoEDI(null);
		cliente.setBlNumeroVolumeEDI(false);
		cliente.setBlUtilizaPesoEDI(false);
		cliente.setBlAtualizaDestinatarioEdi(false);
		cliente.setBlAtualizaConsignatarioEdi(true);
		cliente.setBlUtilizaFreteEDI(false);
		cliente.setBlLiberaEtiquetaEdi(false);
		cliente.setBlAtualizaIEDestinatarioEdi(false);
		cliente.setInformacaoDoctoClienteEDI(null);
		cliente.setBlSemChaveNfeEdi(false);
		
		cliente.setTpDificuldadeColeta(new DomainValue(TP_DIFICULDADE_COLETA_DEFAULT));
		cliente.setTpDificuldadeClassificacao(new DomainValue(TP_DIFICULDADE_CLASSIFICACAO_DEFAULT));
		cliente.setTpDificuldadeEntrega(new DomainValue(TP_DIFICULDADE_ENTREGA_DEFAULT));
		cliente.setBlAgrupaNotas(true);
		cliente.setBlPermiteCte(false);
		cliente.setBlObrigaSerie(false);
		cliente.setBlAgendamento(false);
		cliente.setBlGeraNovoDPE(false);
		cliente.setBlAssinaturaDigital(false);
	}
	
	public void updateDadosAbaFinanceiro(Cliente cliente){
		cliente.setMoedaByIdMoedaLimCred(null);
		cliente.setVlLimiteCredito(null);
		cliente.setNrDiasLimiteDebito(null);
		cliente.setCliente(null);
		cliente.setBlBaseCalculo(false);
		cliente.setDivisaoClienteResponsavel(null);
		cliente.setCedente(null);
		cliente.setBlRessarceFreteFob(false);
		cliente.setBlPreFatura(false);
		cliente.setBlCobrancaCentralizada(false);
		cliente.setBlIndicadorProtesto(false);
		cliente.setBlEmiteBoletoCliDestino(false);
		cliente.setBlGeraReciboFreteEntrega(false);
		cliente.setBlFaturaDocsConferidos(false);
		cliente.setBlFaturaDocsEntregues(false);
		cliente.setBlAgrupaFaturamentoMes(false);
		cliente.setBlFaturaDocReferencia(false);
		cliente.setBlFronteiraRapida(false);
		cliente.setTpPeriodicidadeTransf(null);
		cliente.setMoedaByIdMoedaLimDoctos(null);
		cliente.setVlLimiteDocumentos(null);
		cliente.setTpMeioEnvioBoleto(null);
		cliente.setBlCalculoArqPreFatura(false);
		cliente.setBlEmiteDacteFaturamento(false);
		cliente.setBlMtzLiberaRIM(false);
		
		cliente.setTpCobranca(new DomainValue(TP_COBRANCA_BOLETO));
		cliente.setTpCobrancaSolicitado(new DomainValue(TP_COBRANCA_BOLETO));
		cliente.setBlSeparaFaturaModal(true);
		cliente.setPcJuroDiario(PC_JURO_DIARIO_DEFAULT);
	}
	
	private String executeTpClienteChange(Cliente cliente, Boolean isWorkflowAprovado) {
		Boolean gerarWKTpCliente = Boolean.FALSE;
		String nmTrocaTpCliente = null;
		Cliente original = findById(cliente.getIdCliente());
		String tpClienteOriginal = original.getTpCliente().getValue();
		String tpClienteSolicitado = cliente.getTpClienteSolicitado().getValue();

		
		/** Verifica se o Tipo de Cliente foi alterado */
		if (!tpClienteSolicitado.equals(tpClienteOriginal)) {
			/** Caso situação do cliente nao seja INATIVO, seta o mesmo para INCOMPLETO até se concluir as validações */
			if (!ConstantesVendas.SITUACAO_INATIVO.equals(cliente.getTpSituacao().getValue())) {
				cliente.setTpSituacao(new DomainValue(ConstantesVendas.SITUACAO_INCOMPLETO));
			}

			if (isTrocaClientePotencial(tpClienteOriginal, tpClienteSolicitado)) {
				cliente.setTpCliente(cliente.getTpClienteSolicitado());
				validateConcluirCadastro(cliente);
			}

			if (isTrocaClienteEventual(tpClienteOriginal, tpClienteSolicitado)) {
				cliente.setTpCliente(cliente.getTpClienteSolicitado());
				validateConcluirCadastro(cliente);
			}
			
			// Alterações Projeto Workflow
			if (isTrocaClienteEspecialParaFilial(tpClienteOriginal, tpClienteSolicitado)) {
				clienteService.validateFiliaisMatriz(cliente.getIdCliente(), "LMS-01141");
				executeDesativarParametrizacoes(cliente.getIdCliente());
				executeDesativarMunicipiosTrt(cliente.getIdCliente());
				removeDadosRelacionados(cliente.getIdCliente());
				cliente.setTpCliente(cliente.getTpClienteSolicitado());
			}
				
			if (isTrocaClienteFilialParaEspecial(tpClienteOriginal, tpClienteSolicitado)) {
				if(isWorkflowAprovado){
					nmTrocaTpCliente = ConstantesVendas.FILIAL_PARA_ESPECIAL;
				} else {
					gerarWKTpCliente = Boolean.TRUE;
				}
			}
			
			if (isTrocaClienteFilialParaEventualOuPotencial(tpClienteOriginal, tpClienteSolicitado)) {
				if(isWorkflowAprovado){
					executeDesativarMunicipiosTrt(cliente.getIdCliente());
					nmTrocaTpCliente = ConstantesVendas.FILIAL_PARA_PONTENCIAL_OU_EVENTUAL;
				} else {
					gerarWKTpCliente = Boolean.TRUE;
				}
			}				
			
			if (isTrocaClienteEspecialParaEventualOuPotencial(tpClienteOriginal, tpClienteSolicitado)) {
				if(isWorkflowAprovado){
					// se não encontrou desativa as parametrizacoes do cliente
					executeDesativarParametrizacoes(cliente.getIdCliente());
					executeDesativarMunicipiosTrt(cliente.getIdCliente());
					removeDadosRelacionados(cliente.getIdCliente());
					nmTrocaTpCliente = ConstantesVendas.ESPECIAL_PARA_PONTENCIAL_OU_EVENTUAL;
				} else {
					clienteService.validateFiliaisMatriz(cliente.getIdCliente(), "LMS-01141");
					gerarWKTpCliente = Boolean.TRUE;
				}
			}
		}
		
		if(gerarWKTpCliente && cliente.getDsMotivoSolicitacao() != null 
				&& !historicoWorkflowService.validateWorkflowPendenciaAprovacao(cliente.getIdCliente(), TabelaHistoricoWorkflow.CLIENTE, CampoHistoricoWorkflow.TPCL)){
			String dsVlAntigo = original.getTpCliente().getDescriptionAsString();
			String dsVlNovo = configuracoesFacade.getDomainValue("DM_TIPO_CLIENTE", cliente.getTpClienteSolicitado().getValue()).getDescriptionAsString();
			generatePendenciaWorkflow(cliente, ConstantesWorkflow.NR141_APROVACAO_TIPO_CLIENTE, dsVlAntigo, dsVlNovo, CampoHistoricoWorkflow.TPCL);
		}
		
		getClienteDAO().getAdsmHibernateTemplate().evict(original.getPessoa());
		getClienteDAO().getAdsmHibernateTemplate().evict(original);
		
		return nmTrocaTpCliente;
	}

	private boolean isTrocaClienteEspecialParaEventualOuPotencial(String tpClienteOriginal, String tpClienteSolicitado) {
		return ConstantesVendas.CLIENTE_ESPECIAL.equals(tpClienteOriginal)
			&& (ConstantesVendas.CLIENTE_EVENTUAL.equals(tpClienteSolicitado)
				|| ConstantesVendas.CLIENTE_POTENCIAL.equals(tpClienteSolicitado));
	}

	private boolean isTrocaClienteEspecialParaFilial(String tpClienteOriginal, String tpClienteSolicitado) {
		return ConstantesVendas.CLIENTE_ESPECIAL.equals(tpClienteOriginal)
				&& ConstantesVendas.CLIENTE_FILIAL.equals(tpClienteSolicitado);
	}

	private boolean isTrocaClienteFilialParaEventualOuPotencial(String tpClienteOriginal, String tpClienteSolicitado) {
		return ConstantesVendas.CLIENTE_FILIAL.equals(tpClienteOriginal) &&
				(ConstantesVendas.CLIENTE_EVENTUAL.equals(tpClienteSolicitado) || 
						ConstantesVendas.CLIENTE_POTENCIAL.equals(tpClienteSolicitado));
	}

	private boolean isTrocaClienteFilialParaEspecial(String tpClienteOriginal, String tpClienteSolicitado) {
		return ConstantesVendas.CLIENTE_FILIAL.equals(tpClienteOriginal)
				&& ConstantesVendas.CLIENTE_ESPECIAL.equals(tpClienteSolicitado);
	}

	private boolean isTrocaClienteEventual(String tpClienteOriginal, String tpClienteSolicitado) {
		return ConstantesVendas.CLIENTE_EVENTUAL.equals(tpClienteOriginal) && 
				(ClienteUtils.isParametroClienteEspecial(tpClienteSolicitado) ||
				 ConstantesVendas.CLIENTE_POTENCIAL.equals(tpClienteSolicitado));
	}

	private boolean isTrocaClientePotencial(String tpClienteOriginal, String tpClienteSolicitado) {
		return ConstantesVendas.CLIENTE_POTENCIAL.equals(tpClienteOriginal) && 
				(ClienteUtils.isParametroClienteEspecial(tpClienteSolicitado) ||
				 ConstantesVendas.CLIENTE_EVENTUAL.equals(tpClienteSolicitado));
	}

	public Cliente findPessoaOriginalbyCliente (Cliente cliente){
		Pessoa pessoa = cliente.getPessoa();

		if (pessoa.getIdPessoa() != null) {
			Pessoa pessoaOriginal = pessoaService.findById(pessoa.getIdPessoa());

			pessoaOriginal.setNmPessoa(pessoa.getNmPessoa());
			pessoaOriginal.setNmFantasia(pessoa.getNmFantasia());
			pessoaOriginal.setTpIdentificacao(pessoa.getTpIdentificacao());
			pessoaOriginal.setNrIdentificacao(pessoa.getNrIdentificacao());
			pessoaOriginal.setTpPessoa(pessoa.getTpPessoa());
			pessoaOriginal.setDsEmail(pessoa.getDsEmail());
			pessoaOriginal.setNrRg(pessoa.getNrRg());
			pessoaOriginal.setDtEmissaoRg(pessoa.getDtEmissaoRg());
			pessoaOriginal.setDsOrgaoEmissorRg(pessoa.getDsOrgaoEmissorRg());
			pessoaOriginal.setNrInscricaoMunicipal(pessoa.getNrInscricaoMunicipal());
			pessoa = pessoaOriginal;
			cliente.setPessoa(pessoa);
		}
		return cliente;
	}

	private void removeDadosRelacionados(Long idCliente) {
		classificacaoClienteService.removeByIdCliente(idCliente);
		municipioRegionalClienteService.removeByIdCliente(idCliente);
		gerenciaRegionalService.removeByIdCliente(idCliente);
		liberacaoEmbarqueService.removeByIdCliente(idCliente);
		coletaAutomaticaClienteService.removeByIdCliente(idCliente);
		horarioCorteClienteService.removeByIdCliente(idCliente);
		ciaAereaClienteService.removeByIdCliente(idCliente);
		clienteRegiaoService.removeByIdCliente(idCliente);
		clienteDespachanteService.removeByIdCliente(idCliente);
		documentoClienteService.removeByIdCliente(idCliente);
		eventoClienteService.removeByIdCliente(idCliente);
		ocorrenciaClienteService.removeByIdCliente(idCliente);
	}

	public void validaCliente(Cliente cliente){		
		pessoaService.validadeCliente(cliente);		
	}	
	
	
	private void executeFilialSolicitadaChange(Cliente cliente) {
		if(cliente.getDsMotivoSolicitacao() != null){
			String dsVlAntigo = null;
			String dsVlNovo = null;
			
			if(!cliente.getFilialByIdFilialAtendeComercial().equals(cliente.getFilialByIdFilialComercialSolicitada()) 
					&& !historicoWorkflowService.validateWorkflowPendenciaAprovacao(cliente.getIdCliente(), TabelaHistoricoWorkflow.CLIENTE, CampoHistoricoWorkflow.FCOM)){
				dsVlAntigo = filialService.findSgFilialByIdFilial(cliente.getFilialByIdFilialAtendeComercial().getIdFilial());
				dsVlNovo = filialService.findSgFilialByIdFilial(cliente.getFilialByIdFilialComercialSolicitada().getIdFilial());
				generatePendenciaWorkflow(cliente, ConstantesWorkflow.NR138_APROVACAO_FILIAL_COMERCIAL, dsVlAntigo, dsVlNovo, CampoHistoricoWorkflow.FCOM);
			}
			
			if(!cliente.getFilialByIdFilialAtendeOperacional().equals(cliente.getFilialByIdFilialOperacionalSolicitada())
					&& !historicoWorkflowService.validateWorkflowPendenciaAprovacao(cliente.getIdCliente(), TabelaHistoricoWorkflow.CLIENTE, CampoHistoricoWorkflow.FOPE)){
				dsVlAntigo = filialService.findSgFilialByIdFilial(cliente.getFilialByIdFilialAtendeOperacional().getIdFilial());
				dsVlNovo = filialService.findSgFilialByIdFilial(cliente.getFilialByIdFilialOperacionalSolicitada().getIdFilial()); 
				generatePendenciaWorkflow(cliente, ConstantesWorkflow.NR139_APROVACAO_FILIAL_OPERACIONAL, dsVlAntigo, dsVlNovo, CampoHistoricoWorkflow.FOPE);
			}
			
			if(!cliente.getFilialByIdFilialCobranca().equals(cliente.getFilialByIdFilialCobrancaSolicitada())
					&& !historicoWorkflowService.validateWorkflowPendenciaAprovacao(cliente.getIdCliente(), TabelaHistoricoWorkflow.CLIENTE, CampoHistoricoWorkflow.FCOB)){
				dsVlAntigo = filialService.findSgFilialByIdFilial(cliente.getFilialByIdFilialCobranca().getIdFilial());
				dsVlNovo = filialService.findSgFilialByIdFilial(cliente.getFilialByIdFilialCobrancaSolicitada().getIdFilial());
				generatePendenciaWorkflow(cliente, ConstantesWorkflow.NR140_APROVACAO_FILIAL_FINANCEIRO, dsVlAntigo, dsVlNovo, CampoHistoricoWorkflow.FCOB);
			}
		}
	}
	
	/**
	 * 
	 * @param cliente
	 * @param nrTpEvento
	 * @param dsVlNovo
	 * @param dsVlAntigo
	 */
	private void generatePendenciaWorkflow(Cliente cliente, Short nrTpEvento, String dsVlAntigo, String dsVlNovo,
			CampoHistoricoWorkflow campoWK) {

		String dsProcesso = null;
		Long idFilial = null;
		if(CampoHistoricoWorkflow.TPCL.equals(campoWK)){
			idFilial = SessionUtils.getFilialSessao().getIdFilial();
			if (ConstantesVendas.CLIENTE_FILIAL.equals(cliente.getTpCliente().getValue())) {
				dsProcesso = this.generateDsProcessoWKClienteFilial(cliente, dsVlNovo);
			} else {
				dsProcesso = this.generateDsProcessoWK("LMS-01245", cliente, dsVlAntigo, dsVlNovo);
			}
		} else if(CampoHistoricoWorkflow.FCOM.equals(campoWK)){
			idFilial = cliente.getFilialByIdFilialAtendeComercial().getIdFilial();
			dsProcesso = this.generateDsProcessoWK("LMS-01242", cliente, dsVlAntigo, dsVlNovo);
		} else if(CampoHistoricoWorkflow.FOPE.equals(campoWK)){
			idFilial = cliente.getFilialByIdFilialComercialSolicitada().getIdFilial();
			dsProcesso = this.generateDsProcessoWK("LMS-01243", cliente, dsVlAntigo, dsVlNovo);
		} else if(CampoHistoricoWorkflow.FCOB.equals(campoWK)){
			idFilial = cliente.getFilialByIdFilialCobrancaSolicitada().getIdFilial();
			dsProcesso = this.generateDsProcessoWK("LMS-01244", cliente, dsVlAntigo, dsVlNovo);
		}
		
		PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
		pendenciaHistoricoDTO.setIdFilial(idFilial);
		pendenciaHistoricoDTO.setNrTipoEvento(nrTpEvento);
		pendenciaHistoricoDTO.setIdProcesso(cliente.getIdCliente());
		pendenciaHistoricoDTO.setDsProcesso(dsProcesso);
		pendenciaHistoricoDTO.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());
		pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.CLIENTE);
		pendenciaHistoricoDTO.setCampoHistoricoWorkflow(campoWK);
		pendenciaHistoricoDTO.setDsVlAntigo(dsVlAntigo);
		pendenciaHistoricoDTO.setDsVlNovo(dsVlNovo);
		pendenciaHistoricoDTO.setDsObservacao(cliente.getDsMotivoSolicitacao());

		workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
	}
	
	private String generateDsProcessoWKClienteFilial(Cliente cliente, String dsVlNovo) {
		String dsPessoa = getDsPessoa(cliente);
		String dsPessoaMatriz = null;
		if (cliente.getClienteMatriz() != null) {
			dsPessoaMatriz = getDsPessoa(cliente.getClienteMatriz());
		}
		return configuracoesFacade.getMensagem("LMS-01253", new Object[] { 
				dsVlNovo, 
				dsPessoa, 
				dsPessoaMatriz,
				cliente.getDsMotivoSolicitacao() });
	}

	private String getDsPessoa(Cliente cliente) {
		StringBuilder dsPessoa = new StringBuilder();
		dsPessoa.append(FormatUtils.formatIdentificacao(cliente.getPessoa()));
		dsPessoa.append(" - " );
		dsPessoa.append(cliente.getPessoa().getNmPessoa());
		return dsPessoa.toString();
	}

	private String generateDsProcessoWK(String chave, Cliente cliente, String dsVlAntigo, String dsVlNovo) {
		String dsPessoa = getDsPessoa(cliente);

		return configuracoesFacade.getMensagem(chave, new Object[] { 
				dsVlAntigo, 
				dsVlNovo,
				dsPessoa, 
				cliente.getDsMotivoSolicitacao() });
	}

	/*
	 * GETTERS E SETTERS
	 */
	public void setClienteDao(ClienteDAO dao) {
		setDao( dao );
	}
	private ClienteDAO getClienteDAO() {
		return (ClienteDAO) getDao();
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
	public void setParametroClienteService(ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setCiaAereaClienteService(CiaAereaClienteService ciaAereaClienteService) {
		this.ciaAereaClienteService = ciaAereaClienteService;
	}
	public void setClassificacaoClienteService(ClassificacaoClienteService classificacaoClienteService) {
		this.classificacaoClienteService = classificacaoClienteService;
	}
	public void setClienteDespachanteService(ClienteDespachanteService clienteDespachanteService) {
		this.clienteDespachanteService = clienteDespachanteService;
	}
	public void setClienteRegiaoService(ClienteRegiaoService clienteRegiaoService) {
		this.clienteRegiaoService = clienteRegiaoService;
	}
	public void setColetaAutomaticaClienteService(ColetaAutomaticaClienteService coletaAutomaticaClienteService) {
		this.coletaAutomaticaClienteService = coletaAutomaticaClienteService;
	}
	public void setDocumentoClienteService(DocumentoClienteService documentoClienteService) {
		this.documentoClienteService = documentoClienteService;
	}
	public void setEventoClienteService(EventoClienteService eventoClienteService) {
		this.eventoClienteService = eventoClienteService;
	}
	public void setGerenciaRegionalService(GerenciaRegionalService gerenciaRegionalService) {
		this.gerenciaRegionalService = gerenciaRegionalService;
	}
	public void setHorarioCorteClienteService(HorarioCorteClienteService horarioCorteClienteService) {
		this.horarioCorteClienteService = horarioCorteClienteService;
	}
	public void setLiberacaoEmbarqueService(LiberacaoEmbarqueService liberacaoEmbarqueService) {
		this.liberacaoEmbarqueService = liberacaoEmbarqueService;
	}
	public void setMunicipioRegionalClienteService(MunicipioRegionalClienteService municipioRegionalClienteService) {
		this.municipioRegionalClienteService = municipioRegionalClienteService;
	}
	public void setOcorrenciaClienteService(OcorrenciaClienteService ocorrenciaClienteService) {
		this.ocorrenciaClienteService = ocorrenciaClienteService;
	}
	public void setAnaliseCreditoClienteService(AnaliseCreditoClienteService analiseCreditoClienteService) {
		this.analiseCreditoClienteService = analiseCreditoClienteService;
	}
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setPrazoVencimentoService(
			PrazoVencimentoService prazoVencimentoService) {
		this.prazoVencimentoService = prazoVencimentoService;
}

	public void setDiaFaturamentoService(DiaFaturamentoService diaFaturamentoService) {
		this.diaFaturamentoService = diaFaturamentoService;
	}

	public void setClienteOperadorLogisticoService(
            ClienteOperadorLogisticoService clienteOperadorLogisticoService) {
    	this.clienteOperadorLogisticoService = clienteOperadorLogisticoService;
}

	public TrtClienteService getTrtClienteService() {
		return trtClienteService;
}

	public void setTrtClienteService(TrtClienteService trtClienteService) {
		this.trtClienteService = trtClienteService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public HistoricoWorkflowService getHistoricoWorkflowService() {
		return historicoWorkflowService;
	}

	public void setHistoricoWorkflowService(HistoricoWorkflowService historicoWorkflowService) {
		this.historicoWorkflowService = historicoWorkflowService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}
