package com.mercurio.lms.ppd.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.PpdStatusRecibo;
import com.mercurio.lms.ppd.model.service.PpdReciboService;
import com.mercurio.lms.ppd.model.service.PpdStatusReciboService;

public class ManterImportarPagamentosAction {
	PpdStatusReciboService statusReciboService;	
	PpdReciboService reciboService;
	FilialService filialService;
	EmpresaService empresaService;
	
	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
	
	@SuppressWarnings("deprecation")
	public Map<String, Object> store(Map<String, Object> bean) {
		String param = (String)bean.get("recibos");
		String[] recibos = param.split(LINE_SEPARATOR);
		Map<String, Object> retorno = new HashMap<String, Object>();
		List<String> recibosRetorno = new ArrayList<String>();
		
		for(String strRecibo : recibos) {
			if (strRecibo.trim().length() == 0) {
				continue;
			}
			
			if ((strRecibo.trim().length() < 19) || (strRecibo.split(";").length < 3)) {
				recibosRetorno.add(strRecibo + " -> Verifique formato! Ex.: CWB;7515;01/01/1972");
				continue;
			}
			strRecibo = strRecibo.substring(0,19);
			
			String sgFil = strRecibo.split(";")[0].trim();
			
			if (sgFil.length() < 3) {
				recibosRetorno.add(strRecibo + " -> Filial Inválida!");
				continue;
			}
			
			Filial filial = filialService.findFilial(empresaService.findEmpresaMercurio().getIdEmpresa(), sgFil);
			
			if(filial != null) {
				Long nrRecibo = 0L;
				try {
					nrRecibo = Long.parseLong(strRecibo.split(";")[1]);
				} catch (Exception e) {
					recibosRetorno.add(strRecibo + " -> Número Recibo Inválido!");
					continue;
				}
				YearMonthDay dataRecibo = null;
				// Data de emissão do recibo
				try {
					int dayRecibo = Integer.parseInt(strRecibo.split(";")[2].split("/")[0]);
					int monthRecibo = Integer.parseInt(strRecibo.split(";")[2].split("/")[1]);
					int yearRecibo = Integer.parseInt(strRecibo.split(";")[2].split("/")[2].substring(0,4));
					dataRecibo = new YearMonthDay(yearRecibo, monthRecibo, dayRecibo);
				}
				catch (Exception e){
					recibosRetorno.add(strRecibo + " -> Formato da Data Inválida");
					continue;
				}
				
				PpdRecibo recibo = reciboService.findByRecibo(
						filial.getIdFilial(), 
						nrRecibo, 
						dataRecibo);
				
				if (recibo != null) {
					
					if (recibo.getTpStatus().getValue().equals("G")) {
						recibosRetorno.add(strRecibo + " -> Recibo já pago.");
						continue;
					}
					
					Map criteria = new HashedMap();
					criteria.put("recibo.idRecibo", recibo.getIdRecibo());
					List<PpdStatusRecibo> allStatus = statusReciboService.find(criteria);
					DateTime dhPendente = new DateTime();
					Map<String, DateTime> eventos = new HashMap<String, DateTime>();

					for(PpdStatusRecibo status : allStatus) {
						eventos.put(status.getTpStatusRecibo().getValue(), status.getDhStatusRecibo());
					}
			
					if (eventos.get("L") == null) {
						PpdStatusRecibo statusLote = statusReciboService.generateStatus("L", "");
						statusLote.setUsuario(null);	
						dhPendente = dhPendente.plusHours(1);
						statusLote.setDhStatusRecibo(
								new DateTime(
										dhPendente.getYear(),
										dhPendente.getMonthOfYear(),
										dhPendente.getDayOfMonth(),
										dhPendente.getHourOfDay(),
										dhPendente.getMinuteOfHour(),
										dhPendente.getSecondOfMinute(),
										dhPendente.getMillisOfSecond(),
										DateTimeZone.forID("America/Sao_Paulo")));
						statusReciboService.storeChangeStatus(statusLote, recibo);
					}
						
					if (eventos.get("E") == null) {
						PpdStatusRecibo statusEnviado = statusReciboService.generateStatus("E", "");
						statusEnviado.setUsuario(null);						
						dhPendente = dhPendente.plusHours(1);
						statusEnviado.setDhStatusRecibo(
								new DateTime(
										dhPendente.getYear(),
										dhPendente.getMonthOfYear(),
										dhPendente.getDayOfMonth(),
										dhPendente.getHourOfDay(),
										dhPendente.getMinuteOfHour(),
										dhPendente.getSecondOfMinute(),
										dhPendente.getMillisOfSecond(),
										DateTimeZone.forID("America/Sao_Paulo")));
						statusReciboService.storeChangeStatus(statusEnviado, recibo);
					}
					
					PpdStatusRecibo statusPagamento = statusReciboService.generateStatus("G", "");
					statusPagamento.setUsuario(null);
					
					if(strRecibo.split(";").length == 4) {
						// Data de Pagamento vinda no parâmetro
						int dayPagto = Integer.parseInt(strRecibo.split(";")[3].split("/")[0]);
						int monthPagto = Integer.parseInt(strRecibo.split(";")[3].split("/")[1]);
						int yearPagto = Integer.parseInt(strRecibo.split(";")[3].split("/")[2]);
						
						statusPagamento.setDhStatusRecibo(
								new DateTime(yearPagto,monthPagto,dayPagto,0,0,0,0,DateTimeZone.forID("America/Sao_Paulo")));						
					} else {
						// Data de pagamento não foi informada, gera a data de pagamento
						dhPendente = dhPendente.plusHours(1);
						statusPagamento.setDhStatusRecibo(
								new DateTime( 
										dhPendente.getYear(),
										dhPendente.getMonthOfYear(),
										dhPendente.getDayOfMonth(),
										dhPendente.getHourOfDay(),
										dhPendente.getMinuteOfHour(),
										dhPendente.getSecondOfMinute(),
										dhPendente.getMillisOfSecond(),
										DateTimeZone.forID("America/Sao_Paulo")));
					}
					// Atualiza data programada para pagto de acordo com a data de pagto
					recibo.setDtProgramadaPagto(
							new YearMonthDay(
									statusPagamento.getDhStatusRecibo().getYear(),
									statusPagamento.getDhStatusRecibo().getMonthOfYear(),
									statusPagamento.getDhStatusRecibo().getDayOfMonth()));
					
					statusReciboService.storeChangeStatus(statusPagamento, recibo);
					
					recibosRetorno.add(strRecibo + " -> OK");
				} else {
					recibosRetorno.add(strRecibo + " -> Recibo não encontrado");
				}
			} else {
				recibosRetorno.add(strRecibo + " -> Filial não encontrada");
			}
		}		
		retorno.put("recibos", recibosRetorno);
		return retorno;
	}
	
	// Set das classes de serviço
	public void setStatusReciboService(PpdStatusReciboService statusReciboService) {
		this.statusReciboService = statusReciboService;
	}

	public void setReciboService(PpdReciboService reciboService) {
		this.reciboService = reciboService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}	 	
}