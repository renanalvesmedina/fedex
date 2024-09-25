package com.mercurio.lms.vendas.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.vendas.model.PotencialComercialCliente;
import com.mercurio.lms.vendas.model.dao.PotencialComercialClienteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.potencialComercialClienteService"
 */
public class PotencialComercialClienteService extends CrudService<PotencialComercialCliente, Long> {
	private DomainValueService domainValueService;	
	private UsuarioService usuarioService;

	/**
	 * Recupera uma inst�ncia de <code>PotencialComercialCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public PotencialComercialCliente findById(java.lang.Long id) {
		return (PotencialComercialCliente)super.findById(id);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getPotencialComercialClienteDAO().findPaginated(criteria, def);
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
	public java.io.Serializable store(PotencialComercialCliente bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setPotencialComercialClienteDAO(PotencialComercialClienteDAO dao) {
		setDao( dao );
	}
	
	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private PotencialComercialClienteDAO getPotencialComercialClienteDAO() {
		return (PotencialComercialClienteDAO) getDao();
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;	
	}
	
	/**
	 * Chama a verifica��o de permiss�es do usu�rio sobre uma filial / regional
	 */
	public Boolean validatePermissao(Long idFilial) {
		return getUsuarioService().verificaAcessoFilialRegionalUsuarioLogado(idFilial);
	}
	
   }