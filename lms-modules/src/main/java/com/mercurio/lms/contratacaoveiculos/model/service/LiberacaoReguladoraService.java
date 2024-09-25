package com.mercurio.lms.contratacaoveiculos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.RegraLiberacaoReguladora;
import com.mercurio.lms.contratacaoveiculos.model.dao.LiberacaoReguladoraDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.liberacaoReguladoraService"
 */

public class LiberacaoReguladoraService extends CrudService<LiberacaoReguladora, Long> {
	private RegraLiberacaoReguladoraService regraLiberacaoReguladoraService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private DomainValueService domainValueService;

	/**
	 * Recupera uma instância de <code>LiberacaoReguladora</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public LiberacaoReguladora findById(java.lang.Long id) {
		return (LiberacaoReguladora)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		LiberacaoReguladora bean = (LiberacaoReguladora)getLiberacaoReguladoraDAO().getAdsmHibernateTemplate().get(LiberacaoReguladora.class,id);
		Pendencia pendencia = bean.getPendencia();
		if (pendencia != null)
			workflowPendenciaService.cancelPendencia(pendencia.getIdPendencia());
		getLiberacaoReguladoraDAO().getAdsmHibernateTemplate().delete(bean);
		super.removeById(id);
	} 

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for(Iterator<Long> i = ids.iterator(); i.hasNext();)
			removeById(i.next());
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getLiberacaoReguladoraDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		for(Iterator<Map<String, Object>> i = rsp.getList().iterator(); i.hasNext();) {
			Map<String, Object> row = i.next();
			if (row.get("proprietario_pessoa_tpIdentificacao") != null) {
				row.put("proprietario_pessoa_nrIdentificacao",FormatUtils.formatIdentificacao((DomainValue)row.get("proprietario_pessoa_tpIdentificacao"),(String)row.get("proprietario_pessoa_nrIdentificacao")));
				row.remove("proprietario_pessoa_nrIdentificacaoFormatado");
			}
			if (row.get("motorista_pessoa_tpIdentificacao") != null) {
				row.put("motorista_pessoa_nrIdentificacao",FormatUtils.formatIdentificacao((DomainValue)row.get("motorista_pessoa_tpIdentificacao"),(String)row.get("motorista_pessoa_nrIdentificacao")));
				row.remove("motorista_pessoa_nrIdentificacaoFormatado");
			}
		}
		return rsp;
	} 

	private void executeLiberacaoMotoristaValido(LiberacaoReguladora liberacaoReguladora) {
		String tpOperacao = liberacaoReguladora.getTpOperacao().getValue();
		Long idLiberacaoReguladora = liberacaoReguladora.getIdLiberacaoReguladora();
		Long idMotorista = liberacaoReguladora.getMotorista().getIdMotorista();

		LiberacaoReguladora ultimaLiberacaoReguladora = getLiberacaoReguladoraDAO().findLastLiberacaoReguladora(idMotorista, tpOperacao, idLiberacaoReguladora);
		if (ultimaLiberacaoReguladora != null) {
			YearMonthDay dtVencimento = ultimaLiberacaoReguladora.getDtVencimento();
			if(dtVencimento != null) {
				if(CompareUtils.gt(dtVencimento, JTDateTimeUtils.getDataAtual())) {
					throw new BusinessException("LMS-00003");
				}
			} else if("V".equals(tpOperacao)) {
				DateTime dtLiberacao = JTDateTimeUtils.yearMonthDayToDateTime(ultimaLiberacaoReguladora.getDtLiberacao());
				BigDecimal qtHorasLiberacao = (BigDecimal) configuracoesFacade.getValorParametro("QT_HORAS_LIBERACAO");
				if(CompareUtils.ge(dtLiberacao.plusHours(qtHorasLiberacao.intValue()), JTDateTimeUtils.getDataHoraAtual())) {
					throw new BusinessException("LMS-00003"); 
				} else {
					ultimaLiberacaoReguladora.setDtVencimento(JTDateTimeUtils.getDataAtual().minusDays(1));
					store(ultimaLiberacaoReguladora);
				}
			}
		}
	}

	private void processLiberacaoViagem(LiberacaoReguladora liberacaoReguladora) {
		Motorista motorista = liberacaoReguladora.getMotorista();
		Long idMotorista = motorista.getIdMotorista();
		Long idReguladora = liberacaoReguladora.getReguladoraSeguro().getIdReguladora();
		String tpOperacao = liberacaoReguladora.getTpOperacao().getValue();

		String tpVinculo = motorista.getTpVinculo().getValue();
		YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		List<RegraLiberacaoReguladora> rs = regraLiberacaoReguladoraService.findRegraLiberacaoByTpVinculoAndVigencia(
				tpVinculo,
				idReguladora,
				dtAtual
		);
		if (rs.size() == 0)
			throw new BusinessException("LMS-26031");
		RegraLiberacaoReguladora regraLiberacaoReguladora = (RegraLiberacaoReguladora)rs.get(0);

		Integer qtViagensAnoLiberacao = regraLiberacaoReguladora.getQtViagensAnoLiberacao();
		if (regraLiberacaoReguladora.getBlLiberacaoPorViagem().booleanValue()
				&& qtViagensAnoLiberacao != null
		) {
			YearMonthDay dtFim = dtAtual;
			YearMonthDay dtInicio = dtFim.minusYears(1);
			Integer total = getCountLiberacaoMotorista(
					idMotorista,
					idReguladora,
					dtInicio,
					dtFim,
					tpOperacao
			);
			if (((qtViagensAnoLiberacao != null && qtViagensAnoLiberacao.compareTo(total) <= 0)) && tpVinculo.equalsIgnoreCase("E")) {
				rs = regraLiberacaoReguladoraService.findRegraLiberacaoByTpVinculoAndVigencia(
						"A",
						idReguladora,
						dtAtual
				);
				if (rs.size() == 0)
					throw new BusinessException("LMS-26031");
				motorista.setTpVinculo(new DomainValue("A"));
				return;
			} else {
				//Condição 3.3
				liberacaoReguladora.setDtLiberacao(dtAtual);
				if (!tpVinculo.equalsIgnoreCase("A"))
					liberacaoReguladora.setDtVencimento(null);
			}
		} else {
			//Condição 3.2
			liberacaoReguladora.setDtLiberacao(dtAtual);
			if (regraLiberacaoReguladora.getQtMesesValidade()!=null)
				liberacaoReguladora.setDtVencimento(liberacaoReguladora.getDtLiberacao().plusMonths(regraLiberacaoReguladora.getQtMesesValidade().intValue()));
			else
				liberacaoReguladora.setDtVencimento(null);
		}
		if (tpVinculo.equalsIgnoreCase("A") && liberacaoReguladora.getDtVencimento() != null) {			
			DateTime dhLiberacao = liberacaoReguladora.getDtVencimento().minusDays(5).toLocalDate().toDateTimeAtStartOfDay(JTDateTimeUtils.getUserDtz());

			List<Long> processIds = new ArrayList<Long>(1);
			processIds.add(idMotorista);
			List<String> processNames = new ArrayList<String>(1);
			processNames.add(configuracoesFacade.getMensagem("motoristaValidadePrestesVencer") + " - " + motorista.getPessoa().getNmPessoa());
			//Gera WorkFlow
			List<Pendencia> pendencias = workflowPendenciaService.generatePendencia(
					SessionUtils.getFilialSessao().getIdFilial(),
					ConstantesWorkflow.NR2604_LIBRE_VENCER,
					processIds,
					processNames,
					dhLiberacao
			);

			if (pendencias != null && pendencias.size() > 0)
				liberacaoReguladora.setPendencia((Pendencia)pendencias.get(0));
		} 
	}

	private void processLiberacaoColetaEntrega(LiberacaoReguladora liberacaoReguladora) {
		Motorista motorista = liberacaoReguladora.getMotorista();
		String tpVinculo = motorista.getTpVinculo().getValue();

		YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		liberacaoReguladora.setDtLiberacao(dtAtual);
		String nmParametro = "";
		if("E".equals(tpVinculo)) {
			nmParametro = "NR_DIAS_LIBERACAO_EVENTUAL";
		} else {
			nmParametro = "NR_DIAS_LIBERACAO_PROPRIO";
		}
		BigDecimal nrDiasLiberacao = (BigDecimal) configuracoesFacade.getValorParametro(nmParametro);
		YearMonthDay dtVencimento = dtAtual.plusDays(nrDiasLiberacao.intValue());

		liberacaoReguladora.setDtVencimento(dtVencimento);
	}

	@Override
	protected LiberacaoReguladora beforeUpdate(LiberacaoReguladora bean) {
		LiberacaoReguladora liberacaoReguladora = (LiberacaoReguladora)bean;
		getLiberacaoReguladoraDAO().getAdsmHibernateTemplate().evict(liberacaoReguladora);
		LiberacaoReguladora liberacaoReguladoraAnterior = (LiberacaoReguladora)getLiberacaoReguladoraDAO().getHibernateTemplate().get(LiberacaoReguladora.class, liberacaoReguladora.getIdLiberacaoReguladora());

		YearMonthDay dtVencimento = liberacaoReguladora.getDtVencimento();
		YearMonthDay dtVencimentoAnterior = liberacaoReguladoraAnterior.getDtVencimento();
		if(dtVencimentoAnterior != null) {
			if( (dtVencimento == null)
					|| CompareUtils.gt(dtVencimento, liberacaoReguladoraAnterior.getDtVencimento())
					|| CompareUtils.lt(dtVencimento, JTDateTimeUtils.getDataAtual())
			) {
				throw new BusinessException("LMS-26104");
			}
		}

		getLiberacaoReguladoraDAO().getAdsmHibernateTemplate().evict(liberacaoReguladoraAnterior);
		return super.beforeUpdate(bean);
	}

	@Override
	protected LiberacaoReguladora beforeInsert(LiberacaoReguladora bean) {
		LiberacaoReguladora liberacaoReguladora = (LiberacaoReguladora)bean;

		String tpOperacao = liberacaoReguladora.getTpOperacao().getValue();
		Long idLiberacaoReguladora = liberacaoReguladora.getIdLiberacaoReguladora();
		Motorista motorista = liberacaoReguladora.getMotorista();

		if (motorista != null && idLiberacaoReguladora == null) {
			executeLiberacaoMotoristaValido(liberacaoReguladora);
			if("V".equals(tpOperacao)) {
				processLiberacaoViagem(liberacaoReguladora);
			} else if("C".equals(tpOperacao)) {
				processLiberacaoColetaEntrega(liberacaoReguladora);
			}
		}
		return super.beforeInsert(bean);
	}

	public Integer getCountLiberacaoMotorista(
		Long idMotorista,
		Long idReguladoraSeguro,
		YearMonthDay dtInicial,
		YearMonthDay dtFinal,
		String tpOperacao
	) {
		return getLiberacaoReguladoraDAO().getCountLiberacaoMotorista(
				idMotorista,
				idReguladoraSeguro,
				dtInicial,
				dtFinal,
				tpOperacao
		);
	}

	public Map<String, Object> findLiberacaoReguladoraById(Long id) {
		Map<String, Object> result = getLiberacaoReguladoraDAO().findLiberacaoReguladoraById(id);

		Long idMotorista = (Long)result.get("motorista_idMotorista");
		DomainValue tpOperacao = MapUtilsPlus.getDomainValue(result, "tpOperacao");
		DomainValue tpVinculo = MapUtilsPlus.getDomainValue(result, "motorista_tpVinculo");
		YearMonthDay dtVencimento = MapUtilsPlus.getYearMonthDay(result, "dtVencimento");
		result.put("blEnableUpdate",
			verifyUpdateDtVencimento(
				idMotorista,
				tpOperacao,
				tpVinculo,
				dtVencimento,
				id
			)
		);
		return result;
	}

	/**
	 * Verifica se a Data de Vencimento pode ser alterada
	 * @param idMotorista
	 * @param tpOperacao
	 * @param idLiberacaoReguladora
	 * @return
	 */
	public boolean verifyUpdateDtVencimento(
			Long idMotorista,
			DomainValue tpOperacao,
			DomainValue tpVinculo,
			YearMonthDay dtVencimento,
			Long idLiberacaoReguladora
	) {
		if( ("A".equals(tpVinculo.getValue()) || "F".equals(tpVinculo.getValue()))
				&& (dtVencimento != null && CompareUtils.gt(dtVencimento, JTDateTimeUtils.getDataAtual()))
		) {
			LiberacaoReguladora ultimaLiberacaoReguladora = getLiberacaoReguladoraDAO().findLastLiberacaoReguladora(idMotorista, tpOperacao.getValue(), idLiberacaoReguladora);
			if(ultimaLiberacaoReguladora == null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retorna a liberação do meio de transporte, caso não haja liberação será lançada uma exceção
	 * @param idMotorista
	 * @param tpOperacao
	 * @return LiberacaoReguladora
	 */
	public LiberacaoReguladora findLiberacaoReguladoraMotorista(Long idMotorista, String tpOperacao) {
		return findLiberacaoReguladoraMotorista(idMotorista, tpOperacao, true);
	}
	
	public LiberacaoReguladora findLiberacaoReguladoraMotorista(Long idMotorista, String tpOperacao, boolean lancaExcecao) {
		LiberacaoReguladora liberacaoReguladora = getLiberacaoReguladoraDAO().findLiberacaoMotorista(idMotorista, JTDateTimeUtils.getDataAtual(), tpOperacao);
		if(liberacaoReguladora != null)
			return liberacaoReguladora;
		
		liberacaoReguladora = getLiberacaoReguladoraDAO().findLiberacaoReguladoraSemVencimento(idMotorista, tpOperacao);
		if(liberacaoReguladora != null) {
			DateTime dtLiberacao = JTDateTimeUtils.yearMonthDayToDateTime(liberacaoReguladora.getDtLiberacao());
			BigDecimal qtHorasLiberacao = (BigDecimal) configuracoesFacade.getValorParametro("QT_HORAS_LIBERACAO");
			if(CompareUtils.le(dtLiberacao.plusHours(qtHorasLiberacao.intValue()), JTDateTimeUtils.getDataHoraAtual())) {
				Integer nrLiberacoesControleCarga = getLiberacaoReguladoraDAO().getRowCountLiberacaoReguladoraControleCarga(liberacaoReguladora.getIdLiberacaoReguladora());
				if(CompareUtils.gt(nrLiberacoesControleCarga, IntegerUtils.ZERO)) {
					if (lancaExcecao) {
						throw new BusinessException("LMS-26105");
					} else {
						return liberacaoReguladora;
					}
				}
			}
		} else {
			DomainValue operacao = domainValueService.findDomainValueByValue("DM_TIPO_CONTROLE_CARGAS", tpOperacao);
			if (lancaExcecao) {
				throw new BusinessException("LMS-26034", new Object[] { operacao.getDescription().getValue() });
			} else {
				return liberacaoReguladora;
			}
		}
		
		return liberacaoReguladora;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
	public Boolean store(LiberacaoReguladora bean) {
		super.store(bean);
		return verifyUpdateDtVencimento(
			bean.getMotorista().getIdMotorista(),
			bean.getTpOperacao(),
			bean.getMotorista().getTpVinculo(),
			bean.getDtVencimento(),
			bean.getIdLiberacaoReguladora());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getLiberacaoReguladoraDAO().getRowCount(criteria);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setLiberacaoReguladoraDAO(LiberacaoReguladoraDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private LiberacaoReguladoraDAO getLiberacaoReguladoraDAO() {
		return (LiberacaoReguladoraDAO) getDao();
	}
	public void setRegraLiberacaoReguladoraService(RegraLiberacaoReguladoraService regraLiberacaoReguladoraService) {
		this.regraLiberacaoReguladoraService = regraLiberacaoReguladoraService;
	}
	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}