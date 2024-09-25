package com.mercurio.lms.vendas.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.ClienteDespachante;
import com.mercurio.lms.vendas.model.dao.ClienteDespachanteDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.vendas.clienteDespachanteService"
 */
public class ClienteDespachanteService extends CrudService<ClienteDespachante, Long> {

	private UsuarioService usuarioService;

	private TelefoneEnderecoService telefoneEnderecoService;

	/**
	 * Verifica permiss�o de usu�rio logado
	 * 
	 * @author Robson Edemar Gehl
	 * @return
	 */
	public Boolean validatePermissaoUsuarioLogado(Long id) {
		return getUsuarioService().verificaAcessoFilialRegionalUsuarioLogado(id);
	}

	/**
	 * Recupera uma inst�ncia de <code>ClienteDespachante</code>
	 * a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ClienteDespachante findById(java.lang.Long id) {
		return (ClienteDespachante) super.findById(id);
	}

	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = this.getClienteDespachanteDAO().findPaginated(
				criteria, FindDefinition.createFindDefinition(criteria));
		List list = rsp.getList();

		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Map despachante = (Map) iter.next();
			TelefoneEndereco telefoneEndereco = this
					.getTelefoneEnderecoService()
					.findTelefoneEnderecoPadraoTela(
							(Long) despachante.get("idPessoa"));
			if (telefoneEndereco != null) {
				despachante.put("tpTelefone", telefoneEndereco.getTpTelefone()
						.getDescription());
				despachante.put("tpUso", telefoneEndereco.getTpUso()
						.getDescription());
				despachante.put("nrDdi", telefoneEndereco.getNrDdi());
				despachante.put("nrTelefone", telefoneEndereco.getNrTelefone());
				despachante
						.put("nrTelefone", telefoneEndereco.getDddTelefone());
			}
		}
		rsp.setList(list);
		return rsp;
	}

	/**
	 * Recupera uma inst�ncia de <code>ClienteDespachante</code>
	 * a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Map findByIdSpecificTela(java.lang.Long id) {
		Map map = (Map) getClienteDespachanteDAO().findByIdSpecificTela(id);

		if (((Map) ((Map) ((Map) map.get("despachante")).get("pessoa"))
				.get("tpIdentificacao")) != null) {
			// Busca os tpIdentificacao e nrIdentificacao
			String tpIdentificacao = (String) ((Map) ((Map) ((Map) map
					.get("despachante")).get("pessoa")).get("tpIdentificacao"))
					.get("value");
			String nrIdentificacao = (String) ((Map) ((Map) map
					.get("despachante")).get("pessoa")).get("nrIdentificacao");

			// Formata o nrIdentificacao
			String nrIdentificacaoFormatado = FormatUtils.formatIdentificacao(
					tpIdentificacao, nrIdentificacao);
			((Map) ((Map) map.get("despachante")).get("pessoa")).put(
					"nrIdentificacaoFormatado", nrIdentificacaoFormatado);
		}
		return map;
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ClienteDespachante bean) {
		return super.store(bean);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		getClienteDespachanteDAO().removeByIdCliente(idCliente);
    }

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setClienteDespachanteDAO(ClienteDespachanteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private ClienteDespachanteDAO getClienteDespachanteDAO() {
		return (ClienteDespachanteDAO) getDao();
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public TelefoneEnderecoService getTelefoneEnderecoService() {
		return telefoneEnderecoService;
	}

	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	
	public List findAll(){
		return getClienteDespachanteDAO().findAll();
	}

	public List findByIdCliente(Long id){
		return getClienteDespachanteDAO().findByIdCliente(id);
	}
}