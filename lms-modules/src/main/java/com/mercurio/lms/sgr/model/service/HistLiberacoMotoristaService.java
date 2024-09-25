package com.mercurio.lms.sgr.model.service;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.sgr.dto.FiltroLiberacaoMotoristaDto;
import com.mercurio.lms.sgr.model.HistLiberacaoMotorista;
import com.mercurio.lms.sgr.model.dao.HistLiberacoMotoristaDao;

public class HistLiberacoMotoristaService extends CrudService<HistLiberacaoMotorista, Long> {
	
	public ResultSetPage<HistLiberacaoMotorista> findPaginated(FiltroLiberacaoMotoristaDto filtro, FindDefinition findDef) {

		return getHistLiberacoMotoristaDao().findPaginated(filtro, findDef);

	}
	
	public Integer getRowCount(FiltroLiberacaoMotoristaDto filtro) {

		return getHistLiberacoMotoristaDao().getRowCount(filtro);

	}
	
	public String findConteudoArquivo(Long idHistLiberacaoMotorista) {
		return getHistLiberacoMotoristaDao().findConteudosArquivo(idHistLiberacaoMotorista);
	}
	
	public void setHistLiberacoMotoristaDao(HistLiberacoMotoristaDao dao) {
		setDao(dao);
	}
	
	private HistLiberacoMotoristaDao getHistLiberacoMotoristaDao() {
		return (HistLiberacoMotoristaDao)getDao();
	}

}
