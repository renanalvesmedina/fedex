package com.mercurio.lms.rest.vendas;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.service.ImportarTRTService;

@Path("/vendas/importartrt")
public class ImportarTRTRest extends BaseRest {

	@InjectInJersey ImportarTRTService importarTRTService;
	
	@POST
	@Path("importar")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public List<String> importar(FormDataMultiPart formDataMultiPart) throws IOException{
		String arquivo = getCharacterLobUserTypeFromForm(formDataMultiPart, "arquivo");

		String vigenciaInicialStr = getModelFromForm(formDataMultiPart,	String.class, "vigenciaInicial");
		YearMonthDay vigenciaInicial = JTDateTimeUtils.formatStringToYearMonthDay(vigenciaInicialStr);

		try {
			return importarTRTService.executeImportar(arquivo, vigenciaInicial);
		} 
		catch (BusinessException e) {
				throw e;
		}
		catch (Exception e) {
			throw new BusinessException("LMS-36193",e);
		}
	}

}
