package com.mercurio.lms.veiculoonline.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.veiculoonline.model.dao.RelatorioGreenPODDAO;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.veiculoonline.RelatorioGreenPODService"
 */
public class RelatorioGreenPODService {

	private RelatorioGreenPODDAO relatorioGreenPODDAO;
	
	public List<Map<String, Object>> findByParameters(Map<String, Object> parameters) {
		List<Map<String, Object>> list = relatorioGreenPODDAO.findDadosReport(parameters);		
		
		if(list == null || list.isEmpty()) {
			throw new BusinessException("emptyReport");
		}
		
		return ajustarValores(list);
	}
	
	private List<Map<String, Object>> ajustarValores(List<Map<String, Object>> inputList) {
		List<Map<String, Object>> outputList = new ArrayList<Map<String,Object>>(); 
		for (Map<String, Object> inputMap : inputList) {
			if(inputMap.get("hora_entrega") != null){
				DateTime dtLimite = new DateTime(((java.sql.Timestamp) inputMap.get("hora_entrega")).getTime());
				inputMap.put("hora_entrega", JTDateTimeUtils.formatDateTimeToString(dtLimite, "HH:mm"));
			}
			if(inputMap.get("dh_inclusao_eds") != null){
				int horaInclusao = ((java.sql.Timestamp) inputMap.get("dh_inclusao_eds")).getHours();
				if(horaInclusao >= 7 && horaInclusao < 18 ){
					inputMap.put("dh_inclusao_eds", "Sim");
				} else {
					inputMap.put("dh_inclusao_eds", "Não");
				}
			}
			
			outputList.add(inputMap);
		}
		return outputList;
	}
	
	public void setRelatorioSegurosDAO(RelatorioGreenPODDAO relatorioSegurosDAO) {
		this.relatorioGreenPODDAO = relatorioSegurosDAO;
	}
}