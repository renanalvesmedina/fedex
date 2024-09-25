package com.mercurio.lms.rest.recepcaodescarga.exemplo.dto;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseFilterDTO; 
import com.mercurio.adsm.rest.Range;
 
public class TelaComponentesFilterDTO extends BaseFilterDTO { 
	
	private static final long serialVersionUID = 1L; 
	
	private TelaComponentesDTO telaComponentesDTO;//using Delegation Pattern (see getters and setters)
	
	private YearMonthDay data;
	private Range<DateTime> dataHoraRange;
	
	public TelaComponentesFilterDTO() {
		super();
		telaComponentesDTO = new TelaComponentesDTO();
	}

	public Long getId() {
		return telaComponentesDTO.getId();
	}

	public void setId(Long id) {
		telaComponentesDTO.setId(id);
	}

	public Range<DateTime> getDataHoraRange() {
		return dataHoraRange;
	}

	public void setDataHoraRange(Range<DateTime> dataHoraRange) {
		this.dataHoraRange = dataHoraRange;
	}

	public YearMonthDay getData() {
		return data;
	}

	public void setData(YearMonthDay data) {
		this.data = data;
	}
	
} 
