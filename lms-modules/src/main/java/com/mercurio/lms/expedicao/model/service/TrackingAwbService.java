package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.AwbOcorrenciaLocalizacao;
import com.mercurio.lms.expedicao.model.LocalizacaoAwbCiaAerea;
import com.mercurio.lms.expedicao.model.TrackingAwb;
import com.mercurio.lms.expedicao.model.dao.TrackingAwbDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class TrackingAwbService extends CrudService<TrackingAwb, Long> {
	
	private AwbOcorrenciaService awbOcorrenciaService;
	
	private DomainValueService domainValueService;
	
	private AwbService awbService;
	
	private LocalizacaoAwbCiaAereaService localizacaoAwbCiaAereaService;
	
	private TrackingAwbDAO getTrackingAwbDAO() {
        return (TrackingAwbDAO) getDao();
    }
    
    public void setTrackingAwbDAO(TrackingAwbDAO dao) {
        setDao(dao);
    }
    
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }
    
    /**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Long store(TrackingAwb bean) {
    	return getTrackingAwbDAO().store(bean);
    }
    
    public java.io.Serializable store(Awb awb, LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea, DateTime dhEvento) {
    	TrackingAwb trackingAwb = new TrackingAwb();
    	trackingAwb.setAwb(awb);
    	trackingAwb.setDhEvento(dhEvento);
    	trackingAwb.setLocalizacaoAwbCiaAerea(localizacaoAwbCiaAerea);
    	
    	return super.store(trackingAwb);
    }

    public java.io.Serializable storeTrackingAwb(Awb awb, Long idCiaAerea, String tpLocalizacao) {
    	LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea = new LocalizacaoAwbCiaAerea();
    	localizacaoAwbCiaAerea = localizacaoAwbCiaAereaService.findByIdCiaAereaAndTpLocalizacao(idCiaAerea, new DomainValue(tpLocalizacao));
    	
    	return store(awb, localizacaoAwbCiaAerea, JTDateTimeUtils.getDataHoraAtual());
    }
    
	public ResultSetPage<Map<String, Object>> findPaginated(Long idAwb) {
		ResultSetPage<Map<String, Object>> rs = getTrackingAwbDAO().findPaginated(idAwb);
		List<Map<String, Object>> result = rs.getList();
		for (Map<String, Object> map : result) {
			map.put("tpLocalizacao", map.get("tpLocalizacao.description"));
		}
		return rs;
	}

	public List<Map<String, Object>> findTrackingAwbByCiaAereaAndAwb(Long idAwb) {
		return getTrackingAwbDAO().findTrackingAwbByCiaAereaAndAwb(idAwb);
	}

	public void storeIncluirEventoLocalizacaoModal(Long idAwb, Long idLocalizacao, DateTime dtEvento) {
		TrackingAwb trackingAwb = new TrackingAwb();
		
		Awb awb = awbService.findById(idAwb);
		
		LocalizacaoAwbCiaAerea localizacaoAwbCiaAerea = localizacaoAwbCiaAereaService.findById(idLocalizacao);
		
		trackingAwb.setAwb(awb);
		trackingAwb.setLocalizacaoAwbCiaAerea(localizacaoAwbCiaAerea);
		trackingAwb.setDhEvento(dtEvento);
		
		getDao().store(trackingAwb);
		
		AwbOcorrenciaLocalizacao awbOcorrenciaLocalizacao = new AwbOcorrenciaLocalizacao();
		awbOcorrenciaLocalizacao.setAwb(awb);
		awbOcorrenciaLocalizacao.setDhOcorrencia(dtEvento);
		awbOcorrenciaLocalizacao.setTpLocalizacao(localizacaoAwbCiaAerea.getTpLocalizacaoCiaAerea());
		
		awbOcorrenciaService.store(awbOcorrenciaLocalizacao);
		
		awb.setTpLocalizacao(localizacaoAwbCiaAerea.getTpLocalizacaoCiaAerea());
		
		awbService.store(awb);
	}

	public AwbOcorrenciaService getAwbOcorrenciaService() {
		return awbOcorrenciaService;
	}

	public void setAwbOcorrenciaService(AwbOcorrenciaService awbOcorrenciaService) {
		this.awbOcorrenciaService = awbOcorrenciaService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public AwbService getAwbService() {
		return awbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	public LocalizacaoAwbCiaAereaService getLocalizacaoAwbCiaAereaService() {
		return localizacaoAwbCiaAereaService;
	}

	public void setLocalizacaoAwbCiaAereaService(
			LocalizacaoAwbCiaAereaService localizacaoAwbCiaAereaService) {
		this.localizacaoAwbCiaAereaService = localizacaoAwbCiaAereaService;
	}

}
