package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.ppd.model.AvisoPagoRim;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.dao.AvisoPagoRimDao;
import com.mercurio.lms.util.JTDateTimeUtils;

@Assynchronous
public class AvisoPagoRimService extends CrudService<AvisoPagoRim, Long>{
	private FilialService filialService;
	private PpdReciboService ppdReciboService;
	private ParametroGeralService parametroGeralService;
	private PpdStatusReciboService ppdStatusReciboService;
	private ConfiguracoesFacade configuracoesFacade;
	
	
	public Serializable store(AvisoPagoRim avisoPagoRim) {		
		return super.store(avisoPagoRim);		
	}
	
	@AssynchronousMethod( name="ppd.AvisoPagoRimService",
			type = BatchType.BATCH_SERVICE,
			feedback = BatchFeedbackType.ON_ERROR)
	public void sendEmailAvisoPagamentoParaClientes() {
		Filial matriz = filialService.findFilialPessoaBySgFilial("MTZ", true);
		
		ParametroGeral diasAtraso = parametroGeralService.findByNomeParametro("INDENIZACOES_QTD_DIAS_ENVIO_EMAIL",false);
		YearMonthDay dataParaConsulta = JTDateTimeUtils.getDataAtual().minusDays( Integer.parseInt(diasAtraso.getDsConteudo()) );		
		
		List<PpdRecibo> reciboIndenizacaoList = ppdReciboService.findReciboIndenizacaoByStatusIndenizacaoDtPagtoEfetuadoBlEmailPagto(
				"G", dataParaConsulta, false);
		
		int maxRecibos = 0;
		for(PpdRecibo reciboIndenizacao : reciboIndenizacaoList){
			
			//Para não sobrecarregar no envio de e-mails, madar no Máximo 50 e-mails de uma vez só.		
			maxRecibos++;
			if (maxRecibos > 50) break;
			
			if ((reciboIndenizacao.getPessoa() != null)&& (reciboIndenizacao.getPessoa().getDsEmail() != null)) {
			storeReciboIndenizacao( reciboIndenizacao, true );
			ppdStatusReciboService.storeChangeStatus(
						ppdStatusReciboService.generateStatus("V",	configuracoesFacade.getMensagem("PPD-02005",
	    	    				new Object[]{reciboIndenizacao.getNrRecibo()}
	    				)),
	    				reciboIndenizacao
					); 
			storeAvisoPagoRim(reciboIndenizacao, false );
				
			} else {
		}
				
	}
	}
	
	private void storeReciboIndenizacao(PpdRecibo reciboIndenizacao, Boolean blEmailPgto){
		reciboIndenizacao.setBlEmailPgto(blEmailPgto);
		ppdReciboService.store(reciboIndenizacao);
	}

	
	public void storeAvisoPagoRim(PpdRecibo reciboIndenizacao, Boolean blRetornou ){
		AvisoPagoRim avisoPagoRim = new AvisoPagoRim();
		avisoPagoRim.setDhAvisoPagoRim( JTDateTimeUtils.getDataHoraAtual() );
		avisoPagoRim.setDsEmail( reciboIndenizacao.getPessoa().getDsEmail() );
		avisoPagoRim.setBlRetornou( blRetornou );
		avisoPagoRim.setReciboIndenizacao( reciboIndenizacao );
		this.store(avisoPagoRim);
	}

	public void setDAO(AvisoPagoRimDao dao) {
		setDao(dao);
	}

	private AvisoPagoRimDao getDAO() {
		return (AvisoPagoRimDao) getDao();		
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}


	public void setPpdReciboService(PpdReciboService ppdReciboService) {
		this.ppdReciboService = ppdReciboService;
	}
	
	public void setPpdStatusReciboService(
			PpdStatusReciboService ppdStatusReciboService) {
		this.ppdStatusReciboService = ppdStatusReciboService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public List<AvisoPagoRim> findAvisoPagoRim(PpdRecibo reciboIndenizacao, String blRetornou) {
		return getDAO().findAvisoPagoRim(reciboIndenizacao, blRetornou);
	}

}
