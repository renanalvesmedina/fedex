package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.TipoDificuldadeAcesso;
import com.mercurio.lms.municipios.model.dao.RotaIntervaloCepDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.vendas.model.Cliente;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.rotaIntervaloCepService"
 */
public class RotaIntervaloCepService extends CrudService<RotaIntervaloCep, Long> {
	private IntervaloCepService intervaloCepService;
	private VigenciaService vigenciaService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private MunicipioFilialService municipioFilialService;

	/**
	 * Recupera uma instância de <code>RotaIntervaloCep</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public RotaIntervaloCep findById(Long id) {
		return (RotaIntervaloCep)super.findById(id);
	}

	public TypedFlatMap findByIdDetalhamento(Long id) {
		RotaIntervaloCep rotaIntervaloCep = (RotaIntervaloCep)super.findById(id);
		TypedFlatMap item = bean2map(rotaIntervaloCep);
		item.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(rotaIntervaloCep));

		String intervaloCep = "";
		if (rotaIntervaloCep.getNrCepInicial() != null) {
			intervaloCep = rotaIntervaloCep.getNrCepInicial(); 

			if (rotaIntervaloCep.getNrCepFinal() != null) {
				intervaloCep = intervaloCep + " - " + rotaIntervaloCep.getNrCepFinal();
			}			
		}
		item.put("intervaloCep", intervaloCep);

		return item;
	}

	protected void beforeRemoveById(Long id) {
		RotaIntervaloCep rota = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(rota);
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		RotaIntervaloCep rota = null;
		for(int x = 0; x < ids.size(); x++) {
			rota = findById((Long)ids.get(x));
			JTVigenciaUtils.validaVigenciaRemocao(rota);
		}
		super.beforeRemoveByIds(ids);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 */
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(RotaIntervaloCep bean) {
		return super.store(bean);
	}

	protected RotaIntervaloCep beforeStore(RotaIntervaloCep bean) {
		RotaIntervaloCep rotaIntervaloCep = (RotaIntervaloCep) bean;

		if (rotaIntervaloCep.getCliente() != null && !StringUtils.isBlank(rotaIntervaloCep.getNrCepInicial())) {
			throw new BusinessException("LMS-29159");
		} else if (rotaIntervaloCep.getCliente() == null && StringUtils.isBlank(rotaIntervaloCep.getNrCepInicial())) {
			throw new BusinessException("LMS-29164");			
		}

		if ( StringUtils.isBlank(rotaIntervaloCep.getNrCepInicial()) ) {
			if (rotaIntervaloCep.getEnderecoPessoa() == null) {
				if (verificaIntervaloCep(rotaIntervaloCep.getIdRotaIntervaloCep(), rotaIntervaloCep.getNrCepInicial(), rotaIntervaloCep.getNrCepFinal(), rotaIntervaloCep.getDtVigenciaInicial(), rotaIntervaloCep.getDtVigenciaFinal(), rotaIntervaloCep.getCliente())) {
					throw new BusinessException("LMS-29160");			
				}
			} else {
				if (verificaIntervaloCep(rotaIntervaloCep.getIdRotaIntervaloCep(), rotaIntervaloCep.getNrCepInicial(), rotaIntervaloCep.getNrCepFinal(), rotaIntervaloCep.getDtVigenciaInicial(), rotaIntervaloCep.getDtVigenciaFinal(), rotaIntervaloCep.getCliente(), rotaIntervaloCep.getEnderecoPessoa())) {
					throw new BusinessException("LMS-29161");			
				}
			}			
		} else {
			if (!intervaloCepService.validaIntervalo(rotaIntervaloCep.getNrCepInicial(), rotaIntervaloCep.getNrCepFinal(), rotaIntervaloCep.getMunicipio().getIdMunicipio())){
				throw new BusinessException("LMS-29050");
			}	

			if(BooleanUtils.isFalse(rotaIntervaloCep.getBlAtendimentoTemporario())){
				if (verificaIntervaloCep(rotaIntervaloCep.getIdRotaIntervaloCep(), rotaIntervaloCep.getNrCepInicial(), rotaIntervaloCep.getNrCepFinal(), rotaIntervaloCep.getDtVigenciaInicial(), rotaIntervaloCep.getDtVigenciaFinal())){
				throw new BusinessException("LMS-29051");
		}
			}
		}

		if (!rotaColetaEntregaService.findRotaColetaEntregaValidaVigencias(rotaIntervaloCep.getRotaColetaEntrega().getIdRotaColetaEntrega(), rotaIntervaloCep.getDtVigenciaInicial(), rotaIntervaloCep.getDtVigenciaFinal())){
			throw new BusinessException("LMS-29023");
		}

		if (getRotaIntervaloCepDAO().validateNrOrdemVigente(rotaIntervaloCep)){
			throw new BusinessException("LMS-29134");
		}

		Map retorno = municipioFilialService.findIntervaloVigenciaByMunicipio(rotaIntervaloCep.getMunicipio().getIdMunicipio(), rotaIntervaloCep.getDtVigenciaInicial(), rotaIntervaloCep.getDtVigenciaFinal());
		if (retorno != null){
			YearMonthDay dtVigenciaInicialAtendimento = (YearMonthDay) retorno.get("dtVigenciaInicial");
			YearMonthDay dtVigenciaFinalAtendimento = JTDateTimeUtils.maxYmd((YearMonthDay) retorno.get("dtVigenciaFinal"));

			YearMonthDay dtVigenciaInicial = rotaIntervaloCep.getDtVigenciaInicial();
			YearMonthDay dtVigenciaFinal = JTDateTimeUtils.maxYmd(rotaIntervaloCep.getDtVigenciaFinal());

			if (!((dtVigenciaInicial.equals(dtVigenciaInicialAtendimento) || dtVigenciaInicial.isAfter(dtVigenciaInicialAtendimento))
					&& (dtVigenciaFinal.equals(dtVigenciaFinalAtendimento) || dtVigenciaFinal.isBefore(dtVigenciaFinalAtendimento)))){
				throw new BusinessException("LMS-29132");
			}
		} else
			throw new BusinessException("LMS-29132");

		return super.beforeStore(bean);
	}

	public Map storeMap(TypedFlatMap map) {
		RotaIntervaloCep bean = map2bean(map);

		vigenciaService.validaVigenciaBeforeStore(bean);

		super.store(bean);

		TypedFlatMap retorno = new TypedFlatMap();

		retorno.put("idRotaIntervaloCep", bean.getIdRotaIntervaloCep());
		retorno.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(bean));
		retorno.put("intervaloCep", bean.getNrCepInicial() + " - " + bean.getNrCepFinal());

		return retorno;
	}

	private RotaIntervaloCep map2bean(TypedFlatMap map){
		RotaIntervaloCep rotaIntervaloCep = new RotaIntervaloCep();

		rotaIntervaloCep.setIdRotaIntervaloCep(map.getLong("idRotaIntervaloCep"));
		rotaIntervaloCep.setDsBairro(map.getString("dsBairro"));
		rotaIntervaloCep.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));
		rotaIntervaloCep.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
		rotaIntervaloCep.setHrCorteExecucao(map.getTimeOfDay("hrCorteExecucao"));
		rotaIntervaloCep.setHrCorteSolicitacao(map.getTimeOfDay("hrCorteSolicitacao"));
		rotaIntervaloCep.setNrCepFinal(map.getString("nrCepFinal"));
		rotaIntervaloCep.setNrCepInicial(map.getString("nrCepInicial"));
		rotaIntervaloCep.setNrOrdemOperacao(map.getShort("nrOrdemOperacao"));
		rotaIntervaloCep.setTpGrauRisco(map.getDomainValue("tpGrauRisco"));
		rotaIntervaloCep.setBlAtendimentoTemporario(map.getBoolean("blAtendimentoTemporario"));

		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(map.getLong("municipio.idMunicipio"));
		rotaIntervaloCep.setMunicipio(municipio);

		Long idCliente = map.getLong("cliente.idCliente");
		if (idCliente != null) {
			Cliente cliente = new Cliente();
			cliente.setIdCliente(idCliente);
			rotaIntervaloCep.setCliente(cliente);
		}

		Long idEnderecoPessoa = map.getLong("enderecoCliente.idEnderecoPessoa");
		if (idEnderecoPessoa != null) {
			EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
			enderecoPessoa.setIdEnderecoPessoa(idEnderecoPessoa);
			rotaIntervaloCep.setEnderecoPessoa(enderecoPessoa);
		}

		RotaColetaEntrega rotaColetaEntrega = new RotaColetaEntrega();
		rotaColetaEntrega.setIdRotaColetaEntrega(map.getLong("rotaColetaEntrega.idRotaColetaEntrega"));
		rotaIntervaloCep.setRotaColetaEntrega(rotaColetaEntrega);

		Long idTipoDificuldadeAcesso = map.getLong("tipoDificuldadeAcesso.idTipoDificuldadeAcesso");
		if (idTipoDificuldadeAcesso != null) {
			TipoDificuldadeAcesso tipoDificuldadeAcesso = new TipoDificuldadeAcesso();
			tipoDificuldadeAcesso.setIdTipoDificuldadeAcesso(idTipoDificuldadeAcesso);
			rotaIntervaloCep.setTipoDificuldadeAcesso(tipoDificuldadeAcesso);
		}

		return rotaIntervaloCep;
	}

	private TypedFlatMap bean2map(RotaIntervaloCep bean){
		TypedFlatMap map = new TypedFlatMap();
		map.put("idRotaIntervaloCep", bean.getIdRotaIntervaloCep());

		RotaColetaEntrega rotaColetaEntrega = bean.getRotaColetaEntrega();
		Filial filialRotaColetaEntrega = rotaColetaEntrega.getFilial();
		map.put("rotaColetaEntrega.filial.idFilial", filialRotaColetaEntrega.getIdFilial());
		map.put("rotaColetaEntrega.filial.sgFilial", filialRotaColetaEntrega.getSgFilial());
		map.put("rotaColetaEntrega.filial.pessoa.nmFantasia", filialRotaColetaEntrega.getPessoa().getNmFantasia());

		map.put("rotaColetaEntrega.idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());
		map.put("rotaColetaEntrega.nrRota", rotaColetaEntrega.getNrRota());
		map.put("rotaColetaEntrega.dsRota", rotaColetaEntrega.getDsRota());

		Cliente cliente = bean.getCliente();
		if (cliente != null) {
			map.put("cliente.idCliente", cliente.getIdCliente());
			Pessoa pessoa = cliente.getPessoa();
			map.put("cliente.pessoa.nrIdentificacao", pessoa.getNrIdentificacao());
			map.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pessoa));
			map.put("cliente.pessoa.nmPessoa", pessoa.getNmPessoa());
		}

		EnderecoPessoa enderecoPessoa = bean.getEnderecoPessoa();
		if (enderecoPessoa != null) {
			map.put("enderecoCliente.idEnderecoPessoa", enderecoPessoa.getIdEnderecoPessoa());
			map.put("enderecoCliente.dsEndereco", enderecoPessoa.getDsEndereco());
		}

		Municipio municipio = bean.getMunicipio();
		map.put("municipio.idMunicipio", municipio.getIdMunicipio());
		map.put("municipio.nmMunicipio", municipio.getNmMunicipio());
		map.put("dsBairro", bean.getDsBairro());
		map.put("nrCepInicial", bean.getNrCepInicial());
		map.put("nrCepFinal", bean.getNrCepFinal());
		map.put("nrOrdemOperacao", bean.getNrOrdemOperacao());
		map.put("tpGrauRisco", bean.getTpGrauRisco().getValue());

		if (bean.getTipoDificuldadeAcesso() != null)
			map.put("tipoDificuldadeAcesso.idTipoDificuldadeAcesso", bean.getTipoDificuldadeAcesso().getIdTipoDificuldadeAcesso());

		map.put("hrCorteSolicitacao", bean.getHrCorteSolicitacao());
		map.put("hrCorteExecucao", bean.getHrCorteExecucao());
		map.put("dtVigenciaInicial", bean.getDtVigenciaInicial());
		map.put("dtVigenciaFinal", bean.getDtVigenciaFinal());
		map.put("blAtendimentoTemporario", bean.getBlAtendimentoTemporario());

		return map;	
	}

	/**
	 * Verifica se o intervalo de cep informado ja nao esta cadastrado para outra rota
	 * @param idRotaIntervaloCep
	 * @param nrCepInicial
	 * @param nrCepFinal
	 * @return TRUE se o intervalo ja esta cadastrado, FALSE caso contrario
	 */
	public boolean verificaIntervaloCep(Long idRotaIntervaloCep, String nrCepInicial, String nrCepFinal, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getRotaIntervaloCepDAO().verificaIntervaloCep(idRotaIntervaloCep, nrCepInicial, nrCepFinal, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Verifica se o cliente é atendido em outro intervalo de CEP sem endereço e que esteja vigente no período selecionado
	 * @param idRotaIntervaloCep
	 * @param nrCepInicial
	 * @param nrCepFinal
	 * @param cliente
	 * @return TRUE se o intervalo ja esta cadastrado, FALSE caso contrario
	 */
	public boolean verificaIntervaloCep(Long idRotaIntervaloCep, String nrCepInicial, String nrCepFinal, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Cliente cliente){
		return getRotaIntervaloCepDAO().verificaIntervaloCep(idRotaIntervaloCep, nrCepInicial, nrCepFinal, dtVigenciaInicial, dtVigenciaFinal, cliente);
	}

	/**
	 * Verifica se o cliente e o endereço são atendidos em outro intervalo de CEP e que esteja vigente no período selecionado
	 * @param idRotaIntervaloCep
	 * @param nrCepInicial
	 * @param nrCepFinal
	 * @param cliente
	 * @param enderecoPessoa
	 * @return TRUE se o intervalo ja esta cadastrado, FALSE caso contrario
	 */
	public boolean verificaIntervaloCep(Long idRotaIntervaloCep, String nrCepInicial, String nrCepFinal, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Cliente cliente, EnderecoPessoa enderecoPessoa){
		return getRotaIntervaloCepDAO().verificaIntervaloCep(idRotaIntervaloCep, nrCepInicial, nrCepFinal, dtVigenciaInicial, dtVigenciaFinal, cliente, enderecoPessoa);
	}
	
	public List findRotaIntervaloCepByCep(Long idMunicipio, String cep) {
		DetachedCriteria dc = DetachedCriteria.forClass(RotaIntervaloCep.class);
		dc.add(Restrictions.eq("municipio.idMunicipio", idMunicipio));
		dc.add(Restrictions.le("nrCepInicial", cep));
		dc.add(Restrictions.ge("nrCepFinal", cep));
		dc.setFetchMode("rotaColetaEntrega", FetchMode.JOIN);
		return getRotaIntervaloCepDAO().findByDetachedCriteria(dc);
	}

	// LMSA-6786
	public List<RotaIntervaloCep> findRotaIntervaloCepByCep(Filial filial, String cep) {
		return getRotaIntervaloCepDAO().findRotaIntervaloCepByCep(cep, filial.getIdFilial(), JTDateTimeUtils.getDataAtual());
	}

	public List findRotaIntervaloCepAtendimento(String cep, Long idCliente, Long idEnderecoPessoa, Long idFilial, YearMonthDay data) {
		return getRotaIntervaloCepDAO().findRotaIntervaloCepAtendimento(cep, idCliente, idEnderecoPessoa, idFilial, data);
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		return getRotaIntervaloCepDAO().findPaginatedCustom(criteria);
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getRotaIntervaloCepDAO().getRowCountCustom(criteria);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setRotaIntervaloCepDAO(RotaIntervaloCepDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private RotaIntervaloCepDAO getRotaIntervaloCepDAO() {
		return (RotaIntervaloCepDAO) getDao();
	}

	/**
	 * @param intervaloCepService The intervaloCepService to set.
	 */
	public void setIntervaloCepService(IntervaloCepService intervaloCepService) {
		this.intervaloCepService = intervaloCepService;
	}
	/**
	 * @param vigenciaService The vigenciaService to set.
	 */
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	/**
	 * @param rotaColetaEntregaService The rotaColetaEntregaService to set.
	 */
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	/**
	 * @param municipioFilialService The municipioFilialService to set.
	 */
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	
}