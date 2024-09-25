package com.mercurio.lms.sim.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.lms.constantes.ConsErro;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.dao.EventoDispositivoUnitizacaoDAO;
import com.mercurio.lms.sim.model.dao.LMEventoDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.sim.eventoDispositivoService"
 */
public class EventoDispositivoUnitizacaoService extends CrudService<EventoDispositivoUnitizacao, Long> {

	private static final short CD_EVENTO = (short) 21;
	private static final String MATRIZ = "MA";
	private static final String DISPOSITIVO_UNITIZACAO = "DISPOSITIVO_UNITIZACAO";
	private static final String EVENTO_DISPOSITIVO_UNITIZACAO = "EVENTO_DISPOSITIVO_UNITIZACAO";

	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private LMEventoDAO lmEventoDao;
	private ParametroGeralService parametroGeralService;
	private EventoService eventoService;

	/**
	 * @param idDispositivoUnitizacao
	 * @param cdEvento
	 * @param tpEvento
	 * @param blEventoCancelado
	 * @return List<EventoDispositivoUnitizacao>
	 */
	public List<EventoDispositivoUnitizacao> findEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, Short cdEvento, DomainValue tpEvento, Boolean blEventoCancelado) {
		return getEventoDispositivoUnitizacaoDAO().findEventoDispositivoUnitizacao(idDispositivoUnitizacao, cdEvento, tpEvento, blEventoCancelado);
	}
	
	
	/**
	 * 
	 * @param idDispositivoUnitizacao
	 * @param cdEvento
	 * @param tpScan
	 * @param blEventoCancelado
	 * @return List<EventoDispositivoUnitizacao>
	 */
	public List<EventoDispositivoUnitizacao> findEventosDispositivoUnitizacao(Long idDispositivoUnitizacao, String parametroGeralEvento, DomainValue tpScan, Boolean blEventoCancelado) {
		String cdEvento = getParametroGeral(parametroGeralEvento);
		return getEventoDispositivoUnitizacaoDAO().findEventosDispositivoUnitizacao(idDispositivoUnitizacao, Short.valueOf(cdEvento), tpScan, blEventoCancelado);
	}
	/**
	 * Procura evento(s) no dispositivoUnitizacao informado que seja igual ao código do evento
	 * passado por parâmetro.
	 *
	 * @param idDispositivoUnitizacao
	 * @param cdEvento
	 * @return
	 */
	public List<EventoDispositivoUnitizacao> findEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, Short cdEvento) {
		return getEventoDispositivoUnitizacaoDAO().findEventoDispositivoUnitizacao(idDispositivoUnitizacao, cdEvento);
	}

	/**
	 * Procura evento(s) no dispositivoUnitizacao informado que seja igual ao código e a filial do evento informado.
	 * passado por parâmetro.
	 *
	 * @param idDispositivoUnitizacao
	 * @param idFilialEvento
	 * @param cdEvento
	 * @return
	 */
	public List<EventoDispositivoUnitizacao> findEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, Long idFilialEvento, Short[] cdEvento) {
		return getEventoDispositivoUnitizacaoDAO().findEventoDispositivoUnitizacao(idDispositivoUnitizacao, idFilialEvento, cdEvento);
	}

	/**
	 * Retorna o último Evento cadastrado para o Documento de Servico
	 *
	 * @param idDispositivoUnitizacao
	 * @return
	 */
	public EventoDispositivoUnitizacao findUltimoEventoDispositivoUnitizacao(Long idDispositivoUnitizacao) {
		return getEventoDispositivoUnitizacaoDAO().findUltimoEventoDispositivoUnitizacao(idDispositivoUnitizacao, null, null);
	}

	public EventoDispositivoUnitizacao findUltimoEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, String tpEvento, Boolean blEventoCancelado) {
		return getEventoDispositivoUnitizacaoDAO().findUltimoEventoDispositivoUnitizacao(idDispositivoUnitizacao, tpEvento, blEventoCancelado);
	}

	public EventoDispositivoUnitizacao findUltimoEventoDispositivoUnitizacaoOnSession(Long idDispositivoUnitizacao, String tpEvento, Boolean blEventoCancelado) {
		return getEventoDispositivoUnitizacaoDAO().findUltimoEventoDispositivoUnitizacaoOnSession(idDispositivoUnitizacao, tpEvento, blEventoCancelado);
	}

	public void removeByIdDispositivoUnitizacao(Long idDispositivoUnitizacao) {
		getEventoDispositivoUnitizacaoDAO().removeByIdDispositivo(idDispositivoUnitizacao);
	}

	/**
	 * Recupera uma instância de <code>EventoDispositivoUnitizacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public EventoDispositivoUnitizacao findById(java.lang.Long id) {
		return (EventoDispositivoUnitizacao) super.findById(id);
	}

	/**
	 * Verifica se o evento informado foi lancado no documento de servico
	 *
	 * @param idEvento
	 * @return
	 */
	public List<EventoDispositivoUnitizacao> findByEventoByDispositivo(Long idEvento, Long idDispositivo) {
		return getEventoDispositivoUnitizacaoDAO().findByEventoByDispositivo(idEvento, idDispositivo);
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
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(EventoDispositivoUnitizacao bean) {
		return super.store(bean);
	}

	private String getParametroGeral(String nmParam) {
		ParametroGeral param = parametroGeralService.findByNomeParametro(nmParam, Boolean.FALSE);
		if (param != null) {
			return param.getDsConteudo();
		} else {
			return null;
		}
	}

	public java.io.Serializable generateEventoDispositivo(Long idDispositivo, String parametroGeralEvento, String tpScan) {
		return generateEventoDispositivo(idDispositivo, parametroGeralEvento, tpScan, null);
	}

	public java.io.Serializable generateEventoDispositivo(Long idDispositivo, String parametroGeralEvento, String tpScan, String obComplemento) {

		String cdEvento = getParametroGeral(parametroGeralEvento);
		if (cdEvento == null || "".equals(cdEvento)) {
			throw new BusinessException(ConsErro.PARAMETRO_GERAL_NAO_ENCONTRADO);
		}

		return generateEventoDispositivo(idDispositivo, Short.valueOf(cdEvento), tpScan, obComplemento);
	}

	public java.io.Serializable generateEventoDispositivo(Long idDispositivo, Short cdEvento, String tpScan) {
		return generateEventoDispositivo(idDispositivo, cdEvento, tpScan, null);
	}

	public java.io.Serializable generateEventoDispositivo(Long idDispositivo, Short cdEvento, String tpScan, String obComplemento) {
		DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findById(idDispositivo);
		return generateEventoDispositivo(dispositivo, cdEvento, tpScan, obComplemento);
	}
	
	public void generateEventoDispositivoUnitizacao(DispositivoUnitizacao dispositivo, String parametroGeralEvento, String tpScan, String obComplemento) {		
		
		String cdEvento = getParametroGeral(parametroGeralEvento);
		if (cdEvento == null || "".equals(cdEvento)) {
			throw new BusinessException(ConsErro.PARAMETRO_GERAL_NAO_ENCONTRADO);
		} 
		generateEventoDispositivo(dispositivo, Short.valueOf(cdEvento), tpScan, obComplemento);
	}

	public java.io.Serializable generateEventoDispositivoComBatch(DispositivoUnitizacao dispositivo, Short cdEvento,
																  String tpScan, String obComplemento,
																  AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
		Evento evento = fetchEvento(dispositivo, cdEvento);

		EventoDispositivoUnitizacao eventoDispositivoUnitizacao =
				generateEventoDispositivo(dispositivo, evento, tpScan, obComplemento);

		Filial filialSessao = SessionUtils.getFilialSessao();
		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
		DateTime dhEvento = JTDateTimeUtils.getDataHoraAtual();

		DispositivoUnitizacao dispositivoUnitizacao =
				storeEventoDispositivoUnitizacaoComBatch(filialSessao, usuarioLogado, eventoDispositivoUnitizacao, dhEvento,adsmNativeBatchSqlOperations);

		updateDispositivoUnitizacaoComBatch(adsmNativeBatchSqlOperations, dispositivoUnitizacao);
		storeEventoDispositivoUnitizacaoComBatch(adsmNativeBatchSqlOperations, eventoDispositivoUnitizacao);

		return eventoDispositivoUnitizacao;
	}

	private void updateDispositivoUnitizacaoComBatch(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, DispositivoUnitizacao dispositivoUnitizacao) {
		String alias =
				"dispositivoUnitizacao" +
						(dispositivoUnitizacao.getLocalizacaoFilial() != null ? dispositivoUnitizacao.getLocalizacaoFilial().getIdFilial() : "") +
						(dispositivoUnitizacao.getLocalizacaoMercadoria() != null ? dispositivoUnitizacao.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria() : "");

		if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(DISPOSITIVO_UNITIZACAO, alias)) {

			adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(DISPOSITIVO_UNITIZACAO,
					"ID_LOCALIZACAO_MERCADORIA",
					dispositivoUnitizacao.getLocalizacaoFilial() != null ? dispositivoUnitizacao.getLocalizacaoFilial().getIdFilial() : null,
					alias);

			adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(DISPOSITIVO_UNITIZACAO,
					"ID_LOCALIZACAO_FILIAL",
					dispositivoUnitizacao.getLocalizacaoMercadoria() != null ? dispositivoUnitizacao.getLocalizacaoMercadoria().getIdLocalizacaoMercadoria() : null,
					alias);
		}

		adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(DISPOSITIVO_UNITIZACAO,
				dispositivoUnitizacao.getIdDispositivoUnitizacao(), alias);

		getDao().getAdsmHibernateTemplate().evict(dispositivoUnitizacao);
	}

	private void storeEventoDispositivoUnitizacaoComBatch(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, EventoDispositivoUnitizacao eventoDispositivoUnitizacao) {
		Map<String, Object> eventoDispositivoUnitizacaoKeyValueMap = new HashMap<String, Object>();
		eventoDispositivoUnitizacaoKeyValueMap.put("ID_EVENTO", eventoDispositivoUnitizacao.getEvento() != null ? eventoDispositivoUnitizacao.getEvento().getIdEvento() : null);
		eventoDispositivoUnitizacaoKeyValueMap.put("ID_DISPOSITIVO_UNITIZACAO", eventoDispositivoUnitizacao.getDispositivoUnitizacao() != null ? eventoDispositivoUnitizacao.getDispositivoUnitizacao().getIdDispositivoUnitizacao() : null);
		eventoDispositivoUnitizacaoKeyValueMap.put("TP_SCAN", eventoDispositivoUnitizacao.getTpScan() != null ? eventoDispositivoUnitizacao.getTpScan().getValue() : null);
		eventoDispositivoUnitizacaoKeyValueMap.put("ID_FILIAL", eventoDispositivoUnitizacao.getFilial() != null ? eventoDispositivoUnitizacao.getFilial().getIdFilial() : null);
		eventoDispositivoUnitizacaoKeyValueMap.put("DH_EVENTO", eventoDispositivoUnitizacao.getDhEvento());
		eventoDispositivoUnitizacaoKeyValueMap.put("DH_EVENTO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
		eventoDispositivoUnitizacaoKeyValueMap.put("DH_INCLUSAO", eventoDispositivoUnitizacao.getDhInclusao());
		eventoDispositivoUnitizacaoKeyValueMap.put("DH_INCLUSAO_TZR", SessionUtils.getFilialSessao().getDateTimeZone().getID());
		eventoDispositivoUnitizacaoKeyValueMap.put("ID_USUARIO", eventoDispositivoUnitizacao.getUsuario() != null ? eventoDispositivoUnitizacao.getUsuario().getIdUsuario() : null);
		eventoDispositivoUnitizacaoKeyValueMap.put("BL_EVENTO_CANCELADO", eventoDispositivoUnitizacao.getBlEventoCancelado());
		eventoDispositivoUnitizacaoKeyValueMap.put("OB_COMPLEMENTO", eventoDispositivoUnitizacao.getObComplemento());

		adsmNativeBatchSqlOperations.addNativeBatchInsert(EVENTO_DISPOSITIVO_UNITIZACAO, eventoDispositivoUnitizacaoKeyValueMap);
	}

	public java.io.Serializable generateEventoDispositivo(DispositivoUnitizacao dispositivo, Short cdEvento, String tpScan, String obComplemento) {

		Evento evento = fetchEvento(dispositivo, cdEvento);

		EventoDispositivoUnitizacao eventoDispositivoUnitizacao =
				generateEventoDispositivo(dispositivo, evento, tpScan, obComplemento);
		return this.storeEventoDispositivoUnitizacao(eventoDispositivoUnitizacao);
	}

	private Evento fetchEvento(DispositivoUnitizacao dispositivo, Short cdEvento) {
		if (dispositivo == null) {
			throw new BusinessException(ConsErro.DISP_UNITIZACAO_NAO_ENCONTRADO);
		}

		Evento evento = eventoService.findByCdEvento(Short.valueOf(cdEvento));
		if (evento == null) {
			throw new BusinessException(ConsErro.EVENTO_CODIGO_NAO_ENCONTRADO, new Object[]{cdEvento});
		}
		return evento;
	}


	public EventoDispositivoUnitizacao generateEventoDispositivo(
			Filial filial,
			Usuario usuario,
			DispositivoUnitizacao dispositivoUnitizacao,
			Evento evento,
			String tpScan,
			DateTime dhEvento) {
		EventoDispositivoUnitizacao eventoDispositivoUnitizacao =
				generateEventoDispositivo(dispositivoUnitizacao, evento, tpScan, null);
		storeEventoDispositivoUnitizacao(filial, usuario, eventoDispositivoUnitizacao, dhEvento);
		return eventoDispositivoUnitizacao;
	}

	public EventoDispositivoUnitizacao generateEventoDispositivo(
			DispositivoUnitizacao dispositivo, Evento evento, String tpScan, String obComplemento) {
		EventoDispositivoUnitizacao eventoDispositivoUnitizacao = new EventoDispositivoUnitizacao();
		eventoDispositivoUnitizacao.setDispositivoUnitizacao(dispositivo);
		eventoDispositivoUnitizacao.setEvento(evento);
		eventoDispositivoUnitizacao.setTpScan(new DomainValue(tpScan));
		eventoDispositivoUnitizacao.setObComplemento(obComplemento);
		return eventoDispositivoUnitizacao;
	}

	public void generateEventoDispositivo(List<DispositivoUnitizacao> listDispositivoUnitizacao, Short cdEvento, String tpScan) {
		generateEventoDispositivo(listDispositivoUnitizacao, cdEvento, tpScan, null);
	}

	public void generateEventoDispositivo(List<DispositivoUnitizacao> listDispositivoUnitizacao, Short cdEvento, String tpScan, Long idFilial) {

		Evento evento = eventoService.findByCdEvento(cdEvento);
		if (evento == null) {
			throw new BusinessException(ConsErro.EVENTO_CODIGO_NAO_ENCONTRADO, new Object[]{cdEvento});
		}

		LocalizacaoMercadoria localizacaoMercadoria = evento.getLocalizacaoMercadoria();
		Filial filialUsuarioLogado = idFilial == null ? SessionUtils.getFilialSessao() : this.generateNewFilial(idFilial);

		List<EventoDispositivoUnitizacao> listEventoDispositivoUnitizacao = new ArrayList<EventoDispositivoUnitizacao>();

		if (listDispositivoUnitizacao != null && !listDispositivoUnitizacao.isEmpty()) {

			for (DispositivoUnitizacao dispositivoUnitizacao : listDispositivoUnitizacao) {
				EventoDispositivoUnitizacao eventoDispositivoUnitizacao = getNewEventoDispositivoUnitizacao(tpScan, evento, dispositivoUnitizacao, filialUsuarioLogado);
				setLocalizacaoMercadoriaAndLocalizacaoFilial(evento, localizacaoMercadoria, filialUsuarioLogado, dispositivoUnitizacao);
				validaCancelamentoEventoDispositivoUnitizacao(evento.getCancelaEvento(), dispositivoUnitizacao);
				listEventoDispositivoUnitizacao.add(eventoDispositivoUnitizacao);
			}

		}

		dispositivoUnitizacaoService.storeAll(listDispositivoUnitizacao);

		storeAll(listEventoDispositivoUnitizacao);
	}

	private Filial generateNewFilial(Long idFilial) {
		Filial filial = new Filial();
		filial.setIdFilial(idFilial);
		return filial;
	}


	/***
	 * Método faz mais do que diz no nome para minimizar impactos. Como esse refactoring foi feito devido a problemas de performance feito assim para
	 *  manter a compatibilidade com storeEventoDispositivoUnitizacao o qual continuará sendo utilizado e caso seja alterado o impacto será menor
	 * @param cancelaEvento
	 * @param dispositivoUnitizacao
	 */
	private void validaCancelamentoEventoDispositivoUnitizacao(Evento cancelaEvento, DispositivoUnitizacao dispositivoUnitizacao) {

		if (cancelaEvento != null) {
			EventoDispositivoUnitizacao eventoDispositivoUnitizacaoCancelado = cancelaEventoDispositivoUnitizacao(
					cancelaEvento.getIdEvento(), dispositivoUnitizacao.getIdDispositivoUnitizacao());

			if (eventoDispositivoUnitizacaoCancelado != null) {
				this.store(eventoDispositivoUnitizacaoCancelado);
			}

			setLocalizacaoMercadoriaEventoDispositivoUnitizacaoNaoCanc(dispositivoUnitizacao);
		}

	}


	/***
	 * Método faz mais do que diz no nome para minimizar impactos. Como esse refactoring foi feito devido a problemas de performance feito assim para
	 *  manter a compatibilidade com storeEventoDispositivoUnitizacao o qual continuará sendo utilizado e caso seja alterado o impacto será menor
	 * @param cancelaEvento
	 * @param dispositivoUnitizacao
	 * @param adsmNativeBatchSqlOperations
	 */
	private void validaCancelamentoEventoDispositivoUnitizacaoComBatch(Evento cancelaEvento, DispositivoUnitizacao dispositivoUnitizacao,
																	   AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

		if (cancelaEvento != null) {

			List<EventoDispositivoUnitizacao> listEventoDispositivoUnitizacao = findByEventoByDispositivo(
					cancelaEvento.getIdEvento(), dispositivoUnitizacao.getIdDispositivoUnitizacao());

			if (listEventoDispositivoUnitizacao != null && !listEventoDispositivoUnitizacao.isEmpty()) {

				EventoDispositivoUnitizacao eventoDispositivoUnitizacaoCancelado = listEventoDispositivoUnitizacao.get(0);

				String alias = "eventoDispositivoUnitizacaoCancelado";

				if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(EVENTO_DISPOSITIVO_UNITIZACAO, alias)) {
					adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
							EVENTO_DISPOSITIVO_UNITIZACAO, "BL_EVENTO_CANCELADO", true, alias
					);
				}

				adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(EVENTO_DISPOSITIVO_UNITIZACAO,
						eventoDispositivoUnitizacaoCancelado.getIdEventoDispositivoUnitizacao(), alias);

			}

			setLocalizacaoMercadoriaEventoDispositivoUnitizacaoNaoCancCBatch(dispositivoUnitizacao, listEventoDispositivoUnitizacao);

		}

	}

	private void setLocalizacaoMercadoriaAndLocalizacaoFilial(Evento evento, LocalizacaoMercadoria localizacaoMercadoria, Filial filialUsuarioLogado,
															  DispositivoUnitizacao dispositivoUnitizacao) {

		if (evento.getLocalizacaoMercadoria() != null) {
			dispositivoUnitizacao.setLocalizacaoMercadoria(localizacaoMercadoria);
			setLocalizacaoFilialDispositivoUnitizacao(dispositivoUnitizacao, filialUsuarioLogado);
		}

	}

	private EventoDispositivoUnitizacao getNewEventoDispositivoUnitizacao(String tpScan, Evento evento, DispositivoUnitizacao dispositivoUnitizacao, Filial filial) {
		EventoDispositivoUnitizacao eventoDispositivoUnitizacao = new EventoDispositivoUnitizacao();
		eventoDispositivoUnitizacao.setEvento(evento);
		eventoDispositivoUnitizacao.setDispositivoUnitizacao(dispositivoUnitizacao);
		eventoDispositivoUnitizacao.setTpScan(new DomainValue(tpScan));

		eventoDispositivoUnitizacao.setFilial(filial);
		eventoDispositivoUnitizacao.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		eventoDispositivoUnitizacao.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
		eventoDispositivoUnitizacao.setUsuario(SessionUtils.getUsuarioLogado());
		eventoDispositivoUnitizacao.setBlEventoCancelado(Boolean.FALSE);
		return eventoDispositivoUnitizacao;
	}

	private void setLocalizacaoFilialDispositivoUnitizacao(DispositivoUnitizacao dispositivoUnitizacao, Filial filialUsuarioLogado) {

		if (filialUsuarioLogado != null) {
			HistoricoFilial historicoFilial = SessionUtils.getUltimoHistoricoFilialSessao();
			String tpFilial = historicoFilial.getTpFilial().getValue();
			if (!tpFilial.equals(MATRIZ)) {
				dispositivoUnitizacao.setLocalizacaoFilial(filialUsuarioLogado);
			}
		}

	}

	private EventoDispositivoUnitizacao cancelaEventoDispositivoUnitizacao(Long idCancelaEvento, Long idDispositivoUnitizacao) {
		List<EventoDispositivoUnitizacao> listEventoDispositivoUnitizacao = findByEventoByDispositivo(idCancelaEvento, idDispositivoUnitizacao);

		EventoDispositivoUnitizacao eventoDispositivoUnitizacao = null;
		if (listEventoDispositivoUnitizacao != null && !listEventoDispositivoUnitizacao.isEmpty()) {
			eventoDispositivoUnitizacao = listEventoDispositivoUnitizacao.get(0);
			eventoDispositivoUnitizacao.setBlEventoCancelado(true);
		}
		return eventoDispositivoUnitizacao;
	}

	public java.io.Serializable storeEventoDispositivoUnitizacao(EventoDispositivoUnitizacao eventoDispositivoUnitizacao) {
		Filial filialSessao = SessionUtils.getFilialSessao();
		Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
		DateTime dhEvento = JTDateTimeUtils.getDataHoraAtual();

		DispositivoUnitizacao dispositivoUnitizacao =
				storeEventoDispositivoUnitizacao(filialSessao, usuarioLogado, eventoDispositivoUnitizacao, dhEvento);

		//SALVA ALTERAÇÕES NO VOLUME
		dispositivoUnitizacaoService.store(dispositivoUnitizacao);

		//SALVA NOVO EVENTO VOLUME
		return super.store(eventoDispositivoUnitizacao);
	}

	public DispositivoUnitizacao storeEventoDispositivoUnitizacaoComBatch(
			Filial filial,
			Usuario usuario,
			EventoDispositivoUnitizacao eventoDispositivoUnitizacao,
			DateTime dhEvento,AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

		Evento evento = eventoDispositivoUnitizacao.getEvento();
		LocalizacaoMercadoria localizacaoMercadoria = evento.getLocalizacaoMercadoria();
		Evento cancelaEvento = evento.getCancelaEvento();
		DispositivoUnitizacao dispositivoUnitizacao = eventoDispositivoUnitizacao.getDispositivoUnitizacao();

		setLocalizacaoMercadoriaAndLocalizacaoFilial(evento, localizacaoMercadoria, filial, dispositivoUnitizacao);

		validaCancelamentoEventoDispositivoUnitizacaoComBatch(cancelaEvento, dispositivoUnitizacao,adsmNativeBatchSqlOperations);

		setEventoUnitizacaoData(filial, usuario, eventoDispositivoUnitizacao, dhEvento);
		return dispositivoUnitizacao;
	}

	private void setEventoUnitizacaoData(Filial filial, Usuario usuario, EventoDispositivoUnitizacao eventoDispositivoUnitizacao, DateTime dhEvento) {
		eventoDispositivoUnitizacao.setFilial(filial);
		eventoDispositivoUnitizacao.setUsuario(usuario);
		eventoDispositivoUnitizacao.setDhEvento(dhEvento);
		eventoDispositivoUnitizacao.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		eventoDispositivoUnitizacao.setBlEventoCancelado(Boolean.FALSE);
	}

	public DispositivoUnitizacao storeEventoDispositivoUnitizacao(
			Filial filial,
			Usuario usuario,
			EventoDispositivoUnitizacao eventoDispositivoUnitizacao,
			DateTime dhEvento) {

		Evento evento = eventoDispositivoUnitizacao.getEvento();
		LocalizacaoMercadoria localizacaoMercadoria = evento.getLocalizacaoMercadoria();
		Evento cancelaEvento = evento.getCancelaEvento();
		DispositivoUnitizacao dispositivoUnitizacao = eventoDispositivoUnitizacao.getDispositivoUnitizacao();

		setLocalizacaoMercadoriaAndLocalizacaoFilial(evento, localizacaoMercadoria, filial, dispositivoUnitizacao);
		validaCancelamentoEventoDispositivoUnitizacao(cancelaEvento, dispositivoUnitizacao);

		setEventoUnitizacaoData(filial, usuario, eventoDispositivoUnitizacao, dhEvento);
		return dispositivoUnitizacao;
	}


	private DispositivoUnitizacao setLocalizacaoMercadoriaEventoDispositivoUnitizacaoNaoCancCBatch(DispositivoUnitizacao dispositivoUnitizacao,
																								   List<EventoDispositivoUnitizacao> eventoDispositivoUnitizacoes) {
		if (eventoDispositivoUnitizacoes != null && !eventoDispositivoUnitizacoes.isEmpty() && eventoDispositivoUnitizacoes.size() > 1) {
			EventoDispositivoUnitizacao eventoDispositivoUnitizacaoNaoCanc = (EventoDispositivoUnitizacao) eventoDispositivoUnitizacoes.get(1);
			dispositivoUnitizacao.setLocalizacaoMercadoria(eventoDispositivoUnitizacaoNaoCanc.getEvento().getLocalizacaoMercadoria());
        /* Caso o evento cancelado seja o último, seta localização para null */
		} else {
			dispositivoUnitizacao.setLocalizacaoMercadoria(null);
		}
		return dispositivoUnitizacao;
	}


	private DispositivoUnitizacao setLocalizacaoMercadoriaEventoDispositivoUnitizacaoNaoCanc(DispositivoUnitizacao dispositivoUnitizacao) {
		List<EventoDispositivoUnitizacao> listEventoDispositivoUnitizacaoNaoCanc = findEventosDispositivoUnitizacaoNaoCancelados(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		if (!listEventoDispositivoUnitizacaoNaoCanc.isEmpty()) {
			EventoDispositivoUnitizacao eventoDispositivoUnitizacaoNaoCanc = (EventoDispositivoUnitizacao) listEventoDispositivoUnitizacaoNaoCanc.get(0);
			dispositivoUnitizacao.setLocalizacaoMercadoria(eventoDispositivoUnitizacaoNaoCanc.getEvento().getLocalizacaoMercadoria());
        /* Caso o evento cancelado seja o último, seta localização para null */
		} else {
			dispositivoUnitizacao.setLocalizacaoMercadoria(null);
		}
		return dispositivoUnitizacao;
	}

	public List<Long> findIdsByIdDispositivoUnitizacao(Long idDispositivoUnitizacao) {
		return getEventoDispositivoUnitizacaoDAO().findIdsByIdDispositivo(idDispositivoUnitizacao);
	}

	/**
	 * Método que retorna a maior dhEvento da tabela de EventoDispositivoUnitizacao com
	 * o ID do DispositivoUnitizacao e IDs de LocalizacaoMercadoria
	 *
	 * @param idDispositivoUnitizacao
	 * @param idsLocalizacaoMercadoria
	 * @return
	 */
	public DateTime findMaiorDhEventoByIdDispositivoUnitizacaoByIdsLocalizacaoMercadoria(Long idDispositivoUnitizacao, List<Long> idsLocalizacaoMercadoria) {
		return this.getEventoDispositivoUnitizacaoDAO().findMaiorDhEventoByIdDispositivoByIdsLocalizacaoMercadoria(idDispositivoUnitizacao, idsLocalizacaoMercadoria);
	}

	/**
	 *
	 * Ver getEventoDispositivoUnitizacaoDAO().findEventosDispositivoUnitizacaoNaoCancelados(Long idDispositivo)
	 *
	 * @param idDispositivo
	 * @return
	 */
	public List<EventoDispositivoUnitizacao> findEventosDispositivoUnitizacaoNaoCancelados(Long idDispositivo) {
		return getEventoDispositivoUnitizacaoDAO().findEventosDispositivoNaoCancelados(idDispositivo);
	}

	public ResultSetPage<Map<String, Object>> findPaginatedEventosByIdDispositivoUnitizacao(Long idDispositivoUnitizacao) {
		ResultSetPage<Map<String, Object>> rs = lmEventoDao.findPaginatedEventos(idDispositivoUnitizacao);
		List<Map<String, Object>> result = rs.getList();
		for (Map<String, Object> map : result) {
			if (map.get("dhEvento") != null) {
				String dia = JTDateTimeUtils.getWeekdayName((DateTime) map.get("dhEvento"));
				map.put("dia", dia);
			}
		}
		return rs;
	}

	/**
	 * Retorna a lista de eventos não cancelados do documento informado filtrando por o código do evento
	 *
	 * @param idDispositivoUnitizacao
	 * @param cdEvento
	 * @return
	 */
	public List<EventoDispositivoUnitizacao> findEventoDispositivoUnitizacao(Long idDispositivoUnitizacao, Short[] cdEvento) {
		return getEventoDispositivoUnitizacaoDAO().findEventoDispositivoUnitizacao(idDispositivoUnitizacao, cdEvento);
	}

	//####################### *** MÉTODO PARA INTEGRAÇÃO *** ###################################//

	/**
	 *
	 * Ver getEventoDispositivoUnitizacaoDAO().findEventoDispositivoUnitizacao(nrDocumento, cdEvento, idFilial, dhEvento)
	 *
	 * @param nrDocumento
	 * @param cdEvento
	 * @param idFilial
	 * @param dhEvento
	 *
	 */
	public EventoDispositivoUnitizacao findEventoDispositivoUnitizacao(String nrDocumento, Short cdEvento, Long idFilial, DateTime dhEvento) {
		return getEventoDispositivoUnitizacaoDAO().findEventoDispositivoUnitizacao(nrDocumento, cdEvento, idFilial, dhEvento);
	}

	/**
	 * Valida se o DispositivoUnitizacao possui código do evento igual a '21'.
	 * Nesses casos não permitir bloquear / liberar.
	 *
	 * @param idDispositivoUnitizacao
	 */
	public void validateDispositivoUnitizacao(Long idDispositivoUnitizacao) {
		if (!SessionUtils.isFilialSessaoMatriz()) {
			DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findById(idDispositivoUnitizacao);
			Long idFilialLocalizacao = null;
			if (dispositivo.getLocalizacaoFilial() != null) {
				idFilialLocalizacao = dispositivo.getLocalizacaoFilial().getIdFilial();
			}
			Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
			if (idFilialLocalizacao == null || CompareUtils.ne(idFilialLocalizacao, idFilialUsuario)) {
				throw new BusinessException("LMS-45025");
			}
			//LMS-45025 O dispositivo unitização informado não se encontra nesta filial.
		}

		List<EventoDispositivoUnitizacao> list21 = findEventoDispositivoUnitizacao(idDispositivoUnitizacao, CD_EVENTO);
		if (!list21.isEmpty()) {
			throw new BusinessException("LMS-45027");
			//LMS-45027 Dispositivo unitização já entregue.
		}
	}

	public ResultSetPage<EventoDispositivoUnitizacao> findPaginatedByIdDispositivo(Long id, FindDefinition findDefinition) {
		return getEventoDispositivoUnitizacaoDAO().findPaginatedByIdDispositivo(id, findDefinition);
	}

	/**
	 * @param idDispositivoUnitizacao
	 * @param blOrigemCancelamentoRIM
	 * @return
	 */
	public EventoDispositivoUnitizacao findEventoDispositivoUnitizacaoByLastDhEventoByIdDispositivoUnitizacao(Long idDispositivoUnitizacao, Boolean blOrigemCancelamentoRIM) {
		return getEventoDispositivoUnitizacaoDAO().findEventoDispositivoUnitizacaoByLastDhEventoByIdDispositivo(idDispositivoUnitizacao, blOrigemCancelamentoRIM);
	}

	public ResultSetPage<EventoDispositivoUnitizacao> findPaginated(PaginatedQuery paginatedQuery) {
		return getEventoDispositivoUnitizacaoDAO().findPaginated(paginatedQuery);
	}

	public Integer getRowCount(Map criteria) {
		return getEventoDispositivoUnitizacaoDAO().getRowCount(criteria);
	}

	public ResultSetPage<Map<String, Object>> findPaginatedMap(PaginatedQuery paginatedQuery) {
		ResultSetPage rsp = this.findPaginated(paginatedQuery);

		List<EventoDispositivoUnitizacao> list = rsp.getList();
		List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>(list.size());

		for (EventoDispositivoUnitizacao eventoDispositivo : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idEventoDispositivoUnitizacao", eventoDispositivo.getIdEventoDispositivoUnitizacao());
			map.put("dhInclusao", eventoDispositivo.getDhInclusao());
			map.put("cdEvento", eventoDispositivo.getEvento().getCdEvento());
			map.put("dsEvento", eventoDispositivo.getEvento().getDsEvento());
			map.put("obComplemento", eventoDispositivo.getObComplemento());
			map.put("tpScan", eventoDispositivo.getTpScan().getDescription());
			map.put("sgFilial", eventoDispositivo.getFilial().getSgFilial());
			map.put("nmUsuario", eventoDispositivo.getUsuario().getNmUsuario());

			retorno.add(map);
		}

		rsp.setList(retorno);
		return rsp;
	}

	public void generateEventoByManifesto(Long idManifesto, Short cdEvento, String tpScan, String obComplemento) {
		/* Gera o evento de cancelamento para os dispositivos do manifesto */
		List<DispositivoUnitizacao> dispositivos = dispositivoUnitizacaoService.findByIdManifesto(idManifesto);
		for (DispositivoUnitizacao dispositivo : dispositivos) {
			this.generateEventoDispositivo(dispositivo, cdEvento, tpScan, obComplemento);
		}
	}

	/**
	 * Método que gera o evento a partir do cancelamento de Manifesto
	 *
	 * @param idManifesto
	 * @param cdEvento
	 * @param dsObservacao
	 * @param tpScan
	 * @param comAproveitamento - Se o cancelamento foi no Modo Com Aproveitamento
	 */
	public void generateEventoCancelamentoManifestoByManifesto(Long idManifesto, Short cdEvento, String dsObservacao, String tpScan, boolean comAproveitamento) {
		/* Gera o evento de cancelamento para os dispositivos do manifesto */
		List<DispositivoUnitizacao> dispositivos = dispositivoUnitizacaoService.findByIdManifesto(idManifesto);
		for (DispositivoUnitizacao dispositivo : dispositivos) {
			if (comAproveitamento && dispositivo.getLocalizacaoMercadoria() != null && dispositivo.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().intValue() == ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA.intValue()) {
				this.generateEventoDispositivo(dispositivo, Short.valueOf("101"), tpScan, null);
			}
			this.generateEventoDispositivo(dispositivo, cdEvento, tpScan, dsObservacao);
		}
	}


	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param dao Instância do DAO.
	 */
	public void setEventoDispositivoUnitizacaoDAO(EventoDispositivoUnitizacaoDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private EventoDispositivoUnitizacaoDAO getEventoDispositivoUnitizacaoDAO() {
		return (EventoDispositivoUnitizacaoDAO) getDao();
	}

	public void setLmEventoDao(LMEventoDAO lmEventoDao) {
		this.lmEventoDao = lmEventoDao;
	}

	public void setDispositivoUnitizacaoService(
			DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}
}