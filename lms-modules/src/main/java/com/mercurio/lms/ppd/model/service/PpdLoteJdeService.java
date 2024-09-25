package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.ppd.model.PpdLoteJde;
import com.mercurio.lms.ppd.model.PpdLoteRecibo;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.dao.PpdLoteJdeDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class PpdLoteJdeService extends CrudService<PpdLoteJde, Long> {			
	private PpdLoteReciboService loteReciboService;
	private PpdJdeService jdeService;
	private PpdStatusReciboService statusReciboService;
	private ConfiguracoesFacade configs;
	
	public PpdLoteJde findById(Long id) {
		return getDAO().findById(id); 
	}
	
	public Serializable store(PpdLoteJde bean) {
		return super.store(bean);		
	}	
	
	public PpdLoteJde storeBloqueio(Boolean blBloqueado) {
		PpdLoteJde lote = this.findLoteAberto();				
		//Seta o usuário de bloqueio caso esteja bloqueando
		if(blBloqueado) {
			//Tentando bloquear lote já bloqueado anteriormente
			if(lote.getBlBloqueado()) {
				throw new BusinessException("PPD-02020");
			}
			UsuarioLMS usuario = new UsuarioLMS();
			usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
			lote.setUsuario(usuario);
		} else {
			lote.setUsuario(null);
		}
		lote.setBlBloqueado(blBloqueado);
		this.store(lote);
		return lote;
	}
	
	public void removeById(Long id) {				
		super.removeById(id);
	}

	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);		
	}		
	
	public ResultSetPage<PpdLoteJde> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}	
	
	public Serializable storeSendLoteAberto() {
		PpdLoteJde lote = this.findLoteAberto();
		if(lote != null) {					
			//Não permite envio de lote se ele não está bloqueado
			if(!lote.getBlBloqueado()) {
				throw new BusinessException("PPD-02022");
			}
			DateTime dataEnvio = JTDateTimeUtils.getDataHoraAtual();
			//Se já foi enviado um lote na última hora, não deixa enviar outro
			PpdLoteJde enviado = getDAO().findUltimoLoteEnviado();
			if(enviado != null) {
				if(		enviado.getDhEnvio().hourOfDay().get() == dataEnvio.hourOfDay().get() &&
						enviado.getDhEnvio().dayOfMonth().get() == dataEnvio.dayOfMonth().get() && 
						enviado.getDhEnvio().monthOfYear().get() == dataEnvio.monthOfYear().get() &&
						enviado.getDhEnvio().year().get() == dataEnvio.year().get()
				  )
					throw new BusinessException("PPD-02021");
			}
					
			lote.setNrLoteJde("IR" + dataEnvio.toString(DateTimeFormat.forPattern("yyMMddHH")));
			lote.setDhEnvio(dataEnvio);			
			this.store(lote);
			
			//Busca os recibos do lote
			HashMap<String,Object> criteria = new HashMap<String, Object>();
			criteria.put("lote.idLoteJde", lote.getIdLoteJde());
			List<PpdLoteRecibo> recibosLote = loteReciboService.find(criteria);
			for(int z=0; z<recibosLote.size(); z++) {
				PpdRecibo recibo = recibosLote.get(z).getRecibo();
				
				//Valida data programada. Se menor que hoje, não permite o envio do lote!				
				if(recibo.getDtProgramadaPagto() != null && recibo.getDtProgramadaPagto().isBefore(new YearMonthDay(DateTimeZone.forID("America/Sao_Paulo")))) {
					throw new BusinessException("PPD-02023",new Object[]{recibo.getNrReciboCompleto()});
				}		
				
				jdeService.store(jdeService.generateRegistroRecibo(Long.valueOf((z+1)*1000), lote, recibo));
				jdeService.store(jdeService.generateRegistroCadastro(Long.valueOf((z+1)*1000), lote, recibo));
				
				statusReciboService.storeChangeStatus(
						statusReciboService.generateStatus("E", 
							configs.getMensagem("PPD-02014",
									new Object[]{lote.getNrLoteJde(),SessionUtils.getUsuarioLogado().getNmUsuario()}
							)),
						recibo
					);
				
			}
			
			return lote.getIdLoteJde();
		} else {
			return null;
		}		
	}
	
	
	public PpdLoteJde findLoteAberto() {	
		PpdLoteJde lote = getDAO().findLoteAberto();	
		return lote;		
	}
	
	//Get e Set do DAO correspondente a esta service
	public void setDAO(PpdLoteJdeDAO dao) {
		setDao(dao);
	}
	
	private PpdLoteJdeDAO getDAO() {
		return (PpdLoteJdeDAO) getDao();		
	}

	public void setLoteReciboService(PpdLoteReciboService loteReciboService) {
		this.loteReciboService = loteReciboService;
	}

	public void setJdeService(PpdJdeService jdeService) {
		this.jdeService = jdeService;
	}

	public void setStatusReciboService(PpdStatusReciboService statusReciboService) {
		this.statusReciboService = statusReciboService;
	}

	public void setConfigs(ConfiguracoesFacade configs) {
		this.configs = configs;
	}
	
}