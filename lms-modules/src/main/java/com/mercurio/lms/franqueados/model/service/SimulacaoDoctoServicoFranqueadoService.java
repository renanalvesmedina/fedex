package com.mercurio.lms.franqueados.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.SimulacaoDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.dao.SimulacaoDoctoServicoFranqueadoDAO;

public class SimulacaoDoctoServicoFranqueadoService extends CrudService<SimulacaoDoctoServicoFranqueado, Long> {
	
	private SimulacaoDoctoServicoFranqueadoDAO getSimulacaoDoctoServicoFranqueadoDAO() {
		return (SimulacaoDoctoServicoFranqueadoDAO) getDao();
	}
	
	public void setSimulacaoDoctoServicoFranqueadoDAO(SimulacaoDoctoServicoFranqueadoDAO simulacaoDoctoServicoFranqueadoDAO) {
        setDao(simulacaoDoctoServicoFranqueadoDAO);
    }
	
	@Override
	public Serializable store(SimulacaoDoctoServicoFranqueado bean) {
		return super.store(bean);
	}
	
	@Override
	public void storeAll(List<SimulacaoDoctoServicoFranqueado> list) {
		super.storeAll(list);
	}
 
	public List<SimulacaoDoctoServicoFranqueado> findServicosAdicionais(YearMonthDay dtCompetencia, Long idFranquia) {
		return getSimulacaoDoctoServicoFranqueadoDAO().findServicosAdicionais(dtCompetencia, idFranquia);
	}

	public List<Map<String,Object>> findRelatorioSinteticoDefault(Map<String, Object> parameters, boolean sum) {
		return getSimulacaoDoctoServicoFranqueadoDAO().findRelatorioSinteticoDefault(parameters,sum);
	}
	
	public List<Map<String,Object>> findRelatorioSinteticoColetaEntrega(Map<String, Object> parameters) {
		return getSimulacaoDoctoServicoFranqueadoDAO().findRelatorioSinteticoColetaEntrega(parameters);
	}
	
	public List<Map<String,Object>> findRelatorioSinteticoServicoAdicional(Map<String, Object> parameters) {
		return getSimulacaoDoctoServicoFranqueadoDAO().findRelatorioSinteticoServicoAdicional(parameters);
	}
	
	public List<Map<String,Object>> findRelatorioSinteticoDocumentosFiscais(Map<String, Object> parameters) {
		return getSimulacaoDoctoServicoFranqueadoDAO().findRelatorioSinteticoDocumentosFiscais(parameters);
	}
	
	public List<Map<String,Object>> findRelatorioAnaliticoDocumentos(boolean filtraFranquia, boolean isCSV, Map<String,Object> parameters){
		return getSimulacaoDoctoServicoFranqueadoDAO().findRelatorioAnaliticoDocumentos(filtraFranquia, isCSV, parameters);
	}

	public List<Map<String,Object>> findRelatorioAnaliticoFretesLocal(boolean filtraFranquia, boolean isCSV ,Map<String,Object> parameters) {
		return getSimulacaoDoctoServicoFranqueadoDAO().findRelatorioAnaliticoFretesLocal(filtraFranquia, isCSV, parameters);
	}
	
	public List<Map<String,Object>> findRelatorioAnaliticoServicosAdicionais(boolean filtraFranquia, boolean isCSV, Map<String,Object> parameters) {
		return getSimulacaoDoctoServicoFranqueadoDAO().findRelatorioAnaliticoServicosAdicionais(filtraFranquia, isCSV, parameters);
	}

}
