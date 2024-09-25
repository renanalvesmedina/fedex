package com.mercurio.lms.services.expedicao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.vendas.CTeDMN;

import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.tntbrasil.integracao.domains.expedicao.DoctoServicoSaltoDMN;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoDeclaracaoMdfe;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoEnvioAverbacao;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoEnvioEmailAverbacao;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoRetornoAverbacao;
import br.com.tntbrasil.integracao.domains.expedicao.nfeconjugada.transfer.NFEConjugadaTransfer;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoNaturaDMN;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoEletronicoService;
import com.mercurio.lms.expedicao.model.service.NFEConjugadaService;
import com.mercurio.lms.expedicao.model.service.NovoDpeDoctoServicoService;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/expedicao/conhecimento")
public class ConhecimentoServiceRest {

    private static final Logger LOGGER = LogManager.getLogger(ConhecimentoServiceRest.class);
    
    @InjectInJersey
    private ConhecimentoService conhecimentoService;

    @InjectInJersey
    private ManifestoEletronicoService manifestoEletronicoService;

    @InjectInJersey
    private NFEConjugadaService nfeConjugadaService;

    @InjectInJersey
    private DoctoServicoService doctoServicoService;

    @InjectInJersey
    private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;

    @InjectInJersey
    private NovoDpeDoctoServicoService novoDpeDoctoServicoService;
   
    @POST
    @Path("findSaltosCTE")
    public Response findSaltosCTE() {
        return Response.ok(doctoServicoService.executefindSaltosNrCte()).build();
    }

    @POST
    @Path("generateSaltoCTE")
    public Response generateSaltoCTE(DoctoServicoSaltoDMN docto) {
        conhecimentoService.generateInutilizadoSalto(docto);
        return Response.ok().build();
    }

    @POST
    @Path("gerarDadosAverbacaoConhecimento")
    public Response generateDadosAverbacaoConhecimento() throws Exception {
        List<DocumentoEnvioAverbacao> listEnvio = this.conhecimentoService.generateDadosAverbacaoConhecimento();
        return Response.ok(listEnvio).build();
    }

    /**
     * LMSA-7762: Método responsável por gravar o XML de retorno de autorização,
     * cancelamento ou encerramento de MDF-e da comunicação da NDD com a Sefaz.
     * 
     * @param documentoDeclaracaoMdfe Dados do documento lido no SFTP da NDD.
     * @return Uma response vazia com http status 200.
     * 
     * @author leonardo.carmona
     */
    @POST
    @Path("gravarDocumentoMdfe")
    public Response gravarDocumentoMdfe(DocumentoDeclaracaoMdfe documentoDeclaracaoMdfe) {
        ManifestoEletronico manifestoEletronico = manifestoEletronicoService.storeDocumentoMdfe(documentoDeclaracaoMdfe);
        if(manifestoEletronico == null){
        	return Response.status(400).entity(documentoDeclaracaoMdfe).build();
	}

		return Response.ok().build();
    }    

    @POST
    @Path("atualizaRetornoNFEConjugada")
    public Response atualizaRetornoNFEConjugada(NFEConjugadaTransfer retorno) {
        nfeConjugadaService.storeRetornoMonitoramento(retorno);
        return Response.ok().build();
    }

    @POST
    @Path("atualizaEnvioNFEConjugada")
    public Response atualizaEnvioNFEConjugada(NFEConjugadaTransfer retorno) {
	nfeConjugadaService.storeEnvioMonitoramento(retorno);
	return Response.ok().build();
    }

    @POST
    @Path("findXmlDataNFEConjugada")
    public Response findXmlDataNFEConjugada(String nrChave) {
        return Response.ok(nfeConjugadaService.findXMLData(nrChave)).build();
    }

    @POST
    @Path("gravaDadosCancelamentoConhecimentoAverbacao")
    public Response gravaDadosCancelamentoConhecimentoAverbacao(CTeDMN cTeDMN) {
        return Response.ok(monitoramentoDocEletronicoService.storeDadosCancelamentoConhecimentoAverbacao(cTeDMN)).build();
    }
    
    @POST
    @Path("gravarDadosRetornoAverbacaoConhecimento")
    public Response gravaDadosRetornoAverbacaoConhecimento(List<DocumentoRetornoAverbacao> retorno) {
        conhecimentoService.storeDadosRetornoAverbacaoConhecimento(retorno);
        return Response.ok().build();
    }

    @POST
    @Path("generateRecalculoDPE")
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateRecalculoDPE(EventoDocumentoServicoNaturaDMN evento) {
        novoDpeDoctoServicoService.executeAtivaBlAtendido(evento.getIdDoctoServico(), evento.getDhEvento());
        return Response.ok().build();
    }
    
    @POST
    @Path("findAverbacoesSemRetornoParaEnvioEmail")
    public Response findAverbacoesSemRetornoParaEnvioEmail() {
        List<DocumentoEnvioEmailAverbacao> listEnvio = new ArrayList<DocumentoEnvioEmailAverbacao>();
        conhecimentoService.generateAverbacoesSemRetornoParaEnvioEmail(listEnvio);
        
        return Response.ok(listEnvio).build();
    }

    @POST
    @Path("findAverbacoesParaReenvio")
    public Response findAverbacoesParaReenvio() {
        List<DocumentoEnvioAverbacao> listEnvio = new ArrayList<DocumentoEnvioAverbacao>();
        conhecimentoService.generateAverbacoesParaReenvio(listEnvio);

        return Response.ok(listEnvio).build();
    }

}
