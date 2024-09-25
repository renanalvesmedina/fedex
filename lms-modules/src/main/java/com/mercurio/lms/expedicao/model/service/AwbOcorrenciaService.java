package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.AwbOcorrenciaLocalizacao;
import com.mercurio.lms.expedicao.model.dao.AwbOcorrenciaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class AwbOcorrenciaService extends CrudService<AwbOcorrenciaLocalizacao, Long>{

	public java.io.Serializable store(Awb awb, DomainValue tpLocalizacao) {
		return store(awb, tpLocalizacao, JTDateTimeUtils.getDataHoraAtual());
	}
	
	public java.io.Serializable store(Awb awb, DomainValue tpLocalizacao, DateTime dhOcorrencia) {
		AwbOcorrenciaLocalizacao awbOcorrencia = new AwbOcorrenciaLocalizacao();
		awbOcorrencia.setAwb(awb);
		awbOcorrencia.setTpLocalizacao(tpLocalizacao);
		awbOcorrencia.setDhOcorrencia(dhOcorrencia);
		return super.store(awbOcorrencia);
	}
	
	public Serializable store(AwbOcorrenciaLocalizacao awbOcorrenciaLocalizacao) {
		return super.store(awbOcorrenciaLocalizacao);
	}
	
	
	
	public void setAwbOcorrenciaDao(AwbOcorrenciaDAO dao) {
		setDao( dao );
	}
	
	private AwbOcorrenciaDAO getAwbOcorrenciaDAO() {
		return (AwbOcorrenciaDAO) getDao();
	}
	
	public Boolean findBooleanAwbOcorrenciaLocalizacaoByIdAwbAndTpLocalizacao(Long idAwb, String tpLocalizacaoAwb){
		List<AwbOcorrenciaLocalizacao> ocorrencias = findAwbOcorrenciaLocalizacaoByIdAwbAndTpLocalizacao(idAwb, tpLocalizacaoAwb);
		return ocorrencias.isEmpty();
	}
	
	public List<AwbOcorrenciaLocalizacao> findAwbOcorrenciaLocalizacaoByIdAwbAndTpLocalizacao(Long idAwb, String tpLocalizacaoAwb){
		return getAwbOcorrenciaDAO().findAwbOcorrenciaLocalizacaoByIdAwbAndTpLocalizacao(idAwb, tpLocalizacaoAwb);
	}
	
	public void removeAwbOcorrenciaByIdAwb(List<Long> idsAwb) {
		for (Long idAwb : idsAwb) {
			List<AwbOcorrenciaLocalizacao> l = getAwbOcorrenciaDAO().findAwbOcorrenciaLocalizacaoByIdAwb(idAwb);
			for (AwbOcorrenciaLocalizacao awbOcorrenciaLocalizacao : l) {
				removeById(awbOcorrenciaLocalizacao.getIdAwbOcorrencia());
			}
		}
	}

}
