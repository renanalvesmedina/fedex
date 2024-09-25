package com.mercurio.lms.services.expedicao;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.edi.enums.CampoNotaFiscalEdiComplementoFedex;
import com.mercurio.lms.edi.model.LogEDI;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiComplemento;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;
import com.mercurio.lms.edi.model.NotaFiscalEdiVolume;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;

import br.com.tntbrasil.integracao.domains.edi.NotfisDMN;
import br.com.tntbrasil.integracao.domains.edi.NotfisDocumentoDMN;
import br.com.tntbrasil.integracao.domains.edi.NotfisRetornoDMN;
import br.com.tntbrasil.integracao.domains.edi.NotfisRetornoDocumentoDMN;

@Path("/expedicao/conhecimento")
public class CteConsolidadoRest {
    
    @InjectInJersey
    PessoaService pessoaService; 
    
    @InjectInJersey
    InscricaoEstadualService inscricaoEstadualService;
    
    @InjectInJersey
    EnderecoPessoaService enderecoPessoaService;
    
    @InjectInJersey
    MunicipioService municipioService;
    
    @InjectInJersey
    UnidadeFederativaService unidadeFederativaService; 
    
    @InjectInJersey
    InformacaoDoctoClienteService informacaoDoctoClienteService; 
    
    @InjectInJersey
    NotaFiscalEDIService notaFiscalEDIService;
    
    
    @InjectInJersey
    ParametroFilialService parametroFilialService;
    
    @InjectInJersey
    ParametroGeralService parametroGeralService; 
    
    @InjectInJersey
    ConfiguracoesFacade configuracoesFacade; 
    

    @POST
    @Path("generateCteConsolidado")
    public Response generateCteConsolidado(NotfisDMN notfisDMN) {

        NotfisRetornoDMN retornoDMN = new NotfisRetornoDMN();

        retornoDMN.setNmArquivo(notfisDMN.getNmArquivo());
        retornoDMN.setDhProcessamento(notfisDMN.getDhProcessamento());
        retornoDMN.setDocumentos(new ArrayList<NotfisRetornoDocumentoDMN>());
        retornoDMN.setStatusProcessamento("OK");
        retornoDMN.setEmailRetornoErro((String)parametroGeralService.findConteudoByNomeParametro("EMAIL_ERROS_EDI_FEDEX", false));

        LogEDI logEDI = null;
        boolean processadoComSucesso = true;

        for (NotfisDocumentoDMN documentoDMN : notfisDMN.getDocumentos()) {

            NotfisRetornoDocumentoDMN retornoDocumentoDMN = new NotfisRetornoDocumentoDMN();
            retornoDocumentoDMN.setChaveNFe(documentoDMN.getChaveNFe());
            retornoDocumentoDMN.setNrCTeFedEx(documentoDMN.getNrCTeFedEx());
            retornoDocumentoDMN.setSerieCTeFedEx(documentoDMN.getSerieCTeFedEx());
            retornoDocumentoDMN.setStatusProcessamento("OK");
            retornoDocumentoDMN.setErrorMessages(new ArrayList<String>());
            retornoDMN.getDocumentos().add(retornoDocumentoDMN);

            if (logEDI == null){
                logEDI = notaFiscalEDIService.storeLogEDISemLayout(documentoDMN.getCnpjRemetente(), notfisDMN.getNmArquivo());
                if (logEDI.getClienteEDIFilialEmbarcadora() == null){
                    retornoDocumentoDMN.getErrorMessages().add(configuracoesFacade.getMensagem("LMS-28017", new String[]{documentoDMN.getCnpjRemetente()}));
                    retornoDocumentoDMN.setStatusProcessamento("ERRO");
                    processadoComSucesso = false;
                    continue;
                }
            }

            try{
                NotaFiscalEdi notaFiscalEdi = new NotaFiscalEdi();
                
                notaFiscalEdi.setChaveNfe(documentoDMN.getChaveNFe());
                notaFiscalEdi.setNrNotaFiscal(Integer.valueOf(documentoDMN.getNrCTeFedEx()));
                notaFiscalEdi.setSerieNf(documentoDMN.getSerieCTeFedEx());
                notaFiscalEdi.setChaveNfe(documentoDMN.getChaveNFe());
                
                notaFiscalEdi.setNomeTomador(documentoDMN.getNmTomador());
                notaFiscalEdi.setCnpjTomador(Long.valueOf(documentoDMN.getCnpjTomador()));
                notaFiscalEdi.setIeTomador(documentoDMN.getInscricaoEstadualTomador());
                notaFiscalEdi.setEnderecoTomador(documentoDMN.getEnderecoTomador());
                notaFiscalEdi.setBairroTomador(documentoDMN.getBairroTomador());
                notaFiscalEdi.setMunicipioTomador(documentoDMN.getMunicipioTomador());
                notaFiscalEdi.setUfTomador(documentoDMN.getUfTomador());
                notaFiscalEdi.setCepEnderTomador(Integer.valueOf(documentoDMN.getCepTomador()));
                
                Pessoa remetente = buscaDadosRemetenteRedespacho(documentoDMN.getCnpjRemetente(), notaFiscalEdi);
                buscaDadosDestinatarioConsignatario(documentoDMN.getCnpjDestinatario(), notaFiscalEdi);
                
                notaFiscalEdi.setCnpjDest(Long.valueOf(documentoDMN.getCnpjDestinatario()));
                
                notaFiscalEdi.setCnpjConsig(Long.valueOf(documentoDMN.getCnpjConsignatario()));
                notaFiscalEdi.setCnpjRedesp(Long.valueOf(documentoDMN.getCnpjRedespacho()));
                
                notaFiscalEdi.setNatureza(documentoDMN.getNaturezaOperacao());
                notaFiscalEdi.setEspecie(documentoDMN.getEspecieDocumento());
                notaFiscalEdi.setTipoFrete(documentoDMN.getTpFrete());
                notaFiscalEdi.setModalFrete(documentoDMN.getModalFrete());
                
                notaFiscalEdi.setQtdeVolumes(new BigDecimal(documentoDMN.getQtdVolumes()));
                
                notaFiscalEdi.setVlrTotalMerc(documentoDMN.getVlTotalMercadoria());
                notaFiscalEdi.setPesoReal(documentoDMN.getPsReal());
                notaFiscalEdi.setPesoCubado(documentoDMN.getPsCubado());
                
                if (documentoDMN.getDtEmissaoCTeFedEx()!=null){
                    notaFiscalEdi.setDataEmissaoNf(documentoDMN.getDtEmissaoCTeFedEx().toLocalDate().toDateTimeAtStartOfDay().toDate());
                }
                
                notaFiscalEdi.setVolumes(new ArrayList<NotaFiscalEdiVolume>());
                
                for (Long nrSeq = 1L; nrSeq <= documentoDMN.getQtdVolumes(); nrSeq++){
                    NotaFiscalEdiVolume volume = new NotaFiscalEdiVolume();
                    String codigoBarras = documentoDMN.getChaveNFe().substring(6, 20);
                    String nrCte = FormatUtils.completaDados(documentoDMN.getNrCTeFedEx(), "0", 9, 0, true);
                    String qtVolumes = FormatUtils.completaDados(documentoDMN.getQtdVolumes(), "0", 5, 0, true);
                    String nrSeqString = FormatUtils.completaDados(nrSeq, "0", 5, 0, true);
                    
                    volume.setCodigoVolume(codigoBarras.concat(nrCte).concat(qtVolumes).concat(nrSeqString));
                    volume.setNotaFiscalEdi(notaFiscalEdi);
                    notaFiscalEdi.getVolumes().add(volume);                    
                }

                notaFiscalEdi.setComplementos(new ArrayList<NotaFiscalEdiComplemento>());
                
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.AVERBACAO_CTE_FEDEX, documentoDMN.getProtocoloAverbacaoCTeFedEx()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.RESPONSAVEL_SEGURO_CTE_FEDEX, documentoDMN.getIdentificacaoRespSeguro()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.CNPJ_SEGURADORA_FEDEX, documentoDMN.getCnpjSeguradora()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.NOME_SEGURADORA_FEDEX, documentoDMN.getNmSeguradora()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.APOLICE_SEGURO_CTE_FEDEX, documentoDMN.getNrApoliceSeguro()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.MANIFESTO_FEDEX, documentoDMN.getNrManifestoFedEx()));

                // INTA-379
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.CNPJ_REMETENTE_CTE_FEDEX, documentoDMN.getCnpjRemetenteCteFedex()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.CNPJ_DESTINATARIO_CTE_FEDEX, documentoDMN.getCnpjDestinatarioCteFedex()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.NATUREZA_PRODUTO_FEDEX, documentoDMN.getNaturezaProdutoCteFedex()));

                // LMSA-6443->LMSA-6451
                if (documentoDMN.getPlacaCarreta() != null && !documentoDMN.getPlacaCarreta().isEmpty()) {
                	notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.PLACA_CARRETA_FEDEX, documentoDMN.getPlacaCarreta()));
                }
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.PLACA_VEICULO_FEDEX, documentoDMN.getPlacaVeiculo()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.CNPJ_MOTORISTA_FEDEX, documentoDMN.getCnpjMotorista()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.NOME_MOTORISTA_FEDEX, documentoDMN.getNomeMotorista()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.FILIAL_FEDEX_DESTINO_VIAGEM, documentoDMN.getFilialDestinoViagem()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.TIPO_DOCUMENTO_FEDEX, documentoDMN.getTipoDocumento()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.FILIAL_ORIGEM_CTE_FEDEX, documentoDMN.getFilialOrigemCte()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.FILIAL_DESTINO_CTE_FEDEX, documentoDMN.getFilialDestinoCTe()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.TIPO_SERVICO_CTE_FEDEX, documentoDMN.getTipoServicoCTe()));
                //
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.NOME_REMETENTE_CTE_FEDEX, documentoDMN.getNomeRemetenteCteFedex()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.NOME_DESTINATARIO_CTE_FEDEX, documentoDMN.getNomeDestinatarioCteFedex()));
                //
                BigDecimal conversaoStringToBigDecimal = null;
                CampoNotaFiscalEdiComplementoFedex nomeCampoComplemento = null; 
                String valorOriginalJSon = null;
                try {
                	nomeCampoComplemento = CampoNotaFiscalEdiComplementoFedex.PESO_AFERIDO_CTE_FEDEX;
                	valorOriginalJSon = documentoDMN.getPesoAferidoCTe();
                	conversaoStringToBigDecimal = new BigDecimal(valorOriginalJSon);
                    notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, nomeCampoComplemento, conversaoStringToBigDecimal.toString() ));
                    
                	nomeCampoComplemento = CampoNotaFiscalEdiComplementoFedex.PESO_AFORADO_CTE_FEDEX;
                	valorOriginalJSon = documentoDMN.getPesoAforadoCTe();
                	conversaoStringToBigDecimal = new BigDecimal(valorOriginalJSon);
                    notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, nomeCampoComplemento, conversaoStringToBigDecimal.toString()));
                    
                	nomeCampoComplemento = CampoNotaFiscalEdiComplementoFedex.PESO_CALCULO_CTE_FEDEX;
                	valorOriginalJSon = documentoDMN.getPesoCalculoCTe();
                	conversaoStringToBigDecimal = new BigDecimal(valorOriginalJSon);
                    notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, nomeCampoComplemento, conversaoStringToBigDecimal.toString()));
                } catch (Exception e) {
                	throw new Exception("Conteúdo do campo ["+nomeCampoComplemento.getNomeCampo()+"] não é válido. Valor recebido via JSon[" + valorOriginalJSon +"]");
                }
                
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.MOEDA_CTE_FEDEX, documentoDMN.getMoedaCTe()));
                notaFiscalEdi.getComplementos().add(buscaComplemento(remetente, CampoNotaFiscalEdiComplementoFedex.CHAVE_MDFE_FEDEX, documentoDMN.getChaveMdfe()));

                notaFiscalEdi.setItens(new ArrayList<NotaFiscalEdiItem>());

                notaFiscalEDIService.store(notaFiscalEdi, notaFiscalEdi.getComplementos(), notaFiscalEdi.getVolumes(), new ArrayList<NotaFiscalEdiItem>());
                notaFiscalEDIService.storeLogEDIDetalhe(logEDI, notaFiscalEdi);

            }catch(Exception e){
                processadoComSucesso = false;
                retornoDMN.setStatusProcessamento("ERRO");
                retornoDocumentoDMN.setStatusProcessamento("ERRO");
                retornoDocumentoDMN.getErrorMessages().add(e.getMessage());
            }
        }
        notaFiscalEDIService.storeLogEDI(logEDI, processadoComSucesso, 0);
        
        return Response.ok(retornoDMN).build();
    }
    
    private NotaFiscalEdiComplemento buscaComplemento(Pessoa remetente, CampoNotaFiscalEdiComplementoFedex dsCampo, String vlCampo) throws Exception {
        NotaFiscalEdiComplemento complemento = new NotaFiscalEdiComplemento();
        
        InformacaoDoctoCliente informacaoDoctoCliente = informacaoDoctoClienteService.findByIdClienteAndDsCampo(remetente.getIdPessoa(), dsCampo.getNomeCampo());
        
        if(informacaoDoctoCliente!=null){
            complemento.setIndcIdInformacaoDoctoClien(informacaoDoctoCliente.getIdInformacaoDoctoCliente());
            complemento.setValorComplemento(vlCampo);
            return complemento;
        }else{
            throw new Exception(configuracoesFacade.getMensagem("LMS-28019", new String[]{dsCampo.getNomeCampo(),remetente.getNrIdentificacao()}));
        }
    }

    private Pessoa buscaDadosDestinatarioConsignatario(String cnpjDestinatario, NotaFiscalEdi notaFiscalEdi) throws Exception {
        notaFiscalEdi.setCnpjDest(Long.valueOf(cnpjDestinatario));
        notaFiscalEdi.setCnpjConsig(Long.valueOf(cnpjDestinatario));
        Pessoa pessoa = pessoaService.findByNrIdentificacao(cnpjDestinatario);
        if (pessoa != null){
            notaFiscalEdi.setNomeDest(pessoa.getNmPessoa());
            notaFiscalEdi.setNomeConsig(pessoa.getNmPessoa());
            InscricaoEstadual inscricaoEstadual = inscricaoEstadualService.findIeByIdPessoaAtivoPadrao(pessoa.getIdPessoa());
            if (inscricaoEstadual != null){
                notaFiscalEdi.setIeDest(inscricaoEstadual.getNrInscricaoEstadual());
                notaFiscalEdi.setIeConsig(inscricaoEstadual.getNrInscricaoEstadual());
            }
            EnderecoPessoa enderecoPessoa = enderecoPessoaService.findByIdPessoa(pessoa.getIdPessoa());
            if (enderecoPessoa != null){
                notaFiscalEdi.setEnderecoDest(enderecoPessoa.getDsEndereco());
                notaFiscalEdi.setEnderecoConsig(enderecoPessoa.getDsEndereco());
                notaFiscalEdi.setBairroDest(enderecoPessoa.getDsBairro());
                notaFiscalEdi.setBairroConsig(enderecoPessoa.getDsBairro());
                notaFiscalEdi.setCepEnderDest(Integer.valueOf(enderecoPessoa.getNrCep()));
                notaFiscalEdi.setCepEnderConsig(Integer.valueOf(enderecoPessoa.getNrCep()));
                Municipio municipio = municipioService.findById(enderecoPessoa.getMunicipio().getIdMunicipio());
                notaFiscalEdi.setMunicipioDest(municipio.getNmMunicipio());
                notaFiscalEdi.setMunicipioConsig(municipio.getNmMunicipio());
                UnidadeFederativa unidadeFederativa = unidadeFederativaService.findById(municipio.getUnidadeFederativa().getIdUnidadeFederativa());
                notaFiscalEdi.setUfDest(unidadeFederativa.getSgUnidadeFederativa());
                notaFiscalEdi.setUfConsig(unidadeFederativa.getSgUnidadeFederativa());
            }
        }else{
            throw new Exception(configuracoesFacade.getMensagem("LMS-28018", new String[]{"destinatário",cnpjDestinatario}));
        }
        return pessoa;
    }

    private Pessoa buscaDadosRemetenteRedespacho(String cnpjRemetente, NotaFiscalEdi  notaFiscalEdi) throws Exception {
        notaFiscalEdi.setCnpjReme(Long.valueOf(cnpjRemetente));
        notaFiscalEdi.setCnpjRedesp(Long.valueOf(cnpjRemetente));
    	Pessoa pessoa = pessoaService.findByNrIdentificacao(cnpjRemetente);
    	
        if (pessoa != null){
            notaFiscalEdi.setNomeReme(pessoa.getNmPessoa());
            notaFiscalEdi.setNomeRedesp(pessoa.getNmPessoa());
            InscricaoEstadual inscricaoEstadual = inscricaoEstadualService.findIeByIdPessoaAtivoPadrao(pessoa.getIdPessoa());
            if (inscricaoEstadual != null){
                notaFiscalEdi.setIeReme(inscricaoEstadual.getNrInscricaoEstadual());
                notaFiscalEdi.setIeRedesp(inscricaoEstadual.getNrInscricaoEstadual());
            }
            EnderecoPessoa enderecoPessoa = enderecoPessoaService.findByIdPessoa(pessoa.getIdPessoa());
            if (enderecoPessoa != null){
                notaFiscalEdi.setEnderecoReme(enderecoPessoa.getDsEndereco()+", "+enderecoPessoa.getNrEndereco());
                notaFiscalEdi.setEnderecoRedesp(enderecoPessoa.getDsEndereco()+", "+enderecoPessoa.getNrEndereco());
                notaFiscalEdi.setBairroReme(enderecoPessoa.getDsBairro());
                notaFiscalEdi.setBairroRedesp(enderecoPessoa.getDsBairro());
                notaFiscalEdi.setCepEnderReme(Integer.valueOf(enderecoPessoa.getNrCep()));
                notaFiscalEdi.setCepEnderRedesp(Integer.valueOf(enderecoPessoa.getNrCep()));
                Municipio municipio = municipioService.findById(enderecoPessoa.getMunicipio().getIdMunicipio());
                notaFiscalEdi.setMunicipioReme(municipio.getNmMunicipio());
                notaFiscalEdi.setMunicipioRedesp(municipio.getNmMunicipio());
                UnidadeFederativa unidadeFederativa = unidadeFederativaService.findById(municipio.getUnidadeFederativa().getIdUnidadeFederativa());
                notaFiscalEdi.setUfReme(unidadeFederativa.getSgUnidadeFederativa());
                notaFiscalEdi.setUfRedesp(unidadeFederativa.getSgUnidadeFederativa());
            }
        }else{
            throw new Exception(configuracoesFacade.getMensagem("LMS-28018", new String[]{"remetente",cnpjRemetente}));
        }
        return pessoa;
    }
}
