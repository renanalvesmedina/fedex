package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.ConhecimentoFedex;
import com.mercurio.lms.edi.model.ConhecimentoVolumeFedex;
import com.mercurio.lms.edi.model.dao.ConhecimentoFedexDAO;
import com.mercurio.lms.edi.model.dao.ConhecimentoVolumeFedexDAO;
import com.mercurio.lms.municipios.model.Filial;

public class ConhecimentoFedexService extends CrudService<ConhecimentoFedex, Long> {

	private ConhecimentoVolumeFedexDAO conhecimentoVolumeFedexDAO;
	
	@Override
	protected ConhecimentoFedexDAO getDao() {
		return (ConhecimentoFedexDAO) super.getDao();
	}
	
	public void setConhecimentoFedexDAO(ConhecimentoFedexDAO dao) {
		this.setDao(dao);
	}

	// LMSA-6267: LMSA-6630
	public BigDecimal findTotalPesoRealConhecimentoFedexByIdControleCarga(Long idControleCarga) {
		return getDao().findTotalPesoRealConhecimentoFedexByIdControleCarga(idControleCarga);
	}
	
	public List<ConhecimentoFedex> findByChaveMdfeFedex(final String chaveMdfeFedex) {
		if (chaveMdfeFedex == null || chaveMdfeFedex.isEmpty()) {
			throw new BusinessException("Chave MDFe não informada!");
		}
		return getDao().findByChaveMdfeFedex(chaveMdfeFedex);
	}
	
	public List<ConhecimentoFedex> findByControleCarga(final Long idControleCarga) {
		if (idControleCarga == null) {
			throw new BusinessException("Controle de Carga não informado!");
		}
		return getDao().findByControleCarga(idControleCarga);
	}
	
	public void desvincularConhecimentosFedexControleCarga(Long idControleCarga) {
		List<ConhecimentoFedex> conhecimentos = findByControleCarga(idControleCarga);
		if (conhecimentos != null && !conhecimentos.isEmpty()) {
			List<ConhecimentoFedex> updates = new ArrayList<ConhecimentoFedex>(conhecimentos.size());
			for (ConhecimentoFedex conhecimento : conhecimentos) {
				conhecimento.setControleCarga(null);
				updates.add(conhecimento);
			}
			storeAll(updates);
		}
	}
	
	public void setConhecimentoVolumeFedexDAO (ConhecimentoVolumeFedexDAO conhecimentoVolumeFedexDAO) {
		this.conhecimentoVolumeFedexDAO = conhecimentoVolumeFedexDAO;
	}
	
    public ConhecimentoFedex findByChaveMdfeAndConhecimentoFedex(final String chaveMdfeFedex, final String conhecimentoFedex) {
		if (chaveMdfeFedex == null || chaveMdfeFedex.isEmpty()) {
			throw new BusinessException("Chave MDFe não informada!");
		}
		if (conhecimentoFedex == null || conhecimentoFedex.isEmpty()) {
			throw new BusinessException("Conhecimento Fedex não informado!");
		}
		return getDao().findByChaveMdfeAndConhecimentoFedex(chaveMdfeFedex, conhecimentoFedex);
    }

    @Override
    public Serializable store(ConhecimentoFedex bean) {
		if(bean.getIdConhecimentoFedex() == null){
			bean.setIdConhecimentoFedex(getDao().findSequence());
		}
		if (bean.getVolumes() != null && !bean.getVolumes().isEmpty()) {
			for (ConhecimentoVolumeFedex volume : bean.getVolumes()) {
				if(volume.getIdVolumeConhecimentoFedex() == null){
					volume.setIdVolumeConhecimentoFedex(conhecimentoVolumeFedexDAO.findSequence());
				}
			}
		}
		return super.store(bean);
	}
	
    @Override
    public void storeAll(List<ConhecimentoFedex> list) {
    	for (ConhecimentoFedex bean : list)  {
			this.store(bean);
    	}
	}

	public ConhecimentoFedex findByFilialNrConhecimento(String sgFilial,
			String nrDocumentoTNT) {
		return getDao().findByFilialNrConhecimento(sgFilial,
				nrDocumentoTNT);
	}

	public ConhecimentoFedex findByIdConhecimento(Long idConhecimento) {
		return  getDao().findByIdConhecimento(idConhecimento);
	}

}
