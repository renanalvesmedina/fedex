package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.AnaliseCreditoCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DiaFaturamento;
import com.mercurio.lms.vendas.model.HistoricoAnaliseCredito;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.vendas.model.dao.AnaliseCreditoClienteDAO;
import com.mercurio.lms.vendas.util.ConstantesVendas;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.analiseCreditoClienteService"
 */
public class AnaliseCreditoClienteService extends CrudService<AnaliseCreditoCliente, Long> {
	private HistoricoAnaliseCreditoService historicoAnaliseCreditoService;
	private PrazoVencimentoService prazoVencimentoService;
	private DiaFaturamentoService diaFaturamentoService;
	private ClienteService clienteService;
	private ConfiguracoesFacade configuracoesFacade;
	private DomainValueService domainValueService;
	private IntegracaoJmsService integracaoJmsService;
	private ParametroGeralService parametroGeralService;

	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
	private static final String TEXT_HTML= "text/html; charset='utf-8'";

	public AnaliseCreditoCliente findById(Long id) {
		return (AnaliseCreditoCliente)super.findById(id);
	}

	public AnaliseCreditoCliente findByIdCliente(Long idCliente) {
		return getAnaliseCreditoClienteDAO().findByIdCliente(idCliente);
	}

	public TypedFlatMap findByIdMapped(Long idAnaliseCreditoCliente) {
		return getAnaliseCreditoClienteDAO().findByIdMapped(idAnaliseCreditoCliente);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getAnaliseCreditoClienteDAO().findPaginated(criteria, def);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getAnaliseCreditoClienteDAO().getRowCount(criteria);
	}

	@Override
	public Serializable store(AnaliseCreditoCliente bean) {
		return super.store(bean);
	}

	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}

	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Valida a Situação da Analise e o Perfil para geração do Historico de Credito
	 * @author Andre Valadas
	 * @param analiseCreditoMapped
	 * @return
	 */
	public void generateHistoricoCreditoByUserAccess(TypedFlatMap analiseCreditoMapped) {
		Long idAnaliseCreditoCliente = analiseCreditoMapped.getLong("idAnaliseCreditoCliente");
		String tpSituacaoAnalise = analiseCreditoMapped.getString("tpSituacao.value");

		/** Verifica se perfil de usuario(ou substituto) possui permissão */
		Boolean userCanAccess = this.allowUserAccess(tpSituacaoAnalise);
		if(Boolean.TRUE.equals(userCanAccess) && ("1".equals(tpSituacaoAnalise) || "2".equals(tpSituacaoAnalise))) {
			AnaliseCreditoCliente analiseCreditoCliente = this.findById(idAnaliseCreditoCliente);
			analiseCreditoCliente.setUsuarioAnalise(SessionUtils.getUsuarioLogado());

			/** Analista de crédito nível 1 */
			if("1".equals(tpSituacaoAnalise)) {
				analiseCreditoCliente.setTpSituacao(new DomainValue("E"));

				/** STORE Historico Analise de Credito */
				historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(
						analiseCreditoCliente
						,new DomainValue("I")
						,configuracoesFacade.getMensagem("LMS-01061", new Object[]{1}));
				/** Analista de crédito nível 2 */
			} else if("2".equals(tpSituacaoAnalise)) {
				analiseCreditoCliente.setTpSituacao(new DomainValue("M"));

				/** STORE Historico Analise de Credito */
				historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(
						analiseCreditoCliente
						,new DomainValue("N")
						,configuracoesFacade.getMensagem("LMS-01061", new Object[]{2}));
			}

			/** STORE Analise de Credito Cliente */
			this.store(analiseCreditoCliente);

			analiseCreditoMapped.put("tpSituacao.value", analiseCreditoCliente.getTpSituacao().getValue());
		}
		analiseCreditoMapped.put("userCanAccess", userCanAccess);
	}
	public Boolean allowUserAccess(String tpSituacaoAnalise) {
		String accessLevel = tpSituacaoAnalise;
		if("C".equals(tpSituacaoAnalise)) {
			return Boolean.FALSE; //Analise concluida
		} else if("E".equals(tpSituacaoAnalise)) {
			accessLevel = "1";
		} else if("M".equals(tpSituacaoAnalise)) {
			accessLevel = "2";
		}
		return this.allowUserAccessByPerfil(SessionUtils.getUsuarioLogado().getIdUsuario(), "ID_PERFIL_AN_CRED_NIVEL".concat(accessLevel));
	}

	/**
	 * Verifica se usuario tem permissao no Perfil ou como Substituto
	 * @author Andre Valadas
	 * @param idUsuario
	 * @param idsPerfil
	 * @return
	 */
	public Boolean allowUserAccessByPerfil(Long idUsuario, String...idsPerfil) {
		Boolean userCanAccess = getAnaliseCreditoClienteDAO().allowUserAccessByPerfil(idUsuario, idsPerfil);
		if(Boolean.FALSE.equals(userCanAccess)) {
			return getAnaliseCreditoClienteDAO().allowUserAccessByPerfilSubstituto(idUsuario, idsPerfil);
		}
		return userCanAccess;
	}

	/**
	 * Regras para a conclusão da Analise de Credito
	 * @author Andre Valadas
	 * @param analiseCreditoCliente
	 * @param imPdfSerasa
	 * @param obEvento
	 * @param diasFaturamento
	 * @param prazosVencimento
	 */
	public void storeConcluirAnaliseCredito(AnaliseCreditoCliente analiseCreditoCliente, String imPdfSerasa, String obEvento, List<TypedFlatMap> diasFaturamento, List<TypedFlatMap> prazosVencimento) {
		/** Atualiza campos das tabelas CLIENTE, PRAZO_VENCIMENTO e DIA_FATURAMENTO
		 * pelos valores informados na Análise de Crédito. */
		this.updateRelatedTablesFromAnalise(analiseCreditoCliente, diasFaturamento, prazosVencimento);

		/** STORE Historico Analise de Credito */
		String tpSituacaoAnalise = analiseCreditoCliente.getTpSituacao().getValue();
		if("E".equals(tpSituacaoAnalise)) {
			historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(analiseCreditoCliente, new DomainValue("P"), obEvento, imPdfSerasa);
			/** Caso crédito liberado */
			if(Boolean.TRUE.equals(analiseCreditoCliente.getBlCreditoLiberado())) {
				analiseCreditoCliente.setTpSituacao(new DomainValue("2"));
				analiseCreditoCliente.setUsuarioAnalise(SessionUtils.getUsuarioLogado());
				this.store(analiseCreditoCliente);
				return;
			}
		} else if("M".equals(tpSituacaoAnalise)) {
			historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(analiseCreditoCliente, new DomainValue("S"), obEvento, imPdfSerasa);
			/** Caso crédito liberado */
			if(Boolean.TRUE.equals(analiseCreditoCliente.getBlCreditoLiberado())) {
				/** Valida Faturas e Prazos */
				Boolean endProcess = Boolean.FALSE;
				for (TypedFlatMap diaFaturamentoMapped : diasFaturamento) {
					DomainValue tpPeriodicidadeAprovado = diaFaturamentoMapped.getDomainValue("tpPeriodicidadeAprovado");
					if("Q".equals(tpPeriodicidadeAprovado.getValue()) || "M".equals(tpPeriodicidadeAprovado.getValue())) {
						endProcess = Boolean.TRUE;
						break;
					}
				}
				if(Boolean.FALSE.equals(endProcess)) {
					for (TypedFlatMap prazoVencimentoMapped : prazosVencimento) {
						Short nrPrazoPagamentoAprovado = prazoVencimentoMapped.getShort("nrPrazoPagamentoAprovado");
						String nrPrazoPagamentoParametro = parametroGeralService.findSimpleConteudoByNomeParametro("NR_PRAZO_PAGAMENTO_AN_CRED");
						if(CompareUtils.gt(nrPrazoPagamentoAprovado, Short.valueOf(nrPrazoPagamentoParametro))) {
							endProcess = Boolean.TRUE;
							break;
						}
					}
				}
				if(Boolean.TRUE.equals(endProcess)) {
					analiseCreditoCliente.setTpSituacao(new DomainValue("3"));
					analiseCreditoCliente.setUsuarioAnalise(SessionUtils.getUsuarioLogado());
					this.store(analiseCreditoCliente);
					/** Envia e-mail para os usuários ligado a o perfil “Analista de
					 * crédito nível 3” e para os substitutos vigentes na data. */
					this.executeSendMailPerfilUsuario(analiseCreditoCliente, "ID_PERFIL_AN_CRED_NIVEL3");
					return;
				}
			}
		} else if("3".equals(tpSituacaoAnalise)) {
			historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(analiseCreditoCliente, new DomainValue("T"), obEvento, imPdfSerasa);
		} else if("4".equals(tpSituacaoAnalise)) {
			historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(analiseCreditoCliente, new DomainValue("Q"), obEvento, imPdfSerasa);
		} else return;

		/** STORE Historico Analise de Credito CONCLUIDO */
		historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(analiseCreditoCliente, new DomainValue("C"), null);

		/** STORE Analise de Credito */
		analiseCreditoCliente.setTpSituacao(new DomainValue("C"));
		analiseCreditoCliente.setUsuarioAnalise(SessionUtils.getUsuarioLogado());
		analiseCreditoCliente.setDhConclusao(JTDateTimeUtils.getDataHoraAtual());
		this.store(analiseCreditoCliente);

		/** Se CRÉDITO LIBERADO, atualiza tabelas relacionadas */
		if(Boolean.TRUE.equals(analiseCreditoCliente.getBlCreditoLiberado())) {
			this.updateRelatedTablesFromAnalise(analiseCreditoCliente, diasFaturamento, prazosVencimento);
		}

		/** Envia e-mail para o usuário solicitante. */
		this.executeSendMailUsuario(analiseCreditoCliente, analiseCreditoCliente.getUsuario());
	}

	/**
	 * Regras para Aprovação do Gerente
	 * @author Andre Valadas
	 * @param analiseCreditoCliente
	 * @param imPdfSerasa
	 * @param obEvento
	 * @param diasFaturamento
	 * @param prazosVencimento
	 */
	public void storeAprovacaoGerente(AnaliseCreditoCliente analiseCreditoCliente, String imPdfSerasa, String obEvento, List<TypedFlatMap> diasFaturamento, List<TypedFlatMap> prazosVencimento) {
		historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(analiseCreditoCliente, new DomainValue("S"), obEvento, imPdfSerasa);
		analiseCreditoCliente.setTpSituacao(new DomainValue("3"));
		analiseCreditoCliente.setUsuarioAnalise(SessionUtils.getUsuarioLogado());
		this.store(analiseCreditoCliente);

		/** Atualiza os registros */
		this.updateRelatedTablesFromAnalise(analiseCreditoCliente, diasFaturamento, prazosVencimento);

		/** Envia e-mail para os usuários ligado a o perfil “Analista de
		 * crédito nível 3” e para os substitutos vigentes na data. */
		this.executeSendMailPerfilUsuario(analiseCreditoCliente, "ID_PERFIL_AN_CRED_NIVEL3");
	}

	/**
	 * Regras para Aprovação do Diretor
	 * @author Andre Valadas
	 * @param analiseCreditoCliente
	 * @param imPdfSerasa
	 * @param obEvento
	 * @param diasFaturamento
	 * @param prazosVencimento
	 */
	public void storeAprovacaoDiretor(AnaliseCreditoCliente analiseCreditoCliente, String imPdfSerasa, String obEvento, List<TypedFlatMap> diasFaturamento, List<TypedFlatMap> prazosVencimento) {
		historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(analiseCreditoCliente, new DomainValue("T"), obEvento, imPdfSerasa);
		analiseCreditoCliente.setTpSituacao(new DomainValue("4"));
		analiseCreditoCliente.setUsuarioAnalise(SessionUtils.getUsuarioLogado());
		this.store(analiseCreditoCliente);

		/** Atualiza os registros */
		this.updateRelatedTablesFromAnalise(analiseCreditoCliente, diasFaturamento, prazosVencimento);

		/** Envia e-mail para os usuários ligado a o perfil “Analista de
		 * crédito nível 4” e para os substitutos vigentes na data. */
		this.executeSendMailPerfilUsuario(analiseCreditoCliente, "ID_PERFIL_AN_CRED_NIVEL4");
	}

	/**
	 * Exclui todas referencias do cliente na Analise de Credito, Historico e Serasa
	 * @author Andre Valadas
	 * @param idCliente
	 */
	public void removeByIdCliente(Long idCliente) {
		/** Exclui todas referencias do cliente na analise */
		AnaliseCreditoCliente analiseCreditoCliente = getAnaliseCreditoClienteDAO().findByIdCliente(idCliente);
		if(analiseCreditoCliente != null) {
			this.removeById(analiseCreditoCliente.getIdAnaliseCreditoCliente());

			/** Exclui todas referencias do cliente no historico e serasa */
			HistoricoAnaliseCredito historicoAnaliseCredito = historicoAnaliseCreditoService.findByIdCliente(idCliente);
			historicoAnaliseCreditoService.removeById(historicoAnaliseCredito.getIdHistoricoAnaliseCredito());
		}
	}

	/**
	 * Grava Analise de Credito, gera seu historico e atualiza os campos nas tabelas envolvidas.
	 * @author Andre Valadas
	 * @since 07/05/2008
	 * @param analiseCreditoCliente
	 * @param obEvento
	 * @return
	 */
	public java.io.Serializable storeAnaliseCreditoCliente(AnaliseCreditoCliente analiseCreditoCliente, String obEvento, String imPdfSerasa) {
		/** Atualiza campos das tabelas CLIENTE, PRAZO_VENCIMENTO e DIA_FATURAMENTO */
		this.updateRelatedTablesFromCliente(analiseCreditoCliente.getCliente().getIdCliente());

		/** Analise de Credito Cliente */
		super.store(analiseCreditoCliente);

		/** Historico Analise de Credito */
		historicoAnaliseCreditoService.generateHistoricoAnaliseCredito(analiseCreditoCliente, new DomainValue("A"), obEvento, imPdfSerasa);

		return analiseCreditoCliente.getIdAnaliseCreditoCliente();
	}

	/**
	 * Atualiza campos das tabelas CLIENTE, PRAZO_VENCIMENTO e DIA_FATURAMENTO
	 * pelos valores informados na Análise.
	 * @param analiseCreditoCliente
	 * @param diasFaturamento
	 * @param prazosVencimento
	 */
	private void updateRelatedTablesFromAnalise(AnaliseCreditoCliente analiseCreditoCliente, List<TypedFlatMap> diasFaturamento, List<TypedFlatMap> prazosVencimento) {
		boolean updateAll = "C".equals(analiseCreditoCliente.getTpSituacao().getValue()) && Boolean.TRUE.equals(analiseCreditoCliente.getBlCreditoLiberado());

		/** STORE Cliente com tpCobrancaAprovado */
		Cliente cliente = analiseCreditoCliente.getCliente();
		cliente.setMoedaByIdMoedaLimCred(analiseCreditoCliente.getMoedaByIdMoedaLimCred());
		cliente.setVlLimiteCredito(analiseCreditoCliente.getVlLimiteCredito());
		if(updateAll) {
			cliente.setTpCobranca(cliente.getTpCobrancaAprovado());
		}
		clienteService.store(cliente);

		/** STORE Faturamentos e Prazos para Pagamento */
		for (TypedFlatMap diaFaturamentoMapped : diasFaturamento) {
			Long idDiaFaturamento = diaFaturamentoMapped.getLong("id");
			DomainValue tpPeriodicidadeAprovado = diaFaturamentoMapped.getDomainValue("tpPeriodicidadeAprovado");

			DiaFaturamento diaFaturamento = diaFaturamentoService.findById(idDiaFaturamento);
			diaFaturamento.setTpPeriodicidadeAprovado(tpPeriodicidadeAprovado);
			if(updateAll) {
				diaFaturamento.setTpPeriodicidade(tpPeriodicidadeAprovado);
				diaFaturamento.setNrDiaFaturamento(diaFaturamento.getNrDiaFaturamentoSolicitado());
				diaFaturamento.setTpDiaSemana(diaFaturamento.getTpDiaSemanaSolicitado());
			}
			diaFaturamentoService.store(diaFaturamento);
		}

		/** STORE Prazos para Pagamento */
		for (TypedFlatMap prazoVencimentoMapped : prazosVencimento) {
			Long idPrazoVencimento = prazoVencimentoMapped.getLong("id");
			Short nrPrazoPagamentoAprovado = prazoVencimentoMapped.getShort("nrPrazoPagamentoAprovado");

			PrazoVencimento prazoVencimento = prazoVencimentoService.findById(idPrazoVencimento);
			prazoVencimento.setNrPrazoPagamentoAprovado(nrPrazoPagamentoAprovado);
			if(updateAll) {
				prazoVencimento.setNrPrazoPagamento(nrPrazoPagamentoAprovado);
			}
			prazoVencimentoService.storeBasic(prazoVencimento);
		}
	}

	/**
	 * Atualiza campos das tabelas CLIENTE, PRAZO_VENCIMENTO e DIA_FATURAMENTO.
	 * @author Andre Valadas
	 * @param idCliente
	 */
	private void updateRelatedTablesFromCliente(Long idCliente) {
		Cliente cliente = clienteService.findById(idCliente);
		cliente.setTpCobrancaAprovado(null);
		clienteService.store(cliente);

		List<PrazoVencimento> prazosVencimento = prazoVencimentoService.findPrazoVencimento(idCliente);
		for (PrazoVencimento prazoVencimento : prazosVencimento) {
			prazoVencimento.setNrPrazoPagamentoAprovado(null);
			prazoVencimentoService.storeBasic(prazoVencimento);
		}

		List<DiaFaturamento> diasFaturamento = diaFaturamentoService.findDiaFaturamento(idCliente);
		for (DiaFaturamento diaFaturamento : diasFaturamento) {
			diaFaturamento.setTpPeriodicidadeAprovado(null);
			diaFaturamentoService.store(diaFaturamento);
		}
	}

	/**
	 * Valida campos obrigatórios para geração da Análise de Crédito
	 * @author Andre Valadas
	 * @param idCliente
	 */
	public void validateAnaliseCreditoCliente(Long idCliente) {
		/** Valida se existe algum prazoVencimento com o campo NrPrazoPagamentoSolicitado informado */
		List<PrazoVencimento> prazosVencimento = prazoVencimentoService.findPrazoVencimento(idCliente);

		/** Verifica se existe pelo menos UM PrazoVencimento */
		Boolean wasValidated = Boolean.valueOf(!prazosVencimento.isEmpty());
		for (PrazoVencimento prazoVencimento : prazosVencimento) {
			if(prazoVencimento.getNrPrazoPagamentoSolicitado() == null) {
				wasValidated = Boolean.FALSE;
				break;
			}
		}

		/** Se validou o processo anterior */
		if(wasValidated.equals(Boolean.TRUE)) {
			/** Valida se existe algum diaFaturamento com o campo TpPeriodicidadeSolicitado informado */
			List<DiaFaturamento> diasFaturamento = diaFaturamentoService.findDiaFaturamento(idCliente);

			/** Verifica se existe pelo menos UM DiaVencimento */
			wasValidated = Boolean.valueOf(!diasFaturamento.isEmpty());
			for (DiaFaturamento diaFaturamento : diasFaturamento) {
				if(diaFaturamento.getTpPeriodicidadeSolicitado() == null) {
					wasValidated = Boolean.FALSE;
					break;
				}
			}
		}

		/** Se validou os processos anteriores */
		if(wasValidated.equals(Boolean.TRUE)) {
			wasValidated = Boolean.FALSE;
			/** Valida se o Cliente possui o campo TpCobrancaSolicitado informado,
			 * e se o VlFaturamentoPrevisto é maior que ZERO. */
			Cliente cliente = clienteService.findById(idCliente);
			if(cliente.getTpCobrancaSolicitado() != null
					&& CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(cliente.getVlFaturamentoPrevisto()), BigDecimal.ZERO)) {
				wasValidated = Boolean.TRUE;
			}
		}

		/** Se não validou algum processo acima, lança Exceção */
		if(wasValidated.equals(Boolean.FALSE)) {
			throw new BusinessException("LMS-01038");
		}
	}

	/**
	 * Enviar e-mail para os usuários ligado ao perfil passado
	 * e para os seus substitutos vigentes na data atual.
	 * @author Andre Valadas
	 * @param analiseCreditoCliente
	 * @param idsPerfil
	 */
	private void executeSendMailPerfilUsuario(AnaliseCreditoCliente analiseCreditoCliente, String...idsPerfil) {
		List<Usuario> usuarios = getAnaliseCreditoClienteDAO().findUsersAccessByPerfil(idsPerfil);

		/** Adiciona Usuarios Substitutos */
		usuarios.addAll(getAnaliseCreditoClienteDAO().findUsersAccessByPerfilSubstituto(idsPerfil));
		for (Usuario usuario : usuarios) {
			this.executeSendMailUsuario(analiseCreditoCliente, usuario);
		}
	}

	/**
	 * Envia e-mail para o Usuario informado
	 * @author Andre Valadas
	 * @param analiseCreditoCliente
	 * @param usuario
	 */
	private void executeSendMailUsuario(AnaliseCreditoCliente analiseCreditoCliente, Usuario usuario) {
		Pessoa pessoa = analiseCreditoCliente.getCliente().getPessoa();
		String nrIdentificacaoFormatted = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao());
		DomainValue tpSituacaoAnalise = analiseCreditoCliente.getTpSituacao();

		/** Dados do E-mail */
		if(StringUtils.isNotBlank(usuario.getDsEmail())) {
			String[] dsEmails = new String[]{usuario.getDsEmail()};
			String strSubject = configuracoesFacade.getMensagem("lmsAnaliseCredito");
			String strFrom = (String)configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
			StringBuilder strText = new StringBuilder();

			/** Caso Analise Concluida */
			if("C".equals(tpSituacaoAnalise.getValue())) {
				strText.append(configuracoesFacade.getMensagem("LMS-01062", new Object[]{nrIdentificacaoFormatted.concat(" ").concat(pessoa.getNmPessoa())}));
				strText.append(LINE_SEPARATOR).append(configuracoesFacade.getMensagem("LMS-01063", new Object[]{configuracoesFacade.getMensagem("manterAnaliseCreditoCliente")}));

				/** Adiciona mensagem caso Crédito liberado e cliente com situação em INCOMPLETO */
				if(Boolean.TRUE.equals(analiseCreditoCliente.getBlCreditoLiberado())
						&& ConstantesVendas.SITUACAO_INCOMPLETO.equals(analiseCreditoCliente.getCliente().getTpSituacao().getValue())) {
					strText.append(LINE_SEPARATOR).append(configuracoesFacade.getMensagem("LMS-01166"));
				}
			} else {
				if(StringUtils.isBlank(tpSituacaoAnalise.getDescriptionAsString())) {
					tpSituacaoAnalise = domainValueService.findDomainValueByValue("DM_SITUACAO_ANALISE_CREDITO", tpSituacaoAnalise.getValue());
				}
				strText.append(configuracoesFacade.getMensagem("LMS-01064", new Object[]{nrIdentificacaoFormatted.concat(" ").concat(pessoa.getNmPessoa()), tpSituacaoAnalise.getDescriptionAsString()}));
			}
			strText.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(configuracoesFacade.getMensagem("LMS-39022"));

			/** Envia E-mail */
			this.sendEmail(
					dsEmails
					,strSubject
					,strFrom
					,strText.toString());
		}
	}

	/**
	 * @author Andre Valadas
	 * @param strEmails
	 * @param strSubject
	 * @param strFrom
	 * @param strText
	 */
	private void sendEmail(final String[] strEmails,final  String strSubject,final  String strFrom,final  String strText){
		Mail mail = createMail(StringUtils.join(strEmails, ";"), strFrom, strSubject, strText);

		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		return mail;
	}

	public List<Usuario> findUsersAccessByPerfil(String...idsPerfil) {
		return getAnaliseCreditoClienteDAO().findUsersAccessByPerfil(idsPerfil);
	}

	public List<Usuario> findUsersAccessByPerfilSubstituto(String...idsPerfil) {
		return getAnaliseCreditoClienteDAO().findUsersAccessByPerfilSubstituto(idsPerfil);
	}

	public void setAnaliseCreditoClienteDAO(AnaliseCreditoClienteDAO dao) {
		setDao( dao );
	}
	private AnaliseCreditoClienteDAO getAnaliseCreditoClienteDAO() {
		return (AnaliseCreditoClienteDAO) getDao();
	}
	public void setHistoricoAnaliseCreditoService(HistoricoAnaliseCreditoService historicoAnaliseCreditoService) {
		this.historicoAnaliseCreditoService = historicoAnaliseCreditoService;
	}

	public void setPrazoVencimentoService(PrazoVencimentoService prazoVencimentoService) {
		this.prazoVencimentoService = prazoVencimentoService;
	}
	public void setDiaFaturamentoService(DiaFaturamentoService diaFaturamentoService) {
		this.diaFaturamentoService = diaFaturamentoService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}
