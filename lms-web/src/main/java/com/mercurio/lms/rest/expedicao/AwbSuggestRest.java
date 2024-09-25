package com.mercurio.lms.rest.expedicao;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.format.ISODateTimeFormat;

import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.rest.expedicao.dto.AwbSuggestDTO;
import com.mercurio.lms.rest.expedicao.dto.AwbSuggestFilterDTO;

@Path("/expedicao/awbSuggest")
public class AwbSuggestRest extends BaseSuggestRest<AwbSuggestDTO, AwbSuggestFilterDTO> {
	private static final int INICIO_NR_AWB = 3;
	private static final int MIN_AWB_SUGGEST = 4;

	@InjectInJersey
	AwbService awbService;
	
	@Override
	protected Map<String, Object> filterConvert(AwbSuggestFilterDTO filter) {
		
		if (filter.getValue() == null || filter.getTpStatusAwb() == null) {
			return null;
		}
		
		String value = filter.getValue().replace(" ", "");
		if (value.length() < MIN_AWB_SUGGEST) {
			return null;
		}
		
		String sNrAwb = value.substring(INICIO_NR_AWB);
		if (!StringUtils.isNumeric(sNrAwb)) {
			return null;
		}
		
		String sgEmpresa = value.substring(0, INICIO_NR_AWB).toUpperCase();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tpStatusAwb", filter.getTpStatusAwb());
		map.put("sgEmpresa", sgEmpresa);
		map.put("nrAwb", sNrAwb);
		
		return map;
	}

	@Override
	protected AwbSuggestDTO responseConvert(Map<String, Object> map) {
		AwbSuggestDTO awbSuggestDTO = new AwbSuggestDTO();
			
		awbSuggestDTO.setIdAwb(Long.valueOf(map.get("idAwb").toString()));
		
		String nrAwb;
		String nrAwbFormated;
		if (ConstantesExpedicao.TP_STATUS_AWB_EMITIDO.equals(map.get("tpStatusAwb").toString())) {
			String dsSerie = map.get("dsSerie").toString();
			Long nr = Long.valueOf(map.get("nrAwb").toString());
			Integer dvAwb = Integer.valueOf(map.get("dvAwb").toString());
			
			nrAwb = AwbUtils.getNrAwb(dsSerie, nr, dvAwb);
			nrAwbFormated = AwbUtils.getNrAwbFormated(dsSerie, nr, dvAwb);
		} else {
			nrAwb = awbSuggestDTO.getIdAwb().toString();
			nrAwbFormated = awbSuggestDTO.getIdAwb().toString();
		}
		
		awbSuggestDTO.setDsAwb(map.get("sgEmpresa").toString() +  " " + nrAwb);
		awbSuggestDTO.setDsFormatedAwb(map.get("sgEmpresa").toString() +  " " + nrAwbFormated);
		
		if (map.get("dhEmissao") != null) {
			awbSuggestDTO.setDhEmissao(ISODateTimeFormat.dateTimeParser().parseDateTime(MapUtils.getString(map, "dhEmissao")));
		}
		
		return awbSuggestDTO;
	}

	@Override
	protected AwbService getService() {
		return awbService;
	}

}
