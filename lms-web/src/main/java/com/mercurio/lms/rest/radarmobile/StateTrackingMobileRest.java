package com.mercurio.lms.rest.radarmobile;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.tracking.service.StateTrackingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Public
@Path("/statetracking/mobile")
public class StateTrackingMobileRest extends BaseRest {
	
	private static final Logger LOGGER = LogManager.getLogger(StateTrackingMobileRest.class);

	@InjectInJersey
	private StateTrackingService stateTrackingService;

	@GET
	@Path("/findStates")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> findStates() {
		try {
			return stateTrackingService.findStates(new TypedFlatMap());
		} catch (Exception exception) {
			LOGGER.error(exception);
		}
		return null;
	}

}
