package com.mercurio.lms.vol.model.service;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.service.EventoColetaService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vol.model.VolEquipamentos;
import com.mercurio.lms.vol.model.VolLogEnviosSms;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volAvisoPendenciaService"
 */
@Deprecated
public class VolAvisoPendenciaService {
	private VolLogEnviosSmsService volLogEnviosSmsService;
	private VolEquipamentosService volEquipamentosService;
	private EventoColetaService eventoColetaService;
	
	public EventoColetaService getEventoColetaService() {
		return eventoColetaService;
	}
	
	public void setEventoColetaService(EventoColetaService eventoColetaService) {
		this.eventoColetaService = eventoColetaService;
	}
	
	public void executeEnvioUltimaTransmissao(Long idPedidoColeta) {
	
		List eventos = eventoColetaService.findEventoColetaByIdPedidoColeta(idPedidoColeta);
		
		if ((eventos != null) && (eventos.size() > 0)) {
			Map result = (Map)eventos.get(0);
			MeioTransporteRodoviario transporte = (MeioTransporteRodoviario)result.get("meioTransporteRodoviario");
			Long idMeioTranporte = transporte.getIdMeioTransporte();
			if (idMeioTranporte != null) {
				executeEnvio(idMeioTranporte);
			}
		}
	}
	
	public void executeEnvio(Long idMeioTranporte){
		Long idEquipamento = findEquipamento(idMeioTranporte);
		if (idEquipamento!=null){
			executeEnvioSMS(volEquipamentosService.findById(idEquipamento));
		}
	}
	/**
	 * @param equipamento
	 */
	public void executeEnvioSMS(VolEquipamentos equipamento) {
		volLogEnviosSmsService.executeEnvioSms(
				equipamento.getVolOperadorasTelefonia().getIdOperadora(),
				equipamento.getDsNumero(),
				"PEGAR COLETAS\n " + JTFormatUtils.format(JTDateTimeUtils.getDataHoraAtual())
		); 
		VolLogEnviosSms log = new VolLogEnviosSms();
		log.setVolEquipamento(equipamento);
		log.setDhEnvio( JTDateTimeUtils.getDataHoraAtual() );
		log.setTpEvento(new DomainValue("C"));
		log.setObMensagem("PEGAR COLETAS " + log.getDhEnvio());
		volLogEnviosSmsService.store(log);
	}

	private Long findEquipamento(Long idMeioTranporte) {
		List equipamentos = volEquipamentosService.findMeioTransporte(idMeioTranporte);

		if (equipamentos.size() > 0) {
			Map map = (Map) equipamentos.get(0);
			return (Long) map.get("idEquipamento");
		}
		return null;
	}
	
	public void setVolLogEnviosSmsService(VolLogEnviosSmsService volLogEnviosSmsService) {
		this.volLogEnviosSmsService = volLogEnviosSmsService;
	}
	
	public void setVolEquipamentosService(
			VolEquipamentosService volEquipamentosService) {
		this.volEquipamentosService = volEquipamentosService;
	}	
}
