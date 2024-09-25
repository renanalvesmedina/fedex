package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.vendas.model.GerenciaRegional;
import com.mercurio.lms.vendas.model.MunicipioRegionalCliente;
import com.mercurio.lms.vendas.model.dao.GerenciaRegionalDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.gerenciaRegionalService"
 */
public class GerenciaRegionalService extends CrudService<GerenciaRegional, Long> {
	
	private MunicipioService municipioService;
	private MunicipioRegionalClienteService municipioRegionalClienteService;
	private UsuarioService usuarioService;


	/**
	 * Recupera uma instância de <code>GerenciaRegional</code> a partir do ID.
	 * Busca os municípios associados a GerenciaRegional através da MunicipioRegionalCliente
	 * Troca o nome do município pelo nome do município - unidade federativa
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public GerenciaRegional findById(java.lang.Long id) {
		GerenciaRegional gerenciaRegional = (GerenciaRegional)super.findById(id);

		Map<String, Object> criterios = new HashMap<String, Object>();
		ReflectionUtils.setNestedBeanPropertyValue(criterios,"gerenciaRegional.idGerenciaRegional",gerenciaRegional.getIdGerenciaRegional());

		List<MunicipioRegionalCliente> listaMunicipioRegional = getMunicipioRegionalClienteService().find(criterios);
		List<Municipio> municipios = new ArrayList<Municipio>(listaMunicipioRegional.size());
		for(MunicipioRegionalCliente element : listaMunicipioRegional) {
			Municipio municipio = getMunicipioService().findById(element.getMunicipio().getIdMunicipio());
			municipio.setNmMunicipio(municipio.getNmMunicipioAndSgUnidadeFederativa());
			municipios.add(municipio);
		}

		gerenciaRegional.setMunicipios(municipios);

		return gerenciaRegional;
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
	 */
	@Override
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
	public java.io.Serializable store(GerenciaRegional bean) {
		return super.store(bean);
	}

	/**
	 * Antes de Salvar o objeto GerenciaRegional é necessário montar os dados 
	 * dos municípios associados a GerenciaRegional através da classe MunicipioRegionalCliente.
	 * @return Object contendo a GerenciaRegional para salvar
	 */
	@Override
	protected GerenciaRegional beforeStore(GerenciaRegional bean) {
		List<MunicipioRegionalCliente> retorno = new ArrayList<MunicipioRegionalCliente>();
		MunicipioRegionalCliente municipioRegionalCliente = null;

		List<Municipio> municipios = bean.getMunicipios();
		for(Municipio municipio : municipios) {
			municipio = getMunicipioService().findById(municipio.getIdMunicipio());   		
			municipioRegionalCliente = new MunicipioRegionalCliente();
			municipioRegionalCliente.setMunicipio(municipio);
			municipioRegionalCliente.setUnidadeFederativa(municipio.getUnidadeFederativa());
			municipioRegionalCliente.setGerenciaRegional(bean);

			retorno.add(municipioRegionalCliente);

			this.getGerenciaRegionalDAO().getAdsmHibernateTemplate().evict(municipio);
		}

		bean.setMunicipioRegionalClientes(retorno);

		this.getGerenciaRegionalDAO().deleteAllMunicipioRegionalClientesByGerenciaRegional(bean.getIdGerenciaRegional());

		((GerenciaRegional)bean).setMunicipios(null);

		return bean;
	}

	/**
	 * Antes de remover as Gerencias Regionais selecionadas deve-se remover suas associações em Municipio Regional Cliente
	 */
	protected void beforeRemoveById(Long id) {
		this.getGerenciaRegionalDAO().deleteAllMunicipioRegionalClientesByGerenciaRegional((Long)id);
		super.beforeRemoveById(id);
	}

	/**
	 * Antes de remover as Gerencias Regionais selecionadas deve-se remover suas associações em Municipio Regional Cliente
	 */
	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		for(Long id : ids) {
			this.getGerenciaRegionalDAO().deleteAllMunicipioRegionalClientesByGerenciaRegional(id);
		}
		super.beforeRemoveByIds(ids);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		getGerenciaRegionalDAO().removeByIdCliente(idCliente);
    }

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setGerenciaRegionalDAO(GerenciaRegionalDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private GerenciaRegionalDAO getGerenciaRegionalDAO() {
		return (GerenciaRegionalDAO) getDao();
	}

	/**
	 * Chama a verificação de permissões do usuário sobre uma filial / regional
	 */
	public Boolean validatePermissao(Long idFilial) {
		return getUsuarioService().verificaAcessoFilialRegionalUsuarioLogado(idFilial);
	}

	public MunicipioService getMunicipioService() {
		return municipioService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public MunicipioRegionalClienteService getMunicipioRegionalClienteService() {
		return municipioRegionalClienteService;
	}

	public void setMunicipioRegionalClienteService(
			MunicipioRegionalClienteService municipioRegionalClienteService) {
		this.municipioRegionalClienteService = municipioRegionalClienteService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

}
