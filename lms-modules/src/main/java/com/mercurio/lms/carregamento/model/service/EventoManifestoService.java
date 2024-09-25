package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.EventoManifesto;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.dao.EventoManifestoDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.eventoManifestoService"
 */
public class EventoManifestoService extends CrudService<EventoManifesto, Long> {

	/**
	 * Cria novo evento para o Manifesto
	 * @param manifesto
	 * @param tpEventoManifesto
	 */
	public void generateEventoManifesto(Manifesto manifesto, Filial filial, String tpEventoManifesto) {
		generateEventoManifesto(manifesto, filial, tpEventoManifesto, JTDateTimeUtils.getDataHoraAtual());
		
	}
	
	public void generateEventoManifesto(Manifesto manifesto, Filial filial, String tpEventoManifesto, DateTime dhEvento) {
		EventoManifesto eventoManifesto = new EventoManifesto();
		eventoManifesto.setManifesto(manifesto);
		eventoManifesto.setFilial(filial);
		eventoManifesto.setUsuario(SessionUtils.getUsuarioLogado());
		eventoManifesto.setDhEvento(dhEvento);
		eventoManifesto.setTpEventoManifesto(new DomainValue(tpEventoManifesto));

		store(eventoManifesto);
	}
	

	/**
	 * Recupera uma instância de <code>EventoManifesto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
	public EventoManifesto findById(java.lang.Long id) {
		return (EventoManifesto)super.findById(id);
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
	 * Retorna os EventosManifesto de um Manifesto passado por parâmetro.
	 * @param idManifesto
	 * @return
	 */
	public List findEventoManifestoByIdManifesto(Long idManifesto) {
		return this.getEventoManifestoDAO().findEventoManifesto(idManifesto, null);
	}

	/**
	 * Retorna os Eventos do Manifesto conforme o tipo.
	 * @param idManifesto
	 * @param tpEventoManifesto
	 * @return
	 */
	public List findEventoManifesto(Long idManifesto, String tpEventoManifesto) {
		return this.getEventoManifestoDAO().findEventoManifesto(idManifesto, tpEventoManifesto);
	}

	/**
	 * Retorna o último evento do Manifesto passado por parâmetro.
	 * @param idManifesto
	 * @return
	 */
	public EventoManifesto findUltimoEventoManifesto(Long idManifesto){
		return this.getEventoManifestoDAO().findUltimoEventoManifesto(idManifesto);
	}

	/**
	 * Busca um EventoManifesto a partir do idManifesto, idFilial e tpEventoManifesto.
	 * Caso encontrar mais de um registro, uma NonUniqueResultException é disparada. 
	 * @param idManifesto
	 * @param idFilial
	 * @param tpEventoManifesto
	 * @return
	 */
	public EventoManifesto findEventoManifestoByIdManifestoIdFilialTpEventoManifesto(Long idManifesto, Long idFilial, String tpEventoManifesto) {
		return this.getEventoManifestoDAO().findEventoManifestoByIdManifestoIdFilialTpEventoManifesto(idManifesto, idFilial, tpEventoManifesto);
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
	public java.io.Serializable store(EventoManifesto bean) {
		return super.store(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * Este store nao possui regras de negocio.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable storeBasic(EventoManifesto bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setEventoManifestoDAO(EventoManifestoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private EventoManifestoDAO getEventoManifestoDAO() {
		return (EventoManifestoDAO) getDao();
	}

	/**
	 * Remove o evento do Manifesto
	 * @param idManifesto
	 */
	public void removeByIdManifesto(Long idManifesto) {
		this.getEventoManifestoDAO().removeByIdManifesto(idManifesto);
	}

	/**
	 * Remove o evento do Manifesto
	 * @param idManifesto
	 * @param tpEventoManifesto
	 */
	public void removeByManifesto(Long idManifesto, String tpEventoManifesto) {
		List<EventoManifesto> eventosManifesto = findEventoManifesto(idManifesto, tpEventoManifesto);
		if(!eventosManifesto.isEmpty()) {
			this.getEventoManifestoDAO().getHibernateTemplate().deleteAll(eventosManifesto);
		}
	}

}