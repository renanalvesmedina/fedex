package com.mercurio.lms.carregamento.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.PrestadorServico;
import com.mercurio.lms.carregamento.model.dao.PrestadorServicoDAO;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.prestadorServicoService"
 */
public class PrestadorServicoService extends CrudService<PrestadorServico, Long> {
	private PessoaService pessoaService;
	private ConfiguracoesFacade configuracoesFacade;
	
	/**
	 * Valida identificação dO Prestador de Serviço.
	 * A Identificação deve ser de Pessoa Física e não pode já estar cadastrada.
	 * @see verificaExistenciaPrestadorServico
	 * @see verificaExistenciaPessoa
	 * @param parametros deve conter pessoa.tpIdentificacao e pessoa.nrIdentificacao; idPrestadorServico, é opcional (para atualizacao)
	 * @return Pessoa com identificação validada
	 */
	public Pessoa validateIdentificacao(Map<String, Object> map) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.putAll(map);
		PrestadorServico prestadorServico = new PrestadorServico();

		Long idPrestadorServico = tfm.getLong("idPrestadorServico");
		if (idPrestadorServico != null){
			prestadorServico.setIdPrestadorServico(idPrestadorServico);
		}

		Pessoa p = new Pessoa();
		p.setTpIdentificacao(new DomainValue(tfm.getString("pessoa.tpIdentificacao") ));
		p.setNrIdentificacao(tfm.getString("pessoa.nrIdentificacao"));
		prestadorServico.setPessoa(p);

		verificaExistenciaPrestadorServico(prestadorServico);
		
		Pessoa pessoa = verificaExistenciaPessoa(prestadorServico);
		return pessoa;
	}

	/**
	 * Verifica se já existe um Prestador de Serviço com o Número e Tipo de Identificação informado.
	 * @param map deve conter o numero e tipo de identificação; 
	 */
	private void verificaExistenciaPrestadorServico(PrestadorServico prestadorServico){
		if (getPrestadorServicoDAO().verificaExistenciaPrestadorServico(prestadorServico)){
			throw new BusinessException("LMS-05032");	
		}
	}
	
	/**
	 * Verifica se já existe uma Pessoa e uma prestadorServico cobranca com o Número e Tipo de Identificação informado.
	 * @param map deve conter o numero e tipo de identificação; 
	 */
	public Pessoa validatePrestadorServico(Map<String, Object> map) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.putAll(map);

		Pessoa pessoa = configuracoesFacade.getPessoa(tfm.getString("nrIdentificacao"),tfm.getString("tpIdentificacao"));
		//Map pessoa = pessoaService.findByIdentificacao(tfm.getString("tpIdentificacao"),tfm.getString("nrIdentificacao"), "F");
		if(pessoa != null){
			/** Valida se já existe uma prestadorServico cobrança para a pessoa em questão*/
			if(getPrestadorServicoDAO().findPrestadorServicoById(pessoa.getIdPessoa())){
				throw new BusinessException("LMS-05032");	
			}
			return pessoa;
		}
		return null;
	}
	
	/**
	 * @see verificaExistenciaPessoa(Map map)
	 * @param prestadorServico
	 * @return
	 */
	public Pessoa verificaExistenciaPessoa(PrestadorServico prestadorServico){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("nrIdentificacao",prestadorServico.getPessoa().getNrIdentificacao());
		parameters.put("tpIdentificacao",prestadorServico.getPessoa().getTpIdentificacao().getValue());
		return verificaExistenciaPessoa(parameters);
	}
	
	/**
	 * Verifica existência de Pessoa.
	 * A pessoa deve ser do tipo física, caso contrário é lançada uma exceção
	 * @param map
	 * @return Pessoa
	 */
	public Pessoa verificaExistenciaPessoa(Map<String, Object> map){
		List<Pessoa> pessoas = pessoaService.find(map);
		if (!pessoas.isEmpty()) {
			Pessoa pessoa = pessoas.get(0);
			if (!"F".equals(pessoa.getTpPessoa().getValue())){
				throw new BusinessException("LMS-05033");
			}
			return pessoa;
		}
		return null;
	}

	/**
	 * @param prestadorServico
	 */
	private void validateDataInclusao(PrestadorServico prestadorServico){
		if ( "T".equals( prestadorServico.getPessoa().getTpPessoa().getValue() ) ){
			prestadorServico.getPessoa().setDhInclusao(JTDateTimeUtils.getDataHoraAtual());	
		}
	}
	
	/**
	 * Store que realiza um store da Pessoa, antes de salvar/atualizar dados. Também é realizado um teste com o Tipo de Pessoa,
	 * permitindo Store de apenas Pessoas Júridicas.
	 */
	@Override
	protected PrestadorServico beforeStore(PrestadorServico bean) {
		verificaExistenciaPrestadorServico(bean);
		verificaExistenciaPessoa(bean);
		pessoaService.store(bean);
		return super.beforeStore(bean);
	}

	/**
	 * 
	 */
	protected PrestadorServico beforeInsert(PrestadorServico bean) {
		validateDataInclusao((PrestadorServico) bean);
		return super.beforeInsert(bean);
	}
	
	/**
	 * Recupera uma instância de <code>PrestadorServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public PrestadorServico findById(java.lang.Long id) {
		PrestadorServico bean = (PrestadorServico)super.findById(id);
		bean.getPessoa().setNrIdentificacao( FormatUtils.formatIdentificacao(bean.getPessoa()) );
		return bean;
	}

	/**
	 * Apaga uma entidade através do Id.
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * @param id
	 *
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removePrestadorServicoById(java.lang.Long id) {
		this.removeById(id);
		try{
			pessoaService.removeById(id);	
		}catch (Exception e){
			//ignora de Pessoa
		}
	}

	/**
	 * @param id
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removePrestadorServicoByIds(List<Long> ids) {
		this.removeByIds(ids);
		for(Long id : ids) {
			try {
				pessoaService.removeById(id);	
			}catch(Exception e) {
				//sem tratamentos, simplesmente ignora. Pois, provavelmente, há dependencias da Pessoa.
			}
		}
	}

	/**
	 * Apaga várias entidades através do Id.
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 */
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
	public java.io.Serializable store(PrestadorServico bean) {
		return super.store(bean);
	}
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPrestadorServicoDAO(PrestadorServicoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private PrestadorServicoDAO getPrestadorServicoDAO() {
		return (PrestadorServicoDAO) getDao();
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}