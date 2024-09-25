package com.mercurio.lms.franqueados.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.ReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.dao.ReembarqueDoctoServicoFranqueadoDAO;

public class ReembarqueDoctoServicoFranqueadoService extends CrudService<ReembarqueDoctoServicoFranqueado, Long> {

	private ReembarqueDoctoServicoFranqueadoDAO getReembarqueDoctoServicoFranqueadoDAO() {
		return (ReembarqueDoctoServicoFranqueadoDAO) getDao();
	}
	
	public void setReembarqueDoctoServicoFranqueadoDAO(ReembarqueDoctoServicoFranqueadoDAO reembarqueDoctoServicoFranqueadoDAO) {
        setDao(reembarqueDoctoServicoFranqueadoDAO);
    }
	
	@Override
	protected Serializable store(ReembarqueDoctoServicoFranqueado bean) {
		return super.store(bean);
	}
	
	@Override
	public void storeAll(List<ReembarqueDoctoServicoFranqueado> list) {
		super.storeAll(list);
	}
	
	public List<Map<String,Object>> findRelatorioAnaliticoReembarques(boolean filtraFranquia, boolean isCSV, Map<String,Object> parameters){
		return getReembarqueDoctoServicoFranqueadoDAO().findRelatorioAnaliticoReembarques(filtraFranquia, isCSV, parameters);
	}
	
	@SuppressWarnings("rawtypes")
	public List findValorTotalReembarque(YearMonthDay dtCompetencia, Long idFranquia) {
		return getReembarqueDoctoServicoFranqueadoDAO().findValorTotalReembarque(dtCompetencia, idFranquia);
	}

	
	public List<ReembarqueDoctoServicoFranqueado> findServicosReembarque(YearMonthDay dtCompetencia, Long idFranquia) {
		return getReembarqueDoctoServicoFranqueadoDAO().findServicosReembarque(dtCompetencia, idFranquia);
    }
}
