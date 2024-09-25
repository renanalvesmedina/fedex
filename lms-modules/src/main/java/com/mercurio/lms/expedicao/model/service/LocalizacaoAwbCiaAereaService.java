package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.LocalizacaoAwbCiaAerea;
import com.mercurio.lms.expedicao.model.dao.LocalizacaoAwbCiaAereaDAO;

public class LocalizacaoAwbCiaAereaService extends CrudService<LocalizacaoAwbCiaAerea, Long> {
	private LocalizacaoAwbCiaAereaDAO getLocalizacaoAwbCiaAereaDAO() {
        return (LocalizacaoAwbCiaAereaDAO) getDao();
    }
    
    public void setLocalizacaoAwbCiaAereaDAO(LocalizacaoAwbCiaAereaDAO dao) {
        setDao(dao);
    }
    
    public Integer getRowCountLocalizacaoAwbCiaAerea(final TypedFlatMap criteria) {
		return getLocalizacaoAwbCiaAereaDAO().getRowCount(criteria);
	}
	
	public List<Map<String, Object>> findPaginated(final PaginatedQuery paginatedQuery) {
		
		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		
		List list = getLocalizacaoAwbCiaAereaDAO().findPaginated(paginatedQuery);	
		
		
		for (Object lac : list) {
			Map<String, Object> lcaMap = new TypedFlatMap();
			Object[] campos = (Object[]) lac;
			lcaMap.put("idLocalizacaoAwbCiaAerea", Long.parseLong(campos[0].toString()));
			lcaMap.put("idEmpresa", Long.parseLong(campos[1].toString()));
			lcaMap.put("nmCiaAerea", campos[2].toString());
			lcaMap.put("dsTracking", campos[3].toString());
			lcaMap.put("tpLocalizacaoCiaAerea", campos[4].toString());
			lcaMap.put("tpLocalizacaoAtual", campos[5] != null ? campos[5].toString() : "");
			mapList.add(lcaMap);
		}
		
		return mapList;
	}
    
    /**
	 * Recupera uma instância de <code>LocalizacaoAwbCiaAerea</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public LocalizacaoAwbCiaAerea findById(java.lang.Long id) {
        return getLocalizacaoAwbCiaAereaDAO().findById(id);
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
    public Long store(LocalizacaoAwbCiaAerea bean) {
    	return getLocalizacaoAwbCiaAereaDAO().store(bean);
    }
    
    
    public List<LocalizacaoAwbCiaAerea> findLocalizacaoesCiaAereaByIdCiaAereaAndTpLocalizacao(Long idCiaAerea){
    	return getLocalizacaoAwbCiaAereaDAO().findLocalizacaoesCiaAereaByIdCiaAereaAndTpLocalizacao(idCiaAerea);
    }

	public LocalizacaoAwbCiaAerea findByIdCiaAereaAndTpLocalizacao(Long idCiaAerea, DomainValue tpLocalizacao) {
		return getLocalizacaoAwbCiaAereaDAO().findByIdCiaAereaAndTpLocalizacao(idCiaAerea, tpLocalizacao);
	}
    
}
