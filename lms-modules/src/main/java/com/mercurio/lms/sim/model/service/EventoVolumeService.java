package com.mercurio.lms.sim.model.service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.lms.carregamento.model.HistoricoVolume;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.entrega.model.NotaFiscalOperada;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.gm.model.dao.HistoricoVolumeDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.dao.EventoVolumeDAO;
import com.mercurio.lms.sim.model.dao.LMEventoDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.sim.eventoVolumeService"
 */
public class EventoVolumeService extends CrudService<EventoVolume, Long> {

	private static final String EVENTO_VOLUME = "EVENTO_VOLUME";
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private LMEventoDAO lmEventoDao;
	private ParametroGeralService parametroGeralService;
	private EventoService eventoService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private HistoricoVolumeDAO historicoVolumeDao;
	private UsuarioService usuarioService;
	private HistoricoFilialService historicoFilialService;
	private NotaFiscalOperadaService notaFiscalOperadaService;
	private IntegracaoJwtService integracaoJwtService;

	private static final String MATRIZ = "MA";

	/**
	 * @param idVolumeNotaFiscal
	 * @param cdEvento
	 * @param tpEvento
	 * @param blEventoCancelado
	 * @return List<EventoVolume>
	 */
	public List<EventoVolume> findEventoVolumeNotaFiscal(Long idVolumeNotaFiscal, Short cdEvento, DomainValue tpEvento,
														 Boolean blEventoCancelado) {
		return getEventoVolumeDAO().findEventoVolume(idVolumeNotaFiscal, cdEvento, tpEvento, blEventoCancelado);
	}

	public ResultSetPage<EventoVolume> findPaginatedByIdVolume(Long idVolumeNotaFiscal, FindDefinition findDefinition) {
		return getEventoVolumeDAO().findPaginatedByIdVolume(idVolumeNotaFiscal, findDefinition);
	}

	/**
	 * Procura evento(s) no volumeNotaFiscal informado que seja igual ao código
	 * do evento passado por parâmetro.
	 *
	 * @param idVolumeNotaFiscal
	 * @param cdEvento
	 * @return
	 */
	public List<EventoVolume> findEventoVolumeNotaFiscal(Long idVolumeNotaFiscal, Short cdEvento) {
		return getEventoVolumeDAO().findEventoVolume(idVolumeNotaFiscal, cdEvento);
	}

	/**
	 * Procura evento(s) no volumeNotaFiscal informado que seja igual ao código
	 * e a filial do evento informado. passado por parâmetro.
	 *
	 * @param idVolumeNotaFiscal
	 * @param idFilialEvento
	 * @param cdEvento
	 * @return
	 */
	public List<EventoVolume> findEventoVolumeNotaFiscal(Long idVolumeNotaFiscal, Long idFilialEvento, Short[] cdEvento) {
		return getEventoVolumeDAO().findEventoVolume(idVolumeNotaFiscal, idFilialEvento, cdEvento);
	}

	/**
	 * Retorna o último Evento cadastrado para o Documento de Servico
	 *
	 * @param idVolumeNotaFiscal
	 * @return
	 */
	public EventoVolume findUltimoEventoVolumeNotaFiscal(Long idVolumeNotaFiscal) {
		return getEventoVolumeDAO().findUltimoEventoVolume(idVolumeNotaFiscal, null, null);
	}

	public EventoVolume findUltimoEventoVolumeNotaFiscal(Long idVolumeNotaFiscal, String tpEvento,
														 Boolean blEventoCancelado) {
		return getEventoVolumeDAO().findUltimoEventoVolume(idVolumeNotaFiscal, tpEvento, blEventoCancelado);
	}

	public boolean hasEventoVolumeDescarregado(Long idVolumeNotaFiscal) {
		List<EventoVolume> eventos = findEventoVolumeNotaFiscal(idVolumeNotaFiscal, ConstantesSim.EVENTO_DESCARGA_CONCLUIDA);
		return (eventos != null) ? !eventos.isEmpty() : false;
	}

	public boolean hasEventoVolumeFimDescarga(Long idVolumeNotaFiscal) {
		List<EventoVolume> eventos = findEventoVolumeNotaFiscal(idVolumeNotaFiscal, ConstantesSim.EVENTO_FIM_DESCARGA);
		return (eventos != null) ? !eventos.isEmpty() : false;
	}

	public void removeByIdVolumeNotaFiscal(Long idVolumeNotaFiscal) {
		getEventoVolumeDAO().removeByIdVolume(idVolumeNotaFiscal);
	}

	/**
	 * Recupera uma instância de <code>EventoVolume</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public EventoVolume findById(java.lang.Long id) {
		return (EventoVolume) super.findById(id);
	}

	/**
	 * Verifica se o evento informado foi lancado no documento de servico
	 *
	 * @param idEvento
	 * @return
	 */
	public List<EventoVolume> findByEventoByVolume(Long idEvento, Long idVolume) {
		return getEventoVolumeDAO().findByEventoByVolume(idEvento, idVolume);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(EventoVolume bean) {
		return super.store(bean);
	}

	/**
	 *
	 * @return
	 */
	public EventoVolumeGenerator newEventoVolumeGenerator() {
		return EventoVolumeGenerator.newEventoVolumeGenerator(this);
	}

	/**
	 *
	 * @param idVolumeNotaFiscal
	 * @return
	 */
	public VolumeNotaFiscal findVolumeNotaFiscal(Long idVolumeNotaFiscal) {
		return getDao().get(VolumeNotaFiscal.class, idVolumeNotaFiscal);
	}

	/**
	 *
	 * @param nmParametroGeral
	 * @return
	 */
	public String findParametroGeral(String nmParametroGeral) {
		ParametroGeral parametroGeral = parametroGeralService.findByNomeParametro(nmParametroGeral, false);
		return parametroGeral != null ? parametroGeral.getDsConteudo() : null;
	}

	/**
	 *
	 * @param cdEvento
	 * @return
	 */
	public Evento findEvento(Short cdEvento) {
		Evento evento = eventoService.findByCdEvento(cdEvento);
		if (evento == null) {
			throw new BusinessException("LMS-45015", new Object[]{cdEvento});
		}
		return evento;
	}

	@Deprecated
	public Serializable generateEventoVolume(Long idVolumeNotaFiscal, Short cdEvento, String tpScan) {
		return this.generateEventoVolume(idVolumeNotaFiscal, cdEvento, tpScan, null);
	}

	@Deprecated
	public Serializable generateEventoVolume(Long idVolumeNotaFiscal, Short cdEvento, String tpScan,
											 String obComplemento) {
		VolumeNotaFiscal volume = volumeNotaFiscalService.findById(idVolumeNotaFiscal);
		return this.generateEventoVolume(volume, cdEvento, tpScan, obComplemento);
	}

	@Deprecated
	public Serializable generateEventoVolume(VolumeNotaFiscal volume, Short cdEvento, String tpScan) {
		return this.generateEventoVolume(volume, cdEvento, tpScan, null);
	}

	@Deprecated
	public java.io.Serializable generateEventoVolume(Long idVolumeNotaFiscal, String parametroGeral, String tpScan) {
		return generateEventoVolume(idVolumeNotaFiscal, parametroGeral, tpScan, null);
	}

	// LMS 428 - Volume Unitizado
	public java.io.Serializable generateEventoVolumeUnitizado(VolumeNotaFiscal volumeNotaFiscal, String parametroGeral, String tpScan) {
		return generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), parametroGeral, tpScan,
				volumeNotaFiscal.getDispositivoUnitizacao().getTipoDispositivoUnitizacao() != null ? volumeNotaFiscal.getDispositivoUnitizacao().getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().getValue() : null);
	}

	@Deprecated
	public java.io.Serializable generateEventoVolume(Long idVolumeNotaFiscal, String parametroGeral, String tpScan,
													 String obComplemento) {
		String cdEvento = getParametroGeral(parametroGeral);
		if (cdEvento == null || "".equals(cdEvento)) {
			throw new BusinessException("LMS-45016", new Object[]{cdEvento});
		}
		return generateEventoVolume(idVolumeNotaFiscal, Short.valueOf(cdEvento), tpScan, obComplemento);
	}
	
	public void generateEventoVolumeUnitizacao (VolumeNotaFiscal volumeNotaFiscal, String parametroGeral, String tpScan) {
		String cdEvento = getParametroGeral(parametroGeral);
		if (cdEvento == null || "".equals(cdEvento)) {
			throw new BusinessException("LMS-45016", new Object[]{cdEvento});
		}		
		this.generateEventoVolumeUnitizacaoPA(volumeNotaFiscal, Short.valueOf(cdEvento), tpScan);
	}
	
	public void generateEventoVolumeUnitizacaoPA (VolumeNotaFiscal volumeNotaFiscal, Short cdEvento, String tpScan) { 	
		
		Evento evento = eventoService.findByCdEvento(cdEvento);

		if (evento == null) {
			throw new BusinessException("LMS-45015", new Object[]{cdEvento});
		}
		
		LocalizacaoMercadoria localizacaoMercadoria = evento.getLocalizacaoMercadoria();
		
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		
		if(localizacaoMercadoria != null) {
			setLocalizacaoMercadoriaAndLocalizacaoFilial(filialUsuarioLogado, localizacaoMercadoria, volumeNotaFiscal);
			volumeNotaFiscalService.updateLocalizacaoELocalizacaoFilial(volumeNotaFiscal.getIdVolumeNotaFiscal(), volumeNotaFiscal.getLocalizacaoMercadoria(), volumeNotaFiscal.getLocalizacaoFilial());
		}		
		
		EventoVolume eventoVolume = getNewEventoVolume(volumeNotaFiscal, tpScan, null, null, filialUsuarioLogado, JTDateTimeUtils.getDataHoraAtual(), evento, SessionUtils.getUsuarioLogado());		
		super.store(eventoVolume);
	}

	@Deprecated
	public Serializable generateEventoVolume(VolumeNotaFiscal volume, Short cdEvento, String tpScan,
											 String obComplemento) {
		return this.storeEventoVolume(volume, cdEvento, tpScan, obComplemento, null, null);
	}

	@Deprecated
	public Serializable generateEventoVolume(VolumeNotaFiscal volume, Short cdEvento, String tpScan,
											 String obComplemento, OcorrenciaEntrega ocorrenciaEntrega, DateTime dhOcorrencia) {
		return this.storeEventoVolume(volume, cdEvento, tpScan, obComplemento, ocorrenciaEntrega, dhOcorrencia);
	}

	@Deprecated
	//LMS-3569 - chamada ajustada para incluir a filial + DH do evento
	public Serializable generateEventoVolume(VolumeNotaFiscal volume, Short cdEvento, String tpScan,
											 String obComplemento, OcorrenciaEntrega ocorrenciaEntrega, Filial filial, DateTime dhOcorrencia, Usuario usuario) {
		return this.storeEventoVolume(volume, cdEvento, tpScan, obComplemento, ocorrenciaEntrega, filial, dhOcorrencia, usuario);
	}

	@Deprecated
	public Serializable storeEventoVolume(VolumeNotaFiscal volume, Short cdEvento, String tpScan,
										  String obComplemento, OcorrenciaEntrega ocorrenciaEntrega, DateTime dhOcorrencia) {
		return storeEventoVolume(volume, cdEvento, tpScan, obComplemento, ocorrenciaEntrega, null, dhOcorrencia, SessionUtils.getUsuarioLogado());
	}

	@Deprecated
	public void storeEventoVolume(List<VolumeNotaFiscal> volumeNotaFiscalList, Short cdEvento, String tpScan) {
		storeEventoVolume(volumeNotaFiscalList, cdEvento, tpScan, null, SessionUtils.getUsuarioLogado());
	}


	public void storeEventoVolume(List<VolumeNotaFiscal> volumeNotaFiscalList, Short cdEvento, String tpScan, Long idFilial, Usuario usuario) {
		storeEventoVolumeDhOcorrencia(volumeNotaFiscalList, cdEvento, tpScan, idFilial, usuario, JTDateTimeUtils.getDataHoraAtual());
	}

	public void storeEventoVolumeDhOcorrencia(List<VolumeNotaFiscal> volumeNotaFiscalList, Short cdEvento, String tpScan, Long idFilial, Usuario usuario, DateTime dhOcorrencia) {
		Evento evento = eventoService.findByCdEvento(cdEvento);
		if (evento == null) {
			throw new BusinessException("LMS-45015", new Object[]{cdEvento});
		}

		Filial filialUsuarioLogado = idFilial == null ? SessionUtils.getFilialSessao() : this.generateNewFilial(idFilial);
		LocalizacaoMercadoria localizacaoMercadoria = evento.getLocalizacaoMercadoria();

		List<EventoVolume> eventoVolumeList = new ArrayList<EventoVolume>();
		for (VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscalList) {

			setLocalizacaoMercadoriaAndLocalizacaoFilial(filialUsuarioLogado, localizacaoMercadoria, volumeNotaFiscal);

			validaCancelaEventoVolume(evento, volumeNotaFiscal);

			EventoVolume eventoVolume = getNewEventoVolume(volumeNotaFiscal, tpScan, null, null, filialUsuarioLogado, dhOcorrencia, evento, usuario);
			eventoVolumeList.add(eventoVolume);
		}

		volumeNotaFiscalService.storeAll(volumeNotaFiscalList);

		this.storeAll(eventoVolumeList);

	}


	private Filial generateNewFilial(Long idFilial) {
		Filial filial = new Filial();
		filial.setIdFilial(idFilial);
		return filial;
	}

	public EventoVolume generateEventoVolume(
			VolumeNotaFiscal volumeNotaFiscal,
			Evento evento,
			String tpScan,
			String obComplemento,
			OcorrenciaEntrega ocorrenciaEntrega,
			Filial filial,
			Usuario usuario,
			DateTime dhOcorrencia) {
		setLocalizacaoMercadoriaAndLocalizacaoFilial(filial, evento.getLocalizacaoMercadoria(), volumeNotaFiscal);
		validaCancelaEventoVolume(evento, volumeNotaFiscal);
		return getNewEventoVolume(
				volumeNotaFiscal, tpScan, obComplemento, ocorrenciaEntrega, filial, dhOcorrencia, evento, usuario);
	}

	public EventoVolume generateEventoVolumeComBatch(
			Map<String, Object> map,
			AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

		VolumeNotaFiscal volumeNotaFiscal = (VolumeNotaFiscal) map.get("volumeNotaFiscal");
		Evento evento = (Evento) map.get("evento");
		String tpScan = (String) map.get("tpScan");
		String obComplemento = (String) map.get("obComplemento");
		OcorrenciaEntrega ocorrenciaEntrega = (OcorrenciaEntrega) map.get("ocorrenciaEntrega");
		Filial filial = (Filial) map.get("filial");
		Usuario usuario = (Usuario) map.get("usuario");
		DateTime dhOcorrencia = (DateTime) map.get("dhOcorrencia");

		setLocalizacaoMercadoriaAndLocalizacaoFilial(filial, evento.getLocalizacaoMercadoria(), volumeNotaFiscal);
		validaCancelaEventoVolumeComBatch(evento, volumeNotaFiscal, adsmNativeBatchSqlOperations);

		return getNewEventoVolume(volumeNotaFiscal, tpScan, obComplemento, ocorrenciaEntrega, filial, dhOcorrencia, evento, usuario);

	}

	public void storeEventoVolume(
			List<VolumeNotaFiscal> volumeNotaFiscalList, List<EventoVolume> eventoVolumeList) {
		volumeNotaFiscalService.storeAll(volumeNotaFiscalList);
		storeAll(eventoVolumeList);
	}

	/***
	 * Método faz mais do que diz no nome com o objetivo de minimizar impactos. Como esse refactoring foi feito devido a problemas de performance
	 * feito assim para manter a compatibilidade com storeEventoVolume(VolumeNotaFiscal volumeNotaFiscal, Short cdEvento, String tpScan,String obComplemento,
	 *   OcorrenciaEntrega ocorrenciaEntrega, Filial filialUsuarioLogado, DateTime dhOcorrencia) o qual continuará sendo utilizado e caso seja
	 *   alterado o impacto será menor
	 *
	 * @param evento
	 * @param volumeNotaFiscal
	 * @param adsmNativeBatchSqlOperations
	 */
	private void validaCancelaEventoVolumeComBatch(Evento evento, VolumeNotaFiscal volumeNotaFiscal, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
		if (evento.getCancelaEvento() != null) {
			List<EventoVolume> listEventoVolume = findByEventoByVolume(evento.getIdEvento(), volumeNotaFiscal.getIdVolumeNotaFiscal());
			if (!listEventoVolume.isEmpty()) {
				EventoVolume eventoVolumeCancelado = listEventoVolume.get(0);
				String alias = "eventoVolumeCancelado";
				if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(EVENTO_VOLUME, alias)) {
					adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
							EVENTO_VOLUME, "BL_EVENTO_CANCELADO", true, alias
					);
				}

				adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(EVENTO_VOLUME,
						eventoVolumeCancelado.getIdEventoVolume(), alias);
			}
			setLocalizacaoMercadoriaEventoVolumeNaoCanceladoComBatch(volumeNotaFiscal, listEventoVolume);
		}
	}

	/***
	 * Método faz mais do que diz no nome com o objetivo de minimizar impactos. Como esse refactoring foi feito devido a problemas de performance
	 * feito assim para manter a compatibilidade com storeEventoVolume(VolumeNotaFiscal volumeNotaFiscal, Short cdEvento, String tpScan,String obComplemento,
	 *   OcorrenciaEntrega ocorrenciaEntrega, Filial filialUsuarioLogado, DateTime dhOcorrencia) o qual continuará sendo utilizado e caso seja
	 *   alterado o impacto será menor
	 *
	 * @param evento
	 * @param volumeNotaFiscal
	 */
	private void validaCancelaEventoVolume(Evento evento, VolumeNotaFiscal volumeNotaFiscal) {

		if (evento.getCancelaEvento() != null) {
			setLocalizacaoMercadoriaEventoVolumeNaoCancelado(volumeNotaFiscal);
			EventoVolume eventoVolumeCancelado = cancelaEventoVolume(volumeNotaFiscal, evento.getCancelaEvento());

			if (eventoVolumeCancelado != null) {
				this.store(eventoVolumeCancelado);
			}


		}

	}

	public void setLocalizacaoMercadoriaAndLocalizacaoFilial(Filial filialUsuarioLogado,
															 LocalizacaoMercadoria localizacaoMercadoria, VolumeNotaFiscal volumeNotaFiscal) {
		if (localizacaoMercadoria != null) {
			volumeNotaFiscal.setLocalizacaoMercadoria(localizacaoMercadoria);
			if (filialUsuarioLogado != null) {
				setLocalizacaoFilial(volumeNotaFiscal, filialUsuarioLogado);
			}
		}
	}

	@Deprecated
	//LMS-3569 - assinatura ajustada para incluir a filial + DH do evento
	public Serializable storeEventoVolume(VolumeNotaFiscal volumeNotaFiscal, Short cdEvento, String tpScan,
										  String obComplemento, OcorrenciaEntrega ocorrenciaEntrega, Filial filialUsuarioLogado, DateTime dhOcorrencia, Usuario usuario) {
		Evento evento = eventoService.findByCdEvento(cdEvento);

//		LMS-45015 - Evento código {0} não foi encontrado.
		if (evento == null) {
			throw new BusinessException("LMS-45015", new Object[]{cdEvento});
		}

		LocalizacaoMercadoria localizacaoMercadoria = evento.getLocalizacaoMercadoria();

		//LMS-3569
		if (filialUsuarioLogado == null) {
			filialUsuarioLogado = SessionUtils.getFilialSessao();
		}

		setLocalizacaoMercadoriaAndLocalizacaoFilial(filialUsuarioLogado, localizacaoMercadoria, volumeNotaFiscal);

		validaCancelaEventoVolume(evento, volumeNotaFiscal);

		//LMS-3569
		if (dhOcorrencia == null) {
			dhOcorrencia = JTDateTimeUtils.getDataHoraAtual();
		}

		//SALVA ALTERAÇÕES NO VOLUME
		//LMS-4627 - via MWW só atualiza a localização da mercadoria e a filial
		if (ConstantesSim.TP_SCAN_FISICO.equalsIgnoreCase(tpScan) || ConstantesSim.TP_SCAN_CASCADE.equalsIgnoreCase(tpScan)) {
			volumeNotaFiscalService.updateLocalizacaoELocalizacaoFilial(volumeNotaFiscal.getIdVolumeNotaFiscal(), volumeNotaFiscal.getLocalizacaoMercadoria(), volumeNotaFiscal.getLocalizacaoFilial());
		} else {
			volumeNotaFiscalService.store(volumeNotaFiscal);
		}

		EventoVolume eventoVolume = getNewEventoVolume(volumeNotaFiscal, tpScan, obComplemento, ocorrenciaEntrega, filialUsuarioLogado, dhOcorrencia, evento, usuario);

		//SALVA NOVO EVENTO VOLUME
		Long retorno = (Long) super.store(eventoVolume);

		return retorno;
	}

	public EventoVolume getNewEventoVolume(VolumeNotaFiscal volumeNotaFiscal, String tpScan, String obComplemento,
										   OcorrenciaEntrega ocorrenciaEntrega, Filial filialUsuarioLogado, DateTime dhOcorrencia, Evento evento,
										   Usuario usuario) {

		EventoVolume eventoVolume = new EventoVolume();
		eventoVolume.setEvento(evento);
		eventoVolume.setVolumeNotaFiscal(volumeNotaFiscal);
		eventoVolume.setTpScan(new DomainValue(tpScan));
		eventoVolume.setFilial(filialUsuarioLogado);
		eventoVolume.setDhEvento(dhOcorrencia);
		eventoVolume.setDhInclusao(dhOcorrencia);
		eventoVolume.setUsuario(usuario);
		eventoVolume.setBlEventoCancelado(Boolean.FALSE);
		eventoVolume.setObComplemento(obComplemento);
		eventoVolume.setOcorrenciaEntrega(ocorrenciaEntrega);

		return eventoVolume;
	}


	private void setLocalizacaoMercadoriaEventoVolumeNaoCanceladoComBatch(VolumeNotaFiscal volumeNotaFiscal, List<EventoVolume> eventoVolumes) {

		if (eventoVolumes != null && !eventoVolumes.isEmpty() && eventoVolumes.size() > 1) {
			EventoVolume eventoVolumeNaoCanc = (EventoVolume) eventoVolumes.get(1);
			volumeNotaFiscal.setLocalizacaoMercadoria(eventoVolumeNaoCanc.getEvento().getLocalizacaoMercadoria());

			if (!getHistoricoFilialService().validateFilialUsuarioMatriz(eventoVolumeNaoCanc.getFilial().getIdFilial())) {
				volumeNotaFiscal.setLocalizacaoFilial(eventoVolumeNaoCanc.getFilial());
			}
            /* Caso o evento cancelado seja o último, seta localização para null */
		} else {
			volumeNotaFiscal.setLocalizacaoMercadoria(null);
		}
	}

	private void setLocalizacaoMercadoriaEventoVolumeNaoCancelado(VolumeNotaFiscal volumeNotaFiscal) {
		List<EventoVolume> listEventoVolumeNaoCanc = findEventosVolumeNotaFiscalNaoCancelados(volumeNotaFiscal.getIdVolumeNotaFiscal());
		if (!listEventoVolumeNaoCanc.isEmpty()) {
			EventoVolume eventoVolumeNaoCanc = (EventoVolume) listEventoVolumeNaoCanc.get(0);
			volumeNotaFiscal.setLocalizacaoMercadoria(eventoVolumeNaoCanc.getEvento().getLocalizacaoMercadoria());

			if (!getHistoricoFilialService().validateFilialUsuarioMatriz(eventoVolumeNaoCanc.getFilial().getIdFilial())) {
				volumeNotaFiscal.setLocalizacaoFilial(eventoVolumeNaoCanc.getFilial());
			}
            /* Caso o evento cancelado seja o último, seta localização para null */
		} else {
			volumeNotaFiscal.setLocalizacaoMercadoria(null);
		}
	}

	private EventoVolume cancelaEventoVolume(VolumeNotaFiscal volumeNotaFiscal, Evento cancelaEvento) {
		List<EventoVolume> listEventoVolume = findByEventoByVolume(cancelaEvento.getIdEvento(), volumeNotaFiscal.getIdVolumeNotaFiscal());

		EventoVolume eventoVolumeCancelado = null;
		if (!listEventoVolume.isEmpty()) {
			eventoVolumeCancelado = listEventoVolume.get(0);
			eventoVolumeCancelado.setBlEventoCancelado(true);
		}

		return eventoVolumeCancelado;

	}

	@Transactional
	private void setLocalizacaoFilial(VolumeNotaFiscal volumeNotaFiscal, Filial filialUsuarioLogado) {
		HistoricoFilial historicoFilial = SessionUtils.getUltimoHistoricoFilialSessao();
		if(historicoFilial == null){
			historicoFilial =  integracaoJwtService.getUltimoHistoricoFilialSessao(filialUsuarioLogado.getIdFilial());
		}
		String tpFilial = historicoFilial.getTpFilial().getValue();
		if (!tpFilial.equals(MATRIZ)) {
			volumeNotaFiscal.setLocalizacaoFilial(filialUsuarioLogado);
		}
	}

	public Serializable generateEventoVolumeSorter(VolumeNotaFiscal volume, Filial filial, DateTime dhLeituraSorter, Usuario usuarioLogado, DomainValue tpScan) {

		EventoVolume ultimoEventoVolume = findEventoVolumeNotaFiscalByLastDhEventoByIdVolumeNotaFiscal(volume.getIdVolumeNotaFiscal(), false);
		Evento evento = eventoService.findByCdEvento(Short.valueOf("30"));

		EventoVolume eventoGerado = new EventoVolume();
		eventoGerado.setBlEventoCancelado(Boolean.FALSE);

		eventoGerado.setVolumeNotaFiscal(volume);
		eventoGerado.setDhEvento(dhLeituraSorter);
		eventoGerado.setDhInclusao(dhLeituraSorter);

		eventoGerado.setFilial(filial);
		eventoGerado.setTpScan(tpScan);
		eventoGerado.setUsuario(usuarioLogado);
		eventoGerado.setEvento(evento);

		Long idEventoGerado = (Long) store(eventoGerado);

		if (ultimoEventoVolume == null || eventoGerado.getDhEvento().isAfter(ultimoEventoVolume.getDhEvento())) {
			volumeNotaFiscalService.updateLocalizacaoELocalizacaoFilial(volume.getIdVolumeNotaFiscal(), evento.getLocalizacaoMercadoria(), filial);
		}

		return idEventoGerado;
	}

	private String getParametroGeral(String nmParam) {
		ParametroGeral param = parametroGeralService.findByNomeParametro(nmParam, Boolean.FALSE);
		if (param != null) {
			return param.getDsConteudo();
		} else {
			return null;
		}
	}

	public List<Long> findIdsByIdVolumeNotaFiscal(Long idVolumeNotaFiscal) {
		return getEventoVolumeDAO().findIdsByIdVolume(idVolumeNotaFiscal);
	}

	/**
	 * Método que retorna a maior dhEvento da tabela de EventoVolume com o ID do
	 * VolumeNotaFiscal e IDs de LocalizacaoMercadoria
	 *
	 * @param idVolumeNotaFiscal
	 * @param idsLocalizacaoMercadoria
	 * @return
	 */
	public DateTime findMaiorDhEventoByIdVolumeNotaFiscalByIdsLocalizacaoMercadoria(Long idVolumeNotaFiscal,
																					List<Long> idsLocalizacaoMercadoria) {
		return this.getEventoVolumeDAO().findMaiorDhEventoByIdVolumeByIdsLocalizacaoMercadoria(idVolumeNotaFiscal,
				idsLocalizacaoMercadoria);
	}

	/**
	 * @author Andresa Vargas
	 * @param idVolume
	 * @return
	 */
	public List<EventoVolume> findEventosVolumeNotaFiscalNaoCancelados(Long idVolume) {
		return getEventoVolumeDAO().findEventosVolumeNaoCancelados(idVolume);
	}

	public ResultSetPage<Map<String, Object>> findPaginatedEventosByIdVolumeNotaFiscal(Long idVolumeNotaFiscal) {
		ResultSetPage<Map<String, Object>> rs = lmEventoDao.findPaginatedEventos(idVolumeNotaFiscal);
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
	 * Retorna a lista de eventos não cancelados do documento informado
	 * filtrando por o código do evento
	 *
	 * @param idVolumeNotaFiscal
	 * @param cdEvento
	 * @return
	 * @author Mickaël Jalbert
	 * @since 21/02/2007
	 */
	public List<EventoVolume> findEventoVolumeNotaFiscal(Long idVolumeNotaFiscal, Short[] cdEvento) {
		return getEventoVolumeDAO().findEventoVolume(idVolumeNotaFiscal, cdEvento);
	}

	// ####################### *** MÉTODO PARA INTEGRAÇÃO ***
	// ###################################//

	/**
	 * @param nrDocumento
	 * @param cdEvento
	 * @param idFilial
	 * @param dhEvento
	 * @return
	 */
	public EventoVolume findEventoVolume(String nrDocumento, Short cdEvento, Long idFilial, DateTime dhEvento) {
		return getEventoVolumeDAO().findEventoVolume(nrDocumento, cdEvento, idFilial, dhEvento);
	}

	/**
	 * Valida se o VolumeNotaFiscal possui código do evento igual a '21'. Nesses
	 * casos não permitir bloquear / liberar.
	 *
	 * @param idVolumeNotaFiscal
	 */
	public void validateVolumeNotaFiscal(Long idVolumeNotaFiscal) {
		if (!SessionUtils.isFilialSessaoMatriz()) {
			VolumeNotaFiscal volume = volumeNotaFiscalService.findById(idVolumeNotaFiscal);
			Long idFilialLocalizacao = null;
			if (volume.getLocalizacaoFilial() != null) {
				idFilialLocalizacao = volume.getLocalizacaoFilial().getIdFilial();
			}
			Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
			if (idFilialLocalizacao == null || CompareUtils.ne(idFilialLocalizacao, idFilialUsuario)){
				//LMS-45020 -	O volume informado não se encontra nesta filial.
				throw new BusinessException("LMS-45020");
			}
		}

		List<EventoVolume> list21 = findEventoVolumeNotaFiscal(idVolumeNotaFiscal, (short) 21);
		if (!list21.isEmpty()) {
//			LMS-45026 - Volume já entregue.
			throw new BusinessException("LMS-45026");
		}
	}

	/**
	 * @param idVolumeNotaFiscal
	 * @param blOrigemCancelamentoRIM
	 * @return
	 */
	public EventoVolume findEventoVolumeNotaFiscalByLastDhEventoByIdVolumeNotaFiscal(Long idVolumeNotaFiscal,
																					 Boolean blOrigemCancelamentoRIM) {
		return getEventoVolumeDAO().findEventoVolumeByLastDhEventoByIdVolume(idVolumeNotaFiscal,
				blOrigemCancelamentoRIM);
	}

	public EventoVolume findUltimoEventoVolumeDeDescargaByFilial(Long idVolume, Long idFilial) {
		//Busca data do último evento
		return getEventoVolumeDAO().findUltimoEventoVolumeDeDescargaByFilial(idVolume,
				idFilial);
	}

	public ResultSetPage<EventoVolume> findPaginated(PaginatedQuery paginatedQuery) {
		return getEventoVolumeDAO().findPaginated(paginatedQuery);
	}

	public Integer getRowCount(Map criteria) {
		return getEventoVolumeDAO().getRowCount(criteria);
	}

	public ResultSetPage<Map<String, Object>> findPaginatedMap(PaginatedQuery paginatedQuery) {

		return getEventoVolumeDAO().findPaginatedBySql(paginatedQuery);
	}

	public void generateEventoByManifesto(Long idManifesto, Short cdEvento, String tpScan) {
		/** Otimização LMS-804 - LMS-806 - LMS-807 */
		final ProjectionList projection = Projections.projectionList()
				.add(Projections.property("vnf.id"), "idVolumeNotaFiscal");

		final Map<String, String> alias = new HashMap<String, String>();
		alias.put("vnf.localizacaoMercadoria", "loc");

		final List<Criterion> criterion = new ArrayList<Criterion>();
		criterion.add(Restrictions.ne("loc.cdLocalizacaoMercadoria", ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA));

		final List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findByIdManifesto(idManifesto, projection, alias, criterion);

		for (VolumeNotaFiscal volume : volumes) {
			this.generateEventoVolume(volume.getIdVolumeNotaFiscal(), cdEvento, tpScan);
		}
	}

	/**
	 * Método que gera evento para volumeNotaFiscal vinculados ao Manifesto de Entrega
	 * Chamado no momento da emissão do manifesto.
	 * Gera os eventos para os dispositivos também.
	 *
	 * @param idFilial
	 * @param manifesto
	 */
	public void generateEventoEmissaoManifestoByManifesto(Long idFilial, Manifesto manifesto) {
		final ProjectionList projection = Projections.projectionList()
				.add(Projections.property("vnf.id"), "idVolumeNotaFiscal")
				.add(Projections.property("loc.cdLocalizacaoMercadoria"), "localizacaoMercadoria.cdLocalizacaoMercadoria")
				.add(Projections.property("nfc.idNotaFiscalConhecimento"), "notaFiscalConhecimento.idNotaFiscalConhecimento");
		final Map<String, String> alias = new HashMap<String, String>();
		alias.put("vnf.localizacaoMercadoria", "loc");
		alias.put("vnf.notaFiscalConhecimento", "nfc");
		Short cdEvento = null;
		String tpManifesto = manifesto.getTpManifestoEntrega().getValue();
		String complemento = null;
		if ("CR".equals(tpManifesto)) {
			cdEvento = ConstantesSim.EVENTO_MERCADORIA_SENDO_ENTREGUE;
			complemento = "Cliente Retira";
		} else if ("PR".equals(tpManifesto) || "EP".equals(tpManifesto)) {
			cdEvento = ConstantesSim.EVENTO_MANIFESTO_EMITIDO_PARCEIRA;
		} else {
			cdEvento = ConstantesSim.EVENTO_MANIFESTO_EMITIDO;
		}
		final List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findByIdManifesto(manifesto.getIdManifesto(), projection, alias, null);
		for (VolumeNotaFiscal volume : volumes) {
			
			List<NotaFiscalOperada> listNFO = notaFiscalOperadaService.findByIdNotaFiscalConhecimentoFinalizada(volume.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento());
        	if(CollectionUtils.isNotEmpty(listNFO)){
        		continue;
        	}
			
			this.generateEventoVolume(volume.getIdVolumeNotaFiscal(), cdEvento, ConstantesSim.TP_SCAN_LMS, complemento);
		}
		eventoDispositivoUnitizacaoService.generateEventoByManifesto(manifesto.getIdManifesto(), cdEvento, ConstantesSim.TP_SCAN_LMS, complemento);
	}

	/**
	 * Método que gera evento para volumeNotaFiscal para diferentes LocalizacaoMercadoria, não só para Mercadoria Entregue
	 *
	 * @param idManifesto
	 * @param tpManifestoEntrega
	 * @param cdEvento
	 * @param tpScan
	 * @param comAproveitamento
	 */
	public void generateEventoCancelamentoManifestoByManifesto(Long idManifesto, String tpManifestoEntrega, Short cdEvento, String tpScan, boolean comAproveitamento) {
		final ProjectionList projection = Projections.projectionList()
				.add(Projections.property("vnf.id"), "idVolumeNotaFiscal").add(Projections.property("loc.cdLocalizacaoMercadoria"), "localizacaoMercadoria.cdLocalizacaoMercadoria");
		final Map<String, String> alias = new HashMap<String, String>();
		alias.put("vnf.localizacaoMercadoria", "loc");

		String dsObervacao = "CR".equals(tpManifestoEntrega) ? "Cliente Retira" : "";
		final List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findByIdManifesto(idManifesto, projection, alias, null);
		for (VolumeNotaFiscal volume : volumes) {
			Short cdlocalizacaomercadoria = volume.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();

			if (comAproveitamento && cdlocalizacaomercadoria.intValue() == ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA.intValue() && "CR".equals(tpManifestoEntrega)) {
				this.generateEventoVolume(volume.getIdVolumeNotaFiscal(), Short.valueOf("101"), tpScan);
			}

			this.generateEventoVolume(volume.getIdVolumeNotaFiscal(), cdEvento, tpScan, dsObervacao);
		}
	}

	/**
	 * Cria eventos conforme o histórico do volume adquirido no carregamento in-house. Rotina: GeraEventoVolumeGM, LMS-2784
	 *
	 * @param nrIdentificacao
	 * @param nrNotaFiscal
	 */
	public void generateEventoVolumeGM(String nrIdentificacao, Integer nrNotaFiscal) {
		List<VolumeNotaFiscal> listaVolumeNotaFiscal = volumeNotaFiscalService.findVolumesNotaFiscalByNrIdentificacaoClienteENrNota(nrIdentificacao, nrNotaFiscal);

		for (VolumeNotaFiscal volumeNotaFiscal : listaVolumeNotaFiscal) {
			List<HistoricoVolume> listaHistoricoVolume = historicoVolumeDao.findHistoricosVolumesByCodigoVolume(volumeNotaFiscal.getNrVolumeColeta());

			Short cdEventoInHouse = null;
			Evento evento = null;
			for (HistoricoVolume historico : listaHistoricoVolume) {
				switch (Integer.valueOf(historico.getCodigoStatus().getValue())) {
					case 0:
						cdEventoInHouse = 300;
						break;
					case 1:
						cdEventoInHouse = 301;
						break;
					case 2:
						cdEventoInHouse = 302;
						break;
					case 3:
						cdEventoInHouse = 303;
						break;
					case 4:
						cdEventoInHouse = 304;
						break;
					case 5:
						cdEventoInHouse = 305;
						break;
					case 6:
						cdEventoInHouse = 306;
						break;
					case 7:
						cdEventoInHouse = 307;
						break;
					default:
						break;
				}

				evento = eventoService.findByCdEvento(cdEventoInHouse);

				EventoVolume eventoVolume = new EventoVolume();
				eventoVolume.setEvento(evento);
				eventoVolume.setVolumeNotaFiscal(volumeNotaFiscal);
				eventoVolume.setFilial(SessionUtils.getFilialSessao());
				eventoVolume.setBlEventoCancelado(Boolean.FALSE);
				eventoVolume.setDhEvento(historico.getDataHistorico());
				eventoVolume.setDhInclusao(new DateTime(DateTimeZone.forID("America/Sao_Paulo")));
				eventoVolume.setUsuario(usuarioService.findById(historico.getMatriculaResponsavel()));
				eventoVolume.setTpScan(new DomainValue("SF"));
				this.store(eventoVolume);

			}
		}
	}

	public Long findQuantidadeEventoVolumeByIdVolumeByIdFilialByCdEvento (Long idVolumeNotaFiscal, Long idFilial, Long idEvento) {
		return getEventoVolumeDAO().findQuantidadeEventoVolumeByIdVolumeByIdFilialByCdEvento(idVolumeNotaFiscal, idFilial, idEvento);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 *
	 * @param dao Instância do DAO.
	 */
	public void setEventoVolumeDAO(EventoVolumeDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private EventoVolumeDAO getEventoVolumeDAO() {
		return (EventoVolumeDAO) getDao();
	}

	public void setLmEventoDao(LMEventoDAO lmEventoDao) {
		this.lmEventoDao = lmEventoDao;
	}


	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}

	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}

	public void setHistoricoVolumeDao(HistoricoVolumeDAO historicoVolumeDao) {
		this.historicoVolumeDao = historicoVolumeDao;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public HistoricoFilialService getHistoricoFilialService() {
		return historicoFilialService;
	}

	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
        
	public boolean eventoVolumePossuiEventoEntrega(Long idVolumeNotaFiscal) {
		List<Short> cdEventos = getEventoVolumeDAO().findCdEventoByIdVolumeNotaFiscal(idVolumeNotaFiscal);
		return cdEventos.contains(Short.valueOf("21"));
	}

	public void executeCancelaEventoByIdEvento(Long idEventoVolume) {
		getEventoVolumeDAO().executeCancelaEventoByIdEvento(idEventoVolume);
	}

	public void setNotaFiscalOperadaService(NotaFiscalOperadaService notaFiscalOperadaService) {
		this.notaFiscalOperadaService = notaFiscalOperadaService;
	}

	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}
}
