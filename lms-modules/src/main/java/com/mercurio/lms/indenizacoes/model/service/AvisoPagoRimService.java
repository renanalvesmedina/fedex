package com.mercurio.lms.indenizacoes.model.service;

import java.io.Serializable;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.indenizacoes.model.AvisoPagoRimInd;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.dao.AvisoPagoRimDao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;

@Assynchronous
public class AvisoPagoRimService extends CrudService<AvisoPagoRimInd, Long>{
	private FilialService filialService;
	private ReciboIndenizacaoService reciboIndenizacaoService;
	private ParametroGeralService parametroGeralService;
	private EventoRimService eventoRimService;
	
	
	public Serializable store(AvisoPagoRimInd avisoPagoRim) {		
		return super.store(avisoPagoRim);		
	}

	
	
	@AssynchronousMethod( name="indenizacoes.AvisoPagoRimService",
			type = BatchType.BATCH_SERVICE,
			feedback = BatchFeedbackType.ON_ERROR)
	public void sendEmailAvisoPagamentoParaClientes() {
		Filial matriz = filialService.findFilialPessoaBySgFilial("MTZ", true);
		
		ParametroGeral diasAtraso = parametroGeralService.findByNomeParametro("INDENIZACOES_QTD_DIAS_ENVIO_EMAIL",false);
		YearMonthDay dataParaConsulta = JTDateTimeUtils.getDataAtual().minusDays( Integer.parseInt(diasAtraso.getDsConteudo()) );		
		
		List<ReciboIndenizacao> reciboIndenizacaoList = reciboIndenizacaoService.findReciboIndenizacaoByStatusIndenizacaoDtPagtoEfetuadoBlEmailPagto(
				"P", dataParaConsulta, false);
		
		for(ReciboIndenizacao reciboIndenizacao : reciboIndenizacaoList){
			storeReciboIndenizacao( reciboIndenizacao, true );
			eventoRimService.storeEventoRim(matriz, reciboIndenizacao, new DomainValue("AP") );
			storeAvisoPagoRim(reciboIndenizacao, false );
				
		}
	}
	
	private void storeReciboIndenizacao(ReciboIndenizacao reciboIndenizacao, Boolean blEmailPgto){
		reciboIndenizacao.setBlEmailPgto(blEmailPgto);
		reciboIndenizacaoService.store(reciboIndenizacao);
	}

	
	public void storeAvisoPagoRim(ReciboIndenizacao reciboIndenizacao, Boolean blRetornou ){
		AvisoPagoRimInd avisoPagoRim = new AvisoPagoRimInd();
		avisoPagoRim.setDhAvisoPagoRim( JTDateTimeUtils.getDataHoraAtual() );
		avisoPagoRim.setDsEmail( reciboIndenizacao.getPessoaByIdFavorecido().getDsEmail() );
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



	public void setReciboIndenizacaoService(
			ReciboIndenizacaoService reciboIndenizacaoService) {
		this.reciboIndenizacaoService = reciboIndenizacaoService;
	}



	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setEventoRimService(EventoRimService eventoRimService) {
		this.eventoRimService = eventoRimService;
	}

}
