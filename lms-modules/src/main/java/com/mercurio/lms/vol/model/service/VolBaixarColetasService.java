package com.mercurio.lms.vol.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;


import com.mercurio.adsm.framework.BusinessException;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.VolEquipamentos;
import com.mercurio.lms.vol.utils.VolFomatterUtil;
/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volBaixarColetasService"
 * FIXME hj a qtd de coletas é passada do celular, mas não é usada
 */
public class VolBaixarColetasService {
	private PedidoColetaService pedidoColetaService;
	private OcorrenciaColetaService ocorrenciaColetaService;
	private VolEquipamentosService volEquipamentosService; 
	
	
	public void executeBaixa(TypedFlatMap tfm){
		
		PedidoColeta pedidoColeta = pedidoColetaService.findById( tfm.getLong("idPedidoColeta") );
		VolEquipamentos equipamento = volEquipamentosService.findById(tfm.getLong("idEquipamento"));
		
		if( pedidoColeta.getManifestoColeta() != null && 
				equipamento.getMeioTransporte().getIdMeioTransporte() ==  pedidoColeta.getManifestoColeta().getControleCarga().getMeioTransporteByIdTransportado().getIdMeioTransporte()){
			
		//sempre seta os valores enviados pelo celular CQPRO00028533
		
		pedidoColeta.setQtTotalVolumesVerificado(tfm.getInteger("qtVolumes"));
		pedidoColeta.setPsTotalVerificado( tfm.getBigDecimal("peso"));
		pedidoColeta.setVlTotalVerificado( tfm.getBigDecimal("valor"));
		pedidoColetaService.store(pedidoColeta);
		
		String cdOcorrenciaColeta = tfm.getString("cdOcorrenciaColeta");
		
		if (cdOcorrenciaColeta.equals("1") || cdOcorrenciaColeta.equals("01")){
			if (!pedidoColetaService.findPedidoColetaJaExecutadoByMeiodeTransporte(tfm.getLong("idPedidoColeta"), equipamento.getMeioTransporte().getIdMeioTransporte())) {			
				List list = new ArrayList();
				list.add(pedidoColeta.getIdPedidoColeta());
				
				String retorno;
				DateTime dhEvento;
				if(tfm.getString("dhEvento") != null){
					dhEvento = VolFomatterUtil.formatStringToDateTime(tfm.getString("dhEvento"));
					retorno = pedidoColetaService.generateExecutarColetasPendentes(list, dhEvento);
				}else{
					retorno = pedidoColetaService.generateExecutarColetasPendentes(list);					
				}
				
				 
				if (!retorno.equals(""))
					throw new BusinessException(retorno);
			}
		} else {
			tfm.put("dsDescricao","Baixa por celular");
			
			List ids = new ArrayList();
			ids.add(pedidoColeta.getIdPedidoColeta().toString());			
			tfm.put("idsPedidoColeta.ids",ids);
			OcorrenciaColeta ocorrencia = findOcorrenciaColeta(cdOcorrenciaColeta);			
			if (ocorrencia!=null){
				tfm.put("ocorrenciaColeta.idOcorrenciaColeta",ocorrencia.getIdOcorrenciaColeta());
				
				if( ocorrencia.getBlIneficienciaFrota() != null){
					tfm.put("ocorrenciaColeta.blIneficienciaFrota",(ocorrencia.getBlIneficienciaFrota() ? "S" : "N"));
				}else{
					tfm.put("ocorrenciaColeta.blIneficienciaFrota", "N");
			} 
				} 
				
			pedidoColetaService.generateRetornarColeta(tfm);
		}
	}
	}
	/**
	 * Retorna a Ocorrencia da coleta a partir do código
	 * @param cdOcorrenciaColeta	  
	 * @return
	 * 
	 */
	private OcorrenciaColeta findOcorrenciaColeta(String cdOcorrenciaColeta){
		Map criteria = new HashMap();
		criteria.put("codigo",cdOcorrenciaColeta);
		List retorno = ocorrenciaColetaService.find(criteria);
		if (retorno.size()>0)
			return (OcorrenciaColeta) retorno.get(0);
		return null;		
	}
	public void setOcorrenciaColetaService(OcorrenciaColetaService ocorrenciaColetaService) {
		this.ocorrenciaColetaService = ocorrenciaColetaService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	public VolEquipamentosService getVolEquipamentosService() {
		return volEquipamentosService;
	}
	public void setVolEquipamentosService(
			VolEquipamentosService volEquipamentosService) {
		this.volEquipamentosService = volEquipamentosService;
	}



}
