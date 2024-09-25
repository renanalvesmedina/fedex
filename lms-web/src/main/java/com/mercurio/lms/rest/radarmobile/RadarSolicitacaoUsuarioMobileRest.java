package com.mercurio.lms.rest.radarmobile;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.tracking.service.RadarSolicitacaoUsuarioService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Public
@Path("/radarsolicitacaousuario/mobile")
public class RadarSolicitacaoUsuarioMobileRest extends BaseRest {
	
	private static final Logger LOGGER = LogManager.getLogger(RadarSolicitacaoUsuarioMobileRest.class);

	@InjectInJersey
	private RadarSolicitacaoUsuarioService radarSolicitacaoUsuarioService;

	@POST
	@Path("/storeOrder")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> storeOrder(TypedFlatMap map) {
		try {
			return radarSolicitacaoUsuarioService.storeOrder(map);
		} catch (Exception exception) {
			LOGGER.error(exception);
		}
		return null;
	}
}
