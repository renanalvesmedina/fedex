package com.mercurio.lms.ppd.model.service;

import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.ppd.model.dao.PpdCorporativoDAO;

public class PpdCorporativoService {		
	private PpdCorporativoDAO corporativoDAO;
	
	public Map<String,Object> findConhecimento(String unidSiglaOrigem, Long numero, YearMonthDay dtEmissao) {
		return corporativoDAO.findConhecimento(unidSiglaOrigem, numero, dtEmissao);		
	}

	public Map<String,Object> findLastRnc(String unidSiglaOrigem, Long numero) {
		return corporativoDAO.findLastRnc(unidSiglaOrigem, numero);		
	}

	public Map<String,Object> findRnc(String unidSigla, Long numero, YearMonthDay dtEmissao) {
		return corporativoDAO.findRnc(unidSigla, numero, dtEmissao);		
	}
	
	public Map<String,Object> findRncByConhecimento(String unidSiglaOrigem, Long numero, YearMonthDay dtEmissao) {
		return corporativoDAO.findRncByConhecimento(unidSiglaOrigem, numero, dtEmissao);		
	}
	
	public Map<String,Object> findConhecimentoByRnc(String unidSigla, Long numero, YearMonthDay dtEmissao) {
		Map<String,Object> rnc = corporativoDAO.findRnc(unidSigla, numero, dtEmissao);
		if(rnc!=null) {
			String ctrcUnidSigla = (String)rnc.get("ctrcUnidSigla");
			Long ctrcNumero = (Long)rnc.get("ctrcNumero");
			YearMonthDay ctrcDataEmissao = (YearMonthDay)rnc.get("ctrcDataEmissao");
			return corporativoDAO.findConhecimento(ctrcUnidSigla, ctrcNumero, ctrcDataEmissao);
		} else {
			return null;
		}			
	}
	
	public String findSerieDocumento(String unidOrigemLeg, DateTime dataEmissao, String tipoRetorno, Long nrConhecimento) {

		return corporativoDAO.findSerieDocumento(unidOrigemLeg, dataEmissao, tipoRetorno, nrConhecimento);
	}
	
	public Long findIdDoctoServico(String sgFilialOrigem, Long nrDoctoServico){
		return corporativoDAO.findIdDoctoServico(sgFilialOrigem, nrDoctoServico);
	}
	
	public PpdCorporativoDAO getCorporativoDAO() {
		return corporativoDAO;
	}

	public void setConhecimentoCorporativoDAO(PpdCorporativoDAO corporativoDAO) {
		this.corporativoDAO = corporativoDAO;
	}	
}