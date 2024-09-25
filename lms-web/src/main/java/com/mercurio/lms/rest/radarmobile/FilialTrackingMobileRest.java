package com.mercurio.lms.rest.radarmobile;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.tracking.service.FilialTrackingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Public
@Path("/filialtracking/mobile")
public class FilialTrackingMobileRest extends BaseRest {
	
	private static final Logger LOGGER = LogManager.getLogger(FilialTrackingMobileRest.class);

	@InjectInJersey
	private FilialTrackingService filialTrackingService;

	@GET
	@Path("/findDepot")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> findDepot() {
		try {
			return filialTrackingService.findDepot(new TypedFlatMap());
		} catch (Exception exception) {
			LOGGER.error(exception);
		}
		return null;
	}

	@POST
	@Path("/findDepotByStateAndCity")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> findDepotByStateAndCity(TypedFlatMap map) {
		try {
			return filialTrackingService.findDepotByStateAndCity(map);
		} catch (Exception exception) {
			LOGGER.error(exception);
		}
		return null;
	}

	@GET
	@Path("/findFiliais")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> findFiliais() {
		try {
			return filialTrackingService.findFiliais(new TypedFlatMap());
		} catch (Exception exception) {
			LOGGER.error(exception);
		}
		return null;
	}
	
}
