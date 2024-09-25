package com.mercurio.lms.franqueados.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.SimulacaoReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.dao.SimulacaoReembarqueDoctoServicoFranqueadoDAO;

public class SimulacaoReembarqueDoctoServicoFranqueadoService extends CrudService<SimulacaoReembarqueDoctoServicoFranqueado, Long> {

	private SimulacaoReembarqueDoctoServicoFranqueadoDAO getSimulacaoReembarqueDoctoServicoFranqueadoDAO() {
		return (SimulacaoReembarqueDoctoServicoFranqueadoDAO) getDao();
	}
	
	public void setSimulacaoReembarqueDoctoServicoFranqueadoDAO(SimulacaoReembarqueDoctoServicoFranqueadoDAO reembarqueDoctoServicoFranqueadoDAO) {
        setDao(reembarqueDoctoServicoFranqueadoDAO);
    }

	@SuppressWarnings("rawtypes")
	public List findValorTotalReembarque(YearMonthDay dtCompetencia, Long idFranquia) {
		return getSimulacaoReembarqueDoctoServicoFranqueadoDAO().findValorTotalReembarque(dtCompetencia, idFranquia);
	}

	
	public List<SimulacaoReembarqueDoctoServicoFranqueado> findServicosReembarque(YearMonthDay dtCompetencia, Long idFranquia) {
		return getSimulacaoReembarqueDoctoServicoFranqueadoDAO().findServicosReembarque(dtCompetencia, idFranquia);
    }
	

	public List<Map<String,Object>> findRelatorioAnaliticoReembarques(boolean filtraFranquia, boolean isCSV, Map<String,Object> parameters){
		return getSimulacaoReembarqueDoctoServicoFranqueadoDAO().findRelatorioAnaliticoReembarques(filtraFranquia, isCSV, parameters);
	}
	
	@Override
	protected Serializable store(SimulacaoReembarqueDoctoServicoFranqueado bean) {
		return super.store(bean);
	}
	
	@Override
	public void storeAll(List<SimulacaoReembarqueDoctoServicoFranqueado> list) {
		super.storeAll(list);
	}
	
	public void removeAll(YearMonthDay dtCompentencia, Long idFilial) {
		getSimulacaoReembarqueDoctoServicoFranqueadoDAO().removeAll(dtCompentencia, idFilial);
	}
}
