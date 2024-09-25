package com.mercurio.lms.services.evento;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.edi.EdiConembDTO;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.municipios.model.service.MunicipioService;


@Path("/evento/doctoservico/conemb") 
public class EventoEDIConembRest {
    
    @InjectInJersey
    DoctoServicoService doctoServicoService;
    
    @InjectInJersey
    ConhecimentoService conhecimentoService;
    
    @InjectInJersey
    DadosComplementoService dadosComplementoService; 
    
    @InjectInJersey
    MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
    
    @InjectInJersey
    MunicipioService municipioService;
    
    
    @InjectInJersey
    PessoaService pessoaService;
    
    @POST
    @Path("buscar")
    public Response buscarDadosConemb(EventoDocumentoServicoDMN eventoDMN) {
       EdiConembDTO dto = conhecimentoService.findDadosEnvioConhecimento(eventoDMN.getIdDoctoServico());
        return Response.ok(dto).build();
    }


}
