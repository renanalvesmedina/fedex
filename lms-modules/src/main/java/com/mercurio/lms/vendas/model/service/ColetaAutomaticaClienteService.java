package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.ColetaAutomaticaCliente;
import com.mercurio.lms.vendas.model.dao.ColetaAutomaticaClienteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.coletaAutomaticaClienteService"
 */
public class ColetaAutomaticaClienteService extends CrudService<ColetaAutomaticaCliente, Long> {
	private UsuarioService usuarioService;

	/**
	 * Recupera uma instância de <code>ColetaAutomaticaCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public ColetaAutomaticaCliente findById(java.lang.Long id) {
		return (ColetaAutomaticaCliente)super.findById(id);
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
	public java.io.Serializable store(ColetaAutomaticaCliente bean) {
		return super.store(bean);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		getColetaAutomaticaClienteDAO().removeByIdCliente(idCliente);
    }

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setColetaAutomaticaClienteDAO(ColetaAutomaticaClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ColetaAutomaticaClienteDAO getColetaAutomaticaClienteDAO() {
		return (ColetaAutomaticaClienteDAO) getDao();
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	/**
	 * Chama a verificação de permissões do usuário sobre uma filial / regional
	 */
	public Boolean validatePermissao(Long idFilial) {
		return getUsuarioService().verificaAcessoFilialRegionalUsuarioLogado(idFilial);
	}

	/**
	 * Retorna as coletas automáticas de todos os clientes com coletas no diaSemana passado
	 * por parâmetro e que a filial que atende operacional seja igual à filial passada por parâmetro.
	 * @param filial
	 * @param diaSemana
	 * @return List: Coletas automáticas de clientes.
	 */
	public List findColetasAutomaticasClienteByDiaSemanaAndFilialAtendeOperacional(Filial filial, int diaSemana) {
		return getColetaAutomaticaClienteDAO().findColetasAutomaticasClienteByDiaSemanaAndFilialAtendeOperacional(filial, diaSemana);
	}

}
