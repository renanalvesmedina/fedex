package com.mercurio.lms.rest.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.rest.config.FileTypeHeadEnum;

public class FileResponseBuilder {
	
	private ReportExecutionManager reportExecutionManager;
	
	public FileResponseBuilder() {
	}
	
	public FileResponseBuilder(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	
	public Response executeResponseReport(final ReportServiceSupport reportService, String formatoRelatorio, TypedFlatMap parameters)
			throws Exception {
		final File file = reportExecutionManager.executeReport(reportService, parameters);
		return convertToResponse(file, formatoRelatorio);
	}
	
	public Response convertToResponse(final File file, final String type) {
		String typeHead = FileTypeHeadEnum.getTypeHead(type);
		return Response.ok(fileStream(file))
		            .encoding("ISO-8859-1")
		            .header("Content-disposition", "attachment;")
		            .type(typeHead)
		            .build();
	}
	
	private StreamingOutput fileStream(final File file) {
		return new StreamingOutput() {
			private FileInputStream fis = null;

			@Override
			public void write(OutputStream output) throws IOException {
				fis = new FileInputStream(file);
				int content;
				while ((content = fis.read()) != -1) {
					output.write(content);
				}
			}
		};
	}
}