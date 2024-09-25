package com.mercurio.lms.rest.municipios;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.VarcharI18nConverter;
import com.mercurio.adsm.core.util.VarcharI18nUtil;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.rest.BaseSuggestRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.coleta.model.service.LocalidadeEspecialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.rest.municipios.dto.LocalidadeEspecialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.LocalidadeEspecialSuggestFilterDTO;
import com.mercurio.lms.rest.municipios.dto.PaisSuggestDTO;
import com.mercurio.lms.util.PessoaUtils;



@Path("/municipios/localidadeEspecialSuggest")
public class LocalidadeEspecialSuggestRest extends BaseSuggestRest<LocalidadeEspecialSuggestDTO, LocalidadeEspecialSuggestFilterDTO> {
	
	@InjectInJersey
	LocalidadeEspecialService localidadeEspecialService;

	@Override
	protected Map<String, Object> filterConvert(LocalidadeEspecialSuggestFilterDTO filter) {
		String value = filter.getValue();
		
		
		Map<String, Object> filtros = new HashMap<String, Object>();
		filtros.put("dsLocalidade", value);
		
		return filtros;
	}

	@Override
	protected LocalidadeEspecialSuggestDTO responseConvert(Map<String, Object> map) {
		LocalidadeEspecialSuggestDTO localidadeEspecialSuggestDTO = new LocalidadeEspecialSuggestDTO();
		if (map.get("DSLOCALIDADE") != null) {
			VarcharI18n dsLocalidade = VarcharI18nUtil.createVarcharI18n( map.get("DSLOCALIDADE"));
			//VarcharI18n dsLocalidade =  new VarcharI18n(map.get("DSLOCALIDADE").toString()) ;
			localidadeEspecialSuggestDTO.setDsLocalidade(dsLocalidade.getValue());
		}
		
		return localidadeEspecialSuggestDTO;
	}

	@Override
	protected LocalidadeEspecialService getService() {
		return localidadeEspecialService;
	}

}
	