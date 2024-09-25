package com.mercurio.lms.ppd.model.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.edi.util.FtpConnection;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.PpdReciboGrm;
import com.mercurio.lms.ppd.model.PpdStatusRecibo;

@Assynchronous(name = "PpdImportacaoGrmService")
public class PpdImportacaoGrmService {
	private Logger log = LogManager.getLogger(this.getClass());
	private PpdReciboService reciboService;
	private PpdStatusReciboService statusReciboService;
	private FilialService filialService;
	private PessoaService pessoaService;
	private DomainValueService domainValueService;
	private PpdParametroService parametroService;

	@AssynchronousMethod(
			name="ppdImportacaoGrmService.importRecibosGrm",
			type=BatchType.BATCH_SERVICE,
			feedback=BatchFeedbackType.ALWAYS
			)				
	public void importRecibosGrm() {
		long inicioExecucao = System.currentTimeMillis();
		log.info("Iniciou importacao GRM");
		String ftpOS = (String)parametroService.getValorParametro("PPD_FTP_OS");	
		String ftpHost = (String)parametroService.getValorParametro("PPD_FTP_HOST");		
		String ftpUser = (String)parametroService.getValorParametro("PPD_FTP_USER");
		String ftpPass = (String)parametroService.getValorParametro("PPD_FTP_PASSWORD");
		String ftpFolder = (String)parametroService.getValorParametro("PPD_FTP_FOLDER");

		List<String> errors = new ArrayList<String>();		
		FtpConnection conn = new FtpConnection();		
		conn.connect(ftpHost, ftpFolder, ftpUser, ftpPass, ftpOS);	
		File arquivo = null;
		try {
			arquivo = conn.getFile("GRM_DATABASE.TXT");
		} catch (Exception e) {
			log.error(e);
			errors.add(e.getMessage()); 
		}	
		conn.disconnect();
					
		if(arquivo != null) {								
			try {
				BufferedReader in = new BufferedReader(new FileReader(arquivo));
		        String str;	
		        Long contador = Long.valueOf(0);
		        while ((str = in.readLine()) != null) {        		        	
		        	PpdReciboGrm reciboGrm = new PpdReciboGrm(str);		        	        	
	        		String retorno = importRecibo(reciboGrm);
	        		if(!retorno.equals("OK")) {
	        			errors.add(retorno);
	        		}		        					        	
		        	contador++;
		        }			        			       
		        in.close();		        		       
			} catch (IOException e) {				
				log.error(e);
			}																	
		} 
		
		log.info("Finalizou importacao GRM");
		log.info("Executado em " + ((System.currentTimeMillis() - inicioExecucao)/1000)/60 + " minutos.");
		log.info("Erros:");
		for(String erro : errors) {
			log.info(erro);
		}
		log.info("---------------------------------------------------------------------------------------------");					
	}	
	
	//Método que recebe o registro do arquivo e o grava na base
	private String importRecibo(PpdReciboGrm recibo) throws BusinessException {
		Filial filial = filialService.findFilialBySgFilialLegado(recibo.getFilialRecibo()); 
		
		if(filial != null) {
			PpdRecibo reciboPpd = reciboService.findByRecibo(
					filial.getIdFilial(), 
					Long.valueOf(recibo.getNumeroRecibo()), 
					recibo.stringToDate(recibo.getDataEmissaoRecibo())); 				
			
			//Se o recibo não existe, cria.
			if(reciboPpd == null) {																										
				reciboPpd = this.convertReciboGrmToRecibo(recibo);																											
				
				PpdStatusRecibo status = statusReciboService.generateStatus("P", "Importado do GRM.");
				status.setUsuario(null);
				status.setDhStatusRecibo(new DateTime(
						reciboPpd.getDtRecibo().getYear(),
						reciboPpd.getDtRecibo().getMonthOfYear(),
						reciboPpd.getDtRecibo().getDayOfMonth(),
						0,0,0,1,DateTimeZone.forID("America/Sao_Paulo")));				
				statusReciboService.storeChangeStatusImportacaoGrm(
						status,
						reciboPpd);
				//Cancelado
				if(recibo.getFlagCancelamento().trim().equalsIgnoreCase("T")) {
					status = statusReciboService.generateStatus("C", "Importado do GRM.");
					status.setUsuario(null);
					status.setDhStatusRecibo(new DateTime(
							reciboPpd.getDtRecibo().getYear(),
							reciboPpd.getDtRecibo().getMonthOfYear(),
							reciboPpd.getDtRecibo().getDayOfMonth(),
							0,0,0,5,DateTimeZone.forID("America/Sao_Paulo")));
					statusReciboService.storeChangeStatusImportacaoGrm(
							status,
							reciboPpd);
				} else {
					boolean enviadoJde = false;					
					if(recibo.getDataLiberacao() != null && (
							recibo.getFlagEnvioJde().toUpperCase().equals("T") ||
							recibo.getDataTransmissao() != null)) {
						enviadoJde = true;
						status = statusReciboService.generateStatus("L", "Importado do GRM.");
						status.setUsuario(null);
						YearMonthDay dataLiberacao = recibo.stringToDate(recibo.getDataLiberacao());
						status.setDhStatusRecibo(new DateTime(
								dataLiberacao.getYear(),
								dataLiberacao.getMonthOfYear(),
								dataLiberacao.getDayOfMonth(),
								0,0,0,2,DateTimeZone.forID("America/Sao_Paulo")));
						statusReciboService.storeChangeStatusImportacaoGrm(
								status,
								reciboPpd);
						status = statusReciboService.generateStatus("E", "Importado do GRM.");
						status.setUsuario(null);						
						status.setDhStatusRecibo(new DateTime(
								dataLiberacao.getYear(),
								dataLiberacao.getMonthOfYear(),
								dataLiberacao.getDayOfMonth(),
								0,0,0,3,DateTimeZone.forID("America/Sao_Paulo")));
						statusReciboService.storeChangeStatusImportacaoGrm(
								status,
								reciboPpd);
					}
					if(recibo.getDataPagamento() != null) {
						status = statusReciboService.generateStatus("G", "Importado do GRM.");
						status.setUsuario(null);
						YearMonthDay dataPagamento = recibo.stringToDate(recibo.getDataPagamento());
						status.setDhStatusRecibo(new DateTime(
								dataPagamento.getYear(),
								dataPagamento.getMonthOfYear(),
								dataPagamento.getDayOfMonth(),
								0,0,0,4,DateTimeZone.forID("America/Sao_Paulo")));						
						statusReciboService.storeChangeStatusImportacaoGrm(
								status,
								reciboPpd);
					} 
				}
				
			}
			if(reciboPpd.getPessoa() == null) {
				return "RECIBO: " + recibo.getFilialRecibo() + " " + recibo.getNumeroRecibo() + ". FAVORECIDO " + recibo.getCnpjFavorecido() + " NAO ENCONTRADO.";
			}
			
			return "OK";
		} else {
			return "RECIBO: " + recibo.getFilialRecibo() + " " + recibo.getNumeroRecibo() + ". FILIAL " + recibo.getFilialRecibo() + " NAO ENCONTRADA.";
		}			
	}
	
	private PpdRecibo convertReciboGrmToRecibo(PpdReciboGrm reciboGrm) {
		PpdRecibo recibo = new PpdRecibo();		
		
		recibo.setNrRecibo(Long.valueOf(reciboGrm.getNumeroRecibo()));
		recibo.setFilial(filialService.findFilialBySgFilialLegado(reciboGrm.getFilialRecibo()));
		recibo.setDtRecibo(reciboGrm.stringToDate(reciboGrm.getDataEmissaoRecibo()));
		recibo.setNrCtrc(Long.valueOf(reciboGrm.getNumeroCtrc()));
		recibo.setSgFilialOrigem(reciboGrm.getFilialOrigem());
		recibo.setDtEmissaoCtrc(reciboGrm.stringToDate(reciboGrm.getDataEmissaoCtrc()));
		
		recibo.setVlIndenizacao(new BigDecimal(reciboGrm.getValorIndenizacao()));
		recibo.setTpIndenizacao(domainValueService.findDomainValueByValue("DM_PPD_TIPO_INDENIZACAO", reciboGrm.getTipoIndenizacao()));
		recibo.setSgFilialComp1(reciboGrm.getFilialComp1());
		recibo.setSgFilialComp2(reciboGrm.getFilialComp2());
		recibo.setSgFilialComp3(reciboGrm.getFilialComp3());
		if(reciboGrm.getPercentComp1() != null)
			recibo.setPcFilialComp1(Integer.valueOf(reciboGrm.getPercentComp1()));
		if(reciboGrm.getPercentComp2() != null)
			recibo.setPcFilialComp2(Integer.valueOf(reciboGrm.getPercentComp2()));
		if(reciboGrm.getPercentComp3() != null)
			recibo.setPcFilialComp3(Integer.valueOf(reciboGrm.getPercentComp3()));
		recibo.setPessoa(pessoaService.findByNrIdentificacao(reciboGrm.getCnpjFavorecido()));
		if(reciboGrm.getBanco() != null)
			recibo.setNrBanco(Long.valueOf(reciboGrm.getBanco()));
		if(reciboGrm.getAgencia() != null)
			recibo.setNrAgencia(Long.valueOf(reciboGrm.getAgencia()));
		recibo.setNrDigitoAgencia(reciboGrm.getDigitoAgencia());
		if(reciboGrm.getContaCorrente() != null)
			recibo.setNrContaCorrente(Long.valueOf(reciboGrm.getContaCorrente()));
		recibo.setNrDigitoContaCorrente(reciboGrm.getDigitoContaCorrente());
		if(reciboGrm.getDataProgramada() != null) 
			recibo.setDtProgramadaPagto(reciboGrm.stringToDate(reciboGrm.getDataProgramada()));
		if(recibo.getTpIndenizacao().getValue().equals("3") || 
				recibo.getTpIndenizacao().getValue().equals("4") ||
				recibo.getTpIndenizacao().getValue().equals("6")) {			
			recibo.setNrSeguro(reciboGrm.getNumeroSeguro());
		}		
		
		return recibo;
	}
	
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setStatusReciboService(PpdStatusReciboService statusReciboService) {
		this.statusReciboService = statusReciboService;
	}

	public void setReciboService(PpdReciboService reciboService) {
		this.reciboService = reciboService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setParametroService(PpdParametroService parametroService) {
		this.parametroService = parametroService;
	}	
}
