package com.mercurio.lms.rest.recepcaodescarga.exemplo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.rest.recepcaodescarga.exemplo.dto.TelaComponentesDTO;
import com.mercurio.lms.rest.recepcaodescarga.exemplo.dto.TelaComponentesFilterDTO;

class TelaComponentesFakeService {
	
	private static TelaComponentesFakeService instance;
	
	public static TelaComponentesFakeService getInstance() {
		if (instance == null) {
			instance = new TelaComponentesFakeService();
		}
		
		return instance;
	}
	
	private Map<Long, TelaComponentesDTO> repository;
	
	private TelaComponentesFakeService() {
		repository = new HashMap<Long, TelaComponentesDTO>();
	}
	
	public List<TelaComponentesDTO> find(TelaComponentesFilterDTO filter) { 
		List<TelaComponentesDTO> result = new ArrayList<TelaComponentesDTO>();
		
		for (TelaComponentesDTO telaComponentesDTO : repository.values()) {
			if (applyFilter(filter, telaComponentesDTO)) {
				result.add(telaComponentesDTO);
			}
		}
		
		return result;
	} 
	
	public TelaComponentesDTO findById(Long id) {
		return repository.get(id);
	}
	
	public Integer count (TelaComponentesFilterDTO filter) {
		Integer count = 0;
		
		for (TelaComponentesDTO telaComponentesDTO : repository.values()) {
			if (applyFilter(filter, telaComponentesDTO)) {
				count++;
			}
		}
		
		return count;
	}
	
	public Long store(TelaComponentesDTO bean) {
		if (bean.getId() == null) {
			bean.setId(Long.valueOf(repository.size() + 1));
		}
		
		repository.put(bean.getId(), bean);
		
		return bean.getId();
	}
	
	public void removeById(Long id) {
		repository.remove(id);
	}
	
	public void removeByIds(List<Long> ids) { 
		for (Long id : ids) {
			removeById(id);
		}
	}
	
	private boolean applyFilter(TelaComponentesFilterDTO filter, TelaComponentesDTO telaComponentesDTO) {
		if (filter.getId() != null && !filter.getId().equals(telaComponentesDTO.getId())) {
			return false;
		}
		
		if (filter.getData() != null && !filter.getData().equals(telaComponentesDTO.getData())) {
			return false;
		}
		
		if (filter.getDataHoraRange() != null && telaComponentesDTO.getDataHora() != null) {
			DateTime dataHora = telaComponentesDTO.getDataHora();
			
			DateTime start = filter.getDataHoraRange().getStart();
			if (start != null && start.compareTo(dataHora) > 0) {
				return false;
			}
			
			DateTime end = filter.getDataHoraRange().getEnd();
			if (end != null && dataHora.compareTo(end) > 0) {
				return false;
			}
		}
		
		return true;
	}
}