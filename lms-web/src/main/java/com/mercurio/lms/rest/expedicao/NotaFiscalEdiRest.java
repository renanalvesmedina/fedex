package com.mercurio.lms.rest.expedicao;

import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.expedicao.GeracaoEtiquetasPostoAvancadoDTO;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.annotation.Public;
import com.mercurio.lms.expedicao.edi.model.service.NotaFiscalEDIService;

@Public
@Path("/expedicao")
public class NotaFiscalEdiRest {
    
    @InjectInJersey NotaFiscalEDIService notaFiscalEDIService;
    
    @POST
    @Path("generateEtiquetasPostoAvancado")
    public Response generateEtiquetasPostoAvancado(GeracaoEtiquetasPostoAvancadoDTO dto){
        
        @SuppressWarnings("unchecked")
        Map etiquetas = notaFiscalEDIService.generateEtiquetasPostoAvancado(
                dto.getIdentificacao(),
                dto.getCnpj(),
                dto.getSerieNotaFiscal(),
                dto.getNrNotaFiscal(),
                dto.getNrVolumes(),
                dto.getPsTotal(),
                dto.getAtualizaVolumesPesos());
        
        
        return Response.ok(etiquetas).build();
    }

}
