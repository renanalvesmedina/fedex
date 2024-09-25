package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.ModeloMensagem;
import com.mercurio.lms.contasreceber.model.dao.ModeloDeMensagemDAO;

public class ModeloDeMensagemService extends CrudService<ModeloMensagem, Long> {

	private ModeloDeMensagemDAO modeloMensagemDao;
	
	public List<ModeloMensagem> findModelosDeMensagem(YearMonthDay dtVigencia, DomainValue tpModeloMensagem) {
		return modeloMensagemDao.find(dtVigencia,tpModeloMensagem);
	}
	
	public ModeloMensagem findById(Long id){
		return (ModeloMensagem) super.findById(id);
	}
	public Serializable store(ModeloMensagem bean){
		return super.store(bean);
	}

	public void setModeloMensagemDao(ModeloDeMensagemDAO modeloMensagemDao) {
		setDao( modeloMensagemDao );
		this.modeloMensagemDao = (ModeloDeMensagemDAO) getDao();
	}



}
