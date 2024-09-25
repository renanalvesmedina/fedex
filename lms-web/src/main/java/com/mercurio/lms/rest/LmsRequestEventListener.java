package com.mercurio.lms.rest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;

import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import com.mercurio.adsm.core.model.distribution.ServiceDataObject;
import com.mercurio.adsm.core.monitor.AdsmMonitorProfilerWrap;
import com.mercurio.adsm.monitor.model.base.v200.post.BaseResponsePost;

public class LmsRequestEventListener implements RequestEventListener {
	private final BaseResponsePost rpm;
	private static final String X_FORWARD_FOR = "X-Forwarded-For";
	
	public LmsRequestEventListener(RequestEvent requestEvent) {
		rpm = AdsmMonitorProfilerWrap.postSetStart(getClientAddress(requestEvent.getContainerRequest()));				
	}

	@Override
	public void onEvent(RequestEvent event) {
		switch (event.getType()) {
			case RESOURCE_METHOD_START:
				List<ServiceDataObject> sdos = createSDOS(event);				
				AdsmMonitorProfilerWrap.postSetInputData(rpm, sdos);
				break;
        	case ON_EXCEPTION:
         		AdsmMonitorProfilerWrap.postAddError(rpm, event.getException());
         		break;
         	case FINISHED:
	         	AdsmMonitorProfilerWrap.postSetStop(rpm);
	         	break;
         	default:
         		break;		
		 } 
	}
	
	private String getClientAddress(ContainerRequestContext requestContext) {		
		return requestContext.getHeaderString(X_FORWARD_FOR);		
	}
	
	private List<ServiceDataObject> createSDOS(RequestEvent event) {
		final String methodName = getMethodName(event);			
		
		ServiceDataObject sdo = new ServiceDataObject(
				methodName, "REST->"+methodName, getParametros(event.getContainerRequest()));
		
		sdo.setCallTS(System.currentTimeMillis());
		
		List<ServiceDataObject> sdos = new ArrayList<ServiceDataObject>();
		sdos.add(sdo);
		return sdos;
	}

	private Map<String, Object> getParametros(ContainerRequestContext requestContext) {
		MultivaluedMap<String, String> params = requestContext.getUriInfo().getQueryParameters(); 
		Map<String, Object> result = new HashMap<String, Object>();
		
		for(Entry<String, List<String>> entry : params.entrySet()) {
			List<String> value = entry.getValue();
			if(value != null && value.size() == 1) {
				result.put(entry.getKey(), value.get(0));
			} else {
				result.put(entry.getKey(), value);
			}
		}
		
		return result;
	}

	private String getMethodName(RequestEvent event) {		
		final Method method = event.getUriInfo().getMatchedResourceMethod()
				.getInvocable().getHandlingMethod();

		return method.getDeclaringClass().getCanonicalName()
				.replace("com.mercurio.", "")
				+ "." + method.getName();
	}
}
