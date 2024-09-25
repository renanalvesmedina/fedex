package com.mercurio.lms.tributos.model.util;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.tributos.dto.DadosInscricaoEstadualColetivaDto;
import com.mercurio.lms.tributos.dto.InscricaoEstadualColetivaFilterDto;
import com.mercurio.lms.tributos.model.param.CalcularIcmsParam;

public class InscricaoEstadualColetivaHelper {

	public static InscricaoEstadualColetivaFilterDto buildFilter(Long idRemetente, YearMonthDay dtEmissao, Long idUfDestino, Long idPais) {
		InscricaoEstadualColetivaFilterDto result = new InscricaoEstadualColetivaFilterDto();
		result.setIdRemetente(idRemetente);
		result.setDtEmissao(dtEmissao);  
		result.setIdUfDestino(idUfDestino);
		result.setIdPais(idPais);
		return result;
	}

	public static void buildObservacao(DadosInscricaoEstadualColetivaDto dadosDto, CalcularIcmsParam cip, String msgObservacaoDocto) {
		String formattedMsg = MessageFormat.format(msgObservacaoDocto, new Object[]{dadosDto.getSgUnidadeFederativa(), dadosDto.getNrInscricaoEstadualColetiva()});
		if (cip != null && StringUtils.isNotBlank(formattedMsg)) {
			cip.addObIcms(formattedMsg);
		}
	}
	
}
