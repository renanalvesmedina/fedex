package com.mercurio.lms.portaria.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.ConfiguracaoAuditoria;
import com.mercurio.lms.portaria.model.dao.ConfiguracaoAuditoriaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;


/**
 * Classe de serviço para CRUD:    
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.configuracaoAuditoriaService"
 */
public class ConfiguracaoAuditoriaService extends CrudService<ConfiguracaoAuditoria, Long> {

	private MeioTransporteService meioTransporteService;
	private EventoMeioTransporteService eventoMeioTransporteService;
	private ConfiguracoesFacade configuracoesFacade;
	private VigenciaService vigenciaService;
    private ControleCargaService controleCargaService;
    private WorkflowPendenciaService workflowPendenciaService;
    
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	
	/**
	 * Recupera uma instância de <code>ConfiguracaoAuditoria</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public ConfiguracaoAuditoria findById(java.lang.Long id) {
        return (ConfiguracaoAuditoria)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }
    
	protected void beforeRemoveByIds(List ids) {
		ConfiguracaoAuditoria bean = null;
		for (Iterator ie = ids.iterator(); ie.hasNext();) {
			bean = findById((Long) ie.next());
			JTVigenciaUtils.validaVigenciaRemocao(bean);
		}
		super.beforeRemoveByIds(ids);
	}

	protected void beforeRemoveById(Long id) {
		List list = new ArrayList();
		list.add(id);
		beforeRemoveByIds(list);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ConfiguracaoAuditoria bean) {
        return super.store(bean);        
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setConfiguracaoAuditoriaDAO(ConfiguracaoAuditoriaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ConfiguracaoAuditoriaDAO getConfiguracaoAuditoriaDAO() {
        return (ConfiguracaoAuditoriaDAO) getDao();
    }
   
	protected ConfiguracaoAuditoria beforeStore(ConfiguracaoAuditoria bean) {
		ConfiguracaoAuditoria configuracaoAuditoria = (ConfiguracaoAuditoria) bean;
		
		vigenciaService.validaVigenciaBeforeStore(configuracaoAuditoria);
		
		int qtVeiculosProprios = configuracaoAuditoria.getQtVeiculosProprios() == null ? 0 : configuracaoAuditoria.getQtVeiculosProprios().intValue();
		int qtVeiculosTerceiros = configuracaoAuditoria.getQtVeiculosTerceiros() == null ? 0 : configuracaoAuditoria.getQtVeiculosTerceiros().intValue();
		
		int nrPrazoAuditoria = configuracaoAuditoria.getNrPrazoAuditoria().intValue();
		int hrTempoAuditoria = configuracaoAuditoria.getHrTempoAuditoria().intValue();
				
		// a soma entre meios de transporte proprios e de terceiros deve ser maior que 0
		if (qtVeiculosProprios + qtVeiculosTerceiros == 0)
			throw new BusinessException("LMS-06004");
		
		if (nrPrazoAuditoria <= 0 || hrTempoAuditoria <= 0){
			throw new BusinessException("LMS-06021");
		}
		
		// nao deve haver mais de uma configuração vigente para a mesma filial, mesmo tipo de operacao,
		// e no mesmo intervalo de horario de auditoria
		if (isThereConfiguracaoVigente(configuracaoAuditoria.getIdConfiguracaoAuditoria(),
							configuracaoAuditoria.getFilial().getIdFilial(), 
							configuracaoAuditoria.getTpOperacao(), 
							configuracaoAuditoria.getDtVigenciaInicial(),
							configuracaoAuditoria.getDtVigenciaFinal(),
							configuracaoAuditoria.getHrConfiguracaoInicial(),
							configuracaoAuditoria.getHrConfiguracaoFinal())) {
			throw new BusinessException("LMS-00003");
		}
		
		return super.beforeStore(bean);
	}
	
	public boolean isThereConfiguracaoVigente(Long idConfiguracaoAuditoria, Long idFilial, DomainValue tpOperacao, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, TimeOfDay hrConfiguracaoInicial, TimeOfDay hrConfiguracaoFinal) {
		return getConfiguracaoAuditoriaDAO().isThereConfiguracaoVigente(idConfiguracaoAuditoria, idFilial, tpOperacao, dtVigenciaInicial, dtVigenciaFinal, hrConfiguracaoInicial, hrConfiguracaoFinal);
	}

	
	/**
	 * ROTINA ET 06.04.01.01 Retornar todas as auditorias já realizadas conforma parametros
	 * 
	 * Método que retorna o count(*) de todas as auditorias já realizadas, de acordo com os parâmetros.
	 * @param idFilial Filial de onde a consulta está sendo realizada.
	 * @param dtConsulta Data em que a contagem de auditorias deve ser realizada.
	 * @param hrInicioAuditoria Horário em que a auditoria foi iniciada.
	 * @param hrFinalAuditoria Horário em que a auditoria será finalizada.
	 * @param somenteProprios Se true, somente serão levados em consideracao, veiculos próprios. 
	 * @param somenteTerceiros Se true, somente serão levados em consideracao, veiculos de terceiros.
	 * @param tpOperacao Tipo de operacao que o meio de transporte efetuava no momento da auditoria.
	 * @return Contagem de auditorias realizadas.
	 * @author luisfco
	 */
	public Integer contarAuditorias(Long idFilial, DateTime dtConsulta, TimeOfDay hrInicioAuditoria, TimeOfDay hrFinalAuditoria, 
			boolean somenteProprios, boolean somenteTerceiros, String tpOperacao) {
		return getConfiguracaoAuditoriaDAO().contarAuditorias(idFilial, dtConsulta, hrInicioAuditoria, hrFinalAuditoria, somenteProprios, somenteTerceiros, tpOperacao);
	}
	
	public Integer findContarAuditorias(Long idFilial, DateTime dtConsulta, TimeOfDay hrInicioAuditoria, TimeOfDay hrFinalAuditoria, 
			boolean somenteProprios, boolean somenteTerceiros, String tpOperacao) {
		return contarAuditorias(idFilial, dtConsulta, hrInicioAuditoria, hrFinalAuditoria, somenteProprios, somenteTerceiros, tpOperacao);
	}
	
	public String executeVerificarMeioTransporteParaAuditoria(Long idFilial, Long idMeioTransporte) {
		return verificarMeioTransporteParaAuditoria(idFilial, idMeioTransporte, false);
	}

	public String executeVerificarMeioTransporteParaAuditoriaCodigoMensagem(Long idFilial, Long idMeioTransporte) {
		return verificarMeioTransporteParaAuditoria(idFilial, idMeioTransporte, true);
	}
	
	/**
	 * ROTINA ET 06.04.01.01 Verificar seleção do meio de transporte para auditoria
	 * 
	 * @param idFilial
	 * @param idMeioTransporte
	 * @param codigoMensagem Se true, retorna o código da mensagem ao invés de seu conteúdo
	 * @return Mensagem de retorno ou código da mensagem de retorno
	 */
	public String verificarMeioTransporteParaAuditoria(Long idFilial, Long idMeioTransporte, boolean codigoMensagem) {
		MeioTransporte meioTransporte = getMeioTransporteService().findByIdInitLazyProperties(idMeioTransporte, false);

		String alerta = null;
		if(codigoMensagem) {
			alerta = "LMS-06001";
		} else {
			alerta = configuracoesFacade.getMensagem("LMS-06001");
		}


		// REGRA 01: se sábado ou domingo, então nao faz nada
		int diaSemana = JTDateTimeUtils.getNroDiaSemana(JTDateTimeUtils.getDataAtual());
		if ((diaSemana == DateTimeConstants.SATURDAY) || (diaSemana == DateTimeConstants.SUNDAY)) {
			return null;
		}
		
		List eventosMeioTransporte = findEventosMeioTransporteByIdMeioTransporte(idFilial, idMeioTransporte);
		
		// REGRA 02: se está em auditoria, então veiculo nao pode sair
		if (isMeioTransporteEmAuditoria(eventosMeioTransporte)) {
			return alerta;
		}
		
		// REGRA 03: se o meio de transporte acabou de ser auditado ou se o mesmo foi auditado, reprovado e recarregado
		if (isMeioTransporteAuditado(eventosMeioTransporte)) {
			return null;
		}
			
		// REGRA 04: se existe auditoria vigente para o meio transporte
		if (isThereConfiguracaoAuditoriaFilVigenteParaMeioTransporte(idMeioTransporte)) {
			gerarEventoMeioTransporte(idFilial, idMeioTransporte);
			return alerta;
		}
		
		// REGRA 4.1	
		
		DomainValue tipoOperacao =  getTipoOperacaoControleCargaDoMeioTransporte(idMeioTransporte);
		String tpOperacaoMeioTransporte = tipoOperacao != null ? tipoOperacao.getValue() : "";
		
		ConfiguracaoAuditoria ca = null;
		
		// REGRA 4.1
		if (StringUtils.isNotBlank(tpOperacaoMeioTransporte)) {
			List list = getConfiguracaoAuditoriaVigenteParaTpOperacao(tpOperacaoMeioTransporte, idFilial);

			// REGRA 4.2
			if (list.size() == 1) {
				ca = (ConfiguracaoAuditoria) list.get(0); 
				YearMonthDay hojeMenosDiasPrazoAuditoria = JTDateTimeUtils.getDataAtual();

				///retorna a data/hora da ultima auditoria para esta congiguracao
				DateTime dataHoraAuditoria = getConfiguracaoAuditoriaDAO().getDataUltimaAuditoriaRealizada(idFilial, JTDateTimeUtils.getDataHoraAtual(), ca.getHrConfiguracaoInicial(), ca.getHrConfiguracaoFinal(), false, false, ca.getTpOperacao().getValue());

				// tempo da auditoria
				BigDecimal minutosAdicionais =  new BigDecimal(ca.getHrTempoAuditoria().intValue());
				
				// hora da configuracao inicial = hora da configuracao inicial + minutos adicionais
				TimeOfDay configuracaoInicial = new TimeOfDay();
				if (dataHoraAuditoria == null || !JTDateTimeUtils.getDataAtual().isEqual(dataHoraAuditoria.toYearMonthDay())){
					configuracaoInicial = ca.getHrConfiguracaoInicial();
				}else {
					configuracaoInicial = new TimeOfDay(dataHoraAuditoria); 
					configuracaoInicial = configuracaoInicial.plusMinutes(minutosAdicionais.intValue());
				}    
				
				// REGRA 4.4: se hora inicial + minutos adicionais menor que a hora final
				if (configuracaoInicial.isBefore(ca.getHrConfiguracaoFinal()) && configuracaoInicial.compareTo(JTDateTimeUtils.getHorarioAtual()) <= 0) {
					
					// REGRA 4.5
					if (tpOperacaoConfiguracaoEqualsTpOperacaoControleCarga(ca.getTpOperacao().getValue(), tpOperacaoMeioTransporte) ) {
						
						// REGRA 4.6: se meio transporte próprio
						if ("P".equals(meioTransporte.getTpVinculo().getValue())) {
							Integer nrAuditoriasProprios = contarAuditorias(idFilial, JTDateTimeUtils.getDataHoraAtual(), ca.getHrConfiguracaoInicial(), ca.getHrConfiguracaoFinal(), true, false, ca.getTpOperacao().getValue());
							
							Integer qtVeiculosProprios = ca.getQtVeiculosProprios() != null ? ca.getQtVeiculosProprios().intValue() : 0;
							
							// se qtde. veiculos proprios > qtde. auditorias realizadas
							if (qtVeiculosProprios.compareTo(nrAuditoriasProprios) > 0 && isPrazoAuditoria(ca, hojeMenosDiasPrazoAuditoria, dataHoraAuditoria)) {
								gerarEventoMeioTransporte(idFilial, idMeioTransporte);
								return alerta;
							}
							
						// REGRA 4.6: senao, se meio transporte de terceiros
						} else {
							Integer nrAuditoriasTerceiros = contarAuditorias(idFilial, JTDateTimeUtils.getDataHoraAtual(), ca.getHrConfiguracaoInicial(), ca.getHrConfiguracaoFinal(), false, true, ca.getTpOperacao().getValue());
							nrAuditoriasTerceiros = nrAuditoriasTerceiros != null ? nrAuditoriasTerceiros : 0;

							Integer qtVeiculosTerceiros = ca.getQtVeiculosTerceiros() != null ? ca.getQtVeiculosTerceiros().intValue() : 0;
							
							// se qtde. veiculos terceiros > qtde. auditorias realizadas
							if (qtVeiculosTerceiros.compareTo(nrAuditoriasTerceiros) > 0 && isPrazoAuditoria(ca, hojeMenosDiasPrazoAuditoria, dataHoraAuditoria)) {
								gerarEventoMeioTransporte(idFilial, idMeioTransporte);
								return alerta;
							}
						}
					}
				}
			}
		}
		
		return null;
	}

	private boolean isPrazoAuditoria(ConfiguracaoAuditoria ca, YearMonthDay hojeMenosDiasPrazoAuditoria, DateTime dataHoraAuditoria) {
		Period period =  new Period(new YearMonthDay(dataHoraAuditoria), hojeMenosDiasPrazoAuditoria, PeriodType.days());
		return (period.getDays() == 0 || (period.getDays() > ca.getNrPrazoAuditoria().intValue()));
	}
	
	private boolean tpOperacaoConfiguracaoEqualsTpOperacaoControleCarga(String tpOperacaoConfiguracao, String tpOperacaoControleCarga) {
		
		if ("A".equals(tpOperacaoConfiguracao))
			return true;
		else if ("E".equals(tpOperacaoConfiguracao) && "C".equals(tpOperacaoControleCarga))
			return true;
		else if ("V".equals(tpOperacaoConfiguracao) && "V".equals(tpOperacaoControleCarga))
			return true;

		return false;
	}

	/**
	 * Verifica se o meio de transporte está em auditoria apartir dos eventos do mesmo
	 * @param eventosMeioTransporte
	 * @return
	 */
	private boolean isMeioTransporteEmAuditoria(List eventosMeioTransporte) {
		String tpSituacaoMeioTransporteUltimoEvento = "";
		if (eventosMeioTransporte.size() > 0) {
			tpSituacaoMeioTransporteUltimoEvento = ((EventoMeioTransporte)eventosMeioTransporte.get(eventosMeioTransporte.size() - 1)).getTpSituacaoMeioTransporte().getValue();
			
		}
		return "EADT".equals(tpSituacaoMeioTransporteUltimoEvento);
	}
	
	/**
	 * Verifica se o meio de transporte acabou de ser auditado ou se o mesmo foi auditado, reprovado e recarregado 
	 * @param eventosMeioTransporte
	 * @return
	 */
	private boolean isMeioTransporteAuditado(List eventosMeioTransporte) {
		String tpSituacaoMeioTransporteUltimoEvento = "";
		String tpSituacaoMeioTransportePenultimoEvento = "";
		if (eventosMeioTransporte.size() > 1) {
			tpSituacaoMeioTransporteUltimoEvento = ((EventoMeioTransporte)eventosMeioTransporte.get(eventosMeioTransporte.size() - 1)).getTpSituacaoMeioTransporte().getValue();
			tpSituacaoMeioTransportePenultimoEvento = ((EventoMeioTransporte)eventosMeioTransporte.get(eventosMeioTransporte.size() - 2)).getTpSituacaoMeioTransporte().getValue();
			
		}
		return "EADT".equals(tpSituacaoMeioTransportePenultimoEvento) && "AGSA".equals(tpSituacaoMeioTransporteUltimoEvento);
	}
	
	private List findEventosMeioTransporteByIdMeioTransporte(Long idFilial, Long idMeioTransporte) {
		return getConfiguracaoAuditoriaDAO().findEventosMeioTransporteByIdMeioTransporte(idFilial, idMeioTransporte);
	}

	private boolean isThereConfiguracaoAuditoriaFilVigenteParaMeioTransporte(Long idMeioTransporte) {
		return getConfiguracaoAuditoriaDAO().isThereConfiguracaoAuditoriaFilVigenteParaMeioTransporte(idMeioTransporte);
	}
	
	private DomainValue getTipoOperacaoControleCargaDoMeioTransporte(Long idMeioTransporte) {
		return getConfiguracaoAuditoriaDAO().getTipoOperacaoControleCargaDoMeioTransporte(idMeioTransporte);
	}

	public List getConfiguracaoAuditoriaVigenteParaTpOperacao(String tpOperacao, Long idFilial) {
		return getConfiguracaoAuditoriaDAO().getConfiguracaoAuditoriaVigenteParaTpOperacao(tpOperacao, idFilial);
	}
	
	public Long getIdControleCargaByIdMeioTransporte(Long idMeioTransporte) {
		return getConfiguracaoAuditoriaDAO().getIdControleCargaByIdMeioTransporte(idMeioTransporte);
	}

	
	private void gerarEventoMeioTransporte(Long idFilial, Long idMeioTransporte) {
		
		Long idControleCarga = getIdControleCargaByIdMeioTransporte(idMeioTransporte);
		
		EventoMeioTransporte eventoMeioTranporte = new EventoMeioTransporte();
		Filial filial = new Filial();
		filial.setIdFilial(idFilial);
		MeioTransporte meioTransporte = new MeioTransporte();
		meioTransporte.setIdMeioTransporte(idMeioTransporte);
		
		ControleCarga controleCarga = (ControleCarga)getConfiguracaoAuditoriaDAO().getAdsmHibernateTemplate()
				.load(ControleCarga.class,idControleCarga);		
		controleCarga.setTpStatusControleCarga(new DomainValue("EA"));
		
		controleCargaService.store(controleCarga);
		
		eventoMeioTranporte.setMeioTransporte(meioTransporte);
		eventoMeioTranporte.setFilial(filial);
		eventoMeioTranporte.setControleCarga(controleCarga);
		DomainValue tpSituacaoMeioTransporte = new DomainValue();
		tpSituacaoMeioTransporte.setValue("EADT");
		eventoMeioTranporte.setTpSituacaoMeioTransporte(tpSituacaoMeioTransporte);
		eventoMeioTranporte.setDhInicioEvento(JTDateTimeUtils.getDataHoraAtual());
		
		getEventoMeioTransporteService().generateEvent(eventoMeioTranporte);
		
		
		MeioTransporte mt = controleCarga.getMeioTransporteByIdTransportado();
		String strIdentificacaoMT = new StringBuffer()
				.append(mt.getNrFrota())
				.append(" - ")
				.append(mt.getNrIdentificador())
				.toString();
		
		this.workflowPendenciaService.generatePendencia(SessionUtils.getFilialSessao().getIdFilial(),
				//número do evento referente à aprovação de km maior que o máximo da rota.
				ConstantesWorkflow.NR605_MT_SELEC_AUD,
				idControleCarga, 
				configuracoesFacade.getMensagem("LMS-06029",new Object[]{strIdentificacaoMT}), 
				JTDateTimeUtils.getDataHoraAtual());
	}

	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public EventoMeioTransporteService getEventoMeioTransporteService() {
		return eventoMeioTransporteService;
	}

	public void setEventoMeioTransporteService(EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * @param controleCargaService The controleCargaService to set.
	 */
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
    
   }