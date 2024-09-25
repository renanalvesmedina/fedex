package com.mercurio.lms.franqueados.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.dao.FranquiaDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;

public class FranquiaService extends CrudService<Franquia, Long> {

	private FilialService filialService;
	
	public List<Franquia> findFranquiasAtivasByIdFranquiaVigencia(Long idFranquia, YearMonthDay vigencia){
		return getFranquiaDAO().findFranquiasAtivasByIdFranquiaVigencia(idFranquia, vigencia);
	}
	
	public Franquia findFranquiasAtivasBySiglaFranquia(String sgFranquia, Long idEmpresa, YearMonthDay dtVigencia) {
		return getFranquiaDAO().findFranquiasAtivasBySiglaFranquia(sgFranquia, idEmpresa, dtVigencia);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> findLookupFranquia(Map criteria) {

    	List franquia = new ArrayList();
		
		Filial filial = filialService.findBySgFilialAndTpEmpresa((String)criteria.get("filial.sgFilial"), "M");
		
		if(filial != null && filial.getIdFilial() != null) {
			criteria.put("idFranquia", filial.getIdFilial() );
		} else {
			throw new BusinessException("LMS-29054");
		}
		
		Franquia result  = getFranquiaDAO().findLookupFranquia(criteria);
		if (result != null) {
			Map<String, Object> mapFilial = new HashMap<String, Object>();
			mapFilial.put("filial.sgFranquia", result.getFilial().getSgFilial());
			mapFilial.put("idFranquia", result.getIdFranquia());
			mapFilial.put("filial.nmFantasiaFilial", result.getFilial().getPessoa().getNmFantasia());
			mapFilial.put("Franquia", result);
			franquia.add(mapFilial);
		}
		
		return franquia;
	}
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public ResultSetPage findPaginated(Map criteria) {
		Map<String, Object> newCriteria = new HashMap<String, Object>();
		newCriteria.put("franquia.filial.empresa.idEmpresa", criteria.get("empresa.id") != null ? (Long) criteria.get("empresa.id") : null);
		newCriteria.put("franquia.filial.sgFilial", criteria.get("sgFilial") != null ? (String) criteria.get("sgFilial") : null);
		newCriteria.put("vigenteEm", criteria.get("vigenteEm") != null ? (YearMonthDay) criteria.get("vigenteEm") : null);
		newCriteria.put("franquia.filial.pessoa.nrIdentificacao", criteria.get("nrIdentificacao") != null ? (String) criteria.get("nrIdentificacao") : null);		
		newCriteria.put("franquia.filial.pessoa.tpIdentificacao", criteria.get("tpIdentificacao") != null ? (String) criteria.get("tpIdentificacao") : null);
		newCriteria.put("franquia.filial.pessoa.nmPessoa", criteria.get("nmPessoa") != null ? (String) criteria.get("nmPessoa") : null);
		newCriteria.put("franquia.filial.empresa.pessoa.nmFantasia", criteria.get("nmFantasia") != null ? (String) criteria.get("nmFantasia") : null);
		newCriteria.put("franquia.filial.empresa.pessoa.tpEmpresa", criteria.get("tpEmpresa") != null ? (String) criteria.get("tpEmpresa") : null);
		newCriteria.put("_currentPage", criteria.get("_currentPage") != null ? (String) criteria.get("_currentPage") : null);
		newCriteria.put("_order", criteria.get("_order") != null ? (String) criteria.get("_order") : null);
		newCriteria.put("_pageSize", criteria.get("_pageSize") != null ? (String) criteria.get("_pageSize") : null);

		ResultSetPage rspFilial =  getFranquiaDAO().findPaginatedFranquia(newCriteria, FindDefinition.createFindDefinition(newCriteria));
		
    	List listFilial = new ArrayList();
    	for (int i=0; i< rspFilial.getList().size(); i++) {
    		Franquia tfmFilial = (Franquia) rspFilial.getList().get(i);

    		Map mapFilial = new HashMap();
    		
			mapFilial.put("idFilial", tfmFilial.getIdFranquia());
			mapFilial.put("sgFilial", tfmFilial.getFilial().getSgFilial());
			mapFilial.put("cdFilial", tfmFilial.getFilial().getCodFilial());

			mapFilial.put("nmPessoaEmpresa", tfmFilial.getFilial().getEmpresa().getPessoa().getNmPessoa());
			mapFilial.put("nrIdentificacaoEmpresa", tfmFilial.getFilial().getEmpresa().getPessoa().getNrIdentificacao());
			mapFilial.put("idEmpresa", tfmFilial.getFilial().getEmpresa().getIdEmpresa());

			mapFilial.put("tpIdentificacao", tfmFilial.getFilial().getPessoa().getTpIdentificacao().getValue());
			mapFilial.put("nrIdentificacao", tfmFilial.getFilial().getPessoa().getNrIdentificacaoFormatado());
			mapFilial.put("nmFantasia", tfmFilial.getFilial().getPessoa().getNmFantasia());

			if (tfmFilial.getFilial().getLastHistoricoFilial()!= null) {
				mapFilial.put("dtRealOperacaoInicial", tfmFilial.getFilial().getLastHistoricoFilial().getDtRealOperacaoInicial());
				mapFilial.put("dtRealOperacaoFinal", tfmFilial.getFilial().getLastHistoricoFilial().getDtRealOperacaoFinal());
			}
    		
    		listFilial.add(mapFilial);
    	}

    	rspFilial.setList(listFilial);
    	return rspFilial;
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		return getFranquiaDAO().getRowCountFranquia(criteria);
	}

	private FranquiaDAO getFranquiaDAO() {
		return (FranquiaDAO) getDao();
	}
	
	public void setFranquiaDAO(FranquiaDAO franquiaDAO) {
        setDao(franquiaDAO);
    }

	@Override
	public Franquia findById(Long id) {
        return getFranquiaDAO().findById(id);
    }

	public Boolean isTpOperacaoEntreFranquias(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtVigencia) {
		List<Long> count =  getFranquiaDAO().isTpOperacaoEntreFranquias(idFilialOrigem, idFilialDestino, dtVigencia);
		if(count != null && count.size() == 1 && count.get(0).equals(2L)){
			return true;
		}
		return false;
	}
	
	public Map<String, Object> findDadosFranquia(Long idFranquia){
		return getFranquiaDAO().findDadosFranquia(idFranquia);
	}
	
	@Override
	public Serializable store(Franquia bean) {
		return super.store(bean);
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	@SuppressWarnings("rawtypes")
	public void removeByList(List list) {
		
		List<Long> ids = new ArrayList<Long>(); 
		for (Object object : list) {
			Franquia frq = (Franquia) object;
			ids.add(frq.getIdFranquia());
		}

		super.removeByIds(ids);
	}

	public List<Franquia> findFranquiasAtivasByCompetencia(YearMonthDay vigencia,Long idFranquia){
		return getFranquiaDAO().findFranquiasAtivasByCompetencia(vigencia,idFranquia);
	}
	
}
