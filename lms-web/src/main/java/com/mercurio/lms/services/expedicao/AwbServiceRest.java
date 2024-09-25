package com.mercurio.lms.services.expedicao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import br.com.tntbrasil.integracao.domains.expedicao.AwbCiaAerea;
import br.com.tntbrasil.integracao.domains.expedicao.BaseAwbCiaAerea;
import br.com.tntbrasil.integracao.domains.expedicao.CancelAwbCiaAerea;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.service.AwbService;
 
@Path("/expedicao/awbServiceRest") 
public class AwbServiceRest extends BaseRest{
	
	@InjectInJersey AwbService service;
	
	@POST
	@Path("storeAwb")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response storeAwb(FormDataMultiPart formDataMultiPart) {
		AwbCiaAerea dto = getModelFromForm(formDataMultiPart, AwbCiaAerea.class, "dados");
		setAttachments(formDataMultiPart, dto);
		service.storeAwb(dto);
		return Response.ok().build();
	}
	
	@POST
	@Path("cancelaAwb")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response cancelaAwb(FormDataMultiPart formDataMultiPart) {
		BaseAwbCiaAerea dto = getModelFromForm(formDataMultiPart, CancelAwbCiaAerea.class, "dados");
		setAttachments(formDataMultiPart, dto);		
		service.storeCancelaAwb( (CancelAwbCiaAerea) dto);			
		return Response.ok().build();
	}

	private void setAttachments(FormDataMultiPart formDataMultiPart, BaseAwbCiaAerea baseAwbCiaAerea) {
		Integer qtdArquivos = getModelFromForm(formDataMultiPart, Integer.class, "qtdArquivos");

		if (qtdArquivos != null) {
			List<byte[]> listaAnexos = new ArrayList<byte[]>();
			for (int i = 0; i < qtdArquivos; i++) {	
				byte[] byteArrayParaBlob = getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo_" + i);
				listaAnexos.add(byteArrayParaBlob);
			}
			
			baseAwbCiaAerea.setListaAnexos(listaAnexos);
		}			
	}
		
} 
