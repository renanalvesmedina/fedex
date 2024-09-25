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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.sim.eventoClienteRecebeService"
 */
public class EventoClienteRecebeService extends CrudService<EventoClienteRecebe, Long> {
	

	/**
	 * Recupera uma inst�ncia de <code>EventoClienteRecebe</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public EventoClienteRecebe findById(java.lang.Long id) {
        return (EventoClienteRecebe)super.findById(id);
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
    public java.io.Serializable store(EventoClienteRecebe bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setEventoClienteRecebeDAO(EventoClienteRecebeDAO dao) {
        setDao( dao );
    }
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getEventoClienteRecebeDAO().getRowCountCustom(criteria);
	}    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private EventoClienteRecebeDAO getEventoClienteRecebeDAO() {
        return (EventoClienteRecebeDAO) getDao();
    }

    // M�todo � usado na manutencao de configuracao de eventos, como o cliente quer receber as informa�oes, 
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