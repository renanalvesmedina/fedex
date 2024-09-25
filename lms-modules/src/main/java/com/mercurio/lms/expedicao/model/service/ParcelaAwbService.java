package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.ParcelaAwb;
import com.mercurio.lms.expedicao.model.dao.ParcelaAwbDAO;

public class ParcelaAwbService extends CrudService<ParcelaAwb, Long>{
	
	public void store(List<ParcelaAwb> parcelasAwb, Awb awbInsersaoAutomatica) {
		for (ParcelaAwb parcela : parcelasAwb){
			parcela.setAwb(awbInsersaoAutomatica);
			super.store(parcela);
		}
		
	}
	
	public void setParcelaAwbDao(ParcelaAwbDAO dao) {
		setDao( dao );
	}

}
