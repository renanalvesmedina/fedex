package com.mercurio.lms.vendas.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.vendas.model.EventoCliente;
import com.mercurio.lms.vendas.model.dao.EventoClienteDAO;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.eventoClienteService"
 */
public class EventoClienteService extends CrudService<EventoCliente, Long> {

	private UsuarioService usuarioService;

	/**
	 * Recupera uma instância de <code>EventoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public EventoCliente findById(java.lang.Long id) {
		return (EventoCliente)super.findById(id);
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
	public java.io.Serializable store(EventoCliente bean) {
		return super.store(bean);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * 
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
		getEventoClienteDAO().removeByIdCliente(idCliente);
    }

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setEventoClienteDAO(EventoClienteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private EventoClienteDAO getEventoClienteDAO() {
		return (EventoClienteDAO) getDao();
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

	/**
	 * Find personalizado para montar a combo de Eventos
	 * com campos concatenados
	 * @param map
	 * @return List
	 */
	public List findEventoCombo(Map map) {
		List list = getEventoClienteDAO().findEventoCombo(map);
		return montarDescricaoComboEvento(list);
	}

	/**
	 * Find personalizado para montar a combo de Eventos
	 * com campos concatenados
	 * @param map
	 * @return List
	 */
	public List findEventoComboAtivo(Map map) {
		List list = getEventoClienteDAO().findEventoComboAtivo(map);
		return montarDescricaoComboEvento(list);
	}	

	/**
	 * Monta a descrição na combo evento da tela de evento cliente.
	 * 
	 * @param List list
	 * @return List
	 * */
	private List montarDescricaoComboEvento(List list){
		List retorno = new ArrayList();
		if (list.size() > 0) { 
			Map mapResult = new HashMap();
			Map projections = new HashMap();
			StringBuffer comboText = new StringBuffer(); 

			for(int x = 0; x < list.size(); x++) {
				projections = (Map)list.get(x);
				mapResult.put("idEvento",projections.get("idEvento"));

				comboText.append(projections.get("cdEvento"));
				comboText.append(" - " + projections.get("dsDescricaoEvento"));
				if (projections.get("dsLocalizacaoMercadoria") != null && StringUtils.isNotBlank(((VarcharI18n)projections.get("dsLocalizacaoMercadoria")).toString())){
					comboText.append(" - "+projections.get("dsLocalizacaoMercadoria"));
				}

				// Se o tpSituacao é inativo tem que adicionar ele como inativo para deixar o valor em cinza na tela.
				if (((DomainValue)projections.get("tpSituacaoe")).getValue().equals("I")){
					mapResult.put("tpSituacao",(DomainValue)projections.get("tpSituacaoe"));
				} else {
					if (((DomainValue)projections.get("tpSituacaod")).getValue().equals("I")){
						mapResult.put("tpSituacao",(DomainValue)projections.get("tpSituacaod"));
					} else {
						mapResult.put("tpSituacao",(DomainValue)projections.get("tpSituacaoe"));
					}
				}

				mapResult.put("comboText",comboText.toString());
				retorno.add(mapResult);

				mapResult = new HashMap();
				comboText = new StringBuffer();
			}
		}
		return retorno;
	}
}