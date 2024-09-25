package com.mercurio.lms.rest.config;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.utils.FileResponseBuilder;

@Path("/report")
public class ReportRest {

	@InjectInJersey ReportExecutionManager reportExecutionManager;
	
	@GET
	@Path("{fileName}.csv")
	@Produces({"text/csv"})
	public Response getReportCSV(@PathParam("fileName") final String fileName, @Context final Request request) throws WebApplicationException {
		
		File reportOutputDir = reportExecutionManager.getReportOutputDir();
		final File file = new File(reportOutputDir, fileName + ".csv");
		
		if (!file.exists()) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return new FileResponseBuilder().convertToResponse(file, FileTypeHeadEnum.CSV.getType());
	}
}