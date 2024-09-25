package com.mercurio.lms.contratacaoveiculos.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.AnexoSolicContratacao;
import com.mercurio.lms.contratacaoveiculos.model.BloqueioMotoristaProp;
import com.mercurio.lms.contratacaoveiculos.model.FluxoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.dao.SolicitacaoContratacaoDAO;
import com.mercurio.lms.expedicao.model.service.EmitirDocumentoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega.DM_TP_CALCULO_TABELA_COLETA_ENTREGA;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.ParcelaTabelaCeService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TabelaColetaEntregaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TipoTabelaColetaEntregaService;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReferenciaFreteCarreteiroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.TipoMeioTranspRotaEvent;
import com.mercurio.lms.municipios.model.service.FilialRotaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.municipios.model.service.PostoPassagemTrechoService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.municipios.model.service.RotaViagemService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.WarningCollector;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Integrante;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.AcaoService;
import com.mercurio.lms.workflow.model.service.GerarEmailMensagemAvisoService;
import com.mercurio.lms.workflow.model.service.IntegranteService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.solicitacaoContratacaoService"
 */
public class SolicitacaoContratacaoService extends CrudService<SolicitacaoContratacao, Long> {
	private WorkflowPendenciaService workflowPendenciaService;	
	private ParcelaTabelaCeService parcelaTabelaCeService;
	private TabelaColetaEntregaService tabelaColetaEntregaService;
	private BloqueioMotoristaPropService bloqueioMotoristaPropService;
	private MeioTransporteService meioTransporteService;
	private ConfiguracoesFacade configuracoesFacade;
	private AcaoService acaoService;
	private FluxoContratacaoService fluxoContratacaoService;
	private IntegranteService integranteService;
	private GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService;
	private UsuarioService usuarioService;
	private EmitirDocumentoService emitirDocumentoService;
	private ChaveFluxoContratacaoService chaveFluxoContratacaoService;
	private FluxoFilialService fluxoFilialService;
	private ReferenciaFreteCarreteiroService referenciaFreteCarreteiroService;
	private EnderecoPessoaService enderecoPessoaService;
	private PostoPassagemTrechoService postoPassagemTrechoService;
	private RotaViagemService rotaViagemService;
	private RotaIdaVoltaService rotaIdaVoltaService;
	private FilialRotaService filialRotaService;
	private EventoPuxadaService eventoPuxadaService;
	private TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService;
	private MeioTranspRodoMotoristaService meioTranspRodoMotoristaService;
	private FilialService filialService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private static final String PARAMETRO_FILIAL = "ATIVA_CALCULO_PADRAO";
	private static final String SIM = "S";

	public EventoPuxadaService getEventoPuxadaService() {
		return eventoPuxadaService;
	}

	public void setEventoPuxadaService(EventoPuxadaService eventoPuxadaService) {
		this.eventoPuxadaService = eventoPuxadaService;
	}
	public static final String[] values = new String[]{"DH","EV","QU","FP", "PF", "PV"};

	/**
	 * Recupera uma instância de <code>SolicitacaoContratacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public SolicitacaoContratacao findById(java.lang.Long id) {
		return (SolicitacaoContratacao)super.findById(id);
	}

	public void validateMotoristaVigentePuxada(SolicitacaoContratacao solicitacaoContratacao){
		List<Motorista> motoristas = meioTranspRodoMotoristaService.findMotoristaVigenteBySolicitacaoContratacao(solicitacaoContratacao);
		if (motoristas==null || motoristas.size() == 0){
			throw new BusinessException("LMS-26108",new Object[]{solicitacaoContratacao.getNrIdentificacaoMeioTransp()});
		}
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		SolicitacaoContratacao bean = (SolicitacaoContratacao)getSolicitacaoContratacaoDAO().getAdsmHibernateTemplate().load(SolicitacaoContratacao.class,id);

		String tpStatusContratacao = bean.getTpSituacaoContratacao().getValue();
		if("SA".equals(tpStatusContratacao)) {
			removePendencias(id);
			getSolicitacaoContratacaoDAO().removeById(id);
		} else {
			throw new BusinessException("LMS-26088");
		}
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids) {
			removeById(id);
		}
	}

	
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public void executeCancel(Long idSolicitacaoContratacao) {
		SolicitacaoContratacao bean = (SolicitacaoContratacao)getSolicitacaoContratacaoDAO().getAdsmHibernateTemplate().get(SolicitacaoContratacao.class,idSolicitacaoContratacao);
		removePendencias(idSolicitacaoContratacao);
		bean.setTpSituacaoContratacao(new DomainValue("CA"));
		getSolicitacaoContratacaoDAO().store(bean);
	}
	
	private void removePendencias(Long idSolicitacaoContratacao) {
		List<Pendencia> pendencias = getSolicitacaoContratacaoDAO().findPendenciaBySolicitacao(idSolicitacaoContratacao);
		for(Pendencia pendencia : pendencias) {
			workflowPendenciaService.cancelPendencia(pendencia.getIdPendencia());
		}
	}

	public Map<String, Object> findByIdView(Long id) {
		return getSolicitacaoContratacaoDAO().findByIdView(id);
	}

	public TypedFlatMap findByIdTela(Long id) {
		SolicitacaoContratacao sc = findById(id);
		if (sc != null) {
			TypedFlatMap result = new TypedFlatMap();
			result.put("tpSituacaoContratacao", sc.getTpSituacaoContratacao().getValue());
			result.put("tpModal", sc.getTpModal().getValue());
			result.put("dtViagem", sc.getDtViagem());
			result.put("nrDddSolicitante", sc.getNrDddSolicitante());
			result.put("nrTelefoneSolicitante", sc.getNrTelefoneSolicitante());
			result.put("dtCriacao", sc.getDtCriacao());
			result.put("obMotivoReprovacao", sc.getObMotivoReprovacao());
			result.put("vlFreteMaximoAutorizado", sc.getVlFreteMaximoAutorizado());
			result.put("vlFreteNegociado", sc.getVlFreteNegociado());
			result.put("vlFreteSugerido", sc.getVlFreteSugerido());
			result.put("idSolicitacaoContratacao", sc.getIdSolicitacaoContratacao());
			result.put("tpSolicitacaoContratacao", sc.getTpSolicitacaoContratacao().getValue());
			if(sc.getTpRotaSolicitacao() != null) {
				result.put("tpRotaSolicitacao", sc.getTpRotaSolicitacao().getValue());
			}
			result.put("tpVinculoContratacao", sc.getTpVinculoContratacao().getValue());
			result.put("nrSolicitacaoContratacao", sc.getNrSolicitacaoContratacao());
			result.put("blIndicadorRastreamento", sc.getBlIndicadorRastreamento());
			result.put("obObservacao", sc.getObObservacao());
			result.put("nrIdentificacaoMeioTransp.nrPlaca", sc.getNrIdentificacaoMeioTransp());
			result.put("nrIdentificacaoSemiReboque.nrPlaca", sc.getNrIdentificacaoSemiReboque());
			result.put("nrAnoFabricacaoMeioTransporteSemiReboque", sc.getNrAnoFabricacaoMeioTransporteSemiReboque());
			result.put("nrAnoFabricacaoMeioTransporte", sc.getNrAnoFabricacaoMeioTransporte());
			// correção para evitar NPE ja q o campo banco permite null
			if (sc.getTpFluxoContratacao() != null) {
				result.put("tpFluxoContratacao", sc.getTpFluxoContratacao().getValue());
			}
			result.put("tpAbrangencia", sc.getTpAbrangencia().getValue());
			result.put("eixosTipoMeioTransporte.qtEixos", sc.getQtEixos());
			result.put("vlPostoPassagem", sc.getVlPostoPassagem());
			result.put("blQuebraMeioTransporte", sc.getBlQuebraMeioTransporte());

			if (sc.getAcao() != null) {
				result.put("idAcao", sc.getAcao().getIdAcao());
			}
			if (sc.getPendencia() != null) {
				result.put("idPendencia", sc.getPendencia().getIdPendencia());
				result.put("tpSituacaoPendencia", sc.getPendencia().getTpSituacaoPendencia().getValue());
			}
			if (sc.getTipoMeioTransporte() != null) {
				result.put("tipoMeioTransporte.tpMeioTransporte", sc.getTipoMeioTransporte().getTpMeioTransporte().getValue());
				result.put("tipoMeioTransporte.idTipoMeioTransporte", sc.getTipoMeioTransporte().getIdTipoMeioTransporte());
				result.put("tipoMeioTransporte.dsTipoMeioTransporte", sc.getTipoMeioTransporte().getDsTipoMeioTransporte());
			}
			if (sc.getFilial() != null) {
				result.put("filial.sgFilial", sc.getFilial().getSgFilial());
				result.put("filial.idFilial", sc.getFilial().getIdFilial());
				result.put("filial.pessoa.nmFantasia", sc.getFilial().getPessoa().getNmFantasia());
				result.put("filial.dtImplantacaoLMS", sc.getFilial().getDtImplantacaoLMS());
			}
			if (sc.getMoedaPais() != null) {
				result.put("moedaPais.idMoedaPais", sc.getMoedaPais().getIdMoedaPais());
			}
			if (sc.getUsuarioSolicitador() != null) {
				result.put("usuarioSolicitador.nmUsuario", sc.getUsuarioSolicitador().getNmUsuario());
				result.put("usuarioSolicitador.idUsuario", sc.getUsuarioSolicitador().getIdUsuario());
				result.put("usuarioSolicitador.nrMatricula", sc.getUsuarioSolicitador().getNrMatricula());
			}
			if (sc.getRota() != null) {
				result.put("rota.idRota", sc.getRota().getIdRota());
			}
			if (sc.getRotaIdaVolta() != null) {
				result.put("idRotaIdaVolta", sc.getRotaIdaVolta().getIdRotaIdaVolta());
			}
			if (sc.getControleCarga() != null) {
				result.put("controleCarga.idControleCarga", sc.getControleCarga().getIdControleCarga());
				result.put("controleCarga.nrControleCarga", sc.getControleCarga().getNrControleCarga());
				result.put("controleCarga.filialByIdFilialOrigem.idFilial", sc.getControleCarga().getFilialByIdFilialOrigem().getIdFilial());
				result.put("controleCarga.filialByIdFilialOrigem.sgFilial", sc.getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
			}
				
			result.put("dtVigenciaInicial", sc.getDtInicioContratacao());
			result.put("dtVigenciaFinal", sc.getDtFimContratacao());
			
			result.put("calculoPadrao", isCalculoPadrao(sc.getFilial().getIdFilial()));
			
			// LMSA-6319
			result.put("cargaCompartilhada.tipo", sc.getTpCargaCompartilhada() != null ? sc.getTpCargaCompartilhada().getValue() : null);
			
			return result;
		}
		return null;
	}

	public List<TipoMeioTranspRotaEvent> findRotaViagemVigente(Long idTipoMeioTransporte,String dsRota,Long idMoedaPais) {
		return getSolicitacaoContratacaoDAO().findRotaViagemVigente(idTipoMeioTransporte, dsRota, idMoedaPais);
	}

	public List<ParcelaTabelaCe> findParcelaTabelaCeByTpParcela(String tpParcela) {
		return findParcelaTabelaCeByTpParcela(tpParcela,null);
	}

	public List<ParcelaTabelaCe> findParcelaTabelaCeByTpParcela(String tpParcela,Long idParcelaTabelaCe) {
		return parcelaTabelaCeService.findParcelaTabelaCeByTpParcela(tpParcela, idParcelaTabelaCe);
	}

	public ParcelaTabelaCe findParcelaTabelaCeByTpParcela(String tpParcela, Long idTipoMeioTransporte, YearMonthDay vigenteEm, Long idFilial) {
		return parcelaTabelaCeService.findParcelaTabelaCeByTpParcela(tpParcela, null, null, idTipoMeioTransporte, vigenteEm, idFilial);
	}

	// FIXME corrigir para retornar o ID
	public SolicitacaoContratacao store(SolicitacaoContratacao bean, List gridEditabled) {
		return store(bean, gridEditabled, new ArrayList());
	}

	/**
	 * Execução de persistência de uma
	 * Solicitacao de Contratacao.
	 * 
	 * @author Christian S. Perone 
	 * @since 18/02/2010
	 * @param bean SolicitacaoContratacao
	 */
	public Serializable store(SolicitacaoContratacao bean)
	{
		return super.store(bean);
	}

	// FIXME corrigir para retornar o ID
	public SolicitacaoContratacao store(SolicitacaoContratacao solicitacaoContratacao, List gridEditabled, List gridFluxoContratacao) {
		//6
		Boolean isNew = Boolean.FALSE;
		
		// regra 3.19
		if("V".equals(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue()) && "A".equals(solicitacaoContratacao.getTpModal().getValue())){
			solicitacaoContratacao.setVlFreteSugerido(new BigDecimal(0.01));
			solicitacaoContratacao.setVlFreteNegociado(new BigDecimal(0.01));
			solicitacaoContratacao.setVlFreteMaximoAutorizado(new BigDecimal(0.01));
			
		}
		
		if(solicitacaoContratacao.getIdSolicitacaoContratacao() == null) {
			isNew = Boolean.TRUE;
			if("A".equals(solicitacaoContratacao.getTpModal().getValue())){
				solicitacaoContratacao.setTpSituacaoContratacao(new DomainValue("AP"));
			} else {
			solicitacaoContratacao.setTpSituacaoContratacao(new DomainValue("SA"));
			}
			
			
			solicitacaoContratacao.setDtCriacao(JTDateTimeUtils.getDataAtual());
		} else {
			//Valida se a situação recebida da tela é a mesma salva no banco, do contrário, ocorreu um erro 
			//na navegação da tela, não permitir seguir o processo sem detalhar novamente o registro
			SolicitacaoContratacao beanOld = findById(solicitacaoContratacao.getIdSolicitacaoContratacao());
			if (! solicitacaoContratacao.getTpSituacaoContratacao().getValue().equals(beanOld.getTpSituacaoContratacao().getValue())) {
				throw new BusinessException("LMS-26101");
			}
			getSolicitacaoContratacaoDAO().getAdsmHibernateTemplate().evict(beanOld);
		}

		//9
		if(solicitacaoContratacao.getVlFreteNegociado() != null
			&& StringUtils.isNotBlank(solicitacaoContratacao.getNrIdentificacaoMeioTransp())
			&& solicitacaoContratacao.getTpSolicitacaoContratacao() != null
			&& ("V".equalsIgnoreCase(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue())
					|| "P".equalsIgnoreCase(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue()))
		) {
			solicitacaoContratacao.setTpSituacaoContratacao(new DomainValue("AP"));
		}

		if(StringUtils.isNotBlank(solicitacaoContratacao.getNrIdentificacaoMeioTransp())
			&& solicitacaoContratacao.getTpSolicitacaoContratacao() != null
			&& "C".equalsIgnoreCase(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue())
			&& "A".equals(solicitacaoContratacao.getTpVinculoContratacao().getValue())
			|| (isCalculoPadrao(solicitacaoContratacao.getFilial().getIdFilial()) && StringUtils.isNotBlank(solicitacaoContratacao.getNrIdentificacaoMeioTransp()) 
			&& solicitacaoContratacao.getTpSolicitacaoContratacao() != null
			&& "C".equalsIgnoreCase(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue()))
		) {
			if(solicitacaoContratacao.getPendencia() != null && "A".equals(solicitacaoContratacao.getPendencia().getTpSituacaoPendencia().getValue())){
				solicitacaoContratacao.setTpSituacaoContratacao(new DomainValue("AP"));			
			}
		}

		if(StringUtils.isNotBlank(solicitacaoContratacao.getNrIdentificacaoMeioTransp())) {
			MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByIdentificacaoAprovado(solicitacaoContratacao.getNrIdentificacaoMeioTransp());
			if(meioTransporte != null) {
				solicitacaoContratacao.setNrAnoFabricacaoMeioTransporte(Long.valueOf((meioTransporte).getNrAnoFabricao().longValue()));
				/*Regra Se a contratação for de coleta/entrega e o tipo de vínculo for 
				 * agregado e o meio de transporte já estiver cadastrado e o 
				 * campo ID_FILIAL_AGREGADO_CE estiver em branco preencher o mesmo com a 
				 * SOLICITACAO_CONTRATACAO.ID_FILIAL,
				 * e o TIPO_VINCULO com "A" - Agregado.*/
				if ("A".equals(solicitacaoContratacao.getTpVinculoContratacao().getValue())
						&& "C".equals(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue())
				) {
					MeioTransporte meioTransp = meioTransporte;
					if (meioTransp.getFilialAgregadoCe() == null) {
						meioTransp.setFilialAgregadoCe(solicitacaoContratacao.getFilial());
						meioTransp.setTpVinculo(solicitacaoContratacao.getTpVinculoContratacao());
						getSolicitacaoContratacaoDAO().store(meioTransp);
					}
				}

				//Regra 11
				if (bloqueioMotoristaPropService.findBloqueiosVigentesByMeioTransporte(meioTransporte.getIdMeioTransporte()).size() > 0)
					throw new BusinessException("LMS-26044");
			}
		}

		if(StringUtils.isNotBlank(solicitacaoContratacao.getNrIdentificacaoSemiReboque())) {
			MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByIdentificacaoAprovado(solicitacaoContratacao.getNrIdentificacaoSemiReboque());
			if(meioTransporte != null) {
				solicitacaoContratacao.setNrAnoFabricacaoMeioTransporteSemiReboque(Long.valueOf(meioTransporte.getNrAnoFabricao().longValue()));
				if (solicitacaoContratacao.getTpVinculoContratacao().getValue().equals("A") && solicitacaoContratacao.getTpSolicitacaoContratacao().getValue().equals("C")) {
					if(meioTransporte.getFilialAgregadoCe() == null) {
						meioTransporte.setFilialAgregadoCe(solicitacaoContratacao.getFilial());
						getSolicitacaoContratacaoDAO().store(meioTransporte);
					}
				}
				if (bloqueioMotoristaPropService.findBloqueiosVigentesByMeioTransporte(Long.valueOf(meioTransporte.getIdMeioTransporte().longValue())).size() > 0)
					throw new BusinessException("LMS-26044");
			}
		}

		boolean isRequiredWorkFlow = isNew.booleanValue();
		if (isNew.booleanValue()) {
			//Auto incremento do nro da solicitação de contratação
			emitirDocumentoService.generateProximoNumero(solicitacaoContratacao);
		}

		
		
		if(solicitacaoContratacao.getIdSolicitacaoContratacao() != null && "V".equals(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue()) && "AP".equals(solicitacaoContratacao.getTpSituacaoContratacao().getValue())) {
			executeWorkflowFluxoContratacao(solicitacaoContratacao);
		}
		
		
		//gerando o evento puxada após aprovação
		if("AP".equals(solicitacaoContratacao.getTpSituacaoContratacao().getValue()) 
				&& solicitacaoContratacao.getTpSolicitacaoContratacao() != null 
				&& "P".equals(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue()) ) {
			eventoPuxadaService.generateEventoPuxada(solicitacaoContratacao, "GE");
		}
		
		super.store(solicitacaoContratacao);

		getSolicitacaoContratacaoDAO().getAdsmHibernateTemplate().flush();

		if ("C".equals(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue())) {
			if ("E".equals(solicitacaoContratacao.getTpVinculoContratacao().getValue()) && !isCalculoPadrao(solicitacaoContratacao.getFilial().getIdFilial())) {
				if (gridEditabled.size() == 0 || gridEditabled.get(0) instanceof TabelaColetaEntrega)
					throw new BusinessException("LMS-26036");

				TabelaColetaEntrega tabelaColetaEntrega = ((ParcelaTabelaCe)gridEditabled.get(0)).getTabelaColetaEntrega();
				tabelaColetaEntrega.setTpSituacaoAprovacao(new DomainValue("S"));
				tabelaColetaEntrega.setTpCalculo(DM_TP_CALCULO_TABELA_COLETA_ENTREGA.CALCULO_1.getDomainValue());
				tabelaColetaEntrega.setTpRegistro(new DomainValue("E"));
				tabelaColetaEntrega.setTipoMeioTransporte(solicitacaoContratacao.getTipoMeioTransporte());
				tabelaColetaEntrega.setSolicitacaoContratacao(solicitacaoContratacao);
				tabelaColetaEntrega.setFilial(solicitacaoContratacao.getFilial());
				tabelaColetaEntrega.setMoedaPais(solicitacaoContratacao.getMoedaPais());

				tabelaColetaEntrega.setTipoTabelaColetaEntrega(this.tipoTabelaColetaEntregaService.findTipoTabelaColetaEntregaByBlNormal(true));

				if (tabelaColetaEntrega.getDtVigenciaInicial().compareTo(JTDateTimeUtils.getDataAtual()) < 0)
					throw new BusinessException("LMS-26089");
				if (StringUtils.isNotBlank(solicitacaoContratacao.getNrIdentificacaoMeioTransp())) {
					MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByIdentificacaoAprovado(solicitacaoContratacao.getNrIdentificacaoMeioTransp());
					if(meioTransporte != null) {
						MeioTransporteRodoviario meioTransporteRodoviario = new MeioTransporteRodoviario();
						meioTransporteRodoviario.setIdMeioTransporte(meioTransporte.getIdMeioTransporte());
						tabelaColetaEntrega.setMeioTransporteRodoviario(meioTransporteRodoviario);
					}
				}

				if (!tabelaColetaEntrega.getBlDomingo().booleanValue()
						&& !tabelaColetaEntrega.getBlQuarta().booleanValue()
						&& !tabelaColetaEntrega.getBlQuinta().booleanValue()
						&& !tabelaColetaEntrega.getBlSabado().booleanValue()
						&& !tabelaColetaEntrega.getBlSegunda().booleanValue()
						&& !tabelaColetaEntrega.getBlSexta().booleanValue()
						&& !tabelaColetaEntrega.getBlTerca().booleanValue()
				) {
					throw new BusinessException("LMS-29020");
				}
				//12.1
				if (StringUtils.isNotBlank(tabelaColetaEntrega.getSolicitacaoContratacao().getNrIdentificacaoMeioTransp())
						&& getSolicitacaoContratacaoDAO().getRowCountTabelaColetaEntregaByFilialMeioTransporte(tabelaColetaEntrega).compareTo(Integer.valueOf(0)) > 0
				) {
					throw new BusinessException("LMS-26059");
				}
				//12.2
				if (tabelaColetaEntrega.getMeioTransporteRodoviario() != null
						&& getSolicitacaoContratacaoDAO().validateMeioTranpIsToAgFilial(tabelaColetaEntrega.getMeioTransporteRodoviario().getIdMeioTransporte(),
													tabelaColetaEntrega.getFilial().getIdFilial()) != null)
						throw new BusinessException("LMS-26060");

				tabelaColetaEntregaService.store(tabelaColetaEntrega);

				boolean blVlNegociado = true;
				for(Iterator i = gridEditabled.iterator(); i.hasNext();) {
					ParcelaTabelaCe parcelas = (ParcelaTabelaCe)i.next();
					if (parcelas.getVlNegociado() == null) {
						blVlNegociado = false;
						break;
					}
				}

				if(solicitacaoContratacao.getTpSituacaoContratacao().getValue().equals("AN")) {
					if (StringUtils.isNotBlank(solicitacaoContratacao.getNrIdentificacaoMeioTransp()) && blVlNegociado) {
						solicitacaoContratacao.setTpSituacaoContratacao(new DomainValue("NE"));
						tabelaColetaEntrega.setPendencia(geraWorkFlow2501(tabelaColetaEntrega));
					} else throw new BusinessException("LMS-26042");
				}

				for(Iterator ie = gridEditabled.iterator(); ie.hasNext();) {
					ParcelaTabelaCe parcelas = (ParcelaTabelaCe)ie.next();
					if (parcelas.getVlSugerido() != null || parcelas.getVlMaximoAprovado() != null || parcelas.getVlNegociado() != null	) {
						if (parcelas.getVlMaximoAprovado() != null && parcelas.getVlNegociado() != null	&& parcelas.getVlMaximoAprovado().compareTo(parcelas.getVlNegociado()) < 0){
							throw new BusinessException("LMS-26043");
						}
						if(validaParcela(parcelas)){
						parcelaTabelaCeService.store(parcelas);
						}
					} else if (parcelas.getIdParcelaTabelaCe() != null){
						parcelaTabelaCeService.removeById(parcelas.getIdParcelaTabelaCe());
				} 
				} 

				if (solicitacaoContratacao.getTpSituacaoContratacao().getValue().equals("SA") && !isNew.booleanValue()) {
					List rs = getSolicitacaoContratacaoDAO().findVlMaxAndDtViagemById(solicitacaoContratacao.getIdSolicitacaoContratacao());
					if (rs.size() <= 0){
						throw new BusinessException("LMS-26037");
				}
			}
			}
		} else {
			if (gridFluxoContratacao != null && gridFluxoContratacao.size() > 0) {
				for (Iterator ie = gridFluxoContratacao.iterator(); ie.hasNext();) {
					((FluxoContratacao)ie.next()).setSolicitacaoContratacao(solicitacaoContratacao);
				}
				if("A".equals(solicitacaoContratacao.getTpModal().getValue())){
					gerarChaveLiberacao(solicitacaoContratacao, gridFluxoContratacao);
				} 
				fluxoContratacaoService.storeList(solicitacaoContratacao.getIdSolicitacaoContratacao(), gridFluxoContratacao);
			}
		}
		if (isRequiredWorkFlow) {
			Pendencia pendencia = geraWorkflowSolicitacao(solicitacaoContratacao);
			solicitacaoContratacao.setPendencia(pendencia);
		}
		
		return solicitacaoContratacao;
	}

	private boolean isCalculoPadrao(Long idFilial) {
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(idFilial, PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			return true;
		}
		return false;
	}

	/**
	 * Valida a Parcela, se ela possuir algum atributo diferente de null e zero, ao mesmo tempo, deve inserir.
	 * @param Parcela
	 * @return Boolean
	 */
	private Boolean validaParcela(ParcelaTabelaCe parcela){
		// Os atributos da Entidade Parcela são do tipo BigDecimal, porém com máscara (0.00) setados pela tela.
		BigDecimal maskedValue = new BigDecimal("0.00");
		
		if(parcela.getVlSugerido() != null && !maskedValue.equals(parcela.getVlSugerido())){
			return true;
		}else if(parcela.getVlMaximoAprovado() != null && !maskedValue.equals(parcela.getVlMaximoAprovado() != null)){
			return true;
		}else if(parcela.getVlNegociado() != null && maskedValue.equals(parcela.getVlNegociado())){
			return true;
		}
		return false;
	}

	private Pendencia geraWorkflowSolicitacao(SolicitacaoContratacao solicitacaoContratacao) {
		Pendencia pendencia;
		
		//nao gera wf para modar aereo
		if("A".equals(solicitacaoContratacao.getTpModal().getValue())){
			return null;
		}
		
		if("P".equals(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue())) {
			StringBuffer dsProcesso = new StringBuffer(configuracoesFacade.getMensagem("solicitacaoContratacaoPuxada"));
			if (solicitacaoContratacao.getRotaIdaVolta() != null) {
				RotaIdaVolta riv = (RotaIdaVolta)getSolicitacaoContratacaoDAO().getAdsmHibernateTemplate().get(RotaIdaVolta.class,solicitacaoContratacao.getRotaIdaVolta().getIdRotaIdaVolta());
				dsProcesso.append(" ").append(FormatUtils.formatDecimal("0000",riv.getNrRota())).append(" ").append(riv.getRota().getDsRota());
			} else if (solicitacaoContratacao.getRota() != null){
				Rota r = (Rota)getSolicitacaoContratacaoDAO().getAdsmHibernateTemplate().get(Rota.class,solicitacaoContratacao.getRota().getIdRota());
				dsProcesso.append(" ").append(r.getDsRota());
			}
			
			pendencia = this.geraWorkFlow(solicitacaoContratacao,dsProcesso.toString(),ConstantesWorkflow.NR2608_VLR_CONTRP);
		
		} else if ("V".equals(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue())) {
			StringBuffer dsProcesso = new StringBuffer(configuracoesFacade.getMensagem("solicitacaoContratacaoParaRota"));
			if (solicitacaoContratacao.getRotaIdaVolta() != null) {
				RotaIdaVolta riv = (RotaIdaVolta)getSolicitacaoContratacaoDAO().getAdsmHibernateTemplate().get(RotaIdaVolta.class,solicitacaoContratacao.getRotaIdaVolta().getIdRotaIdaVolta());
				dsProcesso.append(" ").append(FormatUtils.formatDecimal("0000",riv.getNrRota())).append(" ").append(riv.getRota().getDsRota());
			} else if (solicitacaoContratacao.getRota() != null){
				Rota r = (Rota)getSolicitacaoContratacaoDAO().getAdsmHibernateTemplate().get(Rota.class,solicitacaoContratacao.getRota().getIdRota());
				dsProcesso.append(" ").append(r.getDsRota());
			}
			pendencia = this.geraWorkFlow(solicitacaoContratacao,dsProcesso.toString(),ConstantesWorkflow.NR2601_VLR_CONTRV);
		} else {
			if(solicitacaoContratacao.getTpVinculoContratacao().getValue().equals("A") || isCalculoPadrao(solicitacaoContratacao.getFilial().getIdFilial())){
			// LMS-4050 -- Concatena descrição para atender o evento 2602
				StringBuilder descricao = new StringBuilder();		

				Filial filial = filialService.findById(solicitacaoContratacao.getFilial().getIdFilial()); 
				
				descricao.append(configuracoesFacade.getMensagem("solicitacaoContratacaoColetaEntrega"));
	
			    descricao.append(" ");
				descricao.append(filial.getSgFilial());
				descricao.append(" ");
				descricao.append(new DecimalFormat("0000000000").format(solicitacaoContratacao.getNrSolicitacaoContratacao()));
			
				
				pendencia = this.geraWorkFlow(solicitacaoContratacao, descricao.toString() ,ConstantesWorkflow.NR2602_VLR_CONTRCE);
			} else {
				pendencia = null;
				this.geraWorkFlow(solicitacaoContratacao,configuracoesFacade.getMensagem("solicitacaoContratacaoColetaEntrega"),ConstantesWorkflow.NR2600_APRV);
		}
			
		}
		return pendencia;
	}

	private void geraWorkflowAvisoDescargaTrecho(SolicitacaoContratacao solicitacaoContratacao) {
		List<FluxoContratacao> fluxosContratacao = findFluxoContratacaoBySolicitacaoContratacao(solicitacaoContratacao);

		for (FluxoContratacao fluxoContratacao : fluxosContratacao) {
			workflowPendenciaService.generatePendencia(fluxoContratacao.getFilialOrigem().getIdFilial(), 
														ConstantesWorkflow.NR2607_AVI_DESCARGA_TRECHO, 
														solicitacaoContratacao.getIdSolicitacaoContratacao(), 
														configuracoesFacade.getMensagem("solicitacaoContratacaoAvisaDescarga"), 
														JTDateTimeUtils.getDataHoraAtual());
		}
	}

	public TabelaColetaEntrega findTabelaCEByIdSolicitacao(Long id) {
		return getSolicitacaoContratacaoDAO().findTabelaCEByIdSolicitacao(id);
	}
	
	public List<ParcelaTabelaCe> findParcelaTabelaCEByIdSolicitacao(Long id) {
		return getSolicitacaoContratacaoDAO().findParcelaTabelaCEByIdSolicitacao(id);
	}
	
	/**
	 * Rotina: Verificar Solicitação Meio Transporte
	 * Obs: Verifica se o meio de transporte possui uma solicitação vigente para a necessidade do carregamento
	 * 
	 * @author Samuel
	 * @param idFilial
	 * @param idSolicitacaoContratacao
	 */
	public void validateExistSolicitacaoContratacao(Long idFilial, Long idSolicitacaoContratacao) {
		Validate.notNull(idFilial, "idFilial cannot be null");
		Validate.notNull(idSolicitacaoContratacao, "idSolicitacaoContratacao cannot be null");

		SolicitacaoContratacao sc = getSolicitacaoContratacaoDAO().findSCbyId(idSolicitacaoContratacao);
		if (!idFilial.equals(sc.getFilial().getIdFilial()))
			throw new BusinessException("LMS-26045",new Object[]{configuracoesFacade.getMensagem("solicitacaoNaoEncontrada")});
		if (!sc.getTpSolicitacaoContratacao().getValue().equals("V"))
			throw new BusinessException("LMS-26045",new Object[]{configuracoesFacade.getMensagem("solicitacaoColetaEntrega")});

		List<ControleCarga> controlesCarga = sc.getControleCargas();
		for (ControleCarga controleCarga : controlesCarga) {
			if (!controleCarga.getTpStatusControleCarga().getValue().equals("CA"))
				throw new BusinessException("LMS-26045",new Object[]{configuracoesFacade.getMensagem("solicitacaoAssociadaOutroControleCarga")});
		}
		if (sc.getDtViagem().compareTo(JTDateTimeUtils.getDataAtual().minusDays(2)) < 0)
			throw new BusinessException("LMS-26045",new Object[]{configuracoesFacade.getMensagem("dataViagemForaPrazoMaximoUtilizacaoContratacao")});

		if (sc.getDtViagem().plusDays(((BigDecimal)configuracoesFacade.getValorParametro("QT_DIAS_USO_SOLICITACAO")).intValue()).compareTo(JTDateTimeUtils.getDataAtual()) <= 0)
			throw new BusinessException("LMS-26045", new Object[] {configuracoesFacade.getMensagem("dataViagemForaPrazoMaximo")});

		if (!sc.getTpSituacaoContratacao().getValue().equals("AP"))
			throw new BusinessException("LMS-26045",new Object[]{configuracoesFacade.getMensagem("solicitacaoAindaNaoAprovada")});

		if(StringUtils.isNotBlank(sc.getNrIdentificacaoMeioTransp())) {
			MeioTransporte mt = meioTransporteService.findMeioTransporteByIdentificacaoAprovado(sc.getNrIdentificacaoMeioTransp());
			MeioTransporte mtsr = null;
			if(StringUtils.isNotBlank(sc.getNrIdentificacaoSemiReboque())) {
				mtsr = meioTransporteService.findMeioTransporteByIdentificacaoAprovado(sc.getNrIdentificacaoSemiReboque());
			}

			if(((mt == null || !mt.getTpSituacao().getValue().equals("A")))
					|| ((mtsr != null && !mtsr.getTpSituacao().getValue().equals("A")))
			) {
				throw new BusinessException("LMS-26045", new Object[]{configuracoesFacade.getMensagem("meioTransporteSolicitacaoNaoAtivo")});
			}

			/*Verifica se o meio de transporte possui bloqueios vigentes na data atual*/
			List<BloqueioMotoristaProp> bloqueiosVigentes = bloqueioMotoristaPropService.findBloqueiosVigentesByMeioTransporte(mt.getIdMeioTransporte());
			if (bloqueiosVigentes.size() > 0) {
				throw new BusinessException("LMS-26045", new Object[]{configuracoesFacade.getMensagem("meioTransporteSolicitacaoBloqueado")});
			}
		}
	}

	public List findLookup(TypedFlatMap criteria) {
		return getSolicitacaoContratacaoDAO().findLookup(criteria); 
	}

	private Pendencia geraWorkFlow(SolicitacaoContratacao bean, String dsProcesso, Short evento) {

		return workflowPendenciaService.generatePendencia(bean.getFilial().getIdFilial(), evento,
				bean.getIdSolicitacaoContratacao(), dsProcesso, JTDateTimeUtils.getDataHoraAtual());
	}

	private Pendencia geraWorkFlow2501(TabelaColetaEntrega bean) {
		// LMS-4050 -- Concatena a descrição para atender o evento 2501
		StringBuilder descricao = new StringBuilder();
		descricao.append(configuracoesFacade.getMensagem("LMS-26055"));
		descricao.append(" ");
		
		Filial filial = filialService.findById(bean.getFilial().getIdFilial()); 
		descricao.append(filial.getSgFilial());
		descricao.append(" ");
		descricao.append(new DecimalFormat("0000000000").format(bean.getSolicitacaoContratacao().getNrSolicitacaoContratacao()));
		
		return workflowPendenciaService.generatePendencia(bean.getFilial().getIdFilial(), ConstantesWorkflow.NR2501_VLR_CARTCE,
				bean.getIdTabelaColetaEntrega(), descricao.toString() , JTDateTimeUtils.getDataHoraAtual());
	}

	public String executeWorkflow(List<Long> idsSolicitacao, List<String> tpsStituacao) {
		if (idsSolicitacao.size() != 1)
			throw new IllegalArgumentException("Você deve informar uma solicitacao");
		if (tpsStituacao.size() != 1)
			throw new IllegalArgumentException("Você deve informar uma situação");

		String tpSituacao = (String)tpsStituacao.get(0);
		Long idSolicitacao = (Long)idsSolicitacao.get(0);
		SolicitacaoContratacao bean = findById(idSolicitacao);

		Integer numParcelasAtivas = parcelaTabelaCeService.findParcelasTabelaColetaEntrega(idsSolicitacao.get(0)).size();

		//QUEST CQPRO00024547
		if("A".equals(tpSituacao) && 
			bean.getTpSolicitacaoContratacao().getValue().equals("C") &&
			bean.getTpSituacaoContratacao().getValue().equals("SA") &&
			bean.getPendencia() == null ) {
				
			// LMS-4050 -- Gera descrição para atender o evento 2602
			StringBuilder descricao = new StringBuilder();					
			
			descricao.append(configuracoesFacade.getMensagem("solicitacaoContratacaoColetaEntrega"));

		    descricao.append(" ");
			descricao.append(bean.getFilial().getSgFilial());
			descricao.append(" ");
			descricao.append(new DecimalFormat("0000000000").format(bean.getNrSolicitacaoContratacao()));
			
				Pendencia pendencia = this.geraWorkFlow(bean, descricao.toString() ,ConstantesWorkflow.NR2602_VLR_CONTRCE);
				bean.setPendencia(pendencia);
				
				getSolicitacaoContratacaoDAO().store(bean);
				return null;
		}

		//ESSE IF VALIDA SE ELA PERTENCE AO EVENTO 2603 OU NAO, SE ENTRAR É O EVENTO CITADO ANTERIORMENTE.
		if (!bean.getTpSituacaoContratacao().getValue().equals("SA")) {
			if (!bean.getTpSituacaoContratacao().getValue().equals("AP") && 
					!bean.getTpSituacaoContratacao().getValue().equals("NE") && 
					!bean.getTpSituacaoContratacao().getValue().equals("RE")) {
				if (! tpSituacao.equalsIgnoreCase("C")) {
					throw new BusinessException("LMS-26090");
				}
			}
			return null;
		}

		Object[] parameters = new Object[3];
		parameters[0] = bean.getFilial().getSgFilial();
		parameters[1] = new DecimalFormat("0000000000").format(bean.getNrSolicitacaoContratacao().doubleValue());

		if("A".equals(tpSituacao)) {
			//1 .createAlias("TCE.meioTransporteRodoviario","MTR")
			//Coleta
			if (bean.getTpSolicitacaoContratacao().getValue().equals("C")) {
				if (!bean.getTpVinculoContratacao().getValue().equals("A")) {
					List rs = getSolicitacaoContratacaoDAO().findVlMaxAndDtViagemById(idSolicitacao);
					if (rs.size() != numParcelasAtivas){
						throw new BusinessException("LMS-26037");
				}
				}
			} else {
				if(!BigDecimalUtils.hasValue(bean.getVlFreteMaximoAutorizado()))
					throw new BusinessException("LMS-26037");
			}
			if (bean.getTpSolicitacaoContratacao().getValue().equals("C")){
				if(bean.getTpVinculoContratacao().getValue().equals("A") || bean.getTpVinculoContratacao().getValue().equals("E") && isCalculoPadrao(bean.getFilial().getIdFilial())) {
					bean.setTpSituacaoContratacao(new DomainValue("AP"));			
				}else{
					bean.setTpSituacaoContratacao(new DomainValue("AN"));	
				}
			}else{
				bean.setTpSituacaoContratacao(new DomainValue("AN"));	
			}

			parameters[2] = configuracoesFacade.getMensagem("aprovada");
			workflowPendenciaService.generatePendencia(
				bean.getFilial().getIdFilial(),
				ConstantesWorkflow.NR2603_AVI_RET_CONTR,
				idSolicitacao,
				configuracoesFacade.getMensagem("solicitacaoFoi", parameters),
				JTDateTimeUtils.getDataHoraAtual()
			);
			if(bean.getTpSolicitacaoContratacao().getValue().equals("V")) {
				List<Filial> filiais = findFiliaisRotaBySolicitacao(bean);
				BigDecimal vlPostoPassagem = findVlPostoPassagemRota(bean.getTipoMeioTransporte().getIdTipoMeioTransporte(), bean.getMoedaPais().getIdMoedaPais(), bean.getQtEixos(), bean.getDtViagem(), filiais);
				bean.setVlPostoPassagem(vlPostoPassagem);
				geraWorkflowAvisoDescargaTrecho(bean);
			}
		} else if("R".equals(tpSituacao)) {
			bean.setTpSituacaoContratacao(new DomainValue("RE"));

			parameters[2] = configuracoesFacade.getMensagem("reprovada");
			workflowPendenciaService.generatePendencia(
				bean.getFilial().getIdFilial(),
				ConstantesWorkflow.NR2603_AVI_RET_CONTR,
				idSolicitacao,
				configuracoesFacade.getMensagem("solicitacaoFoi", parameters),
				JTDateTimeUtils.getDataHoraAtual()
			);
		}
		if(bean.getPendencia() != null) {
			bean.setAcao(acaoService.findLastAcaoByPendencia(bean.getPendencia().getIdPendencia()));
		}
		getSolicitacaoContratacaoDAO().store(bean);

		return null;
	}

	public List<Filial> findFiliaisRotaBySolicitacao(SolicitacaoContratacao solicitacaoContratacao) {
		List<Filial> filiais = new ArrayList<Filial>();
		if (solicitacaoContratacao.getRotaIdaVolta() != null) {
			List<FilialRota> filiaisRota = (List<FilialRota>)rotaIdaVoltaService.findFiliaisRotaByIdRotaIdaVolta(solicitacaoContratacao.getRotaIdaVolta().getIdRotaIdaVolta());
			for (FilialRota filialRota: filiaisRota) {
				filiais.add(filialRota.getFilial());
			}
		} else {
			filiais = filialRotaService.findFiliaisRotaByRotaPojos(solicitacaoContratacao.getRota().getIdRota());
		}
		return filiais;
	}

	private void executeWorkflowFluxoContratacao(SolicitacaoContratacao bean) {
		List<FluxoContratacao> fluxosContratacao = findFluxoContratacaoBySolicitacaoContratacao(bean);

		
		for (FluxoContratacao fluxoContratacao : fluxosContratacao) {
			getDao().getAdsmHibernateTemplate().evict(fluxoContratacao.getSolicitacaoContratacao());
			fluxoContratacao.setSolicitacaoContratacao(bean);
		}
		gerarChaveLiberacao(bean, fluxosContratacao);
		
		fluxoContratacaoService.storeAll(fluxosContratacao);
		fluxoContratacaoService.flush();
	}
	
	private void gerarChaveLiberacao(SolicitacaoContratacao solicitacaoContratacao ,List<FluxoContratacao> fluxosContratacao){
		Integer nrPrazo = Integer.valueOf(0);
		//Varre os fluxos
		for (FluxoContratacao fluxoContratacao : fluxosContratacao) {
			//Chamar a rotina calcula chave
			Object[] retornoCalculaChave = chaveFluxoContratacaoService.calcularChaveFluxoContratacao(solicitacaoContratacao, fluxoContratacao, nrPrazo);
			String nrChave = (String)retornoCalculaChave[0];
			nrPrazo = (Integer)retornoCalculaChave[1];
			YearMonthDay dtViagem = (YearMonthDay)retornoCalculaChave[2];
			fluxoContratacao.setNrChaveLiberacao(nrChave);
			sendEmailChaveLiberacao(fluxoContratacao, nrChave, dtViagem);
		}
	}

	private void sendEmailChaveLiberacao(FluxoContratacao fluxoContratacao, String nrChave, YearMonthDay dtViagem) {
		String strTextEmail;
		WarningCollectorUtils.remove();

		strTextEmail = configuracoesFacade.getMensagem("numeroChaveLiberacaoViagem", 
				new Object[]{fluxoContratacao.getTpAbrangencia().getDescription(), nrChave, fluxoContratacao.getFilialOrigem().getSgFilial(), fluxoContratacao.getFilialDestino().getSgFilial(), JTFormatUtils.format(dtViagem)});
		Long idEmpresaSessao = SessionUtils.getEmpresaSessao().getIdEmpresa();
		List<Integrante> integrantes = integranteService.findIntegrantesByComite(((BigDecimal)configuracoesFacade.getValorParametro("COMITE_SENHA_LIBERACAO")).longValue(), fluxoContratacao.getFilialOrigem().getIdFilial(), idEmpresaSessao);
		// Quando não existe nenhum integrante, lançar uma business exception
		if (integrantes.size() <= 0) {
			throw new BusinessException("LMS-39012");
		}
		for (Integrante integrante : integrantes) {
			//Usuário
			Short nrTipoEvento = ConstantesWorkflow.NR2606_AVI_CHAVE_LIBER;
			Usuario usuario = integrante.getUsuario(); 
			if (usuario != null && usuario.getIdUsuario() != null) {
				if (usuario.getDsEmail() == null) {
					new WarningCollector(usuario.getNmUsuario());
					continue;
				}
				gerarEmailMensagemAvisoService.sendEmail(usuario.getDsEmail(), nrTipoEvento, strTextEmail);
				//Busca os substitutos
				sendMailSubstitutos(usuario.getIdUsuario(), null, nrTipoEvento, strTextEmail, fluxoContratacao.getFilialOrigem().getIdFilial(), idEmpresaSessao);
			} else {//Perfil
				List<Usuario> usuariosByPerfil = usuarioService.findUsuariosByPerfil(
					integrante.getPerfil().getIdPerfil(), 
					fluxoContratacao.getFilialOrigem().getIdFilial(), 
					idEmpresaSessao
				);
				for (Usuario usuarioByPerfil : usuariosByPerfil) {
					if (usuarioByPerfil.getDsEmail() == null) {
						new WarningCollector(usuarioByPerfil.getNmUsuario());
						continue;
					}
					gerarEmailMensagemAvisoService.sendEmail(usuarioByPerfil.getDsEmail(), nrTipoEvento, strTextEmail);
				}
				//Buscar os substitutos do perfil
				sendMailSubstitutos(null, integrante.getPerfil().getIdPerfil(), nrTipoEvento, strTextEmail, fluxoContratacao.getFilialOrigem().getIdFilial(), idEmpresaSessao);
			}
		}
	}

	private List<FluxoContratacao> findFluxoContratacaoBySolicitacaoContratacao(SolicitacaoContratacao bean) {
		//Buscar apenas as filiais não implantadas (filialOrigem.dtImplantacaoLMS == null ou filialOrigem.dtImplantacaoLMS > dataAtual)
		return fluxoContratacaoService.find(bean.getIdSolicitacaoContratacao(), JTDateTimeUtils.getDataAtual(), "I");
	}

	private void sendMailSubstitutos(
			Long idUsuario,
			Long idPerfil,
			Short nrTipoEvento,
			String strTextEmail,
			Long idFilialOrigem,
			Long idEmpresa
		) {
		List<Usuario> substitutos;
		if (idUsuario != null) {
			substitutos = usuarioService.findSubstitutoByUsuario(
					idUsuario, 
					idFilialOrigem, 
					idEmpresa
				);
		} else {
			substitutos = usuarioService.findSubstitutoByPerfil(
					idPerfil, 
					idFilialOrigem,
					idEmpresa
			);
		}
		for (Usuario usuario : substitutos) {
			gerarEmailMensagemAvisoService.sendEmail(usuario.getDsEmail(), nrTipoEvento, strTextEmail);
		}
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getSolicitacaoContratacaoDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		List<Map<String, Object>> rs = rsp.getList();
		for (int x = 0; x < rs.size(); x++) {
			Map<String, Object> register = rs.get(x);
			String nrIdentificador = (String)register.get("nrIdentificacaoMeioTransp");
			if (StringUtils.isNotBlank(nrIdentificador)) {
				MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByIdentificacao(nrIdentificador);
				if (meioTransporte != null) {
					register.put("nrFrota", meioTransporte.getNrFrota());
				}
			}

			Map<String, Object> filial = (Map<String, Object>)register.get("filial");

			filial.put("siglaNomeFilial",
					(new StringBuffer().append(filial.get("sgFilial"))
							.append(" - ")
							.append(((Map<String, Object>)filial.get("pessoa")).get("nmPessoa")))
							.toString());

			Map<String, Object> moeda = MapUtils.getMap(MapUtils.getMap(register, "moedaPais"), "moeda");
			String dsMoeda = MapUtils.getString(moeda, "sgMoeda") + " " + MapUtils.getString(moeda, "dsSimbolo");
			moeda.put("dsSiglaSimbolo", dsMoeda);

			if (register.get("vlFreteSugerido") != null) {
				register.put("dsSiglaSimboloVlFreteSugerido", dsMoeda);
			}
			if (register.get("vlFreteMaximoAutorizado") != null) {
				register.put("dsSiglaSimboloVlFreteMaximoAutorizado", dsMoeda);
			}
			if (register.get("vlFreteNegociado") != null) {
				register.put("dsSiglaSimboloVlFreteNegociado", dsMoeda);
			}
			rs.set(x,register);
		}
		return rsp;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getSolicitacaoContratacaoDAO().getRowCount(criteria);
	}

	public List findGridFluxoContratacao(TypedFlatMap criteria) {
		return fluxoContratacaoService.find(criteria);
	}

	public Integer getRowCountFluxoContratacao(TypedFlatMap criteria) {
		return fluxoContratacaoService.getRowCount(criteria);
	}

	/**
	 * Atualiza a Tabela de Coleta/entrega de acordo com pendências em soliciitação de contratação.
	 * @param idMeioTransporte
	 * @param nrMeioTransporte
	 * @param nrSemiReboque
	 */
	public void updateMeioTransporteContratacaoAprovada(final Long idMeioTransporte, final List<SolicitacaoContratacao> ids) {
		getSolicitacaoContratacaoDAO().updateMeioTransporteContratacaoAprovada(idMeioTransporte, ids);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setSolicitacaoContratacaoDAO(SolicitacaoContratacaoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private SolicitacaoContratacaoDAO getSolicitacaoContratacaoDAO() {
		return (SolicitacaoContratacaoDAO) getDao();
	}

	/**
	 * Valida se um meio de transporte possui solicitação de contratação aprovada.
	 * 
	 * @param nrMeioTransporte
	 * @param nrSemiReboque
	 * @param tpVinculo
	 * @param idFilialAgregado
	 * @return TRUE se não existir solicitação de contratação aprovada.
	 */
	public List<SolicitacaoContratacao> validadeMeioTransporteContratacaoAprovada(String nrMeioTransporte, Long idTipoMeioTransporte, String tpVinculo, Long idFilialAgregado) {
		return getSolicitacaoContratacaoDAO().validadeMeioTransporteContratacaoAprovada(nrMeioTransporte,idTipoMeioTransporte, tpVinculo, idFilialAgregado);
	}

	public List findLookup(Map criteria) {
		return super.findLookup(criteria);
	}

	public String findRotaByIdSolicitacaoContratacao(Long idSolicitacaoContratacao){
		return getSolicitacaoContratacaoDAO().findRotaByIdSolicitacaoContratacao(idSolicitacaoContratacao);
	}

	public Map findRotasByIdSolicitacaoContratacao(Long idSolicitacaoContratacao){
		return getSolicitacaoContratacaoDAO().findRotasByIdSolicitacaoContratacao(idSolicitacaoContratacao);
	}

	/**
	 * Busca hrTempViagem, nrDistancia, vlFreteReferencia e vlPostoPassagem
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap findVlSugeridoToRota(TypedFlatMap criteria) {
		String RESULT_NR_DISTANCIA = "nrDistancia";
		String RESULT_HR_TEMP_VIAGEM = "hrTempViagem";
		String RESULT_VL_POSTO_PASSAGEM = "vlPostoPassagem";
		String RESULT_VL_FRETE_REFERENCIA = "vlFreteReferencia";

		Long idSolicitacaoContratacao = criteria.getLong("idSolicitacaoContratacao");
		String dsRota = criteria.getString("dsRota");
		String idFiliais = criteria.getString("idFiliais");
		Long idTipoMeioTransporte = criteria.getLong("idTipoMeioTransporte");
		Long idMoedaPais = criteria.getLong("idMoedaPais");
		Integer qtEixos = criteria.getInteger("qtEixos");
		YearMonthDay dtViagem = criteria.getYearMonthDay("dtViagem");

		TypedFlatMap result = new TypedFlatMap();

		String[] ids = idFiliais.split("-");
		Integer nrDistancia = IntegerUtils.ZERO;
		if (ids.length > 1) {
			//Buscar a distância
			nrDistancia = findDistanciaTotalFluxoFiliais(ids);
			if (!nrDistancia.equals(IntegerUtils.ZERO)) {
				result.put(RESULT_NR_DISTANCIA,nrDistancia);
			} else result.put(RESULT_NR_DISTANCIA,"");

			List<Filial> filiaisRota = new ArrayList<Filial>();
			for(int x = 0; x < ids.length; x++) {
				Filial bean = new Filial();
				bean.setIdFilial(Long.valueOf(ids[x]));
				filiaisRota.add(bean);
			}
		
			Long tpViagem = findHrTempoViagem(nrDistancia);
			if (CompareUtils.ne(tpViagem, LongUtils.ZERO))
				result.put(RESULT_HR_TEMP_VIAGEM, FormatUtils.converteMinutosParaHorasMinutos(tpViagem,FormatUtils.ESCALA_HHH));
			else
				result.put(RESULT_HR_TEMP_VIAGEM, "");

			if (idSolicitacaoContratacao == null) {
				BigDecimal vlPostoPassagem = findVlPostoPassagemRota(idTipoMeioTransporte, idMoedaPais, qtEixos, dtViagem, filiaisRota);
				result.put(RESULT_VL_POSTO_PASSAGEM, vlPostoPassagem);
			}
		}

		//3.7 - Buscar valor de referência
		BigDecimal vlFreteReferencia = BigDecimal.ZERO;
		vlFreteReferencia = findVlFreteReferencia(dsRota, idFiliais, idTipoMeioTransporte, idMoedaPais, nrDistancia);
		if (vlFreteReferencia == null || BigDecimal.ZERO.equals(vlFreteReferencia)) {
			result.put(RESULT_VL_FRETE_REFERENCIA,"");
		} else result.put(RESULT_VL_FRETE_REFERENCIA, vlFreteReferencia);
		return result;
	}

	
	/**
	 * Calcula hrTempViagem
	 * 
	 * @return
	 */
	public Long findHrTempoViagem(Integer nrDistancia) {			
		Long tpViagem = LongUtils.ZERO;
		BigDecimal vlNrDistancia = new BigDecimal(nrDistancia.longValue());
		BigDecimal vlNrTempoDistanciaPadrao = (BigDecimal)configuracoesFacade.getValorParametro("RELACAO_TEMPO_DISTANCIA_PADRAO");
		if (CompareUtils.ne(vlNrDistancia, BigDecimalUtils.ZERO) && CompareUtils.ne(vlNrTempoDistanciaPadrao, BigDecimalUtils.ZERO)) {
			vlNrDistancia = vlNrDistancia.multiply(new BigDecimal(60));
			tpViagem = (vlNrDistancia.divide(vlNrTempoDistanciaPadrao, 0, BigDecimal.ROUND_UP)).longValue();
		}
		return tpViagem;
	}

	
	private BigDecimal findVlPostoPassagemRota(Long idTipoMeioTransporte, Long idMoedaPais, Integer qtEixos, YearMonthDay dtViagem, List filiaisRota) {
		BigDecimal vlPostoPassagem = BigDecimal.ZERO;
		//Buscar valor de pedágio vlPostoPassagem caso tenha informado qt de eixos e data da viagem
		if (dtViagem != null && qtEixos != null) {
			vlPostoPassagem = postoPassagemTrechoService.findValorPostosPassagemRota(filiaisRota, idTipoMeioTransporte, qtEixos, null, null, dtViagem, idMoedaPais);
		}
		return vlPostoPassagem;
	}

	private Integer findDistanciaTotalFluxoFiliais(String[] ids) {
		Integer toReturn;
		List<Filial> filiaisRota = new ArrayList<Filial>();
		for(int x = 0; x < ids.length; x++) {
			Filial bean = new Filial();
			bean.setIdFilial(Long.valueOf(ids[x]));
			filiaisRota.add(bean);
		}
		//Busca nrDistancia (não usar a lista de filiais após esta chamada, pois ela é manipulada dentro do método)
		toReturn = fluxoFilialService.findDistanciaTotalFluxoFilialOrigemDestino(filiaisRota,JTDateTimeUtils.getDataAtual());
		if (toReturn == null) {
			toReturn = IntegerUtils.ZERO;
		}
		return toReturn;
	}

	/**
	 * 3.7 - Buscar valor de referência
	 * @param dsRota
	 * @param idFiliais
	 * @param idTipoMeioTransporte
	 * @param idMoedaPais
	 * @param nrDistancia
	 * @return
	 */
	private BigDecimal findVlFreteReferencia(String dsRota, String idFiliais, Long idTipoMeioTransporte, Long idMoedaPais, Integer nrDistancia) {
		BigDecimal toReturn;
		List<TipoMeioTranspRotaEvent> rs = findRotaViagemVigente(idTipoMeioTransporte,dsRota,idMoedaPais);
		if (rs.size() != 0) {
			TipoMeioTranspRotaEvent tipoMeioTranspRotaEvent = rs.get(0);
			toReturn = tipoMeioTranspRotaEvent.getVlFrete().multiply(new BigDecimal(tipoMeioTranspRotaEvent.getRotaIdaVolta().getNrDistancia().intValue()));
		} else {
			String[] idFilial = idFiliais.split("-");

			toReturn = referenciaFreteCarreteiroService.findValorFreteCarreteiroByUfsFiliais(
				null,
				null,
				Long.valueOf(idFilial[0]),
				Long.valueOf(idFilial[idFilial.length - 1]),
				idMoedaPais,
				idTipoMeioTransporte,
				null
			);
			if (toReturn != null) {
				toReturn = toReturn.multiply(BigDecimal.valueOf(nrDistancia));
			} else{
				//3.5
				EnderecoPessoa epO = enderecoPessoaService.findEnderecoPessoaPadrao(Long.valueOf(idFilial[0]));
				EnderecoPessoa epD = enderecoPessoaService.findEnderecoPessoaPadrao(Long.valueOf(idFilial[idFilial.length - 1]));
				Long idEpO = ((epO != null) ? epO.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa() : null);
				Long idEpD = ((epD != null) ? epD.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa() : null);
				toReturn = referenciaFreteCarreteiroService.findValorFreteCarreteiroByUfsFiliais(idEpO,idEpD,null,null
										,idMoedaPais,idTipoMeioTransporte,nrDistancia);
				if (toReturn != null) {
					toReturn = toReturn.multiply(BigDecimal.valueOf(nrDistancia));
				} else {
					//3.6
					toReturn = referenciaFreteCarreteiroService.findValorFreteCarreteiroByUfsFiliais(null,null,null,null
							,idMoedaPais,idTipoMeioTransporte,nrDistancia);
					if (toReturn != null) {
						toReturn = toReturn.multiply(BigDecimal.valueOf(nrDistancia));
					}
				}
			}
		}
		return toReturn;
	}
	
	public TypedFlatMap findVlSugeridoToRotaExpressa(TypedFlatMap criteria) {
		String RESULT_NR_DISTANCIA = "nrDistancia";
		String RESULT_HR_TEMP_VIAGEM = "hrTempViagem";
		String RESULT_VL_POSTO_PASSAGEM = "vlPostoPassagem";
		String RESULT_VL_FRETE_REFERENCIA = "vlFreteReferencia";
		String RESULT_VL_FRETE_SUGERIDO = "vlFreteSugerido";

		Long idSolicitacaoContratacao = criteria.getLong("idSolicitacaoContratacao");
		Long idRotaIdaVolta = criteria.getLong("idRotaIdaVolta");
		Long idTipoMeioTransporte = criteria.getLong("idTipoMeioTransporte");
		Long idMoedaPais = criteria.getLong("idMoedaPais");
		Integer qtEixos = criteria.getInteger("qtEixos");
		YearMonthDay dtViagem = criteria.getYearMonthDay("dtViagem");

		RotaIdaVolta rota = rotaIdaVoltaService.findById(idRotaIdaVolta);
		List<FilialRota> filiaisRota = (List<FilialRota>)rotaIdaVoltaService.findFiliaisRotaByIdRotaIdaVolta(idRotaIdaVolta);
		List<Filial> filiais = new ArrayList<Filial>();
		for (FilialRota filialRota: filiaisRota) {
			filiais.add(filialRota.getFilial());
		}

		TypedFlatMap result = new TypedFlatMap();
		BigDecimal vlFreteKm = rota.getVlFreteKm();
		if (vlFreteKm == null)
			vlFreteKm = BigDecimal.valueOf(0);
		BigDecimal value = vlFreteKm.multiply(new BigDecimal(rota.getNrDistancia().doubleValue()));
		if (value != null && !value.equals(BigDecimal.ZERO))
			result.put(RESULT_VL_FRETE_REFERENCIA,value);
		else
			result.put(RESULT_VL_FRETE_REFERENCIA,"");

		result.put(RESULT_VL_FRETE_SUGERIDO,value);

		if (rota.getNrDistancia() != null && !rota.getNrDistancia().equals(Integer.valueOf(0)))
			result.put(RESULT_NR_DISTANCIA,rota.getNrDistancia());
		else
			result.put(RESULT_NR_DISTANCIA,"");

		Integer nrTempoViagem = rotaViagemService.findMaiorTempoViagemOfRota(idRotaIdaVolta);
		if (!nrTempoViagem.equals(Integer.valueOf(0))) {
			result.put(RESULT_HR_TEMP_VIAGEM,FormatUtils.converteMinutosParaHorasMinutos(
						Long.valueOf(nrTempoViagem.longValue()),FormatUtils.ESCALA_HHH));
		} else {
			result.put(RESULT_HR_TEMP_VIAGEM,"");
		}

		if (idSolicitacaoContratacao == null) {
			BigDecimal vlPostoPassagem = findVlPostoPassagemRota(idTipoMeioTransporte, idMoedaPais, qtEixos, dtViagem, filiais);
			result.put(RESULT_VL_POSTO_PASSAGEM, vlPostoPassagem);
		}

		return result;
	}
	
	public List<SolicitacaoContratacao> findSolicitacaoContratacao(String nrMeioTransporte, String tpSolicitacaoContratacao, DateTime dhInicioContratacao, Long idFilial) {	
		return getSolicitacaoContratacaoDAO().findSolicitacaoContratacao(nrMeioTransporte, tpSolicitacaoContratacao, dhInicioContratacao, idFilial);	
	}
	
	public List<SolicitacaoContratacao> findSolicitacoesByMeioTransporte( String nrIdentificador, Long idTipoMeioTransporte) {
		return getSolicitacaoContratacaoDAO().findSolicitacoesByMeioTransporte(nrIdentificador, idTipoMeioTransporte);
	}
	
	public ResultSetPage findSolicitacoesContratacaoControleCarga(Map filtros) {
		ResultSetPage resultSetPage = getSolicitacaoContratacaoDAO().findSolicitacoesContratacaoControleCarga(filtros,FindDefinition.createFindDefinition(filtros));
		
		List listaRetorno = new ArrayList();
		for (Iterator iter = resultSetPage.getList().iterator(); iter.hasNext();) {
			Object[] sc = (Object[]) iter.next();
				
			Map map = new HashMap();			
			map.put("nrSolicitacaoContratacao", sc[0]);
			map.put("status", sc[1]);
			map.put("veiculo", sc[2]);
			map.put("dataHora", sc[3]);
			map.put("usuario", sc[4]);
			map.put("tipo_status", sc[5]);
			map.put("id_solicitacao_contratacao", sc[6]);
		
			listaRetorno.add(map);
		}
		resultSetPage.setList(listaRetorno);
		
		return resultSetPage;
		
	}
	
	/**
	 * ET: 26.01.01.01 Solicitar / Aprovar contratação de Meio de Transporte 
	 * Rotina Verificar Solicitação Meio Transporte para Coleta/Entrega
	 * 
	 * @param controleCarga
	 * @param filial
	 */
	public void validateSolicitacaoMeioTransporteColetaEntrega(String tpControleCarga, MeioTransporte meioTransporte, Filial filial){
	    if (meioTransporte == null || "P".equals(meioTransporte.getTpVinculo().getValue())){
	        return;
	    }
	    
	    if (tpControleCarga != null && "C".equals(tpControleCarga)){
	        if (!"A".equals(meioTransporte.getTpSituacao().getValue())){
                throw new BusinessException("LMS-25009");
	        }
	
	        List<SolicitacaoContratacao> solicitacoes = getSolicitacaoContratacaoDAO().findSolicitacoesAprovadasControleCarga(meioTransporte.getNrIdentificador(),filial.getIdFilial());
	        if (solicitacoes == null || solicitacoes.isEmpty()){
	            throw new BusinessException("LMS-05411");
	        }
	    
	        
	       List bloqueios = bloqueioMotoristaPropService.findBloqueiosVigentesByMeioTransporte(meioTransporte.getIdMeioTransporte());
	       if (bloqueios != null && bloqueios.size() > 0){
	           throw new BusinessException("LMS-25010");
	       }

	    }    
	}
	
	
	public Integer getRowCount(Map criteria) {
		return getSolicitacaoContratacaoDAO().getRowCount((Long)criteria.get("idControleCarga"));
	}
	
	public ResultSetPage<AnexoSolicContratacao> findPaginatedAnexoSolicContratacao(PaginatedQuery paginatedQuery) {
		return getSolicitacaoContratacaoDAO().findPaginatedAnexoSolicContratacao(paginatedQuery);
	}
	
	public Integer getRowCountAnexoSolicContratacao(TypedFlatMap criteria) {
		return getSolicitacaoContratacaoDAO().getRowCountAnexoSolicContratacao(criteria);
	}
	
	public AnexoSolicContratacao storeAnexoSolicContratacao(TypedFlatMap map){
		AnexoSolicContratacao anexoSolicContratacao = new AnexoSolicContratacao();
		anexoSolicContratacao.setIdAnexoSolicContratacao(map.getLong("idAnexoSolicContratacao"));

		SolicitacaoContratacao solicitacaoContratacao = new SolicitacaoContratacao();
		solicitacaoContratacao.setIdSolicitacaoContratacao(map.getLong("idSolicitacaoContratacao"));
    	anexoSolicContratacao.setSolicitacaoContratacao(solicitacaoContratacao);
    	    	
    	UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
    	anexoSolicContratacao.setUsuario(usuarioLMS);
    	
    	try{
    		anexoSolicContratacao.setDcArquivo(Base64Util.decode(map.getString("dcArquivo")));
    	} catch (IOException e) {
			throw new InfrastructureException(e);
		}
    	
    	anexoSolicContratacao.setDsAnexo(map.getString("dsAnexo"));
    	anexoSolicContratacao.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
    	
    	getSolicitacaoContratacaoDAO().store(anexoSolicContratacao);
    	
    	return anexoSolicContratacao;
	}
	
	public AnexoSolicContratacao findAnexoSolicContratacaoById(Long idAnexoSolicContratacao) {
		AnexoSolicContratacao anexoSolicContratacao = getSolicitacaoContratacaoDAO().findAnexoSolicContratacaoById(idAnexoSolicContratacao);
		if(anexoSolicContratacao != null){
			Hibernate.initialize(anexoSolicContratacao);
		}
		return anexoSolicContratacao;
	}
	
	public void removeByIdsAnexoSolicContratacao(List ids) {		
		getSolicitacaoContratacaoDAO().removeByIdsAnexoSolicContratacao(ids);
	}
	
	// LMSA-6520: LMSA:6534
	public String getSolicitacaoContratacaoCargaCompartilhada(Long idSolicitacaoContratacao) {
		SolicitacaoContratacao bean = (SolicitacaoContratacao) getSolicitacaoContratacaoDAO().getAdsmHibernateTemplate().get(SolicitacaoContratacao.class,idSolicitacaoContratacao);
		return bean != null && bean.getTpCargaCompartilhada() != null ? bean.getTpCargaCompartilhada().getValue() : null;
	}
	
	public void setIntegranteService(IntegranteService integranteService) {
		this.integranteService = integranteService;
	}
	public void setGerarEmailMensagemAvisoService(GerarEmailMensagemAvisoService gerarEmailMensagemAvisoService) {
		this.gerarEmailMensagemAvisoService = gerarEmailMensagemAvisoService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setFluxoContratacaoService(FluxoContratacaoService fluxoContratacaoService) {
		this.fluxoContratacaoService = fluxoContratacaoService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}
	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setParcelaTabelaCeService(ParcelaTabelaCeService parcelaTabelaCeService) {
		this.parcelaTabelaCeService = parcelaTabelaCeService;
	}
	public void setTabelaColetaEntregaService(TabelaColetaEntregaService tabelaColetaEntregaService) {
		this.tabelaColetaEntregaService = tabelaColetaEntregaService;
	}
	public void setBloqueioMotoristaPropService(BloqueioMotoristaPropService bloqueioMotoristaPropService) {
		this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	}
	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}
	public void setChaveFluxoContratacaoService(ChaveFluxoContratacaoService chaveFluxoContratacaoService) {
		this.chaveFluxoContratacaoService = chaveFluxoContratacaoService;
	}
	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}
	public void setReferenciaFreteCarreteiroService(ReferenciaFreteCarreteiroService referenciaFreteCarreteiroService) {
		this.referenciaFreteCarreteiroService = referenciaFreteCarreteiroService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setPostPassagemTrechoService(PostoPassagemTrechoService postoPassagemTrechoService) {
		this.postoPassagemTrechoService = postoPassagemTrechoService;
	}
	public void setRotaViagemService(RotaViagemService rotaViagemService) {
		this.rotaViagemService = rotaViagemService;
	}
	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}
	public void setFilialRotaService(FilialRotaService filialRotaService) {
		this.filialRotaService = filialRotaService;
	}

	public TipoTabelaColetaEntregaService getTipoTabelaColetaEntregaService() {
		return tipoTabelaColetaEntregaService;
	}

	public void setTipoTabelaColetaEntregaService(
			TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService) {
		this.tipoTabelaColetaEntregaService = tipoTabelaColetaEntregaService;
	}

	public void setMeioTranspRodoMotoristaService(
            MeioTranspRodoMotoristaService meioTranspRodoMotoristaService) {
    	this.meioTranspRodoMotoristaService = meioTranspRodoMotoristaService;
    }

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public Boolean validateSolicitacaoValida(Long idFilial,	String nrIdentificador) {		
		return getSolicitacaoContratacaoDAO().findSolicitacaoValida(idFilial, nrIdentificador) > 0;
	}
	
	public List<Object[]> findUltimaSolicitacaoValida(Long idFilial,	String nrIdentificador, DateTime dateTime) {
		return getSolicitacaoContratacaoDAO().findUltimaSolicitacaoValida(idFilial,nrIdentificador,dateTime); 
	}
	
}