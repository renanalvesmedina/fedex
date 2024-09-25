package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ContaBancariaService;
import com.mercurio.lms.contratacaoveiculos.model.Beneficiario;
import com.mercurio.lms.contratacaoveiculos.model.BeneficiarioProprietario;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.dao.BeneficiarioProprietarioDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;


/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.beneficiarioProprietarioService"
 */
public class BeneficiarioProprietarioService extends CrudService<BeneficiarioProprietario, Long> {
	private BeneficiarioService beneficiarioService;
	private ContaBancariaService contaBancariaService;
	private ConfiguracoesFacade configuracoesFacade;
	private VigenciaService vigenciaService;

	/**
	 * Recupera uma instância de <code>BeneficiarioProprietario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public BeneficiarioProprietario findById(java.lang.Long id) {
		return (BeneficiarioProprietario)super.findById(id);
	}
 
	public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
		BeneficiarioProprietario beneficiarioProprietario = (BeneficiarioProprietario) super.findById(id);

		final Beneficiario beneficiario = beneficiarioProprietario.getBeneficiario();
		final Proprietario proprietario = beneficiarioProprietario.getProprietario();

		Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(beneficiarioProprietario);
		TypedFlatMap mapBeneficiarioProprietario = new TypedFlatMap(); 
		mapBeneficiarioProprietario.put("acaoVigenciaAtual", acaoVigencia);

		mapBeneficiarioProprietario.put("beneficiario.pessoa.idPessoa", beneficiario.getPessoa().getIdPessoa());
		mapBeneficiarioProprietario.put("beneficiario.pessoa.nmPessoa", beneficiario.getPessoa().getNmPessoa());
		mapBeneficiarioProprietario.put("beneficiario.pessoa.dsEmail", beneficiario.getPessoa().getDsEmail());
		mapBeneficiarioProprietario.put("beneficiario.pessoa.tpIdentificacao",beneficiario.getPessoa().getTpIdentificacao().getValue());
		mapBeneficiarioProprietario.put("beneficiario.pessoa.nrIdentificacao", beneficiario.getPessoa().getNrIdentificacao());
		mapBeneficiarioProprietario.put("beneficiario.pessoa.tpPessoa", beneficiario.getPessoa().getTpPessoa().getValue());
		mapBeneficiarioProprietario.put("proprietario.pessoa.idPessoa", proprietario.getPessoa().getIdPessoa());
		mapBeneficiarioProprietario.put("proprietario.pessoa.nmPessoa", proprietario.getPessoa().getNmPessoa());
		mapBeneficiarioProprietario.put("proprietario.pessoa.tpIdentificacao",proprietario.getPessoa().getTpIdentificacao().getValue());
		mapBeneficiarioProprietario.put("proprietario.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(proprietario.getPessoa().getTpIdentificacao().getValue(),proprietario.getPessoa().getNrIdentificacao()));
		mapBeneficiarioProprietario.put("beneficiario.idBeneficiario", beneficiario.getIdBeneficiario());
		mapBeneficiarioProprietario.put("proprietario.idProprietario", proprietario.getIdProprietario());
		mapBeneficiarioProprietario.put("idBeneficiarioProprietario", beneficiarioProprietario.getIdBeneficiarioProprietario());
		mapBeneficiarioProprietario.put("dtVigenciaInicial", beneficiarioProprietario.getDtVigenciaInicial()); 
		mapBeneficiarioProprietario.put("dtVigenciaFinal", beneficiarioProprietario.getDtVigenciaFinal());

		return mapBeneficiarioProprietario;
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(Long id) {
		BeneficiarioProprietario beneficiarioProprietario = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(beneficiarioProprietario);

		Long idBeneficiario = beneficiarioProprietario.getBeneficiario().getIdBeneficiario();
		List contas = contaBancariaService.findContasBancariasByPessoa(idBeneficiario);
		if (contas != null && !contas.isEmpty()){
			getBeneficiarioProprietarioDAO().getAdsmHibernateTemplate().deleteAll(contas);
		}

		super.removeById(id);
		beneficiarioService.removeBeneficiarioById(idBeneficiario);
	}

	protected BeneficiarioProprietario beforeInsert(BeneficiarioProprietario bean) {
		BeneficiarioProprietario beneficiarioProprietario = (BeneficiarioProprietario) bean;
		Beneficiario beneficiario = beneficiarioProprietario.getBeneficiario();

		if (beneficiario.getPessoa().getIdPessoa() != null){
			Object pessoaEspecializada = configuracoesFacade.getPessoa(beneficiario.getPessoa().getIdPessoa(), Proprietario.class, false);
			if (pessoaEspecializada != null) {
				throw new BusinessException("LMS-26068");
			}
		}
		return super.beforeInsert(bean);
	}

	protected BeneficiarioProprietario beforeStore(BeneficiarioProprietario bean) {
		BeneficiarioProprietario beneficiarioProprietario = (BeneficiarioProprietario) bean;

		if ( this.verificaExisteVigenciaBeneficiario(beneficiarioProprietario)) {
			throw new BusinessException("LMS-00003");
		}

		return super.beforeStore(bean);
	}

	private boolean verificaExisteVigenciaBeneficiario(BeneficiarioProprietario bean){
		return this.getBeneficiarioProprietarioDAO().verificaExisteVigencia(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(BeneficiarioProprietario bean) {
		beneficiarioService.store(bean.getBeneficiario());
		return super.store(bean);
	} 

	public Map storeMap(TypedFlatMap map) {
		String nrBeneficiario = map.getString("beneficiario.pessoa.nrIdentificacao");
		String nrProprietario = map.getString("proprietario.pessoa.nrIdentificacao");

		nrBeneficiario = FormatUtils.filterNumber(nrBeneficiario);
		nrProprietario = FormatUtils.filterNumber(nrProprietario);

		if (nrBeneficiario.equals(nrProprietario)) {
			throw new BusinessException("LMS-26026");
		} 		

		BeneficiarioProprietario beneficiarioProprietario = new BeneficiarioProprietario();
		beneficiarioProprietario.setIdBeneficiarioProprietario(map.getLong("idBeneficiarioProprietario"));

		Beneficiario beneficiario = new Beneficiario();
		Pessoa pessoa = new Pessoa();

		beneficiario.setIdBeneficiario(map.getLong("beneficiario.idBeneficiario"));		
		pessoa.setIdPessoa(map.getLong("beneficiario.pessoa.idPessoa"));
		pessoa.setTpPessoa(new DomainValue(map.getString("beneficiario.pessoa.tpPessoa")));
		pessoa.setTpIdentificacao(new DomainValue(map.getString("beneficiario.pessoa.tpIdentificacao")));		
		pessoa.setNrIdentificacao(nrBeneficiario);
		pessoa.setNmPessoa(map.getString("beneficiario.pessoa.nmPessoa"));
		pessoa.setDsEmail(map.getString("beneficiario.pessoa.dsEmail"));
		beneficiario.setPessoa(pessoa);
		beneficiarioProprietario.setBeneficiario(beneficiario);

		Proprietario proprietario = new Proprietario();
		proprietario.setIdProprietario(map.getLong("proprietario.idProprietario"));
		beneficiarioProprietario.setProprietario(proprietario);		

		beneficiarioProprietario.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
		beneficiarioProprietario.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));

		vigenciaService.validaVigenciaBeforeStore(beneficiarioProprietario);

		beneficiarioService.store(beneficiarioProprietario.getBeneficiario());

		Map retorno = new TypedFlatMap();

		if ((existeContaBancariaVigentePeriodo(beneficiarioProprietario.getProprietario().getIdProprietario(), beneficiarioProprietario.getDtVigenciaInicial(), beneficiarioProprietario.getDtVigenciaFinal()))){
			throw new BusinessException("LMS-26023");
		} else if (!existeContaBancariaVigentePeriodo(beneficiarioProprietario.getBeneficiario().getIdBeneficiario(), beneficiarioProprietario.getDtVigenciaInicial(), beneficiarioProprietario.getDtVigenciaFinal())){
			String msg = configuracoesFacade.getMensagem("LMS-26061");
			retorno.put("msgContaBancaria", msg);
		}

		super.store(beneficiarioProprietario);

		retorno.put("idBeneficiarioProprietario", beneficiarioProprietario.getIdBeneficiarioProprietario());
		retorno.put("beneficiario.pessoa.idPessoa", beneficiarioProprietario.getBeneficiario().getIdBeneficiario());
		retorno.put("beneficiario.idBeneficiario", beneficiarioProprietario.getBeneficiario().getIdBeneficiario());
		retorno.put("dtVigenciaInicial", beneficiarioProprietario.getDtVigenciaInicial());
		retorno.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(beneficiarioProprietario));

		return retorno;
	}

	public boolean existeContaBancariaVigentePeriodo(Long idPessoa, YearMonthDay dtInicial, YearMonthDay dtFinal) {
		return contaBancariaService.existeContaBancariaVigentePeriodo(idPessoa, dtInicial, dtFinal); 
	}


	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setBeneficiarioProprietarioDAO(BeneficiarioProprietarioDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private BeneficiarioProprietarioDAO getBeneficiarioProprietarioDAO() {
		return (BeneficiarioProprietarioDAO) getDao();
	}

	public boolean findBeneficiarioProprietarioByVigencias(Long idBeneficiario,YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getBeneficiarioProprietarioDAO().findBeneficiarioProprietarioByVigencias(idBeneficiario,dtVigenciaInicial,dtVigenciaFinal);
	}

	/**
	 * Retorna BeneficiarioProprietario vigente.
	 * @param idBeneficiario
	 * @param idProprietario
	 * @return
	 */
	public List findBeneficiarioProprietarioVigente(Long idBeneficiario, Long idProprietario) {
		YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();
		return findBeneficiarioProprietario(idBeneficiario, idProprietario, dtToday, dtToday);
	}

	/**
	 * Retorna BeneficiarioProprietario a partir da vigência.
	 * 
	 * @param idBeneficiario
	 * @param idProprietario
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public List findBeneficiarioProprietario(Long idBeneficiario, Long idProprietario, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		if( (idBeneficiario == null) && (idProprietario == null) ) {
			throw new IllegalArgumentException("The arguments 'idBeneficiario' and 'idProprietario' cannot be null.");
		}
		return getBeneficiarioProprietarioDAO().findBeneficiarioProprietario(idBeneficiario, idProprietario, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * 
	 * @param idProprietario
	 * @return
	 */
	public List findBeneficiarioByIdProprietario(Long idProprietario){
		return getBeneficiarioProprietarioDAO().findBeneficiarioByIdProprietario(idProprietario);
	}

	/**
	 * @see getBeneficiarioProprietarioDAO().findBeneficiarioProprietarioByProprietarioVigenciaFinal(idProprietario, dtAtual)
	 * @param idProprietario
	 * @param dtAtual
	 * @return
	 */
	public BeneficiarioProprietario findBeneficiarioProprietarioByProprietarioVigenciaFinal(Long idProprietario, YearMonthDay dtAtual){
		return getBeneficiarioProprietarioDAO().findBeneficiarioProprietarioByProprietarioVigenciaFinal(idProprietario, dtAtual);
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setBenefeciarioService(BeneficiarioService benefeciarioService) {
		this.beneficiarioService = benefeciarioService;
	}
	public void setContaBancariaService(ContaBancariaService contaBancariaService) {
		this.contaBancariaService = contaBancariaService;
	}

}