package com.mercurio.lms.franqueados.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.ContaContabilFranqueado;
import com.mercurio.lms.franqueados.model.dao.ContaContabilFranqueadoDAO;

public class ContaContabilFranqueadoService extends CrudService<ContaContabilFranqueado, Long> {

	public ContaContabilFranqueado findById(Long id) {
        return getContaContabilFranqueadoDAO().findById(id);
    }
	
	public List<ContaContabilFranqueado> findByVigencia(YearMonthDay vigencia){
		return getContaContabilFranqueadoDAO().findByVigencia(vigencia);
	}

	public ContaContabilFranqueado findContaByTipo(YearMonthDay dtVigencia, String tipoConta, String tipoLancamento) {
		List<ContaContabilFranqueado> contaContabilFranqueadoList = getContaContabilFranqueadoDAO()
				.findContaByDtVigenciaByTipoContaByTipoLancamento(dtVigencia,tipoConta,tipoLancamento);
		
		if(contaContabilFranqueadoList != null && contaContabilFranqueadoList.size() == 1){
			return contaContabilFranqueadoList.get(0);
		}
		return null;
	}
	
	private ContaContabilFranqueadoDAO getContaContabilFranqueadoDAO() {
		return (ContaContabilFranqueadoDAO) getDao();
	}
	
	public void setContaContabilFranqueadoDAO(ContaContabilFranqueadoDAO contaContabilFranqueadoDAO) {
        setDao(contaContabilFranqueadoDAO);
    }
		
}
