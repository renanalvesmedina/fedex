package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.vendas.model.CiaAereaCliente;
import com.mercurio.lms.vendas.model.dao.CiaAereaClienteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.ciaAereaClienteService"
 */
public class CiaAereaClienteService extends CrudService<CiaAereaCliente, Long> {

	private UsuarioService usuarioService;

	/**
	 * Recupera uma inst�ncia de <code>CiaAereaCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public CiaAereaCliente findById(java.lang.Long id) {
		return (CiaAereaCliente)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(CiaAereaCliente bean) {
		return super.store(bean);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		getCiaAereaClienteDAO().removeByIdCliente(idCliente);
    }

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setCiaAereaClienteDAO(CiaAereaClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private CiaAereaClienteDAO getCiaAereaClienteDAO() {
		return (CiaAereaClienteDAO) getDao();
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	/*
	 * Chama verifica��o de permiss�es do usu�rio sobre uma filial / regional
	 * */
	public Boolean validatePermissao(Long idFilial) {
		return getUsuarioService().verificaAcessoFilialRegionalUsuarioLogado(idFilial);
	}

	public String findCiaAereaPorAWB(Long idCiaFilialMercurio){
		return getCiaAereaClienteDAO().findCiaAereaPorAWB(idCiaFilialMercurio);
}

}
