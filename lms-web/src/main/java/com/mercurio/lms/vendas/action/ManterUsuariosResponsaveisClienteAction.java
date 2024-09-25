package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.UsuarioClienteResponsavel;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.UsuarioClienteResponsavelService;

/**
 * 
 * @spring.bean id="lms.vendas.manterUsuariosResponsalveisClienteAction"
 * */
public class ManterUsuariosResponsaveisClienteAction extends CrudAction{

	private UsuarioClienteResponsavelService usuarioClienteResponsavelService;
	private ClienteService clienteService;
	private UsuarioService usuarioService;
	private UsuarioLMSService usuarioLMSService;
	private FilialService filialService;
	private PessoaService pessoaService;

	public ManterUsuariosResponsaveisClienteAction() {
		super();
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getUsuarioClienteResponsavelService().findPaginated(criteria);
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		List<Object[]> list = rsp.getList(); 
		for (Object[] obj : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)obj[1], obj[0].toString()));
			map.put("nmPessoa",obj[2]);
			Usuario usuario = usuarioService.findById(LongUtils.getLong(obj[3]));
			map.put("nmUsuarioResponsavel", usuario.getNmUsuario());
			map.put("nrMatricula", usuario.getNrMatricula());
			map.put("usuarioLogin", usuario.getLogin());
			map.put("sgFilial", obj[4]);
			map.put("dtVigenciaInicial",JTDateTimeUtils.formatDateYearMonthDayToString((YearMonthDay)obj[5]));
			YearMonthDay dtVigenciaFinal = obj[6]!= null ? (YearMonthDay) obj[6] : null;			
			map.put("dtVigenciaFinal",!dtVigenciaFinal.equals(JTDateTimeUtils.MAX_YEARMONTHDAY) ? JTDateTimeUtils.formatDateYearMonthDayToString(dtVigenciaFinal) : null);
			map.put("idUsuarioClienteResponsavel",obj[7]);
			newList.add(map);
		}
		rsp.setList(newList);		
		return rsp; 
	}
	
	public Integer getRowCount(Map criteria) {
		return getUsuarioClienteResponsavelService().getRowCount(criteria);
	}
	
	public List findLookupCliente(Map criteria){
    	List clientes = this.getClienteService().findLookup(criteria);
    	List retorno = null;
    	if(!clientes.isEmpty()) {
    		retorno = new ArrayList();
	    	for (Iterator iter = clientes.iterator(); iter.hasNext();) {
				Cliente cliente = (Cliente) iter.next();
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("idCliente", cliente.getIdCliente());
				typedFlatMap.put("tpCliente", cliente.getTpCliente().getValue());
				typedFlatMap.put("pessoa.idPessoa", cliente.getPessoa().getIdPessoa());
				typedFlatMap.put("pessoa.nmPessoa", cliente.getPessoa().getNmPessoa());
				typedFlatMap.put("pessoa.nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				typedFlatMap.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(cliente.getPessoa()));
				retorno.add(typedFlatMap);
			}
    	}
    	return retorno;
    }	
	
	public List findLookupUsuarioFuncionario(TypedFlatMap parameters) {

		Long idFilial = parameters.getLong("filial.idFilial");
		String nrMatricula = parameters.getString("nrMatricula");

		return usuarioService.findLookupUsuarioFuncionario(null,
				nrMatricula, idFilial, null, null, null, true);
	}

    public Serializable store(TypedFlatMap bean) {
    	validateUsuariosResponsaveisByCliente(bean);
    	UsuarioClienteResponsavel entity = null;
    	
    	if(bean.getLong("idUsuarioResponsavel") == null) {
    		
    		List<UsuarioClienteResponsavel> l = usuarioClienteResponsavelService.findByUsuarioByCliente(bean);
    		if(!l.isEmpty()){
    			for (UsuarioClienteResponsavel ucr : l) {
					if((ucr.getDtVigenciaInicial().equals(JTDateTimeUtils.getDataAtual()) ||
							ucr.getDtVigenciaInicial().isBefore(JTDateTimeUtils.getDataAtual())
							&& ucr.getDtVigenciaFinal().equals(JTDateTimeUtils.getDataAtual()))
							|| ucr.getDtVigenciaFinal().isAfter(JTDateTimeUtils.getDataAtual())
							|| ucr.getDtVigenciaInicial().isAfter(JTDateTimeUtils.getDataAtual())) {
						throw new BusinessException("LMS-01228");
					}
				}
    		}
    		
    		entity = new UsuarioClienteResponsavel();
    	} else {
    		entity = usuarioClienteResponsavelService.findById(bean.getLong("idUsuarioResponsavel"));
    	}
    	
		entity.setIdUsuarioClienteResponsavel(bean.getLong("idUsuarioResponsavel"));
		entity.setCliente(clienteService.findById(bean.getLong("cliente.idCliente")));
		entity.setUsuario(usuarioLMSService.findById(bean.getLong("usuarioResponsavel.idUsuario")));
		entity.setDtVigenciaInicial(bean.getYearMonthDay("dtVigenciaInicial"));
		if(bean.getYearMonthDay("dtVigenciaFinal") != null) {
			entity.setDtVigenciaFinal(bean.getYearMonthDay("dtVigenciaFinal"));
		} else {
			entity.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);
		}
		usuarioClienteResponsavelService.store(entity);
		
		if(entity.getDtVigenciaFinal().equals(JTDateTimeUtils.MAX_YEARMONTHDAY))
			entity.setDtVigenciaFinal(null);

		return entity;
    }

    public void validateUsuariosResponsaveisByCliente(TypedFlatMap parameters){
    	List<UsuarioClienteResponsavel> list = usuarioClienteResponsavelService.findUsuariosResponsaveisByCliente(parameters);
    	
    	Boolean permissaoSalvar = list.isEmpty();
    	
    	//Testa se o usuário logado é um usuário reponsavel pelo cliente
    	for (UsuarioClienteResponsavel usuarioResponsavel : list) {
			if(usuarioResponsavel.getUsuario().getIdUsuario().
					equals(SessionUtils.getUsuarioLogado().getIdUsuario())) {
				permissaoSalvar = Boolean.TRUE;
				break;
			}
		}
    	
    	if(!permissaoSalvar) {
    		throw new BusinessException("LMS-01227");
    	}
    	
    }
    
    public TypedFlatMap findFilialByCliente(TypedFlatMap parameters) {
    	TypedFlatMap tfm = new TypedFlatMap();
    	Filial f =  filialService.findByCliente(parameters.getLong("idCliente"));
    	
    	tfm.put("filial.sgFilial", f.getSgFilial());
    	tfm.put("filial.nmFilial", f.getPessoa().getNmFantasia());
    	
    	return tfm;
    }
    
    public UsuarioClienteResponsavel findById(Long id) {
    	UsuarioClienteResponsavel r = ((UsuarioClienteResponsavelService)defaultService).findById(id);
    	return r;
    }      
    
    public TypedFlatMap findByIdDetalhado(java.lang.Long id){
		return prepareUsuarioResponsavelByCliente(id);
    }
    
    private TypedFlatMap prepareUsuarioResponsavelByCliente(Long id) {
    	UsuarioClienteResponsavel usuarioClienteResponsavel = usuarioClienteResponsavelService.findById(id);
    	Cliente cliente = usuarioClienteResponsavel.getCliente();
    	Pessoa pessoa = pessoaService.findById(cliente.getIdCliente());
    	Usuario usuario = usuarioService.findById(usuarioClienteResponsavel.getUsuario().getIdUsuario());
    	Filial filial = filialService.findByCliente(cliente.getIdCliente());
    	
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idUsuarioResponsavel", usuarioClienteResponsavel.getIdUsuarioClienteResponsavel());
		retorno.put("cliente.idCliente", cliente.getIdCliente());
		retorno.put("cliente.pessoa.nmPessoa", pessoa.getNmPessoa());		
		retorno.put("cliente.pessoa.nrIdentificacao", pessoa.getNrIdentificacao());	
		retorno.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao()));
		retorno.put("usuarioResponsavel.nmUsuario", usuario.getNmUsuario());
		retorno.put("usuarioResponsavel.idUsuario", usuario.getIdUsuario());
		retorno.put("usuarioResponsavel.nrMatricula", usuario.getNrMatricula());
		retorno.put("filialAntendenteComercial.sgFilial", filial.getSgFilial());
		retorno.put("filialAntendenteComercial.nmFilial", filial.getPessoa().getNmFantasia());
		retorno.put("dtVigenciaInicial", usuarioClienteResponsavel.getDtVigenciaInicial());
		YearMonthDay dtVigenciaFinal = usuarioClienteResponsavel.getDtVigenciaFinal().equals(JTDateTimeUtils.MAX_YEARMONTHDAY) ? null : usuarioClienteResponsavel.getDtVigenciaFinal();
		retorno.put("dtVigenciaFinal", dtVigenciaFinal);
		retorno.put("idUsuarioClienteResponsavel", usuarioClienteResponsavel.getIdUsuarioClienteResponsavel());
		
		return retorno;
    }
    
	public void setUsuariosResponsaveisCliente(UsuarioClienteResponsavelService clienteResponsavelService) {
		this.defaultService = clienteResponsavelService;
	}

	/**
	 * @return the usuarioClienteResponsavelService
	 */
	public UsuarioClienteResponsavelService getUsuarioClienteResponsavelService() {
		return (UsuarioClienteResponsavelService) this.defaultService;
	}

	/**
	 * @param usuarioClienteResponsavelService the usuarioClienteResponsavelService to set
	 */
	public void setUsuarioClienteResponsavelService(
			UsuarioClienteResponsavelService usuarioClienteResponsavelService) {
		this.usuarioClienteResponsavelService = usuarioClienteResponsavelService;
	}

	/**
	 * @return the usuarioService
	 */
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	/**
	 * @param usuarioService the usuarioService to set
	 */
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	/**
	 * @return the usuarioLMSService
	 */
	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}

	/**
	 * @param usuarioLMSService the usuarioLMSService to set
	 */
	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/**
	 * @param pessoaService the pessoaService to set
	 */
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
    
}
