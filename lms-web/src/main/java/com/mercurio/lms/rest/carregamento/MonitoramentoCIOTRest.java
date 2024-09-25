package com.mercurio.lms.rest.carregamento;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseListRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.CIOTControleCarga;
import com.mercurio.lms.expedicao.model.service.CIOTControleCargaService;
import com.mercurio.lms.expedicao.model.service.CIOTService;
import com.mercurio.lms.rest.carregamento.dto.MonitoramentoCIOTDTO;
import com.mercurio.lms.rest.carregamento.dto.MonitoramentoCIOTFilterDTO;
import com.mercurio.lms.rest.carregamento.dto.MonitoramentoCIOTListDTO;
 
@Path("/carregamento/monitoramentoCIOT") 
public class MonitoramentoCIOTRest extends BaseListRest<MonitoramentoCIOTListDTO, MonitoramentoCIOTFilterDTO> {

	@InjectInJersey
	CIOTService ciotService;
	
	@InjectInJersey
	CIOTControleCargaService ciotControleCargaService;
	
	@Override
	protected void removeByIds(List<Long> ids) {
		throw new InfrastructureException("Método não deve ser utilizado!");
	}
	
	@POST
	@Path("reenviar")
	public Response reenviar(MonitoramentoCIOTDTO monitoramentoCIOTDTO){
		CIOT ciot = new CIOT();
		ciot.setIdCIOT(monitoramentoCIOTDTO.getIdCiot());
		ciot.setTpSituacao(monitoramentoCIOTDTO.getTpSituacao());

		ControleCarga controleCarga = new ControleCarga();
		controleCarga.setIdControleCarga(monitoramentoCIOTDTO.getIdControleCarga());
		
		CIOTControleCarga ciotControleCarga = new CIOTControleCarga();
		ciotControleCarga.setCiot(ciot);
		ciotControleCarga.setControleCarga(controleCarga);
		
		ciot = ciotService.executeReenviar(ciotControleCarga);
		monitoramentoCIOTDTO.setTpSituacao(ciot.getTpSituacao());
		
		return Response.ok(monitoramentoCIOTDTO).build();
	}
	
	@GET
	@Path("/{id}")
	public Response findById(@PathParam("id") Long id){
		CIOTControleCarga ciotControleCarga = ciotControleCargaService.findByIdForMonitoramento(id);
		MonitoramentoCIOTDTO monitoramentoCIOTDTO = convertToMonitoramentoCIOTDTO(ciotControleCarga);
		return Response.ok(monitoramentoCIOTDTO).build();
	}
	
	private MonitoramentoCIOTDTO convertToMonitoramentoCIOTDTO(CIOTControleCarga ciotControleCarga){
		CIOT ciot = ciotControleCarga.getCiot();
		ControleCarga controleCarga = ciotControleCarga.getControleCarga();

		MonitoramentoCIOTDTO monitoramentoCIOTDTO = new MonitoramentoCIOTDTO();
		monitoramentoCIOTDTO.setId(ciotControleCarga.getIdCIOTControleCarga());
		
		monitoramentoCIOTDTO.setIdCiot(ciot.getIdCIOT());
		monitoramentoCIOTDTO.setNrCIOT(ciot.getNrCIOT());
		monitoramentoCIOTDTO.setNrCodigoVerificador(ciot.getNrCodigoVerificador());
		monitoramentoCIOTDTO.setTpSituacao(ciot.getTpSituacao());
		monitoramentoCIOTDTO.setDsObservacao(ciot.getDsObservacao());
		monitoramentoCIOTDTO.setDhGeracao(ciot.getDhGeracao());
		monitoramentoCIOTDTO.setNrFrota(ciot.getMeioTransporte().getNrFrota());
		monitoramentoCIOTDTO.setNrIdentificador(ciot.getMeioTransporte().getNrIdentificador());
		monitoramentoCIOTDTO.setValor(ciot.getVlFrete());
		
		monitoramentoCIOTDTO.setIdControleCarga(controleCarga.getIdControleCarga());
		monitoramentoCIOTDTO.setNrControleCarga(controleCarga.getNrControleCarga());
		monitoramentoCIOTDTO.setSgFilialControleCarga(controleCarga.getFilialByIdFilialOrigem().getSgFilial());
		monitoramentoCIOTDTO.setTpStatusControleCarga(controleCarga.getTpStatusControleCarga());
		if(controleCarga.getProprietario() != null){
			monitoramentoCIOTDTO.setDsProprietario(controleCarga.getProprietario().getPessoa().getNmPessoa());
		}
		
		return monitoramentoCIOTDTO;
	}
	
	@Override
	protected Integer count(MonitoramentoCIOTFilterDTO monitoramentoCIOTFilterDTO) {
		TypedFlatMap toReturn = getTypedFlatMap(monitoramentoCIOTFilterDTO);
		return ciotControleCargaService.getRowCountMonitoramento(toReturn);
	} 

	@Override
	protected List<MonitoramentoCIOTListDTO> find(MonitoramentoCIOTFilterDTO monitoramentoCIOTFilterDTO) {
		TypedFlatMap criteria = getTypedFlatMap(monitoramentoCIOTFilterDTO);
		ResultSetPage<Map<String, Object>>  resultSetPage = ciotControleCargaService.findPaginatedMonitoramento(criteria);
		List<MonitoramentoCIOTListDTO> retorno = convertToMonitoramentoCIOTListDTO(resultSetPage);
		return retorno;
	}

	private List<MonitoramentoCIOTListDTO> convertToMonitoramentoCIOTListDTO(ResultSetPage<Map<String, Object>> resultSetPage) {
		List<MonitoramentoCIOTListDTO> retorno = new ArrayList<MonitoramentoCIOTListDTO>();
		
		if(resultSetPage.getList() != null){
			for(Map<String, Object> map : resultSetPage.getList()){
				MonitoramentoCIOTListDTO monitoramentoCIOTListDTO = new MonitoramentoCIOTListDTO();
				monitoramentoCIOTListDTO.setId((Long) map.get("id"));
				monitoramentoCIOTListDTO.setNrControleCarga((Long) map.get("nrControleCarga"));
				monitoramentoCIOTListDTO.setSgFilialControleCarga((String) map.get("sgFilialControleCarga"));
				monitoramentoCIOTListDTO.setNrFrota((String) map.get("nrFrota"));
				monitoramentoCIOTListDTO.setNrIdentificador((String) map.get("nrIdentificador"));
				monitoramentoCIOTListDTO.setNrCIOT((Long) map.get("nrCIOT"));
				monitoramentoCIOTListDTO.setNrCodigoVerificador((String) map.get("nrCodigoVerificador"));
				monitoramentoCIOTListDTO.setTpSituacao((String) map.get("tpSituacao"));
				monitoramentoCIOTListDTO.setDsObservacao((String) map.get("dsObservacao"));
				retorno.add(monitoramentoCIOTListDTO);
			}
		}
		
		return retorno;
	}

	private TypedFlatMap getTypedFlatMap(MonitoramentoCIOTFilterDTO monitoramentoCIOTFilterDTO) {
		
		TypedFlatMap toReturn = super.getTypedFlatMapWithPaginationInfo(monitoramentoCIOTFilterDTO);
		
		if(monitoramentoCIOTFilterDTO.getFilial() != null){
			toReturn.put("idFilial", monitoramentoCIOTFilterDTO.getFilial().getIdFilial());
		}
		
		if(monitoramentoCIOTFilterDTO.getControleCarga() != null){
			toReturn.put("idControleCarga", monitoramentoCIOTFilterDTO.getControleCarga().getIdControleCarga());
		}
		
		toReturn.put("periodoInicial", monitoramentoCIOTFilterDTO.getPeriodoInicial());
		toReturn.put("periodoFinal", monitoramentoCIOTFilterDTO.getPeriodoFinal());
		
		if(monitoramentoCIOTFilterDTO.getTpSituacao() != null){
			toReturn.put("tpSituacao", monitoramentoCIOTFilterDTO.getTpSituacao().getValue());
		}
		
		return toReturn;
	}
	

 
} 
