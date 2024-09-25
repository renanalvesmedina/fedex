package com.mercurio.lms;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;

public class EventoDocumentoServicoUtil {

    public boolean carregarIdEventoDocumentoServicoValidar
        (
            EventoDocumentoServicoDMN eventoDocumentoServicoDMN,
            EventoDocumentoServicoService eventoDocumentoServicoService
    ) {

        eventoDocumentoServicoService.findIdEventoDocumentoServico(eventoDocumentoServicoDMN);

        String tpDoctoServico = eventoDocumentoServicoDMN.getTpDoctoServico();

        return eventoDocumentoServicoDMN.getIdEventoDocumentoServico() == null ||
                !("NTE".equals(tpDoctoServico) || "CTE".equals(tpDoctoServico));
    }
}
