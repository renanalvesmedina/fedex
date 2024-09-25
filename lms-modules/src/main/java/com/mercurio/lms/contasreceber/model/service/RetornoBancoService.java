package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.RetornoBanco;
import com.mercurio.lms.contasreceber.model.dao.RetornoBancoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class RetornoBancoService extends CrudService<RetornoBanco, Long> {

	
	@Override
	public List<RetornoBanco> find(Map criteria){
		validateIfThereIsAtLeastOneFieldFilled(criteria);
		
		return getRetornoBancoDAO().findComDsOcorrenciaEMotivo(criteria);
	}
	
	public Integer findCount(Map criteria){
		
		return getRetornoBancoDAO().findCount(criteria);
	}
	
	public void inclusaoMensagemRetorno(BoletoDMN boletoDmn,
			Boleto boleto, String mensagem, Object[] mensagemArgs) {

		RetornoBanco retornoBanco = new RetornoBanco();

		retornoBanco.setBoleto(boleto);
		retornoBanco.setNrBoleto(extractNrBoleto(boletoDmn, boleto));
		retornoBanco.setNrBanco(boletoDmn.getNrBanco());
		retornoBanco.setDtMovimento(boletoDmn.getDtMovimento());
		retornoBanco.setVlTotal(extractVlTotal(boletoDmn));
		retornoBanco.setVlAbatimento(extractVlAbatimento(boletoDmn));
		retornoBanco.setVlDesconto(extractVlDesconto(boletoDmn));
		retornoBanco.setVlJuros(extractVlJuros(boletoDmn));
		retornoBanco.setNrOcorrencia(boletoDmn.getNrOcorrencia() == null ? null : boletoDmn.getNrOcorrencia().toString());
		retornoBanco.setNrMotivoRejeicao(boletoDmn.getNrMotivoRejeicao() == null ? null : boletoDmn.getNrMotivoRejeicao().toString());
		
		if(mensagemArgs==null) {
			retornoBanco.setDsRetornoBanco(getMessage(mensagem));	
		} else {
			retornoBanco.setDsRetornoBanco(getMessage(mensagem, mensagemArgs));
		}
		
		retornoBanco.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		retornoBanco.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		
		this.store(retornoBanco);
		
	}

	
	public void inclusaoMensagemRetorno(BoletoDMN boletoDmn, Boleto boleto, String mensagem) {
		inclusaoMensagemRetorno(boletoDmn, boleto, mensagem, null);
	}

	@Override
	public java.io.Serializable store(RetornoBanco retornoBanco) {
		getRetornoBancoDAO().store(retornoBanco, true);
		
		return retornoBanco;
	}
	
	public void setRetornoBancoDAO(RetornoBancoDAO dao) {
		setDao(dao);
	}

	public RetornoBancoDAO getRetornoBancoDAO() {
		return (RetornoBancoDAO) getDao();
	}

	private String extractNrBoleto(BoletoDMN boletoDmn,
			Boleto boleto) {
		return boleto != null ? boleto.getNrBoleto() : boletoDmn.getNrBoleto();
	}

	private BigDecimal extractVlTotal(BoletoDMN boletoDmn) {
		return boletoDmn.getVlTotal() != null ? boletoDmn.getVlTotal() : new BigDecimal("0");
	}

	private BigDecimal extractVlAbatimento(BoletoDMN boletoDmn) {
		return boletoDmn.getVlAbatimento() != null ? boletoDmn.getVlAbatimento() : new BigDecimal("0");
	}

	private BigDecimal extractVlDesconto(BoletoDMN boletoDmn) {
		return boletoDmn.getVlDesconto() != null ? boletoDmn.getVlDesconto() : new BigDecimal("0");
	}

	private BigDecimal extractVlJuros(BoletoDMN boletoDmn) {
		return boletoDmn.getVlJuros() != null ? boletoDmn.getVlJuros() : new BigDecimal("0");
	}

	public List<Map<String, Object>> findRetornoBancoReport(Map<String, Object> criteria) {
		validateIfThereIsAtLeastOneFieldFilled(criteria);
		List<Map<String, Object>> report = getRetornoBancoDAO().findReport(criteria);

		removeLineEndings(report);

		return report;
	}

	private void removeLineEndings(List<Map<String, Object>> report) {
		for (Map<String, Object> retornoBanco: report) {
			String observacoes = (String) retornoBanco.get("observacoes");
			if(StringUtils.isNotBlank(observacoes)) {
				retornoBanco.put("observacoes", observacoes.replaceAll("(\\r|\\n)", ""));
			}
		}
	}

	private void validateIfThereIsAtLeastOneFieldFilled(Map map) {
		boolean isThereOneFieldFilled = verifyIfThereIsAtLeastOneFieldFilled(map);
		
		if (!isThereOneFieldFilled) {
			throw new BusinessException("LMS-00055");
		}
	}

	private boolean verifyIfThereIsAtLeastOneFieldFilled(Map map) {
		for (Object key : map.keySet()) {
			Object value = map.get(key);
			
			if (value != null) {
				
				if (value instanceof String) {
					if (!((String) value).isEmpty()) {
						return true;
					}
				} else {
					return true;	
				}
				
			}
		}
		
		return false;
	}
	
	public List<RetornoBanco> fingByNrBoletoDsLinha(String nrBoleto, YearMonthDay dtMovimento, String dsLinha){
		return getRetornoBancoDAO().fingByNrBoletoDsLinha(nrBoleto, dtMovimento, dsLinha);
	}

}
