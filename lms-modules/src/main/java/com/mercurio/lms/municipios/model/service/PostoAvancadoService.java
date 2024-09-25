package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.PostoAvancado;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.PostoAvancadoDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.postoAvancadoService"
 */
public class PostoAvancadoService extends CrudService<PostoAvancado, Long> {

	private ClienteService clienteService;
	private EnderecoPessoaService enderecoPessoaService;
	private FilialService filialService;
	private VigenciaService vigenciaService;
	/**
	 * Recupera uma instância de <code>PostoAvancado</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public PostoAvancado findById(Long id) {
		return (PostoAvancado)super.findById(id);
	}
	
    public Map findByIdDetalhamento(java.lang.Long id) {
    	TypedFlatMap result = new TypedFlatMap();
    	PostoAvancado bean = (PostoAvancado)super.findById(id);

    	Filial filial = bean.getFilial();
		result.put("filial.idFilial",filial.getIdFilial());
		result.put("filial.sgFilial",filial.getSgFilial());
		result.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		
		result.put("idPostoAvancado",bean.getIdPostoAvancado());
		result.put("obPostoAvancado",bean.getObPostoAvancado());
		result.put("dsPostoAvancado",bean.getDsPostoAvancado());
		result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
		result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());
    	result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(bean));
    	
    	Usuario usuario = bean.getUsuario();
    	if (usuario != null) {
    		result.put("usuario.nrMatricula",usuario.getNrMatricula());
    		result.put("usuario.nmUsuario",usuario.getNmUsuario());
    		result.put("usuario.idUsuario",usuario.getIdUsuario());
    	}

    	Cliente cliente = bean.getCliente();    	
		if (cliente != null) {
			result.put("cliente.pessoa.nrIdentificacao",cliente.getPessoa().getNrIdentificacao());
			result.put("cliente.pessoa.nrIdentificacaoFormatado",cliente.getPessoa().getNrIdentificacaoFormatado());
			result.put("cliente.idCliente",cliente.getIdCliente());
			result.put("cliente.pessoa.nmPessoa",cliente.getPessoa().getNmPessoa());
    		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(cliente.getIdCliente());
    		if (enderecoPessoa != null) {
		    	Municipio municipio = enderecoPessoa.getMunicipio();
				UnidadeFederativa unidadeFederativa = municipio.getUnidadeFederativa();
				Pais pais = unidadeFederativa.getPais();
		    	result.put("cliente.endereco.bairro",enderecoPessoa.getDsBairro());
				result.put("cliente.endereco.cep",enderecoPessoa.getNrCep());
				result.put("cliente.endereco.uf",unidadeFederativa.getSgUnidadeFederativa());
				result.put("cliente.endereco.enderecoCompleto", this.enderecoPessoaService.getEnderecoCompleto(enderecoPessoa.getIdEnderecoPessoa()));
				result.put("cliente.endereco.municipio",municipio.getNmMunicipio());
				result.put("cliente.endereco.pais",pais.getNmPais());
    		}
    	}
    	return result;
    }

    
    protected PostoAvancado beforeStore(PostoAvancado bean) {
    	PostoAvancado pa = (PostoAvancado)bean;
    	if ((pa.getObPostoAvancado() == null || pa.getObPostoAvancado().trim().length() == 0) && pa.getCliente() == null)
    		throw new BusinessException("LMS-29006");  
    	
    	if (this.getPostoAvancadoDAO().verificaVigenciaPostoAvancado(pa))
    		throw new BusinessException("LMS-00003"); 
    	
    	this.getFilialService().verificaExistenciaHistoricoFilial(pa.getFilial().getIdFilial(), pa.getDtVigenciaInicial(), pa.getDtVigenciaFinal());    	
    	    	
    	return super.beforeStore(bean);
    }
    public List findPostoAvancadoByFilial(Long idFilial,YearMonthDay dtVigenciaFinal) {
    	return getPostoAvancadoDAO().findPostoAvancadoByFilial(idFilial,dtVigenciaFinal);
    }
    
    protected void beforeRemoveByIds(List ids) {
    	PostoAvancado pa = null;
    	for(int x = 0; x < ids.size(); x++) {
    		pa = findById((Long)ids.get(x));
	    	JTVigenciaUtils.validaVigenciaRemocao(pa);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	PostoAvancado pa = findById((Long)id);
    	JTVigenciaUtils.validaVigenciaRemocao(pa);
    	super.beforeRemoveById(id);
    }
    
    public List findLookupCliente(Map criteria) {
    	List rs = clienteService.findLookup(criteria); 
    	return setaEnderecoVigente(rs);
    }
    
    public Map findEnderecoCliente(Long idCliente) {
		EnderecoPessoa ep = enderecoPessoaService.getUltimoEndereco(idCliente);
		Map cliente = new HashMap();
		if (ep != null) {
    		Map result = new HashMap();
    		Map endereco = new HashMap();
    		result.put("enderecoCompleto",(new StringBuffer(ep.getDsEndereco())).append(", ").append(ep.getNrEndereco()).append(((ep.getDsComplemento() != null) ? " - " + ep.getDsComplemento() : "")).toString());
    		result.put("bairro",ep.getDsBairro());
    		result.put("municipio",ep.getMunicipio().getNmMunicipio());
    		result.put("uf",ep.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
    		result.put("pais",ep.getMunicipio().getUnidadeFederativa().getPais().getNmPais().getValue());
    		result.put("cep",ep.getNrCep());
    		endereco.put("endereco",result);
	    	cliente.put("cliente",endereco);
		}
		return cliente;
    }
    private List setaEnderecoVigente(List rs) {
    	if (rs == null || rs.size() < 1)
    		return rs;
    	List rsReturn = new ArrayList(rs.size());
    	Cliente bean;
    	EnderecoPessoa ep;
    	Map map = new HashMap();
    	for(int x = 0; x < rs.size(); x++) {
    		bean = (Cliente)rs.get(x);
    		bean.setAgendaTransferencias(null);
    		map = new HashMap();
    		ReflectionUtils.copyNestedBean(map,bean);
    		ep = enderecoPessoaService.getUltimoEndereco(bean.getIdCliente());
    		if (ep != null) {
	    		Map mapSub = new HashMap();
	    		mapSub.put("enderecoCompleto",(new StringBuffer(ep.getDsEndereco())).append(", ").append(ep.getNrEndereco()).append(((ep.getDsComplemento() != null) ? " - " + ep.getDsComplemento() : "")).toString());
	    		mapSub.put("bairro",ep.getDsBairro());
	    		mapSub.put("municipio",ep.getMunicipio().getNmMunicipio());
	    		mapSub.put("uf",ep.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
	    		mapSub.put("pais",ep.getMunicipio().getUnidadeFederativa().getPais().getNmPais().getValue());
	    		mapSub.put("cep",ep.getNrCep());
		    	map.put("endereco",mapSub);
    		}
    		rsReturn.add(map);
    	}
    	return rsReturn;
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
    
    public Integer getRowCount(TypedFlatMap criteria) {
    	return getPostoAvancadoDAO().getRowCount(criteria);
    }
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	FilterResultSetPage rspFiltred =  new FilterResultSetPage(getPostoAvancadoDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria))){
			public Map filterItem(Object item) {
				PostoAvancado bean = (PostoAvancado)item;
				TypedFlatMap result = new TypedFlatMap();
				result.put("filial.sgFilial",bean.getFilial().getSgFilial());
				result.put("dsPostoAvancado",bean.getDsPostoAvancado());
				result.put("idPostoAvancado",bean.getIdPostoAvancado());
				result.put("dtVigenciaInicial",bean.getDtVigenciaInicial());
				result.put("dtVigenciaFinal",bean.getDtVigenciaFinal());

				Cliente cliente = bean.getCliente();
				Usuario usuario = bean.getUsuario();
				if (usuario != null)
					result.put("usuario.nmUsuario",usuario.getNmUsuario());
				if (cliente != null)
					result.put("cliente.pessoa.nmPessoa",cliente.getPessoa().getNmPessoa());
				return result;
			}
		};
    	return (ResultSetPage)rspFiltred.doFilter();
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(PostoAvancado bean) {
        return super.store(bean);
    }

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public TypedFlatMap storeMap(Map bean) {
    	PostoAvancado postoAvancado = new PostoAvancado();

        ReflectionUtils.copyNestedBean(postoAvancado,bean);
        
        // Evitando referência a usuário sem id. Está sendo gerado por reflexão. 
        Usuario usuario = postoAvancado.getUsuario();
		if (usuario != null && usuario.getIdUsuario() == null) {
        	postoAvancado.setUsuario(null);
        }
        
        vigenciaService.validaVigenciaBeforeStore(postoAvancado);

        TypedFlatMap result = new TypedFlatMap();
        result.put("idPostoAvancado",(Long)super.store(postoAvancado));
        result.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(postoAvancado));
        
        return result;
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPostoAvancadoDAO(PostoAvancadoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PostoAvancadoDAO getPostoAvancadoDAO() {
        return (PostoAvancadoDAO) getDao();
    }

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}


	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}


	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	/**
	 * @return Returns the filialService.
	 */
	public FilialService getFilialService() {
		return filialService;
	}

	/**
	 * @param filialService The filialService to set.
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
   }