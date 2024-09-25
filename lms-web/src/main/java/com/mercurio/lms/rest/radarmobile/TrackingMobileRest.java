package com.mercurio.lms.rest.radarmobile;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.tracking.service.SearchTrackingService;
import com.mercurio.lms.tracking.service.ServiceTrackingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Public
@Path("/tracking/mobile")
public class TrackingMobileRest extends BaseRest {
	
	private static final Logger LOGGER = LogManager.getLogger(TrackingMobileRest.class);

	@InjectInJersey
	private ServiceTrackingService serviceTrackingService;

	@InjectInJersey
	private SearchTrackingService searchTrackingService;

	@GET
	@Path("/findServices")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> findServices() {
		try {
			return serviceTrackingService.findServices();
		} catch (Exception exception) {
			LOGGER.error(exception);
		}
		return null;
	}

	@POST
	@Path("/searchConsignment")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> searchConsignment(TypedFlatMap map) {
		try {
			return searchTrackingService.searchConsignment(map);
		} catch (Exception exception) {
			LOGGER.error(exception);
		}
		return null;
	}
	
}
