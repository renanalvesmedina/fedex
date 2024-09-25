/**
 * LMSA-7393 - 09/07/2018 - Inicio
 */
package com.mercurio.lms.rest.entrega;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.entrega.model.service.ComprovanteEntregaService;
import com.mercurio.lms.services.entrega.dto.PODScanDTO;
import com.mercurio.lms.services.entrega.dto.PODScanReturnDTO;



@Path("/entrega/podScan")
public class PODScanRest {
	private static final String PARAMETRO_PUDSCAN_VERSAO_CELULAR = "PODSCAN_VERSAO_CELULAR";
	
	@InjectInJersey
	private ComprovanteEntregaService comprovanteEntregaService;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;

	@POST
	@Path("executeSalvarAssinaturaScan")
	public Response executeSalvarAssinaturaScan(PODScanDTO pudScanDTO) {
		String message = "OK";
		try {
			comprovanteEntregaService.generateAssinaturaDigital(pudScanDTO.getBarcode(), pudScanDTO.getMedia());
		}catch( BusinessException e) {
			message ="ERRO " + e.getMessageKey();			
		}catch( InfrastructureException i) {
			message ="ERRO " + i.getMessageKey();	
		}catch( Exception ex) {
			message ="ERRO " + ex.getMessage();	
		}
		
		return Response.ok(new PODScanReturnDTO(message,null)).build();
	}
	
	
	@GET
	@Path("findVersao")	
	public Response findVersao() {		
		String versao = parametroGeralService.findByNomeParametro(PARAMETRO_PUDSCAN_VERSAO_CELULAR).getDsConteudo();		
		return Response.ok(new PODScanReturnDTO(null, versao)).build();
	}
	
}
/**
 * LMSA-7393 - 09/07/2018 - Fim
 */

