package com.mercurio.lms.contratacaoveiculos.model.service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
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
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.dao.GerarDadosFrotaDAO;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.AnexoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspConteudoAtrib;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransportePeriferico;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.MeioTransporteDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.WarningCollector;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.PendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD: 
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.meioTransporteService"
 */
public class MeioTransporteService extends CrudService<MeioTransporte, Long> {

    private static final String LMS_26156 = "LMS-26156";
	private static final String LMS_26154 = "LMS-26154";
	private static final String LMS_26153 = "LMS-26153";
	private static final String LMS_26155 = "LMS-26155";
	private static final String SITUACAO_APROVADA = "A";
	private static final BigDecimal PERCENTUAL = BigDecimal.valueOf(100);
    private static final BigDecimal TWO = BigDecimal.valueOf(2);
	private static final Object TIPO_VINCULO_PROPRIO = "P";
	private static final String STATUS_ATIVO = SITUACAO_APROVADA;
	private static final String STATUS_INATIVO = "I";

	private GerarDadosFrotaDAO gerarDadosFrotaDAO;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	private ModeloMeioTranspAtributoService modeloMeioTranspAtributoService;
	private ConteudoAtributoModeloService conteudoAtributoModeloService;
	private MeioTranspConteudoAtribService meioTranspConteudoAtribService;
	private BloqueioMotoristaPropService bloqueioMotoristaPropService;
	private SolicitacaoContratacaoService solicitacaoContratacaoService;
	private ConfiguracoesFacade configuracoesFacade;
	private HistoricoFilialService historicoFilialService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private EixosTipoMeioTransporteService eixosTipoMeioTransporteService;
	private ExecuteWorkflowMeioTransporteService executeWorkflowMeioTransporteService;
	private PendenciaService pendenciaService;

	
	/**
	 * Recupera uma instância de <code>MeioTransporte</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MeioTransporte findById(java.lang.Long id) {
		return (MeioTransporte) super.findById(id);
	}
	public MeioTransporte findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
		return (MeioTransporte)super.findByIdInitLazyProperties(id, initializeLazyProperties);
	}

	
	public MeioTransporte findMeioTransporteByCodigoBarras(Long codigoBarras) {
		return (MeioTransporte) getMeioTransporteDAO().findMeioTransporteByCodigoBarras(codigoBarras);
	}

	public MeioTransporte findMeioTransporteByNrFrota(String nrFrota) {
		return (MeioTransporte) getMeioTransporteDAO().findMeioTransporteByNrFrota(nrFrota);
	}

	/**
	 * Recupera uma instância de <code>MeioTransporte</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public MeioTransporte findByIdCustom(Long id) {
		return getMeioTransporteDAO().findByIdCustom(id);
	}

	@Override
	public List findLookup(Map criteria) {
		criteria.put("nrFrota", FormatUtils.formatNrFrota(MapUtils.getString(criteria, "nrFrota")));
		return super.findLookup(criteria);
	}
	
	// LMS-6178
	public TypedFlatMap findDataLookupByIdMeioTransporte(TypedFlatMap tfm) {
		String tpVeiculo = getMeioTransporteDAO().findTipoVeiculoByIdMeioTransporte(tfm.getLong("idMeioTransporte"));
		
		TypedFlatMap retorno = new TypedFlatMap();
		MeioTransporte meioTransporte = findById(tfm.getLong("idMeioTransporte"));
		
		if(meioTransporte != null) {
			retorno.put("tipoVeiculo", tpVeiculo);
			retorno.put("nrCertificado", meioTransporte.getMeioTransporteRodoviario().getNrCertificado());
			retorno.put("nrFrota", meioTransporte.getNrFrota());
			retorno.put("nrIdentificacao", meioTransporte.getNrIdentificador());
			retorno.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
		}		
		return retorno;
	}
	
	public String findTipoVeiculoByIdMeioTransporte(Long idMeioTransporte) {
		return getMeioTransporteDAO().findTipoVeiculoByIdMeioTransporte(idMeioTransporte);
	}
	
	public List<Map<String, Object>> findMeioTransportePerifericos(MeioTransporte meioTransporte) {
		Map<String, Object> mapTemp;
		List<MeioTransportePeriferico> meiosTransportePeriferico = getMeioTransporteDAO().findMeioTransportePerifericos(meioTransporte);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(meiosTransportePeriferico.size());
		for (MeioTransportePeriferico meioTransportePeriferico : meiosTransportePeriferico) {
			mapTemp = new HashMap<String, Object>();
			mapTemp.put("idMeioTransportePeriferico", meioTransportePeriferico.getIdMeioTransportePeriferico());
			mapTemp.put("idPerifericoRastreador", meioTransportePeriferico.getPerifericoRastreador().getIdPerifericoRastreador());
			mapTemp.put("dsPerifericoRastreador", meioTransportePeriferico.getPerifericoRastreador().getDsPerifericoRastreador());
			result.add(mapTemp);
		}
		return result;
	}

	public List<TypedFlatMap> findLookupWithProprietario(TypedFlatMap criteria) {
		List<TypedFlatMap> result = new ArrayList<TypedFlatMap>();
		String liberado = configuracoesFacade.getMensagem("liberado");
		String bloqueado = configuracoesFacade.getMensagem("bloqueado");

		AliasToTypedFlatMapResultTransformer a = new AliasToTypedFlatMapResultTransformer();
		List<Map<String, Object>> meiosTransporte = getMeioTransporteDAO().findLookupWithProprietario(criteria);
		for(Map<String, Object> map : meiosTransporte) {
			TypedFlatMap row = a.transformeTupleMap(map);

			MeioTransporte mt = new MeioTransporte();
			mt.setIdMeioTransporte(row.getLong("idMeioTransporte"));
			row.put("tpStatus", bloqueioMotoristaPropService.validateBloqueiosVigentes(mt) ? bloqueado : liberado);

			result.add(row);
		}
		return result;
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		ResultSetPage rsp = getMeioTransporteDAO().findPaginatedCustom(criteria,FindDefinition.createFindDefinition(criteria));

		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {
			String liberado = configuracoesFacade.getMensagem("liberado");
			String bloqueado = configuracoesFacade.getMensagem("bloqueado");
			AliasToTypedFlatMapResultTransformer a = new AliasToTypedFlatMapResultTransformer();

			public Map<String, Object> filterItem(Object item) {
				Map<String, Object> map = (Map<String, Object>)item;
				TypedFlatMap row = a.transformeTupleMap(map);

				MeioTransporte mt = new MeioTransporte();
				mt.setIdMeioTransporte(row.getLong("idMeioTransporte"));
				row.put("tpStatus", bloqueioMotoristaPropService.validateBloqueiosVigentes(mt) ? bloqueado : liberado);

				return row;
			}

		};
		return (ResultSetPage)frsp.doFilter();
	}

	public Integer getRowCountCustom(TypedFlatMap map) {
		return getMeioTransporteDAO().getRowCountCustom(map);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		MeioTransporte mt = (MeioTransporte) getMeioTransporteDAO()
				.getAdsmHibernateTemplate().load(MeioTransporte.class, id);
		mt.getMeioTranspConteudoAtribs().clear();
		mt.getFotoMeioTransportes().clear();

		mt.getMeioTransportePerifericos().clear();
		getMeioTransporteDAO().getAdsmHibernateTemplate().removeById("delete MeioTransportePeriferico where meioTransporte = :id", mt);

		meioTransporteRodoviarioService.removeById(id);
		getMeioTransporteDAO().getAdsmHibernateTemplate().delete(mt);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids)
			removeById(id);
	}

	public Long getRowCountByType(String type, Long idFilial) {
		return getMeioTransporteDAO().getRowCountByType(type, idFilial);
	}

	/**
	 * Valida se meio de transporte eventual ou agregado posssui solicitação de contratação aprovada.
	 * Se possuir, APÒS o store será realizado update.
	 * @param m
	 * @return List de solicitacoes
	 */
	private List<SolicitacaoContratacao> validateSolicitacoesOnStore(MeioTransporte meioTransporte) {
		//Mudanca CQPRO00007420
		if(SessionUtils.isIntegrationRunning()) {
			return Collections.EMPTY_LIST;
		}
		String tpVinculo = meioTransporte.getTpVinculo().getValue();
		List<SolicitacaoContratacao> solicitacoes = new ArrayList<SolicitacaoContratacao>();

		if ("E".equals(tpVinculo) || SITUACAO_APROVADA.equals(tpVinculo)) {
			Long idMeioTransporte = meioTransporte.getIdMeioTransporte();
			boolean isInclusao = idMeioTransporte == null;
			String nrIdentificador = meioTransporte.getNrIdentificador();
			Long idTipoMeioTransporte = meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte();
			Long idFilialAgregado = null;
			Filial filialAgregadoCe = meioTransporte.getFilialAgregadoCe();
			if(filialAgregadoCe != null) {
				idFilialAgregado = filialAgregadoCe.getIdFilial();
			}

			solicitacoes = findSolicitacoesAprovadas(
				nrIdentificador, 
				idTipoMeioTransporte,
				tpVinculo,
				SITUACAO_APROVADA.equals(tpVinculo) && !isInclusao ? null : idFilialAgregado
			);
			//Regra 2 retirada na LMS-6407

			//Regra 13 da especificação, se inclusão, e tpVinculo = agregado
			if(SITUACAO_APROVADA.equals(tpVinculo)) {
				if(isInclusao) {
					if(solicitacoes.size() > 1) {
						throw new BusinessException("LMS-26083");
					}
				} else {
					// CQPRO00024043
					for (SolicitacaoContratacao solicitacaoContratacao : solicitacoes) {
						if(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue().equals("C")
								&& idFilialAgregado == null) {
						throw new BusinessException("LMS-26084");
					}
				}
			}
		}
		}
		
		TipoMeioTransporte tipoMeioTransporte = tipoMeioTransporteService.findById(meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte());
		BigDecimal nrCapacidadeMeioTransporte = meioTransporte.getNrCapacidadeKg();
		
		Integer nrCapacidadePesoInicial = tipoMeioTransporte.getNrCapacidadePesoInicial();
		Integer nrCapacidadePesoFinal = tipoMeioTransporte.getNrCapacidadePesoFinal();
		
		BigDecimal inicialAceita = new BigDecimal(nrCapacidadePesoInicial * 0.9);
		BigDecimal finalAceita = new BigDecimal(nrCapacidadePesoFinal * 1.1);
		
		if(nrCapacidadeMeioTransporte.compareTo(inicialAceita) == -1 || nrCapacidadeMeioTransporte.compareTo(finalAceita) == 1) {
			throw new BusinessException("LMS-26113", new Object[]{nrCapacidadePesoInicial, nrCapacidadePesoFinal});
		}
		
		return solicitacoes;
	}

	private List<SolicitacaoContratacao> findSolicitacoesAprovadas(
			String nrIdentificador,
			Long idTipoMeioTransporte,
			String tpVinculo,
			Long idFilialAgregado
	) {
		return solicitacaoContratacaoService.validadeMeioTransporteContratacaoAprovada(nrIdentificador, 
			idTipoMeioTransporte,
			tpVinculo, 
			idFilialAgregado
		);
	}
	
	
	public java.io.Serializable store(MeioTransporte bean) {
		MeioTransporte meioTransporte = (MeioTransporte) bean;
		
		boolean isCadastroNovo = meioTransporte.getIdMeioTransporte() == null;
		
		MeioTransporteRodoviario meioTransporteRodoviario = meioTransporte.getMeioTransporteRodoviario();
		Filial filialAgregadoCe = meioTransporte.getFilialAgregadoCe();
		String tpVinculo = meioTransporte.getTpVinculo().getValue();
		
		String tpMeioTransporte = null;
		if(meioTransporte.getModeloMeioTransporte().getTpMeioTransporte() != null) {
			tpMeioTransporte = meioTransporte.getModeloMeioTransporte().getTpMeioTransporte().getValue();
		} else {
			tpMeioTransporte = meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte().getTpMeioTransporte().getValue();
		}
		
		//CQPRO00026680 - Inicio
				
		setTpSituacao(meioTransporte, TIPO_VINCULO_PROPRIO.equals(tpVinculo));		
		
		if ("E".equals(tpVinculo) || SITUACAO_APROVADA.equals(tpVinculo)) {
			if(bean.getIdMeioTransporte() != null){
				Long idTipoMeioTransporteOld =  tipoMeioTransporteService.findTipoMeioTransporteByIdMeioTransporte(bean.getIdMeioTransporte()).getIdTipoMeioTransporte();
				Long idTipoMeioTransporteNew = meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte();
				if(idTipoMeioTransporteOld != null){
					Integer qtEixosOld = eixosTipoMeioTransporteService.findByIdMeioTransporte(meioTransporteRodoviario.getIdMeioTransporte() ).getQtEixos();
					
					Integer qtEixosNew = eixosTipoMeioTransporteService.findById(meioTransporteRodoviario.getEixosTipoMeioTransporte().getIdEixosTipoMeioTransporte()).getQtEixos();
					
					if(!idTipoMeioTransporteOld.equals(idTipoMeioTransporteNew) || qtEixosOld.compareTo(qtEixosNew)!=0 ){
						String nrIdentificador = meioTransporte.getNrIdentificador();
						List<Integer> eixosAvaibleNew = eixosTipoMeioTransporteService.findSumEixosByTpMeioTransp(idTipoMeioTransporteNew);
						List<Integer> eixosAvaibleOld = eixosTipoMeioTransporteService.findSumEixosByTpMeioTransp(idTipoMeioTransporteOld);
						
						List<SolicitacaoContratacao> solicitacoes = findSolicitacoes(
								nrIdentificador, 
								idTipoMeioTransporteOld);
						for (SolicitacaoContratacao solicitacaoContratacao : solicitacoes) {
							solicitacaoContratacao.setTipoMeioTransporte(meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte());
							
							if(!eixosAvaibleNew.contains(eixosAvaibleOld));{
								if(eixosAvaibleNew.contains(qtEixosNew)){
									solicitacaoContratacao.setQtEixos(qtEixosNew);
								} else {
									solicitacaoContratacao.setQtEixos(eixosAvaibleNew.get(0));
								}
							}
							
						}
						solicitacaoContratacaoService.storeAll(solicitacoes);
					}
				}
			}
		}
		//CQPRO00026680 - Fim
		
		
		List<SolicitacaoContratacao> solicitacoes = validateSolicitacoesOnStore(meioTransporte);

		//Seta o número de frota caso tipo de vínculo for eventual ou agregado.
		generateProximoNrFrota(meioTransporte);
		
		if(meioTransporte.getNrCodigoBarra() == null && meioTransporte.getTpVinculo().getValue().equals("E")) {
			//Seta o número do codigo de barras caso tipo de vínculo for eventual.
			meioTransporte.setNrCodigoBarra(generateNrBarcodeEventual());
		}
		
				
		
		validaNrCodigoBarras(meioTransporte.getNrCodigoBarra(), meioTransporte.getIdMeioTransporte()); 
		
		if (meioTransporte.getIdMeioTransporte() != null) {
			// edição
			meioTranspConteudoAtribService.removeByMeioTransporte(meioTransporte.getIdMeioTransporte());
		}

		if("R".equals(tpMeioTransporte)) {

			//Valida se a data Emissao é maior que a data atual 
			YearMonthDay dtEmissao = meioTransporteRodoviario.getDtEmissao();
			if (dtEmissao != null && JTDateTimeUtils.comparaData(dtEmissao, JTDateTimeUtils.getDataAtual()) > 0) {
				throw new BusinessException("LMS-26025");
			}

			//valida se a data Vencimento Seguro é menor que a data atual
			if(!SessionUtils.isIntegrationRunning()) {
				YearMonthDay dtVencimentoSeguro = meioTransporteRodoviario.getDtVencimentoSeguro();
				if (dtVencimentoSeguro != null && JTDateTimeUtils.comparaData(dtVencimentoSeguro,JTDateTimeUtils.getDataAtual()) < 0) {
					throw new BusinessException("LMS-26024");
				}
			}
			// Valida se meio de transporte possui proprietário e liberação.
			if (validateSituacaoMeioTransp(meioTransporte.getIdMeioTransporte(), true)) {
				meioTransporte.setTpSituacao(new DomainValue("N"));
				new WarningCollector(configuracoesFacade.getMensagem("LMS-26010"));
			}

			//Regras para tipo vínculo agregado
			if(!solicitacoes.isEmpty()) {
				if (SITUACAO_APROVADA.equals(tpVinculo)) {
					// É garantido que um meio de transporte do tipo Agregado tenha UMA solicitação aprovada.
					SolicitacaoContratacao solicitacaoContratacao = solicitacoes.get(0);
					if (meioTransporte.getIdMeioTransporte() == null) {
						//Inclusão
						Filial filialSolicitacao = solicitacaoContratacao.getFilial();
						meioTransporte.setFilialAgregadoCe(filialSolicitacao);
						filialAgregadoCe = filialSolicitacao;
					} else {
						//Alteração
						//Se a solicitação de contratação for coleta entrega, é obrigatório informar a filial agregado.
						if ("C".equals(solicitacaoContratacao.getTpSolicitacaoContratacao().getValue())) {
							if (filialAgregadoCe == null) {
								throw new BusinessException("LMS-26084");
							}
						}
					}
				}
			}

			if (filialAgregadoCe != null) {
				if (historicoFilialService.validateFilialUsuarioMatriz(filialAgregadoCe.getIdFilial())) {
					throw new BusinessException("LMS-26065");
				}
			}

			if("P".equals(tpVinculo)) {
				if (!SessionUtils.isFilialSessaoMatriz()) {
					throw new BusinessException("LMS-26079");
				}
			}

			meioTransporteRodoviarioService.store(meioTransporteRodoviario);
			getMeioTransporteDAO().storeMeioTransporte(meioTransporte);
			
			

			getMeioTransporteDAO().getAdsmHibernateTemplate().flush();

			if (!solicitacoes.isEmpty()) {
				solicitacaoContratacaoService.updateMeioTransporteContratacaoAprovada(meioTransporte.getIdMeioTransporte(), solicitacoes);
			}
		} else {
			//Valida se meio de transporte possui proprietário.
			if (validateSituacaoMeioTransp(meioTransporte.getIdMeioTransporte(), false)) {
				meioTransporte.setTpSituacao(new DomainValue("N"));
				new WarningCollector(configuracoesFacade.getMensagem("LMS-26010"));
			}
			getMeioTransporteDAO().storeMeioTransporte(meioTransporte);
		}
		
		Serializable store = super.store(bean);
				
		executeWorkflow(bean,isCadastroNovo);
		
		return store;
	}
	
	/**
	 * Executa ação de workflow caso seja necessário.
	 * 
	 * @param meioTransporte
	 * @param retorno
	 */
	private void executeWorkflow(MeioTransporte meioTransporte,boolean isCadastroNovo) {
		if(!isGenerateWorflow(TIPO_VINCULO_PROPRIO.equals(meioTransporte.getTpVinculo().getValue()))){
			return;						
		}
		
		executeWorkflowMeioTransporteService.executeWorkflowPendencia(meioTransporte,isCadastroNovo);		
	}
	private boolean isGenerateWorflow(boolean isProprio) {
		if(SessionUtils.isFilialSessaoMatriz() || isProprio ){
			return false;
		}
		
		return true;
	}

	private List<SolicitacaoContratacao> findSolicitacoes(
			String nrIdentificador, Long idTipoMeioTransporte) {
		return solicitacaoContratacaoService.findSolicitacoesByMeioTransporte(nrIdentificador, idTipoMeioTransporte);
	}


	/**
	 * Valida o valor informado para o campo código de barras.<br />
	 * Regras:
	 * 	Validar que o tamanho seja 12 dígitos.
	 * 	Validar que o código inicie com o número 11.
	 * 	Validar (garantir) que não exista outro Meio de Transporte com o mesmo código de barras no momento de salvar a informação no banco.
	 * 	Caso alguma regra acima não tenha sido satisfatória, entao apresentar mensagem LMS-26106 – Código de Barras inválido.
	 */
	private void validaNrCodigoBarras(final Long nrCodigoBarra, final Long idMeioTransporte) throws BusinessException {
		if(nrCodigoBarra != null){
			if( (nrCodigoBarra.toString().length() < 12) || 
					(!nrCodigoBarra.toString().startsWith("11") && !nrCodigoBarra.toString().startsWith("12"))){
				throw new BusinessException("LMS-26106");
			}
			
			if(!getMeioTransporteDAO().findMeioTransporteByCodigoBarra(nrCodigoBarra,idMeioTransporte).isEmpty()){
				throw new BusinessException("LMS-26106");
			}
		}
	}

	/**
	 * Valida Proprietário e Reguladora.
	 * @param idMeioTransporte
	 * @param rodoviario
	 * @return
	 */
	private boolean validateSituacaoMeioTransp(Long idMeioTransporte,boolean rodoviario) {
		if (idMeioTransporte == null || !verificaSituacaoMeioTransporte(idMeioTransporte, rodoviario))
			return true;
		else
			return false;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMeioTransporteDAO(MeioTransporteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MeioTransporteDAO getMeioTransporteDAO() {
		return (MeioTransporteDAO) getDao();
	}

	/**
	 * Gera o próximo número da frota caso tipo de vínculo for eventual ou agregado.
	 *
	 * @param meioTransporte
	 */
	public void generateProximoNrFrota(MeioTransporte meioTransporte) {
		String tpVinculo = meioTransporte.getTpVinculo().getValue();
		String nrFrota = meioTransporte.getNrFrota();
		if (StringUtils.isBlank(meioTransporte.getNrFrota())
				&& (SITUACAO_APROVADA.equals(tpVinculo) || "E".equals(tpVinculo))) {
			Long nrProximoFrota = configuracoesFacade.incrementaParametroSequencial("NR_FROTA_TERCEIRO", true);
			nrFrota = "T" + FormatUtils.fillNumberWithZero(nrProximoFrota.toString(), 5); 
		} else {
			nrFrota = FormatUtils.fillNumberWithZero(nrFrota, 6);
		}
		meioTransporte.setNrFrota(nrFrota);
	}

	/**
	 * Valida placa de veículo e gera excessão se a placa não respeitar padrões:
	 * BRA, CHI, URU: AAA9999
	 * ARG: AAA999
	 * Gera BusinessException se a placa não obedecer os padrões. 
	 * 
	 * autor Felipe Ferreira
	 * @param pais
	 * @param placa
	 * 
	 */
	public void validaIdentificadorMeioTransporte(String pais, String placa) {
		CharSequence sequence = placa;
		String pattern = "";

		if (pais.toUpperCase().equals("BRA")
				|| pais.toUpperCase().equals("CHI")
				|| pais.toUpperCase().equals("URU"))
			pattern = "\\D\\D\\D\\d\\d\\d\\d";
		else if (pais.toUpperCase().equals("ARG"))
			pattern = "\\D\\D\\D\\d\\d\\d";

		if (!pattern.equals("") && !Pattern.matches(pattern, sequence))
			throw new BusinessException("LMS-26011");

	}

	/**
	 * Retorna Lista com os componentes e valores do modelo de meio de transporte indicado.
	 * 
	 * @param map
	 * @return
	 */
	public List<ModeloMeioTranspAtributo> findAtributosByModelo(TypedFlatMap map) {
		List<ModeloMeioTranspAtributo> atributos = null;
		Long idModelo = map.getLong("idModelo");
		Long idMeioTransporte = map.getLong("idMeioTransporte");
		if (idModelo != null) {
			atributos = modeloMeioTranspAtributoService.findAtributosByModelo(idModelo);
		}

		if(atributos != null) {
			for (ModeloMeioTranspAtributo modeloMeioTranspAtributo : atributos) {
				modeloMeioTranspAtributo.setConteudoAtributoModelos(conteudoAtributoModeloService.findConteudoByAtributo(modeloMeioTranspAtributo.getIdModeloMeioTranspAtributo()));

				List<MeioTranspConteudoAtrib> atribs = null;
				if (idMeioTransporte != null) {
					atribs = meioTranspConteudoAtribService.findConteudoByMeioAndModelo(
							modeloMeioTranspAtributo.getIdModeloMeioTranspAtributo(), idMeioTransporte);
				}
				modeloMeioTranspAtributo.setMeioTranspConteudoAtribs(atribs);
			}
		}
		return atributos;
	}

	/**
	 * Verifica se meio de transporte possui estado incompleto e liberação cadastrada e vigente.
	 * 
	 * @param idMeioTransporte
	 * @param rodoviario
	 * @return true se possuir
	 */
	public boolean verificaSituacaoMeioTransporteForProprietarios(Long idMeioTransporte) {
		return getMeioTransporteDAO().verificaSituacaoMeioTransporteForProprietarios(idMeioTransporte);
	}

	/**
	 * Seta Meio de Transporte para Ativo.
	 * 
	 * @param idMeioTransporte
	 */
	public void updateSituacaoParaAtivo(Long idMeioTransporte) {
		MeioTransporte meioTransporte = findByIdInitLazyProperties(idMeioTransporte, false);
		meioTransporte.setTpSituacao(new DomainValue(SITUACAO_APROVADA));
		store(meioTransporte);
	}

	public boolean verificaSituacaoMeioTransporte(Long idMeioTransporte, boolean rodoviario) {
		return getMeioTransporteDAO().verificaSituacaoMeioTransporte(idMeioTransporte, rodoviario);
	}

	public MeioTransporte findMeioTransporteByIdentificacao(String nrIdentificacao) {
		return getMeioTransporteDAO().findMeioTransporteByIdentificacao(nrIdentificacao);
	}

	public TypedFlatMap findInfoMeioTransporte(Long idMeioTransporte) {
		List<Map<String, Object>> l = getMeioTransporteDAO().findInfoMeioTransporte(idMeioTransporte);
		if (!l.isEmpty()) {
			Map<String, Object> map = l.get(0);
			TypedFlatMap retorno = new TypedFlatMap();

			MeioTransporte mt = (MeioTransporte) map.get("meioTransporte");
			retorno.put("meioTransporte.idMeioTransporte", mt.getIdMeioTransporte());
			retorno.put("meioTransporte.nrFrota", mt.getNrFrota());
			retorno.put("meioTransporte.nrIdentificador", mt.getNrIdentificador());
			retorno.put("meioTransporte.nrAnoFabricao", mt.getNrAnoFabricao());
			retorno.put("meioTransporte.nrCapacidadeKg", mt.getNrCapacidadeKg());
			retorno.put("meioTransporte.nrCapacidadeM3", mt.getNrCapacidadeM3());
			retorno.put("meioTransporte.qtPortas", mt.getQtPortas());
			retorno.put("meioTransporte.tpStatus", mt.getTpStatus());
			retorno.put("meioTransporte.meioTransporteRodoviario.nrRastreador",map.get("nrRastreador"));

			DomainValue tpModal = mt.getTpModal();
			if (tpModal != null) {
				retorno.put("meioTransporte.tpModal.value", tpModal.getValue());
				retorno.put("meioTransporte.tpModal.description", tpModal.getDescription().getValue());
			}

			DomainValue tpSituacao = mt.getTpSituacao();
			if (tpModal != null) {
				retorno.put("meioTransporte.tpSituacao.value", tpSituacao.getValue());
				retorno.put("meioTransporte.tpSituacao.description", tpSituacao.getDescription().getValue());
			}

			DomainValue tpVinculo = mt.getTpVinculo();
			if (tpModal != null) {
				retorno.put("meioTransporte.tpVinculo.value", tpVinculo.getValue());
				retorno.put("meioTransporte.tpVinculo.description", tpVinculo.getDescription().getValue());
			}

			//Coloca todo conteúdo do map no retorno, MAS REMOVE O MEIO DE TRANSPORTE!
			map.remove("meioTransporte");
			retorno.putAll(map);

			return retorno;
		} else
			return null;
	}

	/**
	 * Retorna filial do meio de transporte informado.
	 * @param idMeioTransporte
	 * @return
	 */
	public Long findFilialByMeioTransporte(Long idMeioTransporte) {
		return getMeioTransporteDAO().findFilialByMeioTransporte(idMeioTransporte);
	}

	/**
	 * Verifica se o meio de transporte está ativo.
	 * 
	 * @param map
	 * @return TRUE se o meioTransporte informado está ativo, FALSE caso contrário
	 */
	public Boolean validateMeioTransporteAtivo(Long idMeioTransporte) {
		return getMeioTransporteDAO().validateMeioTransporteAtivo(idMeioTransporte);
	}

	public List findInfoMeioTransporteSolicitacaoByNrPlaca(String nrIdentificador) {
		return getMeioTransporteDAO().findInfoMeioTransporteSolicitacaoByNrPlaca(nrIdentificador);
	}

	public String findNrFrotaByNrIdentificacao(String nrIdentificacao) {
		return getMeioTransporteDAO().findNrFrotaByNrIdentificacao(nrIdentificacao);
	}

	/**
	 * findPaginated utilizado na tela VOL/Métrica/Totais por frota.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedTotaisPorFrota(TypedFlatMap criteria) {
		FindDefinition fd = FindDefinition.createFindDefinition(criteria);
		return getMeioTransporteDAO().findPaginatedTotaisPorFrota(criteria, fd);
	}

	/**
	 * getRowCount utilizado na tela VOL/Métrica/Totais por frota.
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountTotaisPorFrota(TypedFlatMap criteria) {
		return getMeioTransporteDAO().getRowCountTotaisPorFrota(criteria);
	}

	/**
	 * Verifica se o meio de tranporte recebido por parâmetro é um Veículo. Se não for, será
	 * lançada uma exceção.
	 * 
	 * @param idMeioTransporte
	 */
	public void validateMeioTransporteIsVeiculo(Long idMeioTransporte) {
		if (getMeioTransporteDAO().findMeioTransporteIsSemiReboque(idMeioTransporte))
			throw new BusinessException("LMS-26086");
	}

	/**
	 * Verifica se o meio de tranporte recebido por parâmetro é um semi-reboque. Se não for, será
	 * lançada uma exceção.
	 * 
	 * @param idMeioTransporte
	 */
	public void validateMeioTransporteIsSemiReboque(Long idMeioTransporte) {
		if (!getMeioTransporteDAO().findMeioTransporteIsSemiReboque(idMeioTransporte))
			throw new BusinessException("LMS-26087");
	}

	/**
	 * Verifica se o meio de tranporte recebido por parâmetro é um semi-reboque. 
	 * 
	 * @param idMeioTransporte
	 * @return
	 */
	public boolean findMeioTransporteIsSemiReboque(Long idMeioTransporte) {
		return getMeioTransporteDAO().findMeioTransporteIsSemiReboque(idMeioTransporte);			
	}
	
	/**
	 * Verifica se o meio de tranporte recebido por parâmetro é um cavalo-trator. 
	 * 
	 * @param idMeioTransporte
	 * @return
	 */
	public boolean findMeioTransporteIsCavaloTrator(Long idMeioTransporte) {
		return getMeioTransporteDAO().findMeioTransporteIsCavaloTrator(idMeioTransporte);			
	}
	
	public void findMeioTransporteIsCT(Long idMeioTransporte) {
		boolean isCavaloTrator = getMeioTransporteDAO().findMeioTransporteIsCavaloTrator(idMeioTransporte);
		if(!isCavaloTrator){
			throw new BusinessException("LMS-26003");
		}
	}
	
	/**
	 * Verifica na tabela F1201 se a frota passada como parametro existe 
	 * @param nrFrota
	 * @return
	 */
	public List findValidFrota(String nrFrota) {		
		return getMeioTransporteDAO().findValidFrota(nrFrota);
	}	

	/**
	 * Atualiza o campo FAUNIT da tabela F1201
	 * @param nrFrota
	 * @param idcliente
	 */
	public void updateFAUNIT(String nrFrota, String idcliente) {
		if(idcliente == null){
			idcliente = StringUtils.rightPad("", 8, ' ');
		}
		gerarDadosFrotaDAO.updateFAUNIT(nrFrota,idcliente);
	}	
	
	
	/**
	 * Gera o próximo número do codigo de barras caso tipo de vínculo for eventual.
	 *
	 * @param meioTransporte
	 */
	public Long generateNrBarcodeEventual() {				
		String nrPrefixo = configuracoesFacade.getValorParametro("NR_ID_CODBAR_MEIO_TRANSPORTE_EV").toString();
		String nrProximoFrota = configuracoesFacade.incrementaParametroSequencial("NR_BARCODE_MT_EV", true).toString();
		return Long.valueOf(nrPrefixo.concat(FormatUtils.fillNumberWithZero(nrProximoFrota, 10))); 
	}
	
	public BigDecimal findCapacidadeMedia(MeioTransporte transporte, BigDecimal franquia) {
	    BigDecimal capacidadeInicial = BigDecimal.valueOf(
	            transporte.getModeloMeioTransporte().getTipoMeioTransporte().getNrCapacidadePesoInicial());
        BigDecimal capacidadeFinal = BigDecimal.valueOf(
                transporte.getModeloMeioTransporte().getTipoMeioTransporte().getNrCapacidadePesoFinal());
	
        return capacidadeInicial.add(capacidadeFinal).divide(TWO).multiply(franquia.divide(PERCENTUAL));
	}
	
	public java.io.Serializable storeWorkFlow(MeioTransporte bean) {
		return super.store(bean);
	}
	
	public ResultSetPage<AnexoMeioTransporte> findPaginatedAnexoMeioTransporte(PaginatedQuery paginatedQuery) {
		return getMeioTransporteDAO().findPaginatedAnexoMeioTransporte(paginatedQuery);
	}
	
	public Integer getRowCountAnexoMeioTransporte(TypedFlatMap criteria) {
		return getMeioTransporteDAO().getRowCountAnexoMeioTransporte(criteria);
	}
	
	/**
	 * Executa o store de um anexo no banco, como também atualiza a entidade do
	 * meioTransporte com a data e usuário da alteração.
	 * 
	 * @param map
	 * 
	 * @return Serializable
	 */
	public Serializable storeAnexoMeioTransporte(TypedFlatMap map){
		Long idMeioTransporte = map.getLong("idMeioTransporte");
				
		/*
		 * Atualiza alteração do registro.
		 */
		getMeioTransporteDAO().updateMeioTransporteAlteracao(idMeioTransporte, SessionUtils.getUsuarioLogado().getIdUsuario());
		
		/*
		 * Grava anexo.
		 */
		MeioTransporte meioTransporte = findByIdCustom(idMeioTransporte);
		
    	storeAnexo(map, meioTransporte);    	
		
    	/*
    	 * Executa workflow se necessário.
    	 */
    	executeWorkflow(meioTransporte,false);
    	
    	/*
    	 * Retorna os dados atualizados para a tela. 
    	 */
    	getMeioTransporteDAO().getAdsmHibernateTemplate().flush();
    	    	
    	return meioTransporte;
	}

	/**
	 * Persiste um anexo.
	 * 
	 * @param map
	 * @param motorista
	 */
	private void storeAnexo(TypedFlatMap map, MeioTransporte meioTransporte) {
		AnexoMeioTransporte anexoMeioTransporte = new AnexoMeioTransporte();
		anexoMeioTransporte.setIdAnexoMeioTransporte(map.getLong("idAnexoMeioTransporte"));
    	anexoMeioTransporte.setMeioTransporte(meioTransporte);
    	
    	UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
    	anexoMeioTransporte.setUsuario(usuarioLMS);
    	    	
    	try{
    		anexoMeioTransporte.setDcArquivo(Base64Util.decode(map.getString("dcArquivo")));
    	} catch (IOException e) {
			throw new InfrastructureException(e);
		}
    	
    	anexoMeioTransporte.setDsAnexo(map.getString("dsAnexo"));    	
    	anexoMeioTransporte.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
    	
    	getMeioTransporteDAO().store(anexoMeioTransporte);
	}
	
	public AnexoMeioTransporte findAnexoMeioTransporteById(Long idAnexoMeioTransporte) {
		AnexoMeioTransporte anexoMeioTransporte = getMeioTransporteDAO().findAnexoMeioTransporteById(idAnexoMeioTransporte);
		if(anexoMeioTransporte != null){
			Hibernate.initialize(anexoMeioTransporte);
		}
		return anexoMeioTransporte;
	}
	
	/**
	 * 
	 * @param meioTransporte
	 * @param isProprio
	 * @param retorno
	 */
	private void setTpSituacao(MeioTransporte meioTransporte, boolean isProprio) {		
		if(meioTransporte.getIdMeioTransporte() == null){
			if(SessionUtils.isFilialSessaoMatriz() || isProprio){
				meioTransporte.setTpSituacao(new DomainValue(STATUS_ATIVO));
			} else {
				meioTransporte.setTpSituacao(new DomainValue(STATUS_INATIVO));
			}	
		}
	}
	
	/**
	 * Executa a remoção de um anexo no banco, como também atualiza a entidade do
	 * meioTransporte com a data e usuário da alteração.
	 * 
	 * @param map
	 * 
	 * @return Serializable
	 */
	public Serializable removeByIdsAnexoMeioTransporte(TypedFlatMap map) {
		Long idMeioTransporte = map.getLong("idMeioTransporte");
		
		/*
		 * Remove anexo(s).
		 */
		getMeioTransporteDAO().removeByIdsAnexoMeioTransporte(getAnexoIds(map));		
		
		/*
		 * Atualiza alteração do registro.
		 */		
		getMeioTransporteDAO().updateMeioTransporteAlteracao(idMeioTransporte, SessionUtils.getUsuarioLogado().getIdUsuario());
		
		/*
		 * Executa workflow se necessário.
		 */
		MeioTransporte meioTransporte = findByIdCustom(idMeioTransporte);
				
		executeWorkflow(meioTransporte,false);
		
		getMeioTransporteDAO().getAdsmHibernateTemplate().flush();
		
		return meioTransporte;
	}

	/**
	 * Converte uma lista de ids de remoção de String para Long.
	 * 
	 * @param map
	 * @return List<Long>
	 */
	@SuppressWarnings("unchecked")
	private List<Long> getAnexoIds(TypedFlatMap map) {
		List<Long> listaIds = new ArrayList<Long>();						
		List<String> list = map.getList("ids");
		
		for (String id : list) {
			listaIds.add(Long.parseLong(id));
		}
		
		return listaIds;
	}
	
	
	/**
     * @param meioTransporte
     * @param retorno
     */
    public TypedFlatMap findDadosWorkflow(MeioTransporte meioTransporte) {
        TypedFlatMap map = new TypedFlatMap();
        boolean isCadDisabled = false;

        if(meioTransporte.getPendencia() != null){
            Pendencia pendencia = pendenciaService.findById(meioTransporte.getPendencia().getIdPendencia());
            boolean emAprovacao = ConstantesWorkflow.EM_APROVACAO.equals(pendencia.getTpSituacaoPendencia().getValue());
            map.put("dsPendencia", pendencia.getTpSituacaoPendencia().getDescriptionAsString());
            map.put("tpSituacaoPendencia", pendencia.getTpSituacaoPendencia().getValue());
            
            Short nrTipoEvento = pendencia.getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
            
            if(isCadastroNovo(nrTipoEvento) && emAprovacao){
                isCadDisabled = true;
            }
        }       
        
        map.put("desabilitaCad", isCadDisabled);
        return map;
    }
	
    private boolean isCadastroNovo(Short nrTipoEvento) {
        return ConstantesWorkflow.NR2612_APROVACAO_CADASTRO_MEIO_TRANSPORTE_CE.equals(nrTipoEvento) || ConstantesWorkflow.NR2615_APROVACAO_CADASTRO_MEIO_TRANSPORTE_VI.equals(nrTipoEvento);
    }
    
	public void setMeioTransporteRodoviarioService(MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}
	public void setModeloMeioTranspAtributoService(ModeloMeioTranspAtributoService modeloMeioTranspAtributoService) {
		this.modeloMeioTranspAtributoService = modeloMeioTranspAtributoService;
	}
	public void setConteudoAtributoModeloService(ConteudoAtributoModeloService conteudoAtributoModeloService) {
		this.conteudoAtributoModeloService = conteudoAtributoModeloService;
	}
	public void setMeioTranspConteudoAtribService(MeioTranspConteudoAtribService meioTranspConteudoAtribService) {
		this.meioTranspConteudoAtribService = meioTranspConteudoAtribService;
	}
	public void setBloqueioMotoristaPropService(BloqueioMotoristaPropService bloqueioMotoristaPropService) {
		this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	}
	public void setSolicitacaoContratacaoService(SolicitacaoContratacaoService solicitacaoContratacaoService) {
		this.solicitacaoContratacaoService = solicitacaoContratacaoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}

	public GerarDadosFrotaDAO getGerarDadosFrotaDAO() {
		return gerarDadosFrotaDAO;
	}

	public void setGerarDadosFrotaDAO(GerarDadosFrotaDAO gerarDadosFrotaDAO) {
		this.gerarDadosFrotaDAO = gerarDadosFrotaDAO;
	}


	public void setTipoMeioTransporteService(TipoMeioTransporteService tipoMeioTransporteService) {
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}


	public EixosTipoMeioTransporteService getEixosTipoMeioTransporteService() {
		return eixosTipoMeioTransporteService;
	}


	public void setEixosTipoMeioTransporteService(
			EixosTipoMeioTransporteService eixosTipoMeioTransporteService) {
		this.eixosTipoMeioTransporteService = eixosTipoMeioTransporteService;
	}

	public List<Map<String, Object>> findMeioTransporteSuggest(Long idProprietario, String nrIdentificador, String nrFrota, Integer limiteRegistros) {
		return getMeioTransporteDAO().findMeioTransporteSuggest(idProprietario, nrIdentificador, nrFrota, limiteRegistros);		
	}
	
	public void setExecuteWorkflowMeioTransporteService(
			ExecuteWorkflowMeioTransporteService executeWorkflowMeioTransporteService) {
		this.executeWorkflowMeioTransporteService = executeWorkflowMeioTransporteService;
	}
	
	/**
	 * Localiza meio de transporte Aprovado workflow, liberado de bloqueio e ativo;
	 * @param nrIdentificador
	 * @return
	 */
	public MeioTransporte findMeioTransporteByIdentificacaoAprovado(String nrIdentificador) {
		MeioTransporte meio = findMeioTransporteByIdentificacao(nrIdentificador);
		
		hasMeioTransporteAtivo(meio);
		hasLiberacao(meio);
		hasProprietario(meio);
		hasWorkflowAprovado(meio);	
		
		return meio;					
	}
	
	
	private void hasProprietario(MeioTransporte meio) {
		MeioTranspProprietario meioTranspProprietario =  findProprietario(meio.getIdMeioTransporte());
		if(meioTranspProprietario != null && meioTranspProprietario.getIdMeioTranspProprietario() != null){
			return;
		}
		throw new BusinessException(LMS_26155);
		
		
	}
	private void hasMeioTransporteAtivo(MeioTransporte meio) {
		if(meio != null && meio.getIdMeioTransporte() != null && STATUS_ATIVO.equals(meio.getTpSituacao().getValue())){
			return;
		}
		throw new BusinessException(LMS_26153);
	}
	
	private void hasLiberacao(MeioTransporte meio) {
		@SuppressWarnings("rawtypes")
		List bloqueios = bloqueioMotoristaPropService.findBloqueiosVigentesByMeioTransporte(meio.getIdMeioTransporte());
		if( bloqueios == null || bloqueios.isEmpty()){
			return;
		}
		throw new BusinessException(LMS_26154);
	}
	
	private void hasWorkflowAprovado(MeioTransporte meio) {
		if(meio.getTpSituacaoWorkflow()  == null || (meio.getTpSituacaoWorkflow() != null && SITUACAO_APROVADA.equals(meio.getTpSituacaoWorkflow().getValue()))){
			return;
		}
		throw new BusinessException(LMS_26156);	
	}
	public MeioTranspProprietario findProprietario(Long idMeioTransporte) {
		return getMeioTransporteDAO().findProprietario(idMeioTransporte);
	}
	public List<Map<String, Object>> findMeioTransporteByProprietarioAndFilial(Long idProprietario, Long idFilial) {
		return getMeioTransporteDAO().findMeioTransporteByProprietarioAndFilial(idProprietario, idFilial);
	}
    public void setPendenciaService(PendenciaService pendenciaService) {
        this.pendenciaService = pendenciaService;
    }
		

}