package com.mercurio.lms.sim.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.EventoClienteRecebe;
import com.mercurio.lms.sim.model.dao.EventoClienteRecebeDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sim.eventoClienteRecebeService"
 */
public class EventoClienteRecebeService extends CrudService<EventoClienteRecebe, Long> {
	

	/**
	 * Recupera uma instância de <code>EventoClienteRecebe</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public EventoClienteRecebe findById(java.lang.Long id) {
        return (EventoClienteRecebe)super.findById(id);
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
    public java.io.Serializable store(EventoClienteRecebe bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEventoClienteRecebeDAO(EventoClienteRecebeDAO dao) {
        setDao( dao );
    }
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getEventoClienteRecebeDAO().getRowCountCustom(criteria);
	}    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EventoClienteRecebeDAO getEventoClienteRecebeDAO() {
        return (EventoClienteRecebeDAO) getDao();
    }

    // Método é usado na manutencao de configuracao de eventos, como o cliente quer receber as informaçoes, 
    // find customizado para tela de listagem
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		 return getEventoClienteRecebeDAO().findPaginatedCustom(criteria);
   }
	
	public ResultSetPage findByIdDetalhamento(Long id) {
		
		return getEventoClienteRecebeDAO().findByIdDetalhamento(id);
	}

	public List validateEventosClienteVigente(Long idCliente, List list, YearMonthDay dataInicial, YearMonthDay dataFim) {
		
		return getEventoClienteRecebeDAO().validateEventosClienteVigente(idCliente,list,dataInicial,dataFim);
	}

	public List findByObject(TypedFlatMap criteria) {
		return getEventoClienteRecebeDAO().findEventosConfiguracaoComunicacao(criteria);
	}



   }