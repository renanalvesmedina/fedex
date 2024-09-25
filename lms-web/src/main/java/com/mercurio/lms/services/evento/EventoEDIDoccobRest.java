package com.mercurio.lms.services.evento;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.hibernate.ObjectNotFoundException;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;

import br.com.tntbrasil.integracao.domains.edi.EdiDoccobDTO;
import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;


@Path("/evento/fatura/doccob") 
public class EventoEDIDoccobRest {
	
    @InjectInJersey
    FaturaService faturaService;
    
    @InjectInJersey
    MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	
    @POST
    @Path("buscar")
    public Response buscarDadosDoccob(FaturaDMN eventoDMN) {
    	
    	EdiDoccobDTO dto = new EdiDoccobDTO();
        Fatura fatura = null;
        try {
        	fatura = faturaService.findById(eventoDMN.getIdFatura());
        	dto.setCnpjTransportador(fatura.getFilialByIdFilial().getPessoa().getNrIdentificacao());
			dto.setIdFatura(fatura.getIdFatura());
			dto.setNrFatura(fatura.getNrFatura());
			dto.setIdFilial(fatura.getFilialByIdFilial().getIdFilial());
			dto.setSgFilial(fatura.getFilialByIdFilial().getSgFilial());
			dto.setDtEmissao(fatura.getDtEmissao().toString("yyyyMMdd"));
			dto.setDtVencimento(fatura.getDtVencimento().toString("yyyyMMdd"));
			dto.setCnpjTomador(fatura.getCliente().getPessoa().getNrIdentificacao());
			dto.setVlTotal(fatura.getVlTotal());
			
			List<String> chavesCte = monitoramentoDocEletronicoService.findNrChaveByIdFatura(fatura.getIdFatura());
			if(chavesCte!= null && !chavesCte.isEmpty()){
				dto.setChavesCte(chavesCte);
			}
			
		} catch (ObjectNotFoundException e) {
			dto.setIdFatura(eventoDMN.getIdFatura());
		}
        
        return Response.ok(dto).build();
    }

}
