package com.mercurio.lms.municipios.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.MeioTransporteRotaViagem;
import com.mercurio.lms.municipios.model.MotivoParadaPontoTrecho;
import com.mercurio.lms.municipios.model.MotoristaRotaViagem;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.PontoParadaTrecho;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.RotaViagem;
import com.mercurio.lms.municipios.model.ServicoRotaViagem;
import com.mercurio.lms.municipios.model.TipoMeioTranspRotaEvent;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.dao.RotaViagemDAO;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.rotaViagemService"
 */
public class RotaViagemService extends CrudService<RotaViagem, Long> {
	private Logger log = LogManager.getLogger(this.getClass());
	private FilialRotaService filialRotaService;
	private DomainService domainService;
	private MoedaPaisService moedaPaisService;
	private VigenciaService vigenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private PostoPassagemTrechoService postoPassagemTrechoService;
	private TipoMeioTransporteService tipoMeioTransporteService;	

	/**
	 * Recupera uma instância de <code>RotaViagem</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public RotaViagem findById(java.lang.Long id) {
		RotaViagem rv = (RotaViagem)super.findById(id);
		for (RotaIdaVolta riv: (List<RotaIdaVolta>)rv.getRotaIdaVoltas()){
			Hibernate.initialize(riv.getRota());
			Hibernate.initialize(riv.getRota().getFilialRotas());
		}
		return rv;
	}

	/**
	 * Método ultizado nos métodos em que é necessário uma string com a rota.
	 * @param criteria
	 */
	private void setDsRotasOnCriteria(TypedFlatMap criteria) {
		String dsRotaIda = String.valueOf("");
		List<TypedFlatMap> filiaisIda = criteria.getList("rotaIda.filiaisRota");
		if (filiaisIda != null) {
			dsRotaIda = getRotaByFiliais(filiaisIda);
		}
		String dsRotaVolta = String.valueOf("");
		List<TypedFlatMap> filiaisVolta = criteria.getList("rotaVolta.filiaisRota");
		if (filiaisVolta != null) {
			dsRotaVolta = getRotaByFiliais(filiaisVolta);
		}
		criteria.put("dsRotaIda", dsRotaIda);
		criteria.put("dsRotaVolta", dsRotaVolta);
	}

	/**
	 * FindPaginated da tela 'Manter Rotas de Viagem'
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedRotaViagem(TypedFlatMap criteria) {
		// Cria string de dsRota Ida e Volta a partir dos list's.
		setDsRotasOnCriteria(criteria);

		ResultSetPage rsp = getRotaViagemDAO().findPaginatedRotaViagem(criteria,FindDefinition.createFindDefinition(criteria));

		// Como a consulta retornou apenas as chaves dos valores de dominio, é necessário carregar os valores aqui:
		Domain dmTipoRotaViagem = this.domainService.findByName("DM_TIPO_ROTA_VIAGEM");
		Domain dmSistemaRota = this.domainService.findByName("DM_SISTEMA_ROTA");

		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		List<Object[]> list = rsp.getList();
		for(Object[] obj : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idRotaViagem",obj[0]);
			if (obj[1] != null && !"".equals((String)obj[1])) {
				DomainValue dvTipoRotaViagem = dmTipoRotaViagem.findDomainValueByValue((String)obj[1]);
				map.put("tpRota",dvTipoRotaViagem.getDescription().getValue(LocaleContextHolder.getLocale()));
			}
			if (obj[2] != null && !"".equals((String)obj[2])) {
				DomainValue dvSistemaRota = dmSistemaRota.findDomainValueByValue((String)obj[2]);
				map.put("tpSistemaRota",dvSistemaRota.getDescription().getValue(LocaleContextHolder.getLocale()));
			}
			map.put("dtVigenciaInicial", obj[3]);
			map.put("dtVigenciaFinal", obj[4]);
			map.put("dsTipoMeioTransporte", obj[5]);
			map.put("dsRotaIda", obj[6]);
			map.put("dsRotaVolta", obj[7]);
			map.put("hrSaidaIda", obj[8]);
			map.put("hrSaidaVolta", obj[9]);
			map.put("nrRotaIda", obj[10]);
			map.put("nrRotaVolta", obj[11]);
			newList.add(map);
		}

		rsp.setList(newList);
		return rsp;
	}

	public Integer getRowCountRotaViagem(TypedFlatMap criteria) {
		setDsRotasOnCriteria(criteria);
		return getRotaViagemDAO().getRowCountRotaViagem(criteria);
	}

	/**
	 * Cria lista de <b>Filial</b> a partir de uma <b>dsRota</b>.
	 * @author Felipe Ferreira 
	 * @param dsRota
	 * @return
	 */
	public List<Filial> getFiliaisByRota(String dsRota) {
		StringTokenizer filiais = new StringTokenizer(dsRota,"-");
		List<Filial> filiaisTokenizadas = new ArrayList<Filial>();
		while(filiais.hasMoreTokens()){
			Filial filial = new Filial();
			filial.setSgFilial(filiais.nextToken().toString());
			filiaisTokenizadas.add(filial);
		}
		return filiaisTokenizadas;
	}

	/**
	 * Cria uma <b>dsRota</b> a partir de uma lista de <b>Filial</b>.
	 * @author Felipe Ferreira 
	 * @param dsRota
	 * @return
	 */
	public String getRotaByFiliais(List<TypedFlatMap> filiais) {
		StringBuffer retorno = new StringBuffer();
		for(TypedFlatMap mapAux : filiais) {
			String sgFilial = mapAux.getString("filial.sgFilial");
			if (sgFilial != null)
				retorno.append(retorno.length() == 0 ? "" : "-");
				retorno.append(sgFilial);
		}
		return retorno.toString();
	}

	/**
	 * Recupera <b>nrDistância</b> e <b>obItinerario</b> de Rota viagem <b>EXPRESSA</b> a partir dos Parâmetros informados. 
	 * @param idRotaViagem parâmetro exclusivo.
	 * @param idRota
	 * @return Map com <b>nrDistância</b> e <b>obItinerario</b>.
	 */
	public Map findInfoByRotaViagem(Long idRotaViagem,Long idRota) {
		return getRotaViagemDAO().findInfoByRotaViagem(idRotaViagem,idRota);
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
	 * Apaga uma entidade através do Id e seus filhos.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeByIdComplete(java.lang.Long id) {
		validaRemoveById(id);
		getRotaViagemDAO().removeByIdComplete(id);
	}

	/**
	 * Apaga várias entidades através do Id e seus filhos.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsComplete(List ids) {
		for (Iterator<Long> i = ids.iterator() ; i.hasNext() ; )
			validaRemoveById(i.next());
		getRotaViagemDAO().removeByIdsComplete(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(RotaViagem bean) {
		return super.store(bean);
	}

	protected RotaViagem beforeStore(RotaViagem bean) {
		RotaViagem pojo = (RotaViagem)bean;
		List<RotaIdaVolta> rotasIdaVolta = pojo.getRotaIdaVoltas();

		if (rotasIdaVolta != null) {
			if (getRotaViagemDAO().validateDuplicated(
					pojo.getIdRotaViagem(),
					pojo.getDtVigenciaInicial(),
					pojo.getDtVigenciaFinal(),
					rotasIdaVolta)
			) {
				throw new BusinessException("LMS-00003");
			}
			// Valida toda RotaIdaVolta associada a RotaViagem.
			for (RotaIdaVolta rotaIdaVolta : rotasIdaVolta) {
				// Gera o número da Rota:
				if(rotaIdaVolta.getIdRotaIdaVolta() == null) {
					Integer nrRota = configuracoesFacade.incrementaParametroSequencial("NR_ROTA",true).intValue();
					// Seta o número da rota.
					rotaIdaVolta.setNrRota(nrRota);
				}
			}
		}

		return super.beforeStore(bean);
	}

	public void validateVigenciaBeforeStore(RotaViagem rv) {
		this.vigenciaService.validaVigenciaBeforeStore(rv);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable storeWithItems(
		RotaViagem bean, 
		ItemList itemsIda, 
		ItemListConfig itemsIdaConfig, 
		ItemList itemsEvent, 
		ItemListConfig itemsEventConfig, 
		ItemList itemsVolta, 
		ItemListConfig itemsVoltaConfig
	) {
		boolean masterIdIsNull = bean.getIdRotaViagem() == null;
		try {
			this.beforeStore(bean);

			Map<String, Object> mapIdsFilialRota = null;

			List<RotaIdaVolta> rotasIdaVolta = bean.getRotaIdaVoltas();
			for (RotaIdaVolta riv : rotasIdaVolta) {
				switch (riv.getTpRotaIdaVolta().getValue().charAt(0)) {
				case 'I':
					if (itemsIda.isInitialized()) {
						mapIdsFilialRota = this.getMapToGetFiliaisRota(riv.getRota().getIdRota());
						this.configureItemsTrechoRotaIdaVolta(itemsIda,itemsIdaConfig, mapIdsFilialRota,bean.getIdRotaViagem(),riv);	
					}
					break;
				case 'V':
					if (itemsVolta.isInitialized()) {
						mapIdsFilialRota = this.getMapToGetFiliaisRota(riv.getRota().getIdRota());
						configureItemsTrechoRotaIdaVolta(itemsVolta,itemsVoltaConfig, mapIdsFilialRota,bean.getIdRotaViagem(),riv);
					}
					break;
				default:
					break;
				}
			}
			getRotaViagemDAO().storeWithItems(bean,itemsIda,itemsEvent,itemsVolta);
			
		} catch (HibernateOptimisticLockingFailureException e) {
			throw new BusinessException("LMS-00012");
		} catch (RuntimeException e) {
			this.rollbackMasterState(bean, masterIdIsNull, e);

			itemsIda.rollbackItemsState();
			itemsVolta.rollbackItemsState();

			throw e;
		}

		// regra 4 incluída no códdigo em 28/12/2005: 
		if (masterIdIsNull)
			this.storeTipoMeioTranspEventToRotaViagem(bean);
		
		return bean;
	}

	private Map<String, Object> getMapToGetFiliaisRota(Long idRota) {
		List<Object[]> l = getRotaViagemDAO().findFiliaisRotaToTrechosOfRotaIdaVolta(idRota);
		Map<String, Object> retorno = new HashMap<String, Object>();
		for(Object[] data : l) {
			retorno.put(((Byte)data[2]).toString(),data[0]);
		}
		return retorno;
	}

	private void configureItemsTrechoRotaIdaVolta(
		ItemList items,
		ItemListConfig config,
		Map<String, Object> mapIdsFilialRota,
		Long idRotaViagem,
		RotaIdaVolta riv
	) {
		Byte nrOrdemOrigem = null;
		Byte nrOrdemDestino = null;
		for (Iterator<TrechoRotaIdaVolta> i = items.iterator(idRotaViagem, config) ; i.hasNext() ; ) {
			TrechoRotaIdaVolta triv = i.next();
			triv.setRotaIdaVolta(riv);
			
			FilialRota fro = triv.getFilialRotaByIdFilialRotaOrigem();
			nrOrdemOrigem = triv.getFilialRotaByIdFilialRotaOrigem().getNrOrdem();
			fro.setIdFilialRota((Long)mapIdsFilialRota.get(nrOrdemOrigem.toString()));
			triv.setFilialRotaByIdFilialRotaOrigem(fro);

			
			
			FilialRota frd = triv.getFilialRotaByIdFilialRotaDestino();
			nrOrdemDestino = triv.getFilialRotaByIdFilialRotaDestino().getNrOrdem();
			frd.setIdFilialRota((Long)mapIdsFilialRota.get(nrOrdemDestino.toString()));
			triv.setFilialRotaByIdFilialRotaDestino(frd);

			if (nrOrdemOrigem.equals((byte)1) && nrOrdemDestino.equals((byte)mapIdsFilialRota.size())) {
				triv.setNrDistancia(riv.getNrDistancia());
			}
		}
	}

	/**
	 * Retorna true se encontrar a filial informada em algum dos trechos na memória.
	 * @param masterId
	 * @param items
	 * @param idFilial
	 * @return
	 */
	public boolean verificaTrechosInSession(
		Long masterId,
		Map<String, Object> parameters, 
		ItemList items,
		ItemListConfig config,
		Long idFilial,
		Byte nrOrdem
	) {
		Byte nrOrdemFilialEncontrada = null;
		for (Iterator<TrechoRotaIdaVolta> i = items.iterator(masterId, parameters, config) ; i.hasNext() ; ) {
			TrechoRotaIdaVolta triv = i.next();

			FilialRota filialRotaByIdFilialRotaOrigem = triv.getFilialRotaByIdFilialRotaOrigem();
			if (filialRotaByIdFilialRotaOrigem.getFilial().getIdFilial().equals(idFilial)) {
				nrOrdemFilialEncontrada = filialRotaByIdFilialRotaOrigem.getNrOrdem();
				break;
			}

			FilialRota filialRotaByIdFilialRotaDestino = triv.getFilialRotaByIdFilialRotaDestino();
			if (filialRotaByIdFilialRotaDestino.getFilial().getIdFilial().equals(idFilial)) {
				nrOrdemFilialEncontrada = filialRotaByIdFilialRotaDestino.getNrOrdem();
				break;
			}
		}

		// Caso recebeu nrOrdem, é porque será excluído a filial da rota.
		// Devemos decrementar o nrOrdem dos trechos que utilizam a rota.
		if (nrOrdem != null) {
			for (Iterator<TrechoRotaIdaVolta> i = items.iterator(masterId, parameters, config) ; i.hasNext() ; ) {
				TrechoRotaIdaVolta triv = i.next();

				FilialRota filialRotaByIdFilialRotaOrigem = triv.getFilialRotaByIdFilialRotaOrigem();
				if (filialRotaByIdFilialRotaOrigem.getNrOrdem() > nrOrdem) {
					filialRotaByIdFilialRotaOrigem.setNrOrdem((byte)(filialRotaByIdFilialRotaOrigem.getNrOrdem()-1));
				}

				FilialRota filialRotaByIdFilialRotaDestino = triv.getFilialRotaByIdFilialRotaDestino();
				if (filialRotaByIdFilialRotaDestino.getNrOrdem() > nrOrdem) {
					filialRotaByIdFilialRotaDestino.setNrOrdem((byte)(filialRotaByIdFilialRotaDestino.getNrOrdem()-1));
				}
			}
		}
		return nrOrdemFilialEncontrada != null;
	}

	public boolean verificaTrechosHoraInformada(
		Long masterId,
		ItemList items,
		ItemListConfig config,
		Long idTrechoRotaIdaVolta,
		Long idFilialOrigem,
		TimeOfDay hrSaida
	) {
		boolean existeOutroTrecho = false;
		for (Iterator<TrechoRotaIdaVolta> i = items.iterator(masterId, config) ; i.hasNext() ; ) {
			TrechoRotaIdaVolta triv = i.next();
			if (CompareUtils.eq(triv.getFilialRotaByIdFilialRotaOrigem().getFilial().getIdFilial(), idFilialOrigem)
				&& CompareUtils.neNull(triv.getHrSaida(), hrSaida)
				&& CompareUtils.neNull(triv.getIdTrechoRotaIdaVolta(), idTrechoRotaIdaVolta)
			) {
				existeOutroTrecho = true;
				break;
			}
		}
		return existeOutroTrecho;
	}

	/**
	 * Salva um Tipo de Meio de Transporte Eventual para uma Rota Viagem se
	 * tpRota = EXPRESSA
	 * e para todas rotaIdaVoltas.
	 * @param bean
	 * @param riv
	 */
	public void storeTipoMeioTranspEventToRotaViagem(RotaViagem bean) {
		if (bean.getTpRota().getValue().equals("EX")) {
			List<RotaIdaVolta> rotasIdaVolta = bean.getRotaIdaVoltas();
			for(RotaIdaVolta rotaIdaVolta : rotasIdaVolta) {
				TipoMeioTranspRotaEvent tmt = new TipoMeioTranspRotaEvent();
				tmt.setRotaIdaVolta(rotaIdaVolta);
				tmt.setTipoMeioTransporte(bean.getTipoMeioTransporte());
				tmt.setVlFrete(BigDecimalUtils.ZERO);
				tmt.setDtVigenciaInicial(bean.getDtVigenciaInicial());
				tmt.setDtVigenciaFinal(bean.getDtVigenciaFinal());
				getRotaViagemDAO().store(tmt);
			}
		}
	}

	/**
	 * Retorna moeda do pais mais utilizada.
	 * @return pojo de MoedaPais
	 */
	public MoedaPais findMoedaPaisPadrao() {
		Pais p = SessionUtils.getPaisSessao();
		return moedaPaisService.findMoedaPaisMaisUtilizada(p.getIdPais());		
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setRotaViagemDAO(RotaViagemDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private RotaViagemDAO getRotaViagemDAO() {
		return (RotaViagemDAO) getDao();
	}

	public Long storeAlterarRota(Long idRotaViagem, YearMonthDay dtVigenciaInicial) {
		// Se não receber idRotaViagem e nova vigência inicial que são obrigatórios, gera exceção.
		if (idRotaViagem == null || dtVigenciaInicial == null)
			throw new BusinessException("LMS-00010");

		// Carrega antiga rota de viagem.
		RotaViagem rotaViagemOld = (RotaViagem)getRotaViagemDAO().getHibernateTemplate().get(RotaViagem.class,idRotaViagem);

		if(CompareUtils.le(dtVigenciaInicial, rotaViagemOld.getDtVigenciaInicial())) {
			throw new BusinessException("LMS-29174");
		}

		Integer nbrRotasVigentes = getRotaViagemDAO().getRowCountRotasViagemVigentes(idRotaViagem, dtVigenciaInicial, rotaViagemOld.getDtVigenciaFinal());
		if(CompareUtils.gt(nbrRotasVigentes, IntegerUtils.ZERO)) {
			throw new BusinessException("LMS-29175");
		}

		// Instância a qual será nova Rota de Viagem.
		RotaViagem rotaViagemNew = new RotaViagem();

		// Clona-se todas propriedade do antigo bean para o novo:
		try {
			rotaViagemNew = (RotaViagem)BeanUtils.cloneBean(rotaViagemOld);
		} catch (Exception e) {
			log.error(e);
		}

		// Criado nova data com um dia a menos da nova vigência informada:
		YearMonthDay dtVigenciaInicialAnterior = dtVigenciaInicial.minusDays(1);

		//Finaliza a vigencia da rota anterior.
		rotaViagemOld.setDtVigenciaFinal(dtVigenciaInicialAnterior);

		// Setamos null no id e nos relacionamentos de RotaViagem:
		rotaViagemNew.setIdRotaViagem(null);
		rotaViagemNew.setConfiguracaoAuditoriaFis(null);
		rotaViagemNew.setMeioTransporteRotaViagems(null);
		rotaViagemNew.setMotoristaRotaViagems(null);
		rotaViagemNew.setRotaIdaVoltas(null);
		rotaViagemNew.setServicoRotaViagems(null);
		// Na nova rota de viagem setamos a nova vigência.
		rotaViagemNew.setDtVigenciaInicial(dtVigenciaInicial);

		// Store das instâncias de RotaViagem:
		getRotaViagemDAO().store(rotaViagemNew); 
		getRotaViagemDAO().store(rotaViagemOld);

		// inteiro o qual será utilizado para validar como será tratado a inserção das novas vigências!!!
		int intValidacao = 0;
		List<RotaIdaVolta> rotasIdaVoltaOld = rotaViagemOld.getRotaIdaVoltas();
		// Percorre todas rotaIdaVoltas da rotaViagem e clona cada uma delas:
		for(RotaIdaVolta rotaIdaVoltaOld : rotasIdaVoltaOld) {
			RotaIdaVolta rotaIdaVoltaNew = new RotaIdaVolta();
			try {
				rotaIdaVoltaNew = (RotaIdaVolta)BeanUtils.cloneBean(rotaIdaVoltaOld);
			} catch (Exception e) {
				log.error(e);
			}
			// inicializa valores da nova rotaIdaVolta:
			rotaIdaVoltaNew.setIdRotaIdaVolta(null);
			rotaIdaVoltaNew.setRotaViagem(rotaViagemNew);
			rotaIdaVoltaNew.setVersao(Integer.valueOf(0));
			rotaIdaVoltaNew.setControleCargas(null);
			rotaIdaVoltaNew.setCriterioAplicSimulacoes(null);
			rotaIdaVoltaNew.setTipoMeioTranspRotaEvents(null);
			rotaIdaVoltaNew.setTrechoRotaIdaVoltas(null);

			getRotaViagemDAO().store(rotaIdaVoltaNew);

			List<TrechoRotaIdaVolta> trechosRotaIdaVoltaOld = rotaIdaVoltaOld.getTrechoRotaIdaVoltas();
			for(TrechoRotaIdaVolta trechoRotaIdaVoltaOld : trechosRotaIdaVoltaOld) {
				TrechoRotaIdaVolta trechoRotaIdaVoltaNew = new TrechoRotaIdaVolta();			
				try {
					trechoRotaIdaVoltaNew = (TrechoRotaIdaVolta)BeanUtils.cloneBean(trechoRotaIdaVoltaOld);
				} catch (Exception e) {
					log.error(e);
				}
				trechoRotaIdaVoltaNew.setIdTrechoRotaIdaVolta(null);
				trechoRotaIdaVoltaNew.setRotaIdaVolta(rotaIdaVoltaNew);
				trechoRotaIdaVoltaNew.setVersao(Integer.valueOf(0));
				trechoRotaIdaVoltaNew.setControleTrechos(null);
				trechoRotaIdaVoltaNew.setPontoParadaTrechos(null);

				getRotaViagemDAO().store(trechoRotaIdaVoltaNew);
				List<PontoParadaTrecho> pontosParadaTrechoOld = trechoRotaIdaVoltaOld.getPontoParadaTrechos();
				for(PontoParadaTrecho pontoParadaTrechoOld : pontosParadaTrechoOld) {
					intValidacao = validadeDataToUpdate(pontoParadaTrechoOld.getDtVigenciaInicial(),
							pontoParadaTrechoOld.getDtVigenciaFinal(),
							dtVigenciaInicial); 

					if (intValidacao > 0) {
						PontoParadaTrecho pontoParadaTrechoNew = new PontoParadaTrecho();		
						try {
							pontoParadaTrechoNew = (PontoParadaTrecho)BeanUtils.cloneBean(pontoParadaTrechoOld);
						} catch (Exception e) {
							log.error(e);
						}
						pontoParadaTrechoNew.setIdPontoParadaTrecho(null);
						pontoParadaTrechoNew.setTrechoRotaIdaVolta(trechoRotaIdaVoltaNew);
						pontoParadaTrechoNew.setEventoMeioTransportes(null);
						pontoParadaTrechoNew.setMotivoParadaPontoTrechos(null);

						if (intValidacao == 2) {
							pontoParadaTrechoNew.setDtVigenciaInicial(dtVigenciaInicial);
							pontoParadaTrechoOld.setDtVigenciaFinal(dtVigenciaInicialAnterior);
							getRotaViagemDAO().store(pontoParadaTrechoOld);
						}
						getRotaViagemDAO().store(pontoParadaTrechoNew);

						List<MotivoParadaPontoTrecho> motivosParadaPontoTrechoOld = pontoParadaTrechoOld.getMotivoParadaPontoTrechos();
						for(MotivoParadaPontoTrecho motivoParadaPontoTrechoOld : motivosParadaPontoTrechoOld) {
							intValidacao = validadeDataToUpdate(motivoParadaPontoTrechoOld.getDtVigenciaInicial(),
									motivoParadaPontoTrechoOld.getDtVigenciaFinal(),
									dtVigenciaInicial);
							if (intValidacao > 0) {
								MotivoParadaPontoTrecho motivoParadaPontoTrechoNew = new MotivoParadaPontoTrecho();		
								try {
									motivoParadaPontoTrechoNew = (MotivoParadaPontoTrecho)BeanUtils.cloneBean(motivoParadaPontoTrechoOld);
								} catch (Exception e) {
									log.error(e);
								}
								motivoParadaPontoTrechoNew.setIdMotivoParadaPontoTrecho(null);
								motivoParadaPontoTrechoNew.setPontoParadaTrecho(pontoParadaTrechoNew);

								if (intValidacao == 2) {
									motivoParadaPontoTrechoNew.setDtVigenciaInicial(dtVigenciaInicial);
									motivoParadaPontoTrechoOld.setDtVigenciaFinal(dtVigenciaInicialAnterior);
									getRotaViagemDAO().store(motivoParadaPontoTrechoOld);
								}
								getRotaViagemDAO().store(motivoParadaPontoTrechoNew);
							}
						}
					}
				}
			}

			List<TipoMeioTranspRotaEvent> tiposMeioTransporteRotaEventualOld = rotaIdaVoltaOld.getTipoMeioTranspRotaEvents();
			for(TipoMeioTranspRotaEvent tipoMeioTranspRotaEventOld : tiposMeioTransporteRotaEventualOld) {
				intValidacao = validadeDataToUpdate(tipoMeioTranspRotaEventOld.getDtVigenciaInicial(),
						tipoMeioTranspRotaEventOld.getDtVigenciaFinal(),
						dtVigenciaInicial);
				if (intValidacao > 0) {
					TipoMeioTranspRotaEvent tipoMeioTranspRotaEventNew = new TipoMeioTranspRotaEvent();
					try {
						tipoMeioTranspRotaEventNew = (TipoMeioTranspRotaEvent)BeanUtils.cloneBean(tipoMeioTranspRotaEventOld);
					} catch (Exception e) {
						log.error(e);
					}
					tipoMeioTranspRotaEventNew.setIdTipoMeioTranspRotaEvent(null);
					tipoMeioTranspRotaEventNew.setRotaIdaVolta(rotaIdaVoltaNew);

					if (intValidacao == 2) {
						tipoMeioTranspRotaEventNew.setDtVigenciaInicial(dtVigenciaInicial);
						tipoMeioTranspRotaEventOld.setDtVigenciaFinal(dtVigenciaInicialAnterior);
						getRotaViagemDAO().store(tipoMeioTranspRotaEventOld);
					}
					getRotaViagemDAO().store(tipoMeioTranspRotaEventNew);
				}
			}			
		}

		List<ServicoRotaViagem> servicosRotaViagemOld = rotaViagemOld.getServicoRotaViagems();
		for(ServicoRotaViagem servicoRotaViagemOld : servicosRotaViagemOld) {
			intValidacao = validadeDataToUpdate(servicoRotaViagemOld.getDtVigenciaInicial(),
					servicoRotaViagemOld.getDtVigenciaFinal(),
					dtVigenciaInicial);
			if (intValidacao > 0) {
				ServicoRotaViagem servicoRotaViagemNew = new ServicoRotaViagem();		
				try {
					servicoRotaViagemNew = (ServicoRotaViagem)BeanUtils.cloneBean(servicoRotaViagemOld);
				} catch (Exception e) {
					log.error(e);
				}
				servicoRotaViagemNew.setIdServicoRotaViagem(null);
				servicoRotaViagemNew.setRotaViagem(rotaViagemNew);

				if (intValidacao == 2) {
					servicoRotaViagemNew.setDtVigenciaInicial(dtVigenciaInicial);
					servicoRotaViagemOld.setDtVigenciaFinal(dtVigenciaInicialAnterior);
					getRotaViagemDAO().store(servicoRotaViagemOld);
				}

				getRotaViagemDAO().store(servicoRotaViagemNew);
			}
		}

		List<MeioTransporteRotaViagem> meiosTransporteRotaViagemOld = rotaViagemOld.getMeioTransporteRotaViagems();
		for(MeioTransporteRotaViagem meioTransporteRotaViagemOld : meiosTransporteRotaViagemOld) {
			intValidacao = validadeDataToUpdate(meioTransporteRotaViagemOld.getDtVigenciaInicial(),
					meioTransporteRotaViagemOld.getDtVigenciaFinal(),
					dtVigenciaInicial);
			if (intValidacao > 0) {
				MeioTransporteRotaViagem meioTransporteRotaViagemNew = new MeioTransporteRotaViagem();		
				try {
					meioTransporteRotaViagemNew = (MeioTransporteRotaViagem)BeanUtils.cloneBean(meioTransporteRotaViagemOld);
				} catch (Exception e) {
					log.error(e);
				}
				meioTransporteRotaViagemNew.setIdMeioTransporteRotaViagem(null);
				meioTransporteRotaViagemNew.setRotaViagem(rotaViagemNew);

				if (intValidacao == 2) {
					meioTransporteRotaViagemNew.setDtVigenciaInicial(dtVigenciaInicial);
					meioTransporteRotaViagemOld.setDtVigenciaFinal(dtVigenciaInicialAnterior);
					getRotaViagemDAO().store(meioTransporteRotaViagemOld);
				}

				getRotaViagemDAO().store(meioTransporteRotaViagemNew);
			}
		}

		List<MotoristaRotaViagem> motoristasRotaViagemOld = rotaViagemOld.getMotoristaRotaViagems();
		for(MotoristaRotaViagem motoristaRotaViagemOld : motoristasRotaViagemOld) {
			intValidacao = validadeDataToUpdate(motoristaRotaViagemOld.getDtVigenciaInicial(),
					motoristaRotaViagemOld.getDtVigenciaFinal(),
					dtVigenciaInicial);
			if (intValidacao > 0) {
				MotoristaRotaViagem motoristaRotaViagemNew = new MotoristaRotaViagem();		
				try {
					motoristaRotaViagemNew = (MotoristaRotaViagem)BeanUtils.cloneBean(motoristaRotaViagemOld);
				} catch (Exception e) {
					log.error(e);
				}
				motoristaRotaViagemNew.setIdMotoristaRotaViagem(null);
				motoristaRotaViagemNew.setRotaViagem(rotaViagemNew);

				if (intValidacao == 2) {
					motoristaRotaViagemNew.setDtVigenciaInicial(dtVigenciaInicial);
					motoristaRotaViagemOld.setDtVigenciaFinal(dtVigenciaInicialAnterior);
					getRotaViagemDAO().store(motoristaRotaViagemOld);
				}

				getRotaViagemDAO().store(motoristaRotaViagemNew);
			}
		}
		return rotaViagemNew.getIdRotaViagem();
	}

	/** 
	 * Consulta último trecho da Rota inserido onde:
	 * filial origem é igual à primeira rota
	 * @param idRotaIdaVolta
	 * @return TrechoRotaIdaVolta
	 */
	public Integer findMaiorTempoViagemOfRota(Long idRotaIdaVolta) {
		return getRotaViagemDAO().findMaiorTempoViagemOfRota(idRotaIdaVolta);
	}

	/**
	 * Retorna inteiro para verificar qual a ação a ser realizada ao alterar uma vigência no processo alterar rota.
	 * @param dtPojoIni
	 * @param dtPojoFim
	 * @param dtVigenciaInicial
	 * @return
	 * 0 se não é necessário duplicar registro.
	 * 1 se não deve-se alterar vigência.
	 * 2 se além da cópia, deve-se alterar a vigência.
	 */
	private int validadeDataToUpdate(YearMonthDay dtPojoIni, YearMonthDay dtPojoFim, YearMonthDay dtVigenciaInicial) {
		if (dtPojoFim != null && dtPojoFim.compareTo(dtVigenciaInicial) < 0)
			return 0;
		if (dtPojoIni.compareTo(dtVigenciaInicial) > 0)
			return 1;
		else
			return 2;
	}

	/**
	 * Consulta informações da Rota.
	 * Método específico da tela 'Manter Rotas de Viagem'
	 * 
	 * @author Felipe Ferreira
	 * @param id id da Rota de Ida e Votla desejado.
	 * @param tipoRota String o tipo de Rota desejado
	 * @return Map com informações necessárias no detalhamento da RF 'Manter Rotas de Viagem': 29.06.01.01 
	 */
	public Map<String, Object> findByIdDetalhamentoRota(Long id, String tipoRota) {
		return getRotaViagemDAO().findByIdDetalhamentoRota(id,tipoRota);
	}

	/**
	 * Valida a remoção de um registro de acordo com o padrão de comportamento de vigências.
	 * @param id Id do registro a ser validado.
	 */
	private void validaRemoveById(Long id) {
		RotaViagem rotaViagem = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(rotaViagem);
	}

	
//#############################################################################################################################################	
//	Métodos referentes à tela CONSULTAR ROTAS 
//#############################################################################################################################################	
	public ResultSetPage findPaginatedToConsultarRotas(TypedFlatMap criteria) {

		ResultSetPage rsp = getRotaViagemDAO().findPaginatedToConsultarRotas(criteria,FindDefinition.createFindDefinition(criteria));

		Domain dmTipoRotaViagem = this.domainService.findByName("DM_TIPO_ROTA_VIAGEM");
		Domain dmSistemaRota = this.domainService.findByName("DM_SISTEMA_ROTA");

		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		List<Object[]> list = rsp.getList(); 
		for (Object[] obj : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idRotaViagem", obj[0]);

			if (obj[1] != null && !"".equals((String)obj[1])) {
				DomainValue dvTipoRotaViagem = dmTipoRotaViagem.findDomainValueByValue((String)obj[1]);
				map.put("tpRota",dvTipoRotaViagem.getDescription().getValue(LocaleContextHolder.getLocale()));
			}
			if (obj[2] != null && !"".equals((String)obj[2])) {
				DomainValue dvSistemaRota = dmSistemaRota.findDomainValueByValue((String)obj[2]);
				map.put("tpSistemaRota",dvSistemaRota.getDescription().getValue(LocaleContextHolder.getLocale()));
			}

			map.put("dtVigenciaInicial",obj[3]);
			map.put("dtVigenciaFinal",obj[4]);
			map.put("dsTipoMeioTransporte",obj[5]);
			map.put("versao",obj[6]);

			map.put("nrRotaIda",obj[7]);
			map.put("dsRotaIda",obj[8]);
			map.put("hrSaidaIda",obj[9]);
			map.put("nrRotaVolta",obj[10]);
			map.put("dsRotaVolta",obj[11]);
			map.put("hrSaidaVolta",obj[12]);

			newList.add(map);
		}

		rsp.setList(newList);
		return rsp;
	}

	public List<Map<String, Object>> findLookup(TypedFlatMap criteria) {
		this.geraRotasIdaVolta(criteria);

		FindDefinition fd = new FindDefinition(Integer.valueOf(1), Integer.valueOf(2), Collections.EMPTY_LIST);
		ResultSetPage rsp = getRotaViagemDAO().findPaginatedToConsultarRotas(criteria,fd);
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		List<Object[]> list = rsp.getList();
		for (Object[] obj : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idRotaViagem",obj[0]);
			map.put("dsRota",obj[6]);
			map.put("versao",obj[8]);
			newList.add(map);
		}
		return newList;
	}

	/**
	 * Método responsável por interpretar a string dsRotaTotal
	 * ex.: 
	 * "POA-CXS-CBM"		Rota Ida = "POA-CXS-CBM"	Rota Volta = ""
	 * "POA%CXS-CBM/POA		Rota Ida = "POA%CXS-CBM"	Rota Volta = "POA"
	 * "/POA%CBM"			Rota Ida = ""				Rota Volta = "POA%CBM"
	 * 
	 * @param criteria Map recebido da tela com os filtros.
	 */
	private void geraRotasIdaVolta(Map<String, Object> criteria) {
		String dsRotaTotal = criteria.get("dsRotaTotal") != null ? (String)criteria.get("dsRotaTotal") : "";
		if (!"".equals(dsRotaTotal)) {
			if (dsRotaTotal.startsWith("/"))
				criteria.put("dsRotaVolta",dsRotaTotal.substring(1));
			else {
				StringTokenizer st = new StringTokenizer(dsRotaTotal,"/");
				if (st.hasMoreTokens()) {
					criteria.put("dsRotaIda",st.nextToken());
					if (st.hasMoreTokens())
						criteria.put("dsRotaVolta",st.nextToken());
				}
			}
		}
	}
	
	public Integer getRowCountToConsultarRotas(TypedFlatMap criteria) {
		this.geraRotasIdaVolta(criteria);
		return getRotaViagemDAO().getRowCountToConsultarRotas(criteria);
	}
	
	/**
	 * Find do Valor do pedágio da rota de ida e volta
	 * @param idMoedaPais
	 * @param idTipoMeioTransporte
	 * @param idRotaIdaVolta
	 * @return
	 */
	public BigDecimal findVlPedagio(Long idMoedaPais, Long idTipoMeioTransporte, Long idRota) {
		return this.findVlPedagio(idMoedaPais, idTipoMeioTransporte, idRota, null);
	}
	
	/**
	 * Find do Valor do pedágio da rota de ida e volta
	 * @param idMoedaPais
	 * @param idTipoMeioTransporte
	 * @param idRota
	 * @param idRotaIdaVolta
	 * @return
	 */
	public BigDecimal findVlPedagio(Long idMoedaPais, Long idTipoMeioTransporte, Long idRota, Long idRotaIdaVolta) {
		//Recebe o idRotaIdaVolta e busca todas as filiaisRota desta rotaIdaVolta
		List<FilialRota> filiaisRota = filialRotaService.findFiliaisRotaByRotaOrRotaIdaVolta(idRota,idRotaIdaVolta);
		List<Filial> result = new ArrayList<Filial>();
		for (FilialRota fr : filiaisRota) {
			Filial filial = new Filial();
			filial.setIdFilial(fr.getFilial().getIdFilial());
			result.add(filial);
		}
		return findValor(result, idTipoMeioTransporte, idMoedaPais);
	}
	
	/**
	 * Find do Valor do pedágio da rota de ida e volta
	 * @param idMoedaPais
	 * @param idTipoMeioTransporte
	 * @param filiais lista de Filial
	 * @return
	 */
	public BigDecimal findVlPedagio(Long idMoedaPais, Long idTipoMeioTransporte, List<Filial> filiais) {
		return findValor(filiais, idTipoMeioTransporte, idMoedaPais);
	}
	
	/**
	 * Método private para aproveitar a nova busca pelos valores dos pedágios das filiais da rota
	 * @param filiaisRota
	 * @param idTipoMeioTransporte
	 * @param idMoedaPais
	 * @return
	 */
	private BigDecimal findValor(List<Filial> filiaisRota, Long idTipoMeioTransporte, Long idMoedaPais){
		/*Dados do tio de meio de transporte composto*/
		Long idTipoMeioTransporteComposto = null;
		Integer quantEixosComposto = IntegerUtils.ZERO;

		Integer quantEixos = tipoMeioTransporteService.findQuantidadeEixosTipoMeioTransporte(idTipoMeioTransporte);

		TipoMeioTransporte tipoMeioTransporteComposto = tipoMeioTransporteService.findById(idTipoMeioTransporte);
		if (tipoMeioTransporteComposto.getTipoMeioTransporte() != null){
			idTipoMeioTransporteComposto = tipoMeioTransporteComposto.getTipoMeioTransporte().getIdTipoMeioTransporte();
			quantEixosComposto = tipoMeioTransporteService.findQuantidadeEixosTipoMeioTransporte(idTipoMeioTransporteComposto);
		}
		return postoPassagemTrechoService.findValorPostosPassagemRota(
			filiaisRota,
			idTipoMeioTransporte,
			quantEixos,
			idTipoMeioTransporteComposto,
			quantEixosComposto,
			JTDateTimeUtils.getDataAtual(),
			idMoedaPais
		);

	}
	
	/**
     * Verifica se existe alguma rota vigente após a data de vigencia informada.
     * 
     * @param idRotaViagem
     * @param dtVigencia
     * @return
     */
	public Boolean findRotaViagemFutura(Long idRotaViagem, YearMonthDay dtVigencia) {
		//Se vigência for nula não pode existir rota futura 
		if(dtVigencia == null) {
			return false;
		}
		return getRotaViagemDAO().findRotaViagemFutura(idRotaViagem, dtVigencia);
	}
	
//	Getters / Setters
	public void setFilialRotaService(FilialRotaService filialRotaService) {
		this.filialRotaService = filialRotaService;
	}
	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setPostoPassagemTrechoService(PostoPassagemTrechoService postoPassagemTrechoService) {
		this.postoPassagemTrechoService = postoPassagemTrechoService;
	}
	public void setTipoMeioTransporteService(TipoMeioTransporteService tipoMeioTransporteService) {
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}
	
}
