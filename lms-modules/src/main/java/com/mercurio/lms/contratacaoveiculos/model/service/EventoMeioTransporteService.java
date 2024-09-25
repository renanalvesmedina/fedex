package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.dao.EventoMeioTransporteDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.eventoMeioTransporteService"
 */
public class EventoMeioTransporteService extends CrudService<EventoMeioTransporte, Long> {
	private final static List tpMeioTransporte = new ArrayList();
	private final static List tpMeioTransporteControleCarga = new ArrayList();
	private final static List tpMeioTransporteBox = new ArrayList();

	static {
		tpMeioTransporte.add("EMDE");
		tpMeioTransporte.add("AGDE");
		tpMeioTransporte.add("EMCE");
		tpMeioTransporte.add("AGSA");
		tpMeioTransporte.add("EMCA");

		tpMeioTransporte.add("ALCE");
		tpMeioTransporte.add("ALVI");
		tpMeioTransporte.add("ADCE");
		tpMeioTransporte.add("ADFR");
		tpMeioTransporte.add("EADT");

		tpMeioTransporteControleCarga.add("EADT");
		tpMeioTransporteControleCarga.add("EMDE");
		tpMeioTransporteControleCarga.add("AGDE");
		tpMeioTransporteControleCarga.add("EMCE");
		tpMeioTransporteControleCarga.add("EMPA");
		tpMeioTransporteControleCarga.add("AGSA");
		tpMeioTransporteControleCarga.add("EMCA");

		tpMeioTransporteBox.add("EMDE");
		tpMeioTransporteBox.add("EMCA");
	}

	private ConfiguracoesFacade configuracoesFacade;
	
	/**
	 * Recupera uma inst�ncia de <code>EventoMeioTransporte</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public EventoMeioTransporte findById(java.lang.Long id) {
		return (EventoMeioTransporte)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * M�todo que verifica se o bean fornecido possui os campos obrigat�rios preenchidos,
	 * verifica as regras de neg�cio (Datas preenchidas e campos de acordo com o 
	 * dominio DM_EVENTO_MEIO_TRANSPORTE)
	 * 
	 * @param bean
	 */
	public void generateEvent(EventoMeioTransporte bean){
		DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();
		generateEvent(bean, dataHora);
	}

	/**
	 * Chamada alterada para ficar de acordo com a solicita��o da integra��o CQPRO00005473.
	 * Caso n�o seja uma necessidade da Integra��o, utilizar o m�todo generateEvent(EventoMeioTransporte bean)
	 * M�todo que verifica se o bean fornecido possui os campos obrigat�rios preenchidos,
	 * verifica as regras de neg�cio (Datas preenchidas e campos de acordo com o 
	 * dominio DM_EVENTO_MEIO_TRANSPORTE)
	 * 
	 * @param bean
	 * @param dataHora = Solicita��o da integra��o CQPRO00005473 faz com que seja necess�rio passar a data/hora por par�metro.
	 */
	public void generateEvent(EventoMeioTransporte bean, DateTime dataHora) {
		generateEvent(bean, dataHora, true);
	}

	public List<EventoMeioTransporte> generateEvent(EventoMeioTransporte bean, DateTime dataHora, boolean store) {
		List<EventoMeioTransporte> beans = new ArrayList<EventoMeioTransporte>();
		beans.add(bean);

		// FIXME Ap�s a integra��o, m�todo pode voltar a sua forma original,
		// removendo o par�metro dataHora e buscando a data/hora atual
		// diretamente no c�digo.
		MeioTransporte meioTransporte = bean.getMeioTransporte();
		DomainValue tpSituacaoMeioTransporte = bean.getTpSituacaoMeioTransporte();
		if (meioTransporte == null || tpSituacaoMeioTransporte == null)
			throw new BusinessException("LMS-00010");

		if (tpMeioTransporte.contains(tpSituacaoMeioTransporte.getValue()) && bean.getFilial().getIdFilial() == null) {
			throw new BusinessException("LMS-26013");
		}

		if (tpMeioTransporteControleCarga.contains(tpSituacaoMeioTransporte.getValue())
				&& (bean.getControleCarga() == null || bean.getControleCarga().getIdControleCarga() == null)) {
			throw new BusinessException("LMS-26029");
		}

		if (tpSituacaoMeioTransporte.getValue().equals("MELO") && StringUtils.isBlank(bean.getDsLocalManutencao())) {
			throw new BusinessException("LMS-26014");
		}
		if (bean.getDhInicioEvento() == null) {
			bean.setDhInicioEvento(dataHora);
		}

		if (bean.getDhGeracao() == null) {
			bean.setDhGeracao(dataHora);
		}

		CollectionUtils.addIgnoreNull(
				beans, verificaUltimoEvento(bean.getDhInicioEvento(), meioTransporte.getIdMeioTransporte(), store));

		if (store) {
			store(bean);
		}
		return beans;
	}

	/**
	 * Valida �ltimo registro da tabela EventoMeioTransporte.
	 * Se �ltimo registro possuir dhFimEvento nulo e dhInicioEvento menor que a novaDataInicio recebida,
	 * � atualizado a dhFimEvento do registro encontrado.
	 * 
	 * @author Felipe Ferreira
	 * @param novaDataInicio
	 */
	private void verificaUltimoEvento(DateTime novaDataInicio, Long idMeioTransporte) {
		verificaUltimoEvento(novaDataInicio, idMeioTransporte, true);
	}

	public EventoMeioTransporte verificaUltimoEvento(DateTime novaDataInicio, Long idMeioTransporte, boolean store) {
		EventoMeioTransporte eventoMeioTransporte = getEventoMeioTransporteDAO().findUltimoEventoVigenteComFinalNulo(idMeioTransporte);
		if (eventoMeioTransporte != null) {
			if (novaDataInicio.isAfter(eventoMeioTransporte.getDhInicioEvento())) {
				eventoMeioTransporte.setDhFimEvento(novaDataInicio);
				if (store) {
					super.store(eventoMeioTransporte);
				}
			}
			return eventoMeioTransporte;
		}
		return null;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(EventoMeioTransporte bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setEventoMeioTransporteDAO(EventoMeioTransporteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private EventoMeioTransporteDAO getEventoMeioTransporteDAO() {
		return (EventoMeioTransporteDAO) getDao();
	}

	/**
	 * Encontra campos e concatena info��es para visualiza��o na grid.
	 * 
	 * @param criteria
	 * @return lista com eventos encontrados de acordo com o criteria.
	 */
	public ResultSetPage findEventosPaginated(TypedFlatMap criteria){
		ResultSetPage rsp = getEventoMeioTransporteDAO().findEventosPaginated(criteria,FindDefinition.createFindDefinition(criteria));

		FilterResultSetPage f = new FilterResultSetPage(rsp) {

			public Map filterItem(Object item) {
				EventoMeioTransporte evt = (EventoMeioTransporte)item;
				Map mapEvento = new HashMap();

				MeioTransporte meioTransporte = evt.getMeioTransporte();
				mapEvento.put("meioTransporte_nrFrota",meioTransporte.getNrFrota());
				mapEvento.put("meioTransporte_nrIdentificador",meioTransporte.getNrIdentificador());

				mapEvento.put("dhInicioEvento",evt.getDhInicioEvento());
				mapEvento.put("dhFimEvento",evt.getDhFimEvento());

				String dsEvento = determinaDescEvento(evt);
				mapEvento.put("descEvento",dsEvento);

				return mapEvento;
			}

		};
		return (ResultSetPage)f.doFilter();
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getEventoMeioTransporteDAO().getRowCountCustom(criteria);
	}

	public String determinaDescEvento(EventoMeioTransporte evt){
		String tpSituacaoMeioTransporte = evt.getTpSituacaoMeioTransporte().getValue();
		List argumentos = new ArrayList();

		Filial filial = evt.getFilial();

		if (tpSituacaoMeioTransporte.equals("EVPA")){			
			ControleTrecho controleTrecho = evt.getControleTrecho();
			if(controleTrecho != null) {				
				argumentos.add(controleTrecho.getFilialByIdFilialOrigem().getSgFilial());
				String filialDestino = "";
				if (controleTrecho.getFilialByIdFilialDestino() != null) {
					filialDestino = controleTrecho.getFilialByIdFilialDestino().getSgFilial();
				}
				argumentos.add(filialDestino);
			}
		} else if (tpSituacaoMeioTransporte.equals("MELO")){
			argumentos.add(evt.getDsLocalManutencao());
		} else if (tpSituacaoMeioTransporte.equals("MMTZ")){
			return configuracoesFacade.getMensagem("eventoMeioTransporte" + tpSituacaoMeioTransporte);
		} else {
			// PAOP, EMDE, AGDE, EMCE, ADFR, AGSA, AGDE, EMCA, ALVI, ALCE, ADCE, AGDE, EADT
			argumentos.add(filial.getSgFilial() + " - " + filial.getPessoa().getNmFantasia());
		}

		return configuracoesFacade.getMensagem("eventoMeioTransporte" + tpSituacaoMeioTransporte,argumentos.toArray());
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	 /**
	 * Retorna o evento de um meio de transporte para um carragamento especifico.
	 * 
	 * @param idMeioTransporte
	 * @param idFilial
	 * @param idControleCarga
	 * @param tpSituacaoMeioTransporte
	 * @return
	 */
	public EventoMeioTransporte findLastEventoMeioTransporteToMeioTransporte(
		Long idMeioTransporte,
		Long idFilial,
		Long idControleCarga,
		String tpSituacaoMeioTransporte
	) {
		return this.getEventoMeioTransporteDAO().findLastEventoMeioTransporteToMeioTransporte(
			idMeioTransporte,
			idFilial,
			idControleCarga, 
			tpSituacaoMeioTransporte
		);
	}
	
	
	/**
     * Retorna o �ltimo evento registrado.
     * 
     * @param idMeioTransporte
     * @param idControleCarga
     * @return
     */
    public EventoMeioTransporte findLastEventoMeioTransporte(Long idMeioTransporte, Long idControleCarga){
    	return getEventoMeioTransporteDAO().findLastEventoMeioTransporte(idMeioTransporte, idControleCarga);
    }

	/**
     * Retorna o �ltimo evento registrado.
     * 
     * @param idMeioTransporte
     * @return
     */
    public EventoMeioTransporte findLastEventoMeioTransporteByMeioTransporte(Long idMeioTransporte){
    	return getEventoMeioTransporteDAO().findLastEventoMeioTransporteByMeioTransporte(idMeioTransporte);
    }

	/**
	 * Busca um Evento de Meio de Transporte a partir dos par�metros informados.
     * M�todo utilizado pela Integra��o
	 * @author Felipe Ferreira
	 * 
	 * @param idMeioTransporte Identificador do Meio de transporte.
	 * @param tpSituacaoMeioTransporte Dom�nio da situa��o do meio de transporte.
	 * @param dhInicioEvento Data/Hora de in�cio do evento.
	 * @return uma inst�ncia de EventoMeioTransporte caso encontrado. Sen�o, retora null.
	 */
	public EventoMeioTransporte findEventoMeioTransporte(
		Long idMeioTransporte,
		String tpSituacaoMeioTransporte,
		DateTime dhInicioEvento
	) {
		return getEventoMeioTransporteDAO().findEventoMeioTransporte(idMeioTransporte, tpSituacaoMeioTransporte, dhInicioEvento);
	}

	public EventoMeioTransporte getEventoMeioTransporte(Long idMeioTransporte,
													String tpSituacaoMeioTransporte,
													DateTime dhInicioEvento){
		return getEventoMeioTransporteDAO().getEventoMeioTransporte(idMeioTransporte, tpSituacaoMeioTransporte, dhInicioEvento);
	}

	public ResultSetPage<EventoMeioTransporte> findPaginatedByIdMeioTransporte(Long id, FindDefinition findDefinition) {
		return getEventoMeioTransporteDAO().findPaginatedByIdMeioTransporte(id, findDefinition);
	}


}