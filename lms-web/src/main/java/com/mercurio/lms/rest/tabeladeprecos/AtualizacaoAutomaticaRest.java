package com.mercurio.lms.rest.tabeladeprecos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.adsm.rest.ResponseDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.rest.BeanUtils;
import com.mercurio.lms.rest.utils.ExportUtils;
import com.mercurio.lms.tabelaprecos.model.service.AtualizacaoAutomaticaService;

@Path("/tabeladeprecos/atualizacaoAutomatica")
public class AtualizacaoAutomaticaRest extends BaseRest {

	@InjectInJersey AtualizacaoAutomaticaService atualizacaoAutomaticaService;
	
	@POST
	@Path("importar")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response importar(FormDataMultiPart formDataMultiPart) throws IOException{

		try {
			ResponseDTO responseDTO = new ResponseDTO();

			String arquivo = getCharacterLobUserTypeFromForm(formDataMultiPart, "arquivo");
			
			List<Map<String, Object>> listIdsNotFound = atualizacaoAutomaticaService.executeImportar(arquivo);
			
			if(CollectionUtils.isNotEmpty(listIdsNotFound)){
				responseDTO.setFileName(ExportUtils.exportCsv(getReportOutputDir(), "CSV", listIdsNotFound, createHeader()));
			}
			
			return Response.ok(responseDTO).build();
		
		} catch (Exception e) {
			throw new BusinessException("LMS-36193",e);
		}
	}


	@POST
	@Path("atualizacaoManualRodoviario")
	public Response atualizacaoManualRodoviario(){
		
		try {
			atualizarManual(AtualizacaoAutomaticaService.SERVICO_RODOVIARIO);
		} catch (Exception e) {
			throw new BusinessException("erroAtualizacaoManual",e);
		}

		return Response.ok().build();
	}

	@POST
	@Path("atualizacaoManualAereo")
	public Response atualizacaoManualAereo(){
		
		try {
			atualizarManual(AtualizacaoAutomaticaService.SERVICO_AEREO);
		} catch (Exception e) {
			throw new BusinessException("erroAtualizacaoManual",e);
		}

		return Response.ok().build();
	}
	

	private void atualizarManual(Long tipoServico) {
		List<Long> registrosAtualizacaoManual = atualizacaoAutomaticaService.findAtualizacaoManual(tipoServico);

		List<Long> ids = new ArrayList<Long>();
		for (int i = 0; i < registrosAtualizacaoManual.size(); i++) {
			ids.add(registrosAtualizacaoManual.get(i));
			if ( (i + 1) % 1000 == 0) { 
				atualizacaoAutomaticaService.executeAtualizacaoManual(ids);
				ids = new ArrayList<Long>();
			}
		}
		
		if(CollectionUtils.isNotEmpty(ids)){
			atualizacaoAutomaticaService.executeAtualizacaoManual(ids);
		}
	}
	
	private List<Map<String, String>> createHeader(){
		List<Map<String, String>> header = new ArrayList<Map<String,String>>();
		Map<String, String> coluna1 = new HashMap<String, String>();
		coluna1.put("title" ,  "Dados incorretos");
		coluna1.put("column" , "fields");
		header.add(coluna1);
		return header;
	}
	
	
	private File getReportOutputDir() {
		ReportExecutionManager reportExecutionManager = BeanUtils.getBean(ReportExecutionManager.class);
		return reportExecutionManager.getReportOutputDir();
	}

}
