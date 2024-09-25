package com.mercurio.lms.services.evento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.avon.EventoDoctoServicoAvonDMN;
import br.com.tntbrasil.integracao.domains.avon.EventoDoctoServicoAvonDMN.EventoDoctoServicoAvonReturnCode;
import br.com.tntbrasil.integracao.domains.expedicao.VolumeDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

@Path("/evento/doctoservico/avon")
public class EventoDoctoServicoAvonRest {
    
    private static final String PARAMETRO_CNPJ = "CNPJ_AVON_WEBSERVICE";

    @InjectInJersey
    ParametroGeralService parametroGeralService;
    
    @InjectInJersey
    DoctoServicoService doctoServicoService;
    
    @InjectInJersey
    ConhecimentoService conhecimentoService;
    
    @InjectInJersey
    private VolumeNotaFiscalService volumeNotaFiscalService;  
    
    @InjectInJersey
    private PessoaService pessoaService;
    
    @InjectInJersey
    private DadosComplementoService dadosComplementoService; 
    
    @InjectInJersey
    private ConfiguracoesFacade configuracoesFacade; 
    
    @POST
    @Path("buscar")
    public Response buscarDadosDocumento(EventoDoctoServicoAvonDMN evento){
        DoctoServico doctoServico = doctoServicoService.findByIdWithRemetenteDestinatario(evento.getIdDoctoServico());
        Conhecimento  conhecimento = conhecimentoService.findById(doctoServico.getIdDoctoServico());

        
        try{
            if (!validateCnpjAvon(doctoServico, conhecimento.getTpConhecimento().getValue())){
                return generateReturn(evento, EventoDoctoServicoAvonReturnCode.IGNORE);
            }
        }catch(BusinessException be){
            evento.setReturnCode(EventoDoctoServicoAvonReturnCode.ERROR.name()); 
            evento.setReturnMessage(configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments()));
            return Response.ok(evento).build();
        }
        
        evento.setBarcodes(findBarcodes(evento.getIdDoctoServico()));
        
        evento.setTpDoctoServico(conhecimento.getTpConhecimento().getValue());
        evento.setSgFilialOrigem(conhecimento.getFilialByIdFilialOrigem().getSgFilial());
        evento.setNrDoctoServico(doctoServico.getNrDoctoServico());
        
        List<DadosComplemento> complementos = dadosComplementoService.findByConhecimento(evento.getIdDoctoServico());
        
        Map<String,String> complementosMap = new HashMap<String, String>();
        if (complementos != null && !complementos.isEmpty()){
            for(DadosComplemento complemento: complementos){
                complementosMap.put(complemento.getInformacaoDoctoCliente().getDsCampo(), complemento.getDsValorCampo());
            }
        }
        evento.setComplementos(complementosMap);
            
        
        return generateReturn(evento, EventoDoctoServicoAvonReturnCode.OK); 
    }
    
    private List<VolumeDMN> findBarcodes(Long idDoctoServico) {
        List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findByIdConhecimento(idDoctoServico);
        
        List<VolumeDMN> barcodes = new ArrayList<VolumeDMN>();
        
        for (VolumeNotaFiscal volumeNotaFiscal : volumes) {
            VolumeDMN volumeDMN = new VolumeDMN();
            volumeDMN.setBarcode(volumeNotaFiscal.getNrVolumeEmbarque());
            barcodes.add(volumeDMN);
        }
        return barcodes;
    }


    private boolean validateCnpjAvon(DoctoServico doctoServico, String tpConhecimento){
        Pessoa pessoa = null;
        if(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO.equals(tpConhecimento)){
            pessoa = pessoaService.findById(doctoServico.getClienteByIdClienteDestinatario().getIdCliente());
        }else{
            pessoa = pessoaService.findById(doctoServico.getClienteByIdClienteRemetente().getIdCliente());
        }
        
        String cnpjRemetente = pessoa.getNrIdentificacao();
        String parametroCnpj = (String) parametroGeralService.findConteudoByNomeParametro(PARAMETRO_CNPJ, false);
        for (String cnpjWebservice: parametroCnpj.split(";")){
            if (cnpjRemetente.startsWith(cnpjWebservice)){
                return true;
            }
        }
        return false;
    }
    
    private Response generateReturn(EventoDoctoServicoAvonDMN evento,EventoDoctoServicoAvonReturnCode returnCode){
        evento.setReturnCode(returnCode.name());
        evento.setReturnMessage(returnCode.getReturnMessage());
        return Response.ok(evento).build();
    }
}
