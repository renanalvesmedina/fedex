package com.mercurio.lms.vendas.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.DocumentoCliente;
import com.mercurio.lms.vendas.model.dao.DocumentoClienteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD: 
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.documentoClienteService"
 */
public class DocumentoClienteService extends CrudService<DocumentoCliente, Long> {

	private UsuarioService usuarioService;

	/**
	 * Verifica permiss�o de usu�rio logado
	 * @author Robson Edemar Gehl
	 * @return
	 */
	public Boolean validatePermissaoUsuarioLogado(Long id){
		return getUsuarioService().verificaAcessoFilialRegionalUsuarioLogado(id);
	}
	
	/**
	 * Recupera uma inst�ncia de <code>DocumentoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public DocumentoCliente findById(java.lang.Long id) {
		return (DocumentoCliente)super.findById(id);
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
	public java.io.Serializable store(DocumentoCliente bean) {
		return super.store(bean);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		getDocumentoClienteDAO().removeByIdCliente(idCliente);
    }

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setDocumentoClienteDAO(DocumentoClienteDAO dao) {
		setDao( dao );
	}
	
	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private DocumentoClienteDAO getDocumentoClienteDAO() {
		return (DocumentoClienteDAO) getDao();
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public List findByCliente(Long idCliente, String tpModal, String tpAbrangencia) {
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();
		return getDocumentoClienteDAO().findByCliente(idCliente, tpModal, tpAbrangencia, dtVigencia);
	}
	
	/**
	 * @author Jos� Rodrigo Moraes
	 * @since  12/07/2006
	 * 
	 * FindPaginated criado para correto uso do TypedFlatMap
	 * 
	 * @param tfm Crit�rios de pesquisa
	 * @return ResultSetPage com dados da pesquisa e dados de pagina��o
	 */
	public ResultSetPage findPaginatedTyped(TypedFlatMap tfm) {
		FindDefinition findDef = FindDefinition.createFindDefinition(tfm);
		return getDocumentoClienteDAO().findPaginatedTyped(tfm,findDef);
	}
	
	/**
	 * @author Jos� Rodrigo Moraes
	 * @since  12/07/2006
	 * 
	 * RowCount criado para correto funcionamento junto ao findPaginatedTyped
	 * 
	 * @param tfm Crit�rios de pesquisa
	 * @return Quantidade de registros por p�gina
	 */
	public Integer getRowCountTyped(TypedFlatMap tfm) { 
		return getDocumentoClienteDAO().getRowCount(tfm);
	}
}