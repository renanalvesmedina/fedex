package com.mercurio.lms.services.evento;

import br.com.tntbrasil.integracao.domains.sim.EventoDoctoServicoBlackDeckerDMN;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.lms.EventoDocumentoServicoUtil;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.services.evento.dto.bean.EventoDocumentoServicoDTO;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;

@Path("evento/doctoservico/integracaoblackdecker")
public class EventoDoctoServicoBlackDeckerRest {
    @InjectInJersey
    private EventoDocumentoServicoService eventoDocumentoServicoService;

    @POST
    @Path("enriqueceOcorrenciaEntrega")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/json;charset=utf-8")
    public Response enriqueceOcorrenciaEntrega(@Valid EventoDocumentoServicoDTO eventoDocumentoServicoDTO) throws InvocationTargetException, IllegalAccessException {

        EventoDocumentoServicoDMN eventoDocumentoServicoDMN = new EventoDocumentoServicoDMN();
        BeanUtils.copyProperties(eventoDocumentoServicoDMN,eventoDocumentoServicoDTO);

        boolean carregarIdEventoDocumentoServicoValidar = new EventoDocumentoServicoUtil().carregarIdEventoDocumentoServicoValidar
                        (eventoDocumentoServicoDMN, eventoDocumentoServicoService);
        if (carregarIdEventoDocumentoServicoValidar) {
            return Response.ok().build();
        }

        EventoDoctoServicoBlackDeckerDMN eventoDoctoServicoBlackDeckerDMN = eventoDocumentoServicoService.findNotasEventoBlackDecker
                (eventoDocumentoServicoDMN.getIdEventoDocumentoServico(), eventoDocumentoServicoDMN.getIdDoctoServico());

        return Response.ok(eventoDoctoServicoBlackDeckerDMN).build();
    }
}

