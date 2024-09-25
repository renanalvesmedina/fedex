package com.mercurio.lms.ppd.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.integration.convert.Sigla2FilialConverter;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.integracao.model.InconsistenciaJde;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.ppd.model.AvisoPagoRim;
import com.mercurio.lms.ppd.model.PpdJde;
import com.mercurio.lms.ppd.model.PpdLoteJde;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.dao.PpdJdeDAO;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

@Assynchronous
public class PpdJdeService extends CrudService<PpdJde, Long> {			
	
	private FilialService filialService;
	private static final Integer RIGHT_PAD = 1;
	private static final Integer LEFT_PAD = 2;
	private PpdReciboService ppdReciboService;
	private PpdStatusReciboService ppdStatusReciboService;
	private AvisoPagoRimService avisoPagoRimService;
	private ConfiguracoesFacade configuracoesFacade;
	private DoctoServicoService doctoServicoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private EventoService eventoService;
	
	
	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}	

	public void setAvisoPagoRimService(AvisoPagoRimService avisoPagoRimService) {
		this.avisoPagoRimService = avisoPagoRimService;
	}

	public void setPpdStatusReciboService(
			PpdStatusReciboService ppdStatusReciboService) {
		this.ppdStatusReciboService = ppdStatusReciboService;
	}

	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}
	
	public void setPpdReciboService(PpdReciboService ppdReciboService) {
		this.ppdReciboService = ppdReciboService;
	}

	public PpdJde generateRegistroRecibo(Long seq, PpdLoteJde lote, PpdRecibo recibo) {
		PpdJde jde = new PpdJde();
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
		
		if (recibo.getBlSegurado().booleanValue()) {
			if(tpIndenizacao.equals("3")){
				tpIndenizacao = "8";
			}
			if(tpIndenizacao.equals("4")){
				tpIndenizacao = "9";
		}
			if(tpIndenizacao.equals("5")){
				tpIndenizacao = "9";				
			}
		} else {
			if(tpIndenizacao.equals("5")){
				tpIndenizacao = "4";				
			}
		}		
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
	
	public PpdJde generateRegistroCadastro(Long seq, PpdLoteJde lote, PpdRecibo recibo) {
		PpdJde jde = new PpdJde();
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
	
	//Get e Set do DAO correspondente a esta service	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setDAO(PpdJdeDAO dao) {
		setDao(dao);
	}
	
	private PpdJdeDAO getDAO() {
		return (PpdJdeDAO) getDao();		
	}
	
	@AssynchronousMethod( name="ppd.JdeService",
			type = BatchType.BATCH_SERVICE,
			feedback = BatchFeedbackType.ON_ERROR)
	public void getInformacoesPagamentosJde() {
		
		List<PpdJde> listPagamentoJde = getDAO().findPagamentosFromJde( StringUtils.rightPad("LMSIND",8), "0", "L%" );
		
		List<InconsistenciaJde> listInconsistenciaJde = new ArrayList<InconsistenciaJde>();
		
		for(PpdJde jde : listPagamentoJde){
			processaRecibo(jde,listInconsistenciaJde); // JIRA LMS-718
			}
			
		if( !listInconsistenciaJde.isEmpty() ){
			String conteudoCorpoEmail = "";
			for( InconsistenciaJde inconsistenciaJde : listInconsistenciaJde ){
				conteudoCorpoEmail = conteudoCorpoEmail.concat(inconsistenciaJde.getSgFilial() + " " + inconsistenciaJde.getNrReciboIndenizacao() + ", ");				 
			}
			
		}
	}
	
	private Date getDateFromJulianDate( PpdJde jde ){
		Date dataPagamento = null;
		Integer dataTemp = 1900000 + Integer.parseInt( jde.getBiupmj().toString() );
				
		try{
			dataPagamento = new SimpleDateFormat("yyyyDDD").parse( dataTemp.toString() );
		}catch (Exception e) {
			throw new BusinessException("LMS-21069");
		}
		return dataPagamento;
	}
	
	private String getSiglaFilialLegado( PpdJde jde ){
		String sgFilialLegado = jde.getBimid().substring(1, 3);
		
		return sgFilialLegado;
	}
	
	private Long getNrReciboIndenizacao( PpdJde jde ){
		String cBimid = jde.getBimid().trim();
		Long nrReciboIndenizacao = Long.valueOf( cBimid.substring(4, Math.min(cBimid.length(), 8)).trim());
		return nrReciboIndenizacao;		
	}
	
	private void storeReciboIndenizacao( PpdRecibo reciboIndenizacao, DomainValue domainValue, YearMonthDay dtPagamentoEfetuado,
			Boolean blEmailPgto ){
		reciboIndenizacao.setTpStatus(domainValue);
		reciboIndenizacao.setDtPagto( dtPagamentoEfetuado );
		
		if( blEmailPgto != null ){
			reciboIndenizacao.setBlEmailPgto(blEmailPgto);
		}

		ppdReciboService.store(reciboIndenizacao);
	}
	
	private void storeJde( PpdJde jde ){
		jde.setBiedsp("1");
		this.store(jde);
	}
	
	public Serializable store(PpdJde bean) {		
		return super.store(bean);		
	}	
	
	private void storeAvisoPagoRim(PpdRecibo reciboIndenizacao, Boolean blRetornou){
		List<AvisoPagoRim> avisoPagoRimlist = avisoPagoRimService.findAvisoPagoRim(reciboIndenizacao, "N");		
				
			for(AvisoPagoRim avisoPagoRim : avisoPagoRimlist){
				if( !avisoPagoRim.getBlRetornou() ){
					avisoPagoRim.setBlRetornou(blRetornou);
					avisoPagoRimService.store(avisoPagoRim);
				}
			}
	}
	
	public void processoLMS(PpdRecibo recibo, String status) {
		Sigla2FilialConverter filialConverter = new Sigla2FilialConverter();
    	Map idFilialLegado = filialConverter.getMapFilialSigla(); 
		String sgFilialOrigemLeg = recibo.getSgFilialOrigem();
    	Long idFilialOrigemLMS = (Long) idFilialLegado.get(sgFilialOrigemLeg); 
		
		DoctoServico doctoServico = ppdReciboService.findDoctoServicoByRecibo(recibo);
		
		if (doctoServico != null) {
			/*
			 * caso código da localização do documento (docto_servico -> localizacao_mercadoria.cd_localizacao_mercadoria) 
			 * for diferente de 39 (Cliente indenizado) e 70 (Em processo indenizatório):
			 * 	1.	Criar evento para o documento de serviço utilizando a rotina 10.05.01.07 Incluir Eventos Rastreabilidade 
			 * Internacional (IncluirEventosRastreabilidadeInternacionalService. executeInsereEventos), passando os parâmetros:
			 * 		1.	tipo de documento: ‘CRT’
			 * 		2.	número documento: sigla da filial (sigla de 3 letras) concatenado com o número de origem do documento
			 * 		3.	código do evento: 35
			 * 		4.	filial: filial do usuário da sessão
			 * 		5.	data do evento: data atual
			 * 		6.	observação: null
			 * 
			 */

			Short cdLocalizacaoMercadoria = null;
			
			if (doctoServico != null) {
				cdLocalizacaoMercadoria = doctoServico.getLocalizacaoMercadoria() == null ? null : doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
	}

			DateTime now = JTDateTimeUtils.getDataHoraAtual();
			
			if (status.equals("G")) {
				//inserir ocorrenciaDoctoServico
				ppdReciboService.insereOcorrenciaDoctoServico(Short.valueOf("95"), doctoServico, now);

				if (null == cdLocalizacaoMercadoria || (cdLocalizacaoMercadoria.intValue() != 39 )) {
					Filial filial = filialService.findById(idFilialOrigemLMS);
					String nrDocumento = filial.getSgFilial() + " " + doctoServico.getNrDoctoServico();

					Short cdEvento = 38; //pagamento de rim

					incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvento, doctoServico.getIdDoctoServico(), 
							doctoServico.getFilialByIdFilialOrigem().getIdFilial(), nrDocumento, now, null, null, doctoServico.getTpDocumentoServico().getValue());
}
			}else if(status.equals("C")){
				//inserir ocorrenciaDoctoServico
				ppdReciboService.insereOcorrenciaDoctoServico(Short.valueOf("94"), doctoServico, now);

				if (null == cdLocalizacaoMercadoria || (cdLocalizacaoMercadoria.intValue() != 39 )) {
					Filial filial = filialService.findById(idFilialOrigemLMS);
					String nrDocumento = filial.getSgFilial() + " " + doctoServico.getNrDoctoServico();

					Short cdEvento = 88; // cancelamento de rim
					
					Filial filialSessao = SessionUtils.getFilialSessao();

					Evento evento = eventoService.findByCdEvento(cdEvento);
					
					//só cancela evento de inclusão de rim se houve no ctrc
					if (evento != null && evento.getCancelaEvento() != null) {
						List eventosDoctoServico = eventoDocumentoServicoService.findByEventoByDocumentoServico(evento.getCancelaEvento().getIdEvento(), doctoServico.getIdDoctoServico());
						
						if (eventosDoctoServico != null && !eventosDoctoServico.isEmpty()) {
							incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvento, doctoServico.getIdDoctoServico(), 
									filialSessao.getIdFilial(), nrDocumento, now, null, null, doctoServico.getTpDocumentoServico().getValue());
						}
					}
					
					doctoServicoService.store(doctoServico);
			} 
		}
	}
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private void processaRecibo(PpdJde jde, List<InconsistenciaJde> listInconsistenciaJde){
		
		String sgFilialLegado = getSiglaFilialLegado( jde );
		Long nrReciboIndenizacao = getNrReciboIndenizacao( jde );
		Date dataPagamento = getDateFromJulianDate( jde );	
		Filial filial = filialService.findFilialBySgFilialLegado( sgFilialLegado );
		Long nrCTRC = 0L;
		try {
			if ((jde.getBiapta() == null) || (jde.getBiapta().trim().length() < 4)) return; 
			String cCTRC = jde.getBiapta().trim();
			nrCTRC = Long.valueOf(cCTRC.substring(3, Math.min(cCTRC.length(),9)).trim());
			if (nrCTRC <= 0) return;
		} catch (Exception e) {
			return;
		}

		PpdRecibo reciboIndenizacao = ppdReciboService.findByRecibo(filial.getIdFilial(), nrReciboIndenizacao, nrCTRC);
		
		if( reciboIndenizacao != null ){
			if( !"G".equalsIgnoreCase( reciboIndenizacao.getTpStatus().getValue()) && StringUtils.isBlank(jde.getBijobn()) ){
				storeReciboIndenizacao( reciboIndenizacao, new DomainValue("G"), new YearMonthDay(dataPagamento), false );
				
				ppdStatusReciboService.storeChangeStatus(
						ppdStatusReciboService.generateStatus("G",
		    				configuracoesFacade.getMensagem("PPD-02005",
		    	    				new Object[]{reciboIndenizacao.getNrRecibo()}
		    				)),
		    				reciboIndenizacao
	    				); 
				
			}else if( !"X".equalsIgnoreCase(reciboIndenizacao.getTpStatus().getValue()) && "DEVOLUÇÃO".equalsIgnoreCase(jde.getBijobn().trim()) ){	
				storeReciboIndenizacao( reciboIndenizacao, new DomainValue("X"), null, false );
				
				ppdStatusReciboService.storeChangeStatus(
						ppdStatusReciboService.generateStatus("X",
		    				configuracoesFacade.getMensagem("PPD-02005",
		    	    				new Object[]{reciboIndenizacao.getNrRecibo()}
		    				)),
		    				reciboIndenizacao
	    				);
				
				storeAvisoPagoRim(reciboIndenizacao, true);
			}
		}else{
			InconsistenciaJde inconsistenciaJde = new InconsistenciaJde();
			inconsistenciaJde.setSgFilial(filial.getSgFilial());
			inconsistenciaJde.setNrReciboIndenizacao(nrReciboIndenizacao);
			listInconsistenciaJde.add(inconsistenciaJde);
		}
		storeJde(jde);
	}
}

