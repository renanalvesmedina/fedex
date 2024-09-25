package com.mercurio.lms.portaria.model.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.portaria.model.OrdemSaida;
import com.mercurio.lms.portaria.model.dao.ControleQuilometragemDAO;
import com.mercurio.lms.portaria.model.service.utils.ControleQuilometragemHelper;
import com.mercurio.lms.util.CompareUtils;
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
 * @spring.bean id="lms.portaria.controleQuilometragemService"
 */
public class ControleQuilometragemService extends CrudService<ControleQuilometragem, Long> {
	private static final String CAVALO_TRATOR = "Cavalo-Trator";
	private FilialService filialService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private ControleCargaService controleCargaService;
	private RecursoMensagemService recursoMensagemService;
	private MeioTransporteService meioTransporteService;
	private ManifestoService manifestoService;

	/**
	 * Recupera uma instância de <code>ControleQuilometragem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public ControleQuilometragem findById(java.lang.Long id) {
		return (ControleQuilometragem)super.findById(id);
	}

	/**
	 * Retorna para a tela 'Manter Quilometragens de Saída e/ou Chegada' somente campos necessários.
	 * @param id
	 * @return
	 */
	public TypedFlatMap findByIdTela(java.lang.Long id) {
		Map m = getControleQuilometragemDAO().findByIdTela(id);

		TypedFlatMap retorno = new TypedFlatMap();

		Set chaves = m.keySet();
		for (Iterator i = chaves.iterator() ; i.hasNext() ; ) {
			String key = (String)i.next();
			if ((key.endsWith("_tpVinculo") || key.endsWith("_tpSituacaoPendencia")) && m.get(key) != null) {
				DomainValue tpVinculo = (DomainValue)m.get(key);
				retorno.put(key.replace('_','.'),tpVinculo.getValue());
			} else
				retorno.put(key.replace('_','.'),m.get(key));
		}

		return retorno;
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

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ControleQuilometragem bean) {
		return super.store(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @param usuarioLogado
	 * @return entidade que foi armazenada.
	 */
	public TypedFlatMap storeCustom(TypedFlatMap map) {
		ControleQuilometragem bean = new ControleQuilometragem();
		if (map.getLong("idControleQuilometragem") != null) {
			bean = findById(map.getLong("idControleQuilometragem"));
		}

		Filial filial = new Filial();
		filial.setIdFilial(map.getLong("filial.idFilial"));

		ControleCarga controleCarga = null;
		if (map.getLong("controleCarga.idControleCarga") != null) {
			controleCarga = new ControleCarga();
			controleCarga.setIdControleCarga(map.getLong("controleCarga.idControleCarga"));
		}

		MeioTransporte meioTransporte = new MeioTransporte();
		DomainValue tpVinculo = new DomainValue();
		tpVinculo.setValue(map.getString("meioTransporteRodoviario.meioTransporte.tpVinculo"));
		meioTransporte.setTpVinculo(tpVinculo);

		MeioTransporteRodoviario meioTransporteRodoviario = new MeioTransporteRodoviario();
		meioTransporteRodoviario.setIdMeioTransporte(map.getLong("meioTransporteRodoviario.idMeioTransporte"));
		meioTransporteRodoviario.setMeioTransporte(meioTransporte);

		Usuario usuarioByIdUsuario = new Usuario();
		usuarioByIdUsuario.setIdUsuario(map.getLong("usuarioByIdUsuario.idUsuario"));

		bean.setBlSaida(map.getBoolean("blSaida"));
		bean.setBlVirouHodometro(map.getBoolean("blVirouHodometro"));
		bean.setDhCorrecao(map.getDateTime("dhCorrecao"));
		bean.setDhMedicao(map.getDateTime("dhMedicao"));
		bean.setNrQuilometragem(map.getInteger("nrQuilometragem"));
		bean.setObControleQuilometragem(map.getString("obControleQuilometragem"));

		bean.setFilial(filial);
		bean.setControleCarga(controleCarga);
		bean.setMeioTransporteRodoviario(meioTransporteRodoviario);
		bean.setUsuarioByIdUsuario(usuarioByIdUsuario);

		TypedFlatMap retorno = new TypedFlatMap();

		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();

		boolean isUsuarioDIVOP =  controleCargaService.validateManutencaoEspecialCC(usuarioLogado);

		if (!"P".equals(bean.getMeioTransporteRodoviario().getMeioTransporte().getTpVinculo().getValue())) {
			if (!isUsuarioDIVOP)
				throw new BusinessException("LMS-06013");

			ControleCarga controleCargaTest = (ControleCarga)getControleQuilometragemDAO().getAdsmHibernateTemplate()
					.get(ControleCarga.class,bean.getControleCarga().getIdControleCarga());
			if (controleCargaTest.getNotasCredito() != null && !controleCargaTest.getNotasCredito().isEmpty()) {
				retorno.put("msgError",configuracoesFacade.getMensagem("LMS-06014"));
		}
		}

		Filial filialSessao = SessionUtils.getFilialSessao();
		if (!isUsuarioDIVOP && !bean.getFilial().getIdFilial().equals(filialSessao.getIdFilial()))
			throw new BusinessException("LMS-06015");

		bean.setUsuarioByIdUsuarioCorrecao(usuarioLogado);
		bean.setDhCorrecao(JTDateTimeUtils.getDataHoraAtual());

		Long idOrdemSaida = null;
		if(bean.getOrdemSaida() != null ){
			idOrdemSaida = bean.getOrdemSaida().getIdOrdemSaida();
		}

		this.storeInformarQuilometragemMeioTransporte(bean.getFilial().getIdFilial(), false , 
				bean.getMeioTransporteRodoviario().getIdMeioTransporte(), bean.getBlSaida(), 
				bean.getNrQuilometragem(), bean.getBlVirouHodometro(), 
				bean.getControleCarga().getIdControleCarga(), idOrdemSaida, 
				bean.getObControleQuilometragem(), bean.getIdControleQuilometragem(), bean.getDhMedicao());

		retorno.put("idControleQuilometragem", bean.getIdControleQuilometragem());
		retorno.put("dhCorrecao", bean.getDhCorrecao());
		retorno.put("usuarioByIdUsuarioCorrecao.idUsuario", usuarioLogado.getIdUsuario());

		return retorno;
	}

	/**
	 * Consulta última quilometragem a partir de um meio de transporte.
	 * 
	 * @param idMeioTransporte
	 * @param dhRegistroAtual
	 * 
	 * @return Valor da quilometragem encontrada.
	 */
	public ControleQuilometragem findUltimaQuilometragemMeioTransporte(Long idMeioTransporte, DateTime dhMedicao, boolean anterior) {
		return getControleQuilometragemDAO().findUltimaQuilometragemByMeioTransporte(idMeioTransporte, dhMedicao, anterior);
	}

	public ControleQuilometragem findQuilometragemReferenciaMeioTransporte(Long idMeioTransporte, Long idControleCarga) {
		return getControleQuilometragemDAO().findQuilometragemReferenciaMeioTransporte(idMeioTransporte, idControleCarga);
	}

	public ResultSetPage findPaginatedTela(TypedFlatMap criteria) {
		ResultSetPage rsp = getControleQuilometragemDAO().findPaginatedTela(criteria,FindDefinition.createFindDefinition(criteria));

		String labelChegada = configuracoesFacade.getMensagem("chegada");
		String labelSaida = configuracoesFacade.getMensagem("saida");
		for (Iterator i = rsp.getList().iterator() ; i.hasNext() ; ) {
			Map m = (Map)i.next();
			m.put("tipo", ((Boolean)m.get("blSaida")).booleanValue() ? labelSaida : labelChegada);
		}

		return rsp;
	}
	
	public Integer getRowCountTela(TypedFlatMap criteria) {
		return getControleQuilometragemDAO().getRowCountTela(criteria);
	}

	/**
	 * Recebe informações sobre o meio de transporte e quilometragem atual e gera um controle de quilometragem.
	 * 
	 * @param idFilial id da filial onde a quilometragem está sendo informada.
	 * @param blPortaria true se está sendo informada da portaria.
	 * @param idMeioTransporte id do meio de transporte onde a quilometragem está sendo informada.
	 * @param blSaida
	 * 		<b>true</b> se quilometragem de <b>saída</b>.
	 * 		<b>false</b> se quilometragem de <b>chegada</b>.
	 * @param nrQuilometragem quilometragem atual do meio de transporte.
	 * @param blVirouHodometro true se hodômetro foi reiniciado.
	 * @param idControleCargas id do controle de carga onde a quilometragem está sendo informada.
	 * @param idOrdemSaida id da ordem de saída associada a quilometragem que está sendo informada.
	 */
	public void storeInformarQuilometragemMeioTransporte(
		Long idFilial,
		Boolean blPortaria,
		Long idMeioTransporte,
		Boolean blSaida,
		Integer nrQuilometragem,
		Boolean blVirouHodometro,
		Long idControleCargas,
		Long idOrdemSaida,
		String obControleQuilometragem
	) {
		storeInformarQuilometragemMeioTransporte(idFilial, blPortaria, idMeioTransporte, blSaida, 
				nrQuilometragem, blVirouHodometro, idControleCargas, idOrdemSaida, obControleQuilometragem, null,null);
	}
	public void storeInformarQuilometragemMeioTransporte(
		Long idFilial,
		Boolean blPortaria,
		Long idMeioTransporte,
		Boolean blSaida,
		Integer nrQuilometragem,
		Boolean blVirouHodometro,
		Long idControleCargas,
		Long idOrdemSaida,
		String obControleQuilometragem,
		Long idControleQuilometragem,
		DateTime dhMedicao
	) {

		if (dhMedicao == null) {
			dhMedicao = JTDateTimeUtils.getDataHoraAtual();
		}		
		
		Boolean blInformaKmPortaria = filialService.findBlInformaKmPortaria(idFilial);

		if( idControleQuilometragem == null ){
			if ((blInformaKmPortaria.booleanValue() && !blPortaria.booleanValue()) || (!blInformaKmPortaria.booleanValue() && blPortaria.booleanValue())){
			return;
			}
		}

		//REGRA 1
		if (idControleCargas == null && idOrdemSaida == null)
			// A quilometragem deve estar associada a um controle de cargas ou a uma ordem de saída.
			throw new BusinessException("LMS-06002");

		//REGRA 2
		if (blPortaria.booleanValue() && blInformaKmPortaria.booleanValue() && nrQuilometragem == null)
			throw new BusinessException("LMS-06011");

		//REGRA 3
		if (!blInformaKmPortaria.booleanValue() && !blPortaria.booleanValue() && nrQuilometragem == null)
			throw new BusinessException("LMS-06022");
		
		
		
		//LMS-6622
		
		if (!hasManifestosEntregaParceiraByIdControleCargaAndIdFilialOrigem(idFilial, idControleCargas) && !checkTipoVeiculoControleCarga(idControleCargas)) {
            validateKilometragem(idMeioTransporte, nrQuilometragem, blVirouHodometro, dhMedicao);
		}

		ControleQuilometragem controleQuilometragemReferencia = null;
		if( BooleanUtils.isFalse(blSaida)){
			controleQuilometragemReferencia = findQuilometragemReferenciaMeioTransporte(idMeioTransporte, idControleCargas);

		}
		Boolean blLimpaPendencia = controleQuilometragemReferencia == null;

		//Insere um registro em CONTROLE_QUILOMETRAGEM com as informações recebidas
		ControleQuilometragem ultimoControleQuilometragem = storeInformarQuilometragemMeioTransporteConcluir( idControleQuilometragem, idFilial, blPortaria, idMeioTransporte,
				blSaida, nrQuilometragem, blVirouHodometro, idControleCargas, idOrdemSaida,obControleQuilometragem,blLimpaPendencia,dhMedicao);
		
		
		if (controleQuilometragemReferencia != null) {
			ControleCarga cc = (ControleCarga)getControleQuilometragemDAO().getAdsmHibernateTemplate()
					.get(ControleCarga.class,idControleCargas);
			RotaColetaEntrega rce = cc.getRotaColetaEntrega();

				if (rce != null ) {
					int diferencaQuilometragens = 0;
					Integer ultimoNrQuilometragem = controleQuilometragemReferencia.getNrQuilometragem().intValue();
					if (blVirouHodometro.booleanValue()){
						diferencaQuilometragens = (1000000 - ultimoNrQuilometragem.intValue()) + nrQuilometragem.intValue();
					} else {
						diferencaQuilometragens = nrQuilometragem.intValue() - ultimoNrQuilometragem.intValue();
					}
					if (diferencaQuilometragens > rce.getNrKm().intValue() 
							&& (!hasManifestosEntregaParceiraByIdControleCargaAndIdFilialOrigem(idFilial, idControleCargas) && !checkTipoVeiculoControleCarga(idControleCargas))) {
						//LMS-6622
						if (StringUtils.isBlank(obControleQuilometragem)){
							throw new BusinessException("LMS-06017");
						} else {
							if(cc != null && cc.getDhSaidaColetaEntrega() != null && cc.getDhChegadaColetaEntrega() != null
									&& BooleanUtils.isTrue(controleQuilometragemReferencia.getBlSaida()) 
									&& BooleanUtils.isFalse(ultimoControleQuilometragem.getBlSaida()) ) {

								/*
								 * LMS-6215 retirada verificacao possuiVinculoTabelaQuilometragem
								 */
									if (cc.getMeioTransporteByIdTransportado() != null) {
										String tpCategoria = cc.getMeioTransporteByIdTransportado().getModeloMeioTransporte().getTipoMeioTransporte().getTpCategoria().getValue();
										if (StringUtils.isNotBlank(tpCategoria)) {

											String nomeParametro = ControleQuilometragemHelper.getNomeParametroGeralByTpCategoriaMeioTransporte(tpCategoria);

											if (StringUtils.isNotBlank(nomeParametro)) {
												BigDecimal valorParametro = (BigDecimal)configuracoesFacade.getValorParametro(nomeParametro);

												if (valorParametro != null) {
													BigDecimal qtHoras = ControleQuilometragemHelper.calculaDiferencaHoras(cc.getDhSaidaColetaEntrega(), cc.getDhChegadaColetaEntrega());
													//Inverte o sinal da quantidade de horas devido ordem de chegada/saida
													if (qtHoras.compareTo(BigDecimal.ZERO) < 0){
														qtHoras = qtHoras.multiply(new BigDecimal(-1));
					}


													BigDecimal calcParam = ControleQuilometragemHelper.calculaKilometragemParametroXHoras(qtHoras, valorParametro);

													if (calcParam != null && ControleQuilometragemHelper.isLess(calcParam, BigDecimal.valueOf(diferencaQuilometragens)).booleanValue()) {
														throw new BusinessException("LMS-06040", new Object[]{diferencaQuilometragens});
													}
													//LMS-4076
													String nrFrota = meioTransporteService.findById(idMeioTransporte).getNrFrota(); 
													obControleQuilometragem = recursoMensagemService.findByChave("LMS-05150", new Object[]{nrFrota} );
													generateWorkFlowPendenciaControleQuilometragem(idFilial, obControleQuilometragem, ultimoControleQuilometragem.getIdControleQuilometragem());
												}

											}
										}
									}
							}
						}
						
						
					}
				}//fim if
			}//fim if
		}

	 private boolean checkTipoVeiculoControleCarga(Long idControleCargas) {
	    	ControleCarga controleCarga = controleCargaService.findById(idControleCargas);
			
			if(controleCarga != null && controleCarga.getMeioTransporteByIdTransportado() != null){
				MeioTransporte meioTransporte = controleCarga.getMeioTransporteByIdTransportado();
				if(checkValidMeioTransporte(meioTransporte)){
					TipoMeioTransporte tipoMeioTransporte = meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte();
					return checkDsTipoMeioTransporteWithCavaloTrator(tipoMeioTransporte);
				}
			}
			return false;
		}

	private boolean checkValidMeioTransporte(MeioTransporte meioTransporte) {
		return meioTransporte.getModeloMeioTransporte() != null && meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte() != null;
	}

	private boolean checkDsTipoMeioTransporteWithCavaloTrator(TipoMeioTransporte tipoMeioTransporte) {
		if(tipoMeioTransporte != null && tipoMeioTransporte.getDsTipoMeioTransporte().equalsIgnoreCase(CAVALO_TRATOR)){
			return true;
		}
		return false;
	}

    private void validateKilometragem(Long idMeioTransporte, Integer nrQuilometragem, Boolean blVirouHodometro, DateTime dhMedicao) {
        if( BooleanUtils.isFalse(blVirouHodometro) ){
        	ControleQuilometragem cq1 = findUltimaQuilometragemMeioTransporte(idMeioTransporte,dhMedicao, true);
        	if( cq1 != null && CompareUtils.gt(cq1.getNrQuilometragem(),nrQuilometragem)) {
                throw new BusinessException("LMS-06012");
            }
        }
        ControleQuilometragem cq2 = findUltimaQuilometragemMeioTransporte(idMeioTransporte,dhMedicao, false);
        if (cq2 != null && BooleanUtils.isFalse(cq2.getBlVirouHodometro()) && CompareUtils.lt(cq2.getNrQuilometragem(), nrQuilometragem)) {
            throw new BusinessException("LMS-06012");
        }
    }
		
	private Boolean hasManifestosEntregaParceiraByIdControleCargaAndIdFilialOrigem(Long idFilial, Long idControleCarga) {
		return manifestoService.hasManifestosEntregaParceiraByIdControleCargaAndIdFilialOrigem(idControleCarga, idFilial);
	}

	private void generateWorkFlowPendenciaControleQuilometragem(Long idFilial, String obControleQuilometragem, Long idCQ) {
		Filial filial = filialService.findById(idFilial);

		if(BooleanUtils.isTrue(filial.getBlWorkflowKm())){
		Pendencia pendenciaGerada = this.workflowPendenciaService.generatePendencia(idFilial,
					//número do evento referente à aprovação de km maior que o máximo da rota.
					ConstantesWorkflow.NR601_KM_NOTA,
					idCQ, obControleQuilometragem, JTDateTimeUtils.getDataHoraAtual());
		if (pendenciaGerada != null) { // lms-2703
			ControleQuilometragem controleQuilometragemLoaded = this.findById(idCQ);
			controleQuilometragemLoaded.setPendencia(pendenciaGerada);
			controleQuilometragemLoaded.setTpSituacaoPendencia(pendenciaGerada.getTpSituacaoPendencia());
			store(controleQuilometragemLoaded);
		}
	}
	}

	public void storeInformarQuilometragemMeioTransporte(
		Long idFilial,
		Boolean blPortaria,
		Long idMeioTransporte,
		Boolean blSaida,
		Integer nrQuilometragem,
		Boolean blVirouHodometro,
		Long idControleCargas,
		Long idOrdemSaida
	) {
		this.storeInformarQuilometragemMeioTransporte(idFilial, blPortaria, idMeioTransporte, blSaida, nrQuilometragem,
				blVirouHodometro, idControleCargas, idOrdemSaida, null);
	}

	/**
	 * Método auxiliar do método storeInformarQuilometragemMeioTransporte.
	 * @param idFilial
	 * @param blPortaria
	 * @param idMeioTransporte
	 * @param blSaida
	 * @param nrQuilometragem
	 * @param blVirouHodometro
	 * @param idControleCargas
	 * @param idOrdemSaida
 	*/
	public ControleQuilometragem storeInformarQuilometragemMeioTransporteConcluir(
		Long idControleQuilometragem,	
		Long idFilial,
		Boolean blPortaria,
		Long idMeioTransporte,
		Boolean blSaida,
		Integer nrQuilometragem,
		Boolean blVirouHodometro,
		Long idControleCargas,
		Long idOrdemSaida,
		String obControleQuilometragem,
		Boolean blLimpaPendencia,
		DateTime dhMedicao
	) {
		ControleQuilometragem bean;	
		if ( dhMedicao == null ){
			dhMedicao = JTDateTimeUtils.getDataHoraAtual();
		}
		if(idControleQuilometragem != null){
			bean = this.findById(idControleQuilometragem);
			if( BooleanUtils.isTrue(blLimpaPendencia) ){
				bean.setPendencia(null);
				bean.setTpSituacaoPendencia(null);
			}
		}else{
			bean = new ControleQuilometragem();
		bean.setIdControleQuilometragem(null);
		}
		
		bean.setBlSaida(blSaida);
		bean.setBlVirouHodometro(blVirouHodometro);
		bean.setDhMedicao(dhMedicao);
		bean.setNrQuilometragem(nrQuilometragem);
		bean.setObControleQuilometragem(obControleQuilometragem);

		final Usuario usuario = SessionContext.getUser();
		bean.setUsuarioByIdUsuario(usuario);
		bean.setUsuarioByIdUsuarioCorrecao(null);

		if (idControleCargas != null) {
			ControleCarga controleCarga = new ControleCarga();
			controleCarga.setIdControleCarga(idControleCargas);
			bean.setControleCarga(controleCarga);
		} else
			bean.setControleCarga(null);

		Filial filial = new Filial();
		filial.setIdFilial(idFilial);
		bean.setFilial(filial);

		MeioTransporteRodoviario meioTransporteRodoviario = new MeioTransporteRodoviario();
		meioTransporteRodoviario.setIdMeioTransporte(idMeioTransporte);
		bean.setMeioTransporteRodoviario(meioTransporteRodoviario);

		if (idOrdemSaida != null) {
			OrdemSaida ordemSaida = new OrdemSaida();
			ordemSaida.setIdOrdemSaida(idOrdemSaida);
			bean.setOrdemSaida(ordemSaida);
		} else
			bean.setOrdemSaida(null);

		store(bean);
		
		return bean;
	}

	/**
	 * Busca um controle de quilometragem a partir dos parâmetros informados.
	 * Método utilizado pela Integração
	 * @author Felipe Ferreira
	 * 
	 * @param idMeioTransporte Identificador do meio de transporte.
	 * @param dhMedicao Data/hora de medição.
	 * @param blSaida Indicador de Saida na portaria.
	 * @return uma instância de ControleQuilometragem caso encontrado. Senão, retora null.
	 */
	public ControleQuilometragem findControleQuilometragem(
		Long idMeioTransporte,
		DateTime dhMedicao,
		Boolean blSaida
	) {
		return getControleQuilometragemDAO().findControleQuilometragem(idMeioTransporte, dhMedicao, blSaida);
	}

	public ControleQuilometragem getControleQuilometragem(Long idMeioTransporte,DateTime dhMedicao,Boolean blSaida){
		return getControleQuilometragemDAO().getControleQuilometragem(idMeioTransporte, dhMedicao, blSaida);
	}

	/**
	 * Método que retorna um ControleQuilometragem a partir do ID do Controle de Carga e do ID da Filial.
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	public ControleQuilometragem findControleQuilometragemByIdControleCargaByIdFilial(Long idControleCarga, Long idFilial, Boolean blSaida) {
		return this.getControleQuilometragemDAO().findControleQuilometragemByIdControleCargaByIdFilial(idControleCarga, idFilial, blSaida);
	}

	public ControleQuilometragem findUltimoControleQuilometragemByIdControleCarga(Long idControleCarga, Boolean blSaida) {
		return this.getControleQuilometragemDAO().findUltimoControleQuilometragemByIdControleCarga(idControleCarga, blSaida);
	}
	
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	
	public static void sortByDhMedicao(List<ControleQuilometragem> controlesQuilometragem) {
	    Collections.sort(controlesQuilometragem, new Comparator<ControleQuilometragem>() {
            
            public int compare(ControleQuilometragem o1, ControleQuilometragem o2) {
                return o1.getDhMedicao().compareTo(o2.getDhMedicao());
            }
        });
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setControleQuilometragemDAO(ControleQuilometragemDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private final ControleQuilometragemDAO getControleQuilometragemDAO() {
		return (ControleQuilometragemDAO) getDao();
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * @param controleCargaService the controleCargaService to set
	 */
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setRecursoMensagemService(
			RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	
	public List<Map<String, Object>> findQuilometragemExcedenteRota(Map<String, Object> parameters){
		return getControleQuilometragemDAO().findQuilometragemExcedenteRota(parameters);
	}
	
}