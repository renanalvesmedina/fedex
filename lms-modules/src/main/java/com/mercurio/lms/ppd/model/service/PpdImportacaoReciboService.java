package com.mercurio.lms.ppd.model.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.edi.util.FTPFileFilter;
import com.mercurio.lms.edi.util.FtpConnection;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.PpdReciboArquivo;
import com.mercurio.lms.ppd.model.PpdStatusRecibo;

@Assynchronous(name = "PpdImportacaoReciboService")
public class PpdImportacaoReciboService {	
	private Logger log = LogManager.getLogger(this.getClass());
	private ConfiguracoesFacade configuracoesFacade;
	private PpdReciboService reciboService;
	private PpdStatusReciboService statusReciboService;
	private PpdCorporativoService corporativoService;
	private FilialService filialService;
	private PessoaService pessoaService;
	private DomainValueService domainValueService;
	private PpdParametroService parametroService;

	@AssynchronousMethod(
			name="ppdImportacaoReciboService.importArquivosRecibos",
			type=BatchType.BATCH_SERVICE,
			feedback=BatchFeedbackType.ON_ERROR
			)				
	public void importArquivosRecibos() {
		long inicioExecucao = System.currentTimeMillis();
		System.out.println("Iniciou importacao Arquivo");
		String ftpOS = (String)parametroService.getValorParametro("PPD_FTP_OS");	
		String ftpHost = (String)parametroService.getValorParametro("PPD_FTP_HOST");		
		String ftpUser = (String)parametroService.getValorParametro("PPD_FTP_USER");
		String ftpPass = (String)parametroService.getValorParametro("PPD_FTP_PASSWORD");
		String ftpFolder = (String)parametroService.getValorParametro("PPD_FTP_FOLDER");
		String moveArquivo = (String)configuracoesFacade.getValorParametro("PPD_FTP_MOVE_TO_DONE");		
		
		List<String> errors = new ArrayList<String>();
		Boolean fileError = false;
		FTPFile arquivos[] = null;
		FtpConnection conn = new FtpConnection();		
		conn.connect(ftpHost, ftpFolder, ftpUser, ftpPass, ftpOS);
		try {
			arquivos = conn.listFTPFiles(new GrFileFilter());
		} catch (Exception e) {
			log.error(e);			
		}
		
		if(arquivos != null) {					
			for(int y=0; y<arquivos.length; y++) {
				try {
					InputStream is = conn.getFTPClient().retrieveFileStream(arquivos[y].getName());
					BufferedReader in = new BufferedReader(new InputStreamReader(is));
			        String str;
			        fileError = false;
			        while ((str = in.readLine()) != null) {        	
			        	if (str.length() >= 300) {
			        		PpdReciboArquivo recibo = new PpdReciboArquivo(str);			        		
			        		
			        		if(recibo.getIde().toUpperCase().equals("RI") && !recibo.getFilialOrigem().toUpperCase().equals("XX")) {
			        			try {
			        				importRecibo(recibo);
			        			} catch(Exception e) {
			        				fileError = true;			        				
			        				errors.add(e.getMessage());
			        				log.error(e);
			        			}
			        		}
			        	} else if(str.length() > 1) {
			        		fileError = true;
			        		errors.add("ERRO: Tamanho do registro inválido. Arquivo: " + arquivos[y].getName() + ". Registro: " + str);
			        	}
			        }			        			       
			        in.close();
			        conn.getFTPClient().completePendingCommand();
			        
			        if(!fileError) {
			        	if(moveArquivo.trim().equalsIgnoreCase("S")) {			        					    			
		    				DateTime now = new DateTime(DateTimeZone.forID("America/Sao_Paulo"));
		    				String hms = now.toString(DateTimeFormat.forPattern("_yyyyMMdd_HHmm"));
			    			String newFileName = arquivos[y].getName().substring(0, 2) + hms + ".TXT";
			    			
			        		try {
								conn.moveFile(arquivos[y].getName(),"done/",newFileName);
							} catch (Exception e) {
								log.error(e);
							}			        		
			        	}
			        } 
				} catch (IOException e) {					
					log.error(e);
				}																	
			}
		}	
		
		conn.disconnect();
		System.out.println("Finalizou importacao Arquivo");
		System.out.println("Executado em " + ((System.currentTimeMillis() - inicioExecucao)/1000)/60 + " minutos.");
		System.out.println("Erros:");
		for(String erro : errors) {
			System.out.println(erro);
		}
		System.out.println("---------------------------------------------------------------------------------------------");
	}	
	
	//Método que recebe o registro do arquivo e o grava na base
	private void importRecibo(PpdReciboArquivo recibo) {
		Filial filial = filialService.findFilialBySgFilialLegado(recibo.getFilialTransm()); 
		
		if(filial != null) {
			PpdRecibo reciboPpd = reciboService.findByRecibo(
					filial.getIdFilial(), 
					Long.valueOf(recibo.getReciboNumero()), 
					recibo.getDataEmissaoReciboDate()); 
			
			//Se já existe Recibo, e o arquivo está mandando como cancelado
			if(reciboPpd != null && recibo.getCanc().equals("1") && !reciboPpd.getTpStatus().getValue().equals("C")) {				
				//Se recibo estiver no lote, enviado ao jde ou pago, não deixa cancelar	
				if (!ArrayUtils.contains(new String[] { "L", "E", "G" }, reciboPpd.getTpStatus().getValue())) {
					PpdStatusRecibo status = statusReciboService.generateStatus("C", 
							configuracoesFacade.getMensagem("PPD-02030",
									new Object[]{reciboPpd.getFilial().getSgFilial()}));
					status.setUsuario(null);
					statusReciboService.storeChangeStatusImportacaoGrm(
							status,
							reciboPpd);									
				}
			} else {
				//Se o recibo não existe, cria.
				if(reciboPpd == null) {																						
					
					reciboPpd = this.convertReciboArquivoToRecibo(recibo, filial);																				
					
					Long id = (Long)reciboService.storeImportacaoGrm(reciboPpd);	
					if(id != null) {
						PpdStatusRecibo status = statusReciboService.generateStatus("P", 
								configuracoesFacade.getMensagem("PPD-02003",
										new Object[]{reciboPpd.getFilial().getSgFilial()}));
						status.setUsuario(null);
						statusReciboService.storeChangeStatusImportacaoGrm(
								status,
								reciboPpd);							
					}
				}
			}
			if(reciboPpd.getPessoa() == null) {
				System.out.println("RECIBO: " + recibo.getFilialTransm() + " " + recibo.getReciboNumero() + ". FAVORECIDO " + recibo.getFavorecidoCgc() + " NAO ENCONTRADO.");
			}
		} else {
			System.out.println("RECIBO: " + recibo.getFilialTransm() + " " + recibo.getReciboNumero() + ". FILIAL " + recibo.getFilialTransm() + " NAO ENCONTRADA.");			
		}			
	}
	
	private PpdRecibo convertReciboArquivoToRecibo(PpdReciboArquivo reciboArquivo, Filial filial) {
		PpdRecibo recibo = new PpdRecibo();
		if(reciboArquivo.getFilialTransm() != null) 
			recibo.setFilial(filial);
		if(reciboArquivo.getFavorecidoCgc() != null)
			recibo.setPessoa(pessoaService.findByNrIdentificacao(reciboArquivo.getFavorecidoCgc()));
		recibo.setSgFilialOrigem(reciboArquivo.getFilialOrigem());						
		if(reciboArquivo.getCtosNumero() != null)
			recibo.setNrCtrc(Long.valueOf(reciboArquivo.getCtosNumero()));
		recibo.setDtEmissaoCtrc(reciboArquivo.getDataEmissaoCtosDate());
		recibo.setDtRecibo(reciboArquivo.getDataEmissaoReciboDate());						
		if(reciboArquivo.getValorIndenizacao() != null)
			recibo.setVlIndenizacao(reciboArquivo.getValorIndenizacaoBigDecimal());
		if(reciboArquivo.getMotivo() != null)
			recibo.setTpIndenizacao(domainValueService.findDomainValueByValue("DM_PPD_TIPO_INDENIZACAO", reciboArquivo.getMotivo()));
		if(reciboArquivo.getReciboNumero() != null) 
			recibo.setNrRecibo(Long.valueOf(reciboArquivo.getReciboNumero()));
		
		//Se for Seguro, Matriz 100%. Senão considera o que foi passado pela filial.
		if(reciboArquivo.getMotivo().equals("3") || reciboArquivo.getMotivo().equals("4")) {		
			recibo.setNrSeguro(reciboArquivo.getSeguroNumero());
			recibo.setSgFilialComp1("MZ");
			recibo.setPcFilialComp1(100);
		} else {
			recibo.setSgFilialComp1(reciboArquivo.getFilialComp1());
			recibo.setSgFilialComp2(reciboArquivo.getFilialComp2());
			recibo.setSgFilialComp3(reciboArquivo.getFilialComp3());
			if(reciboArquivo.getPercentualFilialComp1() != null)
				recibo.setPcFilialComp1(Integer.valueOf(reciboArquivo.getPercentualFilialComp1()));
			if(reciboArquivo.getPercentualFilialComp2() != null)
				recibo.setPcFilialComp2(Integer.valueOf(reciboArquivo.getPercentualFilialComp2()));
			if(reciboArquivo.getPercentualFilialComp3() != null)
				recibo.setPcFilialComp3(Integer.valueOf(reciboArquivo.getPercentualFilialComp3()));
		}
		
		if(reciboArquivo.getBanco() != null)
			recibo.setNrBanco(Long.valueOf(reciboArquivo.getBanco()));
		if(reciboArquivo.getAgenciaNumero() != null)
			recibo.setNrAgencia(Long.valueOf(reciboArquivo.getAgenciaNumero()));			
		recibo.setNrDigitoAgencia(reciboArquivo.getAgenciaDigito());
		if(reciboArquivo.getContaNumero() != null)
			recibo.setNrContaCorrente(Long.valueOf(reciboArquivo.getContaNumero()));
		recibo.setNrDigitoContaCorrente(reciboArquivo.getContaDigito());			
		
		recibo.setTpStatus(domainValueService.findDomainValueByValue("DM_PPD_STATUS_INDENIZACAO", "P"));
		
		// Busca o RNC a partir do conhecimento que veio no arquivo
		if(recibo.getSgFilialOrigem() != null && recibo.getNrCtrc() != null && recibo.getDtEmissaoCtrc() != null) {
			Map<String, Object> rnc = corporativoService.findRncByConhecimento(
					recibo.getSgFilialOrigem(), 
					recibo.getNrCtrc(),  
					recibo.getDtEmissaoCtrc());
			
			if(rnc != null) {								
				recibo.setNrRnc((Long)rnc.get("nrRnc"));								
				recibo.setSgFilialRnc((String)rnc.get("sgFilialRnc"));
				recibo.setDtEmissaoRnc((YearMonthDay)rnc.get("dtEmissaoRnc"));
			}
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

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setReciboService(PpdReciboService reciboService) {
		this.reciboService = reciboService;
	}

	public void setCorporativoService(PpdCorporativoService corporativoService) {
		this.corporativoService = corporativoService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setParametroService(PpdParametroService parametroService) {
		this.parametroService = parametroService;
	}	
}

class GrFileFilter implements FTPFileFilter {
	public boolean accept(String pathname) {		
		String regExpr = ".{2}+MZI+.{3}+\\.TX+.{1}";

		if( pathname.toUpperCase().matches(regExpr)) {
			return true;
		}
		return false;
	}
}
