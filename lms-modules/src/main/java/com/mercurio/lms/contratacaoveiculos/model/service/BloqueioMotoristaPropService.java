package com.mercurio.lms.contratacaoveiculos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.EmpresaUsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.BloqueioMotoristaProp;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.dao.BloqueioMotoristaPropDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.bloqueioMotoristaPropService"
 */
public class BloqueioMotoristaPropService extends CrudService<BloqueioMotoristaProp, Long> {
	
	private EmpresaUsuarioService empresaUsuarioService;	
	private static final String SIGLA_MATRIZ = "MTZ";
	private MeioTransporteService meioTransporteService;
	private ControleCargaService controleCargaService;
	private FilialService filialService;
	private ConfiguracoesFacade configuracoesFacade;

	/* Cria lista para trazer no bean apenas os campos apresentados no Grid. 
	 * @see com.mercurio.adsm.framework.model.CrudService#findPaginated(java.util.Map)
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) { 
		ResultSetPage rsp = getBloqueioMotoristaPropDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));

		List newArray = new ArrayList();

		Iterator i = rsp.getList().iterator();
		while (i.hasNext()) {
			Object[] projections = (Object[])i.next();
			TypedFlatMap row = new TypedFlatMap();
			row.put("idBloqueioMotoristaProp", projections[0]);
			row.put("obBloqueioMotoristaProp", projections[1]);
			row.put("nmUsuarioBloqueio", projections[2]);
			row.put("nmUsuarioDesbloqueio", projections[3]);
			row.put("sgFilialBloqueio",projections[4]);
			row.put("nmFilialBloqueio",projections[5]);
			row.put("dtRegistroBloqueio", projections[6]);
			row.put("sgFilialDesbloqueio",projections[7]);
			row.put("nmFilialDesbloqueio",projections[8]);
			row.put("dtRegistroDesbloqueio",projections[9]);
			row.put("dhVigenciaInicial",projections[10]);
			DateTime dhVigenciaFinal = (DateTime)projections[11];
			DateTime maxDateTime = JTDateTimeUtils.MAX_YEARMONTHDAY.toDateTimeAtMidnight(SessionUtils.getFilialSessao().getDateTimeZone());
			if (dhVigenciaFinal != null && comparaDataWithoutSec(dhVigenciaFinal, maxDateTime) >= 0) {
				dhVigenciaFinal = null;
			}
			row.put("dhVigenciaFinal",dhVigenciaFinal);

			newArray.add(row);
		}
		rsp.setList(newArray);

		return rsp;
	}	
	
	/**
	 * Recupera uma instância de <code>BloqueioMotoristaProp</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public BloqueioMotoristaProp findById(java.lang.Long id) {
		return (BloqueioMotoristaProp)super.findById(id);
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
	public java.io.Serializable storeMap(Map map) {
		BloqueioMotoristaProp bean = new BloqueioMotoristaProp();

		ReflectionUtils.copyNestedBean(bean,map);
		beforeStore(bean);

		BloqueioMotoristaProp bmp= new BloqueioMotoristaProp();
		if(bean.getIdBloqueioMotoristaProp()!= null){
			bmp = this.findById(bean.getIdBloqueioMotoristaProp());
		}

		DateTime dhVigenciaInicialDetalhe = (DateTime) ReflectionUtils.toObject((String) map.get("dhVigenciaInicialDetalhe"), DateTime.class);
		DateTime dhVigenciaFinalDetalhe = (DateTime) ReflectionUtils.toObject((String) map.get("dhVigenciaFinalDetalhe"), DateTime.class);

		if (bean.getDhVigenciaFinal() == null) {
			bean.setDhVigenciaFinal(JTDateTimeUtils.setLastHourOfDayDateTime(JTDateTimeUtils.MAX_YEARMONTHDAY));
		}

		getBloqueioMotoristaPropDAO().validaVigenciaBeforeStore(bean);

		Usuario usr = SessionUtils.getUsuarioLogado();
		Filial filial = SessionUtils.getFilialSessao();
		JTDateTimeUtils.getDataHoraAtual().withSecondOfMinute(0).withMillisOfSecond(0);
		//quando é um bloqueio novo
		if(bean.getIdBloqueioMotoristaProp()== null){
			if (bean.getDhVigenciaInicial() != null) {
				bean.setUsuarioByIdFuncionarioBloqueio(usr);
				bean.setDtRegistroBloqueio(JTDateTimeUtils.getDataAtual());
				bean.setFilialByIdFilialBloqueio(filial);
				DateTimeZone dateTimeZone = DateTimeZone.forID(filial.getDsTimezone());
				DateTime dataVigenciaInicial = new DateTime(bean.getDhVigenciaInicial(), dateTimeZone);
				bean.setDhVigenciaInicial(dataVigenciaInicial);
				DateTime dataVigenciaFinal = new DateTime(bean.getDhVigenciaFinal(), dateTimeZone);
				bean.setDhVigenciaFinal(dataVigenciaFinal);
			}

			if (checkVigenciaFinalInformada(bean)) {
				bean.setUsuarioByIdFuncionarioDesbloqueio(usr);
				bean.setDtRegistroDesbloqueio(JTDateTimeUtils.getDataAtual());
				bean.setFilialByIdFilialDesbloqueado(filial);
			}
		}
		//quando é alteração de um bloqueio
		else {
			BloqueioMotoristaProp bloqueioMotoristaProp = findById(bean.getIdBloqueioMotoristaProp());
			bean.setDhVigenciaInicial(bloqueioMotoristaProp.getDhVigenciaInicial());
			bean.setBlBloqueiaViagem(bmp.getBlBloqueiaViagem());
			bean.setBlControleCargaNovo(bmp.getBlControleCargaNovo());
			bean.setControleCarga(bmp.getControleCarga());
			
			if (bean.getDhVigenciaInicial() != null && dhVigenciaInicialDetalhe != null && 
					(! isEqualsWithoutSec(bean.getDhVigenciaInicial(), dhVigenciaInicialDetalhe)) ) {
				bean.setUsuarioByIdFuncionarioBloqueio(usr);
				bean.setDtRegistroBloqueio(JTDateTimeUtils.getDataAtual());
				bean.setFilialByIdFilialBloqueio(filial);
			} else {
				bean.setDtRegistroBloqueio(bmp.getDtRegistroBloqueio());
				bean.setFilialByIdFilialBloqueio(bmp.getFilialByIdFilialBloqueio());
				bean.setUsuarioByIdFuncionarioBloqueio(bmp.getUsuarioByIdFuncionarioBloqueio());
			}

			//Se a data de vigencia final foi informada e se diferente da gravada no banco (detalhe) 
			if (checkVigenciaFinalInformada(bean) && checkVigenciaFinalWithRow(bean, dhVigenciaFinalDetalhe)) {
				if(checkBloqueioMeioTransporte(map) 
						&& !checkUsuarioMatriz(bmp.getFilialByIdFilialBloqueio(), usr)){
					throw new BusinessException("LMS-26126");
				}
					bean.setUsuarioByIdFuncionarioDesbloqueio(usr);
					bean.setDtRegistroDesbloqueio(JTDateTimeUtils.getDataAtual());
					bean.setFilialByIdFilialDesbloqueado(filial);
					DateTimeZone dateTimeZone = DateTimeZone.forID(filial.getDsTimezone());
					DateTime data = new DateTime(bean.getDhVigenciaFinal(), dateTimeZone);
					bean.setDhVigenciaFinal(data);
				}
			}

		getBloqueioMotoristaPropDAO().getAdsmHibernateTemplate().evict(bmp);

		getBloqueioMotoristaPropDAO().store(bean);
		return bean;
	}

	private boolean checkBloqueioMeioTransporte(Map map) {
		Map meioTransporte = (Map)map.get("meioTransporte");
		String idMeioTransporte = meioTransporte != null ? (String)meioTransporte.get("idMeioTransporte") : null;
		if(!StringUtils.isEmpty(idMeioTransporte)){
			return true;
		}
		return false;
	}

	private boolean checkVigenciaFinalWithRow(BloqueioMotoristaProp bean,
			DateTime dhVigenciaFinalDetalhe) {
		return dhVigenciaFinalDetalhe == null || ! isEqualsWithoutSec(bean.getDhVigenciaFinal(), dhVigenciaFinalDetalhe);
	}

	private boolean checkVigenciaFinalInformada(BloqueioMotoristaProp bean) {
		return bean.getDhVigenciaFinal() != null && 
				! isEqualsWithoutSec(bean.getDhVigenciaFinal(), JTDateTimeUtils.MAX_DATETIME);
	}

	private boolean checkUsuarioMatriz(Filial filial, Usuario usuarioLogado) {
		EmpresaUsuario empresaDesbloqueio = empresaUsuarioService.findByIdUsuarioUsingEmpresaPadrao(usuarioLogado.getIdUsuario());
		boolean bloqueioMatriz = filial.getSgFilial().equalsIgnoreCase(SIGLA_MATRIZ);
		boolean desbloqueioMatriz = empresaDesbloqueio.getFilialPadrao().getSgFilial().equalsIgnoreCase(SIGLA_MATRIZ);
		if(bloqueioMatriz && !desbloqueioMatriz){
			return false;
		}
			return true;
		}

	public List findBloqueiosVigentesByMeioTransporte(Long idMeioTransporte) {
		return getBloqueioMotoristaPropDAO().findBloqueiosVigentesMeioTransporte(idMeioTransporte);
	}
	
	/**
	 * 
	 * Realiza o bloqueio de meio de transporte eventual
	 * @param controleCarga
	 * @param isNewControleCarga
	 * @param idMeioTransporte
	 * @param lastControleCarga - utilizado para armazenar o id do último controle de carga do meio de transporte
	 * @return
	 */
	public boolean executeBloqueioEventual(ControleCarga controleCarga, boolean isNewControleCarga, Long idMeioTransporte, ControleCarga lastControleCarga) {
		
		final String tpVinculoEventual = "E";
		final String tpControleCargaViagem = "V";
		
		String blBloqueiaViagemEventual = (String)configuracoesFacade.getValorParametro("BL_BLOQUEIA_VIAGEM_EVENTUAL");
		Integer nrViagensBloqEventual = ((BigDecimal)configuracoesFacade.getValorParametro("NR_VIAGENS_BLOQ_EVENTUAL")).intValue();
		
		Integer nrDiasPeriodoBloqEventual = ((BigDecimal)configuracoesFacade.getValorParametro("NR_DIAS_PERIODO_BLOQ_EVENTUAL")).intValue();

		if(isNewControleCarga)  {
			// busca o útimo controle de carga relacionado com o meio de transporte
			controleCarga = controleCargaService.findLastByMeioTransporte(idMeioTransporte, controleCarga.getIdControleCarga());
		}

		// busca dados do meio de transporte
		MeioTransporte mt = meioTransporteService.findById(idMeioTransporte);
		
		if("S".equals(blBloqueiaViagemEventual)
				&& tpVinculoEventual.equals(mt.getTpVinculo().getValue()) 
				&& controleCarga.getTpControleCarga() != null
				&& tpControleCargaViagem.equals(controleCarga.getTpControleCarga().getValue())
				&& getBloqueioMotoristaPropDAO().getRowCountQuantidadeViagens(controleCarga, mt, nrDiasPeriodoBloqEventual) >=  nrViagensBloqEventual
				&& getBloqueioMotoristaPropDAO().verificaBloqueioViagemEventual(controleCarga, mt, isNewControleCarga)) {
			
			lastControleCarga.setIdControleCarga(controleCarga.getIdControleCarga());
				
			return true;
			
		}
		
		return false;
	}

	/**
	 * Persiste o bloqueio de meio de transporte eventual
	 * 
	 * @param idControleCarga
	 * @param idMeioTransporte
	 * @param isNewControleCarga
	 */
	public void storeBloqueioViagemEventual(Long idControleCarga, Long idMeioTransporte, boolean isNewControleCarga) {
		
		final String tpEmpresaMatriz = "M";
		String obBloqueioViagemEventual = (String)configuracoesFacade.getValorParametro("OB_BLOQUEIO_VIAGEM_EVENTUAL");
		
		// Insere o registro de bloqueio
		BloqueioMotoristaProp bmp = new BloqueioMotoristaProp();
		bmp.setObBloqueioMotoristaProp(obBloqueioViagemEventual);
		bmp.setDtRegistroBloqueio(JTDateTimeUtils.getDataAtual());
		bmp.setUsuarioByIdFuncionarioBloqueio(SessionUtils.getUsuarioLogado());
		bmp.setFilialByIdFilialBloqueio(filialService.findBySgFilialAndTpEmpresa(SIGLA_MATRIZ, tpEmpresaMatriz));
		bmp.setMeioTransporte(meioTransporteService.findById(idMeioTransporte));
		bmp.setDhVigenciaInicial(JTDateTimeUtils.getDataHoraAtual());
		bmp.setDhVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY.toDateTimeAtMidnight());
		bmp.setBlBloqueiaViagem(true);
		bmp.setControleCarga(controleCargaService.findById(idControleCarga));
		bmp.setBlControleCargaNovo(isNewControleCarga);
		
		this.store(bmp);
		
	}
	
	private Map getMapDadosBloqueio(List bloqueio) {
		Map retorno = new HashMap();
		BloqueioMotoristaProp bloqueioMotoristaProp = (BloqueioMotoristaProp) bloqueio.get(0);
		retorno.put("idBloqueioMotoristaProp", bloqueioMotoristaProp.getIdBloqueioMotoristaProp());
		retorno.put("obBloqueioMotoristaProp", bloqueioMotoristaProp.getObBloqueioMotoristaProp());
		DateTime dhVigenciaFinal = bloqueioMotoristaProp.getDhVigenciaFinal();
		DateTime maxDateTime = JTDateTimeUtils.MAX_YEARMONTHDAY.toDateTimeAtMidnight(SessionUtils.getFilialSessao().getDateTimeZone());
		if (dhVigenciaFinal != null && comparaDataWithoutSec(dhVigenciaFinal, maxDateTime) >= 0) {
			dhVigenciaFinal = null;
		}
		retorno.put("dhVigenciaInicial", bloqueioMotoristaProp.getDhVigenciaInicial());
		retorno.put("dhVigenciaFinal", dhVigenciaFinal);

		return retorno;
	}

	public Map findDadosBloqueioMotorista(Map dados){
		List bloqueio = null;
		HashMap retorno = new HashMap();

		Motorista motorista = new Motorista();
		motorista.setIdMotorista(Long.valueOf((String)dados.get("idMotorista")));
		bloqueio = this.getBloqueioMotoristaPropDAO().findBloqueiosVigentesMotorista(motorista.getIdMotorista());

		if (bloqueio !=null && bloqueio.size() > 0){
			retorno.putAll(getMapDadosBloqueio(bloqueio));
			retorno.put("blBloqueio", "S");
		} else {
			bloqueio = this.getBloqueioMotoristaPropDAO().findBloqueiosFuturosMotorista(motorista.getIdMotorista());
			if (bloqueio !=null && bloqueio.size() > 0) {
				retorno.putAll(getMapDadosBloqueio(bloqueio));
			}
		}
		return retorno;
	}

	public Map findDadosBloqueioProprietario(Map dados){
		return findBloqueioProprietario(dados,true);
	}

	private Map findBloqueioProprietario(Map dados, boolean verificabloqueiosFuturos) {
		List bloqueio = null;
		HashMap retorno = new HashMap();

		Proprietario proprietario = new Proprietario();
		proprietario.setIdProprietario( Long.valueOf((String)dados.get("idProprietario")) );

		bloqueio = this.getBloqueioMotoristaPropDAO().findBloqueiosVigentesProprietario(proprietario.getIdProprietario());

		if (bloqueio !=null && bloqueio.size() > 0){
			retorno.putAll(getMapDadosBloqueio(bloqueio));
			retorno.put("blBloqueio", "S");
		} else if(verificabloqueiosFuturos){
			bloqueio = this.getBloqueioMotoristaPropDAO().findBloqueiosFuturosProprietario(proprietario.getIdProprietario());
			if (bloqueio != null && bloqueio.size() > 0) {
				retorno.putAll(getMapDadosBloqueio(bloqueio));
			}
		}
		return retorno;
	}

	/**
	 * Verifica apenas bloqueio do proprietario vigente
	 * 
	 * @param dados
	 * @return
	 */
	public Map findDadosBloqueioProprietarioVigente(Map dados){
		return findBloqueioProprietario(dados,false);
	}
	

	/**
	 * Retorna dados de bloqueio de um meio de transporte
	 * 
	 * @author Felipe Ferreira
	 * @param dados
	 * @return
	 */
	public Map findDadosBloqueioMeioTransporte(Map dados) {
		return findBloqueioMeioTransporte(dados,true);
	}
	
	/**
	 * Retorna dados de bloqueio de um meio de transporte vigente
	 * 
	 * @param dados
	 * @return
	 */
	public Map findDadosBloqueioMeioTransporteVigente(Map dados) {
		return findBloqueioMeioTransporte(dados,false);
	}

	private Map findBloqueioMeioTransporte(Map dados,boolean verificabloqueiosFuturos) {
		List bloqueio = null;
		HashMap retorno = new HashMap();

		MeioTransporte meioTransporte = new MeioTransporte();
		meioTransporte.setIdMeioTransporte( Long.valueOf((String)dados.get("idMeioTransporte")) );

		bloqueio = this.getBloqueioMotoristaPropDAO().findBloqueiosVigentesMeioTransporte(meioTransporte.getIdMeioTransporte());

		if (bloqueio !=null && bloqueio.size() > 0){
			retorno.putAll(getMapDadosBloqueio(bloqueio));
			retorno.put("blBloqueio", "S");
		} else if(verificabloqueiosFuturos){
			bloqueio = this.getBloqueioMotoristaPropDAO().findBloqueiosFuturosMeioTransporte(meioTransporte.getIdMeioTransporte());
			if (bloqueio !=null && bloqueio.size() > 0)	
				retorno.putAll(getMapDadosBloqueio(bloqueio));
		}
		return retorno;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setBloqueioMotoristaPropDAO(BloqueioMotoristaPropDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private BloqueioMotoristaPropDAO getBloqueioMotoristaPropDAO() {
		return (BloqueioMotoristaPropDAO) getDao();
	}

	/**
	 * Verifica a existencia de bloqueio vigente para o proprietario
	 * @param pro Proprietario
	 * @return True, se não encontrou nada.
	 */
	public boolean verificaBloqueiosVigentes(Proprietario pro) {
		return getBloqueioMotoristaPropDAO().validateBloqueiosVigentes(pro);
	}

	/**
	 * Verifica a existencia de bloqueios vigentes para o meio de transporte
	 * @param mt Entidade
	 * @return True, se há bloqueios vigentes.
	 */
	public boolean validateBloqueiosVigentes(MeioTransporte mt) {
		return getBloqueioMotoristaPropDAO().validateBloqueiosVigentes(mt);
	}

	private int comparaDataWithoutSec(DateTime date, DateTime dateRef) {
		if (date == null) {
			throw new IllegalArgumentException("date não pode ser null.");
		}
		if (dateRef == null) {
			throw new IllegalArgumentException("dateRef não pode ser null.");
		}
		return date.withSecondOfMinute(0).withMillisOfSecond(0).compareTo(dateRef.withSecondOfMinute(0).withMillisOfSecond(0));
	}

	private boolean isEqualsWithoutSec(DateTime date, DateTime dateRef) {
		return comparaDataWithoutSec(date, dateRef) == 0;
	}
	
	public void setEmpresaUsuarioService(EmpresaUsuarioService empresaUsuarioService) {
		this.empresaUsuarioService = empresaUsuarioService;
	}
	
	/**
	 * Executa o bloqueio/desbloqueio. </br>
	 * 
	 * <b>É necessário popular o bean com id (se houver), data vigência inicial,
	 * vigência inicial final (se houver) e texto de observação.</b>
	 * 
	 * @param bean
	 * @return BloqueioMotoristaProp
	 */
	public BloqueioMotoristaProp storeBloqueio(BloqueioMotoristaProp bean){
		BloqueioMotoristaProp bloqueioMotoristaProp = null;
		
		beforeStore(bean);
		
		if (bean.getDhVigenciaFinal() == null) {
			bean.setDhVigenciaFinal(JTDateTimeUtils.setLastHourOfDayDateTime(JTDateTimeUtils.MAX_YEARMONTHDAY));
		}
		
		getBloqueioMotoristaPropDAO().validaVigenciaBeforeStore(bean);
		
		if(bean.getIdBloqueioMotoristaProp() == null){
			bloqueioMotoristaProp = bloquear(bean);
		} else {
			bloqueioMotoristaProp = desbloquear(bean);
		}
		
		getBloqueioMotoristaPropDAO().store(bean);
		
		return bloqueioMotoristaProp;
	}
	
	/**
	 * Executa o desbloqueio de um registro.
	 * 
	 * @param bean
	 * 
	 * @return BloqueioMotoristaProp
	 */
	private BloqueioMotoristaProp desbloquear(BloqueioMotoristaProp bean){			
		Usuario usuario = SessionUtils.getUsuarioLogado();
		Filial filial = SessionUtils.getFilialSessao();
		YearMonthDay registroBloqueio = JTDateTimeUtils.getDataAtual();
		
		BloqueioMotoristaProp bloqueioMotoristaProp = findById(bean.getIdBloqueioMotoristaProp());
		
		DateTime dhVigenciaInicialAtual = bloqueioMotoristaProp.getDhVigenciaInicial();
		DateTime dhVigenciaFinalAtual = bloqueioMotoristaProp.getDhVigenciaFinal();
		
		if (bean.getDhVigenciaInicial() != null 
				&& dhVigenciaInicialAtual != null && (!isEqualsWithoutSec(bean.getDhVigenciaInicial(), dhVigenciaInicialAtual))) {
			defineDadosBloqueio(bean, filial, usuario, registroBloqueio);
		} else {
			defineDadosBloqueio(
					bean, 
					bloqueioMotoristaProp.getFilialByIdFilialBloqueio(), 
					bloqueioMotoristaProp.getUsuarioByIdFuncionarioBloqueio(), 
					bloqueioMotoristaProp.getDtRegistroBloqueio());
		}

		/*
		 * Se a data de vigência final informada for diferente da gravada no
		 * banco
		 */
		if (checkVigenciaFinalInformada(bean) && checkVigenciaFinalWithRow(bean, dhVigenciaFinalAtual)) {
			if(!checkUsuarioMatriz(bloqueioMotoristaProp.getFilialByIdFilialBloqueio(), usuario)){
				throw new BusinessException("LMS-26126");
			} else {
				defineDadosDesbloqueio(bean, filial, usuario, registroBloqueio);
			}
		}			
		
		getBloqueioMotoristaPropDAO().getAdsmHibernateTemplate().evict(bloqueioMotoristaProp);
				
		return bean;
	}
	
	/**
	 * Executa o bloqueio de um registro.
	 *  
	 * @param bean
	 * 
	 * @return BloqueioMotoristaProp
	 */
	private BloqueioMotoristaProp bloquear(BloqueioMotoristaProp bean){			
		Usuario usuario = SessionUtils.getUsuarioLogado();
		Filial filial = SessionUtils.getFilialSessao();
		YearMonthDay registroBloqueio = JTDateTimeUtils.getDataAtual();
		
		if (bean.getDhVigenciaInicial() != null) {
			defineDadosBloqueio(bean, filial, usuario, registroBloqueio);	
		}
		
		if (checkVigenciaFinalInformada(bean)) {
			defineDadosDesbloqueio(bean, filial, usuario, registroBloqueio);
		}			
		
		return bean;
	}
	
	/**
	 * Define os dados em comum para operação de bloqueio.
	 * 
	 * @param bean
	 * @param filial
	 * @param usuario
	 * @param registroBloqueio
	 */
	private void defineDadosBloqueio(BloqueioMotoristaProp bean, Filial filial,
			Usuario usuario, YearMonthDay registroBloqueio) {
		bean.setDtRegistroBloqueio(registroBloqueio);		
		bean.setUsuarioByIdFuncionarioBloqueio(usuario);		
		bean.setFilialByIdFilialBloqueio(filial);
		bean.setBlBloqueiaViagem(false);
		bean.setBlControleCargaNovo(false);
	}
	
	/**
	 * Define os dados em comum para operação de desbloqueio.
	 * 
	 * @param bean
	 * @param filial
	 * @param usuario
	 * @param registroBloqueio
	 */
	private void defineDadosDesbloqueio(BloqueioMotoristaProp bean,
			Filial filial, Usuario usuario, YearMonthDay registroBloqueio) {
		bean.setDtRegistroDesbloqueio(registroBloqueio);		
		bean.setUsuarioByIdFuncionarioDesbloqueio(usuario);		
		bean.setFilialByIdFilialDesbloqueado(filial);
		bean.setBlBloqueiaViagem(false);
		bean.setBlControleCargaNovo(false);
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
}