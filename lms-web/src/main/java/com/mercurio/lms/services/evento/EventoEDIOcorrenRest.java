package com.mercurio.lms.services.evento;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.edi.EdiOcorrenComplementoDto;
import br.com.tntbrasil.integracao.domains.edi.EdiOcorrenDTO;
import br.com.tntbrasil.integracao.domains.edi.EdiOcorrenNotaDTO;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;

@Path("/evento/doctoservico/ocorren")
public class EventoEDIOcorrenRest {

    @InjectInJersey
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    
    @InjectInJersey
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
    
    @InjectInJersey
    private DadosComplementoService dadosComplementoService;
    
    @POST
    @Path("buscar")
    public Response buscarDadosConemb(EventoDocumentoServicoDMN eventoDMN) {
        List list  = eventoDocumentoServicoService.findDadosEvento(eventoDMN.getIdEventoDocumentoServico());
        EdiOcorrenDTO dto = new EdiOcorrenDTO(); 
        if (list != null && !list.isEmpty()){
            Map<String,Object> dadosEvento = (Map<String,Object>)list.get(0);
            dto.setIdDoctoServico(eventoDMN.getIdDoctoServico());
            dto.setIdEventoDocumentoServico(eventoDMN.getIdEventoDocumentoServico());

            dto.setIdClienteTomador((Long)dadosEvento.get("idClienteTomador"));
            dto.setNrIdentificacaoTomador((String)dadosEvento.get("nrIdentificacaoTomador"));
            dto.setDhOcorrencia((Timestamp)dadosEvento.get("dhEvento"));
            dto.setDsOcorrencia((String)dadosEvento.get("dsOcorrenciaCliente"));
            dto.setCdOcorrencia((Short)dadosEvento.get("cdOcorrenciaCliente"));
            dto.setNrDoctoServico((Long)dadosEvento.get("nrDoctoServico"));
            dto.setSgFilial((String)dadosEvento.get("sgFilial"));
            
            List<NotaFiscalConhecimento> notas = notaFiscalConhecimentoService.findByConhecimento(eventoDMN.getIdDoctoServico());
            
            dto.setNotas(new ArrayList<EdiOcorrenNotaDTO>());
            for(NotaFiscalConhecimento notaFiscalConhecimento: notas){
                EdiOcorrenNotaDTO nota = new EdiOcorrenNotaDTO();
                nota.setNrChave(notaFiscalConhecimento.getNrChave());
                nota.setNrNotaFiscal(notaFiscalConhecimento.getNrNotaFiscal().toString());
                nota.setQtVolumes(Long.valueOf(notaFiscalConhecimento.getQtVolumes()));
                nota.setVlTotal(notaFiscalConhecimento.getVlTotal());
                dto.getNotas().add(nota);
            }
            
            List<DadosComplemento> complementosConhecimento= dadosComplementoService.findByConhecimento(eventoDMN.getIdDoctoServico());
            dto.setComplementos(new ArrayList<EdiOcorrenComplementoDto>());
            for(DadosComplemento complemento: complementosConhecimento){
                String dsCampo = complemento.getInformacaoDoctoCliente().getDsCampo();
                dto.getComplementos().add(new EdiOcorrenComplementoDto(complemento.getDsValorCampo(), dsCampo));
            }
        }
        
        return Response.ok(dto).build();
    }
    
}
