package com.mercurio.lms.services.layoutedi;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.edi.model.LogEDI;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIService;
import com.mercurio.lms.edi.util.NotaFiscalEDIDTO2EntityConverter;
import com.mercurio.lms.layoutedi.action.ManterNotaFiscalEdiAction;
import com.mercurio.lms.services.layoutedi.util.DeParaLayouEDI;

import br.com.tntbrasil.integracao.domains.edi.EdiRestResponse;
import br.com.tntbrasil.integracao.domains.edi.NotaFiscalWebServiceDto;
import br.com.tntbrasil.integracao.domains.webservice.edi.Registro;

/**
 * Controller do serviço REST para recebimento de notas fiscais do EDI.
 * 
 * @author MauroM
 *
 */
@Path("/layoutedi/notafiscaledi")
public class NotaFiscalEdiServiceRest {

	private Logger log = LogManager.getLogger(this.getClass());

	@InjectInJersey
	private NotaFiscalEDIService notaFiscalEDIService;
	
	@InjectInJersey
	private ManterNotaFiscalEdiAction manterNotaFiscalEdiAction;

	/**
	 * Serviço REST para recebimento de notas ficais do EDI. Recebe um
	 * {@link NotaFiscalWebServiceDto} no formato JSON, converte para entities
	 * usando a {@link NotaFiscalEDIDTO2EntityConverter} e persiste com o
	 * serviço {@link NotaFiscalEDIService}
	 * 
	 * @param dto
	 * @return Response
	 */
	@POST
	@Path("receberNotasFiscais")
	public Response receberNotasFiscais(List<List<NotaFiscalWebServiceDto>> ldto) {

		return receberNotasFiscais(ldto, false);
	}

	@POST
	@Path("receberNotasFiscaisNatura")
	public Response receberNotasFiscaisNatura(List<List<NotaFiscalWebServiceDto>> ldto) {

		return receberNotasFiscais(ldto, true);
	}

	private Response receberNotasFiscais(List<List<NotaFiscalWebServiceDto>> ldto, boolean blNatura) {
		List<EdiRestResponse> response = new ArrayList<EdiRestResponse>();
		if (!CollectionUtils.isEmpty(ldto) && !CollectionUtils.isEmpty(ldto.get(0))) {
			String nrIdentificacao = ldto.get(0).get(0).getRemetente().getIdentificacao();
			LogEDI logEDI = notaFiscalEDIService.storeLogEDI(nrIdentificacao);
			boolean sucesso = true;
			int qtdePartes = 0;
			for (List<NotaFiscalWebServiceDto> dtos : ldto) {
				try {
					notaFiscalEDIService.storeNotasFiscais(dtos, logEDI, blNatura);
					qtdePartes += dtos.size();
					response.add(new EdiRestResponse(0, "ok"));
				} catch (Exception e) {
					log.error("Erro ao converter Nota Fiscal EDI.", e);
					sucesso = false;
					response.add(new EdiRestResponse(1, e.getMessage()));
				}
			}
			notaFiscalEDIService.storeLogEDI(logEDI, sucesso, qtdePartes);
		}
		return Response.ok(response).build();
	}

	  /**
     * Rest de entrada para os novos serviços do EDI acessados via Apache Cammel
     * 
     * @param ldto
     * @return
     */
    @POST
    @Path("receberNotasFiscaisEDI")
    public Response receberNotasFiscaisEDI(List<NotaFiscalWebServiceDto> dtoList) {
        List<EdiRestResponse> response = new ArrayList<EdiRestResponse>();
        if (!CollectionUtils.isEmpty(dtoList)){
            String nrIdentificacao = dtoList.get(0).getRemetente().getIdentificacao();
            LogEDI logEDI = notaFiscalEDIService.storeLogEDI(nrIdentificacao);
            boolean sucesso = true;
            try {
                notaFiscalEDIService.storeNotasFiscais(dtoList, logEDI);
                response.add(new EdiRestResponse(0, "ok"));
            } catch (Exception e) {
                log.error("Erro ao converter Nota Fiscal EDI.", e);
                sucesso = false;
                response.add(new EdiRestResponse(1, e.getMessage()));
            }

            notaFiscalEDIService.storeLogEDI(logEDI, sucesso, 1);
        }
        return Response.ok(response).build();
    }

	@POST
	@Path("gravarNotaFiscal")
	public Response gravarNotaFiscal(List<Registro> registros) {
		try {
			return  Response.ok(manterNotaFiscalEdiAction.gravaNotaFiscal(DeParaLayouEDI.deParaRegistros(registros))).build();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return Response.serverError().build();
	}

}
