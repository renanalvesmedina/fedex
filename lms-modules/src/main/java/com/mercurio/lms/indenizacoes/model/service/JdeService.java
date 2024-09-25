package com.mercurio.lms.indenizacoes.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.indenizacoes.model.AvisoPagoRimInd;
import com.mercurio.lms.indenizacoes.model.Jde;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.dao.JdeDAO;
import com.mercurio.lms.integracao.model.InconsistenciaJde;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.ppd.model.PpdLoteJde;
import com.mercurio.lms.ppd.model.PpdRecibo;


@Assynchronous
public class JdeService extends CrudService<Jde, Long> {			
	
	private FilialService filialService;
	private ReciboIndenizacaoService reciboIndenizacaoService;
	private EventoRimService eventoRimService;
	private AvisoPagoRimService avisoPagoRimService;
	private static final Integer RIGHT_PAD = 1;
	private static final Integer LEFT_PAD = 2;
	
	public Serializable store(Jde bean) {		
		return super.store(bean);		
	}	
 
	public Jde generateRegistroRecibo(Long seq, PpdLoteJde lote, PpdRecibo recibo) {
		Jde jde = new Jde();
		jde.setBimid(lote.getNrLoteJde());
		jde.setBilnid(seq);
		jde.setBian8(Long.valueOf(16023));
		jde.setBitytn("JDEMVI");
		jde.setBidrin("1");
		jde.setBiedsp("0"); 
		jde.setBipid("INTERP");
		jde.setBiuser("LMS");
		jde.setBijobn("ux-erp");		 
		jde.setBiupmj(Long.valueOf(lote.getDhEnvio().toString(DateTimeFormat.forPattern("yyyyddd"))) - 1900000);			
		jde.setBiupmt(Long.valueOf(lote.getDhEnvio().toString(DateTimeFormat.forPattern("HHmmss"))));
				
		String tpIndenizacao = recibo.getTpIndenizacao().getValue();
		//Se tipo for furto, manda para o JDE como Falta
		if(tpIndenizacao.equals("5"))
			tpIndenizacao = "2";
		//Se tipo for SEG RCTA-C, manda para o JDE como SEG RCTR-C
		if(tpIndenizacao.equals("6"))
			tpIndenizacao = "3";		
		
		BigDecimal vlComp1 = recibo.getVlFilialComp1();
		BigDecimal vlComp2 = recibo.getVlFilialComp2();
		BigDecimal vlComp3 = recibo.getVlFilialComp3();
		if(vlComp1 == null)
			vlComp1 = BigDecimal.valueOf(0);
		if(vlComp2 == null)
			vlComp2 = BigDecimal.valueOf(0);
		if(vlComp3 == null)
			vlComp3 = BigDecimal.valueOf(0);
		
		jde.setBiapta(
				completeString(recibo.getPessoa().getNrIdentificacao().trim(),14,' ',RIGHT_PAD) +
				completeString("",6,' ',RIGHT_PAD) +
				recibo.getDtProgramadaPagto().toString(DateTimeFormat.forPattern("dd/MM/yy")) +				
				completeString(filialService.findSgFilialLegadoByIdFilial(recibo.getFilial().getIdFilial()), 3, ' ',RIGHT_PAD) +
				completeString(recibo.getNrRecibo(), 4, '0',LEFT_PAD) + 
				completeString("",21,' ',RIGHT_PAD) + 
				completeString(recibo.getVlIndenizacao().setScale(2).toString().replace(".", ""), 15, '0',LEFT_PAD) +
				completeString(recibo.getSgFilialComp1(), 3, ' ',RIGHT_PAD) + 
				completeString(recibo.getSgFilialComp2(), 3, ' ',RIGHT_PAD) +
				completeString(recibo.getSgFilialComp3(), 3, ' ',RIGHT_PAD) +
				completeString(vlComp1.toString().replace(".", ""),15,'0',LEFT_PAD) +
				completeString(vlComp2.toString().replace(".", ""),15,'0',LEFT_PAD) + 
				completeString(vlComp3.toString().replace(".", ""),15,'0',LEFT_PAD) +
				tpIndenizacao + 
				completeString(recibo.getSgFilialOrigem(),3,' ',RIGHT_PAD) + 
				completeString(recibo.getNrCtrc().toString(),6,' ',RIGHT_PAD) +
				completeString(recibo.getNrSeguro(), 16, ' ',RIGHT_PAD) +
				recibo.getDtEmissaoCtrc().toString(DateTimeFormat.forPattern("dd/MM/yy")) +
				completeString(recibo.getFormaPgto().getCdFormaPgto(), 1, ' ',RIGHT_PAD) 
		);
		 
		return jde;
	}
	
	public Jde generateRegistroCadastro(Long seq, PpdLoteJde lote, PpdRecibo recibo) {
		Jde jde = new Jde();
		jde.setBimid("IC" + lote.getDhEnvio().toString(DateTimeFormat.forPattern("yyMMddHH")));
		jde.setBilnid(seq);
		jde.setBian8(Long.valueOf(16023));
		jde.setBitytn("JDEAB");
		jde.setBidrin("1");
		jde.setBiedsp("0");
		jde.setBipid("INTERC");
		jde.setBiuser("LMS");
		jde.setBijobn("ux-erp");		
		jde.setBiupmj(Long.valueOf(lote.getDhEnvio().toString(DateTimeFormat.forPattern("yyyyddd"))) - 1900000);			
		jde.setBiupmt(Long.valueOf(lote.getDhEnvio().toString(DateTimeFormat.forPattern("HHmmss"))));
		jde.setBiapta(
				completeString(recibo.getPessoa().getNrIdentificacao().trim(),14,' ',LEFT_PAD) +
				completeString("",6,' ',RIGHT_PAD) +
				completeString(recibo.getPessoa().getNmPessoa().trim(),40,' ',RIGHT_PAD) +
				completeString(recibo.getNrBanco(),3,'0',LEFT_PAD).replace("000", "   ") + "/" + 
				completeString(recibo.getNrAgencia(),4,'0',LEFT_PAD).replace("0000", "    ") + "-" + 
				completeString(recibo.getNrDigitoAgencia(),1,' ',RIGHT_PAD) + 
				completeString("", 10, ' ',RIGHT_PAD) + 
				completeString(recibo.getNrContaCorrente(),20,' ',LEFT_PAD) + 
				completeString(recibo.getNrDigitoContaCorrente(),2,' ',RIGHT_PAD) + "*"
		);
		
 		return jde; 
	}
	
	// Mode
	//	1 - Right
	//	2 - Left
	
	/**
	 * Utilizar StringUtils
	 */
	@Deprecated
	private String completeString(Object vlr, int length, char caracter, int pad) {			
		String str;
		if(vlr == null) {
			str = "";
		} else {
			str = String.valueOf(vlr);
		}
		
		if(str.length() > length) {
			str = str.substring(0, length);
		} else {
			while(str.length() < length) {
				if(pad == 1) {
					str += caracter;
				} else {
					str = caracter + str;
				}
			}
		}
		
		return str;
	}
	

	@AssynchronousMethod( name="indenizacoes.JdeService",
			type = BatchType.BATCH_SERVICE,
			feedback = BatchFeedbackType.ON_ERROR)
	public void getInformacoesPagamentosJde() {
		
		Filial matriz = filialService.findFilialPessoaBySgFilial("MTZ", true);
				
		List<Jde> listPagamentoJde = getDAO().findPagamentosFromJde( StringUtils.rightPad("LMSIND",8), "0", "L%" );
		
		List<InconsistenciaJde> listInconsistenciaJde = new ArrayList<InconsistenciaJde>();
		
		for(Jde jde : listPagamentoJde){
			String sgFilialLegado = getSiglaFilialLegado( jde );
			Integer nrReciboIndenizacao = getNrReciboIndenizacao( jde );
			Date dataPagamento = getDateFromJulianDate( jde );	
			Filial filial = filialService.findFilialBySgFilialLegado( sgFilialLegado );
			
			ReciboIndenizacao reciboIndenizacao = reciboIndenizacaoService.findByIdFilialNrReciboIndenizacao(filial.getIdFilial(), nrReciboIndenizacao);
			
			if( reciboIndenizacao != null ){
				if( !"P".equalsIgnoreCase( reciboIndenizacao.getTpStatusIndenizacao().getValue()) && StringUtils.isBlank(jde.getBijobn()) ){
					storeReciboIndenizacao( reciboIndenizacao, new DomainValue("P"), new YearMonthDay(dataPagamento), null );
					eventoRimService.storeEventoRim( matriz, reciboIndenizacao,new DomainValue("PA") );
					storeJde(jde);
				}else if( !"T".equalsIgnoreCase(reciboIndenizacao.getTpStatusIndenizacao().getValue()) && "DEVOLUÇÃO".equalsIgnoreCase(jde.getBijobn().trim()) ){	
					storeReciboIndenizacao( reciboIndenizacao, new DomainValue("T"), null, false );
					eventoRimService.storeEventoRim( matriz, reciboIndenizacao, new DomainValue("RP") );
					storeAvisoPagoRim(reciboIndenizacao, true);
					storeJde(jde);	
				}
			}else{
				InconsistenciaJde inconsistenciaJde = new InconsistenciaJde();
				inconsistenciaJde.setSgFilial(filial.getSgFilial());
				inconsistenciaJde.setNrReciboIndenizacao(Long.valueOf(nrReciboIndenizacao));
				listInconsistenciaJde.add(inconsistenciaJde);
			}
		}
		
		if( !listInconsistenciaJde.isEmpty() ){
			String conteudoCorpoEmail = "";
			for( InconsistenciaJde inconsistenciaJde : listInconsistenciaJde ){
				conteudoCorpoEmail = conteudoCorpoEmail.concat(inconsistenciaJde.getSgFilial() + " " + inconsistenciaJde.getNrReciboIndenizacao() + ", ");				 
			}
			
		}
	}
	
	
	private void storeReciboIndenizacao( ReciboIndenizacao reciboIndenizacao, DomainValue domainValue, YearMonthDay dtPagamentoEfetuado,
			Boolean blEmailPgto ){
		reciboIndenizacao.setTpStatusIndenizacao(domainValue);
		reciboIndenizacao.setDtPagamentoEfetuado( dtPagamentoEfetuado );
		
		if( blEmailPgto != null ){
			reciboIndenizacao.setBlEmailPgto(blEmailPgto);
		}

		reciboIndenizacaoService.store(reciboIndenizacao);
	}
	
	
	private void storeAvisoPagoRim(ReciboIndenizacao reciboIndenizacao, Boolean blRetornou){
		List<AvisoPagoRimInd> avisoPagoRimlist = reciboIndenizacao.getAvisoPagoRim();
		
				
			for(AvisoPagoRimInd avisoPagoRim : avisoPagoRimlist){
				if( !avisoPagoRim.getBlRetornou() ){
					avisoPagoRim.setBlRetornou(blRetornou);
					avisoPagoRimService.store(avisoPagoRim);
				}
			}
			
	}
	
	private void storeJde( Jde jde ){
		jde.setBiedsp("1");
		this.store(jde);
	}
	
	private Date getDateFromJulianDate( Jde jde ){
		Date dataPagamento = null;
		Integer dataTemp = 1900000 + Integer.parseInt( jde.getBiupmj().toString() );
				
		try{
			dataPagamento = new SimpleDateFormat("yyyyDDD").parse( dataTemp.toString() );
		}catch (Exception e) {
			throw new BusinessException("LMS-21069");
		}
		return dataPagamento;
	}
	
	private String getSiglaFilialLegado( Jde jde ){
		String sgFilialLegado = jde.getBimid().substring(1, 3);
		
		return sgFilialLegado;
	}
	
	private Integer getNrReciboIndenizacao( Jde jde ){
		Integer nrReciboIndenizacao = Integer.valueOf( jde.getBimid().substring(4).trim() );
		return nrReciboIndenizacao;		
	}

	
	//Get e Set do DAO correspondente a esta service	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setDAO(JdeDAO dao) {
		setDao(dao);
	}
	
	private JdeDAO getDAO() {
		return (JdeDAO) getDao();		
	}

	public void setReciboIndenizacaoService(
			ReciboIndenizacaoService reciboIndenizacaoService) {
		this.reciboIndenizacaoService = reciboIndenizacaoService;
	}

	public void setEventoRimService(EventoRimService eventoRimService) {
		this.eventoRimService = eventoRimService;
	}

	public void setAvisoPagoRimService(AvisoPagoRimService avisoPagoRimService) {
		this.avisoPagoRimService = avisoPagoRimService;
	}
}

