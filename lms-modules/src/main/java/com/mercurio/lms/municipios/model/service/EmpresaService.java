package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.tntbrasil.integracao.domains.autenticacao.AutenticacaoDMN;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.municipios.ConstantesMunicipios;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.dao.EmpresaDAO;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.empresaService"
 */
public class EmpresaService extends CrudService<Empresa, Long> {
	private PessoaService pessoaService;
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Recupera uma instância de <code>Empresa</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public Empresa findById(java.lang.Long id) {
		return (Empresa)super.findById(id);
	} 

	/**
	 * Recupera uma instância de <code>Empresa</code> a partir do ID.
	 * E formata o número de identificação.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public Empresa findByIdDetalhamento(java.lang.Long id) {
		return (Empresa)super.findById(id);
	}
	
	
	public Empresa findEmpresaByProprietario(Proprietario proprietario){
		
		return getEmpresaDAO().findEmpresaByProprietario(proprietario);
		
	}

	/**
	 * Sobrescrito método que retorna um resultSet para a grid.
	 * Retorna no XML apenas os campos que serão visualizados no grid.
	 * Retira máscara do múmero de identificação antes de realizar a consulta. 
	 */
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		removeFormatacaoIdentificacao(criteria);

		List included = new ArrayList();
		included.add("idEmpresa");
		included.add("tpEmpresa");
		included.add("pessoa.tpIdentificacao");
		included.add("pessoa.nrIdentificacao");
		included.add("pessoa.nrIdentificacaoFormatado");
		included.add("pessoa.nmPessoa");
		included.add("pessoa.nmFantasia");
		included.add("tpSituacao");
		included.add("sgEmpresa");

		ResultSetPage rsp = super.findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));

		return rsp;
	}

	public ResultSetPage findPaginatedFilial(Map criteria) {
		removeFormatacaoIdentificacao(criteria);

		if (criteria.get("tpEmpresa") == null || ((String)criteria.get("tpEmpresa")).length() == 0)
			criteria.put("tpEmpresa","..");
		Usuario usuarioSessao = SessionContext.getUser();
		if (usuarioSessao.getBlAdminFilial() != null && usuarioSessao.getBlAdminFilial() == true) {
			criteria.put("idUsuario", usuarioSessao.getIdUsuario().toString());
			return findEmpresasByUsuario( criteria, FindDefinition.createFindDefinition(criteria) );
		} else {
			return getEmpresaDAO().findPaginatedFilial(criteria,FindDefinition.createFindDefinition(criteria));
		}
	}

	/**
	 * Remove apenas a empresa.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Remove a empresa e tenta remover a pessoa.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 * 
	 */
	@Transactional(propagation = Propagation.NEVER)
	public void removeEmpresaById(Long id) {
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
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	@Transactional(propagation = Propagation.NEVER)
	public void removeByIds(List ids) {
		for (Iterator iterIds = ids.iterator(); iterIds.hasNext();) {
			Long id = (Long) iterIds.next();
			this.removeEmpresaById(id);
		}
	}

	/**
	 * Combo para Empresa.<BR>
	 * @param map
	 * @return
	 */
	public List findComboEmpresa(Map map){
		return getEmpresaDAO().findComboEmpresa(map);
	}

	@Override
	public List findLookup(Map criteria) {
		removeFormatacaoIdentificacao(criteria);

		Usuario usuarioSessao = SessionContext.getUser();
		if (usuarioSessao.getBlAdminFilial() != null
				&& usuarioSessao.getBlAdminFilial() == true) {
			return findEmpresaByUsuario( usuarioSessao, PessoaUtils.clearIdentificacao(MapUtils.getString(MapUtils.getMap(criteria, "pessoa"), "nrIdentificacao")));
		} else {
			return super.findLookup(criteria);
		}
	}
	
	public List findLookupEmpresaAwb(Map criteria) {
		return super.findLookup(criteria);
	}

	public List findLookupTypedFlatMap(TypedFlatMap criteria) {
		if (!StringUtils.isBlank(criteria.getString("pessoa.nrIdentificacao"))) {
			Map pessoa = new HashMap();
			pessoa.put("nrIdentificacao",PessoaUtils.clearIdentificacao(criteria.getString("pessoa.nrIdentificacao")));
			criteria.put("pessoa", pessoa);
			criteria.remove("pessoa.nrIdentificacao");
		}
		return super.findLookup(criteria);
	}

	public List findLookupFilial(Map criteria) {
		removeFormatacaoIdentificacao(criteria);
		return getEmpresaDAO().findLookupFilial(criteria);
	}

	public Map findDadosEmpresaById(Long id) {
		if(id == null || id.compareTo(Long.valueOf(0)) == 0) {
			throw new BusinessException("O id da empresa não foi informado.");
		}
		return getEmpresaDAO().findDadosEmpresaById(id);
	}
	
	/**
	 * Retorna apenas as empresas do tipo Cia Aérea.
	 * @param criteria
	 * @return
	 */
	public List findCiaAerea(Map criteria) {
		List listaOrder = new ArrayList();
		if(criteria == null)
			criteria = new HashMap();
		criteria.put("tpEmpresa", Empresa.TP_EMPRESA_CIA_AEREA);
		listaOrder.add("pessoa_.nmPessoa:asc");
		return getEmpresaDAO().findListByCriteria(criteria,listaOrder);
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

		List retorno = new ArrayList(1);

		Pessoa pessoa = validateEspecializacao(Empresa.class, tpIdentificacao, nrIdentificacao);
		if (pessoa != null) {
			TypedFlatMap row = new TypedFlatMap();
			row.put("idPessoa",pessoa.getIdPessoa());
			row.put("nmPessoa",pessoa.getNmPessoa());
			row.put("nmFantasia",pessoa.getNmFantasia());
			row.put("dsEmail",pessoa.getDsEmail());
			row.put("tpIdentificacao",pessoa.getTpIdentificacao());
			row.put("nrIdentificacao",pessoa.getNrIdentificacao());
			retorno.add(row);
		}
		return retorno;
	}

	public Pessoa validateEspecializacao(Class clazz, String tpIdentificacao, String nrIdentificacao) {
		Pessoa pessoa = configuracoesFacade.getPessoa(nrIdentificacao,tpIdentificacao);
		if (pessoa != null) {
			Object pessoaEspecializada = configuracoesFacade.getPessoa(pessoa.getIdPessoa(),clazz,false);
			if (pessoaEspecializada != null) {
				throw new BusinessException("LMS-29002");
			}
		}
		return pessoa;
	}

	public Integer getRowCountFilial(Map criteria) {
		if (criteria.get("tpEmpresa") == null || ((String)criteria.get("tpEmpresa")).length() == 0)
			criteria.put("tpEmpresa","..");
		return getEmpresaDAO().getRowCountFilial(criteria);
	}

	public List findByTpEmpresaTpSituacao(String tpEmpresa, String tpSituacao) {
		return getEmpresaDAO().findByTpEmpresaTpSituacao(tpEmpresa, tpSituacao);
	}

	public ResultSetPage findEmpresasByUsuario(Map criteria, FindDefinition findDef) {
		return getEmpresaDAO().findEmpresasByUsuario( criteria, findDef );
	}

	public List findEmpresasByUsuario(Usuario usuario ) {
		return getEmpresaDAO().findEmpresasByUsuario( usuario );
	}

	public List findEmpresaByUsuario(Usuario usuario, String nrIdentificacao) {
		return getEmpresaDAO().findEmpresaByUsuario(usuario, nrIdentificacao );
	}

	public Empresa findEmpresaPadraoByUsuario(Usuario usuario) {
		return getEmpresaDAO().findEmpresaPadraoByUsuario(usuario);
	}

	public Empresa findEmpresaLogadoById(Long idEmpresa) {
		return getEmpresaDAO().findEmpresaLogadoById(idEmpresa);
	}

	/**
	 * Método responsável por inserir ou editar um pojo Cedente
	 * @param bean
	 * @return Serializable
	 */
	@Override
	public java.io.Serializable store(Empresa bean) {
		return super.store(bean);
	}

	/**
	 * Seta os atributos da pessoa antes de salvar empresa
	 * @param bean entidade a ser armazenada (Empresa)
	 * @return entidade que foi armazenada. 
	 */
	@Override
	public Empresa beforeStore(Empresa bean) {
		Empresa empresa = bean;

		if (empresa.getTpEmpresa().equals(new DomainValue("C"))){
			if(empresa.getCdIATA() == null) {
				throw new BusinessException("LMS-29123");
			}
		}
		pessoaService.store(empresa);
		return empresa;
	}

	public List findByUsuarioLogado(TypedFlatMap m) {
		List<Object[]> list = this.getEmpresaDAO().findByUsuarioLogado(m);
		List<TypedFlatMap> result = new ArrayList<TypedFlatMap>(list.size());
		for(Object[] object : list) {
			TypedFlatMap row = new TypedFlatMap();
			row.put("idEmpresa", object[0]);
			row.put("pessoa.nmPessoa", object[1]);
			result.add(row);
		}
		return result;
	}

	public List findEmpresaByUsuarioAutenticado(AutenticacaoDMN autenticacaoDMN) {
		List<Object[]> list = this.getEmpresaDAO().findEmpresaByUsuarioAutenticado(autenticacaoDMN);
		List<TypedFlatMap> result = new ArrayList<TypedFlatMap>(list.size());
		for(Object[] object : list) {
			TypedFlatMap row = new TypedFlatMap();
			row.put("idEmpresa", object[0]);
			row.put("nmEmpresa", object[1]);
			row.put("nrIdentificacao", object[2]);
			result.add(row);
		}
		return result;
	}

	public List findByUsuarioLogadoList(TypedFlatMap m) {
		List<Object[]> list = this.getEmpresaDAO().findByUsuarioLogado(m);
		return list;
	}

	/**
	 * Retorna o id da empresa da filial passada por parâmetro
	 * @param idFilial
	 * @return idEmpresa
	 */
	public Long findIdEmpresaByIdFilial(Long idFilial) {
		return this.getEmpresaDAO().findIdEmpresaByIdFilial(idFilial);
	}

	/**
	 * Retorna 'true' se a pessoa informada é uma empresa ativa senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isEmpresa(Long idPessoa){
		return getEmpresaDAO().isEmpresa(idPessoa);
	}
	
	/**
	 * Remove a formatação do campo 'pessoa.nrIdentificacao'.
	 * 
	 * @param criteria mapa contendo um mapa de pessoa
	 */
	private void removeFormatacaoIdentificacao(Map criteria) {
		Map pessoa = (Map) criteria.get("pessoa");
		if (pessoa != null) {
			pessoa.put("nrIdentificacao", PessoaUtils.clearIdentificacao((String) pessoa.remove("nrIdentificacao")));
		}
	}

	/**
	 * Retorna dados da empresa Mercúrio
	 * 
	 * @return Map com dados da empresa Mercúrio
	 */
	public Empresa findEmpresaMercurio() {
		String nrIdentificacao = (String)configuracoesFacade.getValorParametro(ConstantesMunicipios.NR_IDENTIFICACAO_MERCURIO);
		return getEmpresaDAO().findEmpresa(nrIdentificacao);
	}
	
	public Empresa findEmpresaByIndetificador(String nrIdentificador) {
		return getEmpresaDAO().findEmpresa(nrIdentificador);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setEmpresaDAO(EmpresaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private EmpresaDAO getEmpresaDAO() {
		return (EmpresaDAO) getDao();
	}

	/**
	 * @param pessoaService O pessoaService a ser definido.
	 */
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public boolean isEmpresaParceira(Long idProprietario){
		return getEmpresaDAO().isEmpresaParceira(idProprietario);
	}

}
