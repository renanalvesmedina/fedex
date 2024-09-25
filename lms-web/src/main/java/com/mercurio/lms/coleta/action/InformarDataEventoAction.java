package com.mercurio.lms.coleta.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.coleta.model.service.EventoColetaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.InformarDataEventoAction"
 */

public class InformarDataEventoAction  {
	
	private EventoColetaService eventoColetaService;
	private EventoControleCargaService eventoControleCargaService;

	public TypedFlatMap findUsuario(TypedFlatMap map) {
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    	
    	map.put("dtHoraOcorrencia",df.format(new Date()));    	
    	map.put("idUsuario", SessionUtils.getUsuarioLogado().getIdUsuario());
		map.put("nrMatricula", SessionUtils.getUsuarioLogado().getNrMatricula());
		map.put("nmUsuario", SessionUtils.getUsuarioLogado().getNmUsuario());
    	
    	return map;
    }
    
	public TypedFlatMap confirmarDataHoraEvento(TypedFlatMap map) {
    	String pedidos = (String) map.get("idPedidoColeta");
    	DateTime dhLiberacao = map.getDateTime("dtHoraOcorrencia");
    	Long idControleCarga = map.getLong("idControleCarga");
    	
    	String[] pColetas = pedidos.split(";");
    	boolean transmitida = false;
    	
    	DateTime dhTransmitida = null;
    	DateTime dhManifestada = null;
    	
    	List<EventoControleCarga> eventosChegada = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(null, idControleCarga, "CP");    	
    	
    	//verifica se coleta foi transmitida
    	List<Map> eventosColetaTransmitida = eventoColetaService.findEventoColetaTransmitidaByIdPedidoColeta(Long.valueOf(pColetas[0]));
    	if(!eventosColetaTransmitida.isEmpty()){
    		dhTransmitida = (DateTime) eventosColetaTransmitida.get(0).get("dhEvento");
    	}
    	
    	//verificar se há evento de manisfestada
    	List<Map> eventosColetaManifestada = eventoColetaService.findEventoColetaManifestadaByIdPedidoColeta(Long.valueOf(pColetas[0]));
    	if(!eventosColetaManifestada.isEmpty()){
    		dhManifestada = (DateTime) eventosColetaManifestada.get(0).get("dhEvento");    		
    	}
    	//comparo e verifico qual é o maior
    	
    	if(dhTransmitida == null){
    		transmitida = false;
    	}
    	if(dhManifestada == null){
    		transmitida = true;
    	}
    	
    	//se transmitida maior que manifestada seto transmitida
    	if(dhTransmitida != null && dhManifestada != null && dhTransmitida.isAfter(dhManifestada)){
    		transmitida = true;
    	}
    	
    	//escolho qual data hora inicio    	
    	if(transmitida){    	
        	if(dhLiberacao.isBefore(dhTransmitida)){
        		if(!eventosChegada.isEmpty()){
        			//A data/hora da ocorrência deve ser posterior à data/hora da transmissão da coleta e anterior à chegada do veículo na portaria.
        			throw new BusinessException("LMS-02110");    
        		}else{
        			//A data/hora da ocorrência deve ser posterior à data/hora da transmissão da coleta e igual ou anterior à data atual.
        			throw new BusinessException("LMS-02108");        			
        		}
        	}
    	}else{
			//Se não foi transmitida vale data evento saida portaria
    		List<EventoControleCarga> eventosSaidaPortaria = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(null, idControleCarga, "SP");
			if(!eventosSaidaPortaria.isEmpty()){
	    		DateTime dhSaidaPortaria = (DateTime) eventosSaidaPortaria.get(0).getDhEvento();
	    		//A data/hora da ocorrência deve ser posterior à data/hora da saída do veículo na portaria e igual ou anterior à data atual.
	        	if(dhLiberacao.isBefore(dhSaidaPortaria)){
	        		if(!eventosChegada.isEmpty()){
	        			throw new BusinessException("LMS-02107");
	        		}else{
	        			throw new BusinessException("LMS-02109");
	        		}
	        			
	        	}
			}
		}
    	
    	if(!eventosChegada.isEmpty()){
    		//A data/hora da ocorrência deve ser entre a data/hora do evento de saída do veículo na portaria e a data/hora do evento de chegada do veículo na portaria.
    		DateTime dhChegadaPortaria  = eventosChegada.get(0).getDhEvento();
    		if(dhLiberacao.isAfter(dhChegadaPortaria)){
    			if(transmitida){
    				throw new BusinessException("LMS-02110");
    			}else{
    				throw new BusinessException("LMS-02107");	    				
    			}
    		}
    	}else{
    		DateTime dhAgora = JTDateTimeUtils.getDataHoraAtual(); 
			if(dhLiberacao.isAfter(dhAgora)){
				if(transmitida){
					throw new BusinessException("LMS-02108");
				}else{
					throw new BusinessException("LMS-02109");						
				}
    		}
    	}
    	
    	return map;
    }
    
    public EventoColetaService getEventoColetaService() {
		return eventoColetaService;
	}

	public void setEventoColetaService(EventoColetaService eventoColetaService) {
		this.eventoColetaService = eventoColetaService;
	}

	public EventoControleCargaService getEventoControleCargaService() {
		return eventoControleCargaService;
	}

	public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}


    
}