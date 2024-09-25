package com.mercurio.lms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.questionamentofaturas.model.AnexoQuestionamentoFatura;

/**
 * Tratamento para Anexos na Sessão
 * @author Andre Valadas
 */
public abstract class AnexosQuestionamentoUtils {
	private static final String SESSION_ANEXOS = "SESSION_ANEXOS"; 

	public static void addAnexoInSession(AnexoQuestionamentoFatura anexoQuestionamentoFatura) {
		List<AnexoQuestionamentoFatura> anexos = (List)SessionContext.get(SESSION_ANEXOS);
		if(anexos == null) {
			anexos = new ArrayList<AnexoQuestionamentoFatura>();
		}
		anexoQuestionamentoFatura.setIdAnexoQuestionamentoFatura(-System.currentTimeMillis());
		anexos.add(anexoQuestionamentoFatura);
		SessionContext.set(SESSION_ANEXOS, anexos);
	}
	public static void removeAnexosFromSession() {
		SessionContext.set(SESSION_ANEXOS, null);
	}
	public static void removeAnexosFromSession(Long id) {
		List<AnexoQuestionamentoFatura> anexos = (List)SessionContext.get(SESSION_ANEXOS);
		List<AnexoQuestionamentoFatura> toReturn = new ArrayList<AnexoQuestionamentoFatura>();
		if(anexos != null) {
			for (AnexoQuestionamentoFatura anexo : anexos) {
				if(!anexo.getIdAnexoQuestionamentoFatura().equals(id)) {
					toReturn.add(anexo);
				}
			}
		}
		SessionContext.set(SESSION_ANEXOS, toReturn);
	}
	public static Boolean existAnexosInSession() {
		List<String> anexos = (List)SessionContext.get(SESSION_ANEXOS);
		return (anexos != null && anexos.size() > 0);
	}
	public static List<Map<String, Object>> findAnexosMappedInSession() {
		List<AnexoQuestionamentoFatura> anexos = (List)SessionContext.get(SESSION_ANEXOS);
		List<Map<String, Object>> toReturn = new ArrayList<Map<String,Object>>();
		if(anexos != null) {
			for (AnexoQuestionamentoFatura anexo : anexos) {
				toReturn.add(convertBeanToMap(anexo));
			}
		}
		return toReturn;
	}
	public static List<AnexoQuestionamentoFatura> findAnexosInSession() {
		List<AnexoQuestionamentoFatura> anexosInSession = (List)SessionContext.get(SESSION_ANEXOS);
		if(anexosInSession == null) {
			return Collections.EMPTY_LIST;
		}
		return anexosInSession;
	}
	public static Map<String, Object> convertBeanToMap(AnexoQuestionamentoFatura anexo) {
		Map<String, Object> anexoMapped = new HashMap<String, Object>();
		anexoMapped.put("idAnexoQuestionamentoFatura", anexo.getIdAnexoQuestionamentoFatura());
		anexoMapped.put("nmUsuario", anexo.getUsuario().getUsuarioADSM().getNmUsuario());
		anexoMapped.put("dsAnexo", anexo.getDsAnexo());
		anexoMapped.put("dhCriacao", anexo.getDhCriacao());
		anexoMapped.put("dcArquivo", anexo.getDcArquivo());
		anexoMapped.put("dcArquivoTemp", anexo.getDcArquivoTemp());
		return anexoMapped;
	}
}