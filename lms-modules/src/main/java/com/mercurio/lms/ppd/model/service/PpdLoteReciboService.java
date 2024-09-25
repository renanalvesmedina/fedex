package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.ppd.model.PpdLoteJde;
import com.mercurio.lms.ppd.model.PpdLoteRecibo;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.dao.PpdLoteReciboDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class PpdLoteReciboService extends CrudService<PpdLoteRecibo, Long> {			
	private PpdLoteJdeService loteJdeService;
	private PpdStatusReciboService statusReciboService;
	private ConfiguracoesFacade configs;
	
	public PpdLoteRecibo findById(Long id) {
		return getDAO().findById(id); 
	}	
	
	public Serializable store(PpdLoteRecibo bean) {		
		PpdRecibo recibo = bean.getRecibo();	
		
		//Só pode ser incluído no lote quando o status for Recebido (R),Retirado do Lote (T), Devolvido JDE (D) ou Estornado (X)				
		if(	!recibo.getTpStatus().getValue().equals("R") && 
			!recibo.getTpStatus().getValue().equals("T") &&
			!recibo.getTpStatus().getValue().equals("X") &&
			!recibo.getTpStatus().getValue().equals("D")) {
			throw new BusinessException("PPD-02010",new Object[]{recibo.getNrReciboCompleto()});
		} 				
		
		//Dados para pagamento no JDE estão faltando
		if(	recibo.getDtProgramadaPagto() == null ||
			recibo.getPessoa() == null ||
			recibo.getFormaPgto() == null || 
			//Se Forma de pagamento for depósito em conta corrente, informações bancárias devem ser iformadas
			(	recibo.getFormaPgto().getCdFormaPgto().equals("3") && 
					(	recibo.getNrBanco() == null || 
						recibo.getNrAgencia() == null ||
						recibo.getNrContaCorrente() == null )
			) ||					
			(
					recibo.getSgFilialComp1() == null && 
					recibo.getSgFilialComp2() == null && 
					recibo.getSgFilialComp3() == null
			)) {	
			throw new BusinessException("PPD-02012",new Object[]{recibo.getNrReciboCompleto()});		
		}
		
		//Não permite adicionar no lote recibo com data programada menor do que hoje
		if(recibo.getDtProgramadaPagto() != null && recibo.getDtProgramadaPagto().isBefore(new YearMonthDay(DateTimeZone.forID("America/Sao_Paulo")))) {
			throw new BusinessException("PPD-02023",new Object[]{recibo.getNrReciboCompleto()});
		}		
		
		// Busca o lote aberto, ou cria um novo lote
		PpdLoteJde loteAberto = loteJdeService.findLoteAberto();		
		if(loteAberto == null) {
			loteAberto = new PpdLoteJde();
			loteAberto.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
			loteAberto.setBlBloqueado(false);
			loteJdeService.store(loteAberto);			
		} 		
		bean.setLote(loteAberto);
		
		//Recibo não pode ser incluído em lote bloqueado por outro usuário
		if(bean.getLote().getBlBloqueado() && 
			bean.getLote().getUsuario().getIdUsuario().longValue() != SessionUtils.getUsuarioLogado().getIdUsuario().longValue()) {
			throw new BusinessException("PPD-02018",new Object[]{recibo.getNrReciboCompleto()});			
		}
		
		statusReciboService.storeChangeStatus(
				statusReciboService.generateStatus("L", 
				configs.getMensagem("PPD-02017",
						new Object[]{SessionUtils.getUsuarioLogado().getNmUsuario()}
				)), 
				recibo 
			);
		
		//Grava o recibo no lote
		return super.store(bean);		
	}	
	
	
	public List<PpdLoteRecibo> findByIdLoteJde(Long idLoteJde) {
		return getDAO().findByIdLoteJde(idLoteJde);
	}
	public PpdLoteRecibo findReciboInLoteNaoEnviado(Long idRecibo) {
		return getDAO().findReciboInLoteNaoEnviado(idRecibo);
	}
	
	public void removeById(Long id) {				
		super.removeById(id);
	}

	public void removeByIds(List<Long> ids) {
		for(int i=0;i<ids.size();i++) {
			PpdLoteRecibo loteRecibo = this.findById(ids.get(i));
			
			if(!loteRecibo.getLote().getBlBloqueado() ||
				loteRecibo.getLote().getUsuario().getIdUsuario().longValue() == SessionUtils.getUsuarioLogado().getIdUsuario().longValue()) {
				statusReciboService.storeChangeStatus(
						statusReciboService.generateStatus("T", 
								configs.getMensagem("PPD-02015",
										new Object[]{SessionUtils.getUsuarioLogado().getNmUsuario()}
								)), 
						loteRecibo.getRecibo() 
					);
				this.removeById(ids.get(i));
			} else {			 			
				throw new BusinessException("PPD-02019",new Object[]{loteRecibo.getRecibo().getNrReciboCompleto()});			
			}
		}
	}		
	
	public ResultSetPage<PpdLoteRecibo> findPaginated(PaginatedQuery paginatedQuery) {
		return getDAO().findPaginated(paginatedQuery);
	}
		
	//Get e Set do DAO correspondente a esta service
	public void setDAO(PpdLoteReciboDAO dao) {
		setDao(dao);
	}
	
	private PpdLoteReciboDAO getDAO() {
		return (PpdLoteReciboDAO) getDao();		
	}

	public void setLoteJdeService(PpdLoteJdeService loteJdeService) {
		this.loteJdeService = loteJdeService;
	}

	public void setStatusReciboService(PpdStatusReciboService statusReciboService) {
		this.statusReciboService = statusReciboService;
	}

	public void setConfigs(ConfiguracoesFacade configs) {
		this.configs = configs;
	}		
}