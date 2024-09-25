package com.mercurio.lms.rest.contratacaoveiculos;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.joda.time.format.ISODateTimeFormat;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contratacaoveiculos.model.BloqueioMotoristaProp;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.BloqueioMotoristaPropService;

/**
 * Rest responsável pelo controle da tela manter proprietário.
 * 
 */
@Path("/contratacaoveiculos/manterBloqueiosMotoristaProprietario")
public class BloqueiosMotoristaProprietarioRest {

	@InjectInJersey
	private BloqueioMotoristaPropService bloqueioMotoristaPropService;
	
	/**
	 * Remove um ou mais itens da tabela de arquivos de anexos.
	 * 
	 * @param ids
	 */
	@POST
	@Path("store")
	public void store(Map<String, Object> map) {				
		bloqueioMotoristaPropService.storeBloqueio(populateBloqueioMotoristaProp(map));
	}
	
	/**
	 * Verifica se existem bloqueios para o proprietário solicitado.
	 * 
	 * @param id
	 * @return Response
	 */
	@GET
	@Path("findBloqueioByProprietario")
	public Response findDadosBloqueioProprietario(@QueryParam("id") Long id){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idProprietario", id.toString());
		
		return Response.ok(bloqueioMotoristaPropService.findDadosBloqueioProprietario(params)).build();
	}	
	
	/**
	 * Popula um objeto BloqueioMotoristaProp de um objeto Map.
	 * 
	 * @param map
	 * 
	 * @return BloqueioMotoristaProp
	 */
	private BloqueioMotoristaProp populateBloqueioMotoristaProp(Map<String, Object> map) {
		Proprietario proprietario = new Proprietario();
		proprietario.setIdProprietario(MapUtils.getLong(map, "idProprietario"));
		
		BloqueioMotoristaProp bloqueioMotoristaProp = new BloqueioMotoristaProp();		
		bloqueioMotoristaProp.setObBloqueioMotoristaProp(MapUtils.getString(map, "obBloqueioMotoristaProp"));		
		bloqueioMotoristaProp.setDhVigenciaInicial(ISODateTimeFormat.dateTimeParser().parseDateTime(MapUtils.getString(map, "dhVigenciaInicial")));
		bloqueioMotoristaProp.setProprietario(proprietario);
		
		Long idBloqueioMotoristaProp = MapUtils.getLong(map,"idBloqueioMotoristaProp");
		
		if(idBloqueioMotoristaProp != null){
			bloqueioMotoristaProp.setIdBloqueioMotoristaProp(idBloqueioMotoristaProp);	
		}
		
		String dhVigenciaFinal = MapUtils.getString(map, "dhVigenciaFinal");
		
		if(dhVigenciaFinal != null){
			bloqueioMotoristaProp.setDhVigenciaFinal(ISODateTimeFormat.dateTimeParser().parseDateTime(dhVigenciaFinal));	
		}
		
		return bloqueioMotoristaProp;
	}
}