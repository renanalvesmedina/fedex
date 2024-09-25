package com.mercurio.lms.sim.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.entrega.model.AgendamentoMonitCCT;
import com.mercurio.lms.municipios.model.dao.AeroportoDAO;
import com.mercurio.lms.sim.model.dao.AgendamentoMonitCCTDAO;

public class AgendamentoMonitCCTService extends CrudService<AgendamentoMonitCCT, Long> {

    public AgendamentoMonitCCT findById(java.lang.Long id) {
        return (AgendamentoMonitCCT) super.findById(id);
    }
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(AgendamentoMonitCCT bean) {
		return super.store(bean);
	}
	
    public void setAgendamentoMonitCCTDAO(AgendamentoMonitCCTDAO dao) {
        setDao(dao);
    }
    
	private AgendamentoMonitCCTDAO getAgendamentoMonitCCTDAO() {
		return (AgendamentoMonitCCTDAO) getDao();
	}
    
    public List<AgendamentoMonitCCT> findAgendamentoMonitCCTByAgendamentoEntrega(Long idAgendamentoEntrega) {
    	return this.getAgendamentoMonitCCTDAO().findAgendamentoMonitCCTByAgendamentoEntrega(idAgendamentoEntrega);
    }
    
    public List<AgendamentoMonitCCT> findAgendamentoMonitCCTByMonitCCT(Long idMonitoramentoCCT) {
    	return this.getAgendamentoMonitCCTDAO().findAgendamentoMonitCCTByMonitCCT(idMonitoramentoCCT);
    }
}
