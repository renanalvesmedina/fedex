package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.Zona;
import com.mercurio.lms.municipios.model.dao.AeroportoDAO;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.aeroportoService"
 */
public class AeroportoService extends CrudService<Aeroporto, Long> {
	private PessoaService pessoaService;
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Recupera uma instância de <code>Aeroporto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Aeroporto findById(java.lang.Long id) {
		return (Aeroporto)super.findById(id);
	}

	public TypedFlatMap findByIdDetalhado(java.lang.Long id) {
		TypedFlatMap tfm = new TypedFlatMap();
		Aeroporto aeroporto = (Aeroporto)super.findById(id);;
	
		tfm.put("idAeroporto",aeroporto.getIdAeroporto());
		tfm.put("sgAeroporto",aeroporto.getSgAeroporto());
		tfm.put("cdCidade",aeroporto.getCdCidade());
		tfm.put("siglaDescricao",aeroporto.getSiglaDescricao());
		tfm.put("blTaxaTerrestreObrigatoria",aeroporto.getBlTaxaTerrestreObrigatoria());
		tfm.put("tpSituacao",aeroporto.getTpSituacao().getValue());
		tfm.put("pessoa.idPessoa", aeroporto.getPessoa().getIdPessoa());
		tfm.put("pessoa.tpPessoa", aeroporto.getPessoa().getTpPessoa().getValue());

		if (aeroporto.getPessoa().getNrIdentificacao() != null){
			tfm.put("pessoa.nrIdentificacao", aeroporto.getPessoa().getNrIdentificacao());
		} else tfm.put("pessoa.nrIdentificacao","");
		if (aeroporto.getPessoa().getTpIdentificacao() != null){
			tfm.put("pessoa.tpIdentificacao", aeroporto.getPessoa().getTpIdentificacao().getValue());
		} else tfm.put("pessoa.tpIdentificacao","");
		tfm.put("pessoa.nmPessoa", aeroporto.getPessoa().getNmPessoa());

		if (aeroporto.getPessoa().getNmFantasia() != null)
			tfm.put("pessoa.nmFantasia", aeroporto.getPessoa().getNmFantasia());
		if (aeroporto.getPessoa().getDsEmail() != null)
		tfm.put("pessoa.dsEmail", aeroporto.getPessoa().getDsEmail());

		Filial filial = aeroporto.getFilial();
		if (filial != null) {
			tfm.put("filial.idFilial",filial.getIdFilial());
			tfm.put("filial.sgFilial",filial.getSgFilial());
			tfm.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		}
		return tfm;
	}
 
	/* Cria lista para trazer no bean apenas os campos apresentados no Grid. 
	 * @see com.mercurio.adsm.framework.model.CrudService#findPaginated(java.util.Map)
	 */
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) { 
		
		String nrIdentificacaoMap = criteria.getString("pessoa.nrIdentificacao");
		
		if (nrIdentificacaoMap != null){
			criteria.put("pessoa.nrIdentificacao",FormatUtils.filterNumber(nrIdentificacaoMap));
		}	

		ResultSetPage rsp = getAeroportoDAO().findPaginatedCustom(criteria,FindDefinition.createFindDefinition(criteria));

		List<Aeroporto> aeroportos = rsp.getList();
		
		List<TypedFlatMap> newList = new ArrayList<TypedFlatMap>();
		for (Aeroporto aeroporto : aeroportos) {

			Pessoa pessoa = aeroporto.getPessoa();
			Filial filial = aeroporto.getFilial();
			EnderecoPessoa endereco = pessoa.getEnderecoPessoa();
			
			TypedFlatMap row = new TypedFlatMap();
			row.put("idAeroporto",aeroporto.getIdAeroporto());
			row.put("pessoa.tpIdentificacao",pessoa.getTpIdentificacao());
			row.put("pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
			row.put("pessoa.nrIdentificacaoFormatado",FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao()));
			row.put("pessoa.nmPessoa",pessoa.getNmPessoa());
			row.put("sgAeroporto",aeroporto.getSgAeroporto());
			row.put("cdCidade",aeroporto.getCdCidade());
			row.put("tpSituacao",aeroporto.getTpSituacao());

			if (filial != null) {
				row.put("filial.sgFilial",filial.getSgFilial());
				row.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
			}

			if (endereco != null) {
				Municipio municipio = endereco.getMunicipio();
				UnidadeFederativa uf = municipio.getUnidadeFederativa();
				Pais pais = uf.getPais();
				Zona zona = pais.getZona();
				row.put("municUltimoEndereco",municipio.getNmMunicipio());
				row.put("endereco.municipio.unidadeFederativa.pais.idPais",pais.getIdPais());
				row.put("endereco.municipio.unidadeFederativa.pais.nmPais",pais.getNmPais());
				row.put("endereco.municipio.unidadeFederativa.pais.zona.idZona",zona.getIdZona());
				row.put("endereco.municipio.unidadeFederativa.pais.zona.dsZona",zona.getDsZona());
				row.put("endereco.municipio.unidadeFederativa.idUnidadeFederativa",uf.getIdUnidadeFederativa());	
			}
			newList.add(row);			
			
			}
		rsp.setList(newList);

		return rsp;
	}

	public Integer getRowCountCustom(TypedFlatMap criteria){
		return getAeroportoDAO().getRowCountCustom(criteria);
	}

	/**
	 * Busca aeroportos pela situação e sigla do aeroporto
	 * @param criteria
	 * @return Lista de maps com informações do aeroporto
	 */
	public List findLookupAeroporto(Map<String, Object> criteria) {
		String tpSituacao = (String)criteria.get("tpSituacao");
		String sgAeroporto = (String)criteria.get("sgAeroporto");
		return getAeroportoDAO().findLookupAeroporto(sgAeroporto, tpSituacao);
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
	 * Remove o aeroporto e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeAeroportoById(Long id) {
		this.removeById(id);
		try {
			pessoaService.removeById(id); 
		} catch (Exception e) {
			// ignora erros de FK na pessoa
		}	
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades quee deverão ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids) {
			this.removeAeroportoById(id);
		}
	}

	/**
	 * Verifica se a pessoa especificada é Jurídica.
	 * Em uma inclusão: Verifica se existe outra pessoa com o mesmo tipo e número de identificação informados.
	 * @param criteria
	 * @return
	 */
	public List validateIdentificacao(TypedFlatMap map) {
		String tpIdentificacao = map.getString("tpIdentificacao");
		String nrIdentificacao = map.getString("nrIdentificacao");

		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>(1);
		Pessoa pessoa = validateEspecializacao(Aeroporto.class, tpIdentificacao, nrIdentificacao);
		if (pessoa != null) {
			TypedFlatMap row = new TypedFlatMap();
			row.put("idPessoa",pessoa.getIdPessoa());
			row.put("nmPessoa",pessoa.getNmPessoa());
			row.put("dsEmail",pessoa.getDsEmail());
			row.put("tpIdentificacao",pessoa.getTpIdentificacao());
			row.put("nrIdentificacao",pessoa.getNrIdentificacao());	
			row.put("nmFantasia",pessoa.getNmFantasia());
			retorno.add(row);
		}
		return retorno;
	}
	
	private Pessoa validateEspecializacao(Class clazz, String tpIdentificacao, String nrIdentificacao) {
		Pessoa pessoa = configuracoesFacade.getPessoa(nrIdentificacao,tpIdentificacao);
		
		if (pessoa != null) {
			Object pessoaEspecializada = configuracoesFacade.getPessoa(pessoa.getIdPessoa(),clazz,false);
			if (pessoaEspecializada != null) {
				throw new BusinessException("LMS-29003");
			}
		}
		return pessoa;
	}

	public Aeroporto findBySgAeroportoAndTpSituacaoAtivo(String sgAeroporto) {
		return getAeroportoDAO().findBySgAeroportoAndTpSituacao(sgAeroporto, "A");
		
	}	
	
	/**
	 * Método responsável por inserir ou editar um pojo Aeroporto
	 * @param bean
	 * @return Serializable
	 */
	public java.io.Serializable store(Aeroporto bean) {
		return super.store(bean);
	}

	/**
	 * Seta os atributos da pessoa antes de salvar aeroporto
	 * @param bean entidade a ser armazenada (Aeroporto)
	 * @return entidade que foi armazenada. 
	 */
	@Override
	public Aeroporto beforeStore(Aeroporto bean) {
		pessoaService.store(bean);
		return bean;
	}

	/**
	 * Retorna 'true' se a pessoa informada é um aeroporto ativo senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isAeroporto(Long idPessoa){
		return getAeroportoDAO().isAeroporto(idPessoa);
	}
	 
	/**
	 * @see getAeroportoDAO().findBySgAeroporto(sgAeroporto)
	 * @param sgAeroporto
	 * @return
	 */
	public Aeroporto findBySgAeroporto(String sgAeroporto){
		return getAeroportoDAO().findBySgAeroporto(sgAeroporto);
	}
	
	public List<Filial> findByIdFilial(Long idFilial) {
		return getAeroportoDAO().findByIdFilial(idFilial);
	}
	
	public Aeroporto findAeroportoAtendeCliente(Long idCliente){
	    return getAeroportoDAO().findAeroportoAtendeCliente(idCliente);
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setAeroportoDAO(AeroportoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private AeroportoDAO getAeroportoDAO() {
		return (AeroportoDAO) getDao();
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}