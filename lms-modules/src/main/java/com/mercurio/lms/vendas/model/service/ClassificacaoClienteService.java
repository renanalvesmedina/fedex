package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.vendas.model.ClassificacaoCliente;
import com.mercurio.lms.vendas.model.DescClassificacaoCliente;
import com.mercurio.lms.vendas.model.TipoClassificacaoCliente;
import com.mercurio.lms.vendas.model.dao.ClassificacaoClienteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.classificacaoClienteService"
 */
public class ClassificacaoClienteService extends CrudService<ClassificacaoCliente, Long> {
	
	
	private TipoClassificacaoClienteService tipoClassificacaoClienteService;
	private UsuarioService usuarioService;
	private DescClassificacaoClienteService descClassificacaoClienteService;

	/**
	 * Recupera uma inst�ncia de <code>ClassificacaoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ClassificacaoCliente findById(java.lang.Long id) {
		return (ClassificacaoCliente)super.findById(id);
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
	 * Implementa��o para controlar regra aonde s� deve
	 * existir apena um registro de classificacao_cliente
	 * por cliente e tipo de classifica��o
	 * @author Alexandre Menezes
	 * @param bean ClassificacaoCliente
	 * @exception BusinessException("LMS-27024")
	 * @return entidade que foi armazenada.
	 */
	protected ClassificacaoCliente beforeStore(ClassificacaoCliente bean) {
		ClassificacaoCliente classificacaoCliente = (ClassificacaoCliente)bean;
		Long idCliente = classificacaoCliente.getCliente().getIdCliente();
		Long idDescClassificacaoCliente = classificacaoCliente.getDescClassificacaoCliente().getIdDescClassificacaoCliente();
		Long idClassificacaoCliente = classificacaoCliente.getIdClassificacaoCliente();
		Long idTipoClassificacaoCliente = null; 

		TipoClassificacaoCliente tcc = getTipoClassificacaoClienteService().findByDescClassificacaoCliente(idDescClassificacaoCliente);
		if (tcc != null) {
			idTipoClassificacaoCliente = tcc.getIdTipoClassificacaoCliente();	
		}

		if (!getClassificacaoClienteDAO().validateClassificacaoCliente(idCliente,idClassificacaoCliente,idTipoClassificacaoCliente).booleanValue()) {
			throw new BusinessException("LMS-27024");
		}

		DescClassificacaoCliente dcc =this.getDescClassificacaoClienteService().findById(classificacaoCliente.getDescClassificacaoCliente().getIdDescClassificacaoCliente());

		classificacaoCliente.setDescClassificacaoCliente(dcc);

		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ClassificacaoCliente bean) {
		return super.store(bean);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		getClassificacaoClienteDAO().removeByIdCliente(idCliente);
    }

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setClassificacaoClienteDAO(ClassificacaoClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ClassificacaoClienteDAO getClassificacaoClienteDAO() {
		return (ClassificacaoClienteDAO) getDao();
	}

	public TipoClassificacaoClienteService getTipoClassificacaoClienteService() {
		return tipoClassificacaoClienteService;
	}

	public void setTipoClassificacaoClienteService(
			TipoClassificacaoClienteService tipoClassificacaoClienteService) {
		this.tipoClassificacaoClienteService = tipoClassificacaoClienteService;
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

	public DescClassificacaoClienteService getDescClassificacaoClienteService() {
		return descClassificacaoClienteService;
	}

	public void setDescClassificacaoClienteService(
			DescClassificacaoClienteService descClassificacaoClienteService) {
		this.descClassificacaoClienteService = descClassificacaoClienteService;
	}

}
