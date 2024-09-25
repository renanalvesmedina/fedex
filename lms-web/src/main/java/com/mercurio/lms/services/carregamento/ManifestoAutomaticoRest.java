package com.mercurio.lms.services.carregamento;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.MotoristaControleCarga;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.VeiculoControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.util.JTDateTimeUtils;

import br.com.tntbrasil.integracao.domains.fedex.RetornoManifestoAutomaticoDTO;
import br.com.tntbrasil.integracao.domains.fedex.RomaneioEntregaDocumentoFedexDTO;
import br.com.tntbrasil.integracao.domains.fedex.RomaneioEntregaFedexDTO;

@Path("/carregamento/manifestoAutomatico") 
public class ManifestoAutomaticoRest extends BaseRest{

    Logger log = LogManager.getLogger();
    
    @InjectInJersey
    FilialService filialService;
    
    @InjectInJersey
    ConteudoParametroFilialService conteudoParametroFilialService; 
    
    @InjectInJersey
    ParametroGeralService parametroGeralService; 
    
    @InjectInJersey
    MotoristaService motoristaService;
    
    @InjectInJersey
    MeioTransporteService meioTransporteService;
    
    @InjectInJersey
    MeioTransporteRodoviarioService meioTransporteRodoviarioService;
    
    @InjectInJersey
    MeioTranspProprietarioService meioTranspProprietarioService;
    
    @InjectInJersey
    ControleCargaService controleCargaService; 
    
    @InjectInJersey
    RotaColetaEntregaService rotaColetaEntregaService; 
    
    @InjectInJersey
    ConhecimentoService conhecimentoService; 
    
    @InjectInJersey
    ConfiguracoesFacade  configuracoesFacade;
    
    @InjectInJersey
    VolumeNotaFiscalService volumeNotaFiscalService; 
    
    @InjectInJersey
    PessoaService pessoaService; 
    
    private static final String TIPO_DOCT_CTE = "57";
    
    private static Short[] CD_LOCALIZACOES_VALIDAS = new Short[]{
        Short.valueOf("24"), 
        Short.valueOf("35"), 
        Short.valueOf("43"), 
        Short.valueOf("33"), 
        Short.valueOf("34") 
    };

    @POST
    @Path("findFilialByNomeParametro")
    public Response findFilialByNomeParametro() {
        List<Long> idsFiliais = new ArrayList<Long>();
        List<Filial> filiais = conteudoParametroFilialService.findFilialByNmParametro("FECHAR_MANIFESTO_AUT");
        if(!CollectionUtils.isEmpty(filiais)) {
            for (Filial filial : filiais) {
                idsFiliais.add(filial.getIdFilial());
            }
        }
        return Response.ok(idsFiliais).build();
    }

    @POST
    @Path("findControleCarga")
    public Response findControleCarga(Long idFilial){
        List<Long>  listIdControleCarga = controleCargaService
                            .findIdControleCargaFechaManifestoAuto(idFilial);
        return Response.ok(listIdControleCarga).build();

    }

    @GET
	@Path("finalizaManifesto")
    public Response executeFinalizaManifesto(@QueryParam("idControleCarga") Long idControleCarga){
        Boolean retorno;
        try {
            retorno = controleCargaService.executeFinalizaManifesto(idControleCarga);
        } catch (Exception e ) {

            log.error(e.getCause().getMessage());
            retorno = Boolean.FALSE;
        }
		return Response.ok(retorno).build();
	}
    
    @POST
    @Path("gerarManifestoFedex")    
    @SuppressWarnings("unchecked")
    public Response gerarManifestoFedex(RomaneioEntregaFedexDTO romaneioEntrega){
        RetornoManifestoAutomaticoDTO retornoDto = new RetornoManifestoAutomaticoDTO();
        try{
        
            Moeda real = new Moeda();
            real.setIdMoeda(1l);
            
            Usuario usuarioIntegracao = new Usuario();
            usuarioIntegracao.setIdUsuario(5000l);
            
            retornoDto.setFileName(romaneioEntrega.getFileName());
            
            Filial filialFedex = conteudoParametroFilialService.findFilialByConteudoParametro("CD_FILIAL_FEDEX", romaneioEntrega.getFilialDestino());
            if (filialFedex == null){
                retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem("LMS-09147", new Object[]{romaneioEntrega.getFilialDestino()}));
                return Response.ok(retornoDto).build();
            }else{
                retornoDto.setSgFilial(filialFedex.getSgFilial());
            }
    
            retornoDto.setNrRomaneio(romaneioEntrega.getNumeroRomaneio());
            retornoDto.setDestinatariosEmail(findDestinatariosEmailRetorno(filialFedex));
            
            if (!validaFilialHabilitada(filialFedex)){
                retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem("LMS-09148", new Object[]{filialFedex.getSgFilial()}));
                return Response.ok(retornoDto).build();
            }
    
            String cpfMotoristaFedex = null;
            String placaMeioTransporteFedex = null;
            try{
                cpfMotoristaFedex = (String)conteudoParametroFilialService.findConteudoByNomeParametroWithException(filialFedex.getIdFilial(),
                        "MOTORISTA_FILIAL_FDX", false);
                placaMeioTransporteFedex= (String)conteudoParametroFilialService.findConteudoByNomeParametroWithException(filialFedex.getIdFilial(),
                        "PLACA_VEICULO_FDX", false);
            }catch(BusinessException be){
                retornoDto.getMessageErrors().add(be.getMessageKey()+": " + configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments()));
                return Response.ok(retornoDto).build();
            }
            Motorista motoristaFedex = null;
            Pessoa pessoaMotorista = pessoaService.findByNrIdentificacao(cpfMotoristaFedex);
            if (pessoaMotorista != null){
                motoristaFedex = motoristaService.findById(pessoaMotorista.getIdPessoa());
            }
            if (motoristaFedex == null){
                retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem("LMS-09149", new Object[]{cpfMotoristaFedex, filialFedex.getSgFilial()}));
                return Response.ok(retornoDto).build();
            }
            
            MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByIdentificacao(placaMeioTransporteFedex);
            if (meioTransporte == null){
                retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem("LMS-09150", new Object[]{placaMeioTransporteFedex, filialFedex.getSgFilial()}));
                return Response.ok(retornoDto).build();
            }
            
            MeioTransporteRodoviario meioTransporteRodoviario = meioTransporteRodoviarioService.findById(meioTransporte.getIdMeioTransporte());
            Proprietario proprietarioFedex = meioTranspProprietarioService.findProprietarioByIdMeioTransporte(meioTransporte.getIdMeioTransporte(), JTDateTimeUtils.getDataAtual());
            if (proprietarioFedex == null){
                retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem("LMS-09151", new Object[]{placaMeioTransporteFedex}));
                return Response.ok(retornoDto).build();
            }
            
            RotaColetaEntrega rota = rotaColetaEntregaService.findRotaColetaEntrega(filialFedex.getIdFilial(), Short.valueOf(romaneioEntrega.getNrRotaEntrega()));
            if (rota == null){
                retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem("LMS-09152", new Object[]{romaneioEntrega.getNrRotaEntrega()}));
                return Response.ok(retornoDto).build();
            }
            
            String cdFilialFedexRomaneio = romaneioEntrega.getFilialDestino();
            
            List<Conhecimento> doctosRomaneio = findDoctosRomaneio(romaneioEntrega, retornoDto,filialFedex, cdFilialFedexRomaneio);
            if (doctosRomaneio.isEmpty()){
                return Response.ok(retornoDto).build();
            }
            
            ControleCarga cc = buildControleCArga(romaneioEntrega, real, filialFedex, meioTransporte, proprietarioFedex,
                    motoristaFedex, rota);
            
            VeiculoControleCarga veiculoControleCarga = new VeiculoControleCarga();
            veiculoControleCarga.setControleCarga(cc);
            veiculoControleCarga.setMeioTransporte(meioTransporte);
            
            MotoristaControleCarga motoristaControleCarga = new MotoristaControleCarga();
            motoristaControleCarga.setMotorista(motoristaFedex);
            motoristaControleCarga.setControleCarga(cc);
            
            ControleQuilometragem quilometragem = new ControleQuilometragem();
            quilometragem.setFilial(filialFedex);
            quilometragem.setMeioTransporteRodoviario(meioTransporteRodoviario);
            
            DateTime dhLiberacaoRomaneio = getDateTime(romaneioEntrega.getDataLiberacaRomaneio(), romaneioEntrega.getHoraLiberacaRomaneio(), filialFedex);
            quilometragem.setDhMedicao(dhLiberacaoRomaneio);
            quilometragem.setNrQuilometragem(0);
            quilometragem.setBlVirouHodometro(Boolean.FALSE);
            quilometragem.setBlSaida(Boolean.TRUE);
            quilometragem.setUsuarioByIdUsuario(usuarioIntegracao);
            quilometragem.setControleCarga(cc);
    
            Manifesto manifesto = buildManifesto(romaneioEntrega, real, filialFedex);
            totalizaAtributosDocumentosManifesto(manifesto, doctosRomaneio);
            
            manifesto.setControleCarga(cc);
            manifesto.setPreManifestoDocumentos(new ArrayList<PreManifestoDocumento>());
            manifesto.setPreManifestoVolumes(new ArrayList<PreManifestoVolume>());
    
            ManifestoEntrega manifestoEntrega = new ManifestoEntrega();
            manifestoEntrega.setManifesto(manifesto);
            manifestoEntrega.setFilial(filialFedex);
            manifestoEntrega.setDhEmissao(dhLiberacaoRomaneio);
            manifestoEntrega.setObManifestoEntrega(configuracoesFacade.getMensagem("observacaoManifestoAutomatico"));
            manifestoEntrega.setManifestoEntregaDocumentos(new ArrayList<ManifestoEntregaDocumento>());
            manifestoEntrega.setManifestoEntregaVolumes(new ArrayList<ManifestoEntregaVolume>());
            manifesto.setManifestoEntrega(manifestoEntrega);
            
            Integer nrOrdem = 1;
            for (Conhecimento conhecimento:doctosRomaneio){
                ManifestoEntregaDocumento manifestoEntregaDocumento = new ManifestoEntregaDocumento();
                manifestoEntregaDocumento.setManifestoEntrega(manifestoEntrega);
                manifestoEntregaDocumento.setDoctoServico(conhecimento);
                manifestoEntregaDocumento.setTpSituacaoDocumento(new DomainValue("PBAI"));
                manifestoEntregaDocumento.setUsuario(usuarioIntegracao);
                manifestoEntregaDocumento.setDhInclusao(dhLiberacaoRomaneio);
                manifestoEntregaDocumento.setBlRetencaoComprovanteEnt(Boolean.FALSE);
                manifestoEntrega.getManifestoEntregaDocumentos().add(manifestoEntregaDocumento);
                
                PreManifestoDocumento preManifestoDocumento = new PreManifestoDocumento();
                preManifestoDocumento.setManifesto(manifesto);
                preManifestoDocumento.setDoctoServico(conhecimento);
                preManifestoDocumento.setNrOrdem(nrOrdem++);
                preManifestoDocumento.setVersao(0);
                manifesto.getPreManifestoDocumentos().add(preManifestoDocumento);
                
                List<VolumeNotaFiscal> volumesConhecimento = volumeNotaFiscalService.findfindByIdConhecimento(conhecimento.getIdDoctoServico());
                for (VolumeNotaFiscal volumeNotaFiscal: volumesConhecimento){
                    ManifestoEntregaVolume manifestoEntregaVolume = new ManifestoEntregaVolume();
                    manifestoEntregaVolume.setManifestoEntrega(manifestoEntrega);
                    manifestoEntregaVolume.setDoctoServico(conhecimento);
                    manifestoEntregaVolume.setVolumeNotaFiscal(volumeNotaFiscal);
                    manifestoEntregaVolume.setManifestoEntregaDocumento(manifestoEntregaDocumento);   
                    manifestoEntrega.getManifestoEntregaVolumes().add(manifestoEntregaVolume);
                    PreManifestoVolume preManifestoVolume = new PreManifestoVolume();
                    preManifestoVolume.setManifesto(manifesto);
                    preManifestoVolume.setDoctoServico(conhecimento);
                    preManifestoVolume.setPreManifestoDocumento(preManifestoDocumento);
                    preManifestoVolume.setVolumeNotaFiscal(volumeNotaFiscal);
                    preManifestoVolume.setTpScan(new DomainValue("LM"));
                    preManifestoVolume.setVersao(0);
                    manifesto.getPreManifestoVolumes().add(preManifestoVolume);
                }
            }
            
            cc.setManifestos(new ArrayList<Manifesto>());
            cc.getManifestos().add(manifesto);

            controleCargaService.generateControleCargaManifestoAutomatico(cc,veiculoControleCarga,motoristaControleCarga,quilometragem, usuarioIntegracao);
        }catch(BusinessException be){
            retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem(be.getMessageKey(), be.getMessageArguments()));
        }catch(Exception e){
            retornoDto.setDestinatariosEmail(null);
            retornoDto.getMessageErrors().add(e.getMessage());
        }
                
        return Response.ok(retornoDto).build();
    }

    private String findDestinatariosEmailRetorno(Filial filial) {
        String conteudo = (String)conteudoParametroFilialService.findConteudoByNomeParametro(filial.getIdFilial(),
                "DEST_EMAIL_ERROS_FDX", false); 
        return conteudo;
    }

    private Manifesto buildManifesto(RomaneioEntregaFedexDTO romaneioEntrega, Moeda real, Filial filialFedex) {
        Manifesto manifesto = new Manifesto();
        DateTime dataLiberacaoRomaneio = getDateTime(romaneioEntrega.getDataLiberacaRomaneio(), romaneioEntrega.getHoraLiberacaRomaneio(), filialFedex);
        
        manifesto.setFilialByIdFilialOrigem(filialFedex);
        manifesto.setFilialByIdFilialDestino(filialFedex);
        manifesto.setDhGeracaoPreManifesto(dataLiberacaoRomaneio);
        manifesto.setTpManifesto(new DomainValue("E"));
        manifesto.setMoeda(real);
        manifesto.setDhEmissaoManifesto(dataLiberacaoRomaneio);
        manifesto.setTpModal(new DomainValue("R"));
        manifesto.setTpManifestoEntrega(new DomainValue("EP"));
        manifesto.setTpAbrangencia(new DomainValue("N"));
        manifesto.setTpStatusManifesto(new DomainValue("TC"));
        manifesto.setVersao(8);
        manifesto.setBlBloqueado(Boolean.FALSE);
        return manifesto;
    }

    private void totalizaAtributosDocumentosManifesto(Manifesto manifesto, List<Conhecimento> doctosRomaneio) {
        BigDecimal vlTotalManifesto = BigDecimal.ZERO;
        BigDecimal psTotalManifesto = BigDecimal.ZERO;
        BigDecimal psTotalAforadoManifesto = BigDecimal.ZERO;
        BigDecimal vlTotalManifestoEmissao = BigDecimal.ZERO;
        BigDecimal psTotalManifestoEmissao = BigDecimal.ZERO;
        BigDecimal vlTotalFreteEmissao = BigDecimal.ZERO;
        Integer qtTotalVolumesEmissao = Integer.valueOf(0);
        
        for (Conhecimento conhecimento: doctosRomaneio){
            vlTotalManifesto = vlTotalManifesto.add(conhecimento.getVlMercadoria());
            psTotalManifesto = psTotalManifesto.add(conhecimento.getPsReal());
            psTotalAforadoManifesto = psTotalAforadoManifesto.add(conhecimento.getPsAforado());
            vlTotalManifestoEmissao = vlTotalManifestoEmissao.add(conhecimento.getVlMercadoria());
            psTotalManifestoEmissao = psTotalManifestoEmissao.add(conhecimento.getPsReal());
            vlTotalFreteEmissao = vlTotalFreteEmissao.add(conhecimento.getVlTotalDocServico());
            qtTotalVolumesEmissao += conhecimento.getQtVolumes();
        }
        
        manifesto.setVlTotalManifesto(vlTotalManifesto);
        manifesto.setPsTotalManifesto(psTotalManifesto);
        manifesto.setPsTotalAforadoManifesto(psTotalAforadoManifesto);
        manifesto.setVlTotalManifestoEmissao(vlTotalManifestoEmissao);
        manifesto.setPsTotalManifestoEmissao(psTotalManifestoEmissao);
        manifesto.setVlTotalFreteEmissao(vlTotalFreteEmissao);
        manifesto.setQtTotalVolumesEmissao(qtTotalVolumesEmissao);
        
    }

    private List<Conhecimento> findDoctosRomaneio(RomaneioEntregaFedexDTO romaneioEntrega, RetornoManifestoAutomaticoDTO retornoDto, Filial filialFedex, String cdFilialFedexRomaneio) {
        List<Conhecimento> doctos = new ArrayList<Conhecimento>();
        for (RomaneioEntregaDocumentoFedexDTO doctoDTO: romaneioEntrega.getDocumentos()){
        	String tpDocumento = doctoDTO.getChaveDocumento().substring(20, 22);
        	Conhecimento conhecimento = null;
        	if (TIPO_DOCT_CTE.equals(tpDocumento)){
        		conhecimento = conhecimentoService.findByNrChaveDocEletronico(doctoDTO.getChaveDocumento());
        	}else{
        		conhecimento = conhecimentoService.findByNrChaveRpsFedex(doctoDTO.getChaveDocumento());
        	}

            if (conhecimento != null){
                
                conhecimento = conhecimentoService.executeTratamentoManifestoAutomatico(conhecimento, filialFedex, cdFilialFedexRomaneio);
                
                //Refaz a busca para atualizar a instancia com as novas informa??es geradas pelo tratamento. 
                if (TIPO_DOCT_CTE.equals(tpDocumento)){
            		conhecimento = conhecimentoService.findByNrChaveDocEletronico(doctoDTO.getChaveDocumento());
            	}else{
            		conhecimento = conhecimentoService.findByNrChaveRpsFedex(doctoDTO.getChaveDocumento());
            	}
                LocalizacaoMercadoria localizacaoMercadoria = conhecimento.getLocalizacaoMercadoria();
                Filial filialLocalizacao = filialService.findById(conhecimento.getFilialLocalizacao().getIdFilial());
                boolean filialValida = conhecimento.getFilialLocalizacao().getIdFilial().equals(filialFedex.getIdFilial());
                if (Arrays.binarySearch(CD_LOCALIZACOES_VALIDAS, localizacaoMercadoria.getCdLocalizacaoMercadoria()) >= 0 && filialValida && !conhecimento.getBlBloqueado()){
                    doctos.add(conhecimento);
                }else{
                    Filial filial = filialService.findById(conhecimento.getFilialOrigem().getIdFilial());
                    if (conhecimento.getBlBloqueado()){
                        retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem("LMS-09155", new Object[]{filial.getSgFilial(),conhecimento.getNrConhecimento().toString()}));
                    }else{
                        retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem("LMS-09153", new Object[]{filial.getSgFilial(),conhecimento.getNrConhecimento().toString(), localizacaoMercadoria.getDsLocalizacaoMercadoria(), filialLocalizacao.getSgFilial()}));
                    }
                }
            }else{
                retornoDto.getMessageErrors().add(configuracoesFacade.getMensagem("LMS-09154", new Object[]{doctoDTO.getChaveDocumento()}));
            }
        }
        return doctos;
    }

    private ControleCarga buildControleCArga(RomaneioEntregaFedexDTO romaneioEntrega, Moeda real, Filial filialFedex,
            MeioTransporte meioTransporte, Proprietario proprietarioFedex, Motorista motoristaFedex, RotaColetaEntrega rota) {
        
        DateTime dataLiberacaoRomaneio = getDateTime(romaneioEntrega.getDataLiberacaRomaneio(), romaneioEntrega.getHoraLiberacaRomaneio(), filialFedex);
        
        ControleCarga cc = new ControleCarga();
        cc.setIdControleCarga(null);
        cc.setFilialByIdFilialOrigem(filialFedex);
        cc.setFilialByIdFilialAtualizaStatus(filialFedex);
        cc.setProprietario(proprietarioFedex);
        cc.setMotorista(motoristaFedex);
        cc.setTpControleCarga(new DomainValue("C"));
        cc.setTpStatusControleCarga(new DomainValue("TC"));
        cc.setFilialByIdFilialDestino(filialFedex);
        cc.setMoeda(real);
        cc.setDhSaidaColetaEntrega(dataLiberacaoRomaneio);
        cc.setDhGeracao(dataLiberacaoRomaneio);
        cc.setMeioTransporteByIdTransportado(meioTransporte);
        cc.setBlEntregaDireta(Boolean.FALSE);
        cc.setRotaColetaEntrega(rota);
        return cc;
    }

    private DateTime getDateTime(String data, String hora, Filial filial){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMddHHmmss");
        DateTime dateTime = formatter.parseDateTime(data+hora); 
        return dateTime.withZone(filial.getDateTimeZone());
    }
    
private boolean validaFilialHabilitada(Filial filial){
    Object conteudo = conteudoParametroFilialService.findConteudoByNomeParametro(filial.getIdFilial(),
            "MAN_AUTOMATICO_FDX", false);
    return conteudo != null;
}


}
