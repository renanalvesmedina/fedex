package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HistoricoColetaEntrega;
import com.mercurio.lms.municipios.model.HorarioPrevistoSaidaRota;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.dao.RotaColetaEntregaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.rotaColetaEntregaService"
 */
public class RotaColetaEntregaService extends CrudService<RotaColetaEntrega, Long> {
	private HorarioPrevistoSaidaRotaService horarioPrevistoSaidaRotaService;
	private VigenciaService vigenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private RotaIntervaloCepService rotaIntervaloCepService;
	private FilialService filialService;
	private HistoricoColetaEntregaService historicoColetaEntregaService;
	private UsuarioLMSService usuarioLMSService;

	/**
	 * Recupera uma instância de <code>RotaColetaEntrega</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public RotaColetaEntrega findById(java.lang.Long id) {
		return (RotaColetaEntrega)super.findById(id);
	}

	public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
		RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega) super.findById(id);

		TypedFlatMap mapRotaColetaEntrega = new TypedFlatMap();
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(rotaColetaEntrega);
		mapRotaColetaEntrega.put("acaoVigenciaAtual", acaoVigencia);

		mapRotaColetaEntrega.put("filial.idFilial", rotaColetaEntrega.getFilial().getIdFilial()); 
		mapRotaColetaEntrega.put("filial.sgFilial", rotaColetaEntrega.getFilial().getSgFilial()); 
		mapRotaColetaEntrega.put("filial.pessoa.nmFantasia", rotaColetaEntrega.getFilial().getPessoa().getNmFantasia());
		mapRotaColetaEntrega.put("filial.siglaNomeFilial", rotaColetaEntrega.getFilial().getSgFilial() + " - " + rotaColetaEntrega.getFilial().getPessoa().getNmFantasia());

		mapRotaColetaEntrega.put("nrRota", rotaColetaEntrega.getNrRota()); 
		mapRotaColetaEntrega.put("nrKm", rotaColetaEntrega.getNrKm()); 
		mapRotaColetaEntrega.put("dsRota", rotaColetaEntrega.getDsRota()); 
		mapRotaColetaEntrega.put("dtVigenciaInicial", rotaColetaEntrega.getDtVigenciaInicial()); 
		mapRotaColetaEntrega.put("dtVigenciaFinal", rotaColetaEntrega.getDtVigenciaFinal());
		mapRotaColetaEntrega.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega()); 
		mapRotaColetaEntrega.put("numeroDescricaoRota", rotaColetaEntrega.getNrRota() + " - " + rotaColetaEntrega.getDsRota());

		List lista = new ArrayList();
		for (Iterator iter = rotaColetaEntrega.getHorarioPrevistoSaidaRotas().iterator(); iter.hasNext();){
			HorarioPrevistoSaidaRota horarioPrevistoSaidaRota = (HorarioPrevistoSaidaRota)iter.next();
			Map mapRotaColeta = new HashMap();
			mapRotaColeta.put("idHorarioPrevistoSaidaRota", horarioPrevistoSaidaRota.getIdHorarioPrevistoSaidaRota());
			mapRotaColeta.put("hrPrevista",horarioPrevistoSaidaRota.getHrPrevista());
			lista.add(mapRotaColeta);
		}
		mapRotaColetaEntrega.put("horarioPrevistoSaidaRotas",lista);
		return mapRotaColetaEntrega;
	}

	protected void beforeRemoveById(Long id) {
		RotaColetaEntrega rotaColetaEntrega = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(rotaColetaEntrega);
		super.beforeRemoveById(id);
	}

	protected RotaColetaEntrega beforeInsert(RotaColetaEntrega bean) {
		if(!SessionUtils.isIntegrationRunning()){
		((RotaColetaEntrega)bean).setNrRota(this.geraNextNrRota(((RotaColetaEntrega)bean).getFilial().getIdFilial()));
		}
		return super.beforeInsert(bean);
	}

	protected void beforeRemoveByIds(List ids) {
		RotaColetaEntrega rotaColetaEntrega = null;
		for(int x = 0; x < ids.size(); x++) {
			rotaColetaEntrega = findById((Long)ids.get(x));
			JTVigenciaUtils.validaVigenciaRemocao(rotaColetaEntrega);
		}
		super.beforeRemoveByIds(ids);
	}

	public List findRotaColetaEntregaById(Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getRotaColetaEntregaDAO().findRotaColetaEntregaById(idRotaColetaEntrega,dtVigenciaInicial,dtVigenciaFinal);
	}

	public boolean findRotaColetaEntregaValidaVigencias(Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getRotaColetaEntregaDAO().findRotaColetaEntregaValidaVigencias(idRotaColetaEntrega,dtVigenciaInicial,dtVigenciaFinal);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		RotaColetaEntrega rotaColetaEntrega = findById(id);
		List horariosPrevistos = rotaColetaEntrega.getHorarioPrevistoSaidaRotas();
		removeHorarios(horariosPrevistos);

		super.removeById(id);
	}

	private void removeHorarios(List horarios){
		Iterator itHorariosPrevistos = horarios.iterator();
		List ids = new ArrayList();
		while(itHorariosPrevistos.hasNext()){
			HorarioPrevistoSaidaRota horario = (HorarioPrevistoSaidaRota) itHorariosPrevistos.next();
			ids.add(horario.getIdHorarioPrevistoSaidaRota());
		}	
		horarioPrevistoSaidaRotaService.removeByIds(ids);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		Iterator itIds = ids.iterator();
		while(itIds.hasNext()){
			removeById((Long) itIds.next());
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(RotaColetaEntrega bean) {
		return super.store(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Map storeMap(TypedFlatMap map) {
		RotaColetaEntrega bean = map2bean(map);
		

		vigenciaService.validaVigenciaBeforeStore(bean);		
		
		validaNumeroRepetido(map);
		
		Short nrOld = getNumeroAntigo(map);
		
		bean = (RotaColetaEntrega) beforeStore(bean);

		getRotaColetaEntregaDAO().storeRotaColetaEntrega(bean);
		

		atualizaHistoricoRotaColetaEntrega(bean,nrOld);
		
		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(bean);
		map.put("acaoVigenciaAtual", acaoVigencia);
		map.put("idRotaColetaEntrega",bean.getIdRotaColetaEntrega());
		map.put("nrRota", bean.getNrRota());

		return map;
	}

	/**
	 * @param map
	 * @return
	 */
	private Short getNumeroAntigo(TypedFlatMap map) {
		Short nrOld = null;
		Long id = map.getLong("idRotaColetaEntrega");
		
		if(id != null){
			nrOld =  getRotaColetaEntregaDAO().findNrRota(id);
		}
		return nrOld;
	}

	/**
	 * @param id
	 * @param idFilial
	 * @param nrRota
	 * @throws BusinessException
	 */
	private void validaNumeroRepetido(TypedFlatMap map) throws BusinessException {
		Long id       = map.getLong("idRotaColetaEntrega");
		Long idFilial = map.getLong("filial.idFilial");
		Short nrRota  = map.getShort("nrRota");
		
		if(id != null && getRotaColetaEntregaDAO().findExisteRota(idFilial,nrRota,id)){
			throw new BusinessException("LMS-29195",new Object[]{nrRota});
		}
	}

	private RotaColetaEntrega map2bean(TypedFlatMap map) {
		RotaColetaEntrega bean = new RotaColetaEntrega();

		bean.setIdRotaColetaEntrega(map.getLong("idRotaColetaEntrega"));

		Filial filial = new Filial();
		filial.setIdFilial(map.getLong("filial.idFilial"));	
		bean.setFilial(filial);

		bean.setNrRota(map.getShort("nrRota"));
		bean.setNrKm(map.getInteger("nrKm"));
		bean.setDsRota(map.getString("dsRota"));

		List horariosTela = map.getList("horarioPrevistoSaidaRotas");
		List horarios = new ArrayList();
		for (Iterator it = horariosTela.iterator(); it.hasNext();){
			TypedFlatMap item = (TypedFlatMap) it.next();
			HorarioPrevistoSaidaRota horarioPrevistoSaidaRota = new HorarioPrevistoSaidaRota();
			horarioPrevistoSaidaRota.setIdHorarioPrevistoSaidaRota(null);
			horarioPrevistoSaidaRota.setHrPrevista(item.getTimeOfDay("hrPrevista"));
			horarioPrevistoSaidaRota.setRotaColetaEntrega(bean);

			horarios.add(horarioPrevistoSaidaRota);
		}

		bean.setHorarioPrevistoSaidaRotas(horarios);

		bean.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));
		bean.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));

		return bean;
	}

	protected RotaColetaEntrega beforeStore(RotaColetaEntrega bean) {
		RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega) bean;

		filialService.verificaExistenciaHistoricoFilial(
				rotaColetaEntrega.getFilial().getIdFilial(), 
				rotaColetaEntrega.getDtVigenciaInicial(),
				rotaColetaEntrega.getDtVigenciaFinal());

		String label = null;
		
		String chave  = verificaVigencia(rotaColetaEntrega);
		
		label = getLabel(chave);
		
		if (label != null){
			throw new BusinessException("LMS-29129", new Object[]{label} );
		}

		if (getRotaColetaEntregaDAO().verificaVigenciaRotaColetaEntrega(
				rotaColetaEntrega.getIdRotaColetaEntrega(), 
				rotaColetaEntrega.getFilial().getIdFilial(),
				rotaColetaEntrega.getDsRota(),
				rotaColetaEntrega.getDtVigenciaInicial(),
				rotaColetaEntrega.getDtVigenciaFinal()))
			throw new BusinessException("LMS-00003");

		return super.beforeStore(bean);
	}

	private String verificaVigencia(RotaColetaEntrega rotaColetaEntrega) {		
		if(rotaColetaEntrega.getIdRotaColetaEntrega() == null){
			return null;
		}
		return getRotaColetaEntregaDAO().verificaVigencia(rotaColetaEntrega);
	}

	private String getLabel(String chave) {
		if(chave == null){
			return null;
		}else{
			return configuracoesFacade.getMensagem(chave); 
		}
	}

	private void atualizaHistoricoRotaColetaEntrega(RotaColetaEntrega bean,Short nrOld){
		HistoricoColetaEntrega historicoColetaEntrega = new HistoricoColetaEntrega();
		historicoColetaEntrega.setNrRota(bean.getNrRota());
		historicoColetaEntrega.setNrRotaAnt(nrOld);
		historicoColetaEntrega.setDsRota(bean.getDsRota());
		historicoColetaEntrega.setFilial(bean.getFilial());
		historicoColetaEntrega.setNrKm(bean.getNrKm());
		historicoColetaEntrega.setDtVigenciaInicial(bean.getDtVigenciaInicial());
		historicoColetaEntrega.setDtVigenciaFinal(bean.getDtVigenciaFinal());
		historicoColetaEntrega.setDhAtualizacao(JTDateTimeUtils.getDataAtual());
		historicoColetaEntrega.setUsuario(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()).getUsuarioADSM());
		historicoColetaEntrega.setDsSaidasPrevistas(getStringRotas(bean.getHorarioPrevistoSaidaRotas()));
		
		historicoColetaEntregaService.store(historicoColetaEntrega);
	}
	
	private String getStringRotas(List<HorarioPrevistoSaidaRota> horarioPrevistoSaidaRotas) {
		String horarios = "";
		for (HorarioPrevistoSaidaRota horario : horarioPrevistoSaidaRotas) {
			if(horarios.isEmpty()){
				horarios = DateTimeFormat.forPattern("HH:mm").print(horario.getHrPrevista());
			}else{
				horarios+= ",".concat(DateTimeFormat.forPattern("HH:mm").print(horario.getHrPrevista()));
			}
		}
		return horarios;
	}

	/**
	 * Gera o numero da rota
	 * @return o numero da ultima rota + 1
	 */
	private Short geraNextNrRota(Long idFilial ){
		return Short.valueOf((short)(getRotaColetaEntregaDAO().consultaUltimoNrRota(idFilial).shortValue() + (short)1));
	}

	/**
	 * Verifica se existe alguma rota de coleta/entrega com intervalo de cep dentro dos parametros informados e na mesma vigencia informada
	 * @param idFilial
	 * @param idMunicipio
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return TRUE se existe alguma rota, FALSE caso contrario
	 */
	public boolean verificaExisteRotaColetaEntrega(Long idFilial, Long idMunicipio, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getRotaColetaEntregaDAO().verificaExisteRotaColetaEntrega(idFilial, idMunicipio, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Retorna uma list de Rotas de coleta/entrega da filial informada ou do 
	 * usuario logado.
	 * 
	 * @return list
	 */
	public List findLookupByFilialUsuario(Map criteria){	
		Filial filial = null;
		if (criteria.get("filial")==null) {
			filial = SessionUtils.getFilialSessao();
		} else {	
			filial = new Filial();
			filial.setIdFilial(Long.valueOf(((Map)criteria.get("filial")).get("idFilial").toString()));
		}
		Short nrRota = Short.valueOf(criteria.get("nrRota").toString());
		return this.getRotaColetaEntregaDAO().findLookupByFilialUsuario(filial, nrRota);
	}

	/**
	 * Finder de rotas de coleta/entrega.
	 * @return list
	 * @author luisfco
	 */
	public List findRotaColetaEntrega() {
		return getRotaColetaEntregaDAO().findRotaColetaEntrega(false);
	}

	/**
	 * Finder de rotas de coleta/entrega vigentes.
	 * @return list
	 * @author luisfco
	 */
	public List findRotaColetaEntegaVigente() {
		return getRotaColetaEntregaDAO().findRotaColetaEntrega(true);
	}

	
	public List<RotaColetaEntrega> findRotaColetaEntrega(Long idFilial) {			
		return getRotaColetaEntregaDAO().findRotaColetaEntrega(idFilial); 
	}
	
	/**
	 * Retorna a rota de atendimento do cep conforme os critérios enviados
	 * @param String cep, Long idCliente, Long idEnderecoPessoa, Long idFilial, YearMonthDay data
	 * @return TypedFlatMap
	 * @author Salete
	 */ 
	public RotaIntervaloCep findRotaAtendimentoCep(String cep, Long idCliente, Long idEnderecoPessoa, Long idFilial, YearMonthDay data) {
		if (data == null) {
			data = JTDateTimeUtils.getDataAtual();
		}

		List list = rotaIntervaloCepService.findRotaIntervaloCepAtendimento(cep, idCliente, idEnderecoPessoa, idFilial, data);
		if (list.isEmpty()) {
			list = rotaIntervaloCepService.findRotaIntervaloCepAtendimento(cep, idCliente, null, idFilial, data);
			if (list.isEmpty()) {
				list = rotaIntervaloCepService.findRotaIntervaloCepAtendimento(cep, null, null, idFilial, data);
			}
		}

		RotaIntervaloCep rotaIntervaloCep = null;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			rotaIntervaloCep = (RotaIntervaloCep)iter.next();
		}

		return rotaIntervaloCep;
	}

	/**
	 * Finder de rotas de coleta/entrega filtrando por idFilial.
	 * @param idFilial id da filial 
	 * @param apenasRegistrosVigentes define se a consulta trará apenas os registros vigentes ou todos eles.
	 * @return list
	 * @author luisfco
	 */
	public List findRotaColetaEntregaByIdFilial(Long idFilial, boolean apenasRegistrosVigentes) {
		return getRotaColetaEntregaDAO().findRotaColetaEntrega(idFilial,apenasRegistrosVigentes);
	}

	public ResultSetPage findPaginated(Map criteria) {
		return getRotaColetaEntregaDAO().findPaginated(criteria);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getRotaColetaEntregaDAO().getRowCount(criteria);
	}

	/**
	 * Busca as rotas de coleta entrega que possuem pedidos coleta abertos e com data de previsão de coleta
	 * menor igual à data passada por parâmetro e com filial responsável igual à filial informada.
	 * @param tpStatusColeta
	 * @param idFilialResponsavel
	 * @param datePrevisaoColeta
	 * @return
	 */
	public List findIdsRotasColetaEntregaByTpStatusColetaUntilPrevisaoColeta(String tpStatusColeta, Long idFilialResponsavel, YearMonthDay datePrevisaoColeta) {
		return getRotaColetaEntregaDAO().findIdsRotasColetaEntregaByTpStatusColetaUntilPrevisaoColeta(tpStatusColeta, idFilialResponsavel, datePrevisaoColeta);
	}

	/**
	 * Retorna Rota de Coleta/Entrega para os parâmetros informados
	 * Método utilizado pela Integração
	 * 
	 * @author Felipe Ferreira
	 * @param idFilial
	 * @param nrRota
	 * @return Rota de Coleta/Entrega
	 */
	public RotaColetaEntrega findRotaColetaEntrega(Long idFilial, Short nrRota) {
		return getRotaColetaEntregaDAO().findRotaColetaEntrega(idFilial,nrRota);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setRotaColetaEntregaDAO(RotaColetaEntregaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private final RotaColetaEntregaDAO getRotaColetaEntregaDAO() {
		return (RotaColetaEntregaDAO) getDao();
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setRotaIntervaloCepService(RotaIntervaloCepService rotaIntervaloCepService) {
		this.rotaIntervaloCepService = rotaIntervaloCepService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setHorarioPrevistoSaidaRotaService(HorarioPrevistoSaidaRotaService horarioPrevistoSaidaRotaService) {
		this.horarioPrevistoSaidaRotaService = horarioPrevistoSaidaRotaService;
	}

	public RotaColetaEntrega findRotaColetaEntregaByControleCarga(Long idControleCarga) {
		return getRotaColetaEntregaDAO().findRotaColetaEntregaByControleCarga(idControleCarga);		
	}
	
	public void setHistoricoColetaEntregaService(HistoricoColetaEntregaService historicoColetaEntregaService) {
		this.historicoColetaEntregaService = historicoColetaEntregaService;
	}
	
	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}

	/**
	 * Efetua pesquisa para a suggest de rota de coleta/entrega.
	 * 
	 * @param idFilial
	 * @param nrRota
	 * @param dsRota
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findRotaColetaEntregaSuggest(
			Long idFilial, String nrRota, String dsRota, Integer limiteRegistros) {
		return getRotaColetaEntregaDAO().findRotaColetaEntregaSuggest(idFilial, nrRota, dsRota, limiteRegistros);
	}
}
	
	
