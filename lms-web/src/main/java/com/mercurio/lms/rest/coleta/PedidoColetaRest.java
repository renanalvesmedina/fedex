package com.mercurio.lms.rest.coleta;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/coleta/pedidoColeta")
public class PedidoColetaRest {
	
	@InjectInJersey PedidoColetaService pedidoColetaService;

	@POST
	@Path("findPedidoColetaSuggest")
	public Response findPedidoColetaSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		return findPedidoColetaSuggest(value, SessionUtils.getEmpresaSessao().getIdEmpresa());
	}
	
	private Response findPedidoColetaSuggest(String val, Long idEmpresa) {

		if (StringUtils.isNotBlank(val)) {
			
			val = val.replace(" ", "");
			
			if (val.length() >= 4) {
				
				String sgFilial = val.substring(0, 3).toUpperCase();
				
				String snrPedidoColeta = val.substring(3);
				
				if (StringUtils.isNumeric(snrPedidoColeta)) {
					
					Long nrPedidoColeta = Long.valueOf(snrPedidoColeta);
					
					return Response.ok(pedidoColetaService.findPedidoColetaSuggest(sgFilial, nrPedidoColeta, idEmpresa)).build();
					
				}
				
			}
		
		}
		
		return Response.ok().build();
		
	}
	
}
