package com.mercurio.lms.franqueados.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.model.Franqueado;
import com.mercurio.lms.franqueados.model.FranqueadoFranquia;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.dao.FranqueadosDAO;


public class FranqueadoService extends CrudService<Franqueado, Long> {
	
	private FranqueadoFranquiaService franqueadoFranquiaService;
	private FranquiaService franquiaService;

	
	@Override
	public Serializable findById(Long id) {
		return ((FranqueadosDAO)getDao()).findById(id);
	}
	
	public void removeById(Long id, List<Franquia> franquiasRemovidas) {
		YearMonthDay date = new YearMonthDay();
		List<FranqueadoFranquia> franquias = franqueadoFranquiaService.findFranqueadoFranquiasByFranqueado(id,date);
		
		if(!franquias.isEmpty()){
			throw new BusinessException("LMS-46112");
		}
		franqueadoFranquiaService.removeByFranqueado(id);
		franquiaService.removeByList(franquiasRemovidas);
		
		super.removeById(id);
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedFranqueados(Map criteria){
		return ((FranqueadosDAO)getDao()).findPaginatedFranqueados(criteria);
	}
	
	public Serializable findByIdFranqueados(Long id) {
		return ((FranqueadosDAO)getDao()).findByIdFranqueados(id);
	}

	@SuppressWarnings("rawtypes")
	public Long getRowCountFranqueados(Map criteria){
		return ((FranqueadosDAO)getDao()).findPaginatedFranqueados(criteria).getRowCount();

	}

	public List<TypedFlatMap> findDocumentosFranqueados(String tipoCalculo, Long idFranquia, YearMonthDay dtIniCompetencia, YearMonthDay dtFimCompetencia, boolean isLocal){
		return getFranqueadoDAO().findDocumentosFranqueados(tipoCalculo, idFranquia, dtIniCompetencia, dtFimCompetencia, isLocal);
	}

	private FranqueadosDAO getFranqueadoDAO() {
		return (FranqueadosDAO) getDao();
	}
	
	public void setFranqueadoDAO(FranqueadosDAO franqueadoDAO) {
        setDao(franqueadoDAO);
    }

	public FranqueadoFranquiaService getFranqueadoFranquiaService() {
		return franqueadoFranquiaService;
	}

	public void setFranqueadoFranquiaService(FranqueadoFranquiaService franqueadoFranquiaService) {
		this.franqueadoFranquiaService = franqueadoFranquiaService;
	}
	
	public FranquiaService getFranquiaService() {
		return franquiaService;
	}

	public void setFranquiaService(FranquiaService franquiaService) {
		this.franquiaService = franquiaService;
	}


	/**
	 * 
	 * @param franqueado
	 * @param novasFranquias - Novas Franquias
	 * @param removidasFranquias - Removidas Franquias
	 * @param novosFranqueadosFrq - Novas FranqueadoFranquias
	 * @param removidosFranqueadosFrq - Removidos FranqueadoFranquias
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Serializable store(Franqueado franqueado, List novasFranquias, List removidasFranquias,List novosFranqueadosFrq, List removidosFranqueadosFrq) {
		Serializable frq = super.store(franqueado);
		franqueado.setIdFranqueado((Long) frq);
		
		franquiaService.storeAll(novasFranquias);
		franqueadoFranquiaService.storeAll(franqueado,novosFranqueadosFrq);

		franqueadoFranquiaService.removeByList(removidosFranqueadosFrq);
		franquiaService.removeByList(removidasFranquias);
		
		return franqueado;
	}
}
