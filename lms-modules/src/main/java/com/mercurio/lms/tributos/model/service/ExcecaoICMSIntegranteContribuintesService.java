package com.mercurio.lms.tributos.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.tributos.model.ExcecaoICMSIntegrantesContribuintes;
import com.mercurio.lms.tributos.model.dao.ExcecaoICMSIntegrantesContribuintesDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class ExcecaoICMSIntegranteContribuintesService extends CrudService<ExcecaoICMSIntegrantesContribuintes, Long> {

	public final void setExcecaoICMSClienteLogDAO(ExcecaoICMSIntegrantesContribuintesDAO dao){
		setDao(dao);
	}
	
	private ExcecaoICMSIntegrantesContribuintesDAO getExcecaoICMSIntegrantesContribuintesDAO() {
	        return (ExcecaoICMSIntegrantesContribuintesDAO) getDao();
	}
	
	public Serializable store(ExcecaoICMSIntegrantesContribuintes bean) {
		validaExcecaoIcms(bean);
		return super.store(bean);
	}

	private void validaExcecaoIcms(ExcecaoICMSIntegrantesContribuintes bean) {
		
		YearMonthDay today = JTDateTimeUtils.getDataAtual();
		if (bean.getId() == null && (today.equals(bean.getDtVigenciaInicial()) || today.isAfter(bean.getDtVigenciaInicial()))) {
			throw new BusinessException("LMS-30040");
		}
		
		if (bean.getDtVigenciaInicial() != null && bean.getDtVigenciaFinal() != null && bean.getDtVigenciaFinal().isBefore(bean.getDtVigenciaInicial())) {
			throw new BusinessException("LMS-01030");
		}
		
		if(bean.getDtVigenciaFinal() != null && today.isAfter(bean.getDtVigenciaFinal())){
			throw new BusinessException("LMS-01030");
		}
				
		List list = getExcecaoICMSIntegrantesContribuintesDAO().findExcecaoICMSClienteByVigenciaEquals(bean.getDtVigenciaInicial(), 
																			bean.getDtVigenciaFinal(), 
																			bean.getUnidadeFederativaOrigem().getIdUnidadeFederativa(), 
																			bean.getUnidadeFederativaDestino().getIdUnidadeFederativa(), 
																			bean.getTpFrete().getValue(),
																			bean.getTpIntegranteFrete().getValue(), 
																			bean.getId());
		if(!list.isEmpty()){ 
			throw new BusinessException("LMS-00047");
		}		
	}
	
	public ExcecaoICMSIntegrantesContribuintes findById(Long id) {
        return (ExcecaoICMSIntegrantesContribuintes)super.findById(id);
    }
	
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public void removeById(Long id) {
		super.removeById(id);
	}	
	
	
}