package com.mercurio.lms.rest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.core.util.ServerInfo;
import com.mercurio.lms.annotation.Public;

@Public
@Path("/systemInfo")
public class SystemInfoRest {	
	@GET
	public Response getInformacoesSistema(@Context HttpServletRequest request) {
		Map<String, Object> res = new HashMap<String, Object>();
		
		ServletContext servletContext = request.getSession().getServletContext();
		res.put("systemName", servletContext.getInitParameter("systemName"));
		String version = null;
		String chaveVersion = null;
		if (servletContext.getAttribute("systemVersionInfo") != null) {
			version = servletContext.getAttribute("systemVersionInfo").toString();
			chaveVersion = version;
		} else {
			chaveVersion = String.valueOf((new Date()).getTime());
			version = "1.0";
		}
		res.put("version", version);
		res.put("serverInfo", getServerInfo());
		res.put("contextPath", request.getContextPath());
		
		chaveVersion = chaveVersion.replace("?","").replace("&","").replace(" ","").replace("-","").replace("/","").replace(":","");
		res.put("chaveVersion", chaveVersion);

		return Response.ok(res).build();
	}

	private String getServerInfo() {
		return ServerInfo.getServerInfo();
	}
}
