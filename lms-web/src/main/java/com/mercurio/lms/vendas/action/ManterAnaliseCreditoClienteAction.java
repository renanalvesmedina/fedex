package com.mercurio.lms.vendas.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.AnaliseCreditoCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SerasaCliente;
import com.mercurio.lms.vendas.model.service.AnaliseCreditoClienteService;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DiaFaturamentoService;
import com.mercurio.lms.vendas.model.service.PrazoVencimentoService;
import com.mercurio.lms.vendas.model.service.SerasaClienteService;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * @author André Valadas
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.manterAnaliseCreditoClienteAction"
 */
public class ManterAnaliseCreditoClienteAction extends CrudAction {

	private FilialService filialService;
	private ClienteService clienteService;
	private UsuarioService usuarioService;
	private SerasaClienteService serasaClienteService;
	private DiaFaturamentoService diaFaturamentoService;
	private PrazoVencimentoService prazoVencimentoService;
	private ConfiguracoesFacade configuracoesFacade;

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getAnaliseCreditoClienteService().findPaginated(criteria);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getAnaliseCreditoClienteService().getRowCount(criteria);
	}

	public AnaliseCreditoCliente findByIdPadrao(Long id) {
		return (AnaliseCreditoCliente)super.findById(id);
	}

	public TypedFlatMap findById(Long idAnaliseCreditoCliente) {
		TypedFlatMap analiseCreditoMapped = getAnaliseCreditoClienteService().findByIdMapped(idAnaliseCreditoCliente);
		String nrIdentificacao = analiseCreditoMapped.getString("cliente.pessoa.nrIdentificacao");
		String tpIdentificacao = analiseCreditoMapped.getString("cliente.pessoa.tpIdentificacao.value");
		analiseCreditoMapped.put("cliente.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));

		/** Regra para a gerar Historico, baseado na Situação da Analise e no Perfil do Usuario */
		getAnaliseCreditoClienteService().generateHistoricoCreditoByUserAccess(analiseCreditoMapped);

		/** Carregar dados do Faturamento */
		analiseCreditoMapped.put("diasFaturamento", diaFaturamentoService.findDiaFaturamentoMapped(analiseCreditoMapped.getLong("cliente.idCliente")));

		/** Carregar dados do Prazo Pagamento */
		List<TypedFlatMap> prazosVencimento = prazoVencimentoService.findPrazoVencimentoMapped(analiseCreditoMapped.getLong("cliente.idCliente"));
		if(!prazosVencimento.isEmpty()) {
			String descricaoDias = configuracoesFacade.getMensagem("dias");
			for (TypedFlatMap data : prazosVencimento) {
				data.put("descricaoDias", descricaoDias);
			}
		}
		analiseCreditoMapped.put("prazosVencimento", prazosVencimento);

		/** Busca PDF Serasa */
		SerasaCliente serasaCliente = serasaClienteService.findByIdAnaliseCreditoCliente(idAnaliseCreditoCliente);
		if (serasaCliente != null) {
			analiseCreditoMapped.put("imPdfSerasa", Base64Util.encode(serasaCliente.getImPdfSerasa()));
			analiseCreditoMapped.put("idSerasaCliente", serasaCliente.getIdSerasaCliente());
		}
		return analiseCreditoMapped;
	}

	public void removeById(Long id) {
		getAnaliseCreditoClienteService().removeById(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getAnaliseCreditoClienteService().removeByIds(ids);
	}

	/**
	 * Conclui Analise de Credito do Cliente
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap storeConcluirAnaliseCredito(TypedFlatMap criteria) {
		AnaliseCreditoCliente analiseCreditoCliente = this.convertMapToBean(criteria);

		getAnaliseCreditoClienteService().storeConcluirAnaliseCredito(
			 analiseCreditoCliente
			,criteria.getString("imPdfSerasa")
			,criteria.getString("obEvento")
			,criteria.getList("faturamentoCliente")
			,criteria.getList("prazoPagamentoCliente"));

		return this.convertResultToMap(analiseCreditoCliente);
	}

	/**
	 * Aprovação do Gerente
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap storeAprovacaoGerente(TypedFlatMap criteria) {
		AnaliseCreditoCliente analiseCreditoCliente = this.convertMapToBean(criteria);

		getAnaliseCreditoClienteService().storeAprovacaoGerente(
			 analiseCreditoCliente
			,criteria.getString("imPdfSerasa")
			,criteria.getString("obEvento")
			,criteria.getList("faturamentoCliente")
			,criteria.getList("prazoPagamentoCliente"));

		return this.convertResultToMap(analiseCreditoCliente);
	}

	/**
	 * Aprovação do Diretor
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap storeAprovacaoDiretor(TypedFlatMap criteria) {
		AnaliseCreditoCliente analiseCreditoCliente = this.convertMapToBean(criteria);

		getAnaliseCreditoClienteService().storeAprovacaoDiretor(
			 analiseCreditoCliente
			,criteria.getString("imPdfSerasa")
			,criteria.getString("obEvento")
			,criteria.getList("faturamentoCliente")
			,criteria.getList("prazoPagamentoCliente"));

		return this.convertResultToMap(analiseCreditoCliente);
	}

	private AnaliseCreditoCliente convertMapToBean(TypedFlatMap criteria) {
		Long idAnaliseCreditoCliente = criteria.getLong("idAnaliseCreditoCliente");
		AnaliseCreditoCliente analiseCreditoCliente = this.findByIdPadrao(idAnaliseCreditoCliente);
		analiseCreditoCliente.setDhUltimaConsultaSerasa(criteria.getDateTime("dhUltimaConsultaSerasa"));
		analiseCreditoCliente.setBlCreditoLiberado(criteria.getBoolean("blCreditoLiberado"));

		Cliente cliente = analiseCreditoCliente.getCliente();
		cliente.setTpCobrancaAprovado(new DomainValue(criteria.getString("tpCobrancaAprovado")));

		/** Campos transientes */
		analiseCreditoCliente.setVlLimiteCredito(criteria.getBigDecimal("vlLimiteCredito"));
		if(criteria.getLong("moedaByIdMoedaLimCred.idMoeda") != null) {
			Moeda moedaByIdMoedaLimCred = new Moeda();
			moedaByIdMoedaLimCred.setIdMoeda(criteria.getLong("moedaByIdMoedaLimCred.idMoeda"));
			analiseCreditoCliente.setMoedaByIdMoedaLimCred(moedaByIdMoedaLimCred);
		}
		return analiseCreditoCliente;
	}
	
	private TypedFlatMap convertResultToMap(AnaliseCreditoCliente analiseCreditoCliente) {
		String tpSituacaoAnalise = analiseCreditoCliente.getTpSituacao().getValue();

		/** Configura variaveis de retorno */
		TypedFlatMap resultMapped = new TypedFlatMap();
		resultMapped.put("tpSituacao", tpSituacaoAnalise);
		resultMapped.put("userCanAccess", getAnaliseCreditoClienteService().allowUserAccess(tpSituacaoAnalise));

		/** Se Analise CONCLUIDA mostra mensagem pro Usuario */
		if("C".equals(tpSituacaoAnalise)) {
			Pessoa pessoa = analiseCreditoCliente.getCliente().getPessoa();
			String nrIdentificacaoFormatted = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao());
			resultMapped.put("processWarning", configuracoesFacade.getMensagem("LMS-01062", new Object[]{nrIdentificacaoFormatted.concat(" ").concat(pessoa.getNmPessoa())}));
		}

		/** Busca PDF Serasa */
		SerasaCliente serasaCliente = serasaClienteService.findByIdAnaliseCreditoCliente(analiseCreditoCliente.getIdAnaliseCreditoCliente());
		if (serasaCliente != null) {
			resultMapped.put("imPdfSerasa", Base64Util.encode(serasaCliente.getImPdfSerasa()));
		}
		return resultMapped;
	}

	/**
	 * Grava Analise de Credito, gera seu historico e atualiza os campos nas tabelas envolvidas.
	 * @author Andre Valadas
	 * @since 07/05/2008
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap gerarAnaliseCreditoClienteStore(TypedFlatMap criteria) {
		Long idCliente = criteria.getLong("idCliente");
		/** Busca pelo Cliente */
		AnaliseCreditoCliente analiseCreditoCliente = getAnaliseCreditoClienteService().findByIdCliente(idCliente);
		/** Caso não encontre, atribui nova instância */
		if(analiseCreditoCliente == null) {
			analiseCreditoCliente = new AnaliseCreditoCliente();
			createAnaliseCreditoCliente(idCliente, analiseCreditoCliente);
		}else if(analiseCreditoCliente.getTpSituacao() != null && analiseCreditoCliente.getTpSituacao().getValue().equalsIgnoreCase("C")){
			createAnaliseCreditoCliente(idCliente, analiseCreditoCliente);
		}

		

		/** Store AnaliseCreditoCliente */
		getAnaliseCreditoClienteService().storeAnaliseCreditoCliente(analiseCreditoCliente, criteria.getString("obEvento"), criteria.getString("serasaPdf"));

		/** Configura variaveis de retorno */
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("idAnaliseCreditoCliente", analiseCreditoCliente.getIdAnaliseCreditoCliente());
		toReturn.put("tpSituacao", analiseCreditoCliente.getTpSituacao().getValue());
		toReturn.put("obEvento", criteria.getString("obEvento"));
		return toReturn;
	}

	private void createAnaliseCreditoCliente(Long idCliente,
			AnaliseCreditoCliente analiseCreditoCliente) {
		analiseCreditoCliente.setTpSituacao(new DomainValue("1"));
		analiseCreditoCliente.setBlCreditoLiberado(Boolean.FALSE);
		analiseCreditoCliente.setDhConclusao(null);
		analiseCreditoCliente.setDhSolicitacao(JTDateTimeUtils.getDataHoraAtual());
		analiseCreditoCliente.setUsuario(SessionUtils.getUsuarioLogado());

		Cliente cliente = new Cliente();
		cliente.setIdCliente(idCliente);
		analiseCreditoCliente.setCliente(cliente);
	}
	
	/**
	 * Valida campos obrigatórios para geração da Análise de Crédito
	 * @author Andre Valadas
	 * @param criteria
	 */
	public void validateAnaliseCreditoCliente(Long idCliente) {
		getAnaliseCreditoClienteService().validateAnaliseCreditoCliente(idCliente);
	}

	/**
	 * Consulta a filial pela sigla informada 
	 * @param map
	 * @return
	 */
	public List findFilialLookup(Map<String, Object> map) {
		FilterList filter = new FilterList(filialService.findLookup(map)) {
			public Map<String, Object> filterItem(Object item) {
				Filial filial = (Filial)item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idFilial", filial.getIdFilial());
				typedFlatMap.put("sgFilial",  filial.getSgFilial());
				typedFlatMap.put("pessoa.nmFantasia",  filial.getPessoa().getNmFantasia());
				return typedFlatMap;
			}
		};
		return (List)filter.doFilter();
	}

	public List findClienteLookup(TypedFlatMap criteria) {
		return clienteService.findLookupCliente(criteria.getString("pessoa.nrIdentificacao"), ConstantesVendas.CLIENTE_ESPECIAL, null);
	}

	public List findFuncionarioLookup(TypedFlatMap parameters) {
		String nrMatricula = parameters.getString("nrMatricula");
		return usuarioService.findLookupUsuarioFuncionario(null,
				nrMatricula, null, null, null, null, true);
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setSerasaClienteService(SerasaClienteService serasaClienteService) {
		this.serasaClienteService = serasaClienteService;
	}
	public void setDiaFaturamentoService(DiaFaturamentoService diaFaturamentoService) {
		this.diaFaturamentoService = diaFaturamentoService;
	}
	public void setPrazoVencimentoService(PrazoVencimentoService prazoVencimentoService) {
		this.prazoVencimentoService = prazoVencimentoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setAnaliseCreditoClienteService(AnaliseCreditoClienteService serviceService) {
		super.defaultService = serviceService;
	}
	public AnaliseCreditoClienteService getAnaliseCreditoClienteService() {
		return (AnaliseCreditoClienteService) super.defaultService;
	}
}