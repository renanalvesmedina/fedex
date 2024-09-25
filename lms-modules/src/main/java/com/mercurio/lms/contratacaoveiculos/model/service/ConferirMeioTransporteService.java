package com.mercurio.lms.contratacaoveiculos.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.conferirVolumeService"
 */
public class ConferirMeioTransporteService extends CrudService {
	
	private MeioTransporteService meioTransporteService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private EventoMeioTransporteService eventoMeioTransporteService;
	
	
	public Map<String, Object> findMeioTransporteByBarCode(String barCode){			
		
		Map<String, Object> criteria = new HashMap();
		criteria.put("nrCodigoBarra", barCode);

		MeioTransporte meioTransporte = null;
		
		List listaMeioTransporte = getMeioTransporteService().find(criteria);
		
		if(listaMeioTransporte.size() == 1){
			meioTransporte = (MeioTransporte)listaMeioTransporte.get(0);
		}
		else if(listaMeioTransporte.size() == 0){
			throw new BusinessException("LMS_MWW_CONFERIR_MEIO_TRANSPORTE_NOT_FOUND");
		}
		else{
			throw new BusinessException("LMS_MWW_CONFERIR_MEIO_TRANSPORTE_ERROR");		
		}
		
		//CRIA OBJETOS PARA BUSCA DAS INFORMAÇÕES =======
		Proprietario proprietario = getMeioTranspProprietarioService()
													.findProprietarioByIdMeioTransporte(meioTransporte.getIdMeioTransporte(), new YearMonthDay());
		TipoMeioTransporte tipoMeioTransporte = meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte();

		//===============================================
		
		//INICIALIZA AS VARIÁVAEIS COM VALORES DEFAULT ==
		String nrFrota = meioTransporte.getNrFrota();
		String placa = meioTransporte.getNrIdentificador();
		String vinculo = meioTransporte.getTpVinculo().getDescriptionAsString();
		String sProprietario = "";
		String tipo = "";

		
		//VERIFICA SE OBTEVE ALGUM OBJETO NULO E ATRIBUI A INFORMAÇÃO A VARIAVEL		
		if(proprietario != null){
			sProprietario = proprietario.getPessoa().getNmPessoa();
		}
		if(tipoMeioTransporte != null){
			tipo = tipoMeioTransporte.getDsTipoMeioTransporte();
		}
		
				
		//CRIA O MAPA PARA RETORNAR COM OS VALORES OBTIDOS ========
		Map<String, Object> map = new HashMap();
		
		map.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
		map.put("nrFrota", nrFrota);
		map.put("placa", placa);
		map.put("vinculo", vinculo);
		map.put("proprietario", sProprietario);
		map.put("tipo", tipo);
		
		return map;		
	}


	public void setEventoMeioTransporteService(
			EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}


	public EventoMeioTransporteService getEventoMeioTransporteService() {
		return eventoMeioTransporteService;
	}


	public void setMeioTranspProprietarioService(
			MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}


	public MeioTranspProprietarioService getMeioTranspProprietarioService() {
		return meioTranspProprietarioService;
	}


	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}


	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

}
