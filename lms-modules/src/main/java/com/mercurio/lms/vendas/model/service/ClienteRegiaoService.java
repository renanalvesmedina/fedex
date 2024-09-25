package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.vendas.model.ClienteRegiao;
import com.mercurio.lms.vendas.model.dao.ClienteRegiaoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.clienteRegiaoService"
 */
public class ClienteRegiaoService extends CrudService<ClienteRegiao, Long> {

	private UsuarioService usuarioService;

	/**
	 * Recupera uma instância de <code>ClienteRegiao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ClienteRegiao findById(java.lang.Long id) {
		return (ClienteRegiao)super.findById(id);
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
	public java.io.Serializable store(ClienteRegiao bean) {
		return super.store(bean);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		getClienteRegiaoDAO().removeByIdCliente(idCliente);
    }

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setClienteRegiaoDAO(ClienteRegiaoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ClienteRegiaoDAO getClienteRegiaoDAO() {
		return (ClienteRegiaoDAO) getDao();
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	/*
	 * Chama verificação de permissões do usuário sobre uma filial / regional
	 * */
	public Boolean validatePermissao(Long idFilial) {
		return getUsuarioService().verificaAcessoFilialRegionalUsuarioLogado(idFilial);
	}

}
