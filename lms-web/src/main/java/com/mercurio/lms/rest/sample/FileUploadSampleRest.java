package com.mercurio.lms.rest.sample;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.dto.SampleDto;

/**
 * Classe para exemplificar o uso de upload de arquivos no sistema.
 */
@Path("/fileUploadSample")
public class FileUploadSampleRest extends BaseRest {

	/**
	 * Método de exemplo para upload.
	 * 
	 * @param formDataMultiPart
	 * @return
	 */
	@POST
	@Path("/saveSampleUpload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response saveSampleUpload(FormDataMultiPart formDataMultiPart) {
		/* Popula o modelo/dto/entidade/map/etc */
		SampleDto sampleDto = getModelFromForm(formDataMultiPart, SampleDto.class, "sampleDto");
		
		/* Popula o campo com a quantidade de arquivos. */
		Integer qtdArquivos = getModelFromForm(formDataMultiPart, Integer.class, "qtdArquivos");

		if (qtdArquivos != null) {
			/*
			 * Para cada arquivo que foi submetido teremos um byte[] que é o
			 * padrão atual do sistema para salvar arquivos no banco de dados.
			 * Neste exemplo teremos apenas 1.
			 */
			for (int i = 0; i < qtdArquivos; i++) {
				byte[] byteArrayParaBlob = getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo_" + i);
				sampleDto.setDcArquivo(byteArrayParaBlob);
			}
		}

		/*
		 * Em um caso real seguir com o processo normal, que seria algo tipo:
		 * "MeuService.store(sampleDto)".
		 */

		return Response.ok("Ok").build();
	}
}