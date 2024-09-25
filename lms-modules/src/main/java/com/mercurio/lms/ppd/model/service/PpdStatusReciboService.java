package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.PpdStatusRecibo;
import com.mercurio.lms.ppd.model.dao.PpdStatusReciboDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class PpdStatusReciboService extends CrudService<PpdStatusRecibo, Long> {		
	private DomainValueService domainValueService;	
	private PpdReciboService reciboService;
	private UsuarioLMSService usuarioService;
	PpdJdeService ppdJdeService;
	
	
	public void removeById(Long id) {				
		super.removeById(id);
	}
	
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);		
	}	
	
	public PpdStatusRecibo findById(Long id) {
		return getDAO().findById(id);
	}
	
	public ResultSetPage<PpdStatusRecibo> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}
	
	
	public Serializable store(PpdStatusRecibo bean) {
		return super.store(bean);		
	}	
		
	public Serializable storeChangeStatusImportacaoGrm(PpdStatusRecibo status, PpdRecibo recibo) {
		recibo.setTpStatus(status.getTpStatusRecibo());
		if(status.getTpStatusRecibo().getValue().equals("G") && 
				status.getDhStatusRecibo() != null) {
			recibo.setDtPagto(status.getDhStatusRecibo().toYearMonthDay());
		}
		reciboService.storeImportacaoGrm(recibo);		
			
		status.setRecibo(recibo);		
		return (Long)this.store(status);
	}
	
	public Serializable storeChangeStatus(PpdStatusRecibo status, PpdRecibo recibo) {
		String statusRecibo = status.getTpStatusRecibo().getValue();
		if (!statusRecibo.equals("V")) {
		recibo.setTpStatus(status.getTpStatusRecibo());
			if(statusRecibo.equals("G") && status.getDhStatusRecibo() != null) {
			recibo.setDtPagto(status.getDhStatusRecibo().toYearMonthDay());
		}
		reciboService.store(recibo);		
		}
			
		status.setRecibo(recibo);
		Long idStatus = (Long)this.store(status);
		ppdJdeService.processoLMS(recibo,statusRecibo);
		return idStatus;
	}
	
	public PpdStatusRecibo generateStatus(String tpStatus, String obStatus) {
		PpdStatusRecibo status = new PpdStatusRecibo();					
		if(SessionUtils.getFilialSessao() == null) {
			//Chamado inserido pela web ou importado do arquivo, não tem usuário LMS
			status.setDhStatusRecibo(new DateTime(DateTimeZone.forID("America/Sao_Paulo")));
		} else {
			//Chamado inserido por usuário LMS
			status.setDhStatusRecibo(JTDateTimeUtils.getDataHoraAtual());
			UsuarioLMS usuario =  usuarioService.findById(SessionUtils.getUsuarioLogado().getIdUsuario());					
			status.setUsuario(usuario);		
		}
						
		status.setObStatusRecibo(obStatus);
		status.setTpStatusRecibo(domainValueService.findDomainValueByValue("DM_PPD_STATUS_INDENIZACAO", tpStatus));
		
		//fazer aqui 
		
		return status;
	}		

	//Sets DAO e Services
	public void setDAO(PpdStatusReciboDAO dao) {
		setDao(dao);
	}
	
	private PpdStatusReciboDAO getDAO() {
		return (PpdStatusReciboDAO) getDao();		
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}				

	public void setUsuarioService(UsuarioLMSService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setReciboService(PpdReciboService reciboService) {
		this.reciboService = reciboService;
	}			
	public void setPpdJdeService(PpdJdeService ppdJdeService) {
		this.ppdJdeService = ppdJdeService;
	}
}